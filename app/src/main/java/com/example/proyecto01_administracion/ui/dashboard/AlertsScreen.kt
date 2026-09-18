package com.example.proyecto01_administracion.ui.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proyecto01_administracion.domain.models.Alert
import com.example.proyecto01_administracion.ui.theme.*
import com.google.firebase.Timestamp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertsScreen(
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
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Alertas", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        if (uiState.alerts.isNotEmpty()) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = semanticColors.statusRed,
                                shape = CircleShape
                            ) {
                                Text(
                                    text = "${uiState.alerts.size}",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
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
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AlertFilterChip(selected = selectedFilter == "Todas", label = "Todas", onClick = { selectedFilter = "Todas" })
                AlertFilterChip(selected = selectedFilter == "Rojo", label = "Urgentes", onClick = { selectedFilter = "Rojo" })
                AlertFilterChip(selected = selectedFilter == "Amarillo", label = "Próximas", onClick = { selectedFilter = "Amarillo" })
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            } else if (uiState.error != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(uiState.error!!, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center) }
            } else if (filteredAlerts.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.NotificationsNone, contentDescription = null, tint = TextGrayMedium, modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("No tienes alertas pendientes", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                        Text("Las notificaciones aparecerán aquí.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp, textAlign = TextAlign.Center)
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(filteredAlerts) { alert ->
                        AlertItemCard(
                            alert = alert,
                            dateLabel = formatRelativeAlertTime(alert.fecha_creacion),
                            onClick = { viewModel.markAsRead(alert.id) }
                        )
                    }
                }
            }
        }
    }
}

fun formatRelativeAlertTime(timestamp: Timestamp, nowMillis: Long = System.currentTimeMillis()): String {
    val deltaMillis = (nowMillis - timestamp.toDate().time).coerceAtLeast(0L)
    val minutes = deltaMillis / 60_000L
    val hours = deltaMillis / 3_600_000L
    val days = deltaMillis / 86_400_000L
    return when {
        minutes < 1 -> "Ahora"
        minutes < 60 -> "Hace $minutes min"
        hours < 24 -> "Hace $hours h"
        else -> "Hace $days días"
    }
}

@Composable
fun AlertFilterChip(selected: Boolean, label: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = if (selected) AccentBlue else MaterialTheme.colorScheme.surfaceVariant,
        border = if (selected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun AlertItemCard(
    alert: Alert,
    dateLabel: String,
    onClick: () -> Unit
) {
    val semanticColors = LocalTransAndinaColors.current
    val priorityColor = when(alert.severidad) {
        "Rojo" -> semanticColors.statusRed
        "Amarillo" -> semanticColors.statusYellow
        else -> semanticColors.statusBlue
    }
    
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(priorityColor.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    val icon = when(alert.severidad) {
                        "Rojo" -> Icons.Default.Warning
                        "Amarillo" -> Icons.Default.Build
                        else -> Icons.Default.Info
                    }
                    Icon(imageVector = icon, contentDescription = null, tint = priorityColor, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = alert.titulo, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Text(text = dateLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = alert.mensaje,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
