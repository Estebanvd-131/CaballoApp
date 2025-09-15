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

class DetalleMusculo : BaseNavigationActivity() {

    private lateinit var enlace: ActivityDetalleMusculoBinding
    private var musculo: Musculo? = null
    private var regionId: Int = 1
    
    // Variables para zoom inteligente
    private var matriz = Matrix()
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var isZoomEnabled = false
    private var currentZoomLevel = 1.0f
    private var lastTouchX = 0f
    private var lastTouchY = 0f

    // Constantes para zoom
    companion object {
        const val MIN_ZOOM = 1.0f
        const val MAX_ZOOM = 3.0f
        const val ZOOM_STEP = 0.5f
        const val DOUBLE_TAP_ZOOM = 2.0f
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
            
            // Configurar zoom inteligente
            configurarZoomInteligente()

            // Configurar botón volver
            enlace.btnVolver.setOnClickListener {
                finish()
            }
            
            // Configurar el botón de inicio
            setupHomeButton(enlace.btnHome)
            
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

                // Animar la imagen del músculo
                ImageAnimationHelper.animateMuscleDetailImage(enlace.imgMusculoDetalle)
            }
        }
    }
    
    private fun configurarZoomInteligente() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al configurar zoom inteligente"
        ) {
            // Configurar la imagen para zoom
            enlace.imgMusculoDetalle.scaleType = ImageView.ScaleType.MATRIX

            // Configurar ScaleGestureDetector para zoom con pellizco
            scaleGestureDetector = ScaleGestureDetector(this, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    return try {
                        if (isZoomEnabled) {
                            val scaleFactor = detector.scaleFactor
                            val newZoom = (currentZoomLevel * scaleFactor).coerceIn(MIN_ZOOM, MAX_ZOOM)

                            if (newZoom != currentZoomLevel) {
                                currentZoomLevel = newZoom
                                aplicarZoom(currentZoomLevel, detector.focusX, detector.focusY)
                            }
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

            // Configurar touch listener para pan y double tap
            enlace.imgMusculoDetalle.setOnTouchListener { _, event ->
                try {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            lastTouchX = event.x
                            lastTouchY = event.y
                        }
                        MotionEvent.ACTION_MOVE -> {
                            if (isZoomEnabled && currentZoomLevel > MIN_ZOOM) {
                                // Pan the image
                                val dx = event.x - lastTouchX
                                val dy = event.y - lastTouchY

                                matriz.postTranslate(dx, dy)
                                enlace.imgMusculoDetalle.imageMatrix = matriz

                                lastTouchX = event.x
                                lastTouchY = event.y
                            }
                        }
                    }

                    // Handle double tap for zoom toggle
                    if (event.action == MotionEvent.ACTION_UP) {
                        // Simple double tap detection (you might want to use GestureDetector for better detection)
                        // For now, we'll use a simpler approach
                    }

                    // Allow ScaleGestureDetector to handle pinch gestures
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

            // Auto-zoom to muscle after image is loaded
            enlace.imgMusculoDetalle.viewTreeObserver.addOnGlobalLayoutListener(object : android.view.ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    enlace.imgMusculoDetalle.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    // Auto-focus on the muscle
                    centrarEnMusculo()
                }
            })
        }
    }
    
    private fun aplicarZoom(zoomLevel: Float, focusX: Float, focusY: Float) {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al aplicar zoom"
        ) {
            matriz.reset()
            matriz.postScale(zoomLevel, zoomLevel, focusX, focusY)
            enlace.imgMusculoDetalle.imageMatrix = matriz
        }
    }

    private fun centrarEnMusculo() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al centrar en músculo"
        ) {
            musculo?.let { musculoInfo ->
                // Calcular coordenadas del hotspot en píxeles
                val imageView = enlace.imgMusculoDetalle
                val drawable = imageView.drawable ?: return@let

                // Obtener dimensiones reales de la imagen
                val imageWidth = drawable.intrinsicWidth.toFloat()
                val imageHeight = drawable.intrinsicHeight.toFloat()

                // Calcular coordenadas del hotspot en la imagen
                val hotspotX = imageWidth * musculoInfo.hotspotX
                val hotspotY = imageHeight * musculoInfo.hotspotY

                // Calcular escala para centrar el hotspot
                val scaleX = imageView.width.toFloat() / imageWidth
                val scaleY = imageView.height.toFloat() / imageHeight
                val scale = minOf(scaleX, scaleY) * 1.5f // Zoom moderado

                // Limitar el zoom
                currentZoomLevel = scale.coerceIn(MIN_ZOOM, MAX_ZOOM)

                // Calcular traslación para centrar el hotspot
                val centerX = imageView.width / 2f
                val centerY = imageView.height / 2f

                val translateX = centerX - (hotspotX * currentZoomLevel)
                val translateY = centerY - (hotspotY * currentZoomLevel)

                // Aplicar transformación
                matriz.reset()
                matriz.postScale(currentZoomLevel, currentZoomLevel)
                matriz.postTranslate(translateX, translateY)
                imageView.imageMatrix = matriz

                // Habilitar zoom interactivo
                isZoomEnabled = true

                // Actualizar indicador
                enlace.tvModoVista.text = "Zoom Inteligente - Pellizca para ajustar"
            }
        }
    }
    
    override fun applyActivityAccessibilityColors() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al aplicar colores de accesibilidad en DetalleMusculo"
        ) {
            // Aplicar colores de accesibilidad a los elementos de la actividad
            AccesibilityHelper.applyAccessibilityColorsToApp(this)
        }
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