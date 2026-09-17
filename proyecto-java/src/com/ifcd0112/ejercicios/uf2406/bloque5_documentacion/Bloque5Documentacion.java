package com.ifcd0112.ejercicios.uf2406.bloque5_documentacion;

/**
 * Runner del BLOQUE 5 de la UF2406: documentacion, calidad e interfaz de usuario.
 *
 * <p>Ejecuta los ejercicios 11 a 15 del cuaderno
 * Ejercicios_UF2406_Ciclo_Vida.docx.</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Bloque5Documentacion {

    /** Ejecuta los cinco ejercicios del bloque. */
    public static void ejecutar() {
        System.out.println("==================================================");
        System.out.println(" UF2406 - BLOQUE 5: DOCUMENTACION, CALIDAD E INTERFAZ DE USUARIO");
        System.out.println("==================================================");
        Ej11Javadoc.resolver();
        Ej12CambioEspecificacion.resolver();
        Ej13Depuracion.resolver();
        Ej14InterfazUsabilidad.resolver();
        Ej15ProyectoIntegrador.resolver();
    }

    /** Permite ejecutar el bloque de forma aislada. */
    public static void main(String[] args) {
        ejecutar();
    }
}
