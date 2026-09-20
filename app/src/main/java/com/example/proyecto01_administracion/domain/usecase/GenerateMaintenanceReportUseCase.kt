package com.example.proyecto01_administracion.domain.usecase

import com.example.proyecto01_administracion.domain.models.Maintenance
import com.example.proyecto01_administracion.domain.models.MaintenanceReport
import kotlin.math.roundToInt
import javax.inject.Inject

class GenerateMaintenanceReportUseCase @Inject constructor() {

    operator fun invoke(
        maintenances: List<Maintenance>,
        startDateMillis: Long,
        endDateMillis: Long
    ): MaintenanceReport {

        require(startDateMillis <= endDateMillis) {
            "La fecha inicial no puede ser posterior a la fecha final"
        }

        require(maintenances.none { it.costo < 0f }) {
            "El costo del mantenimiento no puede ser negativo"
        }

        val filteredMaintenances =
            maintenances.filter { maintenance ->
                maintenance.fecha.toDate().time in
                        startDateMillis..endDateMillis
            }

        val maintenanceCount =
            filteredMaintenances.size

        val totalCost =
            filteredMaintenances.sumOf {
                it.costo.toDouble()
            }

        val recordsByType =
            filteredMaintenances.groupingBy {
                it.tipo.trim().lowercase()
            }.eachCount()

        fun percentageFor(type: String): Int {
            if (maintenanceCount == 0) {
                return 0
            }

            val typeCount =
                recordsByType[type] ?: 0

            return (
                    typeCount * 100.0 /
                            maintenanceCount
                    ).roundToInt()
        }

        val costByVehicle =
            filteredMaintenances
                .groupBy {
                    it.vehiculo_id
                }
                .mapValues { (_, records) ->
                    records.sumOf {
                        it.costo.toDouble()
                    }
                }

        return MaintenanceReport(
            totalCost = totalCost,
            maintenanceCount = maintenanceCount,
            preventivePercentage =
                percentageFor("preventivo"),
            correctivePercentage =
                percentageFor("correctivo"),
            predictivePercentage =
                percentageFor("predictivo"),
            costByVehicleId = costByVehicle
        )
    }
}