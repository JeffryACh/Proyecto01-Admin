package com.example.proyecto01_administracion

import com.example.proyecto01_administracion.domain.models.MileageRecord
import com.example.proyecto01_administracion.domain.repositories.MileageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeMileageRepository : MileageRepository {

    private val savedRecords =
        mutableListOf<MileageRecord>()

    val records: List<MileageRecord>
        get() = savedRecords.toList()

    override fun getMileageHistory(
        vehicleId: String
    ): Flow<List<MileageRecord>> {
        return flowOf(
            savedRecords.filter {
                it.vehiculo_id == vehicleId
            }
        )
    }

    override suspend fun registerMileage(
        record: MileageRecord
    ): Result<Unit> {
        savedRecords.add(record)
        return Result.success(Unit)
    }
}