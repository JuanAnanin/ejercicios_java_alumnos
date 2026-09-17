package com.ifcd0112.ejercicios.uf2404.bloque1_clases;

/**
 * Runner del BLOQUE 1 de la UF2404: clases, objetos y encapsulacion.
 *
 * <p>Ejecuta en orden los ejercicios 1 y 2 del cuaderno
 * Ejercicios_UF2404_Programacion.docx.</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Bloque1Clases {

    /** Ejecuta los dos ejercicios del bloque. */
    public static void ejecutar() {
        System.out.println("==================================================");
        System.out.println(" UF2404 - BLOQUE 1: CLASES, OBJETOS Y ENCAPSULACION");
        System.out.println("==================================================");
        Ej01Fraccion.resolver();
        Ej02CuentaBancaria.resolver();
    }

    /** Permite ejecutar el bloque de forma aislada. */
    public static void main(String[] args) {
        ejecutar();
    }
}
