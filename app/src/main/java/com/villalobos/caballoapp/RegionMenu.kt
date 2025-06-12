package com.villalobos.caballoapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import com.villalobos.caballoapp.databinding.ActivityRegionMenuBinding

class RegionMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val enlace = ActivityRegionMenuBinding.inflate(layoutInflater)


        setContentView(enlace.root)

    }
}