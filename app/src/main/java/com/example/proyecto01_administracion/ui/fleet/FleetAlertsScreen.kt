package com.example.proyecto01_administracion.ui.fleet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto01_administracion.ui.dashboard.AlertFilterChip
import com.example.proyecto01_administracion.ui.dashboard.AlertStat
import com.example.proyecto01_administracion.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FleetAlertsScreen(
    onBack: () -> Unit
) {
    val semanticColors = LocalTransAndinaColors.current
    val allAlerts = listOf(
        FleetAlert("Urgente", "ABC-123: Cambio de frenos atrasado por 200 km", semanticColors.statusRed),
        FleetAlert("Urgente", "GHI-789: Revisión técnica vence mañana", semanticColors.statusRed),
        FleetAlert("Próxima", "DEF-456: Mantenimiento preventivo en 500 km", semanticColors.statusYellow),
        FleetAlert("Próxima", "JKL-012: Seguro vence en 15 días", semanticColors.statusYellow),
        FleetAlert("Informativa", "MNO-345: Nuevo registro de kilometraje", semanticColors.statusBlue)
    )

    var selectedFilter by remember { mutableStateOf("Todas") }

    val filteredAlerts = if (selectedFilter == "Todas") {
        allAlerts
    } else {
        allAlerts.filter { it.type == selectedFilter.removeSuffix("s") }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Alertas de Flota", color = MaterialTheme.colorScheme.onSurface) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Row(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                ) {
                    AlertStat(
                        modifier = Modifier.weight(1f),
                        count = "${allAlerts.count { it.type == "Urgente" }}",
                        label = "Urgente",
                        color = semanticColors.statusRed
                    )
                    AlertStat(
                        modifier = Modifier.weight(1f),
                        count = "${allAlerts.count { it.type == "Próxima" }}",
                        label = "Próxima",
                        color = semanticColors.statusYellow
                    )
                    AlertStat(
                        modifier = Modifier.weight(1f),
                        count = "${allAlerts.count { it.type == "Informativa" }}",
                        label = "Info",
                        color = semanticColors.statusBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Filters
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AlertFilterChip(selected = selectedFilter == "Todas", label = "Todas", onClick = { selectedFilter = "Todas" })
                AlertFilterChip(selected = selectedFilter == "Urgentes", label = "Urgentes", onClick = { selectedFilter = "Urgentes" })
                AlertFilterChip(selected = selectedFilter == "Próximas", label = "Próximas", onClick = { selectedFilter = "Próximas" })
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                items(filteredAlerts) { alert ->
                    FleetAlertCard(alert = alert)
                }
            }
        }
    }
}

data class FleetAlert(val type: String, val message: String, val color: Color)

@Composable
fun FleetAlertCard(alert: FleetAlert) {
    val semanticColors = LocalTransAndinaColors.current
    val color = alert.color
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (color == semanticColors.statusRed) Icons.Default.Warning else Icons.Default.Info,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }
            Column {
                Text(text = alert.type, color = color, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = alert.message, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyMedium, lineHeight = 20.sp)
            }
        }
    }
}
