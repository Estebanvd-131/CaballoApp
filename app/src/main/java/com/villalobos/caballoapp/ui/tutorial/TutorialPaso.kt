package com.villalobos.caballoapp.ui.tutorial

import androidx.annotation.DrawableRes
import java.io.Serializable

data class TutorialPaso(
    val numero: Int,
    val titulo: String,
    val descripcion: String,
    @DrawableRes val imagen: Int,
    val mostrarCaracteristicas: Boolean = false,
    val caracteristicas: List<String> = emptyList()
) : Serializable 