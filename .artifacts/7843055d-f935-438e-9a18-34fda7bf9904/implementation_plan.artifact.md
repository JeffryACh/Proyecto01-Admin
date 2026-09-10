# Plan de Corrección de Regresiones — Menú "Más", Flota y Navegación

Este plan aborda los problemas de UI y navegación reportados, asegurando que el menú "Más" abra desde la derecha, se cierre correctamente, y que la gestión de flota recupere sus filtros y estados visuales funcionales.

## User Review Required

> [!IMPORTANT]
> Se modificará la estructura de `MainActivity.kt` para usar `CompositionLocalProvider` con `LayoutDirection.Rtl` alrededor del Drawer de "Más", lo que garantiza que abra desde el lado derecho.

## Proposed Changes

### [MainActivity & Navegación](file:///C:/Users/gabob/Documents/GitHub/Proyecto01-Admin/app/src/main/java/com/example/proyecto01_administracion/MainActivity.kt)

#### [MODIFY] [MainActivity.kt](file:///C:/Users/gabob/Documents/GitHub/Proyecto01-Admin/app/src/main/java/com/example/proyecto01_administracion/MainActivity.kt)
- **Menú "Más" (Derecha)**: Envolver el `ModalNavigationDrawer` de "Más" en `LocalLayoutDirection.Rtl` para que abra desde la derecha.
- **Gestión de Estado**: Asegurar que `moreDrawerState` se cierre al navegar, al cerrar sesión y mediante el botón Atrás del sistema (`BackHandler`).
- **Logout**: Limpiar `userRole`, cerrar ambos drawers (`profileDrawerState` y `moreDrawerState`) y navegar a `login`.
- **Opciones del Menú**: Actualizar `MoreOptionsMenu` para incluir rutas válidas y completas según el rol.
- **Bottom Navigation**: Asegurar que la altura sea consistente y no cause espacios negros excesivos (revisar insets y paddings).

### [Gestión de Flota](file:///C:/Users/gabob/Documents/GitHub/Proyecto01-Admin/app/src/main/java/com/example/proyecto01_administracion/ui/fleet/FleetManagementScreen.kt)

#### [MODIFY] [FleetManagementScreen.kt](file:///C:/Users/gabob/Documents/GitHub/Proyecto01-Admin/app/src/main/java/com/example/proyecto01_administracion/ui/fleet/FleetManagementScreen.kt)
- **Filtros**: Restaurar los filtros "Todos", "Al día", "Próximos", "Atrasados".
- **Lógica de Filtrado**: Implementar el filtrado real de la lista de vehículos basado en el estado seleccionado.
- **Visualización**: Actualizar los indicadores de estado en las tarjetas de vehículo y el resumen superior para que coincidan con el estilo de las Alertas del Conductor.

### [Bottom Navigation Components](file:///C:/Users/gabob/Documents/GitHub/Proyecto01-Admin/app/src/main/java/com/example/proyecto01_administracion/ui/dashboard/)

#### [MODIFY] [BottomNavBar.kt](file:///C:/Users/gabob/Documents/GitHub/Proyecto01-Admin/app/src/main/java/com/example/proyecto01_administracion/ui/dashboard/BottomNavBar.kt)
#### [MODIFY] [MechanicBottomNavBar.kt](file:///C:/Users/gabob/Documents/GitHub/Proyecto01-Admin/app/src/main/java/com/example/proyecto01_administracion/ui/dashboard/MechanicBottomNavBar.kt)
#### [MODIFY] [FleetBottomNavBar.kt](file:///C:/Users/gabob/Documents/GitHub/Proyecto01-Admin/app/src/main/java/com/example/proyecto01_administracion/ui/dashboard/FleetBottomNavBar.kt)
- **Estandarización**: Asegurar que el componente `NavigationBar` tenga un tamaño y padding consistente, evitando espacios negros duplicados en combinación con los insets del sistema.

## Verification Plan

### Manual Verification
1.  **Menú "Más"**:
    - Verificar que abre desde el lado DERECHO al presionar "Más" en la BottomNav.
    - Confirmar que se cierra al tocar fuera, presionar Atrás o seleccionar una opción.
    - Verificar que al cerrar sesión el menú no queda abierto sobre la pantalla de Login.
2.  **Gestión de Flota**:
    - Seleccionar cada filtro (Todos, Al día, Próximos, Atrasados) y verificar que la lista se actualiza correctamente.
    - Comprobar que los colores y etiquetas de estado son correctos.
3.  **Bottom Navigation**:
    - Navegar por varias pantallas y verificar que la altura de la barra inferior es constante y no deja espacios negros excesivos.
4.  **Logout**:
    - Realizar el flujo de Logout y verificar que se llega a la pantalla de Login limpia (sin barras de navegación ni menús laterales activos).
