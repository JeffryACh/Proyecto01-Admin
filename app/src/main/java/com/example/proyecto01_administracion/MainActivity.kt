package com.example.proyecto01_administracion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.proyecto01_administracion.ui.dashboard.*
import com.example.proyecto01_administracion.ui.login.LoginScreen
import com.example.proyecto01_administracion.ui.login.PasswordRecoveryScreen
import com.example.proyecto01_administracion.ui.profile.ProfileScreen
import com.example.proyecto01_administracion.ui.profile.EditProfileScreen
import com.example.proyecto01_administracion.ui.mechanic.VehicleSelectionScreen
import com.example.proyecto01_administracion.ui.mechanic.RegisterMaintenanceScreen
import com.example.proyecto01_administracion.ui.mechanic.MaintenanceDetailScreen
import com.example.proyecto01_administracion.ui.fleet.*
import com.example.proyecto01_administracion.ui.vehicle.*
import com.example.proyecto01_administracion.ui.theme.Proyecto01AdministracionTheme
import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlinx.coroutines.launch

enum class UserRole { DRIVER, MECHANIC, FLEET_MANAGER, NONE }

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
    var isDarkTheme by remember { mutableStateOf(true) }
    
    Proyecto01AdministracionTheme(darkTheme = isDarkTheme) {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        
        var userRole by remember { mutableStateOf(UserRole.NONE) }
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

        val isAuthScreen = currentRoute == "login" || currentRoute == "password_recovery" || currentRoute == null

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
                        userName = when(userRole) {
                            UserRole.FLEET_MANAGER -> "Carlos Rodríguez"
                            else -> "Juan Pérez"
                        },
                        userRole = when(userRole) {
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
                                navController.navigate("login") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        },
                        onProfileClick = {
                            scope.launch { profileDrawerState.close() }
                            navController.navigate("profile")
                        },
                        onSettingsClick = {
                            scope.launch { profileDrawerState.close() }
                            navController.navigate("settings")
                        }
                    )
                }
            ) {
                // El menú "Más" debe abrirse desde la derecha
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
                                            onHomeClick = { navController.navigate("driver_dashboard") },
                                            onVehicleClick = { navController.navigate("vehicle_details") },
                                            onAlertsClick = { navController.navigate("alerts") },
                                            onMoreClick = { scope.launch { moreDrawerState.open() } }
                                        )
                                        UserRole.MECHANIC -> MechanicBottomNavBar(
                                            selectedItem = getSelectedItemForRole(currentRoute, userRole),
                                            onHomeClick = { navController.navigate("mechanic_dashboard") },
                                            onMaintenanceClick = { navController.navigate("mechanic_vehicle_selection") },
                                            onAlertsClick = { navController.navigate("alerts") },
                                            onMoreClick = { scope.launch { moreDrawerState.open() } }
                                        )
                                        UserRole.FLEET_MANAGER -> FleetBottomNavBar(
                                            selectedItem = getSelectedItemForRole(currentRoute, userRole),
                                            onHomeClick = { navController.navigate("fleet_manager_dashboard") },
                                            onFleetClick = { navController.navigate("fleet_management") },
                                            onAlertsClick = { navController.navigate("fleet_alerts") },
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
            text = "Opciones",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        when(role) {
            UserRole.DRIVER -> {
                MoreMenuButton(text = "Mi Perfil", onClick = { onOptionClick("profile") })
                MoreMenuButton(text = "Alertas", onClick = { onOptionClick("alerts") })
                MoreMenuButton(text = "Mi Vehículo", onClick = { onOptionClick("vehicle_details") })
                MoreMenuButton(text = "Historial de Mantenimiento", onClick = { onOptionClick("maintenance_history") })
                MoreMenuButton(text = "Historial de Kilometraje", onClick = { onOptionClick("mileage_history") })
                MoreMenuButton(text = "Documentos", onClick = { onOptionClick("vehicle_documents") })
            }
            UserRole.MECHANIC -> {
                MoreMenuButton(text = "Mi Perfil", onClick = { onOptionClick("profile") })
                MoreMenuButton(text = "Alertas", onClick = { onOptionClick("alerts") })
                MoreMenuButton(text = "Seleccionar Vehículo", onClick = { onOptionClick("mechanic_vehicle_selection") })
            }
            UserRole.FLEET_MANAGER -> {
                MoreMenuButton(text = "Mi Perfil", onClick = { onOptionClick("profile") })
                MoreMenuButton(text = "Alertas", onClick = { onOptionClick("fleet_alerts") })
                MoreMenuButton(text = "Gestión de Flota", onClick = { onOptionClick("fleet_management") })
                MoreMenuButton(text = "Registrar Vehículo", onClick = { onOptionClick("vehicle_form") })
                MoreMenuButton(text = "Reportes y Estadísticas", onClick = { onOptionClick("reports") })
                MoreMenuButton(text = "Gestión de Usuarios", onClick = { onOptionClick("user_management") })
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

private fun getSelectedItemForRole(route: String?, role: UserRole): Int {
    return when (role) {
        UserRole.DRIVER -> when (route) {
            "driver_dashboard" -> 0
            "vehicle_details", "register_mileage", "maintenance_history", "mileage_history", "vehicle_documents" -> 1
            "alerts" -> 2
            else -> 0
        }
        UserRole.MECHANIC -> when (route) {
            "mechanic_dashboard" -> 0
            "mechanic_vehicle_selection", "register_maintenance/{plate}" -> 1
            "alerts" -> 2
            else -> 0
        }
        UserRole.FLEET_MANAGER -> when (route) {
            "fleet_manager_dashboard" -> 0
            "fleet_management", "fleet_vehicle_detail/{plate}", "vehicle_form", "vehicle_form?plate={plate}", "reassign_driver/{plate}" -> 1
            "fleet_alerts" -> 2
            else -> 0
        }
        else -> 0
    }
}

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
        startDestination = "login",
        modifier = modifier
    ) {
        composable("login") {
            LoginScreen(
                onLoginAsDriver = { 
                    onRoleChange(UserRole.DRIVER)
                    navController.navigate("driver_dashboard") 
                },
                onLoginAsMechanic = { 
                    onRoleChange(UserRole.MECHANIC)
                    navController.navigate("mechanic_dashboard") 
                },
                onLoginAsFleetManager = { 
                    onRoleChange(UserRole.FLEET_MANAGER)
                    navController.navigate("fleet_manager_dashboard") 
                },
                onForgotPassword = { navController.navigate("password_recovery") }
            )
        }
        
        composable("password_recovery") {
            PasswordRecoveryScreen(onBack = { navController.popBackStack() })
        }
        
        composable("settings") {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle
            )
        }

        composable("profile") {
            ProfileScreen(
                onBack = { navController.popBackStack() },
                onNavigateToEditProfile = { navController.navigate("edit_profile") },
                onLogout = {
                    onRoleChange(UserRole.NONE)
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        
        composable("edit_profile") {
            EditProfileScreen(
                onBack = { navController.popBackStack() },
                onSave = { navController.popBackStack() }
            )
        }
        
        composable("driver_dashboard") {
            DashboardScreen(
                onNavigateToMaintenanceHistory = { navController.navigate("maintenance_history") },
                onNavigateToMileageHistory = { navController.navigate("mileage_history") },
                onAvatarClick = onOpenProfileDrawer,
                onNotificationClick = { navController.navigate("alerts") }
            )
        }

        composable("vehicle_details") {
            VehicleScreen(
                onBack = { navController.popBackStack() },
                onNavigateToRegisterMileage = { navController.navigate("register_mileage") },
                onNavigateToMileageHistory = { navController.navigate("mileage_history") },
                onNavigateToDocuments = { navController.navigate("vehicle_documents") },
                onNavigateToMaintenanceHistory = { navController.navigate("maintenance_history") }
            )
        }
        
        composable("register_mileage") {
            RegisterMileageScreen(
                onBack = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() }
            )
        }
        
        composable("maintenance_history") {
            MaintenanceHistoryScreen(onBack = { navController.popBackStack() })
        }

        composable("mileage_history") {
            MileageHistoryScreen(onBack = { navController.popBackStack() })
        }
        
        composable("vehicle_documents") {
            VehicleDocumentsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable("alerts") {
            AlertsScreen(
                onBack = { navController.popBackStack() }
            )
        }
        
        composable("mechanic_dashboard") {
            MechanicDashboardScreen(
                onNavigateToMaintenanceHistory = { navController.navigate("maintenance_history") },
                onNavigateToVehicleSelection = { navController.navigate("mechanic_vehicle_selection") },
                onAvatarClick = onOpenProfileDrawer,
                onNotificationClick = { navController.navigate("alerts") }
            )
        }
        
        composable("mechanic_vehicle_selection") {
            VehicleSelectionScreen(
                onBack = { navController.popBackStack() },
                onVehicleSelected = { plate ->
                    navController.navigate("register_maintenance/$plate")
                }
            )
        }
        
        composable(
            route = "register_maintenance/{plate}",
            arguments = listOf(navArgument("plate") { type = NavType.StringType })
        ) { backStackEntry ->
            val plate = backStackEntry.arguments?.getString("plate") ?: ""
            RegisterMaintenanceScreen(
                plate = plate,
                onBack = { navController.popBackStack() },
                onSuccess = { 
                    navController.popBackStack("mechanic_dashboard", false)
                }
            )
        }
        
        composable("maintenance_detail") {
            MaintenanceDetailScreen(onBack = { navController.popBackStack() })
        }
        
        composable("fleet_manager_dashboard") {
            FleetManagerDashboardScreen(
                onNavigateToFleet = { navController.navigate("fleet_management") },
                onNavigateToAlerts = { navController.navigate("fleet_alerts") },
                onNavigateToMaintenanceHistory = { navController.navigate("maintenance_history") },
                onAvatarClick = onOpenProfileDrawer,
                onNotificationClick = { navController.navigate("fleet_alerts") }
            )
        }
        
        composable("fleet_management") {
            FleetManagementScreen(
                onBack = { navController.popBackStack() },
                onNavigateToVehicleDetail = { plate ->
                    navController.navigate("fleet_vehicle_detail/$plate")
                },
                onNavigateToCreateVehicle = { navController.navigate("vehicle_form") }
            )
        }
        
        composable(
            route = "fleet_vehicle_detail/{plate}",
            arguments = listOf(navArgument("plate") { type = NavType.StringType })
        ) { backStackEntry ->
            val plate = backStackEntry.arguments?.getString("plate") ?: ""
            FleetVehicleDetailScreen(
                plate = plate,
                onBack = { navController.popBackStack() },
                onEdit = { navController.navigate("vehicle_form?plate=$plate") },
                onNavigateToMaintenanceHistory = { navController.navigate("maintenance_history") },
                onNavigateToMileageHistory = { navController.navigate("mileage_history") },
                onReassignDriver = { navController.navigate("reassign_driver/$plate") },
                onNavigateToDocuments = { navController.navigate("vehicle_documents") }
            )
        }

        composable(
            route = "reassign_driver/{plate}",
            arguments = listOf(navArgument("plate") { type = NavType.StringType })
        ) { backStackEntry ->
            val plate = backStackEntry.arguments?.getString("plate") ?: ""
            ReassignDriverScreen(
                plate = plate,
                onBack = { navController.popBackStack() },
                onConfirm = { navController.popBackStack() }
            )
        }
        
        composable(
            route = "vehicle_form?plate={plate}",
            arguments = listOf(navArgument("plate") { 
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val plate = backStackEntry.arguments?.getString("plate")
            VehicleFormScreen(
                plate = plate,
                onBack = { navController.popBackStack() },
                onSave = { navController.popBackStack() }
            )
        }
        
        composable("user_management") {
            UserManagementScreen(
                onBack = { navController.popBackStack() },
                onNavigateToUserDetail = { userId ->
                    navController.navigate("user_detail/$userId")
                },
                onNavigateToCreateUser = { navController.navigate("create_user") }
            )
        }

        composable("create_user") {
            UserFormScreen(
                onBack = { navController.popBackStack() },
                onSave = { navController.popBackStack() }
            )
        }
        
        composable(
            route = "user_detail/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            UserDetailScreen(
                userId = userId,
                onBack = { navController.popBackStack() },
                onEdit = { navController.navigate("edit_user/$userId") },
                onReassignVehicle = { navController.navigate("reassign_vehicle/$userId") }
            )
        }

        composable(
            route = "edit_user/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            UserFormScreen(
                userId = userId,
                onBack = { navController.popBackStack() },
                onSave = { navController.popBackStack() }
            )
        }

        composable(
            route = "reassign_vehicle/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            ReassignVehicleScreen(
                userId = userId,
                onBack = { navController.popBackStack() },
                onConfirm = { navController.popBackStack() }
            )
        }
        
        composable("reports") {
            ReportsScreen(onBack = { navController.popBackStack() })
        }
        
        composable("fleet_alerts") {
            FleetAlertsScreen(onBack = { navController.popBackStack() })
        }
    }
}
