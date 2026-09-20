package com.example.proyecto01_administracion.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto01_administracion.domain.models.FleetStatus
import com.example.proyecto01_administracion.domain.usecase.CalculateFleetStatusUseCase
import com.example.proyecto01_administracion.data.local.dao.MaintenanceDao
import com.example.proyecto01_administracion.data.local.dao.MaintenancePlanDao
import com.example.proyecto01_administracion.domain.repositories.VehicleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MechanicDashboardUiState(val pendingCount:Int=0,val completedCount:Int=0,val isLoading:Boolean=false,val error:String?=null)

@HiltViewModel
class MechanicDashboardViewModel @Inject constructor(
    private val vehicleRepository: VehicleRepository,
    private val planDao: MaintenancePlanDao,
    private val maintenanceDao: MaintenanceDao,
    private val calc: CalculateFleetStatusUseCase
) : ViewModel() {
    private val _uiState=MutableStateFlow(MechanicDashboardUiState(isLoading=true))
    val uiState:StateFlow<MechanicDashboardUiState> = _uiState.asStateFlow()

    init{
        viewModelScope.launch{
            combine(vehicleRepository.getVehicles(),maintenanceDao.observeAll()){vehicles,maintenances->vehicles to maintenances}
                .catch{e->_uiState.update{it.copy(isLoading=false,error=e.message?:"No se pudo cargar el resumen")}}
                .collect{(vehicles,maintenances)->
                    var pending=0
                    for(v in vehicles.filter{it.estado=="Activo"}){
                        val plans=planDao.observeByClassification(v.clasificacion).first()
                        val statuses = plans.map { plan ->
                            val maintenance =
                                maintenanceDao.getLatestByCategory(
                                    v.id,
                                    plan.categoryId
                                )

                            calc(
                                currentOdometer = v.kilometraje_actual,
                                lastMaintenanceOdometer = maintenance?.mileage,
                                maintenanceInterval = plan.intervalKm.toLong()
                            )
                        }
                        if(statuses.any{it==FleetStatus.MAINTENANCE_DUE_SOON||it==FleetStatus.MAINTENANCE_OVERDUE}) pending++
                    }
                    _uiState.update{it.copy(pendingCount=pending,completedCount=maintenances.size,isLoading=false,error=null)}
                }
        }
    }
}
