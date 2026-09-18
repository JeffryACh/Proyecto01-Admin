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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proyecto01_administracion.ui.dashboard.AlertFilterChip
import com.example.proyecto01_administracion.ui.dashboard.AlertStat
import com.example.proyecto01_administracion.ui.dashboard.AlertViewModel
import com.example.proyecto01_administracion.ui.dashboard.formatRelativeAlertTime
import com.example.proyecto01_administracion.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FleetAlertsScreen(
    viewModel: AlertViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val semanticColors = LocalTransAndinaColors.current

    var selectedFilter by remember { mutableStateOf("Todas") }

    val filteredAlerts = if (selectedFilter == "Todas") {
        uiState.alerts
    } else {
        uiState.alerts.filter { it.severidad == selectedFilter }
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
                        count = "${uiState.alerts.count { it.severidad == "Rojo" }}",
                        label = "Urgente",
                        color = semanticColors.statusRed
                    )
                    AlertStat(
                        modifier = Modifier.weight(1f),
                        count = "${uiState.alerts.count { it.severidad == "Amarillo" }}",
                        label = "Próxima",
                        color = semanticColors.statusYellow
                    )
                    AlertStat(
                        modifier = Modifier.weight(1f),
                        count = "${uiState.alerts.count { it.severidad == "Info" }}",
                        label = "Info",
                        color = semanticColors.statusBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Filters
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AlertFilterChip(selected = selectedFilter == "Todas", label = "Todas", onClick = { selectedFilter = "Todas" })
                AlertFilterChip(selected = selectedFilter == "Rojo", label = "Urgentes", onClick = { selectedFilter = "Rojo" })
                AlertFilterChip(selected = selectedFilter == "Amarillo", label = "Próximas", onClick = { selectedFilter = "Amarillo" })
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (uiState.error != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(uiState.error!!, color = MaterialTheme.colorScheme.error)
                }
            } else if (filteredAlerts.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay alertas registradas", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    items(filteredAlerts) { alert ->
                        FleetAlertCard(
                            type = alert.severidad,
                            message = alert.mensaje,
                            dateLabel = formatRelativeAlertTime(alert.fecha_creacion),
                            color = when(alert.severidad) {
                                "Rojo" -> semanticColors.statusRed
                                "Amarillo" -> semanticColors.statusYellow
                                else -> semanticColors.statusBlue
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FleetAlertCard(type: String, message: String, dateLabel: String, color: Color) {
    val semanticColors = LocalTransAndinaColors.current
    
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
                Text(text = type, color = color, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = message, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyMedium, lineHeight = 20.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = dateLabel, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
