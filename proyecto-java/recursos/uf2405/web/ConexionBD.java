package datos;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * UF2405 - BLOQUE 5 - EJERCICIO 14: pool de conexiones.
 *
 * <p>Criterios de evaluacion: CE2.5</p>
 *
 * <p>NO forma parte de la compilacion del proyecto: necesita la dependencia
 * HikariCP ademas del driver. Ver LEEME.md.</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public final class ConexionBD {

    private static final DataSource POOL;

    static {
        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl("jdbc:mysql://localhost:3306/clinica?useUnicode=true&characterEncoding=UTF-8");
        cfg.setUsername("usuario");
        cfg.setPassword("clave");
        cfg.setMaximumPoolSize(10);
        POOL = new HikariDataSource(cfg);
    }

    private ConexionBD() {
        // Clase de utilidad: no se instancia.
    }

    /**
     * Apartado 6: entrega una conexion del pool.
     *
     * @return conexion lista para usar
     * @throws SQLException si el pool no puede entregar ninguna
     */
    public static Connection obtener() throws SQLException {
        return POOL.getConnection();
    }

    /*
     * APARTADO 6: por que un pool y no DriverManager en cada peticion.
     *
     * Abrir una conexion no es una llamada a un metodo: es abrir un socket,
     * negociar el protocolo, autenticar al usuario y reservar recursos en el
     * servidor. Cuesta del orden de decenas de milisegundos. En una aplicacion
     * de escritorio da igual; en una web que atiende cientos de peticiones por
     * minuto es el cuello de botella, y ademas es un coste que se paga en cada
     * peticion aunque la consulta dure un milisegundo.
     *
     * El pool abre unas cuantas conexiones al arrancar y las presta. Cuando el
     * codigo llama a close() sobre una conexion prestada, esta NO se cierra: se
     * devuelve al pool limpia y lista para el siguiente. De ahi que el
     * try-with-resources siga siendo obligatorio: sin el, la conexion no vuelve
     * al pool y a las pocas horas la aplicacion se queda sin ninguna.
     *
     * El segundo motivo es el control. setMaximumPoolSize pone un techo al
     * numero de conexiones simultaneas contra la base de datos. Sin pool, un
     * pico de trafico abre tantas conexiones como peticiones haya y tumba el
     * servidor de base de datos. Con pool, las peticiones sobrantes esperan
     * turno, que es mucho mejor que caerse.
     */
}
