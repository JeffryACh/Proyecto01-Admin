package com.example.proyecto01_administracion.ui.fleet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proyecto01_administracion.ui.dashboard.VehicleSummary
import com.example.proyecto01_administracion.ui.theme.*
import com.example.proyecto01_administracion.ui.vehicle.BaseVehicleCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FleetVehicleDetailScreen(
    plate: String,
    viewModel: FleetVehicleDetailViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onRegisterMaintenance: (String) -> Unit,
    onNavigateToMaintenanceHistory: () -> Unit,
    onNavigateToMileageHistory: () -> Unit,
    onReassignDriver: () -> Unit,
    onNavigateToDocuments: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val semanticColors = LocalTransAndinaColors.current
    var showDeactivateDialog by remember { mutableStateOf(false) }

    LaunchedEffect(plate) {
        viewModel.loadVehicle(plate)
    }

    if (showDeactivateDialog) {
        AlertDialog(
            onDismissRequest = { showDeactivateDialog = false },
            title = { Text("Marcar como inactivo", color = MaterialTheme.colorScheme.onSurface) },
            text = { Text("¿Deseas marcar este vehículo como inactivo temporalmente?", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            confirmButton = {
                TextButton(onClick = { 
                    viewModel.deactivateVehicle()
                    showDeactivateDialog = false 
                }) {
                    Text("Confirmar", color = semanticColors.statusRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeactivateDialog = false }) {
                    Text("Cancelar", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Vehículo", color = MaterialTheme.colorScheme.onSurface) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onEdit) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.onSurface)
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
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState.error != null) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text(uiState.error!!, color = MaterialTheme.colorScheme.error)
            }
        } else if (uiState.vehicle == null) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("Vehículo no encontrado", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            val vehicle = uiState.vehicle!!
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        VehicleSummary(model = "${vehicle.marca} ${vehicle.modelo}", plate = vehicle.placa)
                        val statusColor = if (vehicle.estado == "Activo") semanticColors.statusGreen else semanticColors.statusRed
                        Surface(shape = RoundedCornerShape(16.dp), color = statusColor.copy(alpha = 0.1f)) {
                            Text(text = vehicle.estado, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), color = statusColor, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                
                item {
                    BaseVehicleCard(title = "Conductor Asignado") {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Box(modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.surfaceVariant, CircleShape), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = AccentBlue)
                                }
                                Column {
                                    Text(text = uiState.assignedDriver?.nombre ?: "Sin asignar", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                    Text(text = "Conductor", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            TextButton(onClick = onReassignDriver) {
                                Text("Reasignar", color = AccentBlue)
                            }
                        }
                    }
                }
                
                item {
                    BaseVehicleCard(title = "Información General") {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            DetailRow(label = "Kilometraje Actual", value = "${vehicle.kilometraje_actual} km")
                            DetailRow(label = "Año", value = vehicle.anio.toString())
                            DetailRow(label = "Tipo", value = vehicle.tipo)
                            DetailRow(label = "Clasificación", value = vehicle.clasificacion)
                            DetailRow(label = "Capacidad", value = "${vehicle.capacidad} kg")
                        }
                    }
                }
                
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        ActionMenuItem(text = "Registrar Mantenimiento", icon = Icons.Default.Build, onClick = { onRegisterMaintenance(plate) })
                        ActionMenuItem(text = "Historial de Mantenimiento", icon = Icons.Default.History, onClick = onNavigateToMaintenanceHistory)
                        ActionMenuItem(text = "Historial de Kilometraje", icon = Icons.Default.Timeline, onClick = onNavigateToMileageHistory)
                        ActionMenuItem(text = "Documentos del Vehículo", icon = Icons.Default.Description, onClick = onNavigateToDocuments)
                    }
                }
                
                if (vehicle.estado == "Activo") {
                    item {
                        OutlinedButton(
                            onClick = { showDeactivateDialog = true },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = semanticColors.statusRed),
                            border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(brush = SolidColor(semanticColors.statusRed))
                        ) {
                            Icon(imageVector = Icons.Default.Block, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Marcar como Inactivo", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun ActionMenuItem(text: String, icon: ImageVector, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Icon(icon, contentDescription = null, tint = AccentBlue)
                Text(text = text, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
