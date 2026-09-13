package com.example.proyecto01_administracion.ui.dashboard

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.proyecto01_administracion.ui.theme.AccentBlue
import com.example.proyecto01_administracion.ui.theme.BackgroundBlack
import com.example.proyecto01_administracion.ui.theme.TextGrayMedium

@Composable
fun FleetBottomNavBar(
    selectedItem: Int = 0,
    onHomeClick: () -> Unit = {},
    onFleetClick: () -> Unit = {},
    onAlertsClick: () -> Unit = {},
    onMoreClick: () -> Unit = {}
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        tonalElevation = 0.dp,
        modifier = Modifier.height(88.dp)
    ) {
        NavigationBarItem(
            selected = selectedItem == 0,
            onClick = onHomeClick,
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio", modifier = Modifier.size(26.dp).offset(y = 4.dp)) },
            label = { Text("Inicio", style = MaterialTheme.typography.labelMedium) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AccentBlue,
                selectedTextColor = AccentBlue,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = selectedItem == 1,
            onClick = onFleetClick,
            icon = { Icon(Icons.Default.LocalShipping, contentDescription = "Flota", modifier = Modifier.size(26.dp).offset(y = 4.dp)) },
            label = { Text("Flota", style = MaterialTheme.typography.labelMedium) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AccentBlue,
                selectedTextColor = AccentBlue,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = selectedItem == 2,
            onClick = onAlertsClick,
            icon = { Icon(Icons.Default.Notifications, contentDescription = "Alertas", modifier = Modifier.size(26.dp).offset(y = 4.dp)) },
            label = { Text("Alertas", style = MaterialTheme.typography.labelMedium) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AccentBlue,
                selectedTextColor = AccentBlue,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = selectedItem == 3,
            onClick = onMoreClick,
            icon = { Icon(Icons.Default.Menu, contentDescription = "Menú", modifier = Modifier.size(26.dp).offset(y = 4.dp)) },
            label = { Text("Menú", style = MaterialTheme.typography.labelMedium) },
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
