package com.example.proyecto01_administracion.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proyecto01_administracion.ui.login.AuthViewModel
import com.example.proyecto01_administracion.domain.models.FleetStatus
import java.text.SimpleDateFormat
import java.util.Locale

private fun formatDashboardDate(epochMillis: Long): String =
    SimpleDateFormat("dd MMM yyyy · HH:mm", Locale.getDefault()).format(epochMillis)

@Composable
fun DashboardScreen(
    viewModel: DriverDashboardViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    onNavigateToMaintenanceHistory: () -> Unit = {},
    onNavigateToMileageHistory: () -> Unit = {},
    onAvatarClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        DashboardHeader(
            userName = currentUser?.nombre ?: "Conductor",
            userRole = "Conductor",
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
                Text(
                    text = uiState.error!!,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
        } else if (uiState.vehicle == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No tienes un vehículo asignado.\nContacta al encargado de flota.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            val vehicle = uiState.vehicle!!
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item {
                    SectionHeader(
                        title = "Mi Vehículo",
                        icon = Icons.Default.DirectionsCar
                    )
                    
                    VehicleCard(
                        model = "${vehicle.marca} ${vehicle.modelo}",
                        plate = vehicle.placa,
                        mileage = "${vehicle.kilometraje_actual} km",
                        status = when (uiState.fleetStatus) {
                            FleetStatus.UP_TO_DATE -> VehicleStatus.ON_TRACK
                            FleetStatus.MAINTENANCE_DUE_SOON -> VehicleStatus.UPCOMING
                            FleetStatus.MAINTENANCE_OVERDUE -> VehicleStatus.DELAYED
                            null -> null
                        },
                        nextMaintenanceTask = uiState.fleetStatus?.let { "Estado de mantenimiento" } ?: "Sin plan de mantenimiento disponible",
                        remainingKm = "",
                        progress = 0f
                    )
                }

                item {
                    SectionHeader(
                        title = "Mantenimiento Reciente",
                        icon = Icons.Default.Build
                    )
                    val recentMaintenance = uiState.recentMaintenance
                    if (recentMaintenance == null) {
                        Text(
                            text = "No hay mantenimientos registrados.",
                            modifier = Modifier.padding(horizontal = 24.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        MaintenanceCard(
                            date = formatDashboardDate(recentMaintenance.fecha.toDate().time),
                            type = recentMaintenance.tipo,
                            mileage = "${recentMaintenance.kilometraje} km",
                            onViewHistory = onNavigateToMaintenanceHistory
                        )
                    }
                }

                item {
                    SectionHeader(
                        title = "Último Kilometraje",
                        icon = Icons.AutoMirrored.Filled.ShowChart
                    )
                    val latestMileage = uiState.latestMileage
                    if (latestMileage == null) {
                        Text(
                            text = "Aún no hay registros de kilometraje.",
                            modifier = Modifier.padding(horizontal = 24.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        MileageCard(
                            date = formatDashboardDate(latestMileage.fecha.toDate().time),
                            mileage = "${latestMileage.kilometraje} km",
                            onViewHistory = onNavigateToMileageHistory
                        )
                    }
                }
            }
        }
    }
}
