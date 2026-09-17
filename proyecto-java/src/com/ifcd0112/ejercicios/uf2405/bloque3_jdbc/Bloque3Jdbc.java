package com.ifcd0112.ejercicios.uf2405.bloque3_jdbc;

/**
 * Runner del BLOQUE 3 de la UF2405: acceso a datos desde Java con JDBC.
 *
 * <p>Ejecuta los ejercicios 5 a 8 del cuaderno
 * Ejercicios_UF2405_Web_BBDD.docx.</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Bloque3Jdbc {

    /** Ejecuta los cuatro ejercicios del bloque. */
    public static void ejecutar() {
        System.out.println("==================================================");
        System.out.println(" UF2405 - BLOQUE 3: ACCESO A DATOS DESDE JAVA CON JDBC");
        System.out.println("==================================================");
        Ej05Conexion.resolver();
        Ej06InyeccionSql.resolver();
        Ej07Dao.resolver();
        Ej08Transacciones.resolver();
    }

    /** Permite ejecutar el bloque de forma aislada. */
    public static void main(String[] args) {
        ejecutar();
    }
}
