package com.villalobos.caballoapp.ui.base

import android.os.Bundle
import com.villalobos.caballoapp.util.AccesibilityHelper
import com.villalobos.caballoapp.util.ErrorHandler

/**
 * Clase base para actividades que aplican automáticamente la configuración de accesibilidad
 * Todas las actividades deberían extender esta clase o llamar a applyAccessibilityOnStart()
 */
abstract class AccessibilityActivity : BaseNavigationActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Aplicar configuración de accesibilidad al crear la actividad
        applyAccessibilityOnStart()
    }
    
    override fun onResume() {
        super.onResume()
        
        // Re-aplicar configuración al reanudar la actividad (por si cambió en otra actividad)
        applyAccessibilityOnResume()
    }
    
    /**
     * Aplica la configuración de accesibilidad al iniciar la actividad
     */
    private fun applyAccessibilityOnStart() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al aplicar configuración de accesibilidad al iniciar"
        ) {
            AccesibilityHelper.applyAccessibilityColorsToApp(this)
            // Aplicar colores específicos de la actividad
            applyActivityAccessibilityColors()
        }
    }
    
    /**
     * Aplica la configuración de accesibilidad al reanudar la actividad
     */
    private fun applyAccessibilityOnResume() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al aplicar configuración de accesibilidad al reanudar"
        ) {
            AccesibilityHelper.applyAccessibilityColorsToApp(this)
            // Aplicar colores específicos de la actividad
            applyActivityAccessibilityColors()
        }
    }
}
