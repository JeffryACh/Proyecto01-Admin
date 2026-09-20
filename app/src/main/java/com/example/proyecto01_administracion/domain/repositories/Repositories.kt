package com.example.proyecto01_administracion.domain.repositories

import com.example.proyecto01_administracion.domain.models.*
import kotlinx.coroutines.flow.Flow

interface VehicleRepository {
    fun getVehicles(): Flow<List<Vehicle>>
    fun getVehicleById(id: String): Flow<Vehicle?>
    suspend fun findVehicleByPlate(plate: String): Vehicle?
    suspend fun saveVehicle(vehicle: Vehicle): Result<Unit>
    suspend fun updateVehicleMileage(vehicleId: String, newMileage: Long): Result<Unit>
}

interface MaintenanceRepository {

    fun getAllMaintenances(): Flow<List<Maintenance>>

    fun getMaintenancesByVehicle(
        vehicleId: String
    ): Flow<List<Maintenance>>

    suspend fun saveMaintenance(
        maintenance: Maintenance,
        evidences: List<String>
    ): Result<Unit>

    fun getCategories(): Flow<List<MaintenanceCategory>>
}

interface MileageRepository {

    fun getMileageHistory(
        vehicleId: String
    ): Flow<List<MileageRecord>>

    suspend fun getLastMileage(
        vehicleId: String
    ): Long?

    suspend fun registerMileage(
        record: MileageRecord
    ): Result<Unit>
}

interface AlertRepository {
    fun getAlertsForUser(userId: String): Flow<List<Alert>>
    suspend fun markAlertAsRead(alertId: String, userId: String): Result<Unit>
}

interface DocumentRepository {
    fun getDocumentsByVehicle(vehicleId: String): Flow<List<LegalDocument>>
    suspend fun saveDocument(document: LegalDocument): Result<Unit>
}

interface UserRepository {
    fun getUsers(): Flow<List<User>>
    fun getUserById(id: String): Flow<User?>
    suspend fun saveUser(user: User): Result<Unit>
    fun getRoles(): Flow<List<Role>>
}

interface AuthRepository {
    val currentUser: Flow<User?>
    suspend fun signIn(email: String, password: String): Result<User>
    suspend fun signOut()
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>
}
