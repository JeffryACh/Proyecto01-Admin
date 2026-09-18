package com.example.proyecto01_administracion.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.proyecto01_administracion.domain.models.UserRole
import com.example.proyecto01_administracion.ui.login.LoginScreen
import com.example.proyecto01_administracion.ui.login.PasswordRecoveryScreen

fun NavGraphBuilder.authNavigationGraph(
    navController: NavHostController,
    onRoleChange: (UserRole) -> Unit
) {
    composable(AppRoutes.LOGIN) {
        LoginScreen(
            onLoginSuccess = { user ->
                val role = when (user.rol_id) {
                    "DRIVER" -> UserRole.DRIVER
                    "MECHANIC" -> UserRole.MECHANIC
                    "FLEET_MANAGER" -> UserRole.FLEET_MANAGER
                    else -> UserRole.NONE
                }
                onRoleChange(role)
                
                val destination = when (role) {
                    UserRole.DRIVER -> AppRoutes.DRIVER_DASHBOARD
                    UserRole.MECHANIC -> AppRoutes.MECHANIC_DASHBOARD
                    UserRole.FLEET_MANAGER -> AppRoutes.FLEET_MANAGER_DASHBOARD
                    else -> AppRoutes.LOGIN
                }
                
                if (destination != AppRoutes.LOGIN) {
                    navController.navigate(destination) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                }
            },
            onForgotPassword = { navController.navigate(AppRoutes.PASSWORD_RECOVERY) }
        )
    }

    composable(AppRoutes.PASSWORD_RECOVERY) {
        PasswordRecoveryScreen(onBack = { navController.popBackStack() })
    }
}
