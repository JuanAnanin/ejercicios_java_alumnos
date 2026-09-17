package presentacion;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import datos.ConexionBD;
import datos.TratamientoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modelo.Tratamiento;

/**
 * UF2405 - BLOQUE 4 - EJERCICIO 12 (2 de 3): muestra la consulta en curso con
 * su importe total acumulado.
 *
 * <p>Criterios de evaluacion: CE1.7</p>
 *
 * <p>NO forma parte de la compilacion del proyecto. Ver LEEME.md.</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
@WebServlet("/verConsulta")
public class VerConsultaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Apartado 2: pinta el contenido de la consulta en curso y su total.
     *
     * @param request  peticion
     * @param response respuesta
     * @throws ServletException si falla el procesamiento
     * @throws IOException      si falla la respuesta
     */
    @Override
    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        if (sesion == null || sesion.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        Map<Integer, Integer> consulta =
                (Map<Integer, Integer>) sesion.getAttribute(AnadirTratamientoServlet.CONSULTA_EN_CURSO);

        out.println("<!DOCTYPE html><html lang='es'><head><meta charset='UTF-8'>");
        out.println("<title>Consulta en curso</title></head><body>");
        out.println("<h1>Consulta en curso</h1>");

        if (consulta == null || consulta.isEmpty()) {
            out.println("<p>Todavia no has anadido ningun tratamiento.</p>");
        } else {
            double total = 0;
            out.println("<table border='1' cellpadding='4'>");
            out.println("<tr><th>Tratamiento</th><th>Cantidad</th><th>Importe</th></tr>");
            try (Connection con = ConexionBD.obtener()) {
                TratamientoDAO dao = new TratamientoDAO(con);
                for (Map.Entry<Integer, Integer> linea : consulta.entrySet()) {
                    Tratamiento t = dao.buscarPorId(linea.getKey());
                    double importe = t.getPrecio() * linea.getValue();
                    total += importe;
                    out.printf("<tr><td>%s</td><td>%d</td><td>%.2f EUR</td></tr>%n",
                            escapar(t.getNombre()), linea.getValue(), importe);
                }
            } catch (SQLException e) {
                out.println("<tr><td colspan='3'>No se pudo consultar el tarifario.</td></tr>");
                log("Error al leer tratamientos", e);
            }
            out.println("</table>");
            out.printf("<p><strong>Total: %.2f EUR</strong></p>%n", total);
            out.println("<form action='confirmarConsulta' method='post'>");
            out.println("<input type='submit' value='Confirmar consulta'></form>");
        }

        out.println("</body></html>");
    }

    /**
     * @param texto texto a escapar
     * @return texto seguro para HTML
     */
    private String escapar(String texto) {
        return (texto == null) ? "" : texto.replace("&", "&amp;")
                .replace("<", "&lt;").replace(">", "&gt;");
    }
}
