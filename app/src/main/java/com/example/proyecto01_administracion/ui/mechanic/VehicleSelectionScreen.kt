package com.example.proyecto01_administracion.ui.mechanic

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto01_administracion.ui.theme.AccentBlue
import com.example.proyecto01_administracion.ui.theme.BackgroundBlack
import com.example.proyecto01_administracion.ui.theme.CardGray
import com.example.proyecto01_administracion.ui.theme.StatusGreen
import com.example.proyecto01_administracion.ui.theme.StatusRed
import com.example.proyecto01_administracion.ui.theme.StatusYellow
import com.example.proyecto01_administracion.ui.theme.TextGrayLight
import com.example.proyecto01_administracion.ui.theme.TextWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleSelectionScreen(
    onBack: () -> Unit,
    onVehicleSelected: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val vehicles = listOf(
        VehicleItem("ABC-123", "Toyota Hilux", "Al día", StatusGreen),
        VehicleItem("DEF-456", "Hyundai H1", "Próximo", StatusYellow),
        VehicleItem("GHI-789", "Freightliner Cascadia", "Atrasado", StatusRed),
        VehicleItem("JKL-012", "Isuzu NPR", "Al día", StatusGreen),
        VehicleItem("MNO-345", "Mercedes-Benz Actros", "Al día", StatusGreen)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Seleccionar Vehículo", color = TextWhite) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar por placa o modelo...", color = TextGrayLight) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = AccentBlue) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CardGray,
                    unfocusedContainerColor = CardGray,
                    focusedBorderColor = AccentBlue,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = TextWhite
                )
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(vehicles.filter { it.plate.contains(searchQuery, true) || it.model.contains(searchQuery, true) }) { vehicle ->
                    VehicleSelectionCard(vehicle = vehicle, onClick = { onVehicleSelected(vehicle.plate) })
                }
            }
        }
    }
}

data class VehicleItem(val plate: String, val model: String, val status: String, val statusColor: Color)

@Composable
fun VehicleSelectionCard(vehicle: VehicleItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardGray),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = vehicle.model, fontWeight = FontWeight.Bold, color = TextWhite, fontSize = 16.sp)
                Text(text = vehicle.plate, color = TextGrayLight, fontSize = 14.sp)
            }
            
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.size(8.dp).background(vehicle.statusColor, CircleShape))
                Text(text = vehicle.status, color = vehicle.statusColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
