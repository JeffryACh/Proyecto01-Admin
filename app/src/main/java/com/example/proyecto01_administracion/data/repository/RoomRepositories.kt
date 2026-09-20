package com.example.proyecto01_administracion.data.repository

import androidx.room.withTransaction
import com.example.proyecto01_administracion.data.local.dao.*
import com.example.proyecto01_administracion.data.local.database.AppDatabase
import com.example.proyecto01_administracion.data.local.entity.MaintenanceEvidenceEntity
import com.example.proyecto01_administracion.data.local.entity.RoleEntity
import com.example.proyecto01_administracion.domain.models.*
import com.example.proyecto01_administracion.domain.repositories.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomVehicleRepository @Inject constructor(
    private val vehicleDao: VehicleDao
) : VehicleRepository {
    override fun getVehicles(): Flow<List<Vehicle>> = vehicleDao.observeAll().map { list -> list.map { it.toDomain() } }
    override fun getVehicleById(id: String): Flow<Vehicle?> = vehicleDao.observeById(id).map { it?.toDomain() }
    override suspend fun findVehicleByPlate(plate: String): Vehicle? = vehicleDao.getByPlate(plate)?.toDomain()
    override suspend fun saveVehicle(vehicle: Vehicle): Result<Unit> = runCatching { vehicleDao.upsert(vehicle.toEntity()) }
    override suspend fun updateVehicleMileage(vehicleId: String, newMileage: Long): Result<Unit> = runCatching { vehicleDao.updateMileage(vehicleId, newMileage) }
}

@Singleton
class RoomUserRepository @Inject constructor(
    private val userDao: UserDao,
    private val roleDao: RoleDao
) : UserRepository {
    override fun getUsers(): Flow<List<User>> = userDao.observeAll().map { list -> list.map { it.toDomain() } }
    override fun getUserById(id: String): Flow<User?> = userDao.observeById(id).map { it?.toDomain() }
    override suspend fun saveUser(user: User): Result<Unit> = runCatching {
        roleDao.upsert(RoleEntity(id = user.rol_id, name = when (user.rol_id) {
            "DRIVER" -> "Conductor"
            "MECHANIC" -> "Mecánico"
            "FLEET_MANAGER" -> "Encargado de flota"
            else -> user.rol_id
        }))
        userDao.upsert(user.toEntity())
    }
    override fun getRoles(): Flow<List<Role>> = roleDao.observeAll().map { list -> list.map { it.toDomain() } }
}

@Singleton
class RoomMileageRepository @Inject constructor(
    private val database: AppDatabase,
    private val mileageDao: MileageRecordDao,
    private val vehicleDao: VehicleDao
) : MileageRepository {

    override fun getMileageHistory(
        vehicleId: String
    ): Flow<List<MileageRecord>> {
        return mileageDao.observeHistory(vehicleId).map { records ->
            records.map { it.toDomain() }
        }
    }

    override suspend fun getLastMileage(
        vehicleId: String
    ): Long? {
        val vehicleMileage =
            vehicleDao.getById(vehicleId)?.currentMileage

        val highestRecordedMileage =
            mileageDao.getHighestMileage(vehicleId)

        return listOfNotNull(
            vehicleMileage,
            highestRecordedMileage
        ).maxOrNull()
    }

    override suspend fun registerMileage(
        record: MileageRecord
    ): Result<Unit> {
        return try {
            database.withTransaction {

                require(record.vehiculo_id.isNotBlank()) {
                    "Debe indicar el vehículo"
                }

                require(record.usuario_id.isNotBlank()) {
                    "Debe indicar el usuario"
                }

                require(record.kilometraje >= 0) {
                    "El kilometraje no puede ser negativo"
                }

                val vehicle = requireNotNull(
                    vehicleDao.getById(record.vehiculo_id)
                ) {
                    "El vehículo no existe"
                }

                val highestRecordedMileage =
                    mileageDao.getHighestMileage(
                        record.vehiculo_id
                    )

                val previousMileage = maxOf(
                    vehicle.currentMileage,
                    highestRecordedMileage
                        ?: vehicle.currentMileage
                )

                require(
                    record.kilometraje > previousMileage
                ) {
                    "El kilometraje debe ser mayor a $previousMileage km"
                }

                mileageDao.upsert(
                    record.toEntity()
                )

                vehicleDao.updateMileage(
                    vehicleId = record.vehiculo_id,
                    mileage = record.kilometraje
                )
            }

            Result.success(Unit)
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}

@Singleton
class RoomMaintenanceRepository @Inject constructor(
    private val maintenanceDao: MaintenanceDao,
    private val categoryDao: MaintenanceCategoryDao,
    private val evidenceDao: MaintenanceEvidenceDao
) : MaintenanceRepository {
    override fun getAllMaintenances():
            Flow<List<Maintenance>> {
        return maintenanceDao.observeAll().map { records ->
            records.map { it.toDomain() }
        }
    }

    override fun getMaintenancesByVehicle(vehicleId: String): Flow<List<Maintenance>> =
        maintenanceDao.observeByVehicle(vehicleId).map { list -> list.map { it.toDomain() } }

    override suspend fun saveMaintenance(maintenance: Maintenance, evidences: List<String>): Result<Unit> = runCatching {
        maintenanceDao.upsert(maintenance.toEntity())
        if (evidences.isNotEmpty()) {
            evidenceDao.upsertAll(evidences.mapIndexed { index, uri ->
                MaintenanceEvidenceEntity(
                    id = UUID.randomUUID().toString(),
                    maintenanceId = maintenance.id,
                    localUri = uri,
                    order = index
                )
            })
        }
    }

    override fun getCategories(): Flow<List<MaintenanceCategory>> =
        categoryDao.observeAll().map { list -> list.map { it.toDomain() } }
}

@Singleton
class RoomAlertRepository @Inject constructor(
    private val alertDao: AlertDao,
    private val recipientDao: AlertRecipientDao
) : AlertRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAlertsForUser(userId: String): Flow<List<Alert>> =
        recipientDao.observeByUser(userId).mapLatest { recipients ->
            recipients.mapNotNull { recipient -> alertDao.getById(recipient.alertId)?.toDomain() }
                .sortedByDescending { it.fecha_creacion.toDate().time }
        }

    override suspend fun markAlertAsRead(alertId: String, userId: String): Result<Unit> = runCatching {
        recipientDao.markAsRead(alertId = alertId, userId = userId)
    }
}

@Singleton
class RoomDocumentRepository @Inject constructor(
    private val documentDao: LegalDocumentDao
) : DocumentRepository {
    override fun getDocumentsByVehicle(vehicleId: String): Flow<List<LegalDocument>> =
        documentDao.observeByVehicle(vehicleId).map { list -> list.map { it.toDomain() } }

    override suspend fun saveDocument(document: LegalDocument): Result<Unit> = runCatching {
        documentDao.upsert(document.toEntity())
    }
}
