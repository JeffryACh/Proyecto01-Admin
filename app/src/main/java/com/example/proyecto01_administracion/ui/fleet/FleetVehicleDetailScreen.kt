package com.example.proyecto01_administracion.ui.fleet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto01_administracion.ui.dashboard.HistoryLink
import com.example.proyecto01_administracion.ui.theme.AccentBlue
import com.example.proyecto01_administracion.ui.theme.BackgroundBlack
import com.example.proyecto01_administracion.ui.theme.CardGray
import com.example.proyecto01_administracion.ui.theme.StatusGreen
import com.example.proyecto01_administracion.ui.theme.TextGrayLight
import com.example.proyecto01_administracion.ui.theme.TextWhite
import com.example.proyecto01_administracion.ui.vehicle.BaseVehicleCard
import com.example.proyecto01_administracion.ui.vehicle.InfoRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FleetVehicleDetailScreen(
    plate: String,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onNavigateToMaintenanceHistory: () -> Unit,
    onNavigateToMileageHistory: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Vehículo", color = TextWhite) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = TextWhite
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onEdit) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar", tint = TextWhite)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundBlack
                )
            )
        },
        containerColor = BackgroundBlack
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Toyota Hilux", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = TextWhite)
                        Text(text = plate, style = MaterialTheme.typography.bodyLarge, color = TextGrayLight)
                    }
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = StatusGreen.copy(alpha = 0.1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(modifier = Modifier.size(8.dp).background(StatusGreen, CircleShape))
                            Text(text = "Al día", color = StatusGreen, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        }
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
                            Box(modifier = Modifier.size(40.dp).background(BackgroundBlack, CircleShape), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = AccentBlue)
                            }
                            Column {
                                Text(text = "Juan Pérez", fontWeight = FontWeight.Bold, color = TextWhite)
                                Text(text = "ID: 1-2345-6789", fontSize = 12.sp, color = TextGrayLight)
                            }
                        }
                        TextButton(onClick = { /* Reassign */ }) {
                            Text("Reasignar", color = AccentBlue)
                        }
                    }
                }
            }
            
            item {
                BaseVehicleCard(title = "Información Técnica") {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        InfoRow(label = "Marca", value = "Toyota")
                        InfoRow(label = "Modelo", value = "Hilux")
                        InfoRow(label = "Año", value = "2022")
                        InfoRow(label = "Kilometraje Total", value = "125,430 km")
                        InfoRow(label = "Capacidad", value = "1,500 kg")
                    }
                }
            }
            
            item {
                BaseVehicleCard(title = "Historial y Documentos") {
                    Column {
                        HistoryLink(text = "Historial de Mantenimiento", icon = Icons.Default.History, onClick = onNavigateToMaintenanceHistory)
                        HistoryLink(text = "Historial de Kilometraje", icon = Icons.Default.History, onClick = onNavigateToMileageHistory)
                        HistoryLink(text = "Documentos del Vehículo", icon = Icons.Default.ChevronRight, onClick = {})
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { /* Inactivate */ },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(brush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.error))
                ) {
                    Icon(imageVector = Icons.Default.Block, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Desactivar Vehículo", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
