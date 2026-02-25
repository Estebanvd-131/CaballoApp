package com.villalobos.caballoapp.ui.tutorial

import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.villalobos.caballoapp.R
import com.villalobos.caballoapp.ui.base.BaseNavigationActivity
import com.villalobos.caballoapp.util.ErrorHandler
import com.villalobos.caballoapp.util.AccesibilityHelper
import com.villalobos.caballoapp.databinding.ActivityTutorialBinding
import com.villalobos.caballoapp.ui.tutorial.TutorialViewModel
import com.villalobos.caballoapp.util.setOnSafeClickListener
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity para mostrar el tutorial de la aplicación.
 * Implementa MVVM delegando la lógica al TutorialViewModel.
 */
@AndroidEntryPoint
class TutorialActivity : BaseNavigationActivity() {

    private lateinit var enlace: ActivityTutorialBinding
    private lateinit var tutorialAdapter: TutorialAdapter
    private val viewModel: TutorialViewModel by viewModels()
    
    private val pasosTutorial = listOf(
        TutorialPaso(
            numero = 1,
            titulo = "¡Bienvenido a EquusApp!",
            descripcion = "Aprende anatomía muscular del Caballo Criollo Colombiano de forma interactiva, científica y completamente gratuita.",
            imagen = R.drawable.zona_parieto_temporal,
            mostrarCaracteristicas = true,
            caracteristicas = listOf(
                "Exploración por regiones anatómicas",
                "Información detallada de cada músculo",
                "Búsqueda rápida de músculos",
                "Funciona sin conexión a internet"
            )
        ),
        TutorialPaso(
            numero = 2,
            titulo = "Navegación Principal",
            descripcion = "Desde la pantalla principal puedes acceder a todas las funciones: explorar regiones, buscar músculos y ajustar accesibilidad.",
            imagen = R.drawable.zona_cervico_lateral
        ),
        TutorialPaso(
            numero = 3,
            titulo = "Exploración por Regiones",
            descripcion = "Selecciona una región anatómica (Cabeza, Cuello, Tronco, Miembros) para ver sus músculos específicos con imágenes detalladas.",
            imagen = R.drawable.musculo_interoseo
        ),
        TutorialPaso(
            numero = 4,
            titulo = "Interacción con Músculos",
            descripcion = "Toca cualquier punto destacado en las imágenes para ver información completa: origen, inserción y función biomecánica del músculo.",
            imagen = R.drawable.planta_casco
        ),
        TutorialPaso(
            numero = 5,
            titulo = "Funciones Avanzadas",
            descripcion = "Usa la búsqueda para encontrar músculos rápidamente y explora los detalles anatómicos de cada músculo.",
            imagen = R.drawable.group_1,
            mostrarCaracteristicas = true,
            caracteristicas = listOf(
                "Búsqueda por nombre de músculo",
                "Filtros por región anatómica",
                "Navegación detallada por músculos",
                "Hotspots interactivos en imágenes"
            )
        ),
        TutorialPaso(
            numero = 6,
            titulo = "¡Listo para Comenzar!",
            descripcion = "Ya conoces todas las funciones principales. Recuerda que puedes acceder a configuraciones de accesibilidad desde el menú principal si las necesitas.",
            imagen = R.drawable.zona_mandibular
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            enlace = ActivityTutorialBinding.inflate(layoutInflater)
            setContentView(enlace.root)

            // Inicializar ViewModel con el número de pasos
            viewModel.initialize(pasosTutorial.size)

            // Configurar ViewPager2
            configurarViewPager()
            
            // Configurar botones
            configurarBotones()
            
            // Observar estado del ViewModel
            observeViewModel()
            
            // Aplicar colores de accesibilidad
            applyActivityAccessibilityColors()
            
            // Configurar handler para botón de retroceso
            setupBackPressedHandler()
            
        } catch (e: Exception) {
            ErrorHandler.handleError(
                context = this,
                throwable = e,
                errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
                userMessage = "Error al inicializar tutorial",
                canRecover = true,
                recoveryAction = { viewModel.completeTutorial() }
            )
        }
    }

    private fun observeViewModel() {
        // Observar estado del tutorial
        viewModel.state.observe(this) { state ->
            actualizarInterfaz(state)
        }

        // Observar eventos
        viewModel.event.observe(this) { event ->
            when (event) {
                is TutorialViewModel.TutorialEvent.CompleteTutorial,
                is TutorialViewModel.TutorialEvent.SkipTutorial -> {
                    viewModel.clearEvent()
                    finish()
                }
                is TutorialViewModel.TutorialEvent.StepChanged -> {
                    enlace.viewPagerTutorial.currentItem = event.step
                    viewModel.clearEvent()
                }
                null -> { /* No event */ }
            }
        }
    }

    private fun configurarViewPager() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al configurar tutorial"
        ) {
            tutorialAdapter = TutorialAdapter(this, pasosTutorial)
            enlace.viewPagerTutorial.adapter = tutorialAdapter
            
            // Configurar listener para cambios de página
            enlace.viewPagerTutorial.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    viewModel.goToStep(position)
                }
            })
        }
    }

    private fun configurarBotones() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al configurar botones"
        ) {
            // Botón anterior con protección anti-doble clic
            enlace.btnAnterior.setOnSafeClickListener {
                viewModel.previousStep()
            }

            // Botón siguiente/finalizar con protección anti-doble clic
            enlace.btnSiguiente.setOnSafeClickListener {
                viewModel.nextStep()
            }

            // Botón saltar con protección anti-doble clic
            enlace.btnSaltarTutorial.setOnSafeClickListener {
                viewModel.skipTutorial()
            }

            // Checkbox no mostrar más
            enlace.cbNoMostrarMas.setOnCheckedChangeListener { _, isChecked ->
                viewModel.setNoMostrarAgain(isChecked)
            }
        }
    }

    private fun actualizarInterfaz(state: TutorialViewModel.TutorialState) {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al actualizar interfaz"
        ) {
            // Actualizar indicador de paso
            enlace.tvPasoActual.text = "${state.currentStep + 1}/${pasosTutorial.size}"
            
            // Actualizar barra de progreso
            enlace.progressBarTutorial.progress = state.currentStep + 1
            
            // Actualizar estado de botones
            enlace.btnAnterior.isEnabled = viewModel.canGoPrevious()
            
            // Cambiar texto del botón siguiente en el último paso
            if (state.isLastStep) {
                enlace.btnSiguiente.text = "¡Comenzar!"
            } else {
                enlace.btnSiguiente.text = "Siguiente →"
            }
        }
    }

    private fun setupBackPressedHandler() {
        onBackPressedDispatcher.addCallback(this, object : androidx.activity.OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Permitir retroceder en el tutorial o salir
                if (viewModel.canGoPrevious()) {
                    viewModel.previousStep()
                } else {
                    viewModel.completeTutorial()
                }
            }
        })
    }
    
    override fun applyActivityAccessibilityColors() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al aplicar colores de accesibilidad en TutorialActivity"
        ) {
            // Aplicar colores de accesibilidad a los elementos de la actividad
            AccesibilityHelper.applyAccessibilityColorsToApp(this)
        }
    }
}