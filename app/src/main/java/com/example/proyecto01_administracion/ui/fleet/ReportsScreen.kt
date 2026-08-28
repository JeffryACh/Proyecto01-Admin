package com.example.proyecto01_administracion.ui.fleet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto01_administracion.ui.theme.AccentBlue
import com.example.proyecto01_administracion.ui.theme.BackgroundBlack
import com.example.proyecto01_administracion.ui.theme.CardGray
import com.example.proyecto01_administracion.ui.theme.TextGrayLight
import com.example.proyecto01_administracion.ui.theme.TextWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reportes y Estadísticas", color = TextWhite) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = TextWhite
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Filter */ }) {
                        Icon(imageVector = Icons.Default.FilterList, contentDescription = "Filtrar", tint = TextWhite)
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
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardGray),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.DateRange, contentDescription = null, tint = AccentBlue, modifier = Modifier.size(20.dp))
                            Text(text = "Últimos 30 días", color = TextGrayLight, style = MaterialTheme.typography.bodySmall)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Costo Total Mantenimiento", color = TextGrayLight, fontSize = 14.sp)
                        Text(text = "$4,250.00", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
            
            item {
                Text(text = "Distribución por Tipo", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextWhite)
                Spacer(modifier = Modifier.height(16.dp))
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ReportBar(label = "Preventivo", value = 65, color = AccentBlue)
                    ReportBar(label = "Correctivo", value = 25, color = Color(0xFFF44336))
                    ReportBar(label = "Predictivo", value = 10, color = Color(0xFF4CAF50))
                }
            }
            
            item {
                Text(text = "Top Vehículos por Gasto", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextWhite)
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardGray),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        VehicleExpenseItem(plate = "GHI-789", amount = "$1,200")
                        VehicleExpenseItem(plate = "ABC-123", amount = "$850")
                        VehicleExpenseItem(plate = "DEF-456", amount = "$420")
                    }
                }
            }
        }
    }
}

@Composable
fun ReportBar(label: String, value: Int, color: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = label, color = TextGrayLight, fontSize = 12.sp)
            Text(text = "$value%", color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Box(modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape).background(CardGray)) {
            Box(modifier = Modifier.fillMaxWidth(value / 100f).fillMaxHeight().background(color, CircleShape))
        }
    }
}

@Composable
fun VehicleExpenseItem(plate: String, amount: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(Icons.Default.BarChart, contentDescription = null, tint = AccentBlue, modifier = Modifier.size(20.dp))
            Text(text = plate, color = TextWhite, fontWeight = FontWeight.Medium)
        }
        Text(text = amount, color = TextWhite, fontWeight = FontWeight.Bold)
    }
}
