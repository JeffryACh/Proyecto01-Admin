package com.example.proyecto01_administracion.domain.repository

interface MileageRepository {
    fun getLatestOdometer(): Long?
    fun register(odometerValue: Long): Result<Unit>
}