package com.example.proyecto01_administracion.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "maintenance_evidence",
    foreignKeys = [
        ForeignKey(
            entity = MaintenanceEntity::class,
            parentColumns = ["id"],
            childColumns = ["maintenanceId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["maintenanceId"]),
        Index(
            value = ["maintenanceId", "order"],
            unique = true
        )
    ]
)
data class MaintenanceEvidenceEntity(
    @PrimaryKey
    val id: String,
    val maintenanceId: String,
    /*
     * URI/ruta de la fotografía dentro del teléfono.
     */
    val localUri: String? = null,
    /*
     * URL de Firebase Storage después de sincronizar.
     */
    val remoteUrl: String? = null,
    val order: Int,
    val syncStatus: SyncStatus = SyncStatus.PENDING,
    val updatedAt: Long = System.currentTimeMillis()
)