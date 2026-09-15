package com.example.proyecto01_administracion

// Los roles definidos para el proyecto
enum class UserRole {
    FLEET_MANAGER,
    MECHANIC,
    DRIVER
}

enum class AccountStatus {
    ACTIVE,
    SUSPENDED
}

// Modelo de usuario simulado
data class User(
    val id: String,
    val email: String,
    val passwordHash: String, // En la vida real Firebase oculta esto, aquí lo simulamos
    val role: UserRole,
    val status: AccountStatus = AccountStatus.ACTIVE
)

// Interfaz que tanto Firebase como nuestro Fake deben respetar
interface AuthRepository {
    fun signIn(email: String, password: String): Result<User>
}