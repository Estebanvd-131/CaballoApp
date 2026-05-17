package com.villalobos.caballoapp.ui.accessibility

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.villalobos.caballoapp.R
import com.villalobos.caballoapp.ui.base.AccessibilityActivity
import com.villalobos.caballoapp.util.AccesibilityHelper
import com.villalobos.caballoapp.util.ErrorHandler
import com.villalobos.caballoapp.data.model.AccessibilityConfig
import com.villalobos.caballoapp.data.model.ColorblindType
import com.villalobos.caballoapp.data.model.TextScale
import com.villalobos.caballoapp.databinding.ActivityAccesibilidadBinding
import com.villalobos.caballoapp.ui.tutorial.TutorialActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity de configuración de accesibilidad.
 * Usa arquitectura MVVM con Hilt para inyección de dependencias.
 */
@AndroidEntryPoint
class Accesibilidad : AccessibilityActivity() {

    // MVVM: ViewModel inyectado con Hilt
    private val viewModel: AccessibilityViewModel by viewModels()
    
    private lateinit var binding: ActivityAccesibilidadBinding
    
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
        
        try {
            binding = ActivityAccesibilidadBinding.inflate(layoutInflater)
            setContentView(binding.root)
            
            setupUI()
            observeViewModel()
            
        } catch (e: Exception) {
            ErrorHandler.handleError(
                context = this,
                throwable = e,
                errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
                userMessage = "Error al inicializar configuración de accesibilidad",
                canRecover = true,
                recoveryAction = { finish() }
            )
        }
    }

    private fun setupUI() {
        // Los listeners individuales se han eliminado para evitar conflictos con el RadioGroup.
        // La lógica de selección se maneja completamente en setOnCheckedChangeListener.

        // RadioGroup listener para compatibilidad
        binding.rgModosDaltonismo.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId != -1) {
                val type = when (checkedId) {
                    R.id.rbNormal -> ColorblindType.NONE
                    R.id.rbProtanopia -> ColorblindType.PROTANOPIA
                    R.id.rbDeuteranopia -> ColorblindType.DEUTERANOPIA
                    R.id.rbTritanopia -> ColorblindType.TRITANOPIA
                    R.id.rbAcromatopsia -> ColorblindType.ACHROMATOPSIA
                    else -> ColorblindType.NONE
                }
                viewModel.setColorblindType(type)
                viewModel.applyColorsPreview(this)
            }
        }

        binding.switchHighContrast.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setHighContrast(isChecked)
            viewModel.applyColorsPreview(this)
        }

        binding.switchLargeText.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setLargeText(isChecked)
            viewModel.applyColorsPreview(this)
        }
        
        // Botón desactivar daltonismo
        binding.btnDesactivarDaltonismo.setOnClickListener {
            viewModel.disableColorblindMode()
            binding.rbNormal.isChecked = true
            viewModel.applyColorsPreview(this)
        }
        
        // Botones de acción
        binding.btnVolverAccesibilidad.setOnClickListener {
            finish()
        }
        
        binding.btnGuardarAccesibilidad.setOnClickListener {
            viewModel.saveConfig()
        }
        
        // Botón tutorial
        binding.btnReiniciarTutorial.setOnClickListener {
            startActivity(Intent(this, TutorialActivity::class.java))
        }
    }

    private fun observeViewModel() {
        // Observar estado
        viewModel.state.observe(this) { state ->
            // Actualizar RadioButtons
            when (state.colorblindType) {
                ColorblindType.NONE, ColorblindType.NORMAL -> binding.rbNormal.isChecked = true
                ColorblindType.PROTANOPIA -> binding.rbProtanopia.isChecked = true
                ColorblindType.DEUTERANOPIA -> binding.rbDeuteranopia.isChecked = true
                ColorblindType.TRITANOPIA -> binding.rbTritanopia.isChecked = true
                ColorblindType.ACHROMATOPSIA -> binding.rbAcromatopsia.isChecked = true
            }
            
            // Mostrar/ocultar botón de desactivar
            binding.btnDesactivarDaltonismo.visibility =
                if (state.colorblindType != ColorblindType.NONE && state.colorblindType != ColorblindType.NORMAL) View.VISIBLE
                else View.GONE

            if (binding.switchHighContrast.isChecked != state.highContrast) {
                binding.switchHighContrast.isChecked = state.highContrast
            }

            val isLargeText = state.textScale == TextScale.LARGE || state.textScale == TextScale.EXTRA_LARGE
            if (binding.switchLargeText.isChecked != isLargeText) {
                binding.switchLargeText.isChecked = isLargeText
            }

            // Actualizar vista previa de colores
            actualizarVistaPreviaColores()

            AccesibilityHelper.applyAccessibilityConfig(
                this,
                AccessibilityConfig(
                    colorblindType = state.colorblindType,
                    textScale = state.textScale,
                    highContrast = state.highContrast
                )
            )
        }

        // Observar eventos
        viewModel.event.observe(this) { event ->
            when (event) {
                is AccessibilityViewModel.AccessibilityEvent.ConfigSaved -> {
                    Toast.makeText(this, event.message, Toast.LENGTH_LONG).show()
                    viewModel.clearEvent()
                }
                is AccessibilityViewModel.AccessibilityEvent.ConfigChanged -> {
                    Toast.makeText(this, "${event.typeName}: ${event.description}", Toast.LENGTH_SHORT).show()
                    viewModel.clearEvent()
                }
                is AccessibilityViewModel.AccessibilityEvent.RestartRequired -> {
                    Handler(Looper.getMainLooper()).postDelayed({
                        viewModel.restartApp(this)
                    }, 1000)
                    viewModel.clearEvent()
                }
                is AccessibilityViewModel.AccessibilityEvent.Error -> {
                    Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show()
                    viewModel.clearEvent()
                }
                null -> { /* No action */ }
            }
        }
    }
    
    private fun actualizarVistaPreviaColores() {
        val colors = viewModel.getPreviewColors()
        if (colors.size >= 4) {
            binding.previewColor1.setBackgroundColor(colors[0])
            binding.previewColor2.setBackgroundColor(colors[1])
            binding.previewColor3.setBackgroundColor(colors[2])
            binding.previewColor4.setBackgroundColor(colors[3])
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
                userMessage = "Error al cerrar configuración",
                canRecover = false
            )
        }
    }
}