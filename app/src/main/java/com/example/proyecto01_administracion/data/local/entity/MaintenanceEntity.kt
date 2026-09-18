package com.example.proyecto01_administracion.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "maintenances",
    foreignKeys = [
        ForeignKey(
            entity = VehicleEntity::class,
            parentColumns = ["id"],
            childColumns = ["vehicleId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MaintenanceCategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["registeredBy"],
            onDelete = ForeignKey.NO_ACTION
        )
    ],
    indices = [
        Index(value = ["vehicleId"]),
        Index(value = ["categoryId"]),
        Index(value = ["registeredBy"]),
        Index(value = ["vehicleId", "date"])
    ]
)
data class MaintenanceEntity(
    @PrimaryKey
    val id: String,
    val vehicleId: String,
    val type: String,
    val categoryId: String,
    val date: Long,
    val workshop: String? = null,
    val mileage: Long,
    val cost: Double,
    val description: String,
    val registeredBy: String,
    val status: String = "ACTIVE",
    val syncStatus: SyncStatus = SyncStatus.PENDING,
    val updatedAt: Long = System.currentTimeMillis()
)