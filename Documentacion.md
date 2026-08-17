# Documentación Técnica y Arquitectura: Sistema TransAndina

Este documento detalla las tecnologías elegidas para el desarrollo del Sistema de Gestión de Mantenimiento de Flotillas "TransAndina", justificando su uso tanto desde una perspectiva técnica como de negocio (no técnica).

## 1. Tecnologías Obligatorias (Según requerimientos base)

Según los lineamientos del proyecto, el desarrollo tiene restricciones explícitas que debemos cumplir:

*   **Desarrollo Móvil Nativo para Android:**
    *   *Para no técnicos:* La aplicación se construirá específicamente para teléfonos y tabletas Android, garantizando que sea rápida, fluida y aproveche al máximo las capacidades del dispositivo (como la cámara o las notificaciones).
    *   *Para técnicos:* El desarrollo se realizará utilizando **Kotlin** como lenguaje principal y el SDK de Android, asegurando rendimiento nativo, acceso directo a APIs del sistema (como CameraX para la toma de evidencias de facturas) y cumplimiento estricto con las guías de Material Design.

## 2. Opciones Tecnológicas Propuestas (No implícitas en el enunciado)

Para cumplir con las funcionalidades (registro de usuarios, fotos, alertas, semaforización y roles), proponemos el siguiente stack tecnológico:

### A. Interfaz de Usuario (UI) y Arquitectura
*   **Jetpack Compose (UI) + MVVM (Arquitectura)**
    *   *Para no técnicos:* Es la forma más moderna de "dibujar" las pantallas. Permite que los cambios visuales (como cambiar el semáforo de un vehículo de verde a rojo) ocurran de forma instantánea sin recargar la página. Además, mantiene el código ordenado para evitar que un error en el diseño afecte el funcionamiento.
    *   *Para técnicos:* Jetpack Compose permite un desarrollo declarativo de la UI. Acoplado con el patrón MVVM (Model-View-ViewModel), separa la lógica de presentación de la lógica de negocio. Esto facilita las pruebas unitarias y permite observar cambios de estado (StateFlow/LiveData) reactivamente.

### B. Backend y Base de Datos en la Nube
*   **Google Firebase (Firestore, Auth, Storage, Cloud Messaging)**
    *   *Para no técnicos:* Es el "cerebro y la memoria" en internet de nuestra aplicación. Nos permite asegurar que el inicio de sesión sea seguro, que las fotos de las facturas no se pierdan, y es el encargado de enviar las alertas ("push") cuando un mantenimiento está por vencer.
    *   *Para técnicos:* 
        *   **Firebase Auth:** Gestión de identidades (email/password) y recuperación de contraseñas.
        *   **Cloud Firestore:** Base de datos NoSQL en tiempo real, ideal para sincronizar el estado del "semáforo" de la flotilla instantáneamente a los encargados.
        *   **Cloud Storage:** Almacenamiento de objetos masivos (fotografías de facturas y mantenimientos).
        *   **Firebase Cloud Messaging (FCM):** Servicio de notificaciones push dirigidas por rol o por conductor.

### C. Almacenamiento Local (Offline)
*   **Room (SQLite local)**
    *   *Para no técnicos:* Permite que la aplicación siga funcionando parcialmente incluso si el conductor o mecánico se queda sin internet en la carretera o en el taller, guardando los datos en el teléfono y enviándolos cuando regrese la señal.
    *   *Para técnicos:* ORM de persistencia local. Esencial para cachear el historial de vehículos y permitir el modo "offline-first". Una vez que el dispositivo recupere la conexión, un `WorkManager` se encarga de sincronizar con Firestore.

### D. Captura de Imágenes
*   **CameraX + Coil**
    *   *Para no técnicos:* Herramientas para que la cámara del celular tome fotos nítidas de las facturas rápidamente, y otra herramienta para que las fotos carguen velozmente en la pantalla de historial sin gastar todos los datos móviles del usuario.
    *   *Para técnicos:* `CameraX` abstrae la complejidad de la API de cámara nativa manejando el ciclo de vida, rotaciones y captura de imágenes. `Coil` es una librería de carga de imágenes asíncrona optimizada para Kotlin y Jetpack Compose.

## 3. Relación e Integración entre Tecnologías

El flujo de trabajo e integración tecnológica es el siguiente:
1.  **Captura y Acción (Jetpack Compose + CameraX):** El mecánico usa la interfaz (Compose) para abrir la cámara (CameraX) y tomar foto de la factura.
2.  **Lógica y Procesamiento (MVVM + Kotlin):** El ViewModel valida que se haya ingresado el kilometraje correcto y formatea los datos.
3.  **Almacenamiento Local (Room):** Si no hay red, la información y foto se guardan temporalmente en el dispositivo usando Room.
4.  **Sincronización en la Nube (Firebase):** Al haber red, la foto sube a *Cloud Storage*, se obtiene su URL, y se guarda el registro completo en *Firestore*.
5.  **Alertas (FCM):** Si el registro indica un mantenimiento correctivo grave, *Firestore* dispara un evento (mediante Cloud Functions o triggers) que usa *FCM* para notificar al Encargado de Flota en tiempo real.
