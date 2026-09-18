package com.example.proyecto01_administracion.domain.model

data class RegisterUserRequest(
    val name: String,
    val identification: String,
    val email: String,
    val password: String,
    val phone: String? = null,
    val role: UserRole,
    val licenseNumber: String? = null
)