package com.villalobos.caballoapp.ui.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.villalobos.caballoapp.data.source.DatosMusculares
import com.villalobos.caballoapp.data.source.QuizData
import com.villalobos.caballoapp.data.model.QuizQuestion
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel para CorrectAnswersActivity.
 * Maneja la lógica de mostrar las respuestas correctas del quiz.
 */
@HiltViewModel
class CorrectAnswersViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        const val KEY_REGION_ID = "REGION_ID"
        private const val DEFAULT_REGION_ID = 1
    }

    // ============ Estado ============

    data class CorrectAnswersState(
        val regionId: Int = DEFAULT_REGION_ID,
        val regionName: String = "Anatomía General",
        val questions: List<QuizQuestion> = emptyList(),
        val isLoading: Boolean = true
    ) {
        val hasQuestions: Boolean get() = questions.isNotEmpty()
    }

    // ============ LiveData ============

    private val _state = MutableLiveData(CorrectAnswersState())
    val state: LiveData<CorrectAnswersState> = _state

    // ============ Inicialización ============

    init {
        loadCorrectAnswers()
    }

    // ============ Acciones ============

    /**
     * Carga las respuestas correctas para la región especificada.
     */
    fun loadCorrectAnswers() {
        val regionId = savedStateHandle.get<Int>(KEY_REGION_ID) ?: DEFAULT_REGION_ID
        
        val regionName = DatosMusculares.obtenerRegionPorId(regionId)?.nombreCompleto 
            ?: "Anatomía General"
        
        val questions = QuizData.getQuestionsByRegion(regionId)
        
        _state.value = CorrectAnswersState(
            regionId = regionId,
            regionName = regionName,
            questions = questions,
            isLoading = false
        )
    }

    /**
     * Obtiene la respuesta correcta para una pregunta.
     */
    fun getCorrectAnswerText(question: QuizQuestion): String {
        return question.options.getOrNull(question.correctAnswer) ?: "Respuesta no disponible"
    }
}

