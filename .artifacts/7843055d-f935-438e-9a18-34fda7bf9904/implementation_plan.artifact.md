# Plan de Pulido Visual — TransAndina

Este plan detalla los ajustes visuales para mejorar el contraste en el tema claro, optimizar la densidad de información en la selección de vehículos y refinar la barra de navegación inferior.

## User Review Required

> [!IMPORTANT]
> Se implementarán nuevos colores semánticos específicos para el tema claro para garantizar el cumplimiento de accesibilidad y contraste sobre fondo blanco.
> Se ajustará la altura de la `BottomNavigation` y el tamaño de sus elementos para que sea más compacta pero con mayor presencia visual.

## Proposed Changes

### [Theming & Colors](file:///C:/Users/gabob/Documents/GitHub/Proyecto01-Admin/app/src/main/java/com/example/proyecto01_administracion/ui/theme/)

#### [MODIFY] [Color.kt](file:///C:/Users/gabob/Documents/GitHub/Proyecto01-Admin/app/src/main/java/com/example/proyecto01_administracion/ui/theme/Color.kt)
- Definir colores semánticos para Light Theme:
    - Verde: `#12A40A`
    - Rojo: `#D00202`
    - Amarillo: `#FFC300`
    - Azul Semántico: `#0A1FA4`
- Acentuar grises de Light Theme para mejorar contraste:
    - `BackgroundWhite`: `#F1F3F5`
    - `SurfaceGrayLight`: `#E9ECEF`
    - `BorderGrayLight`: `#CED4DA`

#### [MODIFY] [Theme.kt](file:///C:/Users/gabob/Documents/GitHub/Proyecto01-Admin/app/src/main/java/com/example/proyecto01_administracion/ui/theme/Theme.kt)
- Implementar un sistema para proveer estos colores semánticos según el tema activo (usando una función auxiliar o CompositionLocal).

### [Pantallas de Flota & Gestión](file:///C:/Users/gabob/Documents/GitHub/Proyecto01-Admin/app/src/main/java/com/example/proyecto01_administracion/ui/fleet/)

#### [MODIFY] [ReportsScreen.kt](file:///C:/Users/gabob/Documents/GitHub/Proyecto01-Admin/app/src/main/java/com/example/proyecto01_administracion/ui/fleet/ReportsScreen.kt)
- Eliminar el botón de filtro en la esquina superior derecha que no tiene funcionalidad.

#### [MODIFY] [FleetManagementScreen.kt](file:///C:/Users/gabob/Documents/GitHub/Proyecto01-Admin/app/src/main/java/com/example/proyecto01_administracion/ui/fleet/FleetManagementScreen.kt)
- Refinar las `FleetSummaryCard` para que se sientan más integradas y con mejor tipografía.

### [Mecánico — Selección de Vehículo](file:///C:/Users/gabob/Documents/GitHub/Proyecto01-Admin/app/src/main/java/com/example/proyecto01_administracion/ui/mechanic/)

#### [MODIFY] [VehicleSelectionScreen.kt](file:///C:/Users/gabob/Documents/GitHub/Proyecto01-Admin/app/src/main/java/com/example/proyecto01_administracion/ui/mechanic/VehicleSelectionScreen.kt)
- Reducir altura y padding vertical de las tarjetas de vehículo.
- Disminuir tamaño de fuente de la placa (`ABC-123`).
- Mover el indicador de estado al extremo derecho de la tarjeta.
- Aplicar nuevos colores semánticos a los estados.

### [Navegación Inferior](file:///C:/Users/gabob/Documents/GitHub/Proyecto01-Admin/app/src/main/java/com/example/proyecto01_administracion/ui/dashboard/)

#### [MODIFY] [BottomNavBar.kt](file:///C:/Users/gabob/Documents/GitHub/Proyecto01-Admin/app/src/main/java/com/example/proyecto01_administracion/ui/dashboard/BottomNavBar.kt) (y equivalentes para Mecánico/Encargado)
- Ajustar `NavigationBar` para que sea ligeramente menos alta.
- Aumentar tamaño de iconos y etiquetas de texto.
- Optimizar espaciado interno para evitar "espacio muerto".

### [Estabilidad & Estados](file:///C:/Users/gabob/Documents/GitHub/Proyecto01-Admin/app/src/main/java/com/example/proyecto01_administracion/MainActivity.kt)

#### [MODIFY] [MainActivity.kt](file:///C:/Users/gabob/Documents/GitHub/Proyecto01-Admin/app/src/main/java/com/example/proyecto01_administracion/MainActivity.kt)
- Cambiar `remember` por `rememberSaveable` en estados críticos como `userRole` e `isDarkTheme` para evitar que la BottomNav desaparezca o el rol se pierda al rotar la pantalla o cambiar el tema del sistema.

## Verification Plan

### Manual Verification
1.  **Light Theme**:
    - Verificar que los nuevos colores semánticos se aplican correctamente en alertas y estados.
    - Confirmar que los grises permiten distinguir mejor las tarjetas del fondo.
2.  **Bottom Navigation**:
    - Cambiar entre temas y verificar que la barra no desaparece.
    - Verificar que los iconos y textos son más grandes y la barra es más compacta.
3.  **Selección de Vehículo**:
    - Verificar la nueva distribución (Estado a la derecha) y tamaño de fuente de la placa.
4.  **Reportes**:
    - Confirmar que el botón fantasma ha sido eliminado.
