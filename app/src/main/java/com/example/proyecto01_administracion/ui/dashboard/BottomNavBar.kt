package com.example.proyecto01_administracion.ui.dashboard

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.proyecto01_administracion.ui.theme.AccentBlue

private data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@Composable
fun BottomNavBar(
    selectedItem: Int = 0,
    onHomeClick: () -> Unit = {},
    onVehicleClick: () -> Unit = {},
    onAlertsClick: () -> Unit = {},
    onMoreClick: () -> Unit = {}
) {
    AppBottomNavBar(
        selectedItem = selectedItem,
        items = listOf(
            BottomNavItem("Inicio", Icons.Default.Home, onHomeClick),
            BottomNavItem("Vehículo", Icons.Default.DirectionsCar, onVehicleClick),
            BottomNavItem("Alertas", Icons.Default.Notifications, onAlertsClick),
            BottomNavItem("Menú", Icons.Default.Menu, onMoreClick)
        )
    )
}

@Composable
fun FleetBottomNavBar(
    selectedItem: Int = 0,
    onHomeClick: () -> Unit = {},
    onFleetClick: () -> Unit = {},
    onAlertsClick: () -> Unit = {},
    onMoreClick: () -> Unit = {}
) {
    AppBottomNavBar(
        selectedItem = selectedItem,
        items = listOf(
            BottomNavItem("Inicio", Icons.Default.Home, onHomeClick),
            BottomNavItem("Flota", Icons.Default.LocalShipping, onFleetClick),
            BottomNavItem("Alertas", Icons.Default.Notifications, onAlertsClick),
            BottomNavItem("Menú", Icons.Default.Menu, onMoreClick)
        )
    )
}

@Composable
fun MechanicBottomNavBar(
    selectedItem: Int = 0,
    onHomeClick: () -> Unit = {},
    onMaintenanceClick: () -> Unit = {},
    onAlertsClick: () -> Unit = {},
    onMoreClick: () -> Unit = {}
) {
    AppBottomNavBar(
        selectedItem = selectedItem,
        items = listOf(
            BottomNavItem("Inicio", Icons.Default.Home, onHomeClick),
            BottomNavItem("Mantenimiento", Icons.Default.Build, onMaintenanceClick),
            BottomNavItem("Alertas", Icons.Default.Notifications, onAlertsClick),
            BottomNavItem("Menú", Icons.Default.Menu, onMoreClick)
        )
    )
}

@Composable
private fun AppBottomNavBar(
    selectedItem: Int,
    items: List<BottomNavItem>
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        tonalElevation = 0.dp,
        modifier = Modifier.height(88.dp)
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = item.onClick,
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier
                            .size(26.dp)
                            .offset(y = 4.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AccentBlue,
                    selectedTextColor = AccentBlue,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
