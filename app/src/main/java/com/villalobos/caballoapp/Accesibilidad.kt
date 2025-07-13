package com.villalobos.caballoapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.villalobos.caballoapp.databinding.ActivityAccesibilidadBinding

class Accesibilidad : AppCompatActivity() {
    
    private lateinit var enlace: ActivityAccesibilidadBinding
    private var configActual = AccesibilityHelper.AccessibilityConfig()
    
    enum class TipoDaltonismo {
        NORMAL, PROTANOPIA, DEUTERANOPIA, TRITANOPIA
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            enlace = ActivityAccesibilidadBinding.inflate(layoutInflater)
            setContentView(enlace.root)
            
            // Cargar configuración actual
            cargarConfiguracionActual()
            
            // Configurar listeners
            configurarListeners()
            
            // Inicializar vistas de previsualización
            inicializarVistasPrevia()
            
            // Configurar colores iniciales
            configurarColoresIniciales()
            
            // Cargar configuración en las vistas
            cargarConfiguracionEnVistas()
            
        } catch (e: Exception) {
            ErrorHandler.handleError(
                context = this,
                throwable = e,
                errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
                userMessage = "Error al inicializar configuración de accesibilidad",
                canRecover = true,
                recoveryAction = { finish() }
            )
        }
    }
    
    private fun cargarConfiguracionActual() {
        configActual = AccesibilityHelper.getAccessibilityConfig(this)
    }
    
    private fun configurarListeners() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al configurar controles"
        ) {
            // Radio group para daltonismo - usando el ID correcto del layout
            enlace.rgModosDaltonismo.setOnCheckedChangeListener { _, checkedId ->
                val nuevoTipo = when (checkedId) {
                    R.id.rbNormal -> AccesibilityHelper.ColorblindType.NONE
                    R.id.rbProtanopia -> AccesibilityHelper.ColorblindType.PROTANOPIA
                    R.id.rbDeuteranopia -> AccesibilityHelper.ColorblindType.DEUTERANOPIA
                    R.id.rbTritanopia -> AccesibilityHelper.ColorblindType.TRITANOPIA
                    else -> AccesibilityHelper.ColorblindType.NONE
                }
                configActual = configActual.copy(colorblindType = nuevoTipo)
                actualizarVistaPreviaColores()
                actualizarInterfazConConfiguracion()
            }
            
            // Botón reset
            enlace.btnResetColors.setOnClickListener {
                configActual = AccesibilityHelper.AccessibilityConfig()
                cargarConfiguracionEnVistas()
                actualizarVistaPreviaColores()
                actualizarInterfazConConfiguracion()
            }
            
            // Botón desactivar daltonismo
            enlace.btnDesactivarDaltonismo.setOnClickListener {
                configActual = configActual.copy(colorblindType = AccesibilityHelper.ColorblindType.NONE)
                enlace.rbNormal.isChecked = true
                actualizarVistaPreviaColores()
                actualizarInterfazConConfiguracion()
            }
        }
        
        // Botones de acción
        enlace.btnVolverAccesibilidad.setOnClickListener {
            finish() // Volver sin guardar
        }
        
        enlace.btnGuardarAccesibilidad.setOnClickListener {
            guardarConfiguracion()
        }
        
        // Botón tutorial
        enlace.btnReiniciarTutorial.setOnClickListener {
            reiniciarTutorial()
        }
    }
    
    private fun cargarConfiguracionEnVistas() {
        // Actualizar RadioButtons según configuración actual
        when (configActual.colorblindType) {
            AccesibilityHelper.ColorblindType.NONE -> enlace.rbNormal.isChecked = true
            AccesibilityHelper.ColorblindType.PROTANOPIA -> enlace.rbProtanopia.isChecked = true
            AccesibilityHelper.ColorblindType.DEUTERANOPIA -> enlace.rbDeuteranopia.isChecked = true
            AccesibilityHelper.ColorblindType.TRITANOPIA -> enlace.rbTritanopia.isChecked = true
            AccesibilityHelper.ColorblindType.ACHROMATOPSIA -> {
                // Si hay acromatopsia, usar el más cercano
                enlace.rbNormal.isChecked = true
            }
        }
    }
    
    private fun reiniciarTutorial() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.NAVIGATION_ERROR,
            errorMessage = "Error al abrir tutorial"
        ) {
            val intent = Intent(this, TutorialActivity::class.java)
            startActivity(intent)
        }
    }
    
    private fun inicializarVistasPrevia() {
        // Configurar colores iniciales para la vista previa
        actualizarVistaPreviaColores()
    }
    
    private fun configurarColoresIniciales() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al configurar colores iniciales"
        ) {
            // Configurar colores actuales en las vistas usando AccesibilityHelper
            val colorPrimario = AccesibilityHelper.getAccessibleColor(this, R.color.primary_brown)
            val colorSecundario = AccesibilityHelper.getAccessibleColor(this, R.color.secondary_brown)
            val colorTexto = AccesibilityHelper.getAccessibleColor(this, R.color.text_dark)
            
            enlace.viewColorPrimary.setBackgroundColor(colorPrimario)
            enlace.viewColorSecondary.setBackgroundColor(colorSecundario)
            enlace.viewColorText.setBackgroundColor(colorTexto)
        }
    }
    
    private fun actualizarInterfazConConfiguracion() {
        // Mostrar/ocultar botón de desactivar
        enlace.btnDesactivarDaltonismo.visibility = 
            if (configActual.colorblindType != AccesibilityHelper.ColorblindType.NONE) View.VISIBLE 
            else View.GONE
    }
    
    private fun actualizarVistaPreviaColores() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al actualizar vista previa"
        ) {
            when (configActual.colorblindType) {
                AccesibilityHelper.ColorblindType.NONE -> {
                    enlace.previewColor1.setBackgroundColor(Color.parseColor("#FF0000")) // Rojo
                    enlace.previewColor2.setBackgroundColor(Color.parseColor("#00FF00")) // Verde
                    enlace.previewColor3.setBackgroundColor(Color.parseColor("#0000FF")) // Azul
                    enlace.previewColor4.setBackgroundColor(Color.parseColor("#FFFF00")) // Amarillo
                    mostrarMensajeConfiguracion("Visión normal de colores")
                }
                AccesibilityHelper.ColorblindType.PROTANOPIA -> {
                    // Simular protanopia usando colores más distinguibles
                    enlace.previewColor1.setBackgroundColor(Color.parseColor("#B8860B")) // Rojo → Marrón
                    enlace.previewColor2.setBackgroundColor(Color.parseColor("#00FF00")) // Verde
                    enlace.previewColor3.setBackgroundColor(Color.parseColor("#0000FF")) // Azul
                    enlace.previewColor4.setBackgroundColor(Color.parseColor("#FFFF00")) // Amarillo
                    mostrarMensajeConfiguracion("Protanopia: Dificultad para distinguir rojos")
                }
                AccesibilityHelper.ColorblindType.DEUTERANOPIA -> {
                    enlace.previewColor1.setBackgroundColor(Color.parseColor("#FF0000")) // Rojo
                    enlace.previewColor2.setBackgroundColor(Color.parseColor("#B8860B")) // Verde → Marrón
                    enlace.previewColor3.setBackgroundColor(Color.parseColor("#0000FF")) // Azul
                    enlace.previewColor4.setBackgroundColor(Color.parseColor("#FFFF00")) // Amarillo
                    mostrarMensajeConfiguracion("Deuteranopia: Dificultad para distinguir verdes")
                }
                AccesibilityHelper.ColorblindType.TRITANOPIA -> {
                    enlace.previewColor1.setBackgroundColor(Color.parseColor("#FF0000")) // Rojo
                    enlace.previewColor2.setBackgroundColor(Color.parseColor("#00FF00")) // Verde
                    enlace.previewColor3.setBackgroundColor(Color.parseColor("#FF1493")) // Azul → Rosa
                    enlace.previewColor4.setBackgroundColor(Color.parseColor("#FF69B4")) // Amarillo → Rosa
                    mostrarMensajeConfiguracion("Tritanopia: Dificultad para distinguir azules y amarillos")
                }
                AccesibilityHelper.ColorblindType.ACHROMATOPSIA -> {
                    // Mostrar en escala de grises
                    enlace.previewColor1.setBackgroundColor(Color.parseColor("#666666"))
                    enlace.previewColor2.setBackgroundColor(Color.parseColor("#999999"))
                    enlace.previewColor3.setBackgroundColor(Color.parseColor("#333333"))
                    enlace.previewColor4.setBackgroundColor(Color.parseColor("#CCCCCC"))
                    mostrarMensajeConfiguracion("Acromatopsia: Visión en escala de grises")
                }
            }
        }
    }
    
    private fun mostrarMensajeConfiguracion(mensaje: String) {
        // Mostrar mensaje informativo sobre el tipo de daltonismo
        android.widget.Toast.makeText(this, mensaje, android.widget.Toast.LENGTH_SHORT).show()
    }
    
    private fun guardarConfiguracion() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al guardar configuración"
        ) {
            // Guardar configuración usando AccesibilityHelper
            AccesibilityHelper.saveAccessibilityConfig(this, configActual)
            
            val tipoString = when (configActual.colorblindType) {
                AccesibilityHelper.ColorblindType.NONE -> "Colores estándar"
                AccesibilityHelper.ColorblindType.PROTANOPIA -> "Protanopia"
                AccesibilityHelper.ColorblindType.DEUTERANOPIA -> "Deuteranopia"
                AccesibilityHelper.ColorblindType.TRITANOPIA -> "Tritanopia"
                AccesibilityHelper.ColorblindType.ACHROMATOPSIA -> "Acromatopsia"
            }
            
            val mensaje = "✅ Configuración de accesibilidad guardada:\n• Tipo: $tipoString"
            
            android.widget.Toast.makeText(this, mensaje, android.widget.Toast.LENGTH_LONG).show()
            
            // Volver a la pantalla anterior
            finish()
        }
    }
    
    override fun onDestroy() {
        try {
            super.onDestroy()
        } catch (e: Exception) {
            ErrorHandler.handleError(
                context = this,
                throwable = e,
                errorType = ErrorHandler.ErrorType.MEMORY_ERROR,
                level = ErrorHandler.ErrorLevel.WARNING,
                userMessage = "Error al cerrar configuración",
                canRecover = false
            )
        }
    }
}