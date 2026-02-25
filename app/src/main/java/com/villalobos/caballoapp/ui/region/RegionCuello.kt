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
import com.villalobos.caballoapp.databinding.ActivityRegionCuelloBinding
import com.villalobos.caballoapp.ui.detail.DetalleMusculo
import com.villalobos.caballoapp.ui.components.InteractiveAnatomyView
import com.villalobos.caballoapp.util.setOnSafeClickListener

class RegionCuello : BaseRegionActivity() {

    private lateinit var enlace: ActivityRegionCuelloBinding

    override fun inflarLayout() {
        enlace = ActivityRegionCuelloBinding.inflate(layoutInflater)
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
    override fun getDefaultRegionId(): Int = 2

    override fun configurarHotspotsZonas() {
        val mapping = mapOf(
            R.id.hotspotZona2001 to 2001,
            R.id.hotspotZona2002 to 2002,
            R.id.hotspotZona2003 to 2003,
            R.id.hotspotZona2004 to 2004,
            R.id.hotspotZona2005 to 2005,
            R.id.hotspotZona2006 to 2006
        )

        mapping.forEach { (viewId, zonaId) ->
            findViewById<TextView>(viewId)?.setOnSafeClickListener {
                navegarADetalleZona(zonaId)
            }
        }
    }
}