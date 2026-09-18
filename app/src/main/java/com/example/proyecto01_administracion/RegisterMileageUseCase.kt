package com.example.proyecto01_administracion

import com.example.proyecto01_administracion.domain.repository.MileageRepository

class RegisterMileageUseCase(
    private val repository: MileageRepository
) {
    operator fun invoke(newOdometer: Long): Result<Unit> {
        // Regla 1: No puede ser negativo
        if (newOdometer < 0) {
            return Result.failure(IllegalArgumentException("El kilometraje no puede ser negativo"))
        }

        // Regla 2: Debe ser mayor al anterior
        val previous = repository.getLatestOdometer()
        if (previous != null && newOdometer <= previous) {
            return Result.failure(IllegalArgumentException("El kilometraje debe ser mayor al último registrado"))
        }

        return repository.register(newOdometer)
    }
}