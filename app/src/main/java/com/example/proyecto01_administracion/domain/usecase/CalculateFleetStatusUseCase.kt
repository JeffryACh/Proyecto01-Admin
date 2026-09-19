package com.example.proyecto01_administracion.domain.usecase

import com.example.proyecto01_administracion.domain.models.FleetStatus
import javax.inject.Inject

class CalculateFleetStatusUseCase @Inject constructor() {

    operator fun invoke(
        currentOdometer: Long,
        lastMaintenanceOdometer: Long?,
        maintenanceInterval: Long
    ): FleetStatus {

        require(currentOdometer >= 0) {
            "El kilometraje actual no puede ser negativo"
        }

        require(
            lastMaintenanceOdometer == null ||
                    lastMaintenanceOdometer >= 0
        ) {
            "El kilometraje del último mantenimiento no puede ser negativo"
        }

        require(maintenanceInterval > 0) {
            "El intervalo de mantenimiento debe ser mayor que cero"
        }

        /*
         * Si el vehículo no tiene mantenimientos registrados,
         * se utiliza 0 km como punto inicial.
         */
        val maintenanceBaseline =
            lastMaintenanceOdometer ?: 0L

        require(currentOdometer >= maintenanceBaseline) {
            "El kilometraje actual no puede ser menor al del último mantenimiento"
        }

        val distanceTraveled =
            currentOdometer - maintenanceBaseline

        val distanceRemaining =
            maintenanceInterval - distanceTraveled

        return when {
            distanceRemaining < 0 ->
                FleetStatus.MAINTENANCE_OVERDUE

            distanceRemaining <= DUE_SOON_THRESHOLD_KM ->
                FleetStatus.MAINTENANCE_DUE_SOON

            else ->
                FleetStatus.UP_TO_DATE
        }
    }

    companion object {
        const val DUE_SOON_THRESHOLD_KM = 1_000L
    }
}