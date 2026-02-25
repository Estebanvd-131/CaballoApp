package com.villalobos.caballoapp.data.model

data class Region(
    val id: Int,
    val nombre: String,
    val nombreCompleto: String,
    val descripcion: String,
    val nombreImagen: String,
    val codigoColor: String,
    val orden: Int
)

object RegionIds {
    const val CABEZA = 1
    const val CUELLO = 2
    const val TRONCO = 3
    const val MIEMBROS_TORACICOS = 4
    const val MIEMBROS_PELVICOS = 5
    const val REGION_DISTAL = 6
    const val REGION_DISTAL_LEGACY = 7

    val canonicalIds = listOf(
        CABEZA,
        CUELLO,
        TRONCO,
        MIEMBROS_TORACICOS,
        MIEMBROS_PELVICOS,
        REGION_DISTAL
    )

    fun normalize(regionId: Int): Int {
        return if (regionId == REGION_DISTAL_LEGACY) REGION_DISTAL else regionId
    }
}

// Enum para los tipos de región según los mockups
enum class TipoRegion(val id: Int, val nombreCompleto: String, val codigoColor: String, val nombreImagen: String) {
    CABEZA(1, "Región de la Cabeza", "#D4A574", "cabeza_lateral"),
    CUELLO(2, "Región del Cuello", "#B8956A", "cuello_y_torax"),
    TRONCO(3, "Región del Tronco", "#FFA500", "torsoequino"),
    MIEMBROS_TORACICOS(4, "Miembros Torácicos", "#C8A882", "hombro_miembro_anterior"),
    MIEMBROS_PELVICOS(5, "Región Pélvica", "#A0825C", "2- MusculoGluteoMedio.png"),
    REGION_DISTAL(6, "Región Distal (Casco)", "#8D6E63", "miembro_distal")
} 