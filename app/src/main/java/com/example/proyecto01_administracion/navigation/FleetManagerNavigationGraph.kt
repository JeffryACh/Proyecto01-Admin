package com.example.proyecto01_administracion.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.proyecto01_administracion.ui.dashboard.FleetManagerDashboardScreen
import com.example.proyecto01_administracion.ui.fleet.*

fun NavGraphBuilder.fleetManagerNavigationGraph(
    navController: NavHostController,
    onOpenProfileDrawer: () -> Unit
) {
    composable(AppRoutes.FLEET_MANAGER_DASHBOARD) {
        FleetManagerDashboardScreen(
            onNavigateToFleet = { navController.navigate(AppRoutes.FLEET_MANAGEMENT) },
            onNavigateToAlerts = { navController.navigate(AppRoutes.FLEET_ALERTS) },
            onNavigateToMaintenanceHistory = { navController.navigate(AppRoutes.MAINTENANCE_HISTORY) },
            onAvatarClick = onOpenProfileDrawer,
            onNotificationClick = { navController.navigate(AppRoutes.FLEET_ALERTS) }
        )
    }

    composable(AppRoutes.FLEET_MANAGEMENT) {
        FleetManagementScreen(
            onBack = { navController.popBackStack() },
            onNavigateToVehicleDetail = { plate ->
                navController.navigate(AppRoutes.fleetVehicleDetail(plate))
            },
            onNavigateToCreateVehicle = { navController.navigate(AppRoutes.VEHICLE_FORM) }
        )
    }

    composable(
        route = AppRoutes.FLEET_VEHICLE_DETAIL,
        arguments = listOf(navArgument(AppRoutes.ARG_PLATE) { type = NavType.StringType })
    ) { backStackEntry ->
        val plate = backStackEntry.arguments?.getString(AppRoutes.ARG_PLATE) ?: ""
        FleetVehicleDetailScreen(
            plate = plate,
            onBack = { navController.popBackStack() },
            onEdit = { navController.navigate(AppRoutes.vehicleForm(plate)) },
            onRegisterMaintenance = { navController.navigate(AppRoutes.registerMaintenance(it)) },
            onNavigateToMaintenanceHistory = { navController.navigate(AppRoutes.MAINTENANCE_HISTORY) },
            onNavigateToMileageHistory = { navController.navigate(AppRoutes.MILEAGE_HISTORY) },
            onReassignDriver = { navController.navigate(AppRoutes.reassignDriver(plate)) },
            onNavigateToDocuments = { navController.navigate(AppRoutes.vehicleDocuments(plate)) }
        )
    }

    composable(
        route = AppRoutes.REASSIGN_DRIVER,
        arguments = listOf(navArgument(AppRoutes.ARG_PLATE) { type = NavType.StringType })
    ) { backStackEntry ->
        val plate = backStackEntry.arguments?.getString(AppRoutes.ARG_PLATE) ?: ""
        ReassignDriverScreen(
            plate = plate,
            onBack = { navController.popBackStack() },
            onSuccess = { navController.popBackStack() }
        )
    }

    composable(
        route = AppRoutes.VEHICLE_FORM_WITH_PLATE,
        arguments = listOf(
            navArgument(AppRoutes.ARG_PLATE) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    ) { backStackEntry ->
        val plate = backStackEntry.arguments?.getString(AppRoutes.ARG_PLATE)
        VehicleFormScreen(
            plate = plate,
            onBack = { navController.popBackStack() },
            onSuccess = { navController.popBackStack() }
        )
    }

    composable(AppRoutes.USER_MANAGEMENT) {
        UserManagementScreen(
            onBack = { navController.popBackStack() },
            onNavigateToUserDetail = { userId ->
                navController.navigate(AppRoutes.userDetail(userId))
            },
            onNavigateToCreateUser = { navController.navigate(AppRoutes.CREATE_USER) }
        )
    }

    composable(AppRoutes.CREATE_USER) {
        UserFormScreen(
            onBack = { navController.popBackStack() },
            onSuccess = { navController.popBackStack() }
        )
    }

    composable(
        route = AppRoutes.USER_DETAIL,
        arguments = listOf(navArgument(AppRoutes.ARG_USER_ID) { type = NavType.StringType })
    ) { backStackEntry ->
        val userId = backStackEntry.arguments?.getString(AppRoutes.ARG_USER_ID) ?: ""
        UserDetailScreen(
            userId = userId,
            onBack = { navController.popBackStack() },
            onEdit = { navController.navigate(AppRoutes.editUser(userId)) },
            onReassignVehicle = { navController.navigate(AppRoutes.reassignVehicle(userId)) }
        )
    }

    composable(
        route = AppRoutes.EDIT_USER,
        arguments = listOf(navArgument(AppRoutes.ARG_USER_ID) { type = NavType.StringType })
    ) { backStackEntry ->
        val userId = backStackEntry.arguments?.getString(AppRoutes.ARG_USER_ID) ?: ""
        UserFormScreen(
            userId = userId,
            onBack = { navController.popBackStack() },
            onSuccess = { navController.popBackStack() }
        )
    }

    composable(
        route = AppRoutes.REASSIGN_VEHICLE,
        arguments = listOf(navArgument(AppRoutes.ARG_USER_ID) { type = NavType.StringType })
    ) { backStackEntry ->
        val userId = backStackEntry.arguments?.getString(AppRoutes.ARG_USER_ID) ?: ""
        ReassignVehicleScreen(
            userId = userId,
            onBack = { navController.popBackStack() },
            onSuccess = { navController.popBackStack() }
        )
    }

    composable(AppRoutes.REPORTS) {
        ReportsScreen(onBack = { navController.popBackStack() })
    }

    composable(AppRoutes.FLEET_ALERTS) {
        FleetAlertsScreen(onBack = { navController.popBackStack() })
    }
}
