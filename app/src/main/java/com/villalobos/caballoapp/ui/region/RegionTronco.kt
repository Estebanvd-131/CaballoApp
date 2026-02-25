package com.villalobos.caballoapp.ui.region

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.villalobos.caballoapp.R
import com.villalobos.caballoapp.ui.base.BaseRegionActivity
import com.villalobos.caballoapp.data.source.DatosMusculares
import com.villalobos.caballoapp.util.ProgressionManager
import com.villalobos.caballoapp.databinding.ActivityRegionTroncoBinding
import com.villalobos.caballoapp.ui.detail.DetalleMusculo
import com.villalobos.caballoapp.ui.components.InteractiveAnatomyView
import com.villalobos.caballoapp.util.setOnSafeClickListener

class RegionTronco : BaseRegionActivity() {

    private lateinit var enlace: ActivityRegionTroncoBinding

    override fun inflarLayout() {
        enlace = ActivityRegionTroncoBinding.inflate(layoutInflater)
        setContentView(enlace.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // configurarHotspotsZonas() is called in BaseRegionActivity.onCreate
    }



    override fun getRegionImageView(): InteractiveAnatomyView = enlace.imgRegion
    override fun getTitleTextView(): TextView = enlace.tvTitle
    override fun getMusclesRecyclerView(): RecyclerView = enlace.rvMuscles
    override fun getHomeButton(): ImageButton = enlace.btnHome
    override fun getQuizButton(): Button = enlace.btnQuizRegion
    override fun getDefaultRegionId(): Int = 3

    override fun configurarHotspotsZonas() {
        val mapping = mapOf(
            R.id.hotspotZona3001 to 3001,
            R.id.hotspotZona3002 to 3002,
            R.id.hotspotZona3003 to 3003,
            R.id.hotspotZona3004 to 3004,
            R.id.hotspotZona3005 to 3005,
            R.id.hotspotZona3006 to 3006,
            R.id.hotspotZona3007 to 3007,
            R.id.hotspotZona3008 to 3008,
            R.id.hotspotZona3009 to 3009
        )

        mapping.forEach { (viewId, zonaId) ->
            findViewById<TextView>(viewId)?.setOnSafeClickListener {
                navegarADetalleZona(zonaId)
            }
        }
    }
}