package com.example.proyecto01_administracion

import com.example.proyecto01_administracion.domain.model.AccountStatus
import com.example.proyecto01_administracion.domain.model.User
import com.example.proyecto01_administracion.domain.model.UserRole
import com.example.proyecto01_administracion.domain.usecase.RegisterMileageUseCase
import com.example.proyecto01_administracion.domain.usecase.SignInUseCase
import com.example.proyecto01_administracion.domain.models.MileageRecord
import com.google.firebase.Timestamp
import java.util.Date
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.test.runTest

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
            createRecord(kilometraje = -50L)
        )

        assertTrue(result.isFailure)

        assertEquals(
            "El kilometraje no puede ser negativo",
            result.exceptionOrNull()?.message
        )

        assertTrue(fakeRepository.records.isEmpty())
    }

    @Test
    fun vehiculo_vacio_retorna_fallo() = runTest {
        val result = useCase(
            createRecord(
                kilometraje = 10_500L,
                vehicleId = ""
            )
        )

        assertTrue(result.isFailure)

        assertEquals(
            "Debe indicar el vehículo",
            result.exceptionOrNull()?.message
        )

        assertTrue(fakeRepository.records.isEmpty())
    }

    @Test
    fun usuario_vacio_retorna_fallo() = runTest {
        val result = useCase(
            createRecord(
                kilometraje = 10_500L,
                userId = ""
            )
        )

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

        val result = useCase(record)

        assertTrue(result.isSuccess)
        assertEquals(1, fakeRepository.records.size)
        assertEquals(
            10_500L,
            fakeRepository.records.first().kilometraje
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
        )
    }

    @Test
    fun login_exitoso_retorna_el_usuario_y_su_rol_correcto() = runTest {
        val result = signInUseCase("encargado@transandina.com", "123456")

        assertTrue(result.isSuccess)
        assertEquals(UserRole.FLEET_MANAGER, result.getOrNull()?.role)
    }

    @Test
    fun login_con_contrasena_incorrecta_falla() = runTest {
        val result = signInUseCase("encargado@transandina.com", "clave-equivocada")

        assertTrue(result.isFailure)
        assertEquals("Contraseña incorrecta", result.exceptionOrNull()?.message)
    }

    @Test
    fun login_de_usuario_suspendido_bloquea_el_acceso() = runTest {
        // Intentamos entrar con el conductor que está suspendido
        val result = signInUseCase("conductor@transandina.com", "clave123")

        assertTrue(result.isFailure)
        assertEquals("Cuenta suspendida. Contacte al administrador.", result.exceptionOrNull()?.message)
        assertTrue(result.exceptionOrNull() is SecurityException)
    }

    @Test
    fun campos_vacios_son_rechazados_antes_de_consultar_al_repositorio() = runTest {
        val result = signInUseCase("", "")

        assertTrue(result.isFailure)
        assertEquals("El correo y contraseña no pueden estar vacíos", result.exceptionOrNull()?.message)
    }
}