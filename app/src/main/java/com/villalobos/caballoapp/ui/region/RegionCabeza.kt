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
import com.villalobos.caballoapp.util.AccesibilityHelper
import com.villalobos.caballoapp.databinding.ActivityRegionCabezaBinding
import com.villalobos.caballoapp.ui.detail.DetalleMusculo
import com.villalobos.caballoapp.ui.components.InteractiveAnatomyView
import com.villalobos.caballoapp.util.ProgressionManager
import com.villalobos.caballoapp.util.setOnSafeClickListener

class RegionCabeza : BaseRegionActivity() {

    private lateinit var enlace: ActivityRegionCabezaBinding

    override fun inflarLayout() {
        enlace = ActivityRegionCabezaBinding.inflate(layoutInflater)
        setContentView(enlace.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // configurarHotspotsZonas() is called in BaseRegionActivity.onCreate
    }

    /**
     * Configura los listeners de las zonas táctiles (hotspots).
     * Cada hotspot abre el detalle de la zona correspondiente.
     * Usa setOnSafeClickListener para prevenir múltiples clics rápidos.
     */


    override fun getRegionImageView(): InteractiveAnatomyView = enlace.imgRegion
    override fun getTitleTextView(): TextView = enlace.tvTitle
    override fun getMusclesRecyclerView(): RecyclerView = enlace.rvMuscles
    override fun getHomeButton(): ImageButton = enlace.btnHome
    override fun getQuizButton(): Button = enlace.btnQuizRegion
    override fun getDefaultRegionId(): Int = 1

    override fun configurarHotspotsZonas() {
        val mapping = mapOf(
            R.id.hotspotZona1001 to 1001,
            R.id.hotspotZona1002 to 1002,
            R.id.hotspotZona1003 to 1003,
            R.id.hotspotZona1004 to 1004,
            R.id.hotspotZona1005 to 1005,
            R.id.hotspotZona1006 to 1006,
            R.id.hotspotZona1007 to 1007,
            R.id.hotspotZona1008 to 1008
        )

        mapping.forEach { (viewId, zonaId) ->
            findViewById<TextView>(viewId)?.setOnSafeClickListener {
                navegarADetalleZona(zonaId)
            }
        }
    }

    override fun configurarDescripcionesRegion() {
        super.configurarDescripcionesRegion()
        
        val region = DatosMusculares.obtenerRegionPorId(regionId)
        region?.let {
            // Configurar instrucciones adicionales específicas de la cabeza
            AccesibilityHelper.setContentDescription(
                enlace.tvInstructions,
                "Instrucciones: Selecciona un músculo de la lista para ver información detallada",
                "Instrucción"
            )

            AccesibilityHelper.setContentDescription(
                enlace.tvSubInstructions,
                "Sugerencia: Puedes navegar por los músculos usando gestos de deslizamiento o navegación por teclado",
                "Ayuda"
            )
        }
    }
}