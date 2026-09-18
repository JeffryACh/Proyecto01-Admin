package com.example.proyecto01_administracion.domain.model

// Los roles definidos para el proyecto
enum class UserRole {
    FLEET_MANAGER,
    MECHANIC,
    DRIVER,
    NONE
}

enum class AccountStatus {
    ACTIVE,
    SUSPENDED
}

// Modelo de usuario simulado
data class User(
    val id: String,
    val email: String,
    val role: UserRole,
    val status: AccountStatus = AccountStatus.ACTIVE
)