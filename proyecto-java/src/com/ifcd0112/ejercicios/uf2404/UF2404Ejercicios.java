package com.ifcd0112.ejercicios.uf2404;

import com.ifcd0112.ejercicios.uf2404.bloque1_clases.Bloque1Clases;
import com.ifcd0112.ejercicios.uf2404.bloque2_herencia.Bloque2Herencia;
import com.ifcd0112.ejercicios.uf2404.bloque3_estructuras.Bloque3Estructuras;
import com.ifcd0112.ejercicios.uf2404.bloque4_excepciones.Bloque4Excepciones;
import com.ifcd0112.ejercicios.uf2404.bloque5_integrador.Bloque5Integrador;

/**
 * Runner de la UF2404: Principios de la programacion orientada a objetos.
 *
 * <p>Ejecuta los 14 ejercicios del cuaderno Ejercicios_UF2404_Programacion.docx,
 * agrupados en sus cinco bloques.</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class UF2404Ejercicios {

    /** Ejecuta los cinco bloques de la unidad formativa. */
    public static void ejecutar() {
        System.out.println();
        System.out.println("##################################################");
        System.out.println(" UF2404 - PRINCIPIOS DE LA PROGRAMACION ORIENTADA A OBJETOS");
        System.out.println(" 14 ejercicios en 5 bloques");
        System.out.println("##################################################");
        System.out.println();
        Bloque1Clases.ejecutar();
        Bloque2Herencia.ejecutar();
        Bloque3Estructuras.ejecutar();
        Bloque4Excepciones.ejecutar();
        Bloque5Integrador.ejecutar();
    }

    /** Permite ejecutar la unidad formativa completa de forma aislada. */
    public static void main(String[] args) {
        ejecutar();
    }
}
