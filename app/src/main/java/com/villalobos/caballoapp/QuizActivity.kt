package com.villalobos.caballoapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.RadioGroup
import com.google.android.material.radiobutton.MaterialRadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.villalobos.caballoapp.databinding.ActivityQuizBinding

class QuizActivity : BaseNavigationActivity() {

    private lateinit var binding: ActivityQuizBinding
    private lateinit var quizEngine: QuizEngine
    private var regionId: Int? = null
    private var timeUpdateHandler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            binding = ActivityQuizBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // Obtener parámetros
            regionId = intent.getIntExtra("REGION_ID", 0).takeIf { it != 0 }

            // Inicializar quiz engine
            quizEngine = QuizEngine(this)

            // Configurar callbacks
            setupQuizCallbacks()

            // Configurar UI
            setupUI()

            // Iniciar quiz
            startQuiz()
            
            // Configurar el botón de inicio
            setupHomeButton(binding.btnHome)
            
            // Aplicar colores de accesibilidad
            applyActivityAccessibilityColors()

        } catch (e: Exception) {
            ErrorHandler.handleError(
                context = this,
                throwable = e,
                errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
                userMessage = "Error al inicializar quiz",
                canRecover = true,
                recoveryAction = { finish() }
            )
        }
    }

    private fun setupQuizCallbacks() {
        quizEngine.onQuestionChanged = { question, current, total ->
            updateQuestionUI(question, current, total)
        }

        quizEngine.onQuizCompleted = { result ->
            showQuizResults(result)
        }

        quizEngine.onTimeUp = {
            // Handle time up if needed
            Toast.makeText(this, "¡Tiempo agotado!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupUI() {
        // Configurar título del quiz
        val regionName = regionId?.let { id ->
            DatosMusculares.obtenerRegionPorId(id)?.nombreCompleto ?: "Anatomía General"
        } ?: "Anatomía General"

        binding.tvQuizTitle.text = "Quiz: $regionName"

        // Configurar botones
        binding.btnAnswer.setOnClickListener {
            submitAnswer()
        }

        binding.btnSkip.setOnClickListener {
            skipQuestion()
        }

        // Configurar accesibilidad
        AccesibilityHelper.setContentDescription(
            binding.btnAnswer,
            "Botón para enviar respuesta seleccionada",
            "Acción"
        )

        AccesibilityHelper.setContentDescription(
            binding.btnSkip,
            "Botón para saltar la pregunta actual",
            "Acción"
        )

        // Iniciar actualización de tiempo
        startTimeUpdates()
    }

    private fun startQuiz() {
        val success = quizEngine.startQuiz(regionId, questionCount = 10)

        if (!success) {
            Toast.makeText(this, "No hay suficientes preguntas disponibles", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun updateQuestionUI(question: QuizQuestion, current: Int, total: Int) {
        // Actualizar progreso
        binding.tvProgress.text = "Pregunta $current de $total"
        binding.progressBar.progress = (current * 100) / total

        // Actualizar pregunta
        binding.tvQuestion.text = question.question

        // Limpiar selección anterior
        binding.radioGroupOptions.clearCheck()

        // Configurar opciones
        val radioButtons = listOf(
            binding.rbOption1,
            binding.rbOption2,
            binding.rbOption3,
            binding.rbOption4
        )

        question.options.forEachIndexed { index, option ->
            radioButtons[index].text = option
            radioButtons[index].isEnabled = true
        }

        // Configurar accesibilidad para opciones
        question.options.forEachIndexed { index, option ->
            AccesibilityHelper.setContentDescription(
                radioButtons[index],
                "Opción ${'A' + index}: $option",
                "Opción de respuesta"
            )
        }

        // IMPORTANTE: Re-habilitar botones para la nueva pregunta
        setButtonsEnabled(true)
    }

    private fun submitAnswer() {
        val selectedId = binding.radioGroupOptions.checkedRadioButtonId

        if (selectedId == -1) {
            Toast.makeText(this, "Por favor selecciona una respuesta", Toast.LENGTH_SHORT).show()
            return
        }

        // Encontrar el índice de la opción seleccionada
        val radioButtons = listOf(
            binding.rbOption1,
            binding.rbOption2,
            binding.rbOption3,
            binding.rbOption4
        )

        val selectedIndex = radioButtons.indexOfFirst { it.id == selectedId }

        if (selectedIndex != -1) {
            // Deshabilitar botones mientras procesa
            setButtonsEnabled(false)

            // Enviar respuesta
            quizEngine.answerQuestion(selectedIndex)
        }
    }

    private fun skipQuestion() {
        AlertDialog.Builder(this)
            .setTitle("Saltar Pregunta")
            .setMessage("¿Estás seguro de que quieres saltar esta pregunta?")
            .setPositiveButton("Saltar") { _, _ ->
                setButtonsEnabled(false)
                quizEngine.skipQuestion()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun showQuizResults(result: QuizEngine.QuizResult) {
        // Detener actualización de tiempo
        stopTimeUpdates()

        val timeFormatted = formatTime(result.timeSpent)

        AlertDialog.Builder(this)
            .setTitle("¡Quiz Completado!")
            .setMessage(buildString {
                append("Puntuación: ${result.score}%\n")
                append("Respuestas correctas: ${result.correctAnswers}/${result.totalQuestions}\n")
                append("Tiempo total: $timeFormatted\n")
                append("Tiempo promedio por pregunta: ${result.averageTimePerQuestion / 1000}s")
            })
            .setPositiveButton("Ver Respuestas Correctas") { _, _ ->
                showCorrectAnswers()
            }
            .setNegativeButton("Ver Logros") { _, _ ->
                showAchievements()
            }
            .setNeutralButton("Continuar") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()

        // Verificar logros desbloqueados
        checkForNewAchievements(result)
    }

    private fun showAchievements() {
        val userStats = quizEngine.getUserStats()
        val unlockedAchievements = AchievementData.getUnlockedAchievements(userStats)
        val newAchievements = unlockedAchievements.filter { achievement ->
            // Aquí podríamos verificar cuáles son nuevos comparando con logros previos
            true // Por simplicidad, mostrar todos desbloqueados
        }

        if (newAchievements.isNotEmpty()) {
            val achievementNames = newAchievements.joinToString("\n") { "🏆 ${it.title}" }

            AlertDialog.Builder(this)
                .setTitle("¡Nuevos Logros!")
                .setMessage("Has desbloqueado:\n\n$achievementNames")
                .setPositiveButton("Genial!") { _, _ ->
                    finish()
                }
                .show()
        } else {
            finish()
        }
    }

    private fun showCorrectAnswers() {
        val session = quizEngine.getCurrentSession()
        if (session == null) {
            Toast.makeText(this, "No se pudo obtener la información del quiz", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val regionId = session.questions.firstOrNull()?.regionId ?: 1
        
        val intent = Intent(this, CorrectAnswersActivity::class.java)
        intent.putExtra("REGION_ID", regionId)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        
        // No cerrar esta actividad hasta que el usuario decida volver
    }

    private fun checkForNewAchievements(result: QuizEngine.QuizResult) {
        // Aquí podríamos implementar notificaciones push o celebraciones
        // Por ahora, solo guardamos las estadísticas que ya se hacen en QuizEngine
    }

    private fun setButtonsEnabled(enabled: Boolean) {
        binding.btnAnswer.isEnabled = enabled
        binding.btnSkip.isEnabled = enabled

        // Habilitar/deshabilitar radio buttons
        val radioButtons = listOf(
            binding.rbOption1,
            binding.rbOption2,
            binding.rbOption3,
            binding.rbOption4
        )

        radioButtons.forEach { it.isEnabled = enabled }
    }

    private fun startTimeUpdates() {
        timeUpdateHandler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                if (quizEngine.isQuizActive()) {
                    val timeElapsed = quizEngine.getTimeElapsed()
                    binding.tvTimeInfo.text = "Tiempo: ${formatTime(timeElapsed)}"
                    timeUpdateHandler?.postDelayed(this, 1000)
                }
            }
        }
        timeUpdateHandler?.post(runnable)
    }

    private fun stopTimeUpdates() {
        timeUpdateHandler?.removeCallbacksAndMessages(null)
        timeUpdateHandler = null
    }

    private fun formatTime(milliseconds: Long): String {
        val totalSeconds = milliseconds / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimeUpdates()
        quizEngine.abandonQuiz()
    }

    override fun onBackPressed() {
        if (quizEngine.isQuizActive()) {
            AlertDialog.Builder(this)
                .setTitle("Abandonar Quiz")
                .setMessage("¿Estás seguro de que quieres salir? Perderás el progreso actual.")
                .setPositiveButton("Salir") { _, _ ->
                    super.onBackPressed()
                }
                .setNegativeButton("Continuar", null)
                .show()
        } else {
            super.onBackPressed()
        }
    }
    
    override fun applyActivityAccessibilityColors() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al aplicar colores de accesibilidad en QuizActivity"
        ) {
            // Aplicar colores de accesibilidad a los elementos de la actividad
            AccesibilityHelper.applyAccessibilityColorsToApp(this)
        }
    }
}