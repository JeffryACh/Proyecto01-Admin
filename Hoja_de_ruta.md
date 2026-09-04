# 🗺️ Hoja de Ruta Detallada: Proyecto TransAndina

**Fecha límite de entrega:** 16 de septiembre de 2026 a las 23:45 ⏰
**Equipo de Desarrollo:** 4 Programadores
**Metodología:** Desarrollo Ágil (Sprints Semanales)
**Fecha de inicio:** 17 de agosto de 2026

---

## 👥 Asignación de Roles y Especialidades del Equipo

Para maximizar la eficiencia y paralelizar el trabajo, el equipo operará bajo los siguientes roles especializados:

| Rol | Especialista | Responsabilidades Principales |
|-----|--------------|-------------------------------|
| **Programador 1** | Líder Backend y Nube | Firebase (Auth, Firestore, Storage, Cloud Functions), reglas de seguridad, arquitectura de datos, sincronización en la nube |
| **Programador 2** | UI/UX Frontend | Jetpack Compose, Navigation, diseño de pantallas, Material Design 3, animaciones, experiencia de usuario |
| **Programador 3** | Lógica Core y Hardware | Kotlin, CameraX, algoritmos de cálculo, Room (SQLite local), WorkManager, sincronización offline |
| **Programador 4** | QA, Reportes y Alertas | Firebase Cloud Messaging (FCM), lógica del semáforo, gráficos/reportes, pruebas automatizadas, testing |

---

## 📅 Cronograma Detallado por Etapa

### 🚀 ETAPA 1: Setup, Autenticación y Arquitectura Base
**Período:** 17 de Agosto al 23 de Agosto de 2026 (7 días)
**Objetivo:** Establecer las bases tecnológicas y permitir que el equipo trabaje en paralelo sin conflictos.

#### Programador 1 (Backend y Nube)
**Tareas:**
- [ ] Crear proyecto en Firebase Console (Firestore, Auth, Storage, Cloud Messaging)
- [ ] Descargar `google-services.json` y documentar credenciales
- [ ] Crear proyecto base en Android Studio con Gradle 8.x
- [ ] Configurar módulo de autenticación Firebase Auth (email/password)
- [ ] Diseñar estructura de colecciones en Firestore:
  - `usuarios` (uid, email, nombre, rol, estado, fechaCreacion)
  - `vehiculos` (id, placa, marca, modelo, año, tipo, capacidad, kilometrajeActual, conductorAsignado, documentosLegales, estado)
  - `mantenimientos` (id, vehiculoId, tipo, categoria, descripcion, costo, fotosURLs, fecha, mecanico, estado)
  - `alertas` (id, tipo, vehiculoId, mensaje, fechaCreacion, leida)
  - `kilometraje` (id, vehiculoId, valor, fecha, conductor)
- [ ] Implementar reglas de seguridad en Firestore por rol (conductor, mecánico, encargado)
- [ ] Crear índices compuestos para consultas complejas
- [ ] Documentar esquema de datos en archivo `FIREBASE_SCHEMA.md`

**Fecha de Corte:** 23 de Agosto - Validación: Proyecto compilable, Firebase conectado, autenticación funcional

#### Programador 2 (UI/UX Frontend)
**Tareas:**
- [ ] Definir sistema de diseño (colores, tipografías, espaciados según Material Design 3)
- [ ] Crear archivo `Theme.kt` con paleta de colores y estilos
- [ ] Diseñar pantalla de Login (email, contraseña, botón "Olvidé contraseña")
- [ ] Diseñar pantalla de Recuperación de Contraseña
- [ ] Diseñar pantalla de Registro de Usuarios (solo vista, sin lógica)
- [ ] Crear estructura de navegación con Navigation Compose (Bottom Navigation para 3 roles)
- [ ] Diseñar pantalla principal vacía para cada rol (placeholder)
- [ ] Implementar tema claro/oscuro (opcional pero recomendado)

**Fecha de Corte:** 23 de Agosto - Validación: Navegación fluida, pantallas visuales sin errores

#### Programador 3 (Lógica Core y Hardware)
**Tareas:**
- [ ] Configurar inyección de dependencias con Hilt
- [ ] Crear estructura MVVM base del proyecto (carpetas: ui, viewmodel, data, domain)
- [ ] Implementar repositorios base (UserRepository, VehicleRepository, MaintenanceRepository)
- [ ] Crear DAOs y entidades de Room para almacenamiento local
- [ ] Configurar base de datos Room con migraciones
- [ ] Crear extensiones de Kotlin útiles (formateo de fechas, validaciones)
- [ ] Investigar y probar CameraX (permisos, captura básica)

**Fecha de Corte:** 23 de Agosto - Validación: Proyecto compila, Hilt inyecta dependencias, Room funciona

#### Programador 4 (QA, Reportes y Alertas)
**Tareas:**
- [ ] Configurar entorno de pruebas (JUnit 4, Espresso, Mockito)
- [ ] Crear suite de pruebas unitarias base
- [ ] Investigar librerías de gráficos (Vico vs MPAndroidChart)
- [ ] Investigar Firebase Cloud Messaging (FCM) y configuración básica
- [ ] Crear documento de criterios de aceptación para cada funcionalidad
- [ ] Configurar CI/CD básico (GitHub Actions o similar)

**Fecha de Corte:** 23 de Agosto - Validación: Pruebas ejecutables, documentación de criterios

---

### ⚙️ ETAPA 2: Módulos Principales (Vehículos y Mantenimientos)
**Período:** 24 de Agosto al 30 de Agosto de 2026 (7 días)
**Objetivo:** Implementar funcionalidades 1, 2 y 3 (Registro de usuarios, vehículos y mantenimientos).

#### Programador 1 (Backend y Nube)
**Tareas:**
- [ ] Implementar operaciones CRUD completas en Firebase para Vehículos
- [ ] Implementar operaciones CRUD completas en Firebase para Mantenimientos
- [ ] Crear Cloud Functions para validar datos antes de guardar
- [ ] Implementar paginación en consultas de Firestore (para listados grandes)
- [ ] Crear índices adicionales si es necesario
- [ ] Documentar endpoints/funciones disponibles

**Fecha de Corte:** 30 de Agosto - Validación: CRUD funcional, datos persisten en Firestore

#### Programador 2 (UI/UX Frontend)
**Tareas:**
- [ ] Desarrollar pantalla de "Ficha del Vehículo" (mostrar detalles completos)
- [ ] Desarrollar pantalla de "Listado de Vehículos" con búsqueda por placa
- [ ] Desarrollar formulario de "Registro de Vehículo" (solo para encargados)
- [ ] Desarrollar formulario de "Registro de Mantenimiento" (campos: tipo, categoría, descripción, costo)
- [ ] Implementar validaciones visuales en formularios
- [ ] Crear pantalla de "Historial de Mantenimientos" con filtros básicos

**Fecha de Corte:** 30 de Agosto - Validación: Pantallas navegables, formularios sin errores visuales

#### Programador 3 (Lógica Core y Hardware)
**Tareas:**
- [ ] Integrar CameraX en el formulario de mantenimiento (captura de fotos)
- [ ] Implementar Coil para previsualización de imágenes capturadas
- [ ] Crear lógica de compresión de imágenes (Compressor)
- [ ] Implementar almacenamiento local de fotos en Room
- [ ] Crear ViewModels para Vehículos y Mantenimientos
- [ ] Implementar validaciones de datos (ej. kilometraje > 0)

**Fecha de Corte:** 30 de Agosto - Validación: Cámara funciona, fotos se capturan y comprimen

#### Programador 4 (QA, Reportes y Alertas)
**Tareas:**
- [ ] Crear sistema para subir fotografías a Firebase Cloud Storage
- [ ] Implementar obtención de URLs firmadas para descargas seguras
- [ ] Crear pruebas unitarias para validaciones de datos
- [ ] Crear pruebas de UI para formularios
- [ ] Documentar casos de prueba para cada funcionalidad

**Fecha de Corte:** 30 de Agosto - Validación: Fotos suben a Cloud Storage, URLs funcionan

---

### 📊 ETAPA 3: Dashboards, Kilometraje y Semáforo de Flotilla
**Período:** 31 de Agosto al 06 de Septiembre de 2026 (7 días)
**Objetivo:** Implementar funcionalidades 4 y 6 (Kilometraje, Dashboard de Flotilla y Semáforo).

#### Programador 1 (Backend y Nube)
**Tareas:**
- [ ] Desarrollar consultas complejas de Firestore (filtros por fecha, rango de costos, estado)
- [ ] Implementar lógica de asignación/desasignación de conductores a vehículos
- [ ] Crear Cloud Functions para calcular estado del semáforo (verde, amarillo, rojo)
- [ ] Implementar agregaciones de costos totales por rango de fechas
- [ ] Crear listeners en tiempo real para cambios de estado

**Fecha de Corte:** 06 de Septiembre - Validación: Consultas retornan datos correctos, semáforo se calcula

#### Programador 2 (UI/UX Frontend)
**Tareas:**
- [ ] Crear pantalla del Dashboard de Flotilla (listado con semáforos visuales)
- [ ] Implementar indicadores visuales (Verde, Amarillo, Rojo) con iconografía clara
- [ ] Diseñar pantalla de "Historial de Kilometraje" con gráfico de tendencias
- [ ] Crear pantalla de "Detalles del Vehículo" expandida (incluir semáforo, próximo mantenimiento)
- [ ] Implementar filtros en listados (por estado, por conductor, por fecha)

**Fecha de Corte:** 06 de Septiembre - Validación: Dashboard muestra semáforos, gráficos visibles

#### Programador 3 (Lógica Core y Hardware)
**Tareas:**
- [ ] Implementar lógica matemática de validación de kilometraje (debe ser >= último valor)
- [ ] Crear algoritmo para predecir próximo mantenimiento basado en km y fecha
- [ ] Implementar cálculo de "km restantes" hasta próximo mantenimiento
- [ ] Crear ViewModels para Dashboard y Kilometraje
- [ ] Implementar sincronización de datos locales con Firestore

**Fecha de Corte:** 06 de Septiembre - Validación: Cálculos correctos, predicciones precisas

#### Programador 4 (QA, Reportes y Alertas)
**Tareas:**
- [ ] Integrar librería de gráficos (Vico o MPAndroidChart) en pantalla de histórico
- [ ] Crear gráficos de líneas para kilometraje histórico
- [ ] Crear gráficos de barras para costos de mantenimiento
- [ ] Implementar lógica de cálculo global para reportes de costos
- [ ] Crear pruebas para cálculos de semáforo

**Fecha de Corte:** 06 de Septiembre - Validación: Gráficos se renderizan correctamente, reportes generan datos

---

### 🔔 ETAPA 4: Alertas, Notificaciones y Pulido
**Período:** 07 de Septiembre al 13 de Septiembre de 2026 (7 días)
**Objetivo:** Implementar funcionalidades 5 y 7 (Alertas, Notificaciones y Gestión de Usuarios Administradores).

#### Programador 1 (Backend y Nube)
**Tareas:**
- [ ] Crear Cloud Functions para detectar mantenimientos atrasados
- [ ] Crear Cloud Functions para detectar documentos a punto de vencer
- [ ] Implementar triggers automáticos que disparen alertas
- [ ] Crear colección de "alertas" con estado (leída/no leída)
- [ ] Implementar lógica de permisos para edición/eliminación de registros
- [ ] Crear Cloud Functions para reasignación de conductores

**Fecha de Corte:** 13 de Septiembre - Validación: Alertas se generan automáticamente

#### Programador 2 (UI/UX Frontend)
**Tareas:**
- [ ] Refinar interfaz de usuario (pulido visual)
- [ ] Implementar loaders/spinners para estados de carga
- [ ] Crear pantalla de errores amigable
- [ ] Implementar modo oscuro/claro completo
- [ ] Crear pantalla de "Gestión de Usuarios" (para encargados)
- [ ] Diseñar pantalla de "Panel de Alertas Consolidadas"

**Fecha de Corte:** 13 de Septiembre - Validación: UI pulida, sin errores visuales

#### Programador 3 (Lógica Core y Hardware)
**Tareas:**
- [ ] Terminar sincronización Offline-Online con WorkManager
- [ ] Implementar reintentos exponenciales para sincronización
- [ ] Crear lógica de conflicto de datos (si se edita offline y online)
- [ ] Implementar caché inteligente en Room
- [ ] Crear ViewModels para Alertas y Gestión de Usuarios

**Fecha de Corte:** 13 de Septiembre - Validación: Sincronización offline funciona, sin pérdida de datos

#### Programador 4 (QA, Reportes y Alertas)
**Tareas:**
- [ ] Configurar Firebase Cloud Messaging (FCM) completamente
- [ ] Implementar notificaciones push por rol (conductor, mecánico, encargado)
- [ ] Crear tópicos de FCM para notificaciones masivas
- [ ] Implementar notificaciones individuales por token de dispositivo
- [ ] Crear pruebas integrales del flujo de alertas
- [ ] Realizar pruebas de carga y rendimiento

**Fecha de Corte:** 13 de Septiembre - Validación: Notificaciones push funcionales, recibidas en dispositivos

---

### 🏁 ETAPA 5: Entrega Final y Despliegue
**Período:** 14 de Septiembre al 16 de Septiembre de 2026 (3 días)
**Objetivo:** Estabilización, testing final y entrega del producto.

#### Todos los Programadores (1, 2, 3, 4)

**14 de Septiembre (Corrección Intensiva de Bugs)**
- [ ] Ejecutar suite completa de pruebas (unitarias, UI, integración)
- [ ] Identificar y documentar todos los bugs encontrados
- [ ] Priorizar bugs por severidad (crítico, mayor, menor)
- [ ] Asignar bugs a programadores según especialidad
- [ ] Corregir bugs críticos y mayores
- [ ] Realizar pruebas de regresión

**15 de Septiembre (Pruebas de Campo)**
- [ ] Crear usuarios de prueba para cada rol (conductor, mecánico, encargado)
- [ ] Registrar vehículos de prueba con datos realistas
- [ ] Registrar mantenimientos falsos (preventivos y correctivos)
- [ ] Capturar fotos reales de facturas
- [ ] Verificar que los semáforos cambien correctamente
- [ ] Probar notificaciones push en dispositivos reales
- [ ] Verificar sincronización offline (desconectar WiFi/datos, registrar datos, reconectar)
- [ ] Generar reportes de costos y verificar precisión
- [ ] Documentar cualquier problema encontrado

**16 de Septiembre (Compilación y Entrega)**
- **Mañana/Tarde:**
  - [ ] Compilar APK de Release (sin modo debug)
  - [ ] Aplicar ofuscación de código (ProGuard/R8)
  - [ ] Generar firma de aplicación (keystore)
  - [ ] Realizar pruebas finales del APK compilado
  - [ ] Limpiar código (remover logs de debug, comentarios innecesarios)
  - [ ] Actualizar documentación final
  
- **Noche (antes de las 23:45):**
  - [ ] Subir APK a repositorio (Release)
  - [ ] Crear tag de versión final (v1.0.0)
  - [ ] Generar changelog final
  - [ ] Entregar documentación completa (README, Documentacion.md, Hoja_de_ruta.md)
  - [ ] Confirmar entrega exitosa

---

## 📋 Criterios de Aceptación por Funcionalidad

### Funcionalidad 1: Registro de Usuarios
- ✅ Login con email y contraseña funciona
- ✅ Recuperación de contraseña vía email funciona
- ✅ Usuarios pueden editar sus datos personales
- ✅ Roles se asignan correctamente (conductor, mecánico, encargado)
- ✅ Datos se persisten en Firestore

### Funcionalidad 2: Registro de Vehículos
- ✅ Encargados pueden registrar vehículos con todos los datos
- ✅ Vehículos se muestran en listado con búsqueda por placa
- ✅ Ficha individual del vehículo muestra información completa
- ✅ Conductores pueden asignarse a vehículos
- ✅ Datos se persisten en Firestore

### Funcionalidad 3: Registro de Mantenimientos
- ✅ Mecánicos pueden registrar mantenimientos (preventivo/correctivo)
- ✅ Fotos se capturan y comprimen correctamente
- ✅ Fotos se suben a Cloud Storage
- ✅ Historial de mantenimientos es consultable
- ✅ Encargados pueden editar/eliminar registros
- ✅ Datos se persisten en Firestore

### Funcionalidad 4: Registro de Kilometraje
- ✅ Conductores pueden actualizar kilometraje
- ✅ Validación: nuevo km >= km anterior
- ✅ Gráfico histórico de kilometraje se muestra
- ✅ Sistema calcula próximo mantenimiento correctamente
- ✅ Datos se persisten en Firestore

### Funcionalidad 5: Gestión de Usuarios Administradores
- ✅ Encargados pueden ver listado de usuarios
- ✅ Encargados pueden activar/suspender cuentas
- ✅ Encargados pueden reasignar conductores a vehículos
- ✅ Permisos se validan correctamente

### Funcionalidad 6: Módulo de Gestión de Flotilla
- ✅ Dashboard muestra todos los vehículos con semáforos
- ✅ Semáforos se actualizan en tiempo real
- ✅ Reportes de costos se generan correctamente
- ✅ Filtros funcionan (por estado, fecha, conductor)

### Funcionalidad 7: Centro de Alertas y Notificaciones
- ✅ Alertas se generan automáticamente
- ✅ Notificaciones push se reciben en dispositivos
- ✅ Panel consolidado muestra todas las alertas
- ✅ Alertas se priorizan por urgencia

---

## 🎯 Hitos Clave y Validaciones

| Fecha | Hito | Validación |
|-------|------|-----------|
| 23/08 | Fin Etapa 1 | Proyecto compilable, Firebase conectado, autenticación funcional |
| 30/08 | Fin Etapa 2 | CRUD de vehículos y mantenimientos funcional, cámara integrada |
| 06/09 | Fin Etapa 3 | Dashboard con semáforos, gráficos de kilometraje, reportes |
| 13/09 | Fin Etapa 4 | Alertas automáticas, notificaciones push, sincronización offline |
| 15/09 | Pruebas de Campo | Todos los flujos probados con datos reales |
| 16/09 23:45 | **ENTREGA FINAL** | APK compilado, documentación completa, proyecto entregado |

---

## 🔄 Comunicación y Sincronización del Equipo

- **Reuniones Diarias:** 09:00 AM (15 minutos) - Standup de estado
- **Reuniones de Sincronización:** Miércoles 02:00 PM (30 minutos) - Revisión de progreso y resolución de bloqueos
- **Reuniones de Integración:** Viernes 03:00 PM (1 hora) - Integración de cambios, pruebas conjuntas
- **Comunicación Asíncrona:** Slack/Discord para actualizaciones rápidas

---

## 📝 Notas Importantes

1. **Paralelismo:** Cada programador trabaja en su especialidad para evitar conflictos de merge.
2. **Dependencias:** Si un programador se bloquea esperando trabajo de otro, escalar inmediatamente en reunión de sincronización.
3. **Calidad:** Todas las funcionalidades deben pasar pruebas unitarias y de UI antes de considerarse "hechas".
4. **Documentación:** Cada programador documenta su código y cambios en el archivo correspondiente.
5. **Backup:** Hacer commits frecuentes (al menos 1 por día) para evitar pérdida de trabajo.