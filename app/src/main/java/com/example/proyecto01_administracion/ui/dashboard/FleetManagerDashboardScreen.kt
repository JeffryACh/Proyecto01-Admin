package com.example.proyecto01_administracion.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyecto01_administracion.ui.fleet.UpcomingMaintenancesFleetCard
import com.example.proyecto01_administracion.ui.theme.StatusGreen
import com.example.proyecto01_administracion.ui.theme.StatusYellow

@Composable
fun FleetManagerDashboardScreen(
    onNavigateToFleet: () -> Unit = {},
    onNavigateToAlerts: () -> Unit = {},
    onNavigateToMaintenanceHistory: () -> Unit = {},
    onAvatarClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize()) {
        DashboardHeader(
            userName = "Carlos Rodríguez",
            userRole = "Encargado de Flotilla",
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
                    title = "Estado de la Flotilla",
                    icon = Icons.Default.LocalShipping
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        label = "Vehículos al día",
                        value = "12",
                        accentColor = StatusGreen,
                        onClick = onNavigateToFleet
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        label = "Próximos vehículos",
                        value = "4",
                        accentColor = StatusYellow,
                        onClick = onNavigateToFleet
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                AnnouncementsCard(
                    onViewAlerts = onNavigateToAlerts
                )
            }

            item {
                SectionHeader(
                    title = "Próximos Mantenimientos",
                    icon = Icons.Default.Build
                )
                UpcomingMaintenancesFleetCard(
                    onViewAll = onNavigateToMaintenanceHistory
                )
            }
        }
    }
}
