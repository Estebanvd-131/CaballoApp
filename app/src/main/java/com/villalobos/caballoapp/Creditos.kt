package com.villalobos.caballoapp

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Creditos : BaseNavigationActivity() {

    private lateinit var btnBackCreditos: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_creditos)

        // Bind back button
        btnBackCreditos = findViewById(R.id.btnBackCreditos)
        btnBackCreditos.setOnClickListener {
            finish()
        }

        // Configurar el botón de inicio
        setupHomeButton(findViewById(R.id.btnHome))
        
        // Aplicar colores de accesibilidad
        applyActivityAccessibilityColors()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    
    override fun applyActivityAccessibilityColors() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al aplicar colores de accesibilidad en Creditos"
        ) {
            // Aplicar colores de accesibilidad a los elementos de la actividad
            AccesibilityHelper.applyAccessibilityColorsToApp(this)
        }
    }
}