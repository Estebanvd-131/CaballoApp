package com.villalobos.caballoapp.data.model

/**
 * Data class que representa la configuración de accesibilidad del usuario.
 */
data class AccessibilityConfig(
    val colorblindType: ColorblindType = ColorblindType.NORMAL,
    val textScale: TextScale = TextScale.NORMAL,
    val highContrast: Boolean = false,
    val textScaleFactor: Float = 1.0f
) {
    constructor(colorblindType: ColorblindType, scaleFactor: Float) : this(
        colorblindType = colorblindType,
        textScale = TextScale.fromFloat(scaleFactor),
        textScaleFactor = scaleFactor
    )
}
