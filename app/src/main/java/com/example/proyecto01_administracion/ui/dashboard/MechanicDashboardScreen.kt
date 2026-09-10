package com.example.proyecto01_administracion.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyecto01_administracion.ui.theme.StatusGreen
import com.example.proyecto01_administracion.ui.theme.StatusYellow

@Composable
fun MechanicDashboardScreen(
    onNavigateToMaintenanceHistory: () -> Unit = {},
    onNavigateToVehicleSelection: () -> Unit = {},
    onAvatarClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize()) {
        DashboardHeader(
            userName = "Juan Pérez",
            userRole = "Mecánico",
            hasNotifications = true,
            onAvatarClick = onAvatarClick,
            onNotificationClick = onNotificationClick
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                SectionHeader(
                    title = "Resumen de mantenimiento",
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
                        value = "3",
                        accentColor = StatusYellow,
                        onClick = onNavigateToMaintenanceHistory
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        label = "Realizados",
                        value = "12",
                        accentColor = StatusGreen,
                        onClick = onNavigateToMaintenanceHistory
                    )
                }
            }

            item {
                SectionHeader(
                    title = "Consultar vehículos",
                    icon = Icons.Default.DirectionsCar
                )
                ConsultVehiclesCard(
                    onViewAll = onNavigateToVehicleSelection
                )
            }

            item {
                SectionHeader(
                    title = "Acciones rápidas",
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
