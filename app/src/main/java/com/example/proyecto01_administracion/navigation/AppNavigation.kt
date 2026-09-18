package com.example.proyecto01_administracion.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.proyecto01_administracion.domain.model.UserRole

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onRoleChange: (UserRole) -> Unit,
    onOpenProfileDrawer: () -> Unit = {},
    isDarkTheme: Boolean = true,
    onThemeToggle: (Boolean) -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.LOGIN,
        modifier = modifier
    ) {
        authNavigationGraph(
            navController = navController,
            onRoleChange = onRoleChange
        )
        sharedNavigationGraph(
            navController = navController,
            onRoleChange = onRoleChange,
            isDarkTheme = isDarkTheme,
            onThemeToggle = onThemeToggle
        )
        driverNavigationGraph(
            navController = navController,
            onOpenProfileDrawer = onOpenProfileDrawer
        )
        mechanicNavigationGraph(
            navController = navController,
            onOpenProfileDrawer = onOpenProfileDrawer
        )
        fleetManagerNavigationGraph(
            navController = navController,
            onOpenProfileDrawer = onOpenProfileDrawer
        )
    }
}
