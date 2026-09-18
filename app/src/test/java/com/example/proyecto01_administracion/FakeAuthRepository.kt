package com.example.proyecto01_administracion

import com.example.proyecto01_administracion.domain.model.User
import com.example.proyecto01_administracion.domain.repository.AuthRepository
import com.example.proyecto01_administracion.domain.model.RegisterUserRequest

class FakeAuthRepository : AuthRepository {
        // Lista temporal que simula los documentos de usuarios y contraseñas en Firestore
    private val registeredUsers = mutableListOf<User>()
    private val passwords = mutableMapOf<String, String>()

    // Función auxiliar exclusiva para que QA inyecte datos de prueba
    fun seedUser(user: User, password: String) {
        registeredUsers.add(user)
        passwords[user.email] = password
    }

    override suspend fun signIn(email: String, password: String): Result<User> {
        val user = registeredUsers.find { it.email == email }

        return when {
            user == null -> Result.failure(Exception("Usuario no encontrado"))
            passwords[email] != password -> Result.failure(Exception("Contraseña incorrecta"))
            else -> Result.success(user)
        }
    }

    override suspend fun registerUser(
        request: RegisterUserRequest
    ): Result<User> {
        return Result.success(
            User(
                id = "fake-user-id",
                email = request.email,
                role = request.role
            )
        )
    }

}