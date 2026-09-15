package com.example.proyecto01_administracion.ui.fleet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyecto01_administracion.ui.theme.*
import com.example.proyecto01_administracion.ui.dashboard.AppChoiceChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserFormScreen(
    userId: String? = null,
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    val isEdit = userId != null
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var cedula by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("Conductor") }
    var license by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEdit) "Editar Usuario" else "Crear Usuario", color = MaterialTheme.colorScheme.onSurface) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            FormTextField(value = name, onValueChange = { name = it }, label = "Nombre completo", placeholder = "Juan Pérez")
            FormTextField(value = cedula, onValueChange = { cedula = it }, label = "Cédula", enabled = !isEdit, placeholder = userId ?: "1-2345-6789")
            FormTextField(value = email, onValueChange = { email = it }, label = "Correo electrónico", placeholder = "ejemplo@transandina.com")
            FormTextField(value = phone, onValueChange = { phone = it }, label = "Teléfono", placeholder = "8888-8888")
            
            if (!isEdit) {
                Text("Rol", style = MaterialTheme.typography.labelMedium, color = TextGrayLight)
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    AppChoiceChip(selected = role == "Conductor", label = "Conductor", onClick = { role = "Conductor" })
                    AppChoiceChip(selected = role == "Mecánico", label = "Mecánico", onClick = { role = "Mecánico" })
                }
                
                FormTextField(value = password, onValueChange = { password = it }, label = "Contraseña", placeholder = "********")
            }

            if (role == "Conductor") {
                FormTextField(value = license, onValueChange = { license = it }, label = "Número de licencia", placeholder = "123456789")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onSave,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(if (isEdit) "Guardar cambios" else "Crear usuario", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    enabled: Boolean = true,
    placeholder: String = ""
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant) },
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContainerColor = MaterialTheme.colorScheme.background,
            focusedBorderColor = AccentBlue,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}
