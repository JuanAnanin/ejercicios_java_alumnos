package com.ifcd0112.ejercicios.uf2406.bloque3_planificacion;

/**
 * Runner del BLOQUE 3 de la UF2406: planificacion y gestion del proyecto.
 *
 * <p>Ejecuta el ejercicio 6 del cuaderno Ejercicios_UF2406_Ciclo_Vida.docx.</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Bloque3Planificacion {

    /** Ejecuta el ejercicio del bloque. */
    public static void ejecutar() {
        System.out.println("==================================================");
        System.out.println(" UF2406 - BLOQUE 3: PLANIFICACION Y GESTION DEL PROYECTO");
        System.out.println("==================================================");
        Ej06GanttCaminoCritico.resolver();
    }

    /** Permite ejecutar el bloque de forma aislada. */
    public static void main(String[] args) {
        ejecutar();
    }
}
