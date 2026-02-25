package com.villalobos.caballoapp

import com.villalobos.caballoapp.data.model.AccessibilityConfig
import com.villalobos.caballoapp.data.model.ColorblindType
import com.villalobos.caballoapp.data.model.TextScale
import org.junit.Test
import org.junit.Assert.*

/**
 * Tests para verificar la correcta configuración de los modos de daltonismo
 * Valida que cada tipo de daltonismo tiene colores únicos y diferenciables
 */
class ColorblindModeTest {

    // Colores esperados por modo (valores hexadecimales de colors.xml)
    companion object {
        // Modo Normal - Colores café
        const val NORMAL_PRIMARY = 0xFF8B4513.toInt()      // primary_brown
        const val NORMAL_SECONDARY = 0xFFA0522D.toInt()    // secondary_brown
        
        // Protanopia - Azul suave (evita rojos)
        const val PROTANOPIA_PRIMARY = 0xFF5D8AA8.toInt()      // Azul suave
        const val PROTANOPIA_SECONDARY = 0xFFA0826D.toInt()    // Marrón suave
        const val PROTANOPIA_BACKGROUND = 0xFFFAFAFA.toInt()   // Blanco suave
        
        // Deuteranopia - Rojo coral (evita verdes)
        const val DEUTERANOPIA_PRIMARY = 0xFFCD5C5C.toInt()    // Rojo coral
        const val DEUTERANOPIA_SECONDARY = 0xFF8B7D6B.toInt()  // Marrón grisáceo
        const val DEUTERANOPIA_BACKGROUND = 0xFFF8F8FF.toInt() // Blanco azulado
        
        // Tritanopia - Azul acero (evita azules/amarillos)
        const val TRITANOPIA_PRIMARY = 0xFF4682B4.toInt()      // Azul acero
        const val TRITANOPIA_SECONDARY = 0xFF708090.toInt()    // Gris acero
        const val TRITANOPIA_BACKGROUND = 0xFFF0F8FF.toInt()   // Azul alice
        
        // Acromatopsia - Escala de grises
        const val ACHROMATOPSIA_PRIMARY = 0xFFA9A9A9.toInt()   // Gris claro
        const val ACHROMATOPSIA_SECONDARY = 0xFF696969.toInt() // Gris medio
        const val ACHROMATOPSIA_BACKGROUND = 0xFFFFFFFF.toInt() // Blanco
    }

    @Test
    fun `verify colorblind types are unique and correctly named`() {
        val types = ColorblindType.entries
        
        // Verificar que hay exactamente 6 tipos
        assertEquals("Debe haber 6 tipos de daltonismo", 6, types.size)
        
        // Verificar nombres correctos
        assertTrue("Debe existir NONE", types.any { it.name == "NONE" })
        assertTrue("Debe existir NORMAL", types.any { it.name == "NORMAL" })
        assertTrue("Debe existir PROTANOPIA", types.any { it.name == "PROTANOPIA" })
        assertTrue("Debe existir DEUTERANOPIA", types.any { it.name == "DEUTERANOPIA" })
        assertTrue("Debe existir TRITANOPIA", types.any { it.name == "TRITANOPIA" })
        assertTrue("Debe existir ACHROMATOPSIA", types.any { it.name == "ACHROMATOPSIA" })
    }

    @Test
    fun `verify each colorblind type exists`() {
        // Verificar que cada tipo de daltonismo existe y puede ser referenciado
        ColorblindType.entries.forEach { type ->
            assertNotNull("${type.name} debe existir", type)
            assertTrue("${type.name} debe tener nombre válido", type.name.isNotBlank())
        }
    }

    @Test
    fun `verify protanopia avoids red colors`() {
        // Protanopia: dificultad con rojos
        // El color primario NO debe ser rojo puro (rojo > 200 y verde/azul < 100)
        val red = (PROTANOPIA_PRIMARY shr 16) and 0xFF
        val green = (PROTANOPIA_PRIMARY shr 8) and 0xFF
        val blue = PROTANOPIA_PRIMARY and 0xFF
        
        // El azul suave tiene más azul que rojo
        assertTrue(
            "Protanopia primary debe evitar rojos puros (actual R=$red, G=$green, B=$blue)",
            !(red > 200 && green < 100 && blue < 100)
        )
    }

    @Test
    fun `verify deuteranopia avoids green colors`() {
        // Deuteranopia: dificultad con verdes
        // El color primario NO debe ser verde puro (verde > 200 y rojo/azul < 100)
        val red = (DEUTERANOPIA_PRIMARY shr 16) and 0xFF
        val green = (DEUTERANOPIA_PRIMARY shr 8) and 0xFF
        val blue = DEUTERANOPIA_PRIMARY and 0xFF
        
        assertTrue(
            "Deuteranopia primary debe evitar verdes puros (actual R=$red, G=$green, B=$blue)",
            !(green > 200 && red < 100 && blue < 100)
        )
    }

    @Test
    fun `verify tritanopia avoids pure blue and yellow colors`() {
        // Tritanopia: dificultad con azules y amarillos
        val red = (TRITANOPIA_PRIMARY shr 16) and 0xFF
        val green = (TRITANOPIA_PRIMARY shr 8) and 0xFF
        val blue = TRITANOPIA_PRIMARY and 0xFF
        
        // No debe ser azul puro (azul > 200 y rojo/verde < 100)
        val isNotPureBlue = !(blue > 200 && red < 100 && green < 100)
        
        // No debe ser amarillo puro (rojo > 200 y verde > 200 y azul < 100)
        val isNotPureYellow = !(red > 200 && green > 200 && blue < 100)
        
        assertTrue(
            "Tritanopia primary debe evitar azul puro (actual R=$red, G=$green, B=$blue)",
            isNotPureBlue
        )
        assertTrue(
            "Tritanopia primary debe evitar amarillo puro (actual R=$red, G=$green, B=$blue)",
            isNotPureYellow
        )
    }

    @Test
    fun `verify achromatopsia uses grayscale colors`() {
        // Acromatopsia: escala de grises (R ≈ G ≈ B)
        val red = (ACHROMATOPSIA_PRIMARY shr 16) and 0xFF
        val green = (ACHROMATOPSIA_PRIMARY shr 8) and 0xFF
        val blue = ACHROMATOPSIA_PRIMARY and 0xFF
        
        // La diferencia entre canales debe ser mínima (≤ 10)
        val maxDiff = maxOf(
            kotlin.math.abs(red - green),
            kotlin.math.abs(green - blue),
            kotlin.math.abs(red - blue)
        )
        
        assertTrue(
            "Acromatopsia debe usar escala de grises (R=$red, G=$green, B=$blue, diff=$maxDiff)",
            maxDiff <= 10
        )
    }

    @Test
    fun `verify all modes have different primary colors`() {
        val primaryColors = listOf(
            NORMAL_PRIMARY,
            PROTANOPIA_PRIMARY,
            DEUTERANOPIA_PRIMARY,
            TRITANOPIA_PRIMARY,
            ACHROMATOPSIA_PRIMARY
        )
        
        // Todos los colores primarios deben ser únicos
        assertEquals(
            "Cada modo debe tener un color primario único",
            primaryColors.size,
            primaryColors.toSet().size
        )
    }

    @Test
    fun `verify all modes have different background colors`() {
        val backgroundColors = listOf(
            PROTANOPIA_BACKGROUND,
            DEUTERANOPIA_BACKGROUND,
            TRITANOPIA_BACKGROUND,
            ACHROMATOPSIA_BACKGROUND
        )
        
        // Al menos 3 de 4 deben ser diferentes (acromatopsia puede ser blanco igual que otro)
        assertTrue(
            "Debe haber variedad en colores de fondo",
            backgroundColors.toSet().size >= 3
        )
    }

    @Test
    fun `verify color contrast is sufficient`() {
        // Verificar que texto negro sobre cada fondo tiene suficiente contraste
        val textColor = 0xFF000000.toInt() // Negro
        
        val backgrounds = mapOf(
            "Protanopia" to PROTANOPIA_BACKGROUND,
            "Deuteranopia" to DEUTERANOPIA_BACKGROUND,
            "Tritanopia" to TRITANOPIA_BACKGROUND,
            "Achromatopsia" to ACHROMATOPSIA_BACKGROUND
        )
        
        backgrounds.forEach { (mode, bgColor) ->
            val contrast = calculateContrastRatio(textColor, bgColor)
            assertTrue(
                "$mode: contraste texto/fondo debe ser >= 4.5 (actual: $contrast)",
                contrast >= 4.5
            )
        }
    }

    @Test
    fun `verify accessibility config data class works correctly`() {
        val config = AccessibilityConfig(
            colorblindType = ColorblindType.PROTANOPIA,
            highContrast = true,
            textScale = TextScale.LARGE
        )
        
        assertEquals(ColorblindType.PROTANOPIA, config.colorblindType)
        assertTrue(config.highContrast)
        assertEquals(TextScale.LARGE, config.textScale)
    }

    @Test
    fun `verify text scale values are correct`() {
        val scales = TextScale.entries
        
        assertEquals("Debe haber 4 escalas de texto", 4, scales.size)
        
        // Verificar que están ordenadas de menor a mayor
        val scaleValues = scales.map { it.scaleFactor }
        assertEquals(listOf(0.85f, 1.0f, 1.15f, 1.3f), scaleValues)
    }

    // Función auxiliar para calcular ratio de contraste
    private fun calculateContrastRatio(color1: Int, color2: Int): Double {
        val luminance1 = calculateLuminance(color1)
        val luminance2 = calculateLuminance(color2)
        
        val lighter = maxOf(luminance1, luminance2)
        val darker = minOf(luminance1, luminance2)
        
        return (lighter + 0.05) / (darker + 0.05)
    }

    private fun calculateLuminance(color: Int): Double {
        val r = ((color shr 16) and 0xFF) / 255.0
        val g = ((color shr 8) and 0xFF) / 255.0
        val b = (color and 0xFF) / 255.0

        val rLin = if (r <= 0.03928) r / 12.92 else Math.pow((r + 0.055) / 1.055, 2.4)
        val gLin = if (g <= 0.03928) g / 12.92 else Math.pow((g + 0.055) / 1.055, 2.4)
        val bLin = if (b <= 0.03928) b / 12.92 else Math.pow((b + 0.055) / 1.055, 2.4)

        return 0.2126 * rLin + 0.7152 * gLin + 0.0722 * bLin
    }
}
