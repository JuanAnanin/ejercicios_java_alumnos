package com.ifcd0112.ejercicios.uf2405.bloque4_web;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * UF2405 - BLOQUE 4 - EJERCICIO 11: Procesamiento de formulario con Servlet.
 *
 * <p>Criterios de evaluacion: CE1.8</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej11ServletFormulario {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Implementa el Servlet que recibe y procesa los datos del formulario de
     * alta de mascotas creado en el ejercicio 9.
     *
     * Se pide:
     *   1. Sobrescribe el metodo doPost() y recupera todos los parametros del
     *      formulario.
     *   2. Valida en el servidor todos los datos recibidos, sin confiar en la
     *      validacion de JavaScript: comprueba obligatoriedad, formato numerico
     *      del peso y coherencia de la fecha.
     *   3. Si algun dato es invalido, vuelve a mostrar el formulario indicando
     *      el error, conservando los valores que el usuario ya habia introducido
     *      correctamente.
     *   4. Si los datos son validos, da de alta la mascota mediante el DAO y
     *      redirige al listado del ejercicio 10.
     *   5. Utiliza sendRedirect() tras el alta y explica en un comentario por
     *      que es preferible a mostrar directamente la pagina de resultado
     *      (piensa en que ocurre si el usuario recarga la pagina).
     *   6. Convierte adecuadamente los tipos: los parametros llegan siempre como
     *      String y deben transformarse a los tipos correspondientes controlando
     *      posibles errores de conversion.
     *
     * Pista: si tras un alta se muestra directamente el resultado sin redirigir,
     * al pulsar F5 el navegador reenviara el formulario y creara un registro
     * duplicado. La redireccion evita ese problema.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * DONDE ESTA LA SOLUCION.
     *
     * El Servlet completo esta en:
     *
     *      recursos/uf2405/web/AltaMascotaServlet.java
     *
     * La parte del apartado 2, la validacion, es Java corriente y no necesita
     * ningun contenedor: se reproduce aqui abajo tal cual, para poder
     * ejecutarla y comprobar que hace lo que dice. Es ademas una buena
     * costumbre profesional: si la validacion esta en un metodo que solo
     * necesita los valores, se puede probar sin levantar el servidor.
     *
     * APARTADO 5: por que sendRedirect y no pintar el resultado.
     *
     * Si tras el POST se pintara el resultado en la misma respuesta, la ultima
     * peticion que ha hecho el navegador seguiria siendo ese POST. Al pulsar F5
     * el navegador lo reenvia, con ese aviso de "confirmar reenvio del
     * formulario" que todo el mundo ha visto, y se crea una mascota duplicada.
     * Lo mismo ocurre al pulsar atras y adelante.
     *
     * Con sendRedirect el servidor responde 302 y el navegador pide otra
     * direccion con GET. La ultima peticion pasa a ser un GET, que se puede
     * recargar todas las veces que haga falta sin consecuencias. Ese es el
     * patron POST-Redirect-GET.
     *
     * Matiz que conviene explicar: en el caso de ERROR (apartado 3) se hace lo
     * contrario, un forward y no un redirect. El motivo es que ahi interesa
     * conservar los atributos de la peticion, que son los que llevan los
     * mensajes de error y los valores ya escritos por el usuario. Con
     * sendRedirect empieza una peticion nueva y esos atributos se pierden.
     *
     * APARTADO 6: la conversion de tipos.
     *
     * request.getParameter devuelve siempre String, o null si el parametro no
     * viene. Los dos casos hay que controlarlos:
     *
     *   - Double.parseDouble(null)  lanza NullPointerException.
     *   - Double.parseDouble("dos") lanza NumberFormatException.
     *   - LocalDate.parse("32/13/2026") lanza DateTimeParseException.
     *
     * Sin capturarlas, cualquiera de las tres se convierte en un error 500 con
     * la traza en el navegador, que es justo lo que el ejercicio 10 pedia
     * evitar. Y no hace falta un atacante: basta con que el usuario escriba una
     * coma en lugar de un punto.
     */

    /** Ruta del Servlet que resuelve el ejercicio. */
    public static final String SERVLET = "recursos/uf2405/web/AltaMascotaServlet.java";

    /**
     * Apartados 2 y 6: la validacion del servidor, aislada del Servlet.
     *
     * <p>Recibe los valores tal y como llegarian de request.getParameter, es
     * decir, como String o como null.</p>
     *
     * @param nombre   nombre de la mascota
     * @param especie  especie elegida
     * @param pesoTxt  peso, tal cual llega del formulario
     * @param fechaTxt fecha de nacimiento en formato ISO, tal cual llega
     * @param dni      documento del dueno
     * @return lista de errores; vacia si todo es correcto
     */
    public static List<String> validar(String nombre, String especie, String pesoTxt,
                                       String fechaTxt, String dni) {
        List<String> errores = new ArrayList<>();

        // Obligatoriedad. Ojo al trim: una cadena de espacios no es vacia para
        // isEmpty(), y sin embargo es exactamente igual de inutil como nombre.
        if (nombre == null || nombre.trim().isEmpty()) {
            errores.add("El nombre es obligatorio");
        }
        if (dni == null || dni.trim().isEmpty()) {
            errores.add("El DNI del dueno es obligatorio");
        }
        if (especie == null || especie.trim().isEmpty()) {
            errores.add("La especie es obligatoria");
        }

        // Formato numerico. Se capturan las DOS excepciones posibles.
        try {
            double peso = Double.parseDouble(pesoTxt);
            if (peso <= 0) {
                errores.add("El peso debe ser positivo");
            }
        } catch (NumberFormatException | NullPointerException e) {
            errores.add("El peso debe ser un numero");
        }

        // Coherencia de la fecha.
        try {
            if (fechaTxt != null && !fechaTxt.isEmpty()) {
                if (LocalDate.parse(fechaTxt).isAfter(LocalDate.now())) {
                    errores.add("La fecha de nacimiento no puede ser futura");
                }
            }
        } catch (DateTimeParseException e) {
            errores.add("La fecha de nacimiento no tiene un formato valido");
        }

        return errores;
    }

    // =====================================================================
    // COMPROBACION
    // =====================================================================

    /**
     * Ejecuta una validacion y muestra el resultado.
     *
     * @param titulo   descripcion del caso
     * @param nombre   nombre de la mascota
     * @param especie  especie
     * @param pesoTxt  peso como texto
     * @param fechaTxt fecha como texto
     * @param dni      documento del dueno
     */
    private static void probar(String titulo, String nombre, String especie,
                               String pesoTxt, String fechaTxt, String dni) {
        List<String> errores = validar(nombre, especie, pesoTxt, fechaTxt, dni);
        System.out.println("    " + titulo);
        if (errores.isEmpty()) {
            System.out.println("      Aceptado: se da de alta y se redirige al listado");
        } else {
            for (String error : errores) {
                System.out.println("      Rechazado: " + error);
            }
        }
    }

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2405-E11] Procesamiento de formulario con Servlet");
        System.out.println("  Servlet completo:");
        System.out.println("    " + SERVLET);
        System.out.println("  Validacion del servidor en funcionamiento (apartados 2 y 6):");

        probar("Datos correctos:", "Nube", "Gato", "3.8", "2022-06-18", "11111111A");
        probar("Peticion enviada con curl, saltandose el formulario:", "", "Gato", "-5", "2022-06-18", "");
        probar("Peso escrito con coma, error tipico del usuario:", "Toby", "Perro", "22,5", "2019-04-12", "11111111A");
        probar("Peso ausente (parametro null):", "Toby", "Perro", null, "2019-04-12", "11111111A");
        probar("Fecha futura y fecha mal formada:", "Kiwi", "Ave", "0.3", "2030-01-01", "33333333C");
        probar("Fecha ilegible:", "Kiwi", "Ave", "0.3", "32/13/2026", "33333333C");

        System.out.println("  Apartado 5: tras el alta, sendRedirect. Si se pintara el resultado");
        System.out.println("    directamente, un F5 reenviaria el POST y crearia un duplicado.");
        System.out.println("    En el caso de ERROR se hace lo contrario, forward, para no perder");
        System.out.println("    los valores que el usuario ya habia escrito.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
