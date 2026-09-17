package com.ifcd0112.ejercicios.uf2405.bloque3_jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * UF2405 - BLOQUE 3 - EJERCICIO 8: Transacciones, alta de consulta con tratamientos.
 *
 * <p>Criterios de evaluacion: CE2.4</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej08Transacciones {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Registrar una consulta en la clinica implica varias operaciones sobre
     * tablas distintas que deben completarse todas o ninguna: insertar la
     * consulta, insertar sus tratamientos asociados y actualizar la fecha de
     * ultima visita de la mascota.
     *
     * Se pide:
     *   1. Implementa el metodo registrarConsulta(...) que realice las tres
     *      operaciones dentro de una unica transaccion.
     *   2. Desactiva el autocommit al inicio y confirma con commit() unicamente
     *      si todas las operaciones han tenido exito.
     *   3. Captura cualquier SQLException y deshaz todos los cambios con
     *      rollback() antes de propagar el error.
     *   4. Restaura el autocommit en un bloque finally, independientemente del
     *      resultado.
     *   5. Provoca deliberadamente un fallo en la segunda operacion (por
     *      ejemplo, referenciando un tratamiento inexistente) y comprueba
     *      consultando la base de datos que la consulta tampoco se ha insertado.
     *   6. Explica en un comentario que inconsistencia concreta se habria
     *      producido en los datos si no se hubiera utilizado una transaccion.
     *
     * Pista: para comprobar que el rollback funciona, consulta la tabla de
     * consultas inmediatamente despues de que se produzca el error: no debe
     * existir ninguna fila nueva.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * COMO ESTA ORGANIZADO ESTE EJERCICIO.
     *
     * GestorConsultasJdbc contiene el metodo que se entrega como solucion, con
     * JDBC real. Compila tal cual y funciona en cuanto haya driver y base de
     * datos.
     *
     * Para poder EJECUTAR aqui los apartados 5 y 6, el ejercicio incluye ademas
     * una base de datos en memoria con transacciones de verdad: mantiene un
     * estado confirmado y un estado de trabajo, commit() pasa el segundo al
     * primero y rollback() lo descarta. Con eso se pueden ejecutar los tres
     * escenarios que hacen falta y comprobar los conteos reales despues de cada
     * uno, que es lo que pide la pista.
     */

    // ---------------------------------------------------------------------
    // Apartados 1 a 4: la version JDBC
    // ---------------------------------------------------------------------

    /** Registro de consultas contra MySQL. */
    public static class GestorConsultasJdbc {

        private final Connection con;

        /**
         * @param con conexion abierta; la transaccion se hace sobre ella
         */
        public GestorConsultasJdbc(Connection con) {
            this.con = con;
        }

        /**
         * Apartados 1 a 4: registra una consulta con sus tratamientos.
         *
         * <p>Las tres operaciones forman una unidad: o se hacen todas o no se
         * hace ninguna.</p>
         *
         * @param idMascota     mascota atendida
         * @param idVeterinario veterinario que atiende
         * @param motivo        motivo de la consulta
         * @param tratamientos  tratamientos aplicados y su cantidad
         * @return identificador de la consulta creada
         * @throws SQLException si falla cualquiera de las tres operaciones
         */
        public int registrarConsulta(int idMascota, int idVeterinario, String motivo,
                                     Map<Integer, Integer> tratamientos) throws SQLException {
            try {
                // Apartado 2: a partir de aqui nada se graba de verdad hasta el
                // commit. Con autocommit activado, cada executeUpdate seria una
                // transaccion independiente y ya confirmada: imposible deshacer.
                con.setAutoCommit(false);

                // 1) Cabecera de la consulta
                int idConsulta;
                String sqlC = "INSERT INTO consulta (motivo, id_mascota, id_veterinario) "
                        + "VALUES (?, ?, ?)";
                try (PreparedStatement ps = con.prepareStatement(sqlC, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, motivo);
                    ps.setInt(2, idMascota);
                    ps.setInt(3, idVeterinario);
                    ps.executeUpdate();
                    try (ResultSet claves = ps.getGeneratedKeys()) {
                        if (!claves.next()) {
                            throw new SQLException("La base de datos no devolvio el id de la consulta");
                        }
                        idConsulta = claves.getInt(1);
                    }
                }

                // 2) Tratamientos aplicados
                String sqlT = "INSERT INTO consulta_tratamiento "
                        + "(id_consulta, id_tratamiento, cantidad) VALUES (?, ?, ?)";
                try (PreparedStatement ps = con.prepareStatement(sqlT)) {
                    for (Map.Entry<Integer, Integer> e : tratamientos.entrySet()) {
                        ps.setInt(1, idConsulta);
                        ps.setInt(2, e.getKey());
                        ps.setInt(3, e.getValue());
                        ps.executeUpdate();
                    }
                }

                // 3) Ultima visita de la mascota
                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE mascota SET ultima_visita = NOW() WHERE id_mascota = ?")) {
                    ps.setInt(1, idMascota);
                    ps.executeUpdate();
                }

                con.commit();                 // ---- TODO CORRECTO ----
                return idConsulta;

            } catch (SQLException e) {
                // Apartado 3: deshacer y propagar. Se propaga a proposito: quien
                // llamo tiene que enterarse de que la consulta no se registro.
                // Tragarse la excepcion aqui seria mucho peor que el error.
                con.rollback();
                throw e;
            } finally {
                // Apartado 4: la conexion puede venir de un pool y volver a el.
                // Si se devolviera con el autocommit desactivado, el siguiente
                // que la use se encontraria una transaccion abierta sin saberlo.
                con.setAutoCommit(true);
            }
        }
    }

    // ---------------------------------------------------------------------
    // Base de datos en memoria con transacciones, para ejecutar la prueba
    // ---------------------------------------------------------------------

    /**
     * Base de datos minima con soporte real de commit y rollback.
     *
     * <p>Guarda dos estados: el confirmado, que es lo que veria otra conexion, y
     * el de trabajo, donde se acumulan los cambios de la transaccion en curso.
     * commit() sustituye el confirmado por el de trabajo; rollback() descarta el
     * de trabajo.</p>
     */
    public static class BaseDatosMemoria {

        /** Tratamientos que existen. Insertar otro id viola la clave ajena. */
        private static final List<Integer> TRATAMIENTOS_VALIDOS = Arrays.asList(1, 2, 3, 4);

        /** Una foto completa de las tres tablas que toca la transaccion. */
        private static class Estado {
            private final List<String> consultas = new ArrayList<>();
            private final List<String> puente = new ArrayList<>();
            private String ultimaVisitaMascota1 = "(nunca)";

            Estado copiar() {
                Estado e = new Estado();
                e.consultas.addAll(consultas);
                e.puente.addAll(puente);
                e.ultimaVisitaMascota1 = ultimaVisitaMascota1;
                return e;
            }
        }

        private Estado confirmado = new Estado();
        private Estado trabajo;
        private boolean autoCommit = true;
        private int siguienteIdConsulta = 1;

        /**
         * @param valor false para abrir una transaccion
         */
        public void setAutoCommit(boolean valor) {
            if (!valor && autoCommit) {
                trabajo = confirmado.copiar();   // empieza la transaccion
            }
            this.autoCommit = valor;
        }

        /** Confirma los cambios de la transaccion en curso. */
        public void commit() {
            if (trabajo != null) {
                confirmado = trabajo;
                trabajo = null;
            }
        }

        /** Descarta los cambios de la transaccion en curso. */
        public void rollback() {
            trabajo = null;
        }

        /** @return el estado sobre el que hay que escribir ahora mismo */
        private Estado destino() {
            return (trabajo != null) ? trabajo : confirmado;
        }

        /**
         * @param motivo        motivo de la consulta
         * @param idMascota     mascota atendida
         * @param idVeterinario veterinario
         * @return identificador de la consulta creada
         */
        public int insertarConsulta(String motivo, int idMascota, int idVeterinario) {
            int id = siguienteIdConsulta++;
            destino().consultas.add(id + "|" + motivo + "|" + idMascota + "|" + idVeterinario);
            return id;
        }

        /**
         * @param idConsulta    consulta a la que se asocia
         * @param idTratamiento tratamiento aplicado
         * @param cantidad      unidades
         * @throws SQLException si el tratamiento no existe (clave ajena)
         */
        public void insertarTratamiento(int idConsulta, int idTratamiento, int cantidad)
                throws SQLException {
            if (!TRATAMIENTOS_VALIDOS.contains(idTratamiento)) {
                // Es lo que haria MySQL: error 1452, SQLState 23000.
                throw new SQLException("Cannot add or update a child row: a foreign key constraint "
                        + "fails (id_tratamiento=" + idTratamiento + ")", "23000", 1452);
            }
            destino().puente.add(idConsulta + "|" + idTratamiento + "|" + cantidad);
        }

        /**
         * @param idMascota mascota cuya fecha se actualiza
         * @param fecha     fecha de la visita
         */
        public void actualizarUltimaVisita(int idMascota, String fecha) {
            if (idMascota == 1) {
                destino().ultimaVisitaMascota1 = fecha;
            }
        }

        /** @return filas de consulta que veria otra conexion */
        public int filasConsulta() { return confirmado.consultas.size(); }

        /** @return filas de consulta_tratamiento que veria otra conexion */
        public int filasPuente() { return confirmado.puente.size(); }

        /** @return ultima visita confirmada de la mascota 1 */
        public String ultimaVisita() { return confirmado.ultimaVisitaMascota1; }
    }

    /** Registro de consultas sobre la base de datos en memoria. */
    public static class GestorConsultasMemoria {

        private final BaseDatosMemoria bd;

        /**
         * @param bd base de datos sobre la que operar
         */
        public GestorConsultasMemoria(BaseDatosMemoria bd) {
            this.bd = bd;
        }

        /**
         * Misma estructura que la version JDBC: setAutoCommit(false), las tres
         * operaciones, commit, rollback en el catch y restauracion en finally.
         *
         * @param idMascota     mascota atendida
         * @param idVeterinario veterinario
         * @param motivo        motivo de la consulta
         * @param tratamientos  tratamientos y cantidades
         * @param fecha         fecha de la visita, para poder distinguirla
         * @param conTransaccion false para ver que pasa SIN transaccion
         * @return identificador de la consulta creada
         * @throws SQLException si falla alguna de las tres operaciones
         */
        public int registrarConsulta(int idMascota, int idVeterinario, String motivo,
                                     Map<Integer, Integer> tratamientos,
                                     String fecha, boolean conTransaccion) throws SQLException {
            try {
                if (conTransaccion) {
                    bd.setAutoCommit(false);
                }
                int idConsulta = bd.insertarConsulta(motivo, idMascota, idVeterinario);
                for (Map.Entry<Integer, Integer> e : tratamientos.entrySet()) {
                    bd.insertarTratamiento(idConsulta, e.getKey(), e.getValue());
                }
                bd.actualizarUltimaVisita(idMascota, fecha);
                if (conTransaccion) {
                    bd.commit();
                }
                return idConsulta;
            } catch (SQLException e) {
                if (conTransaccion) {
                    bd.rollback();
                }
                throw e;
            } finally {
                bd.setAutoCommit(true);
            }
        }
    }

    /*
     * APARTADO 6: que inconsistencia se habria producido sin transaccion.
     *
     * El metodo hace tres cosas en tres tablas distintas. Si la segunda falla y
     * cada operacion se confirma por su cuenta, en la base de datos queda:
     *
     *   - Una consulta ya creada, con su identificador y su fecha.
     *   - Solo una parte de sus tratamientos, los que se insertaron antes del
     *     fallo. Los demas se han perdido.
     *   - La mascota SIN actualizar su ultima visita, porque esa operacion ni
     *     siquiera llego a ejecutarse.
     *
     * El resultado no es "faltan datos", que ya seria malo. El resultado es que
     * los datos MIENTEN, y ademas de forma verosimil:
     *
     *   - La factura de esa consulta saldra mas barata de lo que corresponde,
     *     porque faltan tratamientos. Nadie lo notara: la consulta existe y
     *     tiene tratamientos, simplemente no todos.
     *   - El informe de facturacion por mascota del ejercicio 3 dara un total
     *     equivocado.
     *   - La ficha de la mascota dira que su ultima visita fue hace meses, aun
     *     habiendo una consulta registrada hoy. Dos datos de la misma base que
     *     se contradicen.
     *
     * Y lo peor: nadie puede saber cuales de las filas incompletas vinieron de
     * un fallo y cuales son correctas. Un dato que falta se detecta; un dato
     * incoherente que parece plausible, no.
     *
     * Eso es lo que significa la A de ATOMICIDAD en las propiedades ACID: la
     * unidad de trabajo es indivisible. No hay medio registro de consulta, igual
     * que no hay media transferencia bancaria.
     */

    // =====================================================================
    // COMPROBACION (apartados 5 y 6)
    // =====================================================================

    /**
     * Imprime el estado confirmado de las tres tablas.
     *
     * @param bd       base de datos
     * @param etiqueta texto que precede al conteo
     */
    private static void estado(BaseDatosMemoria bd, String etiqueta) {
        System.out.println("    " + etiqueta + ": consulta=" + bd.filasConsulta()
                + " filas, consulta_tratamiento=" + bd.filasPuente()
                + " filas, ultima_visita=" + bd.ultimaVisita());
    }

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2405-E08] Transacciones: alta de consulta con tratamientos");

        BaseDatosMemoria bd = new BaseDatosMemoria();
        GestorConsultasMemoria gestor = new GestorConsultasMemoria(bd);

        estado(bd, "Estado inicial");

        // Caso 1: todo correcto.
        Map<Integer, Integer> correctos = new LinkedHashMap<>();
        correctos.put(1, 1);
        correctos.put(2, 1);
        System.out.println("  1) Consulta con tratamientos validos (1 y 2):");
        try {
            int id = gestor.registrarConsulta(1, 1, "Revision anual", correctos,
                    "2026-08-20 10:00:00", true);
            System.out.println("    Registrada con id " + id + " y confirmada con commit");
        } catch (SQLException e) {
            System.out.println("    Error inesperado: " + e.getMessage());
        }
        estado(bd, "Despues del commit");

        // Caso 2 (apartado 5): fallo deliberado en la segunda operacion.
        Map<Integer, Integer> conFallo = new LinkedHashMap<>();
        conFallo.put(1, 1);
        conFallo.put(99, 1);        // tratamiento inexistente
        System.out.println("  2) Consulta con un tratamiento inexistente (99), CON transaccion:");
        try {
            gestor.registrarConsulta(1, 2, "Cojera pata trasera", conFallo,
                    "2026-08-26 12:30:00", true);
            System.out.println("    ERROR: deberia haber fallado");
        } catch (SQLException e) {
            System.out.println("    SQLException capturada, SQLState " + e.getSQLState()
                    + ", codigo " + e.getErrorCode());
            System.out.println("    " + e.getMessage());
        }
        estado(bd, "Despues del rollback");
        System.out.println("    Los conteos no han cambiado: la consulta tampoco se inserto.");

        // Caso 3 (apartado 6): el mismo fallo SIN transaccion.
        System.out.println("  3) El MISMO fallo SIN transaccion:");
        try {
            gestor.registrarConsulta(1, 2, "Cojera pata trasera", conFallo,
                    "2026-08-26 12:30:00", false);
            System.out.println("    ERROR: deberia haber fallado");
        } catch (SQLException e) {
            System.out.println("    SQLException capturada: " + e.getMessage());
        }
        estado(bd, "Despues del fallo");
        System.out.println("    Ahora si ha cambiado: hay una consulta de mas con un solo");
        System.out.println("    tratamiento de los dos, y ultima_visita sigue en el 20 de agosto");
        System.out.println("    aunque conste una consulta del 26. Dos datos que se contradicen.");
        System.out.println("    Esos son datos incoherentes que parecen correctos, que es");
        System.out.println("    exactamente lo que evita la transaccion.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
