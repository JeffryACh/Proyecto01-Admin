package com.example.proyecto01_administracion.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.proyecto01_administracion.data.local.entity.AlertEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {

    @Upsert
    suspend fun upsert(alert: AlertEntity)

    @Upsert
    suspend fun upsertAll(alerts: List<AlertEntity>)

    @Query("""
        SELECT * FROM alerts
        WHERE id = :id
        LIMIT 1
    """)
    suspend fun getById(id: String): AlertEntity?

    @Query("""
        SELECT * FROM alerts
        WHERE vehicleId = :vehicleId
        ORDER BY createdAt DESC
    """)
    fun observeByVehicle(
        vehicleId: String
    ): Flow<List<AlertEntity>>
}