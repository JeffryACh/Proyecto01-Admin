package com.example.proyecto01_administracion

import com.example.proyecto01_administracion.domain.models.MileageRecord
import com.example.proyecto01_administracion.domain.repositories.MileageRepository
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.Date

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

    override suspend fun getLastMileage(
        vehicleId: String
    ): Long? {
        return savedRecords
            .asSequence()
            .filter {
                it.vehiculo_id == vehicleId
            }
            .map {
                it.kilometraje
            }
            .maxOrNull()
    }

    override suspend fun registerMileage(
        record: MileageRecord
    ): Result<Unit> {
        savedRecords.add(record)
        return Result.success(Unit)
    }

    fun seedMileage(
        odometerValue: Long,
        vehicleId: String = "vehicle-test"
    ) {
        savedRecords.add(
            MileageRecord(
                id = "seed-${savedRecords.size}",
                vehiculo_id = vehicleId,
                usuario_id = "user-test",
                kilometraje = odometerValue,
                fecha = Timestamp(Date(0L))
            )
        )
    }
}