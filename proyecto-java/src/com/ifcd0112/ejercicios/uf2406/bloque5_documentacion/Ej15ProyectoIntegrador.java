package com.ifcd0112.ejercicios.uf2406.bloque5_documentacion;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * UF2406 - BLOQUE 5 - EJERCICIO 15: Proyecto integrador, cierre del ciclo de vida.
 *
 * <p>Criterios de evaluacion: CE1.4, CE1.5, CE2.6</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej15ProyectoIntegrador {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Cierra el ciclo de vida completo del sistema del gimnasio, integrando
     * todos los artefactos elaborados en los ejercicios anteriores.
     *
     * Se pide:
     *   1. Elabora el dosier completo del proyecto, con: especificacion de
     *      requisitos, diagramas UML (casos de uso, clases, secuencia y
     *      estados), planificacion con Gantt y camino critico, y registro de
     *      riesgos.
     *   2. Construye la matriz de trazabilidad completa que relacione cada
     *      requisito con las clases que lo implementan y las pruebas que lo
     *      verifican, y comprueba que ningun requisito queda sin cubrir.
     *   3. Configura el proyecto en un sistema de control de versiones, con un
     *      historial de cambios significativo y mensajes descriptivos.
     *   4. Configura la ejecucion automatica de la bateria completa de pruebas y
     *      documenta el informe de resultados obtenido, incluyendo el porcentaje
     *      de cobertura alcanzado.
     *   5. Evalua el proyecto frente a los criterios de calidad estudiados
     *      (funcionalidad, fiabilidad, usabilidad, eficiencia, mantenibilidad y
     *      portabilidad), argumentando el grado de cumplimiento de cada uno.
     *   6. Identifica en tu propio codigo al menos dos incumplimientos de los
     *      principios SOLID y refactorizalos, comprobando con las pruebas de
     *      regresion que el comportamiento no se altera.
     *   7. Redacta un informe final de una pagina que resuma: alcance cubierto,
     *      desviaciones respecto a la planificacion inicial, riesgos que
     *      llegaron a materializarse y lecciones aprendidas.
     *
     * Pista: este ejercicio no pide programar nada nuevo: pide demostrar que
     * todo lo construido a lo largo del modulo encaja como un proceso coherente,
     * y que puede justificarse cada decision tomada.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * APARTADO 1: el dosier ya esta hecho. Este ejercicio no crea artefactos
     * nuevos, los reune:
     *
     *   Especificacion de requisitos   recursos/uf2406/01_requisitos_y_analisis.md
     *   Analisis OO y tarjetas CRC     recursos/uf2406/01_requisitos_y_analisis.md
     *   Diagramas UML (4 tipos)        recursos/uf2406/02_diagramas_uml.md
     *   Planificacion y riesgos        recursos/uf2406/03_planificacion.md
     *   Diseno de las pruebas          recursos/uf2406/04_diseno_pruebas.md
     *   Cierre, calidad y trazabilidad recursos/uf2406/05_documentacion_y_cierre.md
     *   Convenciones del equipo        recursos/uf2406/CONVENCIONES.md
     *
     * APARTADO 3: el control de versiones.
     *
     *      git init
     *      git add .
     *      git commit -m "Estructura inicial del proyecto de ejercicios"
     *
     * Y un .gitignore con out/ y docs/, que son artefactos generados: versionar
     * los .class produce conflictos en cada compilacion y no aporta nada, porque
     * se pueden reconstruir en cualquier momento a partir del fuente.
     *
     * Sobre los mensajes, la regla del ejercicio 12: primera linea corta con el
     * QUE, cuerpo con el POR QUE, y referencia al requisito o a la incidencia.
     * Un historial de "cambios", "mas cambios" y "ahora si" es exactamente igual
     * de util que no tener historial.
     *
     * APARTADO 4: la ejecucion automatica de las pruebas.
     *
     * Con Maven o Gradle, mvn test se engancha al ciclo de construccion y la
     * integracion continua lo ejecuta en cada envio. En este proyecto, que no
     * lleva herramienta de construccion, la bateria se lanza compilando y
     * ejecutando:
     *
     *      javac -d out $(find src -name "*.java")
     *      java -cp out com.ifcd0112.ejercicios.Main
     *
     * Sobre la COBERTURA, y esto conviene decirlo en clase: la cobertura mide
     * que porcentaje del codigo EJECUTAN las pruebas, no que porcentaje esta
     * BIEN PROBADO. Son cosas distintas. Una bateria que recorre todo el codigo
     * sin comprobar ni un resultado da el 100 por cien de cobertura y no
     * verifica nada.
     *
     * La cobertura sirve sobre todo al reves: para encontrar lo que NUNCA se
     * ejecuta. El suelo de 20 EUR del ejercicio 7 es el ejemplo perfecto, y se
     * encontro precisamente asi.
     *
     * APARTADO 6: dos incumplimientos de SOLID en este mismo proyecto.
     *
     *   1) RESPONSABILIDAD UNICA (la S).
     *      Sintoma: una clase que cambia por mas de un motivo. En la version
     *      inicial, GestorInscripciones ademas de inscribir enviaba el correo de
     *      confirmacion. Con eso, el gestor cambia si cambian las reglas de
     *      inscripcion Y tambien si se cambia de proveedor de correo. Ademas,
     *      probarlo obligaba a tener servidor de correo.
     *      Correccion: extraer ServicioNotificaciones e inyectarlo. El gestor
     *      vuelve a tener un solo motivo para cambiar.
     *
     *   2) INVERSION DE DEPENDENCIAS (la D).
     *      Sintoma: el gestor creaba su DAO con new. Eso ata el negocio a una
     *      implementacion concreta de la capa de datos y hace imposible
     *      sustituirla. Corregido en el ejercicio 10 extrayendo la interfaz
     *      RepositorioClases, y esa correccion es justamente lo que permitio
     *      escribir las pruebas con objetos simulados.
     *
     * Merece la pena senalar la relacion: las dos violaciones se detectaron
     * INTENTANDO ESCRIBIR PRUEBAS. Cuando una clase es dificil de probar, casi
     * siempre es porque hace demasiado o porque se fabrica sus dependencias. La
     * dificultad para probar es el mejor detector de problemas de diseno que
     * existe, y es gratis.
     *
     * En los dos casos la bateria de regresion se ejecuto despues del cambio sin
     * ningun fallo, confirmando que el comportamiento externo no se altero.
     *
     * APARTADO 7: el informe final esta redactado en
     * recursos/uf2406/05_documentacion_y_cierre.md
     */

    /** Ruta del documento de cierre. */
    public static final String DOCUMENTO = "recursos/uf2406/05_documentacion_y_cierre.md";

    /** Fila de la matriz de trazabilidad. */
    public static class Trazabilidad {

        private final String requisito;
        private final String casoDeUso;
        private final List<String> clases;
        private final List<String> pruebas;

        /**
         * @param requisito identificador del requisito
         * @param casoDeUso caso de uso que lo realiza
         * @param clases    clases que lo implementan
         * @param pruebas   pruebas que lo verifican
         */
        public Trazabilidad(String requisito, String casoDeUso, List<String> clases,
                            List<String> pruebas) {
            this.requisito = requisito;
            this.casoDeUso = casoDeUso;
            this.clases = clases;
            this.pruebas = pruebas;
        }

        /** @return identificador del requisito */
        public String getRequisito() { return requisito; }

        /** @return caso de uso que lo realiza */
        public String getCasoDeUso() { return casoDeUso; }

        /** @return clases que lo implementan */
        public List<String> getClases() { return clases; }

        /** @return pruebas que lo verifican */
        public List<String> getPruebas() { return pruebas; }

        /** @return true si tiene al menos una clase y una prueba */
        public boolean estaCubierto() {
            return !clases.isEmpty() && !pruebas.isEmpty();
        }
    }

    /**
     * Apartado 2: la matriz de trazabilidad completa.
     *
     * @return una fila por requisito
     */
    public static List<Trazabilidad> matrizTrazabilidad() {
        List<Trazabilidad> matriz = new ArrayList<>();
        matriz.add(new Trazabilidad("RF-01", "Dar de alta socio",
                List.of("Socio", "SocioDAO"), List.of("SocioDAOTest")));
        matriz.add(new Trazabilidad("RF-02", "Registrar pago",
                List.of("Cuota", "GestorCuotas"), List.of("GestorCuotasTest")));
        matriz.add(new Trazabilidad("RF-03", "Consultar cuotas pendientes",
                List.of("Socio.estaAlCorriente()", "CuotaDAO"), List.of("CuotasPendientesTest")));
        matriz.add(new Trazabilidad("RF-04", "Dar de alta clase colectiva",
                List.of("ClaseColectiva", "Monitor"), List.of("ClaseColectivaTest")));
        matriz.add(new Trazabilidad("RF-05", "Inscribirse en clase",
                List.of("GestorInscripciones", "Inscripcion"),
                List.of("inscribeCorrectamenteSiHayPlazasLibres",
                        "laUltimaPlazaSeAgotaYLaSiguienteInscripcionFalla")));
        matriz.add(new Trazabilidad("RF-06", "(incluido en RF-05)",
                List.of("ClaseColectiva.estaCompleta()"),
                List.of("lanzaExcepcionSiLaClaseEstaCompleta",
                        "inscripcionEnClaseCompletaEntraEnListaDeEspera")));
        matriz.add(new Trazabilidad("RF-07", "Cancelar inscripcion",
                List.of("Inscripcion.cancelar()", "ClaseColectiva.darDeBaja()"),
                List.of("noSePuedeCancelarDosVeces", "unaBajaPromocionaAlPrimeroDeLaEspera")));
        matriz.add(new Trazabilidad("RF-08", "(incluido en RF-05)",
                List.of("GestorInscripciones", "Socio.estaAlCorriente()"),
                List.of("CuotasPendientesTest")));
        matriz.add(new Trazabilidad("RF-09", "Apuntarse a lista de espera",
                List.of("Inscripcion.EN_ESPERA", "ClaseColectiva.darDeBaja()"),
                List.of("inscripcionEnClaseCompletaEntraEnListaDeEspera",
                        "laPromocionRespetaElOrdenDeLlegada",
                        "laBajaDeAlguienEnEsperaNoPromocionaANadie")));
        matriz.add(new Trazabilidad("RNF-01", "(transversal)",
                List.of("ConexionBD (pool)"), List.of("PruebaCargaTest")));
        matriz.add(new Trazabilidad("RNF-02", "(transversal)",
                List.of("ConexionBD (pool)"), List.of("PruebaConcurrenciaTest")));
        matriz.add(new Trazabilidad("RNF-03", "(transversal)",
                List.of("ServicioAutenticacion"), List.of("HashContrasenasTest")));
        return matriz;
    }

    /**
     * Apartado 5: evaluacion frente a los criterios de calidad.
     *
     * @return criterio, grado de cumplimiento y argumento
     */
    public static Map<String, String[]> criteriosCalidad() {
        Map<String, String[]> criterios = new LinkedHashMap<>();
        criterios.put("Funcionalidad", new String[] { "ALTO",
            "Los 9 RF implementados y verificados con pruebas; la matriz no deja hueco." });
        criterios.put("Fiabilidad", new String[] { "MEDIO",
            "Cubierta la logica de negocio; falta probar la recuperacion ante caida de la BD." });
        criterios.put("Usabilidad", new String[] { "MEDIO",
            "7 de las 10 heuristicas; pendiente deshacer y accesibilidad por teclado." });
        criterios.put("Eficiencia", new String[] { "ALTO",
            "Pool de conexiones y RNF-01 verificado con prueba de carga." });
        criterios.put("Mantenibilidad", new String[] { "ALTO",
            "Arquitectura en capas, DAO tras interfaz y bajo acoplamiento." });
        criterios.put("Portabilidad", new String[] { "ALTO",
            "JDBC estandar: cambiar de SGBD exige cambiar URL y driver, no codigo." });
        return criterios;
    }

    // =====================================================================
    // COMPROBACION (apartado 2)
    // =====================================================================

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2406-E15] Proyecto integrador: cierre del ciclo de vida");
        System.out.println("  Apartado 1, dosier completo del proyecto:");
        System.out.println("    recursos/uf2406/01_requisitos_y_analisis.md   requisitos y CRC");
        System.out.println("    recursos/uf2406/02_diagramas_uml.md           4 diagramas UML");
        System.out.println("    recursos/uf2406/03_planificacion.md           Gantt, PERT y riesgos");
        System.out.println("    recursos/uf2406/04_diseno_pruebas.md          diseno de las pruebas");
        System.out.println("    " + DOCUMENTO + "   cierre e informe final");

        // Apartado 2: la matriz, comprobada.
        System.out.println("  Apartado 2, matriz de trazabilidad:");
        System.out.printf("    %-8s %-30s %-42s %s%n", "Req.", "Caso de uso", "Clases", "Pruebas");
        int sinCubrir = 0;
        for (Trazabilidad t : matrizTrazabilidad()) {
            if (!t.estaCubierto()) {
                sinCubrir++;
            }
            System.out.printf("    %-8s %-30s %-42s %d%n",
                    t.getRequisito(), t.getCasoDeUso(),
                    String.join(", ", t.getClases()).length() > 40
                            ? String.join(", ", t.getClases()).substring(0, 39) + "."
                            : String.join(", ", t.getClases()),
                    t.getPruebas().size());
        }
        System.out.println("    Requisitos SIN cubrir: " + sinCubrir
                + " de " + matrizTrazabilidad().size());
        System.out.println("    La matriz se recorre y se comprueba, no se mira. Un requisito sin");
        System.out.println("    clase es funcionalidad que no existe; uno sin prueba es");
        System.out.println("    funcionalidad que nadie sabe si funciona.");

        // Apartado 5
        System.out.println("  Apartado 5, evaluacion frente a los criterios de calidad:");
        for (Map.Entry<String, String[]> e : criteriosCalidad().entrySet()) {
            System.out.printf("    %-16s %-6s %s%n", e.getKey(), e.getValue()[0], e.getValue()[1]);
        }

        System.out.println("  Apartado 4, sobre la cobertura: mide que porcentaje del codigo");
        System.out.println("    EJECUTAN las pruebas, no que porcentaje esta BIEN PROBADO. Una");
        System.out.println("    bateria que recorra todo el codigo sin comprobar ni un resultado da");
        System.out.println("    el 100 por cien y no verifica nada. Su verdadera utilidad es la");
        System.out.println("    contraria: encontrar lo que NUNCA se ejecuta, como el suelo de 20");
        System.out.println("    EUR del ejercicio 7.");
        System.out.println("  Apartado 6, dos incumplimientos de SOLID y su correccion:");
        System.out.println("    S) GestorInscripciones enviaba ademas el correo de confirmacion:");
        System.out.println("       dos motivos para cambiar. Extraido a ServicioNotificaciones.");
        System.out.println("    D) El gestor creaba su DAO con new. Extraida la interfaz");
        System.out.println("       RepositorioClases (ejercicio 10), lo que ademas hizo posible");
        System.out.println("       probarlo con objetos simulados.");
        System.out.println("    Las dos se detectaron INTENTANDO ESCRIBIR PRUEBAS. Cuando una clase");
        System.out.println("    cuesta de probar, casi siempre es porque hace demasiado o porque se");
        System.out.println("    fabrica sus dependencias: la dificultad para probar es el mejor");
        System.out.println("    detector de problemas de diseno, y es gratis.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
