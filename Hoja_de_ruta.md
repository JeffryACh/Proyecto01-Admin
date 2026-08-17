# Hoja de Ruta: Proyecto TransAndina

**Fecha límite de entrega:** 16 de septiembre de 2026 a las 23:45.
**Equipo de Desarrollo:** 4 Programadores.
**Metodología:** Desarrollo Ágil (Sprints Semanales).

## 👥 Asignación de Roles del Equipo

Para maximizar la eficiencia y paralelizar el trabajo, el equipo operará bajo los siguientes roles:
*   **👨‍💻 Programador 1 (Líder Backend y Nube):** Especialista en Firebase (Auth, Firestore, Storage), configuración del proyecto, reglas de seguridad y arquitectura de datos.
*   **👨‍💻 Programador 2 (UI/UX Frontend):** Especialista en Jetpack Compose, navegación, interfaces de usuario y experiencia gráfica interactiva.
*   **👨‍💻 Programador 3 (Lógica Core y Hardware):** Especialista en Kotlin, interacción con CameraX, algoritmos de cálculo de kilometraje y almacenamiento local (Room).
*   **👨‍💻 Programador 4 (QA, Reportes y Alertas):** Especialista en notificaciones push (FCM), lógica del semáforo, generación de gráficos/reportes y pruebas automatizadas.

---

## 📅 Cronograma y Etapas de Desarrollo (A partir de hoy, 17/08/2026)

### 🚀 Etapa 1: Setup, Autenticación y Arquitectura Base
**Fechas:** 17 de Agosto al 23 de Agosto de 2026

*   **Programador 1:** Crear proyecto en Android Studio y en Firebase. Configurar `google-services.json`. Desarrollar el módulo de autenticación (Login/Recuperar Contraseña) y modelado de datos en Firestore (Colecciones: Usuarios, Vehículos).
*   **Programador 2:** Diseñar el sistema de diseño (Colores, Tipografías Material Design 3). Crear pantallas de Login, Registro de Usuarios (solo vista) y el menú principal de navegación (Bottom Navigation).
*   **Programador 3:** Configurar inyección de dependencias (Hilt). Establecer la estructura MVVM base del proyecto y repositorios de datos locales vacíos.
*   **Programador 4:** Configurar entorno de pruebas (JUnit/Espresso). Investigar y probar dependencias para gráficos (MPAndroidChart o librerías Compose equivalentes).

### ⚙️ Etapa 2: Módulos Principales (Vehículos y Mantenimientos)
**Fechas:** 24 de Agosto al 30 de Agosto de 2026

*   **Programador 1:** Implementar operaciones CRUD (Crear, Leer, Actualizar) en Firebase para los módulos de Vehículos y Mantenimientos.
*   **Programador 2:** Desarrollar las pantallas de "Ficha del Vehículo", "Listado de Vehículos" y "Formulario de Registro de Mantenimiento".
*   **Programador 3:** Integrar `CameraX` y `Coil` en el formulario de mantenimiento para capturar y previsualizar evidencias/facturas. Desarrollar base de datos local `Room` para cachear la información si no hay red.
*   **Programador 4:** Crear el sistema para subir las fotografías tomadas a `Firebase Cloud Storage` y adjuntar sus URLs al registro del mantenimiento.

### 📊 Etapa 3: Dashboards, Kilometraje y Semáforo de Flotilla
**Fechas:** 31 de Agosto al 06 de Septiembre de 2026

*   **Programador 1:** Desarrollar consultas complejas de Firestore: filtros por rangos de fechas y costos. Lógica para asignar y desasignar conductores.
*   **Programador 2:** Crear la pantalla del Encargado (Dashboard de Flotilla), listado visual con semáforo (Verde, Amarillo, Rojo). Diseñar pantalla de historial de kilometraje.
*   **Programador 3:** Implementar la lógica matemática que compara el último kilometraje con el ingresado (validación de inconsistencias) y predice la proximidad del próximo mantenimiento.
*   **Programador 4:** Integrar los gráficos históricos de kilometraje en la vista del usuario. Desarrollar la lógica de cálculo global para reportes de costos.

### 🔔 Etapa 4: Alertas, Notificaciones y Pulido
**Fechas:** 07 de Septiembre al 13 de Septiembre de 2026

*   **Programador 1:** Configurar triggers/Cloud Functions para detectar mantenimientos atrasados o documentos a punto de vencer en el backend.
*   **Programador 2:** Refinar la interfaz de usuario, manejo de estados de carga (Loaders), errores visuales, y modo oscuro/claro.
*   **Programador 3:** Terminar la sincronización Offline-Online con `WorkManager`. Asegurar que si el teléfono recupera red, los mantenimientos locales se suban.
*   **Programador 4:** Configurar e integrar `Firebase Cloud Messaging (FCM)`. Implementar notificaciones push tanto para el chofer individual como para el panel consolidado del gerente. Realizar pruebas integrales del flujo.

### 🏁 Etapa 5: Entrega Final y Despliegue
**Fechas:** 14 de Septiembre al 16 de Septiembre de 2026

*   **Todos los Programadores (1, 2, 3, 4):**
    *   **14/09:** Corrección intensiva de Bugs (Bug-fixing) de las pruebas de QA.
    *   **15/09:** Pruebas de campo (crear usuarios prueba, ingresar mantenimientos falsos, verificar semáforos y tomar fotos reales).
    *   **16/09 (Mañana/Tarde):** Compilación del `.apk` de Release y ofuscación de código (ProGuard/R8). Limpieza del código.
    *   **16/09 (Noche):** Cierre final de documentación y entrega del proyecto (Previo a las 23:45).