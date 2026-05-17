package com.villalobos.caballoapp.ui.tutorial

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

/**
 * ViewModel para TutorialActivity.
 * Maneja la lógica de navegación del tutorial.
 */
@HiltViewModel
class TutorialViewModel @Inject constructor(
    @Named("tutorial_prefs") private val sharedPreferences: SharedPreferences
) : ViewModel() {

    companion object {
        private const val KEY_NO_MOSTRAR_TUTORIAL = "no_mostrar_tutorial"
    }

    // ============ Estados ============

    data class TutorialState(
        val currentStep: Int = 0,
        val totalSteps: Int = 0,
        val isLastStep: Boolean = false,
        val noMostrarAgain: Boolean = false
    ) {
        val progress: Int get() = if (totalSteps > 0) ((currentStep + 1) * 100) / totalSteps else 0
    }

    sealed class TutorialEvent {
        object CompleteTutorial : TutorialEvent()
        object SkipTutorial : TutorialEvent()
        data class StepChanged(val step: Int) : TutorialEvent()
    }

    // ============ LiveData ============

    private val _state = MutableLiveData(TutorialState())
    val state: LiveData<TutorialState> = _state

    private val _event = MutableLiveData<TutorialEvent?>()
    val event: LiveData<TutorialEvent?> = _event

    // ============ Acciones ============

    /**
     * Inicializa el estado del tutorial con el número total de pasos.
     */
    fun initialize(totalSteps: Int) {
        _state.value = TutorialState(
            currentStep = 0,
            totalSteps = totalSteps,
            isLastStep = totalSteps <= 1
        )
    }

    /**
     * Avanza al siguiente paso.
     */
    fun nextStep() {
        val currentState = _state.value ?: return
        val newStep = currentState.currentStep + 1

        if (newStep >= currentState.totalSteps) {
            completeTutorial()
            return
        }

        _state.value = currentState.copy(
            currentStep = newStep,
            isLastStep = newStep >= currentState.totalSteps - 1
        )
        _event.value = TutorialEvent.StepChanged(newStep)
    }

    /**
     * Retrocede al paso anterior.
     */
    fun previousStep() {
        val currentState = _state.value ?: return
        val newStep = maxOf(0, currentState.currentStep - 1)

        _state.value = currentState.copy(
            currentStep = newStep,
            isLastStep = false
        )
        _event.value = TutorialEvent.StepChanged(newStep)
    }

    /**
     * Va a un paso específico.
     */
    fun goToStep(step: Int) {
        val currentState = _state.value ?: return
        val validStep = step.coerceIn(0, currentState.totalSteps - 1)

        _state.value = currentState.copy(
            currentStep = validStep,
            isLastStep = validStep >= currentState.totalSteps - 1
        )
        _event.value = TutorialEvent.StepChanged(validStep)
    }

    /**
     * Cambia el estado de "no mostrar de nuevo".
     */
    fun setNoMostrarAgain(value: Boolean) {
        _state.value = _state.value?.copy(noMostrarAgain = value)
    }

    /**
     * Completa el tutorial.
     */
    fun completeTutorial() {
        val noMostrar = _state.value?.noMostrarAgain == true
        if (noMostrar) {
            sharedPreferences.edit()
                .putBoolean(KEY_NO_MOSTRAR_TUTORIAL, true)
                .apply()
        }
        _event.value = TutorialEvent.CompleteTutorial
    }

    /**
     * Salta el tutorial.
     */
    fun skipTutorial() {
        val noMostrar = _state.value?.noMostrarAgain == true
        if (noMostrar) {
            sharedPreferences.edit()
                .putBoolean(KEY_NO_MOSTRAR_TUTORIAL, true)
                .apply()
        }
        _event.value = TutorialEvent.SkipTutorial
    }

    /**
     * Limpia el evento actual.
     */
    fun clearEvent() {
        _event.value = null
    }

    // ============ Helpers ============

    /**
     * Verifica si puede retroceder.
     */
    fun canGoPrevious(): Boolean {
        return (_state.value?.currentStep ?: 0) > 0
    }

    /**
     * Verifica si puede avanzar.
     */
    fun canGoNext(): Boolean {
        val state = _state.value ?: return false
        return state.currentStep < state.totalSteps - 1
    }
}
