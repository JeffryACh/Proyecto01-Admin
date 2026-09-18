package com.example.proyecto01_administracion.ui.fleet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto01_administracion.data.auth.SessionManager
import com.example.proyecto01_administracion.data.local.dao.VehicleAssignmentDao
import com.example.proyecto01_administracion.data.local.entity.VehicleAssignmentEntity
import com.example.proyecto01_administracion.domain.models.User
import com.example.proyecto01_administracion.domain.models.Vehicle
import com.example.proyecto01_administracion.domain.repositories.UserRepository
import com.example.proyecto01_administracion.domain.repositories.VehicleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class AssignmentUiState(
    val user: User? = null,
    val vehicle: Vehicle? = null,
    val drivers: List<User> = emptyList(),
    val vehicles: List<Vehicle> = emptyList(),
    val selectedDriverId: String = "",
    val selectedVehicleId: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

@HiltViewModel
class AssignmentViewModel @Inject constructor(
    private val vehicleRepository: VehicleRepository,
    private val userRepository: UserRepository,
    private val assignmentDao: VehicleAssignmentDao,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(AssignmentUiState())
    val uiState: StateFlow<AssignmentUiState> = _uiState.asStateFlow()

    fun loadData(id: String) {
        _uiState.update { it.copy(isLoading = true, error = null, isSuccess = false) }
        viewModelScope.launch {
            runCatching {
                val vehicle = vehicleRepository.findVehicleByPlate(id)
                if (vehicle != null) {
                    val drivers = userRepository.getUsers().first().filter { it.rol_id == "DRIVER" && it.estado == "Activo" }
                    _uiState.update { it.copy(vehicle = vehicle, drivers = drivers, isLoading = false) }
                } else {
                    val user = userRepository.getUserById(id).first()
                    if (user == null) error("Usuario no encontrado")
                    val vehicles = vehicleRepository.getVehicles().first().filter { it.estado == "Activo" }
                    _uiState.update { it.copy(user = user, vehicles = vehicles, isLoading = false) }
                }
            }.onFailure { e -> _uiState.update { it.copy(isLoading = false, error = e.message ?: "No se pudieron cargar los datos") } }
        }
    }

    fun onDriverSelected(id: String) = _uiState.update { it.copy(selectedDriverId = id, error = null) }
    fun onVehicleSelected(id: String) = _uiState.update { it.copy(selectedVehicleId = id, error = null) }

    fun reassign() {
        val state = _uiState.value
        val vehicleId = state.vehicle?.id ?: state.selectedVehicleId
        val userId = state.user?.id ?: state.selectedDriverId
        if (vehicleId.isBlank() || userId.isBlank()) {
            _uiState.update { it.copy(error = if (state.vehicle != null) "Debe seleccionar un conductor" else "Debe seleccionar un vehículo") }
            return
        }
        val assignedBy = sessionManager.currentUser.value?.id
        if (assignedBy.isNullOrBlank()) {
            _uiState.update { it.copy(error = "No hay una sesión activa") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, isSuccess = false) }
            runCatching {
                assignmentDao.closeActiveByVehicle(vehicleId)
                assignmentDao.closeActiveByUser(userId)
                assignmentDao.upsert(
                    VehicleAssignmentEntity(
                        id = UUID.randomUUID().toString(),
                        vehicleId = vehicleId,
                        userId = userId,
                        startDate = System.currentTimeMillis(),
                        assignedBy = assignedBy
                    )
                )
            }.fold(
                onSuccess = { _uiState.update { it.copy(isLoading = false, isSuccess = true) } },
                onFailure = { e -> _uiState.update { it.copy(isLoading = false, error = e.message ?: "No se pudo guardar la asignación") } }
            )
        }
    }
}
