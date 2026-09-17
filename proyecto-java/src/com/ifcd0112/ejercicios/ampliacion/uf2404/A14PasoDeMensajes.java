package com.ifcd0112.ejercicios.ampliacion.uf2404;

import java.lang.reflect.Method;

/**
 * AMPLIACION UF2404 - EJERCICIO A14: El paso de mensajes.
 *
 * <p>Criterios de evaluacion: CE1.4, CE2.4</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class A14PasoDeMensajes {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * En el paradigma orientado a objetos los objetos no se leen ni se
     * manipulan: se les pide algo. A esa peticion se le llama mensaje, y el
     * comportamiento del programa es el resultado de decidir, en cada envio,
     * quien responde. Este ejercicio pretende que veas esa decision ocurrir.
     *
     * Se pide:
     *   1. Explica que es un mensaje y de que tres partes se compone. Relaciona
     *      cada parte con lo que escribes en Java al invocar un metodo.
     *   2. Escribe una jerarquia Notificador con tres subclases (Email, Sms y
     *      Push) que respondan al mismo mensaje de formas distintas. Envia el
     *      mismo mensaje a los tres a traves de una variable del tipo padre y
     *      anota que responde cada uno.
     *   3. Demuestra la diferencia entre sobrecarga y redefinicion: construye un
     *      caso en el que el metodo elegido dependa del tipo DECLARADO de la
     *      variable y otro en el que dependa del tipo REAL del objeto. Explica
     *      en que momento se decide cada uno.
     *   4. Comprueba tu respuesta con reflexion: para cada envio imprime la
     *      clase real del receptor, el selector, los tipos de los argumentos y
     *      la clase en la que se ha encontrado finalmente el metodo.
     *   5. Envia un mensaje a una referencia nula, captura el error y explica
     *      por que es un error de ejecucion y no de compilacion.
     *   6. Usa super para enviar el mismo mensaje empezando la busqueda en la
     *      superclase, y demuestra que el receptor sigue siendo el mismo objeto.
     *   7. Explica por que se dice que el comportamiento de un objeto es el
     *      conjunto de mensajes que sabe responder, y no el valor de sus
     *      atributos.
     *
     * Pista: para el apartado 3, prueba a declarar una variable de tipo Object
     * que contenga una cadena y pasarsela a un metodo sobrecargado que tenga una
     * version para Object y otra para String. El resultado sorprende, y sorprende
     * porque la sobrecarga la resuelve el compilador, que solo conoce lo que has
     * escrito en la declaracion.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * APARTADO 1: las tres partes de un mensaje.
     *
     *      destinatario.enviar("Clase de spinning a las 19:00")
     *      ^^^^^^^^^^^^ ^^^^^^ ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
     *      RECEPTOR     SELECTOR          ARGUMENTOS
     *
     *   RECEPTOR   el objeto al que se le pide algo. No es el tipo de la
     *              variable: es el objeto que hay dentro de ella en ese
     *              instante. De el depende la respuesta.
     *   SELECTOR   el nombre de la operacion que se solicita. Es lo que el
     *              receptor tiene que reconocer; si no lo reconoce, el
     *              compilador se niega.
     *   ARGUMENTOS los datos que acompanan a la peticion. Pueden faltar.
     *
     * La diferencia con una llamada a funcion del paradigma estructurado esta
     * en el receptor. En enviar(destinatario, texto) el que decide que se hace
     * es quien escribe la llamada; en destinatario.enviar(texto) lo decide el
     * objeto que recibe el mensaje. Por eso se puede anadir un Notificador
     * nuevo sin tocar el codigo que envia.
     *
     * APARTADO 7: comportamiento frente a estado.
     *
     * Dos objetos con los mismos atributos pero que respondan distinto al mismo
     * mensaje son objetos distintos para el programa que los usa; dos objetos
     * con atributos distintos que respondan igual son intercambiables. Lo que
     * el resto del sistema ve de un objeto es exactamente el conjunto de
     * mensajes que sabe responder, es decir, su interfaz. Los atributos son el
     * medio que cada clase elige para poder responder, y por eso se declaran
     * privados: cambiarlos no debe afectar a nadie mientras las respuestas
     * sigan siendo las mismas.
     */

    // -----------------------------------------------------------------
    // APARTADO 2: la jerarquia
    // -----------------------------------------------------------------

    /** Un destinatario capaz de recibir avisos. */
    public static class Notificador {

        /** Direccion o identificador del destinatario. */
        protected final String destino;

        /**
         * @param destino direccion o identificador del destinatario
         */
        public Notificador(String destino) {
            this.destino = destino;
        }

        /**
         * Responde al mensaje "enviar".
         *
         * @param texto contenido del aviso
         * @return descripcion de lo que se ha hecho
         */
        public String enviar(String texto) {
            return "aviso generico para " + destino + ": " + texto;
        }
    }

    /** Notificador por correo electronico. */
    public static class Email extends Notificador {

        /**
         * @param destino direccion de correo
         */
        public Email(String destino) {
            super(destino);
        }

        @Override
        public String enviar(String texto) {
            return "correo a " + destino + " con asunto \"" + texto + "\"";
        }

        /**
         * Apartado 6: reenvia el mismo mensaje empezando la busqueda en la
         * superclase, sin cambiar de receptor.
         *
         * @param texto contenido del aviso
         * @return las dos respuestas y la identidad del receptor en cada una
         */
        public String enviarConSuper(String texto) {
            String propio = this.enviar(texto);
            String heredado = super.enviar(texto);
            String yo = this.getClass().getSimpleName() + "#" + System.identityHashCode(this);
            return String.format(
                    "    this.enviar()   -> %s%n"
                    + "                       receptor: %s%n"
                    + "    super.enviar()  -> %s%n"
                    + "                       receptor: %s  (el mismo objeto)",
                    propio, yo, heredado, yo);
        }
    }

    /** Notificador por mensaje corto. */
    public static class Sms extends Notificador {

        /**
         * @param destino numero de telefono
         */
        public Sms(String destino) {
            super(destino);
        }

        @Override
        public String enviar(String texto) {
            String corto = texto.length() > 20 ? texto.substring(0, 17) + "..." : texto;
            return "sms al " + destino + ": \"" + corto + "\" (" + corto.length() + " car.)";
        }
    }

    /** Notificador por aviso emergente en la aplicacion movil. */
    public static class Push extends Notificador {

        /**
         * @param destino identificador del dispositivo
         */
        public Push(String destino) {
            super(destino);
        }

        @Override
        public String enviar(String texto) {
            return "push al dispositivo " + destino + " -> " + texto.toUpperCase();
        }
    }

    // -----------------------------------------------------------------
    // APARTADO 3: sobrecarga frente a redefinicion
    // -----------------------------------------------------------------

    /*
     * Las dos parejas de metodos de abajo se parecen, pero se resuelven en
     * momentos distintos:
     *
     *   SOBRECARGA (registrar)   varios metodos con el mismo nombre y distinta
     *                            lista de parametros. Elige el COMPILADOR, y
     *                            solo puede mirar el tipo DECLARADO de lo que le
     *                            pasas. Se decide al compilar: enlace estatico.
     *
     *   REDEFINICION (enviar)    un metodo de la subclase con la misma firma que
     *                            el de la superclase. Elige la MAQUINA VIRTUAL
     *                            mirando la clase REAL del receptor. Se decide
     *                            al ejecutar: enlace dinamico.
     *
     * De ahi la regla practica: la sobrecarga es comodidad de escritura, el
     * polimorfismo esta en la redefinicion. Un mensaje solo es polimorfico si lo
     * resuelve el receptor.
     */

    /**
     * Version de la sobrecarga para cualquier objeto.
     *
     * @param o objeto a registrar
     * @return la version elegida por el compilador
     */
    public static String registrar(Object o) {
        return "registrar(Object)";
    }

    /**
     * Version de la sobrecarga para cadenas.
     *
     * @param s cadena a registrar
     * @return la version elegida por el compilador
     */
    public static String registrar(String s) {
        return "registrar(String)";
    }

    // -----------------------------------------------------------------
    // APARTADO 4: el mensaje observado con reflexion
    // -----------------------------------------------------------------

    /**
     * Descompone un envio en sus tres partes y averigua donde acaba resolviendose.
     *
     * @param receptor  objeto que recibe el mensaje
     * @param selector  nombre de la operacion solicitada
     * @param argumento unico argumento del mensaje
     * @return una linea con receptor, selector, argumentos, clase resolutora y respuesta
     */
    public static String observar(Object receptor, String selector, String argumento) {
        try {
            Method m = receptor.getClass().getMethod(selector, String.class);
            Object respuesta = m.invoke(receptor, argumento);
            return String.format("%-11s . %-7s (String)  ->  resuelto en %-12s  %s",
                    receptor.getClass().getSimpleName(), selector,
                    m.getDeclaringClass().getSimpleName(), respuesta);
        } catch (ReflectiveOperationException e) {
            return receptor.getClass().getSimpleName() + " no sabe responder a " + selector;
        }
    }

    // =====================================================================
    // COMPROBACION
    // =====================================================================

    /** Ejecuta el ejercicio completo. Lo invoca el runner de la ampliacion. */
    public static void resolver() {
        System.out.println("[AMPL-A14] El paso de mensajes");

        final String aviso = "Clase de spinning a las 19:00";

        // Apartado 2: el mismo mensaje, tres respuestas.
        Notificador[] destinatarios = {
            new Email("ana@gimnasio.es"),
            new Sms("600123456"),
            new Push("disp-4417")
        };
        System.out.println("  Apartado 2: mismo selector, mismo argumento, tres respuestas.");
        for (Notificador n : destinatarios) {
            System.out.println("    " + n.enviar(aviso));
        }
        System.out.println("    La variable es Notificador en los tres casos: quien decide");
        System.out.println("    la respuesta es el objeto receptor, no el tipo declarado.");

        // Apartado 3: sobrecarga (estatica) frente a redefinicion (dinamica).
        String cadena = "socio nuevo";
        Object comoObjeto = cadena;
        System.out.println("  Apartado 3: quien elige, y cuando.");
        System.out.printf("    SOBRECARGA  registrar(cadena)      -> %s   (tipo declarado String)%n",
                registrar(cadena));
        System.out.printf("    SOBRECARGA  registrar(comoObjeto)  -> %s   (tipo declarado Object,%n",
                registrar(comoObjeto));
        System.out.println("                                             aunque dentro haya un String)");
        Notificador comoPadre = new Push("disp-4417");
        System.out.printf("    REDEFINICION comoPadre.enviar(...)  -> %s%n", comoPadre.enviar("hola"));
        System.out.println("                                             (tipo declarado Notificador,");
        System.out.println("                                              responde Push: clase real)");

        // Apartado 4: reflexion.
        System.out.println("  Apartado 4: el mismo envio, observado con reflexion.");
        for (Notificador n : destinatarios) {
            System.out.println("    " + observar(n, "enviar", aviso));
        }
        System.out.println("    " + observar(new Notificador("generico"), "enviar", aviso));
        System.out.println("    " + observar(destinatarios[0], "colgar", aviso));

        // Apartado 5: mensaje a nadie.
        System.out.println("  Apartado 5: un mensaje sin receptor.");
        Notificador nadie = null;
        try {
            System.out.println(nadie.enviar(aviso));
        } catch (NullPointerException e) {
            System.out.println("    NullPointerException: " + e.getMessage());
            System.out.println("    (si el nombre sale como <localN> en vez de \"nadie\", es que se");
            System.out.println("     ha compilado sin -g: sin esa opcion el .class no guarda los");
            System.out.println("     nombres de las variables locales. Con javac -g aparece el nombre.)");
            System.out.println("    El compilador acepto la linea porque el TIPO de la variable");
            System.out.println("    conoce el selector; lo que falta en ejecucion es el receptor,");
            System.out.println("    y sin receptor no hay quien decida la respuesta.");
        }

        // Apartado 6: super no cambia de receptor.
        System.out.println("  Apartado 6: super cambia donde se busca, no a quien se pregunta.");
        Email correo = new Email("ana@gimnasio.es");
        System.out.println(correo.enviarConSuper(aviso));
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
