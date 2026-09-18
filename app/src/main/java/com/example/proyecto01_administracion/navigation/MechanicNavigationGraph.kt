package com.example.proyecto01_administracion.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.proyecto01_administracion.ui.dashboard.MechanicDashboardScreen
import com.example.proyecto01_administracion.ui.mechanic.VehicleSelectionScreen

fun NavGraphBuilder.mechanicNavigationGraph(
    navController: NavHostController,
    onOpenProfileDrawer: () -> Unit
) {
    composable(AppRoutes.MECHANIC_DASHBOARD) {
        MechanicDashboardScreen(
            onNavigateToMaintenanceHistory = { navController.navigate(AppRoutes.MAINTENANCE_HISTORY) },
            onNavigateToVehicleSelection = { navController.navigate(AppRoutes.MECHANIC_VEHICLE_SELECTION) },
            onAvatarClick = onOpenProfileDrawer,
            onNotificationClick = { navController.navigate(AppRoutes.ALERTS) }
        )
    }

    composable(AppRoutes.MECHANIC_VEHICLE_SELECTION) {
        VehicleSelectionScreen(
            onBack = { navController.popBackStack() },
            onVehicleSelected = { plate ->
                navController.navigate(AppRoutes.registerMaintenance(plate))
            }
        )
    }
}
