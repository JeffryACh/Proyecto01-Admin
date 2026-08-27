package com.example.proyecto01_administracion.ui.vehicle

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto01_administracion.ui.theme.*

data class MaintenanceRecord(
    val title: String,
    val type: String, // Preventivo or Correctivo
    val date: String,
    val mileage: String,
    val cost: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaintenanceHistoryScreen(
    onBack: () -> Unit,
    onNavigateToDetail: (MaintenanceRecord) -> Unit = {}
) {
    val maintenanceRecords = listOf(
        MaintenanceRecord("Cambio de aceite", "Preventivo", "10 Ago 2026", "120,000 km", "₡45,000"),
        MaintenanceRecord("Cambio de pastillas de freno", "Correctivo", "25 Jul 2026", "118,500 km", "₡85,000"),
        MaintenanceRecord("Revisión general", "Preventivo", "10 Jun 2026", "115,000 km", "₡30,000")
    )

    var selectedFilter by remember { mutableStateOf("Todos") }

    val filteredRecords = if (selectedFilter == "Todos") {
        maintenanceRecords
    } else {
        maintenanceRecords.filter { it.type == selectedFilter }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial de mantenimiento", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundBlack)
            )
        },
        containerColor = BackgroundBlack
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            // Vehicle Identification
            Text(
                text = "Toyota Hilux",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "ABC-123",
                style = MaterialTheme.typography.bodySmall,
                color = TextGrayLight
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Filters
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(selected = selectedFilter == "Todos", label = "Todos", onClick = { selectedFilter = "Todos" })
                FilterChip(selected = selectedFilter == "Preventivo", label = "Preventivo", onClick = { selectedFilter = "Preventivo" })
                FilterChip(selected = selectedFilter == "Correctivo", label = "Correctivo", onClick = { selectedFilter = "Correctivo" })
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Últimos 12 meses",
                style = MaterialTheme.typography.labelMedium,
                color = TextGrayMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            if (filteredRecords.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("No hay mantenimientos registrados", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("Los mantenimientos realizados aparecerán aquí.", color = TextGrayLight, fontSize = 14.sp)
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(filteredRecords) { record ->
                        MaintenanceItemCard(record = record, onClick = { onNavigateToDetail(record) })
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChip(selected: Boolean, label: String, onClick: () -> Unit) {
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
fun MaintenanceItemCard(record: MaintenanceRecord, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardGray),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = record.title, fontWeight = FontWeight.Bold, color = Color.White)
                StatusTag(type = record.type)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(text = "Toyota Hilux · ABC-123", style = MaterialTheme.typography.bodySmall, color = TextGrayMedium)
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = record.date, style = MaterialTheme.typography.bodySmall, color = TextGrayLight)
                    Text(text = record.mileage, style = MaterialTheme.typography.bodySmall, color = TextGrayLight)
                }
                Text(
                    text = record.cost,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Bottom)
                )
            }
        }
    }
}

@Composable
fun StatusTag(type: String) {
    val color = if (type == "Preventivo") StatusGreen else StatusYellow
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.5f))
    ) {
        Text(
            text = type,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
