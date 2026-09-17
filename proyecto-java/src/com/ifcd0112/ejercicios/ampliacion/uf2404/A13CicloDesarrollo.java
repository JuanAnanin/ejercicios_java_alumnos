package com.ifcd0112.ejercicios.ampliacion.uf2404;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * AMPLIACION UF2404 - EJERCICIO A13: El ciclo de desarrollo orientado a objetos.
 *
 * <p>Criterios de evaluacion: CE1.1, CE1.8</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class A13CicloDesarrollo {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Este proyecto no empieza en la primera clase que escribiste: empieza
     * mucho antes, con un cliente que cuenta lo que necesita. El sistema de
     * gestion del gimnasio que recorre la UF2406 esta terminado, y sus
     * artefactos siguen ahi: requisitos, tarjetas CRC, diagramas, codigo,
     * pruebas y manuales. Este ejercicio pide recorrer ese ciclo hacia atras,
     * sobre un caso ya resuelto, para situar donde encaja exactamente la
     * programacion orientada a objetos.
     *
     * Se pide:
     *   1. Enumera las fases del ciclo de desarrollo del software bajo el
     *      paradigma orientado a objetos y describe, para cada una, que entra,
     *      que sale y que documento o artefacto la representa.
     *   2. Localiza dentro de este proyecto el artefacto concreto que
     *      corresponde a cada fase del sistema del gimnasio y anotalo junto a
     *      la fase. Si alguna fase no tiene artefacto, dilo.
     *   3. Comprueba la trazabilidad de la cadena: verifica que todo lo que
     *      entra en una fase ha salido de alguna fase anterior, y senala en que
     *      punto exacto del ciclo entra la programacion orientada a objetos.
     *   4. Explica por que la POO es UNA fase del ciclo y no el ciclo entero:
     *      que decisiones estan ya tomadas antes de escribir la primera clase, y
     *      que queda por hacer despues de que el codigo compile.
     *   5. Compara este ciclo con el del paradigma estructurado: que unidad
     *      recorre las fases en cada uno, y que consecuencia tiene eso sobre el
     *      mantenimiento cuando cambia un requisito.
     *   6. Explica por que el ciclo orientado a objetos se recorre en espiral y
     *      no en cascada, y que significa que las fases compartan vocabulario.
     *   7. Escribe un programa que, dado el nombre de una fase, devuelva sus
     *      entradas, sus salidas y su artefacto, y que compruebe de forma
     *      automatica la trazabilidad de la cadena completa.
     *
     * Pista: la prueba de si has entendido el ciclo es sencilla. Coge el nombre
     * de una clase cualquiera del proyecto, por ejemplo ClaseColectiva, y
     * rastrea hacia atras de donde ha salido: de un diagrama, que salio de unas
     * tarjetas CRC, que salieron de un texto de requisitos, que salio de una
     * conversacion. Si el rastro se corta en algun punto, ahi falta una fase.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * APARTADO 4: por que la POO es una fase y no el ciclo.
     *
     * Cuando te sientas a escribir la primera clase ya estan decididos:
     *
     *   - QUE tiene que hacer el sistema y que NO (analisis de requisitos).
     *   - QUE clases existen, que sabe cada una y de quien depende (diseno).
     *   - Como se reparte la responsabilidad entre ellas (tarjetas CRC).
     *   - Que estructura de datos conviene a cada atributo (diseno detallado).
     *
     * Y despues de que el codigo compile todavia queda:
     *
     *   - Probar que hace lo que dice que hace (pruebas).
     *   - Documentar la clase y la operacion del sistema (documentacion).
     *   - Incorporar los cambios que pida el cliente sin romper lo anterior
     *     (mantenimiento).
     *
     * Programar es la fase en la que las decisiones ya tomadas se convierten en
     * codigo ejecutable. Es la unica fase que produce algo que la maquina
     * entiende, y por eso se confunde con el todo; pero un proyecto que empieza
     * por ahi solo puede acertar por casualidad.
     *
     * APARTADO 5: frente al paradigma estructurado.
     *
     *   Estructurado                          Orientado a objetos
     *   -----------------------------------   -----------------------------------
     *   Los datos y las funciones se disenan   La clase (datos + comportamiento)
     *   por separado y viajan por separado     es la unidad que recorre TODAS las
     *   por las fases                          fases, del analisis al codigo
     *
     *   El analisis produce diagramas de       El analisis produce clases que se
     *   flujo de datos; el diseno, una         reconocen en el diseno y siguen
     *   descomposicion funcional distinta      llamandose igual en el codigo
     *
     *   Cambiar un requisito obliga a tocar    Cambiar un requisito suele afectar
     *   todas las funciones que manipulan      a la clase responsable de ese
     *   ese dato                               concepto, y poco mas
     *
     * La consecuencia practica es el llamado salto de representacion. En el
     * ciclo estructurado hay que traducir de un lenguaje a otro entre fase y
     * fase; en el orientado a objetos, "Socio" se llama Socio en la entrevista
     * con el cliente, en el diagrama de clases y en el fichero .java. Eso es lo
     * que significa que las fases compartan vocabulario (apartado 6), y es la
     * razon de que la trazabilidad se pueda comprobar leyendo, como hace el
     * apartado 3.
     *
     * APARTADO 6: espiral, no cascada.
     *
     * La cascada supone que una fase termina antes de que empiece la siguiente.
     * En la practica, escribir la clase descubre huecos en el diseno, y probarla
     * descubre requisitos que nadie habia contado. El ciclo orientado a objetos
     * asume ese retorno: se recorre entero sobre un subconjunto pequeno del
     * sistema, se entrega, y se vuelve a recorrer anadiendo. Cada vuelta pasa
     * por las mismas fases, con mas sistema dentro.
     *
     * En este proyecto eso se ve en el ejercicio 12 de la UF2406: el cliente
     * pide la lista de espera con el sistema ya funcionando, y el cambio vuelve
     * a recorrer requisitos, diseno, codigo, pruebas y documentacion. Es la
     * segunda vuelta de la espiral.
     */

    // -----------------------------------------------------------------
    // APARTADOS 1, 2 y 7: el ciclo como datos
    // -----------------------------------------------------------------

    /**
     * Una fase del ciclo de desarrollo.
     *
     * @param nombre     nombre de la fase
     * @param entradas   productos que necesita para empezar
     * @param salidas    productos que deja terminados
     * @param artefacto  fichero de este proyecto que la representa
     * @param esPoo      cierto solo en la fase en la que se programa
     */
    public record Fase(String nombre, List<String> entradas, List<String> salidas,
                       String artefacto, boolean esPoo) { }

    /** El producto con el que arranca todo y que no fabrica ninguna fase. */
    private static final String ORIGEN = "necesidad del cliente";

    /** El ciclo completo, en orden. */
    public static List<Fase> ciclo() {
        List<Fase> f = new ArrayList<>();
        f.add(new Fase("1. Analisis de requisitos",
                List.of(ORIGEN),
                List.of("catalogo de requisitos", "casos de uso"),
                "uf2406/bloque1_requisitos/Ej01Requisitos.java", false));
        f.add(new Fase("2. Analisis orientado a objetos",
                List.of("catalogo de requisitos"),
                List.of("clases candidatas", "reparto de responsabilidades"),
                "uf2406/bloque1_requisitos/Ej02TarjetasCrc.java", false));
        f.add(new Fase("3. Diseno estructural",
                List.of("clases candidatas", "reparto de responsabilidades"),
                List.of("diagrama de clases"),
                "uf2406/bloque2_uml/Ej04DiagramaClases.java", false));
        f.add(new Fase("4. Diseno del comportamiento",
                List.of("diagrama de clases", "casos de uso"),
                List.of("diagramas de secuencia y de estados"),
                "uf2406/bloque2_uml/Ej05SecuenciaEstados.java", false));
        f.add(new Fase("5. Diseno detallado",
                List.of("diagrama de clases", "diagramas de secuencia y de estados"),
                List.of("documento de diseno de la clase"),
                "ampliacion/uf2406/A07DocumentoDiseno.java", false));
        f.add(new Fase("6. PROGRAMACION ORIENTADA A OBJETOS",
                List.of("documento de diseno de la clase"),
                List.of("codigo fuente compilable"),
                "uf2406/bloque5_documentacion/Ej14InterfazUsabilidad.java", true));
        f.add(new Fase("7. Pruebas",
                List.of("codigo fuente compilable", "catalogo de requisitos"),
                List.of("bateria de pruebas", "informe de pruebas"),
                "uf2406/bloque4_pruebas/Ej09Junit.java", false));
        f.add(new Fase("8. Documentacion",
                List.of("codigo fuente compilable", "informe de pruebas"),
                List.of("javadoc", "manual de operacion", "guia de usuario"),
                "ampliacion/uf2406/A09ManualOperacion.java", false));
        f.add(new Fase("9. Mantenimiento",
                List.of("codigo fuente compilable", "bateria de pruebas",
                        "manual de operacion", "catalogo de requisitos"),
                List.of("version nueva", "historico de cambios"),
                "uf2406/bloque5_documentacion/Ej12CambioEspecificacion.java", false));
        return f;
    }

    /**
     * Apartado 7: busca una fase por una palabra de su nombre.
     *
     * @param texto fragmento del nombre de la fase, sin distinguir mayusculas
     * @return la primera fase cuyo nombre lo contenga, o null si no hay ninguna
     */
    public static Fase buscar(String texto) {
        for (Fase f : ciclo()) {
            if (f.nombre().toLowerCase().contains(texto.toLowerCase())) {
                return f;
            }
        }
        return null;
    }

    /**
     * Apartado 3: comprueba que todo lo que entra en una fase ha salido antes.
     *
     * @return lista vacia si la cadena es trazable; si no, un aviso por rotura
     */
    public static List<String> comprobarTrazabilidad() {
        List<String> fallos = new ArrayList<>();
        Set<String> disponible = new LinkedHashSet<>();
        disponible.add(ORIGEN);
        for (Fase f : ciclo()) {
            for (String e : f.entradas()) {
                if (!disponible.contains(e)) {
                    fallos.add(f.nombre() + " necesita \"" + e
                            + "\", que no ha producido ninguna fase anterior");
                }
            }
            disponible.addAll(f.salidas());
        }
        return fallos;
    }

    /**
     * Apartado 2: localiza la raiz del proyecto subiendo desde el directorio de
     * trabajo, para poder comprobar si los artefactos existen de verdad.
     *
     * @return el directorio que contiene src/com/ifcd0112/ejercicios, o null
     */
    private static File raizDelProyecto() {
        File d = new File(System.getProperty("user.dir")).getAbsoluteFile();
        for (int i = 0; i < 6 && d != null; i++, d = d.getParentFile()) {
            if (new File(d, "src/com/ifcd0112/ejercicios").isDirectory()) {
                return d;
            }
        }
        return null;
    }

    // =====================================================================
    // COMPROBACION
    // =====================================================================

    /** Ejecuta el ejercicio completo. Lo invoca el runner de la ampliacion. */
    public static void resolver() {
        System.out.println("[AMPL-A13] El ciclo de desarrollo orientado a objetos");

        // Apartados 1 y 2.
        System.out.println("  Fases del ciclo, con su artefacto en este proyecto:");
        File raiz = raizDelProyecto();
        int encontrados = 0;
        for (Fase f : ciclo()) {
            String marca = "   ";
            if (raiz != null) {
                File art = new File(raiz, "src/com/ifcd0112/ejercicios/" + f.artefacto());
                boolean hay = art.isFile();
                if (hay) {
                    encontrados++;
                }
                marca = hay ? "[ok]" : "[??]";
            }
            System.out.printf("    %s %-38s %s%n", marca, f.nombre(), f.artefacto());
            System.out.println("         entra: " + String.join(", ", f.entradas()));
            System.out.println("         sale : " + String.join(", ", f.salidas()));
        }
        if (raiz == null) {
            System.out.println("  (no se ha localizado la raiz del proyecto desde "
                    + System.getProperty("user.dir") + ";");
            System.out.println("   los artefactos no se han podido comprobar en disco)");
        } else {
            System.out.println("  Artefactos localizados en disco: " + encontrados
                    + " de " + ciclo().size());
        }

        // Apartado 3.
        List<String> fallos = comprobarTrazabilidad();
        System.out.println("  Trazabilidad de la cadena: "
                + (fallos.isEmpty() ? "completa, ninguna entrada sin origen"
                                    : fallos.size() + " roturas"));
        for (String f : fallos) {
            System.out.println("    ROTURA: " + f);
        }

        // Donde entra la POO.
        for (int i = 0; i < ciclo().size(); i++) {
            if (ciclo().get(i).esPoo()) {
                System.out.println("  La POO es la fase " + (i + 1) + " de " + ciclo().size()
                        + ": recibe un diseno y entrega codigo que compila.");
                System.out.println("    Antes de ella hay " + i + " fases; despues, "
                        + (ciclo().size() - i - 1) + ".");
            }
        }

        // Apartado 7: consulta suelta.
        Fase p = buscar("pruebas");
        System.out.println("  Consulta de ejemplo, buscar(\"pruebas\") -> " + p.nombre());
        System.out.println("    entra: " + String.join(", ", p.entradas()));
        System.out.println("    sale : " + String.join(", ", p.salidas()));
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
