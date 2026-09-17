package presentacion;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import datos.ConexionBD;
import datos.ConsultaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * UF2405 - BLOQUE 4 - EJERCICIO 12 (3 de 3): confirma la consulta en curso,
 * la registra con la transaccion del ejercicio 8 y vacia la sesion.
 *
 * <p>Criterios de evaluacion: CE1.7</p>
 *
 * <p>NO forma parte de la compilacion del proyecto. Ver LEEME.md.</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
@WebServlet("/confirmarConsulta")
public class ConfirmarConsultaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Apartado 3: registra la consulta y vacia la sesion.
     *
     * @param request  peticion
     * @param response respuesta
     * @throws ServletException si falla el registro
     * @throws IOException      si falla la respuesta
     */
    @Override
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        if (sesion == null || sesion.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Map<Integer, Integer> tratamientos =
                (Map<Integer, Integer>) sesion.getAttribute(AnadirTratamientoServlet.CONSULTA_EN_CURSO);

        if (tratamientos == null || tratamientos.isEmpty()) {
            response.sendRedirect("verConsulta");
            return;
        }

        int idMascota = Integer.parseInt(request.getParameter("idMascota"));
        int idVeterinario = (Integer) sesion.getAttribute("idVeterinario");
        String motivo = request.getParameter("motivo");

        try (Connection con = ConexionBD.obtener()) {
            ConsultaDAO dao = new ConsultaDAO(con);
            // Aqui se ve por que el DAO recibe la conexion: las tres
            // inserciones de registrarConsulta comparten transaccion.
            int id = dao.registrarConsulta(idMascota, idVeterinario, motivo, tratamientos);

            // Vaciar la sesion SOLO despues del commit. Si se vaciara antes y
            // la transaccion fallara, el veterinario perderia todo el trabajo.
            sesion.removeAttribute(AnadirTratamientoServlet.CONSULTA_EN_CURSO);

            // POST-Redirect-GET otra vez: sin esto, un F5 registraria la misma
            // consulta dos veces.
            response.sendRedirect("consulta?id=" + id);

        } catch (SQLException e) {
            throw new ServletException("No se pudo registrar la consulta", e);
        }
    }
}
