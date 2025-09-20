package com.villalobos.caballoapp

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.villalobos.caballoapp.databinding.ActivityTutorialBinding

class TutorialActivity : BaseNavigationActivity() {

    private lateinit var enlace: ActivityTutorialBinding
    private lateinit var tutorialAdapter: TutorialAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private var pasoActual = 0

    private lateinit var btnBackTutorial: Button
    
    // Preferencia para no mostrar más el tutorial
    private val PREF_NO_MOSTRAR_TUTORIAL = "no_mostrar_tutorial"
    
    private val pasosTutorial = listOf(
        TutorialPaso(
            numero = 1,
            titulo = "¡Bienvenido a CaballoApp!",
            descripcion = "Aprende anatomía muscular del Caballo Criollo Colombiano de forma interactiva, científica y completamente gratuita.",
            imagen = R.drawable.zona_parieto_temporal,
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
            imagen = R.drawable.zona_cervico_lateral
        ),
        TutorialPaso(
            numero = 3,
            titulo = "Exploración por Regiones",
            descripcion = "Selecciona una región anatómica (Cabeza, Cuello, Tronco, Miembros) para ver sus músculos específicos con imágenes detalladas.",
            imagen = R.drawable.musculo_interoseo
        ),
        TutorialPaso(
            numero = 4,
            titulo = "Interacción con Músculos",
            descripcion = "Toca cualquier punto destacado en las imágenes para ver información completa: origen, inserción y función biomecánica del músculo.",
            imagen = R.drawable.planta_casco
        ),
        TutorialPaso(
            numero = 5,
            titulo = "Funciones Avanzadas",
            descripcion = "Usa la búsqueda para encontrar músculos rápidamente y explora los detalles anatómicos de cada músculo.",
            imagen = R.drawable.group_1,
            mostrarCaracteristicas = true,
            caracteristicas = listOf(
                "Búsqueda por nombre de músculo",
                "Filtros por región anatómica",
                "Navegación detallada por músculos",
                "Hotspots interactivos en imágenes"
            )
        ),
        TutorialPaso(
            numero = 6,
            titulo = "¡Listo para Comenzar!",
            descripcion = "Ya conoces todas las funciones principales. Recuerda que puedes acceder a configuraciones de accesibilidad desde el menú principal si las necesitas.",
            imagen = R.drawable.zona_mandibular
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
            
            // Configurar el botón de inicio
            setupHomeButton(enlace.btnHome)
            
            // Aplicar colores de accesibilidad
            applyActivityAccessibilityColors()
            
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
            // Bind back button
            btnBackTutorial = enlace.btnBackTutorial
            btnBackTutorial.setOnClickListener {
                finish()
            }

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
            // Guardar la preferencia del usuario sobre no mostrar más el tutorial
            val noMostrarMas = enlace.cbNoMostrarMas.isChecked
            sharedPreferences.edit()
                .putBoolean(PREF_NO_MOSTRAR_TUTORIAL, noMostrarMas)
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
    
    override fun applyActivityAccessibilityColors() {
        ErrorHandler.safeExecute(
            context = this,
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al aplicar colores de accesibilidad en TutorialActivity"
        ) {
            // Aplicar colores de accesibilidad a los elementos de la actividad
            AccesibilityHelper.applyAccessibilityColorsToApp(this)
        }
    }
}