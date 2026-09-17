package com.ifcd0112.ejercicios.uf2404.bloque5_integrador;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * UF2404 - BLOQUE 5 - EJERCICIO 13: Sistema de gestion de un videoclub.
 *
 * <p>Criterios de evaluacion: CE1.8, CE2.3, CE2.9, CE2.10</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej13Videoclub {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Desarrolla una aplicacion de consola que gestione el catalogo y los
     * alquileres de un videoclub. El sistema debe manejar peliculas y
     * videojuegos, ambos alquilables pero con condiciones distintas: las
     * peliculas se alquilan por 2 dias y los videojuegos por 5, y el precio del
     * alquiler se calcula de forma diferente en cada caso.
     *
     * Se pide:
     *   1. Disena una jerarquia de clases con una superclase abstracta Articulo
     *      y las subclases Pelicula y Videojuego, aplicando polimorfismo para el
     *      calculo del precio y del plazo de devolucion.
     *   2. Crea una clase Socio con sus datos y la lista de articulos que tiene
     *      alquilados en ese momento.
     *   3. Crea una clase Videoclub que mantenga el catalogo de articulos y el
     *      registro de socios, aplicando la relacion adecuada (composicion o
     *      agregacion) en cada caso y justificandola.
     *   4. Implementa las operaciones: alquilar un articulo, devolverlo,
     *      consultar los articulos alquilados por un socio y listar el catalogo
     *      disponible.
     *   5. Un socio no puede tener mas de 3 articulos alquilados simultaneamente:
     *      modela esta regla con una excepcion propia.
     *   6. Utiliza colecciones de la biblioteca estandar para gestionar las
     *      listas, y un HashMap para localizar socios por su DNI de forma
     *      eficiente.
     *   7. Documenta todas las clases y metodos publicos con comentarios Javadoc.
     *   8. Escribe un menu de consola que permita probar todas las operaciones.
     *
     * Pista: antes de programar, dibuja el diagrama de clases con sus relaciones
     * y multiplicidades. Invertir quince minutos en el diseno te ahorrara
     * bastante mas tiempo durante la implementacion.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * DIAGRAMA DE CLASES (la pista pide dibujarlo primero; aqui va en texto).
     *
     *                          Articulo {abstract}
     *                                 ^
     *                    +------------+------------+
     *                    |                         |
     *                Pelicula                  Videojuego
     *
     *      Videoclub  *--------  Articulo      (composicion, 1 a *)
     *      Videoclub  o--------  Socio         (agregacion,   1 a *)
     *      Socio      --------->  Articulo     (asociacion,   0 a 3)
     *
     * APARTADO 3: por que cada relacion es la que es.
     *
     * Videoclub - Articulo => COMPOSICION (rombo relleno).
     * Un Articulo aqui no es "la pelicula Casablanca" como obra, sino el
     * EJEMPLAR concreto que este videoclub tiene en su estanteria. Ese ejemplar
     * pertenece al videoclub, se da de alta dentro de el (los metodos
     * altaPelicula y altaVideojuego hacen el new) y no tiene ningun sentido
     * fuera de el: si el videoclub cierra, sus ejemplares dejan de existir como
     * articulos alquilables.
     *
     * Videoclub - Socio => AGREGACION (rombo hueco).
     * Un socio es una PERSONA. Existia antes de darse de alta y sigue
     * existiendo despues de darse de baja; puede incluso ser socio de dos
     * videoclubes a la vez. Por eso el objeto Socio se crea FUERA y se entrega
     * al videoclub con altaSocio(Socio).
     *
     * Socio - Articulo => ASOCIACION simple.
     * El socio guarda referencias a los articulos que tiene en casa ahora
     * mismo. Ni los crea ni los posee: son del videoclub, y la referencia
     * desaparece al devolverlos.
     */

    // ---------------------------------------------------------------------
    // Apartado 5: la excepcion propia (y sus companeras)
    // ---------------------------------------------------------------------

    /**
     * Raiz de todos los errores de negocio del videoclub.
     *
     * <p>Tener una raiz comun permite que el menu capture una sola excepcion
     * cuando lo unico que quiere es mostrar el mensaje al usuario, sin
     * renunciar a distinguirlas cuando hace falta reaccionar distinto.</p>
     */
    public static class VideoclubException extends Exception {

        private static final long serialVersionUID = 1L;

        /**
         * @param mensaje descripcion del problema
         */
        public VideoclubException(String mensaje) {
            super(mensaje);
        }
    }

    /**
     * Apartado 5: el socio ya tiene el maximo de articulos alquilados.
     *
     * <p>Transporta el limite y el numero actual para que quien la capture
     * pueda construir un mensaje util sin volver a consultar al videoclub.</p>
     */
    public static class LimiteAlquileresException extends VideoclubException {

        private static final long serialVersionUID = 1L;

        private final String dni;
        private final int limite;
        private final int actuales;

        /**
         * @param dni      documento del socio
         * @param limite   maximo de articulos permitidos
         * @param actuales articulos que tiene ahora mismo
         */
        public LimiteAlquileresException(String dni, int limite, int actuales) {
            super("El socio " + dni + " ya tiene " + actuales + " articulos alquilados (maximo " + limite + ")");
            this.dni = dni;
            this.limite = limite;
            this.actuales = actuales;
        }

        /** @return documento del socio */
        public String getDni() { return dni; }

        /** @return maximo de articulos permitidos */
        public int getLimite() { return limite; }

        /** @return articulos alquilados en el momento del error */
        public int getActuales() { return actuales; }
    }

    /** El articulo existe en el catalogo pero ya lo tiene otro socio. */
    public static class ArticuloNoDisponibleException extends VideoclubException {

        private static final long serialVersionUID = 1L;

        private final String codigo;

        /**
         * @param codigo codigo del articulo solicitado
         */
        public ArticuloNoDisponibleException(String codigo) {
            super("El articulo " + codigo + " esta alquilado en este momento");
            this.codigo = codigo;
        }

        /** @return codigo del articulo */
        public String getCodigo() { return codigo; }
    }

    /** No consta ningun socio con ese DNI, o ningun articulo con ese codigo. */
    public static class NoEncontradoException extends VideoclubException {

        private static final long serialVersionUID = 1L;

        /**
         * @param mensaje descripcion de lo que no se ha encontrado
         */
        public NoEncontradoException(String mensaje) {
            super(mensaje);
        }
    }

    // ---------------------------------------------------------------------
    // Apartado 1: la jerarquia de articulos
    // ---------------------------------------------------------------------

    /**
     * Articulo alquilable del catalogo. Clase abstracta.
     *
     * <p>Es abstracta porque un articulo generico no se puede alquilar: no se
     * sabe cuanto cuesta ni cuantos dias dura. Esas dos preguntas son
     * justamente las que responde cada subclase, y por eso son metodos
     * abstractos.</p>
     */
    public abstract static class Articulo {

        private final String codigo;
        private final String titulo;
        private final double tarifaBase;
        private boolean alquilado;

        /**
         * @param codigo     identificador unico del ejemplar
         * @param titulo     titulo del articulo
         * @param tarifaBase importe de referencia sobre el que cada subclase
         *                   calcula su precio
         */
        protected Articulo(String codigo, String titulo, double tarifaBase) {
            this.codigo = codigo;
            this.titulo = titulo;
            this.tarifaBase = tarifaBase;
        }

        /** @return identificador unico del ejemplar */
        public String getCodigo() { return codigo; }

        /** @return titulo del articulo */
        public String getTitulo() { return titulo; }

        /** @return importe de referencia del articulo */
        protected double getTarifaBase() { return tarifaBase; }

        /** @return true si el articulo esta alquilado ahora mismo */
        public boolean estaAlquilado() { return alquilado; }

        /**
         * Marca el articulo como alquilado o disponible.
         *
         * <p>Tiene visibilidad de paquete a proposito: solo el Videoclub debe
         * poder cambiar este estado. Si fuera publico, cualquiera podria marcar
         * un articulo como devuelto sin pasar por el videoclub y el sistema
         * quedaria descuadrado.</p>
         *
         * @param alquilado nuevo estado
         */
        void setAlquilado(boolean alquilado) { this.alquilado = alquilado; }

        /**
         * Apartado 1: dias que dura el alquiler. Lo decide cada subclase.
         *
         * @return numero de dias de prestamo
         */
        public abstract int getDiasAlquiler();

        /**
         * Apartado 1: precio del alquiler. Lo calcula cada subclase con su
         * propia formula.
         *
         * @return importe en euros
         */
        public abstract double calcularPrecio();

        /**
         * Tipo de articulo, para los listados.
         *
         * @return descripcion corta del tipo
         */
        public abstract String getTipo();

        /**
         * Fecha limite de devolucion.
         *
         * <p>Este metodo NO es abstracto aunque el plazo dependa del tipo: la
         * regla "fecha de alquiler mas los dias que correspondan" es la misma
         * para todos, y lo unico que cambia es el numero de dias. Se escribe
         * una sola vez aqui y se delega la parte variable en
         * getDiasAlquiler(). Es el patron metodo plantilla.</p>
         *
         * @param fechaAlquiler dia en que se lleva el articulo
         * @return dia en que debe devolverlo
         */
        public LocalDate getFechaDevolucion(LocalDate fechaAlquiler) {
            return fechaAlquiler.plusDays(getDiasAlquiler());
        }

        @Override
        public String toString() {
            return String.format("%-6s %-28s %-11s %2d dias  %6s EUR",
                    codigo, titulo, getTipo(), getDiasAlquiler(),
                    String.format("%.2f", calcularPrecio()));
        }
    }

    /** Pelicula: 2 dias de alquiler y recargo si es estreno. */
    public static class Pelicula extends Articulo {

        /** Dias de prestamo de una pelicula, segun el enunciado. */
        public static final int DIAS = 2;

        /** Recargo que se aplica a los estrenos. */
        private static final double RECARGO_ESTRENO = 0.20;

        private final String director;
        private final boolean estreno;

        /**
         * @param codigo     identificador unico del ejemplar
         * @param titulo     titulo de la pelicula
         * @param tarifaBase precio por dia
         * @param director   director de la pelicula
         * @param estreno    true si es un estreno
         */
        public Pelicula(String codigo, String titulo, double tarifaBase, String director, boolean estreno) {
            super(codigo, titulo, tarifaBase);
            this.director = director;
            this.estreno = estreno;
        }

        /** @return director de la pelicula */
        public String getDirector() { return director; }

        /** @return true si es un estreno */
        public boolean esEstreno() { return estreno; }

        @Override
        public int getDiasAlquiler() {
            return DIAS;
        }

        /**
         * Precio de una pelicula: tarifa por dia multiplicada por los dos dias,
         * mas un 20 por ciento si es estreno.
         *
         * @return importe en euros
         */
        @Override
        public double calcularPrecio() {
            double precio = getTarifaBase() * DIAS;
            return estreno ? precio * (1 + RECARGO_ESTRENO) : precio;
        }

        @Override
        public String getTipo() {
            return estreno ? "Estreno" : "Pelicula";
        }
    }

    /** Videojuego: 5 dias de alquiler y tarifa con parte fija y parte variable. */
    public static class Videojuego extends Articulo {

        /** Dias de prestamo de un videojuego, segun el enunciado. */
        public static final int DIAS = 5;

        /** Importe que se cobra por el hecho de sacar el juego. */
        private static final double CUOTA_FIJA = 3.00;

        private final String plataforma;

        /**
         * @param codigo     identificador unico del ejemplar
         * @param titulo     titulo del videojuego
         * @param tarifaBase precio de cada dia adicional al primero
         * @param plataforma consola o sistema
         */
        public Videojuego(String codigo, String titulo, double tarifaBase, String plataforma) {
            super(codigo, titulo, tarifaBase);
            this.plataforma = plataforma;
        }

        /** @return consola o sistema del videojuego */
        public String getPlataforma() { return plataforma; }

        @Override
        public int getDiasAlquiler() {
            return DIAS;
        }

        /**
         * Precio de un videojuego: una cuota fija por el prestamo mas la tarifa
         * de cada dia adicional al primero.
         *
         * <p>Es una formula de forma DISTINTA a la de la pelicula, no solo con
         * numeros distintos, y es lo que justifica que calcularPrecio sea
         * abstracto en lugar de un unico metodo con un if sobre el tipo.</p>
         *
         * @return importe en euros
         */
        @Override
        public double calcularPrecio() {
            return CUOTA_FIJA + getTarifaBase() * (DIAS - 1);
        }

        @Override
        public String getTipo() {
            return "Videojuego";
        }
    }

    // ---------------------------------------------------------------------
    // Apartado 2: el socio
    // ---------------------------------------------------------------------

    /** Socio del videoclub, con los articulos que tiene alquilados ahora. */
    public static class Socio {

        private final String dni;
        private final String nombre;
        private final List<Articulo> alquilados = new ArrayList<>();

        /**
         * @param dni    documento del socio, usado como clave
         * @param nombre nombre y apellidos
         */
        public Socio(String dni, String nombre) {
            this.dni = dni;
            this.nombre = nombre;
        }

        /** @return documento del socio */
        public String getDni() { return dni; }

        /** @return nombre y apellidos */
        public String getNombre() { return nombre; }

        /**
         * Apartado 4: articulos que el socio tiene alquilados en este momento.
         *
         * <p>Se devuelve una vista NO modificable. Si se devolviera la lista
         * real, cualquiera podria anadirle articulos desde fuera saltandose el
         * limite de tres del apartado 5.</p>
         *
         * @return lista de solo lectura
         */
        public List<Articulo> getAlquilados() {
            return Collections.unmodifiableList(alquilados);
        }

        /** @return numero de articulos alquilados ahora mismo */
        public int numeroAlquilados() { return alquilados.size(); }

        /**
         * Anade un articulo a la lista del socio. Visibilidad de paquete: solo
         * el Videoclub controla los alquileres.
         *
         * @param a articulo alquilado
         */
        void anadirAlquilado(Articulo a) { alquilados.add(a); }

        /**
         * Quita un articulo de la lista del socio.
         *
         * @param a articulo devuelto
         * @return true si el socio lo tenia
         */
        boolean quitarAlquilado(Articulo a) { return alquilados.remove(a); }

        @Override
        public String toString() {
            return nombre + " (" + dni + ")";
        }
    }

    // ---------------------------------------------------------------------
    // Apartado 3: el videoclub
    // ---------------------------------------------------------------------

    /** Videoclub: catalogo de articulos, registro de socios y operaciones. */
    public static class Videoclub {

        /** Apartado 5: articulos que puede tener un socio a la vez. */
        public static final int MAX_ALQUILERES = 3;

        private final String nombre;

        // Apartado 6: LinkedHashMap para el catalogo. Da busqueda por codigo en
        // tiempo constante igual que un HashMap y ademas conserva el orden de
        // alta, para que los listados salgan siempre igual y sean predecibles.
        private final Map<String, Articulo> catalogo = new LinkedHashMap<>();

        // Apartado 6: HashMap de socios por DNI, tal y como pide el enunciado.
        // Con una lista habria que recorrerla entera en cada alquiler.
        private final Map<String, Socio> socios = new HashMap<>();

        /**
         * @param nombre nombre comercial del videoclub
         */
        public Videoclub(String nombre) {
            this.nombre = nombre;
        }

        /** @return nombre comercial del videoclub */
        public String getNombre() { return nombre; }

        /**
         * Da de alta una pelicula en el catalogo.
         *
         * <p>Composicion: el ejemplar se crea AQUI dentro. No hay ningun metodo
         * que acepte un Articulo ya construido, y por eso ningun ejemplar puede
         * estar en dos videoclubes a la vez.</p>
         *
         * @param codigo     identificador unico
         * @param titulo     titulo de la pelicula
         * @param tarifaBase precio por dia
         * @param director   director
         * @param estreno    true si es estreno
         * @return el ejemplar creado
         */
        public Pelicula altaPelicula(String codigo, String titulo, double tarifaBase,
                                     String director, boolean estreno) {
            Pelicula p = new Pelicula(codigo, titulo, tarifaBase, director, estreno);
            catalogo.put(codigo, p);
            return p;
        }

        /**
         * Da de alta un videojuego en el catalogo (composicion, igual que arriba).
         *
         * @param codigo     identificador unico
         * @param titulo     titulo del videojuego
         * @param tarifaBase precio de cada dia adicional
         * @param plataforma consola o sistema
         * @return el ejemplar creado
         */
        public Videojuego altaVideojuego(String codigo, String titulo, double tarifaBase, String plataforma) {
            Videojuego v = new Videojuego(codigo, titulo, tarifaBase, plataforma);
            catalogo.put(codigo, v);
            return v;
        }

        /**
         * Da de alta un socio ya existente.
         *
         * <p>Agregacion: el objeto Socio se construye fuera y se entrega. El
         * videoclub guarda una referencia, no una copia, y si el socio se da de
         * baja el objeto sigue existiendo.</p>
         *
         * @param socio persona que se hace socia
         */
        public void altaSocio(Socio socio) {
            socios.put(socio.getDni(), socio);
        }

        /**
         * Apartado 4: alquila un articulo a un socio.
         *
         * @param dni    documento del socio
         * @param codigo codigo del articulo
         * @return el articulo alquilado
         * @throws VideoclubException si el socio o el articulo no constan, si el
         *                            articulo ya esta alquilado o si el socio ha
         *                            llegado al maximo de tres
         */
        public Articulo alquilar(String dni, String codigo) throws VideoclubException {
            Socio socio = buscarSocio(dni);
            Articulo articulo = buscarArticulo(codigo);

            if (articulo.estaAlquilado()) {
                throw new ArticuloNoDisponibleException(codigo);
            }
            // Apartado 5: la comprobacion del limite va ANTES de tocar nada.
            // Si se comprobara despues de marcar el articulo, una excepcion
            // dejaria el articulo bloqueado sin que nadie lo tenga.
            if (socio.numeroAlquilados() >= MAX_ALQUILERES) {
                throw new LimiteAlquileresException(dni, MAX_ALQUILERES, socio.numeroAlquilados());
            }

            articulo.setAlquilado(true);
            socio.anadirAlquilado(articulo);
            return articulo;
        }

        /**
         * Apartado 4: devuelve un articulo.
         *
         * @param dni    documento del socio
         * @param codigo codigo del articulo
         * @return el articulo devuelto
         * @throws VideoclubException si el socio o el articulo no constan, o si
         *                            ese socio no tenia ese articulo
         */
        public Articulo devolver(String dni, String codigo) throws VideoclubException {
            Socio socio = buscarSocio(dni);
            Articulo articulo = buscarArticulo(codigo);

            if (!socio.quitarAlquilado(articulo)) {
                throw new NoEncontradoException(
                        "El socio " + dni + " no tiene alquilado el articulo " + codigo);
            }
            articulo.setAlquilado(false);
            return articulo;
        }

        /**
         * Apartado 4: articulos que tiene alquilados un socio.
         *
         * @param dni documento del socio
         * @return lista de solo lectura
         * @throws VideoclubException si el socio no consta
         */
        public List<Articulo> consultarAlquilados(String dni) throws VideoclubException {
            return buscarSocio(dni).getAlquilados();
        }

        /**
         * Apartado 4: articulos del catalogo que estan disponibles.
         *
         * @return lista de articulos no alquilados
         */
        public List<Articulo> catalogoDisponible() {
            List<Articulo> disponibles = new ArrayList<>();
            for (Articulo a : catalogo.values()) {
                if (!a.estaAlquilado()) {
                    disponibles.add(a);
                }
            }
            return disponibles;
        }

        /**
         * Catalogo entero, este alquilado o no.
         *
         * @return copia de la lista de articulos
         */
        public List<Articulo> catalogoCompleto() {
            return new ArrayList<>(catalogo.values());
        }

        /**
         * Socios dados de alta.
         *
         * @return copia de la lista de socios
         */
        public List<Socio> listaSocios() {
            return new ArrayList<>(socios.values());
        }

        /**
         * Localiza un socio por su DNI.
         *
         * @param dni documento a buscar
         * @return el socio
         * @throws VideoclubException si no consta
         */
        public Socio buscarSocio(String dni) throws VideoclubException {
            Socio socio = socios.get(dni);
            if (socio == null) {
                throw new NoEncontradoException("No existe ningun socio con DNI " + dni);
            }
            return socio;
        }

        /**
         * Localiza un articulo por su codigo.
         *
         * @param codigo codigo a buscar
         * @return el articulo
         * @throws VideoclubException si no consta
         */
        public Articulo buscarArticulo(String codigo) throws VideoclubException {
            Articulo articulo = catalogo.get(codigo);
            if (articulo == null) {
                throw new NoEncontradoException("No existe ningun articulo con codigo " + codigo);
            }
            return articulo;
        }
    }

    // =====================================================================
    // COMPROBACION
    // =====================================================================

    /**
     * Crea un videoclub con datos de ejemplo, para la demostracion y el menu.
     *
     * @return videoclub ya poblado
     */
    private static Videoclub crearVideoclubDeEjemplo() {
        Videoclub vc = new Videoclub("Videoclub Central");

        vc.altaPelicula("P001", "Casablanca", 2.50, "Michael Curtiz", false);
        vc.altaPelicula("P002", "Origen", 2.50, "Christopher Nolan", true);
        vc.altaPelicula("P003", "El padrino", 2.50, "Francis Ford Coppola", false);
        vc.altaVideojuego("V001", "Super Mario Bros", 1.00, "Nintendo Switch");
        vc.altaVideojuego("V002", "The Last of Us", 1.00, "PlayStation 5");

        vc.altaSocio(new Socio("11111111A", "Ana Lopez"));
        vc.altaSocio(new Socio("22222222B", "Luis Gomez"));
        return vc;
    }

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2404-E13] Sistema de gestion de un videoclub");

        Videoclub vc = crearVideoclubDeEjemplo();
        LocalDate hoy = LocalDate.of(2026, 3, 2);   // fecha fija: salida reproducible

        // Apartado 1: el polimorfismo en accion. El bucle no pregunta de que
        // tipo es cada articulo; cada uno responde con su propio precio y plazo.
        System.out.println("  Catalogo completo (precio y plazo calculados por polimorfismo):");
        for (Articulo a : vc.catalogoCompleto()) {
            System.out.println("    " + a + "  devolver el " + a.getFechaDevolucion(hoy));
        }

        // Apartado 4: alquilar.
        System.out.println("  Alquileres de Ana Lopez:");
        try {
            for (String codigo : new String[] { "P001", "V001", "P002" }) {
                Articulo a = vc.alquilar("11111111A", codigo);
                System.out.println("    " + a.getTitulo() + " por " + String.format("%.2f", a.calcularPrecio())
                        + " EUR, devolver el " + a.getFechaDevolucion(hoy));
            }
        } catch (VideoclubException e) {
            System.out.println("    Error inesperado: " + e.getMessage());
        }

        // Apartado 5: el cuarto alquiler debe fallar.
        try {
            vc.alquilar("11111111A", "P003");
            System.out.println("    ERROR: se ha permitido un cuarto alquiler");
        } catch (LimiteAlquileresException e) {
            System.out.println("    Cuarto alquiler rechazado: " + e.getMessage());
        } catch (VideoclubException e) {
            System.out.println("    " + e.getMessage());
        }

        // Articulo ya alquilado por otro socio.
        try {
            vc.alquilar("22222222B", "P001");
            System.out.println("    ERROR: se ha alquilado dos veces el mismo ejemplar");
        } catch (ArticuloNoDisponibleException e) {
            System.out.println("    Luis pide P001: " + e.getMessage());
        } catch (VideoclubException e) {
            System.out.println("    " + e.getMessage());
        }

        // Apartado 4: consultar y listar.
        try {
            System.out.println("  Articulos de " + vc.buscarSocio("11111111A") + ":");
            for (Articulo a : vc.consultarAlquilados("11111111A")) {
                System.out.println("    " + a.getCodigo() + " " + a.getTitulo());
            }
        } catch (VideoclubException e) {
            System.out.println("    " + e.getMessage());
        }

        System.out.print("  Catalogo disponible:");
        for (Articulo a : vc.catalogoDisponible()) {
            System.out.print(" " + a.getCodigo());
        }
        System.out.println();

        // Apartado 4: devolver, y comprobar que vuelve al catalogo.
        try {
            Articulo devuelto = vc.devolver("11111111A", "P001");
            System.out.println("  Devuelto " + devuelto.getTitulo() + "; Ana tiene ahora "
                    + vc.buscarSocio("11111111A").numeroAlquilados() + " articulos");
            Articulo ahora = vc.alquilar("22222222B", "P001");
            System.out.println("  Luis ya puede alquilar " + ahora.getTitulo());
        } catch (VideoclubException e) {
            System.out.println("  " + e.getMessage());
        }

        // Devolver algo que no se tiene.
        try {
            vc.devolver("22222222B", "P003");
        } catch (VideoclubException e) {
            System.out.println("  " + e.getMessage());
        }

        System.out.println("  (apartado 8) Menu de consola: ejecuta esta clase con el argumento menu");
        System.out.println();
    }

    // ---------------------------------------------------------------------
    // Apartado 8: el menu de consola
    // ---------------------------------------------------------------------

    /**
     * Menu interactivo que permite probar todas las operaciones.
     *
     * <p>Se ejecuta con el argumento "menu". Se separa de resolver() porque el
     * runner del bloque no puede quedarse esperando a que alguien teclee.</p>
     */
    public static void menu() {
        Videoclub vc = crearVideoclubDeEjemplo();
        // try-with-resources sobre el Scanner: cierra System.in al salir.
        try (Scanner sc = new Scanner(System.in)) {
            boolean salir = false;
            while (!salir) {
                System.out.println();
                System.out.println("=== " + vc.getNombre() + " ===");
                System.out.println("1. Listar catalogo disponible");
                System.out.println("2. Listar catalogo completo");
                System.out.println("3. Alquilar articulo");
                System.out.println("4. Devolver articulo");
                System.out.println("5. Consultar alquileres de un socio");
                System.out.println("6. Listar socios");
                System.out.println("0. Salir");
                System.out.print("Opcion: ");

                String opcion = sc.hasNextLine() ? sc.nextLine().trim() : "0";
                try {
                    switch (opcion) {
                        case "1":
                            for (Articulo a : vc.catalogoDisponible()) {
                                System.out.println("  " + a);
                            }
                            break;
                        case "2":
                            for (Articulo a : vc.catalogoCompleto()) {
                                System.out.println("  " + a + (a.estaAlquilado() ? "  [ALQUILADO]" : ""));
                            }
                            break;
                        case "3": {
                            System.out.print("DNI del socio: ");
                            String dni = sc.nextLine().trim();
                            System.out.print("Codigo del articulo: ");
                            String codigo = sc.nextLine().trim();
                            Articulo a = vc.alquilar(dni, codigo);
                            System.out.println("  Alquilado " + a.getTitulo() + " por "
                                    + String.format("%.2f", a.calcularPrecio()) + " EUR, devolver el "
                                    + a.getFechaDevolucion(LocalDate.now()));
                            break;
                        }
                        case "4": {
                            System.out.print("DNI del socio: ");
                            String dni = sc.nextLine().trim();
                            System.out.print("Codigo del articulo: ");
                            String codigo = sc.nextLine().trim();
                            System.out.println("  Devuelto " + vc.devolver(dni, codigo).getTitulo());
                            break;
                        }
                        case "5": {
                            System.out.print("DNI del socio: ");
                            String dni = sc.nextLine().trim();
                            List<Articulo> lista = vc.consultarAlquilados(dni);
                            if (lista.isEmpty()) {
                                System.out.println("  No tiene articulos alquilados");
                            }
                            for (Articulo a : lista) {
                                System.out.println("  " + a);
                            }
                            break;
                        }
                        case "6":
                            for (Socio s : vc.listaSocios()) {
                                System.out.println("  " + s + " - " + s.numeroAlquilados() + " alquilados");
                            }
                            break;
                        case "0":
                            salir = true;
                            break;
                        default:
                            System.out.println("  Opcion no valida");
                    }
                } catch (VideoclubException e) {
                    // Un solo catch de la raiz: aqui la reaccion es la misma en
                    // todos los casos, mostrar el mensaje y volver al menu.
                    System.out.println("  " + e.getMessage());
                }
            }
        }
        System.out.println("Hasta luego.");
    }

    /**
     * Permite ejecutar este ejercicio de forma aislada.
     *
     * @param args si el primero es "menu", arranca el menu interactivo
     */
    public static void main(String[] args) {
        if (args.length > 0 && "menu".equalsIgnoreCase(args[0])) {
            menu();
        } else {
            resolver();
        }
    }
}
