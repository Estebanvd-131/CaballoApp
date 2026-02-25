package com.villalobos.caballoapp.ui.detail

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
 * ViewModel para DetalleMusculo.
 * Maneja la lógica de visualización del detalle de un músculo.
 */
@HiltViewModel
class DetalleMusculoViewModel @Inject constructor(
    private val musculoRepository: MusculoRepository
) : ViewModel() {

    // ============ Estados ============

    data class DetalleState(
        val musculo: Musculo? = null,
        val region: Region? = null,
        val isLoading: Boolean = false,
        val error: String? = null,
        val imageName: String? = null
    ) {
        val hasValidData: Boolean
            get() = musculo != null && error == null
    }

    sealed class DetalleEvent {
        object NavigateBack : DetalleEvent()
        data class Error(val message: String) : DetalleEvent()
        data class ImageNotFound(val imageName: String) : DetalleEvent()
    }

    // ============ LiveData ============

    private val _state = MutableLiveData(DetalleState())
    val state: LiveData<DetalleState> = _state

    private val _event = MutableLiveData<DetalleEvent?>()
    val event: LiveData<DetalleEvent?> = _event

    // ============ Acciones ============

    /**
     * Carga los datos del músculo por ID.
     */
    fun loadMusculo(musculoId: Int, regionId: Int) {
        if (musculoId <= 0) {
            _state.value = DetalleState(error = "ID de músculo inválido")
            _event.value = DetalleEvent.Error("ID de músculo inválido")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value?.copy(isLoading = true)

            try {
                val musculo = musculoRepository.getMuscleById(musculoId)
                val region = musculoRepository.getRegionById(regionId)

                if (musculo == null) {
                    _state.value = DetalleState(
                        isLoading = false,
                        error = "Músculo no encontrado"
                    )
                    _event.value = DetalleEvent.Error("Músculo no encontrado con ID: $musculoId")
                    return@launch
                }

                // Procesar nombre de imagen
                val imageName = musculo.imagen
                    ?.substringBeforeLast(".")
                    ?.lowercase()

                _state.value = DetalleState(
                    musculo = musculo,
                    region = region,
                    isLoading = false,
                    imageName = imageName
                )

            } catch (e: Exception) {
                _state.value = DetalleState(
                    isLoading = false,
                    error = "Error al cargar músculo: ${e.message}"
                )
                _event.value = DetalleEvent.Error("Error al cargar datos: ${e.message}")
            }
        }
    }

    /**
     * Notifica que la imagen no fue encontrada.
     */
    fun notifyImageNotFound() {
        val imageName = _state.value?.musculo?.imagen ?: "desconocida"
        _event.value = DetalleEvent.ImageNotFound(imageName)
    }

    /**
     * Navega hacia atrás.
     */
    fun navigateBack() {
        _event.value = DetalleEvent.NavigateBack
    }

    /**
     * Limpia el evento actual.
     */
    fun clearEvent() {
        _event.value = null
    }

    // ============ Helpers ============

    /**
     * Obtiene el nombre del músculo actual.
     */
    fun getMusculoName(): String {
        return _state.value?.musculo?.nombre ?: "Músculo"
    }

    /**
     * Obtiene el origen del músculo.
     */
    fun getOrigen(): String {
        return _state.value?.musculo?.origen?.ifBlank { "Información no disponible" } 
            ?: "Información no disponible"
    }

    /**
     * Obtiene la inserción del músculo.
     */
    fun getInsercion(): String {
        return _state.value?.musculo?.insercion?.ifBlank { "Información no disponible" } 
            ?: "Información no disponible"
    }

    /**
     * Obtiene la función del músculo.
     */
    fun getFuncion(): String {
        return _state.value?.musculo?.funcion?.ifBlank { "Información no disponible" } 
            ?: "Información no disponible"
    }

    /**
     * Verifica si los datos del músculo son válidos.
     */
    fun isDataValid(): Boolean {
        return _state.value?.hasValidData == true
    }

    /**
     * Obtiene la descripción para accesibilidad.
     */
    fun getAccessibilityDescription(): String {
        val musculo = _state.value?.musculo ?: return "Detalle de músculo"
        return buildString {
            append("Músculo ${musculo.nombre}. ")
            append("Origen: ${musculo.origen}. ")
            append("Inserción: ${musculo.insercion}. ")
            append("Función: ${musculo.funcion}")
        }
    }
}

