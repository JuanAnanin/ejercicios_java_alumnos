package com.ifcd0112.ejercicios.ampliacion.uf2406;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;

/**
 * AMPLIACION UF2406 - EJERCICIO A11: De la documentacion de diseno a la interfaz.
 *
 * <p>Criterios de evaluacion: CE5.4, CE5.6, CE5.7</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class A11InterfazDesdeDiseno {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * En el ejercicio 14 construiste una ventana a partir de un enunciado. En un
     * proyecto real se construye a partir de la DOCUMENTACION DE DISENO, que
     * llega ya hecha, y hay que respetarla.
     *
     * Se te entrega el diseno de la pantalla de inscripcion del gimnasio, con su
     * distribucion, sus componentes y su comportamiento.
     *
     * Se pide:
     *   1. Enumera las clases de la biblioteca de interfaz que necesitas para
     *      cada elemento del diseno, y explica que criterio has seguido para
     *      elegir entre las que hacen cosas parecidas: por ejemplo, una lista
     *      desplegable frente a un grupo de botones de opcion.
     *   2. Construye la ventana respetando exactamente la distribucion indicada,
     *      de modo que se comporte bien al cambiar de tamano y al cambiar el
     *      tamano de letra del sistema.
     *   3. Incorpora recursos multimedia: el logotipo del gimnasio en la
     *      cabecera y un aviso sonoro cuando la inscripcion falla.
     *   4. Genera el logotipo mediante codigo en lugar de cargarlo de un
     *      fichero, y explica en que casos conviene cada opcion.
     *   5. Programa las clases que conectan la interfaz con la aplicacion, de
     *      forma que la ventana no contenga NINGUNA regla de negocio.
     *   6. Demuestra esa separacion: sustituye la capa de negocio por otra
     *      distinta sin tocar ni una linea de la ventana.
     *   7. Explica que problema tienen los recursos multimedia cargados desde
     *      una ruta absoluta, y como se resuelve al empaquetar la aplicacion.
     *
     * Pista: si para probar un cambio en la logica tienes que abrir la ventana y
     * hacer clic, la separacion no esta bien hecha. La prueba definitiva es
     * poder ejecutar la logica entera sin que aparezca nada en pantalla.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * APARTADO 1: elegir entre componentes que hacen lo mismo.
     *
     *   Lista desplegable frente a botones de opcion. El criterio es el NUMERO
     *   de alternativas y si conviene verlas todas a la vez. Hasta cuatro o
     *   cinco opciones que el usuario debe comparar, botones de opcion, porque
     *   se ven de golpe. A partir de ahi, desplegable. Para los socios del
     *   gimnasio, que son cientos, desplegable con busqueda.
     *
     *   Lista frente a tabla. Si de cada elemento interesa un dato, lista. Si
     *   interesan varios y hay que compararlos por columnas, tabla.
     *
     *   Etiqueta de estado frente a ventana emergente. La emergente INTERRUMPE y
     *   obliga a hacer clic; la etiqueta informa sin cortar el trabajo. Se
     *   reserva la emergente para lo que de verdad requiere una decision. En el
     *   ejercicio 14 se eligio etiqueta a proposito: una confirmacion no merece
     *   detener a quien esta inscribiendo a treinta personas seguidas.
     *
     * APARTADO 4: generar el logotipo o cargarlo de un fichero.
     *
     *   GENERADO POR CODIGO
     *     + No hay fichero que se pueda perder, ni ruta que falle.
     *     + Se adapta al tamano y al tema sin perder calidad.
     *     + Util para elementos sencillos: iconos de estado, marcadores, graficos.
     *     - Inviable para cualquier cosa con diseno de verdad.
     *
     *   CARGADO DE UN FICHERO
     *     + Lo hace quien sabe disenar, no quien programa.
     *     + Se cambia sin recompilar.
     *     - Hay que empaquetarlo y encontrarlo en ejecucion (apartado 7).
     *
     *   Criterio: si el recurso lo dibujaria un programador, generarlo; si lo
     *   dibujaria un disenador, cargarlo. Aqui se genera para que el ejercicio
     *   no dependa de ningun fichero externo.
     *
     * APARTADO 7: el problema de las rutas absolutas.
     *
     * Esto funciona en el ordenador de quien lo escribio y en ningun otro:
     *
     *      new ImageIcon("C:/Users/juan/proyecto/img/logo.png")
     *
     * Al empaquetar la aplicacion en un jar, los recursos dejan de ser ficheros
     * del disco: son entradas dentro del archivo comprimido, y ninguna ruta del
     * sistema de ficheros los alcanza.
     *
     * La forma correcta es pedirlos al cargador de clases, con una ruta relativa
     * al classpath:
     *
     *      getClass().getResource("/imagenes/logo.png")
     *
     * Asi funciona igual en desarrollo, donde el recurso esta en una carpeta, y
     * en produccion, donde esta dentro del jar. Es el fallo mas repetido al
     * entregar una aplicacion de escritorio: funciona al ejecutarla desde el
     * entorno y deja de funcionar al empaquetarla.
     */

    /** Ancho del logotipo generado, en puntos. */
    private static final int ANCHO_LOGO = 220;

    /** Alto del logotipo generado, en puntos. */
    private static final int ALTO_LOGO = 64;

    // ---------------------------------------------------------------------
    // Apartados 3 y 4: el logotipo, generado por codigo
    // ---------------------------------------------------------------------

    /**
     * Dibuja el logotipo del gimnasio sin depender de ningun fichero.
     *
     * @param ancho anchura en puntos
     * @param alto  altura en puntos
     * @return la imagen generada
     */
    public static BufferedImage generarLogotipo(int ancho, int alto) {
        BufferedImage imagen = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = imagen.createGraphics();
        // Sin suavizado, los bordes salen dentados y se nota mucho en un
        // logotipo pequeno.
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setColor(new Color(24, 62, 110));
        g.fillRoundRect(0, 0, ancho, alto, 12, 12);

        // Una pesa dibujada con tres rectangulos: barra y dos discos.
        g.setColor(Color.WHITE);
        g.fillRect(14, alto / 2 - 3, 44, 6);
        g.fillRoundRect(10, alto / 2 - 14, 8, 28, 4, 4);
        g.fillRoundRect(54, alto / 2 - 14, 8, 28, 4, 4);

        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        g.drawString("Gimnasio", 74, alto / 2 + 6);
        g.dispose();
        return imagen;
    }

    /**
     * Apartado 3: prepara el aviso sonoro de error.
     *
     * <p>Se sintetiza una onda cuadrada corta en lugar de cargar un fichero de
     * sonido, por el mismo motivo que el logotipo: para no depender de nada
     * externo. En una aplicacion real se cargaria un .wav desde el classpath.</p>
     *
     * @param frecuencia frecuencia del tono en hercios
     * @param milisegundos duracion del aviso
     * @return las muestras de audio listas para reproducir
     */
    public static byte[] generarAvisoSonoro(int frecuencia, int milisegundos) {
        AudioFormat formato = new AudioFormat(8000f, 8, 1, true, false);
        int muestras = (int) (formato.getSampleRate() * milisegundos / 1000);
        byte[] datos = new byte[muestras];
        double periodo = formato.getSampleRate() / frecuencia;
        for (int i = 0; i < muestras; i++) {
            // Onda cuadrada: la mitad del periodo arriba y la mitad abajo.
            datos[i] = (byte) ((i % periodo) < (periodo / 2) ? 40 : -40);
        }
        return datos;
    }

    // ---------------------------------------------------------------------
    // Apartados 5 y 6: la conexion entre interfaz y aplicacion
    // ---------------------------------------------------------------------

    /**
     * Lo que la ventana necesita de la aplicacion, y nada mas.
     *
     * <p>La ventana depende de esta interfaz, no de una clase concreta. Eso es
     * lo que permite el apartado 6: cambiar la capa de negocio sin tocar la
     * ventana.</p>
     */
    public interface ServicioInscripciones {

        /**
         * @param dniSocio    socio que se inscribe
         * @param codigoClase clase solicitada
         * @return mensaje de confirmacion
         * @throws IllegalStateException si la inscripcion no se puede realizar
         */
        String inscribir(String dniSocio, String codigoClase);

        /** @return nombre de la implementacion, para poder distinguirlas */
        String descripcion();
    }

    /** Implementacion real: aplica las reglas del gimnasio. */
    public static class ServicioReal implements ServicioInscripciones {

        @Override
        public String inscribir(String dniSocio, String codigoClase) {
            if ("SPIN-2".equals(codigoClase)) {
                throw new IllegalStateException("aforo agotado");
            }
            return "Inscripcion realizada correctamente";
        }

        @Override
        public String descripcion() { return "ServicioReal (reglas del gimnasio)"; }
    }

    /**
     * Implementacion de demostracion: acepta todo.
     *
     * <p>Existe para el apartado 6 y para poder ensenar la aplicacion en una
     * feria sin base de datos. La ventana no nota la diferencia.</p>
     */
    public static class ServicioDemostracion implements ServicioInscripciones {

        @Override
        public String inscribir(String dniSocio, String codigoClase) {
            return "Inscripcion simulada (modo demostracion)";
        }

        @Override
        public String descripcion() { return "ServicioDemostracion (acepta todo)"; }
    }

    /**
     * Controlador de la ventana. No contiene ninguna regla de negocio: recoge,
     * pide y traduce el resultado a algo que el usuario entienda.
     */
    public static class ControladorPantalla {

        private final ServicioInscripciones servicio;
        private boolean sonar;

        /**
         * @param servicio capa de negocio, inyectada desde fuera
         */
        public ControladorPantalla(ServicioInscripciones servicio) {
            this.servicio = servicio;
        }

        /**
         * Atiende la pulsacion del boton de inscribir.
         *
         * @param dniSocio    socio seleccionado
         * @param codigoClase clase seleccionada
         * @return mensaje que se mostrara en la etiqueta de estado
         */
        public String alPulsarInscribir(String dniSocio, String codigoClase) {
            sonar = false;
            try {
                return servicio.inscribir(dniSocio, codigoClase);
            } catch (IllegalStateException e) {
                // Apartado 3: el aviso sonoro acompana al mensaje, no lo
                // sustituye. Un sonido sin texto es inaccesible para quien no
                // oye, y ademas no dice que ha pasado.
                sonar = true;
                return "Esa clase ya no tiene plazas disponibles. Prueba con otro horario.";
            }
        }

        /** @return true si la ultima operacion debe ir acompanada del aviso sonoro */
        public boolean debeSonar() { return sonar; }
    }

    // =====================================================================
    // COMPROBACION
    // =====================================================================

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[AMP-UF2406-A11] De la documentacion de diseno a la interfaz");

        // Apartados 3 y 4: el logotipo generado.
        BufferedImage logo = generarLogotipo(ANCHO_LOGO, ALTO_LOGO);
        System.out.println("  Apartados 3 y 4, logotipo generado por codigo:");
        System.out.println("    Dimensiones: " + logo.getWidth() + "x" + logo.getHeight()
                + " puntos, con canal de transparencia");
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ImageIO.write(logo, "png", buffer);
            System.out.println("    Codificado en PNG ocupa " + buffer.size() + " bytes");
        } catch (IOException e) {
            System.out.println("    No se pudo codificar la imagen: " + e.getMessage());
        }
        System.out.println("    Se dibuja con Graphics2D, sin ningun fichero externo: no hay ruta");
        System.out.println("    que falle ni recurso que se pierda al empaquetar.");

        byte[] aviso = generarAvisoSonoro(880, 150);
        System.out.println("    Aviso sonoro sintetizado: " + aviso.length
                + " muestras a 8 kHz, 150 ms a 880 Hz");
        System.out.println("    Se genera, no se reproduce: en un servidor sin tarjeta de sonido");
        System.out.println("    reproducirlo lanzaria excepcion, y eso no debe tumbar la");
        System.out.println("    aplicacion. El sonido es un adorno; el mensaje es lo obligatorio.");

        // Apartados 5 y 6: la separacion, demostrada.
        System.out.println("  Apartados 5 y 6, la MISMA pantalla con dos capas de negocio:");
        ServicioInscripciones[] servicios = { new ServicioReal(), new ServicioDemostracion() };
        for (ServicioInscripciones s : servicios) {
            ControladorPantalla controlador = new ControladorPantalla(s);
            System.out.println("    " + s.descripcion());
            String ok = controlador.alPulsarInscribir("11111111A", "YOGA-1");
            System.out.println("      Inscripcion en YOGA-1 -> " + ok
                    + (controlador.debeSonar() ? "  [+aviso sonoro]" : ""));
            String lleno = controlador.alPulsarInscribir("11111111A", "SPIN-2");
            System.out.println("      Inscripcion en SPIN-2 -> " + lleno
                    + (controlador.debeSonar() ? "  [+aviso sonoro]" : ""));
        }
        System.out.println("    No se ha tocado ni una linea del controlador entre las dos: solo");
        System.out.println("    ha cambiado el objeto que se le pasa al construirlo.");
        System.out.println("    Y observese que todo esto se ha ejecutado SIN abrir una ventana.");
        System.out.println("    Esa es la prueba de la pista: si hiciera falta hacer clic para");
        System.out.println("    comprobar la logica, la separacion estaria mal hecha.");

        System.out.println("  Apartado 7: nunca rutas absolutas para los recursos. Al empaquetar,");
        System.out.println("    la imagen deja de ser un fichero del disco y pasa a estar dentro");
        System.out.println("    del jar. Se pide al cargador de clases:");
        System.out.println("      getClass().getResource(\"/imagenes/logo.png\")");
        System.out.println("    Asi funciona igual en desarrollo y empaquetado. Es el fallo mas");
        System.out.println("    repetido al entregar una aplicacion de escritorio.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
