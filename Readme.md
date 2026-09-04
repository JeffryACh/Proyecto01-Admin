# 🚛 Sistema de Gestión de Mantenimiento de Flotillas (TransAndina)

Bienvenido al repositorio oficial de la aplicación móvil de **TransAndina**. Este proyecto ha sido desarrollado para solucionar los desafíos operativos y logísticos asociados con el mantenimiento de flotillas, migrando de los registros manuales a una plataforma digital centralizada y en tiempo real. La aplicación está diseñada para reducir tiempos de inactividad de vehículos, optimizar costos de mantenimiento y facilitar la toma de decisiones estratégicas mediante reportes y alertas automáticas.

## 📖 ¿De qué trata el proyecto?
Esta es una aplicación móvil nativa para Android que permite a las empresas de transporte mantener un control exhaustivo sobre el estado de sus vehículos, con sincronización en tiempo real y funcionalidad offline. Sus principales usuarios son:
*   **Conductores:** Actualizan el kilometraje de forma periódica, consultan el historial de mantenimientos de su vehículo asignado, y reciben notificaciones exclusivas sobre próximos servicios.
*   **Mecánicos:** Registran mantenimientos preventivos/correctivos con detalle completo, adjuntando evidencia fotográfica de facturas y trabajos realizados, costos aproximados, y descripción del servicio.
*   **Encargados de Flota (Administradores):** Supervisan el estado global de todos los vehículos mediante indicadores visuales de semáforo (verde=al día, amarillo=próximo, rojo=atrasado), gestionan perfiles de usuarios, asignan conductores a vehículos, generan reportes financieros de mantenimiento, y reciben alertas consolidadas sobre vencimientos de documentos legales y mantenimientos atrasados.

## 🚀 ¿Cómo usar la aplicación? (Guía Básica)

### Para Personas No Técnicas (Usuarios finales)
1.  **Instalación:** Descarga el archivo `.apk` provisto en la sección de "Releases" o instálalo desde la tienda de aplicaciones e inícialo en tu dispositivo Android (API 24 o superior). La aplicación requiere permisos de cámara, almacenamiento y notificaciones.
2.  **Inicio de sesión:** Ingresa con tu correo y contraseña asignados por recursos humanos o la gerencia. Si olvidaste tu contraseña, selecciona "¿Olvidaste tu contraseña?" y recibirás un enlace de recuperación en tu correo electrónico.
3.  **Navegación según tu rol:**
    *   **Si eres Conductor:** 
        - Verás la pantalla de tu vehículo asignado con su información general (placa, marca, modelo, kilometraje actual).
        - Usa el botón "Actualizar Odómetro" para registrar el kilometraje actual de forma periódica (diaria o según políticas de la empresa).
        - Consulta el historial de mantenimientos realizados a tu vehículo.
        - Recibe notificaciones exclusivas sobre próximos mantenimientos preventivos.
    *   **Si eres Mecánico:** 
        - Busca el vehículo por su placa en el listado de vehículos disponibles.
        - Selecciona "Registrar Mantenimiento" y completa el formulario con: tipo (preventivo/correctivo), categoría de servicio (cambio de aceite, frenos, etc.), descripción, costo aproximado.
        - Captura fotos de las facturas o trabajos realizados usando la cámara integrada.
        - Guarda el registro. Si no hay conexión, se guardará localmente y se sincronizará automáticamente cuando recuperes señal.
    *   **Si eres Encargado de Flota:** 
        - Accede al panel principal (Dashboard) que muestra todos los vehículos de la empresa.
        - Cada vehículo tiene un indicador visual: Verde (al día), Amarillo (próximo a revisión en 2 semanas), Rojo (mantenimiento atrasado).
        - Selecciona un vehículo para ver su ficha completa: historial de mantenimientos, kilometraje, conductor asignado, estado de documentos legales.
        - Genera reportes de costos totales invertidos en mantenimiento por rango de fechas.
        - Visualiza el panel de alertas consolidadas con todas las notificaciones urgentes de la flotilla.
        - Gestiona usuarios: activa, suspende o reasigna conductores a otros vehículos.
4.  **Notificaciones:** Recibirás avisos en tu teléfono cuando se requiera tu atención:
    - Conductores: "Próximo mantenimiento preventivo de tu vehículo en 500 km"
    - Encargados: "Revisión técnica de placa XXX-123 próxima a vencer en 5 días" o "Mantenimiento correctivo registrado en vehículo YYY-456"

### Para Personas Técnicas (Desarrolladores)
1.  **Requisitos previos:**
    - Android Studio Koala (2024.1.1) o superior.
    - JDK 17 o superior.
    - Gradle 8.x.
    - Cuenta de Google Cloud con proyecto Firebase configurado.
    - Emulador Android con API 24+ o dispositivo físico conectado.

2.  **Clonar el repositorio:** 
    ```bash
    git clone <url-del-repo>
    cd Proyecto01-Admin
    ```

3.  **Configuración de Firebase:**
    - Descarga el archivo `google-services.json` desde la consola de Firebase.
    - Colócalo en el directorio `app/` del proyecto.
    - Verifica que el archivo contenga las credenciales correctas para autenticación, Firestore, Storage y Cloud Messaging.

4.  **Sincronización de Gradle:**
    - Abre el proyecto en Android Studio.
    - Selecciona `File > Sync Project with Gradle Files`.
    - Espera a que se descarguen todas las dependencias (puede tomar 2-5 minutos en la primera sincronización).

5.  **Configuración de Emulador o Dispositivo:**
    - Para emulador: Abre el AVD Manager y crea/selecciona un emulador con API 24+.
    - Para dispositivo físico: Conecta el dispositivo vía USB, habilita "Depuración USB" en Opciones de Desarrollador.

6.  **Ejecutar la aplicación:**
    - Presiona `Shift + F10` (Windows/Linux) o `Ctrl + R` (Mac) para compilar y ejecutar.
    - Alternativamente, selecciona `Run > Run 'app'` desde el menú.
    - La aplicación se instalará y abrirá automáticamente en el emulador o dispositivo.

7.  **Estructura del Proyecto:**
    ```
    app/
    ├── src/
    │   ├── main/
    │   │   ├── java/com/transandina/
    │   │   │   ├── ui/              # Composables y pantallas
    │   │   │   ├── viewmodel/       # ViewModels (MVVM)
    │   │   │   ├── data/            # Repositorios y DAOs
    │   │   │   ├── domain/          # Modelos de negocio
    │   │   │   └── utils/           # Utilidades y extensiones
    │   │   ├── res/                 # Recursos (strings, colores, etc.)
    │   │   └── AndroidManifest.xml
    │   └── test/                    # Pruebas unitarias
    ├── build.gradle.kts             # Configuración de Gradle
    └── google-services.json         # Credenciales de Firebase
    ```

## 📚 Resumen de Documentación y Hoja de Ruta

*   **Documentación Técnica (`Documentacion.md`):** Explica en detalle (para perfiles técnicos y de negocio) las herramientas seleccionadas, como Kotlin (Android Nativo), Firebase (nube y autenticación), Room (bases de datos sin conexión), Jetpack Compose (diseño de pantallas), CameraX (captura de imágenes), WorkManager (sincronización offline), y librerías de gráficos. Incluye flujos de integración entre tecnologías y dependencias principales del proyecto.

*   **Hoja de Ruta (`Hoja_de_ruta.md`):** Detalla el cronograma de desarrollo estructurado para un equipo de 4 programadores, con hitos semanales, responsabilidades claras por rol, fechas de corte específicas para cada funcionalidad, y criterios de aceptación. Asegura la entrega final y estable de la aplicación antes de la fecha límite del **16 de septiembre de 2026 a las 23:45**.

## 🔧 Características Principales Implementadas

- ✅ **Autenticación Multi-rol:** Login seguro con recuperación de contraseña para conductores, mecánicos y encargados.
- ✅ **Registro de Vehículos:** Gestión completa de datos de vehículos (placa, marca, modelo, documentos legales, conductor asignado).
- ✅ **Registro de Mantenimientos:** Captura detallada de mantenimientos preventivos/correctivos con evidencia fotográfica.
- ✅ **Actualización de Kilometraje:** Registro periódico del odómetro con validación de consistencia.
- ✅ **Dashboard de Flotilla:** Visualización en tiempo real del estado de todos los vehículos con indicadores de semáforo.
- ✅ **Alertas Automáticas:** Notificaciones push para próximos mantenimientos, vencimientos de documentos y cambios críticos.
- ✅ **Reportes Financieros:** Generación de reportes de costos totales de mantenimiento por rango de fechas.
- ✅ **Sincronización Offline:** Funcionamiento parcial sin conexión con sincronización automática cuando recupera señal.
- ✅ **Gráficos Históricos:** Visualización de tendencias de kilometraje y costos de mantenimiento.

## 📋 Limitaciones Conocidas

El sistema **no incluye** las siguientes funcionalidades (según especificaciones del proyecto):
- Integración con sistemas de rastreo GPS en tiempo real.
- Facturación electrónica de servicios de mantenimiento.
- Integración con proveedores externos de repuestos o talleres.

## 🤝 Contribución y Soporte

Para reportar bugs, sugerencias o contribuir al proyecto, por favor:
1. Crea un issue en el repositorio describiendo el problema o sugerencia.
2. Si deseas contribuir código, crea un fork, realiza tus cambios en una rama (`feature/nombre-feature`), y abre un Pull Request.
3. Asegúrate de que tu código siga las convenciones de estilo del proyecto y pase todas las pruebas.

## 📞 Contacto

Para preguntas técnicas o administrativas sobre el proyecto, contacta al equipo de desarrollo de TransAndina.