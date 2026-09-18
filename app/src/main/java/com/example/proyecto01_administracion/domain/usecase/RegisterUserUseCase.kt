package com.example.proyecto01_administracion.domain.usecase

import com.example.proyecto01_administracion.domain.model.RegisterUserRequest
import com.example.proyecto01_administracion.domain.model.User
import com.example.proyecto01_administracion.domain.model.UserRole
import com.example.proyecto01_administracion.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(
        request: RegisterUserRequest
    ): Result<User> {

        val name = request.name.trim()
        val identification = request.identification.trim()
        val email = request.email.trim().lowercase()
        val password = request.password
        val phone = request.phone?.trim()?.takeIf { it.isNotBlank() }
        val licenseNumber =
            request.licenseNumber?.trim()?.takeIf { it.isNotBlank() }

        if (name.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "El nombre no puede estar vacío."
                )
            )
        }

        if (identification.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "La identificación no puede estar vacía."
                )
            )
        }

        if (!isValidEmail(email)) {
            return Result.failure(
                IllegalArgumentException(
                    "El correo electrónico no es válido."
                )
            )
        }

        if (password.length < 6) {
            return Result.failure(
                IllegalArgumentException(
                    "La contraseña debe tener al menos 6 caracteres."
                )
            )
        }

        if (request.role == UserRole.NONE) {
            return Result.failure(
                IllegalArgumentException(
                    "Debe seleccionar un rol válido."
                )
            )
        }

        val normalizedRequest = request.copy(
            name = name,
            identification = identification,
            email = email,
            phone = phone,
            licenseNumber = licenseNumber
        )

        return authRepository.registerUser(normalizedRequest)
    }

    private fun isValidEmail(email: String): Boolean {
        return EMAIL_REGEX.matches(email)
    }

    companion object {
        private val EMAIL_REGEX =
            Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
    }
}