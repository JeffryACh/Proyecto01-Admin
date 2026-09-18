package com.example.proyecto01_administracion.ui.fleet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proyecto01_administracion.domain.models.User
import com.example.proyecto01_administracion.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserManagementScreen(
    viewModel: UserManagementViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onNavigateToUserDetail: (String) -> Unit,
    onNavigateToCreateUser: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val filteredUsers = remember(uiState.users, uiState.searchQuery) {
        uiState.users.filter { user ->
            user.nombre.contains(uiState.searchQuery, ignoreCase = true) ||
            user.correo.contains(uiState.searchQuery, ignoreCase = true) ||
            user.cedula.contains(uiState.searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Usuarios", color = MaterialTheme.colorScheme.onSurface) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Listado de Usuarios", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text("Total: ${uiState.users.size}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                placeholder = { Text("Buscar por nombre, correo o cédula", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = AccentBlue) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedBorderColor = AccentBlue,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(modifier = Modifier.weight(1f)) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (filteredUsers.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No hay usuarios para mostrar.",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Los usuarios registrados aparecerán aquí.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(filteredUsers) { user ->
                            UserCard(user = user, onClick = { onNavigateToUserDetail(user.id) })
                        }
                    }
                }
                
                FloatingActionButton(
                    onClick = onNavigateToCreateUser,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 16.dp),
                    containerColor = AccentBlue,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Crear usuario")
                }
            }
        }
    }
}

@Composable
fun UserCard(user: User, onClick: () -> Unit) {
    val semanticColors = LocalTransAndinaColors.current
    val statusColor = if (user.estado == "Activo") semanticColors.statusGreen else semanticColors.statusRed

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(modifier = Modifier.size(48.dp).background(MaterialTheme.colorScheme.background, CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Person, contentDescription = null, tint = AccentBlue)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = user.nombre, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text(text = user.rol_id, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = user.correo, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Box(modifier = Modifier.size(8.dp).background(statusColor, CircleShape))
        }
    }
}
