package com.ifcd0112.ejercicios.ampliacion.uf2405;

/**
 * Runner de la ampliacion de la UF2405.
 *
 * <p>Dos ejercicios que cierran los criterios de evaluacion sin ejercicio en el
 * cuaderno original: protocolos y formatos de intercambio (CE1.2) y eleccion de
 * la tecnologia de acceso a datos (CE2.2 y CE1.3).</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class AmpliacionUf2405 {

    /** Ejecuta los dos ejercicios de ampliacion. */
    public static void ejecutar() {
        System.out.println("==================================================");
        System.out.println(" AMPLIACION UF2405 - MODELO WEB Y BASES DE DATOS");
        System.out.println("==================================================");
        A05ProtocolosFormatos.resolver();
        A06TecnologiasAccesoDatos.resolver();
    }

    /** Permite ejecutar la ampliacion de forma aislada. */
    public static void main(String[] args) {
        ejecutar();
    }
}
