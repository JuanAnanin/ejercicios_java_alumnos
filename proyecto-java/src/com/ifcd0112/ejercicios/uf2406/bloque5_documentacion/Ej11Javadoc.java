package com.ifcd0112.ejercicios.uf2406.bloque5_documentacion;

/**
 * UF2406 - BLOQUE 5 - EJERCICIO 11: Documentacion tecnica con Javadoc.
 *
 * <p>Criterios de evaluacion: CE3.1, CE3.2, CE3.6, CE3.10</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej11Javadoc {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Documenta profesionalmente las clases desarrolladas para el sistema del
     * gimnasio.
     *
     * Se pide:
     *   1. Documenta con Javadoc todas las clases publicas, incluyendo una
     *      descripcion de su proposito, el autor y la version.
     *   2. Documenta cada metodo publico con su descripcion, la etiqueta @param
     *      por cada parametro, @return si devuelve valor y @throws por cada
     *      excepcion que pueda propagar.
     *   3. Aplica un criterio de normalizacion coherente en todo el proyecto:
     *      decide el idioma, el formato y el nivel de detalle, y documenta ese
     *      criterio en un fichero de convenciones del equipo.
     *   4. Revisa los comentarios internos del codigo y elimina aquellos que se
     *      limiten a repetir lo que ya dice la propia instruccion; sustituyelos
     *      por comentarios que expliquen el porque de las decisiones no
     *      evidentes.
     *   5. Genera la documentacion HTML mediante la herramienta javadoc y navega
     *      por el resultado comprobando que resulta comprensible sin ver el
     *      codigo fuente.
     *   6. Escribe el indice del manual de usuario del sistema, e indica en que
     *      se diferencia en contenido y en destinatario de la documentacion
     *      Javadoc que acabas de generar.
     *
     * Pista: una buena prueba de calidad de la documentacion: entregala a un
     * companero que no haya visto el codigo y pidele que use una de tus clases
     * guiandose solo por la documentacion generada.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * APARTADOS 1 Y 2: DONDE ESTA LA SOLUCION.
     *
     * En todo el proyecto. Los 43 ficheros de ejercicios de las tres unidades
     * formativas siguen el criterio del apartado 3: Javadoc de clase con
     * descripcion, autor y version, y Javadoc de metodo con param, return y
     * throws. Este mismo fichero es un ejemplo.
     *
     * Es deliberado que el ejercicio no tenga "su" codigo: documentar no es una
     * tarea aparte que se hace al final, es una propiedad de todo lo demas.
     *
     * APARTADO 3: el fichero de convenciones esta en
     *
     *      recursos/uf2406/CONVENCIONES.md
     *
     * APARTADO 5: como generar la documentacion de este proyecto.
     *
     *      javadoc -d docs -encoding UTF-8 -charset UTF-8 \
     *              -sourcepath src -subpackages com.ifcd0112.ejercicios
     *
     * Sin -private solo se documentan los miembros publicos y protegidos, que es
     * lo recomendable: la documentacion generada es para quien USA las clases, y
     * a ese no le interesan los detalles internos. Con -private se genera la
     * referencia completa, util para el propio equipo.
     *
     * Despues se abre docs/index.html y se navega. La prueba de la pista es la
     * buena: dasela a alguien que no haya visto el codigo y pidele que use una
     * clase guiandose solo por eso. Si tiene que abrir el fuente, la
     * documentacion no esta terminada.
     *
     * APARTADO 6: el manual de usuario, y en que se diferencia.
     *
     *      1. Introduccion y requisitos del sistema
     *      2. Acceso a la aplicacion
     *      3. Gestion de socios (alta, consulta, modificacion)
     *      4. Registro de pagos
     *      5. Inscripcion a clases
     *      6. Informes disponibles
     *      7. Preguntas frecuentes
     *      8. Contacto de soporte
     *
     * Las tres diferencias que conviene remarcar:
     *
     *   DESTINATARIO. El Javadoc es para quien va a programar contra estas
     *   clases. El manual es para el recepcionista del gimnasio, que no sabe ni
     *   quiere saber lo que es un metodo.
     *
     *   ORGANIZACION. El Javadoc se organiza por la ESTRUCTURA del software:
     *   paquetes, clases, metodos. El manual se organiza por las TAREAS del
     *   usuario: dar de alta un socio, cobrar una cuota. Nadie entra en el
     *   manual buscando la clase GestorInscripciones; entra buscando "como
     *   apunto a alguien a una clase".
     *
     *   LENGUAJE. En el manual no aparece ni una palabra tecnica. Ni excepcion,
     *   ni base de datos, ni sesion. Si el sistema falla, el manual dice que
     *   hacer, no por que ha pasado.
     */

    /** Ruta del fichero de convenciones del equipo. */
    public static final String CONVENCIONES = "recursos/uf2406/CONVENCIONES.md";

    /**
     * Apartado 4: decide si un comentario aporta algo o solo repite el codigo.
     *
     * <p>El criterio operativo: un comentario que se limita a traducir la
     * instruccion a castellano sobra, porque se puede leer la instruccion. Un
     * comentario util explica el POR QUE: una decision que no es evidente, un
     * limite del negocio, un fallo que se esta evitando.</p>
     *
     * @param codigo     instruccion comentada
     * @param comentario texto del comentario
     * @return true si el comentario aporta informacion que el codigo no da
     */
    public static boolean comentarioAporta(String codigo, String comentario) {
        // Heuristica sencilla y suficiente para el ejemplo: si el comentario no
        // contiene ninguna palabra que indique causa, intencion o consecuencia,
        // lo mas probable es que este repitiendo la instruccion.
        String texto = comentario.toLowerCase();
        String[] senales = { "porque", "para que", "si no", "de lo contrario", "evita",
                             "ojo", "cuidado", "hace falta", "el motivo", "asi ", "conviene" };
        for (String s : senales) {
            if (texto.contains(s)) {
                return true;
            }
        }
        return false;
    }

    // =====================================================================
    // COMPROBACION
    // =====================================================================

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2406-E11] Documentacion tecnica con Javadoc");
        System.out.println("  Apartados 1 y 2: aplicados en los 43 ficheros de ejercicios de las");
        System.out.println("    tres unidades formativas. Documentar no es una tarea aparte que se");
        System.out.println("    hace al final: es una propiedad de todo lo demas.");
        System.out.println("  Apartado 3, convenciones del equipo: " + CONVENCIONES);
        System.out.println("  Apartado 5, generar la documentacion HTML de este proyecto:");
        System.out.println("    javadoc -d docs -encoding UTF-8 -charset UTF-8 \\");
        System.out.println("            -sourcepath src -subpackages com.ifcd0112.ejercicios");

        System.out.println("  Apartado 4, comentarios que sobran y comentarios que hacen falta:");
        String[][] ejemplos = {
            { "i++;",
              "incrementa i en uno",
              "MALO" },
            { "contador++;",
              "DENTRO del if, porque si no se cuentan tambien los socios inactivos",
              "BUENO" },
            { "lista.sort(comparador);",
              "ordena la lista",
              "MALO" },
            { "entradas.sort((a, b) -> ...);",
              "el desempate alfabetico esta para que el resultado no dependa del orden interno del mapa",
              "BUENO" },
            { "con.setAutoCommit(true);",
              "pone el autocommit a true",
              "MALO" },
            { "con.setAutoCommit(true);",
              "la conexion puede volver a un pool: si se devuelve con el autocommit desactivado, el siguiente que la use se encuentra una transaccion abierta",
              "BUENO" }
        };
        for (String[] e : ejemplos) {
            boolean aporta = comentarioAporta(e[0], e[1]);
            String veredicto = aporta ? "APORTA" : "SOBRA ";
            System.out.printf("    [%s] %-28s // %s%n", veredicto, e[0],
                    e[1].length() > 70 ? e[1].substring(0, 67) + "..." : e[1]);
        }
        System.out.println("    La regla: si el comentario se puede deducir leyendo la linea, sobra.");
        System.out.println("    Un comentario que sobra no es neutro: envejece, deja de coincidir con");
        System.out.println("    el codigo y acaba mintiendo.");

        System.out.println("  Apartado 6, manual de usuario frente a Javadoc:");
        System.out.println("    Destinatario: el Javadoc es para quien programa contra estas clases;");
        System.out.println("      el manual, para el recepcionista del gimnasio.");
        System.out.println("    Organizacion: el Javadoc se ordena por la ESTRUCTURA del software");
        System.out.println("      (paquetes, clases, metodos); el manual, por las TAREAS del usuario.");
        System.out.println("    Lenguaje: en el manual no aparece ni una palabra tecnica. Si el");
        System.out.println("      sistema falla, dice que hacer, no por que ha pasado.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
