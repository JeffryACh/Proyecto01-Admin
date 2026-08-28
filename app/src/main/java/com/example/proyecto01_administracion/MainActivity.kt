package com.example.proyecto01_administracion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyecto01_administracion.ui.dashboard.AlertsScreen
import com.example.proyecto01_administracion.ui.dashboard.DashboardScreen
import com.example.proyecto01_administracion.ui.dashboard.FleetManagerDashboardScreen
import com.example.proyecto01_administracion.ui.dashboard.MechanicDashboardScreen
import com.example.proyecto01_administracion.ui.login.LoginScreen
import com.example.proyecto01_administracion.ui.login.PasswordRecoveryScreen
import com.example.proyecto01_administracion.ui.profile.ProfileScreen
import com.example.proyecto01_administracion.ui.profile.EditProfileScreen
import com.example.proyecto01_administracion.ui.mechanic.VehicleSelectionScreen
import com.example.proyecto01_administracion.ui.mechanic.RegisterMaintenanceScreen
import com.example.proyecto01_administracion.ui.mechanic.MaintenanceDetailScreen
import com.example.proyecto01_administracion.ui.fleet.*
import com.example.proyecto01_administracion.ui.vehicle.VehicleScreen
import com.example.proyecto01_administracion.ui.vehicle.MaintenanceHistoryScreen
import com.example.proyecto01_administracion.ui.vehicle.VehicleDocumentsScreen
import com.example.proyecto01_administracion.ui.vehicle.RegisterMileageScreen
import com.example.proyecto01_administracion.ui.vehicle.MileageHistoryScreen
import com.example.proyecto01_administracion.ui.theme.Proyecto01AdministracionTheme
import androidx.navigation.NavType
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Proyecto01AdministracionTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLoginAsDriver = { navController.navigate("driver_dashboard") },
                onLoginAsMechanic = { navController.navigate("mechanic_dashboard") },
                onLoginAsFleetManager = { navController.navigate("fleet_manager_dashboard") },
                onForgotPassword = { navController.navigate("password_recovery") }
            )
        }
        
        composable("password_recovery") {
            PasswordRecoveryScreen(onBack = { navController.popBackStack() })
        }
        
        composable("profile") {
            ProfileScreen(
                onBack = { navController.popBackStack() },
                onNavigateToEditProfile = { navController.navigate("edit_profile") },
                onLogout = {
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
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToVehicle = { navController.navigate("vehicle_details") },
                onNavigateToAlerts = { navController.navigate("alerts") },
                onNavigateToMaintenanceHistory = { navController.navigate("maintenance_history") },
                onNavigateToMileageHistory = { navController.navigate("mileage_history") },
                onNavigateToProfile = { navController.navigate("profile") }
            )
        }

        composable("vehicle_details") {
            VehicleScreen(
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToDashboard = {
                    navController.navigate("driver_dashboard") {
                        popUpTo("driver_dashboard") { inclusive = true }
                    }
                },
                onNavigateToRegisterMileage = { navController.navigate("register_mileage") },
                onNavigateToMileageHistory = { navController.navigate("mileage_history") },
                onNavigateToDocuments = { navController.navigate("vehicle_documents") },
                onNavigateToMaintenanceHistory = { navController.navigate("maintenance_history") },
                onNavigateToProfile = { navController.navigate("profile") }
            )
        }
        
        composable("register_mileage") {
            RegisterMileageScreen(
                onBack = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() }
            )
        }
        
        composable("maintenance_history") {
            MaintenanceHistoryScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable("mileage_history") {
            MileageHistoryScreen(
                onBack = { navController.popBackStack() }
            )
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
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToProfile = { navController.navigate("profile") },
                onNavigateToMaintenanceHistory = { navController.navigate("maintenance_history") },
                onNavigateToVehicleSelection = { navController.navigate("mechanic_vehicle_selection") },
                onNavigateToAlerts = { navController.navigate("alerts") }
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
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToProfile = { navController.navigate("profile") },
                onNavigateToFleet = { navController.navigate("fleet_management") },
                onNavigateToUsers = { navController.navigate("user_management") },
                onNavigateToReports = { navController.navigate("reports") },
                onNavigateToAlerts = { navController.navigate("fleet_alerts") }
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
                onNavigateToMileageHistory = { navController.navigate("mileage_history") }
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
                onNavigateToCreateUser = { /* TODO */ }
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
                onEdit = { /* TODO */ }
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
