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
                    // En modo normal, mantener el gradiente café original
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

            // Aplicar colores a toda la vista de manera más agresiva
            applyAccessibilityColorsToView(context, activity.window.decorView)

            // Forzar redibujado completo de la vista
            activity.window.decorView.invalidate()
            
            // Forzar actualización del layout
            activity.window.decorView.requestLayout()
            
            // Aplicar colores nuevamente después de un pequeño delay para asegurar persistencia
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                applySpecificColorblindColors(context, activity.window.decorView, config.colorblindType)
                activity.window.decorView.invalidate()
                Log.d(TAG, "Colores de accesibilidad aplicados agresivamente: ${config.colorblindType.displayName}")
            }, 100)
        } catch (e: Exception) {
            Log.e(TAG, "Error aplicando colores de accesibilidad: ${e.message}")
        }
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
                // Aplicar colores a botones
                val backgroundColor = getAccessibleColor(context, R.color.primary_brown)
                view.backgroundTintList = android.content.res.ColorStateList.valueOf(backgroundColor)
                
                val textColor = getAccessibleColor(context, R.color.white)
                view.setTextColor(textColor)
            }
            
            if (view is androidx.cardview.widget.CardView) {
                // Aplicar color de fondo a tarjetas
                val cardColor = getAccessibleColor(context, R.color.white)
                view.setCardBackgroundColor(cardColor)
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
            
            // Aplicar color de fondo si la vista tiene un fondo de color
            if (view.background is android.graphics.drawable.ColorDrawable) {
                val backgroundDrawable = view.background as android.graphics.drawable.ColorDrawable
                val backgroundColor = getAccessibleColor(context, backgroundDrawable.color)
                view.setBackgroundColor(backgroundColor)
            }
            
            // Aplicar recursivamente a hijos
            if (view is android.view.ViewGroup) {
                for (i in 0 until view.childCount) {
                    applyAccessibilityColorsToView(context, view.getChildAt(i))
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
     * Aplica colores específicos para un tipo de daltonismo
     */
    fun applySpecificColorblindColors(context: Context, view: View, colorblindType: ColorblindType) {
        try {
            if (view is TextView) {
                // Aplicar color de texto según el tipo de daltonismo
                val textColor = when (colorblindType) {
                    ColorblindType.NONE -> ContextCompat.getColor(context, R.color.text_primary)
                    ColorblindType.PROTANOPIA -> ContextCompat.getColor(context, R.color.protanopia_text_primary)
                    ColorblindType.DEUTERANOPIA -> ContextCompat.getColor(context, R.color.deuteranopia_text_primary)
                    ColorblindType.TRITANOPIA -> ContextCompat.getColor(context, R.color.tritanopia_text_primary)
                    ColorblindType.ACHROMATOPSIA -> ContextCompat.getColor(context, R.color.achromatopsia_black)
                }
                view.setTextColor(textColor)
                
                // También aplicar color de fondo para TextViews que no sean transparentes
                if (view.background != null && view.background !is android.graphics.drawable.ColorDrawable) {
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
            
            if (view is com.google.android.material.button.MaterialButton) {
                // Aplicar colores específicos para botones según el tipo de daltonismo
                val (backgroundColor, textColor, strokeColor) = when (colorblindType) {
                    ColorblindType.NONE -> Triple(
                        ContextCompat.getColor(context, R.color.primary_brown),
                        ContextCompat.getColor(context, R.color.white),
                        ContextCompat.getColor(context, R.color.secondary_brown)
                    )
                    ColorblindType.PROTANOPIA -> Triple(
                        ContextCompat.getColor(context, R.color.protanopia_primary),
                        ContextCompat.getColor(context, R.color.protanopia_button_text),
                        ContextCompat.getColor(context, R.color.protanopia_secondary)
                    )
                    ColorblindType.DEUTERANOPIA -> Triple(
                        ContextCompat.getColor(context, R.color.deuteranopia_primary),
                        ContextCompat.getColor(context, R.color.deuteranopia_button_text),
                        ContextCompat.getColor(context, R.color.deuteranopia_secondary)
                    )
                    ColorblindType.TRITANOPIA -> Triple(
                        ContextCompat.getColor(context, R.color.tritanopia_primary),
                        ContextCompat.getColor(context, R.color.tritanopia_button_text),
                        ContextCompat.getColor(context, R.color.tritanopia_secondary)
                    )
                    ColorblindType.ACHROMATOPSIA -> Triple(
                        ContextCompat.getColor(context, R.color.achromatopsia_medium_gray),
                        ContextCompat.getColor(context, R.color.achromatopsia_button_text),
                        ContextCompat.getColor(context, R.color.achromatopsia_dark_gray)
                    )
                }
                
                view.backgroundTintList = android.content.res.ColorStateList.valueOf(backgroundColor)
                view.setTextColor(textColor)
                view.strokeColor = android.content.res.ColorStateList.valueOf(strokeColor)
                view.strokeWidth = 3 // Hacer los bordes más visibles
            }
            
            if (view is androidx.cardview.widget.CardView) {
                // Aplicar color de fondo a tarjetas según el tipo de daltonismo
                val cardColor = when (colorblindType) {
                    ColorblindType.NONE -> ContextCompat.getColor(context, R.color.white)
                    ColorblindType.PROTANOPIA -> ContextCompat.getColor(context, R.color.protanopia_background)
                    ColorblindType.DEUTERANOPIA -> ContextCompat.getColor(context, R.color.deuteranopia_background)
                    ColorblindType.TRITANOPIA -> ContextCompat.getColor(context, R.color.tritanopia_background)
                    ColorblindType.ACHROMATOPSIA -> ContextCompat.getColor(context, R.color.achromatopsia_white)
                }
                view.setCardBackgroundColor(cardColor)
            }
            
            if (view is android.widget.ImageView) {
                // EN MODO DALTONISMO ESTÁ PROHIBIDO TOCAR LAS IMÁGENES
                // Eliminar cualquier filtro o tinte de imágenes
                view.clearColorFilter()
                
                // Asegurar que las imágenes no tengan fondo ni bordes en modo daltonismo
                if (colorblindType != ColorblindType.NONE) {
                    view.setBackgroundColor(android.graphics.Color.TRANSPARENT)
                }
            }
            
            // Aplicar color de fondo si la vista tiene un fondo de color SOLO SI NO ES UN GRADIENTE O SHAPE
            // (Los gradientes se aplican por separado en applyBackgroundGradient)
            if (view.background is android.graphics.drawable.ColorDrawable) {
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
            
            // APLICAR COLOR DE FONDO A TODOS LOS LAYOUTS Y CONTENEDORES EN MODO DALTONISMO
            if (view is android.view.ViewGroup && colorblindType != ColorblindType.NONE) {
                // Aplicar color de fondo a layouts y contenedores
                val backgroundColor = when (colorblindType) {
                    ColorblindType.PROTANOPIA -> ContextCompat.getColor(context, R.color.protanopia_background)
                    ColorblindType.DEUTERANOPIA -> ContextCompat.getColor(context, R.color.deuteranopia_background)
                    ColorblindType.TRITANOPIA -> ContextCompat.getColor(context, R.color.tritanopia_background)
                    ColorblindType.ACHROMATOPSIA -> ContextCompat.getColor(context, R.color.achromatopsia_white)
                    else -> android.graphics.Color.TRANSPARENT
                }
                
                // Aplicar color de fondo a todos los contenedores, incluso si ya tienen un fondo especial
                view.setBackgroundColor(backgroundColor)
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