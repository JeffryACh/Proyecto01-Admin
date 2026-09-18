package com.example.proyecto01_administracion.domain.repository

import com.example.proyecto01_administracion.domain.model.RegisterUserRequest
import com.example.proyecto01_administracion.domain.model.User

interface AuthRepository {
    suspend fun signIn(
        email: String,
        password: String
    ): Result<User>

    suspend fun registerUser(
        request: RegisterUserRequest
    ): Result<User>
}