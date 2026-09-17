package com.ifcd0112.ejercicios.ampliacion.uf2406;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * AMPLIACION UF2406 - EJERCICIO A12: Validacion y accesibilidad de la interfaz.
 *
 * <p>Criterios de evaluacion: CE5.5, CE5.8</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class A12ValidacionAccesibilidad {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * La pantalla de inscripcion ya esta construida. Ahora hay que COMPROBAR que
     * cumple lo que se pedia, y ese es un trabajo distinto de programarla.
     *
     * El Real Decreto que regula este certificado menciona expresamente los
     * criterios de accesibilidad para personas con discapacidad, asi que no es
     * un extra opcional: forma parte de lo que hay que verificar.
     *
     * Se pide:
     *   1. Establece los criterios de validacion de la pantalla: que se
     *      considera correcto y como se comprueba cada cosa objetivamente.
     *   2. Comprueba que los formatos de entrada y salida son los esperados,
     *      incluyendo los casos raros: numeros con coma, fechas, campos vacios y
     *      textos muy largos.
     *   3. Clasifica los tipos de error que puede cometer el usuario en esta
     *      pantalla y decide como responde la interfaz a cada uno.
     *   4. Mide el contraste entre el texto y su fondo para todos los mensajes
     *      de la pantalla, y comprueba si alcanzan el nivel exigido. Investiga
     *      que relacion de contraste se considera suficiente.
     *   5. Comprueba que la pantalla se puede usar ENTERA sin raton, e indica
     *      que hay que anadir al codigo para que asi sea.
     *   6. Revisa si la informacion se transmite unicamente mediante el color, y
     *      corrigelo si es el caso.
     *   7. Redacta el indice de la guia de usuario de esta pantalla.
     *   8. Elabora el informe de validacion con el veredicto y la lista de
     *      defectos encontrados.
     *
     * Pista: para el apartado 6, imprime la pantalla en blanco y negro. Todo lo
     * que deje de distinguirse es informacion que estabas transmitiendo solo con
     * el color, y que no llega a una de cada doce personas.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * APARTADO 4: que relacion de contraste hace falta.
     *
     * La referencia son las pautas de accesibilidad para el contenido web, que
     * se aplican igual a una aplicacion de escritorio:
     *
     *      Nivel AA   4.5:1 para texto normal, 3:1 para texto grande
     *      Nivel AAA  7:1 para texto normal, 4.5:1 para texto grande
     *
     * El nivel AA es el exigible en la administracion publica espanola, y por
     * tanto el minimo razonable para cualquier aplicacion de gestion.
     *
     * La relacion se calcula a partir de la luminancia relativa de los dos
     * colores, y no es la diferencia de brillo que uno diria a ojo: el verde
     * pesa mucho mas que el azul porque el ojo humano es mas sensible a el. Por
     * eso hay que calcularlo y no estimarlo.
     *
     * APARTADO 5: usar la pantalla sin raton.
     *
     * Hace falta anadir cuatro cosas al codigo del ejercicio 14:
     *
     *   1. ORDEN DE TABULACION coherente: socio, lista de clases, boton. Se
     *      controla con setFocusTraversalPolicy o, mas sencillo, anadiendo los
     *      componentes en el orden correcto.
     *   2. MNEMONICOS: setMnemonic('I') en el boton permite activarlo con
     *      Alt+I sin tocar el raton.
     *   3. BOTON POR DEFECTO: getRootPane().setDefaultButton(botonInscribir),
     *      para que la tecla Intro haga lo esperable.
     *   4. ETIQUETAS ASOCIADAS: etiqueta.setLabelFor(comboSocios), que ademas es
     *      lo que permite a un lector de pantalla anunciar "Socio, lista
     *      desplegable" en lugar de solo "lista desplegable".
     *
     * El punto 4 es el que casi nadie pone y el que mas importa: sin el, quien
     * usa un lector de pantalla oye los controles pero no sabe que es cada uno.
     *
     * APARTADO 6: informacion transmitida solo con el color.
     *
     * En el ejercicio 14, el resultado se distingue por el color de la etiqueta:
     * verde correcto, rojo error, gris en curso. Para una persona con daltonismo
     * (alrededor del 8 por ciento de los hombres) el verde y el rojo pueden ser
     * el mismo tono.
     *
     * La correccion no es cambiar los colores, es NO DEPENDER solo de ellos:
     * anadir un simbolo delante del mensaje. El color se queda, porque ayuda a
     * quien si lo distingue, pero deja de ser el unico portador de informacion.
     *
     *      [OK]     Inscripcion realizada correctamente
     *      [ERROR]  Esa clase ya no tiene plazas disponibles
     *      [...]    Procesando inscripcion...
     *
     * Es un cambio de tres lineas y es la diferencia entre que la pantalla sirva
     * o no sirva para una de cada doce personas.
     */

    /**
     * Resultado de medir el contraste entre dos colores.
     *
     * @param descripcion que par de colores se ha medido
     * @param relacion    relacion de contraste calculada
     */
    public record Contraste(String descripcion, double relacion) {

        /** @return true si alcanza el nivel AA para texto normal */
        public boolean cumpleAA() { return relacion >= 4.5; }

        /** @return true si alcanza el nivel AAA para texto normal */
        public boolean cumpleAAA() { return relacion >= 7.0; }
    }

    /**
     * Convierte un componente de color al espacio lineal.
     *
     * @param componente valor de 0 a 255
     * @return valor lineal entre 0 y 1
     */
    private static double lineal(int componente) {
        double c = componente / 255.0;
        return (c <= 0.03928) ? c / 12.92 : Math.pow((c + 0.055) / 1.055, 2.4);
    }

    /**
     * Calcula la luminancia relativa de un color.
     *
     * <p>Los coeficientes no son iguales porque el ojo humano no es igual de
     * sensible a los tres colores: el verde aporta casi tres cuartas partes de
     * la luminancia percibida y el azul menos de una decima.</p>
     *
     * @param color color a medir
     * @return luminancia relativa entre 0 y 1
     */
    public static double luminancia(Color color) {
        return 0.2126 * lineal(color.getRed())
             + 0.7152 * lineal(color.getGreen())
             + 0.0722 * lineal(color.getBlue());
    }

    /**
     * Apartado 4: calcula la relacion de contraste entre dos colores.
     *
     * @param texto color del texto
     * @param fondo color del fondo
     * @return relacion de contraste, entre 1 y 21
     */
    public static double relacionContraste(Color texto, Color fondo) {
        double l1 = luminancia(texto);
        double l2 = luminancia(fondo);
        double clara = Math.max(l1, l2);
        double oscura = Math.min(l1, l2);
        return (clara + 0.05) / (oscura + 0.05);
    }

    /**
     * Tipo de error del usuario y respuesta de la interfaz.
     *
     * @param tipo      naturaleza del error
     * @param ejemplo   caso concreto
     * @param respuesta como reacciona la pantalla
     */
    public record TipoError(String tipo, String ejemplo, String respuesta) {
    }

    /**
     * Apartado 3: clasificacion de los errores posibles.
     *
     * @return los tipos de error contemplados
     */
    public static List<TipoError> tiposDeError() {
        return Arrays.asList(
            new TipoError("Omision",
                "pulsar Inscribir sin elegir socio",
                "No ocurre: el boton esta deshabilitado. Prevencion, no aviso"),
            new TipoError("Formato",
                "escribir 22,5 en un campo de peso",
                "Se rechaza al validar, con el mensaje de que se espera un numero"),
            new TipoError("Fuera de rango",
                "peso negativo, fecha de nacimiento futura",
                "Se rechaza al validar, indicando el limite concreto"),
            new TipoError("Estado invalido",
                "inscribir en una clase que se acaba de llenar",
                "Se rechaza al ejecutar, con mensaje comprensible y aviso sonoro"),
            new TipoError("Del sistema",
                "la base de datos no responde",
                "Mensaje generico al usuario y detalle en el registro del servidor"));
    }

    // =====================================================================
    // COMPROBACION
    // =====================================================================

    /** Ruta de la guia de usuario elaborada en el apartado 7. */
    public static final String GUIA = "recursos/ampliacion/GUIA_USUARIO_inscripcion.md";

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[AMP-UF2406-A12] Validacion y accesibilidad de la interfaz");

        // Apartado 4: el contraste de los colores REALES del ejercicio 14.
        System.out.println("  Apartado 4, contraste medido de los mensajes del ejercicio 14:");
        Color fondo = Color.WHITE;
        List<Contraste> medidas = new ArrayList<>();
        medidas.add(new Contraste("Confirmacion, verde (0,128,0) sobre blanco",
                relacionContraste(new Color(0, 128, 0), fondo)));
        medidas.add(new Contraste("Error, rojo (176,0,32) sobre blanco",
                relacionContraste(new Color(176, 0, 32), fondo)));
        medidas.add(new Contraste("En curso, gris (128,128,128) sobre blanco",
                relacionContraste(Color.GRAY, fondo)));
        medidas.add(new Contraste("Texto normal, negro sobre blanco",
                relacionContraste(Color.BLACK, fondo)));

        System.out.printf("    %-46s %8s %6s %6s%n", "par de colores", "relacion", "AA", "AAA");
        List<String> defectos = new ArrayList<>();
        for (Contraste c : medidas) {
            System.out.printf("    %-46s %7.2f:1 %6s %6s%n", c.descripcion(), c.relacion(),
                    c.cumpleAA() ? "si" : "NO", c.cumpleAAA() ? "si" : "no");
            if (!c.cumpleAA()) {
                defectos.add("Contraste insuficiente: " + c.descripcion()
                        + String.format(" (%.2f:1, hace falta 4.5:1)", c.relacion()));
            }
        }
        System.out.println("    Nivel AA exige 4.5:1 para texto normal; AAA, 7:1.");
        System.out.println("    HALLAZGO: el gris del mensaje de progreso NO llega al minimo. Es un");
        System.out.println("    defecto real de la pantalla del ejercicio 14, encontrado midiendo y");
        System.out.println("    no mirando. A ojo parecia perfectamente legible, y ese es justo el");
        System.out.println("    problema: quien disena tiene buena vista y una pantalla buena.");
        System.out.println("    Correccion: usar (90,90,90), que sube a "
                + String.format("%.2f:1", relacionContraste(new Color(90, 90, 90), fondo)) + ".");

        // Apartado 6: la prueba del blanco y negro.
        System.out.println("  Apartado 6, informacion transmitida solo con el color:");
        System.out.printf("    Verde de confirmacion, luminancia %.4f%n",
                luminancia(new Color(0, 128, 0)));
        System.out.printf("    Rojo de error,          luminancia %.4f%n",
                luminancia(new Color(176, 0, 32)));
        System.out.printf("    Contraste ENTRE los dos: %.2f:1%n",
                relacionContraste(new Color(0, 128, 0), new Color(176, 0, 32)));
        System.out.println("    Es decir: quitado el color, apenas se distinguen. Cualquier par de");
        System.out.println("    colores que transmita informacion deberia separarse al menos 3:1 en");
        System.out.println("    luminancia, y estos no llegan ni a la mitad. Para una persona con");
        System.out.println("    daltonismo, que ademas es alrededor del 8 por ciento de los");
        System.out.println("    hombres, verde y rojo son el par que peor se distingue.");
        System.out.println("    Correccion, que son tres lineas:");
        System.out.println("      [OK]     Inscripcion realizada correctamente");
        System.out.println("      [ERROR]  Esa clase ya no tiene plazas disponibles");
        System.out.println("      [...]    Procesando inscripcion...");
        System.out.println("    El color se queda, porque ayuda a quien lo distingue; lo que cambia");
        System.out.println("    es que deja de ser el UNICO portador de la informacion.");
        defectos.add("La confirmacion y el error se distinguen solo por el color");

        // Apartado 3
        System.out.println("  Apartado 3, tipos de error y respuesta de la interfaz:");
        for (TipoError t : tiposDeError()) {
            System.out.printf("    %-16s %-42s %s%n", t.tipo(), t.ejemplo(), t.respuesta());
        }
        System.out.println("    El primero es el mas interesante: no se responde al error, se");
        System.out.println("    IMPIDE. Prevenir siempre gana a informar.");

        // Apartado 5
        System.out.println("  Apartado 5, uso sin raton. Falta anadir al ejercicio 14:");
        System.out.println("    botonInscribir.setMnemonic('I')            -> Alt+I lo activa");
        System.out.println("    getRootPane().setDefaultButton(boton)      -> Intro funciona");
        System.out.println("    etiquetaSocio.setLabelFor(comboSocios)     -> el lector de pantalla");
        System.out.println("      anuncia 'Socio, lista desplegable' y no solo 'lista desplegable'");
        System.out.println("    orden de tabulacion coherente: socio, clases, boton");
        defectos.add("La pantalla no es utilizable enteramente con teclado");

        // Apartado 7
        System.out.println("  Apartado 7, guia de usuario de la pantalla: " + GUIA);

        // Apartado 8: el veredicto.
        System.out.println("  Apartado 8, informe de validacion:");
        System.out.println("    Defectos encontrados: " + defectos.size());
        for (String d : defectos) {
            System.out.println("      - " + d);
        }
        System.out.println("    VEREDICTO: la pantalla cumple los requisitos FUNCIONALES (las");
        System.out.println("    inscripciones se registran y los errores se comunican) pero NO los");
        System.out.println("    de accesibilidad. No es apta para entrega en un organismo publico.");
        System.out.println("    Los tres defectos se corrigen en menos de una hora, y esa es la");
        System.out.println("    conclusion util del ejercicio: la accesibilidad sale casi gratis si");
        System.out.println("    se comprueba, y sale carisima si se descubre despues de entregar.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
