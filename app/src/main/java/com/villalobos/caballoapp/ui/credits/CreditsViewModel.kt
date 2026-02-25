package com.villalobos.caballoapp.ui.credits

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel para Creditos.
 * Maneja la lógica de la pantalla de créditos.
 */
@HiltViewModel
class CreditsViewModel @Inject constructor() : ViewModel() {

    // ============ Estado ============

    data class CreditsState(
        val developerName: String = "David Villalobos",
        val universityName: String = "Universidad Santiago de Cali",
        val appVersion: String = "1.0.0",
        val projectName: String = "EquusApp - Anatomía Muscular Equina"
    )

    // ============ Eventos ============

    sealed class CreditsEvent {
        object NavigateBack : CreditsEvent()
    }

    // ============ LiveData ============

    private val _state = MutableLiveData(CreditsState())
    val state: LiveData<CreditsState> = _state

    private val _event = MutableLiveData<CreditsEvent?>()
    val event: LiveData<CreditsEvent?> = _event

    // ============ Acciones ============

    /**
     * Navega hacia atrás.
     */
    fun navigateBack() {
        _event.value = CreditsEvent.NavigateBack
    }

    /**
     * Limpia el evento actual.
     */
    fun clearEvent() {
        _event.value = null
    }
}
