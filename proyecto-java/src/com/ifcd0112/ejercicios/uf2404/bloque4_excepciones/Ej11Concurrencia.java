package com.ifcd0112.ejercicios.uf2404.bloque4_excepciones;

/**
 * UF2404 - BLOQUE 4 - EJERCICIO 11: Concurrencia, la condicion de carrera.
 *
 * <p>Criterios de evaluacion: CE2.10</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej11Concurrencia {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Este ejercicio pretende que compruebes empiricamente el efecto de una
     * condicion de carrera y como se corrige mediante sincronizacion.
     *
     * Se pide:
     *   1. Implementa una clase Contador con un atributo entero y un metodo
     *      incrementar() que le sume una unidad.
     *   2. Crea un programa que lance 4 hilos, cada uno de los cuales
     *      incremente 10.000 veces el mismo objeto Contador compartido.
     *   3. Espera a que todos los hilos terminen (investiga el metodo join()) y
     *      muestra el valor final del contador.
     *   4. Ejecuta el programa cinco veces seguidas y anota los resultados
     *      obtenidos: comprobaras que casi nunca coincide con el valor esperado
     *      de 40.000.
     *   5. Anade el modificador synchronized al metodo incrementar(), vuelve a
     *      ejecutar cinco veces y compara los resultados.
     *   6. Explica en un comentario por que la operacion contador++ no es
     *      atomica y en que consiste exactamente la condicion de carrera.
     *
     * Salida esperada (orientativa):
     *      Sin synchronized -> 37421, 39980, 38145, 40000, 39012  (variables)
     *      Con synchronized -> 40000, 40000, 40000, 40000, 40000  (siempre correcto)
     *
     * Pista: si no invocas join() sobre cada hilo antes de imprimir el
     * resultado, el hilo principal mostrara el valor del contador antes de que
     * los demas hayan terminado, falseando la prueba.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * APARTADO 6: por que contador++ NO es atomica.
     *
     * En el codigo fuente "valor++" es una sola instruccion, y de ahi viene la
     * intuicion equivocada. Pero la maquina virtual la ejecuta en TRES pasos:
     *
     *      1. LEER el valor actual del atributo y llevarlo a un registro.
     *      2. SUMARLE uno en ese registro.
     *      3. ESCRIBIR el resultado de vuelta en el atributo.
     *
     * Entre cualquiera de esos tres pasos, el planificador del sistema puede
     * detener el hilo y dar paso a otro. Y ahi esta el problema.
     *
     * LA CONDICION DE CARRERA, paso a paso, con el contador valiendo 100:
     *
     *      Hilo A LEE 100
     *      Hilo B LEE 100          <- todavia no ha escrito nadie
     *      Hilo A SUMA -> 101
     *      Hilo B SUMA -> 101
     *      Hilo A ESCRIBE 101
     *      Hilo B ESCRIBE 101      <- pisa lo que escribio A
     *
     * Dos incrementos y el contador solo ha subido uno. Ese incremento perdido,
     * repetido unas cuantas veces entre cuarenta mil operaciones, es lo que
     * explica que el resultado final salga por debajo de 40.000.
     *
     * Se llama condicion de CARRERA porque el resultado depende de quien llegue
     * antes, es decir, del orden en que el planificador reparta el tiempo de
     * CPU. Ese orden no esta garantizado y cambia en cada ejecucion, y por eso
     * el fallo es intermitente: es la peor clase de error, porque en pruebas
     * puede no aparecer nunca y en produccion aparece un martes por la tarde.
     *
     * synchronized lo corrige convirtiendo los tres pasos en una operacion
     * indivisible: mientras un hilo esta dentro del metodo, ningun otro puede
     * entrar. El precio es que los hilos se esperan unos a otros, asi que la
     * version sincronizada es algo mas lenta; a cambio, es correcta.
     */

    /** Apartado 1: contador SIN sincronizar. Es el que exhibe el fallo. */
    public static class Contador {

        private int valor = 0;

        /** Incrementa en uno. Ojo: NO es atomica, ver la explicacion de arriba. */
        public void incrementar() {
            valor++;
        }

        public int getValor() { return valor; }
    }

    /** Apartado 5: el mismo contador CON synchronized. */
    public static class ContadorSincronizado {

        private int valor = 0;

        /**
         * Incrementa en uno de forma segura entre hilos.
         *
         * <p>synchronized hace que el hilo tenga que adquirir el cerrojo del
         * objeto antes de entrar y lo libere al salir. Los tres pasos de la
         * suma quedan asi dentro de una region que solo puede recorrer un hilo
         * a la vez.</p>
         */
        public synchronized void incrementar() {
            valor++;
        }

        public synchronized int getValor() { return valor; }
    }

    /** Numero de hilos que compiten por el contador. */
    private static final int NUM_HILOS = 4;

    /** Incrementos que hace cada hilo. */
    private static final int INCREMENTOS = 10_000;

    /** Valor correcto: 4 hilos por 10.000 incrementos. */
    private static final int ESPERADO = NUM_HILOS * INCREMENTOS;

    /**
     * Apartados 2 y 3: lanza los hilos sobre el contador sin sincronizar y
     * devuelve el valor final.
     *
     * @return el valor del contador tras terminar los cuatro hilos
     * @throws InterruptedException si el hilo principal es interrumpido esperando
     */
    public static int ejecutarSinSincronizar() throws InterruptedException {
        Contador contador = new Contador();
        Thread[] hilos = new Thread[NUM_HILOS];

        for (int i = 0; i < NUM_HILOS; i++) {
            hilos[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENTOS; j++) {
                    contador.incrementar();
                }
            });
            hilos[i].start();
        }
        // Apartado 3 y pista: sin este join(), el hilo principal seguiria
        // adelante e imprimiria el contador mientras los otros cuatro todavia
        // estan sumando. El resultado saldria absurdamente bajo, pero no por la
        // condicion de carrera, sino por haber mirado demasiado pronto: seria
        // una prueba mal hecha, no una demostracion del problema.
        for (Thread h : hilos) {
            h.join();
        }
        return contador.getValor();
    }

    /**
     * Apartado 5: lo mismo con el contador sincronizado.
     *
     * @return el valor del contador tras terminar los cuatro hilos
     * @throws InterruptedException si el hilo principal es interrumpido esperando
     */
    public static int ejecutarSincronizado() throws InterruptedException {
        ContadorSincronizado contador = new ContadorSincronizado();
        Thread[] hilos = new Thread[NUM_HILOS];

        for (int i = 0; i < NUM_HILOS; i++) {
            hilos[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENTOS; j++) {
                    contador.incrementar();
                }
            });
            hilos[i].start();
        }
        for (Thread h : hilos) {
            h.join();
        }
        return contador.getValor();
    }

    // =====================================================================
    // COMPROBACION (apartados 4 y 5)
    // =====================================================================

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2404-E11] Concurrencia: la condicion de carrera");
        System.out.println("  " + NUM_HILOS + " hilos x " + INCREMENTOS
                + " incrementos = " + ESPERADO + " esperado");

        try {
            // Apartado 4: cinco ejecuciones seguidas sin sincronizar.
            StringBuilder sin = new StringBuilder();
            int fallos = 0;
            for (int i = 0; i < 5; i++) {
                int resultado = ejecutarSinSincronizar();
                sin.append(resultado);
                if (i < 4) {
                    sin.append(", ");
                }
                if (resultado != ESPERADO) {
                    fallos++;
                }
            }
            System.out.println("  Sin synchronized -> " + sin);
            System.out.println("    Ejecuciones incorrectas: " + fallos + " de 5");
            if (fallos == 0) {
                // Honestidad: la condicion de carrera es intermitente por
                // definicion. Puede no manifestarse en una maquina concreta o en
                // una ejecucion concreta, y eso NO significa que el codigo sea
                // correcto: significa que esta vez hubo suerte.
                System.out.println("    Esta vez han salido las 5 correctas. No prueba que el codigo");
                System.out.println("    sea seguro: la condicion de carrera es intermitente por");
                System.out.println("    naturaleza. Vuelve a ejecutarlo o sube el numero de hilos.");
            }

            // Apartado 5: cinco ejecuciones con synchronized.
            StringBuilder con = new StringBuilder();
            int fallosSinc = 0;
            for (int i = 0; i < 5; i++) {
                int resultado = ejecutarSincronizado();
                con.append(resultado);
                if (i < 4) {
                    con.append(", ");
                }
                if (resultado != ESPERADO) {
                    fallosSinc++;
                }
            }
            System.out.println("  Con synchronized -> " + con);
            System.out.println("    Ejecuciones incorrectas: " + fallosSinc + " de 5");

        } catch (InterruptedException e) {
            // Buena practica: al capturar InterruptedException hay que restaurar
            // la marca de interrupcion, porque el catch la ha borrado y el
            // codigo de mas arriba podria necesitar saber que se pidio parar.
            Thread.currentThread().interrupt();
            System.out.println("  La prueba fue interrumpida: " + e.getMessage());
        }
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
