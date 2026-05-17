package com.villalobos.caballoapp.ui.accessibility

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.villalobos.caballoapp.util.AccesibilityHelper
import com.villalobos.caballoapp.data.model.AccessibilityConfig
import com.villalobos.caballoapp.data.model.ColorblindType
import com.villalobos.caballoapp.data.model.TextScale
import com.villalobos.caballoapp.data.repository.AccessibilityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel para la pantalla de Accesibilidad.
 * Maneja la lógica de configuración de accesibilidad y expone estados observables.
 * Usa Hilt para inyección de dependencias.
 */
@HiltViewModel
class AccessibilityViewModel @Inject constructor(
    private val repository: AccessibilityRepository
) : ViewModel() {

    // ============ Estados ============

    data class AccessibilityState(
        val colorblindType: ColorblindType = ColorblindType.NONE,
        val highContrast: Boolean = false,
        val textScale: TextScale = TextScale.NORMAL,
        val hasUnsavedChanges: Boolean = false
    )

    sealed class AccessibilityEvent {
        data class ConfigSaved(val message: String) : AccessibilityEvent()
        data class ConfigChanged(val typeName: String, val description: String) : AccessibilityEvent()
        object RestartRequired : AccessibilityEvent()
        data class Error(val message: String) : AccessibilityEvent()
    }

    // ============ LiveData Observables ============

    private val _state = MutableLiveData(AccessibilityState())
    val state: LiveData<AccessibilityState> = _state

    private val _event = MutableLiveData<AccessibilityEvent?>()
    val event: LiveData<AccessibilityEvent?> = _event

    private var initialConfig: AccessibilityConfig? = null

    // ============ Inicialización ============

    init {
        loadCurrentConfig()
    }

    /**
     * Carga la configuración actual desde el repository.
     */
    fun loadCurrentConfig() {
        val config = repository.getAccessibilityConfig()
        initialConfig = config

        _state.value = AccessibilityState(
            colorblindType = config.colorblindType,
            highContrast = config.highContrast,
            textScale = config.textScale,
            hasUnsavedChanges = false
        )
    }

    // ============ Acciones ============

    /**
     * Establece el tipo de daltonismo.
     */
    fun setColorblindType(type: ColorblindType) {
        val currentState = _state.value ?: return

        _state.value = currentState.copy(
            colorblindType = type,
            hasUnsavedChanges = hasChanges(type, currentState.highContrast, currentState.textScale)
        )

        // Notificar cambio
        val typeName = repository.getColorblindTypeName(type)
        val description = repository.getColorblindTypeDescription(type)
        _event.value = AccessibilityEvent.ConfigChanged(typeName, description)
    }

    /**
     * Desactiva el modo daltonismo.
     */
    fun disableColorblindMode() {
        setColorblindType(ColorblindType.NONE)
    }

    /**
     * Establece el alto contraste.
     */
    fun setHighContrast(enabled: Boolean) {
        val currentState = _state.value ?: return

        _state.value = currentState.copy(
            highContrast = enabled,
            hasUnsavedChanges = hasChanges(currentState.colorblindType, enabled, currentState.textScale)
        )
    }

    /**
     * Establece el texto grande.
     */
    fun setLargeText(enabled: Boolean) {
        val currentState = _state.value ?: return
        val newScale = if (enabled) TextScale.LARGE else TextScale.NORMAL

        _state.value = currentState.copy(
            textScale = newScale,
            hasUnsavedChanges = hasChanges(currentState.colorblindType, currentState.highContrast, newScale)
        )
    }

    /**
     * Guarda la configuración actual.
     */
    fun saveConfig() {
        val currentState = _state.value ?: return

        val config = AccessibilityConfig(
            colorblindType = currentState.colorblindType,
            highContrast = currentState.highContrast,
            textScale = currentState.textScale
        )

        repository.saveAccessibilityConfig(config)
        initialConfig = config

        _state.value = currentState.copy(hasUnsavedChanges = false)

        val typeName = repository.getColorblindTypeName(currentState.colorblindType)
        _event.value = AccessibilityEvent.ConfigSaved("✅ Configuración guardada. Modo: $typeName")
        _event.value = AccessibilityEvent.RestartRequired
    }

    /**
     * Aplica los colores inmediatamente (preview).
     */
    fun applyColorsPreview(activity: Activity) {
        val currentState = _state.value ?: return

        val config = AccessibilityConfig(
            colorblindType = currentState.colorblindType,
            highContrast = currentState.highContrast,
            textScale = currentState.textScale
        )

        AccesibilityHelper.applyAccessibilityConfig(activity, config)
    }

    /**
     * Reinicia la app para aplicar cambios de color.
     */
    fun restartApp(activity: Activity) {
        repository.restartAppForColorChanges(activity)
    }

    /**
     * Limpia el evento actual (después de ser consumido).
     */
    fun clearEvent() {
        _event.value = null
    }

    // ============ Helpers ============

    /**
     * Obtiene colores de previsualización para el tipo de daltonismo actual.
     */
    fun getPreviewColors(): List<Int> {
        val currentState = _state.value ?: return emptyList()
        
        return when (currentState.colorblindType) {
            ColorblindType.NONE, ColorblindType.NORMAL -> listOf(
                0xFFFF0000.toInt(), // Rojo
                0xFF00FF00.toInt(), // Verde
                0xFF0000FF.toInt(), // Azul
                0xFFFFFF00.toInt()  // Amarillo
            )
            ColorblindType.PROTANOPIA -> listOf(
                0xFFB8860B.toInt(), // Marrón
                0xFF00FF00.toInt(), // Verde
                0xFF0000FF.toInt(), // Azul
                0xFFFFFF00.toInt()  // Amarillo
            )
            ColorblindType.DEUTERANOPIA -> listOf(
                0xFFFF0000.toInt(), // Rojo
                0xFF8B0000.toInt(), // Rojo oscuro
                0xFF0000FF.toInt(), // Azul
                0xFFFF1493.toInt()  // Rosa magenta
            )
            ColorblindType.TRITANOPIA -> listOf(
                0xFFFF0000.toInt(), // Rojo
                0xFF00FF00.toInt(), // Verde
                0xFFFF1493.toInt(), // Rosa
                0xFFFF69B4.toInt()  // Rosa claro
            )
            ColorblindType.ACHROMATOPSIA -> listOf(
                0xFF666666.toInt(),
                0xFF999999.toInt(),
                0xFF333333.toInt(),
                0xFFCCCCCC.toInt()
            )
        }
    }

    /**
     * Verifica si hay cambios sin guardar.
     */
    fun hasUnsavedChanges(): Boolean = _state.value?.hasUnsavedChanges == true

    /**
     * Verifica si el botón de desactivar debe mostrarse.
     */
    fun shouldShowDisableButton(): Boolean {
        val type = _state.value?.colorblindType
        return type != ColorblindType.NONE && type != ColorblindType.NORMAL
    }

    private fun hasChanges(
        colorblindType: ColorblindType,
        highContrast: Boolean,
        textScale: TextScale
    ): Boolean {
        val initial = initialConfig ?: return false
        return colorblindType != initial.colorblindType ||
               highContrast != initial.highContrast ||
               textScale != initial.textScale
    }
}
