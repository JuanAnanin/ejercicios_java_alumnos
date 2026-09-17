-- =====================================================================
-- UF2405 - BLOQUE 1 - EJERCICIO 1
-- Modelo relacional de una clinica veterinaria
-- Criterios de evaluacion: CE2.5
--
-- Script de creacion completo. Probado sobre MySQL 8.
-- Se ejecuta con:  mysql -u root -p < 01_esquema_clinica.sql
-- =====================================================================

CREATE DATABASE clinica;
USE clinica;

-- ---------------------------------------------------------------------
-- Apartados 3, 4 y 5: tablas, claves y restricciones
-- ---------------------------------------------------------------------

CREATE TABLE cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre     VARCHAR(100) NOT NULL,
    telefono   VARCHAR(15),
    email      VARCHAR(100) UNIQUE
);

CREATE TABLE mascota (
    id_mascota INT AUTO_INCREMENT PRIMARY KEY,
    nombre     VARCHAR(50) NOT NULL,
    especie    VARCHAR(30) NOT NULL,
    fecha_nac  DATE,
    id_cliente INT NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente)
);

CREATE TABLE veterinario (
    id_veterinario INT AUTO_INCREMENT PRIMARY KEY,
    nombre         VARCHAR(100) NOT NULL,
    num_colegiado  VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE consulta (
    id_consulta    INT AUTO_INCREMENT PRIMARY KEY,
    fecha          DATETIME DEFAULT CURRENT_TIMESTAMP,
    motivo         VARCHAR(200),
    estado         VARCHAR(20) DEFAULT 'pendiente',
    id_mascota     INT NOT NULL,
    id_veterinario INT NOT NULL,
    FOREIGN KEY (id_mascota)     REFERENCES mascota(id_mascota),
    FOREIGN KEY (id_veterinario) REFERENCES veterinario(id_veterinario)
);

CREATE TABLE tratamiento (
    id_tratamiento INT AUTO_INCREMENT PRIMARY KEY,
    nombre         VARCHAR(100) NOT NULL,
    categoria      VARCHAR(50),
    precio         DECIMAL(8,2) NOT NULL CHECK (precio >= 0)
);

-- TABLA PUENTE que resuelve la relacion N:M entre consulta y tratamiento.
-- La clave primaria compuesta impide registrar dos veces el mismo
-- tratamiento en la misma consulta; para permitirlo habria que sustituirla
-- por un identificador propio y anadir la cantidad como columna normal.
CREATE TABLE consulta_tratamiento (
    id_consulta    INT,
    id_tratamiento INT,
    cantidad       INT DEFAULT 1 CHECK (cantidad > 0),
    PRIMARY KEY (id_consulta, id_tratamiento),
    FOREIGN KEY (id_consulta)    REFERENCES consulta(id_consulta),
    FOREIGN KEY (id_tratamiento) REFERENCES tratamiento(id_tratamiento)
);

-- ---------------------------------------------------------------------
-- Apartado 6: datos de prueba
-- 3 clientes, 5 mascotas, 2 veterinarios y 4 consultas como minimo.
-- ---------------------------------------------------------------------

INSERT INTO cliente (nombre, telefono, email) VALUES
    ('Ana Lopez',   '600111222', 'ana.lopez@correo.es'),
    ('Luis Gomez',  '600333444', 'luis.gomez@correo.es'),
    ('Marta Ruiz',  '600555666', 'marta.ruiz@correo.es');

INSERT INTO veterinario (nombre, num_colegiado) VALUES
    ('Dra. Elena Vidal', 'COL-1001'),
    ('Dr. Pablo Serra',  'COL-1002');

INSERT INTO mascota (nombre, especie, fecha_nac, id_cliente) VALUES
    ('Toby',   'Perro', '2019-04-12', 1),
    ('Misu',   'Gato',  '2021-09-30', 1),
    ('Rocky',  'Perro', '2018-01-05', 2),
    ('Nube',   'Gato',  '2022-06-18', 3),
    ('Kiwi',   'Ave',   '2023-02-01', 3);

INSERT INTO tratamiento (nombre, categoria, precio) VALUES
    ('Consulta general',   'consulta', 25.00),
    ('Vacuna polivalente', 'vacuna',   38.50),
    ('Extraccion dental',  'cirugia',  120.00),
    ('Analitica completa', 'pruebas',  45.00);

INSERT INTO consulta (fecha, motivo, estado, id_mascota, id_veterinario) VALUES
    ('2026-01-15 10:00:00', 'Revision anual',        'atendida',  1, 1),
    ('2026-02-03 12:30:00', 'Cojera pata trasera',   'atendida',  1, 2),
    ('2026-02-20 09:15:00', 'Vacunacion',            'atendida',  3, 1),
    ('2026-08-30 17:00:00', 'Perdida de apetito',    'pendiente', 4, 2);

INSERT INTO consulta_tratamiento (id_consulta, id_tratamiento, cantidad) VALUES
    (1, 1, 1),
    (1, 2, 1),
    (2, 1, 1),
    (2, 4, 1),
    (3, 2, 1);
