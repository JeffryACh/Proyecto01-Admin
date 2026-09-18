package com.example.proyecto01_administracion.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.proyecto01_administracion.data.local.entity.MaintenanceEvidenceEntity
import com.example.proyecto01_administracion.data.local.entity.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface MaintenanceEvidenceDao {

    @Upsert
    suspend fun upsert(evidence: MaintenanceEvidenceEntity)

    @Upsert
    suspend fun upsertAll(
        evidence: List<MaintenanceEvidenceEntity>
    )

    @Query("""
        SELECT * FROM maintenance_evidence
        WHERE maintenanceId = :maintenanceId
        ORDER BY `order` ASC
    """)
    fun observeByMaintenance(
        maintenanceId: String
    ): Flow<List<MaintenanceEvidenceEntity>>

    @Query("""
        SELECT * FROM maintenance_evidence
        WHERE syncStatus = :status
        ORDER BY updatedAt ASC
    """)
    suspend fun getBySyncStatus(
        status: SyncStatus = SyncStatus.PENDING
    ): List<MaintenanceEvidenceEntity>

    @Query("""
        UPDATE maintenance_evidence
        SET remoteUrl = :remoteUrl,
            syncStatus = :status,
            updatedAt = :updatedAt
        WHERE id = :id
    """)
    suspend fun markUploaded(
        id: String,
        remoteUrl: String,
        status: SyncStatus = SyncStatus.SYNCED,
        updatedAt: Long = System.currentTimeMillis()
    )

    @Query("""
        DELETE FROM maintenance_evidence
        WHERE id = :id
    """)
    suspend fun deleteById(id: String)
}