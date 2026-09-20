package com.example.proyecto01_administracion.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
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
import com.example.proyecto01_administracion.data.local.entity.AlertEntity
import com.example.proyecto01_administracion.data.local.entity.AlertRecipientEntity
import com.example.proyecto01_administracion.data.local.entity.LegalDocumentEntity
import com.example.proyecto01_administracion.data.local.entity.MaintenanceCategoryEntity
import com.example.proyecto01_administracion.data.local.entity.MaintenanceEntity
import com.example.proyecto01_administracion.data.local.entity.MaintenanceEvidenceEntity
import com.example.proyecto01_administracion.data.local.entity.MaintenancePlanEntity
import com.example.proyecto01_administracion.data.local.entity.MileageRecordEntity
import com.example.proyecto01_administracion.data.local.entity.RoleEntity
import com.example.proyecto01_administracion.data.local.entity.UserEntity
import com.example.proyecto01_administracion.data.local.entity.VehicleAssignmentEntity
import com.example.proyecto01_administracion.data.local.entity.VehicleEntity

@Database(
    entities = [
        RoleEntity::class,
        UserEntity::class,
        VehicleEntity::class,
        VehicleAssignmentEntity::class,
        MileageRecordEntity::class,
        MaintenanceCategoryEntity::class,
        MaintenanceEntity::class,
        MaintenanceEvidenceEntity::class,
        LegalDocumentEntity::class,
        MaintenancePlanEntity::class,
        AlertEntity::class,
        AlertRecipientEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun roleDao(): RoleDao
    abstract fun userDao(): UserDao
    abstract fun vehicleDao(): VehicleDao
    abstract fun vehicleAssignmentDao(): VehicleAssignmentDao
    abstract fun mileageRecordDao(): MileageRecordDao
    abstract fun maintenanceCategoryDao(): MaintenanceCategoryDao
    abstract fun maintenanceDao(): MaintenanceDao
    abstract fun maintenanceEvidenceDao(): MaintenanceEvidenceDao
    abstract fun legalDocumentDao(): LegalDocumentDao
    abstract fun maintenancePlanDao(): MaintenancePlanDao
    abstract fun alertDao(): AlertDao
    abstract fun alertRecipientDao(): AlertRecipientDao
}