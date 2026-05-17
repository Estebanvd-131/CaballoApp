package com.villalobos.caballoapp

import android.content.Intent
import android.os.Bundle
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.villalobos.caballoapp.databinding.ActivityRegionTroncoBinding
import com.villalobos.caballoapp.HotspotHelper
import com.villalobos.caballoapp.AccesibilityHelper

class RegionTronco : AccessibilityActivity() {

    private lateinit var enlace: ActivityRegionTroncoBinding
    private lateinit var adaptadorMusculos: AdaptadorMusculos
    private var musculos: List<Musculo> = emptyList()
    private var regionId: Int = 3
    
    // Botón de quiz para la región
    private lateinit var btnQuizRegion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        enlace = ActivityRegionTroncoBinding.inflate(layoutInflater)
        setContentView(enlace.root)

        // Obtener el ID de la región del intent
        regionId = intent.getIntExtra("REGION_ID", 3)
        
        // Obtener los músculos de la región
        musculos = DatosMusculares.obtenerMusculosPorRegion(regionId)
        
        val region = DatosMusculares.obtenerRegionPorId(regionId)
        region?.let { enlace.tvTitle.text = it.nombreCompleto.uppercase() }

        // Animar la imagen de la región
        ImageAnimationHelper.animateRegionImage(enlace.imgRegion)

        configurarRecyclerView()
        configurarHotspots()
        configurarBotonesNavegacion()
    }

    private fun configurarRecyclerView() {
        adaptadorMusculos = AdaptadorMusculos(musculos) { musculo -> irADetalleMusculo(musculo) }
        enlace.rvMuscles.layoutManager = LinearLayoutManager(this)
        enlace.rvMuscles.adapter = adaptadorMusculos
    }

    private fun configurarHotspots() {
        enlace.imgRegion.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                enlace.imgRegion.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val container = enlace.imgRegion.parent as? RelativeLayout ?: return
                HotspotHelper.crearHotspots(
                    context = this@RegionTronco,
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
        // Configurar el botón de inicio
        setupHomeButton(enlace.btnHome)
        
        // Bindear botón de quiz
        btnQuizRegion = enlace.btnQuizRegion

        // Configurar listener del botón de quiz
        btnQuizRegion.setOnClickListener {
            irAQuizRegion()
        }
    }

    private fun irAQuizRegion() {
        val intent = Intent(this, QuizActivity::class.java)
        // Pasar el ID de la región específica (3 para Tronco)
        intent.putExtra("REGION_ID", 3)
        startActivity(intent)
    }
    
    override fun applyActivityAccessibilityColors() {
        // Aplicar colores de accesibilidad a elementos específicos de esta actividad
        AccesibilityHelper.applyAccessibilityColorsToApp(this)
    }
}