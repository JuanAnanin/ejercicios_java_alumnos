package com.ifcd0112.ejercicios.ampliacion.uf2406;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ifcd0112.ejercicios.uf2406.bloque4_pruebas.Ej07CajaNegra;
import com.ifcd0112.ejercicios.uf2406.bloque4_pruebas.Ej09Junit;
import com.ifcd0112.ejercicios.uf2406.bloque5_documentacion.Ej12CambioEspecificacion;

/**
 * AMPLIACION UF2406 - EJERCICIO A8: Plantilla e informe de pruebas.
 *
 * <p>Criterios de evaluacion: CE3.4, CE3.8</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class A08InformePruebas {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Has disenado pruebas (ejercicio 7), las has automatizado (ejercicio 9) y
     * has ejecutado una regresion (ejercicio 12). Falta lo que se entrega al
     * cliente o al responsable de calidad: el INFORME.
     *
     * Se pide:
     *   1. Propon el indice (plantilla) del documento de pruebas, que debe
     *      recoger tanto el DISENO de las pruebas como sus RESULTADOS.
     *   2. Explica por que esos dos contenidos van en el mismo documento y que
     *      pasaria si se entregaran por separado.
     *   3. Elabora el informe completo de las pruebas del sistema del gimnasio,
     *      con los datos reales obtenidos al ejecutar las baterias de los
     *      ejercicios 9 y 12.
     *   4. Genera el informe de forma AUTOMATICA a partir de la ejecucion, en
     *      lugar de copiar los numeros a mano. Explica que ventaja tiene.
     *   5. Incluye en el informe el veredicto: se acepta la entrega o no, y con
     *      que criterio se decide. Un informe que no concluye nada no sirve.
     *   6. Anade el apartado de defectos detectados, con su gravedad y su
     *      estado, y explica la diferencia entre un defecto ABIERTO y uno
     *      ACEPTADO.
     *   7. Indica que cifras del informe NO deben usarse como objetivo, y por
     *      que.
     *
     * Pista: un informe de pruebas que solo diga "todas las pruebas pasan" es
     * inutil, porque no dice cuantas eran ni que cubrian. Cien pruebas que pasan
     * sobre el diez por ciento del codigo son peores que veinte sobre el
     * ochenta.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * APARTADO 2: por que diseno y resultados van juntos.
     *
     * Porque un resultado sin su diseno no se puede interpretar. "47 pruebas
     * correctas" no dice nada si no se sabe QUE se probo: pueden ser 47 pruebas
     * del mismo caso trivial. Y al reves, un diseno sin resultados es una
     * intencion, no una comprobacion.
     *
     * Separarlos tiene ademas una consecuencia practica muy conocida: los dos
     * documentos se desincronizan. El diseno se queda con veinte casos y el
     * informe presenta dieciocho, y nadie sabe si faltan dos por ejecutar o si
     * se eliminaron a proposito.
     *
     * APARTADO 4: por que generarlo automaticamente.
     *
     *   - No se puede equivocar al copiar, ni redondear a favor.
     *   - Se puede regenerar en cada cambio sin coste, asi que siempre esta al
     *     dia. Un informe que cuesta media hora se actualiza una vez al mes.
     *   - Y la razon de fondo: si el informe se genera de la ejecucion, es
     *     IMPOSIBLE entregar un informe verde con las pruebas en rojo. Lo que se
     *     escribe a mano siempre se puede maquillar, aunque sea sin querer.
     *
     * APARTADO 6: defecto abierto frente a defecto aceptado.
     *
     *   ABIERTO   se ha detectado, no se ha corregido y no se ha decidido nada.
     *             Bloquea la entrega si su gravedad es alta.
     *   ACEPTADO  se ha detectado, no se ha corregido y ALGUIEN CON NOMBRE Y
     *             FECHA ha decidido conscientemente entregarlo asi, por coste,
     *             plazo o baja gravedad.
     *
     * La diferencia no es tecnica, es de responsabilidad. Un defecto aceptado
     * es una decision documentada; uno abierto es un descuido. El mismo fallo
     * puede ser cualquiera de las dos cosas, y lo unico que los distingue es que
     * exista esa firma.
     *
     * APARTADO 7: cifras que no deben usarse como objetivo.
     *
     * La COBERTURA, sobre todo. En cuanto se fija "hay que llegar al 80 por
     * ciento", aparecen pruebas que ejecutan codigo sin comprobar nada: suben el
     * porcentaje y no verifican una sola cosa. La cobertura sirve para BUSCAR
     * huecos, no para premiar.
     *
     * Lo mismo con el NUMERO de pruebas. Medir el trabajo por el numero de
     * pruebas escritas empuja a trocear una prueba en cinco.
     *
     * Es un caso particular de la ley de Goodhart: cuando una medida se
     * convierte en objetivo, deja de ser una buena medida. La cifra que si tiene
     * sentido vigilar es la contraria: defectos que se escaparon a produccion.
     */

    /** Gravedad de un defecto detectado. */
    public enum Gravedad {
        /** Impide usar el sistema. Bloquea la entrega siempre. */
        CRITICA,
        /** Afecta a una funcionalidad importante, pero hay forma de sortearlo. */
        ALTA,
        /** Molesta, no impide trabajar. */
        MEDIA,
        /** Cosmetico o de detalle. */
        BAJA
    }

    /** Estado de un defecto en el momento del informe. */
    public enum Estado {
        /** Detectado y sin decidir. */
        ABIERTO,
        /** Corregido y verificado. */
        CORREGIDO,
        /** Se entrega asi por decision documentada. */
        ACEPTADO
    }

    /**
     * Defecto detectado durante las pruebas.
     *
     * @param id        identificador del defecto
     * @param resumen   descripcion breve
     * @param gravedad  impacto sobre el usuario
     * @param estado    situacion actual
     * @param decision  quien decidio y cuando, si esta aceptado
     */
    public record Defecto(String id, String resumen, Gravedad gravedad, Estado estado,
                          String decision) {
    }

    /**
     * Resultado de ejecutar una bateria concreta.
     *
     * @param bateria   nombre de la bateria
     * @param que       que verifica
     * @param total     pruebas ejecutadas
     * @param fallos    pruebas que fallaron
     * @param errores   pruebas que no llegaron a terminar
     */
    public record Resultado(String bateria, String que, int total, int fallos, int errores) {

        /** @return true si la bateria paso entera */
        public boolean correcta() { return fallos == 0 && errores == 0; }
    }

    /**
     * Apartado 1: el indice del documento de pruebas.
     *
     * @return los apartados en orden
     */
    public static List<String> plantilla() {
        return Arrays.asList(
            "1. Identificacion: sistema, version probada, fecha y responsable",
            "2. Alcance: que se ha probado y, muy importante, que NO",
            "3. Entorno de pruebas: version de Java, datos de partida, dependencias",
            "4. Estrategia: tipos de prueba aplicados y criterio de diseno de casos",
            "5. Diseno de las pruebas: clases de equivalencia, limites, tabla de decision",
            "6. Casos de prueba: entrada, resultado esperado y que verifica cada uno",
            "7. Resultados de la ejecucion: por bateria, con totales",
            "8. Cobertura alcanzada, con su interpretacion",
            "9. Defectos detectados: gravedad, estado y decision",
            "10. Veredicto: se acepta o no la entrega, y con que criterio",
            "11. Riesgos residuales: que puede fallar aunque todo este verde",
            "12. Historial de revisiones del documento");
    }

    /**
     * Apartado 4: ejecuta las baterias de verdad y recoge los numeros.
     *
     * @return resultados reales, no copiados a mano
     */
    public static List<Resultado> ejecutarBaterias() {
        List<Resultado> resultados = new ArrayList<>();

        // Bateria del ejercicio 7: caja negra sobre el calculo de la cuota.
        Ej07CajaNegra.CalculadoraCuota calc = new Ej07CajaNegra.CalculadoraCuota();
        int fallosCajaNegra = Ej07CajaNegra.ejecutarBateria(calc, false, false);
        resultados.add(new Resultado("Caja negra (ej. 7)",
                "equivalencia y valores limite de la cuota",
                Ej07CajaNegra.bateria().length, fallosCajaNegra, 0));

        // Bateria del ejercicio 9, sobre la version correcta.
        Ej09Junit.Resultado unitarias = Ej09Junit.bateria(calc::calcular).ejecutar(false, "      ");
        resultados.add(new Resultado("Unitarias (ej. 9)",
                "la cuota mensual, automatizada",
                unitarias.getEjecutadas(), unitarias.getFallos(), unitarias.getErrores()));

        // Bateria de regresion del ejercicio 12.
        Ej09Junit.Resultado regresion =
                Ej12CambioEspecificacion.bateriaRegresion().ejecutar(false, "      ");
        resultados.add(new Resultado("Regresion (ej. 12)",
                "lista de espera y lo que ya funcionaba",
                regresion.getEjecutadas(), regresion.getFallos(), regresion.getErrores()));

        return resultados;
    }

    /**
     * Apartado 6: defectos detectados durante las pruebas.
     *
     * @return el registro de defectos
     */
    public static List<Defecto> defectos() {
        return Arrays.asList(
            new Defecto("D-01",
                "El suelo de 20 EUR de la cuota es inalcanzable: el minimo real es 23",
                Gravedad.BAJA, Estado.ACEPTADO,
                "Aceptado por el responsable funcional el 27/08/2026: no afecta al usuario "
                + "y se revisara si se anaden mas descuentos"),
            new Defecto("D-02",
                "mascota.ultima_visita se actualiza pero ninguna sentencia DDL la crea",
                Gravedad.ALTA, Estado.CORREGIDO,
                "Corregido en 02_evolucion_esquema.sql y verificado"),
            new Defecto("D-03",
                "La pantalla de inscripcion no permite deshacer una inscripcion recien hecha",
                Gravedad.MEDIA, Estado.ABIERTO,
                "Sin decidir: pendiente de estimar"));
    }

    // =====================================================================
    // COMPROBACION
    // =====================================================================

    /** Ruta del informe elaborado en el apartado 3. */
    public static final String INFORME = "recursos/ampliacion/INFORME_PRUEBAS_gimnasio.md";

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[AMP-UF2406-A8] Plantilla e informe de pruebas");

        System.out.println("  Apartado 1, indice del documento de pruebas:");
        for (String apartado : plantilla()) {
            System.out.println("    " + apartado);
        }
        System.out.println("  Apartado 3, informe completo: " + INFORME);

        System.out.println("  Apartado 4, informe GENERADO ejecutando las baterias de verdad:");
        List<Resultado> resultados = ejecutarBaterias();
        System.out.printf("    %-22s %-42s %6s %7s %7s%n",
                "bateria", "que verifica", "total", "fallos", "errores");
        int total = 0;
        int fallos = 0;
        int errores = 0;
        for (Resultado r : resultados) {
            System.out.printf("    %-22s %-42s %6d %7d %7d%n",
                    r.bateria(), r.que(), r.total(), r.fallos(), r.errores());
            total += r.total();
            fallos += r.fallos();
            errores += r.errores();
        }
        System.out.printf("    %-22s %-42s %6d %7d %7d%n", "TOTAL", "", total, fallos, errores);
        System.out.println("    Estos numeros NO estan escritos a mano: salen de ejecutar las tres");
        System.out.println("    baterias ahora mismo. Por eso es imposible entregar un informe en");
        System.out.println("    verde con las pruebas en rojo.");

        System.out.println("  Apartado 6, defectos detectados:");
        System.out.printf("    %-6s %-8s %-10s %s%n", "id", "gravedad", "estado", "resumen");
        int bloqueantes = 0;
        for (Defecto d : defectos()) {
            System.out.printf("    %-6s %-8s %-10s %s%n",
                    d.id(), d.gravedad(), d.estado(), d.resumen());
            if (d.estado() == Estado.ABIERTO
                    && (d.gravedad() == Gravedad.CRITICA || d.gravedad() == Gravedad.ALTA)) {
                bloqueantes++;
            }
        }
        System.out.println("    D-01 y D-03 son el mismo tipo de situacion, un defecto que no se");
        System.out.println("    corrige, y sin embargo son cosas distintas: el primero lleva firma");
        System.out.println("    y fecha de quien decidio entregarlo asi, y el segundo no. Eso es");
        System.out.println("    lo unico que separa una decision de un descuido.");

        // Apartado 5: el veredicto.
        System.out.println("  Apartado 5, veredicto:");
        System.out.println("    Criterio de aceptacion acordado: cero fallos y cero errores en las");
        System.out.println("    tres baterias, y ningun defecto ABIERTO de gravedad alta o critica.");
        boolean aceptada = fallos == 0 && errores == 0 && bloqueantes == 0;
        System.out.println("    Pruebas correctas: " + (fallos == 0 && errores == 0 ? "SI" : "NO")
                + "   Defectos bloqueantes abiertos: " + bloqueantes);
        System.out.println("    ENTREGA " + (aceptada ? "ACEPTADA" : "NO ACEPTADA"));
        System.out.println("    Un informe que no concluye nada no sirve: el veredicto es el");
        System.out.println("    apartado por el que empieza a leer quien lo recibe.");

        System.out.println("  Apartado 7: la cobertura y el numero de pruebas NO deben fijarse como");
        System.out.println("    objetivo. En cuanto se exige un 80 por ciento, aparecen pruebas que");
        System.out.println("    ejecutan codigo sin comprobar nada. Es la ley de Goodhart: cuando");
        System.out.println("    una medida se convierte en objetivo, deja de ser buena medida. La");
        System.out.println("    cifra que si conviene vigilar es la contraria, los defectos que se");
        System.out.println("    escaparon a produccion.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
