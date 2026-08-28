package com.example.proyecto01_administracion.ui.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.proyecto01_administracion.ui.theme.AccentBlue
import com.example.proyecto01_administracion.ui.theme.TextGrayMedium

@Composable
fun MechanicBottomNavBar(
    selectedItem: Int = 0,
    onHomeClick: () -> Unit = {},
    onMaintenanceClick: () -> Unit = {},
    onAlertsClick: () -> Unit = {},
    onMoreClick: () -> Unit = {}
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = TextGrayMedium,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = selectedItem == 0,
            onClick = onHomeClick,
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AccentBlue,
                selectedTextColor = AccentBlue,
                unselectedIconColor = TextGrayMedium,
                unselectedTextColor = TextGrayMedium,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = selectedItem == 1,
            onClick = onMaintenanceClick,
            icon = { Icon(Icons.Default.Build, contentDescription = "Mantenimiento") },
            label = { Text("Mantenimiento") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AccentBlue,
                selectedTextColor = AccentBlue,
                unselectedIconColor = TextGrayMedium,
                unselectedTextColor = TextGrayMedium,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = selectedItem == 2,
            onClick = onAlertsClick,
            icon = { Icon(Icons.Default.Notifications, contentDescription = "Alertas") },
            label = { Text("Alertas") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AccentBlue,
                selectedTextColor = AccentBlue,
                unselectedIconColor = TextGrayMedium,
                unselectedTextColor = TextGrayMedium,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = selectedItem == 3,
            onClick = onMoreClick,
            icon = { Icon(Icons.Default.Menu, contentDescription = "Más") },
            label = { Text("Más") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AccentBlue,
                selectedTextColor = AccentBlue,
                unselectedIconColor = TextGrayMedium,
                unselectedTextColor = TextGrayMedium,
                indicatorColor = Color.Transparent
            )
        )
    }
}
