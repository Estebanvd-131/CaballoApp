package com.villalobos.caballoapp.data.model

/**
 * Enum que representa las escalas de texto disponibles para accesibilidad.
 */
enum class TextScale(val scaleFactor: Float) {
    SMALL(0.85f),
    NORMAL(1.0f),
    LARGE(1.15f),
    EXTRA_LARGE(1.3f);
    
    companion object {
        fun fromFloat(value: Float): TextScale {
            return when {
                value <= 0.85f -> SMALL
                value <= 1.0f -> NORMAL
                value <= 1.15f -> LARGE
                else -> EXTRA_LARGE
            }
        }
    }
}
