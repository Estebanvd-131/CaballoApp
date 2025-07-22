package com.villalobos.caballoapp

import android.content.Context
import android.os.Bundle
import android.widget.RadioButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat.applyTheme
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.villalobos.caballoapp.databinding.ActivityConfiguracionBinding

class Configuracion : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {

        val enlace = ActivityConfiguracionBinding.inflate(layoutInflater)

        applyTheme()

        val colorModeGroup = enlace.colorModeGroup

        //cargar seleccion previa
        val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val currentMode = prefs.getString("color_mode", "light")

        when(currentMode){
            "protanopia" -> colorModeGroup.check(enlace.rbProtanopia)
            "deuteranopia" -> colorModeGroup.check(enlace.rbDeuteranopia)
            "tritanopia" -> colorModeGroup.check(enlace.rbTritanopia)
            else -> colorModeGroup.check(enlace.rbNormal)
        }

        //SEGUIR DESDE DE ESTA PARTE DEL CODIGO 




        setContentView(enlace.root)
    }

    
}