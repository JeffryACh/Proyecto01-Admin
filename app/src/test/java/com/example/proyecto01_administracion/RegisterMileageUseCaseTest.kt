package com.example.proyecto01_administracion

import com.example.proyecto01_administracion.domain.model.AccountStatus
import com.example.proyecto01_administracion.domain.model.User
import com.example.proyecto01_administracion.domain.model.UserRole
import com.example.proyecto01_administracion.domain.usecase.SignInUseCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.test.runTest

class RegisterMileageUseCaseTest {

    private lateinit var fakeRepository: FakeMileageRepository
    private lateinit var useCase: RegisterMileageUseCase

    @Before
    fun setup() {
        // Se ejecuta antes de cada test para limpiar la memoria
        fakeRepository = FakeMileageRepository()
        useCase = RegisterMileageUseCase(fakeRepository)
    }

    @Test
    fun odometro_negativo_retorna_fallo() {
        val result = useCase(-50L)

        assertTrue(result.isFailure)
        assertEquals("El kilometraje no puede ser negativo", result.exceptionOrNull()?.message)
    }

    @Test
    fun odometro_menor_al_anterior_retorna_fallo() {
        // Arrange: Guardamos un kilometraje inicial de 10,000
        fakeRepository.register(10000L)

        // Act: Intentamos guardar 9,500
        val result = useCase(9500L)

        // Assert: Comprobamos que el sistema lo rechaza
        assertTrue(result.isFailure)
        assertEquals("El kilometraje debe ser mayor al último registrado", result.exceptionOrNull()?.message)
    }

    @Test
    fun odometro_correcto_guarda_el_registro_exitosamente() {
        fakeRepository.register(10000L)

        // Intentamos guardar 10,500 (un valor válido)
        val result = useCase(10500L)

        assertTrue(result.isSuccess)
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