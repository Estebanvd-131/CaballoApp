package com.villalobos.caballoapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.villalobos.caballoapp.databinding.ActivityMainBinding

//ventana de inicio que enlaza con todas las ventanas de la app
class MainActivity : AccessibilityActivity() {
    
    override fun applyActivityAccessibilityColors() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al aplicar colores de accesibilidad específicos de la actividad"
        ) {
            // Obtener la configuración actual de accesibilidad
            val config = AccesibilityHelper.getAccessibilityConfig(this)
            
            // Aplicar colores específicos para los elementos de la actividad principal
            AccesibilityHelper.applySpecificColorblindColors(this, window.decorView, config.colorblindType)
            
            // Aplicar gradiente de fondo
            AccesibilityHelper.applyBackgroundGradient(this, window.decorView, config.colorblindType)
        }
    }

    // Declaracion de variables
    lateinit var btnIniciar: Button
    lateinit var btnAccesibilidad: Button
    lateinit var btnCreditos: Button
    lateinit var btnSalir: Button
    
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Validacion del ViewBinding
        val enlace = ActivityMainBinding.inflate(layoutInflater)
        
        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("tutorial_prefs", MODE_PRIVATE)

        btnIniciar = enlace.btnIniciar
        btnAccesibilidad = enlace.btnAccesibilidad
        btnCreditos = enlace.btnCreditos
        btnSalir = enlace.btnSalir

        btnIniciar.setOnClickListener {
            btnIniciar(it)
        }

        btnAccesibilidad.setOnClickListener {
            btnAccesibilidad(it)
        }

        btnCreditos.setOnClickListener {
            btnCreditos(it)
        }

        btnSalir.setOnClickListener {
            btnSalir(it)
        }

        setContentView(enlace.root)
        
        // Aplicar configuración de accesibilidad al iniciar
        aplicarConfiguracionAccesibilidad()
        
        // Verificar si es primera vez y mostrar tutorial
        verificarPrimeraVez()
    }
    
    private fun aplicarConfiguracionAccesibilidad() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al aplicar configuración de accesibilidad"
        ) {
            // Aplicar colores de accesibilidad
            AccesibilityHelper.applyAccessibilityColorsToApp(this)
        }
    }
    
    private fun verificarPrimeraVez() {
        // Verificar si el usuario ha marcado la opción de no mostrar más el tutorial
        val noMostrarTutorial = sharedPreferences.getBoolean("no_mostrar_tutorial", false)
        
        // Si el usuario no ha marcado la opción de no mostrar más el tutorial, mostrarlo
        if (!noMostrarTutorial) {
            val intent = Intent(this, TutorialActivity::class.java)
            startActivity(intent)
        }
    }

    //funcionamiento de llamado de ventanas de la App
    // Intent para iniciar la actividad y pasar a la siguiente ventana
    fun btnIniciar(view: View) {
        val op = Intent(this, RegionMenu::class.java)
        startActivity(op)
    }

    fun btnAccesibilidad(view: View) {
        val op = Intent(this, Accesibilidad::class.java)
        startActivity(op)
    }

    fun btnCreditos(view: View) {
        val op = Intent(this, Creditos::class.java)
        startActivity(op)
    }

    fun btnSalir(view: View) {
        finishAffinity()
    }
}