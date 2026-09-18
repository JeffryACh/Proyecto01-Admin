package com.example.proyecto01_administracion.ui.vehicle

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.proyecto01_administracion.domain.models.Maintenance
import com.example.proyecto01_administracion.ui.dashboard.VehicleSummary
import com.example.proyecto01_administracion.ui.dashboard.AppFilterChip
import com.example.proyecto01_administracion.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaintenanceHistoryScreen(
    vehicleId: String? = null,
    viewModel: MaintenanceHistoryViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onNavigateToDetail: (Maintenance) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

    LaunchedEffect(vehicleId) {
        viewModel.loadData(vehicleId)
    }

    val filteredRecords = if (uiState.filter == "Todos") {
        uiState.maintenanceRecords
    } else {
        uiState.maintenanceRecords.filter { it.tipo == uiState.filter }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial de mantenimiento", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
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
            Spacer(modifier = Modifier.height(16.dp))
            
            uiState.vehicle?.let { vehicle ->
                VehicleSummary(model = "${vehicle.marca} ${vehicle.modelo}", plate = vehicle.placa)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AppFilterChip(selected = uiState.filter == "Todos", label = "Todos", onClick = { viewModel.onFilterChange("Todos") })
                AppFilterChip(selected = uiState.filter == "Preventivo", label = "Preventivo", onClick = { viewModel.onFilterChange("Preventivo") })
                AppFilterChip(selected = uiState.filter == "Correctivo", label = "Correctivo", onClick = { viewModel.onFilterChange("Correctivo") })
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (uiState.error != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(uiState.error!!, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
                }
            } else if (filteredRecords.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("No hay mantenimientos registrados", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                        Text("Los mantenimientos realizados aparecerán aquí.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp, textAlign = TextAlign.Center)
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(filteredRecords) { record ->
                        MaintenanceItemCard(
                            record = record, 
                            dateLabel = dateFormat.format(record.fecha.toDate()),
                            onClick = { onNavigateToDetail(record) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MaintenanceItemCard(record: Maintenance, dateLabel: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = record.taller, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                StatusTag(type = record.tipo)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(text = record.descripcion, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = dateLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = "${record.kilometraje} km", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text(
                    text = "₡${String.format(Locale.US, "%,.2f", record.costo)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
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
