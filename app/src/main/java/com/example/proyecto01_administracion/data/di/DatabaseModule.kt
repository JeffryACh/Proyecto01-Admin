package com.example.proyecto01_administracion.data.di

import android.content.Context
import androidx.room.Room
import com.example.proyecto01_administracion.data.local.dao.AlertDao
import com.example.proyecto01_administracion.data.local.dao.AlertRecipientDao
import com.example.proyecto01_administracion.data.local.dao.LegalDocumentDao
import com.example.proyecto01_administracion.data.local.dao.MaintenanceCategoryDao
import com.example.proyecto01_administracion.data.local.dao.MaintenanceDao
import com.example.proyecto01_administracion.data.local.dao.MaintenanceEvidenceDao
import com.example.proyecto01_administracion.data.local.dao.MaintenancePlanDao
import com.example.proyecto01_administracion.data.local.dao.MileageRecordDao
import com.example.proyecto01_administracion.data.local.dao.RoleDao
import com.example.proyecto01_administracion.data.local.dao.UserDao
import com.example.proyecto01_administracion.data.local.dao.VehicleAssignmentDao
import com.example.proyecto01_administracion.data.local.dao.VehicleDao
import com.example.proyecto01_administracion.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "transandina_database"
        ).build()
    }

    @Provides
    fun provideRoleDao(
        database: AppDatabase
    ): RoleDao = database.roleDao()

    @Provides
    fun provideUserDao(
        database: AppDatabase
    ): UserDao = database.userDao()

    @Provides
    fun provideVehicleDao(
        database: AppDatabase
    ): VehicleDao = database.vehicleDao()

    @Provides
    fun provideVehicleAssignmentDao(
        database: AppDatabase
    ): VehicleAssignmentDao = database.vehicleAssignmentDao()

    @Provides
    fun provideMileageRecordDao(
        database: AppDatabase
    ): MileageRecordDao = database.mileageRecordDao()

    @Provides
    fun provideMaintenanceCategoryDao(
        database: AppDatabase
    ): MaintenanceCategoryDao = database.maintenanceCategoryDao()

    @Provides
    fun provideMaintenanceDao(
        database: AppDatabase
    ): MaintenanceDao = database.maintenanceDao()

    @Provides
    fun provideMaintenanceEvidenceDao(
        database: AppDatabase
    ): MaintenanceEvidenceDao = database.maintenanceEvidenceDao()

    @Provides
    fun provideLegalDocumentDao(
        database: AppDatabase
    ): LegalDocumentDao = database.legalDocumentDao()

    @Provides
    fun provideMaintenancePlanDao(
        database: AppDatabase
    ): MaintenancePlanDao = database.maintenancePlanDao()

    @Provides
    fun provideAlertDao(
        database: AppDatabase
    ): AlertDao = database.alertDao()

    @Provides
    fun provideAlertRecipientDao(
        database: AppDatabase
    ): AlertRecipientDao = database.alertRecipientDao()
}