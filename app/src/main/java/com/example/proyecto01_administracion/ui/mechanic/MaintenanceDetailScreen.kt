package com.example.proyecto01_administracion.ui.mechanic

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.proyecto01_administracion.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaintenanceDetailScreen(
    maintenanceId: String = "",
    viewModel: MaintenanceDetailViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

    LaunchedEffect(maintenanceId) {
        if (maintenanceId.isNotEmpty()) {
            viewModel.loadMaintenance(maintenanceId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Mantenimiento", color = MaterialTheme.colorScheme.onSurface) },
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
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState.error != null) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text(uiState.error!!, color = MaterialTheme.colorScheme.error)
            }
        } else if (uiState.maintenance == null) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("Mantenimiento no encontrado", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            val maintenance = uiState.maintenance!!
            val vehicle = uiState.vehicle
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text("Vehículo", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(vehicle?.let { "${it.marca} ${it.modelo}" } ?: "No registrado", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                Text(vehicle?.placa ?: "No registrada", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Surface(shape = RoundedCornerShape(8.dp), color = StatusGreen.copy(alpha = 0.1f)) {
                                Text(text = "Registrado", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), color = StatusGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text("Detalles de la Operación", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(16.dp))

                DetailItem(label = "Fecha", value = dateFormat.format(maintenance.fecha.toDate()))
                DetailItem(label = "Tipo", value = maintenance.tipo)
                DetailItem(label = "Kilometraje", value = "${maintenance.kilometraje} km")
                DetailItem(label = "Taller", value = maintenance.taller)
                DetailItem(label = "Costo", value = "₡${maintenance.costo}")

                Spacer(modifier = Modifier.height(24.dp))

                Text("Descripción", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = maintenance.descripcion,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                Text("Evidencia fotográfica", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(12.dp))
                if (uiState.evidenceUris.isEmpty()) {
                    Text(
                        text = "No hay fotografías registradas.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    uiState.evidenceUris.forEach { uri ->
                        var imageError by remember(uri) { mutableStateOf(false) }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (imageError) {
                                Text("No se pudo cargar la fotografía", color = MaterialTheme.colorScheme.error)
                            } else {
                                AsyncImage(
                                    model = uri,
                                    contentDescription = "Evidencia de mantenimiento",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop,
                                    onError = { imageError = true }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
    }
}
