# Presta U
Sistema web para gestionar préstamos de equipos universitarios. Centraliza el catálogo, las solicitudes y su seguimiento para una biblioteca o laboratorio universitario como cliente potencial.

## Equipo
- Sebastián Campos Rojas — P2.
- Fiorella Maria Piña Solano — P1.
- Universidad Fidélitas, SC-403 Desarrollo de Aplicaciones Web y Patrones.
- Profesor: Allam Mauricio Fernández Rivera.

## Avance de implementación
P1 implementó inicio, catálogo, búsqueda, detalle, inventario, mantenimiento y cambio de idioma. P2 incorpora categorías, creación y consulta de solicitudes, cancelación e historial de préstamos.

**Pendiente: login, registro y permisos por roles.** Inventario y categorías están abiertos. Se usa el usuario activo definido por `prestau.usuario-demo-id` (por defecto 1), resuelto en el servidor. No se recibe un identificador de usuario desde el formulario. Esto permite demostrar el filtrado de registros, pero todos los visitantes comparten ese usuario hasta incorporar autenticación; no es aislamiento real entre sesiones.

## Cómo ejecutar
Requisitos: JDK 21, Maven 3.9 o superior y MySQL 8.0/8.4. El proyecto Maven está dentro de `presta-u/`.

1. Obtener el código: `git clone https://github.com/sebastiancr0510/presta-u.git`, entrar al repositorio y ejecutar `git switch develop` y `git pull origin develop`.
2. En MySQL Workbench, ejecutar `database/01-base.sql` **solo si la base está vacía**. Conserva la estructura y datos de P1; no elimina bases ni usuarios. Si ya tiene la base de P1, omitir este paso.
3. Ejecutar `database/02-solicitudes.sql` para agregar las tablas de solicitudes y préstamos.
4. Opcional, en una base de demostración con los datos de P1: ejecutar **una sola vez** `presta-u/src/main/resources/data-solicitudes.sql`. Incluye estados pendiente/aprobada/rechazada/cancelada, préstamos activo/atrasado/devuelto y una solicitud ajena. Las fechas son relativas al momento de ejecución. No se carga automáticamente al iniciar.
5. Crear o utilizar un usuario local de MySQL con permisos SELECT, INSERT, UPDATE y DELETE sobre `presta_u.*`. La creación de tablas se realiza con una cuenta administradora local. No compartir ni subir la contraseña.
6. Entrar a la carpeta `presta-u/`. Copiar `application-local.properties.example` como `application-local.properties` y colocar su usuario y contraseña MySQL. Este archivo está excluido de Git. `src/main/resources/application.properties` contiene la configuración compartida y acepta `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `SERVER_PORT` y `DEMO_USER_ID`.
7. Ejecutar `mvn spring-boot:run` y abrir <http://localhost:8080/equipos>.

La base se llama `presta_u`. En NetBeans, abrir como proyecto Maven la carpeta interna `presta-u`, seleccionar JDK 21 y establecer esa carpeta como directorio de ejecución para cargar el archivo local. Usar la zona horaria local de Costa Rica para las fechas de demostración.

## Recorrido de P2
- `/categorias`: crear, renombrar e inactivar; nombre único y bloqueo si hay equipos activos.
- `/equipos/{id}` → Solicitar → `/solicitudes/nueva`: equipo, fechas y motivo. Consulta JPQL de intervalos aprobados; permite intervalos contiguos, impide duplicados pendientes y préstamos sin devolver.
- `/solicitudes` → `/solicitudes/{id}`: solicitudes del usuario demo y detalle. Un ID ajeno devuelve 404.
- Detalle pendiente → Cancelar → modal → confirmar: actualiza a CANCELADA, conserva el registro y vuelve a comprobar el estado en la base.
- `/prestamos`: historial propio, estado calculado a partir de entrega, vencimiento y devolución.

Todas las vistas reutilizan `general/fragmentos :: head`, `header` y `footer` de P1.

## Paquetes y datos
- `controller`: rutas y formularios MVC.
- `service`: validaciones y operaciones transaccionales.
- `repository`: acceso JPA y consultas JPQL.
- `domain`: entidades y formulario de solicitud.
- `resources/templates`: vistas Thymeleaf; `static/css`: estilos de P1.
- `database`: base inicial y ampliación de P2.
- `src/test`: pruebas de reglas y navegación HTTP.

Entidades implementadas: Rol, Usuario, Categoria, Equipo, Mantenimiento, Solicitud y Prestamo. Solicitud tiene relaciones ManyToOne con Usuario y Equipo. Prestamo corresponde a una solicitud entregada. EstadoSolicitud contiene PENDIENTE, APROBADA, RECHAZADA y CANCELADA. Bitácora continúa pendiente.

## Estado por historia
| HU | Estado del avance |
|---|---|
| 01 | Pendiente: registro |
| 02 | Pendiente: login y cierre de sesión |
| 03 | Implementada por P1: catálogo |
| 04 | Implementada por P1: búsqueda y filtro |
| 05 | Implementada por P1; P2 conectó el botón Solicitar |
| 06 | Implementada por P2 con usuario demo |
| 07 | Implementada por P2 con usuario demo |
| 08 | Implementada por P2 con usuario demo |
| 09 | Implementada por P2 con usuario demo |
| 10 | Pendiente: perfil |
| 11 | Pendiente: solicitudes del encargado |
| 12 | Pendiente: aprobación y rechazo |
| 13 | Pendiente: registro de entrega |
| 14 | Pendiente: registro de devolución |
| 15 | Parcial P1: inventario/mantenimiento; faltan restricciones ligadas a reservas/préstamos y roles |
| 16 | Implementada por P2; pendiente restringir por rol |
| 17 | Pendiente: consulta global del encargado |
| 18 | Pendiente: usuarios y roles |
| 19 | Pendiente: reportes y bitácora |
| 20 | Parcial P1: ES/EN; cambio de contraseña y traducción de nuevas pantallas pendientes |

## Pruebas
`mvn test` usa H2 en modo MySQL, aislado de la base local. Las pruebas cubren validaciones, reservas superpuestas y contiguas, duplicados, registros ajenos, cancelación, categorías e historial; además solicitan las páginas por HTTP para comprobar Thymeleaf.

GitHub Actions está preparado para ejecutar las mismas pruebas con JDK 21 y MySQL 8.4 temporal, y ejecutar los tres scripts SQL en una base nueva. Su resultado debe consultarse en la pestaña Actions; no equivale a una ejecución en la computadora de cada integrante.

## Documentación y ramas
- [Documento del avance](Avance_1_SC-403.pdf).
- [Backlog y prioridades](BACKLOG.md).
- [Acuerdo de ramas](CONTRIBUTING.md).

`main`: entrega; `develop`: integración; una rama `feature/hu-XX` por historia. Los PR de P2 se dirigen a `develop`; el PR final se dirige a `main`. Al iniciar este trabajo no había PR de implementación de P1 para aprobar; no se registra una aprobación ficticia. La revisión cruzada por la compañera sigue siendo parte del acuerdo del equipo.

## Prototipo
- [Prototipo navegable en Figma](https://www.figma.com/proto/V1pfsM94xh7CFtefzMbpg7/Prototipo-Final-Presta-U?node-id=2-3&p=f&t=oCkg5Wy5G1WKbrLY-0&scaling=min-zoom&content-scaling=fixed&page-id=0%3A1&starting-point-node-id=2%3A3).
- [Archivo de diseño en Figma](https://www.figma.com/design/V1pfsM94xh7CFtefzMbpg7/Prototipo-Final-Presta-U?node-id=0-1).
- [Exportación PDF de las 22 pantallas](Prototipo_Final_Presta_U.pdf): copia suministrada por el equipo el 4 de octubre de 2026, consultable sin acceso a Figma.
- [Guía de pantallas y revisión](PROTOTIPO.md): correspondencia con historias y aspectos por comprobar.

El diseño incluye pantallas del solicitante y del encargado. El PDF es una evidencia visual estática; la navegación interactiva se consulta en Figma y queda pendiente de verificación. Los mapas de navegación y datos están en el documento del avance.
