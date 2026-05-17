package com.villalobos.caballoapp

data class Region(
    val id: Int,
    val nombre: String,
    val nombreCompleto: String,
    val descripcion: String,
    val nombreImagen: String,
    val codigoColor: String,
    val orden: Int
)

// Enum para los tipos de región según los mockups
enum class TipoRegion(val id: Int, val nombreCompleto: String, val codigoColor: String, val nombreImagen: String) {
    CABEZA(1, "Región de la Cabeza", "#D4A574", "cabeza_lateral"),
    CUELLO(2, "Región del Cuello", "#B8956A", "cuello_y_torax"),
    TRONCO(3, "Región del Tronco", "#FFA500", "torsoequino"),
    MIEMBROS_TORACICOS(4, "Miembros Torácicos", "#C8A882", "hombro_miembro_anterior"),
    MIEMBROS_PELVICOS(5, "Región Pélvica", "#A0825C", "2- MusculoGluteoMedio.png")
} 