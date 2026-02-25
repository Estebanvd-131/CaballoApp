package com.villalobos.caballoapp.data.repository

import com.villalobos.caballoapp.data.local.dao.MusculoDao
import com.villalobos.caballoapp.data.local.entity.MusculoEntity
import com.villalobos.caballoapp.data.source.DatosMusculares
import com.villalobos.caballoapp.data.model.Musculo
import com.villalobos.caballoapp.data.model.Region
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository para manejar datos de músculos.
 * Usa Room Database con fallback a datos hardcodeados.
 */
@Singleton
class MusculoRepository @Inject constructor(
    private val musculoDao: MusculoDao
) {

    // ============ Regiones ============

    /**
     * Obtiene todas las regiones disponibles.
     */
    fun getAllRegions(): List<Region> {
        return DatosMusculares.regiones
    }

    /**
     * Obtiene una región por su ID.
     */
    fun getRegionById(id: Int): Region? {
        return DatosMusculares.obtenerRegionPorId(id)
    }

    /**
     * Obtiene el nombre completo de una región.
     */
    fun getRegionName(regionId: Int): String {
        return DatosMusculares.obtenerRegionPorId(regionId)?.nombreCompleto ?: "Región desconocida"
    }

    /**
     * Obtiene las sub-zonas de una región.
     */
    fun getSubZonasByRegion(regionId: Int): List<com.villalobos.caballoapp.data.model.Zona> {
        return DatosMusculares.obtenerSubZonasPorRegion(regionId)
    }

    // ============ Músculos (Room) ============

    /**
     * Obtiene todos los músculos como Flow (reactivo).
     */
    fun getAllMusclesFlow(): Flow<List<Musculo>> {
        return musculoDao.getAllMusculos().map { entities ->
            entities.map { it.toModel() }
        }
    }

    /**
     * Obtiene músculos por región como Flow (reactivo).
     */
    fun getMusclesByRegionFlow(regionId: Int): Flow<List<Musculo>> {
        return musculoDao.getMusculosByRegion(regionId).map { entities ->
            entities.map { it.toModel() }
        }
    }

    /**
     * Busca músculos por nombre como Flow (reactivo).
     */
    fun searchMusclesFlow(query: String): Flow<List<Musculo>> {
        return musculoDao.searchMusculos(query).map { entities ->
            entities.map { it.toModel() }
        }
    }

    /**
     * Obtiene un músculo por ID (suspending).
     */
    suspend fun getMuscleByIdAsync(id: Int): Musculo? {
        return musculoDao.getMusculoById(id)?.toModel()
    }

    /**
     * Obtiene todos los músculos de forma síncrona (suspending).
     */
    suspend fun getAllMusclesAsync(): List<Musculo> {
        return musculoDao.getAllMusculosSync().map { it.toModel() }
    }

    /**
     * Obtiene todos los músculos usando Room como fuente de verdad.
     * Hace fallback a hardcoded solo si Room no tiene datos aún.
     */
    suspend fun getAllMusclesUnified(): List<Musculo> {
        val roomData = getAllMusclesAsync()
        return if (roomData.isNotEmpty()) roomData else DatosMusculares.obtenerTodosLosMusculos()
    }

    /**
     * Obtiene músculos por región de forma síncrona (suspending).
     */
    suspend fun getMusclesByRegionAsync(regionId: Int): List<Musculo> {
        return musculoDao.getMusculosByRegionSync(regionId).map { it.toModel() }
    }

    /**
     * Obtiene músculos por región usando Room como fuente de verdad.
     * Hace fallback a hardcoded solo si Room no tiene datos para la región.
     */
    suspend fun getMusclesByRegionUnified(regionId: Int): List<Musculo> {
        val roomData = getMusclesByRegionAsync(regionId)
        return if (roomData.isNotEmpty()) roomData else DatosMusculares.obtenerMusculosPorRegion(regionId)
    }

    /**
     * Obtiene un músculo por ID usando Room como fuente de verdad.
     * Hace fallback a hardcoded solo si Room no tiene el registro.
     */
    suspend fun getMuscleByIdUnified(id: Int): Musculo? {
        return getMuscleByIdAsync(id) ?: DatosMusculares.obtenerMusculoPorId(id)
    }

    // ============ Músculos (Compatibilidad con datos hardcodeados) ============

    /**
     * Obtiene todos los músculos (fallback a hardcoded).
     * Uso recomendado solo para migración o cuando Room no esté disponible.
     */
    fun getAllMuscles(): List<Musculo> {
        return DatosMusculares.obtenerTodosLosMusculos()
    }

    /**
     * Obtiene los músculos de una región específica (fallback a hardcoded).
     */
    fun getMusclesByRegion(regionId: Int): List<Musculo> {
        return DatosMusculares.obtenerMusculosPorRegion(regionId)
    }

    /**
     * Obtiene un músculo por su ID (fallback a hardcoded).
     */
    fun getMuscleById(id: Int): Musculo? {
        return DatosMusculares.obtenerMusculoPorId(id)
    }

    /**
     * Busca músculos por nombre (fallback a hardcoded).
     */
    fun searchMuscles(query: String): List<Musculo> {
        if (query.isBlank()) return emptyList()

        val lowerQuery = query.lowercase()
        return getAllMuscles().filter { musculo ->
            musculo.nombre.lowercase().contains(lowerQuery) ||
                    musculo.descripcion.lowercase().contains(lowerQuery)
        }
    }

    // ============ Estadísticas ============

    /**
     * Obtiene el conteo de músculos por región.
     */
    fun getMuscleCountByRegion(regionId: Int): Int {
        return getMusclesByRegion(regionId).size
    }

    /**
     * Obtiene el conteo total de músculos.
     */
    fun getTotalMuscleCount(): Int {
        return getAllMuscles().size
    }

    /**
     * Obtiene estadísticas de músculos por región.
     */
    fun getMuscleStatsByRegion(): Map<Region, Int> {
        return getAllRegions().associateWith { region ->
            getMuscleCountByRegion(region.id)
        }
    }

    /**
     * Verifica si una región tiene músculos.
     */
    fun regionHasMuscles(regionId: Int): Boolean {
        return getMusclesByRegion(regionId).isNotEmpty()
    }

    /**
     * Cuenta total de músculos en Room (suspending).
     */
    suspend fun getMusculoCountAsync(): Int {
        return musculoDao.getMusculoCount()
    }

    /**
     * Cuenta músculos por región en Room (suspending).
     */
    suspend fun getMusculoCountByRegionAsync(regionId: Int): Int {
        return musculoDao.getMusculoCountByRegion(regionId)
    }
}