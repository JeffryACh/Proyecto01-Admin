package com.example.proyecto01_administracion.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyecto01_administracion.ui.theme.StatusGreen
import com.example.proyecto01_administracion.ui.theme.StatusYellow
import kotlinx.coroutines.launch

@Composable
fun MechanicDashboardScreen(onLogout: () -> Unit) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                userName = "Juan Pérez",
                userRole = "Mecánico",
                onLogout = {
                    scope.launch { drawerState.close() }
                    onLogout()
                }
            )
        }
    ) {
        Scaffold(
            bottomBar = {
                MechanicBottomNavBar(selectedItem = 0)
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
                        userRole = "Mecánico",
                        hasNotifications = true,
                        onAvatarClick = { scope.launch { drawerState.open() } }
                    )
                }

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
                            onClick = { /* Navigate to Pending */ }
                        )
                        StatCard(
                            modifier = Modifier.weight(1f),
                            label = "Realizados",
                            value = "12",
                            accentColor = StatusGreen,
                            onClick = { /* Navigate to Done */ }
                        )
                    }
                }

                item {
                    SectionHeader(
                        title = "Trabajos recientes",
                        icon = Icons.Default.History
                    )
                    RecentWorksCard(
                        onViewHistory = { /* Navigate to History */ }
                    )
                }

                item {
                    SectionHeader(
                        title = "Acciones rápidas",
                        icon = Icons.Default.FlashOn
                    )
                    QuickActionsCard(
                        onRegisterMaintenance = { /* Navigate to Register Form */ },
                        onViewHistory = { /* Navigate to History */ }
                    )
                }
            }
        }
    }
}
