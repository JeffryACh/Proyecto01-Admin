package com.example.proyecto01_administracion.ui.fleet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyecto01_administracion.ui.dashboard.StatCard
import com.example.proyecto01_administracion.ui.theme.AccentBlue
import com.example.proyecto01_administracion.ui.theme.BackgroundBlack
import com.example.proyecto01_administracion.ui.theme.CardGray
import com.example.proyecto01_administracion.ui.theme.StatusGreen
import com.example.proyecto01_administracion.ui.theme.StatusRed
import com.example.proyecto01_administracion.ui.theme.StatusYellow
import com.example.proyecto01_administracion.ui.theme.TextGrayLight
import com.example.proyecto01_administracion.ui.theme.TextWhite
import com.example.proyecto01_administracion.ui.mechanic.VehicleItem
import com.example.proyecto01_administracion.ui.mechanic.VehicleSelectionCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FleetManagementScreen(
    onBack: () -> Unit,
    onNavigateToVehicleDetail: (String) -> Unit,
    onNavigateToCreateVehicle: () -> Unit
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
                title = { Text("Gestión de Flota", color = TextWhite) },
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreateVehicle,
                containerColor = AccentBlue,
                contentColor = TextWhite,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Registrar vehículo")
            }
        },
        containerColor = BackgroundBlack
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        label = "Al día",
                        value = "12",
                        accentColor = StatusGreen,
                        onClick = {}
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        label = "Próximos",
                        value = "4",
                        accentColor = StatusYellow,
                        onClick = {}
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        label = "Atrasados",
                        value = "2",
                        accentColor = StatusRed,
                        onClick = {}
                    )
                }
            }

            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Buscar vehículo...", color = TextGrayLight) },
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
            }

            items(vehicles.filter { it.plate.contains(searchQuery, true) || it.model.contains(searchQuery, true) }) { vehicle ->
                VehicleSelectionCard(vehicle = vehicle, onClick = { onNavigateToVehicleDetail(vehicle.plate) })
            }
        }
    }
}
