package com.example.proyecto01_administracion.ui.fleet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyecto01_administracion.ui.profile.EditField
import com.example.proyecto01_administracion.ui.theme.AccentBlue
import com.example.proyecto01_administracion.ui.theme.BackgroundBlack
import com.example.proyecto01_administracion.ui.theme.TextWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleFormScreen(
    plate: String? = null,
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    val isEdit = plate != null
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEdit) "Editar Vehículo" else "Registrar Vehículo", color = TextWhite) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = TextWhite
                        )
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
                EditField(label = "Placa", value = plate ?: "", onValueChange = {}, icon = Icons.Default.Badge)
            }
            item {
                EditField(label = "Marca", value = "Toyota", onValueChange = {}, icon = Icons.Default.Settings)
            }
            item {
                EditField(label = "Modelo", value = "Hilux", onValueChange = {}, icon = Icons.Default.DirectionsCar)
            }
            item {
                EditField(label = "Año", value = "2022", onValueChange = {}, icon = Icons.Default.CalendarToday)
            }
            item {
                EditField(label = "Kilometraje Inicial", value = "0", onValueChange = {}, icon = Icons.Default.Numbers)
            }
            item {
                EditField(label = "Conductor Asignado", value = "Juan Pérez", onValueChange = {}, icon = Icons.Default.Person)
            }
            
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onSave,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentBlue)
                ) {
                    Text(if (isEdit) "Guardar Cambios" else "Registrar Vehículo", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
