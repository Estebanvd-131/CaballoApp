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
import com.villalobos.caballoapp.databinding.ActivityRegionToracicaBinding
import com.villalobos.caballoapp.ui.detail.DetalleMusculo
import com.villalobos.caballoapp.ui.components.InteractiveAnatomyView
import com.villalobos.caballoapp.util.setOnSafeClickListener

class RegionToracica : BaseRegionActivity() {

    private lateinit var enlace: ActivityRegionToracicaBinding

    override fun inflarLayout() {
        enlace = ActivityRegionToracicaBinding.inflate(layoutInflater)
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
    override fun getDefaultRegionId(): Int = 4

    override fun configurarHotspotsZonas() {
        val mapping = mapOf(
            R.id.hotspotZona4001 to 4001,
            R.id.hotspotZona4002 to 4002,
            R.id.hotspotZona4003 to 4003,
            R.id.hotspotZona4004 to 4004,
            R.id.hotspotZona4005 to 4005,
            R.id.hotspotZona4006 to 4006
        )

        mapping.forEach { (viewId, zonaId) ->
            findViewById<TextView>(viewId)?.setOnSafeClickListener {
                navegarADetalleZona(zonaId)
            }
        }
    }
}