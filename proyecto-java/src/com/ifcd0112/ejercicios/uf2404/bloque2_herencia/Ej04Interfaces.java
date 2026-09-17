package com.ifcd0112.ejercicios.uf2404.bloque2_herencia;

import java.util.ArrayList;
import java.util.List;

/**
 * UF2404 - BLOQUE 2 - EJERCICIO 4: Interfaces: elementos reproducibles y descargables.
 *
 * <p>Criterios de evaluacion: CE1.2, CE1.6, CE2.10</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej04Interfaces {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Una plataforma multimedia gestiona canciones, peliculas y podcasts.
     * Todos ellos pueden reproducirse, pero solo las canciones y los podcasts
     * pueden descargarse para escucharse sin conexion.
     *
     * Se pide:
     *   1. Define una interfaz Reproducible con los metodos reproducir() y
     *      obtenerDuracion().
     *   2. Define una interfaz Descargable con los metodos descargar() y
     *      obtenerTamanioMB().
     *   3. Implementa las clases Cancion, Pelicula y Podcast, haciendo que cada
     *      una implemente las interfaces que le correspondan.
     *   4. Demuestra que una misma clase puede implementar dos interfaces a la
     *      vez, y explica en un comentario por que este mecanismo permite en
     *      Java una forma de herencia multiple sin los problemas del "problema
     *      del diamante".
     *   5. En el main, crea una lista de Reproducible con objetos de los tres
     *      tipos y reproducelos todos con un unico bucle.
     *   6. A continuacion, recorre la misma lista y descarga unicamente
     *      aquellos elementos que ademas sean descargables.
     *
     * Pista: Para el segundo recorrido necesitaras comprobar el tipo real con
     * instanceof antes de hacer el casting a Descargable.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /**
     * Apartado 1: contrato de todo lo que se puede poner en marcha.
     *
     * <p>Una interfaz describe QUE sabe hacer un objeto, no COMO ni de que
     * familia procede. Cancion, Pelicula y Podcast no comparten ninguna
     * superclase y aun asi pueden convivir en una misma lista gracias a este
     * contrato comun.</p>
     */
    public interface Reproducible {

        /** Pone en marcha la reproduccion del elemento. */
        void reproducir();

        /** @return la duracion total del elemento en segundos */
        int obtenerDuracion();
    }

    /**
     * Apartado 2: contrato de lo que ademas se puede guardar en el dispositivo.
     *
     * <p>Se mantiene separado de Reproducible a proposito: si ambos metodos
     * estuviesen en una unica interfaz, Pelicula estaria obligada a implementar
     * descargar() aunque no pueda descargarse, y tendria que resolverlo con un
     * metodo vacio o lanzando una excepcion. Interfaces pequenas y separadas
     * evitan justamente eso.</p>
     */
    public interface Descargable {

        /** Guarda el elemento en el dispositivo para su uso sin conexion. */
        void descargar();

        /** @return el espacio que ocupa el elemento descargado, en megabytes */
        double obtenerTamanioMB();
    }

    /* ---------------------------------------------------------------------
     * Apartado 4: POR QUE LAS INTERFACES SON HERENCIA MULTIPLE SEGURA
     * ---------------------------------------------------------------------
     * El "problema del diamante" aparece en los lenguajes que permiten heredar
     * de varias CLASES a la vez: si B y C extienden de A y D extiende de B y C,
     * D recibe dos copias del estado de A y dos implementaciones distintas de
     * sus metodos, y el compilador no puede decidir cual vale.
     *
     * Java lo evita por construccion, con dos reglas:
     *   - Una clase solo puede extender UNA clase, de modo que el estado
     *     (los atributos de instancia) se hereda por un unico camino.
     *   - Una clase puede implementar TANTAS interfaces como quiera, porque una
     *     interfaz no aporta estado: solo declara firmas. La unica
     *     implementacion posible es la que escribe la propia clase, asi que no
     *     hay nada ambiguo que resolver.
     *
     * Cancion y Podcast implementan Reproducible y Descargable a la vez: heredan
     * dos contratos, no dos implementaciones. Y en el caso limite en que dos
     * interfaces aportasen metodos default con la misma firma, el compilador NO
     * elige por su cuenta: obliga a la clase a sobrescribir ese metodo y a
     * desambiguar explicitamente con Interfaz.super.metodo().
     * ------------------------------------------------------------------- */

    /** Apartado 3 y 4: se puede reproducir y ademas se puede descargar. */
    public static class Cancion implements Reproducible, Descargable {

        private final String titulo;
        private final String artista;
        private final int duracionSegundos;
        private final double tamanioMB;

        /**
         * @param titulo           titulo de la cancion
         * @param artista          interprete
         * @param duracionSegundos duracion en segundos
         * @param tamanioMB        tamano del fichero descargable, en megabytes
         */
        public Cancion(String titulo, String artista, int duracionSegundos, double tamanioMB) {
            this.titulo = titulo;
            this.artista = artista;
            this.duracionSegundos = duracionSegundos;
            this.tamanioMB = tamanioMB;
        }

        /** {@inheritDoc} */
        @Override
        public void reproducir() {
            System.out.println("    Sonando: \"" + titulo + "\" de " + artista
                    + " [" + formatearDuracion(duracionSegundos) + "]");
        }

        /** {@inheritDoc} */
        @Override
        public int obtenerDuracion() {
            return duracionSegundos;
        }

        /** {@inheritDoc} */
        @Override
        public void descargar() {
            System.out.println("    Descargando cancion \"" + titulo + "\" ("
                    + String.format("%.1f", tamanioMB) + " MB)");
        }

        /** {@inheritDoc} */
        @Override
        public double obtenerTamanioMB() {
            return tamanioMB;
        }
    }

    /**
     * Apartado 3: solo se puede reproducir. Al no implementar Descargable, el
     * compilador impide siquiera intentar descargarla: la restriccion del
     * enunciado queda expresada en el sistema de tipos, no en un if.
     */
    public static class Pelicula implements Reproducible {

        private final String titulo;
        private final String director;
        private final int duracionSegundos;

        /**
         * @param titulo           titulo de la pelicula
         * @param director         director de la pelicula
         * @param duracionSegundos duracion en segundos
         */
        public Pelicula(String titulo, String director, int duracionSegundos) {
            this.titulo = titulo;
            this.director = director;
            this.duracionSegundos = duracionSegundos;
        }

        /** {@inheritDoc} */
        @Override
        public void reproducir() {
            System.out.println("    Proyectando: \"" + titulo + "\" de " + director
                    + " [" + formatearDuracion(duracionSegundos) + "]");
        }

        /** {@inheritDoc} */
        @Override
        public int obtenerDuracion() {
            return duracionSegundos;
        }
    }

    /** Apartado 3 y 4: igual que Cancion, implementa las dos interfaces. */
    public static class Podcast implements Reproducible, Descargable {

        private final String titulo;
        private final String presentador;
        private final int duracionSegundos;
        private final double tamanioMB;

        /**
         * @param titulo           titulo del episodio
         * @param presentador      persona que presenta el episodio
         * @param duracionSegundos duracion en segundos
         * @param tamanioMB        tamano del fichero descargable, en megabytes
         */
        public Podcast(String titulo, String presentador, int duracionSegundos, double tamanioMB) {
            this.titulo = titulo;
            this.presentador = presentador;
            this.duracionSegundos = duracionSegundos;
            this.tamanioMB = tamanioMB;
        }

        /** {@inheritDoc} */
        @Override
        public void reproducir() {
            System.out.println("    Emitiendo: \"" + titulo + "\" con " + presentador
                    + " [" + formatearDuracion(duracionSegundos) + "]");
        }

        /** {@inheritDoc} */
        @Override
        public int obtenerDuracion() {
            return duracionSegundos;
        }

        /** {@inheritDoc} */
        @Override
        public void descargar() {
            System.out.println("    Descargando podcast \"" + titulo + "\" ("
                    + String.format("%.1f", tamanioMB) + " MB)");
        }

        /** {@inheritDoc} */
        @Override
        public double obtenerTamanioMB() {
            return tamanioMB;
        }
    }

    /**
     * Utilidad compartida por las tres clases. Vive en la clase envolvente y no
     * en las interfaces para no ensuciar los contratos: formatear no es algo
     * que un elemento multimedia deba "saber hacer", es una ayuda de
     * presentacion.
     *
     * @param segundos duracion en segundos
     * @return la duracion en formato hh:mm:ss
     */
    private static String formatearDuracion(int segundos) {
        return String.format("%02d:%02d:%02d", segundos / 3600, (segundos % 3600) / 60, segundos % 60);
    }

    // =====================================================================
    // COMPROBACION (apartados 5 y 6)
    // =====================================================================

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2404-E04] Interfaces: elementos reproducibles y descargables");

        // Apartado 5: la lista se declara del tipo de la INTERFAZ. Es lo unico
        // que tienen en comun los tres objetos, y es suficiente.
        List<Reproducible> catalogo = new ArrayList<>();
        catalogo.add(new Cancion("Bohemian Rhapsody", "Queen", 355, 8.2));
        catalogo.add(new Pelicula("Casablanca", "Michael Curtiz", 6120));
        catalogo.add(new Podcast("Historia en 10 min", "Marta Diaz", 600, 5.1));

        // Apartado 5: un unico bucle reproduce los tres tipos. Aqui no hace
        // falta instanceof porque TODOS cumplen el contrato Reproducible.
        System.out.println("  Reproduciendo la lista completa:");
        int reproducidos = 0;
        int duracionTotal = 0;
        for (Reproducible elemento : catalogo) {
            elemento.reproducir();
            duracionTotal += elemento.obtenerDuracion();
            reproducidos++;
        }
        System.out.println("  Elementos reproducidos: " + reproducidos
                + " (duracion total " + formatearDuracion(duracionTotal) + ")");

        // Apartado 6: aqui SI hace falta preguntar, y no es un fallo de diseno.
        // La lista es de Reproducible, asi que el compilador no sabe si un
        // elemento concreto cumple ademas el contrato Descargable; instanceof
        // resuelve esa duda en tiempo de ejecucion y el casting hace visible
        // la otra mitad del objeto.
        System.out.println("  Descargando solo lo que se puede descargar:");
        int descargados = 0;
        double espacioTotal = 0;
        for (Reproducible elemento : catalogo) {
            if (elemento instanceof Descargable) {
                // Casting explicito clasico. Desde Java 16 se puede abreviar con
                // pattern matching:  if (elemento instanceof Descargable d) {...}
                Descargable descargable = (Descargable) elemento;
                descargable.descargar();
                espacioTotal += descargable.obtenerTamanioMB();
                descargados++;
            }
        }
        System.out.println("  Elementos descargados: " + descargados + " de " + catalogo.size()
                + " (" + String.format("%.1f", espacioTotal) + " MB en total)");
        System.out.println("  La pelicula no se descarga: no implementa Descargable");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
