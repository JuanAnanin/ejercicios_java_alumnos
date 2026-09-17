-- =====================================================================
-- UF2405 - BLOQUE 2 - EJERCICIO 4
-- Actualizaciones y borrados controlados
-- Criterios de evaluacion: CE2.6
--
-- REGLA DE ORO: antes de ejecutar cualquiera de estas sentencias sobre
-- datos reales, escribela primero como SELECT con la MISMA clausula WHERE
-- y comprueba cuantas filas devuelve. Solo entonces cambia el verbo.
-- =====================================================================

USE clinica;

-- 1. Subida del 8 por ciento a los tratamientos de cirugia.
-- COMPROBACION PREVIA:
-- SELECT id_tratamiento, nombre, precio FROM tratamiento WHERE categoria = 'cirugia';
UPDATE tratamiento SET precio = precio * 1.08 WHERE categoria = 'cirugia';

-- 2. Marcar como atendidas las consultas pasadas que sigan pendientes.
-- COMPROBACION PREVIA:
-- SELECT id_consulta, fecha, estado FROM consulta WHERE fecha < CURDATE() AND estado = 'pendiente';
UPDATE consulta SET estado = 'atendida'
WHERE fecha < CURDATE() AND estado = 'pendiente';

-- 3. Purgar consultas de mas de cinco anos.
--    OJO al orden: primero hay que borrar las filas de la tabla puente que
--    referencian esas consultas, o la integridad referencial lo impedira.
DELETE FROM consulta_tratamiento
WHERE id_consulta IN (
    SELECT id_consulta FROM (
        SELECT id_consulta FROM consulta
        WHERE fecha < DATE_SUB(CURDATE(), INTERVAL 5 YEAR)
    ) AS antiguas
);
DELETE FROM consulta WHERE fecha < DATE_SUB(CURDATE(), INTERVAL 5 YEAR);

-- 4. Asignar a un cliente concreto las mascotas que no tienen dueno.
--    NOTA: con el esquema del ejercicio 1 esta sentencia no afecta a
--    ninguna fila, porque mascota.id_cliente esta declarado NOT NULL y por
--    tanto no puede haber mascotas sin dueno. Para que el caso exista
--    habria que permitir el nulo:
--        ALTER TABLE mascota MODIFY COLUMN id_cliente INT NULL;
UPDATE mascota SET id_cliente = 3 WHERE id_cliente IS NULL;

-- ---------------------------------------------------------------------
-- Apartado 5: que habria pasado sin la clausula WHERE
--
--   1) Sin WHERE, la subida del 8 por ciento se aplicaria a TODOS los
--      tratamientos, no solo a los de cirugia.
--   2) Sin WHERE, quedarian marcadas como atendidas todas las consultas,
--      incluidas las futuras que todavia no se han producido.
--   3) Sin WHERE, el DELETE vaciaria el historico completo de consultas.
--   4) Sin WHERE, todas las mascotas de la clinica pasarian a tener el
--      mismo dueno.
--
-- Medidas preventivas:
--   - Escribir siempre primero el SELECT con la misma condicion.
--   - Trabajar con autocommit desactivado: START TRANSACTION, comprobar el
--     numero de filas afectadas y solo entonces COMMIT (o ROLLBACK).
--   - En MySQL, el modo seguro de actualizaciones rechaza directamente un
--     UPDATE o DELETE sin WHERE sobre la clave:  SET SQL_SAFE_UPDATES = 1;
--   - Tener una copia de seguridad reciente antes de cualquier
--     mantenimiento masivo.
-- ---------------------------------------------------------------------
