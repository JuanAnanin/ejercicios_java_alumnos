package com.ifcd0112.ejercicios.uf2406.bloque3_planificacion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * UF2406 - BLOQUE 3 - EJERCICIO 6: Estimacion, Gantt y camino critico.
 *
 * <p>Criterios de evaluacion: CE1.2</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej06GanttCaminoCritico {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * El proyecto del gimnasio debe planificarse. Se han identificado las
     * siguientes tareas con su duracion estimada: Analisis de requisitos
     * (4 dias), Diseno de la base de datos (3 dias), Diseno de la interfaz
     * (3 dias), Desarrollo del acceso a datos (6 dias), Desarrollo de la
     * interfaz (5 dias), Integracion (3 dias) y Pruebas de sistema (4 dias). El
     * diseno de la base de datos y el de la interfaz solo pueden comenzar tras
     * el analisis; cada desarrollo requiere su diseno correspondiente; la
     * integracion exige ambos desarrollos terminados; y las pruebas van tras la
     * integracion.
     *
     * Se pide:
     *   1. Elabora la tabla de tareas indicando, para cada una, su duracion y
     *      sus tareas predecesoras.
     *   2. Dibuja el diagrama de Gantt correspondiente, aprovechando el
     *      paralelismo entre las tareas que puedan solaparse.
     *   3. Dibuja el diagrama de red (PERT) con las dependencias entre tareas.
     *   4. Calcula el camino critico e indica la duracion minima total del
     *      proyecto.
     *   5. Determina cuantos dias de holgura tiene la tarea "Desarrollo de la
     *      interfaz" y explica que significa exactamente esa holgura.
     *   6. El cliente pide adelantar la entrega una semana. Indica razonadamente
     *      sobre que tareas concretas habria que actuar y por que seria inutil
     *      acelerar las demas.
     *   7. Elabora el registro de riesgos del proyecto con al menos cuatro
     *      riesgos, valorando probabilidad e impacto y proponiendo una medida de
     *      mitigacion para cada uno.
     *
     * Pista: el camino critico es la secuencia de tareas dependientes mas larga:
     * determina la duracion minima del proyecto, y ninguna de sus tareas tiene
     * holgura.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * COMO ESTA RESUELTO ESTE EJERCICIO.
     *
     * El documento con la tabla, el Gantt, la red PERT y el registro de riesgos
     * esta en:
     *
     *      recursos/uf2406/03_planificacion.md
     *
     * Pero los numeros de ese documento no estan copiados a mano: los calcula
     * este fichero. Aqui esta implementado el metodo del camino critico
     * completo, con su recorrido hacia delante y su recorrido hacia atras, y el
     * Gantt se dibuja a partir de las fechas calculadas, no al reves.
     *
     * Merece la pena hacerlo asi por dos motivos. El primero es que asi se
     * comprueba de verdad que la duracion son 20 dias y que la holgura de E es
     * 1, en lugar de creerlo. El segundo es que el apartado 6 se puede
     * demostrar: basta con recortar un dia a una tarea y volver a calcular para
     * ver si la fecha final se mueve o no. Eso convierte una explicacion en un
     * experimento.
     *
     * EL METODO DEL CAMINO CRITICO, EN DOS PASADAS.
     *
     * HACIA DELANTE (calcula lo mas pronto que puede ocurrir cada cosa):
     *      inicio_temprano = el mayor de los fin_temprano de sus predecesoras
     *      fin_temprano    = inicio_temprano + duracion
     *      La duracion del proyecto es el mayor fin_temprano de todas.
     *
     * HACIA ATRAS (calcula lo mas tarde que puede ocurrir sin retrasar el fin):
     *      fin_tardio    = el menor de los inicio_tardio de sus sucesoras
     *                      (o la duracion del proyecto si no tiene sucesoras)
     *      inicio_tardio = fin_tardio - duracion
     *
     * HOLGURA = inicio_tardio - inicio_temprano
     *
     * Una tarea con holgura cero es critica: cualquier retraso suyo se traslada
     * dia por dia a la fecha de entrega.
     */

    /** Ruta del documento de planificacion. */
    public static final String DOCUMENTO = "recursos/uf2406/03_planificacion.md";

    /** Tarea del proyecto con sus fechas calculadas. */
    public static class Tarea {

        private final String codigo;
        private final String nombre;
        private final int duracion;
        private final List<String> predecesoras;

        private int inicioTemprano;
        private int finTemprano;
        private int inicioTardio;
        private int finTardio;

        /**
         * @param codigo       letra que identifica la tarea
         * @param nombre       descripcion
         * @param duracion     dias que dura
         * @param predecesoras codigos de las tareas que deben terminar antes
         */
        public Tarea(String codigo, String nombre, int duracion, String... predecesoras) {
            this.codigo = codigo;
            this.nombre = nombre;
            this.duracion = duracion;
            this.predecesoras = Arrays.asList(predecesoras);
        }

        /** @return letra que identifica la tarea */
        public String getCodigo() { return codigo; }

        /** @return descripcion de la tarea */
        public String getNombre() { return nombre; }

        /** @return dias que dura */
        public int getDuracion() { return duracion; }

        /** @return codigos de las tareas predecesoras */
        public List<String> getPredecesoras() { return predecesoras; }

        /** @return dia en que puede empezar como muy pronto */
        public int getInicioTemprano() { return inicioTemprano; }

        /** @return dia en que puede terminar como muy pronto */
        public int getFinTemprano() { return finTemprano; }

        /** @return dia en que debe empezar como muy tarde */
        public int getInicioTardio() { return inicioTardio; }

        /** @return dia en que debe terminar como muy tarde */
        public int getFinTardio() { return finTardio; }

        /**
         * Apartado 5: dias que puede retrasarse sin mover la fecha de entrega.
         *
         * @return holgura en dias
         */
        public int getHolgura() { return inicioTardio - inicioTemprano; }

        /** @return true si la tarea esta en el camino critico */
        public boolean esCritica() { return getHolgura() == 0; }
    }

    /** Resultado del calculo: las tareas con sus fechas y el camino critico. */
    public static class Planificacion {

        private final Map<String, Tarea> tareas;
        private final int duracionTotal;
        private final List<String> caminoCritico;

        Planificacion(Map<String, Tarea> tareas, int duracionTotal, List<String> caminoCritico) {
            this.tareas = tareas;
            this.duracionTotal = duracionTotal;
            this.caminoCritico = caminoCritico;
        }

        /** @return tareas por codigo, en orden */
        public Map<String, Tarea> getTareas() { return tareas; }

        /** @return duracion minima del proyecto en dias */
        public int getDuracionTotal() { return duracionTotal; }

        /** @return codigos de las tareas del camino critico, en orden */
        public List<String> getCaminoCritico() { return caminoCritico; }
    }

    /**
     * Apartados 1, 4 y 5: calcula fechas, holguras y camino critico.
     *
     * @param listaTareas tareas del proyecto con sus precedencias
     * @return la planificacion resuelta
     */
    public static Planificacion calcular(List<Tarea> listaTareas) {
        Map<String, Tarea> tareas = new LinkedHashMap<>();
        for (Tarea t : listaTareas) {
            tareas.put(t.getCodigo(), t);
        }

        // ---- Recorrido HACIA DELANTE ----
        // Se repite hasta que nada cambia. Con las tareas en orden topologico
        // bastaria una pasada, pero asi el metodo funciona con la tabla escrita
        // en cualquier orden, que es como llegan las tablas de verdad.
        boolean cambio = true;
        while (cambio) {
            cambio = false;
            for (Tarea t : tareas.values()) {
                int inicio = 0;
                for (String p : t.getPredecesoras()) {
                    Tarea pred = tareas.get(p);
                    if (pred != null && pred.finTemprano > inicio) {
                        inicio = pred.finTemprano;
                    }
                }
                if (inicio != t.inicioTemprano) {
                    t.inicioTemprano = inicio;
                    cambio = true;
                }
                int fin = t.inicioTemprano + t.getDuracion();
                if (fin != t.finTemprano) {
                    t.finTemprano = fin;
                    cambio = true;
                }
            }
        }

        int duracionTotal = 0;
        for (Tarea t : tareas.values()) {
            if (t.finTemprano > duracionTotal) {
                duracionTotal = t.finTemprano;
            }
        }

        // ---- Recorrido HACIA ATRAS ----
        for (Tarea t : tareas.values()) {
            t.finTardio = duracionTotal;
            t.inicioTardio = duracionTotal - t.getDuracion();
        }
        cambio = true;
        while (cambio) {
            cambio = false;
            for (Tarea t : tareas.values()) {
                // El fin tardio de una tarea es el menor de los inicios tardios
                // de todas las tareas que dependen de ella.
                int fin = duracionTotal;
                boolean tieneSucesoras = false;
                for (Tarea posible : tareas.values()) {
                    if (posible.getPredecesoras().contains(t.getCodigo())) {
                        tieneSucesoras = true;
                        if (posible.inicioTardio < fin) {
                            fin = posible.inicioTardio;
                        }
                    }
                }
                if (!tieneSucesoras) {
                    fin = duracionTotal;
                }
                if (fin != t.finTardio) {
                    t.finTardio = fin;
                    t.inicioTardio = fin - t.getDuracion();
                    cambio = true;
                }
            }
        }

        List<String> critico = new ArrayList<>();
        for (Tarea t : tareas.values()) {
            if (t.esCritica()) {
                critico.add(t.getCodigo());
            }
        }
        return new Planificacion(tareas, duracionTotal, critico);
    }

    /**
     * Tabla de tareas del apartado 1.
     *
     * @return las siete tareas del proyecto del gimnasio
     */
    public static List<Tarea> tareasDelGimnasio() {
        return new ArrayList<>(Arrays.asList(
                new Tarea("A", "Analisis de requisitos",        4),
                new Tarea("B", "Diseno de la base de datos",    3, "A"),
                new Tarea("C", "Diseno de la interfaz",         3, "A"),
                new Tarea("D", "Desarrollo del acceso a datos", 6, "B"),
                new Tarea("E", "Desarrollo de la interfaz",     5, "C"),
                new Tarea("F", "Integracion",                   3, "D", "E"),
                new Tarea("G", "Pruebas de sistema",            4, "F")));
    }

    // =====================================================================
    // COMPROBACION
    // =====================================================================

    /**
     * Apartado 2: dibuja el Gantt a partir de las fechas ya calculadas.
     *
     * @param plan planificacion resuelta
     */
    private static void dibujarGantt(Planificacion plan) {
        StringBuilder cabecera = new StringBuilder("    Dia:    ");
        for (int d = 1; d <= plan.getDuracionTotal(); d++) {
            cabecera.append(String.format("%3d", d));
        }
        System.out.println(cabecera);
        for (Tarea t : plan.getTareas().values()) {
            StringBuilder linea = new StringBuilder(
                    String.format("    %s (%dd) ", t.getCodigo(), t.getDuracion()));
            for (int d = 1; d <= plan.getDuracionTotal(); d++) {
                if (d > t.getInicioTemprano() && d <= t.getFinTemprano()) {
                    linea.append(" ##");
                } else if (t.getHolgura() > 0 && d > t.getFinTemprano() && d <= t.getFinTardio()) {
                    linea.append(" ..");        // la holgura, dibujada
                } else {
                    linea.append("   ");
                }
            }
            System.out.println(linea);
        }
        System.out.println("    (## trabajo, .. holgura disponible)");
    }

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2406-E06] Estimacion, Gantt y camino critico");
        System.out.println("  Documento con la red PERT y el registro de riesgos:");
        System.out.println("    " + DOCUMENTO);

        Planificacion plan = calcular(tareasDelGimnasio());

        // Apartado 1
        System.out.println("  Apartado 1, tabla de tareas y fechas calculadas:");
        System.out.println("    Cod  Tarea                          Dur  Pred    IT  FT  IL  FL  Holg  Critica");
        for (Tarea t : plan.getTareas().values()) {
            System.out.printf("     %s   %-30s %2d  %-6s %3d %3d %3d %3d  %3d   %s%n",
                    t.getCodigo(), t.getNombre(), t.getDuracion(),
                    t.getPredecesoras().isEmpty() ? "-" : String.join(",", t.getPredecesoras()),
                    t.getInicioTemprano(), t.getFinTemprano(),
                    t.getInicioTardio(), t.getFinTardio(),
                    t.getHolgura(), t.esCritica() ? "SI" : "no");
        }

        // Apartado 2
        System.out.println("  Apartado 2, diagrama de Gantt:");
        dibujarGantt(plan);

        // Apartado 4
        System.out.println("  Apartado 4: duracion minima del proyecto = "
                + plan.getDuracionTotal() + " dias laborables");
        System.out.println("    Camino critico: " + String.join(" -> ", plan.getCaminoCritico()));

        // Apartado 5
        Tarea e = plan.getTareas().get("E");
        System.out.println("  Apartado 5: holgura de " + e.getNombre() + " = "
                + e.getHolgura() + " dia");
        System.out.println("    Significa que E puede empezar un dia mas tarde, o durar un dia mas,");
        System.out.println("    sin mover la fecha de entrega: acaba el dia " + e.getFinTemprano()
                + " pero F no empieza hasta el " + (plan.getTareas().get("F").getInicioTemprano() + 1)
                + " porque espera a D.");

        // Apartado 6, demostrado en lugar de explicado.
        System.out.println("  Apartado 6, experimento: recortar un dia a cada tarea y ver que pasa.");
        for (String codigo : new String[] { "A", "B", "D", "F", "G", "C", "E" }) {
            List<Tarea> modificadas = new ArrayList<>();
            for (Tarea t : tareasDelGimnasio()) {
                int dur = t.getCodigo().equals(codigo) ? t.getDuracion() - 1 : t.getDuracion();
                modificadas.add(new Tarea(t.getCodigo(), t.getNombre(), dur,
                        t.getPredecesoras().toArray(new String[0])));
            }
            int nueva = calcular(modificadas).getDuracionTotal();
            int ganancia = plan.getDuracionTotal() - nueva;
            System.out.printf("    Recortar 1 dia a %s (%-30s) -> proyecto de %d dias, se gana %d%n",
                    codigo, plan.getTareas().get(codigo).getNombre(), nueva, ganancia);
        }
        System.out.println("    Solo se gana dia acelerando tareas del camino critico. Acelerar C o E");
        System.out.println("    no adelanta nada: solo aumenta su holgura, que ya estaba sobrando.");
        System.out.println("    Para adelantar una semana conviene atacar D, que con 6 dias es la mas");
        System.out.println("    larga del critico. Aviso: al comprimir, la holgura de E se agota y");
        System.out.println("    aparece un segundo camino critico, con lo que el coste se dispara.");
        System.out.println("  Apartado 7: registro de 5 riesgos con probabilidad, impacto y");
        System.out.println("    mitigacion en el documento. El mas grave es R2 (el desarrollo de");
        System.out.println("    acceso a datos se alarga) porque es probable Y esta en el critico.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
