-- =====================================================================
-- UF2405 - BLOQUE 1 - EJERCICIO 2
-- Evolucion del esquema: ALTER, vistas y disparadores
-- Criterios de evaluacion: CE2.6
--
-- Se ejecuta sobre la base de datos ya creada por 01_esquema_clinica.sql.
-- ATENCION: las sentencias DDL provocan un commit implicito en la mayoria
-- de SGBD, asi que no se pueden deshacer con ROLLBACK. Probar siempre
-- sobre una copia, nunca en produccion.
-- =====================================================================

USE clinica;

-- 1. Anadir columna sin perder datos.
--    Las filas existentes toman el valor por defecto, cero.
ALTER TABLE mascota ADD COLUMN peso DECIMAL(5,2) DEFAULT 0;

-- 1-bis. Esta columna NO aparece en el enunciado del ejercicio 2, pero el
--        ejercicio 8 (transacciones) la actualiza. Si no se crea aqui, la
--        transaccion falla con "Unknown column 'ultima_visita'". Se anade
--        para que el bloque 3 pueda ejecutarse sobre este mismo esquema.
ALTER TABLE mascota ADD COLUMN ultima_visita DATETIME NULL;

-- 2. Ampliar la longitud de una columna existente.
ALTER TABLE consulta MODIFY COLUMN motivo VARCHAR(500);

-- 3. Vista de consultas pendientes.
CREATE VIEW consultas_pendientes AS
SELECT m.nombre AS mascota, c.nombre AS dueno, co.fecha
FROM consulta co
JOIN mascota m ON co.id_mascota = m.id_mascota
JOIN cliente c ON m.id_cliente  = c.id_cliente
WHERE co.estado = 'pendiente';

-- 4. Tabla de historico y disparador de auditoria.
CREATE TABLE mascotas_baja (
    id_mascota INT,
    nombre     VARCHAR(50),
    especie    VARCHAR(30),
    fecha_baja DATETIME
);

DELIMITER //
CREATE TRIGGER auditar_baja_mascota
BEFORE DELETE ON mascota
FOR EACH ROW
BEGIN
    INSERT INTO mascotas_baja (id_mascota, nombre, especie, fecha_baja)
    VALUES (OLD.id_mascota, OLD.nombre, OLD.especie, NOW());
END //
DELIMITER ;

-- 5. DROP TABLE frente a TRUNCATE TABLE.
--
--    DROP TABLE consulta;      elimina la tabla entera: datos Y estructura.
--                              Para volver a usarla hay que crearla de nuevo.
--    TRUNCATE TABLE consulta;  vacia la tabla pero conserva su definicion,
--                              sus indices y sus restricciones. Ademas
--                              reinicia el contador AUTO_INCREMENT.
--
--    Para vaciar la tabla de consultas conservando su estructura, TRUNCATE.
--    Ojo: TRUNCATE falla si hay claves ajenas apuntando a esa tabla, asi que
--    antes hay que vaciar consulta_tratamiento.
