package com.ifcd0112.ejercicios.uf2405.bloque3_jdbc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * UF2405 - BLOQUE 3 - EJERCICIO 6: Inyeccion SQL, demostracion y correccion.
 *
 * <p>Criterios de evaluacion: CE2.3</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej06InyeccionSql {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Este ejercicio tiene como objetivo que compruebes de primera mano el
     * riesgo de construir consultas concatenando texto.
     *
     * Se pide:
     *   1. Escribe un metodo buscarClientePorNombre(String nombre) que
     *      construya la consulta concatenando directamente el parametro
     *      recibido, utilizando un objeto Statement.
     *   2. Invocalo con un nombre normal y comprueba que funciona correctamente.
     *   3. Invocalo ahora pasandole como parametro el texto: ' OR '1'='1 y
     *      observa que devuelve. Explica por escrito exactamente por que
     *      devuelve ese resultado, mostrando como queda la consulta SQL final.
     *   4. Reescribe el metodo utilizando PreparedStatement con un parametro (?)
     *      y vuelve a invocarlo con el mismo texto malicioso.
     *   5. Explica por que la segunda version no es vulnerable, es decir, que
     *      hace exactamente el driver con el valor del parametro.
     *   6. AMPLIACION: investiga que otro tipo de ataque permitiria un parametro
     *      que contuviese un punto y coma seguido de una sentencia DROP TABLE.
     *
     * Pista: para entender el ataque, imprime por consola la cadena SQL completa
     * que resulta de la concatenacion antes de ejecutarla: veras que la
     * condicion del WHERE se convierte en una expresion siempre verdadera.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * SOBRE ESTA IMPLEMENTACION.
     *
     * El codigo JDBC que se entrega como solucion es el de los dos metodos
     * comentados mas abajo, buscarVulnerable y buscarSeguro. Para poder
     * DEMOSTRAR el ataque sin depender de un servidor MySQL, este fichero
     * incluye ademas un motor SQL minimo que entiende exactamente la clase de
     * WHERE que interviene en el ataque: comparaciones de igualdad unidas por
     * OR. No es un gestor de bases de datos ni pretende serlo; es lo justo para
     * que se vea, ejecutandolo, que la consulta manipulada devuelve todas las
     * filas y la parametrizada no.
     *
     * CODIGO JDBC DE LA SOLUCION (apartado 1, version vulnerable):
     *
     *      public List<Cliente> buscarVulnerable(String nombre) throws SQLException {
     *          String sql = "SELECT * FROM cliente WHERE nombre = '" + nombre + "'";
     *          System.out.println("SQL generada: " + sql);   // para ver el ataque
     *          Statement stmt = con.createStatement();
     *          ResultSet rs = stmt.executeQuery(sql);
     *          return mapear(rs);
     *      }
     *
     * CODIGO JDBC DE LA SOLUCION (apartado 4, version segura):
     *
     *      public List<Cliente> buscarSeguro(String nombre) throws SQLException {
     *          String sql = "SELECT * FROM cliente WHERE nombre = ?";
     *          try (PreparedStatement ps = con.prepareStatement(sql)) {
     *              ps.setString(1, nombre);          // el driver trata el valor como dato
     *              try (ResultSet rs = ps.executeQuery()) {
     *                  return mapear(rs);
     *              }
     *          }
     *      }
     */

    /** Tabla cliente en memoria: id, nombre, telefono, email. */
    private static final String[][] TABLA_CLIENTE = {
        { "1", "Ana Lopez",  "600111222", "ana.lopez@correo.es"  },
        { "2", "Luis Gomez", "600333444", "luis.gomez@correo.es" },
        { "3", "Marta Ruiz", "600555666", "marta.ruiz@correo.es" }
    };

    /** Nombres de las columnas, en el mismo orden que TABLA_CLIENTE. */
    private static final String[] COLUMNAS = { "id_cliente", "nombre", "telefono", "email" };

    /**
     * Motor SQL minimo, solo para esta demostracion.
     *
     * <p>Entiende sentencias de la forma
     * SELECT * FROM cliente WHERE cond [OR cond]..., donde cada condicion es
     * una igualdad entre dos operandos y cada operando es o bien un literal
     * entre comillas simples o bien un nombre de columna. Es exactamente la
     * gramatica que hace falta para reproducir el ataque, y ni una regla
     * mas.</p>
     */
    public static class MotorMinimo {

        /**
         * Ejecuta una sentencia SELECT sobre la tabla cliente.
         *
         * @param sql sentencia completa, ya construida
         * @return filas que cumplen la condicion
         */
        public List<String[]> ejecutar(String sql) {
            int donde = sql.toUpperCase().indexOf("WHERE");
            List<String[]> resultado = new ArrayList<>();
            if (donde < 0) {
                // Sin WHERE, un SELECT devuelve la tabla entera.
                resultado.addAll(Arrays.asList(TABLA_CLIENTE));
                return resultado;
            }
            String condicion = sql.substring(donde + "WHERE".length()).trim();
            for (String[] fila : TABLA_CLIENTE) {
                if (evaluarOr(condicion, fila)) {
                    resultado.add(fila);
                }
            }
            return resultado;
        }

        /**
         * Evalua una cadena de condiciones unidas por OR.
         *
         * @param condicion condicion completa
         * @param fila      fila sobre la que se evalua
         * @return true si alguna de las condiciones se cumple
         */
        private boolean evaluarOr(String condicion, String[] fila) {
            for (String termino : condicion.split("(?i)\\s+OR\\s+")) {
                if (evaluarIgualdad(termino, fila)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Evalua una igualdad entre dos operandos.
         *
         * @param termino igualdad de la forma "a = b"
         * @param fila    fila sobre la que se evalua
         * @return true si los dos operandos valen lo mismo
         */
        private boolean evaluarIgualdad(String termino, String[] fila) {
            String[] lados = termino.split("=", 2);
            if (lados.length != 2) {
                return false;
            }
            String izq = resolver(lados[0].trim(), fila);
            String der = resolver(lados[1].trim(), fila);
            return izq != null && izq.equals(der);
        }

        /**
         * Resuelve un operando: literal entre comillas o nombre de columna.
         *
         * @param operando texto del operando
         * @param fila     fila actual
         * @return el valor del operando
         */
        private String resolver(String operando, String[] fila) {
            if (operando.length() >= 2 && operando.startsWith("'") && operando.endsWith("'")) {
                return operando.substring(1, operando.length() - 1);   // literal
            }
            for (int i = 0; i < COLUMNAS.length; i++) {
                if (COLUMNAS[i].equalsIgnoreCase(operando)) {
                    return fila[i];                                    // columna
                }
            }
            return null;
        }
    }

    /**
     * Apartado 1: version VULNERABLE. Concatena el parametro en la sentencia.
     *
     * @param nombre valor buscado, tal y como llega del usuario
     * @return filas devueltas
     */
    public static List<String[]> buscarVulnerable(String nombre) {
        String sql = "SELECT * FROM cliente WHERE nombre = '" + nombre + "'";
        // La pista del enunciado: imprimir la SQL resultante es lo que hace
        // visible el ataque. Sin esta linea, el alumno ve un resultado raro
        // pero no entiende de donde sale.
        System.out.println("    SQL generada: " + sql);
        return new MotorMinimo().ejecutar(sql);
    }

    /**
     * Apartado 4: version SEGURA. El valor viaja aparte, nunca dentro de la
     * sentencia.
     *
     * @param nombre valor buscado, tal y como llega del usuario
     * @return filas devueltas
     */
    public static List<String[]> buscarSeguro(String nombre) {
        String sql = "SELECT * FROM cliente WHERE nombre = ?";
        System.out.println("    SQL enviada al motor: " + sql);
        System.out.println("    Parametro 1 (dato, no codigo): [" + nombre + "]");
        // Esto es lo que hace un PreparedStatement: la estructura de la
        // sentencia ya esta fijada y el valor solo se compara. Nadie vuelve a
        // analizar ese texto buscando comillas ni palabras clave.
        List<String[]> resultado = new ArrayList<>();
        for (String[] fila : TABLA_CLIENTE) {
            if (fila[1].equals(nombre)) {
                resultado.add(fila);
            }
        }
        return resultado;
    }

    /*
     * APARTADO 3: por que la consulta manipulada devuelve todas las filas.
     *
     * El parametro que envia el atacante es:      ' OR '1'='1
     *
     * El metodo lo mete entre las comillas de la sentencia, con lo que la SQL
     * final queda:
     *
     *      SELECT * FROM cliente WHERE nombre = '' OR '1'='1'
     *
     * Y ahi esta el truco completo:
     *   - La comilla inicial del atacante CIERRA la cadena vacia que habia
     *     empezado el programador.
     *   - Lo que viene detras ya no es un dato, es codigo SQL: OR '1'='1'.
     *   - La comilla final del programador cierra la que abrio el atacante, de
     *     modo que la sentencia queda sintacticamente valida.
     *
     * La condicion '1'='1' es siempre verdadera, y como esta unida con OR, el
     * WHERE se cumple para todas las filas. El filtro ha dejado de filtrar.
     *
     * Lo importante no es esta consulta concreta: es que el dato del usuario ha
     * conseguido cambiar la ESTRUCTURA de la sentencia. A partir de ahi, con un
     * poco de paciencia se puede leer cualquier tabla de la base de datos.
     *
     * APARTADO 5: por que PreparedStatement no es vulnerable.
     *
     * Con PreparedStatement la sentencia se envia al SGBD con los interrogantes
     * puestos, ANTES de conocer los valores. El motor la analiza, decide el plan
     * de ejecucion y deja fijada su estructura. Despues, los valores viajan por
     * separado, en el protocolo, como datos.
     *
     * Es decir: cuando llega el texto del atacante, ya no hay ningun analizador
     * sintactico esperandolo. Ese texto solo puede acabar en un sitio, que es la
     * comparacion con la columna nombre. El resultado es que se busca,
     * literalmente, un cliente que se llame  ' OR '1'='1  y, como no existe, no
     * se devuelve ninguna fila.
     *
     * Ojo con una idea muy extendida y falsa: PreparedStatement no protege por
     * "escapar comillas". Puede hacerlo en algunos drivers, pero lo que de
     * verdad protege es la separacion entre la sentencia y sus datos. Por eso
     * tampoco sirve de nada usar PreparedStatement si despues se concatena el
     * nombre de una tabla o de una columna: eso es estructura, no dato, y ahi no
     * se puede poner un interrogante.
     *
     * APARTADO 6 (ampliacion): el punto y coma y las sentencias apiladas.
     *
     * Si el parametro fuese      '; DROP TABLE cliente; --
     * la sentencia final seria:
     *
     *      SELECT * FROM cliente WHERE nombre = ''; DROP TABLE cliente; --'
     *
     * Ya no es una consulta, son TRES cosas: un SELECT, un DROP TABLE y un
     * comentario que se come el resto de la linea para que no quede una comilla
     * suelta. El ataque se llama apilamiento de sentencias, y convierte una
     * fuga de informacion en destruccion de datos.
     *
     * Que ocurra o no depende de la configuracion: el conector de MySQL, por
     * defecto, NO permite varias sentencias en una misma llamada, y hay que
     * activarlo expresamente con allowMultiQueries=true en la URL. Otros
     * gestores, como SQL Server, si lo permiten de serie. Esa diferencia es
     * importante en clase: que un ataque no funcione en tu maquina no significa
     * que el codigo sea seguro, solo que la configuracion te ha salvado esta vez.
     *
     * Defensa en profundidad, ademas de PreparedStatement: que el usuario con
     * el que se conecta la aplicacion no tenga permiso de DROP. Una aplicacion
     * que solo lee y escribe filas no necesita poder borrar tablas.
     */

    // =====================================================================
    // COMPROBACION (apartados 2, 3, 4 y 6)
    // =====================================================================

    /**
     * Muestra el resultado de una busqueda.
     *
     * @param filas filas devueltas
     */
    private static void mostrar(List<String[]> filas) {
        System.out.println("    Filas devueltas: " + filas.size());
        for (String[] fila : filas) {
            System.out.println("      " + fila[0] + "  " + fila[1] + "  " + fila[3]);
        }
    }

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2405-E06] Inyeccion SQL: demostracion y correccion");

        // Apartado 2: uso normal.
        System.out.println("  1) Version vulnerable con un nombre normal:");
        mostrar(buscarVulnerable("Ana Lopez"));

        // Apartado 3: el ataque.
        String ataque = "' OR '1'='1";
        System.out.println("  2) Version vulnerable con el texto  " + ataque);
        List<String[]> robadas = buscarVulnerable(ataque);
        mostrar(robadas);
        System.out.println("    La condicion '1'='1' es siempre cierta y esta unida con OR:");
        System.out.println("    el WHERE deja de filtrar y salen los " + robadas.size() + " clientes de la tabla.");

        // Apartado 4: la misma agresion contra la version parametrizada.
        System.out.println("  3) Version segura con el MISMO texto:");
        mostrar(buscarSeguro(ataque));
        System.out.println("    Se busca un cliente que se llame literalmente  " + ataque);
        System.out.println("    Como no existe, no hay ninguna fila. El texto nunca fue codigo.");

        // Apartado 6: sentencias apiladas.
        String destructivo = "'; DROP TABLE cliente; --";
        String sqlApilada = "SELECT * FROM cliente WHERE nombre = '" + destructivo + "'";
        System.out.println("  4) Ampliacion, apilamiento de sentencias con  " + destructivo);
        System.out.println("    SQL generada: " + sqlApilada);
        String[] sentencias = sqlApilada.split(";");
        System.out.println("    Un motor que admita varias sentencias por llamada ejecutaria "
                + sentencias.length + ":");
        for (String sentencia : sentencias) {
            System.out.println("      [" + sentencia.trim() + "]");
        }
        System.out.println("    La segunda borra la tabla; el -- final comenta la comilla sobrante.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
