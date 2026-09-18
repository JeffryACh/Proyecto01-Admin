package com.example.proyecto01_administracion.ui.fleet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto01_administracion.data.local.dao.VehicleAssignmentDao
import com.example.proyecto01_administracion.data.local.dao.VehicleDao
import com.example.proyecto01_administracion.domain.models.User
import com.example.proyecto01_administracion.domain.models.Vehicle
import com.example.proyecto01_administracion.domain.repositories.UserRepository
import com.example.proyecto01_administracion.domain.repositories.VehicleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FleetVehicleDetailUiState(val vehicle: Vehicle?=null,val assignedDriver:User?=null,val isLoading:Boolean=false,val error:String?=null)

@HiltViewModel
class FleetVehicleDetailViewModel @Inject constructor(
    private val vehicleRepository: VehicleRepository,
    private val userRepository: UserRepository,
    private val assignmentDao: VehicleAssignmentDao,
    private val vehicleDao: VehicleDao
):ViewModel(){
    private val _uiState=MutableStateFlow(FleetVehicleDetailUiState())
    val uiState:StateFlow<FleetVehicleDetailUiState> = _uiState.asStateFlow()

    fun loadVehicle(plate:String){
        _uiState.update{it.copy(isLoading=true,error=null)}
        viewModelScope.launch{
            runCatching{
                val v=vehicleRepository.findVehicleByPlate(plate) ?: error("Vehículo no encontrado")
                val assignment=assignmentDao.getActiveByVehicle(v.id)
                val driver=assignment?.let{userRepository.getUserById(it.userId).first()}
                _uiState.update{it.copy(vehicle=v,assignedDriver=driver,isLoading=false,error=null)}
            }.onFailure{e->_uiState.update{it.copy(isLoading=false,error=e.message?:"No se pudo cargar el vehículo")}}
        }
    }

    fun deactivateVehicle(){
        val id=_uiState.value.vehicle?.id ?: return
        viewModelScope.launch{
            runCatching{vehicleDao.updateStatus(id,"Inactivo")}.fold(
                onSuccess={_uiState.update{it.copy(vehicle=it.vehicle?.copy(estado="Inactivo"))}},
                onFailure={e->_uiState.update{it.copy(error=e.message?:"No se pudo desactivar el vehículo")}}
            )
        }
    }
}
