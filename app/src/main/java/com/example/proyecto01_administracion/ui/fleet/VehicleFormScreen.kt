package com.example.proyecto01_administracion.ui.fleet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyecto01_administracion.ui.theme.*
import com.example.proyecto01_administracion.ui.dashboard.AppChoiceChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleFormScreen(
    plate: String? = null,
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    val isEdit = plate != null
    var model by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var mileage by remember { mutableStateOf("") }
    var vin by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("Liviano") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEdit) "Editar Vehículo" else "Registrar Vehículo", color = MaterialTheme.colorScheme.onSurface) },
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

            FormTextField(value = model, onValueChange = { model = it }, label = "Marca y Modelo", placeholder = "Toyota Hilux")
            FormTextField(value = plate ?: "", onValueChange = { }, label = "Placa", enabled = !isEdit, placeholder = "ABC-123")
            FormTextField(value = year, onValueChange = { year = it }, label = "Año", placeholder = "2022")
            FormTextField(value = vin, onValueChange = { vin = it }, label = "VIN / Chasis", placeholder = "1A2B3C4D5E6F7G8H9")
            
            Text("Tipo de Vehículo", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AppChoiceChip(selected = type == "Liviano", label = "Liviano", onClick = { type = "Liviano" })
                AppChoiceChip(selected = type == "Pesado", label = "Pesado", onClick = { type = "Pesado" })
            }

            if (!isEdit) {
                FormTextField(value = mileage, onValueChange = { mileage = it }, label = "Kilometraje Inicial", placeholder = "0")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onSave,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(if (isEdit) "Guardar Cambios" else "Registrar Vehículo", fontWeight = FontWeight.Bold)
            }
        }
    }
}
