package com.example.proyecto01_administracion

import com.example.proyecto01_administracion.domain.models.MileageRecord
import com.example.proyecto01_administracion.domain.repositories.MileageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeMileageRepository : MileageRepository {
<<<<<<< Updated upstream

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

=======
<<<<<<< HEAD
    private val records = mutableListOf<MileageRecord>()

    // MÉTODOS REALES DE LA INTERFAZ (Ajusta los nombres si son distintos en tu MileageRepository)
    override fun getMileageHistory(vehicleId: String): Flow<List<MileageRecord>> {
        return flowOf(records.filter { it.vehiculo_id == vehicleId })
    }

    override suspend fun registerMileage(record: MileageRecord): Result<Unit> {
        records.add(record)
=======

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

>>>>>>> Stashed changes
    override suspend fun registerMileage(
        record: MileageRecord
    ): Result<Unit> {
        savedRecords.add(record)
<<<<<<< Updated upstream
=======
>>>>>>> fd2f5ff22034617b78024429c695b16ea267dd33
>>>>>>> Stashed changes
        return Result.success(Unit)
    }

    // MÉTODO AUXILIAR PARA PRUEBAS (Nota: NO lleva "override" porque es exclusivo de este Fake)
    fun seedMileage(odometerValue: Long) {
        records.add(
            MileageRecord(
                id = java.util.UUID.randomUUID().toString(),
                vehiculo_id = "vehiculo-1",
                usuario_id = "user-1",
                kilometraje = odometerValue,
                // Simulamos el registro semilla en el pasado (ej. hace 24 horas)
                fecha = com.google.firebase.Timestamp(java.util.Date(System.currentTimeMillis() - 86400000))
            )
        )
    }
}