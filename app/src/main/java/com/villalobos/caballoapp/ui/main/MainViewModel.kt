package com.villalobos.caballoapp.ui.main

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.villalobos.caballoapp.data.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

/**
 * ViewModel para MainActivity.
 * Maneja la lógica de navegación principal y estado del tutorial.
 * Sincroniza estadísticas en tiempo real usando QuizRepository.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    @Named("tutorial_prefs") private val sharedPreferences: SharedPreferences,
    private val quizRepository: QuizRepository
) : ViewModel() {

    companion object {
        private const val KEY_NO_MOSTRAR_TUTORIAL = "no_mostrar_tutorial"
    }

    // ============ Estados ============

    sealed class MainEvent {
        object NavigateToRegionMenu : MainEvent()
        object NavigateToAccessibility : MainEvent()
        object NavigateToCredits : MainEvent()
        object NavigateToTutorial : MainEvent()
        object ExitApp : MainEvent()
    }

    // ============ LiveData ============

    private val _event = MutableLiveData<MainEvent?>()
    val event: LiveData<MainEvent?> = _event

    private val _shouldShowTutorial = MutableLiveData<Boolean>()
    val shouldShowTutorial: LiveData<Boolean> = _shouldShowTutorial

    // Stats del usuario (Observados desde Room)
    private val _userLevel = MutableLiveData<Int>(1)
    val userLevel: LiveData<Int> = _userLevel

    private val _userXp = MutableLiveData<Int>(0)
    val userXp: LiveData<Int> = _userXp

    private val _userStreak = MutableLiveData<Int>(0)
    val userStreak: LiveData<Int> = _userStreak

    // ============ Inicialización ============

    init {
        checkFirstTimeUser()
        observeUserStats()
    }

    private fun observeUserStats() {
        viewModelScope.launch {
            quizRepository.getUserProgressFlow().collectLatest { progress ->
                if (progress != null) {
                    _userLevel.value = progress.level
                    _userXp.value = progress.totalXp
                    _userStreak.value = progress.studyStreak
                } else {
                    // Valores por defecto si no hay progreso aún
                    _userLevel.value = 1
                    _userXp.value = 0
                    _userStreak.value = 0
                }
            }
        }
    }

    // ============ Acciones ============

    /**
     * Verifica si es la primera vez que el usuario abre la app.
     */
    fun checkFirstTimeUser() {
        val noMostrarTutorial = sharedPreferences.getBoolean(KEY_NO_MOSTRAR_TUTORIAL, false)
        _shouldShowTutorial.value = !noMostrarTutorial
    }

    /**
     * Marca que el tutorial no debe mostrarse de nuevo.
     */
    fun markTutorialAsShown() {
        sharedPreferences.edit()
            .putBoolean(KEY_NO_MOSTRAR_TUTORIAL, true)
            .apply()
        _shouldShowTutorial.value = false
    }

    /**
     * Reinicia el tutorial para mostrarlo de nuevo.
     */
    fun resetTutorial() {
        sharedPreferences.edit()
            .putBoolean(KEY_NO_MOSTRAR_TUTORIAL, false)
            .apply()
        _shouldShowTutorial.value = true
    }

    // ============ Navegación ============

    fun navigateToRegionMenu() {
        _event.value = MainEvent.NavigateToRegionMenu
    }

    fun navigateToAccessibility() {
        _event.value = MainEvent.NavigateToAccessibility
    }

    fun navigateToCredits() {
        _event.value = MainEvent.NavigateToCredits
    }

    fun navigateToTutorial() {
        _event.value = MainEvent.NavigateToTutorial
    }

    fun exitApp() {
        _event.value = MainEvent.ExitApp
    }

    /**
     * Limpia el evento actual (después de ser consumido).
     */
    fun clearEvent() {
        _event.value = null
    }

    // ============ Helpers ============

    /**
     * Verifica si el tutorial está habilitado.
     */
    fun isTutorialEnabled(): Boolean {
        return !sharedPreferences.getBoolean(KEY_NO_MOSTRAR_TUTORIAL, false)
    }
}
