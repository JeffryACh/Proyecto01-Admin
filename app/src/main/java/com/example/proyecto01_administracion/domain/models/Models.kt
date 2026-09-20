package com.example.proyecto01_administracion.domain.models

import com.google.firebase.Timestamp

enum class UserRole {
    FLEET_MANAGER,
    MECHANIC,
    DRIVER,
    NONE
}

enum class AccountStatus {
    ACTIVE,
    SUSPENDED
}

enum class FleetStatus {
    UP_TO_DATE,           // Verde
    MAINTENANCE_DUE_SOON, // Amarillo
    MAINTENANCE_OVERDUE   // Rojo
}

data class Role(
    val id: String = "",
    val nombre: String = ""
)

data class User(
    val id: String = "", // Firebase UID
    val nombre: String = "",
    val cedula: String = "",
    val correo: String = "",
    val telefono: String = "",
    val rol_id: String = "",
    val numero_licencia: String? = null,
    val estado: String = "Activo",
    val foto_url: String? = null
)

data class Vehicle(
    val id: String = "",
    val placa: String = "",
    val marca: String = "",
    val modelo: String = "",
    val anio: Int = 0,
    val tipo: String = "",
    val clasificacion: String = "",
    val capacidad: Float = 0f, // kilogramos
    val kilometraje_actual: Long = 0L,
    val estado: String = "Activo"
)

data class MileageRecord(
    val id: String = "",
    val vehiculo_id: String = "",
    val usuario_id: String = "",
    val kilometraje: Long = 0L,
    val fecha: Timestamp = Timestamp.now()
)

data class VehicleAssignment(
    val id: String = "",
    val vehiculo_id: String = "",
    val usuario_id: String = "",
    val fecha_inicio: Timestamp = Timestamp.now(),
    val fecha_fin: Timestamp? = null,
    val asignado_por: String = ""
)

data class LegalDocument(
    val id: String = "",
    val vehiculo_id: String = "",
    val tipo: String = "",
    val fecha_vencimiento: Timestamp = Timestamp.now(),
    val archivo_url: String = ""
)

data class MaintenanceCategory(
    val id: String = "",
    val nombre: String = ""
)

data class Maintenance(
    val id: String = "",
    val vehiculo_id: String = "",
    val tipo: String = "", // Preventivo / Correctivo
    val categoria_id: String = "",
    val fecha: Timestamp = Timestamp.now(),
    val taller: String = "",
    val kilometraje: Long = 0L,
    val costo: Float = 0f,
    val descripcion: String = "",
    val registrado_por: String = ""
)

data class MaintenanceEvidence(
    val id: String = "",
    val mantenimiento_id: String = "",
    val archivo_url: String = "",
    val orden: Int = 0
)

data class MaintenancePlan(
    val id: String = "",
    val clasificacion_vehiculo: String = "",
    val categoria_id: String = "",
    val intervalo_km: Long = 0L,
    val intervalo_dias: Int = 0
)

data class MaintenanceReport(
    val totalCost: Double = 0.0,
    val maintenanceCount: Int = 0,
    val preventivePercentage: Int = 0,
    val correctivePercentage: Int = 0,
    val predictivePercentage: Int = 0,
    val costByVehicleId: Map<String, Double> = emptyMap()
)

data class Alert(
    val id: String = "",
    val vehiculo_id: String = "",
    val mantenimiento_id: String? = null,
    val documento_id: String? = null,
    val severidad: String = "", // Verde, Amarillo, Rojo
    val titulo: String = "",
    val mensaje: String = "",
    val fecha_creacion: Timestamp = Timestamp.now()
)

data class AlertRecipient(
    val id: String = "",
    val alerta_id: String = "",
    val usuario_id: String = "",
    val leida: Boolean = false,
    val leida_en: Timestamp? = null
)
