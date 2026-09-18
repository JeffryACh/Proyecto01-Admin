package com.example.proyecto01_administracion.ui.mechanic

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto01_administracion.data.auth.SessionManager
import com.example.proyecto01_administracion.domain.models.Maintenance
import com.example.proyecto01_administracion.domain.models.MaintenanceCategory
import com.example.proyecto01_administracion.domain.repositories.MaintenanceRepository
import com.example.proyecto01_administracion.domain.repositories.VehicleRepository
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class MaintenanceFormUiState(
    val vehiculoId: String = "",
    val plate: String = "",
    val vehicleMileage: Long = 0L,
    val tipo: String = "Preventivo",
    val categoriaId: String = "",
    val categorias: List<MaintenanceCategory> = emptyList(),
    val taller: String = "",
    val kilometraje: String = "",
    val costo: String = "",
    val descripcion: String = "",
    val evidencias: List<Uri> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val validationErrors: Map<String, String> = emptyMap()
)

@HiltViewModel
class MaintenanceViewModel @Inject constructor(
    private val maintenanceRepository: MaintenanceRepository,
    private val vehicleRepository: VehicleRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(MaintenanceFormUiState(isLoading = true))
    val uiState: StateFlow<MaintenanceFormUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            maintenanceRepository.getCategories()
                .catch { e -> _uiState.update { it.copy(isLoading = false, error = e.message ?: "No se pudieron cargar las categorías") } }
                .collect { categories -> _uiState.update { it.copy(categorias = categories, isLoading = false, error = null) } }
        }
    }

    fun setVehicleFromPlate(plate: String) {
        viewModelScope.launch {
            runCatching { vehicleRepository.findVehicleByPlate(plate) }.fold(
                onSuccess = { v ->
                    _uiState.update {
                        it.copy(
                            vehiculoId = v?.id.orEmpty(),
                            plate = plate,
                            vehicleMileage = v?.kilometraje_actual ?: 0L,
                            error = if (v == null) "Vehículo no encontrado" else null
                        )
                    }
                },
                onFailure = { e -> _uiState.update { it.copy(error = e.message ?: "No se pudo cargar el vehículo") } }
            )
        }
    }

    fun onVehiculoIdChange(v: String) = _uiState.update { it.copy(vehiculoId = v, validationErrors = it.validationErrors - "vehiculo") }
    fun onTipoChange(v: String) = _uiState.update { it.copy(tipo = v, validationErrors = it.validationErrors - "tipo") }
    fun onCategoriaChange(v: String) = _uiState.update { it.copy(categoriaId = v, validationErrors = it.validationErrors - "categoria") }
    fun onTallerChange(v: String) = _uiState.update { it.copy(taller = v) }
    fun onKilometrajeChange(v: String) = _uiState.update { it.copy(kilometraje = v, validationErrors = it.validationErrors - "kilometraje") }
    fun onCostoChange(v: String) = _uiState.update { it.copy(costo = v, validationErrors = it.validationErrors - "costo") }
    fun onDescripcionChange(v: String) = _uiState.update { it.copy(descripcion = v) }
    fun addEvidence(uri: Uri) = _uiState.update { it.copy(evidencias = it.evidencias + uri) }
    fun removeEvidence(uri: Uri) = _uiState.update { it.copy(evidencias = it.evidencias - uri) }

    fun saveMaintenance() {
        val s = _uiState.value
        val errors = mutableMapOf<String, String>()

        if (s.vehiculoId.isBlank()) errors["vehiculo"] = "El vehículo es obligatorio"
        if (s.categoriaId.isBlank()) errors["categoria"] = "La categoría es obligatoria"
        
        val km = s.kilometraje.toLongOrNull()
        if (s.kilometraje.isBlank()) {
            errors["kilometraje"] = "El kilometraje es obligatorio"
        } else if (km == null || km < 0) {
            errors["kilometraje"] = "Ingresa un kilometraje válido"
        } else if (km < s.vehicleMileage) {
            errors["kilometraje"] = "No puede ser menor al kilometraje actual (${s.vehicleMileage} km)"
        }

        val costo = if (s.costo.isNotBlank()) {
            val c = s.costo.toFloatOrNull()
            if (c == null || c < 0) {
                errors["costo"] = "Ingresa un costo válido"
                null
            } else c
        } else 0f

        if (errors.isNotEmpty()) {
            _uiState.update { it.copy(validationErrors = errors, error = "Por favor, corrige los errores en el formulario") }
            return
        }

        val userId = sessionManager.currentUser.value?.id
        if (userId.isNullOrBlank()) {
            _uiState.update { it.copy(error = "No hay una sesión activa") }
            return
        }

        val maintenance = Maintenance(
            id = UUID.randomUUID().toString(),
            vehiculo_id = s.vehiculoId,
            tipo = s.tipo,
            categoria_id = s.categoriaId,
            fecha = Timestamp.now(),
            taller = s.taller.trim(),
            kilometraje = km!!,
            costo = costo ?: 0f,
            descripcion = s.descripcion.trim(),
            registrado_por = userId
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, isSuccess = false) }
            maintenanceRepository.saveMaintenance(maintenance, s.evidencias.map(Uri::toString)).fold(
                onSuccess = { _uiState.update { it.copy(isLoading = false, isSuccess = true) } },
                onFailure = { e -> _uiState.update { it.copy(isLoading = false, error = e.message ?: "No se pudo registrar el mantenimiento") } }
            )
        }
    }
}
