package com.villalobos.caballoapp

import android.content.Intent
import android.os.Bundle
import android.view.ViewTreeObserver
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.villalobos.caballoapp.databinding.ActivityRegionPelvicaBinding
import com.villalobos.caballoapp.HotspotHelper

class RegionPelvica : AppCompatActivity() {

    private lateinit var enlace: ActivityRegionPelvicaBinding
    private lateinit var adaptadorMusculos: AdaptadorMusculos
    private var musculos: List<Musculo> = emptyList()
    private var regionId: Int = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enlace = ActivityRegionPelvicaBinding.inflate(layoutInflater)
        setContentView(enlace.root)

        regionId = intent.getIntExtra("REGION_ID", 5)
        musculos = DatosMusculares.obtenerMusculosPorRegion(regionId)
        DatosMusculares.obtenerRegionPorId(regionId)?.let { enlace.tvTitle.text = it.nombreCompleto.uppercase() }

        configurarRecyclerView()
        configurarHotspots()
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
                    context = this@RegionPelvica,
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
}