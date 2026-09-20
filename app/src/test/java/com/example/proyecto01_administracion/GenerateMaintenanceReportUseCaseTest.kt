package com.example.proyecto01_administracion

import com.example.proyecto01_administracion.domain.models.Maintenance
import com.example.proyecto01_administracion.domain.usecase.GenerateMaintenanceReportUseCase
import com.google.firebase.Timestamp
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Date

class GenerateMaintenanceReportUseCaseTest {

    private val useCase =
        GenerateMaintenanceReportUseCase()

    @Test
    fun suma_solo_mantenimientos_dentro_del_rango() {
        val maintenances = listOf(
            createMaintenance(
                id = "1",
                vehicleId = "vehicle-1",
                dateMillis = 2_000L,
                cost = 50_000f
            ),
            createMaintenance(
                id = "2",
                vehicleId = "vehicle-2",
                dateMillis = 3_000L,
                cost = 25_000f
            ),
            createMaintenance(
                id = "3",
                vehicleId = "vehicle-1",
                dateMillis = 500L,
                cost = 10_000f
            )
        )

        val report = useCase(
            maintenances = maintenances,
            startDateMillis = 1_000L,
            endDateMillis = 4_000L
        )

        assertEquals(
            75_000.0,
            report.totalCost,
            0.0
        )

        assertEquals(
            2,
            report.maintenanceCount
        )

        assertEquals(
            50_000.0,
            report.costByVehicleId["vehicle-1"]!!,
            0.0
        )

        assertEquals(
            25_000.0,
            report.costByVehicleId["vehicle-2"]!!,
            0.0
        )
    }

    @Test
    fun retorna_reporte_vacio_sin_registros() {
        val report = useCase(
            maintenances = emptyList(),
            startDateMillis = 1_000L,
            endDateMillis = 4_000L
        )

        assertEquals(
            0.0,
            report.totalCost,
            0.0
        )

        assertEquals(
            0,
            report.maintenanceCount
        )

        assertEquals(
            0,
            report.preventivePercentage
        )

        assertTrue(
            report.costByVehicleId.isEmpty()
        )
    }

    private fun createMaintenance(
        id: String,
        vehicleId: String,
        dateMillis: Long,
        cost: Float,
        type: String = "Preventivo"
    ): Maintenance {
        return Maintenance(
            id = id,
            vehiculo_id = vehicleId,
            tipo = type,
            categoria_id = "category-test",
            fecha = Timestamp(
                Date(dateMillis)
            ),
            kilometraje = 10_000L,
            costo = cost,
            registrado_por = "user-test"
        )
    }
}