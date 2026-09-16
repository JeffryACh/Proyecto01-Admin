package com.example.proyecto01_administracion

import org.junit.Assert.assertEquals
import org.junit.Test

class CalculateFleetStatusUseCaseTest {

    private val calculateStatus = CalculateFleetStatusUseCase()

    @Test
    fun vehiculo_con_margen_seguro_retorna_UP_TO_DATE_Verde() {
        // Último chequeo a los 50,000 km. Toca cada 10,000 km (es decir, a los 60,000).
        // Odómetro actual: 55,000 km (faltan 5,000 km)
        val status = calculateStatus(
            currentOdometer = 55000L,
            lastMaintenanceOdometer = 50000L,
            maintenanceInterval = 10000L
        )
        assertEquals(FleetStatus.UP_TO_DATE, status)
    }

    @Test
    fun vehiculo_a_mil_kilometros_del_limite_retorna_MAINTENANCE_DUE_SOON_Amarillo() {
        // Toca a los 60,000 km. Odómetro actual: 59,500 km (faltan 500 km)
        val status = calculateStatus(
            currentOdometer = 59500L,
            lastMaintenanceOdometer = 50000L,
            maintenanceInterval = 10000L
        )
        assertEquals(FleetStatus.MAINTENANCE_DUE_SOON, status)
    }

    @Test
    fun vehiculo_que_supero_el_limite_retorna_MAINTENANCE_OVERDUE_Rojo() {
        // Toca a los 60,000 km. Odómetro actual: 61,000 km (se pasó por 1,000 km)
        val status = calculateStatus(
            currentOdometer = 61000L,
            lastMaintenanceOdometer = 50000L,
            maintenanceInterval = 10000L
        )
        assertEquals(FleetStatus.MAINTENANCE_OVERDUE, status)
    }
}
