package com.example.proyecto01_administracion.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.proyecto01_administracion.data.local.entity.MaintenanceCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MaintenanceCategoryDao {

    @Upsert
    suspend fun upsert(category: MaintenanceCategoryEntity)

    @Upsert
    suspend fun upsertAll(categories: List<MaintenanceCategoryEntity>)

    @Query("""
        SELECT * FROM maintenance_categories
        ORDER BY name ASC
    """)
    fun observeAll(): Flow<List<MaintenanceCategoryEntity>>

    @Query("""
        SELECT * FROM maintenance_categories
        WHERE id = :id
        LIMIT 1
    """)
    suspend fun getById(
        id: String
    ): MaintenanceCategoryEntity?
}