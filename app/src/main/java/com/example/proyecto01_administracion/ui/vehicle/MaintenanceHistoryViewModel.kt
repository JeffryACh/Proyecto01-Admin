package com.example.proyecto01_administracion.ui.vehicle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto01_administracion.data.auth.SessionManager
import com.example.proyecto01_administracion.data.local.dao.VehicleAssignmentDao
import com.example.proyecto01_administracion.domain.models.Maintenance
import com.example.proyecto01_administracion.domain.models.Vehicle
import com.example.proyecto01_administracion.domain.repositories.MaintenanceRepository
import com.example.proyecto01_administracion.domain.repositories.VehicleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MaintenanceHistoryUiState(
    val vehicle:Vehicle?=null,val maintenanceRecords:List<Maintenance> = emptyList(),val filter:String="Todos",
    val isLoading:Boolean=false,val error:String?=null
)

@HiltViewModel
class MaintenanceHistoryViewModel @Inject constructor(
    private val vehicleRepository: VehicleRepository,
    private val maintenanceRepository: MaintenanceRepository,
    private val assignmentDao: VehicleAssignmentDao,
    private val sessionManager: SessionManager
):ViewModel(){
    private val _uiState=MutableStateFlow(MaintenanceHistoryUiState())
    val uiState:StateFlow<MaintenanceHistoryUiState> = _uiState.asStateFlow()
    fun loadData(vehicleId:String?=null){
        _uiState.update{it.copy(isLoading=true,error=null)}
        viewModelScope.launch{
            runCatching{
                val resolvedId=vehicleId ?: sessionManager.currentUser.value?.id?.let{uid->assignmentDao.observeActiveByUser(uid).first().firstOrNull()?.vehicleId}
                if(resolvedId.isNullOrBlank()){
                    _uiState.update{it.copy(vehicle=null,maintenanceRecords=emptyList(),isLoading=false,error=null)}
                    return@runCatching
                }
                val vehicle=vehicleRepository.getVehicleById(resolvedId).first()
                if(vehicle==null){_uiState.update{it.copy(isLoading=false,error="Vehículo no encontrado")};return@runCatching}
                _uiState.update{it.copy(vehicle=vehicle,isLoading=false,error=null)}
                maintenanceRepository.getMaintenancesByVehicle(resolvedId)
                    .catch{e->_uiState.update{it.copy(error=e.message?:"No se pudo cargar el historial")}}
                    .collect{records->_uiState.update{it.copy(maintenanceRecords=records,isLoading=false)}}
            }.onFailure{e->_uiState.update{it.copy(isLoading=false,error=e.message?:"No se pudo cargar el historial")}}
        }
    }
    fun onFilterChange(filter:String)=_uiState.update{it.copy(filter=filter)}
}
