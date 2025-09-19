package com.villalobos.caballoapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.RadioGroup
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.villalobos.caballoapp.databinding.ActivityAccesibilidadBinding

class Accesibilidad : AccessibilityActivity() {
    
    override fun applyActivityAccessibilityColors() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al aplicar colores de accesibilidad específicos de la actividad"
        ) {
            // Aplicar colores específicos para los elementos de la actividad de accesibilidad
            AccesibilityHelper.applySpecificColorblindColors(this, window.decorView, configActual.colorblindType)
            
            // Aplicar gradiente de fondo
            AccesibilityHelper.applyBackgroundGradient(this, window.decorView, configActual.colorblindType)
        }
    }
    
    private lateinit var enlace: ActivityAccesibilidadBinding
    private var configActual = AccesibilityHelper.AccessibilityConfig()
    
    enum class TipoDaltonismo {
        NORMAL, PROTANOPIA, DEUTERANOPIA, TRITANOPIA, ACROMATOPSIA
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            enlace = ActivityAccesibilidadBinding.inflate(getLayoutInflater())
            setContentView(enlace.root)
            
            // Cargar configuración actual
            cargarConfiguracionActual()
            
            // Configurar listeners
            configurarListeners()
            
            // Inicializar vistas de previsualización
            inicializarVistasPrevia()
            
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
            // Radio group para daltonismo con selección única y deselección
            val radioButtons = listOf(
                enlace.rbNormal,
                enlace.rbProtanopia,
                enlace.rbDeuteranopia,
                enlace.rbTritanopia,
                enlace.rbAcromatopsia
            )
            
            // Configurar listeners individuales para cada RadioButton
            radioButtons.forEach { radioButton ->
                radioButton.setOnClickListener {
                    if (radioButton.isChecked) {
                        // Desmarcar todos los demás botones
                        radioButtons.forEach { otherButton ->
                            if (otherButton != radioButton) {
                                otherButton.isChecked = false
                            }
                        }
                        
                        val nuevoTipo = when (radioButton.id) {
                            R.id.rbNormal -> AccesibilityHelper.ColorblindType.NONE
                            R.id.rbProtanopia -> AccesibilityHelper.ColorblindType.PROTANOPIA
                            R.id.rbDeuteranopia -> AccesibilityHelper.ColorblindType.DEUTERANOPIA
                            R.id.rbTritanopia -> AccesibilityHelper.ColorblindType.TRITANOPIA
                            R.id.rbAcromatopsia -> AccesibilityHelper.ColorblindType.ACHROMATOPSIA
                            else -> AccesibilityHelper.ColorblindType.NONE
                        }
                        configActual = configActual.copy(colorblindType = nuevoTipo)
                        aplicarColoresInmediatamente()
                        actualizarVistaPreviaColores()
                        actualizarInterfazConConfiguracion()
                    } else {
                        // Si se hace clic en un botón ya seleccionado, se deselecciona
                        configActual = configActual.copy(colorblindType = AccesibilityHelper.ColorblindType.NONE)
                        aplicarColoresInmediatamente()
                        actualizarVistaPreviaColores()
                        actualizarInterfazConConfiguracion()
                    }
                }
            }
            
            // Listener para mantener la compatibilidad con navegación por teclado
            enlace.rgModosDaltonismo.setOnCheckedChangeListener { _, checkedId ->
                if (checkedId != -1) { // -1 significa que se deseleccionó todo
                    val nuevoTipo = when (checkedId) {
                        R.id.rbNormal -> AccesibilityHelper.ColorblindType.NONE
                        R.id.rbProtanopia -> AccesibilityHelper.ColorblindType.PROTANOPIA
                        R.id.rbDeuteranopia -> AccesibilityHelper.ColorblindType.DEUTERANOPIA
                        R.id.rbTritanopia -> AccesibilityHelper.ColorblindType.TRITANOPIA
                        R.id.rbAcromatopsia -> AccesibilityHelper.ColorblindType.ACHROMATOPSIA
                        else -> AccesibilityHelper.ColorblindType.NONE
                    }
                    configActual = configActual.copy(colorblindType = nuevoTipo)
                    aplicarColoresInmediatamente()
                    actualizarVistaPreviaColores()
                    actualizarInterfazConConfiguracion()
                }
            }
            
            
            // Botón desactivar daltonismo
            enlace.btnDesactivarDaltonismo.setOnClickListener {
                configActual = configActual.copy(colorblindType = AccesibilityHelper.ColorblindType.NONE)
                enlace.rbNormal.isChecked = true
                aplicarColoresInmediatamente()
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
            AccesibilityHelper.ColorblindType.ACHROMATOPSIA -> enlace.rbAcromatopsia.isChecked = true
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
    
    
    private fun actualizarInterfazConConfiguracion() {
        // Mostrar/ocultar botón de desactivar
        enlace.btnDesactivarDaltonismo.visibility =
            if (configActual.colorblindType != AccesibilityHelper.ColorblindType.NONE) View.VISIBLE
            else View.GONE
    }
    
    private fun aplicarColoresInmediatamente() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al aplicar colores de daltonismo"
        ) {
            // Guardar temporalmente la configuración actual para que los colores se mantengan
            AccesibilityHelper.saveAccessibilityConfig(this, configActual)
            
            // Aplicar los colores de daltonismo inmediatamente a la actividad actual
            when (configActual.colorblindType) {
                AccesibilityHelper.ColorblindType.NONE -> {
                    // Restaurar colores originales
                    AccesibilityHelper.restoreOriginalColors(this)
                }
                AccesibilityHelper.ColorblindType.PROTANOPIA -> {
                    AccesibilityHelper.adjustForProtanopia(this, this)
                }
                AccesibilityHelper.ColorblindType.DEUTERANOPIA -> {
                    AccesibilityHelper.adjustForDeuteranopia(this, this)
                }
                AccesibilityHelper.ColorblindType.TRITANOPIA -> {
                    AccesibilityHelper.adjustForTritanopia(this, this)
                }
                AccesibilityHelper.ColorblindType.ACHROMATOPSIA -> {
                    AccesibilityHelper.adjustForAchromatopsia(this, this)
                }
            }
            
            // Forzar actualización inmediata y agresiva de la interfaz
            window.decorView.post {
                // Aplicar colores específicos de forma inmediata
                AccesibilityHelper.applySpecificColorblindColors(this, window.decorView, configActual.colorblindType)
                
                // Forzar redibujado completo
                window.decorView.invalidate()
                
                // Volver a aplicar los colores después de un pequeño delay para asegurar que persistan
                Handler(Looper.getMainLooper()).postDelayed({
                    // Aplicar gradiente de fondo de forma agresiva
                    AccesibilityHelper.applyBackgroundGradient(this, window.decorView, configActual.colorblindType)
                    
                    // Aplicar colores a todas las vistas de forma recursiva y agresiva
                    AccesibilityHelper.applySpecificColorblindColors(this, window.decorView, configActual.colorblindType)
                    
                    // Forzar actualización de todos los componentes de la interfaz
                    window.decorView.requestLayout()
                    window.decorView.invalidate()
                    
                    // Aplicar colores nuevamente para asegurar que persistan
                    Handler(Looper.getMainLooper()).postDelayed({
                        AccesibilityHelper.applySpecificColorblindColors(this, window.decorView, configActual.colorblindType)
                        window.decorView.invalidate()
                        
                    }, 100)
                }, 200)
            }
        }
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
                    enlace.previewColor1.setBackgroundColor(Color.parseColor("#FF0000")) // Rojo (distinguible)
                    enlace.previewColor2.setBackgroundColor(Color.parseColor("#8B0000")) // Rojo oscuro (distinguible)
                    enlace.previewColor3.setBackgroundColor(Color.parseColor("#0000FF")) // Azul (distinguible)
                    enlace.previewColor4.setBackgroundColor(Color.parseColor("#FF1493")) // Rosa magenta (distinguible)
                    mostrarMensajeConfiguracion("Deuteranopia: Dificultad para distinguir verdes - usa rojos, azules y magentas")
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
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
    
    private fun guardarConfiguracion() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al guardar configuración"
        ) {
            // Guardar configuración usando AccesibilityHelper
            AccesibilityHelper.saveAccessibilityConfig(this, configActual)
            
            // Aplicar inmediatamente la configuración para asegurar que persista
            AccesibilityHelper.applyAccessibilityColorsToApp(this)
            
            val tipoString = when (configActual.colorblindType) {
                AccesibilityHelper.ColorblindType.NONE -> "Colores estándar"
                AccesibilityHelper.ColorblindType.PROTANOPIA -> "Protanopia"
                AccesibilityHelper.ColorblindType.DEUTERANOPIA -> "Deuteranopia"
                AccesibilityHelper.ColorblindType.TRITANOPIA -> "Tritanopia"
                AccesibilityHelper.ColorblindType.ACHROMATOPSIA -> "Acromatopsia"
            }
            
            val mensaje = "✅ Configuración guardada. Reiniciando aplicación con: $tipoString"
            
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
            
            // Reiniciar la aplicación para aplicar completamente los cambios de colores
            Handler(Looper.getMainLooper()).postDelayed({
                AccesibilityHelper.restartAppForColorChanges(this)
            }, 1000) // Pequeño delay para que el usuario vea el mensaje
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