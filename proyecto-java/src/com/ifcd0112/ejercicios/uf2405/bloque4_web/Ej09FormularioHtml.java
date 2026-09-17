package com.ifcd0112.ejercicios.uf2405.bloque4_web;

/**
 * UF2405 - BLOQUE 4 - EJERCICIO 9: Formulario HTML con validacion en cliente.
 *
 * <p>Criterios de evaluacion: CE1.5, CE1.6</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej09FormularioHtml {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Disena la pagina de alta de nuevas mascotas de la clinica, aplicando
     * validacion en el navegador antes de enviar los datos al servidor.
     *
     * Se pide:
     *   1. Construye un formulario HTML con los campos: nombre, especie (lista
     *      desplegable), fecha de nacimiento, peso y DNI del dueno.
     *   2. El formulario debe enviarse por POST a la direccion altaMascota.
     *   3. Escribe una funcion JavaScript que valide, antes del envio, que el
     *      nombre no este vacio, que el peso sea un numero positivo y que la
     *      fecha de nacimiento no sea posterior al dia de hoy.
     *   4. Si alguna validacion falla, muestra el mensaje de error junto al
     *      campo correspondiente y cancela el envio.
     *   5. Aplica el principio de prevencion de errores manteniendo el boton de
     *      envio deshabilitado mientras los campos obligatorios esten vacios.
     *   6. Explica en un comentario por que esta validacion en el cliente NO
     *      exime de validar tambien en el servidor.
     *
     * Pista: la validacion en cliente mejora la experiencia de usuario, pero
     * cualquiera puede saltarsela desactivando JavaScript o enviando la peticion
     * directamente: la validacion del servidor es la unica que realmente protege
     * los datos.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * DONDE ESTA LA SOLUCION.
     *
     * Este ejercicio produce una pagina HTML con JavaScript, no codigo Java. El
     * fichero completo, comentado apartado por apartado, esta en:
     *
     *      recursos/uf2405/web/altaMascota.html
     *
     * Se abre directamente en el navegador para probar la validacion. Para que
     * el envio funcione hace falta desplegar la aplicacion web con el Servlet
     * del ejercicio 11; ver recursos/uf2405/web/LEEME.md.
     *
     * APARTADO 2: por que POST y no GET.
     *
     * Esta peticion CAMBIA el estado del servidor: crea una mascota. Con GET,
     * los datos irian en la URL, quedarian en el historial del navegador y en
     * los registros del servidor, y la pagina seria recargable, con lo que un
     * F5 crearia duplicados. La regla es sencilla: GET para pedir informacion,
     * POST para provocar un cambio.
     *
     * APARTADO 5: prevencion de errores.
     *
     * El boton nace deshabilitado y solo se habilita cuando estan los campos
     * obligatorios. Es uno de los principios clasicos de usabilidad: mejor
     * impedir el error que informar de el una vez cometido. El detalle
     * importante es que deshabilitar el boton NO sustituye a la validacion:
     * hace falta ademas la funcion validar(), porque el usuario puede rellenar
     * el nombre y despues escribir un peso negativo.
     *
     * APARTADO 6: por que esto no exime de validar en el servidor.
     *
     * Porque la validacion en cliente se ejecuta en una maquina que no
     * controlamos. Todas estas formas de saltarsela funcionan:
     *
     *   - Desactivar JavaScript en el navegador.
     *   - Abrir las herramientas de desarrollo, quitar el atributo disabled del
     *     boton o cambiar el onsubmit. Son dos clics.
     *   - No usar el formulario en absoluto y enviar la peticion a mano:
     *         curl -X POST -d "nombre=&peso=-5" http://servidor/altaMascota
     *   - Un cliente que no sea un navegador: una aplicacion movil, un script,
     *     una herramienta de pruebas.
     *
     * Y hay un motivo que no es de seguridad y se olvida a menudo: hay
     * validaciones que el cliente no PUEDE hacer, porque necesitan datos que
     * solo estan en el servidor. Comprobar que el DNI del dueno existe, que no
     * hay ya una mascota con ese nombre para ese cliente, o que el usuario tiene
     * permiso para dar altas, exige mirar la base de datos.
     *
     * El reparto correcto es: en cliente por comodidad, en servidor por
     * seguridad. Siempre las dos. Y si solo se pudiera tener una, la del
     * servidor, que es la que esta en el ejercicio 11.
     */

    /** Ruta del formulario que resuelve el ejercicio. */
    public static final String PAGINA = "recursos/uf2405/web/altaMascota.html";

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2405-E09] Formulario HTML con validacion en cliente");
        System.out.println("  Formulario completo, con la validacion y el boton deshabilitado:");
        System.out.println("    " + PAGINA);
        System.out.println("  Apartado 6: la validacion en cliente se ejecuta en una maquina que");
        System.out.println("    no controlamos. Se salta desactivando JavaScript, editando la");
        System.out.println("    pagina desde el navegador o enviando la peticion con curl. Ademas,");
        System.out.println("    hay comprobaciones que el cliente no puede hacer porque necesitan");
        System.out.println("    la base de datos. En cliente por comodidad, en servidor por");
        System.out.println("    seguridad: la del servidor esta en el ejercicio 11.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
