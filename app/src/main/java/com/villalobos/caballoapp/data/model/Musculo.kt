package com.villalobos.caballoapp.data.model

data class Musculo(
    override val id: Int,
    override val nombre: String,
    val origen: String,
    val insercion: String,
    val funcion: String, // Contiene la Biomecánica
    val regionId: Int,
    override val hotspotX: Float = 0f,
    override val hotspotY: Float = 0f,
    val hotspotNumero: Int = 0,
    val descripcion: String = "",
    val imagen: String? = null // Imagen de detalle del músculo
) : HotspotItem