package com.villalobos.caballoapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.villalobos.caballoapp.databinding.ActivityMainBinding


//ventana de inicio que enlaza con todas las ventanas de la app
class MainActivity : AppCompatActivity() {


    // Declaracion de variables
    lateinit var btnIniciar: Button
    lateinit var btnAccesibilidad: Button
    lateinit var btnCreditos: Button
    lateinit var btnSalir: Button

    override fun onCreate(savedInstanceState: Bundle?) {



        //Validacion del ViewBinding
        val enlace = ActivityMainBinding.inflate(layoutInflater)


        btnIniciar = enlace.btnIniciar
        btnAccesibilidad = enlace.btnAccesibilidad
        btnCreditos = enlace.btnCreditos
        btnSalir = enlace.btnSalir


        super.onCreate(savedInstanceState)

        btnIniciar.setOnClickListener {
            btnIniciar(it)}

        btnAccesibilidad.setOnClickListener {
            btnAccesibilidad(it)}

        btnCreditos.setOnClickListener {
            btnCreditos(it)}

        btnSalir.setOnClickListener {
            btnSalir(it)}

        setContentView(enlace.root)

    }

    //funcionamiento de llamado de ventanas de la App
    // Intent para iniciar la actividad y pasar a la siguiente ventana
    fun btnIniciar(view: View) {
        val op = Intent(this, RegionMenu::class.java)
        startActivity(op)

    }

    fun btnAccesibilidad(view: View){
        val op = Intent(this, Accesibilidad::class.java)
        startActivity(op)

    }

    fun btnCreditos(view: View){
        val op = Intent(this, Creditos::class.java)
        startActivity(op)
    }

    fun btnSalir(view: View){
        finish()
    }

}