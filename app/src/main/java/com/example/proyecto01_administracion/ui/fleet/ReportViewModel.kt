package com.example.proyecto01_administracion.ui.fleet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto01_administracion.data.local.dao.MaintenanceDao
import com.example.proyecto01_administracion.domain.repositories.VehicleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

data class ReportUiState(
    val totalCost:Float=0f,val preventivoPercent:Int=0,val correctivoPercent:Int=0,val predictivoPercent:Int=0,
    val topVehicles:List<VehicleExpense> = emptyList(),val isLoading:Boolean=false,val error:String?=null
)
data class VehicleExpense(val plate:String,val amount:String)

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val maintenanceDao: MaintenanceDao,
    private val vehicleRepository: VehicleRepository
):ViewModel(){
    private val _uiState=MutableStateFlow(ReportUiState(isLoading=true))
    val uiState:StateFlow<ReportUiState> = _uiState.asStateFlow()
    init{
        viewModelScope.launch{
            combine(maintenanceDao.observeAll(),vehicleRepository.getVehicles()){records,vehicles->records to vehicles}
                .catch{e->_uiState.update{it.copy(isLoading=false,error=e.message?:"No se pudo generar el reporte")}}
                .collect{(records,vehicles)->
                    val cutoff = System.currentTimeMillis() - 30L * 24L * 60L * 60L * 1000L
                    val periodRecords = records.filter { it.date >= cutoff }
                    val total=periodRecords.sumOf{it.cost}
                    val count=periodRecords.size.coerceAtLeast(1)
                    val byType=periodRecords.groupingBy{it.type.lowercase()}.eachCount()
                    val plates=vehicles.associateBy({it.id},{it.placa})
                    val top=periodRecords.groupBy{it.vehicleId}.map{(id,rs)->id to rs.sumOf{it.cost}}
                        .sortedByDescending{it.second}.take(5)
                        .map{(id,cost)->VehicleExpense(plates[id]?:"Vehículo", "₡${"%.2f".format(cost)}")}
                    _uiState.update{it.copy(
                        totalCost=total.toFloat(),
                        preventivoPercent=((byType["preventivo"]?:0)*100f/count).roundToInt(),
                        correctivoPercent=((byType["correctivo"]?:0)*100f/count).roundToInt(),
                        predictivoPercent=((byType["predictivo"]?:0)*100f/count).roundToInt(),
                        topVehicles=top,isLoading=false,error=null
                    )}
                }
        }
    }
}
