package com.villalobos.caballoapp.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para SplashActivity.
 * Maneja la lógica de temporización del splash screen.
 */
@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {

    companion object {
        private const val SPLASH_TIMEOUT_MS = 3000L // 3 segundos
    }

    // ============ Eventos ============

    sealed class SplashEvent {
        object NavigateToMain : SplashEvent()
    }

    // ============ LiveData ============

    private val _event = MutableLiveData<SplashEvent?>()
    val event: LiveData<SplashEvent?> = _event

    // ============ Acciones ============

    /**
     * Inicia el temporizador del splash.
     */
    fun startSplashTimer() {
        viewModelScope.launch {
            delay(SPLASH_TIMEOUT_MS)
            _event.value = SplashEvent.NavigateToMain
        }
    }

    /**
     * Fuerza la navegación inmediata (por ejemplo, al tocar la pantalla).
     */
    fun skipSplash() {
        _event.value = SplashEvent.NavigateToMain
    }

    /**
     * Limpia el evento actual.
     */
    fun clearEvent() {
        _event.value = null
    }
}
