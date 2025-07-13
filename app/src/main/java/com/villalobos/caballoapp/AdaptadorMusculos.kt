package com.villalobos.caballoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdaptadorMusculos(
    private var musculos: List<Musculo>,
    private val onMusculoClick: (Musculo) -> Unit
) : RecyclerView.Adapter<AdaptadorMusculos.MusculoViewHolder>() {

    class MusculoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNumero: TextView = view.findViewById(R.id.tvNumero)
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)
        val tvDescripcion: TextView = view.findViewById(R.id.tvDescripcion)
        val contenedor: View = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusculoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_musculo, parent, false)
        return MusculoViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusculoViewHolder, position: Int) {
        val musculo = musculos[position]
        
        holder.tvNumero.text = musculo.hotspotNumero.toString()
        holder.tvNombre.text = musculo.nombre
        holder.tvDescripcion.text = musculo.descripcion
        
        holder.contenedor.setOnClickListener {
            onMusculoClick(musculo)
        }
    }

    override fun getItemCount(): Int = musculos.size

    fun actualizarMusculos(nuevosMusulos: List<Musculo>) {
        musculos = nuevosMusulos
        notifyDataSetChanged()
    }
} 