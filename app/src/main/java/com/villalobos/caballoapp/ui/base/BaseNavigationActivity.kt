package com.villalobos.caballoapp.ui.base

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.villalobos.caballoapp.util.AccesibilityHelper
import com.villalobos.caballoapp.util.ErrorHandler
import com.villalobos.caballoapp.data.model.ColorblindType
import com.villalobos.caballoapp.R
import com.villalobos.caballoapp.ui.main.MainActivity

/**
 * Clase base para actividades que necesitan navegación.
 * Proporciona un botón de inicio en la esquina superior derecha
 * que es adaptable a los modos de daltonismo.
 */
abstract class BaseNavigationActivity : AppCompatActivity() {

    private var btnHomeView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * Método para agregar la barra de navegación al layout de la actividad
     */
    protected fun setupNavigationBar() {
        // Este método debe ser llamado por las actividades hijas
        // en su método onCreate después de setContentView
        applyAccessibilityColors()
    }

    /**
     * Configura el botón de inicio en una vista específica.
     * Acepta tanto ImageButton como MaterialButton.
     */
    protected fun setupHomeButton(homeButton: View) {
        btnHomeView = homeButton
        btnHomeView?.setOnClickListener {
            goToMainActivity()
        }
        applyAccessibilityColors()
    }

    /**
     * Navega a la actividad principal
     */
    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    /**
     * Aplica los colores de accesibilidad al botón de inicio
     */
    private fun applyAccessibilityColors() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al aplicar colores de accesibilidad a la navegación"
        ) {
            // Aplicar colores adaptativos según el modo de daltonismo
            val config = AccesibilityHelper.getAccessibilityConfig(this)
            val colorTint = when (config.colorblindType) {
                ColorblindType.PROTANOPIA -> ContextCompat.getColor(this, R.color.protanopia_primary)
                ColorblindType.DEUTERANOPIA -> ContextCompat.getColor(this, R.color.deuteranopia_primary)
                ColorblindType.TRITANOPIA -> ContextCompat.getColor(this, R.color.tritanopia_primary)
                ColorblindType.ACHROMATOPSIA -> ContextCompat.getColor(this, R.color.achromatopsia_dark_gray)
                ColorblindType.NORMAL, ColorblindType.NONE -> ContextCompat.getColor(this, R.color.primary_brown)
            }
            
            // Aplicar según el tipo de vista
            when (val view = btnHomeView) {
                is ImageButton -> view.setColorFilter(colorTint)
                is MaterialButton -> view.setIconTint(android.content.res.ColorStateList.valueOf(colorTint))
            }
        }
    }

    /**
     * Método que las actividades hijas deben implementar para aplicar
     * colores de accesibilidad a sus elementos específicos
     */
    protected abstract fun applyActivityAccessibilityColors()
}