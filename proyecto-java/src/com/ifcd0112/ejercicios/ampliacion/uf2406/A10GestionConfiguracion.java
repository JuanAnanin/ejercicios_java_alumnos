package com.ifcd0112.ejercicios.ampliacion.uf2406;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * AMPLIACION UF2406 - EJERCICIO A10: Gestion de la configuracion del software.
 *
 * <p>Criterios de evaluacion: CE4.2, CE4.4, CE4.6</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class A10GestionConfiguracion {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * En el ejercicio 12 modificaste una clase siguiendo un procedimiento. Este
     * ejercicio se ocupa de lo que rodea a ese cambio: como se identifica cada
     * version, como se guarda su historia y como se sabe exactamente que codigo
     * hay instalado en el gimnasio.
     *
     * Se pide:
     *   1. Explica que es la gestion de la configuracion del software y en que
     *      se diferencia de la gestion de la configuracion de la documentacion.
     *   2. Define los elementos de configuracion del proyecto del gimnasio: que
     *      se versiona y que no, justificando cada exclusion.
     *   3. Implementa un versionador que, dado el tipo de cambio, calcule la
     *      version siguiente segun el versionado semantico.
     *   4. Escribe un validador de mensajes de registro de cambios que compruebe
     *      una convencion acordada por el equipo, y pasale mensajes reales,
     *      buenos y malos.
     *   5. Explica que es una linea base y por que hace falta poder reconstruir
     *      exactamente la version que hay instalada en el cliente.
     *   6. Aplica el procedimiento de cambio a un DOCUMENTO, no a una clase:
     *      indica como se identifica una edicion y una revision, y quien
     *      aprueba cada cambio.
     *   7. Enumera las ordenes concretas del sistema de control de versiones
     *      para: crear la rama del cambio del ejercicio 12, integrarla, etiquetar
     *      la entrega y volver atras si sale mal.
     *
     * Pista: la pregunta que resuelve la gestion de la configuracion es siempre
     * la misma, y es mas practica de lo que parece: si el gimnasio llama manana
     * diciendo que algo falla, puedes reconstruir EXACTAMENTE el codigo que
     * tienen instalado?
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * APARTADO 1: configuracion del software y de la documentacion.
     *
     *   DEL SOFTWARE. Identifica y controla el codigo, sus dependencias, los
     *   scripts de base de datos y la configuracion de despliegue. Responde a
     *   "que version esta instalada y como la reconstruyo".
     *
     *   DE LA DOCUMENTACION. Lo mismo con los documentos: requisitos, diagramas,
     *   manuales. Responde a "que version del documento aprobo el cliente".
     *
     *   Se parecen tanto que suele hacerse en el mismo sitio, versionando la
     *   documentacion junto al codigo. Y ahi esta la ventaja: si el documento
     *   vive en el repositorio, la etiqueta de la version 1.3 incluye el codigo
     *   Y los manuales de esa version. Si vive en una carpeta compartida, nadie
     *   sabe que manual corresponde a que entrega.
     *
     *   La diferencia que queda es de APROBACION: un cambio de codigo lo aprueba
     *   quien revisa; un cambio en el documento de requisitos lo aprueba el
     *   cliente, y esa firma tiene consecuencias contractuales.
     *
     * APARTADO 2: que se versiona y que no.
     *
     *   SE VERSIONA        src/, recursos/, los scripts SQL, la documentacion,
     *                      los ficheros de construccion y el .gitignore.
     *   NO SE VERSIONA     out/ y docs/, porque se generan: versionar los .class
     *                      produce conflictos en cada compilacion y no aporta
     *                      nada, ya que se reconstruyen del fuente.
     *                      Tampoco las credenciales ni los ficheros del IDE,
     *                      que son de cada persona.
     *
     *   La regla: se versiona lo que NO se puede regenerar. Y una excepcion
     *   importante que suele discutirse: las dependencias externas SI conviene
     *   fijarlas por version exacta, aunque no se guarden los jar. Un proyecto
     *   que dice "la ultima version" de una biblioteca no se puede reconstruir
     *   igual dentro de dos anos.
     *
     * APARTADO 5: la linea base.
     *
     * Una linea base es una foto aprobada del conjunto: este codigo, con estos
     * documentos, con estas dependencias, es la version 1.3. A partir de ahi,
     * los cambios se hacen SOBRE la linea base y de forma controlada.
     *
     * Por que hace falta: cuando el gimnasio llama diciendo que la lista de
     * espera hace algo raro, la primera pregunta es que version tienen. Si no se
     * puede reconstruir ese codigo exacto, se esta depurando a ciegas sobre una
     * version que a lo mejor ni siquiera es la suya. Es la diferencia entre
     * arreglar un fallo y perseguirlo.
     *
     * APARTADO 6: el cambio de un DOCUMENTO.
     *
     *   EDICION   cambia cuando el contenido se modifica de forma sustancial y
     *             vuelve a aprobarse. Se numera 1, 2, 3.
     *   REVISION  cambio menor dentro de la misma edicion: una errata, una
     *             aclaracion. Se numera 1.0, 1.1, 1.2.
     *
     *   Procedimiento: identificar el documento y su edicion actual, registrar
     *   la peticion de cambio con su motivo, redactar la version nueva marcando
     *   lo que cambia, someterla a aprobacion de quien corresponda, publicarla y
     *   RETIRAR la anterior de circulacion. Este ultimo paso es el que se olvida,
     *   y es el que provoca que alguien trabaje seis meses con un manual viejo.
     */

    /** Tipo de cambio, segun su efecto sobre quien usa el software. */
    public enum TipoCambio {
        /** Rompe la compatibilidad: quien lo use tendra que cambiar su codigo. */
        INCOMPATIBLE,
        /** Anade funcionalidad sin romper nada de lo anterior. */
        FUNCIONALIDAD,
        /** Corrige un fallo sin anadir ni romper nada. */
        CORRECCION
    }

    /** Version segun el versionado semantico: mayor.menor.parche. */
    public record Version(int mayor, int menor, int parche) {

        /**
         * Apartado 3: calcula la version siguiente segun el tipo de cambio.
         *
         * @param cambio naturaleza del cambio realizado
         * @return la version resultante
         */
        public Version siguiente(TipoCambio cambio) {
            switch (cambio) {
                case INCOMPATIBLE:
                    // Al subir el mayor, los otros dos vuelven a cero: la 2.0.0
                    // no es "la 1.4.7 con algo mas", es otra cosa.
                    return new Version(mayor + 1, 0, 0);
                case FUNCIONALIDAD:
                    return new Version(mayor, menor + 1, 0);
                default:
                    return new Version(mayor, menor, parche + 1);
            }
        }

        @Override
        public String toString() { return mayor + "." + menor + "." + parche; }
    }

    /**
     * Apartado 4: valida un mensaje de registro de cambios.
     *
     * <p>La convencion acordada por el equipo: primera linea de como mucho 72
     * caracteres, empezando por la referencia al requisito o a la incidencia,
     * sin punto final, y con un cuerpo que explique el porque.</p>
     *
     * @param mensaje mensaje completo, con sus saltos de linea
     * @return lista de incumplimientos; vacia si el mensaje es correcto
     */
    public static List<String> validarMensaje(String mensaje) {
        List<String> problemas = new ArrayList<>();
        if (mensaje == null || mensaje.isBlank()) {
            problemas.add("el mensaje esta vacio");
            return problemas;
        }
        String[] lineas = mensaje.split("\n");
        String primera = lineas[0].trim();

        if (primera.length() > 72) {
            problemas.add("la primera linea tiene " + primera.length()
                    + " caracteres y el maximo son 72");
        }
        if (primera.endsWith(".")) {
            problemas.add("la primera linea no debe terminar en punto");
        }
        if (!primera.matches("^(RF|RNF|INC)-\\d+:.*")) {
            problemas.add("debe empezar por la referencia, por ejemplo RF-09: o INC-42:");
        }
        // Un mensaje sin cuerpo dice QUE se hizo pero no POR QUE, que es lo unico
        // que no se puede deducir del propio cambio.
        boolean tieneCuerpo = false;
        for (int i = 1; i < lineas.length; i++) {
            if (!lineas[i].isBlank()) {
                tieneCuerpo = true;
                break;
            }
        }
        if (!tieneCuerpo) {
            problemas.add("falta el cuerpo que explica el porque del cambio");
        }
        String texto = primera.toLowerCase();
        for (String vago : new String[] { "cambios", "arreglos", "varios", "actualizacion",
                                          "fix", "wip", "pruebas" }) {
            if (texto.contains(": " + vago) || texto.endsWith(vago)) {
                problemas.add("el resumen es vago: '" + vago + "' no dice nada");
                break;
            }
        }
        return problemas;
    }

    // =====================================================================
    // COMPROBACION
    // =====================================================================

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[AMP-UF2406-A10] Gestion de la configuracion del software");

        // Apartado 3: el versionador, sobre la historia real del proyecto.
        System.out.println("  Apartado 3, versionado semantico aplicado a la historia del gimnasio:");
        Version v = new Version(1, 0, 0);
        Object[][] historia = {
            { "Version inicial de la gestion de inscripciones", null },
            { "Se corrige la media de cuotas: contador fuera del if (ej. 13)", TipoCambio.CORRECCION },
            { "RF-09: lista de espera en clases completas (ej. 12)", TipoCambio.FUNCIONALIDAD },
            { "Se corrige la columna ultima_visita que faltaba", TipoCambio.CORRECCION },
            { "inscribir() deja de lanzar ClaseCompletaException", TipoCambio.INCOMPATIBLE }
        };
        for (Object[] paso : historia) {
            TipoCambio tipo = (TipoCambio) paso[1];
            if (tipo != null) {
                v = v.siguiente(tipo);
            }
            System.out.printf("    %-8s %-14s %s%n", v,
                    (tipo == null) ? "(inicial)" : tipo.toString().toLowerCase(), paso[0]);
        }
        System.out.println("    El ultimo cambio es el interesante: quitar una excepcion del");
        System.out.println("    contrato ROMPE a quien la capturaba, aunque el codigo siga");
        System.out.println("    compilando. Por eso sube el numero mayor y no el menor.");

        // Apartado 4: el validador, con mensajes reales.
        System.out.println("  Apartado 4, validacion de mensajes de registro:");
        String[] mensajes = {
            "cambios",
            "Arreglado el tema de las inscripciones que no iban bien del todo cuando la clase "
                + "estaba llena.",
            "RF-09: gestion de lista de espera en clases completas\n"
                + "\n"
                + "Las inscripciones sobre clases con aforo lleno pasan a EN_ESPERA en lugar de\n"
                + "rechazarse, y se promocionan al producirse una baja. Incluye 4 pruebas nuevas."
        };
        for (String m : mensajes) {
            String primera = m.split("\n")[0];
            System.out.println("    Mensaje: \""
                    + (primera.length() > 62 ? primera.substring(0, 59) + "..." : primera) + "\"");
            List<String> problemas = validarMensaje(m);
            if (problemas.isEmpty()) {
                System.out.println("      CORRECTO");
            } else {
                for (String p : problemas) {
                    System.out.println("      RECHAZADO: " + p);
                }
            }
        }

        // Apartado 7: las ordenes concretas.
        System.out.println("  Apartado 7, el cambio del ejercicio 12 con control de versiones:");
        List<String> ordenes = Arrays.asList(
            "git switch -c RF-09-lista-de-espera        # rama del cambio",
            "git add src recursos                       # solo lo que se versiona",
            "git commit                                 # con el mensaje del apartado 4",
            "git switch main && git merge --no-ff RF-09-lista-de-espera",
            "git tag -a v1.1.0 -m \"Entrega con lista de espera\"   # la LINEA BASE",
            "git push origin main --tags",
            "",
            "# Si sale mal en produccion:",
            "git switch --detach v1.0.1                 # el codigo exacto que tenian",
            "git revert -m 1 <hash-del-merge>           # deshacer conservando la historia");
        for (String o : ordenes) {
            System.out.println("    " + o);
        }
        System.out.println("    revert y no reset: reset REESCRIBE la historia, y en una rama");
        System.out.println("    compartida eso rompe el repositorio de los demas. revert crea un");
        System.out.println("    cambio nuevo que deshace el anterior y deja constancia de las dos");
        System.out.println("    cosas, que es lo que hace falta para poder explicar que paso.");

        System.out.println("  Apartado 5: la etiqueta v1.1.0 es la LINEA BASE. Sin ella, cuando el");
        System.out.println("    gimnasio llame no se podra reconstruir el codigo que tienen");
        System.out.println("    instalado, y se estara depurando a ciegas sobre otra version.");
        System.out.println("  Apartado 6: en los documentos, la EDICION cambia con una revision");
        System.out.println("    sustancial que vuelve a aprobarse y la REVISION con una errata o");
        System.out.println("    una aclaracion. El paso que se olvida siempre es retirar de");
        System.out.println("    circulacion la version anterior.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
