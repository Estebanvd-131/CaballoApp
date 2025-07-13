package com.villalobos.caballoapp

import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat

/**
 * Clase de utilidad para soporte de accesibilidad y daltonismo
 * Cumple con RF-006: Soporte para daltonismo con paleta accesible y alternativas textuales
 */
object AccesibilityHelper {

    private const val PREFS_NAME = "accessibility_prefs"
    private const val KEY_COLORBLIND_TYPE = "colorblind_type"
    private const val KEY_HIGH_CONTRAST = "high_contrast"
    private const val KEY_TEXT_INDICATORS = "text_indicators"
    private const val KEY_TEXT_SCALE = "text_scale"
    private const val KEY_SCREEN_READER_ENABLED = "screen_reader_enabled"
    private const val KEY_ALTERNATIVE_DESCRIPTIONS = "alternative_descriptions"

    /**
     * Tipos de daltonismo soportados
     */
    enum class ColorblindType(val displayName: String, val description: String) {
        NONE("Normal", "Visión normal de colores"),
        PROTANOPIA("Protanopia", "Dificultad para ver rojos"),
        DEUTERANOPIA("Deuteranopia", "Dificultad para ver verdes"),
        TRITANOPIA("Tritanopia", "Dificultad para ver azules"),
        ACHROMATOPSIA("Acromatopsia", "Visión en escala de grises")
    }

    /**
     * Escalas de texto disponibles para deficiencias visuales
     */
    enum class TextScale(val displayName: String, val scale: Float) {
        SMALL("Pequeño", 0.85f),
        NORMAL("Normal", 1.0f),
        LARGE("Grande", 1.15f),
        EXTRA_LARGE("Extra Grande", 1.3f),
        HUGE("Enorme", 1.5f)
    }

    /**
     * Configuración de accesibilidad
     */
    data class AccessibilityConfig(
        val colorblindType: ColorblindType = ColorblindType.NONE,
        val highContrast: Boolean = false,
        val showTextIndicators: Boolean = false,
        val textScale: TextScale = TextScale.NORMAL,
        val screenReaderEnabled: Boolean = false,
        val alternativeDescriptions: Boolean = true
    )

    /**
     * Obtiene la configuración actual de accesibilidad
     */
    fun getAccessibilityConfig(context: Context): AccessibilityConfig {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val colorblindTypeOrdinal = prefs.getInt(KEY_COLORBLIND_TYPE, 0)
        val highContrast = prefs.getBoolean(KEY_HIGH_CONTRAST, false)
        val textIndicators = prefs.getBoolean(KEY_TEXT_INDICATORS, false)
        val textScaleOrdinal = prefs.getInt(KEY_TEXT_SCALE, 1) // Default to NORMAL
        val screenReaderEnabled = prefs.getBoolean(KEY_SCREEN_READER_ENABLED, false)
        val alternativeDescriptions = prefs.getBoolean(KEY_ALTERNATIVE_DESCRIPTIONS, true)

        val colorblindType = ColorblindType.values().getOrElse(colorblindTypeOrdinal) { 
            ColorblindType.NONE 
        }
        
        val textScale = TextScale.values().getOrElse(textScaleOrdinal) {
            TextScale.NORMAL
        }

        return AccessibilityConfig(
            colorblindType = colorblindType,
            highContrast = highContrast,
            showTextIndicators = textIndicators,
            textScale = textScale,
            screenReaderEnabled = screenReaderEnabled,
            alternativeDescriptions = alternativeDescriptions
        )
    }

    /**
     * Guarda la configuración de accesibilidad
     */
    fun saveAccessibilityConfig(context: Context, config: AccessibilityConfig) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putInt(KEY_COLORBLIND_TYPE, config.colorblindType.ordinal)
            putBoolean(KEY_HIGH_CONTRAST, config.highContrast)
            putBoolean(KEY_TEXT_INDICATORS, config.showTextIndicators)
            putInt(KEY_TEXT_SCALE, config.textScale.ordinal)
            putBoolean(KEY_SCREEN_READER_ENABLED, config.screenReaderEnabled)
            putBoolean(KEY_ALTERNATIVE_DESCRIPTIONS, config.alternativeDescriptions)
            apply()
        }
    }

    /**
     * Aplica configuración de accesibilidad a una vista
     */
    fun applyAccessibilityToView(context: Context, view: View, contentType: ContentType = ContentType.GENERAL) {
        val config = getAccessibilityConfig(context)
        
        when (contentType) {
            ContentType.REGION_BUTTON -> applyToRegionButton(context, view, config)
            ContentType.MUSCLE_ITEM -> applyToMuscleItem(context, view, config)
            ContentType.INFO_TEXT -> applyToInfoText(context, view, config)
            ContentType.GENERAL -> applyGeneralAccessibility(context, view, config)
        }
    }

    /**
     * Tipos de contenido para aplicar accesibilidad específica
     */
    enum class ContentType {
        REGION_BUTTON,
        MUSCLE_ITEM,
        INFO_TEXT,
        GENERAL
    }

    /**
     * Obtiene colores accesibles según el tipo de daltonismo
     */
    fun getAccessibleColor(context: Context, originalColorRes: Int): Int {
        val config = getAccessibilityConfig(context)
        val originalColor = ContextCompat.getColor(context, originalColorRes)

        return when (config.colorblindType) {
            ColorblindType.NONE -> originalColor
            ColorblindType.PROTANOPIA -> adjustForProtanopia(context, originalColorRes)
            ColorblindType.DEUTERANOPIA -> adjustForDeuteranopia(context, originalColorRes)
            ColorblindType.TRITANOPIA -> adjustForTritanopia(context, originalColorRes)
            ColorblindType.ACHROMATOPSIA -> adjustForAchromatopsia(context, originalColorRes)
        }
    }

    /**
     * Aplica accesibilidad a botones de región
     */
    private fun applyToRegionButton(context: Context, view: View, config: AccessibilityConfig) {
        if (view is TextView) {
            // Aplicar alto contraste si está habilitado
            if (config.highContrast) {
                view.setTextColor(ContextCompat.getColor(context, R.color.black))
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            }

            // Agregar indicadores textuales si está habilitado
            if (config.showTextIndicators) {
                val regionText = view.text.toString()
                val indicator = getRegionIndicator(regionText)
                view.text = "$indicator $regionText"
            }

            // Agregar descripción de contenido para lectores de pantalla
            view.contentDescription = "${view.text} - Toque para explorar músculos de esta región"
        }
    }

    /**
     * Aplica accesibilidad a elementos de músculo
     */
    private fun applyToMuscleItem(context: Context, view: View, config: AccessibilityConfig) {
        if (view is TextView) {
            // Aplicar colores accesibles
            val textColor = getAccessibleColor(context, R.color.text_primary)
            view.setTextColor(textColor)

            // Agregar indicadores textuales para funciones musculares
            if (config.showTextIndicators) {
                addMuscleTypeIndicator(view)
            }

            // Mejorar descripción para lectores de pantalla
            view.contentDescription = "${view.text} - Toque para ver detalles anatómicos completos"
        }
    }

    /**
     * Aplica accesibilidad a texto informativo
     */
    private fun applyToInfoText(context: Context, view: View, config: AccessibilityConfig) {
        if (view is TextView) {
            // Aumentar contraste si es necesario
            if (config.highContrast) {
                view.setTextColor(ContextCompat.getColor(context, R.color.black))
            }

            // Asegurar texto legible
            val textColor = getAccessibleColor(context, R.color.text_primary)
            view.setTextColor(textColor)
        }
    }

    /**
     * Aplica accesibilidad general
     */
    private fun applyGeneralAccessibility(context: Context, view: View, config: AccessibilityConfig) {
        // Configurar contraste general
        if (config.highContrast && view is TextView) {
            view.setTextColor(ContextCompat.getColor(context, R.color.black))
        }
    }

    /**
     * Obtiene indicador textual para regiones
     */
    private fun getRegionIndicator(regionText: String): String {
        return when {
            regionText.contains("CABEZA", ignoreCase = true) -> "🟤"
            regionText.contains("CUELLO", ignoreCase = true) -> "🟫"
            regionText.contains("TRONCO", ignoreCase = true) -> "⬛"
            regionText.contains("TORÁCIC", ignoreCase = true) -> "🔷"
            regionText.contains("PÉLVIC", ignoreCase = true) -> "🔶"
            else -> "📍"
        }
    }

    /**
     * Agrega indicadores de tipo de músculo
     */
    private fun addMuscleTypeIndicator(textView: TextView) {
        val muscleText = textView.text.toString()
        val indicator = when {
            muscleText.contains("flexor", ignoreCase = true) -> "↩️"
            muscleText.contains("extensor", ignoreCase = true) -> "↪️"
            muscleText.contains("aductor", ignoreCase = true) -> "⬅️"
            muscleText.contains("abductor", ignoreCase = true) -> "➡️"
            muscleText.contains("rotador", ignoreCase = true) -> "🔄"
            muscleText.contains("elevador", ignoreCase = true) -> "⬆️"
            muscleText.contains("depresor", ignoreCase = true) -> "⬇️"
            else -> ""
        }
        
        if (indicator.isNotEmpty()) {
            textView.text = "$indicator $muscleText"
        }
    }

    /**
     * Ajustes específicos para protanopia (dificultad con rojos)
     */
    private fun adjustForProtanopia(context: Context, colorRes: Int): Int {
        return when (colorRes) {
            R.color.primary_brown -> ContextCompat.getColor(context, R.color.elegant_gray)
            R.color.secondary_brown -> ContextCompat.getColor(context, R.color.accent_light_brown)
            R.color.error_red -> ContextCompat.getColor(context, R.color.elegant_gray)
            else -> ContextCompat.getColor(context, colorRes)
        }
    }

    /**
     * Ajustes específicos para deuteranopia (dificultad con verdes)
     */
    private fun adjustForDeuteranopia(context: Context, colorRes: Int): Int {
        return when (colorRes) {
            R.color.success_green -> ContextCompat.getColor(context, R.color.info_blue)
            else -> ContextCompat.getColor(context, colorRes)
        }
    }

    /**
     * Ajustes específicos para tritanopia (dificultad con azules)
     */
    private fun adjustForTritanopia(context: Context, colorRes: Int): Int {
        return when (colorRes) {
            R.color.info_blue -> ContextCompat.getColor(context, R.color.elegant_gray)
            else -> ContextCompat.getColor(context, colorRes)
        }
    }

    /**
     * Ajustes para acromatopsia (visión en escala de grises)
     */
    private fun adjustForAchromatopsia(context: Context, colorRes: Int): Int {
        return when (colorRes) {
            R.color.primary_brown -> ContextCompat.getColor(context, R.color.elegant_gray)
            R.color.secondary_brown -> ContextCompat.getColor(context, R.color.text_secondary)
            R.color.accent_light_brown -> ContextCompat.getColor(context, R.color.light_background)
            R.color.success_green -> ContextCompat.getColor(context, R.color.elegant_gray)
            R.color.error_red -> ContextCompat.getColor(context, R.color.black)
            R.color.warning_orange -> ContextCompat.getColor(context, R.color.text_secondary)
            R.color.info_blue -> ContextCompat.getColor(context, R.color.elegant_gray)
            else -> ContextCompat.getColor(context, colorRes)
        }
    }

    /**
     * Crea drawable con patrones para diferenciar elementos sin depender del color
     */
    fun createPatternDrawable(context: Context, patternType: PatternType): GradientDrawable {
        val drawable = GradientDrawable()
        
        when (patternType) {
            PatternType.SOLID -> {
                drawable.setColor(ContextCompat.getColor(context, R.color.primary_brown))
            }
            PatternType.STRIPED -> {
                drawable.orientation = GradientDrawable.Orientation.LEFT_RIGHT
                drawable.colors = intArrayOf(
                    ContextCompat.getColor(context, R.color.primary_brown),
                    ContextCompat.getColor(context, R.color.secondary_brown)
                )
            }
            PatternType.DOTTED -> {
                drawable.setColor(ContextCompat.getColor(context, R.color.accent_light_brown))
                drawable.setStroke(4, ContextCompat.getColor(context, R.color.primary_brown))
            }
        }
        
        drawable.cornerRadius = 8f
        return drawable
    }

    /**
     * Tipos de patrones para elementos visuales
     */
    enum class PatternType {
        SOLID,
        STRIPED,
        DOTTED
    }

    /**
     * Valida si dos colores tienen contraste suficiente
     */
    fun hasEnoughContrast(color1: Int, color2: Int): Boolean {
        val ratio = calculateContrastRatio(color1, color2)
        return ratio >= 4.5 // WCAG AA estándar
    }

    /**
     * Calcula la ratio de contraste entre dos colores
     */
    private fun calculateContrastRatio(color1: Int, color2: Int): Double {
        val luminance1 = calculateLuminance(color1)
        val luminance2 = calculateLuminance(color2)
        
        val lighter = maxOf(luminance1, luminance2)
        val darker = minOf(luminance1, luminance2)
        
        return (lighter + 0.05) / (darker + 0.05)
    }

    /**
     * Calcula la luminancia de un color
     */
    private fun calculateLuminance(color: Int): Double {
        val r = android.graphics.Color.red(color) / 255.0
        val g = android.graphics.Color.green(color) / 255.0
        val b = android.graphics.Color.blue(color) / 255.0

        val rLin = if (r <= 0.03928) r / 12.92 else Math.pow((r + 0.055) / 1.055, 2.4)
        val gLin = if (g <= 0.03928) g / 12.92 else Math.pow((g + 0.055) / 1.055, 2.4)
        val bLin = if (b <= 0.03928) b / 12.92 else Math.pow((b + 0.055) / 1.055, 2.4)

        return 0.2126 * rLin + 0.7152 * gLin + 0.0722 * bLin
    }

    // ===== FUNCIONES PARA RF-007: SOPORTE PARA DEFICIENCIAS VISUALES =====

    /**
     * Aplica escala de texto a una TextView según la configuración de accesibilidad
     */
    fun applyTextScale(context: Context, textView: TextView) {
        val config = getAccessibilityConfig(context)
        val originalSize = textView.textSize / context.resources.displayMetrics.scaledDensity
        val scaledSize = originalSize * config.textScale.scale
        textView.textSize = scaledSize
    }

    /**
     * Aplica escala de texto a múltiples TextViews
     */
    fun applyTextScaleToViews(context: Context, vararg textViews: TextView) {
        textViews.forEach { applyTextScale(context, it) }
    }

    /**
     * Configura descripciones para lectores de pantalla
     */
    fun setContentDescription(view: View, description: String, contentType: String = "") {
        val context = view.context
        val config = getAccessibilityConfig(context)
        
        if (config.alternativeDescriptions) {
            val fullDescription = if (contentType.isNotBlank()) {
                "$contentType: $description"
            } else {
                description
            }
            view.contentDescription = fullDescription
        }
        
        // Habilitar importancia para lectores de pantalla
        view.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
    }

    /**
     * Configura descripciones específicas para músculos
     */
    fun setMuscleDescription(view: View, musculo: Musculo) {
        val description = buildString {
            append("Músculo ${musculo.nombre}. ")
            append("Origen: ${musculo.origen}. ")
            append("Inserción: ${musculo.insercion}. ")
            append("Función: ${musculo.funcion}")
        }
        setContentDescription(view, description, "Información muscular")
    }

    /**
     * Configura descripciones para botones de región
     */
    fun setRegionButtonDescription(view: View, region: Region) {
        val description = "Botón para explorar ${region.nombreCompleto}. ${region.descripcion}"
        setContentDescription(view, description, "Región anatómica")
    }

    /**
     * Configura descripciones para imágenes anatómicas
     */
    fun setAnatomicalImageDescription(view: View, regionName: String, muscleCount: Int) {
        val description = "Imagen anatómica de $regionName mostrando $muscleCount músculos. " +
                "Toque para ver lista de músculos disponibles"
        setContentDescription(view, description, "Imagen anatómica")
    }

    /**
     * Habilita navegación por teclado para un conjunto de vistas
     */
    fun enableKeyboardNavigation(vararg views: View) {
        views.forEach { view ->
            view.isFocusable = true
            view.isFocusableInTouchMode = true
        }
    }

    /**
     * Configura accesibilidad completa para una vista de músculo
     */
    fun setupMuscleViewAccessibility(context: Context, view: View, musculo: Musculo) {
        // Aplicar escala de texto si hay TextViews
        view.findViewById<TextView>(R.id.tvNombre)?.let { applyTextScale(context, it) }
        view.findViewById<TextView>(R.id.tvDescripcion)?.let { applyTextScale(context, it) }
        view.findViewById<TextView>(R.id.tvNumero)?.let { applyTextScale(context, it) }
        
        // Configurar descripción para lectores de pantalla
        setMuscleDescription(view, musculo)
        
        // Habilitar navegación por teclado
        enableKeyboardNavigation(view)
    }

    /**
     * Configura accesibilidad completa para una vista de región
     */
    fun setupRegionViewAccessibility(context: Context, view: View, region: Region) {
        // Aplicar escala de texto a títulos
        view.findViewById<TextView>(R.id.tvTitle)?.let { applyTextScale(context, it) }
        
        // Configurar descripción para lectores de pantalla
        setRegionButtonDescription(view, region)
        
        // Habilitar navegación por teclado
        enableKeyboardNavigation(view)
    }

    /**
     * Verifica si los lectores de pantalla están activos en el sistema
     */
    fun isScreenReaderActive(context: Context): Boolean {
        val accessibilityManager = context.getSystemService(Context.ACCESSIBILITY_SERVICE) 
                as android.view.accessibility.AccessibilityManager
        return accessibilityManager.isEnabled && accessibilityManager.isTouchExplorationEnabled
    }

    /**
     * Configura accesibilidad según las capacidades del dispositivo
     */
    fun setupDeviceAccessibility(context: Context, rootView: View) {
        val config = getAccessibilityConfig(context)
        
        // Si el lector de pantalla está activo, aplicar configuraciones específicas
        if (isScreenReaderActive(context) || config.screenReaderEnabled) {
            // Asegurar que todas las vistas importantes tengan descripciones
            ensureViewsHaveDescriptions(rootView)
            
            // Configurar orden de navegación lógico
            setupNavigationOrder(rootView)
        }
        
        // Aplicar escala de texto global
        applyGlobalTextScale(context, rootView)
    }

    /**
     * Asegura que todas las vistas tengan descripciones apropiadas
     */
    private fun ensureViewsHaveDescriptions(rootView: View) {
        // Si es un ViewGroup, revisar hijos recursivamente
        if (rootView is android.view.ViewGroup) {
            for (i in 0 until rootView.childCount) {
                ensureViewsHaveDescriptions(rootView.getChildAt(i))
            }
        }
        
        // Si la vista no tiene descripción y es interactiva, agregar una básica
        if (rootView.contentDescription.isNullOrBlank() && 
            (rootView.isClickable || rootView.isFocusable)) {
            rootView.contentDescription = "Elemento interactivo"
        }
    }

    /**
     * Configura orden de navegación lógico para lectores de pantalla
     */
    private fun setupNavigationOrder(rootView: View) {
        // Los lectores de pantalla navegan generalmente de arriba a abajo, izquierda a derecha
        // Esto es manualmente configurable si es necesario
        rootView.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
    }

    /**
     * Aplica escala de texto globalmente a todas las TextViews en una vista
     */
    private fun applyGlobalTextScale(context: Context, rootView: View) {
        if (rootView is TextView) {
            applyTextScale(context, rootView)
        } else if (rootView is android.view.ViewGroup) {
            for (i in 0 until rootView.childCount) {
                applyGlobalTextScale(context, rootView.getChildAt(i))
            }
        }
    }
} 