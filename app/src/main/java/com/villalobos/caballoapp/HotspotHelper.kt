package com.villalobos.caballoapp

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.view.setPadding

object HotspotHelper {

    /**
     * Crea botones invisibles sobre la imagen de región para cada músculo.
     * @param context Contexto para crear las vistas
     * @param container Relativo que contiene la ImageView (mismo tamaño)
     * @param imageView ImageView con la ilustración de la región
     * @param musculos Lista de músculos con coordenadas hotspotX/hotspotY (0..1)
     * @param onClick Callback al pulsar un músculo
     */
    fun crearHotspots(
        context: Context,
        container: RelativeLayout,
        imageView: ImageView,
        musculos: List<Musculo>,
        onClick: (Musculo) -> Unit
    ) {
        // Limpiar antiguos
        container.removeAllViews()
        container.addView(imageView)

        val ancho = imageView.width
        val alto = imageView.height

        // Tamaño base del hotspot (48dp)
        val sizePx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            48f,
            context.resources.displayMetrics
        ).toInt()

        musculos.forEach { musculo ->
            val btn = View(context).apply {
                id = View.generateViewId()
                // Hacer completamente invisible
                background = null
                setBackgroundColor(android.graphics.Color.TRANSPARENT)
                // Para accesibilidad
                contentDescription = "Hotspot ${musculo.nombre}"
                setOnClickListener { onClick(musculo) }
            }

            val params = RelativeLayout.LayoutParams(sizePx, sizePx)
            val x = (musculo.hotspotX * ancho - sizePx / 2).toInt()
            val y = (musculo.hotspotY * alto - sizePx / 2).toInt()
            params.leftMargin = x.coerceAtLeast(0)
            params.topMargin = y.coerceAtLeast(0)
            container.addView(btn, params)
        }
    }
} 