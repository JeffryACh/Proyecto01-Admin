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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proyecto01_administracion.domain.models.Vehicle
import com.example.proyecto01_administracion.domain.models.FleetStatus
import com.example.proyecto01_administracion.ui.dashboard.VehicleSummary
import com.example.proyecto01_administracion.ui.dashboard.AppFilterChip
import com.example.proyecto01_administracion.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FleetManagementScreen(
    viewModel: FleetViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onNavigateToVehicleDetail: (String) -> Unit,
    onNavigateToCreateVehicle: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val filteredVehicles = remember(uiState.vehicles, uiState.fleetStatusByVehicleId, uiState.searchQuery, uiState.selectedFilter) {
        uiState.vehicles.filter { vehicle ->
            // Filter logic:
            val matchesSearch = vehicle.placa.contains(uiState.searchQuery, ignoreCase = true) || 
                               vehicle.marca.contains(uiState.searchQuery, ignoreCase = true) ||
                               vehicle.modelo.contains(uiState.searchQuery, ignoreCase = true)
            
            val status = uiState.fleetStatusByVehicleId[vehicle.id]
            val matchesFilter = when (uiState.selectedFilter) {
                "Todos" -> true
                "Inactivos" -> vehicle.estado == "Inactivo"
                "Al día" -> vehicle.estado == "Activo" && status == FleetStatus.UP_TO_DATE
                "Próximos" -> vehicle.estado == "Activo" && status == FleetStatus.MAINTENANCE_DUE_SOON
                "Atrasados" -> vehicle.estado == "Activo" && status == FleetStatus.MAINTENANCE_OVERDUE
                else -> false
            }
            matchesSearch && matchesFilter
        }
    }

    val semanticColors = LocalTransAndinaColors.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Flota", color = MaterialTheme.colorScheme.onSurface) },
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
            
            // 2x2 Grid for Summary Cards
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FleetSummaryCard(
                        modifier = Modifier.weight(1f),
                        label = "Al día",
                        count = uiState.upToDateCount.toString(),
                        color = semanticColors.statusGreen,
                        isSelected = uiState.selectedFilter == "Al día",
                        onClick = { viewModel.onFilterChange(if (uiState.selectedFilter == "Al día") "Todos" else "Al día") }
                    )
                    FleetSummaryCard(
                        modifier = Modifier.weight(1f),
                        label = "Próximos",
                        count = uiState.dueSoonCount.toString(),
                        color = semanticColors.statusYellow,
                        isSelected = uiState.selectedFilter == "Próximos",
                        onClick = { viewModel.onFilterChange(if (uiState.selectedFilter == "Próximos") "Todos" else "Próximos") }
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FleetSummaryCard(
                        modifier = Modifier.weight(1f),
                        label = "Atrasados",
                        count = uiState.overdueCount.toString(),
                        color = semanticColors.statusRed,
                        isSelected = uiState.selectedFilter == "Atrasados",
                        onClick = { viewModel.onFilterChange(if (uiState.selectedFilter == "Atrasados") "Todos" else "Atrasados") }
                    )
                    FleetSummaryCard(
                        modifier = Modifier.weight(1f),
                        label = "Inactivos",
                        count = uiState.inactiveCount.toString(),
                        color = Color.Gray,
                        isSelected = uiState.selectedFilter == "Inactivos",
                        onClick = { viewModel.onFilterChange(if (uiState.selectedFilter == "Inactivos") "Todos" else "Inactivos") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
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
                AppFilterChip(selected = uiState.selectedFilter == "Todos", label = "Todos", onClick = { viewModel.onFilterChange("Todos") })
                AppFilterChip(selected = uiState.selectedFilter == "Al día", label = "Al día", onClick = { viewModel.onFilterChange("Al día") })
                AppFilterChip(selected = uiState.selectedFilter == "Próximos", label = "Próximos", onClick = { viewModel.onFilterChange("Próximos") })
                AppFilterChip(selected = uiState.selectedFilter == "Atrasados", label = "Atrasados", onClick = { viewModel.onFilterChange("Atrasados") })
                AppFilterChip(selected = uiState.selectedFilter == "Inactivos", label = "Inactivos", onClick = { viewModel.onFilterChange("Inactivos") })
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.weight(1f)) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (uiState.error != null) {
                    Text(uiState.error!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.align(Alignment.Center))
                } else if (filteredVehicles.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No hay vehículos registrados",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Los vehículos aparecerán aquí.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(filteredVehicles) { vehicle ->
                            FleetVehicleCard(
                                vehicle = vehicle,
                                onClick = { onNavigateToVehicleDetail(vehicle.placa) }
                            )
                        }
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
fun FleetSummaryCard(
    modifier: Modifier = Modifier,
    label: String,
    count: String,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) color.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant
        ),
        border = if (isSelected) BorderStroke(2.dp, color) else null
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(color, CircleShape)
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
            Text(
                text = count,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun FleetVehicleCard(vehicle: Vehicle, onClick: () -> Unit) {
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
                VehicleSummary(model = "${vehicle.marca} ${vehicle.modelo}", plate = vehicle.placa)
                Text(
                    text = "${vehicle.kilometraje_actual} km", 
                    style = MaterialTheme.typography.bodyMedium, 
                    fontWeight = FontWeight.Bold, 
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Clasificación", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(vehicle.clasificacion, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Estado", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(vehicle.estado, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            val semanticColors = LocalTransAndinaColors.current
            val displayColor = if (vehicle.estado == "Inactivo") Color.Gray else semanticColors.statusGreen // Logic pending
            
            Surface(
                color = displayColor.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, displayColor.copy(alpha = 0.5f))
            ) {
                Text(
                    text = vehicle.estado,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = displayColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
