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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto01_administracion.ui.theme.*

enum class AlertPriority(val label: String, val color: Color) {
    URGENT("Urgente", StatusRed),
    UPCOMING("Próxima", StatusYellow),
    INFO("Informativa", AccentBlue)
}

data class Alert(
    val typeIcon: ImageVector,
    val title: String,
    val description: String,
    val priority: AlertPriority,
    val dateLabel: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertsScreen(
    onBack: () -> Unit
) {
    val allAlerts = listOf(
        Alert(Icons.Default.Warning, "Mantenimiento atrasado", "El mantenimiento preventivo requiere atención.", AlertPriority.URGENT, "Hace 5 horas"),
        Alert(Icons.Default.Build, "Cambio de aceite próximo", "Faltan aproximadamente 450 km para el próximo mantenimiento.", AlertPriority.UPCOMING, "Hace 2 días"),
        Alert(Icons.Default.Description, "Seguro próximo a vencer", "El seguro del vehículo vence el 15 Sep 2026.", AlertPriority.UPCOMING, "Hace 1 día"),
        Alert(Icons.Default.CheckCircle, "Mantenimiento registrado", "El cambio de aceite fue registrado correctamente.", AlertPriority.INFO, "10 Ago 2026"),
        Alert(Icons.Default.DirectionsCar, "Vehículo reasignado", "Tu vehículo asignado ha cambiado.", AlertPriority.INFO, "Hace 1 semana")
    )

    var selectedFilter by remember { mutableStateOf("Todas") }

    val filteredAlerts = if (selectedFilter == "Todas") {
        allAlerts
    } else {
        allAlerts.filter { it.priority.label == selectedFilter.removeSuffix("s") }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Alertas", fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = StatusRed,
                            shape = CircleShape
                        ) {
                            Text(
                                text = "${allAlerts.size}",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundBlack)
            )
        },
        bottomBar = {
            BottomNavBar(selectedItem = 2, onHomeClick = onBack)
        },
        containerColor = BackgroundBlack
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            // Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardGray),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Row(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AlertStat(count = "1", label = "Urgente", color = StatusRed)
                    AlertStat(count = "2", label = "Próxima", color = StatusYellow)
                    AlertStat(count = "2", label = "Info", color = AccentBlue)
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

            if (filteredAlerts.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.NotificationsNone, contentDescription = null, tint = TextGrayMedium, modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("No tienes alertas pendientes", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("Las notificaciones relacionadas con tu vehículo aparecerán aquí.", color = TextGrayLight, fontSize = 14.sp)
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(filteredAlerts) { alert ->
                        AlertItemCard(alert = alert)
                    }
                }
            }
        }
    }
}

@Composable
fun AlertStat(count: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(count, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
        Text(label, style = MaterialTheme.typography.labelSmall, color = color)
    }
}

@Composable
fun AlertFilterChip(selected: Boolean, label: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = if (selected) AccentBlue else CardGray,
        border = if (selected) null else BorderStroke(1.dp, CardBorderGray)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = if (selected) Color.White else TextGrayLight,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun AlertItemCard(alert: Alert) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardGray),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(alert.priority.color.copy(alpha = 0.5f))
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(alert.priority.color.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = alert.typeIcon, contentDescription = null, tint = alert.priority.color, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = alert.title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(text = alert.dateLabel, style = MaterialTheme.typography.labelSmall, color = TextGrayMedium)
                }
                Surface(
                    color = alert.priority.color.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, alert.priority.color.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = alert.priority.label,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = alert.priority.color,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = alert.description,
                style = MaterialTheme.typography.bodyMedium,
                color = TextGrayLight
            )
        }
    }
}
