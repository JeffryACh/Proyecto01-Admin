package com.example.proyecto01_administracion.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "vehicles",
    indices = [
        Index(value = ["plate"], unique = true)
    ]
)
data class VehicleEntity(
    @PrimaryKey
    val id: String,
    val plate: String,
    val brand: String,
    val model: String,
    val year: Int,
    val type: String,
    val classification: String,
    val capacity: Double,
    val currentMileage: Long,
    val status: String,
    val syncStatus: SyncStatus = SyncStatus.PENDING,
    val updatedAt: Long = System.currentTimeMillis()
)