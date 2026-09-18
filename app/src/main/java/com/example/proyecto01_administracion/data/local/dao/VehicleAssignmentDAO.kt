package com.example.proyecto01_administracion.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.proyecto01_administracion.data.local.entity.SyncStatus
import com.example.proyecto01_administracion.data.local.entity.VehicleAssignmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleAssignmentDao {

    @Upsert
    suspend fun upsert(assignment: VehicleAssignmentEntity)

    @Query("""
        SELECT * FROM vehicle_assignments
        WHERE vehicleId = :vehicleId
        AND endDate IS NULL
        LIMIT 1
    """)
    suspend fun getActiveByVehicle(
        vehicleId: String
    ): VehicleAssignmentEntity?

    @Query("""
        SELECT * FROM vehicle_assignments
        WHERE userId = :userId
        AND endDate IS NULL
    """)
    fun observeActiveByUser(
        userId: String
    ): Flow<List<VehicleAssignmentEntity>>

    @Query("""
        SELECT * FROM vehicle_assignments
        WHERE syncStatus = :status
    """)
    suspend fun getBySyncStatus(
        status: SyncStatus = SyncStatus.PENDING
    ): List<VehicleAssignmentEntity>

    @Query("""
        UPDATE vehicle_assignments
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
        UPDATE vehicle_assignments
        SET endDate = :endDate, syncStatus = :syncStatus, updatedAt = :updatedAt
        WHERE vehicleId = :vehicleId AND endDate IS NULL
    """)
    suspend fun closeActiveByVehicle(
        vehicleId: String,
        endDate: Long = System.currentTimeMillis(),
        syncStatus: SyncStatus = SyncStatus.PENDING,
        updatedAt: Long = System.currentTimeMillis()
    )

    @Query("""
        UPDATE vehicle_assignments
        SET endDate = :endDate, syncStatus = :syncStatus, updatedAt = :updatedAt
        WHERE userId = :userId AND endDate IS NULL
    """)
    suspend fun closeActiveByUser(
        userId: String,
        endDate: Long = System.currentTimeMillis(),
        syncStatus: SyncStatus = SyncStatus.PENDING,
        updatedAt: Long = System.currentTimeMillis()
    )
}