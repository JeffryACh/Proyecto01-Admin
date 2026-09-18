package com.example.proyecto01_administracion.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto01_administracion.BuildConfig
import com.example.proyecto01_administracion.data.auth.SessionManager
import com.example.proyecto01_administracion.domain.models.User
import com.example.proyecto01_administracion.domain.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRecoveryEmailSent: Boolean = false,
    val profileUpdateSuccess: Boolean = false,
    val profileUpdateError: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
    val currentUser = sessionManager.currentUser

    fun onEmailChange(email: String) = _uiState.update { it.copy(email = email, error = null, isRecoveryEmailSent = false) }
    fun onPasswordChange(password: String) = _uiState.update { it.copy(password = password, error = null) }

    fun login() {
        val state = _uiState.value
        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(error = "El correo y contraseña son obligatorios") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            if (BuildConfig.DEBUG) {
                val debugUser = checkDebugCredentials(state.email.trim(), state.password)
                if (debugUser != null) {
                    userRepository.saveUser(debugUser).fold(
                        onSuccess = {
                            sessionManager.setCurrentUser(debugUser)
                            _uiState.update { it.copy(isLoading = false) }
                        },
                        onFailure = { e -> _uiState.update { it.copy(isLoading = false, error = e.message ?: "No se pudo iniciar la sesión DEBUG") } }
                    )
                    return@launch
                }
            }
            // El repositorio AuthRepository definido en la nueva capa domain todavía no tiene implementación Firebase.
            _uiState.update { it.copy(isLoading = false, error = "Autenticación remota aún no implementada. Usa credenciales DEBUG.") }
        }
    }

    fun logout() = sessionManager.clearSession()

    fun sendRecoveryEmail() {
        if (_uiState.value.email.isBlank()) {
            _uiState.update { it.copy(error = "El correo es obligatorio") }
            return
        }
        _uiState.update { it.copy(error = "Recuperación de contraseña aún no conectada a AuthRepository") }
    }

    fun updateProfile(name: String, phone: String, cedula: String) {
        val current = sessionManager.currentUser.value ?: run {
            _uiState.update { it.copy(profileUpdateError = "No hay una sesión activa") }
            return
        }
        val updated = current.copy(nombre = name.trim(), telefono = phone.trim(), cedula = cedula.trim())
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, profileUpdateSuccess = false, profileUpdateError = null) }
            userRepository.saveUser(updated).fold(
                onSuccess = {
                    sessionManager.setCurrentUser(updated)
                    _uiState.update { it.copy(isLoading = false, profileUpdateSuccess = true) }
                },
                onFailure = { e -> _uiState.update { it.copy(isLoading = false, profileUpdateError = e.message ?: "No se pudo guardar el perfil") } }
            )
        }
    }

    fun consumeProfileUpdateSuccess() = _uiState.update { it.copy(profileUpdateSuccess = false) }
    fun resetState() = _uiState.update { LoginUiState() }

    private fun checkDebugCredentials(email: String, password: String): User? = when {
        email == "driver@debug.local" && password == "driver123" -> User(id="debug_driver", nombre="Conductor Debug", correo=email, rol_id="DRIVER")
        email == "mechanic@debug.local" && password == "mechanic123" -> User(id="debug_mechanic", nombre="Mecánico Debug", correo=email, rol_id="MECHANIC")
        email == "fleet@debug.local" && password == "fleet123" -> User(id="debug_fleet", nombre="Encargado Debug", correo=email, rol_id="FLEET_MANAGER")
        else -> null
    }
}
