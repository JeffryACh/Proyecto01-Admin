package com.example.proyecto01_administracion.ui.fleet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyecto01_administracion.ui.dashboard.HistoryLink
import com.example.proyecto01_administracion.ui.theme.*

data class UpcomingMaintenance(
    val vehicleModel: String,
    val plate: String,
    val task: String,
    val remainingInfo: String,
    val statusColor: Color
)

@Composable
fun UpcomingMaintenancesFleetCard(
    maintenances: List<UpcomingMaintenance>,
    onViewAll: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (maintenances.isEmpty()) {
                Text(
                    text = "No hay mantenimientos próximos.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                maintenances.take(2).forEachIndexed { index, item ->
                    MaintenanceFleetItem(
                        vehicle = item.vehicleModel,
                        plate = item.plate,
                        task = item.task,
                        remaining = item.remainingInfo,
                        statusColor = item.statusColor
                    )
                    
                    if (index < maintenances.take(2).size - 1) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                    }
                }
            }

            HistoryLink(
                text = "Ver Mantenimientos",
                onClick = onViewAll
            )
        }
    }
}

@Composable
private fun MaintenanceFleetItem(
    vehicle: String,
    plate: String,
    task: String,
    remaining: String,
    statusColor: Color
) {
    val semanticColors = LocalTransAndinaColors.current
    val displayColor = when(statusColor) {
        StatusGreen -> semanticColors.statusGreen
        StatusYellow -> semanticColors.statusYellow
        StatusRed -> semanticColors.statusRed
        else -> statusColor
    }
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = vehicle,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = plate,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = task,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(displayColor, CircleShape)
            )
            Text(
                text = remaining,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = displayColor
            )
        }
    }
}
