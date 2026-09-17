package presentacion;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * UF2405 - BLOQUE 4 - EJERCICIO 12 (1 de 3): anade un tratamiento a la consulta
 * en curso, guardada en la sesion HTTP.
 *
 * <p>Criterios de evaluacion: CE1.7</p>
 *
 * <p>NO forma parte de la compilacion del proyecto. Ver LEEME.md.</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
@WebServlet("/anadirTratamiento")
public class AnadirTratamientoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /** Nombre del atributo de sesion que guarda la consulta en curso. */
    public static final String CONSULTA_EN_CURSO = "consultaEnCurso";

    /**
     * Apartado 1: anade un tratamiento a la consulta en curso.
     *
     * @param request  peticion
     * @param response respuesta
     * @throws ServletException si falla el procesamiento
     * @throws IOException      si falla la respuesta
     */
    @Override
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Apartado 6: control de acceso. getSession(false) NO crea una sesion
        // si no existe; getSession() si la crearia, y entonces el control no
        // serviria de nada porque siempre habria sesion.
        HttpSession sesion = request.getSession(false);
        if (sesion == null || sesion.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Map<Integer, Integer> consulta =
                (Map<Integer, Integer>) sesion.getAttribute(CONSULTA_EN_CURSO);

        if (consulta == null) {
            // LinkedHashMap y no HashMap: asi los tratamientos se muestran en
            // el orden en que los fue anadiendo el veterinario.
            consulta = new LinkedHashMap<>();
            sesion.setAttribute(CONSULTA_EN_CURSO, consulta);
        }

        try {
            int idTratamiento = Integer.parseInt(request.getParameter("idTratamiento"));
            consulta.merge(idTratamiento, 1, Integer::sum);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Tratamiento no valido");
        }

        response.sendRedirect("verConsulta");
    }

    /*
     * APARTADO 5: por que en la sesion y no en un atributo de instancia.
     *
     * El contenedor crea UNA SOLA instancia de cada Servlet y la usa para
     * atender a todos los usuarios, cada peticion en su propio hilo. Un
     * atributo de instancia seria por tanto compartido por todo el mundo: cada
     * veterinario veria los tratamientos que estan anadiendo los demas, y dos
     * peticiones simultaneas se pisarian los datos, exactamente igual que la
     * condicion de carrera del ejercicio 11 de la UF2404.
     *
     * La sesion, en cambio, es un almacen por usuario: el contenedor identifica
     * al navegador mediante la cookie JSESSIONID y le entrega su propio espacio.
     *
     * Regla practica: en un Servlet, los atributos de instancia solo valen para
     * cosas inmutables y compartidas por todos, como una referencia a un DAO sin
     * estado. Cualquier dato que pertenezca a UN usuario va en la sesion.
     */
}
