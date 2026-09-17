package com.ifcd0112.ejercicios.uf2406.bloque2_uml;

/**
 * UF2406 - BLOQUE 2 - EJERCICIO 3: Diagrama de casos de uso del gimnasio.
 *
 * <p>Criterios de evaluacion: CE5.1</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej03CasosDeUso {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Elabora el diagrama de casos de uso del sistema del gimnasio a partir de
     * los requisitos del ejercicio 1.
     *
     * Se pide:
     *   1. Identifica todos los actores del sistema (ten en cuenta que socio,
     *      monitor y personal de recepcion tienen necesidades distintas).
     *   2. Representa cada requisito funcional como un caso de uso dentro del
     *      limite del sistema.
     *   3. Incluye al menos una relacion "include" y justifica por que la has
     *      aplicado ahi.
     *   4. Asocia cada caso de uso con el actor o actores que pueden ejecutarlo.
     *   5. Elabora la descripcion textual detallada de uno de los casos de uso,
     *      indicando: actor principal, precondiciones, flujo normal paso a paso
     *      y al menos dos flujos alternativos o de error.
     *
     * Pista: un error frecuente es dibujar como casos de uso acciones internas
     * del sistema (por ejemplo, "validar datos"). Un caso de uso debe aportar
     * valor observable a un actor, no describir un paso tecnico interno.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * DONDE ESTA LA SOLUCION.
     *
     * El diagrama, la tabla de correspondencia entre casos de uso, actores y
     * requisitos, y la descripcion textual completa de "Inscribirse en clase"
     * con sus tres flujos alternativos estan en:
     *
     *      recursos/uf2406/02_diagramas_uml.md
     *
     * APARTADO 3: por que un "include" y no un "extend".
     *
     * "Comprobar cuotas al corriente", que sale del RF-08, se ejecuta SIEMPRE
     * como parte de "Inscribirse en clase", y NUNCA de forma aislada por
     * decision del actor. Eso es exactamente lo que significa include:
     * comportamiento comun y obligatorio que se extrae para no repetirlo en
     * varios casos de uso.
     *
     * La distincion con extend conviene dejarla clara porque se confunden a
     * todas horas:
     *
     *      include   el comportamiento incluido SIEMPRE ocurre. La flecha va
     *                DEL caso base AL incluido.
     *      extend    el comportamiento anadido ocurre SOLO en ciertas
     *                condiciones. La flecha va DEL caso que extiende AL base,
     *                que es al reves de lo que dicta la intuicion.
     *
     * Si el gimnasio ofreciese "apuntarse a la lista de espera" unicamente
     * cuando la clase esta llena, esa si seria una relacion extend sobre
     * "Inscribirse en clase". Y es justo lo que va a pasar en el ejercicio 12,
     * cuando el cliente pida ese cambio.
     *
     * EL ERROR DE LA PISTA, POR QUE IMPORTA.
     *
     * Dibujar "validar datos" o "conectar a la base de datos" como casos de uso
     * es el fallo mas repetido del ejercicio. La comprobacion practica es
     * sencilla: si el actor no sabe explicar para que le sirve, no es un caso de
     * uso. Un socio entiende perfectamente "inscribirme en una clase"; nunca
     * dira que quiere "validar datos".
     *
     * El motivo de fondo es que el diagrama de casos de uso pertenece al ANALISIS
     * y describe QUE hace el sistema para alguien. Los pasos tecnicos internos
     * son diseno y se documentan en un diagrama de secuencia, que es lo que se
     * hace en el ejercicio 5.
     */

    /** Ruta del documento con el diagrama y la descripcion textual. */
    public static final String DOCUMENTO = "recursos/uf2406/02_diagramas_uml.md";

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2406-E03] Diagrama de casos de uso del gimnasio");
        System.out.println("  Diagrama, actores y descripcion textual completa:");
        System.out.println("    " + DOCUMENTO);
        System.out.println("  Apartado 1, actores: Socio, Monitor y Recepcionista. Cada uno tiene");
        System.out.println("    necesidades distintas, y por eso son tres y no uno.");
        System.out.println("  Apartado 3, el include: Comprobar cuotas al corriente (RF-08) se");
        System.out.println("    ejecuta SIEMPRE dentro de Inscribirse en clase y nunca por decision");
        System.out.println("    del actor. Si fuera opcional seria extend, no include.");
        System.out.println("  Apartado 5: el caso de uso Inscribirse en clase esta descrito con su");
        System.out.println("    flujo normal de 6 pasos y tres flujos alternativos: clase completa,");
        System.out.println("    cuotas pendientes y socio ya inscrito.");
        System.out.println("  Al corregir: nada de casos de uso tipo validar datos o conectar a la");
        System.out.println("    base de datos. Si el actor no sabe para que le sirve, no es un caso");
        System.out.println("    de uso, es un paso tecnico y va en el diagrama de secuencia.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
