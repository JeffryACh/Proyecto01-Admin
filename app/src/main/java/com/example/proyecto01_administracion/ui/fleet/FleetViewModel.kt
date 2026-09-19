package com.example.proyecto01_administracion.ui.fleet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto01_administracion.domain.models.FleetStatus
import com.example.proyecto01_administracion.domain.usecase.CalculateFleetStatusUseCase
import com.example.proyecto01_administracion.data.local.dao.MaintenanceDao
import com.example.proyecto01_administracion.data.local.dao.MaintenancePlanDao
import com.example.proyecto01_administracion.domain.models.Vehicle
import com.example.proyecto01_administracion.domain.repositories.VehicleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FleetUiState(
    val vehicles: List<Vehicle> = emptyList(),
    val fleetStatusByVehicleId: Map<String, FleetStatus> = emptyMap(),
    val searchQuery: String = "",
    val selectedFilter: String = "Todos",
    val upToDateCount: Int = 0,
    val dueSoonCount: Int = 0,
    val overdueCount: Int = 0,
    val inactiveCount: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class FleetViewModel @Inject constructor(
    private val vehicleRepository: VehicleRepository,
    private val maintenancePlanDao: MaintenancePlanDao,
    private val maintenanceDao: MaintenanceDao
) : ViewModel() {
    private val _uiState = MutableStateFlow(FleetUiState(isLoading = true))
    val uiState: StateFlow<FleetUiState> = _uiState.asStateFlow()
    private val calculateStatus = CalculateFleetStatusUseCase()

    init {
        viewModelScope.launch {
            vehicleRepository.getVehicles()
                .catch { e -> _uiState.update { it.copy(isLoading = false, error = e.message ?: "No se pudo cargar la flota") } }
                .collect { vehicles -> refreshStatuses(vehicles) }
        }
    }

    private suspend fun refreshStatuses(vehicles: List<Vehicle>) {
        val statuses = mutableMapOf<String, FleetStatus>()
        for (vehicle in vehicles.filter { it.estado == "Activo" }) {
            val plans = maintenancePlanDao.observeByClassification(vehicle.clasificacion).first()
            if (plans.isEmpty()) continue
            val perPlan = plans.map { plan ->
                val lastMaintenance =
                    maintenanceDao.getLatestByCategory(
                        vehicle.id,
                        plan.categoryId
                    )

                calculateStatus(
                    currentOdometer = vehicle.kilometraje_actual,
                    lastMaintenanceOdometer = lastMaintenance?.mileage,
                    maintenanceInterval = plan.intervalKm.toLong()
                )
            }
            if (perPlan.isNotEmpty()) {
                statuses[vehicle.id] = when {
                    FleetStatus.MAINTENANCE_OVERDUE in perPlan -> FleetStatus.MAINTENANCE_OVERDUE
                    FleetStatus.MAINTENANCE_DUE_SOON in perPlan -> FleetStatus.MAINTENANCE_DUE_SOON
                    else -> FleetStatus.UP_TO_DATE
                }
            }
        }
        _uiState.update {
            it.copy(
                vehicles = vehicles,
                fleetStatusByVehicleId = statuses,
                upToDateCount = statuses.values.count { s -> s == FleetStatus.UP_TO_DATE },
                dueSoonCount = statuses.values.count { s -> s == FleetStatus.MAINTENANCE_DUE_SOON },
                overdueCount = statuses.values.count { s -> s == FleetStatus.MAINTENANCE_OVERDUE },
                inactiveCount = vehicles.count { v -> v.estado == "Inactivo" },
                isLoading = false,
                error = null
            )
        }
    }

    fun onSearchQueryChange(query: String) = _uiState.update { it.copy(searchQuery = query) }
    fun onFilterChange(filter: String) = _uiState.update { it.copy(selectedFilter = filter) }
}
