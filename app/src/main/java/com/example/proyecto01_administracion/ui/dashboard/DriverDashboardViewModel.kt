package com.example.proyecto01_administracion.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto01_administracion.domain.models.FleetStatus
import com.example.proyecto01_administracion.domain.usecase.CalculateFleetStatusUseCase
import com.example.proyecto01_administracion.data.auth.SessionManager
import com.example.proyecto01_administracion.data.local.dao.MaintenanceDao
import com.example.proyecto01_administracion.data.local.dao.MaintenancePlanDao
import com.example.proyecto01_administracion.data.local.dao.MileageRecordDao
import com.example.proyecto01_administracion.data.local.dao.VehicleAssignmentDao
import com.example.proyecto01_administracion.data.repository.toDomain
import com.example.proyecto01_administracion.domain.models.Maintenance
import com.example.proyecto01_administracion.domain.models.MileageRecord
import com.example.proyecto01_administracion.domain.models.Vehicle
import com.example.proyecto01_administracion.domain.repositories.VehicleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DriverDashboardUiState(
    val vehicle: Vehicle? = null,
    val fleetStatus: FleetStatus? = null,
    val recentMaintenance: Maintenance? = null,
    val latestMileage: MileageRecord? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class DriverDashboardViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val assignmentDao: VehicleAssignmentDao,
    private val vehicleRepository: VehicleRepository,
    private val planDao: MaintenancePlanDao,
    private val maintenanceDao: MaintenanceDao,
    private val mileageDao: MileageRecordDao,
    private val calc: CalculateFleetStatusUseCase
) : ViewModel() {
    private val _uiState=MutableStateFlow(DriverDashboardUiState(isLoading=true))
    val uiState:StateFlow<DriverDashboardUiState> = _uiState.asStateFlow()

    init{ loadData() }

    private fun loadData(){
        val userId=sessionManager.currentUser.value?.id ?: run{_uiState.update{it.copy(isLoading=false,error="No hay sesión activa")};return}
        viewModelScope.launch{
            runCatching{
                val assignment=assignmentDao.observeActiveByUser(userId).first().firstOrNull()
                if(assignment==null){_uiState.update{it.copy(vehicle=null,fleetStatus=null,isLoading=false,error=null)};return@runCatching}
                val vehicle=vehicleRepository.getVehicleById(assignment.vehicleId).first() ?: error("Vehículo asignado no encontrado")
                val plans=planDao.observeByClassification(vehicle.clasificacion).first()
                val maintenanceHistory = maintenanceDao.observeByVehicle(vehicle.id).first()
                val recentMaintenance = maintenanceHistory.firstOrNull()?.toDomain()
                val latestMileage = mileageDao.getLatest(vehicle.id)?.toDomain()
                val statuses = plans.map { plan ->
                    val maintenance =
                        maintenanceDao.getLatestByCategory(
                            vehicle.id,
                            plan.categoryId
                        )

                    calc(
                        currentOdometer = vehicle.kilometraje_actual,
                        lastMaintenanceOdometer = maintenance?.mileage,
                        maintenanceInterval = plan.intervalKm.toLong()
                    )
                }
                val status=if(statuses.isEmpty()) null else when{
                    FleetStatus.MAINTENANCE_OVERDUE in statuses -> FleetStatus.MAINTENANCE_OVERDUE
                    FleetStatus.MAINTENANCE_DUE_SOON in statuses -> FleetStatus.MAINTENANCE_DUE_SOON
                    else -> FleetStatus.UP_TO_DATE
                }
                _uiState.update {
                    it.copy(
                        vehicle = vehicle,
                        fleetStatus = status,
                        recentMaintenance = recentMaintenance,
                        latestMileage = latestMileage,
                        isLoading = false,
                        error = null
                    )
                }
            }.onFailure{e->_uiState.update{it.copy(isLoading=false,error=e.message?:"No se pudo cargar el vehículo")}}
        }
    }
}
