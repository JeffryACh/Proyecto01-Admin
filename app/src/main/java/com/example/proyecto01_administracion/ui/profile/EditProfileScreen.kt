package com.example.proyecto01_administracion.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proyecto01_administracion.ui.dashboard.FormTextField
import com.example.proyecto01_administracion.ui.login.AuthViewModel
import com.example.proyecto01_administracion.ui.theme.AccentBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()
    val authUiState by authViewModel.uiState.collectAsStateWithLifecycle()
    
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var cedula by remember { mutableStateOf("") }

    LaunchedEffect(currentUser) {
        currentUser?.let {
            name = it.nombre
            email = it.correo
            phone = it.telefono
            cedula = it.cedula
        }
    }

    LaunchedEffect(authUiState.profileUpdateSuccess) {
        if (authUiState.profileUpdateSuccess) {
            authViewModel.consumeProfileUpdateSuccess()
            onSave()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil", color = MaterialTheme.colorScheme.onSurface) },
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
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            Box(contentAlignment = Alignment.BottomEnd) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        tint = AccentBlue
                    )
                }
                Surface(
                    shape = CircleShape,
                    color = AccentBlue,
                    modifier = Modifier.size(32.dp),
                    shadowElevation = 4.dp
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Cambiar foto",
                        tint = Color.White,
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            FormTextField(value = name, onValueChange = { name = it }, label = "Nombre completo")
            FormTextField(value = email, onValueChange = { email = it }, label = "Correo electrónico", enabled = false)
            FormTextField(value = phone, onValueChange = { phone = it }, label = "Teléfono")
            FormTextField(value = cedula, onValueChange = { cedula = it }, label = "Cédula")
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = { authViewModel.updateProfile(name, phone, cedula) },
                modifier = Modifier.fillMaxWidth().height(56.dp).padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = !authUiState.isLoading
            ) {
                if (authUiState.isLoading) CircularProgressIndicator(modifier = Modifier.size(22.dp))
                else Text("Guardar Cambios", fontWeight = FontWeight.Bold)
            }
            authUiState.profileUpdateError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        }
    }
}
