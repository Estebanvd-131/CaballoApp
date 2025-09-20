package com.villalobos.caballoapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.shape.MaterialShapeDrawable
import androidx.core.view.ViewCompat
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatRadioButton

// TAG para los logs
private const val TAG = "AccesibilityHelper"

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
    private const val KEY_PRIMARY_COLOR = "primary_color"
    private const val KEY_SECONDARY_COLOR = "secondary_color"
    private const val KEY_TEXT_COLOR = "text_color"

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
        val alternativeDescriptions: Boolean = true,
        val primaryColor: Int = -1, // -1 means use default
        val secondaryColor: Int = -1, // -1 means use default
        val textColor: Int = -1 // -1 means use default
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
        val primaryColor = prefs.getInt(KEY_PRIMARY_COLOR, -1)
        val secondaryColor = prefs.getInt(KEY_SECONDARY_COLOR, -1)
        val textColor = prefs.getInt(KEY_TEXT_COLOR, -1)

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
            alternativeDescriptions = alternativeDescriptions,
            primaryColor = primaryColor,
            secondaryColor = secondaryColor,
            textColor = textColor
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
            putInt(KEY_PRIMARY_COLOR, config.primaryColor)
            putInt(KEY_SECONDARY_COLOR, config.secondaryColor)
            putInt(KEY_TEXT_COLOR, config.textColor)
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

        // First check if custom colors are set
        val customColor = when (originalColorRes) {
            R.color.primary_brown -> if (config.primaryColor != -1) config.primaryColor else null
            R.color.secondary_brown -> if (config.secondaryColor != -1) config.secondaryColor else null
            R.color.text_primary, R.color.text_dark -> if (config.textColor != -1) config.textColor else null
            else -> null
        }

        if (customColor != null) {
            return customColor
        }

        // En modo normal (NONE), usar los colores del tema actual
        if (config.colorblindType == ColorblindType.NONE) {
            val isNightMode = context.resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK == android.content.res.Configuration.UI_MODE_NIGHT_YES
            
            return when (originalColorRes) {
                R.color.primary_brown -> {
                    if (isNightMode) ContextCompat.getColor(context, R.color.dark_primary)
                    else ContextCompat.getColor(context, R.color.light_primary)
                }
                R.color.secondary_brown -> {
                    if (isNightMode) ContextCompat.getColor(context, R.color.dark_secondary)
                    else ContextCompat.getColor(context, R.color.light_secondary)
                }
                R.color.text_primary, R.color.text_dark -> {
                    if (isNightMode) ContextCompat.getColor(context, R.color.dark_primary_text)
                    else ContextCompat.getColor(context, R.color.light_primary_text)
                }
                R.color.light_background -> {
                    if (isNightMode) ContextCompat.getColor(context, R.color.dark_background)
                    else ContextCompat.getColor(context, R.color.light_background)
                }
                else -> ContextCompat.getColor(context, originalColorRes)
            }
        }

        val originalColor = ContextCompat.getColor(context, originalColorRes)

        return when (config.colorblindType) {
            ColorblindType.PROTANOPIA -> adjustForProtanopia(context, originalColorRes)
            ColorblindType.DEUTERANOPIA -> adjustForDeuteranopia(context, originalColorRes)
            ColorblindType.TRITANOPIA -> adjustForTritanopia(context, originalColorRes)
            ColorblindType.ACHROMATOPSIA -> adjustForAchromatopsia(context, originalColorRes)
            else -> originalColor
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
     * Paleta optimizada para distinguir colores en protanopia
     */
    private fun adjustForProtanopia(context: Context, colorRes: Int): Int {
        return when (colorRes) {
            R.color.primary_brown -> ContextCompat.getColor(context, R.color.protanopia_primary)
            R.color.secondary_brown -> ContextCompat.getColor(context, R.color.protanopia_secondary)
            R.color.accent_light_brown -> ContextCompat.getColor(context, R.color.protanopia_accent)
            R.color.success_green -> ContextCompat.getColor(context, R.color.protanopia_success)
            R.color.error_red -> ContextCompat.getColor(context, R.color.protanopia_error)
            R.color.warning_orange -> ContextCompat.getColor(context, R.color.protanopia_warning)
            R.color.info_blue -> ContextCompat.getColor(context, R.color.protanopia_info)
            R.color.text_primary -> ContextCompat.getColor(context, R.color.protanopia_text_primary)
            R.color.text_secondary -> ContextCompat.getColor(context, R.color.protanopia_text_secondary)
            R.color.light_background -> ContextCompat.getColor(context, R.color.protanopia_background)
            else -> ContextCompat.getColor(context, colorRes)
        }
    }

    /**
     * Ajustes específicos para deuteranopia (dificultad con verdes)
     * Paleta optimizada para distinguir colores en deuteranopia
     */
    private fun adjustForDeuteranopia(context: Context, colorRes: Int): Int {
        return when (colorRes) {
            R.color.primary_brown -> ContextCompat.getColor(context, R.color.deuteranopia_primary)
            R.color.secondary_brown -> ContextCompat.getColor(context, R.color.deuteranopia_secondary)
            R.color.accent_light_brown -> ContextCompat.getColor(context, R.color.deuteranopia_accent)
            R.color.success_green -> ContextCompat.getColor(context, R.color.deuteranopia_success)
            R.color.error_red -> ContextCompat.getColor(context, R.color.deuteranopia_error)
            R.color.warning_orange -> ContextCompat.getColor(context, R.color.deuteranopia_warning)
            R.color.info_blue -> ContextCompat.getColor(context, R.color.deuteranopia_info)
            R.color.text_primary -> ContextCompat.getColor(context, R.color.deuteranopia_text_primary)
            R.color.text_secondary -> ContextCompat.getColor(context, R.color.deuteranopia_text_secondary)
            R.color.light_background -> ContextCompat.getColor(context, R.color.deuteranopia_background)
            else -> ContextCompat.getColor(context, colorRes)
        }
    }

    /**
     * Ajustes específicos para tritanopia (dificultad con azules/amarillos)
     * Paleta optimizada para distinguir colores en tritanopia
     */
    private fun adjustForTritanopia(context: Context, colorRes: Int): Int {
        return when (colorRes) {
            R.color.primary_brown -> ContextCompat.getColor(context, R.color.tritanopia_primary)
            R.color.secondary_brown -> ContextCompat.getColor(context, R.color.tritanopia_secondary)
            R.color.accent_light_brown -> ContextCompat.getColor(context, R.color.tritanopia_accent)
            R.color.success_green -> ContextCompat.getColor(context, R.color.tritanopia_success)
            R.color.error_red -> ContextCompat.getColor(context, R.color.tritanopia_error)
            R.color.warning_orange -> ContextCompat.getColor(context, R.color.tritanopia_warning)
            R.color.info_blue -> ContextCompat.getColor(context, R.color.tritanopia_info)
            R.color.text_primary -> ContextCompat.getColor(context, R.color.tritanopia_text_primary)
            R.color.text_secondary -> ContextCompat.getColor(context, R.color.tritanopia_text_secondary)
            R.color.light_background -> ContextCompat.getColor(context, R.color.tritanopia_background)
            else -> ContextCompat.getColor(context, colorRes)
        }
    }

    /**
     * Ajustes para acromatopsia (visión en escala de grises)
     * Paleta en escala de grises con buen contraste
     */
    private fun adjustForAchromatopsia(context: Context, colorRes: Int): Int {
        return when (colorRes) {
            R.color.primary_brown -> ContextCompat.getColor(context, R.color.achromatopsia_dark_gray)
            R.color.secondary_brown -> ContextCompat.getColor(context, R.color.achromatopsia_medium_gray)
            R.color.accent_light_brown -> ContextCompat.getColor(context, R.color.achromatopsia_light_gray)
            R.color.success_green -> ContextCompat.getColor(context, R.color.achromatopsia_dark_gray)
            R.color.error_red -> ContextCompat.getColor(context, R.color.achromatopsia_black)
            R.color.warning_orange -> ContextCompat.getColor(context, R.color.achromatopsia_medium_gray)
            R.color.info_blue -> ContextCompat.getColor(context, R.color.achromatopsia_dark_gray)
            R.color.text_primary -> ContextCompat.getColor(context, R.color.achromatopsia_black)
            R.color.text_secondary -> ContextCompat.getColor(context, R.color.achromatopsia_dark_gray)
            R.color.light_background -> ContextCompat.getColor(context, R.color.achromatopsia_white)
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

    /**
     * Aplica gradiente de fondo según el tipo de daltonismo
     */
    fun applyBackgroundGradient(context: Context, view: View, colorblindType: ColorblindType) {
        try {
            val background = when (colorblindType) {
                ColorblindType.NONE -> {
                    // En modo normal, usar el gradiente actualizado con los nuevos colores
                    ContextCompat.getDrawable(context, R.drawable.gradient_background)
                }
                ColorblindType.PROTANOPIA -> {
                    // Usar gradiente específico para protanopia
                    ContextCompat.getDrawable(context, R.drawable.gradient_coffee_protanopia)
                }
                ColorblindType.DEUTERANOPIA -> {
                    // Usar gradiente específico para deuteranopia
                    ContextCompat.getDrawable(context, R.drawable.gradient_coffee_deuteranopia)
                }
                ColorblindType.TRITANOPIA -> {
                    // Usar gradiente específico para tritanopia
                    ContextCompat.getDrawable(context, R.drawable.gradient_coffee_tritanopia)
                }
                ColorblindType.ACHROMATOPSIA -> {
                    // Usar gradiente específico para acromatopsia
                    ContextCompat.getDrawable(context, R.drawable.gradient_coffee_achromatopsia)
                }
            }

            view.background = background

            Log.d(TAG, "Fondo aplicado: ${colorblindType.displayName}")
        } catch (e: Exception) {
            Log.e(TAG, "Error aplicando fondo: ${e.message}")
        }
    }

    /**
     * Aplica los colores de accesibilidad a toda la aplicación
     * Este método debe llamarse cuando cambia la configuración de accesibilidad
     */
    fun applyAccessibilityColorsToApp(context: Context) {
        val config = getAccessibilityConfig(context)

        // Obtener la actividad actual
        val activity = (context as? android.app.Activity) ?: return

        try {
            // Aplicar gradiente de fondo según el tipo de daltonismo
            applyBackgroundGradient(context, activity.window.decorView, config.colorblindType)

            // Aplicar colores específicos según el tipo de daltonismo configurado
            applySpecificColorblindColors(context, activity.window.decorView, config.colorblindType)

            // Fuerza inmediata de paleta de botones en todo el árbol (MaterialButton, Button, RadioButton)
            forceApplyButtonPalette(context, activity.window.decorView, config.colorblindType)

            // Aplicar colores a toda la vista de manera más agresiva
            applyAccessibilityColorsToView(context, activity.window.decorView)

            // Forzar redibujado completo de la vista
            activity.window.decorView.invalidate()
            
            // Forzar actualización del layout
            activity.window.decorView.requestLayout()
            
            // Aplicar colores nuevamente después de un pequeño delay para asegurar persistencia
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                applySpecificColorblindColors(context, activity.window.decorView, config.colorblindType)

                // LIMPIEZA FINAL: Remover cualquier overlay blanco que haya quedado en imágenes
                removeWhiteOverlaysFromImages(context, activity.window.decorView)

                activity.window.decorView.invalidate()
                Log.d(TAG, "Colores de accesibilidad aplicados agresivamente: ${config.colorblindType.displayName}")
            }, 100)

            // Reaplicar colores tras el próximo layout para asegurar que todos los botones tomen el tinte
            reapplyButtonColorsOnNextLayout(context, config.colorblindType)
        } catch (e: Exception) {
            Log.e(TAG, "Error aplicando colores de accesibilidad: ${e.message}")
        }
    }

// Reaplica colores de botones tras el próximo layout para asegurar que tomen el tinte del modo
private fun reapplyButtonColorsOnNextLayout(context: Context, colorblindType: ColorblindType) {
    val activity = context as? Activity ?: return
    val root = activity.window.decorView
    root.viewTreeObserver.addOnGlobalLayoutListener(object : android.view.ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            try {
                // Reaplicar paleta por modo y recorrido profundo a todos los botones
                applySpecificColorblindColors(context, root, colorblindType)
                applyAccessibilityColorsToView(context, root)
                // Forzar redibujo
                root.invalidate()
            } catch (_: Exception) {
                // Ignorar para no romper el ciclo de vida
            } finally {
                // Remover listener para evitar loops
                root.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        }
    })
}
    // Detecta si una vista pertenece al grupo de opciones de daltonismo (RadioGroup) para no recolorearla
    private fun isInDaltonismRadioArea(view: View): Boolean {
        var parent = view.parent
        while (parent is android.view.View) {
            val v = parent as android.view.View
            if (v.id == R.id.rgModosDaltonismo) return true
            parent = v.parent
        }
        return false
    }

    // Detecta si una vista pertenece al grupo de opciones del quiz (RadioGroup) para no recolorearla
    private fun isInQuizRadioArea(view: View): Boolean {
        var parent = view.parent
        while (parent is android.view.View) {
            val v = parent as android.view.View
            if (v.id == R.id.radioGroupOptions) return true
            parent = v.parent
        }
        return false
    }

    /**
     * Aplica colores de accesibilidad recursivamente a una vista y sus hijos
     */
    private fun applyAccessibilityColorsToView(context: Context, view: View) {
        try {
            if (view is TextView) {
                // Aplicar color de texto accesible
                val textColor = getAccessibleColor(context, R.color.text_primary)
                view.setTextColor(textColor)
                
                // Aplicar escala de texto
                applyTextScale(context, view)
            }
            
            if (view is com.google.android.material.button.MaterialButton) {
                // Limpiar tints/fondos que pueden forzar café por XML
                ViewCompat.setBackgroundTintList(view, null)
                view.backgroundTintList = null
                view.backgroundTintMode = null
                view.setBackgroundResource(0)

                // Aplicar colores a botones (MaterialButton) con paleta según modo de daltonismo
                val backgroundColor = getAccessibleColor(context, R.color.primary_brown)
                val stroke = getAccessibleColor(context, R.color.secondary_brown)

                // Forzar relleno en cualquier estilo (incl. Outlined)
                view.setBackgroundColor(backgroundColor)
                (view.background as? com.google.android.material.shape.MaterialShapeDrawable)?.let { shape ->
                    shape.fillColor = android.content.res.ColorStateList.valueOf(backgroundColor)
                    shape.strokeColor = android.content.res.ColorStateList.valueOf(stroke)
                }

                view.backgroundTintList = android.content.res.ColorStateList.valueOf(backgroundColor)
                view.backgroundTintMode = android.graphics.PorterDuff.Mode.SRC_IN
                view.strokeColor = android.content.res.ColorStateList.valueOf(stroke)
                view.strokeWidth = if (view.strokeWidth > 0) view.strokeWidth else 3
                view.rippleColor = android.content.res.ColorStateList.valueOf(stroke)
                view.iconTint = android.content.res.ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.black))

                val textColor = ContextCompat.getColor(context, android.R.color.black)
                view.setTextColor(textColor)
            }

            // Soporte para botones estándar (android.widget.Button y AppCompatButton)
            if ((view is android.widget.Button || view is AppCompatButton) && !(view is android.widget.RadioButton || view is AppCompatRadioButton || view is com.google.android.material.radiobutton.MaterialRadioButton)) {
                val btn = view as android.widget.TextView
                // Limpiar tints/fondos que pueden forzar café por XML
                ViewCompat.setBackgroundTintList(btn, null)
                btn.backgroundTintList = null
                btn.setBackgroundResource(0)

                // Aplicar colores a botones estándar con paleta según modo de daltonismo
                val backgroundColor = getAccessibleColor(context, R.color.primary_brown)
                btn.setBackgroundColor(backgroundColor)
                btn.backgroundTintList = android.content.res.ColorStateList.valueOf(backgroundColor)

                val textColor = ContextCompat.getColor(context, android.R.color.black)
                btn.setTextColor(textColor)
            }

            // Soporte para RadioButtons (tinte del check y texto) - omitir en Accesibilidad.rgModosDaltonismo
            if (view is android.widget.RadioButton || view is AppCompatRadioButton || view is com.google.android.material.radiobutton.MaterialRadioButton) {
                if (!isInDaltonismRadioArea(view)) {
                    val rb = view as android.widget.CompoundButton
                    val tintColor = getAccessibleColor(context, R.color.primary_brown)
                    val inDaltonismArea = isInDaltonismRadioArea(view)
val appliedTint = if (inDaltonismArea) ContextCompat.getColor(context, R.color.primary_brown) else tintColor
rb.buttonTintList = android.content.res.ColorStateList.valueOf(appliedTint)
if (inDaltonismArea) {
    (rb as android.widget.TextView).background = ContextCompat.getDrawable(context, R.drawable.radio_option_background)
}
                    (rb as android.widget.TextView).setTextColor(ContextCompat.getColor(context, android.R.color.black))
                } else {
                    // Restaurar apariencia por defecto en la pantalla de Accesibilidad (no recolorear)
                    (view as android.widget.CompoundButton).buttonTintList = null
                    (view as android.widget.TextView).setTextColor(ContextCompat.getColor(context, android.R.color.black))
                }
            }
            
            if (view is androidx.cardview.widget.CardView) {
                if (!isInDaltonismRadioArea(view)) {
                    // Aplicar color de fondo a tarjetas
                    val cardColor = getAccessibleColor(context, R.color.white)
                    view.setCardBackgroundColor(cardColor)
                } else {
                    // Mantener opciones de daltonismo con fondo blanco sólido
                    view.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
                }
            }
            
            if (view is android.widget.ImageView) {
                // EN MODO DALTONISMO ESTÁ PROHIBIDO TOCAR LAS IMÁGENES
                // Eliminar cualquier filtro o tinte de imágenes
                view.clearColorFilter()
                
                // Asegurar que las imágenes no tengan fondo ni bordes en modo daltonismo
                val config = getAccessibilityConfig(context)
                if (config.colorblindType != ColorblindType.NONE) {
                    view.setBackgroundColor(android.graphics.Color.TRANSPARENT)
                }
            }
            
            // Evitar pintar fondos sobre overlays o vistas cercanas a imágenes
            if (view.background is android.graphics.drawable.ColorDrawable) {
                val isRadioInProtectedArea =
                    (view is android.widget.RadioButton || view is AppCompatRadioButton || view is com.google.android.material.radiobutton.MaterialRadioButton) &&
                    (isInDaltonismRadioArea(view) || isInQuizRadioArea(view))

                val shouldPaintBackground =
                    (view is TextView || view is com.google.android.material.button.MaterialButton) &&
                    !ancestorContainsImages(view) &&
                    !isRadioInProtectedArea

                if (shouldPaintBackground) {
                    val backgroundDrawable = view.background as android.graphics.drawable.ColorDrawable
                    val backgroundColor = getAccessibleColor(context, backgroundDrawable.color)
                    view.setBackgroundColor(backgroundColor)
                }
            }
            
            // Aplicar recursivamente a hijos, pero con cuidado de no aplicar fondos a contenedores de imágenes
            if (view is android.view.ViewGroup) {
                for (i in 0 until view.childCount) {
                    val child = view.getChildAt(i)
                    // Evitar aplicar colores a contenedores que contienen imágenes para prevenir overlays
                    if (!(child is android.widget.ImageView ||
                          child.javaClass.simpleName.contains("Image"))) {
                        // Para CardViews, verificar si contienen imágenes antes de procesar
                        if (child is androidx.cardview.widget.CardView) {
                            if (!containsImagesRecursively(child)) {
                                applyAccessibilityColorsToView(context, child)
                            }
                        } else {
                            applyAccessibilityColorsToView(context, child)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error aplicando colores a vista: ${e.message}")
        }
    }

    /**
     * Notifica a todas las actividades para que actualicen sus colores
     * Esto debería llamarse después de guardar la configuración de accesibilidad
     */
    fun notifyAppColorChange(context: Context) {
        try {
            // También aplicar colores a la actividad actual inmediatamente
            applyAccessibilityColorsToApp(context)
            
            // Mostrar mensaje de que los colores se han aplicado
            Toast.makeText(context, "Colores de accesibilidad aplicados", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e(TAG, "Error notificando cambio de colores: ${e.message}")
        }
    }

    /**
     * Método para que actividades que no pueden extender AccessibilityActivity apliquen accesibilidad
     * Debe llamarse en onCreate() y onResume() de cada actividad
     */
    fun applyAccessibilityToActivity(activity: AppCompatActivity) {
        ErrorHandler.safeExecute(
            context = activity,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al aplicar configuración de accesibilidad"
        ) {
            applyAccessibilityColorsToApp(activity)
        }
    }
    

    /**
     * Reinicia la aplicación para aplicar completamente los cambios de colores
     * Esto asegura que todas las actividades se recarguen con los nuevos colores
     */
    fun restartAppForColorChanges(context: Context) {
        try {
            // Forzar la aplicación de la configuración antes de reiniciar
            applyAccessibilityColorsToApp(context)
            
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
            
            if (context is android.app.Activity) {
                context.finishAffinity()
            }
            
            Log.d(TAG, "Aplicación reiniciada para aplicar cambios de colores")
        } catch (e: Exception) {
            Log.e(TAG, "Error reiniciando aplicación: ${e.message}")
        }
    }

    /**
     * Aplica colores de protanopia inmediatamente a una actividad
     */
    fun adjustForProtanopia(context: Context, activity: Activity) {
        try {
            // Guardar temporalmente configuración de protanopia para que los colores persistan
            val protanopiaConfig = getAccessibilityConfig(context).copy(colorblindType = ColorblindType.PROTANOPIA)
            saveAccessibilityConfig(context, protanopiaConfig)

            // Aplicar gradiente de protanopia
            applyBackgroundGradient(context, activity.window.decorView, ColorblindType.PROTANOPIA)

            // Aplicar colores de protanopia a toda la actividad
            applySpecificColorblindColors(context, activity.window.decorView, ColorblindType.PROTANOPIA)

            // Forzar redibujado de todas las vistas
            refreshAllViews(activity.window.decorView)

            Log.d(TAG, "Colores de protanopia aplicados inmediatamente")
        } catch (e: Exception) {
            Log.e(TAG, "Error aplicando colores de protanopia: ${e.message}")
        }
    }

    /**
     * Aplica colores de deuteranopia inmediatamente a una actividad
     */
    fun adjustForDeuteranopia(context: Context, activity: Activity) {
        try {
            // Guardar temporalmente configuración de deuteranopia para que los colores persistan
            val deuteranopiaConfig = getAccessibilityConfig(context).copy(colorblindType = ColorblindType.DEUTERANOPIA)
            saveAccessibilityConfig(context, deuteranopiaConfig)

            // Aplicar gradiente de deuteranopia
            applyBackgroundGradient(context, activity.window.decorView, ColorblindType.DEUTERANOPIA)

            // Aplicar colores de deuteranopia a toda la actividad
            applySpecificColorblindColors(context, activity.window.decorView, ColorblindType.DEUTERANOPIA)

            // Forzar redibujado de todas las vistas
            refreshAllViews(activity.window.decorView)

            Log.d(TAG, "Colores de deuteranopia aplicados inmediatamente")
        } catch (e: Exception) {
            Log.e(TAG, "Error aplicando colores de deuteranopia: ${e.message}")
        }
    }

    /**
     * Aplica colores de tritanopia inmediatamente a una actividad
     */
    fun adjustForTritanopia(context: Context, activity: Activity) {
        try {
            // Guardar temporalmente configuración de tritanopia para que los colores persistan
            val tritanopiaConfig = getAccessibilityConfig(context).copy(colorblindType = ColorblindType.TRITANOPIA)
            saveAccessibilityConfig(context, tritanopiaConfig)

            // Aplicar gradiente de tritanopia
            applyBackgroundGradient(context, activity.window.decorView, ColorblindType.TRITANOPIA)

            // Aplicar colores de tritanopia a toda la actividad
            applySpecificColorblindColors(context, activity.window.decorView, ColorblindType.TRITANOPIA)

            // Forzar redibujado de todas las vistas
            refreshAllViews(activity.window.decorView)

            Log.d(TAG, "Colores de tritanopia aplicados inmediatamente")
        } catch (e: Exception) {
            Log.e(TAG, "Error aplicando colores de tritanopia: ${e.message}")
        }
    }

    /**
     * Aplica colores de acromatopsia inmediatamente a una actividad
     */
    fun adjustForAchromatopsia(context: Context, activity: Activity) {
        try {
            // Guardar temporalmente configuración de acromatopsia para que los colores persistan
            val achromatopsiaConfig = getAccessibilityConfig(context).copy(colorblindType = ColorblindType.ACHROMATOPSIA)
            saveAccessibilityConfig(context, achromatopsiaConfig)

            // Aplicar gradiente de acromatopsia
            applyBackgroundGradient(context, activity.window.decorView, ColorblindType.ACHROMATOPSIA)

            // Aplicar colores de acromatopsia a toda la actividad
            applySpecificColorblindColors(context, activity.window.decorView, ColorblindType.ACHROMATOPSIA)

            // Forzar redibujado de todas las vistas
            refreshAllViews(activity.window.decorView)

            Log.d(TAG, "Colores de acromatopsia aplicados inmediatamente")
        } catch (e: Exception) {
            Log.e(TAG, "Error aplicando colores de acromatopsia: ${e.message}")
        }
    }

    /**
     * Restaura los colores originales inmediatamente a una actividad
     */
    fun restoreOriginalColors(context: Context) {
        try {
            // Guardar temporalmente configuración normal para que los colores persistan
            val normalConfig = getAccessibilityConfig(context).copy(colorblindType = ColorblindType.NONE)
            saveAccessibilityConfig(context, normalConfig)

            // Aplicar gradiente normal
            applyBackgroundGradient(context, (context as Activity).window.decorView, ColorblindType.NONE)

            // Aplicar colores normales
            applySpecificColorblindColors(context, (context as Activity).window.decorView, ColorblindType.NONE)

            // Forzar redibujado de todas las vistas
            refreshAllViews((context as Activity).window.decorView)

            Log.d(TAG, "Colores originales restaurados inmediatamente")
        } catch (e: Exception) {
            Log.e(TAG, "Error restaurando colores originales: ${e.message}")
        }
    }

    /**
     * Refresca todas las vistas para que los cambios de color sean visibles inmediatamente
     */
    private fun refreshAllViews(view: View) {
        try {
            // Invalidar la vista actual
            view.invalidate()
            
            // Si es un ViewGroup, refrescar todos los hijos recursivamente
            if (view is android.view.ViewGroup) {
                for (i in 0 until view.childCount) {
                    refreshAllViews(view.getChildAt(i))
                }
            }
            
            // Forzar un redibujado inmediato
            view.postInvalidate()
        } catch (e: Exception) {
            Log.e(TAG, "Error refrescando vistas: ${e.message}")
        }
    }

    /**
     * Verifica recursivamente si un ViewGroup contiene imágenes en cualquier nivel de su jerarquía
     */
    private fun containsImagesRecursively(viewGroup: android.view.ViewGroup): Boolean {
        // Evitar verificar el decorView raíz para no bloquear toda la aplicación
        if (viewGroup.id == android.R.id.content || viewGroup.javaClass.simpleName == "DecorView") {
            return false
        }

        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)

            // Verificar si el hijo directo es una imagen
            if (child is android.widget.ImageView ||
                child.javaClass.simpleName.contains("Image")) {
                return true
            }

            // Verificar si el hijo es un CardView (que podría contener imágenes)
            if (child is androidx.cardview.widget.CardView) {
                // Verificar si el CardView tiene un ImageView como hijo directo
                for (j in 0 until child.childCount) {
                    val cardChild = child.getChildAt(j)
                    if (cardChild is android.widget.ImageView ||
                        cardChild.javaClass.simpleName.contains("Image")) {
                        return true
                    }
                }
                // En caso de duda, no pintar sobre CardView
                return true
            }

            // Si es otro ViewGroup, verificar recursivamente
            if (child is android.view.ViewGroup) {
                if (containsImagesRecursively(child)) {
                    return true
                }
            }
        }
        return false
    }

    // Detecta si este ViewGroup contiene el RadioGroup de modos de daltonismo
    private fun containsDaltonismRadioGroup(viewGroup: android.view.ViewGroup): Boolean {
        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)
            if (child.id == R.id.rgModosDaltonismo) return true
            if (child is android.view.ViewGroup) {
                if (containsDaltonismRadioGroup(child)) return true
            }
        }
        return false
    }

    // Detecta si este ViewGroup contiene el RadioGroup de opciones del quiz
    private fun containsQuizRadioGroup(viewGroup: android.view.ViewGroup): Boolean {
        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)
            if (child.id == R.id.radioGroupOptions) return true
            if (child is android.view.ViewGroup) {
                if (containsQuizRadioGroup(child)) return true
            }
        }
        return false
    }
    // Verifica si algún ancestro contiene imágenes (para excluir pintura de fondos)
    private fun ancestorContainsImages(view: View): Boolean {
        var p = view.parent
        while (p is android.view.ViewGroup) {
            if (containsImagesRecursively(p)) return true
            p = p.parent
        }
        return false
    }

    /**
     * Función de limpieza final para remover overlays blancos de imágenes
     */
    fun removeWhiteOverlaysFromImages(context: Context, rootView: View) {
        try {
            if (rootView is android.view.ViewGroup) {
                for (i in 0 until rootView.childCount) {
                    val child = rootView.getChildAt(i)

                    // Si encontramos una imagen, verificar y limpiar su contenedor padre
                    if (child is android.widget.ImageView) {
                        val parent = child.parent
                        if (parent is android.view.ViewGroup) {
                            // Remover cualquier fondo blanco del contenedor padre
                            if (parent.background is android.graphics.drawable.ColorDrawable) {
                                val bgColor = (parent.background as android.graphics.drawable.ColorDrawable).color
                                if (isLightColor(bgColor)) {
                                    parent.setBackgroundColor(android.graphics.Color.TRANSPARENT)
                                }
                            }
                        }
                    }

                    // Recursivamente verificar hijos
                    if (child is android.view.ViewGroup) {
                        removeWhiteOverlaysFromImages(context, child)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error removiendo overlays blancos: ${e.message}")
        }
    }

    /**
     * Verifica si un color es claro (blanco o casi blanco)
     */
    private fun isLightColor(color: Int): Boolean {
        val alpha = android.graphics.Color.alpha(color)
        val red = android.graphics.Color.red(color)
        val green = android.graphics.Color.green(color)
        val blue = android.graphics.Color.blue(color)

        // Considerar colores con alta luminosidad como "claros"
        val brightness = (red * 0.299 + green * 0.587 + blue * 0.114) / 255.0
        return brightness > 0.7 && alpha > 150
    }

    /**
     * Aplica colores específicos para un tipo de daltonismo
     */
    fun applySpecificColorblindColors(context: Context, view: View, colorblindType: ColorblindType) {
        try {
            // No alterar el área del RadioGroup de daltonismo: mantener fondos blancos/transparentes
            if (view.id == R.id.rgModosDaltonismo && view is android.view.ViewGroup) {
                view.setBackgroundColor(android.graphics.Color.TRANSPARENT)
            }
            if (view is TextView) {
                // Aplicar color de texto negro para todos los modos
                val textColor = ContextCompat.getColor(context, android.R.color.black)
                view.setTextColor(textColor)
                
                // También aplicar color de fondo para TextViews que no sean transparentes,
                // excepto cuando sea un RadioButton dentro de áreas protegidas (daltonismo o quiz).
                if (view.background != null && view.background !is android.graphics.drawable.ColorDrawable) {
                    val isRadioInProtectedArea =
                        (view is android.widget.RadioButton || view is AppCompatRadioButton || view is com.google.android.material.radiobutton.MaterialRadioButton) &&
                        (isInDaltonismRadioArea(view) || isInQuizRadioArea(view))
                    if (!isRadioInProtectedArea) {
                        val backgroundColor = when (colorblindType) {
                            ColorblindType.NONE -> android.graphics.Color.TRANSPARENT
                            ColorblindType.PROTANOPIA -> ContextCompat.getColor(context, R.color.protanopia_background)
                            ColorblindType.DEUTERANOPIA -> ContextCompat.getColor(context, R.color.deuteranopia_background)
                            ColorblindType.TRITANOPIA -> ContextCompat.getColor(context, R.color.tritanopia_background)
                            ColorblindType.ACHROMATOPSIA -> ContextCompat.getColor(context, R.color.achromatopsia_white)
                        }
                        if (colorblindType != ColorblindType.NONE) {
                            view.setBackgroundColor(backgroundColor)
                        }
                    }
                }
            }
            
            if (view is com.google.android.material.button.MaterialButton) {
                // Verificar si es uno de los botones especiales que deben mantener su estilo
                val isSpecialButton = view.id == R.id.btnAccesibilidad || view.id == R.id.btnCreditos
                
                // Aplicar colores con texto negro para todos los botones Material
                val (backgroundColor, textColor, strokeColor) = when (colorblindType) {
                    ColorblindType.NONE -> {
                        if (isSpecialButton) {
                            // Botones especiales en modo normal: fondo beige oscuro con texto negro y bordes
                            Triple(
                                ContextCompat.getColor(context, R.color.light_secondary),
                                ContextCompat.getColor(context, android.R.color.black),
                                ContextCompat.getColor(context, R.color.light_primary)
                            )
                        } else {
                            // Botones normales en modo normal: colores cafés originales con texto negro
                            Triple(
                                ContextCompat.getColor(context, R.color.primary_brown),
                                ContextCompat.getColor(context, android.R.color.black),
                                ContextCompat.getColor(context, R.color.secondary_brown)
                            )
                        }
                    }
                    ColorblindType.PROTANOPIA -> Triple(
                        ContextCompat.getColor(context, R.color.protanopia_primary),
                        ContextCompat.getColor(context, android.R.color.black),
                        ContextCompat.getColor(context, R.color.protanopia_secondary)
                    )
                    ColorblindType.DEUTERANOPIA -> Triple(
                        ContextCompat.getColor(context, R.color.deuteranopia_primary),
                        ContextCompat.getColor(context, android.R.color.black),
                        ContextCompat.getColor(context, R.color.deuteranopia_secondary)
                    )
                    ColorblindType.TRITANOPIA -> Triple(
                        ContextCompat.getColor(context, R.color.tritanopia_primary),
                        ContextCompat.getColor(context, android.R.color.black),
                        ContextCompat.getColor(context, R.color.tritanopia_secondary)
                    )
                    ColorblindType.ACHROMATOPSIA -> Triple(
                        ContextCompat.getColor(context, R.color.achromatopsia_medium_gray),
                        ContextCompat.getColor(context, android.R.color.black),
                        ContextCompat.getColor(context, R.color.achromatopsia_dark_gray)
                    )
                }
                
                // Limpiar tints/fondos que pueden forzar café por XML
                ViewCompat.setBackgroundTintList(view, null)
                view.backgroundTintList = null
                view.backgroundTintMode = null
                view.setBackgroundResource(0)

                view.backgroundTintList = android.content.res.ColorStateList.valueOf(backgroundColor)
                view.backgroundTintMode = android.graphics.PorterDuff.Mode.SRC_IN
                // Forzar relleno en estilos Outlined usando el drawable de Material
                (view.background as? com.google.android.material.shape.MaterialShapeDrawable)?.let { shape ->
                    shape.fillColor = android.content.res.ColorStateList.valueOf(backgroundColor)
                    shape.strokeColor = android.content.res.ColorStateList.valueOf(strokeColor)
                }
                view.setBackgroundColor(backgroundColor)
                view.setTextColor(textColor)
                view.strokeColor = android.content.res.ColorStateList.valueOf(strokeColor)
                view.strokeWidth = if (isSpecialButton && colorblindType == ColorblindType.NONE) 2 else 3
                view.rippleColor = android.content.res.ColorStateList.valueOf(strokeColor)
                view.iconTint = android.content.res.ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.black))
            }

            // Soporte para botones estándar (android.widget.Button / AppCompatButton) con la misma paleta por modo
            if ((view is android.widget.Button || view is AppCompatButton) && !(view is android.widget.RadioButton || view is AppCompatRadioButton || view is com.google.android.material.radiobutton.MaterialRadioButton)) {
                val isSpecialButton = view.id == R.id.btnAccesibilidad || view.id == R.id.btnCreditos

                val (backgroundColor, textColor) = when (colorblindType) {
                    ColorblindType.NONE -> {
                        if (isSpecialButton) {
                            Pair(
                                ContextCompat.getColor(context, R.color.light_secondary),
                                ContextCompat.getColor(context, android.R.color.black)
                            )
                        } else {
                            Pair(
                                ContextCompat.getColor(context, R.color.primary_brown),
                                ContextCompat.getColor(context, android.R.color.black)
                            )
                        }
                    }
                    ColorblindType.PROTANOPIA -> Pair(
                        ContextCompat.getColor(context, R.color.protanopia_primary),
                        ContextCompat.getColor(context, android.R.color.black)
                    )
                    ColorblindType.DEUTERANOPIA -> Pair(
                        ContextCompat.getColor(context, R.color.deuteranopia_primary),
                        ContextCompat.getColor(context, android.R.color.black)
                    )
                    ColorblindType.TRITANOPIA -> Pair(
                        ContextCompat.getColor(context, R.color.tritanopia_primary),
                        ContextCompat.getColor(context, android.R.color.black)
                    )
                    ColorblindType.ACHROMATOPSIA -> Pair(
                        ContextCompat.getColor(context, R.color.achromatopsia_medium_gray),
                        ContextCompat.getColor(context, android.R.color.black)
                    )
                }

                // Limpiar tints/fondos que pueden forzar café por XML
                val btn = view as android.widget.TextView
                ViewCompat.setBackgroundTintList(btn, null)
                btn.backgroundTintList = null
                btn.setBackgroundResource(0)

                btn.backgroundTintList = android.content.res.ColorStateList.valueOf(backgroundColor)
                // Forzar fondo para asegurar visibilidad en botones estándar
                btn.setBackgroundColor(backgroundColor)
                btn.setTextColor(textColor)
            }

            // RadioButtons: asignar colores por modo
            if (view is android.widget.RadioButton || view is AppCompatRadioButton || view is com.google.android.material.radiobutton.MaterialRadioButton) {
                val rb = view as android.widget.CompoundButton
                val tintColor = when (colorblindType) {
                    ColorblindType.NONE -> ContextCompat.getColor(context, R.color.primary_brown)
                    ColorblindType.PROTANOPIA -> ContextCompat.getColor(context, R.color.protanopia_primary)
                    ColorblindType.DEUTERANOPIA -> ContextCompat.getColor(context, R.color.deuteranopia_primary)
                    ColorblindType.TRITANOPIA -> ContextCompat.getColor(context, R.color.tritanopia_primary)
                    ColorblindType.ACHROMATOPSIA -> ContextCompat.getColor(context, R.color.achromatopsia_medium_gray)
                }
                val inProtectedArea = isInDaltonismRadioArea(view) || isInQuizRadioArea(view)
                val appliedTint = if (inProtectedArea) ContextCompat.getColor(context, R.color.primary_brown) else tintColor
                rb.buttonTintList = android.content.res.ColorStateList.valueOf(appliedTint)
                if (inProtectedArea) {
                    (rb as android.widget.TextView).background = ContextCompat.getDrawable(context, R.drawable.radio_option_background)
                    (rb as android.widget.TextView).backgroundTintList = null
                }
                (rb as android.widget.TextView).setTextColor(ContextCompat.getColor(context, android.R.color.black))
            }
            
            if (view is androidx.cardview.widget.CardView) {
                // Mantener blanco sólido si contiene el RadioGroup de daltonismo o el de quiz
                if (isInDaltonismRadioArea(view) || containsQuizRadioGroup(view)) {
                    view.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
                } else {
                    // Verificar si es el header card que debe mantener su color café
                    val isHeaderCard = view.id == R.id.headerCard
                    
                    // Aplicar color de fondo a tarjetas según el tipo de daltonismo
                    val cardColor = when (colorblindType) {
                        ColorblindType.NONE -> {
                            if (isHeaderCard) {
                                // Header card en modo normal: mantener color café
                                ContextCompat.getColor(context, R.color.primary_brown)
                            } else {
                                // Otras tarjetas en modo normal: usar los colores del tema actual
                                if (context.resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
                                    ContextCompat.getColor(context, R.color.dark_secondary)
                                } else {
                                    ContextCompat.getColor(context, R.color.light_secondary)
                                }
                            }
                        }
                        ColorblindType.PROTANOPIA -> ContextCompat.getColor(context, R.color.protanopia_background)
                        ColorblindType.DEUTERANOPIA -> ContextCompat.getColor(context, R.color.deuteranopia_background)
                        ColorblindType.TRITANOPIA -> ContextCompat.getColor(context, R.color.tritanopia_background)
                        ColorblindType.ACHROMATOPSIA -> ContextCompat.getColor(context, R.color.achromatopsia_white)
                    }
                    view.setCardBackgroundColor(cardColor)
                }
            }
            
            if (view is android.widget.ImageView) {
                // EN MODO DALTONISMO ESTÁ PROHIBIDO TOCAR LAS IMÁGENES
                // Eliminar cualquier filtro o tinte de imágenes
                view.clearColorFilter()

                // Asegurar que las imágenes no tengan fondo ni bordes en modo daltonismo
                if (colorblindType != ColorblindType.NONE) {
                    view.setBackgroundColor(android.graphics.Color.TRANSPARENT)
                }

                // IMPORTANTE: Remover cualquier overlay blanco que pueda haber sido aplicado
                // a contenedores padre de imágenes
                val parent = view.parent
                if (parent is android.view.ViewGroup && colorblindType != ColorblindType.NONE) {
                    // Si el padre tiene un fondo blanco, removerlo para evitar overlays
                    if (parent.background is android.graphics.drawable.ColorDrawable) {
                        val bgColor = (parent.background as android.graphics.drawable.ColorDrawable).color
                        if (bgColor == android.graphics.Color.WHITE ||
                            bgColor == ContextCompat.getColor(context, R.color.white) ||
                            android.graphics.Color.alpha(bgColor) > 200) { // Colores muy claros
                            parent.setBackgroundColor(android.graphics.Color.TRANSPARENT)
                        }
                    }
                }
            }
            
            // Aplicar color de fondo solo a vistas seguras (evitar overlays sobre imágenes)
            // (Los gradientes se aplican por separado en applyBackgroundGradient)
            if (view.background is android.graphics.drawable.ColorDrawable) {
                val isRadioInProtectedArea =
                    (view is android.widget.RadioButton || view is AppCompatRadioButton || view is com.google.android.material.radiobutton.MaterialRadioButton) &&
                    (isInDaltonismRadioArea(view) || isInQuizRadioArea(view))

                val shouldPaintBackground =
                    (view is TextView || view is com.google.android.material.button.MaterialButton) &&
                    !ancestorContainsImages(view) &&
                    !isRadioInProtectedArea

                if (shouldPaintBackground) {
                    val backgroundDrawable = view.background as android.graphics.drawable.ColorDrawable
                    val backgroundColor = when (colorblindType) {
                        ColorblindType.NONE -> backgroundDrawable.color
                        ColorblindType.PROTANOPIA -> ContextCompat.getColor(context, R.color.protanopia_background)
                        ColorblindType.DEUTERANOPIA -> ContextCompat.getColor(context, R.color.deuteranopia_background)
                        ColorblindType.TRITANOPIA -> ContextCompat.getColor(context, R.color.tritanopia_background)
                        ColorblindType.ACHROMATOPSIA -> ContextCompat.getColor(context, R.color.achromatopsia_white)
                    }
                    view.setBackgroundColor(backgroundColor)
                }
            }
            
            // APLICAR COLOR DE FONDO A LAYOUTS Y CONTENEDORES EN MODO DALTONISMO
            // EXCLUYENDO contenedores con imágenes y el área que contiene el RadioGroup de daltonismo
            if (view is android.view.ViewGroup &&
                colorblindType != ColorblindType.NONE &&
                !containsDaltonismRadioGroup(view)) {

                // Verificar recursivamente si este ViewGroup contiene imágenes en cualquier nivel
                val containsImages = containsImagesRecursively(view)

                // Solo aplicar color de fondo si no contiene imágenes en ningún nivel
                if (!containsImages) {
                    val backgroundColor = when (colorblindType) {
                        ColorblindType.PROTANOPIA -> ContextCompat.getColor(context, R.color.protanopia_background)
                        ColorblindType.DEUTERANOPIA -> ContextCompat.getColor(context, R.color.deuteranopia_background)
                        ColorblindType.TRITANOPIA -> ContextCompat.getColor(context, R.color.tritanopia_background)
                        ColorblindType.ACHROMATOPSIA -> ContextCompat.getColor(context, R.color.achromatopsia_white)
                        else -> android.graphics.Color.TRANSPARENT
                    }

                    // Aplicar color de fondo solo a contenedores permitidos (no RadioGroup de daltonismo)
                    view.setBackgroundColor(backgroundColor)
                }
            }
            
            // Aplicar recursivamente a hijos
            if (view is android.view.ViewGroup) {
                for (i in 0 until view.childCount) {
                    applySpecificColorblindColors(context, view.getChildAt(i), colorblindType)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error aplicando colores específicos: ${e.message}")
        }
    }
}

// === Force button palette application helpers (appended) ===
private fun forceApplyButtonPalette(context: Context, root: View, colorblindType: AccesibilityHelper.ColorblindType) {
    try {
        when (root) {
            // IMPORTANT: handle radio buttons FIRST so they don't fall into the generic Button branch
            is com.google.android.material.radiobutton.MaterialRadioButton,
            is android.widget.RadioButton,
            is AppCompatRadioButton -> {
                val rb = root as android.widget.CompoundButton
                val tint = when (colorblindType) {
                    AccesibilityHelper.ColorblindType.NONE -> ContextCompat.getColor(context, R.color.primary_brown)
                    AccesibilityHelper.ColorblindType.PROTANOPIA -> ContextCompat.getColor(context, R.color.protanopia_primary)
                    AccesibilityHelper.ColorblindType.DEUTERANOPIA -> ContextCompat.getColor(context, R.color.deuteranopia_primary)
                    AccesibilityHelper.ColorblindType.TRITANOPIA -> ContextCompat.getColor(context, R.color.tritanopia_primary)
                    AccesibilityHelper.ColorblindType.ACHROMATOPSIA -> ContextCompat.getColor(context, R.color.achromatopsia_medium_gray)
                }
                rb.buttonTintList = android.content.res.ColorStateList.valueOf(tint)
                (rb as android.widget.TextView).setTextColor(ContextCompat.getColor(context, android.R.color.black))
                // Do NOT set any background color here to keep options white
            }

            is com.google.android.material.button.MaterialButton -> {
                val (bg, txt, stroke) = when (colorblindType) {
                    AccesibilityHelper.ColorblindType.NONE -> Triple(
                        ContextCompat.getColor(context, R.color.primary_brown),
                        ContextCompat.getColor(context, android.R.color.black),
                        ContextCompat.getColor(context, R.color.secondary_brown)
                    )
                    AccesibilityHelper.ColorblindType.PROTANOPIA -> Triple(
                        ContextCompat.getColor(context, R.color.protanopia_primary),
                        ContextCompat.getColor(context, android.R.color.black),
                        ContextCompat.getColor(context, R.color.protanopia_secondary)
                    )
                    AccesibilityHelper.ColorblindType.DEUTERANOPIA -> Triple(
                        ContextCompat.getColor(context, R.color.deuteranopia_primary),
                        ContextCompat.getColor(context, android.R.color.black),
                        ContextCompat.getColor(context, R.color.deuteranopia_secondary)
                    )
                    AccesibilityHelper.ColorblindType.TRITANOPIA -> Triple(
                        ContextCompat.getColor(context, R.color.tritanopia_primary),
                        ContextCompat.getColor(context, android.R.color.black),
                        ContextCompat.getColor(context, R.color.tritanopia_secondary)
                    )
                    AccesibilityHelper.ColorblindType.ACHROMATOPSIA -> Triple(
                        ContextCompat.getColor(context, R.color.achromatopsia_medium_gray),
                        ContextCompat.getColor(context, android.R.color.black),
                        ContextCompat.getColor(context, R.color.achromatopsia_dark_gray)
                    )
                }
                root.backgroundTintList = android.content.res.ColorStateList.valueOf(bg)
                root.backgroundTintMode = android.graphics.PorterDuff.Mode.SRC_IN
                // Important for outlined buttons which are transparent by default
                root.setBackgroundColor(bg)
                root.setTextColor(txt)
                root.strokeColor = android.content.res.ColorStateList.valueOf(stroke)
                if (root.strokeWidth <= 0) root.strokeWidth = 3
                root.rippleColor = android.content.res.ColorStateList.valueOf(stroke)
                root.iconTint = android.content.res.ColorStateList.valueOf(txt)
            }

            // Generic Buttons, but EXCLUDE radio buttons (which inherit from AppCompatButton)
            is android.widget.Button, is AppCompatButton -> {
                if (root is android.widget.RadioButton || root is AppCompatRadioButton || root is com.google.android.material.radiobutton.MaterialRadioButton) {
                    // already handled above
                } else {
                    val btn = root as android.widget.TextView
                    val bg = when (colorblindType) {
                        AccesibilityHelper.ColorblindType.NONE -> ContextCompat.getColor(context, R.color.primary_brown)
                        AccesibilityHelper.ColorblindType.PROTANOPIA -> ContextCompat.getColor(context, R.color.protanopia_primary)
                        AccesibilityHelper.ColorblindType.DEUTERANOPIA -> ContextCompat.getColor(context, R.color.deuteranopia_primary)
                        AccesibilityHelper.ColorblindType.TRITANOPIA -> ContextCompat.getColor(context, R.color.tritanopia_primary)
                        AccesibilityHelper.ColorblindType.ACHROMATOPSIA -> ContextCompat.getColor(context, R.color.achromatopsia_medium_gray)
                    }
                    ViewCompat.setBackgroundTintList(btn, null)
                    btn.backgroundTintList = null
                    btn.setBackgroundResource(0)
                    btn.backgroundTintList = android.content.res.ColorStateList.valueOf(bg)
                    // Important to force fill on platform/AppCompat Buttons
                    btn.setBackgroundColor(bg)
                    btn.setTextColor(ContextCompat.getColor(context, android.R.color.black))
                }
            }
        }

        if (root is android.view.ViewGroup) {
            for (i in 0 until root.childCount) {
                forceApplyButtonPalette(context, root.getChildAt(i), colorblindType)
            }
        }
    } catch (e: Exception) {
        Log.e(TAG, "forceApplyButtonPalette error: ${e.message}")
    }
}

// Hook to call after next layout to guarantee palette sticks
private fun reapplyButtonsAfterLayout(context: Context, colorblindType: AccesibilityHelper.ColorblindType) {
    val activity = context as? Activity ?: return
    val root = activity.window.decorView
    root.viewTreeObserver.addOnGlobalLayoutListener(object : android.view.ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            try {
                forceApplyButtonPalette(context, root, colorblindType)
                root.invalidate()
            } catch (_: Exception) { }
            finally { root.viewTreeObserver.removeOnGlobalLayoutListener(this) }
        }
    })
}