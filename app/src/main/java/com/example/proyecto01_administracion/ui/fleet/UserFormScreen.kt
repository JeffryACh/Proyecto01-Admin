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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proyecto01_administracion.ui.theme.*
import com.example.proyecto01_administracion.ui.dashboard.AppChoiceChip
import com.example.proyecto01_administracion.ui.dashboard.FormTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserFormScreen(
    userId: String? = null,
    viewModel: UserViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(userId) {
        viewModel.loadUser(userId)
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (uiState.isEdit) "Editar Usuario" else "Crear Usuario", color = MaterialTheme.colorScheme.onSurface) },
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

            FormTextField(
                value = uiState.nombre,
                onValueChange = viewModel::onNombreChange,
                label = "Nombre completo",
                placeholder = "Juan Pérez",
                enabled = !uiState.isLoading,
                isError = uiState.validationErrors.containsKey("nombre"),
                supportingText = uiState.validationErrors["nombre"]?.let { { Text(it) } }
            )

            FormTextField(
                value = uiState.cedula,
                onValueChange = viewModel::onCedulaChange,
                label = "Cédula",
                enabled = !uiState.isEdit && !uiState.isLoading,
                placeholder = "1-2345-6789",
                isError = uiState.validationErrors.containsKey("cedula"),
                supportingText = uiState.validationErrors["cedula"]?.let { { Text(it) } }
            )

            FormTextField(
                value = uiState.correo,
                onValueChange = viewModel::onCorreoChange,
                label = "Correo electrónico",
                placeholder = "ejemplo@transandina.com",
                enabled = !uiState.isLoading,
                isError = uiState.validationErrors.containsKey("correo"),
                supportingText = uiState.validationErrors["correo"]?.let { { Text(it) } }
            )

            FormTextField(
                value = uiState.telefono,
                onValueChange = viewModel::onTelefonoChange,
                label = "Teléfono",
                placeholder = "8888-8888",
                enabled = !uiState.isLoading
            )
            
            Text("Rol", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Column {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    AppChoiceChip(selected = uiState.rol_id == "DRIVER", label = "Conductor", onClick = { viewModel.onRolChange("DRIVER") })
                    AppChoiceChip(selected = uiState.rol_id == "MECHANIC", label = "Mecánico", onClick = { viewModel.onRolChange("MECHANIC") })
                    AppChoiceChip(selected = uiState.rol_id == "FLEET_MANAGER", label = "Encargado", onClick = { viewModel.onRolChange("FLEET_MANAGER") })
                }
                uiState.validationErrors["rol"]?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 16.dp, top = 4.dp))
                }
            }

            if (uiState.rol_id == "DRIVER") {
                FormTextField(
                    value = uiState.numero_licencia ?: "",
                    onValueChange = viewModel::onLicenciaChange,
                    label = "Número de licencia",
                    placeholder = "123456789",
                    enabled = !uiState.isLoading,
                    isError = uiState.validationErrors.containsKey("licencia"),
                    supportingText = uiState.validationErrors["licencia"]?.let { { Text(it) } }
                )
            }

            Text("Estado", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AppChoiceChip(selected = uiState.estado == "Activo", label = "Activo", onClick = { viewModel.onEstadoChange("Activo") })
                AppChoiceChip(selected = uiState.estado == "Inactivo", label = "Inactivo", onClick = { viewModel.onEstadoChange("Inactivo") })
            }

            if (uiState.error != null) {
                Text(
                    text = uiState.error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = viewModel::saveUser,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 16.dp),
                enabled = !uiState.isLoading,
                shape = RoundedCornerShape(16.dp)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(if (uiState.isEdit) "Guardar cambios" else "Crear usuario", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
