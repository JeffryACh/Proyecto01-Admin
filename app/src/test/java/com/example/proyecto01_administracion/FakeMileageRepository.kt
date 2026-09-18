package com.example.proyecto01_administracion

import com.example.proyecto01_administracion.domain.repository.MileageRepository

class FakeMileageRepository : MileageRepository {
    private val records = mutableListOf<Long>()

    // Simula la base de datos devolviendo el último registro
    override fun getLatestOdometer(): Long? {
        return records.maxOrNull()
    }

    // Simula el guardado en la nube
    override fun register(odometerValue: Long): Result<Unit> {
        records.add(odometerValue)
        return Result.success(Unit)
    }
}