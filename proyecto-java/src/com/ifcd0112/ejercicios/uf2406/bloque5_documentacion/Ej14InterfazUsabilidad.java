package com.ifcd0112.ejercicios.uf2406.bloque5_documentacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

/**
 * UF2406 - BLOQUE 5 - EJERCICIO 14: Interfaz grafica con criterios de usabilidad.
 *
 * <p>Criterios de evaluacion: CE5.2, CE5.3, CE5.5</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej14InterfazUsabilidad {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Desarrolla la pantalla de inscripcion a clases colectivas del gimnasio,
     * aplicando de forma explicita criterios de usabilidad.
     *
     * Se pide:
     *   1. Disena la ventana con los componentes necesarios: seleccion de socio,
     *      lista de clases disponibles con sus plazas libres, y boton de
     *      inscripcion.
     *   2. Organiza los componentes en contenedores adecuados, de modo que la
     *      ventana se comporte correctamente al redimensionarse.
     *   3. Aplica el principio de prevencion de errores: el boton de inscripcion
     *      debe permanecer deshabilitado mientras no se haya seleccionado un
     *      socio y una clase con plazas libres.
     *   4. Aplica el principio de visibilidad del estado: muestra un mensaje de
     *      progreso mientras se procesa la inscripcion y un mensaje claro de
     *      confirmacion o de error al terminar.
     *   5. Ante cualquier error, muestra un mensaje comprensible para el usuario
     *      final, nunca la traza de la excepcion.
     *   6. Implementa la gestion de eventos identificando claramente en el
     *      codigo la fuente del evento, el evento y el listener.
     *   7. Evalua tu propia interfaz frente a las diez heuristicas de usabilidad
     *      y documenta por escrito cuales cumple y cual mejorarias si dispusieras
     *      de mas tiempo.
     *
     * Pista: la capa de interfaz no debe contener logica de negocio: el
     * controlador de la ventana debe limitarse a recoger los datos, invocar al
     * gestor correspondiente y mostrar el resultado.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * SOBRE LA TECNOLOGIA ELEGIDA.
     *
     * La solucion del documento del profesorado usa JavaFX. Aqui la ventana esta
     * hecha con Swing, que forma parte del JDK y no necesita ninguna
     * dependencia, coherente con el resto del proyecto. Los criterios de
     * usabilidad, que es lo que el ejercicio evalua, son exactamente los mismos:
     * cambia el nombre de las clases, no las ideas.
     *
     *      JavaFX                      Swing
     *      -------------------------   ---------------------------
     *      ComboBox<Socio>             JComboBox<Socio>
     *      ListView<ClaseColectiva>    JList<ClaseColectiva>
     *      Button                      JButton
     *      Label                       JLabel
     *      ChangeListener              ItemListener / ListSelectionListener
     *      @FXML + onAction            addActionListener
     *
     * COMO EJECUTARLA.
     *
     *      java com.ifcd0112.ejercicios.uf2406.bloque5_documentacion.Ej14InterfazUsabilidad ventana
     *
     * El metodo resolver(), que es el que invoca el runner del bloque, NO abre
     * la ventana: comprueba la logica del controlador sin pintar nada. Eso no es
     * un apano, es lo que pide la pista: si el controlador no tiene logica de
     * negocio y ademas su logica de habilitacion esta en un metodo aparte, se
     * puede verificar sin abrir una sola ventana. Una interfaz que solo se puede
     * probar a mano es una interfaz mal separada.
     *
     * APARTADO 6: fuente, evento y listener, identificados.
     *
     *      FUENTE   el objeto que genera el evento: botonInscribir, el
     *               JComboBox de socios y el JList de clases.
     *      EVENTO   el objeto que encapsula lo ocurrido: ActionEvent para el
     *               boton, ListSelectionEvent y ItemEvent para las selecciones.
     *               Lleva dentro quien lo genero y cuando.
     *      LISTENER el objeto que reacciona: la implementacion de ActionListener
     *               que se registra con botonInscribir.addActionListener(...).
     *
     * Es el modelo de delegacion de eventos: la fuente no sabe que va a pasar
     * cuando la pulsen, solo avisa a quien se haya apuntado. Por eso se pueden
     * registrar varios listeners sobre el mismo boton y por eso el boton es
     * reutilizable.
     *
     * APARTADO 7: autoevaluacion frente a las diez heuristicas de Nielsen.
     *
     *   1. Visibilidad del estado del sistema        CUMPLE. Etiqueta de estado
     *      con mensaje de progreso, confirmacion o error, y en color.
     *   2. Correspondencia con el mundo real         CUMPLE. Se habla de socios,
     *      clases y plazas, no de registros ni de excepciones.
     *   3. Control y libertad del usuario            NO CUMPLE. Es la que hay
     *      que mejorar: falta poder deshacer una inscripcion recien hecha sin
     *      irse a otra pantalla.
     *   4. Consistencia y estandares                 CUMPLE. Componentes
     *      estandar, boton de accion abajo a la derecha.
     *   5. Prevencion de errores                     CUMPLE. El boton nace
     *      deshabilitado y solo se habilita con la seleccion completa y valida.
     *   6. Reconocer antes que recordar              CUMPLE. Las plazas libres
     *      se ven en la propia lista; no hay que memorizarlas.
     *   7. Flexibilidad y eficiencia                 PARCIAL. Falta atajo de
     *      teclado y acceso rapido por DNI para el recepcionista, que hace esto
     *      cincuenta veces al dia.
     *   8. Diseno estetico y minimalista             CUMPLE. Tres controles y
     *      una etiqueta; nada mas en pantalla.
     *   9. Ayudar a reconocerse y recuperarse de los errores  CUMPLE. El mensaje
     *      dice que ha pasado y que hacer, no un codigo de error.
     *  10. Ayuda y documentacion                     NO CUMPLE. No hay ayuda
     *      contextual. Para una pantalla de tres controles es asumible.
     *
     * Si hubiera tiempo, la numero 3 primero: es la que mas cuesta a los
     * usuarios cuando se equivocan, y ademas es la que convierte una interfaz en
     * un sitio donde uno se atreve a probar cosas.
     */

    /** No quedan plazas en la clase seleccionada. */
    public static class ClaseCompletaException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        /**
         * @param codigo codigo de la clase completa
         */
        public ClaseCompletaException(String codigo) {
            super("Aforo agotado en " + codigo);
        }
    }

    /** El socio tiene mensualidades sin pagar. */
    public static class CuotasPendientesException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        /**
         * @param meses meses impagados
         */
        public CuotasPendientesException(int meses) {
            super("El socio tiene " + meses + " mensualidades impagadas");
        }
    }

    /** Socio, reducido a lo que la pantalla necesita. */
    public static class Socio {

        private final String dni;
        private final String nombre;
        private final int mesesImpagados;

        /**
         * @param dni            documento del socio
         * @param nombre         nombre y apellidos
         * @param mesesImpagados mensualidades pendientes
         */
        public Socio(String dni, String nombre, int mesesImpagados) {
            this.dni = dni;
            this.nombre = nombre;
            this.mesesImpagados = mesesImpagados;
        }

        /** @return documento del socio */
        public String getDni() { return dni; }

        /** @return mensualidades pendientes */
        public int getMesesImpagados() { return mesesImpagados; }

        /** @return true si esta al corriente de pago */
        public boolean estaAlCorriente() { return mesesImpagados == 0; }

        @Override
        public String toString() {
            return nombre + " (" + dni + ")";
        }
    }

    /** Clase colectiva, con sus plazas libres visibles. */
    public static class ClaseColectiva {

        private final String codigo;
        private final String descripcion;
        private final int aforo;
        private int inscritos;

        /**
         * @param codigo      identificador de la clase
         * @param descripcion texto que ve el usuario
         * @param aforo       plazas totales
         * @param inscritos   plazas ocupadas
         */
        public ClaseColectiva(String codigo, String descripcion, int aforo, int inscritos) {
            this.codigo = codigo;
            this.descripcion = descripcion;
            this.aforo = aforo;
            this.inscritos = inscritos;
        }

        /** @return identificador de la clase */
        public String getCodigo() { return codigo; }

        /** @return plazas libres */
        public int plazasLibres() { return aforo - inscritos; }

        /** @return true si no quedan plazas */
        public boolean estaCompleta() { return plazasLibres() <= 0; }

        /** Ocupa una plaza. */
        public void ocuparPlaza() { inscritos++; }

        @Override
        public String toString() {
            // Heuristica 6, reconocer antes que recordar: las plazas libres se
            // ven aqui mismo. Si hubiera que consultarlas en otra pantalla, el
            // usuario tendria que memorizarlas.
            return descripcion + "  -  " + plazasLibres() + " de " + aforo + " plazas libres";
        }
    }

    /** Capa de negocio. La ventana no decide nada: le pregunta a esto. */
    public static class GestorInscripciones {

        /**
         * Inscribe a un socio en una clase.
         *
         * @param socio socio que se inscribe
         * @param clase clase solicitada
         * @throws CuotasPendientesException si el socio debe mensualidades
         * @throws ClaseCompletaException    si no quedan plazas
         */
        public void inscribir(Socio socio, ClaseColectiva clase) {
            if (!socio.estaAlCorriente()) {
                throw new CuotasPendientesException(socio.getMesesImpagados());
            }
            if (clase.estaCompleta()) {
                throw new ClaseCompletaException(clase.getCodigo());
            }
            clase.ocuparPlaza();
        }
    }

    // ---------------------------------------------------------------------
    // La logica del controlador, separada para poder probarla sin ventana
    // ---------------------------------------------------------------------

    /**
     * Apartado 3: decide si el boton de inscripcion debe estar habilitado.
     *
     * <p>Esta en un metodo aparte, y no dentro del listener, precisamente para
     * poder verificarla sin abrir la ventana.</p>
     *
     * @param socio socio seleccionado, o null
     * @param clase clase seleccionada, o null
     * @return true si la seleccion permite inscribir
     */
    public static boolean debeHabilitarBoton(Socio socio, ClaseColectiva clase) {
        return socio != null && clase != null && !clase.estaCompleta();
    }

    /**
     * Apartado 5: traduce una excepcion a un mensaje para el usuario final.
     *
     * <p>Nunca se muestra la traza ni el nombre de la clase de la excepcion. El
     * usuario del gimnasio no sabe que es una excepcion, y ademas la traza
     * revela detalles internos del sistema.</p>
     *
     * @param error excepcion capturada
     * @return mensaje comprensible
     */
    public static String mensajeParaElUsuario(RuntimeException error) {
        if (error instanceof ClaseCompletaException) {
            // Heuristica 9: decir que ha pasado Y que se puede hacer.
            return "Esa clase ya no tiene plazas disponibles. Prueba con otro horario.";
        }
        if (error instanceof CuotasPendientesException) {
            return "El socio tiene cuotas pendientes de pago. Registralas en recepcion "
                    + "antes de inscribirlo.";
        }
        return "No se ha podido completar la inscripcion. Intentalo de nuevo en unos minutos.";
    }

    // ---------------------------------------------------------------------
    // Apartados 1, 2, 4 y 6: la ventana
    // ---------------------------------------------------------------------

    /**
     * Pantalla de inscripcion a clases colectivas.
     *
     * <p>Se declara final a proposito. El constructor llama a metodos heredados
     * de JFrame como setLayout y add; si la clase pudiera extenderse, una
     * subclase podria redefinirlos y verlos ejecutarse antes de que sus propios
     * campos existan. El compilador avisa de ello con -Xlint:this-escape, y
     * marcarla final es la forma limpia de cerrar esa puerta.</p>
     */
    public static final class VentanaInscripcion extends JFrame {

        private static final long serialVersionUID = 1L;

        private final JComboBox<Socio> comboSocios = new JComboBox<>();
        private final DefaultListModel<ClaseColectiva> modeloClases = new DefaultListModel<>();
        private final JList<ClaseColectiva> listaClases = new JList<>(modeloClases);
        private final JButton botonInscribir = new JButton("Inscribir");
        private final JLabel etiquetaEstado = new JLabel(" ");
        // transient porque JFrame es serializable y GestorInscripciones no.
        // No se serializa una ventana en la practica, pero el compilador avisa
        // con -Xlint:serial y el aviso es correcto: si alguien la serializara,
        // esta referencia reventaria.
        private final transient GestorInscripciones gestor;

        /**
         * @param gestor capa de negocio, inyectada
         * @param socios socios disponibles
         * @param clases clases disponibles
         */
        public VentanaInscripcion(GestorInscripciones gestor, List<Socio> socios,
                                  List<ClaseColectiva> clases) {
            super("Inscripcion a clases colectivas");
            this.gestor = gestor;

            for (Socio s : socios) {
                comboSocios.addItem(s);
            }
            for (ClaseColectiva c : clases) {
                modeloClases.addElement(c);
            }
            // Ninguna seleccion de partida: obliga a elegir a proposito, en
            // lugar de dejar preseleccionado al primero de la lista y arriesgarse
            // a que alguien inscriba a quien no era.
            comboSocios.setSelectedIndex(-1);
            listaClases.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            // Apartado 2: contenedores con gestores de disposicion, nunca
            // posiciones absolutas. BorderLayout hace que la lista crezca al
            // agrandar la ventana y que los bordes se queden donde estan. Con
            // setBounds, la ventana se rompe en cuanto alguien la redimensiona o
            // cambia el tamano de letra del sistema.
            JPanel arriba = new JPanel(new GridLayout(2, 1, 4, 4));
            arriba.setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));
            arriba.add(new JLabel("Socio:"));
            arriba.add(comboSocios);

            JPanel centro = new JPanel(new BorderLayout(4, 4));
            centro.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            centro.add(new JLabel("Clases disponibles:"), BorderLayout.NORTH);
            centro.add(new JScrollPane(listaClases), BorderLayout.CENTER);

            JPanel abajo = new JPanel(new BorderLayout(8, 8));
            abajo.setBorder(BorderFactory.createEmptyBorder(4, 8, 8, 8));
            abajo.add(etiquetaEstado, BorderLayout.CENTER);
            abajo.add(botonInscribir, BorderLayout.EAST);

            setLayout(new BorderLayout());
            add(arriba, BorderLayout.NORTH);
            add(centro, BorderLayout.CENTER);
            add(abajo, BorderLayout.SOUTH);

            // Apartado 3: el boton nace deshabilitado.
            botonInscribir.setEnabled(false);
            comboSocios.addActionListener(e -> revisarSeleccion());
            listaClases.addListSelectionListener(e -> revisarSeleccion());

            // Apartado 6: FUENTE botonInscribir, EVENTO ActionEvent,
            // LISTENER este objeto ActionListener.
            botonInscribir.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evento) {
                    alPulsarInscribir();
                }
            });

            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(460, 320);
            setLocationRelativeTo(null);
        }

        /** Apartado 3: recalcula si el boton debe estar habilitado. */
        private void revisarSeleccion() {
            botonInscribir.setEnabled(debeHabilitarBoton(
                    (Socio) comboSocios.getSelectedItem(),
                    listaClases.getSelectedValue()));
        }

        /** Apartados 4 y 5: procesa la inscripcion y ensena el resultado. */
        private void alPulsarInscribir() {
            Socio socio = (Socio) comboSocios.getSelectedItem();
            ClaseColectiva clase = listaClases.getSelectedValue();

            // Apartado 4, visibilidad del estado: decir que se esta trabajando.
            etiquetaEstado.setText("Procesando inscripcion...");
            etiquetaEstado.setForeground(Color.GRAY);

            try {
                // La ventana NO decide nada: pregunta al gestor. Es lo que pide
                // la pista, y es lo que permite que la logica se pruebe sin
                // interfaz.
                gestor.inscribir(socio, clase);
                etiquetaEstado.setText("Inscripcion realizada correctamente");
                etiquetaEstado.setForeground(new Color(0, 128, 0));
                listaClases.repaint();     // las plazas libres han cambiado
                revisarSeleccion();        // puede que la clase ya este llena
            } catch (RuntimeException e) {
                // Apartado 5: mensaje comprensible, nunca la traza.
                etiquetaEstado.setText(mensajeParaElUsuario(e));
                etiquetaEstado.setForeground(new Color(176, 0, 32));
            }
        }
    }

    // =====================================================================
    // COMPROBACION
    // =====================================================================

    /**
     * Datos de ejemplo para la ventana y para las comprobaciones.
     *
     * @return tres socios de ejemplo
     */
    private static List<Socio> sociosDeEjemplo() {
        List<Socio> socios = new ArrayList<>();
        socios.add(new Socio("11111111A", "Ana Lopez", 0));
        socios.add(new Socio("22222222B", "Luis Gomez", 2));   // debe dos meses
        socios.add(new Socio("33333333C", "Marta Ruiz", 0));
        return socios;
    }

    /**
     * @return tres clases de ejemplo, una de ellas completa
     */
    private static List<ClaseColectiva> clasesDeEjemplo() {
        List<ClaseColectiva> clases = new ArrayList<>();
        clases.add(new ClaseColectiva("YOGA-1", "Yoga, lunes 18:00", 20, 12));
        clases.add(new ClaseColectiva("SPIN-2", "Spinning, martes 19:30", 15, 15));  // llena
        clases.add(new ClaseColectiva("PILA-3", "Pilates, jueves 10:00", 12, 3));
        return clases;
    }

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2406-E14] Interfaz grafica con criterios de usabilidad");
        System.out.println("  La ventana esta implementada en este mismo fichero, con Swing.");
        System.out.println("  Para abrirla:");
        System.out.println("    java com.ifcd0112.ejercicios.uf2406.bloque5_documentacion"
                + ".Ej14InterfazUsabilidad ventana");
        System.out.println("  Entorno grafico disponible en esta maquina: "
                + (GraphicsEnvironment.isHeadless() ? "NO" : "SI"));

        List<Socio> socios = sociosDeEjemplo();
        List<ClaseColectiva> clases = clasesDeEjemplo();

        // Apartado 3, verificado sin abrir la ventana.
        System.out.println("  Apartado 3, prevencion de errores: cuando se habilita el boton");
        System.out.printf("    %-28s %-34s %s%n", "socio", "clase", "boton");
        Object[][] casos = {
            { null,          null,          "nada seleccionado"  },
            { socios.get(0), null,          "falta la clase"     },
            { null,          clases.get(0), "falta el socio"     },
            { socios.get(0), clases.get(1), "clase COMPLETA"     },
            { socios.get(0), clases.get(0), "seleccion valida"   }
        };
        for (Object[] c : casos) {
            Socio s = (Socio) c[0];
            ClaseColectiva cl = (ClaseColectiva) c[1];
            boolean habilitado = debeHabilitarBoton(s, cl);
            System.out.printf("    %-28s %-34s %s   (%s)%n",
                    s == null ? "(sin seleccionar)" : s.toString(),
                    cl == null ? "(sin seleccionar)" : cl.getCodigo() + ", "
                            + cl.plazasLibres() + " libres",
                    habilitado ? "HABILITADO" : "deshabilitado", c[2]);
        }

        // Apartado 5, verificado sin abrir la ventana.
        System.out.println("  Apartado 5, mensajes para el usuario final:");
        GestorInscripciones gestor = new GestorInscripciones();
        Object[][] intentos = {
            { socios.get(0), clases.get(0) },   // correcto
            { socios.get(1), clases.get(0) },   // cuotas pendientes
            { socios.get(0), clases.get(1) }    // clase completa
        };
        for (Object[] intento : intentos) {
            Socio s = (Socio) intento[0];
            ClaseColectiva cl = (ClaseColectiva) intento[1];
            try {
                gestor.inscribir(s, cl);
                System.out.println("    " + s + " en " + cl.getCodigo()
                        + " -> Inscripcion realizada correctamente");
            } catch (RuntimeException e) {
                System.out.println("    " + s + " en " + cl.getCodigo() + " -> "
                        + mensajeParaElUsuario(e));
                System.out.println("      (por dentro era " + e.getClass().getSimpleName()
                        + ", pero eso NO llega al usuario)");
            }
        }

        System.out.println("  Apartado 6: FUENTE botonInscribir, EVENTO ActionEvent, LISTENER el");
        System.out.println("    ActionListener registrado con addActionListener. La fuente no sabe");
        System.out.println("    que va a pasar al pulsarla: solo avisa a quien se haya apuntado.");
        System.out.println("  Apartado 7: cumple 7 de las 10 heuristicas de Nielsen (detalle en los");
        System.out.println("    comentarios). La que hay que mejorar es la 3, control y libertad del");
        System.out.println("    usuario: falta poder deshacer una inscripcion recien hecha sin irse");
        System.out.println("    a otra pantalla.");
        System.out.println();
    }

    /**
     * Permite ejecutar este ejercicio de forma aislada.
     *
     * @param args si el primero es "ventana", abre la interfaz grafica
     */
    public static void main(String[] args) {
        if (args.length > 0 && "ventana".equalsIgnoreCase(args[0])) {
            if (GraphicsEnvironment.isHeadless()) {
                System.out.println("No hay entorno grafico disponible en esta maquina.");
                return;
            }
            // Toda interfaz Swing se construye y se manipula en el hilo de
            // despacho de eventos, nunca en el hilo principal. Hacerlo mal
            // produce fallos intermitentes de pintado, que son de la misma
            // familia que la condicion de carrera de la UF2404.
            SwingUtilities.invokeLater(() ->
                    new VentanaInscripcion(new GestorInscripciones(),
                            sociosDeEjemplo(), clasesDeEjemplo()).setVisible(true));
        } else {
            resolver();
        }
    }
}
