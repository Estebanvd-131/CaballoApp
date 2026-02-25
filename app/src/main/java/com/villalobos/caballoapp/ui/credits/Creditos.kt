package com.villalobos.caballoapp.ui.credits

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.villalobos.caballoapp.R
import com.villalobos.caballoapp.ui.base.BaseNavigationActivity
import com.villalobos.caballoapp.util.ErrorHandler
import com.villalobos.caballoapp.util.AccesibilityHelper
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity para mostrar los créditos de la aplicación.
 * Implementa MVVM delegando la lógica al CreditsViewModel.
 */
@AndroidEntryPoint
class Creditos : BaseNavigationActivity() {

    private lateinit var btnBackCreditos: ImageButton
    private val viewModel: CreditsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_creditos)

        // Bind back button
        btnBackCreditos = findViewById(R.id.btnBackCreditos)
        btnBackCreditos.setOnClickListener {
            viewModel.navigateBack()
        }

        // Configurar el botón de inicio
        setupHomeButton(findViewById(R.id.btnHome))
        
        // Observar eventos del ViewModel
        observeViewModel()
        
        // Aplicar colores de accesibilidad
        applyActivityAccessibilityColors()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun observeViewModel() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is CreditsViewModel.CreditsEvent.NavigateBack -> {
                    viewModel.clearEvent()
                    finish()
                }
                null -> { /* No event */ }
            }
        }
    }
    
    override fun applyActivityAccessibilityColors() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al aplicar colores de accesibilidad en Creditos"
        ) {
            // Aplicar colores de accesibilidad a los elementos de la actividad
            AccesibilityHelper.applyAccessibilityColorsToApp(this)
        }
    }
}