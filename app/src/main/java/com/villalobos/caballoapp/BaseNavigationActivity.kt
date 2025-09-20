package com.villalobos.caballoapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.villalobos.caballoapp.databinding.CustomNavigationBarBinding

/**
 * Clase base para actividades que necesitan navegación.
 * Proporciona un botón de inicio en la esquina superior derecha
 * que es adaptable a los modos de daltonismo.
 */
abstract class BaseNavigationActivity : AppCompatActivity() {

    private lateinit var navigationBinding: CustomNavigationBarBinding
    private lateinit var btnHome: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Inflar la barra de navegación personalizada
        navigationBinding = CustomNavigationBarBinding.inflate(layoutInflater)
        btnHome = navigationBinding.btnHome
        
        // Configurar el clic del botón de inicio
        btnHome.setOnClickListener {
            goToMainActivity()
        }
        
        // Aplicar colores de accesibilidad al botón
        applyAccessibilityColors()
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
     * Configura el botón de inicio en una vista específica
     */
    protected fun setupHomeButton(homeButton: ImageButton) {
        btnHome = homeButton
        btnHome.setOnClickListener {
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
                AccesibilityHelper.ColorblindType.PROTANOPIA -> ContextCompat.getColor(this, R.color.protanopia_primary)
                AccesibilityHelper.ColorblindType.DEUTERANOPIA -> ContextCompat.getColor(this, R.color.deuteranopia_primary)
                AccesibilityHelper.ColorblindType.TRITANOPIA -> ContextCompat.getColor(this, R.color.tritanopia_primary)
                AccesibilityHelper.ColorblindType.ACHROMATOPSIA -> ContextCompat.getColor(this, R.color.achromatopsia_dark_gray)
                else -> ContextCompat.getColor(this, R.color.primary_brown)
            }
            
            // Usar directamente el valor ARGB ya resuelto
            btnHome.setColorFilter(colorTint)
        }
    }

    /**
     * Método que las actividades hijas deben implementar para aplicar
     * colores de accesibilidad a sus elementos específicos
     */
    protected abstract fun applyActivityAccessibilityColors()
}