package com.villalobos.caballoapp.data.repository

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.villalobos.caballoapp.util.AccesibilityHelper
import com.villalobos.caballoapp.data.model.AccessibilityConfig
import com.villalobos.caballoapp.data.model.ColorblindType
import com.villalobos.caballoapp.data.model.TextScale
import com.villalobos.caballoapp.ui.main.MainActivity

/**
 * Repository para manejar configuración de accesibilidad.
 * Centraliza el acceso y persistencia de preferencias de accesibilidad.
 */
class AccessibilityRepository(private val context: Context) {

    companion object {
        private const val PREFS_NAME = "accessibility_prefs"
        private const val KEY_COLORBLIND_TYPE = "colorblind_type"
        private const val KEY_HIGH_CONTRAST = "high_contrast"
        private const val KEY_LARGE_TEXT = "large_text"
    }

    private val prefs by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // ============ Configuración ============

    /**
     * Obtiene la configuración actual de accesibilidad.
     */
    fun getAccessibilityConfig(): AccessibilityConfig {
        return AccesibilityHelper.getAccessibilityConfig(context)
    }

    /**
     * Guarda la configuración de accesibilidad.
     */
    fun saveAccessibilityConfig(config: AccessibilityConfig) {
        AccesibilityHelper.saveAccessibilityConfig(context, config)
    }

    /**
     * Obtiene el tipo de daltonismo configurado.
     */
    fun getColorblindType(): ColorblindType {
        val config = getAccessibilityConfig()
        return config.colorblindType
    }

    /**
     * Establece el tipo de daltonismo.
     */
    fun setColorblindType(type: ColorblindType) {
        val currentConfig = getAccessibilityConfig()
        val newConfig = currentConfig.copy(colorblindType = type)
        saveAccessibilityConfig(newConfig)
    }

    /**
     * Verifica si el alto contraste está habilitado.
     */
    fun isHighContrastEnabled(): Boolean {
        val config = getAccessibilityConfig()
        return config.highContrast
    }

    /**
     * Establece el alto contraste.
     */
    fun setHighContrast(enabled: Boolean) {
        val currentConfig = getAccessibilityConfig()
        val newConfig = currentConfig.copy(highContrast = enabled)
        saveAccessibilityConfig(newConfig)
    }

    /**
     * Verifica si el texto grande está habilitado.
     */
    fun isLargeTextEnabled(): Boolean {
        val config = getAccessibilityConfig()
        return config.textScale == TextScale.LARGE || 
               config.textScale == TextScale.EXTRA_LARGE
    }

    /**
     * Establece el texto grande.
     */
    fun setLargeText(enabled: Boolean) {
        val currentConfig = getAccessibilityConfig()
        val newScale = if (enabled) TextScale.LARGE else TextScale.NORMAL
        val newConfig = currentConfig.copy(textScale = newScale)
        saveAccessibilityConfig(newConfig)
    }

    // ============ Aplicación de colores ============

    /**
     * Aplica los colores de accesibilidad a una actividad.
     */
    fun applyAccessibilityColors(activity: Activity) {
        AccesibilityHelper.applyAccessibilityColorsToApp(activity)
    }

    /**
     * Restaura los colores originales.
     */
    fun restoreOriginalColors(activity: Activity) {
        AccesibilityHelper.restoreOriginalColors(activity)
    }

    /**
     * Reinicia la app para aplicar cambios de colores.
     */
    fun restartAppForColorChanges(activity: Activity) {
        AccesibilityHelper.restartAppForColorChanges(activity)
    }

    // ============ Utilidades ============

    /**
     * Obtiene el nombre legible del tipo de daltonismo.
     */
    fun getColorblindTypeName(type: ColorblindType): String {
        return when (type) {
            ColorblindType.NONE, ColorblindType.NORMAL -> "Colores estándar"
            ColorblindType.PROTANOPIA -> "Protanopia"
            ColorblindType.DEUTERANOPIA -> "Deuteranopia"
            ColorblindType.TRITANOPIA -> "Tritanopia"
            ColorblindType.ACHROMATOPSIA -> "Acromatopsia"
        }
    }

    /**
     * Obtiene la descripción del tipo de daltonismo.
     */
    fun getColorblindTypeDescription(type: ColorblindType): String {
        return when (type) {
            ColorblindType.NONE, ColorblindType.NORMAL -> "Visión normal de colores"
            ColorblindType.PROTANOPIA -> "Dificultad para distinguir rojos"
            ColorblindType.DEUTERANOPIA -> "Dificultad para distinguir verdes"
            ColorblindType.TRITANOPIA -> "Dificultad para distinguir azules y amarillos"
            ColorblindType.ACHROMATOPSIA -> "Visión en escala de grises"
        }
    }
}
