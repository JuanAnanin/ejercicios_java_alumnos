package com.ifcd0112.ejercicios.ampliacion.uf2404;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * AMPLIACION UF2404 - EJERCICIO A3: Persistencia en ficheros.
 *
 * <p>Criterios de evaluacion: CE2.8, CE2.10, CE2.3</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class A03PersistenciaFicheros {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Todo lo que has programado hasta ahora desaparece al cerrar el programa.
     * Antes de guardar datos en una base de datos (UF2405), conviene saber
     * guardarlos en un fichero, que es la forma mas sencilla de persistencia y
     * la que se usa para configuraciones, registros e intercambio de datos.
     *
     * El videoclub del ejercicio 13 de la UF2404 debe conservar su catalogo
     * entre ejecuciones.
     *
     * Se pide:
     *   1. Implementa un metodo que guarde una lista de articulos en un fichero
     *      de texto con formato CSV, una linea por articulo, empleando las
     *      clases de la biblioteca estandar y garantizando que el fichero se
     *      cierra pase lo que pase.
     *   2. Implementa el metodo inverso, que lea ese fichero y reconstruya la
     *      lista de objetos.
     *   3. Comprueba el ciclo completo: guardar, leer y verificar que lo leido
     *      coincide exactamente con lo guardado.
     *   4. Fija explicitamente la codificacion de caracteres al escribir y al
     *      leer, y explica en un comentario que ocurre si no se hace.
     *   5. Gestiona los errores que pueden darse: que el fichero no exista, que
     *      una linea este mal formada y que no haya permisos. Decide en cada
     *      caso si conviene abortar o continuar, y justificalo.
     *   6. Anade un metodo que registre una linea en un fichero de bitacora sin
     *      borrar lo anterior, e indica que opcion de apertura lo permite.
     *   7. Guarda ahora la misma lista mediante serializacion de objetos.
     *      Compara las dos soluciones e indica cuando usarias cada una.
     *   8. AMPLIACION: comprueba que ocurre con el identificador de version de
     *      serializacion si se anade un atributo a la clase, y explica por que
     *      conviene declararlo a mano.
     *
     * Pista: al leer un CSV, no basta con partir la linea por comas. Piensa que
     * pasa con una pelicula titulada "Alien, el octavo pasajero".
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * APARTADO 4: por que hay que fijar la codificacion.
     *
     * Si no se indica, Java usa la codificacion por defecto de la maquina. Y esa
     * cambia: en Linux y en macOS suele ser UTF-8, y en Windows ha sido durante
     * anos windows-1252. El resultado es el clasico fichero que se escribe bien
     * en un ordenador y se lee mal en otro, con los acentos convertidos en
     * simbolos raros.
     *
     * Lo peor es que el error es SILENCIOSO: nadie lanza una excepcion, los
     * datos simplemente se corrompen. Y solo afecta a los caracteres no ingleses,
     * asi que en las pruebas con datos de ejemplo puede no aparecer nunca.
     *
     * Desde Java 18 el valor por defecto es UTF-8 en todas las plataformas, lo
     * que quita hierro al asunto, pero indicarlo sigue siendo lo correcto: deja
     * escrito lo que se pretende y funciona con cualquier version.
     *
     * APARTADO 5: que hacer con cada error.
     *
     *   Fichero que no existe. NO es un error si es la primera ejecucion: el
     *   catalogo esta vacio y punto. Se devuelve una lista vacia. Tratarlo como
     *   excepcion obligaria a crear un fichero vacio a mano antes de arrancar.
     *
     *   Linea mal formada. Se AVISA y se sigue con las demas. Perder el catalogo
     *   entero por una linea corrupta seria desproporcionado. Ahora bien, hay
     *   que dejar constancia: una linea que desaparece en silencio es peor que
     *   un fallo ruidoso.
     *
     *   Sin permisos, o disco lleno. Ahi SI se aborta: no se ha guardado nada y
     *   quien llamo tiene que enterarse. Devolver "todo correcto" cuando no se
     *   ha escrito nada es la peor de las opciones posibles.
     *
     * La regla general: continuar cuando el fallo afecta a un dato aislado y se
     * puede aislar; abortar cuando afecta a la operacion completa.
     *
     * APARTADO 7: CSV frente a serializacion.
     *
     *   TEXTO (CSV, JSON, XML)
     *     + Se lee con cualquier editor, y con Excel.
     *     + Sobrevive a los cambios de la clase: si se anade una columna, los
     *       ficheros viejos siguen leyendose.
     *     + Lo entiende cualquier lenguaje, no solo Java.
     *     - Hay que escribir la conversion a mano.
     *     - Ocupa mas.
     *
     *   SERIALIZACION
     *     + Una linea de codigo guarda un grafo entero de objetos, con sus
     *       referencias cruzadas y sin escribir ninguna conversion.
     *     - El fichero es binario e ilegible.
     *     - Es fragil ante los cambios de la clase (apartado 8).
     *     - Solo lo entiende Java.
     *     - Y un aviso serio: deserializar datos que vengan de fuera es un
     *       agujero de seguridad conocido, porque construye objetos arbitrarios
     *       antes de que nadie pueda validarlos.
     *
     *   Criterio practico: para datos que alguien vaya a mirar, intercambiar o
     *   conservar, texto. Para una copia temporal de trabajo dentro del mismo
     *   programa, serializacion. Para datos que vengan de la red, ninguna de las
     *   dos: un formato de datos puro como JSON, y validando.
     */

    /** Articulo del catalogo del videoclub. */
    public static class Articulo implements Serializable {

        private static final long serialVersionUID = 1L;

        private final String codigo;
        private final String titulo;
        private final String tipo;
        private final double precio;
        private final LocalDate alta;

        /**
         * @param codigo identificador del ejemplar
         * @param titulo titulo del articulo
         * @param tipo   Pelicula o Videojuego
         * @param precio importe del alquiler
         * @param alta   fecha de incorporacion al catalogo
         */
        public Articulo(String codigo, String titulo, String tipo, double precio, LocalDate alta) {
            this.codigo = codigo;
            this.titulo = titulo;
            this.tipo = tipo;
            this.precio = precio;
            this.alta = alta;
        }

        /** @return identificador del ejemplar */
        public String getCodigo() { return codigo; }

        /** @return titulo del articulo */
        public String getTitulo() { return titulo; }

        /** @return tipo de articulo */
        public String getTipo() { return tipo; }

        /** @return importe del alquiler */
        public double getPrecio() { return precio; }

        /** @return fecha de alta */
        public LocalDate getAlta() { return alta; }

        /**
         * Compara todos los campos, para poder verificar el ciclo completo.
         *
         * @param otro objeto a comparar
         * @return true si todos los campos coinciden
         */
        @Override
        public boolean equals(Object otro) {
            if (this == otro) {
                return true;
            }
            if (!(otro instanceof Articulo)) {
                return false;
            }
            Articulo a = (Articulo) otro;
            return codigo.equals(a.codigo) && titulo.equals(a.titulo) && tipo.equals(a.tipo)
                    && Double.compare(precio, a.precio) == 0 && alta.equals(a.alta);
        }

        /**
         * Si se redefine equals hay que redefinir hashCode: es el contrato de
         * Object, y sin el la clase se comporta mal dentro de un HashMap.
         *
         * @return codigo hash coherente con equals
         */
        @Override
        public int hashCode() {
            return codigo.hashCode();
        }

        @Override
        public String toString() {
            return codigo + " " + titulo + " (" + tipo + ", " + precio + " EUR, alta " + alta + ")";
        }
    }

    // ---------------------------------------------------------------------
    // Apartados 1 y 4: escritura en CSV
    // ---------------------------------------------------------------------

    /** Separador de campos del fichero CSV. */
    private static final char SEPARADOR = ',';

    /**
     * Escapa un campo para que pueda contener el separador o comillas.
     *
     * <p>Es la respuesta a la pista del enunciado: una pelicula titulada
     * "Alien, el octavo pasajero" rompe cualquier lector que se limite a partir
     * por comas. La convencion habitual, la del estandar RFC 4180, consiste en
     * rodear el campo con comillas y duplicar las comillas interiores.</p>
     *
     * @param campo valor a escapar
     * @return campo listo para escribir
     */
    private static String escapar(String campo) {
        if (campo.indexOf(SEPARADOR) >= 0 || campo.indexOf('"') >= 0) {
            return '"' + campo.replace("\"", "\"\"") + '"';
        }
        return campo;
    }

    /**
     * Parte una linea CSV respetando los campos entrecomillados.
     *
     * @param linea linea leida del fichero
     * @return campos ya desescapados
     */
    private static List<String> partir(String linea) {
        List<String> campos = new ArrayList<>();
        StringBuilder actual = new StringBuilder();
        boolean dentroDeComillas = false;
        for (int i = 0; i < linea.length(); i++) {
            char c = linea.charAt(i);
            if (dentroDeComillas) {
                if (c == '"') {
                    // Dos comillas seguidas dentro de un campo entrecomillado
                    // significan una comilla literal.
                    if (i + 1 < linea.length() && linea.charAt(i + 1) == '"') {
                        actual.append('"');
                        i++;
                    } else {
                        dentroDeComillas = false;
                    }
                } else {
                    actual.append(c);
                }
            } else if (c == '"') {
                dentroDeComillas = true;
            } else if (c == SEPARADOR) {
                campos.add(actual.toString());
                actual.setLength(0);
            } else {
                actual.append(c);
            }
        }
        campos.add(actual.toString());
        return campos;
    }

    /**
     * Apartado 1: guarda el catalogo en un fichero CSV.
     *
     * @param destino    ruta del fichero a escribir
     * @param articulos  catalogo a guardar
     * @throws IOException si no se puede escribir
     */
    public static void guardarCsv(Path destino, List<Articulo> articulos) throws IOException {
        // try-with-resources: el fichero se cierra al salir del bloque aunque
        // salte una excepcion a mitad. Sin esto, un fallo dejaria el fichero
        // abierto y, lo que es peor, con los datos a medio volcar en el bufer.
        // StandardCharsets.UTF_8 es el apartado 4.
        try (BufferedWriter salida = Files.newBufferedWriter(destino, StandardCharsets.UTF_8)) {
            salida.write("codigo,titulo,tipo,precio,alta");
            salida.newLine();
            for (Articulo a : articulos) {
                salida.write(escapar(a.getCodigo()) + SEPARADOR
                        + escapar(a.getTitulo()) + SEPARADOR
                        + escapar(a.getTipo()) + SEPARADOR
                        + a.getPrecio() + SEPARADOR
                        + a.getAlta());
                salida.newLine();
            }
        }
    }

    /**
     * Apartados 2 y 5: lee el catalogo desde un fichero CSV.
     *
     * @param origen ruta del fichero a leer
     * @return catalogo reconstruido; vacio si el fichero no existe
     * @throws IOException si hay un error de lectura distinto de la ausencia
     *                     del fichero
     */
    public static List<Articulo> leerCsv(Path origen) throws IOException {
        List<Articulo> articulos = new ArrayList<>();
        try (BufferedReader entrada = Files.newBufferedReader(origen, StandardCharsets.UTF_8)) {
            String linea = entrada.readLine();          // cabecera, se descarta
            int numero = 1;
            while ((linea = entrada.readLine()) != null) {
                numero++;
                if (linea.isBlank()) {
                    continue;
                }
                try {
                    List<String> campos = partir(linea);
                    if (campos.size() != 5) {
                        throw new IllegalArgumentException("se esperaban 5 campos y hay "
                                + campos.size());
                    }
                    articulos.add(new Articulo(campos.get(0), campos.get(1), campos.get(2),
                            Double.parseDouble(campos.get(3)), LocalDate.parse(campos.get(4))));
                } catch (RuntimeException e) {
                    // Apartado 5: una linea mala no tumba la carga entera, pero
                    // se avisa. Una linea que desaparece en silencio es peor que
                    // un fallo ruidoso.
                    System.out.println("    [aviso] linea " + numero + " descartada: "
                            + e.getMessage() + "  ->  " + linea);
                }
            }
        } catch (NoSuchFileException e) {
            // Apartado 5: primera ejecucion. No es un error.
            System.out.println("    El fichero no existe todavia: se parte de un catalogo vacio.");
        }
        return articulos;
    }

    /**
     * Apartado 6: anade una linea al final de un fichero de bitacora.
     *
     * <p>La opcion APPEND es la que evita machacar lo anterior, y CREATE la que
     * permite que la primera llamada funcione sin haber creado el fichero a
     * mano.</p>
     *
     * @param bitacora ruta del fichero de registro
     * @param mensaje  linea a registrar
     * @throws IOException si no se puede escribir
     */
    public static void registrar(Path bitacora, String mensaje) throws IOException {
        try (BufferedWriter salida = Files.newBufferedWriter(bitacora, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            salida.write(mensaje);
            salida.newLine();
        }
    }

    // ---------------------------------------------------------------------
    // Apartados 7 y 8: serializacion
    // ---------------------------------------------------------------------

    /**
     * Apartado 7: guarda el catalogo entero como objetos serializados.
     *
     * @param destino   ruta del fichero binario
     * @param articulos catalogo a guardar
     * @throws IOException si no se puede escribir
     */
    public static void guardarSerializado(Path destino, List<Articulo> articulos)
            throws IOException {
        try (ObjectOutputStream salida = new ObjectOutputStream(Files.newOutputStream(destino))) {
            // Una sola linea guarda la lista y todos los objetos que contiene,
            // con sus referencias. Eso es lo que se gana.
            salida.writeObject(new ArrayList<>(articulos));
        }
    }

    /**
     * Apartado 7: recupera el catalogo serializado.
     *
     * @param origen ruta del fichero binario
     * @return catalogo recuperado
     * @throws IOException            si no se puede leer
     * @throws ClassNotFoundException si la clase guardada ya no existe
     */
    @SuppressWarnings("unchecked")
    public static List<Articulo> leerSerializado(Path origen)
            throws IOException, ClassNotFoundException {
        try (ObjectInputStream entrada = new ObjectInputStream(Files.newInputStream(origen))) {
            // La conversion no se puede comprobar por el borrado de tipos: por
            // eso hace falta suprimir el aviso. Es una de las razones por las
            // que la serializacion no encaja bien con los genericos.
            return (List<Articulo>) entrada.readObject();
        }
    }

    /*
     * Las dos clases de abajo NO declaran serialVersionUID, y el compilador
     * avisa de ello con -Xlint:serial. Ese aviso es justamente el objeto del
     * apartado 8, asi que aqui se silencia a proposito: no es un descuido, es
     * el material del ejercicio. En cualquier otra clase del proyecto la
     * respuesta correcta al aviso es declararlo, no callarlo.
     */

    /** Apartado 8: version inicial de una clase, SIN declarar serialVersionUID. */
    @SuppressWarnings("serial")
    public static class FichaV1 implements Serializable {
        private String nombre;
        private int edad;

        /**
         * @param nombre nombre de la ficha
         * @param edad   edad registrada
         */
        public FichaV1(String nombre, int edad) {
            this.nombre = nombre;
            this.edad = edad;
        }
    }

    /** Apartado 8: la MISMA clase despues de anadirle un atributo. */
    @SuppressWarnings("serial")
    public static class FichaV2 implements Serializable {
        private String nombre;
        private int edad;
        private String telefono;      // <-- el unico cambio

        /**
         * @param nombre   nombre de la ficha
         * @param edad     edad registrada
         * @param telefono telefono de contacto
         */
        public FichaV2(String nombre, int edad, String telefono) {
            this.nombre = nombre;
            this.edad = edad;
            this.telefono = telefono;
        }
    }

    // =====================================================================
    // COMPROBACION
    // =====================================================================

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[AMP-UF2404-A3] Persistencia en ficheros");

        List<Articulo> catalogo = new ArrayList<>();
        catalogo.add(new Articulo("P001", "Casablanca", "Pelicula", 5.00, LocalDate.of(2024, 3, 1)));
        // El caso de la pista: un titulo con coma dentro.
        catalogo.add(new Articulo("P002", "Alien, el octavo pasajero", "Pelicula", 6.00,
                LocalDate.of(2024, 5, 12)));
        // Y uno con comillas, para el otro caso del escapado.
        catalogo.add(new Articulo("V001", "El \"mejor\" juego del ano", "Videojuego", 7.00,
                LocalDate.of(2025, 1, 20)));

        try {
            Path carpeta = Files.createTempDirectory("videoclub");
            Path csv = carpeta.resolve("catalogo.csv");
            Path bitacora = carpeta.resolve("videoclub.log");
            Path binario = carpeta.resolve("catalogo.dat");
            System.out.println("  Carpeta de trabajo: " + carpeta);

            // Apartado 5: leer antes de que exista nada.
            System.out.println("  Apartado 5, primera ejecucion (el fichero no existe):");
            System.out.println("    Articulos leidos: " + leerCsv(csv).size());

            // Apartados 1, 2 y 3: el ciclo completo.
            System.out.println("  Apartados 1 a 3, ciclo guardar - leer - verificar:");
            guardarCsv(csv, catalogo);
            System.out.println("    Guardados " + catalogo.size() + " articulos. Contenido del fichero:");
            for (String linea : Files.readAllLines(csv, StandardCharsets.UTF_8)) {
                System.out.println("      " + linea);
            }
            List<Articulo> recuperado = leerCsv(csv);
            System.out.println("    Leidos " + recuperado.size() + " articulos.");
            System.out.println("    Coincide exactamente con lo guardado: " + recuperado.equals(catalogo));
            System.out.println("    El titulo con coma se ha conservado: \""
                    + recuperado.get(1).getTitulo() + "\"");
            System.out.println("    Si el lector se limitara a partir por comas, ese titulo se");
            System.out.println("    habria roto en dos campos y la linea entera seria invalida.");

            // Apartado 5: linea corrupta.
            System.out.println("  Apartado 5, fichero con una linea corrupta:");
            Files.writeString(csv, Files.readString(csv, StandardCharsets.UTF_8)
                    + "P003,Titulo sin precio,Pelicula\n"
                    + "P004,Fecha invalida,Pelicula,5.00,32-13-2026\n"
                    + "P005,Correcta,Pelicula,5.00,2026-02-01\n", StandardCharsets.UTF_8);
            List<Articulo> conErrores = leerCsv(csv);
            System.out.println("    Se han recuperado " + conErrores.size()
                    + " articulos de las 6 lineas de datos.");
            System.out.println("    Las dos malas se descartan CON AVISO y las buenas se conservan.");

            // Apartado 6: bitacora.
            System.out.println("  Apartado 6, bitacora en modo anadir:");
            registrar(bitacora, "2026-08-27 10:00 Catalogo guardado con " + catalogo.size() + " articulos");
            registrar(bitacora, "2026-08-27 10:05 Alquilado P001");
            registrar(bitacora, "2026-08-27 10:30 Devuelto P001");
            System.out.println("    Lineas en la bitacora tras tres llamadas: "
                    + Files.readAllLines(bitacora, StandardCharsets.UTF_8).size());
            System.out.println("    Sin StandardOpenOption.APPEND, cada llamada habria dejado el");
            System.out.println("    fichero con una sola linea, borrando el historial anterior.");

            // Apartado 7: serializacion.
            System.out.println("  Apartado 7, serializacion de objetos:");
            guardarSerializado(binario, catalogo);
            List<Articulo> deserializado = leerSerializado(binario);
            System.out.println("    Recuperados " + deserializado.size()
                    + " articulos, iguales a los originales: " + deserializado.equals(catalogo));
            System.out.println("    Tamano del CSV:    " + Files.size(csv) + " bytes (legible)");
            System.out.println("    Tamano del binario: " + Files.size(binario) + " bytes (ilegible)");

            // Apartado 8: el identificador de version.
            System.out.println("  Apartado 8, el identificador de version de serializacion:");
            long v1 = ObjectStreamClass.lookup(FichaV1.class).getSerialVersionUID();
            long v2 = ObjectStreamClass.lookup(FichaV2.class).getSerialVersionUID();
            System.out.println("    FichaV1 (nombre, edad):            " + v1);
            System.out.println("    FichaV2 (nombre, edad, telefono):  " + v2);
            System.out.println("    Son distintos: " + (v1 != v2));
            System.out.println("    Ninguna de las dos lo declara, asi que Java lo CALCULA a partir");
            System.out.println("    de la firma de la clase. Anadir un solo atributo lo cambia, y a");
            System.out.println("    partir de ese momento los ficheros guardados con la version");
            System.out.println("    anterior dejan de poder leerse: InvalidClassException.");
            System.out.println("    Por eso se declara a mano:  private static final long");
            System.out.println("    serialVersionUID = 1L;  Asi el programador decide cuando una");
            System.out.println("    version es incompatible, en lugar de decidirlo el compilador");
            System.out.println("    sin avisar. Es la razon por la que todas las excepciones");
            System.out.println("    propias de este proyecto lo llevan.");

        } catch (IOException e) {
            System.out.println("  No se pudo trabajar con los ficheros: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("  La clase serializada ya no existe: " + e.getMessage());
        }
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
