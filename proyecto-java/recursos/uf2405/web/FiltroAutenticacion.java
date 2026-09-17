package presentacion;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * UF2405 - BLOQUE 5 - EJERCICIO 14: filtro de control de acceso.
 *
 * <p>Criterios de evaluacion: CE1.1, CE1.8</p>
 *
 * <p>NO forma parte de la compilacion del proyecto. Ver LEEME.md.</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
// Apartado 2: un unico punto que protege TODO lo que cuelgue de /privado/.
// Repetir el control en cada Servlet funciona hasta que alguien anade el
// numero doce y se olvida; el filtro no se puede olvidar.
@WebFilter("/privado/*")
public class FiltroAutenticacion implements Filter {

    /**
     * Deja pasar la peticion solo si hay sesion iniciada.
     *
     * @param req   peticion
     * @param res   respuesta
     * @param chain resto de la cadena de filtros
     * @throws IOException      si falla la respuesta
     * @throws ServletException si falla el procesamiento
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest peticion = (HttpServletRequest) req;
        // getSession(false): no crear sesion si no la hay. Con getSession() se
        // crearia una vacia y el control seguiria funcionando por el segundo
        // if, pero se estaria creando una sesion por cada visita anonima.
        HttpSession sesion = peticion.getSession(false);

        if (sesion == null || sesion.getAttribute("usuario") == null) {
            ((HttpServletResponse) res).sendRedirect(peticion.getContextPath() + "/login.jsp");
            return;
        }

        chain.doFilter(req, res);   // sesion valida: que siga su camino
    }
}
