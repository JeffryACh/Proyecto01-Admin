package com.example.proyecto01_administracion.domain.usecase

import com.example.proyecto01_administracion.domain.model.AccountStatus
import com.example.proyecto01_administracion.domain.model.User
import com.example.proyecto01_administracion.domain.repository.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<User> {

        val normalizedEmail = email.trim()

        if (normalizedEmail.isBlank() || password.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "El correo y contraseña no pueden estar vacíos"
                )
            )
        }

        val authResult = authRepository.signIn(
            email = normalizedEmail,
            password = password
        )

        return authResult.fold(
            onSuccess = { user ->

                if (user.status == AccountStatus.SUSPENDED) {
                    Result.failure(
                        SecurityException(
                            "Cuenta suspendida. Contacte al administrador."
                        )
                    )
                } else {
                    Result.success(user)
                }
            },

            onFailure = { error ->
                Result.failure(error)
            }
        )
    }
}