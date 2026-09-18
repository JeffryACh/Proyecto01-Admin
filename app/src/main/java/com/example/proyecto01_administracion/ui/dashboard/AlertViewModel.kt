package com.example.proyecto01_administracion.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto01_administracion.data.auth.SessionManager
import com.example.proyecto01_administracion.domain.models.Alert
import com.example.proyecto01_administracion.domain.repositories.AlertRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AlertUiState(val alerts:List<Alert> = emptyList(),val isLoading:Boolean=false,val error:String?=null)

@HiltViewModel
class AlertViewModel @Inject constructor(
    private val alertRepository: AlertRepository,
    private val sessionManager: SessionManager
):ViewModel(){
    private val _uiState=MutableStateFlow(AlertUiState(isLoading=true))
    val uiState:StateFlow<AlertUiState> = _uiState.asStateFlow()

    init {
        val userId=sessionManager.currentUser.value?.id
        if(userId.isNullOrBlank()) _uiState.update{it.copy(isLoading=false,error="No hay una sesión activa")}
        else viewModelScope.launch{
            alertRepository.getAlertsForUser(userId)
                .catch{e->_uiState.update{it.copy(isLoading=false,error=e.message?:"No se pudieron cargar las alertas")}}
                .collect{alerts->_uiState.update{it.copy(alerts=alerts,isLoading=false,error=null)}}
        }
    }

    fun markAsRead(alertId:String){
        val userId=sessionManager.currentUser.value?.id ?: return
        viewModelScope.launch{
            alertRepository.markAlertAsRead(alertId,userId).onFailure{e->_uiState.update{it.copy(error=e.message?:"No se pudo marcar la alerta")}}
        }
    }
}
