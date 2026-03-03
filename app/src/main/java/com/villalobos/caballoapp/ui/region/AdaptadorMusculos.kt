package com.villalobos.caballoapp.ui.region

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.villalobos.caballoapp.data.model.Musculo
import com.villalobos.caballoapp.R

import android.widget.Toast
import com.villalobos.caballoapp.util.ProgressionManager

class AdaptadorMusculos(
    private var musculos: List<Musculo>,
    private val regionId: Int,
    private val onMusculoClick: (Musculo) -> Unit
) : RecyclerView.Adapter<AdaptadorMusculos.MusculoViewHolder>() {

    init {
        setHasStableIds(true)
    }

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
        val context = holder.itemView.context
        
        holder.tvNumero.text = musculo.hotspotNumero.toString()
        holder.tvNombre.text = musculo.nombre
        holder.tvDescripcion.text = musculo.descripcion
        
        // Check if this item is unlocked
        val isUnlocked = if (position == 0) {
            true
        } else {
            val previousMuscle = musculos[position - 1]
            ProgressionManager.isMuscleCompleted(context, regionId, previousMuscle.id) ||
                ProgressionManager.isCompleted(context, regionId, position - 1)
        }
        
        if (isUnlocked) {
            holder.contenedor.alpha = 1.0f
            holder.contenedor.setOnClickListener {
                onMusculoClick(musculo)
            }
            // Reset background or icon if needed
            holder.tvNumero.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        } else {
            holder.contenedor.alpha = 0.5f
            holder.contenedor.setOnClickListener {
                Toast.makeText(context, "Debes completar el tema anterior para desbloquear este.", Toast.LENGTH_SHORT).show()
            }
            // Optional: Add a lock icon
            // holder.tvNumero.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, 0, 0) 
        }
    }

    override fun getItemId(position: Int): Long = musculos[position].id.toLong()


    override fun getItemCount(): Int = musculos.size

    fun actualizarMusculos(nuevosMusulos: List<Musculo>) {
        musculos = nuevosMusulos
        notifyDataSetChanged()
    }
} 