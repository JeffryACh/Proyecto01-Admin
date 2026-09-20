package com.example.proyecto01_administracion.ui.fleet

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto01_administracion.domain.models.User
import com.example.proyecto01_administracion.domain.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class UserFormUiState(
    val id: String = "",
    val nombre: String = "",
    val cedula: String = "",
    val correo: String = "",
    val telefono: String = "",
    val rol_id: String = "",
    val numero_licencia: String? = null,
    val estado: String = "Activo",
    val foto_url: String? = null,
    val isEdit: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val validationErrors: Map<String, String> = emptyMap()
)

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UserFormUiState())
    val uiState: StateFlow<UserFormUiState> = _uiState.asStateFlow()

    fun loadUser(userId: String?) {
        if (userId.isNullOrBlank()) {
            _uiState.update { it.copy(isEdit = false) }
            return
        }
        _uiState.update { it.copy(isEdit = true, isLoading = true, error = null) }
        viewModelScope.launch {
            userRepository.getUserById(userId)
                .catch { e -> _uiState.update { it.copy(isLoading = false, error = e.message ?: "No se pudo cargar el usuario") } }
                .collect { user ->
                    if (user == null) _uiState.update { it.copy(isLoading = false, error = "Usuario no encontrado") }
                    else _uiState.update {
                        it.copy(
                            id = user.id, nombre = user.nombre, cedula = user.cedula, correo = user.correo,
                            telefono = user.telefono, rol_id = user.rol_id, numero_licencia = user.numero_licencia,
                            estado = user.estado, foto_url = user.foto_url, isEdit = true, isLoading = false, error = null
                        )
                    }
                }
        }
    }

    fun onNombreChange(v: String) = _uiState.update { it.copy(nombre = v, validationErrors = it.validationErrors - "nombre") }
    fun onCedulaChange(v: String) = _uiState.update { it.copy(cedula = v, validationErrors = it.validationErrors - "cedula") }
    fun onCorreoChange(v: String) = _uiState.update { it.copy(correo = v, validationErrors = it.validationErrors - "correo") }
    fun onTelefonoChange(v: String) = _uiState.update { it.copy(telefono = v) }
    fun onRolChange(v: String) = _uiState.update { it.copy(rol_id = v, validationErrors = it.validationErrors - "rol") }
    fun onLicenciaChange(v: String) = _uiState.update { it.copy(numero_licencia = v.ifBlank { null }, validationErrors = it.validationErrors - "licencia") }
    fun onEstadoChange(v: String) = _uiState.update { it.copy(estado = v) }

    fun saveUser() {
        val s = _uiState.value
        val errors = mutableMapOf<String, String>()

        if (s.nombre.trim().isBlank()) errors["nombre"] = "El nombre es obligatorio"
        if (s.cedula.trim().isBlank()) errors["cedula"] = "La cédula es obligatoria"
        
        val email = s.correo.trim()
        if (email.isBlank()) {
            errors["correo"] = "El correo es obligatorio"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errors["correo"] = "Ingresa un correo válido"
        }

        if (s.rol_id.isBlank()) errors["rol"] = "El rol es obligatorio"
        
        if (s.rol_id == "DRIVER" && s.numero_licencia.isNullOrBlank()) {
            errors["licencia"] = "La licencia es obligatoria para conductores"
        }

        if (errors.isNotEmpty()) {
            _uiState.update { it.copy(validationErrors = errors, error = "Por favor, corrige los errores en el formulario") }
            return
        }

        val user = User(
            id = s.id.ifBlank { UUID.randomUUID().toString() },
            nombre = s.nombre.trim(),
            cedula = s.cedula.trim(),
            correo = email,
            password = "default",
            telefono = s.telefono.trim(),
            rol_id = s.rol_id,
            numero_licencia = if (s.rol_id == "DRIVER") s.numero_licencia?.trim() else null,
            estado = s.estado,
            foto_url = s.foto_url
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, isSuccess = false) }
            userRepository.saveUser(user).fold(
                onSuccess = { _uiState.update { it.copy(id = user.id, isLoading = false, isSuccess = true) } },
                onFailure = { e -> _uiState.update { it.copy(isLoading = false, error = e.message ?: "No se pudo guardar el usuario") } }
            )
        }
    }
}
