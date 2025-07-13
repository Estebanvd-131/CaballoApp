package com.villalobos.caballoapp

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.villalobos.caballoapp.databinding.ActivityTutorialBinding

class TutorialActivity : AppCompatActivity() {

    private lateinit var enlace: ActivityTutorialBinding
    private lateinit var tutorialAdapter: TutorialAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private var pasoActual = 0
    
    private val pasosTutorial = listOf(
        TutorialPaso(
            numero = 1,
            titulo = "¡Bienvenido a CaballoApp!",
            descripcion = "Aprende anatomía muscular del Caballo Criollo Colombiano de forma interactiva, científica y completamente gratuita.",
            imagen = R.drawable.miologiaequinocompleto,
            mostrarCaracteristicas = true,
            caracteristicas = listOf(
                "Exploración por regiones anatómicas",
                "Información detallada de cada músculo",
                "Búsqueda rápida de músculos",
                "Funciona sin conexión a internet"
            )
        ),
        TutorialPaso(
            numero = 2,
            titulo = "Navegación Principal",
            descripcion = "Desde la pantalla principal puedes acceder a todas las funciones: explorar regiones, buscar músculos y ajustar accesibilidad.",
            imagen = R.drawable.cabeza_lateral
        ),
        TutorialPaso(
            numero = 3,
            titulo = "Exploración por Regiones",
            descripcion = "Selecciona una región anatómica (Cabeza, Cuello, Tronco, Miembros) para ver sus músculos específicos con imágenes detalladas.",
            imagen = R.drawable.cuello_y_torax
        ),
        TutorialPaso(
            numero = 4,
            titulo = "Interacción con Músculos",
            descripcion = "Toca cualquier punto destacado en las imágenes para ver información completa: origen, inserción y función biomecánica del músculo.",
            imagen = R.drawable.hombro_miembro_anterior
        ),
        TutorialPaso(
            numero = 5,
            titulo = "Funciones Avanzadas",
            descripcion = "Usa la búsqueda para encontrar músculos rápidamente y aprovecha el zoom en vista enfocada para explorar detalles.",
            imagen = R.drawable.grupa_miembro_posterior,
            mostrarCaracteristicas = true,
            caracteristicas = listOf(
                "Búsqueda por nombre de músculo",
                "Filtros por región anatómica",
                "Zoom y navegación en detalle",
                "Hotspots interactivos en imágenes"
            )
        ),
        TutorialPaso(
            numero = 6,
            titulo = "¡Listo para Comenzar!",
            descripcion = "Ya conoces todas las funciones principales. Recuerda que puedes acceder a configuraciones de accesibilidad desde el menú principal si las necesitas.",
            imagen = R.drawable.torsoequino
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            enlace = ActivityTutorialBinding.inflate(layoutInflater)
            setContentView(enlace.root)

            // Inicializar SharedPreferences
            sharedPreferences = getSharedPreferences("tutorial_prefs", MODE_PRIVATE)

            // Configurar ViewPager2
            configurarViewPager()
            
            // Configurar botones
            configurarBotones()
            
            // Configurar estado inicial
            actualizarInterfaz()
            
        } catch (e: Exception) {
            ErrorHandler.handleError(
                context = this,
                throwable = e,
                errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
                userMessage = "Error al inicializar tutorial",
                canRecover = true,
                recoveryAction = { finalizarTutorial() }
            )
        }
    }

    private fun configurarViewPager() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al configurar tutorial"
        ) {
            tutorialAdapter = TutorialAdapter(this, pasosTutorial)
            enlace.viewPagerTutorial.adapter = tutorialAdapter
            
            // Configurar listener para cambios de página
            enlace.viewPagerTutorial.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    pasoActual = position
                    actualizarInterfaz()
                }
            })
        }
    }

    private fun configurarBotones() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al configurar botones"
        ) {
            // Botón anterior
            enlace.btnAnterior.setOnClickListener {
                if (pasoActual > 0) {
                    enlace.viewPagerTutorial.currentItem = pasoActual - 1
                }
            }
            
            // Botón siguiente/finalizar
            enlace.btnSiguiente.setOnClickListener {
                if (pasoActual < pasosTutorial.size - 1) {
                    enlace.viewPagerTutorial.currentItem = pasoActual + 1
                } else {
                    finalizarTutorial()
                }
            }
            
            // Botón saltar
            enlace.btnSaltarTutorial.setOnClickListener {
                finalizarTutorial()
            }
        }
    }

    private fun actualizarInterfaz() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al actualizar interfaz"
        ) {
            // Actualizar indicador de paso
            enlace.tvPasoActual.text = "${pasoActual + 1}/${pasosTutorial.size}"
            
            // Actualizar barra de progreso
            enlace.progressBarTutorial.progress = pasoActual + 1
            
            // Actualizar estado de botones
            enlace.btnAnterior.isEnabled = pasoActual > 0
            
            // Cambiar texto del botón siguiente en el último paso
            if (pasoActual == pasosTutorial.size - 1) {
                enlace.btnSiguiente.text = "¡Comenzar!"
            } else {
                enlace.btnSiguiente.text = "Siguiente →"
            }
        }
    }

    private fun finalizarTutorial() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al finalizar tutorial"
        ) {
            // Marcar tutorial como completado
            sharedPreferences.edit()
                .putBoolean("tutorial_completado", true)
                .apply()
            
            // Cerrar actividad
            finish()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Permitir retroceder en el tutorial o salir
        if (pasoActual > 0) {
            enlace.viewPagerTutorial.currentItem = pasoActual - 1
        } else {
            finalizarTutorial()
        }
        super.onBackPressed()
    }
} 