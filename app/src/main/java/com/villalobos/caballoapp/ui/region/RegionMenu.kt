package com.villalobos.caballoapp.ui.region

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.villalobos.caballoapp.R
import com.villalobos.caballoapp.ui.base.BaseNavigationActivity
import com.villalobos.caballoapp.util.ErrorHandler
import com.villalobos.caballoapp.util.AccesibilityHelper
import com.villalobos.caballoapp.databinding.ActivityRegionMenuBinding
import com.villalobos.caballoapp.data.model.TipoRegion
import com.villalobos.caballoapp.util.setOnSafeClickListener
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity del menú de regiones anatómicas.
 * Usa arquitectura MVVM con Hilt para inyección de dependencias.
 */
@AndroidEntryPoint
class RegionMenu : BaseNavigationActivity() {

    // MVVM: ViewModel inyectado con Hilt
    private val viewModel: RegionMenuViewModel by viewModels()
    
    private lateinit var binding: ActivityRegionMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityRegionMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeViewModel()
        
        // Configurar el botón de inicio
        setupHomeButton(binding.btnHome)
        
        // Aplicar colores de accesibilidad
        applyActivityAccessibilityColors()
    }

    private fun setupUI() {
        // Configurar listeners usando el ViewModel con protección anti-doble clic
        binding.btnRegionCabeza.setOnSafeClickListener {
            viewModel.navigateToCabeza()
        }
        
        binding.btnRegionCuello.setOnSafeClickListener {
            viewModel.navigateToCuello()
        }

        binding.btnRegionTronco.setOnSafeClickListener {
            viewModel.navigateToTronco()
        }
        
        binding.btnRegionToracica.setOnSafeClickListener {
            viewModel.navigateToToracica()
        }

        binding.btnRegionPelvica.setOnSafeClickListener {
            viewModel.navigateToPelvica()
        }

        binding.btnRegionDistal.setOnSafeClickListener {
            viewModel.navigateToDistal()
        }
    }

    private fun observeViewModel() {
        // Observar eventos de navegación
        viewModel.event.observe(this) { event ->
            when (event) {
                is RegionMenuViewModel.RegionMenuEvent.NavigateToRegion -> {
                    navigateToRegion(event.regionId, event.tipoRegion)
                    viewModel.clearEvent()
                }
                is RegionMenuViewModel.RegionMenuEvent.Error -> {
                    ErrorHandler.handleError(
                        context = this,
                        throwable = Exception(event.message),
                        errorType = ErrorHandler.ErrorType.NAVIGATION_ERROR,
                        userMessage = event.message,
                        canRecover = true
                    )
                    viewModel.clearEvent()
                }
                null -> { /* No action */ }
            }
        }
    }

    private fun navigateToRegion(regionId: Int, tipoRegion: TipoRegion) {
        val intent = when (tipoRegion) {
            TipoRegion.CABEZA -> Intent(this, RegionCabeza::class.java)
            TipoRegion.CUELLO -> Intent(this, RegionCuello::class.java)
            TipoRegion.TRONCO -> Intent(this, RegionTronco::class.java)
            TipoRegion.MIEMBROS_TORACICOS -> Intent(this, RegionToracica::class.java)
            TipoRegion.MIEMBROS_PELVICOS -> Intent(this, RegionPelvica::class.java)
            TipoRegion.REGION_DISTAL -> Intent(this, RegionDistal::class.java)
        }
        intent.putExtra("REGION_ID", regionId)
        startActivity(intent)
    }
    
    override fun applyActivityAccessibilityColors() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al aplicar colores de accesibilidad en RegionMenu"
        ) {
            AccesibilityHelper.applyAccessibilityColorsToApp(this)
        }
    }

    // Funciones legacy mantenidas por compatibilidad con XML onClick
    @Suppress("UNUSED_PARAMETER")
    fun btnRegionCabeza(view: View) {
        viewModel.navigateToCabeza()
    }
    
    @Suppress("UNUSED_PARAMETER")
    fun btnRegionCuello(view: View) {
        viewModel.navigateToCuello()
    }

    @Suppress("UNUSED_PARAMETER")
    fun btnRegionTronco(view: View) {
        viewModel.navigateToTronco()
    }
    
    @Suppress("UNUSED_PARAMETER")
    fun btnRegionToracica(view: View) {
        viewModel.navigateToToracica()
    }

    @Suppress("UNUSED_PARAMETER")
    fun btnRegionPelvica(view: View) {
        viewModel.navigateToPelvica()
    }
}