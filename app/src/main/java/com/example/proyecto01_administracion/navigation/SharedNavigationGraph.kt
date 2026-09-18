package com.example.proyecto01_administracion.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.proyecto01_administracion.domain.model.UserRole
import com.example.proyecto01_administracion.ui.dashboard.AlertsScreen
import com.example.proyecto01_administracion.ui.dashboard.SettingsScreen
import com.example.proyecto01_administracion.ui.profile.EditProfileScreen
import com.example.proyecto01_administracion.ui.profile.ProfileScreen
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

    composable(AppRoutes.VEHICLE_DOCUMENTS) {
        VehicleDocumentsScreen(onBack = { navController.popBackStack() })
    }

    composable(AppRoutes.ALERTS) {
        AlertsScreen(onBack = { navController.popBackStack() })
    }
}
