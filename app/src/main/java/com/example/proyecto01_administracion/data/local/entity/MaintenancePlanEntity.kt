package com.example.proyecto01_administracion.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "maintenance_plans",
    foreignKeys = [
        ForeignKey(
            entity = MaintenanceCategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.NO_ACTION
        )
    ],
    indices = [
        Index(value = ["categoryId"])
    ]
)
data class MaintenancePlanEntity(
    @PrimaryKey
    val id: String,
    val vehicleClassification: String,
    val categoryId: String,
    val intervalKm: Int,
    val intervalDays: Int
)