package com.example.proyecto01_administracion.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyecto01_administracion.ui.theme.StatusGreen
import com.example.proyecto01_administracion.ui.theme.StatusYellow
import kotlinx.coroutines.launch

@Composable
fun FleetManagerDashboardScreen(onLogout: () -> Unit) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                userName = "Carlos Rodríguez",
                userRole = "Encargado de Flotilla",
                onLogout = {
                    scope.launch { drawerState.close() }
                    onLogout()
                }
            )
        }
    ) {
        Scaffold(
            bottomBar = {
                FleetBottomNavBar(selectedItem = 0)
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
                        userName = "Carlos Rodríguez",
                        userRole = "Encargado de Flotilla",
                        hasNotifications = true,
                        onAvatarClick = { scope.launch { drawerState.open() } }
                    )
                }

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
                            onClick = { /* Navigate to Fleet */ }
                        )
                        StatCard(
                            modifier = Modifier.weight(1f),
                            label = "Próximos vehículos",
                            value = "4",
                            accentColor = StatusYellow,
                            onClick = { /* Navigate to Fleet Filtered */ }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    AnnouncementsCard(
                        onViewAlerts = { /* Navigate to Alerts */ }
                    )
                }

                item {
                    SectionHeader(
                        title = "Próximos Mantenimientos",
                        icon = Icons.Default.Build
                    )
                    UpcomingMaintenancesFleetCard(
                        onViewAll = { /* Navigate to Maintenance */ }
                    )
                }
            }
        }
    }
}
