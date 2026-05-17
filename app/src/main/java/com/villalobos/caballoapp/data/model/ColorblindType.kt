package com.villalobos.caballoapp.data.model

/**
 * Enum que representa los diferentes tipos de daltonismo soportados.
 */
enum class ColorblindType {
    NORMAL,         // Visión normal de colores
    PROTANOPIA,     // Dificultad para distinguir rojos
    DEUTERANOPIA,   // Dificultad para distinguir verdes
    TRITANOPIA,     // Dificultad para distinguir azules y amarillos
    ACHROMATOPSIA,  // Visión en escala de grises
    NONE;           // Alias para NORMAL
    
    companion object {
        fun fromString(value: String): ColorblindType {
            return when (value.uppercase()) {
                "NORMAL", "NONE" -> NORMAL
                "PROTANOPIA" -> PROTANOPIA
                "DEUTERANOPIA" -> DEUTERANOPIA
                "TRITANOPIA" -> TRITANOPIA
                "ACHROMATOPSIA" -> ACHROMATOPSIA
                else -> NORMAL
            }
        }
    }
}
