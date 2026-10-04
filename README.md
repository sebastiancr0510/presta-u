# Presta U
Sistema web para la gestión de préstamos de equipos universitarios.


## Información académica
- Universidad Fidélitas — Desarrollo de Aplicaciones Web y Patrones (SC-403).
- Profesor: Allam Mauricio Fernández Rivera.
- Equipo: Sebastián Campos Rojas y Fiorella Maria Piña Solano.


## Problema y objetivo
Una unidad universitaria que gestiona préstamos mediante mensajes, papel o archivos separados puede tener solicitudes duplicadas, dificultades para consultar disponibilidad y falta de seguimiento de entregas y devoluciones. Presta U centralizará ese proceso para un cliente potencial: una biblioteca, laboratorio o unidad de apoyo académico. La necesidad deberá validarse con la unidad que adopte la solución.


## Estado del proyecto
Avance de análisis y diseño basado en el documento del equipo fechado el 1 de octubre de 2026. Incluye 20 historias con criterios de aceptación y prioridades, prototipo, navegación y modelo preliminar. Aún no hay aplicación ejecutable ni pruebas de implementación.


## Documentación
- [Avance 1 SC-403](Avance_1_SC-403.pdf): documento de referencia del equipo; contiene los criterios completos de las 20 historias, mapa de navegación y modelo.
- [Backlog inicial](BACKLOG.md): resumen de historias y prioridades.
- [Acuerdo básico de trabajo por ramas](CONTRIBUTING.md).


## Prototipo
- [Prototipo navegable en Figma](https://www.figma.com/proto/V1pfsM94xh7CFtefzMbpg7/Prototipo-Final-Presta-U?node-id=2-3&p=f&t=oCkg5Wy5G1WKbrLY-0&scaling=min-zoom&content-scaling=fixed&page-id=0%3A1&starting-point-node-id=2%3A3).
- [Archivo de diseño en Figma](https://www.figma.com/design/V1pfsM94xh7CFtefzMbpg7/Prototipo-Final-Presta-U?node-id=0-1).
- [Exportación PDF de las 22 pantallas](Prototipo_Final_Presta_U.pdf): copia suministrada por el equipo el 4 de octubre de 2026, consultable sin acceso a Figma.
- [Guía de pantallas y revisión](PROTOTIPO.md): correspondencia con historias y aspectos por comprobar.


El diseño incluye pantallas del solicitante y del encargado. El PDF es una evidencia visual estática; la navegación interactiva se consulta en Figma y queda pendiente de verificación. Los mapas de navegación y datos están en el documento del avance.


## Roles y alcance
- **Solicitante:** estudiantes y docentes; registro, acceso, catálogo, búsqueda, solicitudes, seguimiento, cancelación de pendientes, historial y perfil.
- **Encargado:** revisión de solicitudes, entregas, devoluciones, inventario, mantenimiento, categorías, usuarios, reportes y bitácora.
- Cada solicitud corresponde a una unidad física y un intervalo de fecha y hora.
- Estados de solicitud: Pendiente, Aprobada, Rechazada y Cancelada. Aprobar reserva; entregar crea el préstamo; devolver lo cierra.
- No se permiten reservas aprobadas superpuestas. Dos reservas contiguas pueden compartir el instante de fin e inicio.
- La disponibilidad y los permisos se validan en el servidor. Aprobar, entregar y devolver son operaciones transaccionales con registro en bitácora.
- Se excluyen pagos, multas automáticas, renovaciones, correos automáticos e integración institucional.


## Tecnología prevista
Java, Spring Boot, Thymeleaf, Bootstrap e Hibernate/JPA sobre una base relacional. Capas Controller, Service, Repository, Domain y vistas; consultas JPQL e interfaz en español e inglés. Motor de base de datos, versiones y pasos de instalación pendientes de definir al iniciar la implementación.


## Modelo preliminar
Ocho entidades: Rol, Usuario, Categoría, Equipo, Mantenimiento, Solicitud, Préstamo y Bitácora. El PDF presenta sus relaciones. Se conservan los registros históricos y las contraseñas se almacenarán como hash.


## Trabajo en equipo

El flujo de ramas y revisión se define en [CONTRIBUTING.md](CONTRIBUTING.md).

## Pendientes de la entrega

- Agregar a Fiorella como colaboradora cuando se confirme su usuario y acepte la invitación.
- Revisar las observaciones de [PROTOTIPO.md](PROTOTIPO.md) y comprobar permisos y navegación en Figma.
- Dar acceso al profesor al repositorio privado o acordar su publicación.
- Grabar el video con ambos integrantes y añadir el enlace.
- Revisar el avance en equipo y validar el problema con el cliente potencial.

La siguiente etapa es implementar las historias prioritarias y documentar instalación y pruebas.
