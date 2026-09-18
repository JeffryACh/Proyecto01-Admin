package com.example.proyecto01_administracion.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "alert_recipients",
    foreignKeys = [
        ForeignKey(
            entity = AlertEntity::class,
            parentColumns = ["id"],
            childColumns = ["alertId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["alertId"]),
        Index(value = ["userId"]),
        Index(
            value = ["alertId", "userId"],
            unique = true
        )
    ]
)
data class AlertRecipientEntity(
    @PrimaryKey
    val id: String,
    val alertId: String,
    val userId: String,
    val isRead: Boolean = false,
    val readAt: Long? = null,
    val syncStatus: SyncStatus = SyncStatus.SYNCED,
    val updatedAt: Long = System.currentTimeMillis()
)