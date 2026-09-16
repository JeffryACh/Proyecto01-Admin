package com.example.proyecto01_administracion

class SignInUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String, password: String): Result<User> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("El correo y contraseña no pueden estar vacíos"))
        }

        val authResult = authRepository.signIn(email, password)

        return authResult.fold(
            onSuccess = { user ->
                if (user.status == AccountStatus.SUSPENDED) {
                    Result.failure(SecurityException("Cuenta suspendida. Contacte al administrador."))
                } else {
                    Result.success(user) // Login exitoso
                }
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }
}