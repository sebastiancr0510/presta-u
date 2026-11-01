USE presta_u;
CREATE TABLE IF NOT EXISTS solicitud (
 id_solicitud INT AUTO_INCREMENT PRIMARY KEY,
 id_usuario INT NOT NULL,
 id_equipo INT NOT NULL,
 inicio DATETIME NOT NULL,
 fin DATETIME NOT NULL,
 motivo VARCHAR(300) NOT NULL,
 estado VARCHAR(20) NOT NULL,
 creada DATETIME NOT NULL,
 observacion VARCHAR(300),
 FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
 FOREIGN KEY (id_equipo) REFERENCES equipo(id_equipo),
 CHECK (fin > inicio),
 CHECK (estado IN ('PENDIENTE','APROBADA','RECHAZADA','CANCELADA')),
 INDEX idx_reserva (id_equipo, estado, inicio, fin)
) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS prestamo (
 id_prestamo INT AUTO_INCREMENT PRIMARY KEY,
 id_solicitud INT NOT NULL UNIQUE,
 entrega DATETIME NOT NULL,
 vencimiento DATETIME NOT NULL,
 devolucion DATETIME,
 FOREIGN KEY (id_solicitud) REFERENCES solicitud(id_solicitud),
 CHECK (vencimiento > entrega),
 CHECK (devolucion IS NULL OR devolucion >= entrega)
) ENGINE=InnoDB;
