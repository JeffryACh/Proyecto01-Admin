package com.example.proyecto01_administracion.ui.mechanic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyecto01_administracion.ui.theme.AccentBlue
import com.example.proyecto01_administracion.ui.theme.BackgroundBlack
import com.example.proyecto01_administracion.ui.theme.CardGray
import com.example.proyecto01_administracion.ui.theme.TextGrayLight
import com.example.proyecto01_administracion.ui.theme.TextWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterMaintenanceScreen(
    plate: String,
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Mantenimiento", color = TextWhite) },
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
                Text(
                    text = "Vehículo: $plate",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AccentBlue
                )
            }
            
            item {
                MaintenanceInputField(label = "Tipo de Mantenimiento", value = "Preventivo", onValueChange = {}, icon = Icons.Default.Settings)
            }
            
            item {
                MaintenanceInputField(label = "Categoría", value = "Motor", onValueChange = {}, icon = Icons.Default.Category)
            }
            
            item {
                MaintenanceInputField(label = "Fecha", value = "28/08/2026", onValueChange = {}, icon = Icons.Default.CalendarToday)
            }
            
            item {
                MaintenanceInputField(label = "Taller / Mecánico", value = "Taller Central", onValueChange = {}, icon = Icons.Default.LocationOn)
            }
            
            item {
                MaintenanceInputField(label = "Kilometraje", value = "125,500", onValueChange = {}, icon = Icons.Default.Numbers)
            }
            
            item {
                MaintenanceInputField(label = "Costo ($)", value = "150.00", onValueChange = {}, icon = Icons.Default.Payments)
            }
            
            item {
                MaintenanceInputField(
                    label = "Descripción",
                    value = "",
                    onValueChange = {},
                    icon = Icons.Default.Description,
                    singleLine = false,
                    minLines = 3
                )
            }
            
            item {
                Text(
                    text = "Evidencia fotográfica",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextGrayLight,
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                )
                
                OutlinedButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextGrayLight),
                    border = ButtonDefaults.outlinedButtonBorder(enabled = true)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.AddAPhoto, contentDescription = null, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Subir fotos")
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onSuccess,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentBlue)
                ) {
                    Text("Registrar Mantenimiento", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun MaintenanceInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = TextGrayLight,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            leadingIcon = { Icon(icon, contentDescription = null, tint = AccentBlue) },
            singleLine = singleLine,
            minLines = minLines,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CardGray,
                unfocusedContainerColor = CardGray,
                focusedBorderColor = AccentBlue,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite
            )
        )
    }
}
