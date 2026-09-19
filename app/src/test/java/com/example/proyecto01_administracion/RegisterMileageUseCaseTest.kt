package com.example.proyecto01_administracion

import com.example.proyecto01_administracion.domain.models.MileageRecord
import com.example.proyecto01_administracion.domain.usecase.RegisterMileageUseCase
<<<<<<< HEAD
import com.google.firebase.Timestamp
import kotlinx.coroutines.test.runTest
=======
import com.example.proyecto01_administracion.domain.usecase.SignInUseCase
import com.example.proyecto01_administracion.domain.models.MileageRecord
import com.google.firebase.Timestamp
import java.util.Date
<<<<<<< Updated upstream
=======
>>>>>>> fd2f5ff22034617b78024429c695b16ea267dd33
>>>>>>> Stashed changes
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RegisterMileageUseCaseTest {
<<<<<<< Updated upstream
=======
<<<<<<< HEAD
    private lateinit var fakeRepository: FakeMileageRepository
    private lateinit var useCase: RegisterMileageUseCase
=======
>>>>>>> Stashed changes

    private lateinit var fakeRepository:
            FakeMileageRepository

    private lateinit var useCase:
            RegisterMileageUseCase
<<<<<<< Updated upstream
=======
>>>>>>> fd2f5ff22034617b78024429c695b16ea267dd33
>>>>>>> Stashed changes

    @Before
    fun setup() {
        fakeRepository = FakeMileageRepository()
        useCase = RegisterMileageUseCase(
            fakeRepository
        )
<<<<<<< Updated upstream
    }

    @Test
=======
    }

    // Función auxiliar para crear objetos válidos para la prueba
    private fun createTestRecord(km: Long): MileageRecord {
        return MileageRecord(
            id = java.util.UUID.randomUUID().toString(),
            vehiculo_id = "vehiculo-1",
            usuario_id = "user-1",
            kilometraje = km,
            // Fecha actual
            fecha = com.google.firebase.Timestamp(java.util.Date(System.currentTimeMillis()))
        )
    }

    @Test
<<<<<<< HEAD
    fun odometro_negativo_retorna_fallo() = runTest { // <- Añadir runTest
        val record = createTestRecord(-50L) // <- Usar el modelo completo
        val result = useCase(record)
=======
>>>>>>> Stashed changes
    fun kilometraje_negativo_retorna_fallo() = runTest {
        val result = useCase(
            createRecord(kilometraje = -50L)
        )
<<<<<<< Updated upstream
=======
>>>>>>> fd2f5ff22034617b78024429c695b16ea267dd33
>>>>>>> Stashed changes

        assertTrue(result.isFailure)

        assertEquals(
            "El kilometraje no puede ser negativo",
            result.exceptionOrNull()?.message
        )

        assertTrue(fakeRepository.records.isEmpty())
    }

    @Test
<<<<<<< Updated upstream
    fun vehiculo_vacio_retorna_fallo() = runTest {
        val result = useCase(
            createRecord(
                kilometraje = 10_500L,
                vehicleId = ""
            )
        )

=======
<<<<<<< HEAD
    fun odometro_menor_al_anterior_retorna_fallo() = runTest {
        // Arrange: Guardamos un kilometraje inicial de 10,000 en el repositorio falso
        fakeRepository.seedMileage(10000L)

        // Act: Intentamos guardar 9,500
        val record = createTestRecord(9500L)
        val result = useCase(record)

        // Assert: Comprobamos que el sistema lo rechaza
=======
    fun vehiculo_vacio_retorna_fallo() = runTest {
        val result = useCase(
            createRecord(
                kilometraje = 10_500L,
                vehicleId = ""
            )
        )

>>>>>>> fd2f5ff22034617b78024429c695b16ea267dd33
>>>>>>> Stashed changes
        assertTrue(result.isFailure)

        assertEquals(
            "Debe indicar el vehículo",
            result.exceptionOrNull()?.message
        )

        assertTrue(fakeRepository.records.isEmpty())
    }

    @Test
<<<<<<< Updated upstream
    fun usuario_vacio_retorna_fallo() = runTest {
        val result = useCase(
            createRecord(
                kilometraje = 10_500L,
                userId = ""
            )
        )

=======
<<<<<<< HEAD
    fun odometro_correcto_guarda_el_registro_exitosamente() = runTest {
        fakeRepository.seedMileage(10000L)

        // Intentamos guardar 10,500 (un valor válido)
        val record = createTestRecord(10500L)
=======
    fun usuario_vacio_retorna_fallo() = runTest {
        val result = useCase(
            createRecord(
                kilometraje = 10_500L,
                userId = ""
            )
        )

>>>>>>> Stashed changes
        assertTrue(result.isFailure)

        assertEquals(
            "Debe indicar el usuario",
            result.exceptionOrNull()?.message
        )

        assertTrue(fakeRepository.records.isEmpty())
    }

    @Test
    fun registro_valido_se_envia_al_repositorio() = runTest {
        val record = createRecord(
            kilometraje = 10_500L
        )

<<<<<<< Updated upstream
=======
>>>>>>> fd2f5ff22034617b78024429c695b16ea267dd33
>>>>>>> Stashed changes
        val result = useCase(record)

        assertTrue(result.isSuccess)
        assertEquals(1, fakeRepository.records.size)
        assertEquals(
            10_500L,
            fakeRepository.records.first().kilometraje
<<<<<<< Updated upstream
        )
    }

    private fun createRecord(
        kilometraje: Long,
        vehicleId: String = "vehicle-test",
        userId: String = "user-test"
    ): MileageRecord {
        return MileageRecord(
            id = "mileage-test",
            vehiculo_id = vehicleId,
            usuario_id = userId,
            kilometraje = kilometraje,
            fecha = Timestamp(Date(0L))
        )
    }
}

class SignInUseCaseTest {

    private lateinit var fakeAuthRepository: FakeAuthRepository
    private lateinit var signInUseCase: SignInUseCase

    @Before
    fun setup() {
        fakeAuthRepository = FakeAuthRepository()
        signInUseCase = SignInUseCase(fakeAuthRepository)

        // Preparar la base de datos falsa con usuarios de prueba
        fakeAuthRepository.seedUser(
            User(
                "1",
                "encargado@transandina.com",
                UserRole.FLEET_MANAGER,
                AccountStatus.ACTIVE
            ),
            password = "123456"
        )
        fakeAuthRepository.seedUser(
            User(
                "2",
                "conductor@transandina.com",
                UserRole.DRIVER,
                AccountStatus.SUSPENDED
            ),
            password = "clave123"
=======
>>>>>>> Stashed changes
        )
    }

    private fun createRecord(
        kilometraje: Long,
        vehicleId: String = "vehicle-test",
        userId: String = "user-test"
    ): MileageRecord {
        return MileageRecord(
            id = "mileage-test",
            vehiculo_id = vehicleId,
            usuario_id = userId,
            kilometraje = kilometraje,
            fecha = Timestamp(Date(0L))
        )
    }
}