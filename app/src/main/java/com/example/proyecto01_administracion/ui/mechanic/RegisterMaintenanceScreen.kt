package com.example.proyecto01_administracion.ui.mechanic

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto01_administracion.ui.fleet.FormTextField
import com.example.proyecto01_administracion.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterMaintenanceScreen(
    plate: String,
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    var type by remember { mutableStateOf("Preventivo") }
    var description by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf("") }
    var showConfirmation by remember { mutableStateOf(false) }

    if (showConfirmation) {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                TextButton(onClick = { 
                    showConfirmation = false
                    onSuccess() 
                }) {
                    Text("OK", color = AccentBlue)
                }
            },
            title = { Text("Éxito", color = MaterialTheme.colorScheme.onSurface) },
            text = { Text("Mantenimiento registrado correctamente para el vehículo $plate", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Mantenimiento", color = MaterialTheme.colorScheme.onSurface) },
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Vehículo", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(plate, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                }
            }

            Text("Tipo de Mantenimiento", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                MaintenanceTypeChip(selected = type == "Preventivo", label = "Preventivo", onClick = { type = "Preventivo" })
                MaintenanceTypeChip(selected = type == "Correctivo", label = "Correctivo", onClick = { type = "Correctivo" })
            }

            FormTextField(value = description, onValueChange = { description = it }, label = "Descripción de tareas", placeholder = "Ej: Cambio de aceite y filtros")
            FormTextField(value = cost, onValueChange = { cost = it }, label = "Costo estimado", placeholder = "0.00")

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { showConfirmation = true },
                modifier = Modifier.fillMaxWidth().height(56.dp).padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = description.isNotEmpty()
            ) {
                Text("Finalizar Registro", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun MaintenanceTypeChip(selected: Boolean, label: String, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = AccentBlue,
            selectedLabelColor = Color.White,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}
