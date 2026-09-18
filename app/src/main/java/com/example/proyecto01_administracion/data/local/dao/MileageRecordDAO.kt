package com.example.proyecto01_administracion.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.proyecto01_administracion.data.local.entity.MileageRecordEntity
import com.example.proyecto01_administracion.data.local.entity.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface MileageRecordDao {

    @Upsert
    suspend fun upsert(record: MileageRecordEntity)

    @Query("""
        SELECT * FROM mileage_records
        WHERE vehicleId = :vehicleId
        ORDER BY date DESC
    """)
    fun observeHistory(
        vehicleId: String
    ): Flow<List<MileageRecordEntity>>

    @Query("""
        SELECT * FROM mileage_records
        WHERE vehicleId = :vehicleId
        ORDER BY date DESC
        LIMIT 1
    """)
    suspend fun getLatest(
        vehicleId: String
    ): MileageRecordEntity?

    @Query("""
        SELECT MAX(mileage)
        FROM mileage_records
        WHERE vehicleId = :vehicleId
    """)
    suspend fun getHighestMileage(
        vehicleId: String
    ): Long?

    @Query("""
        SELECT * FROM mileage_records
        WHERE syncStatus = :status
        ORDER BY date ASC
    """)
    suspend fun getBySyncStatus(
        status: SyncStatus = SyncStatus.PENDING
    ): List<MileageRecordEntity>

    @Query("""
        UPDATE mileage_records
        SET syncStatus = :status,
            updatedAt = :updatedAt
        WHERE id = :id
    """)
    suspend fun updateSyncStatus(
        id: String,
        status: SyncStatus,
        updatedAt: Long = System.currentTimeMillis()
    )
}