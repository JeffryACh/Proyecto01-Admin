package com.example.proyecto01_administracion.ui.mechanic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto01_administracion.data.local.dao.MaintenanceDao
import com.example.proyecto01_administracion.data.local.dao.MaintenanceEvidenceDao
import com.example.proyecto01_administracion.data.repository.toDomain
import com.example.proyecto01_administracion.domain.models.Maintenance
import com.example.proyecto01_administracion.domain.models.Vehicle
import com.example.proyecto01_administracion.domain.repositories.VehicleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MaintenanceDetailUiState(
    val maintenance: Maintenance? = null,
    val vehicle: Vehicle? = null,
    val evidenceUris: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class MaintenanceDetailViewModel @Inject constructor(
    private val maintenanceDao: MaintenanceDao,
    private val evidenceDao: MaintenanceEvidenceDao,
    private val vehicleRepository: VehicleRepository
):ViewModel(){
    private val _uiState=MutableStateFlow(MaintenanceDetailUiState())
    val uiState:StateFlow<MaintenanceDetailUiState> = _uiState.asStateFlow()
    fun loadMaintenance(maintenanceId:String){
        _uiState.update{it.copy(isLoading=true,error=null)}
        viewModelScope.launch{
            runCatching{
                val entity=maintenanceDao.getById(maintenanceId) ?: error("Mantenimiento no encontrado")
                val maintenance=entity.toDomain()
                val vehicle=vehicleRepository.getVehicleById(maintenance.vehiculo_id).first()
                val evidenceUris = evidenceDao.observeByMaintenance(maintenance.id).first()
                    .mapNotNull { it.remoteUrl ?: it.localUri }
                _uiState.update {
                    it.copy(
                        maintenance = maintenance,
                        vehicle = vehicle,
                        evidenceUris = evidenceUris,
                        isLoading = false,
                        error = null
                    )
                }
            }.onFailure{e->_uiState.update{it.copy(isLoading=false,error=e.message?:"No se pudo cargar el mantenimiento")}}
        }
    }
}
