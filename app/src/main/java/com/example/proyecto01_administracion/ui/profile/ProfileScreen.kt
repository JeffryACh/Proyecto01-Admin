package com.example.proyecto01_administracion.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.proyecto01_administracion.ui.login.AuthViewModel
import com.example.proyecto01_administracion.ui.theme.AccentBlue
import com.example.proyecto01_administracion.ui.theme.LocalTransAndinaColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onNavigateToEditProfile: () -> Unit,
    onLogout: () -> Unit
) {
    val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()
    val semanticColors = LocalTransAndinaColors.current
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", color = MaterialTheme.colorScheme.onSurface) },
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
        if (currentUser == null) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("No hay sesión activa.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            val user = currentUser!!
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        if (!user.foto_url.isNullOrBlank()) {
                            AsyncImage(
                                model = user.foto_url,
                                contentDescription = "Foto de perfil de ${user.nombre}",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(60.dp),
                                tint = AccentBlue
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = user.nombre,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Text(
                        text = when(user.rol_id) {
                            "DRIVER" -> "Conductor"
                            "MECHANIC" -> "Mecánico"
                            "FLEET_MANAGER" -> "Encargado de flota"
                            else -> user.rol_id
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val statusColor = if (user.estado == "Activo") semanticColors.statusGreen else semanticColors.statusRed
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = statusColor.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = user.estado,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            color = statusColor,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                }
                
                item {
                    ProfileInfoSection(
                        title = "Información Personal",
                        items = buildList {
                            add(ProfileInfoItem("Cédula", user.cedula.ifBlank { "No registrada" }, Icons.Default.Badge))
                            add(ProfileInfoItem("Correo", user.correo, Icons.Default.Email))
                            add(ProfileInfoItem("Teléfono", user.telefono.ifBlank { "No registrado" }, Icons.Default.Phone))
                        }
                    )
                }
                
                if (user.rol_id == "DRIVER") {
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        ProfileInfoSection(
                            title = "Licencia de Conducir",
                            items = listOf(
                                ProfileInfoItem("Número", user.numero_licencia ?: "No registrada", Icons.Default.Badge)
                            )
                        )
                    }
                }
                
                item {
                    Spacer(modifier = Modifier.height(48.dp))
                    
                    Button(
                        onClick = onNavigateToEditProfile,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentBlue)
                    ) {
                        Text("Editar Perfil", fontWeight = FontWeight.Bold)
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedButton(
                        onClick = {
                            authViewModel.logout()
                            onLogout()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = semanticColors.statusRed),
                        border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(brush = SolidColor(semanticColors.statusRed))
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cerrar Sesión", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

data class ProfileInfoItem(val label: String, val value: String, val icon: ImageVector)

@Composable
fun ProfileInfoSection(title: String, items: List<ProfileInfoItem>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            border = CardDefaults.outlinedCardBorder()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            tint = AccentBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = item.label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = item.value, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}
