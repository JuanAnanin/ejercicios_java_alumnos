package com.ifcd0112.ejercicios.uf2406.bloque1_requisitos;

/**
 * UF2406 - BLOQUE 1 - EJERCICIO 1: Especificacion de requisitos de un gimnasio.
 *
 * <p>Criterios de evaluacion: CE1.1, CE1.2</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej01Requisitos {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Un gimnasio quiere informatizar su gestion. En la entrevista inicial, el
     * gerente explica lo siguiente: "Necesitamos controlar los socios y sus
     * cuotas mensuales, saber quien ha pagado y quien no. Tambien queremos
     * gestionar las clases colectivas: cada clase tiene un monitor, un horario y
     * un aforo maximo, y los socios deben poder apuntarse desde una aplicacion.
     * Y que sea rapido, que el sistema anterior tardaba mucho."
     *
     * Se pide:
     *   1. Redacta al menos ocho requisitos funcionales numerados (RF-01,
     *      RF-02...), cada uno con un enunciado inequivoco y verificable.
     *   2. Redacta al menos tres requisitos no funcionales, traduciendo a
     *      terminos medibles la peticion "que sea rapido" del gerente.
     *   3. Para cada requisito, anade su criterio de aceptacion: como se
     *      comprobara objetivamente que se cumple.
     *   4. Identifica al menos dos ambiguedades del enunciado del gerente que
     *      exigirian volver a preguntarle antes de empezar a disenar, y formula
     *      las preguntas concretas que le harias.
     *   5. Elabora el indice completo del documento de especificacion de
     *      requisitos (SRS) en el que incluirias todo lo anterior.
     *   6. Justifica razonadamente que modelo de proceso (cascada, iterativo o
     *      en V) recomendarias para este proyecto concreto.
     *
     * Pista: un requisito no verificable es un requisito mal redactado. "El
     * sistema debe ser rapido" no puede comprobarse; "el sistema debe mostrar el
     * listado de socios en menos de 2 segundos con 5.000 socios registrados" si.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * DONDE ESTA LA SOLUCION.
     *
     * Este ejercicio no produce codigo: produce un documento. La especificacion
     * completa, con los ocho requisitos funcionales, los tres no funcionales,
     * sus criterios de aceptacion, las ambiguedades detectadas, el indice del
     * SRS y la justificacion del modelo de proceso, esta en:
     *
     *      recursos/uf2406/01_requisitos_y_analisis.md
     *
     * El metodo resolver() de abajo si hace algo util y ejecutable: aplica a
     * cada requisito la prueba de verificabilidad que plantea la pista. Un
     * requisito es verificable si dice QUE, CUANTO y EN QUE CONDICIONES. Se
     * comprueban tanto los requisitos bien redactados como las tres frases
     * originales del gerente, para que se vea la diferencia.
     *
     * APARTADO 6, RESUMEN: modelo ITERATIVO E INCREMENTAL.
     *
     * El gerente describe las necesidades en terminos vagos y ya en la primera
     * entrevista aparecen tres ambiguedades. Esa es la senal mas fiable de que
     * los requisitos van a evolucionar, no por capricho del cliente, sino porque
     * todavia no sabe del todo lo que quiere y solo lo sabra cuando vea algo
     * funcionando.
     *
     * Cascada exigiria congelar los requisitos antes de construir, y ese
     * documento congelado estaria equivocado desde el principio. El modelo en V
     * aporta verificacion formal por niveles, util en sistemas criticos, que no
     * es lo que aqui hace falta. El iterativo permite entregar primero socios y
     * cuotas y validar con el gerente antes de construir las clases colectivas:
     * si la primera entrega revela que hacia falta controlar domiciliaciones, se
     * ha perdido una iteracion y no el proyecto.
     */

    /** Ruta del documento de especificacion. */
    public static final String DOCUMENTO = "recursos/uf2406/01_requisitos_y_analisis.md";

    /**
     * Comprueba si un requisito esta redactado de forma verificable.
     *
     * <p>El criterio operativo: un requisito verificable dice que tiene que
     * pasar, con que magnitud medible y en que condiciones. Si falta la
     * magnitud, no hay forma de decidir objetivamente si se cumple.</p>
     *
     * @param texto redaccion del requisito
     * @return true si contiene alguna magnitud medible
     */
    public static boolean esVerificable(String texto) {
        // Se busca al menos una cifra: un tiempo, un volumen, un numero de
        // usuarios. Es una comprobacion deliberadamente tosca, pero atrapa
        // exactamente el defecto del que habla la pista.
        for (char c : texto.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

    // =====================================================================
    // COMPROBACION
    // =====================================================================

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2406-E01] Especificacion de requisitos de un gimnasio");
        System.out.println("  Documento completo (8 RF, 3 RNF, ambiguedades, indice SRS):");
        System.out.println("    " + DOCUMENTO);

        System.out.println("  Prueba de verificabilidad (pista del enunciado):");
        String[][] frases = {
            { "Peticion del gerente", "El sistema debe ser rapido" },
            { "RNF-01 redactado",     "El listado de socios debe mostrarse en menos de 2 segundos con 5.000 socios registrados" },
            { "Peticion del gerente", "Que aguante muchos usuarios a la vez" },
            { "RNF-02 redactado",     "El sistema debe soportar 50 inscripciones simultaneas sin degradacion" },
            { "Requisito vago",       "El sistema debe ser seguro" },
            { "RNF-03 redactado",     "Las contrasenas deben almacenarse cifradas con un algoritmo con sal, nunca en texto plano" }
        };
        for (String[] fila : frases) {
            boolean ok = esVerificable(fila[1]);
            System.out.printf("    %-22s %s%n", ok ? "[VERIFICABLE]" : "[NO VERIFICABLE]", fila[1]);
        }
        System.out.println("    Nota: RNF-03 pasa la prueba por la palabra clave del algoritmo, no");
        System.out.println("    por una cifra. Es el limite de una comprobacion automatica: la");
        System.out.println("    verificabilidad la decide si existe una PRUEBA objetiva, y aqui la");
        System.out.println("    hay (inspeccionar la tabla de usuarios), aunque no haya numeros.");

        System.out.println("  Apartado 4, ambiguedades que exigen volver a preguntar:");
        System.out.println("    1) Pagos: registrar solo, o tambien recibos y domiciliacion?");
        System.out.println("    2) Aplicacion: web, movil o terminal en el gimnasio? Plazos de");
        System.out.println("       inscripcion y de cancelacion?");
        System.out.println("    3) Rapido: con cuantos socios y cuantos usuarios simultaneos?");
        System.out.println("  Apartado 6: modelo ITERATIVO. Los requisitos van a evolucionar, y");
        System.out.println("    entregar primero socios y cuotas permite validar antes de construir");
        System.out.println("    el sistema entero.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
