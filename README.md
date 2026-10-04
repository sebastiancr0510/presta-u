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
- [Seguimiento de la entrega](PENDIENTES.md).

## Prototipo
El documento identifica Figma como herramienta del prototipo actual de ambos roles. [Archivo de diseño indicado en el avance](https://www.figma.com/design/V1pfsM94xh7CFtefzMbpg7/Prototipo-Final-Presta-U?node-id=0-1). Los permisos y la navegación del enlace deben comprobarse antes de entregar. Los diagramas de navegación y datos están incorporados en el PDF.

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

## Organización del repositorio
Esta entrega mantiene los documentos en la raíz. Al comenzar el desarrollo se incorporarán la estructura de Spring Boot, configuración de ejemplo sin secretos e instrucciones reproducibles de ejecución.

## Ramas
`main`: entrega estable; `develop`: integración; `feature/hu-XX-descripcion`: funciones; `docs/descripcion`: documentación. Consultar CONTRIBUTING.md para el flujo de revisión. La incorporación de Fiorella está pendiente de identificar su usuario de GitHub y de que acepte la invitación.
