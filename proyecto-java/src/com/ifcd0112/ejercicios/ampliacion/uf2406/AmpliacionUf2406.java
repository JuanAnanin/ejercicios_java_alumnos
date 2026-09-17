package com.ifcd0112.ejercicios.ampliacion.uf2406;

/**
 * Runner de la ampliacion de la UF2406.
 *
 * <p>Seis ejercicios que cubren la parte documental y de interfaz que quedaba
 * sin ejercicio: documento de diseno (CE3.3 y CE3.7), informe de pruebas
 * (CE3.4 y CE3.8), manual de operacion (CE3.5 y CE3.9), gestion de la
 * configuracion (CE4.2, CE4.4 y CE4.6), construccion de la interfaz desde el
 * diseno (CE5.4, CE5.6 y CE5.7) y su validacion y accesibilidad (CE5.5 y
 * CE5.8).</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class AmpliacionUf2406 {

    /** Ejecuta los seis ejercicios de ampliacion. */
    public static void ejecutar() {
        System.out.println("==================================================");
        System.out.println(" AMPLIACION UF2406 - CICLO DE VIDA DEL DESARROLLO");
        System.out.println("==================================================");
        A07DocumentoDiseno.resolver();
        A08InformePruebas.resolver();
        A09ManualOperacion.resolver();
        A10GestionConfiguracion.resolver();
        A11InterfazDesdeDiseno.resolver();
        A12ValidacionAccesibilidad.resolver();
    }

    /** Permite ejecutar la ampliacion de forma aislada. */
    public static void main(String[] args) {
        ejecutar();
    }
}
