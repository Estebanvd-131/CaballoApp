package com.villalobos.caballoapp.ui.detail

import android.os.Bundle
import androidx.activity.viewModels
import com.villalobos.caballoapp.R
import com.villalobos.caballoapp.ui.base.BaseNavigationActivity
import com.villalobos.caballoapp.util.ErrorHandler
import com.villalobos.caballoapp.util.ImageAnimationHelper
import com.villalobos.caballoapp.util.AccesibilityHelper
import com.villalobos.caballoapp.util.DrawableNameResolver
import com.villalobos.caballoapp.databinding.ActivityDetalleMusculoBinding
import dagger.hilt.android.AndroidEntryPoint

import com.villalobos.caballoapp.data.source.DatosMusculares
import com.villalobos.caballoapp.util.ProgressionManager

/**
 * Activity para mostrar el detalle de un músculo.
 * Usa arquitectura MVVM con Hilt para inyección de dependencias.
 */
@AndroidEntryPoint
class DetalleMusculo : BaseNavigationActivity() {

    // MVVM: ViewModel inyectado con Hilt
    private val viewModel: DetalleMusculoViewModel by viewModels()
    
    private lateinit var binding: ActivityDetalleMusculoBinding
    private var regionId: Int = 1 // Default to 1
    private var imageOverrideFromZone: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            binding = ActivityDetalleMusculoBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // Obtener parámetros y cargar datos
            val musculoId = intent.getIntExtra("MUSCULO_ID", 0)
            regionId = intent.getIntExtra("REGION_ID", 1)
            imageOverrideFromZone = intent.getStringExtra("IMAGE_OVERRIDE")
            
            viewModel.loadMusculo(musculoId, regionId)

            setupUI()
            observeViewModel()

            // Configurar el botón de inicio
            setupHomeButton(binding.btnHome)

            // Aplicar colores de accesibilidad
            applyActivityAccessibilityColors()

        } catch (e: Exception) {
            ErrorHandler.handleError(
                context = this,
                throwable = e,
                errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
                userMessage = "Error al inicializar pantalla de detalle",
                canRecover = true,
                recoveryAction = { finish() }
            )
        }
    }

    private fun setupUI() {
        binding.btnVolver.setOnClickListener {
            viewModel.navigateBack()
        }
    }

    private fun observeViewModel() {
        // Observar estado
        viewModel.state.observe(this) { state ->
            if (state.hasValidData) {
                state.musculo?.let { musculo ->
                    // Configurar título
                    binding.tvTituloMusculo.text = musculo.nombre

                    // Configurar información del músculo
                    binding.tvOrigenTexto.text = viewModel.getOrigen()
                    binding.tvInsercionTexto.text = viewModel.getInsercion()
                    binding.tvFuncionTexto.text = viewModel.getFuncion()

                    // Configurar imagen
                    configurarImagenMusculo(imageOverrideFromZone ?: state.imageName)
                    
                    // Marcar como completado en el sistema de progresión
                    try {
                        val musclesInRegion = DatosMusculares.obtenerMusculosPorRegion(regionId)
                        val index = musclesInRegion.indexOfFirst { it.id == musculo.id }
                        ProgressionManager.markMuscleAsCompleted(this, regionId, musculo.id)
                        if (index != -1) {
                            ProgressionManager.markAsCompleted(this, regionId, index)
                        }
                    } catch (e: Exception) {
                        // Ignorar error en progresión para no afectar la experiencia principal
                        e.printStackTrace()
                    }
                }
            }

            state.error?.let { error ->
                ErrorHandler.handleError(
                    context = this,
                    throwable = Exception(error),
                    errorType = ErrorHandler.ErrorType.DATA_LOADING_ERROR,
                    userMessage = error,
                    canRecover = true,
                    recoveryAction = { finish() }
                )
            }
        }

        // Observar eventos
        viewModel.event.observe(this) { event ->
            when (event) {
                is DetalleMusculoViewModel.DetalleEvent.NavigateBack -> {
                    finish()
                    viewModel.clearEvent()
                }
                is DetalleMusculoViewModel.DetalleEvent.Error -> {
                    ErrorHandler.handleError(
                        context = this,
                        throwable = Exception(event.message),
                        errorType = ErrorHandler.ErrorType.DATA_LOADING_ERROR,
                        userMessage = event.message,
                        canRecover = true,
                        recoveryAction = { finish() }
                    )
                    viewModel.clearEvent()
                }
                is DetalleMusculoViewModel.DetalleEvent.ImageNotFound -> {
                    ErrorHandler.handleError(
                        context = this,
                        throwable = Exception("Imagen no encontrada: ${event.imageName}"),
                        errorType = ErrorHandler.ErrorType.IMAGE_LOADING_ERROR,
                        level = ErrorHandler.ErrorLevel.WARNING,
                        userMessage = "Imagen no disponible, mostrando predeterminada",
                        canRecover = true
                    )
                    viewModel.clearEvent()
                }
                null -> { /* No action */ }
            }
        }
    }

    private fun configurarImagenMusculo(imageName: String?) {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.IMAGE_LOADING_ERROR,
            errorMessage = "Error al cargar imagen del músculo"
        ) {
            val imageResource = DrawableNameResolver.resolve(imageName)

            if (imageResource != 0) {
                binding.imgMusculoDetalle.setImageResource(imageResource)
            } else {
                binding.imgMusculoDetalle.setImageResource(R.drawable.cabeza_lateral)
                viewModel.notifyImageNotFound()
            }

            // Animar la imagen del músculo
            ImageAnimationHelper.animateMuscleDetailImage(binding.imgMusculoDetalle)
        }
    }

    override fun applyActivityAccessibilityColors() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al aplicar colores de accesibilidad en DetalleMusculo"
        ) {
            AccesibilityHelper.applyAccessibilityColorsToApp(this)
        }
    }

    override fun onDestroy() {
        try {
            super.onDestroy()
        } catch (e: Exception) {
            ErrorHandler.handleError(
                context = this,
                throwable = e,
                errorType = ErrorHandler.ErrorType.MEMORY_ERROR,
                level = ErrorHandler.ErrorLevel.WARNING,
                userMessage = "Error al liberar recursos",
                canRecover = false
            )
        }
    }
}
