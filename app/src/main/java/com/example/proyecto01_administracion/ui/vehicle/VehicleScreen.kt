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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto01_administracion.ui.dashboard.*
import com.example.proyecto01_administracion.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleScreen(
    onBack: () -> Unit = {},
    onNavigateToRegisterMileage: () -> Unit = {},
    onNavigateToMileageHistory: () -> Unit = {},
    onNavigateToDocuments: () -> Unit = {},
    onNavigateToMaintenanceHistory: () -> Unit = {}
) {
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
// ...
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                VehicleIdentificationSection(
                    model = "Toyota Hilux",
                    plate = "ABC-123",
                    statusLabel = "Vehículo activo"
                )
            }

            item {
                VehicleInfoCard()
            }

            item {
                MileageCard(
                    onRegister = onNavigateToRegisterMileage,
                    onViewHistory = onNavigateToMileageHistory
                )
            }

            item {
                NextMaintenanceCard()
            }

            item {
                DocumentsCard(
                    onViewDocuments = onNavigateToDocuments
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
fun VehicleInfoCard() {
    BaseVehicleCard(title = "Información del vehículo") {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            InfoRow(label = "Marca", value = "Toyota")
            InfoRow(label = "Modelo", value = "Hilux")
            InfoRow(label = "Año", value = "2022")
            InfoRow(label = "Tipo", value = "Pesado")
            InfoRow(label = "Capacidad", value = "1,500 kg")
        }
    }
}

@Composable
fun MileageCard(
    onRegister: () -> Unit = {},
    onViewHistory: () -> Unit = {}
) {
    BaseVehicleCard(title = "Kilometraje") {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "125,430 km",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Último registro: 25 Ago 2026",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
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
fun NextMaintenanceCard() {
    BaseVehicleCard(title = "Próximo mantenimiento") {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "🔧 Cambio de aceite",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "450 km restantes",
                    style = MaterialTheme.typography.bodySmall,
                    color = StatusYellow
                )
            }
            
            Text(
                text = "Fecha estimada: 15 Sep 2026",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            // Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight()
                        .background(
                            brush = Brush.horizontalGradient(listOf(AccentBlue, AccentPurple)),
                            shape = RoundedCornerShape(4.dp)
                        )
                )
            }
        }
    }
}

@Composable
fun DocumentsCard(
    onViewDocuments: () -> Unit = {}
) {
    BaseVehicleCard(title = "Documentos") {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            DocumentRow(label = "Marchamo", status = "Vigente", color = StatusGreen)
            DocumentRow(label = "Revisión técnica", status = "Vigente", color = StatusGreen)
            DocumentRow(label = "Seguro", status = "Próximo", color = StatusYellow)
            
            Spacer(modifier = Modifier.height(8.dp))
            
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
    BaseVehicleCard(title = "Último mantenimiento") {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Cambio de aceite",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "120,000 km",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "10 Ago 2026",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "Mecánico: Juan Pérez",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            HistoryLink(
                text = "Ver historial",
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

@Composable
fun DocumentRow(label: String, status: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(color, CircleShape)
            )
            Text(text = status, color = color, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}
