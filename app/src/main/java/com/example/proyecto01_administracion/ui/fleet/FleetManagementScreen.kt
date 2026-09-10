package com.example.proyecto01_administracion.ui.fleet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto01_administracion.ui.dashboard.SectionHeader
import com.example.proyecto01_administracion.ui.dashboard.VehicleSummary
import com.example.proyecto01_administracion.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FleetManagementScreen(
    onBack: () -> Unit,
    onNavigateToVehicleDetail: (String) -> Unit,
    onNavigateToCreateVehicle: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Todos") }

    val allVehicles = listOf(
        FleetVehicleItem("ABC-123", "Toyota Hilux", "125,430 km", "Juan Pérez", "Cambio de aceite", "Al día", StatusGreen),
        FleetVehicleItem("XYZ-456", "Isuzu NPR", "98,240 km", "María López", "Revisión de frenos", "Próximo", StatusYellow),
        FleetVehicleItem("DEF-789", "Ford Transit", "87,650 km", "Carlos Rodríguez", "Revisión general", "Atrasado", StatusRed),
        FleetVehicleItem("GHI-012", "Mitsubishi L200", "45,000 km", "Ana García", "Cambio de filtros", "Al día", StatusGreen),
        FleetVehicleItem("JKL-345", "Nissan Frontier", "67,890 km", "Pedro Ruiz", "Alineamiento", "Próximo", StatusYellow),
        FleetVehicleItem("MNO-678", "Hino 300", "150,200 km", "Luis Torres", "Reparación motor", "Atrasado", StatusRed)
    )

    val filteredVehicles = remember(searchQuery, selectedFilter) {
        allVehicles.filter { vehicle ->
            (selectedFilter == "Todos" || vehicle.status == selectedFilter) &&
            (vehicle.plate.contains(searchQuery, ignoreCase = true) || 
             vehicle.model.contains(searchQuery, ignoreCase = true) ||
             vehicle.conductor.contains(searchQuery, ignoreCase = true))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Flota", color = MaterialTheme.colorScheme.onSurface) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Summary Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FleetStat(label = "Al día", count = "${allVehicles.count { it.status == "Al día" }}", color = StatusGreen)
                FleetStat(label = "Próximos", count = "${allVehicles.count { it.status == "Próximo" }}", color = StatusYellow)
                FleetStat(label = "Atrasados", count = "${allVehicles.count { it.status == "Atrasado" }}", color = StatusRed)
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar vehículo", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = AccentBlue) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedBorderColor = AccentBlue,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Filters Scrollable Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(selected = selectedFilter == "Todos", label = "Todos", onClick = { selectedFilter = "Todos" })
                FilterChip(selected = selectedFilter == "Al día", label = "Al día", onClick = { selectedFilter = "Al día" })
                FilterChip(selected = selectedFilter == "Próximo", label = "Próximos", onClick = { selectedFilter = "Próximo" })
                FilterChip(selected = selectedFilter == "Atrasado", label = "Atrasados", onClick = { selectedFilter = "Atrasado" })
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.weight(1f)) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(filteredVehicles) { vehicle ->
                        FleetVehicleCard(vehicle = vehicle, onClick = { onNavigateToVehicleDetail(vehicle.plate) })
                    }
                }
                
                FloatingActionButton(
                    onClick = onNavigateToCreateVehicle,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 16.dp),
                    containerColor = AccentBlue,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Registrar vehículo")
                }
            }
        }
    }
}

@Composable
fun FleetStat(label: String, count: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(modifier = Modifier.size(8.dp).background(color, CircleShape))
        Text(text = "$label $count", color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun FleetVehicleCard(vehicle: FleetVehicleItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                VehicleSummary(model = vehicle.model, plate = vehicle.plate)
                Text(
                    text = vehicle.mileage, 
                    style = MaterialTheme.typography.bodyMedium, 
                    fontWeight = FontWeight.Bold, 
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Conductor", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(vehicle.conductor, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Próximo", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(vehicle.nextTask, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Surface(
                color = vehicle.statusColor.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, vehicle.statusColor.copy(alpha = 0.5f))
            ) {
                Text(
                    text = vehicle.status,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = vehicle.statusColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

data class FleetVehicleItem(
    val plate: String,
    val model: String,
    val mileage: String,
    val conductor: String,
    val nextTask: String,
    val status: String,
    val statusColor: Color
)

@Composable
fun FilterChip(selected: Boolean, label: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = if (selected) AccentBlue else MaterialTheme.colorScheme.surfaceVariant,
        border = if (selected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}
