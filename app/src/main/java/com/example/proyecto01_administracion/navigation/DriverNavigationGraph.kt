package com.example.proyecto01_administracion.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.proyecto01_administracion.ui.dashboard.DashboardScreen
import com.example.proyecto01_administracion.ui.vehicle.RegisterMileageScreen
import com.example.proyecto01_administracion.ui.vehicle.VehicleScreen

fun NavGraphBuilder.driverNavigationGraph(
    navController: NavHostController,
    onOpenProfileDrawer: () -> Unit
) {
    composable(AppRoutes.DRIVER_DASHBOARD) {
        DashboardScreen(
            onNavigateToMaintenanceHistory = { navController.navigate(AppRoutes.MAINTENANCE_HISTORY) },
            onNavigateToMileageHistory = { navController.navigate(AppRoutes.MILEAGE_HISTORY) },
            onAvatarClick = onOpenProfileDrawer,
            onNotificationClick = { navController.navigate(AppRoutes.ALERTS) }
        )
    }

    composable(AppRoutes.VEHICLE_DETAILS) {
        VehicleScreen(
            onBack = { navController.popBackStack() },
            onNavigateToRegisterMileage = { navController.navigate(AppRoutes.REGISTER_MILEAGE) },
            onNavigateToMileageHistory = { navController.navigate(AppRoutes.MILEAGE_HISTORY) },
            onNavigateToDocuments = { plate -> navController.navigate(AppRoutes.vehicleDocuments(plate)) },
            onNavigateToMaintenanceHistory = { navController.navigate(AppRoutes.MAINTENANCE_HISTORY) }
        )
    }

    composable(AppRoutes.REGISTER_MILEAGE) {
        RegisterMileageScreen(
            onBack = { navController.popBackStack() },
            onSuccess = { navController.popBackStack() }
        )
    }
}
