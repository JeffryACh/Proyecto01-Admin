package com.example.proyecto01_administracion.ui.fleet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto01_administracion.data.local.dao.UserDao
import com.example.proyecto01_administracion.data.local.dao.VehicleAssignmentDao
import com.example.proyecto01_administracion.domain.models.User
import com.example.proyecto01_administracion.domain.models.Vehicle
import com.example.proyecto01_administracion.domain.repositories.UserRepository
import com.example.proyecto01_administracion.domain.repositories.VehicleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserDetailUiState(val user:User?=null,val assignedVehicle:Vehicle?=null,val isLoading:Boolean=false,val error:String?=null)

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val vehicleRepository: VehicleRepository,
    private val assignmentDao: VehicleAssignmentDao,
    private val userDao: UserDao
):ViewModel(){
    private val _uiState=MutableStateFlow(UserDetailUiState())
    val uiState:StateFlow<UserDetailUiState> = _uiState.asStateFlow()

    fun loadUser(userId:String){
        _uiState.update{it.copy(isLoading=true,error=null)}
        viewModelScope.launch{
            runCatching{
                val user=userRepository.getUserById(userId).first() ?: error("Usuario no encontrado")
                val assignment=assignmentDao.observeActiveByUser(userId).first().firstOrNull()
                val vehicle=assignment?.let{vehicleRepository.getVehicleById(it.vehicleId).first()}
                _uiState.update{it.copy(user=user,assignedVehicle=vehicle,isLoading=false,error=null)}
            }.onFailure{e->_uiState.update{it.copy(isLoading=false,error=e.message?:"No se pudo cargar el usuario")}}
        }
    }

    fun suspendUser(){
        val user=_uiState.value.user ?: return
        viewModelScope.launch{
            runCatching{userDao.updateStatus(user.id,"Suspendido")}.fold(
                onSuccess={_uiState.update{it.copy(user=it.user?.copy(estado="Suspendido"))}},
                onFailure={e->_uiState.update{it.copy(error=e.message?:"No se pudo suspender el usuario")}}
            )
        }
    }
}
