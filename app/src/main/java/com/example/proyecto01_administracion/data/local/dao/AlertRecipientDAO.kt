package com.example.proyecto01_administracion.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.proyecto01_administracion.data.local.entity.AlertRecipientEntity
import com.example.proyecto01_administracion.data.local.entity.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertRecipientDao {

    @Upsert
    suspend fun upsert(recipient: AlertRecipientEntity)

    @Query("""
        SELECT * FROM alert_recipients
        WHERE userId = :userId
    """)
    fun observeByUser(
        userId: String
    ): Flow<List<AlertRecipientEntity>>

    @Query("""
        SELECT * FROM alert_recipients
        WHERE userId = :userId
        AND isRead = 0
    """)
    fun observeUnread(
        userId: String
    ): Flow<List<AlertRecipientEntity>>

    @Query("""
        UPDATE alert_recipients
        SET isRead = 1,
            readAt = :readAt,
            syncStatus = :syncStatus,
            updatedAt = :updatedAt
        WHERE alertId = :alertId
        AND userId = :userId
    """)
    suspend fun markAsRead(
        alertId: String,
        userId: String,
        readAt: Long = System.currentTimeMillis(),
        syncStatus: SyncStatus = SyncStatus.PENDING,
        updatedAt: Long = System.currentTimeMillis()
    )

    @Query("""
        SELECT * FROM alert_recipients
        WHERE syncStatus = :status
    """)
    suspend fun getBySyncStatus(
        status: SyncStatus = SyncStatus.PENDING
    ): List<AlertRecipientEntity>
}