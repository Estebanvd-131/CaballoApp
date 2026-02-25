package com.villalobos.caballoapp.data.repository

import com.villalobos.caballoapp.data.local.dao.MusculoDao
import com.villalobos.caballoapp.data.local.entity.MusculoEntity
import com.villalobos.caballoapp.data.model.Musculo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

/**
 * Unit tests para MusculoRepository.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MusculoRepositoryTest {

    @Mock
    private lateinit var mockMusculoDao: MusculoDao

    private lateinit var repository: MusculoRepository

    private val testMusculoEntities = listOf(
        MusculoEntity(
            id = 1,
            nombre = "Músculo Temporal",
            origen = "Hueso parietal",
            insercion = "Apófisis coronoides",
            funcion = "Eleva la mandíbula",
            regionId = 1,
            hotspotX = 0.5f,
            hotspotY = 0.3f,
            descripcion = "Músculo de masticación",
            imagen = "temporal"
        ),
        MusculoEntity(
            id = 2,
            nombre = "Músculo Masetero",
            origen = "Arco cigomático",
            insercion = "Cara lateral de mandíbula",
            funcion = "Cierra la mandíbula",
            regionId = 1,
            hotspotX = 0.6f,
            hotspotY = 0.4f,
            descripcion = "Músculo masticatorio potente",
            imagen = "masetero"
        ),
        MusculoEntity(
            id = 100,
            nombre = "Músculo Braquiocefálico",
            origen = "Cresta del húmero",
            insercion = "Proceso mastoideo",
            funcion = "Extiende cabeza y cuello",
            regionId = 2,
            hotspotX = 0.3f,
            hotspotY = 0.5f,
            descripcion = "Músculo del cuello",
            imagen = "braquiocefalico"
        )
    )

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = MusculoRepository(mockMusculoDao)
    }

    // ============ Tests de Flow (Room) ============

    @Test
    fun `getAllMusclesFlow returns all muscles from DAO`() = runTest {
        whenever(mockMusculoDao.getAllMusculos())
            .thenReturn(flowOf(testMusculoEntities))
        
        val result = repository.getAllMusclesFlow().first()
        
        assertEquals(3, result.size)
        assertEquals("Músculo Temporal", result[0].nombre)
        assertEquals("Músculo Masetero", result[1].nombre)
    }

    @Test
    fun `getMusclesByRegionFlow returns only muscles from specified region`() = runTest {
        val region1Muscles = testMusculoEntities.filter { it.regionId == 1 }
        whenever(mockMusculoDao.getMusculosByRegion(1))
            .thenReturn(flowOf(region1Muscles))
        
        val result = repository.getMusclesByRegionFlow(1).first()
        
        assertEquals(2, result.size)
        assertTrue(result.all { it.regionId == 1 })
    }

    @Test
    fun `searchMusclesFlow returns matching muscles`() = runTest {
        val searchResults = testMusculoEntities.filter { 
            it.nombre.contains("Temporal", ignoreCase = true) 
        }
        whenever(mockMusculoDao.searchMusculos("Temporal"))
            .thenReturn(flowOf(searchResults))
        
        val result = repository.searchMusclesFlow("Temporal").first()
        
        assertEquals(1, result.size)
        assertEquals("Músculo Temporal", result[0].nombre)
    }

    // ============ Tests de funciones suspending ============

    @Test
    fun `getMuscleByIdAsync returns correct muscle`() = runTest {
        whenever(mockMusculoDao.getMusculoById(1))
            .thenReturn(testMusculoEntities[0])
        
        val result = repository.getMuscleByIdAsync(1)
        
        assertNotNull(result)
        assertEquals(1, result?.id)
        assertEquals("Músculo Temporal", result?.nombre)
    }

    @Test
    fun `getMuscleByIdAsync returns null for non-existent id`() = runTest {
        whenever(mockMusculoDao.getMusculoById(999))
            .thenReturn(null)
        
        val result = repository.getMuscleByIdAsync(999)
        
        assertNull(result)
    }

    @Test
    fun `getAllMusclesAsync returns all muscles`() = runTest {
        whenever(mockMusculoDao.getAllMusculosSync())
            .thenReturn(testMusculoEntities)
        
        val result = repository.getAllMusclesAsync()
        
        assertEquals(3, result.size)
    }

    @Test
    fun `getMusclesByRegionAsync returns filtered muscles`() = runTest {
        val region2Muscles = testMusculoEntities.filter { it.regionId == 2 }
        whenever(mockMusculoDao.getMusculosByRegionSync(2))
            .thenReturn(region2Muscles)
        
        val result = repository.getMusclesByRegionAsync(2)
        
        assertEquals(1, result.size)
        assertEquals("Músculo Braquiocefálico", result[0].nombre)
    }

    // ============ Tests de conversión Entity -> Model ============

    @Test
    fun `entity to model conversion preserves all fields`() = runTest {
        whenever(mockMusculoDao.getMusculoById(1))
            .thenReturn(testMusculoEntities[0])
        
        val result = repository.getMuscleByIdAsync(1)
        
        assertNotNull(result)
        assertEquals(1, result?.id)
        assertEquals("Músculo Temporal", result?.nombre)
        assertEquals("Hueso parietal", result?.origen)
        assertEquals("Apófisis coronoides", result?.insercion)
        assertEquals("Eleva la mandíbula", result?.funcion)
        assertEquals(1, result?.regionId)
        assertEquals(0.5f, result?.hotspotX)
        assertEquals(0.3f, result?.hotspotY)
        assertEquals("Músculo de masticación", result?.descripcion)
        assertEquals("temporal", result?.imagen)
    }

    // ============ Tests de conteo ============

    @Test
    fun `getMusculoCountAsync returns total count`() = runTest {
        whenever(mockMusculoDao.getMusculoCount())
            .thenReturn(50)
        
        val result = repository.getMusculoCountAsync()
        
        assertEquals(50, result)
    }

    @Test
    fun `getMusculoCountByRegionAsync returns count for specific region`() = runTest {
        whenever(mockMusculoDao.getMusculoCountByRegion(1))
            .thenReturn(15)
        
        val result = repository.getMusculoCountByRegionAsync(1)
        
        assertEquals(15, result)
    }

    // ============ Tests de compatibilidad (hardcoded) ============

    @Test
    fun `getAllRegions returns non-empty list`() {
        val regions = repository.getAllRegions()
        
        assertTrue(regions.isNotEmpty())
    }

    @Test
    fun `getRegionById returns correct region`() {
        val region = repository.getRegionById(1)
        
        assertNotNull(region)
        assertEquals(1, region?.id)
    }

    @Test
    fun `getRegionById returns null for invalid id`() {
        val region = repository.getRegionById(999)
        
        assertNull(region)
    }

    @Test
    fun `getRegionName returns name for valid region`() {
        val name = repository.getRegionName(1)
        
        assertNotEquals("Región desconocida", name)
        assertTrue(name.isNotEmpty())
    }

    @Test
    fun `getRegionName returns default for invalid region`() {
        val name = repository.getRegionName(999)
        
        assertEquals("Región desconocida", name)
    }

    @Test
    fun `searchMuscles with empty query returns empty list`() {
        val result = repository.searchMuscles("")
        
        assertTrue(result.isEmpty())
    }

    @Test
    fun `searchMuscles with blank query returns empty list`() {
        val result = repository.searchMuscles("   ")
        
        assertTrue(result.isEmpty())
    }
}
