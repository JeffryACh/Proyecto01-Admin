package com.example.proyecto01_administracion.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto01_administracion.ui.theme.AccentBlue
import com.example.proyecto01_administracion.ui.theme.AccentPurple
import com.example.proyecto01_administracion.ui.theme.BackgroundBlack
import com.example.proyecto01_administracion.ui.theme.CardGray
import com.example.proyecto01_administracion.ui.theme.TextGrayLight
import com.example.proyecto01_administracion.ui.theme.TextWhite

@Composable
fun LoginScreen(
    onLoginAsDriver: () -> Unit,
    onLoginAsMechanic: () -> Unit,
    onLoginAsFleetManager: () -> Unit,
    onForgotPassword: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundBlack
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo / Icon
            Icon(
                imageVector = Icons.Default.LocalShipping,
                contentDescription = null,
                tint = AccentBlue,
                modifier = Modifier.size(80.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "TransAndina",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            
            Text(
                text = "Gestión de mantenimiento de flotilla",
                style = MaterialTheme.typography.bodyMedium,
                color = TextGrayLight
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Email Field
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Correo electrónico", color = TextGrayLight) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = AccentBlue) },
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
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Password Field
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Contraseña", color = TextGrayLight) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = AccentBlue) },
                visualTransformation = PasswordVisualTransformation(),
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
            
            TextButton(onClick = onForgotPassword) {
                Text(
                    text = "¿Olvidaste tu contraseña?",
                    color = AccentBlue,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = { /* TODO: implement real login */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentBlue)
            ) {
                Text("Iniciar sesión", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Test Access Section
            Text(
                text = "Acceso de prueba",
                style = MaterialTheme.typography.labelLarge,
                color = TextGrayLight,
                fontWeight = FontWeight.SemiBold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                TestAccessButton(
                    label = "Entrar como Conductor",
                    icon = Icons.Default.DirectionsCar,
                    onClick = onLoginAsDriver
                )
                TestAccessButton(
                    label = "Entrar como Mecánico",
                    icon = Icons.Default.PrecisionManufacturing,
                    onClick = onLoginAsMechanic
                )
                TestAccessButton(
                    label = "Entrar como Encargado de Flotilla",
                    icon = Icons.Default.LocalShipping,
                    onClick = onLoginAsFleetManager
                )
            }
        }
    }
}

@Composable
private fun TestAccessButton(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = TextWhite
        ),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = Brush.horizontalGradient(listOf(AccentBlue, AccentPurple))
        )
    ) {
        Row(
            modifier = Modifier.padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(20.dp))
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
