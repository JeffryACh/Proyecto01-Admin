package com.example.proyecto01_administracion.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "maintenance_categories")
data class MaintenanceCategoryEntity(

    @PrimaryKey
    val id: String,

    val name: String
)