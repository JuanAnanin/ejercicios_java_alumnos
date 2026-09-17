package com.ifcd0112.ejercicios.uf2405.bloque2_dml;

/**
 * UF2405 - BLOQUE 2 - EJERCICIO 4: Actualizaciones y borrados controlados.
 *
 * <p>Criterios de evaluacion: CE2.6</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej04ActualizacionesBorrados {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * La clinica necesita realizar una serie de operaciones de mantenimiento
     * sobre los datos. Escribe las sentencias correspondientes prestando
     * especial atencion a la clausula WHERE en cada caso.
     *
     * Se pide:
     *   1. Aplica un incremento del 8% al precio de todos los tratamientos de
     *      la categoria 'cirugia'.
     *   2. Marca como 'atendida' toda consulta cuya fecha sea anterior al dia de
     *      hoy y que siga figurando como 'pendiente'.
     *   3. Elimina del historico las consultas anteriores a hace cinco anos.
     *   4. Escribe la sentencia que asigna a un cliente concreto todas las
     *      mascotas que actualmente no tienen dueno asignado.
     *   5. Explica por escrito que habria ocurrido en cada uno de los cuatro
     *      apartados anteriores si se hubiera olvidado la clausula WHERE, y que
     *      medida preventiva puede adoptarse antes de ejecutar un UPDATE o un
     *      DELETE sobre datos reales.
     *
     * Pista: una practica profesional muy extendida consiste en escribir
     * primero la sentencia como un SELECT con la misma clausula WHERE,
     * comprobar que filas devuelve, y solo entonces sustituir el SELECT por el
     * UPDATE o el DELETE.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * DONDE ESTA LA SOLUCION.
     *
     *      recursos/uf2405/sql/04_actualizaciones.sql
     *
     * El script lleva, antes de cada sentencia peligrosa, el SELECT de
     * comprobacion previa que recomienda la pista, comentado y listo para
     * ejecutar primero.
     *
     * APARTADO 5: que habria pasado sin WHERE.
     *
     *   1) UPDATE tratamiento SET precio = precio * 1.08;
     *      Subiria un 8 por ciento el precio de TODOS los tratamientos, no solo
     *      los de cirugia. Ademas es un error especialmente desagradable porque
     *      no se puede deshacer restando un 8 por ciento: hay que dividir por
     *      1.08, y si alguien ejecuta la sentencia dos veces, la correccion
     *      tampoco es evidente.
     *
     *   2) UPDATE consulta SET estado = 'atendida';
     *      Marcaria como atendidas todas las consultas, incluidas las futuras
     *      que todavia no se han producido. Los pacientes citados para la
     *      semana que viene desaparecerian de la agenda.
     *
     *   3) DELETE FROM consulta;
     *      Borraria el historico clinico completo. Es el caso mas grave de los
     *      cuatro, porque a diferencia de un UPDATE no queda ni rastro del
     *      valor anterior.
     *
     *   4) UPDATE mascota SET id_cliente = 3;
     *      Todas las mascotas de la clinica pasarian a tener el mismo dueno.
     *
     * MEDIDAS PREVENTIVAS.
     *
     *   - Escribir primero la sentencia como SELECT con la misma condicion y
     *     mirar cuantas filas devuelve. Si devuelve 400 y esperabas 3, ahi esta
     *     el fallo, y todavia no se ha tocado nada.
     *   - Trabajar con autocommit desactivado: START TRANSACTION, ejecutar,
     *     comprobar el numero de filas afectadas y solo entonces COMMIT. Si el
     *     numero no cuadra, ROLLBACK.
     *   - En MySQL, SET SQL_SAFE_UPDATES = 1 rechaza directamente cualquier
     *     UPDATE o DELETE que no filtre por clave.
     *   - Copia de seguridad reciente antes de cualquier mantenimiento masivo.
     *
     * DOS OBSERVACIONES SOBRE EL ENUNCIADO, PARA CLASE.
     *
     *   a) El apartado 3 no se resuelve con un unico DELETE. Las filas de
     *      consulta_tratamiento apuntan a consulta mediante una clave ajena, de
     *      modo que borrar primero las consultas viola la integridad
     *      referencial y el motor lo rechaza. Hay que borrar primero la tabla
     *      puente. La alternativa es declarar la clave ajena con ON DELETE
     *      CASCADE, y entonces conviene explicar el riesgo: un borrado en
     *      cascada silencioso puede llevarse por delante mucho mas de lo que se
     *      pretendia.
     *
     *   b) El apartado 4 no puede afectar a ninguna fila con el esquema del
     *      ejercicio 1, porque mascota.id_cliente esta declarado NOT NULL y por
     *      tanto no existen mascotas sin dueno. Es una buena pregunta de clase:
     *      la sentencia es correcta, pero el modelo no permite que se de el
     *      caso. Para que exista habria que permitir el nulo con un ALTER.
     */

    /** Ruta del script SQL que resuelve el ejercicio. */
    public static final String SCRIPT = "recursos/uf2405/sql/04_actualizaciones.sql";

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2405-E04] Actualizaciones y borrados controlados");
        System.out.println("  Las sentencias, con su SELECT de comprobacion previa, estan en:");
        System.out.println("    " + SCRIPT);
        System.out.println("  Apartado 5, sin WHERE: 1) suben todos los precios; 2) se dan por");
        System.out.println("    atendidas hasta las consultas futuras; 3) se borra el historico");
        System.out.println("    entero; 4) todas las mascotas pasan al mismo dueno.");
        System.out.println("  Prevencion: probar como SELECT, trabajar dentro de una transaccion,");
        System.out.println("    activar SQL_SAFE_UPDATES y tener copia de seguridad reciente.");
        System.out.println("  Dos matices del enunciado que conviene comentar en clase:");
        System.out.println("    - el borrado del apartado 3 exige vaciar antes la tabla puente;");
        System.out.println("    - el apartado 4 no afecta a ninguna fila, porque id_cliente es NOT NULL.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
