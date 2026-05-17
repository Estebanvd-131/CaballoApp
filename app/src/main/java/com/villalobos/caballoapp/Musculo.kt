package com.villalobos.caballoapp

data class Musculo(
    val id: Int,
    val nombre: String,
    val origen: String,
    val insercion: String,
    val funcion: String,
    val regionId: Int,
    val hotspotX: Float = 0f,
    val hotspotY: Float = 0f,
    val hotspotNumero: Int = 0,
    val descripcion: String = "",
    val imagen: String? = null
)