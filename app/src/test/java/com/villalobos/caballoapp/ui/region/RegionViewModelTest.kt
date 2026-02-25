package com.villalobos.caballoapp.ui.region

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
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

/**
 * Unit Tests para RegionViewModel.
 * Verifica la lógica de regiones y músculos sin dependencias de Android.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class RegionViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var musculoRepository: MusculoRepository

    private lateinit var viewModel: RegionViewModel

    private val sampleRegion = Region(
        id = 1,
        nombre = "CABEZA",
        nombreCompleto = "Región de la Cabeza",
        descripcion = "Músculos de la cabeza",
        nombreImagen = "cabeza_lateral",
        codigoColor = "#D4A574",
        orden = 1
    )

    private val sampleMuscles = listOf(
        Musculo(
            id = 1,
            nombre = "Masetero",
            descripcion = "Músculo de la mandíbula",
            funcion = "Masticación",
            origen = "Arco cigomático",
            insercion = "Rama de la mandíbula",
            regionId = 1
        ),
        Musculo(
            id = 2,
            nombre = "Temporal",
            descripcion = "Músculo temporal",
            funcion = "Elevación de la mandíbula",
            origen = "Fosa temporal",
            insercion = "Apófisis coronoides",
            regionId = 1
        )
    )

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        
        whenever(musculoRepository.getAllRegions()).thenReturn(listOf(sampleRegion))
        whenever(musculoRepository.getSubZonasByRegion(any())).thenReturn(emptyList())
        viewModel = RegionViewModel(musculoRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadRegion updates state with region data`() = runTest {
        // Given
        whenever(musculoRepository.getRegionById(1)).thenReturn(sampleRegion)
        whenever(musculoRepository.getMusclesByRegionUnified(1)).thenReturn(sampleMuscles)

        // When
        viewModel.loadRegion(1)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertNotNull(state)
        assertEquals(sampleRegion, state?.region)
        assertEquals(sampleMuscles.size, state?.muscles?.size)
    }

    @Test
    fun `selectMuscle updates selected muscle in state`() {
        // Given
        val muscle = sampleMuscles.first()

        // When
        viewModel.selectMuscle(muscle)

        // Then
        val state = viewModel.state.value
        assertEquals(muscle, state?.selectedMuscle)
    }

    @Test
    fun `clearSelectedMuscle resets selected muscle`() {
        // Given
        viewModel.selectMuscle(sampleMuscles.first())

        // When
        viewModel.clearSelectedMuscle()

        // Then
        assertNull(viewModel.state.value?.selectedMuscle)
    }

    @Test
    fun `getRegionName returns correct name`() = runTest {
        // Given
        whenever(musculoRepository.getRegionById(1)).thenReturn(sampleRegion)
        whenever(musculoRepository.getMusclesByRegionUnified(1)).thenReturn(sampleMuscles)
        viewModel.loadRegion(1)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        val name = viewModel.getRegionName()

        // Then
        assertEquals("Región de la Cabeza", name)
    }

    @Test
    fun `getMuscleCount returns correct count`() = runTest {
        // Given
        whenever(musculoRepository.getRegionById(1)).thenReturn(sampleRegion)
        whenever(musculoRepository.getMusclesByRegionUnified(1)).thenReturn(sampleMuscles)
        viewModel.loadRegion(1)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        val count = viewModel.getMuscleCount()

        // Then
        assertEquals(2, count)
    }

    @Test
    fun `hasMuscles returns true when muscles exist`() = runTest {
        // Given
        whenever(musculoRepository.getRegionById(1)).thenReturn(sampleRegion)
        whenever(musculoRepository.getMusclesByRegionUnified(1)).thenReturn(sampleMuscles)
        viewModel.loadRegion(1)
        testDispatcher.scheduler.advanceUntilIdle()

        // When & Then
        assertTrue(viewModel.hasMuscles())
    }

    @Test
    fun `hasMuscles returns false when no muscles`() = runTest {
        // Given
        whenever(musculoRepository.getRegionById(1)).thenReturn(sampleRegion)
        whenever(musculoRepository.getMusclesByRegionUnified(1)).thenReturn(emptyList())
        viewModel.loadRegion(1)
        testDispatcher.scheduler.advanceUntilIdle()

        // When & Then
        assertFalse(viewModel.hasMuscles())
    }

    @Test
    fun `clearEvent sets event to null`() {
        // Given
        viewModel.selectMuscle(sampleMuscles.first())

        // When
        viewModel.clearEvent()

        // Then
        assertNull(viewModel.event.value)
    }

    @Test
    fun `navigateToMuscleDetail emits NavigateToDetail event`() {
        // Given
        val muscle = sampleMuscles.first()

        // When
        viewModel.navigateToMuscleDetail(muscle)

        // Then
        val event = viewModel.event.value
        assertTrue(event is RegionViewModel.RegionEvent.NavigateToDetail)
        assertEquals(muscle, (event as RegionViewModel.RegionEvent.NavigateToDetail).muscle)
    }
}
