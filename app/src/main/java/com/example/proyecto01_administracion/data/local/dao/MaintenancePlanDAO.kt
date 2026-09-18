package com.example.proyecto01_administracion.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.proyecto01_administracion.data.local.entity.MaintenancePlanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MaintenancePlanDao {

    @Upsert
    suspend fun upsert(plan: MaintenancePlanEntity)

    @Upsert
    suspend fun upsertAll(plans: List<MaintenancePlanEntity>)

    @Query("""
        SELECT * FROM maintenance_plans
        WHERE vehicleClassification = :classification
    """)
    fun observeByClassification(
        classification: String
    ): Flow<List<MaintenancePlanEntity>>

    @Query("""
        SELECT * FROM maintenance_plans
        WHERE vehicleClassification = :classification
        AND categoryId = :categoryId
        LIMIT 1
    """)
    suspend fun getPlan(
        classification: String,
        categoryId: String
    ): MaintenancePlanEntity?
}