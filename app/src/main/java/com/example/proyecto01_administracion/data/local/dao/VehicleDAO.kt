package com.example.proyecto01_administracion.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.proyecto01_administracion.data.local.entity.SyncStatus
import com.example.proyecto01_administracion.data.local.entity.VehicleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleDao {

    @Upsert
    suspend fun upsert(vehicle: VehicleEntity)

    @Upsert
    suspend fun upsertAll(vehicles: List<VehicleEntity>)

    @Query("SELECT * FROM vehicles WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): VehicleEntity?

    @Query("SELECT * FROM vehicles WHERE plate = :plate LIMIT 1")
    suspend fun getByPlate(plate: String): VehicleEntity?

    @Query("SELECT * FROM vehicles ORDER BY plate ASC")
    fun observeAll(): Flow<List<VehicleEntity>>

    @Query("SELECT * FROM vehicles WHERE status = :status ORDER BY plate ASC")
    fun observeByStatus(status: String): Flow<List<VehicleEntity>>

    @Query("SELECT * FROM vehicles WHERE syncStatus = :status")
    suspend fun getBySyncStatus(
        status: SyncStatus = SyncStatus.PENDING
    ): List<VehicleEntity>

    @Query("""
        UPDATE vehicles
        SET syncStatus = :status,
            updatedAt = :updatedAt
        WHERE id = :id
    """)
    suspend fun updateSyncStatus(
        id: String,
        status: SyncStatus,
        updatedAt: Long = System.currentTimeMillis()
    )

    @Query("""
        UPDATE vehicles
        SET currentMileage = :mileage,
            syncStatus = :syncStatus,
            updatedAt = :updatedAt
        WHERE id = :vehicleId
    """)
    suspend fun updateMileage(
        vehicleId: String,
        mileage: Long,
        syncStatus: SyncStatus = SyncStatus.PENDING,
        updatedAt: Long = System.currentTimeMillis()
    )
}