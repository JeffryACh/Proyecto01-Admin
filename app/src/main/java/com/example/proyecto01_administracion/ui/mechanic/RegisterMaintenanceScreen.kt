package com.example.proyecto01_administracion.ui.mechanic

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.proyecto01_administracion.ui.dashboard.FormTextField
import com.example.proyecto01_administracion.ui.theme.*
import com.example.proyecto01_administracion.ui.dashboard.AppChoiceChip
import androidx.compose.ui.platform.LocalContext
import android.Manifest
import com.example.proyecto01_administracion.ui.mechanic.components.CameraCapture

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterMaintenanceScreen(
    plate: String,
    viewModel: MaintenanceViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(plate) { viewModel.setVehicleFromPlate(plate) }

    var showCamera by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Mantenimiento", color = MaterialTheme.colorScheme.onSurface) },
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Vehículo Seleccionado", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(plate, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                }
            }

            Text("Tipo de Mantenimiento", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AppChoiceChip(selected = uiState.tipo == "Preventivo", label = "Preventivo", onClick = { viewModel.onTipoChange("Preventivo") })
                AppChoiceChip(selected = uiState.tipo == "Correctivo", label = "Correctivo", onClick = { viewModel.onTipoChange("Correctivo") })
            }

            var expanded by remember { mutableStateOf(false) }
            val selectedCategory = uiState.categorias.find { it.id == uiState.categoriaId }
            
            Column {
                Text("Categoría", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 8.dp))
                Box {
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = uiState.categorias.isNotEmpty() && !uiState.isLoading,
                        border = if (uiState.validationErrors.containsKey("categoria")) 
                            BorderStroke(1.dp, MaterialTheme.colorScheme.error) 
                        else 
                            BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Text(
                            selectedCategory?.nombre ?: if (uiState.categorias.isEmpty() && !uiState.isLoading)
                                "No hay categorías disponibles" else "Seleccionar categoría",
                            color = if (uiState.validationErrors.containsKey("categoria")) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        uiState.categorias.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.nombre) },
                                onClick = {
                                    viewModel.onCategoriaChange(category.id)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                uiState.validationErrors["categoria"]?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }

            FormTextField(
                value = uiState.taller,
                onValueChange = viewModel::onTallerChange,
                label = "Taller",
                placeholder = "Nombre del taller",
                enabled = !uiState.isLoading
            )

            FormTextField(
                value = uiState.kilometraje,
                onValueChange = viewModel::onKilometrajeChange,
                label = "Kilometraje",
                placeholder = "0",
                enabled = !uiState.isLoading,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = uiState.validationErrors.containsKey("kilometraje"),
                supportingText = uiState.validationErrors["kilometraje"]?.let { { Text(it) } }
            )

            FormTextField(
                value = uiState.costo,
                onValueChange = viewModel::onCostoChange,
                label = "Costo",
                placeholder = "0.00",
                enabled = !uiState.isLoading,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = uiState.validationErrors.containsKey("costo"),
                supportingText = uiState.validationErrors["costo"]?.let { { Text(it) } }
            )

            FormTextField(
                value = uiState.descripcion,
                onValueChange = viewModel::onDescripcionChange,
                label = "Descripción",
                placeholder = "Detalle del trabajo",
                enabled = !uiState.isLoading
            )

            Text("Evidencia Fotográfica", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                item {
                    Surface(
                        modifier = Modifier
                            .size(100.dp)
                            .clickable {
                                showCamera = true
                            },
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddAPhoto,
                            contentDescription = "Agregar foto",
                            modifier = Modifier.padding(32.dp),
                            tint = AccentBlue
                        )
                    }
                }
                
                items(uiState.evidencias) { uri ->
                    var thumbnailError by remember(uri) { mutableStateOf(false) }
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        if (thumbnailError) {
                            Icon(
                                imageVector = Icons.Default.BrokenImage,
                                contentDescription = "No se pudo cargar la miniatura",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(32.dp)
                            )
                        } else {
                            AsyncImage(
                                model = uri,
                                contentDescription = "Evidencia fotográfica",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                                onError = { thumbnailError = true }
                            )
                        }
                        IconButton(
                            onClick = { viewModel.removeEvidence(uri) },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(4.dp)
                                .size(24.dp)
                                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Eliminar", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            if(showCamera){
                CameraCapture(
                    context = LocalContext.current,

                    onPhotoCaptured = { uri ->

                        viewModel.addEvidence(uri)

                        showCamera = false
                    }
                )
            }

            if (uiState.error != null) {
                Text(text = uiState.error!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = viewModel::saveMaintenance,
                modifier = Modifier.fillMaxWidth().height(56.dp).padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Finalizar Registro", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
