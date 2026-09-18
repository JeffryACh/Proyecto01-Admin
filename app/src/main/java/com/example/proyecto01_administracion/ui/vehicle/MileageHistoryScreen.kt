package com.example.proyecto01_administracion.ui.vehicle

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proyecto01_administracion.domain.models.MileageRecord
import com.example.proyecto01_administracion.ui.dashboard.VehicleSummary
import com.example.proyecto01_administracion.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MileageHistoryScreen(
    viewModel: MileageViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial de kilometraje", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
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
        } else if (uiState.history.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text(
                    text = "Aún no hay registros de kilometraje.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
            ) {
                item {
                    uiState.vehicle?.let { vehicle ->
                        VehicleSummary(model = "${vehicle.marca} ${vehicle.modelo}", plate = vehicle.placa)
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text("Kilometraje actual", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${uiState.vehicle?.kilometraje_actual ?: 0} km", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            
                            val lastDate = uiState.history.firstOrNull()?.fecha?.toDate()?.let { dateFormat.format(it) } ?: "N/A"
                            Text("Último registro: $lastDate · Registros: ${uiState.history.size}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            if (uiState.history.size >= 2) {
                                MileageLineChart(
                                    records = uiState.history.reversed(),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(150.dp)
                                )
                            } else {
                                Box(modifier = Modifier.fillMaxWidth().height(150.dp), contentAlignment = Alignment.Center) {
                                    Text("Datos insuficientes para la gráfica", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Registros Recientes",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                itemsIndexed(uiState.history) { index, record ->
                    val nextRecord = if (index + 1 < uiState.history.size) uiState.history[index + 1] else null
                    val diff = nextRecord?.let { record.kilometraje - it.kilometraje }
                    MileageItem(
                        date = dateFormat.format(record.fecha.toDate()),
                        mileage = record.kilometraje,
                        difference = diff
                    )
                }
            }
        }
    }
}

@Composable
fun MileageLineChart(
    records: List<MileageRecord>,
    modifier: Modifier = Modifier
) {
    val accentColor = AccentBlue
    val gridColor = MaterialTheme.colorScheme.outlineVariant
    
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        
        val minMileage = records.minOf { it.kilometraje }.toFloat()
        val maxMileage = records.maxOf { it.kilometraje }.toFloat()
        val range = (maxMileage - minMileage).coerceAtLeast(1f)
        
        for (i in 0..3) {
            val y = height - (i * height / 3)
            drawLine(
                color = gridColor,
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1f
            )
        }
        
        val points = records.mapIndexed { index, record ->
            val x = if (records.size > 1) index * width / (records.size - 1) else width / 2
            val y = height - ((record.kilometraje - minMileage) / range * height)
            Offset(x, y)
        }
        
        val path = Path().apply {
            moveTo(points.first().x, points.first().y)
            for (i in 1 until points.size) {
                lineTo(points[i].x, points[i].y)
            }
        }
        
        drawPath(
            path = path,
            color = accentColor,
            style = Stroke(width = 3.dp.toPx())
        )
        
        points.forEach { point ->
            drawCircle(
                color = accentColor,
                radius = 4.dp.toPx(),
                center = point
            )
            drawCircle(
                color = Color.White,
                radius = 2.dp.toPx(),
                center = point
            )
        }
    }
}

@Composable
fun MileageItem(date: String, mileage: Long, difference: Long? = null) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(date, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("${String.format(Locale.US, "%,d", mileage)} km", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            }
            
            if (difference != null) {
                Surface(
                    color = AccentBlue.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "+${String.format(Locale.US, "%,d", difference)} km",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = AccentBlue,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
