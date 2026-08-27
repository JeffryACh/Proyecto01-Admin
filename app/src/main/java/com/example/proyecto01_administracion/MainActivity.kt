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
import com.example.proyecto01_administracion.ui.vehicle.VehicleScreen
import com.example.proyecto01_administracion.ui.vehicle.MaintenanceHistoryScreen
import com.example.proyecto01_administracion.ui.vehicle.VehicleDocumentsScreen
import com.example.proyecto01_administracion.ui.vehicle.RegisterMileageScreen
import com.example.proyecto01_administracion.ui.vehicle.MileageHistoryScreen
import com.example.proyecto01_administracion.ui.theme.Proyecto01AdministracionTheme

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
                onLoginAsFleetManager = { navController.navigate("fleet_manager_dashboard") }
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
                onNavigateToMileageHistory = { navController.navigate("mileage_history") }
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
                }
            )
        }
        
        composable("fleet_manager_dashboard") {
            FleetManagerDashboardScreen(
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
    }
}
