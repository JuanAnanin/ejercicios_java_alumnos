package com.ifcd0112.ejercicios.ampliacion;

import com.ifcd0112.ejercicios.ampliacion.uf2404.AmpliacionUf2404;
import com.ifcd0112.ejercicios.ampliacion.uf2405.AmpliacionUf2405;
import com.ifcd0112.ejercicios.ampliacion.uf2406.AmpliacionUf2406;

/**
 * Runner de los ejercicios de AMPLIACION del modulo MF0227_3.
 *
 * <p>Catorce ejercicios repartidos por unidad formativa que cubren criterios de
 * evaluacion del Real Decreto que no tenian ejercicio en los tres cuadernos
 * originales, y dos temas que el Real Decreto no exige pero que un alumno
 * espera saber hacer al terminar: la entrada y salida de ficheros y los tipos
 * genericos propios.</p>
 *
 * <p>Con esta ampliacion, los criterios de evaluacion con ejercicio pasan de 46
 * a 65 de los 68 del modulo. Los tres restantes son de "enumerar y describir" y
 * se cubren con la teoria y las presentaciones.</p>
 *
 * <p>Los enunciados en limpio, para repartir al alumnado, estan en
 * {@code recursos/ampliacion/Ejercicios_Ampliacion_MF0227_3.pdf}.</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class AmpliacionEjercicios {

    /** Ejecuta los catorce ejercicios de ampliacion, por unidad formativa. */
    public static void ejecutar() {
        System.out.println();
        System.out.println("##################################################");
        System.out.println(" AMPLIACION - MF0227_3 PROGRAMACION ORIENTADA A OBJETOS");
        System.out.println(" 14 ejercicios: 6 de la UF2404, 2 de la UF2405 y 6 de la UF2406");
        System.out.println("##################################################");
        System.out.println();
        AmpliacionUf2404.ejecutar();
        AmpliacionUf2405.ejecutar();
        AmpliacionUf2406.ejecutar();
    }

    /** Permite ejecutar la ampliacion completa de forma aislada. */
    public static void main(String[] args) {
        ejecutar();
    }
}
