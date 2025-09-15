package com.villalobos.caballoapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.villalobos.caballoapp.databinding.ActivityRegionMenuBinding

class RegionMenu : BaseNavigationActivity() {

    lateinit var btnRegionCabeza: Button
    lateinit var btnRegionCuello: Button
    lateinit var btnRegionTronco: Button
    lateinit var btnRegionToracica: Button
    lateinit var btnRegionPelvica: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val enlace = ActivityRegionMenuBinding.inflate(layoutInflater)

        btnRegionCabeza = enlace.btnRegionCabeza
        btnRegionCuello = enlace.btnRegionCuello
        btnRegionTronco = enlace.btnRegionTronco
        btnRegionToracica = enlace.btnRegionToracica
        btnRegionPelvica = enlace.btnRegionPelvica

        // Configurar listeners para cada región
        btnRegionCabeza.setOnClickListener {
            irARegion(TipoRegion.CABEZA.id)
        }
        
        btnRegionCuello.setOnClickListener {
            irARegion(TipoRegion.CUELLO.id)
        }

        btnRegionTronco.setOnClickListener {
            irARegion(TipoRegion.TRONCO.id)
        }
        
        btnRegionToracica.setOnClickListener {
            irARegion(TipoRegion.MIEMBROS_TORACICOS.id)
        }

        btnRegionPelvica.setOnClickListener {
            irARegion(TipoRegion.MIEMBROS_PELVICOS.id)
        }


        setContentView(enlace.root)
        
        // Configurar el botón de inicio
        setupHomeButton(enlace.btnHome)
        
        // Aplicar colores de accesibilidad
        applyActivityAccessibilityColors()
    }
    
    override fun applyActivityAccessibilityColors() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al aplicar colores de accesibilidad en RegionMenu"
        ) {
            // Aplicar colores de accesibilidad a los elementos de la actividad
            AccesibilityHelper.applyAccessibilityColorsToApp(this)
        }
    }

    // Función para ir a la región seleccionada
    private fun irARegion(regionId: Int) {
        val intent = when (regionId) {
            1 -> Intent(this, RegionCabeza::class.java)
            2 -> Intent(this, RegionCuello::class.java)
            3 -> Intent(this, RegionTronco::class.java)
            4 -> Intent(this, RegionToracica::class.java)
            5 -> Intent(this, RegionPelvica::class.java)
            else -> return
        }
        
        // Pasar el ID de la región a la actividad
        intent.putExtra("REGION_ID", regionId)
        startActivity(intent)
    }

    // Funciones originales mantenidas por compatibilidad
    fun btnRegionCabeza(view: View) {
        irARegion(TipoRegion.CABEZA.id)
    }
    
    fun btnRegionCuello(view: View) {
        irARegion(TipoRegion.CUELLO.id)
    }

    fun btnRegionTronco(view: View) {
        irARegion(TipoRegion.TRONCO.id)
    }
    
    fun btnRegionToracica(view: View) {
        irARegion(TipoRegion.MIEMBROS_TORACICOS.id)
    }

    fun btnRegionPelvica(view: View) {
        irARegion(TipoRegion.MIEMBROS_PELVICOS.id)
    }
}