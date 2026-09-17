package com.ifcd0112.ejercicios.uf2405.bloque2_dml;

/**
 * Runner del BLOQUE 2 de la UF2405: consultas y manipulacion de datos.
 *
 * <p>Ejecuta los ejercicios 3 y 4 del cuaderno
 * Ejercicios_UF2405_Web_BBDD.docx.</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Bloque2Dml {

    /** Ejecuta los dos ejercicios del bloque. */
    public static void ejecutar() {
        System.out.println("==================================================");
        System.out.println(" UF2405 - BLOQUE 2: CONSULTAS Y MANIPULACION DE DATOS (DML)");
        System.out.println("==================================================");
        Ej03Consultas.resolver();
        Ej04ActualizacionesBorrados.resolver();
    }

    /** Permite ejecutar el bloque de forma aislada. */
    public static void main(String[] args) {
        ejecutar();
    }
}
