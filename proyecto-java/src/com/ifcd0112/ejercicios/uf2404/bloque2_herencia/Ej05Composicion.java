package com.ifcd0112.ejercicios.uf2404.bloque2_herencia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * UF2404 - BLOQUE 2 - EJERCICIO 5: Composicion y agregacion: gestion de una biblioteca.
 *
 * <p>Criterios de evaluacion: CE1.8, CE2.3</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej05Composicion {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Modela el sistema de una biblioteca segun estas reglas: una Biblioteca
     * tiene varias Estanterias, y cada estanteria pertenece a una unica
     * biblioteca y no tiene sentido fuera de ella. Ademas, la biblioteca
     * gestiona un catalogo de Libros que pueden existir con independencia
     * de ella.
     *
     * Se pide:
     *   1. Identifica razonadamente cual de las dos relaciones
     *      (Biblioteca-Estanteria y Biblioteca-Libro) es una composicion y cual
     *      una agregacion. Justificalo en un comentario en el codigo.
     *   2. Implementa la composicion creando los objetos Estanteria dentro del
     *      propio constructor de Biblioteca, sin ofrecer ningun metodo que
     *      permita sustituirlos desde fuera.
     *   3. Implementa la agregacion mediante metodos agregarLibro(Libro l) y
     *      retirarLibro(Libro l) que reciban objetos ya creados en el exterior.
     *   4. Anade un metodo buscarPorAutor(String autor) que devuelva una lista
     *      con todos los libros de ese autor.
     *   5. Demuestra en el main que, tras retirar un libro del catalogo, el
     *      objeto Libro sigue existiendo y siendo utilizable.
     *
     * Pista: El criterio para distinguirlas es la dependencia del ciclo de
     * vida: si la parte no puede sobrevivir a la destruccion del todo, es
     * composicion.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /* ---------------------------------------------------------------------
     * Apartado 1: JUSTIFICACION DE CADA RELACION
     * ---------------------------------------------------------------------
     * Biblioteca - Estanteria  =>  COMPOSICION (rombo relleno en UML).
     *   La estanteria es una parte fisica del edificio: la crea la propia
     *   biblioteca, pertenece a una sola biblioteca y si la biblioteca cierra
     *   sus estanterias dejan de tener sentido y desaparecen con ella. La parte
     *   NO sobrevive al todo, que es exactamente el criterio de la pista.
     *   En codigo se traduce en: los objetos Estanteria nacen dentro del
     *   constructor de Biblioteca y no existe ningun metodo que permita
     *   inyectarlos, sustituirlos ni extraerlos.
     *
     * Biblioteca - Libro  =>  AGREGACION (rombo hueco en UML).
     *   Un libro existe antes de entrar en el catalogo y sigue existiendo
     *   despues de salir de el. Puede incluso pasar a otra biblioteca. La
     *   biblioteca lo USA y lo gestiona, pero no es su duena ni controla su
     *   ciclo de vida. En codigo se traduce en: los objetos Libro se crean
     *   FUERA y se pasan ya construidos a agregarLibro(...).
     * ------------------------------------------------------------------- */

    /**
     * La parte "debil" de la agregacion: un libro es un objeto independiente
     * que no guarda ninguna referencia a la biblioteca en la que este. Esa
     * ausencia de referencia hacia arriba es lo que le permite cambiar de
     * biblioteca, o no estar en ninguna, sin quedar en un estado incoherente.
     */
    public static class Libro {

        private final String titulo;
        private final String autor;
        private final String isbn;

        /**
         * @param titulo titulo del libro
         * @param autor  nombre del autor
         * @param isbn   identificador ISBN
         */
        public Libro(String titulo, String autor, String isbn) {
            this.titulo = titulo;
            this.autor = autor;
            this.isbn = isbn;
        }

        public String getTitulo() { return titulo; }
        public String getAutor()  { return autor; }
        public String getIsbn()   { return isbn; }

        @Override
        public String toString() {
            return "\"" + titulo + "\" de " + autor + " (ISBN " + isbn + ")";
        }
    }

    /**
     * La parte "fuerte" de la composicion. Se declara inmutable (atributos
     * final y sin setters) para que ni siquiera pueda alterarse desde fuera si
     * alguien obtiene una referencia a ella al consultar la biblioteca.
     */
    public static class Estanteria {

        private final String codigo;
        private final String tematica;

        /**
         * Constructor de uso interno de la biblioteca.
         *
         * @param codigo   identificador de la estanteria dentro de la sala
         * @param tematica materia que alberga
         */
        public Estanteria(String codigo, String tematica) {
            this.codigo = codigo;
            this.tematica = tematica;
        }

        public String getCodigo()   { return codigo; }
        public String getTematica() { return tematica; }

        @Override
        public String toString() {
            return "Estanteria " + codigo + " (" + tematica + ")";
        }
    }

    /** El "todo": compone sus estanterias y agrega sus libros. */
    public static class Biblioteca {

        private static final String[] TEMATICAS = {"Narrativa", "Ensayo", "Poesia", "Infantil", "Consulta"};

        private final String nombre;

        // Apartado 2 (composicion): la lista es final y se rellena dentro del
        // constructor. No hay setEstanterias(...) ni addEstanteria(...): desde
        // fuera es imposible sustituir las partes del objeto.
        private final List<Estanteria> estanterias = new ArrayList<>();

        // Apartado 3 (agregacion): la lista guarda REFERENCIAS a libros que se
        // han creado en otro sitio. La biblioteca apunta a ellos, no los posee.
        private final List<Libro> catalogo = new ArrayList<>();

        /**
         * Apartado 2: al construir la biblioteca se construyen sus estanterias.
         * Nacen y mueren con ella, que es lo que caracteriza a la composicion.
         *
         * @param nombre            nombre de la biblioteca
         * @param numeroEstanterias cuantas estanterias tiene la sala
         */
        public Biblioteca(String nombre, int numeroEstanterias) {
            this.nombre = nombre;
            for (int i = 0; i < numeroEstanterias; i++) {
                // new dentro del constructor del todo: la marca de la composicion.
                estanterias.add(new Estanteria("E" + (i + 1), TEMATICAS[i % TEMATICAS.length]));
            }
        }

        public String getNombre() { return nombre; }

        /**
         * Apartado 2: se puede consultar el mobiliario, pero no tocarlo.
         *
         * <p>Se devuelve una vista de solo lectura y no la lista interna: si
         * devolviesemos la lista tal cual, quien la recibiese podria vaciarla o
         * anadir estanterias ajenas, y la composicion se romperia por la puerta
         * de atras.</p>
         *
         * @return las estanterias de esta biblioteca, en modo solo lectura
         */
        public List<Estanteria> getEstanterias() {
            return Collections.unmodifiableList(estanterias);
        }

        /**
         * Apartado 3: incorpora al catalogo un libro que ya existia.
         *
         * @param l libro ya construido en el exterior
         */
        public void agregarLibro(Libro l) {
            catalogo.add(l);
        }

        /**
         * Apartado 3: saca el libro del catalogo.
         *
         * <p>Solo se elimina la REFERENCIA de la lista. El objeto Libro sigue
         * intacto en memoria mientras alguien lo apunte, y por eso puede
         * incorporarse despues a otra biblioteca.</p>
         *
         * @param l libro a retirar
         * @return true si estaba en el catalogo y se ha retirado
         */
        public boolean retirarLibro(Libro l) {
            return catalogo.remove(l);
        }

        /**
         * Apartado 4: busqueda por autor.
         *
         * <p>Devuelve una lista NUEVA con las coincidencias. Asi quien la reciba
         * puede ordenarla o filtrarla sin alterar el catalogo real.</p>
         *
         * @param autor nombre del autor a buscar; se compara sin distinguir mayusculas
         * @return los libros de ese autor, lista vacia si no hay ninguno
         */
        public List<Libro> buscarPorAutor(String autor) {
            List<Libro> encontrados = new ArrayList<>();
            for (Libro l : catalogo) {
                if (l.getAutor().equalsIgnoreCase(autor)) {
                    encontrados.add(l);
                }
            }
            return encontrados;
        }

        /** @return cuantos libros hay ahora mismo en el catalogo */
        public int getNumeroLibros() {
            return catalogo.size();
        }

        @Override
        public String toString() {
            return nombre + " [" + estanterias.size() + " estanterias, "
                    + catalogo.size() + " libros en catalogo]";
        }
    }

    // =====================================================================
    // COMPROBACION (apartado 5)
    // =====================================================================

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2404-E05] Composicion y agregacion: gestion de una biblioteca");

        // COMPOSICION: basta con crear la biblioteca; sus estanterias vienen
        // dentro. No hemos escrito ni un solo new Estanteria(...) aqui fuera.
        Biblioteca central = new Biblioteca("Biblioteca Central", 3);
        System.out.println("  " + central);
        for (Estanteria e : central.getEstanterias()) {
            System.out.println("    " + e);
        }

        // Prueba de que las partes no se pueden sustituir desde fuera.
        try {
            central.getEstanterias().add(new Estanteria("PIRATA", "Ajena"));
            System.out.println("  ERROR: se ha podido colar una estanteria desde fuera");
        } catch (UnsupportedOperationException ex) {
            System.out.println("  No se pueden anadir ni sustituir estanterias desde fuera: composicion protegida");
        }

        // AGREGACION: los libros se crean AQUI, fuera de la biblioteca, y
        // existen antes de que ninguna biblioteca los conozca.
        Libro elCamino = new Libro("El camino", "Miguel Delibes", "978-84-233-1234-5");
        Libro losSantos = new Libro("Los santos inocentes", "Miguel Delibes", "978-84-233-2345-6");
        Libro laColmena = new Libro("La colmena", "Camilo Jose Cela", "978-84-233-3456-7");
        System.out.println("  " + elCamino + " existe antes de entrar en ninguna biblioteca");

        central.agregarLibro(elCamino);
        central.agregarLibro(losSantos);
        central.agregarLibro(laColmena);
        System.out.println("  Tras agregar 3 libros -> " + central);

        // Apartado 4.
        List<Libro> deDelibes = central.buscarPorAutor("Miguel Delibes");
        System.out.println("  buscarPorAutor(\"Miguel Delibes\") -> " + deDelibes.size() + " resultados:");
        for (Libro l : deDelibes) {
            System.out.println("    " + l);
        }

        // Apartado 5: retiramos el libro del catalogo.
        boolean retirado = central.retirarLibro(elCamino);
        System.out.println("  retirarLibro(El camino) -> " + retirado + ", ahora " + central);

        // El objeto NO ha desaparecido: la variable elCamino sigue apuntando a
        // el y todos sus metodos siguen funcionando.
        System.out.println("  El objeto retirado sigue vivo y utilizable: " + elCamino);
        System.out.println("  Su autor sigue siendo consultable: " + elCamino.getAutor());

        // Prueba definitiva de la agregacion: el mismo objeto pasa a OTRA
        // biblioteca. Si la relacion fuese composicion esto seria imposible,
        // porque el libro habria muerto al salir del catalogo.
        Biblioteca barrio = new Biblioteca("Biblioteca de Barrio", 2);
        barrio.agregarLibro(elCamino);
        System.out.println("  El mismo objeto pasa a otra biblioteca -> " + barrio);
        System.out.println("  Libros de Delibes en la nueva biblioteca: "
                + barrio.buscarPorAutor("Miguel Delibes").size()
                + " | en la de origen quedan: " + central.buscarPorAutor("Miguel Delibes").size());
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
