package com.example.proyecto01_administracion.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun DashboardScreen(
    onLogout: () -> Unit,
    onNavigateToVehicle: () -> Unit = {},
    onNavigateToAlerts: () -> Unit = {},
    onNavigateToMaintenanceHistory: () -> Unit = {},
    onNavigateToMileageHistory: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                userName = "Juan Pérez",
                userRole = "Conductor",
                onLogout = {
                    scope.launch { drawerState.close() }
                    onLogout()
                },
                onProfileClick = {
                    scope.launch { drawerState.close() }
                    onNavigateToProfile()
                }
            )
        }
    ) {
        Scaffold(
            bottomBar = {
                BottomNavBar(
                    selectedItem = 0,
                    onVehicleClick = onNavigateToVehicle,
                    onAlertsClick = onNavigateToAlerts
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item {
                    DashboardHeader(
                        userName = "Juan Pérez",
                        userRole = "Conductor",
                        hasNotifications = true,
                        onAvatarClick = { scope.launch { drawerState.open() } }
                    )
                }

                item {
                    SectionHeader(
                        title = "Vehículo",
                        icon = Icons.Default.DirectionsCar
                    )
                    VehicleCard(
                        model = "Toyota Hilux",
                        plate = "ABC-123",
                        mileage = "125,430 km",
                        status = VehicleStatus.ON_TRACK,
                        nextMaintenanceTask = "Cambio de aceite",
                        remainingKm = "4,570 km",
                        progress = 0.85f // Simulating proximity (e.g. 125k/130k)
                    )
                }

                item {
                    SectionHeader(
                        title = "Mantenimiento",
                        icon = Icons.Default.Build
                    )
                    MaintenanceCard(
                        date = "15 mayo 2024",
                        type = "Preventivo",
                        mileage = "120,860 km",
                        onViewHistory = onNavigateToMaintenanceHistory
                    )
                }

                item {
                    SectionHeader(
                        title = "Kilometraje",
                        icon = Icons.AutoMirrored.Filled.ShowChart
                    )
                    MileageCard(
                        date = "22 mayo 2024",
                        mileage = "125,430 km",
                        onViewHistory = onNavigateToMileageHistory
                    )
                }
            }
        }
    }
}
