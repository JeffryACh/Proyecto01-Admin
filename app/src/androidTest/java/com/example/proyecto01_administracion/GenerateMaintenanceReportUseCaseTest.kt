package com.example.proyecto01_administracion

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class GenerateMaintenanceReportUseCaseTest {

    private val generateReport = GenerateMaintenanceReportUseCase()

    @Test
    fun `suma unicamente los mantenimientos dentro del rango de fechas`() {
        // Arrange
        val records = listOf(
            MaintenanceRecord("1", LocalDate.of(2026, 8, 10), 50000.0), // Dentro del rango
            MaintenanceRecord("2", LocalDate.of(2026, 8, 15), 25000.0), // Dentro del rango
            MaintenanceRecord("3", LocalDate.of(2026, 7, 30), 10000.0)  // Fuera del rango (Julio)
        )

        // Act
        val total = generateReport(
            records = records,
            startDate = LocalDate.of(2026, 8, 1),
            endDate = LocalDate.of(2026, 8, 31)
        )

        // Assert (Se espera 75,000. El tercer parámetro es el margen de error para decimales)
        assertEquals(75000.0, total, 0.0)
    }

    @Test
    fun `retorna cero si no hay mantenimientos en el rango`() {
        val records = listOf(
            MaintenanceRecord("1", LocalDate.of(2026, 4, 10), 50000.0)
        )

        val total = generateReport(
            records = records,
            startDate = LocalDate.of(2026, 8, 1),
            endDate = LocalDate.of(2026, 8, 31)
        )

        assertEquals(0.0, total, 0.0)
    }
}