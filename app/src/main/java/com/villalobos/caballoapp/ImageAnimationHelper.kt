package com.villalobos.caballoapp

import android.view.View
import android.view.animation.*
import android.widget.ImageView

object ImageAnimationHelper {

    enum class AnimationType {
        FADE_IN,
        SCALE_IN,
        SLIDE_IN_LEFT,
        SLIDE_IN_RIGHT,
        SLIDE_IN_TOP,
        SLIDE_IN_BOTTOM,
        BOUNCE_IN,
        ROTATE_IN
    }

    /**
     * Aplica una animación a una ImageView
     * @param imageView La ImageView a animar
     * @param animationType Tipo de animación a aplicar
     * @param duration Duración de la animación en milisegundos (por defecto 500ms)
     * @param delay Retraso antes de iniciar la animación en milisegundos (por defecto 0)
     */
    fun animateImage(
        imageView: ImageView,
        animationType: AnimationType,
        duration: Long = 500,
        delay: Long = 0
    ) {
        try {
            val animation = createAnimation(animationType, duration)

            if (delay > 0) {
                animation.startOffset = delay
            }

            // Configurar listener para limpiar estado después de la animación
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    imageView.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animation: Animation?) {
                    // Limpiar transformación si es necesario
                    imageView.clearAnimation()
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })

            imageView.startAnimation(animation)

        } catch (e: Exception) {
            ErrorHandler.handleError(
                context = imageView.context,
                throwable = e,
                errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
                userMessage = "Error al animar imagen",
                canRecover = false
            )
        }
    }

    /**
     * Aplica una animación con callback personalizado
     */
    fun animateImageWithCallback(
        imageView: ImageView,
        animationType: AnimationType,
        duration: Long = 500,
        delay: Long = 0,
        onAnimationEnd: (() -> Unit)? = null
    ) {
        try {
            val animation = createAnimation(animationType, duration)

            if (delay > 0) {
                animation.startOffset = delay
            }

            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    imageView.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animation: Animation?) {
                    imageView.clearAnimation()
                    onAnimationEnd?.invoke()
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })

            imageView.startAnimation(animation)

        } catch (e: Exception) {
            ErrorHandler.handleError(
                context = imageView.context,
                throwable = e,
                errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
                userMessage = "Error al animar imagen con callback",
                canRecover = false
            )
        }
    }

    /**
     * Crea la animación específica según el tipo
     */
    private fun createAnimation(animationType: AnimationType, duration: Long): Animation {
        return when (animationType) {
            AnimationType.FADE_IN -> createFadeInAnimation(duration)
            AnimationType.SCALE_IN -> createScaleInAnimation(duration)
            AnimationType.SLIDE_IN_LEFT -> createSlideInAnimation(duration, fromX = -1.0f, toX = 0.0f)
            AnimationType.SLIDE_IN_RIGHT -> createSlideInAnimation(duration, fromX = 1.0f, toX = 0.0f)
            AnimationType.SLIDE_IN_TOP -> createSlideInAnimation(duration, fromY = -1.0f, toY = 0.0f)
            AnimationType.SLIDE_IN_BOTTOM -> createSlideInAnimation(duration, fromY = 1.0f, toY = 0.0f)
            AnimationType.BOUNCE_IN -> createBounceInAnimation(duration)
            AnimationType.ROTATE_IN -> createRotateInAnimation(duration)
        }
    }

    private fun createFadeInAnimation(duration: Long): Animation {
        val animation = AlphaAnimation(0.0f, 1.0f)
        animation.duration = duration
        animation.interpolator = AccelerateDecelerateInterpolator()
        return animation
    }

    private fun createScaleInAnimation(duration: Long): Animation {
        val animation = ScaleAnimation(
            0.3f, 1.0f, // fromX, toX
            0.3f, 1.0f, // fromY, toY
            Animation.RELATIVE_TO_SELF, 0.5f, // pivotX
            Animation.RELATIVE_TO_SELF, 0.5f  // pivotY
        )
        animation.duration = duration
        animation.interpolator = OvershootInterpolator()
        return animation
    }

    private fun createSlideInAnimation(
        duration: Long,
        fromX: Float = 0.0f,
        toX: Float = 0.0f,
        fromY: Float = 0.0f,
        toY: Float = 0.0f
    ): Animation {
        val animation = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, fromX,
            Animation.RELATIVE_TO_PARENT, toX,
            Animation.RELATIVE_TO_PARENT, fromY,
            Animation.RELATIVE_TO_PARENT, toY
        )
        animation.duration = duration
        animation.interpolator = DecelerateInterpolator()
        return animation
    }

    private fun createBounceInAnimation(duration: Long): Animation {
        val animation = ScaleAnimation(
            0.3f, 1.05f, // fromX, toX (overshoot)
            0.3f, 1.05f, // fromY, toY (overshoot)
            Animation.RELATIVE_TO_SELF, 0.5f, // pivotX
            Animation.RELATIVE_TO_SELF, 0.5f  // pivotY
        )
        animation.duration = duration
        animation.interpolator = BounceInterpolator()

        // Crear secuencia: bounce + settle
        val settleAnimation = ScaleAnimation(
            1.05f, 1.0f, // fromX, toX
            1.05f, 1.0f, // fromY, toY
            Animation.RELATIVE_TO_SELF, 0.5f, // pivotX
            Animation.RELATIVE_TO_SELF, 0.5f  // pivotY
        )
        settleAnimation.duration = duration / 3
        settleAnimation.startOffset = duration * 2 / 3

        val set = AnimationSet(true)
        set.addAnimation(animation)
        set.addAnimation(settleAnimation)
        return set
    }

    private fun createRotateInAnimation(duration: Long): Animation {
        val animation = RotateAnimation(
            -180f, 0f, // fromDegrees, toDegrees
            Animation.RELATIVE_TO_SELF, 0.5f, // pivotX
            Animation.RELATIVE_TO_SELF, 0.5f  // pivotY
        )
        animation.duration = duration
        animation.interpolator = DecelerateInterpolator()
        return animation
    }

    /**
     * Animaciones predefinidas para diferentes contextos
     */
    fun animateRegionImage(imageView: ImageView) {
        animateImage(imageView, AnimationType.FADE_IN, duration = 600, delay = 100)
    }

    fun animateMuscleDetailImage(imageView: ImageView) {
        animateImage(imageView, AnimationType.SLIDE_IN_BOTTOM, duration = 500, delay = 200)
    }

    fun animateQuizImage(imageView: ImageView) {
        animateImage(imageView, AnimationType.SCALE_IN, duration = 400, delay = 150)
    }

    fun animateTutorialImage(imageView: ImageView) {
        animateImage(imageView, AnimationType.SLIDE_IN_RIGHT, duration = 700, delay = 300)
    }

    fun animateLogoImage(imageView: ImageView) {
        animateImage(imageView, AnimationType.BOUNCE_IN, duration = 800, delay = 200)
    }

    /**
     * Utilidad para animar cambio de imagen con transición suave
     */
    fun animateImageChange(
        imageView: ImageView,
        newImageResource: Int,
        animationType: AnimationType = AnimationType.FADE_IN,
        duration: Long = 300
    ) {
        // Primero fade out
        val fadeOut = AlphaAnimation(1.0f, 0.0f)
        fadeOut.duration = duration / 2
        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                // Cambiar imagen
                imageView.setImageResource(newImageResource)
                // Fade in
                animateImage(imageView, animationType, duration / 2)
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })

        imageView.startAnimation(fadeOut)
    }
}