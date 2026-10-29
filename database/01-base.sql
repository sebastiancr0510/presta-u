-- Base inicial de P1. Ejecutar una sola vez en una base vacía. No elimina datos.
CREATE DATABASE IF NOT EXISTS presta_u CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE presta_u;

CREATE TABLE rol (
  id_rol INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(30) NOT NULL UNIQUE,
  PRIMARY KEY (id_rol)
) ENGINE = InnoDB;

CREATE TABLE usuario (
  id_usuario INT NOT NULL AUTO_INCREMENT,
  id_rol INT NOT NULL,
  identificacion VARCHAR(20) NOT NULL UNIQUE,
  nombre VARCHAR(60) NOT NULL,
  correo VARCHAR(75) NOT NULL UNIQUE,
  telefono VARCHAR(25),
  clave_hash VARCHAR(512) NOT NULL,
  idioma VARCHAR(5) NOT NULL,
  activo BOOLEAN NOT NULL,
  PRIMARY KEY (id_usuario),
  FOREIGN KEY (id_rol) REFERENCES rol(id_rol)
) ENGINE = InnoDB;

CREATE TABLE categoria (
  id_categoria INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(50) NOT NULL UNIQUE,
  activa BOOLEAN NOT NULL,
  PRIMARY KEY (id_categoria)
) ENGINE = InnoDB;

CREATE TABLE equipo (
  id_equipo INT NOT NULL AUTO_INCREMENT,
  id_categoria INT NOT NULL,
  codigo VARCHAR(20) NOT NULL UNIQUE,
  nombre VARCHAR(80) NOT NULL,
  descripcion VARCHAR(300),
  estado_operativo VARCHAR(20) NOT NULL,
  ruta_imagen VARCHAR(1024),
  activo BOOLEAN NOT NULL,
  PRIMARY KEY (id_equipo),
  FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria)
) ENGINE = InnoDB;

CREATE TABLE mantenimiento (
  id_mantenimiento INT NOT NULL AUTO_INCREMENT,
  id_equipo INT NOT NULL,
  motivo VARCHAR(150) NOT NULL,
  fecha_inicio DATETIME NOT NULL,
  fecha_fin DATETIME,
  observacion VARCHAR(300),
  PRIMARY KEY (id_mantenimiento),
  FOREIGN KEY (id_equipo) REFERENCES equipo(id_equipo)
) ENGINE = InnoDB;

USE presta_u;

INSERT INTO rol (id_rol, nombre) VALUES (1, 'Solicitante'), (2, 'Encargado');

INSERT INTO usuario (id_rol, identificacion, nombre, correo, telefono, clave_hash, idioma, activo)
VALUES (1, '119940174', 'Pablo', 'pablo.prueba@prestau.test', '8888-5555', 'pendiente', 'es', 1);

INSERT INTO categoria (nombre, activa) VALUES
    ('Computación', 1),
    ('Calculadoras', 1),
    ('Audio y video', 1),
    ('Redes', 1),
    ('Proyectores', 0);

INSERT INTO equipo (id_categoria, codigo, nombre, descripcion, estado_operativo, ruta_imagen, activo) VALUES
    ((SELECT id_categoria FROM categoria WHERE nombre = 'Computación'),
     'LAP-014', 'Laptop', 'Laptop de 14 pulgadas con 16 GB de RAM.', 'En servicio',
     'https://m.media-amazon.com/images/I/7156U9adB3L._AC_SL1500_.jpg', 1),
    ((SELECT id_categoria FROM categoria WHERE nombre = 'Computación'),
     'LAP-015', 'Laptop HP Pavilion', 'Laptop de 15 pulgadas con 8 GB de RAM.', 'En servicio',
     'https://m.media-amazon.com/images/I/81TNfcvPbWL._AC_SL1500_.jpg', 1),
    ((SELECT id_categoria FROM categoria WHERE nombre = 'Computación'),
     'TAB-002', 'Tablet', 'Tablet de 10 pulgadas para lectura y apuntes.', 'En servicio',
     'https://m.media-amazon.com/images/I/61ssqs-SY3L._AC_SL1500_.jpg', 1),
    ((SELECT id_categoria FROM categoria WHERE nombre = 'Computación'),
     'TAB-009', 'Tablet Samsung Galaxy Tab', 'Tablet de 10 pulgadas.', 'En servicio',
     'https://image-us.samsung.com/SamsungUS/home/mobile/tablets/all-tablets/092923/s9-fe/gray/1.jpg', 0),
    ((SELECT id_categoria FROM categoria WHERE nombre = 'Calculadoras'),
     'CAL-007', 'Calculadora científica', 'Calculadora con funciones trigonométricas y logarítmicas.', 'En servicio',
     'https://m.media-amazon.com/images/I/71myDtrYVkL.jpg', 1),
    ((SELECT id_categoria FROM categoria WHERE nombre = 'Calculadoras'),
     'CAL-008', 'Calculadora gráfica', 'Calculadora con pantalla para graficar funciones.', 'En servicio',
     'https://m.media-amazon.com/images/I/719v5Oq2KTL._AC_.jpg', 1),
    ((SELECT id_categoria FROM categoria WHERE nombre = 'Audio y video'),
     'CAM-003', 'Cámara Canon EOS', 'Cámara réflex para fotografía y video.', 'En servicio',
     'https://i5.walmartimages.com/asr/6f91b7b7-d8d2-460f-80f5-65f5817d3db5.c2a7b583df36bda857383bf631ad17e9.jpeg', 1),
    ((SELECT id_categoria FROM categoria WHERE nombre = 'Audio y video'),
     'MIC-004', 'Micrófono Shure SM58', 'Micrófono dinámico para voz.', 'Mantenimiento',
     'https://d1aeri3ty3izns.cloudfront.net/media/47/479589/1200/preview.jpg', 1),
    ((SELECT id_categoria FROM categoria WHERE nombre = 'Redes'),
     'SWI-005', 'Switch TP-Link 8 puertos', 'Switch de red gigabit de 8 puertos.', 'En servicio',
     'https://mexx-img-2019.s3.amazonaws.com/37625_2.jpeg', 1),
    ((SELECT id_categoria FROM categoria WHERE nombre = 'Proyectores'),
     'PRO-006', 'Proyector Epson PowerLite', 'Proyector para salones.', 'En servicio',
     'https://cuscoinformatico.com/storage-proxy/PowerLite%20FH52+.jpg', 0);

INSERT INTO mantenimiento (id_equipo, motivo, fecha_inicio, observacion)
VALUES ((SELECT id_equipo FROM equipo WHERE codigo = 'MIC-004'),
        'Cable de audio dañado', NOW(), 'Pendiente de revisión.');
        
