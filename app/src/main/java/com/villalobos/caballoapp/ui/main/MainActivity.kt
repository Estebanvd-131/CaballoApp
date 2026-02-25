package com.villalobos.caballoapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import com.villalobos.caballoapp.R
import com.villalobos.caballoapp.ui.base.AccessibilityActivity
import com.villalobos.caballoapp.util.AccesibilityHelper
import com.villalobos.caballoapp.util.ErrorHandler
import com.villalobos.caballoapp.ui.accessibility.Accesibilidad
import com.villalobos.caballoapp.ui.credits.Creditos
import com.villalobos.caballoapp.ui.region.RegionMenu
import com.villalobos.caballoapp.ui.tutorial.TutorialActivity
import com.villalobos.caballoapp.databinding.ActivityMainBinding
import com.villalobos.caballoapp.util.setOnSafeClickListener
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity principal que enlaza con todas las ventanas de la app.
 * Usa arquitectura MVVM con Hilt para inyección de dependencias.
 */
@AndroidEntryPoint
class MainActivity : AccessibilityActivity() {

    // MVVM: ViewModel inyectado con Hilt
    private val viewModel: MainViewModel by viewModels()
    
    private lateinit var binding: ActivityMainBinding
    
    override fun applyActivityAccessibilityColors() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al aplicar colores de accesibilidad específicos de la actividad"
        ) {
            AccesibilityHelper.applyAccessibilityColorsToApp(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeViewModel()
        aplicarConfiguracionAccesibilidad()
    }

    private fun setupUI() {
        // Configurar listeners usando el ViewModel con protección anti-doble clic
        binding.btnIniciar.setOnSafeClickListener {
            viewModel.navigateToRegionMenu()
        }

        binding.btnAccesibilidad.setOnSafeClickListener {
            viewModel.navigateToAccessibility()
        }

        binding.btnCreditos.setOnSafeClickListener {
            viewModel.navigateToCredits()
        }

        binding.btnSalir.setOnSafeClickListener {
            viewModel.exitApp()
        }
    }

    private fun observeViewModel() {
        // Observar eventos de navegación
        viewModel.event.observe(this) { event ->
            when (event) {
                is MainViewModel.MainEvent.NavigateToRegionMenu -> {
                    startActivity(Intent(this, RegionMenu::class.java))
                    viewModel.clearEvent()
                }
                is MainViewModel.MainEvent.NavigateToAccessibility -> {
                    startActivity(Intent(this, Accesibilidad::class.java))
                    viewModel.clearEvent()
                }
                is MainViewModel.MainEvent.NavigateToCredits -> {
                    startActivity(Intent(this, Creditos::class.java))
                    viewModel.clearEvent()
                }
                is MainViewModel.MainEvent.NavigateToTutorial -> {
                    startActivity(Intent(this, TutorialActivity::class.java))
                    viewModel.clearEvent()
                }
                is MainViewModel.MainEvent.ExitApp -> {
                    finishAffinity()
                }
                null -> { /* No action */ }
            }
        }

        // Observar si debe mostrar tutorial
        viewModel.shouldShowTutorial.observe(this) { show ->
            if (show) {
                viewModel.navigateToTutorial()
            }
        }

        // Observar estadísticas del usuario
        viewModel.userLevel.observe(this) { level ->
            binding.tvLevel.text = level.toString()
        }

        viewModel.userXp.observe(this) { xp ->
            binding.tvXp.text = xp.toString()
        }

        viewModel.userStreak.observe(this) { streak ->
            binding.tvStreak.text = "$streak días"
        }
    }
    
    private fun aplicarConfiguracionAccesibilidad() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al aplicar configuración de accesibilidad"
        ) {
            AccesibilityHelper.applyAccessibilityColorsToApp(this)
        }
    }

    // Funciones legacy mantenidas por compatibilidad con XML onClick
    @Suppress("UNUSED_PARAMETER")
    fun btnIniciar(view: View) {
        viewModel.navigateToRegionMenu()
    }

    @Suppress("UNUSED_PARAMETER")
    fun btnAccesibilidad(view: View) {
        viewModel.navigateToAccessibility()
    }

    @Suppress("UNUSED_PARAMETER")
    fun btnCreditos(view: View) {
        viewModel.navigateToCredits()
    }

    @Suppress("UNUSED_PARAMETER")
    fun btnSalir(view: View) {
        viewModel.exitApp()
    }
}