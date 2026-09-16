package com.example.proyecto01_administracion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.proyecto01_administracion.navigation.AppNavigation
import com.example.proyecto01_administracion.navigation.AppRoutes
import com.example.proyecto01_administracion.navigation.getSelectedItemForRole
import com.example.proyecto01_administracion.ui.dashboard.AppDrawer
import com.example.proyecto01_administracion.ui.dashboard.BottomNavBar
import com.example.proyecto01_administracion.ui.dashboard.FleetBottomNavBar
import com.example.proyecto01_administracion.ui.dashboard.MechanicBottomNavBar
import com.example.proyecto01_administracion.ui.theme.Proyecto01AdministracionTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainApp()
        }
    }
}

@Composable
fun MainApp() {
    var isDarkTheme by rememberSaveable { mutableStateOf(true) }

    Proyecto01AdministracionTheme(darkTheme = isDarkTheme) {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        var userRole by rememberSaveable { mutableStateOf(UserRole.NONE) }
        val profileDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val moreDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        // Manejo del botón Atrás para cerrar los menús laterales
        BackHandler(enabled = profileDrawerState.isOpen || moreDrawerState.isOpen) {
            scope.launch {
                profileDrawerState.close()
                moreDrawerState.close()
            }
        }

        val isAuthScreen = currentRoute == AppRoutes.LOGIN ||
            currentRoute == AppRoutes.PASSWORD_RECOVERY ||
            (currentRoute == null && userRole == UserRole.NONE)

        if (isAuthScreen) {
            AppNavigation(
                navController = navController,
                onRoleChange = { userRole = it },
                isDarkTheme = isDarkTheme,
                onThemeToggle = { isDarkTheme = it }
            )
        } else {
            ModalNavigationDrawer(
                drawerState = profileDrawerState,
                drawerContent = {
                    AppDrawer(
                        userName = when (userRole) {
                            UserRole.FLEET_MANAGER -> "Carlos Rodríguez"
                            else -> "Juan Pérez"
                        },
                        userRole = when (userRole) {
                            UserRole.DRIVER -> "Conductor"
                            UserRole.MECHANIC -> "Mecánico"
                            UserRole.FLEET_MANAGER -> "Encargado de Flotilla"
                            else -> ""
                        },
                        onLogout = {
                            scope.launch {
                                profileDrawerState.close()
                                moreDrawerState.close()
                                userRole = UserRole.NONE
                                navController.navigate(AppRoutes.LOGIN) {
                                    popUpTo(AppRoutes.LOGIN) { inclusive = true }
                                }
                            }
                        },
                        onProfileClick = {
                            scope.launch { profileDrawerState.close() }
                            navController.navigate(AppRoutes.PROFILE)
                        },
                        onSettingsClick = {
                            scope.launch { profileDrawerState.close() }
                            navController.navigate(AppRoutes.SETTINGS)
                        },
                        onEditProfileClick = {
                            scope.launch { profileDrawerState.close() }
                            navController.navigate(AppRoutes.EDIT_PROFILE)
                        }
                    )
                }
            ) {
                // El menú "Menú Rápido" debe abrirse desde la derecha
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    ModalNavigationDrawer(
                        drawerState = moreDrawerState,
                        gesturesEnabled = moreDrawerState.isOpen,
                        drawerContent = {
                            // El contenido del menú "Más" debe volver a Ltr para el texto
                            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                                ModalDrawerSheet(
                                    modifier = Modifier.fillMaxWidth(0.75f),
                                    drawerContainerColor = MaterialTheme.colorScheme.background
                                ) {
                                    MoreOptionsMenu(
                                        role = userRole,
                                        onOptionClick = { route ->
                                            scope.launch {
                                                moreDrawerState.close()
                                                if (route.isNotEmpty()) {
                                                    navController.navigate(route)
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    ) {
                        // El contenido principal vuelve a Ltr
                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                            Scaffold(
                                containerColor = MaterialTheme.colorScheme.background,
                                contentWindowInsets = WindowInsets(0.dp),
                                bottomBar = {
                                    when (userRole) {
                                        UserRole.DRIVER -> BottomNavBar(
                                            selectedItem = getSelectedItemForRole(currentRoute, userRole),
                                            onHomeClick = { navController.navigate(AppRoutes.DRIVER_DASHBOARD) },
                                            onVehicleClick = { navController.navigate(AppRoutes.VEHICLE_DETAILS) },
                                            onAlertsClick = { navController.navigate(AppRoutes.ALERTS) },
                                            onMoreClick = { scope.launch { moreDrawerState.open() } }
                                        )

                                        UserRole.MECHANIC -> MechanicBottomNavBar(
                                            selectedItem = getSelectedItemForRole(currentRoute, userRole),
                                            onHomeClick = { navController.navigate(AppRoutes.MECHANIC_DASHBOARD) },
                                            onMaintenanceClick = { navController.navigate(AppRoutes.MECHANIC_VEHICLE_SELECTION) },
                                            onAlertsClick = { navController.navigate(AppRoutes.ALERTS) },
                                            onMoreClick = { scope.launch { moreDrawerState.open() } }
                                        )

                                        UserRole.FLEET_MANAGER -> FleetBottomNavBar(
                                            selectedItem = getSelectedItemForRole(currentRoute, userRole),
                                            onHomeClick = { navController.navigate(AppRoutes.FLEET_MANAGER_DASHBOARD) },
                                            onFleetClick = { navController.navigate(AppRoutes.FLEET_MANAGEMENT) },
                                            onAlertsClick = { navController.navigate(AppRoutes.FLEET_ALERTS) },
                                            onMoreClick = { scope.launch { moreDrawerState.open() } }
                                        )

                                        else -> {}
                                    }
                                }
                            ) { innerPadding ->
                                AppNavigation(
                                    navController = navController,
                                    modifier = Modifier.padding(innerPadding),
                                    onRoleChange = { userRole = it },
                                    onOpenProfileDrawer = { scope.launch { profileDrawerState.open() } },
                                    isDarkTheme = isDarkTheme,
                                    onThemeToggle = { isDarkTheme = it }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MoreOptionsMenu(role: UserRole, onOptionClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .statusBarsPadding()
    ) {
        Text(
            text = "Menú Rápido",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(24.dp))

        when (role) {
            UserRole.DRIVER -> {
                MoreMenuButton(text = "Mi Perfil", onClick = { onOptionClick(AppRoutes.PROFILE) })
                MoreMenuButton(text = "Alertas", onClick = { onOptionClick(AppRoutes.ALERTS) })
                MoreMenuButton(text = "Mi Vehículo", onClick = { onOptionClick(AppRoutes.VEHICLE_DETAILS) })
                MoreMenuButton(text = "Historial de Mantenimiento", onClick = { onOptionClick(AppRoutes.MAINTENANCE_HISTORY) })
                MoreMenuButton(text = "Historial de Kilometraje", onClick = { onOptionClick(AppRoutes.MILEAGE_HISTORY) })
                MoreMenuButton(text = "Documentos", onClick = { onOptionClick(AppRoutes.VEHICLE_DOCUMENTS) })
            }

            UserRole.MECHANIC -> {
                MoreMenuButton(text = "Mi Perfil", onClick = { onOptionClick(AppRoutes.PROFILE) })
                MoreMenuButton(text = "Alertas", onClick = { onOptionClick(AppRoutes.ALERTS) })
                MoreMenuButton(text = "Seleccionar Vehículo", onClick = { onOptionClick(AppRoutes.MECHANIC_VEHICLE_SELECTION) })
            }

            UserRole.FLEET_MANAGER -> {
                MoreMenuButton(text = "Mi Perfil", onClick = { onOptionClick(AppRoutes.PROFILE) })
                MoreMenuButton(text = "Alertas", onClick = { onOptionClick(AppRoutes.FLEET_ALERTS) })
                MoreMenuButton(text = "Gestión de Flota", onClick = { onOptionClick(AppRoutes.FLEET_MANAGEMENT) })
                MoreMenuButton(text = "Registrar Vehículo", onClick = { onOptionClick(AppRoutes.VEHICLE_FORM) })
                MoreMenuButton(text = "Reportes y Estadísticas", onClick = { onOptionClick(AppRoutes.REPORTS) })
                MoreMenuButton(text = "Gestión de Usuarios", onClick = { onOptionClick(AppRoutes.USER_MANAGEMENT) })
            }

            else -> {}
        }
    }
}

@Composable
fun MoreMenuButton(text: String, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 8.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
