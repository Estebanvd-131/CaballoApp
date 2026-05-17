package com.villalobos.caballoapp.ui.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.villalobos.caballoapp.data.model.Achievement
import com.villalobos.caballoapp.data.source.AchievementData
import com.villalobos.caballoapp.data.model.QuizQuestion
import com.villalobos.caballoapp.data.source.UserStats
import com.villalobos.caballoapp.data.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de Quiz.
 * Maneja la lógica del quiz y expone estados observables.
 * Usa Hilt para inyección de dependencias.
 */
@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: QuizRepository
) : ViewModel() {

    // ============ Estados del Quiz ============

    data class QuizState(
        val questions: List<QuizQuestion> = emptyList(),
        val currentQuestionIndex: Int = 0,
        val answers: List<Int> = emptyList(),
        val times: List<Long> = emptyList(),
        val startTime: Long = 0L,
        val questionStartTime: Long = 0L,
        val isActive: Boolean = false,
        val isCompleted: Boolean = false
    ) {
        val currentQuestion: QuizQuestion?
            get() = questions.getOrNull(currentQuestionIndex)
        
        val progress: Pair<Int, Int>
            get() = Pair(currentQuestionIndex + 1, questions.size)
        
        val progressPercent: Int
            get() = if (questions.isNotEmpty()) ((currentQuestionIndex + 1) * 100) / questions.size else 0
    }

    data class QuizResult(
        val score: Int,
        val correctAnswers: Int,
        val totalQuestions: Int,
        val timeSpent: Long,
        val averageTimePerQuestion: Long,
        val regionId: Int?
    )

    sealed class QuizEvent {
        data class QuestionChanged(val question: QuizQuestion, val current: Int, val total: Int) : QuizEvent()
        data class QuizCompleted(val result: QuizResult) : QuizEvent()
        object TimeUp : QuizEvent()
        data class Error(val message: String) : QuizEvent()
    }

    // ============ LiveData Observables ============

    private val _quizState = MutableLiveData(QuizState())
    val quizState: LiveData<QuizState> = _quizState

    private val _quizEvent = MutableLiveData<QuizEvent?>()
    val quizEvent: LiveData<QuizEvent?> = _quizEvent

    private val _timeElapsed = MutableLiveData(0L)
    val timeElapsed: LiveData<Long> = _timeElapsed

    private val _userStats = MutableLiveData<UserStats>()
    val userStats: LiveData<UserStats> = _userStats

    private val _isStartingQuiz = MutableLiveData(false)
    val isStartingQuiz: LiveData<Boolean> = _isStartingQuiz

    private var regionId: Int? = null

    // ============ Acciones del Quiz ============

    /**
     * Inicia un nuevo quiz.
     * @param regionId ID de la región (null para quiz general)
     * @param questionCount Número de preguntas
     * @return true si el quiz se inició correctamente
     */
    fun startQuiz(regionId: Int? = null, questionCount: Int = 10) {
        viewModelScope.launch {
            _isStartingQuiz.value = true
            try {
                val questions = repository.getQuizQuestionsAsync(regionId, questionCount)
                if (questions.isEmpty()) {
                    _quizEvent.value = QuizEvent.Error("No hay suficientes preguntas disponibles")
                    return@launch
                }

                this@QuizViewModel.regionId = regionId
                val now = System.currentTimeMillis()

                _quizState.value = QuizState(
                    questions = questions,
                    currentQuestionIndex = 0,
                    answers = emptyList(),
                    times = emptyList(),
                    startTime = now,
                    questionStartTime = now,
                    isActive = true,
                    isCompleted = false
                )

                notifyQuestionChanged()
            } catch (e: Exception) {
                _quizEvent.value = QuizEvent.Error("Error al iniciar quiz: ${e.message}")
            } finally {
                _isStartingQuiz.value = false
            }
        }
    }

    /**
     * Responde la pregunta actual.
     * @param selectedAnswer Índice de la respuesta seleccionada (-1 para saltar)
     */
    fun answerQuestion(selectedAnswer: Int) {
        val state = _quizState.value ?: return

        val timeTaken = System.currentTimeMillis() - state.questionStartTime
        val newAnswers = state.answers + selectedAnswer
        val newTimes = state.times + timeTaken

        // Verificar si el quiz está completo
        if (state.currentQuestionIndex >= state.questions.size - 1) {
            val finalState = state.copy(
                answers = newAnswers,
                times = newTimes,
                isActive = false,
                isCompleted = true
            )
            _quizState.value = finalState
            completeQuiz(finalState)
            return
        }

        // Avanzar a la siguiente pregunta
        _quizState.value = state.copy(
            currentQuestionIndex = state.currentQuestionIndex + 1,
            answers = newAnswers,
            times = newTimes,
            questionStartTime = System.currentTimeMillis()
        )

        notifyQuestionChanged()
    }

    /**
     * Salta la pregunta actual.
     */
    fun skipQuestion() {
        answerQuestion(-1)
    }

    /**
     * Abandona el quiz actual.
     */
    fun abandonQuiz() {
        _quizState.value = QuizState()
    }

    /**
     * Actualiza el tiempo transcurrido.
     */
    fun updateTimeElapsed() {
        val state = _quizState.value ?: return
        if (state.isActive) {
            _timeElapsed.value = System.currentTimeMillis() - state.startTime
        }
    }

    /**
     * Limpia el evento actual (después de ser consumido).
     */
    fun clearEvent() {
        _quizEvent.value = null
    }

    /**
     * Carga las estadísticas del usuario.
     */
    fun loadUserStats() {
        viewModelScope.launch {
            _userStats.value = repository.getUserStatsAsync()
        }
    }

    // ============ Funciones privadas ============

    private fun notifyQuestionChanged() {
        val state = _quizState.value ?: return
        val question = state.currentQuestion ?: return
        val progress = state.progress

        _quizEvent.value = QuizEvent.QuestionChanged(question, progress.first, progress.second)
    }

    private fun completeQuiz(state: QuizState) {
        val correctAnswers = state.answers.zip(state.questions).count { (answer, question) ->
            answer == question.correctAnswer
        }

        val score = if (state.questions.isNotEmpty()) {
            (correctAnswers * 100) / state.questions.size
        } else 0

        val totalTime = System.currentTimeMillis() - state.startTime
        val averageTime = if (state.times.isNotEmpty()) {
            state.times.average().toLong()
        } else 0L

        val result = QuizResult(
            score = score,
            correctAnswers = correctAnswers,
            totalQuestions = state.questions.size,
            timeSpent = totalTime,
            averageTimePerQuestion = averageTime,
            regionId = regionId
        )

        viewModelScope.launch {
            repository.saveQuizResultAsync(
                score = score,
                correctAnswers = correctAnswers,
                regionId = regionId,
                timeSpent = totalTime
            )

            _quizEvent.value = QuizEvent.QuizCompleted(result)
            loadUserStats()
        }
    }

    // ============ Helpers públicos ============

    /**
     * Verifica si el quiz está activo.
     */
    fun isQuizActive(): Boolean = _quizState.value?.isActive == true

    /**
     * Obtiene el tiempo transcurrido desde el inicio del quiz.
     */
    fun getTimeElapsed(): Long {
        val state = _quizState.value ?: return 0
        return System.currentTimeMillis() - state.startTime
    }

    /**
     * Obtiene las preguntas del quiz actual (para mostrar respuestas correctas).
     */
    fun getCurrentQuestions(): List<QuizQuestion> {
        return _quizState.value?.questions ?: emptyList()
    }

    /**
     * Obtiene logros desbloqueados basados en estadísticas actuales.
     */
    fun getUnlockedAchievements(): List<Achievement> {
        val stats = _userStats.value ?: repository.getUserStats()
        return AchievementData.getUnlockedAchievements(stats)
    }
}

