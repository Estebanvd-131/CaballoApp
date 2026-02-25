package com.villalobos.caballoapp.data.model

/**
 * Interfaz para objetos que tienen un punto de interacción (hotspot) en una imagen.
 * Permite que InteractiveAnatomyView maneje tanto Músculos como Zonas de forma genérica.
 */
interface HotspotItem {
    val id: Int
    val nombre: String
    val hotspotX: Float
    val hotspotY: Float
}
