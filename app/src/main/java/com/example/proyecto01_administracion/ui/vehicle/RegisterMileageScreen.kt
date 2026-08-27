package com.example.proyecto01_administracion.ui.vehicle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto01_administracion.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterMileageScreen(
    onBack: () -> Unit,
    onSuccess: () -> Unit = {}
) {
    var mileage by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("27 Ago 2026") }
    var showConfirmation by remember { mutableStateOf(false) }
    
    val lastMileage = 125430
    val isMileageValid = mileage.isNotEmpty() && (mileage.toIntOrNull() ?: 0) > lastMileage

    if (showConfirmation) {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                TextButton(onClick = { 
                    showConfirmation = false
                    onSuccess() 
                }) {
                    Text("OK", color = AccentBlue)
                }
            },
            title = { Text("Éxito", color = Color.White) },
            text = { Text("Kilometraje registrado correctamente", color = TextGrayLight) },
            containerColor = CardGray
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar kilometraje", fontWeight = FontWeight.Bold, color = Color.White) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Vehicle Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardGray),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Toyota Hilux", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextWhite)
                    Text("ABC-123", style = MaterialTheme.typography.bodyMedium, color = TextGrayLight)
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text("Último kilometraje registrado", style = MaterialTheme.typography.labelSmall, color = TextGrayMedium)
                    Text("${lastMileage} km", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = AccentBlue)
                }
            }

            // Date Field
            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Fecha") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null, tint = AccentBlue) },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CardGray,
                    unfocusedContainerColor = CardGray,
                    focusedBorderColor = AccentBlue,
                    unfocusedBorderColor = CardBorderGray,
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite,
                    focusedLabelColor = AccentBlue,
                    unfocusedLabelColor = TextGrayLight
                )
            )

            // Mileage Field
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = mileage,
                    onValueChange = { mileage = it },
                    label = { Text("Kilometraje actual") },
                    placeholder = { Text("Ingrese el kilometraje", color = TextGrayMedium) },
                    suffix = { Text("km", color = TextWhite) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardGray,
                        unfocusedContainerColor = CardGray,
                        focusedBorderColor = if (mileage.isNotEmpty() && !isMileageValid) StatusRed else AccentBlue,
                        unfocusedBorderColor = if (mileage.isNotEmpty() && !isMileageValid) StatusRed else CardBorderGray,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedLabelColor = if (mileage.isNotEmpty() && !isMileageValid) StatusRed else AccentBlue,
                        unfocusedLabelColor = TextGrayLight
                    )
                )
                
                Text(
                    text = "Último kilometraje registrado: ${lastMileage} km",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextGrayMedium
                )
                
                if (mileage.isNotEmpty() && !isMileageValid) {
                    Text(
                        text = "El kilometraje debe ser mayor al último registro.",
                        style = MaterialTheme.typography.labelSmall,
                        color = StatusRed
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { showConfirmation = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 16.dp),
                enabled = isMileageValid,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentBlue,
                    disabledContainerColor = CardGray
                )
            ) {
                Text("Registrar kilometraje", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}
