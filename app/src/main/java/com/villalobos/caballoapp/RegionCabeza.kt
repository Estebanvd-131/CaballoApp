package com.villalobos.caballoapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.villalobos.caballoapp.databinding.ActivityRegionCabezaBinding
import android.widget.RelativeLayout
import com.villalobos.caballoapp.HotspotHelper

class RegionCabeza : AccessibilityActivity() {

    private lateinit var enlace: ActivityRegionCabezaBinding
    private lateinit var adaptadorMusculos: AdaptadorMusculos
    private var musculos: List<Musculo> = emptyList()
    private var regionId: Int = 1
    
    // Botón de quiz para la región
    private lateinit var btnQuizRegion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            enlace = ActivityRegionCabezaBinding.inflate(layoutInflater)
            setContentView(enlace.root)

            // Obtener el ID de la región del intent con validación
            obtenerParametrosIntent()
            
            // Cargar datos de la región
            cargarDatosRegion()
            
            // Configurar RecyclerView
            configurarRecyclerView()
            
            // Configurar hotspots cuando la imagen esté lista
            configurarHotspots()

            // Configurar botones de navegación
            configurarBotonesNavegacion()

        } catch (e: Exception) {
            ErrorHandler.handleError(
                context = this,
                throwable = e,
                errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
                userMessage = "Error al inicializar la región",
                canRecover = true,
                recoveryAction = { finish() }
            )
        }
    }

    private fun obtenerParametrosIntent() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.INTENT_ERROR,
            errorMessage = "Error al obtener parámetros de navegación"
        ) {
            regionId = intent.getIntExtra("REGION_ID", 1)
            
            if (regionId <= 0) {
                throw IllegalArgumentException("ID de región inválido: $regionId")
            }
        }
    }

    private fun cargarDatosRegion() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.DATA_LOADING_ERROR,
            errorMessage = "Error al cargar datos de la región"
        ) {
            // Obtener los músculos de la región
            musculos = DatosMusculares.obtenerMusculosPorRegion(regionId)
            
            if (musculos.isEmpty()) {
                throw IllegalStateException("No se encontraron músculos para la región $regionId")
            }
            
            // Configurar título dinámico
            val region = DatosMusculares.obtenerRegionPorId(regionId)
            
            if (!ErrorHandler.validarRegion(region)) {
                throw IllegalStateException("Región inválida con ID: $regionId")
            }
            
            region?.let {
                enlace.tvTitle.text = it.nombreCompleto.uppercase()
            }

            // Animar la imagen de la región
            ImageAnimationHelper.animateRegionImage(enlace.imgRegion)
        }
    }

    private fun configurarRecyclerView() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al configurar lista de músculos"
        ) {
                    adaptadorMusculos = AdaptadorMusculos(musculos) { musculo ->
            irADetalleMusculo(musculo)
        }
        
        enlace.rvMuscles.layoutManager = LinearLayoutManager(this)
        enlace.rvMuscles.adapter = adaptadorMusculos
        
        // Aplicar configuración de accesibilidad visual (RF-007)
        aplicarAccesibilidadVisual()
        }
    }

    private fun configurarHotspots() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al configurar hotspots"
        ) {
            enlace.imgRegion.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    try {
                        enlace.imgRegion.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        // Crear hotspots dinámicos
                        val container = enlace.imgRegion.parent as? RelativeLayout ?: return
                        HotspotHelper.crearHotspots(
                            context = this@RegionCabeza,
                            container = container,
                            imageView = enlace.imgRegion,
                            musculos = musculos,
                            onClick = { musculo -> irADetalleMusculo(musculo) }
                        )
                    } catch (e: Exception) {
                        ErrorHandler.handleError(
                            context = this@RegionCabeza,
                            throwable = e,
                            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
                            level = ErrorHandler.ErrorLevel.WARNING,
                            userMessage = "Error en configuración visual",
                            canRecover = true
                        )
                    }
                }
            })
        }
    }

    private fun irADetalleMusculo(musculo: Musculo) {
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

    private fun configurarBotonesNavegacion() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al configurar botones de navegación"
        ) {
            // Configurar botón de home usando la clase base
            setupHomeButton(enlace.btnHome)
            
            // Bindear botón de quiz
            btnQuizRegion = enlace.btnQuizRegion

            // Configurar listener del botón de quiz
            btnQuizRegion.setOnClickListener {
                irAQuizRegion()
            }
        }
    }

    override fun applyActivityAccessibilityColors() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al aplicar colores de accesibilidad"
        ) {
            // Aplicar colores de accesibilidad a los elementos específicos de esta actividad
            AccesibilityHelper.applySpecificColorblindColors(
                this,
                enlace.root,
                AccesibilityHelper.getAccessibilityConfig(this).colorblindType
            )
        }
    }

    private fun irAQuizRegion() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.NAVIGATION_ERROR,
            errorMessage = "Error al navegar al quiz de la región"
        ) {
            val intent = Intent(this, QuizActivity::class.java)
            // Pasar el ID de la región específica (1 para Cabeza)
            intent.putExtra("REGION_ID", 1)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al reanudar actividad"
        ) {
            // Verificar que los datos sigan siendo válidos
            if (musculos.isEmpty()) {
                cargarDatosRegion()
                configurarRecyclerView()
            }
        }
    }

    private fun aplicarAccesibilidadVisual() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al aplicar accesibilidad visual"
        ) {
            // Configurar accesibilidad del dispositivo
            AccesibilityHelper.setupDeviceAccessibility(this, enlace.root)
            
            // Configurar descripciones específicas para la región
            configurarDescripcionesRegion()
            
            // Configurar imagen anatómica con descripción
            val region = DatosMusculares.obtenerRegionPorId(regionId)
            region?.let {
                AccesibilityHelper.setAnatomicalImageDescription(
                    enlace.imgRegion,
                    it.nombreCompleto,
                    musculos.size
                )
            }
            
            // Habilitar navegación por teclado
            AccesibilityHelper.enableKeyboardNavigation(
                enlace.imgRegion,
                enlace.rvMuscles
            )
        }
    }

    private fun configurarDescripcionesRegion() {
        val region = DatosMusculares.obtenerRegionPorId(regionId)
        region?.let {
            // Configurar título de la región
            AccesibilityHelper.setContentDescription(
                enlace.tvTitle,
                "Título de región: ${it.nombreCompleto}",
                "Encabezado"
            )
            
            // Configurar instrucciones
            AccesibilityHelper.setContentDescription(
                enlace.tvInstructions,
                "Instrucciones: Selecciona un músculo de la lista para ver información detallada",
                "Instrucción"
            )
            
            AccesibilityHelper.setContentDescription(
                enlace.tvSubInstructions,
                "Sugerencia: Puedes navegar por los músculos usando gestos de deslizamiento o navegación por teclado",
                "Ayuda"
            )
            
            // Configurar lista de músculos
            AccesibilityHelper.setContentDescription(
                enlace.rvMuscles,
                "Lista de ${musculos.size} músculos en ${it.nombreCompleto}. " +
                "Cada elemento muestra nombre, descripción y función del músculo",
                "Lista de músculos"
            )
        }
    }

    override fun onDestroy() {
        try {
            // Liberar recursos del adaptador si es necesario
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