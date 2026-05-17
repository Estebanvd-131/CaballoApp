package com.villalobos.caballoapp

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.villalobos.caballoapp.databinding.ActivityCorrectAnswersBinding

class CorrectAnswersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCorrectAnswersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            binding = ActivityCorrectAnswersBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // Obtener el ID de la región
            val regionId = intent.getIntExtra("REGION_ID", 1)
            
            // Configurar título según la región
            setupTitle(regionId)
            
            // Mostrar respuestas correctas
            showCorrectAnswers(regionId)
            
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

    private fun setupTitle(regionId: Int) {
        val regionName = DatosMusculares.obtenerRegionPorId(regionId)?.nombreCompleto ?: "Anatomía General"
        binding.tvTitle.text = "Respuestas Correctas - $regionName"
        
        // Log para depuración
        android.util.Log.d("CorrectAnswersActivity", "Configurando título para región ID: $regionId, Nombre: $regionName")
    }

    private fun showCorrectAnswers(regionId: Int) {
        // Log para depuración
        android.util.Log.d("CorrectAnswersActivity", "Mostrando respuestas para región ID: $regionId")
        
        // Obtener todas las preguntas para esta región
        val questions = QuizData.getQuestionsByRegion(regionId)
        
        // Log para depuración
        android.util.Log.d("CorrectAnswersActivity", "Número de preguntas encontradas: ${questions.size}")
        
        // Limpiar contenedor
        binding.answersContainer.removeAllViews()
        
        // Agregar cada pregunta con su respuesta correcta
        questions.forEachIndexed { index, question ->
            val questionCard = createQuestionCard(question, index + 1)
            binding.answersContainer.addView(questionCard)
        }
        
        // Mostrar un mensaje si no hay preguntas
        if (questions.isEmpty()) {
            val noQuestionsText = TextView(this)
            noQuestionsText.text = "No hay preguntas disponibles para esta región"
            noQuestionsText.textSize = 16f
            noQuestionsText.setTextColor(resources.getColor(R.color.text_primary, theme))
            noQuestionsText.setPadding(16, 16, 16, 16)
            binding.answersContainer.addView(noQuestionsText)
        }
    }

    private fun createQuestionCard(question: QuizQuestion, questionNumber: Int): CardView {
        val cardView = CardView(this)
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(16, 16, 16, 16)
        cardView.layoutParams = layoutParams
        
        // Configurar estilo del card
        cardView.cardElevation = 4f
        cardView.radius = 12f
        cardView.setContentPadding(24, 24, 24, 24)
        cardView.setCardBackgroundColor(resources.getColor(R.color.warm_cream, theme))
        
        // Crear contenido del card
        val linearLayout = androidx.appcompat.widget.LinearLayoutCompat(this)
        linearLayout.orientation = androidx.appcompat.widget.LinearLayoutCompat.VERTICAL
        
        // TextView para el número de pregunta
        val questionNumberText = TextView(this)
        questionNumberText.text = "Pregunta $questionNumber"
        questionNumberText.textSize = 16f
        questionNumberText.setTextColor(resources.getColor(R.color.primary_brown, theme))
        questionNumberText.setTypeface(null, android.graphics.Typeface.BOLD)
        linearLayout.addView(questionNumberText)
        
        // TextView para la pregunta
        val questionText = TextView(this)
        questionText.text = question.question
        questionText.textSize = 14f
        questionText.setTextColor(resources.getColor(R.color.text_primary, theme))
        questionText.setPadding(0, 8, 0, 8)
        linearLayout.addView(questionText)
        
        // TextView para la respuesta correcta
        val correctAnswerText = TextView(this)
        correctAnswerText.text = "Respuesta correcta: ${question.options[question.correctAnswer]}"
        correctAnswerText.textSize = 14f
        correctAnswerText.setTextColor(resources.getColor(R.color.correct_green, theme))
        correctAnswerText.setTypeface(null, android.graphics.Typeface.BOLD)
        correctAnswerText.setPadding(0, 8, 0, 0)
        linearLayout.addView(correctAnswerText)
        
        cardView.addView(linearLayout)
        
        return cardView
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}