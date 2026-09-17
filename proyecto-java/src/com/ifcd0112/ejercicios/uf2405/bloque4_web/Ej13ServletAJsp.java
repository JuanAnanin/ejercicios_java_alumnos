package com.ifcd0112.ejercicios.uf2405.bloque4_web;

/**
 * UF2405 - BLOQUE 4 - EJERCICIO 13: De Servlet a JSP, separando la presentacion.
 *
 * <p>Criterios de evaluacion: CE1.4, CE1.8</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej13ServletAJsp {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * El Servlet del ejercicio 10 genera el HTML mediante instrucciones
     * println, lo que resulta dificil de mantener. Refactoriza la solucion
     * aplicando correctamente la arquitectura en tres capas.
     *
     * Se pide:
     *   1. Modifica el Servlet para que se limite a obtener los datos mediante
     *      el DAO y a depositarlos como atributo de la peticion.
     *   2. Traslada toda la generacion del HTML a una pagina JSP independiente.
     *   3. Redirige internamente del Servlet al JSP mediante
     *      RequestDispatcher.forward(), y explica en que se diferencia de
     *      sendRedirect().
     *   4. En el JSP, recorre la lista recibida para generar las filas de la
     *      tabla, sin incluir ninguna sentencia SQL ni ninguna llamada al DAO.
     *   5. Identifica por escrito que parte del codigo resultante pertenece a
     *      cada una de las tres capas (presentacion, aplicacion y datos).
     *   6. Justifica que ventaja concreta de mantenimiento aporta esta
     *      separacion frente a la version original del ejercicio 10.
     *
     * Pista: la diferencia clave: forward() ocurre integramente en el servidor y
     * el navegador no se entera (la URL no cambia); sendRedirect() envia una
     * respuesta al navegador para que solicite otra direccion distinta.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * DONDE ESTA LA SOLUCION.
     *
     *      recursos/uf2405/web/ListadoMascotasServletMvc.java   (apartados 1 y 3)
     *      recursos/uf2405/web/mascotas.jsp                     (apartados 2 y 4)
     *
     * Comparese el Servlet nuevo con ListadoMascotasServlet.java, el del
     * ejercicio 10: el metodo doGet pasa de treinta lineas a seis, y no queda ni
     * un println de HTML.
     *
     * APARTADO 3: forward() frente a sendRedirect().
     *
     *   forward()
     *       Ocurre entero DENTRO del servidor. El navegador hizo UNA peticion y
     *       recibe UNA respuesta; no se entera de que por dentro han
     *       intervenido dos componentes. La URL del navegador no cambia y los
     *       atributos de la peticion siguen ahi, que es precisamente lo que
     *       necesita el JSP para encontrar la lista "mascotas". Una sola ida y
     *       vuelta por la red.
     *
     *   sendRedirect()
     *       El servidor responde 302 con una direccion y el navegador hace una
     *       peticion NUEVA a esa direccion. La URL cambia y los atributos de la
     *       peticion original se pierden, porque aquella peticion ya termino.
     *       Dos idas y vueltas por la red.
     *
     *   Cuando cada uno: forward para repartir el trabajo de una misma peticion
     *   entre varios componentes del servidor. Redirect despues de una
     *   operacion que modifica datos, para que la ultima peticion del navegador
     *   sea un GET recargable, que es el POST-Redirect-GET del ejercicio 11.
     *
     * APARTADO 5: reparto por capas del codigo resultante.
     *
     *   PRESENTACION   mascotas.jsp. Solo decide como se ve. No sabe de donde
     *                  salen los datos ni le importa.
     *   APLICACION     ListadoMascotasServletMvc. Coordina: recibe la peticion,
     *                  pide los datos y elige la vista. Ni pinta ni consulta.
     *   DATOS          MascotaDAO y ConexionBD. Saben de SQL y de conexiones.
     *                  No saben que existe la web.
     *
     *   La regla que ordena esto: las dependencias van hacia abajo. Presentacion
     *   conoce aplicacion, aplicacion conoce datos, y datos no conoce a nadie.
     *   Si una clase del paquete datos importa algo de jakarta.servlet, la capa
     *   se ha roto y ya no se puede reutilizar fuera de la web.
     *
     * APARTADO 6: la ventaja concreta de mantenimiento.
     *
     *   Lo concreto: cambiar el aspecto de la pagina ya no exige recompilar. En
     *   la version del ejercicio 10, mover una columna significaba editar codigo
     *   Java, compilar y volver a desplegar. Ahora se edita un fichero de texto.
     *
     *   Dos consecuencias practicas mas, que se notan enseguida:
     *
     *     - El HTML deja de estar escondido dentro de cadenas de Java, donde no
     *       hay coloreado de sintaxis, hay que escapar las comillas y una
     *       etiqueta sin cerrar no la ve nadie hasta que la pagina sale torcida.
     *     - El trabajo se puede repartir: quien maquete la pagina no necesita
     *       saber Java ni tocar el proyecto.
     *
     *   Y una ventaja de seguridad que suele pasar desapercibida: en el JSP se
     *   usa c:out, que escapa el contenido por defecto. En la version de
     *   println habia que acordarse de llamar a escapar() en cada campo, y
     *   olvidarse en uno solo ya abre la puerta al XSS.
     *
     * DETALLE QUE CONVIENE EXPLICAR: por que el JSP vive bajo WEB-INF.
     *
     * El contenedor no sirve directamente lo que hay dentro de WEB-INF. Si el
     * JSP estuviera fuera, cualquiera podria pedir mascotas.jsp por la URL,
     * saltarse el Servlet que carga los datos y ver la pagina vacia o, en una
     * aplicacion con control de acceso, saltarse tambien ese control. Poniendolo
     * bajo WEB-INF, la unica forma de llegar a la vista es a traves del Servlet.
     *
     * AVISO DE VERSION. El JSP usa la libreria estandar JSTL con el espacio de
     * nombres jakarta.tags.core, que es el de Jakarta EE 9 y posteriores
     * (Tomcat 10 o superior). Con Tomcat 9 o anterior el uri es
     * http://java.sun.com/jsp/jstl/core y los import son javax.servlet. Es el
     * error de despliegue mas frecuente de este bloque: el sintoma es que las
     * etiquetas c:forEach salen impresas tal cual en la pagina.
     */

    /** Ruta del Servlet refactorizado. */
    public static final String SERVLET = "recursos/uf2405/web/ListadoMascotasServletMvc.java";

    /** Ruta de la vista JSP. */
    public static final String VISTA = "recursos/uf2405/web/mascotas.jsp";

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2405-E13] De Servlet a JSP: separando la presentacion");
        System.out.println("  Servlet refactorizado: " + SERVLET);
        System.out.println("  Vista JSP:             " + VISTA);
        System.out.println("  Apartado 3: forward ocurre dentro del servidor, la URL no cambia y");
        System.out.println("    los atributos de la peticion siguen disponibles (por eso el JSP");
        System.out.println("    encuentra la lista). sendRedirect provoca una peticion NUEVA del");
        System.out.println("    navegador: cambia la URL y se pierden esos atributos.");
        System.out.println("  Apartado 5: JSP = presentacion, Servlet = aplicacion, DAO = datos.");
        System.out.println("    Las dependencias van hacia abajo y datos no conoce a nadie.");
        System.out.println("  Apartado 6: cambiar el aspecto ya no exige recompilar ni desplegar,");
        System.out.println("    el HTML sale de dentro de cadenas Java y c:out escapa por defecto,");
        System.out.println("    con lo que el XSS deja de depender de acordarse en cada campo.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
