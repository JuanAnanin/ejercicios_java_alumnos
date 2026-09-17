package com.ifcd0112.ejercicios.uf2405.bloque2_dml;

/**
 * UF2405 - BLOQUE 2 - EJERCICIO 3: Consultas con combinacion y agregacion.
 *
 * <p>Criterios de evaluacion: CE2.6</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej03Consultas {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Sobre la base de datos de la clinica veterinaria, escribe las consultas
     * SQL que resuelvan las siguientes necesidades de informacion. Todas deben
     * resolverse en una unica sentencia SQL, sin recurrir a codigo Java.
     *
     * Se pide:
     *   1. Listado de todas las mascotas con el nombre de su dueno y su
     *      telefono de contacto, ordenado alfabeticamente por el nombre de la
     *      mascota.
     *   2. Numero de consultas atendidas por cada veterinario, mostrando el
     *      nombre del veterinario y el total, de mayor a menor.
     *   3. Listado de TODOS los clientes, incluidos aquellos que todavia no han
     *      registrado ninguna mascota (deben aparecer con valor nulo en la
     *      columna de la mascota).
     *   4. Importe total facturado por cada mascota, sumando el precio de todos
     *      los tratamientos aplicados en todas sus consultas.
     *   5. Nombre de las mascotas que han recibido mas consultas que la media de
     *      consultas por mascota (resuelvelo con una subconsulta).
     *   6. Listado combinado de todos los nombres de personas relacionadas con
     *      la clinica, uniendo en un unico resultado los nombres de clientes y
     *      de veterinarios.
     *
     * Pista: para el tercer apartado necesitas un LEFT JOIN, no un INNER JOIN:
     * es la diferencia entre "todos los clientes, tengan o no mascota" y "solo
     * los clientes que tienen mascota".
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * DONDE ESTA LA SOLUCION.
     *
     * Las seis consultas, comentadas una a una, estan en:
     *
     *      recursos/uf2405/sql/03_consultas.sql
     *
     * LO QUE CONVIENE REMARCAR AL CORREGIR.
     *
     * Apartado 3, JOIN frente a LEFT JOIN. Es el error mas frecuente del
     * bloque. Un INNER JOIN solo devuelve las filas que casan por los dos
     * lados, asi que los clientes sin mascota desaparecen sin dar ningun aviso:
     * la consulta no falla, simplemente miente por omision. El LEFT JOIN
     * conserva todas las filas de la tabla de la izquierda y rellena con NULL
     * las columnas de la derecha cuando no hay pareja.
     *
     * Apartado 5, WHERE frente a HAVING. WHERE filtra filas individuales ANTES
     * de agrupar; HAVING filtra grupos DESPUES de agregar. Como la condicion es
     * "mas consultas que la media", que solo se puede evaluar una vez contadas
     * las consultas de cada mascota, tiene que ir en HAVING. Escribir
     * WHERE COUNT(*) > ... da error de sintaxis, y ese error es en realidad una
     * ayuda: el motor esta senalando que se ha confundido el momento del
     * filtrado.
     *
     * Apartado 5, la subconsulta de dos niveles. La media de consultas por
     * mascota no es AVG de ninguna columna existente: primero hay que contar
     * las consultas de cada mascota y despues hacer la media de esos conteos.
     * De ahi el SELECT anidado dentro de otro SELECT. En MySQL la subconsulta
     * derivada necesita un alias obligatorio, el AS medias del script.
     *
     * Apartado 6, UNION frente a UNION ALL. UNION elimina duplicados, lo que
     * obliga al motor a ordenar el resultado y cuesta tiempo. Si se sabe que no
     * puede haber repetidos, o si interesa conservarlos, UNION ALL es mas
     * rapido. Las dos consultas unidas deben tener el mismo numero de columnas
     * y tipos compatibles; de ahi la constante 'Cliente' o 'Veterinario', que
     * ademas sirve para saber de donde viene cada fila.
     */

    /** Ruta del script SQL que resuelve el ejercicio. */
    public static final String SCRIPT = "recursos/uf2405/sql/03_consultas.sql";

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2405-E03] Consultas con combinacion y agregacion");
        System.out.println("  Las seis consultas estan en:");
        System.out.println("    " + SCRIPT);
        System.out.println("  Puntos clave:");
        System.out.println("    3) LEFT JOIN, no INNER JOIN: si no, los clientes sin mascota");
        System.out.println("       desaparecen del resultado sin ningun aviso.");
        System.out.println("    5) HAVING, no WHERE: la condicion se evalua sobre el resultado");
        System.out.println("       de COUNT(*), es decir, despues de agrupar. Y la media de");
        System.out.println("       consultas por mascota exige una subconsulta de dos niveles.");
        System.out.println("    6) UNION elimina duplicados; UNION ALL los conserva y es mas rapido.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
