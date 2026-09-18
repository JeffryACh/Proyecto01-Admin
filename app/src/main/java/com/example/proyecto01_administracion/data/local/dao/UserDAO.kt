package com.example.proyecto01_administracion.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.proyecto01_administracion.data.local.entity.SyncStatus
import com.example.proyecto01_administracion.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Upsert
    suspend fun upsert(user: UserEntity)

    @Upsert
    suspend fun upsertAll(users: List<UserEntity>)

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun observeById(id: String): Flow<UserEntity?>

    @Query("UPDATE users SET status = :status, syncStatus = :syncStatus, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateStatus(id: String, status: String, syncStatus: SyncStatus = SyncStatus.PENDING, updatedAt: Long = System.currentTimeMillis())

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users ORDER BY name ASC")
    fun observeAll(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE roleId = :roleId ORDER BY name ASC")
    fun observeByRole(roleId: String): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE syncStatus = :status")
    suspend fun getBySyncStatus(
        status: SyncStatus = SyncStatus.PENDING
    ): List<UserEntity>

    @Query("""
        UPDATE users
        SET syncStatus = :status,
            updatedAt = :updatedAt
        WHERE id = :id
    """)
    suspend fun updateSyncStatus(
        id: String,
        status: SyncStatus,
        updatedAt: Long = System.currentTimeMillis()
    )
}