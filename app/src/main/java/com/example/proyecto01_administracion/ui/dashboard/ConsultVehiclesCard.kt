package com.example.proyecto01_administracion.ui.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyecto01_administracion.ui.theme.TextGrayLight
import com.example.proyecto01_administracion.ui.theme.TextGrayMedium

@Composable
fun ConsultVehiclesCard(
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
            ConsultVehicleItem(
                model = "Toyota Hilux",
                plate = "ABC-123",
                mileage = "125,430 km",
                onClick = onViewAll
            )
            
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            
            ConsultVehicleItem(
                model = "Isuzu NPR",
                plate = "XYZ-456",
                mileage = "98,240 km",
                onClick = onViewAll
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

            ConsultVehicleItem(
                model = "Ford Transit",
                plate = "DEF-789",
                mileage = "87,650 km",
                onClick = onViewAll
            )

            HistoryLink(
                text = "Ver todos los vehículos",
                icon = Icons.Default.DirectionsCar,
                onClick = onViewAll
            )
        }
    }
}

@Composable
private fun ConsultVehicleItem(
    model: String,
    plate: String,
    mileage: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = model,
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
            text = mileage,
            style = MaterialTheme.typography.bodyMedium,
            color = TextGrayLight,
            fontWeight = FontWeight.Medium
        )
    }
}
