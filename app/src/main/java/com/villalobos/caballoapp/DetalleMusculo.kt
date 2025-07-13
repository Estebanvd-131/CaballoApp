package com.villalobos.caballoapp

import android.graphics.Matrix
import android.graphics.PointF
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.villalobos.caballoapp.databinding.ActivityDetalleMusculoBinding
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DetalleMusculo : AppCompatActivity() {

    private lateinit var enlace: ActivityDetalleMusculoBinding
    private var musculo: Musculo? = null
    private var regionId: Int = 1
    
    // Variables para zoom y paneo
    private var matriz = Matrix()
    private var savedMatrix = Matrix()
    private var mode = NONE
    private var start = PointF()
    private var mid = PointF()
    private var oldDist = 1f
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var isVistaEnfocada = false
    
    // Constantes para el manejo de gestos
    companion object {
        const val NONE = 0
        const val DRAG = 1
        const val ZOOM = 2
        const val ZOOM_FACTOR = 2.0f  // Zoom fijo único
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            enlace = ActivityDetalleMusculoBinding.inflate(layoutInflater)
            setContentView(enlace.root)

            // Obtener parámetros del intent con validación
            obtenerParametrosIntent()

            // Validar datos del músculo
            if (!ErrorHandler.validarMusculo(musculo)) {
                ErrorHandler.handleError(
                    context = this,
                    throwable = Exception("Datos de músculo inválidos"),
                    errorType = ErrorHandler.ErrorType.DATA_LOADING_ERROR,
                    userMessage = "Error al cargar información del músculo",
                    canRecover = true,
                    recoveryAction = { finish() }
                )
                return
            }

            // Configurar la interfaz
            configurarInterfaz()
            
            // Configurar zoom y paneo
            configurarZoomPaneo()
            
            // Configurar botones de alternancia
            configurarBotonesAlternancia()

            // Configurar botón volver
            enlace.btnVolver.setOnClickListener {
                finish()
            }
            
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

    private fun obtenerParametrosIntent() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.INTENT_ERROR,
            errorMessage = "Error al obtener parámetros de navegación"
        ) {
            val musculoId = intent.getIntExtra("MUSCULO_ID", 0)
            regionId = intent.getIntExtra("REGION_ID", 1)

            if (musculoId == 0) {
                throw IllegalArgumentException("ID de músculo inválido")
            }

            // Obtener información del músculo
            musculo = DatosMusculares.obtenerMusculoPorId(musculoId)
            
            if (musculo == null) {
                throw IllegalStateException("Músculo no encontrado con ID: $musculoId")
            }
        }
    }

    private fun configurarInterfaz() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.DATA_LOADING_ERROR,
            errorMessage = "Error al configurar interfaz de usuario"
        ) {
            musculo?.let { musculoInfo ->
                // Configurar título
                enlace.tvTituloMusculo.text = musculoInfo.nombre

                // Configurar imagen según la región
                configurarImagenRegion()

                // Configurar información del músculo
                enlace.tvOrigenTexto.text = musculoInfo.origen.ifBlank { "Información no disponible" }
                enlace.tvInsercionTexto.text = musculoInfo.insercion.ifBlank { "Información no disponible" }
                enlace.tvFuncionTexto.text = musculoInfo.funcion.ifBlank { "Información no disponible" }
            }
        }
    }
    
    private fun configurarImagenRegion() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.IMAGE_LOADING_ERROR,
            errorMessage = "Error al cargar imagen de la región"
        ) {
            val region = DatosMusculares.obtenerRegionPorId(regionId)
            
            if (!ErrorHandler.validarRegion(region)) {
                throw IllegalStateException("Región inválida con ID: $regionId")
            }
            
            region?.let { regionInfo ->
                val imageResource = when (regionInfo.nombreImagen) {
                    "cabeza_lateral" -> R.drawable.cabeza_lateral
                    "cuello_y_torax" -> R.drawable.cuello_y_torax
                    "torsoequino" -> R.drawable.torsoequino
                    "hombro_miembro_anterior" -> R.drawable.hombro_miembro_anterior
                    "grupa_miembro_posterior" -> R.drawable.grupa_miembro_posterior
                    else -> {
                        ErrorHandler.handleError(
                            context = this@DetalleMusculo,
                            throwable = Exception("Imagen no encontrada: ${regionInfo.nombreImagen}"),
                            errorType = ErrorHandler.ErrorType.IMAGE_LOADING_ERROR,
                            level = ErrorHandler.ErrorLevel.WARNING,
                            userMessage = "Usando imagen predeterminada",
                            canRecover = true
                        )
                        R.drawable.cabeza_lateral
                    }
                }
                enlace.imgMusculoDetalle.setImageResource(imageResource)
            }
        }
    }
    
    private fun configurarZoomPaneo() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al configurar funciones de zoom"
        ) {
            // Configurar ScaleGestureDetector para zoom con pellizco (zoom fijo)
            scaleGestureDetector = ScaleGestureDetector(this, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    return try {
                        if (isVistaEnfocada) {
                            // Aplicar zoom fijo sin escalas variables
                            matriz.reset()
                            matriz.postScale(ZOOM_FACTOR, ZOOM_FACTOR, detector.focusX, detector.focusY)
                            enlace.imgMusculoDetalle.imageMatrix = matriz
                        }
                        true
                    } catch (e: Exception) {
                        ErrorHandler.handleError(
                            context = this@DetalleMusculo,
                            throwable = e,
                            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
                            level = ErrorHandler.ErrorLevel.WARNING,
                            userMessage = "Error en zoom",
                            canRecover = true
                        )
                        false
                    }
                }
            })
            
            // Configurar touch listener para paneo
            enlace.imgMusculoDetalle.setOnTouchListener { _, event ->
                try {
                    when (event.action and MotionEvent.ACTION_MASK) {
                        MotionEvent.ACTION_DOWN -> {
                            savedMatrix.set(matriz)
                            start.set(event.x, event.y)
                            mode = DRAG
                        }
                        MotionEvent.ACTION_POINTER_DOWN -> {
                            oldDist = spacing(event)
                            if (oldDist > 10f) {
                                savedMatrix.set(matriz)
                                midPoint(mid, event)
                                mode = ZOOM
                            }
                        }
                        MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                            mode = NONE
                        }
                        MotionEvent.ACTION_MOVE -> {
                            if (isVistaEnfocada) {
                                when (mode) {
                                    DRAG -> {
                                        matriz.set(savedMatrix)
                                        matriz.postTranslate(event.x - start.x, event.y - start.y)
                                    }
                                    ZOOM -> {
                                        val newDist = spacing(event)
                                        if (newDist > 10f) {
                                            // Aplicar zoom fijo al punto medio
                                            matriz.reset()
                                            matriz.postScale(ZOOM_FACTOR, ZOOM_FACTOR, mid.x, mid.y)
                                        }
                                    }
                                }
                                enlace.imgMusculoDetalle.imageMatrix = matriz
                            }
                        }
                    }
                    
                    // Permitir que ScaleGestureDetector maneje también el evento
                    scaleGestureDetector?.onTouchEvent(event)
                    true
                } catch (e: Exception) {
                    ErrorHandler.handleError(
                        context = this@DetalleMusculo,
                        throwable = e,
                        errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
                        level = ErrorHandler.ErrorLevel.WARNING,
                        userMessage = "Error en gestos táctiles",
                        canRecover = true
                    )
                    false
                }
            }
        }
    }
    
    private fun configurarBotonesAlternancia() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al configurar botones de vista"
        ) {
            // Configurar botón vista regional
            enlace.btnVistaRegional.setOnClickListener {
                cambiarAVistaRegional()
            }
            
            // Configurar botón vista enfocada
            enlace.btnVistaEnfocada.setOnClickListener {
                cambiarAVistaEnfocada()
            }
            
            // Inicializar en vista regional
            cambiarAVistaRegional()
        }
    }
    
    private fun cambiarAVistaRegional() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al cambiar a vista regional"
        ) {
            isVistaEnfocada = false
            
            // Resetear transformaciones
            matriz.reset()
            enlace.imgMusculoDetalle.scaleType = ImageView.ScaleType.FIT_CENTER
            enlace.imgMusculoDetalle.imageMatrix = matriz
            
            // Actualizar estados de botones
            enlace.btnVistaRegional.backgroundTintList = getColorStateList(R.color.secondary_brown)
            enlace.btnVistaRegional.setTextColor(getColor(android.R.color.white))
            
            enlace.btnVistaEnfocada.backgroundTintList = getColorStateList(R.color.warm_cream)
            enlace.btnVistaEnfocada.setTextColor(getColor(R.color.primary_brown))
            
            // Actualizar indicador
            enlace.tvModoVista.text = "Vista Regional"
            
            // Configurar imagen regional normal
            configurarImagenRegion()
        }
    }
    
    private fun cambiarAVistaEnfocada() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al cambiar a vista enfocada"
        ) {
            isVistaEnfocada = true
            
            // Configurar para zoom
            enlace.imgMusculoDetalle.scaleType = ImageView.ScaleType.MATRIX
            
            // Hacer zoom inicial al área del músculo basado en sus coordenadas
            musculo?.let { musculoInfo ->
                inicializarZoomEnMusculo(musculoInfo)
            }
            
            // Actualizar estados de botones  
            enlace.btnVistaEnfocada.backgroundTintList = getColorStateList(R.color.secondary_brown)
            enlace.btnVistaEnfocada.setTextColor(getColor(android.R.color.white))
            
            enlace.btnVistaRegional.backgroundTintList = getColorStateList(R.color.warm_cream)
            enlace.btnVistaRegional.setTextColor(getColor(R.color.primary_brown))
            
            // Actualizar indicador
            enlace.tvModoVista.text = "Vista Enfocada - Pellizca para zoom"
        }
    }
    
    private fun inicializarZoomEnMusculo(musculoInfo: Musculo) {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al inicializar zoom en músculo"
        ) {
            // Aplicar zoom fijo único basado en las coordenadas del hotspot del músculo
            val centerX = enlace.imgMusculoDetalle.width * musculoInfo.hotspotX
            val centerY = enlace.imgMusculoDetalle.height * musculoInfo.hotspotY
            
            matriz.reset()
            matriz.postScale(ZOOM_FACTOR, ZOOM_FACTOR, centerX, centerY)
            
            // Centrar en el músculo
            val dx = enlace.imgMusculoDetalle.width / 2 - centerX * ZOOM_FACTOR
            val dy = enlace.imgMusculoDetalle.height / 2 - centerY * ZOOM_FACTOR
            matriz.postTranslate(dx, dy)
            
            enlace.imgMusculoDetalle.imageMatrix = matriz
        }
    }
    
    // Función auxiliar para calcular distancia entre dos puntos
    private fun spacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return kotlin.math.sqrt(x * x + y * y)
    }
    
    // Función auxiliar para encontrar punto medio
    private fun midPoint(point: PointF, event: MotionEvent) {
        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)
        point.set(x / 2, y / 2)
    }
    
    override fun onDestroy() {
        try {
            // Liberar recursos
            scaleGestureDetector = null
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