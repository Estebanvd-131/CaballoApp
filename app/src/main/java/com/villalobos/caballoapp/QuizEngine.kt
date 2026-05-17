package com.villalobos.caballoapp

import android.content.Context
import android.os.Handler
import android.os.Looper

class QuizEngine(private val context: Context) {

    data class QuizSession(
        val questions: List<QuizQuestion>,
        val startTime: Long = System.currentTimeMillis(),
        var currentQuestionIndex: Int = 0,
        val answers: MutableList<Int> = mutableListOf(),
        val times: MutableList<Long> = mutableListOf(), // Time taken per question
        var isCompleted: Boolean = false
    )

    data class QuizResult(
        val score: Int, // Percentage 0-100
        val correctAnswers: Int,
        val totalQuestions: Int,
        val timeSpent: Long, // Total time in milliseconds
        val averageTimePerQuestion: Long,
        val regionId: Int?
    )

    private var currentSession: QuizSession? = null
    private var questionStartTime: Long = 0

    // Callbacks
    var onQuestionChanged: ((QuizQuestion, Int, Int) -> Unit)? = null
    var onQuizCompleted: ((QuizResult) -> Unit)? = null
    var onTimeUp: (() -> Unit)? = null

    // Timer for time limits (optional)
    private var timerHandler: Handler? = null
    private var timeLimitMs: Long = 0

    fun startQuiz(regionId: Int? = null, questionCount: Int = 5): Boolean {
        return try {
            val questions = QuizData.getQuizQuestions(regionId, questionCount)
            if (questions.isEmpty()) return false

            currentSession = QuizSession(questions)
            questionStartTime = System.currentTimeMillis()

            // Notify first question
            notifyQuestionChanged()

            true
        } catch (e: Exception) {
            ErrorHandler.handleError(
                context = context,
                throwable = e,
                errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
                userMessage = "Error al iniciar quiz",
                canRecover = false
            )
            false
        }
    }

    fun answerQuestion(selectedAnswer: Int): Boolean {
        val session = currentSession ?: return false

        // Record time taken for this question
        val timeTaken = System.currentTimeMillis() - questionStartTime
        session.times.add(timeTaken)
        session.answers.add(selectedAnswer)

        // Check if quiz is completed
        if (session.currentQuestionIndex >= session.questions.size - 1) {
            completeQuiz()
            return true
        }

        // Move to next question
        session.currentQuestionIndex++
        questionStartTime = System.currentTimeMillis()

        notifyQuestionChanged()
        return true
    }

    fun skipQuestion(): Boolean {
        return answerQuestion(-1) // -1 indicates skipped
    }

    fun getCurrentQuestion(): QuizQuestion? {
        return currentSession?.questions?.getOrNull(currentSession?.currentQuestionIndex ?: 0)
    }

    fun getCurrentSession(): QuizSession? {
        return currentSession
    }

    fun getCurrentProgress(): Pair<Int, Int> { // current, total
        val session = currentSession ?: return Pair(0, 0)
        return Pair(session.currentQuestionIndex + 1, session.questions.size)
    }

    fun getTimeElapsed(): Long {
        val session = currentSession ?: return 0
        return System.currentTimeMillis() - session.startTime
    }

    fun getQuestionTimeElapsed(): Long {
        return System.currentTimeMillis() - questionStartTime
    }

    private fun completeQuiz() {
        val session = currentSession ?: return

        session.isCompleted = true

        // Calculate results
        val correctAnswers = session.answers.zip(session.questions).count { (answer, question) ->
            answer == question.correctAnswer
        }

        val score = if (session.questions.isNotEmpty()) {
            (correctAnswers * 100) / session.questions.size
        } else 0

        val totalTime = System.currentTimeMillis() - session.startTime
        val averageTime = if (session.times.isNotEmpty()) {
            session.times.average().toLong()
        } else 0

        val result = QuizResult(
            score = score,
            correctAnswers = correctAnswers,
            totalQuestions = session.questions.size,
            timeSpent = totalTime,
            averageTimePerQuestion = averageTime,
            regionId = session.questions.firstOrNull()?.regionId
        )

        // Save results to preferences
        saveQuizResult(result)

        // Notify completion
        onQuizCompleted?.invoke(result)

        // Don't clean up the session immediately, keep it for viewing correct answers
        cancelTimer()
    }

    private fun notifyQuestionChanged() {
        val question = getCurrentQuestion()
        val progress = getCurrentProgress()

        if (question != null) {
            onQuestionChanged?.invoke(question, progress.first, progress.second)
        }
    }

    private fun saveQuizResult(result: QuizResult) {
        try {
            val prefs = context.getSharedPreferences("quiz_stats", Context.MODE_PRIVATE)
            val editor = prefs.edit()

            // Update total quizzes
            val totalQuizzes = prefs.getInt("total_quizzes", 0) + 1
            editor.putInt("total_quizzes", totalQuizzes)

            // Update best score
            val bestScore = maxOf(prefs.getInt("best_score", 0), result.score)
            editor.putInt("best_score", bestScore)

            // Update region-specific scores
            result.regionId?.let { regionId ->
                val regionScore = prefs.getInt("region_score_$regionId", 0)
                if (result.score > regionScore) {
                    editor.putInt("region_score_$regionId", result.score)
                }
            }

            // Update perfect quizzes count
            if (result.score == 100) {
                val perfectCount = prefs.getInt("perfect_quizzes", 0) + 1
                editor.putInt("perfect_quizzes", perfectCount)
            }

            // Update fastest time
            val fastestTime = prefs.getLong("fastest_quiz_time", Long.MAX_VALUE)
            if (result.timeSpent < fastestTime) {
                editor.putLong("fastest_quiz_time", result.timeSpent)
            }

            editor.apply()
        } catch (e: Exception) {
            ErrorHandler.handleError(
                context = context,
                throwable = e,
                errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
                userMessage = "Error al guardar resultados del quiz",
                canRecover = false
            )
        }
    }

    fun getUserStats(): UserStats {
        val prefs = context.getSharedPreferences("quiz_stats", Context.MODE_PRIVATE)

        val regionScores = mutableMapOf<Int, Int>()
        for (regionId in 1..5) {
            val score = prefs.getInt("region_score_$regionId", 0)
            if (score > 0) {
                regionScores[regionId] = score
            }
        }

        return UserStats(
            totalQuizzes = prefs.getInt("total_quizzes", 0),
            bestScore = prefs.getInt("best_score", 0),
            musclesStudied = 0, // TODO: Implement muscle tracking
            studyStreak = 0, // TODO: Implement streak tracking
            perfectQuizzes = prefs.getInt("perfect_quizzes", 0),
            fastestQuizTime = prefs.getLong("fastest_quiz_time", Long.MAX_VALUE),
            regionScores = regionScores
        )
    }

    // Timer functionality (optional)
    fun startTimer(timeLimitMs: Long) {
        this.timeLimitMs = timeLimitMs
        cancelTimer()

        timerHandler = Handler(Looper.getMainLooper())
        timerHandler?.postDelayed({
            onTimeUp?.invoke()
        }, timeLimitMs)
    }

    fun cancelTimer() {
        timerHandler?.removeCallbacksAndMessages(null)
        timerHandler = null
    }

    fun isQuizActive(): Boolean {
        return currentSession != null && !currentSession!!.isCompleted
    }

    fun abandonQuiz() {
        currentSession = null
        cancelTimer()
    }
}