package com.villalobos.caballoapp.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.accessibility.AccessibilityManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.villalobos.caballoapp.R
import com.villalobos.caballoapp.data.model.AccessibilityConfig
import com.villalobos.caballoapp.data.model.ColorblindType
import com.villalobos.caballoapp.data.model.TextScale
import com.villalobos.caballoapp.ui.components.InteractiveAnatomyView
import androidx.core.graphics.ColorUtils

/**
 * Objeto de utilidad para gestionar la accesibilidad en la aplicación.
 * Proporciona métodos para configurar descripciones de contenido, colores para daltónicos
 * y navegación por teclado.
 */
object AccesibilityHelper {

    private const val PREFS_NAME = "accessibility_prefs"
    private const val KEY_COLORBLIND_TYPE = "colorblind_type"
    private const val KEY_TEXT_SCALE = "text_scale"
    private const val KEY_HIGH_CONTRAST = "high_contrast"

    /**
     * Configura la descripción de contenido para una vista, mejorando la accesibilidad.
     *
     * @param view La vista a la que se le asignará la descripción.
     * @param description El texto que describirá la vista para los servicios de accesibilidad.
     * @param role El rol de la vista (ej. "Botón", "Encabezado").
     */
    fun setContentDescription(view: View, description: String, role: String) {
        view.contentDescription = "$role: $description"
    }

    /**
     * Aplica colores específicos para daltónicos a los BOTONES de una vista.
     * NO modifica las imágenes.
     *
     * @param context Contexto para acceder a los recursos.
     * @param rootView La vista raíz a la que se aplicarán los colores.
     * @param type El tipo de daltonismo para el que se aplicarán los colores.
     */
    fun applySpecificColorblindColors(context: Context, rootView: View, type: ColorblindType) {
        applySpecificColorblindColors(context, rootView, type, highContrast = false)
    }

    fun applySpecificColorblindColors(
        context: Context,
        rootView: View,
        type: ColorblindType,
        highContrast: Boolean
    ) {
        if (highContrast) {
            applyColorsToButtonsRecursively(rootView, Color.BLACK, Color.WHITE)
            return
        }

        val primaryColor = getColorForType(context, type, isPrimary = true)
        val secondaryColor = getColorForType(context, type, isPrimary = false)

        applyColorsToButtonsRecursively(rootView, primaryColor, secondaryColor)
    }
    
    /**
     * Obtiene el color apropiado según el tipo de daltonismo.
     */
    private fun getColorForType(context: Context, type: ColorblindType, isPrimary: Boolean): Int {
        return when (type) {
            ColorblindType.PROTANOPIA -> {
                if (isPrimary) ContextCompat.getColor(context, R.color.protanopia_primary)
                else ContextCompat.getColor(context, R.color.protanopia_secondary)
            }
            ColorblindType.DEUTERANOPIA -> {
                if (isPrimary) ContextCompat.getColor(context, R.color.deuteranopia_primary)
                else ContextCompat.getColor(context, R.color.deuteranopia_secondary)
            }
            ColorblindType.TRITANOPIA -> {
                if (isPrimary) ContextCompat.getColor(context, R.color.tritanopia_primary)
                else ContextCompat.getColor(context, R.color.tritanopia_secondary)
            }
            ColorblindType.ACHROMATOPSIA -> {
                if (isPrimary) ContextCompat.getColor(context, R.color.achromatopsia_dark_gray)
                else ContextCompat.getColor(context, R.color.achromatopsia_light_gray)
            }
            ColorblindType.NORMAL, ColorblindType.NONE -> {
                if (isPrimary) ContextCompat.getColor(context, R.color.primary_brown)
                else ContextCompat.getColor(context, R.color.warm_cream)
            }
        }
    }
    
    /**
     * Aplica colores a los botones de forma recursiva.
     * NO modifica ImageViews.
     */
    private fun applyColorsToButtonsRecursively(view: View, primaryColor: Int, secondaryColor: Int) {
        // DEFENSIVE CHECK: Explicitly ignore ImageView (except ImageButton handled below)
        if (view is android.widget.ImageView && view !is android.widget.ImageButton) {
            // android.util.Log.d("AccesibilityHelper", "⛔ Ignorando ImageView/ImageButton: ${view.id}")
            return
        }
        
        // Ignore specific containers that might hold images
        val viewIdName = try { 
            view.context.resources.getResourceEntryName(view.id) 
        } catch (e: Exception) { 
            "unknown" 
        }
        
        if (viewIdName == "imageCard" || viewIdName == "ivHorse" || viewIdName == "imgCaballo") {
            // android.util.Log.d("AccesibilityHelper", "⛔ Ignorando contenedor de imagen protegido: $viewIdName")
            return
        }

        // Check specifically for the Header Card or Container
        if ((viewIdName == "headerCard" && view is com.google.android.material.card.MaterialCardView) ||
            (viewIdName == "headerContainer" && view is android.view.ViewGroup)) {
             
             if (view is com.google.android.material.card.MaterialCardView) {
                 view.setCardBackgroundColor(primaryColor)
             } else {
                 view.setBackgroundColor(primaryColor)
             }
             // android.util.Log.d("AccesibilityHelper", "✅ Aplicado color Header a: $viewIdName")
        }

        when (view) {
            is com.google.android.material.button.MaterialButton -> {
                // Detectar el ID del botón para aplicar colores apropiados
                val buttonId = view.id
                val buttonIdName = try { 
                    view.context.resources.getResourceEntryName(buttonId) 
                } catch (e: Exception) { 
                    "unknown" 
                }
                
                // Obtener texto del botón si es posible
                val buttonText = view.text?.toString() ?: ""
                
                // Botones que deberían ser "primarios" (oscuros con texto blanco)
                val isPrimaryButton = 
                    // Chequeo por ID
                    buttonIdName.contains("btnIniciar", ignoreCase = true) ||
                    buttonIdName.contains("btnGuardar", ignoreCase = true) ||
                    buttonIdName.contains("Siguiente", ignoreCase = true) ||
                    buttonIdName.contains("Anterior", ignoreCase = true) ||
                    buttonIdName.contains("btnExplorar", ignoreCase = true) ||
                    buttonIdName.contains("btnHome", ignoreCase = true) || // Home button is primary
                    buttonIdName.contains("btnRegion", ignoreCase = true) || // All region buttons
                    // Chequeo por Texto
                    buttonText.contains("Iniciar", ignoreCase = true) ||
                    buttonText.contains("Guardar", ignoreCase = true) || 
                    buttonText.contains("Siguiente", ignoreCase = true) ||
                    buttonText.contains("Anterior", ignoreCase = true) ||
                    buttonText.contains("Explorar", ignoreCase = true) ||
                    buttonText.contains("Continuar", ignoreCase = true) ||
                    buttonText.contains("Región", ignoreCase = true) || 
                    buttonText.contains("Region", ignoreCase = true)
                
                if (isPrimaryButton) {
                    // Botón primario: fondo oscuro, texto blanco
                    view.backgroundTintList = ColorStateList.valueOf(primaryColor)
                    view.strokeColor = ColorStateList.valueOf(primaryColor)
                    view.rippleColor = ColorStateList.valueOf(ColorUtils.blendARGB(primaryColor, Color.WHITE, 0.18f))
                    view.setTextColor(Color.WHITE)
                    view.iconTint = ColorStateList.valueOf(Color.WHITE)
                } else {
                    // Botón secundario: fondo claro, texto oscuro
                    view.backgroundTintList = ColorStateList.valueOf(secondaryColor)
                    view.strokeColor = ColorStateList.valueOf(secondaryColor)
                    view.rippleColor = ColorStateList.valueOf(ColorUtils.blendARGB(secondaryColor, Color.BLACK, 0.12f))
                    // Calcular contraste de texto para asegurar legibilidad (WCAG)
                    // Usamos blanco o negro/gris oscuro dependiendo del brillo del fondo
                    val luminance = ColorUtils.calculateLuminance(secondaryColor)
                    
                    if (luminance > 0.5) {
                        // Fondo claro -> Texto oscuro
                        val darkText = Color.parseColor("#1C1B1F") // Negro suave Material 3
                        view.setTextColor(darkText)
                        view.iconTint = ColorStateList.valueOf(darkText)
                    } else {
                        // Fondo oscuro -> Texto blanco
                        view.setTextColor(Color.WHITE)
                        view.iconTint = ColorStateList.valueOf(Color.WHITE)
                    }
                    // android.util.Log.d("AccesibilityHelper", "✅ Aplicado SECUNDARIO ($secondaryColor) a: $buttonIdName")
                }
            }
            is android.widget.ImageButton -> {
                view.backgroundTintList = ColorStateList.valueOf(primaryColor)
                view.setColorFilter(Color.WHITE)
            }
            is android.widget.Button -> {
                if (view !is android.widget.CompoundButton) {
                    view.setBackgroundColor(primaryColor)
                    view.setTextColor(Color.WHITE)
                }
            }
        }
        
        // Recursivamente aplicar a hijos
        if (view is android.view.ViewGroup) {
            for (i in 0 until view.childCount) {
                applyColorsToButtonsRecursively(view.getChildAt(i), primaryColor, secondaryColor)
            }
        }
    }


    /**
     * Aplica un gradiente de fondo según el tipo de daltonismo.
     */
    fun applyBackgroundGradient(context: Context, rootView: View, type: ColorblindType) {
        applyBackgroundGradient(context, rootView, type, highContrast = false)
    }

    fun applyBackgroundGradient(
        context: Context,
        rootView: View,
        type: ColorblindType,
        highContrast: Boolean
    ) {
        if (highContrast) {
            val highContrastGradient = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(Color.BLACK, Color.WHITE)
            )
            rootView.background = highContrastGradient
            return
        }

        val colors = when (type) {
            ColorblindType.PROTANOPIA -> intArrayOf(
                ContextCompat.getColor(context, R.color.protanopia_primary),
                ContextCompat.getColor(context, R.color.protanopia_secondary)
            )
            ColorblindType.DEUTERANOPIA -> intArrayOf(
                ContextCompat.getColor(context, R.color.deuteranopia_primary),
                ContextCompat.getColor(context, R.color.deuteranopia_secondary)
            )
            ColorblindType.TRITANOPIA -> intArrayOf(
                ContextCompat.getColor(context, R.color.tritanopia_primary),
                ContextCompat.getColor(context, R.color.tritanopia_secondary)
            )
            ColorblindType.ACHROMATOPSIA -> intArrayOf(
                Color.GRAY,
                Color.LTGRAY
            )
            ColorblindType.NORMAL, ColorblindType.NONE -> intArrayOf(
                ContextCompat.getColor(context, R.color.primary_brown),
                ContextCompat.getColor(context, R.color.secondary_brown)
            )
        }
        
        val gradient = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors)
        rootView.background = gradient
    }

    /**
     * Obtiene la configuración de accesibilidad guardada en las preferencias.
     *
     * @param context Contexto para acceder a SharedPreferences.
     * @return La configuración de accesibilidad actual.
     */
    fun getAccessibilityConfig(context: Context): AccessibilityConfig {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val colorblindTypeStr = prefs.getString(KEY_COLORBLIND_TYPE, "NORMAL") ?: "NORMAL"
        val textScaleStr = prefs.getString(KEY_TEXT_SCALE, "NORMAL") ?: "NORMAL"
        val highContrast = prefs.getBoolean(KEY_HIGH_CONTRAST, false)
        
        val colorblindType = ColorblindType.fromString(colorblindTypeStr)
        val textScale = try {
            TextScale.valueOf(textScaleStr)
        } catch (e: Exception) {
            TextScale.NORMAL
        }
        
        return AccessibilityConfig(
            colorblindType = colorblindType,
            textScale = textScale,
            highContrast = highContrast
        )
    }

    /**
     * Guarda la configuración de accesibilidad en las preferencias.
     */
    fun saveAccessibilityConfig(context: Context, config: AccessibilityConfig) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putString(KEY_COLORBLIND_TYPE, config.colorblindType.name)
            putString(KEY_TEXT_SCALE, config.textScale.name)
            putBoolean(KEY_HIGH_CONTRAST, config.highContrast)
            apply()
        }
    }

    /**
     * Aplica los colores de accesibilidad a toda la app.
     */
    fun applyAccessibilityColorsToApp(activity: Activity) {
        val config = getAccessibilityConfig(activity)
        val contentView = activity.findViewById<View>(android.R.id.content)

        val newMode = config.colorblindType.name
        val lastMode = contentView.getTag(R.id.tag_accessibility_mode) as? String
        val lastContrast = contentView.getTag(R.id.tag_accessibility_high_contrast) as? Boolean
        val lastTextScale = contentView.getTag(R.id.tag_accessibility_text_scale) as? String

        if (lastMode == newMode &&
            lastContrast == config.highContrast &&
            lastTextScale == config.textScale.name
        ) {
            return
        }

        applyAccessibilityConfig(activity, config)
    }

    fun applyAccessibilityConfig(activity: Activity, config: AccessibilityConfig) {
        val contentView = activity.findViewById<View>(android.R.id.content)

        applyBackgroundGradient(
            activity,
            contentView,
            config.colorblindType,
            config.highContrast
        )

        applySpecificColorblindColors(
            activity,
            contentView,
            config.colorblindType,
            config.highContrast
        )

        applyTextScaleRecursively(contentView, config.textScale.scaleFactor)

        contentView.setTag(R.id.tag_accessibility_mode, config.colorblindType.name)
        contentView.setTag(R.id.tag_accessibility_high_contrast, config.highContrast)
        contentView.setTag(R.id.tag_accessibility_text_scale, config.textScale.name)
    }

    /**
     * Restaura los colores originales.
     */
    fun restoreOriginalColors(activity: Activity) {
        applyAccessibilityConfig(activity, AccessibilityConfig())
    }

    private fun applyTextScaleRecursively(view: View, scaleFactor: Float) {
        if (view is TextView) {
            val originalSize = (view.getTag(R.id.tag_accessibility_original_text_size) as? Float)
                ?: view.textSize.also {
                    view.setTag(R.id.tag_accessibility_original_text_size, it)
                }

            view.setTextSize(TypedValue.COMPLEX_UNIT_PX, originalSize * scaleFactor)
        }

        if (view is android.view.ViewGroup) {
            for (i in 0 until view.childCount) {
                applyTextScaleRecursively(view.getChildAt(i), scaleFactor)
            }
        }
    }

    /**
     * Reinicia la app para aplicar cambios de colores.
     */
    fun restartAppForColorChanges(activity: Activity) {
        val intent = activity.packageManager.getLaunchIntentForPackage(activity.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
        activity.finish()
    }

    /**
     * Configura la accesibilidad del dispositivo, como el tamaño de la fuente.
     *
     * @param context Contexto de la aplicación.
     * @param view La vista a la que se aplicarán los ajustes.
     */
    fun setupDeviceAccessibility(context: Context) {
        val accessibilityManager = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        if (accessibilityManager.isEnabled) {
            // Aplicar configuraciones de accesibilidad del dispositivo
        }
    }

    /**
     * Asigna una descripción de contenido a la imagen anatómica.
     *
     * @param view La vista de la imagen anatómica interactiva.
     * @param regionName El nombre de la región que se muestra.
     * @param muscleCount El número de músculos en la región.
     */
    fun setAnatomicalImageDescription(view: InteractiveAnatomyView, regionName: String, muscleCount: Int) {
        view.contentDescription = "Imagen anatómica de la región de $regionName, mostrando $muscleCount músculos interactivos."
    }

    /**
     * Habilita la navegación por teclado entre la imagen y la lista de músculos.
     *
     * @param anatomyView La vista de la imagen anatómica.
     * @param recyclerView La lista de músculos.
     */
    fun enableKeyboardNavigation(anatomyView: View, recyclerView: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            anatomyView.focusable = View.FOCUSABLE
            recyclerView.focusable = View.FOCUSABLE
            anatomyView.nextFocusForwardId = recyclerView.id
            recyclerView.nextFocusForwardId = anatomyView.id
        }
    }
}
