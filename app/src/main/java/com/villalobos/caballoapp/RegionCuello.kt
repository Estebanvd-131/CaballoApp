package com.villalobos.caballoapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.villalobos.caballoapp.databinding.ActivityRegionCuelloBinding
import com.villalobos.caballoapp.HotspotHelper

class RegionCuello : AccessibilityActivity() {

    private lateinit var enlace: ActivityRegionCuelloBinding
    private lateinit var adaptadorMusculos: AdaptadorMusculos
    private var musculos: List<Musculo> = emptyList()
    private var regionId: Int = 2
    
    // Botón de quiz para la región
    private lateinit var btnQuizRegion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        enlace = ActivityRegionCuelloBinding.inflate(layoutInflater)
        setContentView(enlace.root)

        // Obtener el ID de la región del intent
        regionId = intent.getIntExtra("REGION_ID", 2)
        
        // Obtener los músculos de la región
        musculos = DatosMusculares.obtenerMusculosPorRegion(regionId)
        
        // Configurar título dinámico
        val region = DatosMusculares.obtenerRegionPorId(regionId)
        region?.let {
            enlace.tvTitle.text = it.nombreCompleto.uppercase()
        }

        // Animar la imagen de la región
        ImageAnimationHelper.animateRegionImage(enlace.imgRegion)
        
        // Configurar RecyclerView
        configurarRecyclerView()
        
        // Configurar hotspots cuando la imagen esté lista
        configurarHotspots()

        // Configurar botones de navegación
        configurarBotonesNavegacion()
    }

    private fun configurarRecyclerView() {
        adaptadorMusculos = AdaptadorMusculos(musculos) { musculo ->
            irADetalleMusculo(musculo)
        }
        
        enlace.rvMuscles.layoutManager = LinearLayoutManager(this)
        enlace.rvMuscles.adapter = adaptadorMusculos
    }

    private fun configurarHotspots() {
        enlace.imgRegion.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                enlace.imgRegion.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val container = enlace.imgRegion.parent as? RelativeLayout ?: return
                HotspotHelper.crearHotspots(
                    context = this@RegionCuello,
                    container = container,
                    imageView = enlace.imgRegion,
                    musculos = musculos,
                    onClick = { musculo -> irADetalleMusculo(musculo) }
                )
            }
        })
    }

    private fun irADetalleMusculo(musculo: Musculo) {
        val intent = Intent(this, DetalleMusculo::class.java)
        intent.putExtra("MUSCULO_ID", musculo.id)
        intent.putExtra("REGION_ID", regionId)
        startActivity(intent)
    }

    private fun configurarBotonesNavegacion() {
        // Configurar botón de home usando la clase base
        setupHomeButton(enlace.btnHome)
        
        // Bindear botón de quiz
        btnQuizRegion = enlace.btnQuizRegion

        // Configurar listener del botón de quiz
        btnQuizRegion.setOnClickListener {
            irAQuizRegion()
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
        val intent = Intent(this, QuizActivity::class.java)
        // Pasar el ID de la región específica (2 para Cuello)
        intent.putExtra("REGION_ID", 2)
        startActivity(intent)
    }
}