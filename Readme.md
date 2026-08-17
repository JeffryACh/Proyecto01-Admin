# 🚛 Sistema de Gestión de Mantenimiento de Flotillas (TransAndina)

Bienvenido al repositorio oficial de la aplicación móvil de **TransAndina**. Este proyecto ha sido desarrollado para solucionar los desafíos operativos y logísticos asociados con el mantenimiento de flotillas, migrando de los registros manuales a una plataforma digital centralizada y en tiempo real.

## 📖 ¿De qué trata el proyecto?
Esta es una aplicación móvil nativa para Android que permite a las empresas de transporte mantener un control exhaustivo sobre el estado de sus vehículos. Sus principales usuarios son:
*   **Conductores:** Actualizan kilometraje y consultan mantenimientos.
*   **Mecánicos:** Registran mantenimientos preventivos/correctivos, adjuntando evidencia (fotos, costos).
*   **Encargados de Flota (Administradores):** Supervisan el estado global (mediante indicadores de semáforo), gestionan usuarios, vehículos, y reciben alertas predictivas sobre futuros mantenimientos o vencimientos de documentos.

## 🚀 ¿Cómo usar la aplicación? (Guía Básica)

### Para Personas No Técnicas (Usuarios finales)
1.  **Instalación:** Descarga el archivo `.apk` provisto en la sección de "Releases" o instálalo desde la tienda de aplicaciones e inícialo en tu dispositivo Android.
2.  **Inicio de sesión:** Ingresa con tu correo y contraseña asignados por recursos humanos o la gerencia.
3.  **Navegación:**
    *   Si eres *Conductor*, verás la pantalla de tu vehículo, donde podrás actualizar tu kilometraje usando el botón "Actualizar Odómetro".
    *   Si eres *Mecánico*, busca el vehículo por su placa para registrar un nuevo mantenimiento, llenando el formulario y tomando fotos de las facturas.
    *   Si eres *Encargado*, accederás a un panel con todos los vehículos, marcados en verde (al día), amarillo (próximo a revisión) o rojo (mantenimiento atrasado).
4.  **Notificaciones:** Recibirás avisos en tu teléfono cuando se requiera tu atención (ej. "Revisión técnica de placa XXX-123 próxima a vencer").

### Para Personas Técnicas (Desarrolladores)
1.  **Clonar el repositorio:** `git clone <url-del-repo>`
2.  **Entorno:** Abrir el proyecto en **Android Studio** (Koala o superior recomendado).
3.  **Configuración:** Sincronizar Gradle (`Sync Project with Gradle Files`). Asegúrese de que el archivo `google-services.json` esté colocado en el directorio `app/` para que la conexión con Firebase funcione.
4.  **Ejecutar:** Compilar el proyecto en un emulador o dispositivo físico Android con API Nivel 24+.

## 📚 Resumen de Documentación y Hoja de Ruta

*   **Documentación Tecnológica (`Documentacion.md`):** Explica en detalle (para perfiles técnicos y de negocio) las herramientas seleccionadas, como Kotlin (Android Nativo), Firebase (nube y autenticación), Room (bases de datos sin conexión) y Jetpack Compose (diseño de pantallas).
*   **Hoja de Ruta (`Hoja_de_ruta.md`):** Detalla el cronograma de desarrollo estructurado para un equipo de 4 programadores, con hitos semanales y responsabilidades claras, para asegurar la entrega final y estable de la aplicación antes de la fecha límite del **16 de septiembre de 2026**.