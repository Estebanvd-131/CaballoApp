package com.villalobos.caballoapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.villalobos.caballoapp.data.model.Musculo

/**
 * Entidad Room para almacenar músculos en la base de datos.
 */
@Entity(tableName = "musculos")
data class MusculoEntity(
    @PrimaryKey
    val id: Int,
    val nombre: String,
    val origen: String,
    val insercion: String,
    val funcion: String,
    val regionId: Int,
    val hotspotX: Float = 0f,
    val hotspotY: Float = 0f,
    val hotspotNumero: Int = 0,
    val descripcion: String = "",
    val imagen: String? = null
) {
    /**
     * Convierte la entidad a modelo de dominio.
     */
    fun toModel(): Musculo = Musculo(
        id = id,
        nombre = nombre,
        origen = origen,
        insercion = insercion,
        funcion = funcion,
        regionId = regionId,
        hotspotX = hotspotX,
        hotspotY = hotspotY,
        hotspotNumero = hotspotNumero,
        descripcion = descripcion,
        imagen = imagen
    )

    companion object {
        /**
         * Crea una entidad desde el modelo de dominio.
         */
        fun fromModel(musculo: Musculo): MusculoEntity = MusculoEntity(
            id = musculo.id,
            nombre = musculo.nombre,
            origen = musculo.origen,
            insercion = musculo.insercion,
            funcion = musculo.funcion,
            regionId = musculo.regionId,
            hotspotX = musculo.hotspotX,
            hotspotY = musculo.hotspotY,
            hotspotNumero = musculo.hotspotNumero,
            descripcion = musculo.descripcion,
            imagen = musculo.imagen
        )
    }
}
