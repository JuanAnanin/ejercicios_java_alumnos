package com.ifcd0112.ejercicios.ampliacion.uf2405;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * AMPLIACION UF2405 - EJERCICIO A6: Tecnologias de acceso a datos.
 *
 * <p>Criterios de evaluacion: CE2.2, CE2.1, CE1.3</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class A06TecnologiasAccesoDatos {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * En el bloque 3 has usado JDBC con DriverManager porque era lo mas
     * sencillo. En un proyecto real hay que ELEGIR la tecnologia de acceso a
     * datos, y esa eleccion tiene consecuencias que se pagan durante anos.
     *
     * Se pide:
     *   1. Enumera las tecnologias habituales de conexion y acceso a datos desde
     *      una aplicacion Java, indicando de cada una que problema resuelve:
     *      DriverManager, DataSource con pool, JPA/Hibernate y los llamados
     *      mapeadores ligeros.
     *   2. Implementa tres estrategias de acceso con el MISMO contrato, de forma
     *      que se puedan intercambiar sin tocar el resto del programa: una que
     *      abra conexion en cada operacion, otra que reutilice una del pool y
     *      otra que ademas convierta automaticamente las filas en objetos.
     *   3. Cronometra las tres haciendo el mismo trabajo y comenta los
     *      resultados. Indica que parte del coste es de la conexion y cual del
     *      mapeo.
     *   4. Cuenta las lineas de codigo que hace falta escribir con cada una para
     *      leer una entidad de cinco columnas, y compara ese dato con el
     *      anterior. Explica por que la tecnologia mas rapida no siempre es la
     *      que conviene.
     *   5. Elabora una tabla de decision que indique, para cuatro escenarios
     *      concretos, cual elegirias y por que: una aplicacion de escritorio de
     *      un solo usuario, una web con 200 usuarios simultaneos, un proceso
     *      nocturno que carga un millon de filas y un microservicio con dos
     *      tablas.
     *   6. Explica que aporta un DataSource frente a DriverManager mas alla del
     *      rendimiento.
     *   7. Compara brevemente el modelo de acceso a datos de la plataforma Java
     *      con el de .NET, senalando los elementos equivalentes.
     *
     * Pista: la pregunta correcta no es cual es la tecnologia mejor, sino cual
     * es el problema dominante en este proyecto. Si el cuello de botella es el
     * numero de conexiones, la respuesta es un pool; si es la cantidad de codigo
     * repetitivo, un mapeador; y si es el control fino de la consulta, JDBC.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * APARTADO 1: que problema resuelve cada una.
     *
     *   DriverManager
     *     La forma basica. Abre una conexion nueva cada vez que se le pide.
     *     Resuelve el problema de "quiero hablar con la base de datos" y ninguno
     *     mas. Es lo correcto para un programa de consola o una prueba.
     *
     *   DataSource con pool (HikariCP, el de Tomcat, el del servidor)
     *     Resuelve el coste de abrir conexiones y, sobre todo, el de NO poder
     *     limitarlas. Mantiene unas cuantas abiertas y las presta. Es lo que
     *     debe usar cualquier aplicacion con varios usuarios.
     *
     *   JPA / Hibernate
     *     Resuelve el codigo repetitivo: el mapeo de filas a objetos, las
     *     consultas basicas, las relaciones entre entidades y la gestion del
     *     ciclo de vida. A cambio anade una capa gruesa que hay que aprender y
     *     que genera SQL que uno no ha escrito.
     *
     *   Mapeadores ligeros (MyBatis, JDBI, Spring JdbcTemplate)
     *     El termino medio: tu escribes el SQL, ellos hacen el mapeo y la
     *     gestion de recursos. Es la opcion que mas se elige cuando el equipo
     *     quiere controlar las consultas sin escribir cien lineas de fontaneria.
     *
     * APARTADO 6: que aporta un DataSource ademas del rendimiento.
     *
     *   - Configuracion FUERA del codigo. La URL, el usuario y la contrasena
     *     dejan de estar compilados y pasan al servidor o a un fichero. Cambiar
     *     de entorno no obliga a recompilar, y las credenciales dejan de estar
     *     en el control de versiones, que es el motivo mas serio.
     *   - Un techo de conexiones. Sin el, un pico de trafico abre tantas como
     *     peticiones haya y tumba el servidor de base de datos.
     *   - Comprobacion de salud. Detecta conexiones muertas antes de
     *     entregarlas, cosa que DriverManager no puede hacer porque no las
     *     conserva.
     *   - Es la puerta a las transacciones distribuidas y a la gestion por parte
     *     del contenedor.
     *
     * APARTADO 7: Java frente a .NET.
     *
     *   JDBC (java.sql)            <->  ADO.NET (System.Data)
     *   Connection                 <->  SqlConnection
     *   PreparedStatement          <->  SqlCommand con parametros
     *   ResultSet                  <->  SqlDataReader
     *   DataSource con pool        <->  pool integrado en la cadena de conexion
     *   JPA / Hibernate            <->  Entity Framework
     *   JPQL                       <->  LINQ to Entities
     *
     * La arquitectura es la misma y hasta los nombres se parecen: driver,
     * conexion, sentencia parametrizada y cursor de resultados. Lo que cambia es
     * que en .NET el pool viene activado por defecto en la propia cadena de
     * conexion, mientras que en Java hay que anadirlo. Conviene decirlo en clase
     * porque explica por que en tutoriales de .NET no se habla de pools: no es
     * que no los usen, es que ya los estan usando sin saberlo.
     *
     * APARTADO 4: por que la mas rapida no siempre conviene.
     *
     * Porque el tiempo de maquina es barato y el de las personas no. Una
     * diferencia de dos milisegundos por consulta es irrelevante en una
     * aplicacion de gestion, y en cambio escribir y mantener cuarenta lineas de
     * mapeo por entidad, multiplicadas por quince entidades, son semanas de
     * trabajo y un sitio mas donde equivocarse.
     *
     * La excepcion es el proceso masivo: cuando se cargan un millon de filas,
     * esos dos milisegundos se convierten en media hora, y ahi si manda el
     * rendimiento.
     */

    // ---------------------------------------------------------------------
    // Apartado 2: el contrato comun
    // ---------------------------------------------------------------------

    /** Mascota, la entidad de la clinica del bloque 3. */
    public static class Mascota {

        private int idMascota;
        private String nombre;
        private String especie;
        private double peso;
        private int idCliente;

        /** Constructor vacio: lo necesita el mapeo automatico. */
        public Mascota() {
        }

        /** @return identificador */
        public int getIdMascota() { return idMascota; }

        /** @return nombre de la mascota */
        public String getNombre() { return nombre; }

        /** @return especie */
        public String getEspecie() { return especie; }

        /** @return peso en kilos */
        public double getPeso() { return peso; }

        /** @return identificador del dueno */
        public int getIdCliente() { return idCliente; }

        @Override
        public String toString() {
            return idMascota + " " + nombre + " (" + especie + ", " + peso + " kg)";
        }
    }

    /**
     * Contrato comun a las tres estrategias.
     *
     * <p>Que las tres implementen la misma interfaz es lo que permite el
     * apartado 2: se pueden intercambiar sin tocar el resto del programa. Es la
     * misma idea del DAO del ejercicio 7, aplicada un nivel mas abajo.</p>
     */
    public interface AccesoDatos {

        /**
         * @return todas las mascotas
         */
        List<Mascota> listarMascotas();

        /**
         * @return nombre de la estrategia
         */
        String nombre();

        /**
         * @return lineas de codigo que exige escribir por entidad
         */
        int lineasPorEntidad();
    }

    /** Base de datos simulada, con el coste de cada operacion. */
    public static class BaseDatosSimulada {

        /** Coste de abrir una conexion nueva, en microsegundos. */
        public static final long COSTE_CONEXION_US = 400;

        /** Coste de ejecutar una consulta con la conexion ya abierta. */
        public static final long COSTE_CONSULTA_US = 30;

        private static final String[][] FILAS = {
            { "1", "Toby",  "Perro", "22.5", "1" },
            { "2", "Misu",  "Gato",  "4.2",  "1" },
            { "3", "Rocky", "Perro", "18.0", "2" },
            { "4", "Nube",  "Gato",  "3.8",  "3" },
            { "5", "Kiwi",  "Ave",   "0.3",  "3" }
        };

        private static final String[] COLUMNAS =
            { "idMascota", "nombre", "especie", "peso", "idCliente" };

        /** Simula el tiempo que cuesta abrir una conexion. */
        public static void abrirConexion() {
            esperar(COSTE_CONEXION_US);
        }

        /**
         * Simula una consulta y devuelve las filas como pares columna-valor.
         *
         * @return filas en bruto, como las daria un ResultSet
         */
        public static List<Map<String, String>> consultar() {
            esperar(COSTE_CONSULTA_US);
            List<Map<String, String>> filas = new ArrayList<>();
            for (String[] datos : FILAS) {
                Map<String, String> fila = new LinkedHashMap<>();
                for (int i = 0; i < COLUMNAS.length; i++) {
                    fila.put(COLUMNAS[i], datos[i]);
                }
                filas.add(fila);
            }
            return filas;
        }

        private static void esperar(long microsegundos) {
            long fin = System.nanoTime() + microsegundos * 1000;
            while (System.nanoTime() < fin) {
                // Espera activa a proposito: dormir tiene una granularidad de
                // milisegundos y aqui se miden microsegundos.
                Thread.onSpinWait();
            }
        }
    }

    /** Estrategia 1: abre una conexion en cada operacion. */
    public static class AccesoDirecto implements AccesoDatos {

        @Override
        public List<Mascota> listarMascotas() {
            BaseDatosSimulada.abrirConexion();          // se paga SIEMPRE
            List<Mascota> mascotas = new ArrayList<>();
            for (Map<String, String> fila : BaseDatosSimulada.consultar()) {
                // Mapeo a mano: una linea por columna, y hay que acordarse del
                // tipo de cada una. Esto es lo que se repite en cada entidad.
                Mascota m = new Mascota();
                m.idMascota = Integer.parseInt(fila.get("idMascota"));
                m.nombre = fila.get("nombre");
                m.especie = fila.get("especie");
                m.peso = Double.parseDouble(fila.get("peso"));
                m.idCliente = Integer.parseInt(fila.get("idCliente"));
                mascotas.add(m);
            }
            return mascotas;
        }

        @Override
        public String nombre() { return "DriverManager (conexion por operacion)"; }

        @Override
        public int lineasPorEntidad() { return 12; }
    }

    /** Estrategia 2: reutiliza una conexion del pool. */
    public static class AccesoConPool implements AccesoDatos {

        private boolean conexionLista;

        @Override
        public List<Mascota> listarMascotas() {
            if (!conexionLista) {
                // El pool paga el coste UNA vez, al arrancar la aplicacion.
                BaseDatosSimulada.abrirConexion();
                conexionLista = true;
            }
            List<Mascota> mascotas = new ArrayList<>();
            for (Map<String, String> fila : BaseDatosSimulada.consultar()) {
                Mascota m = new Mascota();
                m.idMascota = Integer.parseInt(fila.get("idMascota"));
                m.nombre = fila.get("nombre");
                m.especie = fila.get("especie");
                m.peso = Double.parseDouble(fila.get("peso"));
                m.idCliente = Integer.parseInt(fila.get("idCliente"));
                mascotas.add(m);
            }
            return mascotas;
        }

        @Override
        public String nombre() { return "DataSource con pool"; }

        @Override
        public int lineasPorEntidad() { return 12; }
    }

    /** Estrategia 3: pool y ademas mapeo automatico de filas a objetos. */
    public static class AccesoConMapeo implements AccesoDatos {

        private boolean conexionLista;

        @Override
        public List<Mascota> listarMascotas() {
            if (!conexionLista) {
                BaseDatosSimulada.abrirConexion();
                conexionLista = true;
            }
            List<Mascota> mascotas = new ArrayList<>();
            for (Map<String, String> fila : BaseDatosSimulada.consultar()) {
                mascotas.add(mapear(fila, Mascota.class));
            }
            return mascotas;
        }

        /**
         * Convierte una fila en un objeto emparejando nombres de columna con
         * nombres de atributo.
         *
         * <p>Es, en pequeno, lo que hace un ORM: mirar la clase por reflexion y
         * rellenarla sola. Se escribe UNA vez y sirve para todas las
         * entidades.</p>
         *
         * @param fila  valores de la fila
         * @param clase clase destino
         * @param <T>   tipo de la entidad
         * @return el objeto ya relleno
         */
        private <T> T mapear(Map<String, String> fila, Class<T> clase) {
            try {
                T objeto = clase.getDeclaredConstructor().newInstance();
                for (Field f : clase.getDeclaredFields()) {
                    String valor = fila.get(f.getName());
                    if (valor == null) {
                        continue;
                    }
                    f.setAccessible(true);
                    if (f.getType() == int.class) {
                        f.setInt(objeto, Integer.parseInt(valor));
                    } else if (f.getType() == double.class) {
                        f.setDouble(objeto, Double.parseDouble(valor));
                    } else {
                        f.set(objeto, valor);
                    }
                }
                return objeto;
            } catch (ReflectiveOperationException e) {
                // Aqui esta el precio del mapeo automatico: los errores que el
                // compilador detectaba antes pasan a detectarse en ejecucion.
                throw new IllegalStateException("No se pudo mapear " + clase.getSimpleName(), e);
            }
        }

        @Override
        public String nombre() { return "Pool + mapeo automatico (estilo ORM)"; }

        @Override
        public int lineasPorEntidad() { return 0; }
    }

    // =====================================================================
    // COMPROBACION
    // =====================================================================

    /**
     * Apartado 3: cronometra una estrategia haciendo el mismo trabajo.
     *
     * @param acceso       estrategia a medir
     * @param repeticiones consultas a realizar
     * @return milisegundos empleados
     */
    public static long cronometrar(AccesoDatos acceso, int repeticiones) {
        long inicio = System.nanoTime();
        for (int i = 0; i < repeticiones; i++) {
            acceso.listarMascotas();
        }
        return (System.nanoTime() - inicio) / 1_000_000;
    }

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[AMP-UF2405-A6] Tecnologias de acceso a datos");

        AccesoDatos[] estrategias = {
            new AccesoDirecto(), new AccesoConPool(), new AccesoConMapeo()
        };

        System.out.println("  Apartado 2, las tres estrategias devuelven lo mismo:");
        for (AccesoDatos a : estrategias) {
            List<Mascota> m = a.listarMascotas();
            System.out.printf("    %-38s %d mascotas, la primera: %s%n",
                    a.nombre(), m.size(), m.get(0));
        }
        System.out.println("    Se pueden intercambiar sin tocar nada mas porque comparten");
        System.out.println("    interfaz. Es la misma idea del DAO, un nivel mas abajo.");

        final int repeticiones = 200;
        System.out.println("  Apartados 3 y 4, " + repeticiones + " consultas con cada una:");
        System.out.printf("    %-38s %8s %10s%n", "estrategia", "tiempo", "lineas/entidad");
        for (AccesoDatos a : estrategias) {
            long ms = cronometrar(a, repeticiones);
            System.out.printf("    %-38s %6d ms %8d%n", a.nombre(), ms, a.lineasPorEntidad());
        }
        System.out.println("    El reparto del coste: abrir conexion "
                + BaseDatosSimulada.COSTE_CONEXION_US + " microsegundos frente a "
                + BaseDatosSimulada.COSTE_CONSULTA_US + " de la consulta.");
        System.out.println("    Es decir, mas del 90 por ciento del tiempo de la primera");
        System.out.println("    estrategia se va en abrir y cerrar, no en consultar. Ese es");
        System.out.println("    exactamente el problema que resuelve un pool.");
        System.out.println("    Ahora la ultima columna, que es la otra mitad del ejercicio: la");
        System.out.println("    tercera estrategia ahorra 12 lineas POR ENTIDAD. Con quince");
        System.out.println("    entidades son 180 lineas de fontaneria que nadie escribe ni");
        System.out.println("    mantiene. Pero NO sale gratis: la reflexion la hace visiblemente");
        System.out.println("    mas lenta que el mapeo escrito a mano, y por eso en la tabla de");
        System.out.println("    decision el proceso nocturno de un millon de filas no la elige.");
        System.out.println("    Ese es el compromiso real: se cambia tiempo de maquina por tiempo");
        System.out.println("    de personas, y la eleccion depende de cual de los dos escasea.");

        System.out.println("  Apartado 5, tabla de decision:");
        String[][] decision = {
            { "Escritorio, 1 usuario",       "DriverManager",
              "no hay concurrencia; el pool anade complejidad sin aportar nada" },
            { "Web, 200 usuarios",           "DataSource con pool",
              "el coste de conexion domina y hace falta un techo de conexiones" },
            { "Proceso nocturno, 1M filas",  "JDBC puro con lotes",
              "aqui SI manda el rendimiento; un ORM por fila seria inasumible" },
            { "Microservicio, 2 tablas",     "Mapeador ligero",
              "poco modelo, control del SQL y nada de fontaneria" }
        };
        for (String[] d : decision) {
            System.out.printf("    %-28s -> %-22s %s%n", d[0], d[1], d[2]);
        }
        System.out.println("    No hay una tecnologia mejor: hay un problema dominante en cada");
        System.out.println("    proyecto, y la eleccion consiste en identificarlo.");

        System.out.println("  Apartado 6: un DataSource ademas saca las credenciales del codigo,");
        System.out.println("    pone techo al numero de conexiones y comprueba que siguen vivas.");
        System.out.println("  Apartado 7: en .NET el mapa es el mismo. Connection/SqlConnection,");
        System.out.println("    PreparedStatement/SqlCommand, ResultSet/SqlDataReader, Hibernate/");
        System.out.println("    Entity Framework. La diferencia practica es que alli el pool viene");
        System.out.println("    activado por defecto en la cadena de conexion.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
