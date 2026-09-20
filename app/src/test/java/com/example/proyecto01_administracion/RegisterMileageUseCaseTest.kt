package com.example.proyecto01_administracion

import com.example.proyecto01_administracion.domain.models.MileageRecord
import com.example.proyecto01_administracion.domain.usecase.RegisterMileageUseCase
import com.google.firebase.Timestamp
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.Date

class RegisterMileageUseCaseTest {

    private lateinit var fakeRepository:
            FakeMileageRepository

    private lateinit var useCase:
            RegisterMileageUseCase

    @Before
    fun setup() {
        fakeRepository = FakeMileageRepository()
        useCase = RegisterMileageUseCase(
            fakeRepository
        )
    }

    @Test
    fun kilometraje_negativo_retorna_fallo() = runTest {
        val result = useCase(
            createRecord(-50L)
        )

        assertTrue(result.isFailure)

        assertEquals(
            "El kilometraje no puede ser negativo",
            result.exceptionOrNull()?.message
        )

        assertTrue(fakeRepository.records.isEmpty())
    }

    @Test
    fun kilometraje_menor_al_anterior_retorna_fallo() =
        runTest {
            fakeRepository.seedMileage(10_000L)

            val result = useCase(
                createRecord(9_500L)
            )

            assertTrue(result.isFailure)

            assertEquals(
                "El kilometraje debe ser mayor a 10000 km",
                result.exceptionOrNull()?.message
            )

            assertEquals(
                1,
                fakeRepository.records.size
            )
        }

    @Test
    fun kilometraje_igual_al_anterior_retorna_fallo() =
        runTest {
            fakeRepository.seedMileage(10_000L)

            val result = useCase(
                createRecord(10_000L)
            )

            assertTrue(result.isFailure)

            assertEquals(
                1,
                fakeRepository.records.size
            )
        }

    @Test
    fun kilometraje_mayor_guarda_el_registro() =
        runTest {
            fakeRepository.seedMileage(10_000L)

            val result = useCase(
                createRecord(10_500L)
            )

            assertTrue(result.isSuccess)

            assertEquals(
                2,
                fakeRepository.records.size
            )

            assertEquals(
                10_500L,
                fakeRepository.records.last().kilometraje
            )
        }

    private fun createRecord(
        kilometraje: Long,
        vehicleId: String = "vehicle-test",
        userId: String = "user-test"
    ): MileageRecord {
        return MileageRecord(
            id = "record-$kilometraje",
            vehiculo_id = vehicleId,
            usuario_id = userId,
            kilometraje = kilometraje,
            fecha = Timestamp(Date(0L))
        )
    }
}