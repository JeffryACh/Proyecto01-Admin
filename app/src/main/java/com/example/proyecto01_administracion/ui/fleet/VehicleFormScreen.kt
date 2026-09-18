package com.example.proyecto01_administracion.ui.fleet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proyecto01_administracion.ui.theme.*
import com.example.proyecto01_administracion.ui.dashboard.AppChoiceChip
import com.example.proyecto01_administracion.ui.dashboard.FormTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleFormScreen(
    plate: String? = null,
    viewModel: VehicleViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(plate) {
        viewModel.loadVehicle(plate)
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (uiState.isEdit) "Editar Vehículo" else "Registrar Vehículo", color = MaterialTheme.colorScheme.onSurface) },
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
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            FormTextField(
                value = uiState.placa,
                onValueChange = viewModel::onPlacaChange,
                label = "Placa",
                enabled = !uiState.isEdit && !uiState.isLoading,
                placeholder = "ABC-123",
                isError = uiState.validationErrors.containsKey("placa"),
                supportingText = uiState.validationErrors["placa"]?.let { { Text(it) } }
            )

            FormTextField(
                value = uiState.marca,
                onValueChange = viewModel::onMarcaChange,
                label = "Marca",
                enabled = !uiState.isLoading,
                placeholder = "Toyota",
                isError = uiState.validationErrors.containsKey("marca"),
                supportingText = uiState.validationErrors["marca"]?.let { { Text(it) } }
            )

            FormTextField(
                value = uiState.modelo,
                onValueChange = viewModel::onModeloChange,
                label = "Modelo",
                enabled = !uiState.isLoading,
                placeholder = "Hilux",
                isError = uiState.validationErrors.containsKey("modelo"),
                supportingText = uiState.validationErrors["modelo"]?.let { { Text(it) } }
            )

            FormTextField(
                value = uiState.anio,
                onValueChange = viewModel::onAnioChange,
                label = "Año",
                enabled = !uiState.isLoading,
                placeholder = "2022",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = uiState.validationErrors.containsKey("anio"),
                supportingText = uiState.validationErrors["anio"]?.let { { Text(it) } }
            )

            FormTextField(
                value = uiState.clasificacion,
                onValueChange = viewModel::onClasificacionChange,
                label = "Clasificación",
                enabled = !uiState.isLoading,
                placeholder = "Pick-up / Camión / etc."
            )

            FormTextField(
                value = uiState.capacidad,
                onValueChange = viewModel::onCapacidadChange,
                label = "Capacidad (kg)",
                enabled = !uiState.isLoading,
                placeholder = "1000.0",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = uiState.validationErrors.containsKey("capacidad"),
                supportingText = uiState.validationErrors["capacidad"]?.let { { Text(it) } }
            )
            
            Text("Tipo de Vehículo", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AppChoiceChip(selected = uiState.tipo == "Liviano", label = "Liviano", onClick = { viewModel.onTipoChange("Liviano") })
                AppChoiceChip(selected = uiState.tipo == "Pesado", label = "Pesado", onClick = { viewModel.onTipoChange("Pesado") })
            }

            if (!uiState.isEdit) {
                FormTextField(
                    value = uiState.kilometraje,
                    onValueChange = viewModel::onKilometrajeChange,
                    label = "Kilometraje Inicial",
                    enabled = !uiState.isLoading,
                    placeholder = "0",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = uiState.validationErrors.containsKey("kilometraje"),
                    supportingText = uiState.validationErrors["kilometraje"]?.let { { Text(it) } }
                )
            }

            Text("Estado del Vehículo", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AppChoiceChip(selected = uiState.estado == "Activo", label = "Activo", onClick = { viewModel.onEstadoChange("Activo") })
                AppChoiceChip(selected = uiState.estado == "Inactivo", label = "Inactivo", onClick = { viewModel.onEstadoChange("Inactivo") })
            }

            if (uiState.error != null) {
                Text(
                    text = uiState.error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = viewModel::saveVehicle,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 16.dp),
                enabled = !uiState.isLoading,
                shape = RoundedCornerShape(16.dp)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(if (uiState.isEdit) "Guardar Cambios" else "Registrar Vehículo", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
