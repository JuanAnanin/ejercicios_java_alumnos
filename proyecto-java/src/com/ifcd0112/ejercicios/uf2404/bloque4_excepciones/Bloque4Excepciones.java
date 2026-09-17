package com.ifcd0112.ejercicios.uf2404.bloque4_excepciones;

/**
 * Runner del BLOQUE 4 de la UF2404: excepciones, concurrencia y comunicaciones.
 *
 * <p>Ejecuta en orden los ejercicios 10 a 12 del cuaderno
 * Ejercicios_UF2404_Programacion.docx.</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Bloque4Excepciones {

    /** Ejecuta los tres ejercicios del bloque. */
    public static void ejecutar() {
        System.out.println("==================================================");
        System.out.println(" UF2404 - BLOQUE 4: EXCEPCIONES, CONCURRENCIA Y COMUNICACIONES");
        System.out.println("==================================================");
        Ej10Excepciones.resolver();
        Ej11Concurrencia.resolver();
        Ej12Sockets.resolver();
    }

    /** Permite ejecutar el bloque de forma aislada. */
    public static void main(String[] args) {
        ejecutar();
    }
}
