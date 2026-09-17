package com.ifcd0112.ejercicios.uf2405.bloque1_ddl;

/**
 * UF2405 - BLOQUE 1 - EJERCICIO 2: Evolucion del esquema, ALTER, vistas y disparadores.
 *
 * <p>Criterios de evaluacion: CE2.6</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej02EvolucionEsquema {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * La clinica del ejercicio anterior lleva un ano funcionando y solicita
     * varias modificaciones sobre la base de datos ya en produccion, que
     * contiene datos reales que no pueden perderse.
     *
     * Se pide:
     *   1. Anade a la tabla de mascotas una columna para registrar el peso, con
     *      valor por defecto cero, sin perder ningun dato existente.
     *   2. Amplia la longitud del campo que almacena el motivo de la consulta,
     *      que se ha quedado corto.
     *   3. Crea una vista llamada consultas_pendientes que muestre el nombre de
     *      la mascota, el nombre de su dueno y la fecha, unicamente para las
     *      consultas cuyo estado sea 'pendiente'.
     *   4. Crea un disparador que, antes de eliminar una mascota, registre
     *      automaticamente sus datos en una tabla de historico llamada
     *      mascotas_baja.
     *   5. Explica en un comentario la diferencia entre DROP TABLE y TRUNCATE
     *      TABLE, y cual de las dos usarias si quisieras vaciar la tabla de
     *      consultas conservando su estructura para volver a llenarla.
     *
     * Pista: recuerda que las sentencias DDL provocan un commit implicito en la
     * mayoria de SGBD: no podras deshacerlas con ROLLBACK, asi que pruebalas
     * siempre sobre una copia de la base de datos, nunca directamente en
     * produccion.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * DONDE ESTA LA SOLUCION.
     *
     * Los cinco apartados producen SQL. El script esta en:
     *
     *      recursos/uf2405/sql/02_evolucion_esquema.sql
     *
     * Se ejecuta sobre la base de datos que dejo creada el ejercicio 1.
     */

    /*
     * APARTADO 5: DROP TABLE frente a TRUNCATE TABLE.
     *
     *   DROP TABLE consulta;
     *       Elimina la tabla entera: los datos Y la definicion. Desaparecen
     *       tambien sus indices, sus restricciones y los permisos concedidos
     *       sobre ella. Para volver a usarla hay que crearla de nuevo.
     *
     *   TRUNCATE TABLE consulta;
     *       Vacia la tabla pero conserva su estructura. Internamente no borra
     *       fila por fila, sino que descarta y recrea el fichero de datos, por
     *       lo que es mucho mas rapido que un DELETE sin WHERE. Ademas reinicia
     *       el contador AUTO_INCREMENT.
     *
     *   DELETE FROM consulta;
     *       Tambien vacia la tabla, pero fila a fila. Es mas lento, activa los
     *       disparadores de borrado, se puede deshacer dentro de una
     *       transaccion y NO reinicia el AUTO_INCREMENT.
     *
     * Para lo que pide el enunciado, vaciar la tabla conservando su estructura
     * para volver a llenarla, la respuesta es TRUNCATE TABLE.
     *
     * Matiz importante: TRUNCATE falla si existen claves ajenas apuntando a esa
     * tabla. En este esquema, consulta_tratamiento referencia a consulta, asi
     * que habria que vaciar primero la tabla puente.
     *
     * NOTA SOBRE EL ESQUEMA. El script incluye un ALTER que no pide el
     * enunciado: la columna mascota.ultima_visita. El ejercicio 8 la actualiza
     * dentro de su transaccion, pero ni el CREATE TABLE del ejercicio 1 ni los
     * ALTER de este la crean. Sin ese anadido, la transaccion del bloque 3
     * falla con "Unknown column". Merece la pena comentarlo en clase: es
     * exactamente el tipo de desajuste que aparece cuando el esquema evoluciona
     * por un lado y el codigo por otro.
     */

    /** Ruta del script SQL que resuelve el ejercicio. */
    public static final String SCRIPT = "recursos/uf2405/sql/02_evolucion_esquema.sql";

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2405-E02] Evolucion del esquema: ALTER, vistas y disparadores");
        System.out.println("  Apartados 1 a 4 (ALTER, vista y disparador de auditoria):");
        System.out.println("    " + SCRIPT);
        System.out.println("  Apartado 5: DROP TABLE elimina datos y estructura;");
        System.out.println("    TRUNCATE TABLE vacia los datos y conserva la estructura,");
        System.out.println("    y ademas reinicia el AUTO_INCREMENT. Para vaciar consulta y");
        System.out.println("    volver a llenarla: TRUNCATE, vaciando antes la tabla puente");
        System.out.println("    porque una clave ajena apunta a consulta.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
