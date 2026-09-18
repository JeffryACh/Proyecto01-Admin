package com.example.proyecto01_administracion.ui.fleet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proyecto01_administracion.ui.profile.ProfileInfoItem
import com.example.proyecto01_administracion.ui.profile.ProfileInfoSection
import com.example.proyecto01_administracion.ui.theme.*
import com.example.proyecto01_administracion.ui.vehicle.BaseVehicleCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    userId: String,
    viewModel: UserDetailViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onReassignVehicle: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val semanticColors = LocalTransAndinaColors.current
    var showSuspendDialog by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        viewModel.loadUser(userId)
    }

    if (showSuspendDialog) {
        AlertDialog(
            onDismissRequest = { showSuspendDialog = false },
            title = { Text("Suspender cuenta", color = MaterialTheme.colorScheme.onSurface) },
            text = { Text("¿Deseas suspender esta cuenta?", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            confirmButton = {
                TextButton(onClick = { 
                    viewModel.suspendUser()
                    showSuspendDialog = false 
                }) {
                    Text("Confirmar", color = semanticColors.statusRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSuspendDialog = false }) {
                    Text("Cancelar", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Usuario", color = MaterialTheme.colorScheme.onSurface) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onEdit) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.onSurface)
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
        } else if (uiState.user == null) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("Usuario no encontrado", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            val user = uiState.user!!
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(modifier = Modifier.size(80.dp).background(MaterialTheme.colorScheme.surfaceVariant, CircleShape), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = AccentBlue, modifier = Modifier.size(48.dp))
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = user.nombre, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Text(text = when(user.rol_id) {
                            "DRIVER" -> "Conductor"
                            "MECHANIC" -> "Mecánico"
                            "FLEET_MANAGER" -> "Encargado de flota"
                            else -> user.rol_id
                        }, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(8.dp))
                        val statusColor = if (user.estado == "Activo") semanticColors.statusGreen else semanticColors.statusRed
                        Surface(shape = RoundedCornerShape(16.dp), color = statusColor.copy(alpha = 0.1f)) {
                            Text(text = user.estado, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), color = statusColor, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                
                item {
                    ProfileInfoSection(
                        title = "Información Personal",
                        items = buildList {
                            add(ProfileInfoItem("Nombre completo", user.nombre, Icons.Default.Person))
                            add(ProfileInfoItem("Cédula", user.cedula.ifBlank { "No registrada" }, Icons.Default.Person))
                            add(ProfileInfoItem("Correo", user.correo, Icons.Default.Person))
                            add(ProfileInfoItem("Teléfono", user.telefono.ifBlank { "No registrado" }, Icons.Default.Person))
                            if (user.rol_id == "DRIVER") {
                                add(ProfileInfoItem("Licencia", user.numero_licencia ?: "No registrada", Icons.Default.Person))
                            }
                        }
                    )
                }
                
                if (user.rol_id == "DRIVER") {
                    item {
                        BaseVehicleCard(title = "Vehículo Asignado") {
                            val vehicle = uiState.assignedVehicle
                            if (vehicle != null) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                        Box(modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.surfaceVariant, CircleShape), contentAlignment = Alignment.Center) {
                                            Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = AccentBlue)
                                        }
                                        Column {
                                            Text(text = "${vehicle.marca} ${vehicle.modelo}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                            Text(text = vehicle.placa, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    }
                                    TextButton(onClick = onReassignVehicle) {
                                        Text("Reasignar", color = AccentBlue)
                                    }
                                }
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Sin vehículo asignado.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    TextButton(onClick = onReassignVehicle) {
                                        Text("Asignar", color = AccentBlue)
                                    }
                                }
                            }
                        }
                    }
                }
                
                if (user.estado == "Activo") {
                    item {
                        OutlinedButton(
                            onClick = { showSuspendDialog = true },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = semanticColors.statusRed),
                            border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(brush = SolidColor(semanticColors.statusRed))
                        ) {
                            Icon(imageVector = Icons.Default.Block, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Suspender Usuario", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
