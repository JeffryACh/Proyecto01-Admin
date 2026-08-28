package com.example.proyecto01_administracion.ui.mechanic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
fun MaintenanceDetailScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Mantenimiento", color = TextWhite) },
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
            contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
        ) {
            item {
                Text(
                    text = "Toyota Hilux (ABC-123)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AccentBlue
                )
                Text(
                    text = "Cambio de aceite y filtros",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardGray),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        DetailItem(label = "Tipo", value = "Preventivo", icon = Icons.Default.Build)
                        DetailItem(label = "Fecha", value = "15 de mayo, 2024", icon = Icons.Default.CalendarToday)
                        DetailItem(label = "Kilometraje", value = "120,860 km", icon = Icons.Default.Numbers)
                        DetailItem(label = "Taller", value = "Taller Central TransAndina", icon = Icons.Default.LocationOn)
                        DetailItem(label = "Mecánico", value = "Ricardo Alfaro", icon = Icons.Default.Person)
                        DetailItem(label = "Costo", value = "$125.00", icon = Icons.Default.Payments)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            item {
                Text(
                    text = "Descripción",
                    style = MaterialTheme.typography.titleSmall,
                    color = TextGrayLight,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Se realizó cambio de aceite sintético 5W-30, filtro de aceite y filtro de aire. Se revisaron niveles de líquidos y presión de llantas.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextWhite,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            item {
                Text(
                    text = "Evidencia fotográfica",
                    style = MaterialTheme.typography.titleSmall,
                    color = TextGrayLight,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    PhotoPlaceholder(modifier = Modifier.weight(1f))
                    PhotoPlaceholder(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun DetailItem(label: String, value: String, icon: ImageVector) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = AccentBlue, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = TextGrayLight)
            Text(text = value, style = MaterialTheme.typography.bodyLarge, color = TextWhite, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun PhotoPlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .aspectRatio(4f / 3f)
            .clip(RoundedCornerShape(16.dp))
            .background(CardGray),
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = Icons.Default.Image, contentDescription = null, tint = TextGrayLight, modifier = Modifier.size(32.dp))
    }
}
