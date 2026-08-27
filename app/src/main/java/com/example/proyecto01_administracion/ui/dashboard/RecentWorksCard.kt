package com.example.proyecto01_administracion.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyecto01_administracion.ui.theme.TextGrayLight
import com.example.proyecto01_administracion.ui.theme.TextGrayMedium
import com.example.proyecto01_administracion.ui.theme.AccentBlue

@Composable
fun RecentWorksCard(
    onViewHistory: () -> Unit
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
            RecentWorkItem(
                vehicle = "Toyota Hilux",
                plate = "ABC-123",
                task = "Cambio de aceite",
                date = "Hoy"
            )
            
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            
            RecentWorkItem(
                vehicle = "Isuzu NPR",
                plate = "DEF-456",
                task = "Revisión de frenos",
                date = "Ayer"
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

            RecentWorkItem(
                vehicle = "Volvo FH",
                plate = "GHI-789",
                task = "Revisión general",
                date = "20 mayo 2026"
            )

            HistoryLink(
                text = "Ver historial",
                icon = Icons.Default.Build,
                onClick = onViewHistory
            )
        }
    }
}

@Composable
private fun RecentWorkItem(
    vehicle: String,
    plate: String,
    task: String,
    date: String
) {
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
                    color = Color.White
                )
                Text(
                    text = plate,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextGrayMedium
                )
            }
            Text(
                text = task,
                style = MaterialTheme.typography.bodyMedium,
                color = TextGrayLight
            )
        }
        
        Text(
            text = date,
            style = MaterialTheme.typography.labelSmall,
            color = TextGrayMedium
        )
    }
}
