package com.example.proyecto01_administracion.data.di

import com.example.proyecto01_administracion.data.repository.*
import com.example.proyecto01_administracion.domain.repositories.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton abstract fun bindVehicleRepository(impl: RoomVehicleRepository): VehicleRepository
    @Binds @Singleton abstract fun bindUserRepository(impl: RoomUserRepository): UserRepository
    @Binds @Singleton abstract fun bindMileageRepository(impl: RoomMileageRepository): MileageRepository
    @Binds @Singleton abstract fun bindMaintenanceRepository(impl: RoomMaintenanceRepository): MaintenanceRepository
    @Binds @Singleton abstract fun bindAlertRepository(impl: RoomAlertRepository): AlertRepository
    @Binds @Singleton abstract fun bindDocumentRepository(impl: RoomDocumentRepository): DocumentRepository
}
