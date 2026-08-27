package com.example.proyecto01_administracion.ui.vehicle

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto01_administracion.ui.theme.*

data class MileageRecord(
    val date: String,
    val mileage: Int,
    val difference: Int? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MileageHistoryScreen(
    onBack: () -> Unit
) {
    val records = listOf(
        MileageRecord("25 Ago 2026", 125430, 2430),
        MileageRecord("15 Ago 2026", 123000, 2100),
        MileageRecord("01 Ago 2026", 120900, 1850),
        MileageRecord("15 Jul 2026", 116800, 2300),
        MileageRecord("01 Jul 2026", 114500, 2700),
        MileageRecord("15 Jun 2026", 111800, 1800),
        MileageRecord("01 Jun 2026", 110000, null)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial de kilometraje", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundBlack)
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
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                // Vehicle Identification
                Column {
                    Text("Toyota Hilux", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("ABC-123", style = MaterialTheme.typography.bodyMedium, color = TextGrayLight)
                }
            }

            item {
                // Summary and Chart Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardGray),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Kilometraje actual", style = MaterialTheme.typography.labelSmall, color = TextGrayMedium)
                        Text("125,430 km", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Último registro: 25 Ago 2026 · Registros: 7", style = MaterialTheme.typography.bodySmall, color = TextGrayLight)
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Simple Chart
                        MileageLineChart(
                            records = records.reversed(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Jun", style = MaterialTheme.typography.labelSmall, color = TextGrayMedium)
                            Text("Jul", style = MaterialTheme.typography.labelSmall, color = TextGrayMedium)
                            Text("Ago", style = MaterialTheme.typography.labelSmall, color = TextGrayMedium)
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Registros",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            itemsIndexed(records) { index, record ->
                MileageItem(record = record)
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
    val gridColor = CardBorderGray
    
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        
        val minMileage = 110000f
        val maxMileage = 126000f
        val range = maxMileage - minMileage
        
        // Draw grid lines
        for (i in 0..3) {
            val y = height - (i * height / 3)
            drawLine(
                color = gridColor,
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1f
            )
        }
        
        if (records.size < 2) return@Canvas
        
        val points = records.mapIndexed { index, record ->
            val x = index * width / (records.size - 1)
            val y = height - ((record.mileage - minMileage) / range * height)
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
fun MileageItem(record: MileageRecord) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardGray),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(record.date, style = MaterialTheme.typography.labelSmall, color = TextGrayMedium)
                Text("${record.mileage} km", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = Color.White)
            }
            
            if (record.difference != null) {
                Surface(
                    color = AccentBlue.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "+${record.difference} km",
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
