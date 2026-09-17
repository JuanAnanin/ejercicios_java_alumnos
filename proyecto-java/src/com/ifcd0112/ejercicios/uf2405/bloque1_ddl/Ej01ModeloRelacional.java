package com.ifcd0112.ejercicios.uf2405.bloque1_ddl;

/**
 * UF2405 - BLOQUE 1 - EJERCICIO 1: Modelo relacional de una clinica veterinaria.
 *
 * <p>Criterios de evaluacion: CE2.5</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej01ModeloRelacional {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Una clinica veterinaria necesita informatizar su gestion. Del analisis
     * con el cliente se extrae lo siguiente: cada cliente puede tener varias
     * mascotas, pero cada mascota pertenece a un unico cliente. Cada mascota
     * puede recibir muchas consultas a lo largo del tiempo. Cada consulta la
     * atiende un unico veterinario, aunque un veterinario atiende muchas
     * consultas. En cada consulta pueden aplicarse varios tratamientos, y un
     * mismo tratamiento puede aplicarse en muchas consultas distintas.
     *
     * Se pide:
     *   1. Identifica las entidades del problema y determina razonadamente el
     *      tipo de relacion (1:1, 1:N o N:M) que existe entre cada par de ellas.
     *   2. Indica cual de las relaciones exige una tabla intermedia y explica
     *      por que.
     *   3. Escribe el script SQL completo de creacion de la base de datos
     *      (CREATE DATABASE y todas las sentencias CREATE TABLE).
     *   4. Define en cada tabla su clave primaria y las claves ajenas
     *      necesarias, con sus restricciones de integridad referencial.
     *   5. Aplica restricciones de columna adecuadas: NOT NULL en los campos
     *      obligatorios, UNIQUE donde corresponda, DEFAULT en el estado de la
     *      consulta y CHECK para impedir precios negativos en los tratamientos.
     *   6. Anade al final del script las sentencias INSERT necesarias para
     *      cargar al menos tres clientes, cinco mascotas, dos veterinarios y
     *      cuatro consultas de prueba.
     *
     * Pista: la relacion entre consulta y tratamiento es de muchos a muchos:
     * necesitaras una tabla puente cuya clave primaria este formada por la
     * combinacion de las dos claves ajenas.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * DONDE ESTA LA SOLUCION DE ESTE EJERCICIO.
     *
     * Los apartados 3 a 6 producen un artefacto SQL, no codigo Java. El script
     * completo, listo para ejecutar, esta en:
     *
     *      recursos/uf2405/sql/01_esquema_clinica.sql
     *
     * Se lanza con:   mysql -u root -p < recursos/uf2405/sql/01_esquema_clinica.sql
     *
     * Este fichero .java conserva el enunciado en su sitio dentro del proyecto
     * y recoge los apartados 1 y 2, que son razonamiento y no codigo. El
     * metodo resolver() los imprime para que queden a la vista al ejecutar el
     * bloque.
     */

    /*
     * APARTADO 1: entidades y tipo de relacion.
     *
     * ENTIDADES: Cliente, Mascota, Veterinario, Consulta y Tratamiento.
     *
     *   Cliente - Mascota          1:N
     *       Un cliente tiene varias mascotas; cada mascota tiene un solo dueno.
     *       Se resuelve poniendo id_cliente como clave ajena EN mascota, que es
     *       el lado "muchos". Ponerla al reves obligaria a repetir la fila del
     *       cliente por cada mascota.
     *
     *   Mascota - Consulta         1:N
     *       Una mascota acumula muchas consultas a lo largo del tiempo; cada
     *       consulta es de una sola mascota. La clave ajena va en consulta.
     *
     *   Veterinario - Consulta     1:N
     *       Un veterinario atiende muchas consultas; cada consulta la atiende
     *       uno solo. La clave ajena va tambien en consulta.
     *
     *   Consulta - Tratamiento     N:M
     *       En una consulta se aplican varios tratamientos y un mismo
     *       tratamiento se aplica en muchas consultas. Los dos extremos admiten
     *       "varios", y ahi es donde una clave ajena simple ya no llega.
     *
     * No hay ninguna relacion 1:1 en este problema. Cuando aparece una, casi
     * siempre conviene preguntarse si en realidad no son dos tablas sino una.
     *
     * APARTADO 2: que relacion exige tabla intermedia y por que.
     *
     * La unica que la exige es Consulta - Tratamiento, la N:M.
     *
     * El motivo es que una columna de una fila guarda UN valor. Con una
     * relacion 1:N basta con poner la clave ajena en el lado "muchos", porque
     * ahi solo hace falta apuntar a uno. En una N:M haria falta que la fila de
     * consulta apuntase a varios tratamientos y que la de tratamiento apuntase
     * a varias consultas: eso obligaria a listas dentro de una celda, que es
     * justo lo que la primera forma normal prohibe.
     *
     * La solucion es la tabla puente consulta_tratamiento, que convierte la
     * relacion N:M en dos relaciones 1:N. Cada fila representa un hecho
     * concreto: "en la consulta 7 se aplico el tratamiento 3, dos veces". Su
     * clave primaria es la pareja (id_consulta, id_tratamiento), lo que ademas
     * impide por construccion registrar dos veces el mismo tratamiento en la
     * misma consulta.
     *
     * Ventaja anadida: la tabla puente es el sitio natural donde colocar los
     * datos que pertenecen a la relacion y no a ninguno de los dos extremos,
     * como la cantidad aplicada. Ni la consulta ni el tratamiento son el dueno
     * de ese dato: lo es el cruce de ambos.
     */

    /** Ruta del script SQL que resuelve los apartados 3 a 6. */
    public static final String SCRIPT = "recursos/uf2405/sql/01_esquema_clinica.sql";

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2405-E01] Modelo relacional de una clinica veterinaria");
        System.out.println("  Apartado 1: entidades y relaciones");
        System.out.println("    Cliente     - Mascota       1:N   clave ajena en mascota");
        System.out.println("    Mascota     - Consulta      1:N   clave ajena en consulta");
        System.out.println("    Veterinario - Consulta      1:N   clave ajena en consulta");
        System.out.println("    Consulta    - Tratamiento   N:M   TABLA PUENTE");
        System.out.println("  Apartado 2: solo la N:M exige tabla intermedia.");
        System.out.println("    Una columna guarda un valor, no una lista. La tabla puente");
        System.out.println("    consulta_tratamiento parte la N:M en dos relaciones 1:N y ademas");
        System.out.println("    da sitio a los datos propios de la relacion, como la cantidad.");
        System.out.println("  Apartados 3 a 6 (script SQL completo con datos de prueba):");
        System.out.println("    " + SCRIPT);
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
