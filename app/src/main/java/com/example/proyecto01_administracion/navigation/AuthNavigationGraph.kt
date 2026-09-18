package com.example.proyecto01_administracion.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.proyecto01_administracion.domain.model.UserRole
import com.example.proyecto01_administracion.ui.login.LoginScreen
import com.example.proyecto01_administracion.ui.login.PasswordRecoveryScreen

fun NavGraphBuilder.authNavigationGraph(
    navController: NavHostController,
    onRoleChange: (UserRole) -> Unit
) {
    composable(AppRoutes.LOGIN) {
        LoginScreen(
            onLoginAsDriver = {
                onRoleChange(UserRole.DRIVER)
                navController.navigate(AppRoutes.DRIVER_DASHBOARD)
            },
            onLoginAsMechanic = {
                onRoleChange(UserRole.MECHANIC)
                navController.navigate(AppRoutes.MECHANIC_DASHBOARD)
            },
            onLoginAsFleetManager = {
                onRoleChange(UserRole.FLEET_MANAGER)
                navController.navigate(AppRoutes.FLEET_MANAGER_DASHBOARD)
            },
            onForgotPassword = { navController.navigate(AppRoutes.PASSWORD_RECOVERY) }
        )
    }

    composable(AppRoutes.PASSWORD_RECOVERY) {
        PasswordRecoveryScreen(onBack = { navController.popBackStack() })
    }
}
