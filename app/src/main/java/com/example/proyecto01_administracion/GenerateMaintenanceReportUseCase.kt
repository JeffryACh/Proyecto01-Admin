package com.example.proyecto01_administracion

import java.time.LocalDate

// Modelo simplificado para la prueba
data class MaintenanceRecord(
    val id: String,
    val date: LocalDate,
    val cost: Double
)

class GenerateMaintenanceReportUseCase {
    operator fun invoke(
        records: List<MaintenanceRecord>,
        startDate: LocalDate,
        endDate: LocalDate
    ): Double {
        return records
            // Filtra registros que no sean anteriores a startDate ni posteriores a endDate
            .filter { !it.date.isBefore(startDate) && !it.date.isAfter(endDate) }
            .sumOf { it.cost }
    }
}
