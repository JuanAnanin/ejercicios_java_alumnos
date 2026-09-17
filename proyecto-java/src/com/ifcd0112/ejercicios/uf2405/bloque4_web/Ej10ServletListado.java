package com.ifcd0112.ejercicios.uf2405.bloque4_web;

/**
 * UF2405 - BLOQUE 4 - EJERCICIO 10: Servlet de listado con acceso a base de datos.
 *
 * <p>Criterios de evaluacion: CE1.8</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej10ServletListado {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Desarrolla un Servlet que muestre en el navegador el catalogo completo de
     * mascotas de la clinica, obtenido de la base de datos.
     *
     * Se pide:
     *   1. Crea una clase que extienda HttpServlet y sobrescriba el metodo
     *      doGet().
     *   2. Registra el Servlet en la direccion /mascotas mediante la anotacion
     *      correspondiente.
     *   3. Reutiliza el MascotaDAO del ejercicio 7: el Servlet NO debe contener
     *      ninguna sentencia SQL.
     *   4. Genera como respuesta una pagina HTML con una tabla que muestre los
     *      datos de todas las mascotas.
     *   5. Anade a cada fila un enlace que permita ver el detalle de esa mascota
     *      concreta, pasando su identificador como parametro en la URL.
     *   6. Gestiona el caso de que se produzca un error de acceso a datos
     *      mostrando un mensaje comprensible al usuario, nunca la traza de la
     *      excepcion.
     *
     * Pista: recuerda establecer el tipo de contenido y la codificacion de la
     * respuesta con setContentType("text/html;charset=UTF-8") antes de escribir
     * nada, o los acentos se mostraran incorrectamente.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * DONDE ESTA LA SOLUCION.
     *
     * Un Servlet necesita jakarta.servlet, que no forma parte del JDK, y un
     * contenedor web para ejecutarse. Este proyecto es de dependencias cero, asi
     * que el Servlet completo esta en:
     *
     *      recursos/uf2405/web/ListadoMascotasServlet.java
     *
     * Instrucciones de despliegue en recursos/uf2405/web/LEEME.md.
     *
     * LO QUE HAY QUE MIRAR AL CORREGIR.
     *
     * Apartado 3, ni una sentencia SQL en el Servlet. Es el punto que mas se
     * incumple. Poner el SQL en el doGet funciona igual de bien el primer dia y
     * es una trampa a plazo fijo: cuando haya que cambiar la consulta habra que
     * buscarla por todos los Servlets, y esa misma consulta no se podra
     * reutilizar desde ningun otro sitio.
     *
     * La pista, setContentType antes de escribir. La cabecera HTTP se envia
     * junto con los primeros bytes del cuerpo. Si se escribe primero y se
     * declara la codificacion despues, la cabecera ya ha salido y esa llamada
     * no hace nada. El sintoma es siempre el mismo: acentos raros que "a veces
     * funcionan" segun el navegador.
     *
     * Apartado 6, nunca la traza al usuario. Un printStackTrace en la respuesta
     * ensena nombres de tablas, rutas del servidor y version del SGBD. Es
     * informacion de regalo para quien quiera atacar la aplicacion. Al usuario,
     * un mensaje que pueda entender; el detalle, al log del servidor.
     *
     * XSS: por que existe el metodo escapar(). El nombre de la mascota sale de
     * la base de datos y acaba dentro del HTML. Si alguien consiguio guardar
     * ahi una etiqueta script, el navegador la ejecutaria. Es el mismo error de
     * fondo que la inyeccion SQL del ejercicio 6: un dato que acaba
     * interpretandose como codigo. El metodo resolver() lo demuestra.
     */

    /** Ruta del Servlet que resuelve el ejercicio. */
    public static final String SERVLET = "recursos/uf2405/web/ListadoMascotasServlet.java";

    /**
     * La misma proteccion frente a XSS que lleva el Servlet.
     *
     * <p>Se reproduce aqui para poder demostrarla ejecutando el bloque, ya que
     * el Servlet no se puede ejecutar sin contenedor.</p>
     *
     * @param texto texto procedente de la base de datos
     * @return texto seguro para insertar en el HTML
     */
    public static String escapar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;");
    }

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2405-E10] Servlet de listado con acceso a base de datos");
        System.out.println("  Servlet completo:");
        System.out.println("    " + SERVLET);
        System.out.println("  Puntos clave: ninguna sentencia SQL en el Servlet (usa el DAO del");
        System.out.println("    ejercicio 7); setContentType ANTES de escribir nada; al usuario un");
        System.out.println("    mensaje comprensible, nunca la traza de la excepcion.");

        // Demostracion del escapado: es codigo Java corriente y si se puede
        // ejecutar aqui.
        String nombreMalicioso = "<script>alert('XSS')</script>";
        System.out.println("  Proteccion frente a XSS, con un nombre de mascota manipulado:");
        System.out.println("    Sin escapar: <td>" + nombreMalicioso + "</td>");
        System.out.println("      -> el navegador ejecutaria ese script");
        System.out.println("    Escapado:    <td>" + escapar(nombreMalicioso) + "</td>");
        System.out.println("      -> el navegador lo pinta como texto, que es lo que es");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
