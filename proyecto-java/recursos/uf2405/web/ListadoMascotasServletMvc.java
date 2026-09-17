package presentacion;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import datos.ConexionBD;
import datos.MascotaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.Mascota;

/**
 * UF2405 - BLOQUE 4 - EJERCICIO 13: el Servlet del ejercicio 10, refactorizado
 * para delegar toda la presentacion en un JSP.
 *
 * <p>Criterios de evaluacion: CE1.4, CE1.8</p>
 *
 * <p>NO forma parte de la compilacion del proyecto. Ver LEEME.md.</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
@WebServlet("/mascotas")
public class ListadoMascotasServletMvc extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Apartados 1 y 3: obtiene los datos, los deposita en la peticion y delega
     * la vista.
     *
     * <p>Comparese con el ejercicio 10: no hay ni un println de HTML. Este
     * metodo ya no sabe si la respuesta sera una tabla, una lista o un PDF.</p>
     *
     * @param request  peticion
     * @param response respuesta
     * @throws ServletException si falla el acceso a datos
     * @throws IOException      si falla la respuesta
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try (Connection con = ConexionBD.obtener()) {
            List<Mascota> mascotas = new MascotaDAO(con).listarTodas();

            // Apartado 1: los datos se depositan como atributo de la PETICION,
            // no de la sesion. Solo hacen falta para pintar esta pagina; en la
            // sesion se quedarian ocupando memoria hasta que caducara.
            request.setAttribute("mascotas", mascotas);

            // Apartado 3: el JSP esta bajo WEB-INF, que el contenedor NO sirve
            // directamente. Asi nadie puede pedir mascotas.jsp por la URL y
            // saltarse el Servlet, que es quien carga los datos.
            request.getRequestDispatcher("/WEB-INF/mascotas.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Error al listar las mascotas", e);
        }
    }

    /*
     * APARTADO 3: forward() frente a sendRedirect().
     *
     * forward()      Ocurre entero DENTRO del servidor. El navegador hizo una
     *                peticion y recibe una respuesta; no se entera de que por
     *                dentro han intervenido dos componentes. La URL no cambia y
     *                los atributos de la peticion siguen disponibles, que es
     *                justo lo que necesita el JSP para encontrar "mascotas".
     *                Una sola ida y vuelta por la red.
     *
     * sendRedirect() El servidor responde 302 con una direccion, y el navegador
     *                hace una peticion NUEVA a esa direccion. La URL cambia y
     *                los atributos de la peticion original se pierden, porque
     *                aquella peticion ya termino. Dos idas y vueltas por la red.
     *
     * Cuando usar cada uno: forward para repartir el trabajo de una misma
     * peticion entre varios componentes del servidor; redirect despues de una
     * operacion que modifica datos, para que la ultima peticion del navegador
     * sea un GET recargable (el POST-Redirect-GET del ejercicio 11).
     *
     * APARTADO 5: el reparto por capas del codigo resultante.
     *
     *   PRESENTACION   mascotas.jsp. Solo decide como se ve. No sabe de donde
     *                  salen los datos.
     *   APLICACION     este Servlet. Coordina: recibe la peticion, pide los
     *                  datos y elige la vista. No pinta ni consulta.
     *   DATOS          MascotaDAO y ConexionBD. Saben de SQL y de conexiones.
     *                  No saben que existe la web.
     *
     * APARTADO 6: que se gana con esta separacion.
     *
     * Lo concreto: cambiar el aspecto de la pagina ya no exige recompilar. En
     * la version del ejercicio 10, mover una columna significaba editar codigo
     * Java, compilar y volver a desplegar. Ahora se edita un JSP.
     *
     * Y hay dos consecuencias practicas mas. La primera es que el HTML deja de
     * estar escondido dentro de cadenas de Java, donde no hay coloreado de
     * sintaxis ni ayuda del editor y donde una etiqueta sin cerrar no la ve
     * nadie. La segunda es que el trabajo se puede repartir: quien maquete la
     * pagina no necesita saber Java ni tocar el proyecto.
     */
}
