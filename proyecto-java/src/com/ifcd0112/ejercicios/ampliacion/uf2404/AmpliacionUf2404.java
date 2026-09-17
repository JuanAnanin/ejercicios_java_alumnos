package com.ifcd0112.ejercicios.ampliacion.uf2404;

/**
 * Runner de la ampliacion de la UF2404.
 *
 * <p>Seis ejercicios que cubren criterios de evaluacion del Real Decreto sin
 * ejercicio en el cuaderno original: gestion de memoria (CE2.2), estructuras
 * genericas (CE2.7 y CE2.9), persistencia en ficheros (CE2.8 y CE2.10),
 * analisis de codigo ajeno con el entorno de desarrollo (CE2.5 y CE2.6), el
 * ciclo de desarrollo orientado a objetos (CE1.1) y el paso de mensajes
 * (CE1.4).</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class AmpliacionUf2404 {

    /** Ejecuta los seis ejercicios de ampliacion. */
    public static void ejecutar() {
        System.out.println("==================================================");
        System.out.println(" AMPLIACION UF2404 - PRINCIPIOS DE LA POO");
        System.out.println("==================================================");
        A01CicloVidaObjeto.resolver();
        A02EstructurasGenericas.resolver();
        A03PersistenciaFicheros.resolver();
        A04AnalisisDeCodigo.resolver();
        A13CicloDesarrollo.resolver();
        A14PasoDeMensajes.resolver();
    }

    /** Permite ejecutar la ampliacion de forma aislada. */
    public static void main(String[] args) {
        ejecutar();
    }
}
