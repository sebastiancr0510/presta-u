-- Ejecutar UNA SOLA VEZ después de database/01-base.sql y 02-solicitudes.sql.
-- Exclusivo para una base de demostración. No borra ni cambia registros existentes.
USE presta_u;
START TRANSACTION;
INSERT INTO usuario (id_rol, identificacion, nombre, correo, clave_hash, idioma, activo)
VALUES (1, 'DEMO-OTRO', 'Otro solicitante', 'otro@prestau.test', 'pendiente', 'es', 1);
SET @propio = (SELECT id_usuario FROM usuario WHERE correo = 'pablo.prueba@prestau.test');
SET @otro = (SELECT id_usuario FROM usuario WHERE correo = 'otro@prestau.test');
INSERT INTO solicitud (id_usuario,id_equipo,inicio,fin,motivo,estado,creada,observacion) VALUES (@propio,(SELECT id_equipo FROM equipo WHERE codigo='LAP-014'),DATE_ADD(NOW(), INTERVAL 3 DAY),DATE_ADD(NOW(), INTERVAL 4 DAY),'Actividad académica de demostración','PENDIENTE',DATE_SUB(NOW(), INTERVAL 10 DAY),NULL);
INSERT INTO solicitud (id_usuario,id_equipo,inicio,fin,motivo,estado,creada,observacion) VALUES (@propio,(SELECT id_equipo FROM equipo WHERE codigo='LAP-015'),DATE_ADD(NOW(), INTERVAL 5 DAY),DATE_ADD(NOW(), INTERVAL 6 DAY),'Actividad académica de demostración','APROBADA',DATE_SUB(NOW(), INTERVAL 10 DAY),'Reserva aprobada');
INSERT INTO solicitud (id_usuario,id_equipo,inicio,fin,motivo,estado,creada,observacion) VALUES (@propio,(SELECT id_equipo FROM equipo WHERE codigo='TAB-002'),DATE_ADD(NOW(), INTERVAL 3 DAY),DATE_ADD(NOW(), INTERVAL 4 DAY),'Actividad académica de demostración','RECHAZADA',DATE_SUB(NOW(), INTERVAL 10 DAY),'Equipo requerido para una actividad del laboratorio');
INSERT INTO solicitud (id_usuario,id_equipo,inicio,fin,motivo,estado,creada,observacion) VALUES (@propio,(SELECT id_equipo FROM equipo WHERE codigo='CAL-007'),DATE_ADD(NOW(), INTERVAL 4 DAY),DATE_ADD(NOW(), INTERVAL 5 DAY),'Actividad académica de demostración','CANCELADA',DATE_SUB(NOW(), INTERVAL 10 DAY),NULL);
INSERT INTO solicitud (id_usuario,id_equipo,inicio,fin,motivo,estado,creada,observacion) VALUES (@propio,(SELECT id_equipo FROM equipo WHERE codigo='CAL-008'),DATE_ADD(NOW(), INTERVAL -1 DAY),DATE_ADD(NOW(), INTERVAL 2 DAY),'Actividad académica de demostración','APROBADA',DATE_SUB(NOW(), INTERVAL 10 DAY),'Entrega realizada');
INSERT INTO prestamo (id_solicitud,entrega,vencimiento,devolucion) VALUES (LAST_INSERT_ID(),DATE_ADD(NOW(), INTERVAL -1 DAY),DATE_ADD(NOW(), INTERVAL 2 DAY),NULL);
INSERT INTO solicitud (id_usuario,id_equipo,inicio,fin,motivo,estado,creada,observacion) VALUES (@propio,(SELECT id_equipo FROM equipo WHERE codigo='CAM-003'),DATE_ADD(NOW(), INTERVAL -5 DAY),DATE_ADD(NOW(), INTERVAL -2 DAY),'Actividad académica de demostración','APROBADA',DATE_SUB(NOW(), INTERVAL 10 DAY),'Entrega realizada');
INSERT INTO prestamo (id_solicitud,entrega,vencimiento,devolucion) VALUES (LAST_INSERT_ID(),DATE_ADD(NOW(), INTERVAL -5 DAY),DATE_ADD(NOW(), INTERVAL -2 DAY),NULL);
INSERT INTO solicitud (id_usuario,id_equipo,inicio,fin,motivo,estado,creada,observacion) VALUES (@propio,(SELECT id_equipo FROM equipo WHERE codigo='SWI-005'),DATE_ADD(NOW(), INTERVAL -8 DAY),DATE_ADD(NOW(), INTERVAL -6 DAY),'Actividad académica de demostración','APROBADA',DATE_SUB(NOW(), INTERVAL 10 DAY),'Préstamo finalizado');
INSERT INTO prestamo (id_solicitud,entrega,vencimiento,devolucion) VALUES (LAST_INSERT_ID(),DATE_ADD(NOW(), INTERVAL -8 DAY),DATE_ADD(NOW(), INTERVAL -6 DAY),DATE_SUB(NOW(), INTERVAL 6 DAY));
INSERT INTO solicitud (id_usuario,id_equipo,inicio,fin,motivo,estado,creada) VALUES (@otro,(SELECT id_equipo FROM equipo WHERE codigo='LAP-014'),DATE_ADD(NOW(), INTERVAL 10 DAY),DATE_ADD(NOW(), INTERVAL 11 DAY),'Solicitud de otro usuario','PENDIENTE',NOW());
COMMIT;
