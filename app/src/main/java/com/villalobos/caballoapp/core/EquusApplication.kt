package com.villalobos.caballoapp.core

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Clase Application principal de EquusApp.
 * Anotada con @HiltAndroidApp para habilitar la inyección de dependencias con Hilt.
 */
@HiltAndroidApp
class EquusApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // Inicialización global de la app si es necesario
    }
}
