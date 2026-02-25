package com.villalobos.caballoapp.ui.quiz

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.villalobos.caballoapp.R
import com.villalobos.caballoapp.util.AccesibilityHelper
import com.villalobos.caballoapp.util.ErrorHandler
import com.villalobos.caballoapp.databinding.ActivityCorrectAnswersBinding
import com.villalobos.caballoapp.ui.quiz.CorrectAnswersViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity para mostrar las respuestas correctas del quiz.
 * Implementa MVVM delegando la lógica al CorrectAnswersViewModel.
 */
@AndroidEntryPoint
class CorrectAnswersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCorrectAnswersBinding
    private val viewModel: CorrectAnswersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            binding = ActivityCorrectAnswersBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // Observar el estado del ViewModel
            observeViewModel()
            
            // Configurar botón de volver
            setupBackButton()
            
            // Aplicar colores de accesibilidad sin mostrar errores
            try {
                AccesibilityHelper.applyAccessibilityColorsToApp(this)
            } catch (e: Exception) {
                // Silenciosamente ignorar errores de accesibilidad para no molestar al usuario
                android.util.Log.d("CorrectAnswersActivity", "Error al aplicar colores de accesibilidad: ${e.message}")
            }
            
        } catch (e: Exception) {
            ErrorHandler.handleError(
                context = this,
                throwable = e,
                errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
                userMessage = "Error al cargar respuestas correctas",
                canRecover = true,
                recoveryAction = { finish() }
            )
        }
    }

    private fun observeViewModel() {
        viewModel.state.observe(this) { state ->
            // Actualizar título
            binding.tvTitle.text = "Respuestas Correctas - ${state.regionName}"
            
            // Mostrar respuestas
            showCorrectAnswers(state)
        }
    }

    private fun showCorrectAnswers(state: CorrectAnswersViewModel.CorrectAnswersState) {
        // Log para depuración
        android.util.Log.d("CorrectAnswersActivity", "Mostrando respuestas para región ID: ${state.regionId}")
        android.util.Log.d("CorrectAnswersActivity", "Número de preguntas encontradas: ${state.questions.size}")
        
        // Limpiar contenedor
        binding.answersContainer.removeAllViews()
        
        if (state.hasQuestions) {
            // Agregar cada pregunta con su respuesta correcta
            state.questions.forEachIndexed { index, question ->
                val correctAnswer = viewModel.getCorrectAnswerText(question)
                val questionCard = createQuestionCard(question.question, correctAnswer, index + 1)
                binding.answersContainer.addView(questionCard)
            }
        } else {
            // Mostrar un mensaje si no hay preguntas
            val noQuestionsText = TextView(this)
            noQuestionsText.text = "No hay preguntas disponibles para esta región"
            noQuestionsText.textSize = 16f
            noQuestionsText.setTextColor(ContextCompat.getColor(this, R.color.text_primary))
            noQuestionsText.setPadding(16, 16, 16, 16)
            binding.answersContainer.addView(noQuestionsText)
        }
    }

    private fun createQuestionCard(questionText: String, correctAnswer: String, questionNumber: Int): android.view.View {
        val view = layoutInflater.inflate(R.layout.item_correct_answer, binding.answersContainer, false)
        
        val tvQuestionNumber = view.findViewById<TextView>(R.id.tvQuestionNumber)
        val tvQuestionText = view.findViewById<TextView>(R.id.tvQuestionText)
        val tvCorrectAnswer = view.findViewById<TextView>(R.id.tvCorrectAnswer)
        
        tvQuestionNumber.text = "Pregunta $questionNumber"
        tvQuestionText.text = questionText
        tvCorrectAnswer.text = correctAnswer
        
        return view
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}