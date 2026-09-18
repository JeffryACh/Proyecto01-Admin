package com.example.proyecto01_administracion.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "roles")
data class RoleEntity(
    @PrimaryKey
    val id: String,
    val name: String
)

