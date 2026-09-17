-- =====================================================================
-- UF2405 - BLOQUE 2 - EJERCICIO 3
-- Consultas con combinacion y agregacion
-- Criterios de evaluacion: CE2.6
--
-- Cada apartado se resuelve en UNA sola sentencia SQL, sin codigo Java.
-- =====================================================================

USE clinica;

-- 1. Mascotas con el nombre de su dueno y su telefono, por orden alfabetico.
SELECT m.nombre AS mascota, c.nombre AS dueno, c.telefono
FROM mascota m
JOIN cliente c ON m.id_cliente = c.id_cliente
ORDER BY m.nombre;

-- 2. Numero de consultas atendidas por cada veterinario, de mayor a menor.
SELECT v.nombre, COUNT(*) AS total_consultas
FROM consulta co
JOIN veterinario v ON co.id_veterinario = v.id_veterinario
GROUP BY v.id_veterinario, v.nombre
ORDER BY total_consultas DESC;

-- 3. TODOS los clientes, incluidos los que no tienen mascota.
--    Aqui esta la diferencia entre JOIN y LEFT JOIN: con INNER JOIN los
--    clientes sin mascota desaparecerian del resultado.
SELECT c.nombre AS cliente, m.nombre AS mascota
FROM cliente c
LEFT JOIN mascota m ON c.id_cliente = m.id_cliente
ORDER BY c.nombre;

-- 4. Importe total facturado por cada mascota.
SELECT m.nombre AS mascota, SUM(t.precio * ct.cantidad) AS total_facturado
FROM mascota m
JOIN consulta co             ON m.id_mascota      = co.id_mascota
JOIN consulta_tratamiento ct ON co.id_consulta    = ct.id_consulta
JOIN tratamiento t           ON ct.id_tratamiento = t.id_tratamiento
GROUP BY m.id_mascota, m.nombre;

-- 5. Mascotas con mas consultas que la media de consultas por mascota.
--    La subconsulta esta anidada en dos niveles: la interna cuenta las
--    consultas de cada mascota, la externa hace la media de esos conteos.
--    Notese que HAVING filtra sobre el resultado de una agregacion,
--    mientras que WHERE filtra filas individuales antes de agrupar.
SELECT m.nombre, COUNT(*) AS num_consultas
FROM mascota m
JOIN consulta co ON m.id_mascota = co.id_mascota
GROUP BY m.id_mascota, m.nombre
HAVING COUNT(*) > (
    SELECT AVG(total) FROM (
        SELECT COUNT(*) AS total FROM consulta GROUP BY id_mascota
    ) AS medias
);

-- 6. Listado combinado de clientes y veterinarios en un unico resultado.
--    UNION elimina duplicados; UNION ALL los conservaria.
SELECT nombre, 'Cliente' AS tipo FROM cliente
UNION
SELECT nombre, 'Veterinario' AS tipo FROM veterinario
ORDER BY nombre;
