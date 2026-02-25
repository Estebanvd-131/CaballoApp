package com.villalobos.caballoapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.villalobos.caballoapp.data.local.entity.MusculoEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones de base de datos relacionadas con músculos.
 */
@Dao
interface MusculoDao {
    
    /**
     * Obtiene todos los músculos.
     */
    @Query("SELECT * FROM musculos ORDER BY nombre")
    fun getAllMusculos(): Flow<List<MusculoEntity>>
    
    /**
     * Obtiene todos los músculos de forma síncrona (para prepopulación).
     */
    @Query("SELECT * FROM musculos ORDER BY nombre")
    suspend fun getAllMusculosSync(): List<MusculoEntity>
    
    /**
     * Obtiene músculos por región.
     */
    @Query("SELECT * FROM musculos WHERE regionId = :regionId ORDER BY nombre")
    fun getMusculosByRegion(regionId: Int): Flow<List<MusculoEntity>>
    
    /**
     * Obtiene músculos por región de forma síncrona.
     */
    @Query("SELECT * FROM musculos WHERE regionId = :regionId ORDER BY nombre")
    suspend fun getMusculosByRegionSync(regionId: Int): List<MusculoEntity>
    
    /**
     * Obtiene un músculo por ID.
     */
    @Query("SELECT * FROM musculos WHERE id = :id")
    suspend fun getMusculoById(id: Int): MusculoEntity?
    
    /**
     * Busca músculos por nombre.
     */
    @Query("SELECT * FROM musculos WHERE nombre LIKE '%' || :query || '%' ORDER BY nombre")
    fun searchMusculos(query: String): Flow<List<MusculoEntity>>
    
    /**
     * Cuenta el total de músculos.
     */
    @Query("SELECT COUNT(*) FROM musculos")
    suspend fun getMusculoCount(): Int
    
    /**
     * Cuenta músculos por región.
     */
    @Query("SELECT COUNT(*) FROM musculos WHERE regionId = :regionId")
    suspend fun getMusculoCountByRegion(regionId: Int): Int
    
    /**
     * Inserta múltiples músculos.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(musculos: List<MusculoEntity>)
    
    /**
     * Inserta un músculo.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(musculo: MusculoEntity)
    
    /**
     * Elimina todos los músculos.
     */
    @Query("DELETE FROM musculos")
    suspend fun deleteAll()
}
