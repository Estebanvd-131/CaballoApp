package com.villalobos.caballoapp.ui.region

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.villalobos.caballoapp.data.model.Region
import com.villalobos.caballoapp.data.model.TipoRegion
import com.villalobos.caballoapp.data.repository.MusculoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para RegionMenu.
 * Maneja la lógica de selección y navegación de regiones.
 */
@HiltViewModel
class RegionMenuViewModel @Inject constructor(
    private val musculoRepository: MusculoRepository
) : ViewModel() {

    // ============ Estados ============

    data class RegionMenuState(
        val regions: List<RegionItem> = emptyList(),
        val isLoading: Boolean = false
    )

    data class RegionItem(
        val id: Int,
        val nombre: String,
        val muscleCount: Int,
        val tipoRegion: TipoRegion
    )

    sealed class RegionMenuEvent {
        data class NavigateToRegion(val regionId: Int, val tipoRegion: TipoRegion) : RegionMenuEvent()
        data class Error(val message: String) : RegionMenuEvent()
    }

    // ============ LiveData ============

    private val _state = MutableLiveData(RegionMenuState())
    val state: LiveData<RegionMenuState> = _state

    private val _event = MutableLiveData<RegionMenuEvent?>()
    val event: LiveData<RegionMenuEvent?> = _event

    // ============ Inicialización ============

    init {
        loadRegions()
    }

    // ============ Acciones ============

    /**
     * Carga todas las regiones disponibles con estadísticas.
     */
    fun loadRegions() {
        viewModelScope.launch {
            _state.value = _state.value?.copy(isLoading = true)

            try {
                val regions = musculoRepository.getAllRegions()
                val regionItems = regions.map { region ->
                    val muscleCount = musculoRepository.getMusclesByRegionUnified(region.id).size
                    val tipoRegion = TipoRegion.entries.find { it.id == region.id } ?: TipoRegion.CABEZA
                    
                    RegionItem(
                        id = region.id,
                        nombre = region.nombreCompleto,
                        muscleCount = muscleCount,
                        tipoRegion = tipoRegion
                    )
                }

                _state.value = RegionMenuState(
                    regions = regionItems,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value?.copy(isLoading = false)
                _event.value = RegionMenuEvent.Error("Error al cargar regiones: ${e.message}")
            }
        }
    }

    /**
     * Navega a una región específica.
     */
    fun selectRegion(tipoRegion: TipoRegion) {
        _event.value = RegionMenuEvent.NavigateToRegion(tipoRegion.id, tipoRegion)
    }

    /**
     * Navega a la región de cabeza.
     */
    fun navigateToCabeza() {
        selectRegion(TipoRegion.CABEZA)
    }

    /**
     * Navega a la región de cuello.
     */
    fun navigateToCuello() {
        selectRegion(TipoRegion.CUELLO)
    }

    /**
     * Navega a la región de tronco.
     */
    fun navigateToTronco() {
        selectRegion(TipoRegion.TRONCO)
    }

    /**
     * Navega a la región torácica.
     */
    fun navigateToToracica() {
        selectRegion(TipoRegion.MIEMBROS_TORACICOS)
    }

    /**
     * Navega a la región pélvica.
     */
    fun navigateToPelvica() {
        selectRegion(TipoRegion.MIEMBROS_PELVICOS)
    }

    /**
     * Navega a la región distal.
     */
    fun navigateToDistal() {
        selectRegion(TipoRegion.REGION_DISTAL)
    }

    /**
     * Limpia el evento actual.
     */
    fun clearEvent() {
        _event.value = null
    }

    // ============ Helpers ============

    /**
     * Obtiene el conteo total de músculos.
     */
    fun getTotalMuscleCount(): Int {
        return _state.value?.regions?.sumOf { it.muscleCount } ?: 0
    }

    /**
     * Obtiene el número de regiones.
     */
    fun getRegionCount(): Int {
        return _state.value?.regions?.size ?: 0
    }
}
