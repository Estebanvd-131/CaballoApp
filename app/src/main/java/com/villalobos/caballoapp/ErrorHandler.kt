package com.villalobos.caballoapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

/**
 * Clase centralizada para manejo de errores y recuperación automática
 * Cumple con RNF-010 (Manejo correcto de errores) y RNF-011 (Recuperación automática)
 */
object ErrorHandler {
    
    public const val TAG = "CaballoApp_ErrorHandler"
    
    /**
     * Niveles de severidad de errores
     */
    enum class ErrorLevel {
        INFO,
        WARNING,
        ERROR,
        CRITICAL
    }
    
    /**
     * Tipos de errores de la aplicación
     */
    enum class ErrorType {
        NAVIGATION_ERROR,
        DATA_LOADING_ERROR,
        IMAGE_LOADING_ERROR,
        INTENT_ERROR,
        MEMORY_ERROR,
        UNKNOWN_ERROR
    }
    
    /**
     * Maneja errores de forma centralizada con recuperación automática
     */
    fun handleError(
        context: Context,
        throwable: Throwable?,
        errorType: ErrorType = ErrorType.UNKNOWN_ERROR,
        level: ErrorLevel = ErrorLevel.ERROR,
        userMessage: String? = null,
        canRecover: Boolean = true,
        recoveryAction: (() -> Unit)? = null
    ) {
        // Log del error para debugging
        val errorMessage = throwable?.message ?: "Error desconocido"
        when (level) {
            ErrorLevel.INFO -> Log.i(TAG, "$errorType: $errorMessage", throwable)
            ErrorLevel.WARNING -> Log.w(TAG, "$errorType: $errorMessage", throwable)
            ErrorLevel.ERROR -> Log.e(TAG, "$errorType: $errorMessage", throwable)
            ErrorLevel.CRITICAL -> Log.wtf(TAG, "$errorType: $errorMessage", throwable)
        }
        
        // Manejo de recuperación automática
        when (errorType) {
            ErrorType.DATA_LOADING_ERROR -> handleDataLoadingError(context, canRecover, recoveryAction)
            ErrorType.IMAGE_LOADING_ERROR -> handleImageLoadingError(context, canRecover, recoveryAction)
            ErrorType.NAVIGATION_ERROR -> handleNavigationError(context, canRecover)
            ErrorType.INTENT_ERROR -> handleIntentError(context, canRecover)
            ErrorType.MEMORY_ERROR -> handleMemoryError(context, canRecover)
            ErrorType.UNKNOWN_ERROR -> handleUnknownError(context, userMessage, canRecover, recoveryAction)
        }
    }
    
    /**
     * Recuperación automática para errores de carga de datos
     */
    private fun handleDataLoadingError(context: Context, canRecover: Boolean, recoveryAction: (() -> Unit)?) {
        if (canRecover && recoveryAction != null) {
            // Intentar recuperación automática
            try {
                recoveryAction.invoke()
                showToast(context, "Datos recargados exitosamente")
            } catch (e: Exception) {
                // Si falla la recuperación, mostrar error al usuario
                showErrorDialog(
                    context,
                    "Error de Datos",
                    "No se pudieron cargar los datos. La aplicación usará datos predeterminados.",
                    canRetry = false
                )
            }
        } else {
            showToast(context, "Error al cargar datos. Usando datos predeterminados.")
        }
    }
    
    /**
     * Recuperación automática para errores de carga de imágenes
     */
    private fun handleImageLoadingError(context: Context, canRecover: Boolean, recoveryAction: (() -> Unit)?) {
        if (canRecover && recoveryAction != null) {
            try {
                recoveryAction.invoke()
            } catch (e: Exception) {
                showToast(context, "Error al cargar imagen. Usando imagen predeterminada.")
            }
        } else {
            showToast(context, "Imagen no disponible")
        }
    }
    
    /**
     * Recuperación para errores de navegación
     */
    private fun handleNavigationError(context: Context, canRecover: Boolean) {
        if (canRecover && context is Activity) {
            // Regresar a la actividad principal
            try {
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
                context.finish()
            } catch (e: Exception) {
                showToast(context, "Error de navegación. Use el botón Volver.")
            }
        }
    }
    
    /**
     * Recuperación para errores de Intent
     */
    private fun handleIntentError(context: Context, canRecover: Boolean) {
        if (canRecover) {
            showErrorDialog(
                context,
                "Error de Navegación",
                "No se pudo abrir la pantalla solicitada. Será redirigido al menú principal.",
                canRetry = true
            ) {
                // Redirigir al menú principal
                if (context is Activity) {
                    val intent = Intent(context, RegionMenu::class.java)
                    context.startActivity(intent)
                    context.finish()
                }
            }
        }
    }
    
    /**
     * Manejo de errores de memoria
     */
    private fun handleMemoryError(context: Context, canRecover: Boolean) {
        // Forzar garbage collection
        System.gc()
        
        if (canRecover) {
            showToast(context, "Optimizando memoria...")
            // Liberar recursos no esenciales si es posible
        } else {
            showErrorDialog(
                context,
                "Memoria Insuficiente",
                "La aplicación necesita liberar memoria. Se reiniciará automáticamente.",
                canRetry = false
            ) {
                // Reiniciar aplicación
                if (context is Activity) {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                    context.finish()
                }
            }
        }
    }
    
    /**
     * Manejo de errores desconocidos
     */
    private fun handleUnknownError(context: Context, userMessage: String?, canRecover: Boolean, recoveryAction: (() -> Unit)?) {
        val message = userMessage ?: "Ha ocurrido un error inesperado"
        
        if (canRecover && recoveryAction != null) {
            showErrorDialog(
                context,
                "Error",
                "$message. ¿Desea intentar nuevamente?",
                canRetry = true
            ) {
                try {
                    recoveryAction.invoke()
                } catch (e: Exception) {
                    showToast(context, "No se pudo recuperar. Intente más tarde.")
                }
            }
        } else {
            showToast(context, message)
        }
    }
    
    /**
     * Muestra un Toast de forma segura
     */
    private fun showToast(context: Context, message: String) {
        try {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e(TAG, "Error mostrando Toast: ${e.message}")
        }
    }
    
    /**
     * Muestra un diálogo de error con opción de reintentar
     */
    private fun showErrorDialog(
        context: Context,
        title: String,
        message: String,
        canRetry: Boolean,
        retryAction: (() -> Unit)? = null
    ) {
        try {
            if (context is Activity && !context.isFinishing) {
                val builder = AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(message)
                    .setCancelable(false)
                
                if (canRetry && retryAction != null) {
                    builder.setPositiveButton("Reintentar") { _, _ ->
                        retryAction.invoke()
                    }
                    builder.setNegativeButton("Cancelar") { dialog, _ ->
                        dialog.dismiss()
                    }
                } else {
                    builder.setPositiveButton("Aceptar") { dialog, _ ->
                        dialog.dismiss()
                    }
                }
                
                builder.create().show()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error mostrando diálogo: ${e.message}")
            showToast(context, message)
        }
    }
    
    /**
     * Valida datos de músculo de forma segura
     */
    fun validarMusculo(musculo: Musculo?): Boolean {
        return try {
            musculo != null &&
            musculo.id > 0 &&
            musculo.nombre.isNotBlank() &&
            musculo.regionId > 0
        } catch (e: Exception) {
            Log.e(TAG, "Error validando músculo: ${e.message}")
            false
        }
    }
    
    /**
     * Valida datos de región de forma segura
     */
    fun validarRegion(region: Region?): Boolean {
        return try {
            region != null &&
            region.id > 0 &&
            region.nombre.isNotBlank() &&
            region.nombreImagen.isNotBlank()
        } catch (e: Exception) {
            Log.e(TAG, "Error validando región: ${e.message}")
            false
        }
    }
    
    /**
     * Ejecuta una operación de forma segura con manejo automático de errores
     */
    inline fun <T> safeExecute(
        context: Context,
        errorType: ErrorType = ErrorType.UNKNOWN_ERROR,
        errorMessage: String = "Error en operación",
        crossinline operation: () -> T
    ): T? {
        return try {
            operation()
        } catch (e: Exception) {
            // No mostrar mensajes de error para accesibilidad para no molestar al usuario
            if (!errorMessage.contains("accesibilidad", ignoreCase = true) &&
                !errorMessage.contains("accessibility", ignoreCase = true)) {
                handleError(
                    context = context,
                    throwable = e,
                    errorType = errorType,
                    level = ErrorLevel.ERROR,
                    userMessage = errorMessage,
                    canRecover = true
                )
            } else {
                // Solo registrar el error en el log sin mostrar al usuario
                Log.e(TAG, "Error de accesibilidad: ${e.message}")
            }
            null
        }
    }
} 