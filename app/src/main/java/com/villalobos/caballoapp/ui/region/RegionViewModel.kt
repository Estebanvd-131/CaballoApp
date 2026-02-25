package com.villalobos.caballoapp.ui.region

import com.villalobos.caballoapp.data.model.Region

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.villalobos.caballoapp.data.model.Musculo
import com.villalobos.caballoapp.data.repository.MusculoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para las pantallas de Región.
 * Maneja la lógica de navegación y datos de músculos por región.
 * Usa Hilt para inyección de dependencias.
 */
@HiltViewModel
class RegionViewModel @Inject constructor(
    private val musculoRepository: MusculoRepository
) : ViewModel() {

    // ============ Estados ============

    data class RegionState(
        val region: Region? = null,
        val muscles: List<Musculo> = emptyList(),
        val zones: List<com.villalobos.caballoapp.data.model.Zona> = emptyList(), // Zonas de la región
        val selectedMuscle: Musculo? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed class RegionEvent {
        data class MuscleSelected(val muscle: Musculo) : RegionEvent()
        data class NavigateToDetail(val muscle: Musculo) : RegionEvent()
        data class Error(val message: String) : RegionEvent()
    }

    // ============ LiveData ============

    private val _state = MutableLiveData(RegionState())
    val state: LiveData<RegionState> = _state

    private val _event = MutableLiveData<RegionEvent?>()
    val event: LiveData<RegionEvent?> = _event

    private val _allRegions = MutableLiveData<List<Region>>()
    val allRegions: LiveData<List<Region>> = _allRegions

    // ============ Inicialización ============

    init {
        loadAllRegions()
    }

    // ============ Acciones ============

    /**
     * Carga todas las regiones disponibles.
     */
    fun loadAllRegions() {
        viewModelScope.launch {
            _allRegions.value = musculoRepository.getAllRegions()
        }
    }

    /**
     * Carga los datos de una región específica.
     */
    fun loadRegion(regionId: Int) {
        viewModelScope.launch {
            _state.value = _state.value?.copy(isLoading = true)

            try {
                val region = musculoRepository.getRegionById(regionId)
                val muscles = musculoRepository.getMusclesByRegionUnified(regionId)
                val zones = musculoRepository.getSubZonasByRegion(regionId)

                _state.value = RegionState(
                    region = region,
                    muscles = muscles,
                    zones = zones,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value?.copy(
                    isLoading = false,
                    error = "Error al cargar región: ${e.message}"
                )
                _event.value = RegionEvent.Error("Error al cargar los músculos")
            }
        }
    }

    /**
     * Selecciona un músculo.
     */
    fun selectMuscle(muscle: Musculo) {
        _state.value = _state.value?.copy(selectedMuscle = muscle)
        _event.value = RegionEvent.MuscleSelected(muscle)
    }

    /**
     * Navega al detalle de un músculo.
     */
    fun navigateToMuscleDetail(muscle: Musculo) {
        _event.value = RegionEvent.NavigateToDetail(muscle)
    }

    /**
     * Limpia el músculo seleccionado.
     */
    fun clearSelectedMuscle() {
        _state.value = _state.value?.copy(selectedMuscle = null)
    }

    /**
     * Limpia el evento actual.
     */
    fun clearEvent() {
        _event.value = null
    }

    /**
     * Busca músculos por nombre en la región actual.
     */
    fun searchMusclesInRegion(query: String) {
        val currentState = _state.value ?: return
        
        if (query.isBlank()) {
            // Restaurar lista completa
            currentState.region?.let { loadRegion(it.id) }
            return
        }

        val filtered = currentState.muscles.filter { muscle ->
            muscle.nombre.lowercase().contains(query.lowercase()) ||
            muscle.descripcion.lowercase().contains(query.lowercase())
        }

        _state.value = currentState.copy(muscles = filtered)
    }

    // ============ Helpers ============

    /**
     * Obtiene el nombre de la región actual.
     */
    fun getRegionName(): String {
        return _state.value?.region?.nombreCompleto ?: "Región"
    }

    /**
     * Obtiene el conteo de músculos en la región actual.
     */
    fun getMuscleCount(): Int {
        return _state.value?.muscles?.size ?: 0
    }

    /**
     * Verifica si hay músculos cargados.
     */
    fun hasMuscles(): Boolean {
        return _state.value?.muscles?.isNotEmpty() == true
    }

    /**
     * Obtiene estadísticas de todas las regiones.
     */
    fun getRegionStats(): Map<Region, Int> {
        return musculoRepository.getMuscleStatsByRegion()
    }
}

