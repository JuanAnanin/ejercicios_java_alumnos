package com.ifcd0112.ejercicios.uf2404.bloque2_herencia;

/**
 * UF2404 - BLOQUE 2: Herencia, polimorfismo e interfaces.
 *
 * <p>Runner del bloque: lanza en orden los ejercicios 3, 4 y 5. Cada ejercicio
 * vive en su propio fichero y expone un metodo {@code resolver()}, de modo que
 * puede ejecutarse tanto de forma aislada como desde aqui.</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Bloque2Herencia {

    /** Ejecuta secuencialmente los ejercicios del bloque 2. */
    public static void ejecutar() {
        System.out.println("==================================================");
        System.out.println(" UF2404 - BLOQUE 2: HERENCIA, POLIMORFISMO E INTERFACES");
        System.out.println("==================================================");
        Ej03Empleados.resolver();
        Ej04Interfaces.resolver();
        Ej05Composicion.resolver();
    }

    /** Permite ejecutar el bloque de forma aislada. */
    public static void main(String[] args) {
        ejecutar();
    }
}
