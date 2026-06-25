package com.ifcd0112.ejercicios;

// Importamos cada bloque de ejercicios desde el subpaquete 'bloques'.
import com.ifcd0112.ejercicios.bloques.Bloque1Variables;
import com.ifcd0112.ejercicios.bloques.Bloque2Condicionales;
import com.ifcd0112.ejercicios.bloques.Bloque3Bucles;
import com.ifcd0112.ejercicios.bloques.Bloque4Metodos;
import com.ifcd0112.ejercicios.bloques.Bloque5ClasesObjetos;
import com.ifcd0112.ejercicios.bloques.Bloque6Encapsulamiento;
import com.ifcd0112.ejercicios.bloques.Bloque7Integradores;
import com.ifcd0112.ejercicios.bloques.Bloque8MiniProyectos;

/**
 * Punto de entrada del proyecto.
 *
 * Importa los 8 bloques de ejercicios y ejecuta sus metodos {@code ejecutar()}
 * de forma secuencial. Cada bloque imprime por consola sus enunciados y
 * resultados, de modo que al lanzar este Main se recorre todo el cuaderno.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("##################################################");
        System.out.println("#  CUADERNO DE EJERCICIOS DE JAVA - EJECUCION    #");
        System.out.println("#  ifcd0112: POO - MF0227                        #");
        System.out.println("##################################################\n");

        // Ejecucion secuencial de los 8 bloques.
        Bloque1Variables.ejecutar();
        Bloque2Condicionales.ejecutar();
        Bloque3Bucles.ejecutar();
        Bloque4Metodos.ejecutar();
        Bloque5ClasesObjetos.ejecutar();
        Bloque6Encapsulamiento.ejecutar();
        Bloque7Integradores.ejecutar();
        Bloque8MiniProyectos.ejecutar();

        System.out.println("##################################################");
        System.out.println("#  FIN DE LA EJECUCION DE TODOS LOS BLOQUES      #");
        System.out.println("##################################################");
    }
}
