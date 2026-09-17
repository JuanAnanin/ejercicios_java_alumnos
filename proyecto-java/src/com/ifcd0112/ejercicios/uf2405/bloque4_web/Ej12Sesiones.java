package com.ifcd0112.ejercicios.uf2405.bloque4_web;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * UF2405 - BLOQUE 4 - EJERCICIO 12: Sesiones, carrito de tratamientos.
 *
 * <p>Criterios de evaluacion: CE1.7</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej12Sesiones {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * La clinica quiere que el veterinario pueda ir anadiendo tratamientos a una
     * consulta a lo largo de varias pantallas, antes de confirmarla
     * definitivamente.
     *
     * Se pide:
     *   1. Implementa un Servlet que anada un tratamiento a la consulta en
     *      curso, almacenandola en la sesion HTTP del usuario.
     *   2. Implementa un segundo Servlet que muestre el contenido actual de la
     *      consulta en curso, con el importe total acumulado.
     *   3. Implementa un tercer Servlet que confirme la consulta, la registre en
     *      la base de datos mediante la transaccion del ejercicio 8 y vacie la
     *      sesion.
     *   4. Comprueba, abriendo la aplicacion simultaneamente en dos navegadores
     *      distintos, que cada uno mantiene su propia consulta en curso de forma
     *      independiente.
     *   5. Explica en un comentario por que estos datos deben guardarse en la
     *      sesion y no en un atributo de instancia del Servlet.
     *   6. Anade un control que impida acceder a estos Servlets si no existe una
     *      sesion de usuario iniciada, redirigiendo a la pantalla de acceso.
     *
     * Pista: para comprobar el aislamiento entre sesiones no basta con abrir dos
     * pestanas del mismo navegador (comparten cookies): usa dos navegadores
     * distintos o una ventana de incognito.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * DONDE ESTA LA SOLUCION.
     *
     * Los tres Servlets estan en:
     *
     *      recursos/uf2405/web/AnadirTratamientoServlet.java     (apartado 1)
     *      recursos/uf2405/web/VerConsultaServlet.java           (apartado 2)
     *      recursos/uf2405/web/ConfirmarConsultaServlet.java     (apartado 3)
     *
     * Lo que si se puede ejecutar aqui es la DEMOSTRACION del apartado 4 y del
     * apartado 5, porque el problema de fondo no tiene nada que ver con la web:
     * es el mismo de la concurrencia del ejercicio 11 de la UF2404. Un Servlet
     * es un objeto compartido por todos los usuarios; la sesion es un almacen
     * por usuario. Abajo se simula con dos "sesiones" y un atributo compartido.
     *
     * APARTADO 5: por que en la sesion y no en un atributo de instancia.
     *
     * El contenedor crea UNA SOLA instancia de cada Servlet y la reutiliza para
     * atender a todos los usuarios, cada peticion en su propio hilo. Un atributo
     * de instancia seria por tanto compartido por todo el mundo:
     *
     *   - Cada veterinario veria los tratamientos anadidos por los demas.
     *   - Al confirmar, uno registraria la consulta de otro.
     *   - Y como ademas hay varios hilos escribiendo a la vez sobre la misma
     *     estructura, aparece la condicion de carrera de siempre: un HashMap
     *     modificado por dos hilos simultaneos puede corromperse.
     *
     * La sesion, en cambio, es un almacen por usuario. El contenedor identifica
     * al navegador por la cookie JSESSIONID y le entrega su propio espacio.
     *
     * Regla practica: en un Servlet, los atributos de instancia solo valen para
     * cosas inmutables y compartidas, como una referencia a un DAO sin estado.
     * Cualquier dato que pertenezca a UN usuario va en la sesion.
     *
     * APARTADO 6: el control de acceso.
     *
     * request.getSession(false) devuelve la sesion existente o null, sin crear
     * ninguna. Es lo que hay que usar para comprobar: con getSession() se
     * crearia una sesion vacia en cada visita anonima. En la aplicacion completa
     * (ejercicio 14) este control se centraliza en un filtro, para no tener que
     * acordarse de repetirlo en cada Servlet nuevo.
     *
     * SOBRE LA PISTA: dos pestanas no valen.
     *
     * Las pestanas del mismo navegador comparten el almacen de cookies, y por
     * tanto comparten JSESSIONID: para el servidor son el mismo usuario. Para
     * ver dos sesiones de verdad hace falta otro navegador o una ventana de
     * incognito. Es un detalle que conviene avisar antes, porque si no el
     * alumno concluye que su codigo esta mal cuando en realidad esta bien.
     */

    /** Tarifario de ejemplo: id de tratamiento y precio. */
    private static final Map<Integer, String> NOMBRES = new LinkedHashMap<>();

    /** Precios de los tratamientos del tarifario. */
    private static final Map<Integer, Double> PRECIOS = new LinkedHashMap<>();

    static {
        NOMBRES.put(1, "Consulta general");
        NOMBRES.put(2, "Vacuna polivalente");
        NOMBRES.put(3, "Extraccion dental");
        PRECIOS.put(1, 25.00);
        PRECIOS.put(2, 38.50);
        PRECIOS.put(3, 120.00);
    }

    /**
     * Version reducida de HttpSession: un almacen de atributos por usuario.
     *
     * <p>Sirve para demostrar el aislamiento sin levantar un contenedor.</p>
     */
    public static class SesionSimulada {

        private final String id;
        private final Map<String, Object> atributos = new LinkedHashMap<>();

        /**
         * @param id identificador de la sesion, como el JSESSIONID
         */
        public SesionSimulada(String id) {
            this.id = id;
        }

        /** @return identificador de la sesion */
        public String getId() { return id; }

        /**
         * @param nombre nombre del atributo
         * @return valor guardado, o null
         */
        public Object getAttribute(String nombre) { return atributos.get(nombre); }

        /**
         * @param nombre nombre del atributo
         * @param valor  valor a guardar
         */
        public void setAttribute(String nombre, Object valor) { atributos.put(nombre, valor); }

        /**
         * @param nombre nombre del atributo a eliminar
         */
        public void removeAttribute(String nombre) { atributos.remove(nombre); }
    }

    /**
     * Apartado 1: anade un tratamiento a la consulta en curso de esa sesion.
     *
     * @param sesion        sesion del usuario
     * @param idTratamiento tratamiento a anadir
     * @return true si se anadio; false si no hay usuario en sesion
     */
    @SuppressWarnings("unchecked")
    public static boolean anadirTratamiento(SesionSimulada sesion, int idTratamiento) {
        // Apartado 6: sin usuario en sesion, no se hace nada y se redirige.
        if (sesion.getAttribute("usuario") == null) {
            return false;
        }
        Map<Integer, Integer> consulta = (Map<Integer, Integer>) sesion.getAttribute("consultaEnCurso");
        if (consulta == null) {
            // LinkedHashMap para conservar el orden en que se fueron anadiendo.
            consulta = new LinkedHashMap<>();
            sesion.setAttribute("consultaEnCurso", consulta);
        }
        consulta.merge(idTratamiento, 1, Integer::sum);
        return true;
    }

    /**
     * Apartado 2: importe total acumulado de la consulta en curso.
     *
     * @param sesion sesion del usuario
     * @return importe en euros
     */
    @SuppressWarnings("unchecked")
    public static double totalConsulta(SesionSimulada sesion) {
        Map<Integer, Integer> consulta = (Map<Integer, Integer>) sesion.getAttribute("consultaEnCurso");
        if (consulta == null) {
            return 0;
        }
        double total = 0;
        for (Map.Entry<Integer, Integer> linea : consulta.entrySet()) {
            total += PRECIOS.get(linea.getKey()) * linea.getValue();
        }
        return total;
    }

    /**
     * Describe el contenido de la consulta en curso.
     *
     * @param sesion sesion del usuario
     * @return descripcion legible
     */
    @SuppressWarnings("unchecked")
    public static String describir(SesionSimulada sesion) {
        Map<Integer, Integer> consulta = (Map<Integer, Integer>) sesion.getAttribute("consultaEnCurso");
        if (consulta == null || consulta.isEmpty()) {
            return "(vacia)";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Integer> linea : consulta.entrySet()) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(NOMBRES.get(linea.getKey())).append(" x").append(linea.getValue());
        }
        return sb.toString();
    }

    // =====================================================================
    // COMPROBACION (apartados 4, 5 y 6)
    // =====================================================================

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2405-E12] Sesiones: carrito de tratamientos");
        System.out.println("  Servlets completos en recursos/uf2405/web/:");
        System.out.println("    AnadirTratamientoServlet.java, VerConsultaServlet.java,");
        System.out.println("    ConfirmarConsultaServlet.java");

        // Apartado 6: sin sesion iniciada no se puede operar.
        SesionSimulada anonimo = new SesionSimulada("JSESSIONID-000");
        System.out.println("  Apartado 6, control de acceso:");
        System.out.println("    Usuario sin sesion iniciada anade un tratamiento -> "
                + (anadirTratamiento(anonimo, 1) ? "PERMITIDO (mal)" : "rechazado, se redirige a login"));

        // Apartado 4: dos usuarios, dos sesiones, dos consultas independientes.
        SesionSimulada elena = new SesionSimulada("JSESSIONID-A1B2");
        elena.setAttribute("usuario", "Dra. Elena Vidal");
        SesionSimulada pablo = new SesionSimulada("JSESSIONID-C3D4");
        pablo.setAttribute("usuario", "Dr. Pablo Serra");

        anadirTratamiento(elena, 1);
        anadirTratamiento(elena, 2);
        anadirTratamiento(pablo, 3);
        anadirTratamiento(elena, 1);   // repite el primero: debe sumar cantidad

        System.out.println("  Apartado 4, dos sesiones simultaneas:");
        System.out.printf("    [%s] %s -> %s   total %.2f EUR%n",
                elena.getId(), elena.getAttribute("usuario"), describir(elena), totalConsulta(elena));
        System.out.printf("    [%s] %s -> %s   total %.2f EUR%n",
                pablo.getId(), pablo.getAttribute("usuario"), describir(pablo), totalConsulta(pablo));
        System.out.println("    Cada uno tiene la suya: es lo que se comprueba con dos navegadores");
        System.out.println("    distintos (dos pestanas no valen, comparten la cookie).");

        // Apartado 5: que pasaria con un atributo de instancia del Servlet.
        Map<Integer, Integer> atributoDeInstancia = new LinkedHashMap<>();
        atributoDeInstancia.merge(1, 1, Integer::sum);   // lo anade Elena
        atributoDeInstancia.merge(2, 1, Integer::sum);   // lo anade Elena
        atributoDeInstancia.merge(3, 1, Integer::sum);   // lo anade Pablo
        System.out.println("  Apartado 5, lo que pasaria con un atributo de instancia:");
        System.out.println("    Consulta unica compartida por los dos: " + atributoDeInstancia.size()
                + " tratamientos mezclados de ambos veterinarios.");
        System.out.println("    El contenedor crea UNA instancia del Servlet para todos los usuarios,");
        System.out.println("    asi que ese atributo seria comun. Ademas, con varios hilos");
        System.out.println("    escribiendo a la vez, la estructura puede corromperse.");

        // Apartado 3: al confirmar, se vacia la sesion.
        elena.removeAttribute("consultaEnCurso");
        System.out.println("  Apartado 3, tras confirmar y hacer commit se vacia la sesion:");
        System.out.println("    Consulta de Elena: " + describir(elena));
        System.out.println("    Consulta de Pablo: " + describir(pablo) + "  (intacta)");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
