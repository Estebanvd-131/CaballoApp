package com.villalobos.caballoapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.villalobos.caballoapp.databinding.ActivityRegionMenuBinding


//
class RegionMenu : AppCompatActivity() {

    lateinit var btnRegionCabeza: Button
    lateinit var btnRegionCuello: Button
    lateinit var btnRegionTronco: Button
    lateinit var btnRegionToracica: Button
    lateinit var btnRegionPelvica: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val enlace = ActivityRegionMenuBinding.inflate(layoutInflater)

        btnRegionCabeza = enlace.btnRegionCabeza
        btnRegionCuello = enlace.btnRegionCuello
        btnRegionTronco = enlace.btnRegionTronco
        btnRegionToracica = enlace.btnRegionToracica
        btnRegionPelvica = enlace.btnRegionPelvica



        btnRegionCabeza.setOnClickListener {
            btnRegionCabeza(it)
        }
        btnRegionCuello.setOnClickListener {
            btnRegionCuello(it)
        }

        btnRegionTronco.setOnClickListener {
            btnRegionTronco(it)
        }
        btnRegionToracica.setOnClickListener {
            btnRegionToracica(it)}

        btnRegionPelvica.setOnClickListener {
            btnRegionPelvica(it)
        }

        setContentView(enlace.root)



    }

    fun btnRegionCabeza(view: View){
        val op = Intent(this, RegionCabeza:: class.java)
        startActivity(op)
    }
    fun btnRegionCuello(view: View){
        val op = Intent(this, RegionCuello:: class.java)
        startActivity(op)
    }

    fun btnRegionTronco(view: View){
        val op = Intent(this, RegionTronco:: class.java)
        startActivity(op)
    }
    fun btnRegionToracica(view: View){
        val op = Intent(this, RegionToracica:: class.java)
        startActivity(op)
    }

    fun btnRegionPelvica(view: View){
        val op = Intent(this, RegionPelvica:: class.java)
        startActivity(op)
    }



}