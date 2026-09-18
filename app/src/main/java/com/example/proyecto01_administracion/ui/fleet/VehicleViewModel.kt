package com.example.proyecto01_administracion.ui.fleet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto01_administracion.domain.models.Vehicle
import com.example.proyecto01_administracion.domain.repositories.VehicleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class VehicleFormUiState(
    val id: String = "",
    val placa: String = "",
    val marca: String = "",
    val modelo: String = "",
    val anio: String = "",
    val tipo: String = "Liviano",
    val clasificacion: String = "",
    val capacidad: String = "",
    val kilometraje: String = "",
    val estado: String = "Activo",
    val isEdit: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val validationErrors: Map<String, String> = emptyMap()
)

@HiltViewModel
class VehicleViewModel @Inject constructor(
    private val vehicleRepository: VehicleRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(VehicleFormUiState())
    val uiState: StateFlow<VehicleFormUiState> = _uiState.asStateFlow()

    fun loadVehicle(placa: String?) {
        if (placa.isNullOrBlank()) {
            _uiState.update { it.copy(isEdit = false) }
            return
        }
        _uiState.update { it.copy(isEdit = true, isLoading = true, error = null) }
        viewModelScope.launch {
            runCatching { vehicleRepository.findVehicleByPlate(placa) }.fold(
                onSuccess = { v ->
                    if (v == null) _uiState.update { it.copy(isLoading = false, error = "Vehículo no encontrado") }
                    else _uiState.update {
                        it.copy(
                            id = v.id, placa = v.placa, marca = v.marca, modelo = v.modelo, anio = v.anio.toString(), tipo = v.tipo,
                            clasificacion = v.clasificacion, capacidad = v.capacidad.toString(), kilometraje = v.kilometraje_actual.toString(),
                            estado = v.estado, isEdit = true, isLoading = false, error = null
                        )
                    }
                },
                onFailure = { e -> _uiState.update { it.copy(isLoading = false, error = e.message ?: "No se pudo cargar el vehículo") } }
            )
        }
    }

    fun onPlacaChange(v: String) = _uiState.update { it.copy(placa = v, validationErrors = it.validationErrors - "placa") }
    fun onMarcaChange(v: String) = _uiState.update { it.copy(marca = v, validationErrors = it.validationErrors - "marca") }
    fun onModeloChange(v: String) = _uiState.update { it.copy(modelo = v, validationErrors = it.validationErrors - "modelo") }
    fun onAnioChange(v: String) = _uiState.update { it.copy(anio = v, validationErrors = it.validationErrors - "anio") }
    fun onTipoChange(v: String) = _uiState.update { it.copy(tipo = v) }
    fun onClasificacionChange(v: String) = _uiState.update { it.copy(clasificacion = v) }
    fun onCapacidadChange(v: String) = _uiState.update { it.copy(capacidad = v, validationErrors = it.validationErrors - "capacidad") }
    fun onKilometrajeChange(v: String) = _uiState.update { it.copy(kilometraje = v, validationErrors = it.validationErrors - "kilometraje") }
    fun onEstadoChange(v: String) = _uiState.update { it.copy(estado = v) }

    fun saveVehicle() {
        val s = _uiState.value
        val errors = mutableMapOf<String, String>()

        if (s.placa.trim().isBlank()) errors["placa"] = "La placa es obligatoria"
        if (s.marca.trim().isBlank()) errors["marca"] = "La marca es obligatoria"
        if (s.modelo.trim().isBlank()) errors["modelo"] = "El modelo es obligatorio"

        val anioInt = if (s.anio.isNotBlank()) {
            val a = s.anio.toIntOrNull()
            if (a == null || a < 1900 || a > 2100) {
                errors["anio"] = "Ingresa un año válido"
                null
            } else a
        } else 0

        val capacidadFloat = if (s.capacidad.isNotBlank()) {
            val c = s.capacidad.toFloatOrNull()
            if (c == null || c < 0) {
                errors["capacidad"] = "Ingresa una capacidad válida"
                null
            } else c
        } else 0f

        val kmLong = if (!s.isEdit) {
            if (s.kilometraje.isNotBlank()) {
                val k = s.kilometraje.toLongOrNull()
                if (k == null || k < 0) {
                    errors["kilometraje"] = "Ingresa un kilometraje válido"
                    null
                } else k
            } else 0L
        } else s.kilometraje.toLongOrNull() ?: 0L

        if (errors.isNotEmpty()) {
            _uiState.update { it.copy(validationErrors = errors, error = "Por favor, corrige los errores en el formulario") }
            return
        }

        val v = Vehicle(
            id = s.id.ifBlank { UUID.randomUUID().toString() },
            placa = s.placa.trim(),
            marca = s.marca.trim(),
            modelo = s.modelo.trim(),
            anio = anioInt ?: 0,
            tipo = s.tipo,
            clasificacion = s.clasificacion.trim(),
            capacidad = capacidadFloat ?: 0f,
            kilometraje_actual = kmLong ?: 0L,
            estado = s.estado
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, isSuccess = false) }
            vehicleRepository.saveVehicle(v).fold(
                onSuccess = { _uiState.update { it.copy(id = v.id, isLoading = false, isSuccess = true) } },
                onFailure = { e -> _uiState.update { it.copy(isLoading = false, error = e.message ?: "No se pudo guardar el vehículo") } }
            )
        }
    }
}
