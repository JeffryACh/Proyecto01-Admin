package com.example.proyecto01_administracion.data.repository

import com.example.proyecto01_administracion.data.local.entity.*
import com.example.proyecto01_administracion.domain.models.*
import com.google.firebase.Timestamp
import java.util.Date

internal fun UserEntity.toDomain() = User(
    id = id,
    nombre = name,
    cedula = identification.orEmpty(),
    correo = email,
    telefono = phone.orEmpty(),
    rol_id = roleId,
    numero_licencia = licenseNumber,
    estado = status,
    foto_url = photoUrl
)

internal fun User.toEntity() = UserEntity(
    id = id,
    name = nombre,
    identification = cedula.ifBlank { null },
    email = correo,
    phone = telefono.ifBlank { null },
    roleId = rol_id,
    licenseNumber = numero_licencia,
    status = estado,
    photoUrl = foto_url
)

internal fun VehicleEntity.toDomain() = Vehicle(
    id = id,
    placa = plate,
    marca = brand,
    modelo = model,
    anio = year,
    tipo = type,
    clasificacion = classification,
    capacidad = capacity.toFloat(),
    kilometraje_actual = currentMileage,
    estado = status
)

internal fun Vehicle.toEntity() = VehicleEntity(
    id = id,
    plate = placa,
    brand = marca,
    model = modelo,
    year = anio,
    type = tipo,
    classification = clasificacion,
    capacity = capacidad.toDouble(),
    currentMileage = kilometraje_actual,
    status = estado
)

internal fun RoleEntity.toDomain() = Role(id = id, nombre = name)
internal fun MaintenanceCategoryEntity.toDomain() = MaintenanceCategory(id = id, nombre = name)

internal fun MileageRecordEntity.toDomain() = MileageRecord(
    id = id,
    vehiculo_id = vehicleId,
    usuario_id = userId,
    kilometraje = mileage,
    fecha = Timestamp(Date(date))
)

internal fun MileageRecord.toEntity() = MileageRecordEntity(
    id = id,
    vehicleId = vehiculo_id,
    userId = usuario_id,
    mileage = kilometraje,
    date = fecha.toDate().time
)

internal fun MaintenanceEntity.toDomain() = Maintenance(
    id = id,
    vehiculo_id = vehicleId,
    tipo = type,
    categoria_id = categoryId,
    fecha = Timestamp(Date(date)),
    taller = workshop.orEmpty(),
    kilometraje = mileage,
    costo = cost.toFloat(),
    descripcion = description,
    registrado_por = registeredBy
)

internal fun Maintenance.toEntity() = MaintenanceEntity(
    id = id,
    vehicleId = vehiculo_id,
    type = tipo,
    categoryId = categoria_id,
    date = fecha.toDate().time,
    workshop = taller.ifBlank { null },
    mileage = kilometraje,
    cost = costo.toDouble(),
    description = descripcion,
    registeredBy = registrado_por
)

internal fun AlertEntity.toDomain() = Alert(
    id = id,
    vehiculo_id = vehicleId,
    mantenimiento_id = maintenanceId,
    documento_id = documentId,
    severidad = severity,
    titulo = title,
    mensaje = message,
    fecha_creacion = Timestamp(Date(createdAt))
)

internal fun LegalDocumentEntity.toDomain() = LegalDocument(
    id = id,
    vehiculo_id = vehicleId,
    tipo = type,
    fecha_vencimiento = Timestamp(Date(expirationDate)),
    archivo_url = fileUrl.orEmpty()
)

internal fun LegalDocument.toEntity() = LegalDocumentEntity(
    id = id,
    vehicleId = vehiculo_id,
    type = tipo,
    expirationDate = fecha_vencimiento.toDate().time,
    fileUrl = archivo_url.ifBlank { null },
    syncStatus = SyncStatus.PENDING,
    updatedAt = System.currentTimeMillis()
)
