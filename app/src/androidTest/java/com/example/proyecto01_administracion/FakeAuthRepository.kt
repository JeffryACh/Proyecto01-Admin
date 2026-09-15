package com.example.proyecto01_administracion

class FakeAuthRepository : AuthRepository {

    // Lista temporal que simula los documentos de usuarios en Firestore
    private val registeredUsers = mutableListOf<User>()

    // Función auxiliar exclusiva para que QA inyecte datos de prueba
    fun seedUser(user: User) {
        registeredUsers.add(user)
    }

    override fun signIn(email: String, password: String): Result<User> {
        val user = registeredUsers.find { it.email == email }

        return when {
            user == null -> Result.failure(Exception("Usuario no encontrado"))
            user.passwordHash != password -> Result.failure(Exception("Contraseña incorrecta"))
            else -> Result.success(user)
        }
    }
}