package com.villalobos.caballoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class RegionMenuActivity {
    class RegionMenuActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_region_menu)

            // Aplicar tema personalizado
            ThemeManager.applyTheme(this)

            // Configurar botones
            val btnRegionCabeza = findViewById<Button>(R.id.btnRegionCabeza)
            val btnRegionCuello = findViewById<Button>(R.id.btnRegionCuello)
            val btnRegionTronco = findViewById<Button>(R.id.btnRegionTronco)
            val btnRegionToracica = findViewById<Button>(R.id.btnRegionToracica)
            val btnRegionPelvica = findViewById<Button>(R.id.btnRegionPelvica)

            btnRegionCabeza.setOnClickListener {
                val intent = Intent(this, RegionDetalleActivity::class.java)
                intent.putExtra("REGION", "CABEZA")
                startActivity(intent)
            }

            btnRegionCuello.setOnClickListener {
                val intent = Intent(this, RegionDetalleActivity::class.java)
                intent.putExtra("REGION", "CUELLO")
                startActivity(intent)
            }

            btnRegionTronco.setOnClickListener {
                val intent = Intent(this, RegionDetalleActivity::class.java)
                intent.putExtra("REGION", "TRONCO")
                startActivity(intent)
            }

            btnRegionToracica.setOnClickListener {
                val intent = Intent(this, RegionDetalleActivity::class.java)
                intent.putExtra("REGION", "MIEMBROS_TORACICOS")
                startActivity(intent)
            }

            btnRegionPelvica.setOnClickListener {
                val intent = Intent(this, RegionDetalleActivity::class.java)
                intent.putExtra("REGION", "PELVICA")
                startActivity(intent)
            }
        }

        // Actualizar los colores cuando se regresa a esta actividad
        override fun onResume() {
            super.onResume()
            ThemeManager.applyTheme(this)
        }

        override fun onBackPressed() {
            super.onBackPressed()
            finish()
        }
    }
}