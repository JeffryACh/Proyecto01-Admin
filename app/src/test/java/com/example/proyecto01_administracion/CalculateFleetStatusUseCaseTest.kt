package com.example.proyecto01_administracion

import com.example.proyecto01_administracion.domain.models.FleetStatus
import com.example.proyecto01_administracion.domain.usecase.CalculateFleetStatusUseCase
import org.junit.Assert.assertEquals
import org.junit.Test

class CalculateFleetStatusUseCaseTest {

    private val calculateStatus =
        CalculateFleetStatusUseCase()

    @Test
    fun vehiculo_con_margen_seguro_retorna_UP_TO_DATE_Verde() {
        val status = calculateStatus(
            currentOdometer = 55_000L,
            lastMaintenanceOdometer = 50_000L,
            maintenanceInterval = 10_000L
        )

        assertEquals(
            FleetStatus.UP_TO_DATE,
            status
        )
    }

    @Test
    fun vehiculo_a_mil_kilometros_del_limite_retorna_MAINTENANCE_DUE_SOON_Amarillo() {
        val status = calculateStatus(
            currentOdometer = 59_500L,
            lastMaintenanceOdometer = 50_000L,
            maintenanceInterval = 10_000L
        )

        assertEquals(
            FleetStatus.MAINTENANCE_DUE_SOON,
            status
        )
    }

    @Test
    fun vehiculo_que_supero_el_limite_retorna_MAINTENANCE_OVERDUE_Rojo() {
        val status = calculateStatus(
            currentOdometer = 61_000L,
            lastMaintenanceOdometer = 50_000L,
            maintenanceInterval = 10_000L
        )

        assertEquals(
            FleetStatus.MAINTENANCE_OVERDUE,
            status
        )
    }

    @Test
    fun vehiculo_sin_mantenimientos_utiliza_cero_como_punto_inicial() {
        val status = calculateStatus(
            currentOdometer = 5_000L,
            lastMaintenanceOdometer = null,
            maintenanceInterval = 10_000L
        )

        assertEquals(
            FleetStatus.UP_TO_DATE,
            status
        )
    }
}