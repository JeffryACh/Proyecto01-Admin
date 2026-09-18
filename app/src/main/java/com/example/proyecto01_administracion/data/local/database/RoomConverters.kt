package com.example.proyecto01_administracion.data.local.database

import androidx.room.TypeConverter
import com.example.proyecto01_administracion.data.local.entity.SyncStatus

class RoomConverters {

    @TypeConverter
    fun fromSyncStatus(status: SyncStatus): String {
        return status.name
    }

    @TypeConverter
    fun toSyncStatus(value: String): SyncStatus {
        return SyncStatus.valueOf(value)
    }
}
