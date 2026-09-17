package com.ifcd0112.ejercicios.uf2404.bloque3_estructuras;

/**
 * Runner del BLOQUE 3 de la UF2404: estructuras de datos.
 *
 * <p>Ejecuta en orden los ejercicios 6 a 9 del cuaderno
 * Ejercicios_UF2404_Programacion.docx.</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Bloque3Estructuras {

    /** Ejecuta los cuatro ejercicios del bloque. */
    public static void ejecutar() {
        System.out.println("==================================================");
        System.out.println(" UF2404 - BLOQUE 3: ESTRUCTURAS DE DATOS");
        System.out.println("==================================================");
        Ej06Pila.resolver();
        Ej07Cola.resolver();
        Ej08ArbolBinario.resolver();
        Ej09AnalisisTexto.resolver();
    }

    /** Permite ejecutar el bloque de forma aislada. */
    public static void main(String[] args) {
        ejecutar();
    }
}
