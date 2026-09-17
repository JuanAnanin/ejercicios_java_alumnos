package com.ifcd0112.ejercicios.uf2404.bloque5_integrador;

/**
 * Runner del BLOQUE 5 de la UF2404: proyecto integrador.
 *
 * <p>Ejecuta en orden los ejercicios 13 y 14 del cuaderno
 * Ejercicios_UF2404_Programacion.docx.</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Bloque5Integrador {

    /** Ejecuta los dos ejercicios del bloque. */
    public static void ejecutar() {
        System.out.println("==================================================");
        System.out.println(" UF2404 - BLOQUE 5: PROYECTO INTEGRADOR");
        System.out.println("==================================================");
        Ej13Videoclub.resolver();
        Ej14Refactorizacion.resolver();
    }

    /** Permite ejecutar el bloque de forma aislada. */
    public static void main(String[] args) {
        ejecutar();
    }
}
