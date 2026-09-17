package presentacion;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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
 * UF2405 - BLOQUE 4 - EJERCICIO 11: Procesamiento de formulario con Servlet.
 *
 * <p>Criterios de evaluacion: CE1.8</p>
 *
 * <p>NO forma parte de la compilacion del proyecto. Ver LEEME.md.</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
@WebServlet("/altaMascota")
public class AltaMascotaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Apartado 1: recibe y procesa los datos del formulario del ejercicio 9.
     *
     * @param request  peticion con los parametros del formulario
     * @param response respuesta
     * @throws ServletException si falla el procesamiento
     * @throws IOException      si falla la escritura de la respuesta
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Antes de leer ningun parametro, o los acentos llegaran corruptos.
        request.setCharacterEncoding("UTF-8");

        String nombre = request.getParameter("nombre");
        String especie = request.getParameter("especie");
        String pesoTxt = request.getParameter("peso");
        String fechaTxt = request.getParameter("fechaNac");
        String dni = request.getParameter("dni");

        List<String> errores = new ArrayList<>();

        // ---------------------------------------------------------------
        // Apartado 2: VALIDACION EN SERVIDOR.
        // Se repite entera la del ejercicio 9. No es duplicar por duplicar:
        // la del navegador es una comodidad y se puede saltar; esta es la que
        // de verdad protege los datos. Si solo se pudiera tener una, seria esta.
        // ---------------------------------------------------------------
        if (nombre == null || nombre.trim().isEmpty()) {
            errores.add("El nombre es obligatorio");
        }
        if (dni == null || dni.trim().isEmpty()) {
            errores.add("El DNI del dueno es obligatorio");
        }

        // Apartado 6: todo llega como String. Convertir puede fallar, y si no
        // se controla, un NumberFormatException se convierte en un error 500
        // con traza incluida en el navegador.
        double peso = 0;
        try {
            peso = Double.parseDouble(pesoTxt);
            if (peso <= 0) {
                errores.add("El peso debe ser positivo");
            }
        } catch (NumberFormatException | NullPointerException e) {
            errores.add("El peso debe ser un numero");
        }

        LocalDate fechaNac = null;
        try {
            if (fechaTxt != null && !fechaTxt.isEmpty()) {
                fechaNac = LocalDate.parse(fechaTxt);
                if (fechaNac.isAfter(LocalDate.now())) {
                    errores.add("La fecha de nacimiento no puede ser futura");
                }
            }
        } catch (DateTimeParseException e) {
            errores.add("La fecha de nacimiento no tiene un formato valido");
        }

        if (!errores.isEmpty()) {
            // Apartado 3: se vuelve al formulario CON los valores ya escritos.
            // Devolver el formulario en blanco tras un error es una de las
            // peores cosas que se le pueden hacer a un usuario.
            request.setAttribute("errores", errores);
            request.setAttribute("nombre", nombre);
            request.setAttribute("especie", especie);
            request.setAttribute("peso", pesoTxt);
            request.setAttribute("fechaNac", fechaTxt);
            request.setAttribute("dni", dni);
            // forward y no redirect: aqui interesa CONSERVAR los atributos de
            // la peticion, y con sendRedirect se perderian.
            request.getRequestDispatcher("/WEB-INF/altaMascota.jsp").forward(request, response);
            return;
        }

        // Apartado 4: alta y redireccion al listado.
        try (Connection con = ConexionBD.obtener()) {
            Mascota nueva = new Mascota(nombre.trim(), especie, fechaNac, peso, buscarIdCliente(dni));
            new MascotaDAO(con).insertar(nueva);
            // Apartado 5: patron POST-Redirect-GET.
            response.sendRedirect("mascotas");
        } catch (SQLException e) {
            throw new ServletException("Error al dar de alta la mascota", e);
        }
    }

    /*
     * APARTADO 5: por que sendRedirect y no mostrar el resultado directamente.
     *
     * Si tras el POST se pintara el resultado en la misma respuesta, la ultima
     * peticion del navegador seguiria siendo ese POST. Al pulsar F5, el
     * navegador lo reenvia (con el aviso de "confirmar reenvio del formulario"
     * que todo el mundo ha visto alguna vez) y se crea una mascota duplicada.
     * Lo mismo pasa al pulsar atras.
     *
     * Con sendRedirect el servidor responde 302 y el navegador pide otra
     * direccion con GET. La ultima peticion pasa a ser un GET inofensivo, que
     * se puede recargar todas las veces que se quiera. Eso es el patron
     * POST-Redirect-GET.
     */

    /**
     * Localiza el identificador del cliente a partir de su DNI.
     *
     * @param dni documento del dueno
     * @return identificador del cliente
     */
    private int buscarIdCliente(String dni) {
        // En la aplicacion completa esto lo resuelve ClienteDAO. Se deja aqui
        // reducido para no distraer del objetivo del ejercicio.
        return 1;
    }
}
