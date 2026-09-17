package presentacion;

import java.io.IOException;
import java.io.PrintWriter;
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
 * UF2405 - BLOQUE 4 - EJERCICIO 10: Servlet de listado con acceso a base de datos.
 *
 * <p>Criterios de evaluacion: CE1.8</p>
 *
 * <p>NO forma parte de la compilacion del proyecto: necesita la API de Servlets
 * y un contenedor web. Ver LEEME.md de esta carpeta.</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
// Apartado 2: la anotacion sustituye a la declaracion en web.xml.
@WebServlet("/mascotas")
public class ListadoMascotasServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Apartado 1: responde a las peticiones GET con el catalogo de mascotas.
     *
     * @param request  peticion
     * @param response respuesta
     * @throws ServletException si falla el procesamiento
     * @throws IOException      si falla la escritura de la respuesta
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Antes de escribir NADA. Si se escribe primero, la cabecera ya ha
        // salido y este setContentType no tiene ningun efecto: los acentos se
        // veran mal y no habra forma de arreglarlo desde aqui.
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html><html lang='es'><head><meta charset='UTF-8'>");
        out.println("<title>Catalogo de mascotas</title></head><body>");
        out.println("<h1>Mascotas registradas</h1>");

        // Apartado 3: el Servlet no contiene ni una sentencia SQL. Toda la
        // conversacion con la base de datos pasa por el DAO del ejercicio 7.
        try (Connection con = ConexionBD.obtener()) {
            MascotaDAO dao = new MascotaDAO(con);
            List<Mascota> mascotas = dao.listarTodas();

            out.println("<table border='1' cellpadding='4'>");
            out.println("<tr><th>Nombre</th><th>Especie</th><th></th></tr>");
            for (Mascota m : mascotas) {
                // Apartado 5: el enlace al detalle lleva el id en la URL.
                out.printf("<tr><td>%s</td><td>%s</td>"
                        + "<td><a href='detalle?id=%d'>Ver detalle</a></td></tr>%n",
                        escapar(m.getNombre()), escapar(m.getEspecie()), m.getId());
            }
            out.println("</table>");

        } catch (SQLException e) {
            // Apartado 6: al usuario, un mensaje comprensible. NUNCA la traza:
            // revela nombres de tablas, rutas y version del SGBD, que es
            // informacion util para quien quiera atacar la aplicacion.
            out.println("<p>No se pudo consultar el catalogo. Intentelo mas tarde.</p>");
            log("Error al listar mascotas", e);   // el detalle, al log del servidor
        }

        out.println("</body></html>");
    }

    /**
     * Escapa los caracteres que abririan una etiqueta HTML.
     *
     * <p>Proteccion frente a XSS: si una mascota se llamase
     * &lt;script&gt;...&lt;/script&gt;, sin escapar ese texto el navegador lo
     * ejecutaria. Es el mismo principio que la inyeccion SQL del ejercicio 6:
     * un dato del usuario no debe poder convertirse en codigo.</p>
     *
     * @param texto texto a escapar
     * @return texto seguro para insertar en el HTML
     */
    private String escapar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;");
    }
}
