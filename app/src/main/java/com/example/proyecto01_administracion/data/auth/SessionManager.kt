package com.example.proyecto01_administracion.data.auth

import com.example.proyecto01_administracion.domain.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    fun setCurrentUser(user: User?) {
        _currentUser.value = user
    }

    fun clearSession() {
        _currentUser.value = null
    }
}
