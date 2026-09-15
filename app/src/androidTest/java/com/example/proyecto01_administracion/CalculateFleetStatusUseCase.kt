package com.example.proyecto01_administracion

class CalculateFleetStatusUseCase {
    operator fun invoke(
        currentOdometer: Long,
        lastMaintenanceOdometer: Long,
        maintenanceInterval: Long
    ): FleetStatus {
        val nextMaintenanceAt = lastMaintenanceOdometer + maintenanceInterval
        val distanceRemaining = nextMaintenanceAt - currentOdometer

        return when {
            distanceRemaining < 0 -> FleetStatus.MAINTENANCE_OVERDUE
            distanceRemaining <= 1000 -> FleetStatus.MAINTENANCE_DUE_SOON
            else -> FleetStatus.UP_TO_DATE
        }
    }
}