package com.villalobos.caballoapp.data.model

data class Zona (
    override val id: Int,
    override val nombre: String, // El nombre de la zona, ej: "Zona parieto-temporal"
    val regionId: Int,
    override val hotspotX: Float, // Coordenadas para el mapa principal
    override val hotspotY: Float,
    val hotspotNumero: Int = 0,
    val descripcionCorta: String = "",
    val imagenMapa: String? = null,
    val musculos: List<Musculo> // Lista de los músculos que contiene
) : HotspotItem