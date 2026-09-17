package com.ifcd0112.ejercicios.uf2406.bloque1_requisitos;

/**
 * Runner del BLOQUE 1 de la UF2406: requisitos, modelos de proceso y metodologia.
 *
 * <p>Ejecuta los ejercicios 1 y 2 del cuaderno
 * Ejercicios_UF2406_Ciclo_Vida.docx.</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Bloque1Requisitos {

    /** Ejecuta los dos ejercicios del bloque. */
    public static void ejecutar() {
        System.out.println("==================================================");
        System.out.println(" UF2406 - BLOQUE 1: REQUISITOS, MODELOS DE PROCESO Y METODOLOGIA");
        System.out.println("==================================================");
        Ej01Requisitos.resolver();
        Ej02TarjetasCrc.resolver();
    }

    /** Permite ejecutar el bloque de forma aislada. */
    public static void main(String[] args) {
        ejecutar();
    }
}
