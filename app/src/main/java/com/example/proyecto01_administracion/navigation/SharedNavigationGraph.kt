package com.example.proyecto01_administracion.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.proyecto01_administracion.domain.models.UserRole
import com.example.proyecto01_administracion.ui.dashboard.AlertsScreen
import com.example.proyecto01_administracion.ui.dashboard.SettingsScreen
import com.example.proyecto01_administracion.ui.mechanic.MaintenanceDetailScreen
import com.example.proyecto01_administracion.ui.mechanic.RegisterMaintenanceScreen
import com.example.proyecto01_administracion.ui.profile.EditProfileScreen
import com.example.proyecto01_administracion.ui.profile.ProfileScreen
import com.example.proyecto01_administracion.ui.vehicle.AddDocumentScreen
import com.example.proyecto01_administracion.ui.vehicle.MaintenanceHistoryScreen
import com.example.proyecto01_administracion.ui.vehicle.MileageHistoryScreen
import com.example.proyecto01_administracion.ui.vehicle.VehicleDocumentsScreen

fun NavGraphBuilder.sharedNavigationGraph(
    navController: NavHostController,
    onRoleChange: (UserRole) -> Unit,
    isDarkTheme: Boolean,
    onThemeToggle: (Boolean) -> Unit
) {
    composable(AppRoutes.SETTINGS) {
        SettingsScreen(
            onBack = { navController.popBackStack() },
            isDarkTheme = isDarkTheme,
            onThemeToggle = onThemeToggle
        )
    }

    composable(AppRoutes.PROFILE) {
        ProfileScreen(
            onBack = { navController.popBackStack() },
            onNavigateToEditProfile = { navController.navigate(AppRoutes.EDIT_PROFILE) },
            onLogout = {
                onRoleChange(UserRole.NONE)
                navController.navigate(AppRoutes.LOGIN) {
                    popUpTo(AppRoutes.LOGIN) { inclusive = true }
                }
            }
        )
    }

    composable(AppRoutes.EDIT_PROFILE) {
        EditProfileScreen(
            onBack = { navController.popBackStack() },
            onSave = { navController.popBackStack() }
        )
    }

    composable(AppRoutes.MAINTENANCE_HISTORY) {
        MaintenanceHistoryScreen(onBack = { navController.popBackStack() })
    }

    composable(AppRoutes.MILEAGE_HISTORY) {
        MileageHistoryScreen(onBack = { navController.popBackStack() })
    }

    composable(
        route = AppRoutes.VEHICLE_DOCUMENTS,
        arguments = listOf(navArgument(AppRoutes.ARG_PLATE) { type = NavType.StringType })
    ) { backStackEntry ->
        val plate = backStackEntry.arguments?.getString(AppRoutes.ARG_PLATE) ?: ""
        VehicleDocumentsScreen(
            vehicleId = plate,
            onBack = { navController.popBackStack() },
            onAddDocument = { navController.navigate(AppRoutes.addDocument(plate)) }
        )
    }

    composable(
        route = AppRoutes.ADD_DOCUMENT,
        arguments = listOf(navArgument(AppRoutes.ARG_PLATE) { type = NavType.StringType })
    ) { backStackEntry ->
        val plate = backStackEntry.arguments?.getString(AppRoutes.ARG_PLATE) ?: ""
        AddDocumentScreen(
            vehicleId = plate,
            onBack = { navController.popBackStack() },
            onSuccess = { navController.popBackStack() }
        )
    }

    composable(AppRoutes.ALERTS) {
        AlertsScreen(onBack = { navController.popBackStack() })
    }

    composable(
        route = AppRoutes.REGISTER_MAINTENANCE,
        arguments = listOf(navArgument(AppRoutes.ARG_PLATE) { type = NavType.StringType })
    ) { backStackEntry ->
        val plate = backStackEntry.arguments?.getString(AppRoutes.ARG_PLATE) ?: ""
        RegisterMaintenanceScreen(
            plate = plate,
            onBack = { navController.popBackStack() },
            onSuccess = {
                navController.popBackStack()
            }
        )
    }

    composable(
        route = AppRoutes.MAINTENANCE_DETAIL,
        arguments = listOf(navArgument(AppRoutes.ARG_MAINTENANCE_ID) { type = NavType.StringType })
    ) { backStackEntry ->
        val maintenanceId = backStackEntry.arguments?.getString(AppRoutes.ARG_MAINTENANCE_ID) ?: ""
        MaintenanceDetailScreen(
            maintenanceId = maintenanceId,
            onBack = { navController.popBackStack() }
        )
    }
}
