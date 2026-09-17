package com.ifcd0112.ejercicios;

// Importamos cada bloque de ejercicios desde el subpaquete 'POOExpress'.
import com.ifcd0112.ejercicios.POOExpress.Bloque1Variables;
import com.ifcd0112.ejercicios.POOExpress.Bloque2Condicionales;
import com.ifcd0112.ejercicios.POOExpress.Bloque3Bucles;
import com.ifcd0112.ejercicios.POOExpress.Bloque4Metodos;
import com.ifcd0112.ejercicios.POOExpress.Bloque5ClasesObjetos;
import com.ifcd0112.ejercicios.POOExpress.Bloque6Encapsulamiento;
import com.ifcd0112.ejercicios.POOExpress.Bloque7Integradores;
import com.ifcd0112.ejercicios.POOExpress.Bloque8MiniProyectos;

// Ejercicios del modulo formativo MF0227_3, uno por unidad formativa.
import com.ifcd0112.ejercicios.uf2404.UF2404Ejercicios;
import com.ifcd0112.ejercicios.uf2405.UF2405Ejercicios;
import com.ifcd0112.ejercicios.uf2406.UF2406Ejercicios;

// Ejercicios de ampliacion, que cubren los criterios de evaluacion del Real
// Decreto que no tenian ejercicio en los tres cuadernos originales.
import com.ifcd0112.ejercicios.ampliacion.AmpliacionEjercicios;

/**
 * Punto de entrada del proyecto.
 *
 * Recorre las tres partes del cuaderno de forma secuencial:
 *
 *   1. Programacion Express: los 8 bloques del paquete POOExpress, con los
 *      ejercicios de introduccion a Java.
 *   2. El modulo formativo MF0227_3: los 43 ejercicios de las tres unidades
 *      formativas, 14 de la UF2404, 14 de la UF2405 y 15 de la UF2406.
 *   3. La ampliacion: 14 ejercicios mas que cubren los criterios de evaluacion
 *      del Real Decreto sin ejercicio en los cuadernos originales.
 *
 * Cada bloque y cada unidad imprime por consola sus enunciados y resultados, de
 * modo que al lanzar este Main se recorre el cuaderno completo. Todas las partes
 * tienen ademas su propio {@code main()}, asi que se pueden ejecutar por
 * separado sin tocar esta clase (ver recursos/LINEAS_PARA_MAIN.md).
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("##################################################");
        System.out.println("#  CUADERNO DE EJERCICIOS DE JAVA - EJECUCION    #");
        System.out.println("#  ifcd0112: POO - MF0227                        #");
        System.out.println("##################################################\n");

        // ------------------------------------------------------------------
        // PARTE 1: Programacion Express. Ejecucion secuencial de los 8 bloques.
        // ------------------------------------------------------------------
        Bloque1Variables.ejecutar();
        Bloque2Condicionales.ejecutar();
        Bloque3Bucles.ejecutar();
        Bloque4Metodos.ejecutar();
        Bloque5ClasesObjetos.ejecutar();
        Bloque6Encapsulamiento.ejecutar();
        Bloque7Integradores.ejecutar();
        Bloque8MiniProyectos.ejecutar();

        // ------------------------------------------------------------------
        // PARTE 2: ejercicios del modulo formativo MF0227_3, por unidad
        // formativa. 43 ejercicios: 14 de la UF2404, 14 de la UF2405 y 15 de
        // la UF2406.
        // ------------------------------------------------------------------
        UF2404Ejercicios.ejecutar();
        UF2405Ejercicios.ejecutar();
        UF2406Ejercicios.ejecutar();

        // ------------------------------------------------------------------
        // PARTE 3: ampliacion. 14 ejercicios que completan la cobertura de los
        // criterios de evaluacion y anaden ficheros y genericos propios.
        // ------------------------------------------------------------------
        AmpliacionEjercicios.ejecutar();

        System.out.println("##################################################");
        System.out.println("#  FIN DE LA EJECUCION DE TODOS LOS BLOQUES      #");
        System.out.println("##################################################");
    }
}
