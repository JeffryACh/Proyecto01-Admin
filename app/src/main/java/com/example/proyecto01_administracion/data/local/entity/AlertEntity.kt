package com.example.proyecto01_administracion.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "alerts",
    foreignKeys = [
        ForeignKey(
            entity = VehicleEntity::class,
            parentColumns = ["id"],
            childColumns = ["vehicleId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MaintenanceEntity::class,
            parentColumns = ["id"],
            childColumns = ["maintenanceId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = LegalDocumentEntity::class,
            parentColumns = ["id"],
            childColumns = ["documentId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["vehicleId"]),
        Index(value = ["maintenanceId"]),
        Index(value = ["documentId"])
    ]
)
data class AlertEntity(
    @PrimaryKey
    val id: String,
    val vehicleId: String,
    val maintenanceId: String? = null,
    val documentId: String? = null,
    val severity: String,
    val title: String,
    val message: String,
    val createdAt: Long
)