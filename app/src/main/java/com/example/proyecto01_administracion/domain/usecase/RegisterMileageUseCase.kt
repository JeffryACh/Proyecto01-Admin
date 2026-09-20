package com.example.proyecto01_administracion.domain.usecase

import com.example.proyecto01_administracion.domain.models.MileageRecord
import com.example.proyecto01_administracion.domain.repositories.MileageRepository
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class RegisterMileageUseCase @Inject constructor(
    private val repository: MileageRepository
) {

    suspend operator fun invoke(
        record: MileageRecord
    ): Result<Unit> {

        if (record.vehiculo_id.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "Debe indicar el vehículo"
                )
            )
        }

        if (record.usuario_id.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "Debe indicar el usuario"
                )
            )
        }

        if (record.kilometraje < 0) {
            return Result.failure(
                IllegalArgumentException(
                    "El kilometraje no puede ser negativo"
                )
            )
        }

        return try {
            val previousMileage =
                repository.getLastMileage(
                    record.vehiculo_id
                )

            if (
                previousMileage != null &&
                record.kilometraje <= previousMileage
            ) {
                Result.failure(
                    IllegalArgumentException(
                        "El kilometraje debe ser mayor a $previousMileage km"
                    )
                )
            } else {
                repository.registerMileage(record)
            }
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}