package com.example.proyecto01_administracion.ui.vehicle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proyecto01_administracion.domain.models.Vehicle
import com.example.proyecto01_administracion.ui.dashboard.*
import com.example.proyecto01_administracion.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleScreen(
    viewModel: DriverDashboardViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
    onNavigateToRegisterMileage: () -> Unit = {},
    onNavigateToMileageHistory: () -> Unit = {},
    onNavigateToDocuments: (String) -> Unit = {},
    onNavigateToMaintenanceHistory: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Vehículo", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
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
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState.vehicle == null) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("No tienes un vehículo asignado.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            val vehicle = uiState.vehicle!!
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    VehicleIdentificationSection(
                        model = "${vehicle.marca} ${vehicle.modelo}",
                        plate = vehicle.placa,
                        statusLabel = "Vehículo ${vehicle.estado}"
                    )
                }

                item {
                    VehicleInfoCard(vehicle)
                }

                item {
                    MileageCard(
                        mileage = "${vehicle.kilometraje_actual} km",
                        onRegister = onNavigateToRegisterMileage,
                        onViewHistory = onNavigateToMileageHistory
                    )
                }

                item {
                    DocumentsCard(
                        onViewDocuments = { onNavigateToDocuments(vehicle.placa) }
                    )
                }

                item {
                    LastMaintenanceCard(
                        onViewHistory = onNavigateToMaintenanceHistory
                    )
                }
            }
        }
    }
}

@Composable
fun VehicleIdentificationSection(
    model: String,
    plate: String,
    statusLabel: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        VehicleSummary(model = model, plate = plate)
        
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = StatusGreen.copy(alpha = 0.1f)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(StatusGreen, CircleShape)
                )
                Text(
                    text = statusLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = StatusGreen,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun VehicleInfoCard(vehicle: Vehicle) {
    BaseVehicleCard(title = "Información del vehículo") {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            InfoRow(label = "Marca", value = vehicle.marca)
            InfoRow(label = "Modelo", value = vehicle.modelo)
            InfoRow(label = "Año", value = vehicle.anio.toString())
            InfoRow(label = "Tipo", value = vehicle.tipo)
            InfoRow(label = "Clasificación", value = vehicle.clasificacion)
            InfoRow(label = "Capacidad", value = "${vehicle.capacidad} kg")
        }
    }
}

@Composable
fun MileageCard(
    mileage: String,
    onRegister: () -> Unit = {},
    onViewHistory: () -> Unit = {}
) {
    BaseVehicleCard(title = "Kilometraje") {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                text = mileage,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Button(
                onClick = onRegister,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentBlue)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Registrar kilometraje")
            }
            
            HistoryLink(
                text = "Ver historial",
                onClick = onViewHistory
            )
        }
    }
}

@Composable
fun DocumentsCard(
    onViewDocuments: () -> Unit = {}
) {
    BaseVehicleCard(title = "Documentos") {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Consulta la vigencia de tus documentos legales.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            HistoryLink(
                text = "Ver documentos",
                onClick = onViewDocuments
            )
        }
    }
}

@Composable
fun LastMaintenanceCard(
    onViewHistory: () -> Unit = {}
) {
    BaseVehicleCard(title = "Mantenimiento") {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Consulta el historial de servicios realizados.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            HistoryLink(
                text = "Ver historial de servicios",
                onClick = onViewHistory
            )
        }
    }
}

@Composable
fun BaseVehicleCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            content()
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
        Text(text = value, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium, fontSize = 14.sp)
    }
}
