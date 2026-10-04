# Guía del prototipo de Presta U

Fuente: [PDF exportado de Figma](Prototipo_Final_Presta_U.pdf), recibido el 4 de octubre de 2026. Contiene 22 pantallas. La tabla ubica las pantallas relacionadas con cada historia; su presencia no demuestra que los criterios estén implementados ni que los enlaces funcionen.

## Índice de pantallas

| Página PDF | Pantalla | Historias relacionadas |
|---|---|---|
| 1 | Inicio del solicitante | HU-03, HU-07, HU-09 |
| 2 | Catálogo de equipos | HU-03, HU-04 |
| 3 | Mis solicitudes | HU-07 |
| 4 | Mi historial de préstamos | HU-09 |
| 5 | Mi perfil (solicitante) | HU-10, HU-20 |
| 6 | Detalle del equipo | HU-05 |
| 7 | Detalle de solicitud | HU-07, HU-08 |
| 8 | Nueva solicitud | HU-06 |
| 9 | Iniciar sesión | HU-02 |
| 10 | Panel del encargado | HU-11, HU-17 |
| 11 | Solicitudes recibidas | HU-11 |
| 12 | Préstamos | HU-13, HU-14, HU-17 |
| 13 | Inventario de equipos | HU-15 |
| 14 | Usuarios y roles | HU-18 |
| 15 | Reportes de préstamos | HU-19 |
| 16 | Revisar solicitud | HU-12 |
| 17 | Registrar entrega o devolución | HU-13, HU-14 |
| 18 | Nuevo equipo | HU-15 |
| 19 | Mi cuenta y configuración (encargado) | HU-20 |
| 20 | Crear cuenta | HU-01 |
| 21 | Categorías | HU-16 |
| 22 | Editar equipo | HU-15 |

## Recorridos previstos para la demostración

1. Solicitante: iniciar sesión → inicio → catálogo → detalle → nueva solicitud → mis solicitudes → detalle y cancelación de pendiente. Revisar también historial y perfil.
2. Encargado: iniciar sesión → panel → solicitudes recibidas → revisión → préstamos → entrega y devolución. Revisar inventario, categorías, usuarios, reportes y configuración.
3. Cuenta nueva: iniciar sesión → crear cuenta → volver a iniciar sesión.

## Observaciones de coherencia por resolver en Figma

- **HU-19, página 15:** el filtro indica septiembre de 2026, pero la bitácora muestra dos movimientos del 1 de octubre. Ajustar el periodo o las filas de ejemplo.
- **HU-14, página 17:** la exportación muestra la pestaña Entrega. Verificar que Devolución muestre condición de entrada, confirmación y opción de mantenimiento por daño.
- **HU-02, página 9:** los dos botones de acceso por rol facilitan la demostración. En la implementación, el servidor debe determinar el rol desde la cuenta autenticada; el usuario no debe poder asignárselo al entrar.
- **HU-15, página 18:** el alta debe iniciar el equipo en En servicio, según el criterio de aceptación. Aclarar ese valor en la pantalla Nuevo equipo.
- **HU-09/HU-17, páginas 4 y 12:** fijar la fecha de referencia de los datos de ejemplo; el préstamo con vencimiento el 2 de octubre figura Activo. Si se presenta con fecha posterior al vencimiento y sin devolución, debe figurar Atrasado.
- Verificar en Figma las confirmaciones de cancelación y cambio de rol, mensajes de error y estados vacíos. El PDF no permite comprobar las interacciones ni restricciones de edición.

El PDF original se conserva sin modificaciones. Estas observaciones orientan la revisión del equipo y no cambian las historias ni sus prioridades.
