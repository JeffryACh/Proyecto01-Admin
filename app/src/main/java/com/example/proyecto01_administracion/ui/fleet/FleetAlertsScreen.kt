package com.example.proyecto01_administracion.ui.fleet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto01_administracion.ui.theme.AccentBlue
import com.example.proyecto01_administracion.ui.theme.BackgroundBlack
import com.example.proyecto01_administracion.ui.theme.CardGray
import com.example.proyecto01_administracion.ui.theme.StatusRed
import com.example.proyecto01_administracion.ui.theme.StatusYellow
import com.example.proyecto01_administracion.ui.theme.TextGrayLight
import com.example.proyecto01_administracion.ui.theme.TextWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FleetAlertsScreen(
    onBack: () -> Unit
) {
    val alerts = listOf(
        FleetAlert("Urgente", "ABC-123: Cambio de frenos atrasado por 200km", StatusRed),
        FleetAlert("Urgente", "GHI-789: Revisión técnica vence mañana", StatusRed),
        FleetAlert("Próxima", "DEF-456: Mantenimiento preventivo en 500km", StatusYellow),
        FleetAlert("Próxima", "JKL-012: Seguro vence en 15 días", StatusYellow),
        FleetAlert("Informativa", "MNO-345: Nuevo registro de kilometraje", AccentBlue)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Alertas de Flota", color = TextWhite) },
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
        ) {
            items(alerts) { alert ->
                FleetAlertCard(alert = alert)
            }
        }
    }
}

data class FleetAlert(val type: String, val message: String, val color: Color)

@Composable
fun FleetAlertCard(alert: FleetAlert) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardGray),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(alert.color.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (alert.color == StatusRed) Icons.Default.Warning else Icons.Default.Info,
                    contentDescription = null,
                    tint = alert.color,
                    modifier = Modifier.size(24.dp)
                )
            }
            Column {
                Text(text = alert.type, color = alert.color, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = alert.message, color = TextWhite, style = MaterialTheme.typography.bodyMedium, lineHeight = 20.sp)
            }
        }
    }
}
