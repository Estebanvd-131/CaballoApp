package com.villalobos.caballoapp.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.villalobos.caballoapp.data.model.Musculo
import com.villalobos.caballoapp.data.model.Region
import com.villalobos.caballoapp.data.repository.MusculoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

/**
 * Unit tests para DetalleMusculoViewModel.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class DetalleMusculoViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var mockMusculoRepository: MusculoRepository

    private lateinit var viewModel: DetalleMusculoViewModel

    private val testMusculo = Musculo(
        id = 1,
        nombre = "Músculo Temporal",
        origen = "Hueso parietal, hueso temporal, hueso frontal",
        insercion = "Apófisis coronoides de la mandíbula",
        funcion = "Eleva la mandíbula y la presiona contra los dientes superiores",
        regionId = 1,
        hotspotX = 0.5f,
        hotspotY = 0.3f,
        descripcion = "Músculo de masticación principal",
        imagen = "temporal"
    )

    private val testRegion = Region(
        id = 1,
        nombre = "cabeza",
        nombreCompleto = "Región de la Cabeza",
        descripcion = "Músculos de la región de la cabeza",
        nombreImagen = "cabeza_lateral",
        codigoColor = "#D4A574",
        orden = 1
    )

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = DetalleMusculoViewModel(mockMusculoRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ============ Tests de carga de músculo ============

    @Test
    fun `loadMusculo updates state with muscle data`() = runTest {
        whenever(mockMusculoRepository.getMuscleById(1)).thenReturn(testMusculo)
        whenever(mockMusculoRepository.getRegionById(1)).thenReturn(testRegion)
        
        viewModel.loadMusculo(1, 1)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.state.value
        assertNotNull(state?.musculo)
        assertEquals("Músculo Temporal", state?.musculo?.nombre)
        assertFalse(state?.isLoading ?: true)
        assertNull(state?.error)
    }

    @Test
    fun `loadMusculo sets error when muscle not found`() = runTest {
        whenever(mockMusculoRepository.getMuscleById(999)).thenReturn(null)
        whenever(mockMusculoRepository.getRegionById(1)).thenReturn(testRegion)
        
        viewModel.loadMusculo(999, 1)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.state.value
        assertNull(state?.musculo)
        assertNotNull(state?.error)
    }

    @Test
    fun `loadMusculo with invalid id sets error`() = runTest {
        viewModel.loadMusculo(-1, 1)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.state.value
        assertNotNull(state?.error)
        assertEquals("ID de músculo inválido", state?.error)
    }

    @Test
    fun `loadMusculo loads region correctly`() = runTest {
        whenever(mockMusculoRepository.getMuscleById(1)).thenReturn(testMusculo)
        whenever(mockMusculoRepository.getRegionById(1)).thenReturn(testRegion)
        
        viewModel.loadMusculo(1, 1)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.state.value
        assertNotNull(state?.region)
        assertEquals("Región de la Cabeza", state?.region?.nombreCompleto)
    }

    // ============ Tests de navegación ============

    @Test
    fun `navigateBack emits NavigateBack event`() = runTest {
        viewModel.navigateBack()
        
        val event = viewModel.event.value
        assertTrue(event is DetalleMusculoViewModel.DetalleEvent.NavigateBack)
    }

    // ============ Tests de información del músculo ============

    @Test
    fun `getMusculoName returns correct name after loading`() = runTest {
        whenever(mockMusculoRepository.getMuscleById(1)).thenReturn(testMusculo)
        whenever(mockMusculoRepository.getRegionById(1)).thenReturn(testRegion)
        
        viewModel.loadMusculo(1, 1)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val name = viewModel.getMusculoName()
        assertEquals("Músculo Temporal", name)
    }

    @Test
    fun `getMusculoName returns default before loading`() {
        val name = viewModel.getMusculoName()
        assertEquals("Músculo", name)
    }

    @Test
    fun `getOrigen returns origin after loading`() = runTest {
        whenever(mockMusculoRepository.getMuscleById(1)).thenReturn(testMusculo)
        whenever(mockMusculoRepository.getRegionById(1)).thenReturn(testRegion)
        
        viewModel.loadMusculo(1, 1)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val origen = viewModel.getOrigen()
        assertEquals("Hueso parietal, hueso temporal, hueso frontal", origen)
    }

    @Test
    fun `getInsercion returns insertion after loading`() = runTest {
        whenever(mockMusculoRepository.getMuscleById(1)).thenReturn(testMusculo)
        whenever(mockMusculoRepository.getRegionById(1)).thenReturn(testRegion)
        
        viewModel.loadMusculo(1, 1)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val insercion = viewModel.getInsercion()
        assertEquals("Apófisis coronoides de la mandíbula", insercion)
    }

    @Test
    fun `getFuncion returns function after loading`() = runTest {
        whenever(mockMusculoRepository.getMuscleById(1)).thenReturn(testMusculo)
        whenever(mockMusculoRepository.getRegionById(1)).thenReturn(testRegion)
        
        viewModel.loadMusculo(1, 1)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val funcion = viewModel.getFuncion()
        assertEquals("Eleva la mandíbula y la presiona contra los dientes superiores", funcion)
    }

    // ============ Tests de clearEvent ============

    @Test
    fun `clearEvent resets event to null`() = runTest {
        viewModel.navigateBack()
        
        assertNotNull(viewModel.event.value)
        
        viewModel.clearEvent()
        
        assertNull(viewModel.event.value)
    }

    // ============ Tests de validación de datos ============

    @Test
    fun `isDataValid returns true when muscle loaded`() = runTest {
        whenever(mockMusculoRepository.getMuscleById(1)).thenReturn(testMusculo)
        whenever(mockMusculoRepository.getRegionById(1)).thenReturn(testRegion)
        
        viewModel.loadMusculo(1, 1)
        testDispatcher.scheduler.advanceUntilIdle()
        
        assertTrue(viewModel.isDataValid())
    }

    @Test
    fun `isDataValid returns false before loading`() {
        assertFalse(viewModel.isDataValid())
    }

    @Test
    fun `isDataValid returns false when error`() = runTest {
        viewModel.loadMusculo(-1, 1)
        testDispatcher.scheduler.advanceUntilIdle()
        
        assertFalse(viewModel.isDataValid())
    }

    // ============ Tests de accesibilidad ============

    @Test
    fun `getAccessibilityDescription returns formatted description`() = runTest {
        whenever(mockMusculoRepository.getMuscleById(1)).thenReturn(testMusculo)
        whenever(mockMusculoRepository.getRegionById(1)).thenReturn(testRegion)
        
        viewModel.loadMusculo(1, 1)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val description = viewModel.getAccessibilityDescription()
        
        assertTrue(description.contains("Músculo Temporal"))
        assertTrue(description.contains("Origen:"))
        assertTrue(description.contains("Inserción:"))
        assertTrue(description.contains("Función:"))
    }

    @Test
    fun `getAccessibilityDescription returns default before loading`() {
        val description = viewModel.getAccessibilityDescription()
        assertEquals("Detalle de músculo", description)
    }

    // ============ Tests de imagen ============

    @Test
    fun `imageName is set correctly after loading`() = runTest {
        whenever(mockMusculoRepository.getMuscleById(1)).thenReturn(testMusculo)
        whenever(mockMusculoRepository.getRegionById(1)).thenReturn(testRegion)
        
        viewModel.loadMusculo(1, 1)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.state.value
        assertEquals("temporal", state?.imageName)
    }

    @Test
    fun `notifyImageNotFound emits ImageNotFound event`() = runTest {
        whenever(mockMusculoRepository.getMuscleById(1)).thenReturn(testMusculo)
        whenever(mockMusculoRepository.getRegionById(1)).thenReturn(testRegion)
        
        viewModel.loadMusculo(1, 1)
        testDispatcher.scheduler.advanceUntilIdle()
        
        viewModel.notifyImageNotFound()
        
        val event = viewModel.event.value
        assertTrue(event is DetalleMusculoViewModel.DetalleEvent.ImageNotFound)
    }
}
