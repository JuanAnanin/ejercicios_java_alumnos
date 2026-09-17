package com.ifcd0112.ejercicios.uf2405.bloque4_web;

/**
 * Runner del BLOQUE 4 de la UF2405: capa de presentacion web.
 *
 * <p>Ejecuta los ejercicios 9 a 13 del cuaderno
 * Ejercicios_UF2405_Web_BBDD.docx.</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Bloque4Web {

    /** Ejecuta los cinco ejercicios del bloque. */
    public static void ejecutar() {
        System.out.println("==================================================");
        System.out.println(" UF2405 - BLOQUE 4: CAPA DE PRESENTACION WEB");
        System.out.println("==================================================");
        Ej09FormularioHtml.resolver();
        Ej10ServletListado.resolver();
        Ej11ServletFormulario.resolver();
        Ej12Sesiones.resolver();
        Ej13ServletAJsp.resolver();
    }

    /** Permite ejecutar el bloque de forma aislada. */
    public static void main(String[] args) {
        ejecutar();
    }
}
