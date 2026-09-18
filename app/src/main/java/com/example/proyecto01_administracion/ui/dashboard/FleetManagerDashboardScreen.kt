package com.example.proyecto01_administracion.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proyecto01_administracion.ui.login.AuthViewModel
import com.example.proyecto01_administracion.ui.fleet.UpcomingMaintenancesFleetCard
import com.example.proyecto01_administracion.ui.theme.StatusGreen
import com.example.proyecto01_administracion.ui.theme.StatusRed
import com.example.proyecto01_administracion.ui.theme.StatusYellow

@Composable
fun FleetManagerDashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    onNavigateToFleet: () -> Unit = {},
    onNavigateToAlerts: () -> Unit = {},
    onNavigateToMaintenanceHistory: () -> Unit = {},
    onAvatarClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        DashboardHeader(
            userName = currentUser?.nombre ?: "Encargado",
            userRole = "Encargado de flota",
            hasNotifications = false,
            onAvatarClick = onAvatarClick,
            onNotificationClick = onNotificationClick
        )
        
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(uiState.error!!, color = MaterialTheme.colorScheme.error)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item {
                    SectionHeader(
                        title = "Resumen de Flotilla",
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
                            label = "Al día",
                            value = uiState.upToDateCount.toString(),
                            accentColor = StatusGreen,
                            onClick = onNavigateToFleet
                        )
                        StatCard(
                            modifier = Modifier.weight(1f),
                            label = "Próximos",
                            value = uiState.dueSoonCount.toString(),
                            accentColor = StatusYellow,
                            onClick = onNavigateToFleet
                        )
                        StatCard(
                            modifier = Modifier.weight(1f),
                            label = "Atrasados",
                            value = uiState.overdueCount.toString(),
                            accentColor = StatusRed,
                            onClick = onNavigateToFleet
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    AnnouncementsCard(
                        announcements = uiState.announcements,
                        onViewAlerts = onNavigateToAlerts
                    )
                }

                item {
                    SectionHeader(
                        title = "Próximos Mantenimientos",
                        icon = Icons.Default.Build
                    )
                    UpcomingMaintenancesFleetCard(
                        maintenances = uiState.upcomingMaintenances,
                        onViewAll = onNavigateToMaintenanceHistory
                    )
                }
            }
        }
    }
}
