package com.example.proyecto01_administracion.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.proyecto01_administracion.data.local.entity.LegalDocumentEntity
import com.example.proyecto01_administracion.data.local.entity.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface LegalDocumentDao {

    @Upsert
    suspend fun upsert(document: LegalDocumentEntity)

    @Query("""
        SELECT * FROM legal_documents
        WHERE vehicleId = :vehicleId
        ORDER BY expirationDate ASC
    """)
    fun observeByVehicle(
        vehicleId: String
    ): Flow<List<LegalDocumentEntity>>

    @Query("""
        SELECT * FROM legal_documents
        WHERE expirationDate BETWEEN :startDate AND :endDate
        ORDER BY expirationDate ASC
    """)
    suspend fun getExpiringBetween(
        startDate: Long,
        endDate: Long
    ): List<LegalDocumentEntity>

    @Query("""
        SELECT * FROM legal_documents
        WHERE syncStatus = :status
    """)
    suspend fun getBySyncStatus(
        status: SyncStatus = SyncStatus.PENDING
    ): List<LegalDocumentEntity>

    @Query("""
        UPDATE legal_documents
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