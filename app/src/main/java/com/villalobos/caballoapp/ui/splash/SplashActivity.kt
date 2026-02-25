package com.villalobos.caballoapp.ui.splash

import com.villalobos.caballoapp.R

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.villalobos.caballoapp.util.ErrorHandler
import com.villalobos.caballoapp.util.ImageAnimationHelper
import com.villalobos.caballoapp.databinding.ActivitySplashBinding
import com.villalobos.caballoapp.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * Splash Screen de la aplicación.
 * Implementa MVVM delegando la lógica al SplashViewModel.
 */
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            // Configurar pantalla completa para splash
            // Ocultar action bar si existe
            supportActionBar?.hide()

            binding = ActivitySplashBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // Configurar animaciones sutiles
            setupAnimations()

            // Configurar textos dinámicos
            setupTexts()

            // Observar eventos del ViewModel
            observeViewModel()

            // Iniciar temporizador via ViewModel
            viewModel.startSplashTimer()

        } catch (e: Exception) {
            // En caso de error, ir directamente a MainActivity
            ErrorHandler.handleError(
                context = this,
                throwable = e,
                errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
                userMessage = "Error en pantalla de carga",
                canRecover = true,
                recoveryAction = { navigateToMainActivity() }
            )
        }
    }

    private fun observeViewModel() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is SplashViewModel.SplashEvent.NavigateToMain -> {
                    viewModel.clearEvent()
                    navigateToMainActivity()
                }
                null -> { /* No event */ }
            }
        }
    }

    private fun setupAnimations() {
        // Animación de los logos superiores (entrada secuencial)
        binding.ivLogoUSC.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(800)
            .setStartDelay(300)
            .start()

        binding.ivLogoInforma.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(800)
            .setStartDelay(600)
            .start()

        binding.ivLogoEquusnova.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(800)
            .setStartDelay(900)
            .start()

        // Animación sutil del logo principal (escala)
        binding.ivLogo.animate()
            .scaleX(1.1f)
            .scaleY(1.1f)
            .setDuration(2000)
            .setStartDelay(1200)
            .start()

        // Animación del texto de carga (fade in/out)
        binding.tvLoading.animate()
            .alpha(0.5f)
            .setDuration(1000)
            .setStartDelay(1500)
            .start()
    }

    private fun setupTexts() {
        try {
            // Obtener versión de la app desde BuildConfig o package info
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            val versionName = packageInfo.versionName ?: "1.0"
            binding.tvVersion.text = "v$versionName"

            // Configurar accesibilidad
            binding.ivLogoUSC.contentDescription = "Logo Universidad Santiago de Cali"
            binding.ivLogoInforma.contentDescription = "Logo Informa"
            binding.ivLogoEquusnova.contentDescription = "Logo Equusnova"
            binding.ivLogo.contentDescription = "Logo de EquusApp - Anatomía Muscular Equina"
            binding.tvAppName.contentDescription = "Nombre de la aplicación: EquusApp"
            binding.tvTagline.contentDescription = "Subtítulo: Anatomía Muscular Equina"
            binding.tvLoading.contentDescription = "Cargando aplicación"
            binding.tvVersion.contentDescription = "Versión de la aplicación: $versionName"

            // Animar el logo
            ImageAnimationHelper.animateLogoImage(binding.ivLogo)

        } catch (e: Exception) {
            // En caso de error al obtener versión, usar valores por defecto
            binding.tvVersion.text = "v1.0"
        }
    }

    private fun navigateToMainActivity() {
        try {
            val intent = Intent(this, MainActivity::class.java)
            // Flags para asegurar navegación limpia
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

            // Animación de transición suave (API moderna para Android 14+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                overrideActivityTransition(
                    OVERRIDE_TRANSITION_OPEN,
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
                )
            } else {
                @Suppress("DEPRECATION")
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }

            finish() // Cerrar splash activity

        } catch (e: Exception) {
            ErrorHandler.handleError(
                context = this,
                throwable = e,
                errorType = ErrorHandler.ErrorType.NAVIGATION_ERROR,
                userMessage = "Error al iniciar aplicación",
                canRecover = false
            )
        }
    }
}
