# Acuerdo básico de trabajo por ramas

1. `main` conserva las entregas estables y revisadas.
2. `develop` reúne el trabajo de ambos integrantes.
3. Crear desde `develop` una rama `feature/hu-XX-descripcion` para cada función o `docs/descripcion` para documentación.
4. Hacer commits pequeños con mensajes claros, por ejemplo `docs: actualizar criterios de HU-06`.
5. Abrir un pull request hacia `develop`, indicando historia, cambios y comprobaciones realizadas.
6. El otro integrante revisa el cambio antes de integrarlo. Resolver conflictos y comprobar el flujo afectado.
7. Para una entrega, abrir un pull request de `develop` a `main` y revisar documentos, enlaces y funcionamiento disponible.
8. No subir contraseñas, tokens, claves ni archivos con credenciales. La configuración local debe quedar fuera del repositorio.
9. Distribución del avance de implementación: Fiorella (P1) entrega catálogo e inventario; Sebastián (P2) implementa HU-06, HU-07, HU-08, HU-09 y HU-16. Ambos revisan coherencia entre historias, pantallas y datos.

Este es el acuerdo inicial propuesto para el equipo. Las reglas descritas no equivalen a protecciones automáticas de ramas en GitHub.
