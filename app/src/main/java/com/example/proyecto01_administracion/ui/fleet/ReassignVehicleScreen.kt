package com.example.proyecto01_administracion.ui.fleet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proyecto01_administracion.domain.models.Vehicle
import com.example.proyecto01_administracion.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReassignVehicleScreen(
    userId: String,
    viewModel: AssignmentViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(userId) {
        viewModel.loadData(userId) 
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reasignar Vehículo", color = MaterialTheme.colorScheme.onSurface) },
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
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Conductor", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(uiState.user?.nombre ?: if (uiState.isLoading) "Cargando…" else "No registrado", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Text(uiState.user?.correo ?: if (uiState.isLoading) "" else "No registrado", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Seleccionar nuevo vehículo", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 32.dp))
            } else if (uiState.error != null) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(uiState.error!!, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error)
                }
            } else if (uiState.vehicles.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("No hay vehículos disponibles.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(uiState.vehicles) { vehicle ->
                        VehicleOptionItem(
                            vehicle = vehicle,
                            isSelected = uiState.selectedVehicleId == vehicle.id,
                            onClick = { viewModel.onVehicleSelected(vehicle.id) }
                        )
                    }
                }
            }

            Button(
                onClick = viewModel::reassign,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 16.dp),
                enabled = !uiState.isLoading && uiState.selectedVehicleId.isNotBlank(),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Confirmar reasignación", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun VehicleOptionItem(vehicle: Vehicle, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) AccentBlue.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant
        ),
        border = if (isSelected) BorderStroke(2.dp, AccentBlue) else null
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = if (isSelected) AccentBlue else MaterialTheme.colorScheme.onSurfaceVariant)
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "${vehicle.marca} ${vehicle.modelo}", color = MaterialTheme.colorScheme.onSurface, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                Text(text = vehicle.placa, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
            }
            RadioButton(selected = isSelected, onClick = onClick, colors = RadioButtonDefaults.colors(selectedColor = AccentBlue))
        }
    }
}
