package com.example.proyecto01_administracion

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

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
    fun `odometro negativo retorna fallo`() {
        val result = useCase(-50L)

        assertTrue(result.isFailure)
        assertEquals("El kilometraje no puede ser negativo", result.exceptionOrNull()?.message)
    }

    @Test
    fun `odometro menor al anterior retorna fallo`() {
        // Arrange: Guardamos un kilometraje inicial de 10,000
        fakeRepository.register(10000L)

        // Act: Intentamos guardar 9,500
        val result = useCase(9500L)

        // Assert: Comprobamos que el sistema lo rechaza
        assertTrue(result.isFailure)
        assertEquals("El kilometraje debe ser mayor al último registrado", result.exceptionOrNull()?.message)
    }

    @Test
    fun `odometro correcto guarda el registro exitosamente`() {
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
            User("1", "encargado@transandina.com", "123456", UserRole.FLEET_MANAGER, AccountStatus.ACTIVE)
        )
        fakeAuthRepository.seedUser(
            User("2", "conductor@transandina.com", "clave123", UserRole.DRIVER, AccountStatus.SUSPENDED)
        )
    }

    @Test
    fun `login exitoso retorna el usuario y su rol correcto`() {
        val result = signInUseCase("encargado@transandina.com", "123456")

        assertTrue(result.isSuccess)
        assertEquals(UserRole.FLEET_MANAGER, result.getOrNull()?.role)
    }

    @Test
    fun `login con contrasena incorrecta falla`() {
        val result = signInUseCase("encargado@transandina.com", "clave-equivocada")

        assertTrue(result.isFailure)
        assertEquals("Contraseña incorrecta", result.exceptionOrNull()?.message)
    }

    @Test
    fun `login de usuario suspendido bloquea el acceso`() {
        // Intentamos entrar con el conductor que está suspendido
        val result = signInUseCase("conductor@transandina.com", "clave123")

        assertTrue(result.isFailure)
        assertEquals("Cuenta suspendida. Contacte al administrador.", result.exceptionOrNull()?.message)
        assertTrue(result.exceptionOrNull() is SecurityException)
    }

    @Test
    fun `campos vacios son rechazados antes de consultar al repositorio`() {
        val result = signInUseCase("", "")

        assertTrue(result.isFailure)
        assertEquals("El correo y contraseña no pueden estar vacíos", result.exceptionOrNull()?.message)
    }
}