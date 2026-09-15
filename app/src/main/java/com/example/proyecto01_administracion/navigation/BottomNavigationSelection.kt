package com.example.proyecto01_administracion.navigation

import com.example.proyecto01_administracion.UserRole

fun getSelectedItemForRole(route: String?, role: UserRole): Int {
    return when (role) {
        UserRole.DRIVER -> when (route) {
            AppRoutes.DRIVER_DASHBOARD -> 0
            AppRoutes.VEHICLE_DETAILS,
            AppRoutes.REGISTER_MILEAGE,
            AppRoutes.MAINTENANCE_HISTORY,
            AppRoutes.MILEAGE_HISTORY,
            AppRoutes.VEHICLE_DOCUMENTS -> 1
            AppRoutes.ALERTS -> 2
            else -> 0
        }

        UserRole.MECHANIC -> when (route) {
            AppRoutes.MECHANIC_DASHBOARD -> 0
            AppRoutes.MECHANIC_VEHICLE_SELECTION,
            AppRoutes.REGISTER_MAINTENANCE -> 1
            AppRoutes.ALERTS -> 2
            else -> 0
        }

        UserRole.FLEET_MANAGER -> when (route) {
            AppRoutes.FLEET_MANAGER_DASHBOARD -> 0
            AppRoutes.FLEET_MANAGEMENT,
            AppRoutes.FLEET_VEHICLE_DETAIL,
            AppRoutes.VEHICLE_FORM,
            AppRoutes.VEHICLE_FORM_WITH_PLATE,
            AppRoutes.REASSIGN_DRIVER -> 1
            AppRoutes.FLEET_ALERTS -> 2
            else -> 0
        }

        else -> 0
    }
}
