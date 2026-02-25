package com.villalobos.caballoapp.ui.region

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.villalobos.caballoapp.R
import com.villalobos.caballoapp.ui.base.BaseRegionActivity
import com.villalobos.caballoapp.databinding.ActivityRegionPelvicaBinding
import com.villalobos.caballoapp.ui.components.InteractiveAnatomyView
import com.villalobos.caballoapp.util.setOnSafeClickListener

class RegionPelvica : BaseRegionActivity() {

    private lateinit var enlace: ActivityRegionPelvicaBinding

    override fun inflarLayout() {
        enlace = ActivityRegionPelvicaBinding.inflate(layoutInflater)
        setContentView(enlace.root)
    }

    override fun getRegionImageView(): InteractiveAnatomyView = enlace.imgRegion
    override fun getTitleTextView(): TextView = enlace.tvTitle
    override fun getMusclesRecyclerView(): RecyclerView = enlace.rvMuscles
    override fun getHomeButton(): ImageButton = enlace.btnHome
    override fun getQuizButton(): Button = enlace.btnQuizRegion
    override fun getDefaultRegionId(): Int = 5

    override fun configurarHotspotsZonas() {
        val mapping = mapOf(
            R.id.hotspotZona5001 to 5001,
            R.id.hotspotZona5002 to 5002,
            R.id.hotspotZona5003 to 5003,
            R.id.hotspotZona5004 to 5004,
            R.id.hotspotZona5005 to 5005
        )

        mapping.forEach { (viewId, zonaId) ->
            findViewById<TextView>(viewId)?.setOnSafeClickListener {
                navegarADetalleZona(zonaId)
            }
        }
    }


}