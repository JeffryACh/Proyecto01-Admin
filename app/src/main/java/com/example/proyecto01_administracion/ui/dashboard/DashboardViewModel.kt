package com.example.proyecto01_administracion.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto01_administracion.domain.models.FleetStatus
import com.example.proyecto01_administracion.domain.usecase.CalculateFleetStatusUseCase
import com.example.proyecto01_administracion.data.local.dao.MaintenanceDao
import com.example.proyecto01_administracion.data.local.dao.MaintenanceCategoryDao
import com.example.proyecto01_administracion.data.local.dao.MaintenancePlanDao
import com.example.proyecto01_administracion.domain.models.Vehicle
import com.example.proyecto01_administracion.domain.repositories.VehicleRepository
import com.example.proyecto01_administracion.ui.fleet.UpcomingMaintenance
import com.example.proyecto01_administracion.ui.theme.StatusRed
import com.example.proyecto01_administracion.ui.theme.StatusYellow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val upToDateCount:Int=0,val dueSoonCount:Int=0,val overdueCount:Int=0,val totalVehicles:Int=0,
    val announcements:List<Announcement> = emptyList(),val upcomingMaintenances:List<UpcomingMaintenance> = emptyList(),
    val vehicles:List<Vehicle> = emptyList(),val isLoading:Boolean=false,val error:String?=null
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val vehicleRepository: VehicleRepository,
    private val planDao: MaintenancePlanDao,
    private val maintenanceDao: MaintenanceDao,
    private val categoryDao: MaintenanceCategoryDao,
    private val calc: CalculateFleetStatusUseCase
) : ViewModel() {
    private val _uiState=MutableStateFlow(DashboardUiState(isLoading=true))
    val uiState:StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init{
        viewModelScope.launch{
            vehicleRepository.getVehicles()
                .catch{e->_uiState.update{it.copy(isLoading=false,error=e.message?:"No se pudo cargar el dashboard")}}
                .collect { vehicles ->
                    val vehicleStatuses = mutableListOf<FleetStatus>()
                    val upcoming = mutableListOf<UpcomingMaintenance>()

                    for (vehicle in vehicles.filter { it.estado == "Activo" }) {
                        val plans = planDao.observeByClassification(vehicle.clasificacion).first()
                        val evaluatedPlans = plans.map { plan ->
                            val maintenance =
                                maintenanceDao.getLatestByCategory(
                                    vehicle.id,
                                    plan.categoryId
                                )

                            val maintenanceBaseline =
                                maintenance?.mileage ?: 0L

                            val status = calc(
                                currentOdometer = vehicle.kilometraje_actual,
                                lastMaintenanceOdometer = maintenance?.mileage,
                                maintenanceInterval = plan.intervalKm.toLong()
                            )

                            val remainingKm =
                                maintenanceBaseline +
                                        plan.intervalKm.toLong() -
                                        vehicle.kilometraje_actual

                            Triple(
                                status,
                                plan,
                                remainingKm
                            )
                        }
                        if (evaluatedPlans.isEmpty()) continue

                        val worst = evaluatedPlans.minBy { (status, _, _) ->
                            when (status) {
                                FleetStatus.MAINTENANCE_OVERDUE -> 0
                                FleetStatus.MAINTENANCE_DUE_SOON -> 1
                                FleetStatus.UP_TO_DATE -> 2
                            }
                        }
                        vehicleStatuses += worst.first

                        if (worst.first != FleetStatus.UP_TO_DATE) {
                            val category = categoryDao.getById(worst.second.categoryId)
                            upcoming += UpcomingMaintenance(
                                vehicleModel = "${vehicle.marca} ${vehicle.modelo}",
                                plate = vehicle.placa,
                                task = category?.name ?: "Mantenimiento",
                                remainingInfo = if (worst.third < 0)
                                    "Atrasado por ${-worst.third} km"
                                else "En ${worst.third} km",
                                statusColor = if (worst.first == FleetStatus.MAINTENANCE_OVERDUE) StatusRed else StatusYellow
                            )
                        }
                    }

                    _uiState.update {
                        it.copy(
                            upToDateCount = vehicleStatuses.count { status -> status == FleetStatus.UP_TO_DATE },
                            dueSoonCount = vehicleStatuses.count { status -> status == FleetStatus.MAINTENANCE_DUE_SOON },
                            overdueCount = vehicleStatuses.count { status -> status == FleetStatus.MAINTENANCE_OVERDUE },
                            totalVehicles = vehicles.size,
                            upcomingMaintenances = upcoming.sortedBy { item -> item.plate },
                            vehicles = vehicles,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }
}
