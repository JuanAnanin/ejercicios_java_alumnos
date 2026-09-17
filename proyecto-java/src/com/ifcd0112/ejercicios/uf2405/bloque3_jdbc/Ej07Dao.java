package com.ifcd0112.ejercicios.uf2405.bloque3_jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * UF2405 - BLOQUE 3 - EJERCICIO 7: Patron DAO completo.
 *
 * <p>Criterios de evaluacion: CE2.5</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej07Dao {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Implementa una capa de acceso a datos siguiendo el patron DAO para la
     * entidad Mascota de la clinica veterinaria.
     *
     * Se pide:
     *   1. Crea la clase de modelo Mascota con sus atributos, constructores,
     *      metodos de acceso y toString().
     *   2. Crea la clase MascotaDAO que reciba la conexion en el constructor (no
     *      debe abrirla ella misma) e implemente las cuatro operaciones CRUD:
     *      insertar, buscarPorId, listarTodas, actualizar y eliminar.
     *   3. El metodo insertar debe devolver el identificador autogenerado por la
     *      base de datos.
     *   4. Utiliza PreparedStatement en todos los metodos que reciban parametros.
     *   5. Anade un metodo buscarPorEspecie(String especie) que utilice el
     *      operador LIKE para permitir busquedas parciales.
     *   6. Escribe una clase de prueba que ejecute un ciclo completo: dar de
     *      alta una mascota, buscarla, modificarla, comprobar el cambio y darla
     *      de baja.
     *   7. Justifica en un comentario que ventaja aporta que el DAO reciba la
     *      conexion en lugar de abrirla internamente.
     *
     * Pista: que el DAO reciba la conexion permite que varias operaciones de
     * distintos DAO compartan una misma transaccion, y facilita sustituirla por
     * una conexion simulada al escribir las pruebas.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * COMO ESTA ORGANIZADO ESTE EJERCICIO.
     *
     * El DAO real contra MySQL es MascotaDaoJdbc, mas abajo. Compila tal cual
     * porque java.sql forma parte del JDK; solo necesita el driver en el
     * classpath para funcionar contra una base de datos de verdad.
     *
     * Para que el apartado 6 se pueda EJECUTAR aqui y ahora, sin servidor, el
     * ejercicio define primero la interfaz MascotaDao y despues dos
     * implementaciones: la de JDBC y otra en memoria. El ciclo de prueba se
     * escribe una sola vez contra la interfaz y funciona con cualquiera de las
     * dos.
     *
     * Y eso no es un apano para salir del paso: es justamente lo que dice la
     * pista del enunciado y lo que se pide justificar en el apartado 7. Un DAO
     * que no abre su propia conexion se puede sustituir, y un DAO sustituible se
     * puede probar. Aqui esa idea no se cuenta, se usa.
     */

    // ---------------------------------------------------------------------
    // Apartado 1: la clase de modelo
    // ---------------------------------------------------------------------

    /** Mascota de la clinica. Objeto de modelo, sin logica de acceso a datos. */
    public static class Mascota {

        private int id;
        private String nombre;
        private String especie;
        private LocalDate fechaNac;
        private double peso;
        private int idCliente;

        /** Constructor vacio, util para los marcos de trabajo que lo exigen. */
        public Mascota() {
        }

        /**
         * Constructor para una mascota que todavia no esta en la base de datos.
         *
         * @param nombre    nombre de la mascota
         * @param especie   especie
         * @param fechaNac  fecha de nacimiento
         * @param peso      peso en kilos
         * @param idCliente identificador del dueno
         */
        public Mascota(String nombre, String especie, LocalDate fechaNac, double peso, int idCliente) {
            this.nombre = nombre;
            this.especie = especie;
            this.fechaNac = fechaNac;
            this.peso = peso;
            this.idCliente = idCliente;
        }

        /**
         * Constructor completo, con el identificador ya asignado.
         *
         * @param id        identificador en la base de datos
         * @param nombre    nombre de la mascota
         * @param especie   especie
         * @param fechaNac  fecha de nacimiento
         * @param peso      peso en kilos
         * @param idCliente identificador del dueno
         */
        public Mascota(int id, String nombre, String especie, LocalDate fechaNac,
                       double peso, int idCliente) {
            this(nombre, especie, fechaNac, peso, idCliente);
            this.id = id;
        }

        /** @return identificador en la base de datos */
        public int getId() { return id; }

        /** @param id identificador en la base de datos */
        public void setId(int id) { this.id = id; }

        /** @return nombre de la mascota */
        public String getNombre() { return nombre; }

        /** @param nombre nombre de la mascota */
        public void setNombre(String nombre) { this.nombre = nombre; }

        /** @return especie */
        public String getEspecie() { return especie; }

        /** @param especie especie */
        public void setEspecie(String especie) { this.especie = especie; }

        /** @return fecha de nacimiento */
        public LocalDate getFechaNac() { return fechaNac; }

        /** @param fechaNac fecha de nacimiento */
        public void setFechaNac(LocalDate fechaNac) { this.fechaNac = fechaNac; }

        /** @return peso en kilos */
        public double getPeso() { return peso; }

        /** @param peso peso en kilos */
        public void setPeso(double peso) { this.peso = peso; }

        /** @return identificador del dueno */
        public int getIdCliente() { return idCliente; }

        /** @param idCliente identificador del dueno */
        public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

        @Override
        public String toString() {
            return String.format("Mascota[id=%d, nombre=%s, especie=%s, nac=%s, peso=%.1f, dueno=%d]",
                    id, nombre, especie, fechaNac, peso, idCliente);
        }
    }

    // ---------------------------------------------------------------------
    // El contrato del DAO
    // ---------------------------------------------------------------------

    /**
     * Operaciones de acceso a datos de la entidad Mascota.
     *
     * <p>Todas declaran SQLException aunque una implementacion concreta pueda no
     * lanzarla nunca: quien programa contra la interfaz no debe tener que saber
     * si detras hay una base de datos, un fichero o una lista en memoria.</p>
     */
    public interface MascotaDao {

        /**
         * Apartado 3: da de alta una mascota.
         *
         * @param m mascota a insertar
         * @return identificador autogenerado
         * @throws SQLException si falla el acceso a datos
         */
        int insertar(Mascota m) throws SQLException;

        /**
         * @param id identificador buscado
         * @return la mascota, o null si no existe
         * @throws SQLException si falla el acceso a datos
         */
        Mascota buscarPorId(int id) throws SQLException;

        /**
         * @return todas las mascotas
         * @throws SQLException si falla el acceso a datos
         */
        List<Mascota> listarTodas() throws SQLException;

        /**
         * Apartado 5: busqueda parcial por especie.
         *
         * @param especie fragmento del nombre de la especie
         * @return mascotas cuya especie contiene ese fragmento
         * @throws SQLException si falla el acceso a datos
         */
        List<Mascota> buscarPorEspecie(String especie) throws SQLException;

        /**
         * @param m mascota con los datos nuevos y el id ya asignado
         * @return true si se modifico alguna fila
         * @throws SQLException si falla el acceso a datos
         */
        boolean actualizar(Mascota m) throws SQLException;

        /**
         * @param id identificador de la mascota a borrar
         * @return true si se borro alguna fila
         * @throws SQLException si falla el acceso a datos
         */
        boolean eliminar(int id) throws SQLException;
    }

    // ---------------------------------------------------------------------
    // Apartados 2, 3, 4 y 5: la implementacion JDBC
    // ---------------------------------------------------------------------

    /**
     * DAO de Mascota contra MySQL.
     *
     * <p>Compila sin dependencias externas; para ejecutarlo hace falta el driver
     * de MySQL en el classpath y la base de datos creada con los scripts de
     * recursos/uf2405/sql.</p>
     */
    public static class MascotaDaoJdbc implements MascotaDao {

        private final Connection con;

        /**
         * Apartado 2: la conexion se RECIBE, no se abre aqui.
         *
         * @param con conexion abierta y gestionada por quien llama
         */
        public MascotaDaoJdbc(Connection con) {
            this.con = con;
        }

        @Override
        public int insertar(Mascota m) throws SQLException {
            String sql = "INSERT INTO mascota (nombre, especie, fecha_nac, peso, id_cliente) "
                    + "VALUES (?, ?, ?, ?, ?)";
            // Apartado 3: hay que pedir expresamente las claves generadas; si no
            // se pasa RETURN_GENERATED_KEYS, getGeneratedKeys devuelve vacio.
            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, m.getNombre());
                ps.setString(2, m.getEspecie());
                ps.setDate(3, java.sql.Date.valueOf(m.getFechaNac()));
                ps.setDouble(4, m.getPeso());
                ps.setInt(5, m.getIdCliente());
                ps.executeUpdate();
                try (ResultSet claves = ps.getGeneratedKeys()) {
                    return claves.next() ? claves.getInt(1) : -1;
                }
            }
        }

        @Override
        public Mascota buscarPorId(int id) throws SQLException {
            String sql = "SELECT * FROM mascota WHERE id_mascota = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? mapear(rs) : null;
                }
            }
        }

        @Override
        public List<Mascota> listarTodas() throws SQLException {
            List<Mascota> lista = new ArrayList<>();
            // Sin parametros: aqui Statement es suficiente y no hay riesgo,
            // porque no se concatena nada que venga de fuera.
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT * FROM mascota ORDER BY nombre")) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
            return lista;
        }

        @Override
        public List<Mascota> buscarPorEspecie(String especie) throws SQLException {
            String sql = "SELECT * FROM mascota WHERE especie LIKE ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                // Los comodines se ponen en el VALOR, no en la sentencia. Si se
                // escribiera LIKE '%?%' el interrogante quedaria dentro de una
                // cadena literal y dejaria de ser un parametro.
                ps.setString(1, "%" + especie + "%");
                try (ResultSet rs = ps.executeQuery()) {
                    List<Mascota> lista = new ArrayList<>();
                    while (rs.next()) {
                        lista.add(mapear(rs));
                    }
                    return lista;
                }
            }
        }

        @Override
        public boolean actualizar(Mascota m) throws SQLException {
            String sql = "UPDATE mascota SET nombre=?, especie=?, peso=? WHERE id_mascota=?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, m.getNombre());
                ps.setString(2, m.getEspecie());
                ps.setDouble(3, m.getPeso());
                ps.setInt(4, m.getId());
                // executeUpdate devuelve el numero de filas afectadas: es la
                // forma de saber si el id existia.
                return ps.executeUpdate() > 0;
            }
        }

        @Override
        public boolean eliminar(int id) throws SQLException {
            try (PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM mascota WHERE id_mascota = ?")) {
                ps.setInt(1, id);
                return ps.executeUpdate() > 0;
            }
        }

        /**
         * Convierte la fila actual del ResultSet en un objeto Mascota.
         *
         * <p>Existe para no repetir esta conversion en los cuatro metodos que
         * leen. Si manana se anade una columna, se toca un solo sitio.</p>
         *
         * @param rs ResultSet posicionado en una fila
         * @return la mascota correspondiente
         * @throws SQLException si falla la lectura
         */
        private Mascota mapear(ResultSet rs) throws SQLException {
            java.sql.Date fecha = rs.getDate("fecha_nac");
            return new Mascota(
                    rs.getInt("id_mascota"),
                    rs.getString("nombre"),
                    rs.getString("especie"),
                    (fecha != null) ? fecha.toLocalDate() : null,
                    rs.getDouble("peso"),
                    rs.getInt("id_cliente"));
        }
    }

    // ---------------------------------------------------------------------
    // Implementacion en memoria, para poder ejecutar el apartado 6
    // ---------------------------------------------------------------------

    /**
     * DAO de Mascota sobre un mapa en memoria.
     *
     * <p>Mismo contrato que el de JDBC. Sirve para ejecutar el ciclo de prueba
     * sin servidor y es tambien la clase que se usaria en una prueba unitaria
     * de la capa de negocio.</p>
     */
    public static class MascotaDaoMemoria implements MascotaDao {

        private final Map<Integer, Mascota> tabla = new LinkedHashMap<>();
        private int siguienteId = 1;

        @Override
        public int insertar(Mascota m) {
            int id = siguienteId++;          // imita el AUTO_INCREMENT
            m.setId(id);
            tabla.put(id, copiar(m));
            return id;
        }

        @Override
        public Mascota buscarPorId(int id) {
            Mascota m = tabla.get(id);
            // Se devuelve una copia: si se devolviera el objeto guardado,
            // cualquiera podria modificar la "base de datos" sin pasar por
            // actualizar(), y la prueba del apartado 6 pareceria correcta
            // aunque actualizar() estuviera vacio.
            return (m != null) ? copiar(m) : null;
        }

        @Override
        public List<Mascota> listarTodas() {
            List<Mascota> lista = new ArrayList<>();
            for (Mascota m : tabla.values()) {
                lista.add(copiar(m));
            }
            return lista;
        }

        @Override
        public List<Mascota> buscarPorEspecie(String especie) {
            List<Mascota> lista = new ArrayList<>();
            String patron = especie.toLowerCase();
            for (Mascota m : tabla.values()) {
                // Equivalente en memoria de LIKE '%especie%'.
                if (m.getEspecie() != null && m.getEspecie().toLowerCase().contains(patron)) {
                    lista.add(copiar(m));
                }
            }
            return lista;
        }

        @Override
        public boolean actualizar(Mascota m) {
            if (!tabla.containsKey(m.getId())) {
                return false;
            }
            tabla.put(m.getId(), copiar(m));
            return true;
        }

        @Override
        public boolean eliminar(int id) {
            return tabla.remove(id) != null;
        }

        /**
         * @param m mascota a copiar
         * @return copia independiente
         */
        private Mascota copiar(Mascota m) {
            return new Mascota(m.getId(), m.getNombre(), m.getEspecie(),
                    m.getFechaNac(), m.getPeso(), m.getIdCliente());
        }
    }

    /*
     * APARTADO 7: que ventaja aporta que el DAO reciba la conexion.
     *
     * 1. TRANSACCIONES QUE ABARCAN VARIOS DAO. Es la razon de peso. Registrar
     *    una consulta con sus tratamientos toca tres tablas y probablemente dos
     *    o tres DAO distintos. Si cada uno abriera su propia conexion, cada uno
     *    estaria en su propia transaccion y no habria forma de deshacerlas
     *    todas juntas. Compartiendo la conexion, un solo rollback las deshace.
     *    El ejercicio 8 es exactamente ese caso.
     *
     * 2. SE PUEDE PROBAR. Un DAO que abre su conexion arrastra el servidor
     *    entero a cualquier prueba. Recibiendola, se le puede dar una conexion
     *    de prueba, o directamente sustituir la implementacion, que es lo que
     *    hace este ejercicio con MascotaDaoMemoria.
     *
     * 3. NO GESTIONA UN RECURSO QUE NO ES SUYO. Quien abre un recurso es quien
     *    debe cerrarlo. Si el DAO abriera la conexion, tendria que decidir
     *    cuando cerrarla, y no tiene informacion para hacerlo: no sabe si quien
     *    le llama va a seguir necesitandola.
     *
     * 4. RENDIMIENTO. Abrir una conexion es caro. Recibiendola, se puede
     *    reutilizar la misma para varias operaciones o sacarla de un pool, como
     *    se hace en el ejercicio 14.
     *
     * Es un caso concreto de inyeccion de dependencias: la clase declara lo que
     * necesita y se lo dan hecho, en lugar de fabricarselo.
     */

    // =====================================================================
    // COMPROBACION (apartado 6): ciclo CRUD completo
    // =====================================================================

    /**
     * Apartado 6: alta, busqueda, modificacion, comprobacion y baja.
     *
     * <p>Esta escrito contra la INTERFAZ, asi que sirve igual para el DAO de
     * JDBC y para el de memoria.</p>
     *
     * @param dao implementacion a probar
     * @throws SQLException si falla el acceso a datos
     */
    public static void cicloCompleto(MascotaDao dao) throws SQLException {
        // Datos de partida, para que el listado tenga algo mas que la mascota
        // de la prueba.
        dao.insertar(new Mascota("Toby", "Perro", LocalDate.of(2019, 4, 12), 22.5, 1));
        dao.insertar(new Mascota("Misu", "Gato", LocalDate.of(2021, 9, 30), 4.2, 1));

        // 1. Alta
        Mascota nueva = new Mascota("Nube", "Gato", LocalDate.of(2022, 6, 18), 3.8, 3);
        int id = dao.insertar(nueva);
        System.out.println("    Alta: id autogenerado = " + id);

        // 2. Busqueda
        Mascota leida = dao.buscarPorId(id);
        System.out.println("    Buscada: " + leida);

        // 3. Modificacion
        leida.setPeso(4.5);
        leida.setEspecie("Gato comun europeo");
        boolean modificada = dao.actualizar(leida);
        System.out.println("    Actualizada: " + modificada);

        // 4. Comprobacion del cambio: se vuelve a LEER de la fuente de datos,
        // no se mira el objeto que tenemos en la mano. Si actualizar() no
        // hubiera hecho nada, esta linea lo delata.
        Mascota relectura = dao.buscarPorId(id);
        System.out.println("    Releida:  " + relectura);
        System.out.println("    Cambio confirmado: "
                + (relectura.getPeso() == 4.5 && relectura.getEspecie().startsWith("Gato comun")));

        // Apartado 5: busqueda parcial. "Gato" encuentra tambien "Gato comun
        // europeo", que es de lo que se trata con LIKE.
        List<Mascota> gatos = dao.buscarPorEspecie("Gato");
        System.out.println("    buscarPorEspecie(\"Gato\") -> " + gatos.size() + " resultados:");
        for (Mascota m : gatos) {
            System.out.println("      " + m.getNombre() + " (" + m.getEspecie() + ")");
        }

        // 5. Baja
        System.out.println("    Baja: " + dao.eliminar(id));
        System.out.println("    Tras la baja, buscarPorId devuelve: " + dao.buscarPorId(id));
        System.out.println("    Quedan " + dao.listarTodas().size() + " mascotas");
    }

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2405-E07] Patron DAO completo");
        System.out.println("  Ciclo CRUD sobre la implementacion en memoria:");
        try {
            cicloCompleto(new MascotaDaoMemoria());
        } catch (SQLException e) {
            System.out.println("    Error inesperado: " + e.getMessage());
        }
        System.out.println("  El mismo ciclo funciona con MascotaDaoJdbc pasandole una Connection;");
        System.out.println("  esa es la ventaja del apartado 7 puesta en practica.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
