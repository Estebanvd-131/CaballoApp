package com.villalobos.caballoapp.ui.base

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.villalobos.caballoapp.ui.components.InteractiveAnatomyView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.villalobos.caballoapp.util.AccesibilityHelper
import com.villalobos.caballoapp.util.ErrorHandler
import com.villalobos.caballoapp.util.ImageAnimationHelper
import com.villalobos.caballoapp.data.model.Musculo
import com.villalobos.caballoapp.ui.region.AdaptadorMusculos
import com.villalobos.caballoapp.ui.region.RegionViewModel
import com.villalobos.caballoapp.ui.quiz.QuizActivity
import com.villalobos.caballoapp.ui.detail.DetalleMusculo
import dagger.hilt.android.AndroidEntryPoint

import android.widget.Toast
import com.villalobos.caballoapp.data.source.DatosMusculares
import com.villalobos.caballoapp.util.ProgressionManager

/**
 * Clase base abstracta para todas las actividades de región.
 * Proporciona funcionalidad común para mostrar músculos, hotspots y navegación.
 * Usa arquitectura MVVM con Hilt para inyección de dependencias.
 */
@AndroidEntryPoint
abstract class BaseRegionActivity : AccessibilityActivity() {

    // MVVM: ViewModel inyectado con Hilt
    protected val regionViewModel: RegionViewModel by viewModels()
    
    protected lateinit var adaptadorMusculos: AdaptadorMusculos
    // MVVM: ViewModel inyectado con Hilt (ya definido arriba)
    
    // protected lateinit var adaptadorMusculos: AdaptadorMusculos (ya definido)
    protected var musculos: List<Musculo> = emptyList()
    protected var zones: List<com.villalobos.caballoapp.data.model.Zona> = emptyList()
    protected var regionId: Int = 0

    // Métodos abstractos que deben implementar las subclases
    abstract fun getRegionImageView(): InteractiveAnatomyView
    abstract fun getTitleTextView(): TextView
    abstract fun getMusclesRecyclerView(): RecyclerView
    abstract fun getHomeButton(): ImageButton
    abstract fun getQuizButton(): Button
    abstract fun getDefaultRegionId(): Int
    abstract fun inflarLayout()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflarLayout()

        regionId = getDefaultRegionId()

        configurarBotonesNavegacion()
        observeViewModel()

        // Cargar datos de la región
        // Cargar datos de la región
        regionViewModel.loadRegion(regionId)
        
        // Configuración de legacy hotspots (TextView overrides)
        configurarHotspotsZonas()
    }

    /**
     * Método para configurar hotspots basados en TextViews (Legacy).
     * Las subclases pueden sobrescribir esto para añadir listeners a los TextViews del XML.
     */
    protected open fun configurarHotspotsZonas() {
        // Implementación por defecto vacía
    }

    protected open fun navegarADetalleZona(zonaId: Int) {
        // Buscar la zona DIRECTAMENTE en DatosMusculares (no en la lista asíncrona)
        val zonasDeRegion = DatosMusculares.obtenerSubZonasPorRegion(regionId)
        val zona = zonasDeRegion.find { it.id == zonaId }
        
        if (zona != null) {
            // Filtrar la lista de músculos para mostrar solo los de esta zona
            val musculosZona = zona.musculos
            if (musculosZona.isNotEmpty()) {
                // Actualizar el adaptador con los músculos de la zona
                if (::adaptadorMusculos.isInitialized) {
                    adaptadorMusculos.actualizarMusculos(musculosZona)
                }
                Toast.makeText(this, "Zona: ${zona.nombre}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Zona: ${zona.nombre} (Sin músculos)", Toast.LENGTH_SHORT).show()
            }
        } else {
            android.util.Log.w("BaseRegionActivity", "⚠️ Zona ID $zonaId no encontrada en región $regionId")
            Toast.makeText(this, "Zona no encontrada", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        // Recargar datos si es necesario o verificar estado
    }

    protected open fun observeViewModel() {
        // Observar estado
        regionViewModel.state.observe(this) { state ->
            state.region?.let { region ->
                getTitleTextView().text = region.nombreCompleto.uppercase()
            }

            // Actualizar si hay músculos O zonas
            if ((state.muscles.isNotEmpty() && state.muscles != musculos) || (state.zones.isNotEmpty() && state.zones != zones)) {
                musculos = state.muscles
                zones = state.zones
                configurarRecyclerView()
                
                // IMPORTANTE: Re-configurar hotspots cuando los datos se cargan
                android.util.Log.d("BaseRegionActivity", "📦 Datos cargados: ${musculos.size} músculos, ${zones.size} zonas")
                configurarHotspots()
                
                // Animar la imagen de la región
                ImageAnimationHelper.animateRegionImage(getRegionImageView())
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

        // Observar eventos (sin cambios)
        regionViewModel.event.observe(this) { event ->
            when (event) {
                is RegionViewModel.RegionEvent.NavigateToDetail -> {
                    irADetalleMusculo(event.muscle)
                    regionViewModel.clearEvent()
                }
                is RegionViewModel.RegionEvent.MuscleSelected -> {
                    // Opcional: animar o resaltar músculo seleccionado
                    regionViewModel.clearEvent()
                }
                is RegionViewModel.RegionEvent.Error -> {
                    ErrorHandler.handleError(
                        context = this,
                        throwable = Exception(event.message),
                        errorType = ErrorHandler.ErrorType.DATA_LOADING_ERROR,
                        userMessage = event.message,
                        canRecover = true
                    )
                    regionViewModel.clearEvent()
                }
                null -> { /* No action */ }
            }
        }
    }

    protected open fun configurarRecyclerView() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al configurar lista de músculos"
        ) {
            val recyclerView = getMusclesRecyclerView()

            if (!::adaptadorMusculos.isInitialized) {
                adaptadorMusculos = AdaptadorMusculos(musculos, regionId) { musculo ->
                    regionViewModel.navigateToMuscleDetail(musculo)
                }

                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = adaptadorMusculos
                recyclerView.setHasFixedSize(true)
                recyclerView.itemAnimator = null
            } else {
                adaptadorMusculos.actualizarMusculos(musculos)
            }

            // Aplicar configuración de accesibilidad visual
            aplicarAccesibilidadVisual()
        }
    }

    /**
     * Configura la detección táctil de zonas o músculos en la InteractiveAnatomyView.
     */
    protected open fun configurarHotspots() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al configurar hotspots"
        ) {
            // Prioridad: Si hay zonas, mostrar zonas. Si no, mostrar músculos.
            val itemsToShow: List<com.villalobos.caballoapp.data.model.HotspotItem> = if (zones.isNotEmpty()) {
                android.util.Log.d("BaseRegionActivity", "🔧 Configurando hotspots con ${zones.size} ZONAS")
                zones
            } else {
                android.util.Log.d("BaseRegionActivity", "🔧 Configurando hotspots con ${musculos.size} MÚSCULOS")
                musculos
            }

            if (itemsToShow.isEmpty()) {
                android.util.Log.w("BaseRegionActivity", "⚠️ Lista de hotspots vacía")
            }

            getRegionImageView().setHotspots(itemsToShow) { item ->
                if (item is com.villalobos.caballoapp.data.model.Zona) {
                    android.util.Log.d("BaseRegionActivity", "🎯 Zona seleccionada: ${item.nombre}")
                    navegarADetalleZona(item.id)
                } else if (item is Musculo) {
                    android.util.Log.d("BaseRegionActivity", "🎯 Músculo seleccionado: ${item.nombre}")
                    regionViewModel.navigateToMuscleDetail(item)
                }
            }
        }
    }

    protected open fun irADetalleMusculo(musculo: Musculo) {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.NAVIGATION_ERROR,
            errorMessage = "Error al navegar al detalle del músculo"
        ) {
            // Validar músculo antes de navegar
            if (!ErrorHandler.validarMusculo(musculo)) {
                throw IllegalArgumentException("Datos de músculo inválidos para navegación")
            }

            val intent = Intent(this, DetalleMusculo::class.java).apply {
                putExtra("MUSCULO_ID", musculo.id)
                putExtra("REGION_ID", regionId)
            }

            startActivity(intent)
        }
    }

    protected open fun configurarBotonesNavegacion() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al configurar botones de navegación"
        ) {
            // Configurar botón de home usando la clase base
            setupHomeButton(getHomeButton())

            // Configurar listener del botón de quiz
            getQuizButton().setOnClickListener {
                irAQuizRegion()
            }
        }
    }

    protected open fun irAQuizRegion() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.NAVIGATION_ERROR,
            errorMessage = "Error al navegar al quiz de la región"
        ) {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("REGION_ID", regionId)
            startActivity(intent)
        }
    }

    override fun applyActivityAccessibilityColors() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al aplicar colores de accesibilidad"
        ) {
            AccesibilityHelper.applyAccessibilityColorsToApp(this)
        }
    }



    protected open fun aplicarAccesibilidadVisual() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al aplicar accesibilidad visual"
        ) {
            // Configurar accesibilidad del dispositivo
            AccesibilityHelper.setupDeviceAccessibility(this)

            // Configurar descripciones específicas para la región
            configurarDescripcionesRegion()

            // Configurar imagen anatómica con descripción
            val regionName = regionViewModel.getRegionName()
            AccesibilityHelper.setAnatomicalImageDescription(
                getRegionImageView(),
                regionName,
                musculos.size
            )

            // Habilitar navegación por teclado
            AccesibilityHelper.enableKeyboardNavigation(
                getRegionImageView(),
                getMusclesRecyclerView()
            )
        }
    }

    protected open fun configurarDescripcionesRegion() {
        val regionName = regionViewModel.getRegionName()
        
        // Configurar título de la región
        AccesibilityHelper.setContentDescription(
            getTitleTextView(),
            "Título de región: $regionName",
            "Encabezado"
        )

        // Configurar lista de músculos
        AccesibilityHelper.setContentDescription(
            getMusclesRecyclerView(),
            "Lista de ${musculos.size} músculos en $regionName. " +
                    "Cada elemento muestra nombre, descripción y función del músculo",
            "Lista de músculos"
        )
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
