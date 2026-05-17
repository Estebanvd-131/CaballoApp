package com.villalobos.caballoapp.util

import android.view.View

/**
 * Extensiones para prevenir múltiples clics rápidos en botones.
 * Evita que el usuario pueda presionar un botón múltiples veces
 * causando que se abran varias activities o se procesen múltiples acciones.
 */

/**
 * Establece un listener de clic con protección contra clics múltiples.
 * 
 * @param debounceTime Tiempo en milisegundos que el botón permanece deshabilitado
 *                     después de un clic. Por defecto 500ms.
 * @param action La acción a ejecutar cuando se hace clic.
 */
fun View.setOnSafeClickListener(
    debounceTime: Long = 500L,
    action: (View) -> Unit
) {
    setOnClickListener { view ->
        if (isClickable) {
            isClickable = false
            action(view)
            postDelayed({ isClickable = true }, debounceTime)
        }
    }
}

/**
 * Versión alternativa que usa un timestamp para prevenir clics rápidos.
 * Útil cuando no se quiere modificar el estado isClickable.
 */
private var lastClickTime: Long = 0

fun View.setOnThrottledClickListener(
    throttleTime: Long = 500L,
    action: (View) -> Unit
) {
    setOnClickListener { view ->
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime >= throttleTime) {
            lastClickTime = currentTime
            action(view)
        }
    }
}
