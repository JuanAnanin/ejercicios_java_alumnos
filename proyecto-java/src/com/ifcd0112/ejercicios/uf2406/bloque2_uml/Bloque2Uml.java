package com.ifcd0112.ejercicios.uf2406.bloque2_uml;

/**
 * Runner del BLOQUE 2 de la UF2406: modelado con UML.
 *
 * <p>Ejecuta los ejercicios 3 a 5 del cuaderno
 * Ejercicios_UF2406_Ciclo_Vida.docx.</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Bloque2Uml {

    /** Ejecuta los tres ejercicios del bloque. */
    public static void ejecutar() {
        System.out.println("==================================================");
        System.out.println(" UF2406 - BLOQUE 2: MODELADO CON UML");
        System.out.println("==================================================");
        Ej03CasosDeUso.resolver();
        Ej04DiagramaClases.resolver();
        Ej05SecuenciaEstados.resolver();
    }

    /** Permite ejecutar el bloque de forma aislada. */
    public static void main(String[] args) {
        ejecutar();
    }
}
