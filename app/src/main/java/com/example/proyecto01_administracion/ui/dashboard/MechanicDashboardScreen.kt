package com.example.proyecto01_administracion.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proyecto01_administracion.ui.login.AuthViewModel
import com.example.proyecto01_administracion.ui.theme.StatusGreen
import com.example.proyecto01_administracion.ui.theme.StatusYellow

@Composable
fun MechanicDashboardScreen(
    viewModel: MechanicDashboardViewModel = hiltViewModel(),
    dashboardViewModel: DashboardViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    onNavigateToMaintenanceHistory: () -> Unit = {},
    onNavigateToVehicleSelection: () -> Unit = {},
    onAvatarClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val dashboardUiState by dashboardViewModel.uiState.collectAsStateWithLifecycle()
    val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        DashboardHeader(
            userName = currentUser?.nombre ?: "Mecánico",
            userRole = "Mecánico",
            hasNotifications = false,
            onAvatarClick = onAvatarClick,
            onNotificationClick = onNotificationClick
        )
        
        if (uiState.isLoading || dashboardUiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState.error != null || dashboardUiState.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = uiState.error ?: dashboardUiState.error ?: "No se pudo cargar el dashboard",
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item {
                    SectionHeader(
                        title = "Resumen de Mantenimiento",
                        icon = Icons.Default.Build
                    )
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        StatCard(
                            modifier = Modifier.weight(1f),
                            label = "Pendientes",
                            value = uiState.pendingCount.toString(),
                            accentColor = StatusYellow,
                            onClick = onNavigateToMaintenanceHistory
                        )
                        StatCard(
                            modifier = Modifier.weight(1f),
                            label = "Realizados",
                            value = uiState.completedCount.toString(),
                            accentColor = StatusGreen,
                            onClick = onNavigateToMaintenanceHistory
                        )
                    }
                }

                item {
                    SectionHeader(
                        title = "Consultar Vehículos",
                        icon = Icons.Default.DirectionsCar
                    )
                    ConsultVehiclesCard(
                        vehicles = dashboardUiState.vehicles,
                        onViewAll = onNavigateToVehicleSelection
                    )
                }

                item {
                    SectionHeader(
                        title = "Acciones Rápidas",
                        icon = Icons.Default.FlashOn
                    )
                    QuickActionsCard(
                        onRegisterMaintenance = onNavigateToVehicleSelection,
                        onViewHistory = onNavigateToMaintenanceHistory
                    )
                }
            }
        }
    }
}
