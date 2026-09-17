package com.ifcd0112.ejercicios.uf2406.bloque1_requisitos;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * UF2406 - BLOQUE 1 - EJERCICIO 2: Del enunciado a las clases, tarjetas CRC.
 *
 * <p>Criterios de evaluacion: CE1.3</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej02TarjetasCrc {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Partiendo de los requisitos elaborados en el ejercicio anterior, realiza
     * el analisis orientado a objetos del sistema del gimnasio.
     *
     * Se pide:
     *   1. Aplica el criterio de los sustantivos para identificar las clases
     *      candidatas del dominio, y descarta razonadamente al menos dos
     *      sustantivos que NO deban convertirse en clase.
     *   2. Elabora una tarjeta CRC por cada clase identificada, con sus
     *      responsabilidades y sus colaboradores.
     *   3. Identifica las relaciones entre clases y determina en cada caso si se
     *      trata de herencia, composicion, agregacion o asociacion,
     *      justificandolo.
     *   4. Determina la multiplicidad de cada relacion.
     *   5. Comprueba que cada requisito funcional del ejercicio 1 puede
     *      satisfacerse con las clases identificadas: si alguno no encuentra
     *      acomodo, revisa el analisis.
     *
     * Pista: "Cuota" puede parecer una clase, pero conviene preguntarse si tiene
     * comportamiento propio y estado que evolucione, o si es simplemente un
     * atributo de otra entidad. No todo sustantivo merece ser una clase.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * DONDE ESTA LA SOLUCION.
     *
     * Las cinco tarjetas CRC dibujadas, las relaciones con su justificacion y
     * sus multiplicidades estan en:
     *
     *      recursos/uf2406/01_requisitos_y_analisis.md
     *
     * Lo que hace resolver() aqui es el apartado 5, que es el unico
     * mecanizable: recorre los ocho requisitos funcionales y comprueba que
     * cada uno tiene asignada al menos una clase responsable. Si un requisito
     * se quedara sin clase, saldria senalado.
     *
     * APARTADO 1: sustantivos descartados, y por que.
     *
     *   "gimnasio"    Es el sistema completo, no una entidad dentro de el.
     *                 Convertirlo en clase produce el objeto-dios de siempre,
     *                 que acaba concentrando todos los metodos.
     *   "horario"     Es un atributo de ClaseColectiva (dia y hora). No tiene
     *                 comportamiento propio ni estado que evolucione.
     *   "aplicacion"  Es el medio por el que el socio accede, no un concepto del
     *                 negocio del gimnasio.
     *
     * El criterio util no es "es un sustantivo", sino "tiene estado propio que
     * evolucione y comportamiento asociado".
     *
     * SOBRE LA PISTA: el caso dudoso de Cuota.
     *
     * Cuota SI se mantiene como clase, y conviene razonar por que. Tiene estado
     * que cambia (pendiente / pagada), fecha de pago e importe propio. Si fuese
     * una cantidad fija que no cambia nunca, seria un atributo de Socio. La
     * pregunta correcta no es si suena a entidad, sino si algo de ella cambia
     * con el tiempo y si alguien tiene que preguntarle algo.
     *
     * APARTADO 3: las relaciones, en resumen.
     *
     *   Socio 1 -- 0..* Cuota                COMPOSICION
     *       Las cuotas no existen sin el socio al que pertenecen.
     *
     *   ClaseColectiva 0..* -- 1 Monitor     ASOCIACION
     *       El monitor existe con independencia de las clases que imparta.
     *
     *   Socio 1 -- 0..* Inscripcion 0..* -- 1 ClaseColectiva
     *       Inscripcion es una CLASE-ASOCIACION: nace de una relacion N:M y
     *       merece ser clase propia porque tiene atributos y comportamiento que
     *       no pertenecen a ninguno de los dos extremos (fecha, estado y las
     *       reglas de cancelacion).
     *
     * Es el mismo razonamiento que la tabla puente de la UF2405. La diferencia
     * es que aqui la relacion ademas tiene datos propios, y eso la convierte en
     * un objeto de pleno derecho.
     */

    /** Ruta del documento con las tarjetas CRC. */
    public static final String DOCUMENTO = "recursos/uf2406/01_requisitos_y_analisis.md";

    /**
     * Apartado 5: reparto de responsabilidades por requisito.
     *
     * @return correspondencia entre cada requisito funcional y las clases que
     *         lo satisfacen
     */
    public static Map<String, String> cobertura() {
        Map<String, String> mapa = new LinkedHashMap<>();
        mapa.put("RF-01 Alta de socio",                "Socio");
        mapa.put("RF-02 Registrar pago de cuota",      "Cuota, Socio");
        mapa.put("RF-03 Socios con cuotas pendientes", "Socio.estaAlCorriente(), Cuota");
        mapa.put("RF-04 Alta de clase colectiva",      "ClaseColectiva, Monitor");
        mapa.put("RF-05 Inscribirse en clase",         "Inscripcion, ClaseColectiva.plazasLibres()");
        mapa.put("RF-06 Impedir si aforo completo",    "ClaseColectiva.estaCompleta()");
        mapa.put("RF-07 Cancelar inscripcion",         "Inscripcion.cancelar()");
        mapa.put("RF-08 Impedir si hay impagos",       "Socio.estaAlCorriente()");
        return mapa;
    }

    // =====================================================================
    // COMPROBACION (apartado 5)
    // =====================================================================

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2406-E02] Del enunciado a las clases: tarjetas CRC");
        System.out.println("  Tarjetas CRC, relaciones y multiplicidades:");
        System.out.println("    " + DOCUMENTO);
        System.out.println("  Apartado 1: clases del dominio -> Socio, Cuota, ClaseColectiva,");
        System.out.println("    Monitor, Inscripcion. Descartados: gimnasio (es el sistema entero),");
        System.out.println("    horario (atributo de ClaseColectiva) y aplicacion (es el medio).");

        System.out.println("  Apartado 5, cobertura de los requisitos funcionales:");
        Map<String, String> mapa = cobertura();
        int sinCubrir = 0;
        for (Map.Entry<String, String> e : mapa.entrySet()) {
            if (e.getValue() == null || e.getValue().isEmpty()) {
                sinCubrir++;
                System.out.printf("    %-34s SIN CLASE RESPONSABLE%n", e.getKey());
            } else {
                System.out.printf("    %-34s %s%n", e.getKey(), e.getValue());
            }
        }
        System.out.println("    Requisitos sin acomodo: " + sinCubrir + " de " + mapa.size());
        System.out.println("    Si alguno saliera sin clase, la conclusion no seria que sobra el");
        System.out.println("    requisito, sino que falta una clase o una responsabilidad.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
