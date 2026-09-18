package com.example.proyecto01_administracion.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.proyecto01_administracion.data.local.entity.MaintenanceEntity
import com.example.proyecto01_administracion.data.local.entity.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface MaintenanceDao {

    @Upsert
    suspend fun upsert(maintenance: MaintenanceEntity)

    @Query("""
        SELECT * FROM maintenances
        WHERE id = :id
        LIMIT 1
    """)
    suspend fun getById(
        id: String
    ): MaintenanceEntity?

    @Query("""
        SELECT * FROM maintenances
        WHERE vehicleId = :vehicleId
        ORDER BY date DESC
    """)
    fun observeByVehicle(
        vehicleId: String
    ): Flow<List<MaintenanceEntity>>

    @Query("""
        SELECT * FROM maintenances
        WHERE vehicleId = :vehicleId
        AND categoryId = :categoryId
        ORDER BY date DESC
        LIMIT 1
    """)
    suspend fun getLatestByCategory(
        vehicleId: String,
        categoryId: String
    ): MaintenanceEntity?

    @Query("""
        SELECT * FROM maintenances
        WHERE syncStatus = :status
        ORDER BY date ASC
    """)
    suspend fun getBySyncStatus(
        status: SyncStatus = SyncStatus.PENDING
    ): List<MaintenanceEntity>

    @Query("""
        UPDATE maintenances
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
        SELECT SUM(cost)
        FROM maintenances
        WHERE date BETWEEN :startDate AND :endDate
    """)
    suspend fun getTotalCostBetween(
        startDate: Long,
        endDate: Long
    ): Double?

    @Query("""
        SELECT SUM(cost)
        FROM maintenances
        WHERE vehicleId = :vehicleId
        AND date BETWEEN :startDate AND :endDate
    """)
    suspend fun getVehicleTotalCostBetween(
        vehicleId: String,
        startDate: Long,
        endDate: Long
    ): Double?
}