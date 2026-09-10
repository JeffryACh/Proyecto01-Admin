package com.example.proyecto01_administracion.ui.vehicle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyecto01_administracion.ui.theme.*

data class VehicleDocument(
    val title: String,
    val expiryDate: String,
    val status: String,
    val statusColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleDocumentsScreen(
    onBack: () -> Unit
) {
    val documents = listOf(
        VehicleDocument("Marchamo 2026", "31 Dic 2026", "Vigente", StatusGreen),
        VehicleDocument("Revisión Técnica (IVE)", "15 Nov 2026", "Vigente", StatusGreen),
        VehicleDocument("Seguro Voluntario", "15 Sep 2026", "Próximo a vencer", StatusYellow),
        VehicleDocument("Título de Propiedad", "N/A", "Vigente", StatusGreen)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Documentos", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = MaterialTheme.colorScheme.onSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
        ) {
            items(documents) { document ->
                DocumentItemCard(document = document)
            }
        }
    }
}

@Composable
fun DocumentItemCard(document: VehicleDocument) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(AccentBlue.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Description, contentDescription = null, tint = AccentBlue)
            }
            
            Column(modifier = Modifier.weight(1f)) {
                Text(text = document.title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text(text = "Vence: ${document.expiryDate}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = document.status, color = document.statusColor, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            }
            
            IconButton(onClick = { /* Download */ }) {
                Icon(Icons.Default.FileDownload, contentDescription = "Descargar", tint = AccentBlue)
            }
        }
    }
}
