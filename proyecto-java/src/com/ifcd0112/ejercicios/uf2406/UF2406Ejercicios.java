package com.ifcd0112.ejercicios.uf2406;

import com.ifcd0112.ejercicios.uf2406.bloque1_requisitos.Bloque1Requisitos;
import com.ifcd0112.ejercicios.uf2406.bloque2_uml.Bloque2Uml;
import com.ifcd0112.ejercicios.uf2406.bloque3_planificacion.Bloque3Planificacion;
import com.ifcd0112.ejercicios.uf2406.bloque4_pruebas.Bloque4Pruebas;
import com.ifcd0112.ejercicios.uf2406.bloque5_documentacion.Bloque5Documentacion;

/**
 * Runner de la UF2406: El ciclo de vida del desarrollo de aplicaciones.
 *
 * <p>Ejecuta los 15 ejercicios del cuaderno Ejercicios_UF2406_Ciclo_Vida.docx,
 * agrupados en sus cinco bloques. Todos giran en torno al mismo caso, la
 * gestion de un gimnasio, que se construye de forma acumulativa.</p>
 *
 * <p><b>Nota importante.</b> Buena parte de esta unidad formativa no consiste en
 * programar, sino en analizar, modelar, planificar y documentar. Esos artefactos
 * estan resueltos en {@code recursos/uf2406/}, y cada fichero .java conserva el
 * enunciado, el razonamiento que se evalua y la ruta del documento
 * correspondiente. Lo que SI se ejecuta de verdad al lanzar esta clase: la
 * reconstruccion del diagrama de clases por reflexion (ejercicio 4), la maquina
 * de estados con sus 12 transiciones (5), el calculo del camino critico y el
 * experimento de compresion (6), la bateria de caja negra con su mutante (7), la
 * equivalencia exhaustiva de la refactorizacion (8), un armazon de pruebas
 * completo con su informe (9), los objetos simulados con su cronometraje (10),
 * la bateria de regresion del cambio de especificacion (12), la sesion de
 * depuracion (13) y la logica del controlador de la interfaz (14).</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class UF2406Ejercicios {

    /** Ejecuta los cinco bloques de la unidad formativa. */
    public static void ejecutar() {
        System.out.println();
        System.out.println("##################################################");
        System.out.println(" UF2406 - EL CICLO DE VIDA DEL DESARROLLO DE APLICACIONES");
        System.out.println(" 15 ejercicios en 5 bloques");
        System.out.println("##################################################");
        System.out.println();
        Bloque1Requisitos.ejecutar();
        Bloque2Uml.ejecutar();
        Bloque3Planificacion.ejecutar();
        Bloque4Pruebas.ejecutar();
        Bloque5Documentacion.ejecutar();
    }

    /** Permite ejecutar la unidad formativa completa de forma aislada. */
    public static void main(String[] args) {
        ejecutar();
    }
}
