package com.ifcd0112.ejercicios.uf2405.bloque3_jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * UF2405 - BLOQUE 3 - EJERCICIO 5: Conexion, consulta y gestion de errores.
 *
 * <p>Criterios de evaluacion: CE2.1, CE2.4</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej05Conexion {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Escribe una aplicacion Java de consola que se conecte a la base de datos
     * de la clinica y muestre por pantalla el listado completo de mascotas con
     * su especie y el nombre de su dueno.
     *
     * Se pide:
     *   1. Realiza la conexion mediante DriverManager, incluyendo el driver
     *      correspondiente en el proyecto.
     *   2. Utiliza try-with-resources para garantizar el cierre automatico de la
     *      conexion, la sentencia y el ResultSet, aunque se produzca una
     *      excepcion.
     *   3. Recorre el ResultSet mostrando los datos con un formato tabular
     *      alineado.
     *   4. Captura la excepcion SQLException e imprime, ademas del mensaje, el
     *      codigo de error del SGBD y el estado SQL estandarizado.
     *   5. Provoca deliberadamente tres errores distintos (contrasena
     *      incorrecta, nombre de tabla inexistente y puerto equivocado) y anota
     *      que estado SQL devuelve cada uno.
     *   6. Explica en un comentario por que try-with-resources resulta
     *      preferible a cerrar la conexion manualmente en un bloque finally.
     *
     * Pista: el driver de MySQL debe anadirse al proyecto como dependencia o
     * como archivo JAR en el classpath; si no, obtendras un error indicando que
     * no se encuentra un driver adecuado para la URL.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * COMO EJECUTAR ESTE EJERCICIO DE VERDAD.
     *
     * El codigo de abajo es el que se entrega como solucion y COMPILA tal cual,
     * porque el paquete java.sql forma parte del JDK. Lo que no forma parte del
     * JDK es el driver de MySQL, y este proyecto no lleva ninguna dependencia
     * externa a proposito.
     *
     * Para ejecutarlo contra una base de datos real:
     *   1. Descargar mysql-connector-j-<version>.jar
     *   2. Crear la base de datos con recursos/uf2405/sql/01_esquema_clinica.sql
     *   3. Compilar y ejecutar poniendo el jar en el classpath:
     *
     *      javac -cp mysql-connector-j.jar -d out $(find src -name "*.java")
     *      java  -cp out:mysql-connector-j.jar com.ifcd0112.ejercicios.uf2405.bloque3_jdbc.Ej05Conexion
     *
     * Sin el jar, DriverManager lanza SQLException con el estado 08001. Eso no
     * es un fallo del ejercicio: es exactamente lo que describe la pista, y el
     * metodo resolver() lo aprovecha para demostrar el apartado 4 con una
     * excepcion de verdad en lugar de una inventada.
     */

    /** Direccion de la base de datos de la clinica. */
    private static final String URL = "jdbc:mysql://localhost:3306/clinica";

    /** Usuario de la base de datos. */
    private static final String USER = "root";

    /** Contrasena de la base de datos. */
    private static final String PASS = "clave";

    /** Consulta del listado que pide el enunciado. */
    private static final String SQL =
            "SELECT m.id_mascota, m.nombre, m.especie, c.nombre AS dueno "
            + "FROM mascota m JOIN cliente c ON m.id_cliente = c.id_cliente "
            + "ORDER BY m.nombre";

    /**
     * Apartados 1, 2 y 3: conecta, consulta y muestra el listado.
     *
     * <p>El try-with-resources abre conexion, sentencia y ResultSet, y los
     * cierra en orden inverso al de apertura pase lo que pase. Con un finally
     * manual habria que anidar tres comprobaciones de nulo, cada una con su
     * propio try, porque close() tambien puede lanzar SQLException.</p>
     *
     * @return numero de filas mostradas
     * @throws SQLException si falla la conexion o la consulta
     */
    public static int listarMascotas() throws SQLException {
        int filas = 0;
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            System.out.printf("    %-5s %-12s %-11s %s%n", "ID", "NOMBRE", "ESPECIE", "DUENO");
            System.out.println("    ----- ------------ ----------- -----------------");
            while (rs.next()) {
                System.out.printf("    %-5d %-12s %-11s %s%n",
                        rs.getInt("id_mascota"), rs.getString("nombre"),
                        rs.getString("especie"), rs.getString("dueno"));
                filas++;
            }
        }
        return filas;
    }

    /*
     * APARTADO 4: los estados SQL que conviene conocer.
     *
     * getMessage()   texto del fabricante. Util para el log, pero cambia entre
     *                versiones y entre gestores: no se debe programar sobre el.
     * getErrorCode() codigo NUMERICO propio del SGBD. En MySQL, 1045 es acceso
     *                denegado y 1146 tabla inexistente. Es especifico del
     *                fabricante.
     * getSQLState()  codigo de CINCO caracteres del estandar SQL, el mismo en
     *                todos los gestores. Es el que hay que mirar si se quiere
     *                reaccionar de forma distinta segun el tipo de error.
     *
     *      08001  no se puede conectar: host o puerto erroneos, SGBD apagado,
     *             o, como aqui, no hay driver para esa URL
     *      28000  usuario o contrasena incorrectos
     *      42S02  la tabla no existe
     *      23000  violacion de una restriccion de integridad
     */

    /**
     * Apartado 5: los tres intentos de conexion que pide el enunciado, cada uno
     * con un fallo distinto provocado a proposito.
     *
     * <p>Cada elemento es {caso, url, usuario, contrasena}.</p>
     */
    private static final String[][] CASOS_DE_ERROR = {
        { "Contrasena incorrecta",  URL, USER, "clave_que_no_es" },
        { "Tabla inexistente",      URL, USER, PASS },
        { "Puerto equivocado",      "jdbc:mysql://localhost:3399/clinica", USER, PASS }
    };

    /**
     * Consulta contra una tabla que no existe, para el segundo caso del
     * apartado 5.
     */
    private static final String SQL_TABLA_INEXISTENTE =
            "SELECT * FROM mascotas_que_no_existen";

    /**
     * Apartado 5: lanza los tres casos de error y devuelve el SQLState que
     * responde el sistema en cada uno.
     *
     * <p>Los estados se leen en tiempo de ejecucion, no estan escritos a mano:
     * lo que imprime este metodo es lo que realmente ha devuelto el driver en
     * esta maquina.</p>
     *
     * @return una linea por caso, con el estado SQL obtenido
     */
    public static String[] provocarErrores() {
        String[] resultado = new String[CASOS_DE_ERROR.length];
        for (int i = 0; i < CASOS_DE_ERROR.length; i++) {
            String caso = CASOS_DE_ERROR[i][0];
            String url = CASOS_DE_ERROR[i][1];
            String usuario = CASOS_DE_ERROR[i][2];
            String pass = CASOS_DE_ERROR[i][3];
            String consulta = "Tabla inexistente".equals(caso) ? SQL_TABLA_INEXISTENTE : SQL;
            try (Connection con = DriverManager.getConnection(url, usuario, pass);
                 Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(consulta)) {
                rs.next();
                resultado[i] = String.format("%-24s sin error (revisar el montaje del caso)", caso);
            } catch (SQLException e) {
                resultado[i] = String.format("%-24s SQLState %-6s codigo %-6d %s",
                        caso, e.getSQLState(), e.getErrorCode(), e.getMessage());
            }
        }
        return resultado;
    }

    /*
     * APARTADO 5: los estados que devuelve cada caso con el driver instalado.
     *
     * Sin el driver de MySQL en el classpath los tres casos fallan antes de
     * llegar al servidor y los tres devuelven 08001, porque DriverManager ni
     * siquiera encuentra a quien pasarle la URL. Con el driver instalado y la
     * base de datos cargada, cada uno se distingue del resto:
     *
     *   Contrasena incorrecta  ->  SQLState 28000, codigo 1045
     *                              "Access denied for user 'root'@'localhost'"
     *   Tabla inexistente      ->  SQLState 42S02, codigo 1146
     *                              "Table 'clinica.mascotas_que_no_existen' doesn't exist"
     *   Puerto equivocado      ->  SQLState 08001, codigo 0
     *                              "Communications link failure"
     *
     * La conclusion practica del apartado: el primer y el tercer caso son fallos
     * de conexion, pero solo el tercero se puede reintentar (el servidor podria
     * estar arrancando); el segundo es un error del programa, no del entorno, y
     * reintentarlo no arregla nada. Por eso conviene ramificar por getSQLState()
     * y no por el texto del mensaje.
     */

    /*
     * APARTADO 6: por que try-with-resources y no finally.
     *
     * 1. El finally manual no cierra nada por si solo: hay que escribir tres
     *    bloques anidados, cada uno con su comprobacion de nulo, porque si la
     *    conexion fallo la sentencia y el ResultSet valen null.
     * 2. close() tambien puede lanzar SQLException, asi que cada cierre necesita
     *    su propio try/catch dentro del finally. Son unas quince lineas de
     *    fontaneria por cada consulta.
     * 3. Si el cuerpo del try lanza una excepcion y ademas falla el cierre, el
     *    finally manual PIERDE la primera: la del cierre la sustituye y el error
     *    que de verdad importaba desaparece. try-with-resources conserva la
     *    original y engancha la del cierre como "suprimida", recuperable con
     *    getSuppressed().
     * 4. El cierre ocurre en orden inverso al de apertura (ResultSet, luego
     *    Statement, luego Connection), que es el correcto, y ocurre siempre,
     *    tambien cuando se sale del try con un return.
     *
     * En resumen: menos codigo, imposible olvidarse un close(), y no se pierde
     * la excepcion original.
     */

    /**
     * Datos de ejemplo, para poder ensenar la salida esperada aunque no haya
     * base de datos. Son los mismos que carga el script del ejercicio 1.
     */
    private static final String[][] DATOS_EJEMPLO = {
        { "5", "Kiwi",  "Ave",   "Marta Ruiz" },
        { "2", "Misu",  "Gato",  "Ana Lopez"  },
        { "4", "Nube",  "Gato",  "Marta Ruiz" },
        { "3", "Rocky", "Perro", "Luis Gomez" },
        { "1", "Toby",  "Perro", "Ana Lopez"  }
    };

    // =====================================================================
    // COMPROBACION
    // =====================================================================

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2405-E05] Conexion, consulta y gestion de errores");

        try {
            int filas = listarMascotas();
            System.out.println("    " + filas + " mascotas listadas desde la base de datos real");
        } catch (SQLException e) {
            // Apartado 4: esto es lo que pide el enunciado, y aqui esta pasando
            // de verdad. No es una excepcion simulada.
            System.out.println("  No hay conexion con la base de datos. Datos de la excepcion:");
            System.out.println("    Mensaje : " + e.getMessage());
            System.out.println("    Codigo  : " + e.getErrorCode() + "   (codigo propio del SGBD)");
            System.out.println("    SQLState: " + e.getSQLState() + "   (estandar: no se puede conectar)");
            System.out.println("  Es el caso que anuncia la pista: falta el driver en el classpath.");
            System.out.println("  Salida que produciria la consulta con la base de datos cargada:");
            System.out.printf("    %-5s %-12s %-11s %s%n", "ID", "NOMBRE", "ESPECIE", "DUENO");
            System.out.println("    ----- ------------ ----------- -----------------");
            for (String[] fila : DATOS_EJEMPLO) {
                System.out.printf("    %-5s %-12s %-11s %s%n", fila[0], fila[1], fila[2], fila[3]);
            }
        }

        // Apartado 5: los tres errores provocados a proposito.
        System.out.println("  Apartado 5: tres errores provocados, estado SQL de cada uno:");
        for (String linea : provocarErrores()) {
            System.out.println("    " + linea);
        }
        System.out.println("  (sin el driver los tres dan 08001; con el driver instalado se");
        System.out.println("   distinguen: 28000, 42S02 y 08001. Ver el comentario del apartado 5.)");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
