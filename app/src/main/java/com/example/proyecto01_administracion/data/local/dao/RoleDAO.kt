package com.example.proyecto01_administracion.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.proyecto01_administracion.data.local.entity.RoleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoleDao {

    @Upsert
    suspend fun upsert(role: RoleEntity)
    @Upsert
    suspend fun upsertAll(roles: List<RoleEntity>)
    @Query("SELECT * FROM roles ORDER BY name ASC")
    fun observeAll(): Flow<List<RoleEntity>>
    @Query("SELECT * FROM roles WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): RoleEntity?
}