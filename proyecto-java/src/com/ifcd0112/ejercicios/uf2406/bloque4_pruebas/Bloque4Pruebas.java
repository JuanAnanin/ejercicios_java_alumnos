package com.ifcd0112.ejercicios.uf2406.bloque4_pruebas;

/**
 * Runner del BLOQUE 4 de la UF2406: pruebas del software.
 *
 * <p>Ejecuta los ejercicios 7 a 10 del cuaderno
 * Ejercicios_UF2406_Ciclo_Vida.docx.</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Bloque4Pruebas {

    /** Ejecuta los cuatro ejercicios del bloque. */
    public static void ejecutar() {
        System.out.println("==================================================");
        System.out.println(" UF2406 - BLOQUE 4: PRUEBAS DEL SOFTWARE");
        System.out.println("==================================================");
        Ej07CajaNegra.resolver();
        Ej08CajaBlanca.resolver();
        Ej09Junit.resolver();
        Ej10Mocks.resolver();
    }

    /** Permite ejecutar el bloque de forma aislada. */
    public static void main(String[] args) {
        ejecutar();
    }
}
