package com.villalobos.caballoapp.ui.region

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.villalobos.caballoapp.R
import com.villalobos.caballoapp.ui.base.BaseRegionActivity
import com.villalobos.caballoapp.databinding.ActivityRegionDistalBinding
import com.villalobos.caballoapp.ui.components.InteractiveAnatomyView
import com.villalobos.caballoapp.util.setOnSafeClickListener

class RegionDistal : BaseRegionActivity() {

    private lateinit var enlace: ActivityRegionDistalBinding

    override fun inflarLayout() {
        enlace = ActivityRegionDistalBinding.inflate(layoutInflater)
        setContentView(enlace.root)
    }

    override fun getRegionImageView(): InteractiveAnatomyView = enlace.imgRegion
    override fun getTitleTextView(): TextView = enlace.tvTitle
    override fun getMusclesRecyclerView(): RecyclerView = enlace.rvMuscles
    override fun getHomeButton(): ImageButton = enlace.btnHome
    override fun getQuizButton(): Button = enlace.btnQuizRegion
    override fun getDefaultRegionId(): Int = 6

    override fun configurarHotspotsZonas() {
        val mapping = mapOf(
            R.id.hotspotZona6001 to 6001,
            R.id.hotspotZona6002 to 6002
        )

        mapping.forEach { (viewId, zonaId) ->
            findViewById<TextView>(viewId)?.setOnSafeClickListener {
                navegarADetalleZona(zonaId)
            }
        }
    }


}