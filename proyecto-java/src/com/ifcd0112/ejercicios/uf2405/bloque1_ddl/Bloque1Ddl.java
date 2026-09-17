package com.ifcd0112.ejercicios.uf2405.bloque1_ddl;

/**
 * Runner del BLOQUE 1 de la UF2405: diseno y definicion de la base de datos.
 *
 * <p>Ejecuta los ejercicios 1 y 2 del cuaderno
 * Ejercicios_UF2405_Web_BBDD.docx.</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Bloque1Ddl {

    /** Ejecuta los dos ejercicios del bloque. */
    public static void ejecutar() {
        System.out.println("==================================================");
        System.out.println(" UF2405 - BLOQUE 1: DISENO Y DEFINICION DE LA BASE DE DATOS (DDL)");
        System.out.println("==================================================");
        Ej01ModeloRelacional.resolver();
        Ej02EvolucionEsquema.resolver();
    }

    /** Permite ejecutar el bloque de forma aislada. */
    public static void main(String[] args) {
        ejecutar();
    }
}
