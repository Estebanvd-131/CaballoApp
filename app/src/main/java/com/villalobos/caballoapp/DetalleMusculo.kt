package com.villalobos.caballoapp

import android.os.Bundle
import com.villalobos.caballoapp.databinding.ActivityDetalleMusculoBinding

class DetalleMusculo : BaseNavigationActivity() {

    private lateinit var enlace: ActivityDetalleMusculoBinding
    private var musculo: Musculo? = null
    private var regionId: Int = 1

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

                // Configurar imagen específica del músculo
                configurarImagenMusculo()

                // Configurar información del músculo
                enlace.tvOrigenTexto.text =
                    musculoInfo.origen.ifBlank { "Información no disponible" }
                enlace.tvInsercionTexto.text =
                    musculoInfo.insercion.ifBlank { "Información no disponible" }
                enlace.tvFuncionTexto.text =
                    musculoInfo.funcion.ifBlank { "Información no disponible" }
            }
        }
    }

    private fun configurarImagenMusculo() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.IMAGE_LOADING_ERROR,
            errorMessage = "Error al cargar imagen del músculo"
        ) {
            musculo?.let { musculoInfo ->
                // Obtener el nombre de la imagen sin extensión (si trae .png o .jpg se elimina)
                val imageName = musculoInfo.imagen
                    ?.substringBeforeLast(".")
                    ?.lowercase()   // opcional, por si hay mayúsculas
                    ?: ""

                // Buscar el recurso en drawable
                val imageResource = resources.getIdentifier(
                    imageName,
                    "drawable",
                    packageName
                )

                if (imageResource != 0) {
                    enlace.imgMusculoDetalle.setImageResource(imageResource)
                } else {
                    // Si no existe, usar una imagen predeterminada
                    ErrorHandler.handleError(
                        context = this@DetalleMusculo,
                        throwable = Exception("Imagen no encontrada: ${musculoInfo.imagen}"),
                        errorType = ErrorHandler.ErrorType.IMAGE_LOADING_ERROR,
                        level = ErrorHandler.ErrorLevel.WARNING,
                        userMessage = "Imagen no disponible, mostrando predeterminada",
                        canRecover = true
                    )
                    enlace.imgMusculoDetalle.setImageResource(R.drawable.cabeza_lateral)
                }

                // Animar la imagen del músculo (si tienes animación)
                ImageAnimationHelper.animateMuscleDetailImage(enlace.imgMusculoDetalle)
            }
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
