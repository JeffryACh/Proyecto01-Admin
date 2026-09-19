package com.example.proyecto01_administracion.ui.vehicle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto01_administracion.data.auth.SessionManager
import com.example.proyecto01_administracion.data.local.dao.VehicleAssignmentDao
import com.example.proyecto01_administracion.domain.models.MileageRecord
import com.example.proyecto01_administracion.domain.models.Vehicle
import com.example.proyecto01_administracion.domain.repositories.MileageRepository
import com.example.proyecto01_administracion.domain.repositories.VehicleRepository
import com.example.proyecto01_administracion.domain.usecase.RegisterMileageUseCase
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class MileageUiState(
    val vehicle: Vehicle? = null,
    val history: List<MileageRecord> = emptyList(),
    val currentMileageInput: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val validationErrors: Map<String, String> = emptyMap()
)

@HiltViewModel
class MileageViewModel @Inject constructor(
    private val vehicleRepository: VehicleRepository,
    private val mileageRepository: MileageRepository,
    private val assignmentDao: VehicleAssignmentDao,
    private val sessionManager: SessionManager,
    private val registerMileageUseCase: RegisterMileageUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(MileageUiState(isLoading = true))
    val uiState: StateFlow<MileageUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        val userId = sessionManager.currentUser.value?.id
        if (userId.isNullOrBlank()) {
            _uiState.update { it.copy(isLoading = false, error = "No hay una sesión activa") }
            return
        }
        viewModelScope.launch {
            runCatching {
                val assignment = assignmentDao.observeActiveByUser(userId).first().firstOrNull()
                if (assignment == null) {
                    _uiState.update { it.copy(vehicle = null, history = emptyList(), isLoading = false, error = null) }
                    return@runCatching
                }
                val vehicle = vehicleRepository.getVehicleById(assignment.vehicleId).first()
                if (vehicle == null) {
                    _uiState.update { it.copy(isLoading = false, error = "Vehículo asignado no encontrado") }
                    return@runCatching
                }
                _uiState.update { it.copy(vehicle = vehicle, isLoading = false, error = null) }
                mileageRepository.getMileageHistory(vehicle.id)
                    .catch { e -> _uiState.update { it.copy(error = e.message ?: "No se pudo cargar el historial") } }
                    .collect { history -> _uiState.update { it.copy(history = history) } }
            }.onFailure { e -> _uiState.update { it.copy(isLoading = false, error = e.message ?: "No se pudo cargar el kilometraje") } }
        }
    }

    fun onMileageChange(v: String) = _uiState.update { it.copy(currentMileageInput = v, validationErrors = it.validationErrors - "mileage", isSuccess = false) }

    fun registerMileage() {
        val s = _uiState.value
        val vehicle = s.vehicle ?: run { _uiState.update { it.copy(error = "No tienes un vehículo asignado") }; return }
        
        val errors = mutableMapOf<String, String>()
        val km = s.currentMileageInput.toLongOrNull()
        
        if (s.currentMileageInput.isBlank()) {
            errors["mileage"] = "El kilometraje es obligatorio"
        } else if (km == null || km < 0) {
            errors["mileage"] = "Ingresa un valor numérico válido"
        } else if (km <= vehicle.kilometraje_actual) {
            errors["mileage"] = "Debe ser mayor al kilometraje actual (${vehicle.kilometraje_actual} km)"
        }

        if (errors.isNotEmpty()) {
            _uiState.update { it.copy(validationErrors = errors) }
            return
        }

        val userId = sessionManager.currentUser.value?.id ?: run { _uiState.update { it.copy(error = "No hay una sesión activa") }; return }
        val record = MileageRecord(id = UUID.randomUUID().toString(), vehiculo_id = vehicle.id, usuario_id = userId, kilometraje = km!!, fecha = Timestamp.now())

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    isSuccess = false
                )
            }

            registerMileageUseCase(record).fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            vehicle = vehicle.copy(
                                kilometraje_actual = record.kilometraje
                            ),
                            currentMileageInput = "",
                            isLoading = false,
                            isSuccess = true,
                            validationErrors = emptyMap()
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = false,
                            error = error.message
                                ?: "No se pudo registrar el kilometraje"
                        )
                    }
                }
            )
        }
    }
}
