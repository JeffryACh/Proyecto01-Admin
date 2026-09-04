# Documentación Técnica y Arquitectura: Sistema TransAndina

Este documento detalla las tecnologías elegidas para el desarrollo del Sistema de Gestión de Mantenimiento de Flotillas "TransAndina", justificando su uso tanto desde una perspectiva técnica como de negocio (no técnica). La arquitectura ha sido diseñada para soportar un equipo de 4 desarrolladores trabajando en paralelo, garantizando escalabilidad, mantenibilidad y entrega dentro del plazo establecido (16 de septiembre de 2026).

## 1. Tecnologías Obligatorias (Según requerimientos base)

Según los lineamientos del proyecto, el desarrollo tiene restricciones explícitas que debemos cumplir:

*   **Desarrollo Móvil Nativo para Android:**
    *   *Para no técnicos:* La aplicación se construirá específicamente para teléfonos y tabletas Android, garantizando que sea rápida, fluida y aproveche al máximo las capacidades del dispositivo (como la cámara o las notificaciones). Esto significa que los conductores, mecánicos y encargados de flota tendrán una experiencia optimizada en sus dispositivos Android sin necesidad de versiones web o iOS.
    *   *Para técnicos:* El desarrollo se realizará utilizando **Kotlin** como lenguaje principal (versión 1.9+) y el SDK de Android (API mínima 24, API objetivo 34+), asegurando rendimiento nativo, acceso directo a APIs del sistema (como CameraX para la toma de evidencias de facturas, LocationManager para contexto geográfico, y NotificationManager para alertas locales) y cumplimiento estricto con las guías de Material Design 3. Se utilizará Gradle 8.x como sistema de construcción.

## 2. Opciones Tecnológicas Propuestas (No implícitas en el enunciado)

Para cumplir con las funcionalidades (registro de usuarios, fotos, alertas, semaforización y roles), proponemos el siguiente stack tecnológico integral:

### A. Interfaz de Usuario (UI) y Arquitectura
*   **Jetpack Compose (UI) + MVVM (Arquitectura)**
    *   *Para no técnicos:* Es la forma más moderna de "dibujar" las pantallas. Permite que los cambios visuales (como cambiar el semáforo de un vehículo de verde a rojo) ocurran de forma instantánea sin recargar la página. Además, mantiene el código ordenado para evitar que un error en el diseño afecte el funcionamiento. Los usuarios verán actualizaciones en tiempo real sin necesidad de cerrar y abrir la aplicación.
    *   *Para técnicos:* Jetpack Compose (versión 1.6+) permite un desarrollo declarativo de la UI mediante funciones composables. Acoplado con el patrón MVVM (Model-View-ViewModel), separa la lógica de presentación de la lógica de negocio. Esto facilita las pruebas unitarias y permite observar cambios de estado (StateFlow/LiveData) reactivamente. Se implementará Navigation Compose para la navegación entre pantallas, evitando fragmentos y simplificando la gestión del backstack.

### B. Backend y Base de Datos en la Nube
*   **Google Firebase (Firestore, Auth, Storage, Cloud Messaging)**
    *   *Para no técnicos:* Es el "cerebro y la memoria" en internet de nuestra aplicación. Nos permite asegurar que el inicio de sesión sea seguro, que las fotos de las facturas no se pierdan, y es el encargado de enviar las alertas ("push") cuando un mantenimiento está por vencer.
    *   *Para técnicos:* 
        *   **Firebase Auth:** Gestión de identidades (email/password) y recuperación de contraseñas.
        *   **Cloud Firestore:** Base de datos NoSQL en tiempo real, ideal para sincronizar el estado del "semáforo" de la flotilla instantáneamente a los encargados.
        *   **Cloud Storage:** Almacenamiento de objetos masivos (fotografías de facturas y mantenimientos).
        *   **Firebase Cloud Messaging (FCM):** Servicio de notificaciones push dirigidas por rol o por conductor.

### C. Almacenamiento Local (Offline)
*   **Room (SQLite local) + WorkManager**
    *   *Para no técnicos:* Permite que la aplicación siga funcionando parcialmente incluso si el conductor o mecánico se queda sin internet en la carretera o en el taller, guardando los datos en el teléfono y enviándolos cuando regrese la señal. Es como tener una "libreta de notas" en el teléfono que se sincroniza automáticamente cuando hay conexión.
    *   *Para técnicos:* Room (versión 2.6+) es un ORM de persistencia local que abstrae SQLite. Esencial para cachear el historial de vehículos, mantenimientos y usuarios, permitiendo el modo "offline-first". Se implementarán entidades con relaciones (Vehicles, Maintenances, Users) y DAOs (Data Access Objects) para operaciones CRUD. WorkManager (versión 2.8+) se encarga de sincronizar con Firestore cuando el dispositivo recupera la conexión, con reintentos exponenciales y restricciones de batería/red.

### D. Captura de Imágenes y Carga de Medios
*   **CameraX + Coil + Compressor**
    *   *Para no técnicos:* Herramientas para que la cámara del celular tome fotos nítidas de las facturas rápidamente, y otra herramienta para que las fotos carguen velozmente en la pantalla de historial sin gastar todos los datos móviles del usuario. Las imágenes se comprimen automáticamente para ahorrar espacio y ancho de banda.
    *   *Para técnicos:* `CameraX` (versión 1.3+) abstrae la complejidad de la API de cámara nativa manejando el ciclo de vida, rotaciones, permisos y captura de imágenes en alta resolución. `Coil` (versión 2.5+) es una librería de carga de imágenes asíncrona optimizada para Kotlin y Jetpack Compose, con caché en memoria y disco. `Compressor` (versión 3.0+) reduce el tamaño de las imágenes antes de subirlas a Firebase Cloud Storage, mejorando tiempos de carga y reduciendo costos de almacenamiento.

### E. Inyección de Dependencias y Testing
*   **Hilt + JUnit 4 + Espresso**
    *   *Para no técnicos:* Herramientas que permiten que el código sea más fácil de probar y mantener. Hilt es como un "organizador" que asegura que cada componente reciba lo que necesita sin confusiones.
    *   *Para técnicos:* Hilt (versión 2.48+) es el framework de inyección de dependencias recomendado por Google para Android, simplificando la creación de módulos y la inyección en Activities, Fragments y ViewModels. JUnit 4 para pruebas unitarias de lógica de negocio. Espresso para pruebas de UI automatizadas.

### F. Gráficos y Reportes
*   **Vico (Jetpack Compose) o MPAndroidChart**
    *   *Para no técnicos:* Librerías que permiten mostrar gráficos bonitos y claros del histórico de kilometraje y costos de mantenimiento, facilitando la toma de decisiones.
    *   *Para técnicos:* Vico (versión 1.10+) es la librería moderna de gráficos para Compose, con soporte para gráficos de líneas, barras y áreas. MPAndroidChart como alternativa si se requiere mayor compatibilidad con vistas tradicionales.

## 3. Relación e Integración entre Tecnologías

El flujo de trabajo e integración tecnológica es el siguiente:

### Flujo de Registro de Mantenimiento (Caso de Uso Principal)
1.  **Captura y Acción (Jetpack Compose + CameraX):** El mecánico usa la interfaz (Compose) para abrir la cámara (CameraX) y tomar foto de la factura. La aplicación solicita permisos de cámara y almacenamiento.
2.  **Compresión de Medios (Compressor):** La imagen capturada se comprime automáticamente para reducir tamaño sin perder calidad visual.
3.  **Lógica y Procesamiento (MVVM + Kotlin):** El ViewModel valida que se haya ingresado el kilometraje correcto, que el tipo de mantenimiento sea válido, y formatea los datos según el esquema de Firestore.
4.  **Almacenamiento Local (Room):** Si no hay red, la información, foto comprimida y metadatos se guardan temporalmente en el dispositivo usando Room, marcando el registro como "pendiente de sincronización".
5.  **Sincronización en la Nube (Firebase + WorkManager):** Al haber red, WorkManager detecta registros pendientes. La foto sube a *Cloud Storage* con una ruta estructurada (`/mantenimientos/{vehiculoId}/{timestamp}/`), se obtiene su URL firmada, y se guarda el registro completo en *Firestore* con timestamp del servidor.
6.  **Alertas y Triggers (Cloud Functions + FCM):** Si el registro indica un mantenimiento correctivo grave o si el próximo mantenimiento preventivo está próximo, *Cloud Functions* dispara un evento que usa *FCM* para notificar al Encargado de Flota en tiempo real. Los conductores reciben notificaciones exclusivas de sus vehículos asignados.

### Flujo de Visualización del Dashboard (Encargado de Flota)
1.  **Consulta de Datos (Firestore):** El ViewModel del Dashboard consulta la colección `vehiculos` con filtros por estado (al día, próximo, atrasado).
2.  **Cálculo de Semáforo (Kotlin Logic):** Basado en el último mantenimiento, kilometraje actual y parámetros configurados, se determina el color del semáforo.
3.  **Renderizado (Jetpack Compose + Vico):** La interfaz muestra el listado con indicadores visuales y gráficos históricos de costos.
4.  **Sincronización en Tiempo Real (Firestore Listeners):** Los cambios en Firestore se reflejan instantáneamente en la pantalla del encargado sin necesidad de recargar.

## 4. Dependencias Principales del Proyecto

A continuación se listan las dependencias clave que se utilizarán en el archivo `build.gradle.kts`:

```kotlin
// Jetpack Compose
implementation("androidx.compose.ui:ui:1.6.0")
implementation("androidx.compose.material3:material3:1.1.0")
implementation("androidx.navigation:navigation-compose:2.7.0")

// Firebase
implementation("com.google.firebase:firebase-auth:22.3.0")
implementation("com.google.firebase:firebase-firestore:24.10.0")
implementation("com.google.firebase:firebase-storage:20.3.0")
implementation("com.google.firebase:firebase-messaging:23.4.0")

// Room
implementation("androidx.room:room-runtime:2.6.0")
kapt("androidx.room:room-compiler:2.6.0")

// WorkManager
implementation("androidx.work:work-runtime-ktx:2.8.1")

// CameraX
implementation("androidx.camera:camera-core:1.3.0")
implementation("androidx.camera:camera-camera2:1.3.0")
implementation("androidx.camera:camera-lifecycle:1.3.0")

// Coil
implementation("io.coil-kt:coil-compose:2.5.0")

// Hilt
implementation("com.google.dagger:hilt-android:2.48")
kapt("com.google.dagger:hilt-compiler:2.48")

// Gráficos
implementation("com.patrykandpatrick.vico:compose:1.10.0")

// Testing
testImplementation("junit:junit:4.13.2")
androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
```
