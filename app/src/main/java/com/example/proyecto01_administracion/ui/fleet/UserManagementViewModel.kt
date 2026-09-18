package com.example.proyecto01_administracion.ui.fleet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto01_administracion.domain.models.User
import com.example.proyecto01_administracion.domain.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserManagementUiState(
    val users: List<User> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class UserManagementViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UserManagementUiState(isLoading = true))
    val uiState: StateFlow<UserManagementUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.getUsers()
                .catch { error -> _uiState.update { it.copy(isLoading = false, error = error.message ?: "No se pudieron cargar los usuarios") } }
                .collect { users -> _uiState.update { it.copy(users = users, isLoading = false, error = null) } }
        }
    }

    fun onSearchQueryChange(query: String) = _uiState.update { it.copy(searchQuery = query) }
}
