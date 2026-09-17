package com.ifcd0112.ejercicios.uf2406.bloque2_uml;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * UF2406 - BLOQUE 2 - EJERCICIO 4: Diagrama de clases con notacion completa.
 *
 * <p>Criterios de evaluacion: CE1.3</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej04DiagramaClases {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Traslada el analisis del ejercicio 2 a un diagrama de clases UML formal y
     * su implementacion en Java.
     *
     * Se pide:
     *   1. Dibuja el diagrama de clases con los tres compartimentos de cada
     *      clase: nombre, atributos y metodos.
     *   2. Indica la visibilidad de cada miembro con los simbolos
     *      correspondientes (+, -, #).
     *   3. Especifica el tipo de cada atributo y la firma completa de cada
     *      metodo (parametros y tipo de retorno).
     *   4. Representa las relaciones con su notacion correcta (rombo relleno
     *      para composicion, hueco para agregacion, flecha con punta triangular
     *      para herencia) y su multiplicidad en ambos extremos.
     *   5. Traduce el diagrama completo a las clases Java correspondientes,
     *      respetando escrupulosamente las visibilidades indicadas.
     *   6. Comprueba la coherencia inversa: a partir del codigo Java escrito,
     *      podria reconstruirse exactamente el mismo diagrama? Si no es asi,
     *      corrige lo que falte.
     *
     * Pista: la traduccion de UML a Java es practicamente mecanica: cada simbolo
     * de visibilidad tiene su modificador equivalente, y cada multiplicidad
     * "muchos" se convierte en una coleccion.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * DONDE ESTA EL DIAGRAMA.
     *
     *      recursos/uf2406/02_diagramas_uml.md
     *
     * Los apartados 5 y 6 si son codigo, y estan aqui.
     *
     * COMO SE RESUELVE EL APARTADO 6.
     *
     * "Podria reconstruirse el mismo diagrama a partir del codigo?" se puede
     * responder de palabra o se puede DEMOSTRAR. Aqui se demuestra: resolver()
     * usa reflexion para leer los modificadores de acceso y los tipos reales de
     * las clases escritas mas abajo, y con eso reconstruye los tres
     * compartimentos del diagrama. Si alguien cambiase un private por un public,
     * la reconstruccion saldria distinta y el desajuste se veria al ejecutar.
     *
     * LA TABLA DE EQUIVALENCIAS QUE HACE MECANICA LA TRADUCCION:
     *
     *      UML         Java              Notas
     *      ----------  ----------------  ---------------------------------
     *      +           public
     *      -           private
     *      #           protected
     *      (nada)      sin modificador   visibilidad de paquete
     *      0..*        List<X>           una coleccion
     *      0..1        X o null          referencia que puede faltar
     *      1           X final           referencia obligatoria
     *      rombo relleno   el todo hace new de las partes
     *      rombo hueco     el todo recibe las partes ya construidas
     *      flecha triangular   extends
     *
     * DONDE SE ROMPE LA CORRESPONDENCIA, EN LA PRACTICA.
     *
     * Hay tres sitios donde el codigo dice mas o menos que el diagrama, y
     * conviene senalarlos en clase:
     *
     *   1. La multiplicidad exacta. List<Inscripcion> dice "muchos", pero no
     *      dice si el maximo es 20. Esa restriccion se escribe en UML y en el
     *      codigo solo aparece como una comprobacion dentro de un metodo.
     *   2. Composicion frente a agregacion. Los dos rombos se traducen a un
     *      campo del mismo aspecto. Lo que los distingue es QUIEN hace el new, y
     *      eso hay que mirarlo en el constructor.
     *   3. La direccion de la navegabilidad. Si en el diagrama la flecha va solo
     *      de Socio a Inscripcion, en el codigo Inscripcion no deberia tener un
     *      campo socio. Si lo tiene, la relacion es bidireccional y el diagrama
     *      esta incompleto.
     */

    /** Ruta del documento con el diagrama. */
    public static final String DOCUMENTO = "recursos/uf2406/02_diagramas_uml.md";

    // ---------------------------------------------------------------------
    // Apartado 5: traduccion literal del diagrama a Java
    // ---------------------------------------------------------------------

    /** Socio del gimnasio. */
    public static class Socio {

        private String dni;                                              // - dni: String
        private String nombre;                                           // - nombre: String
        private LocalDate fechaAlta;                                     // - fechaAlta: LocalDate
        private List<Inscripcion> inscripciones = new ArrayList<>();     // 0..*

        /**
         * @param dni       documento del socio
         * @param nombre    nombre y apellidos
         * @param fechaAlta fecha en que se hizo socio
         */
        public Socio(String dni, String nombre, LocalDate fechaAlta) {
            this.dni = dni;
            this.nombre = nombre;
            this.fechaAlta = fechaAlta;
        }

        /** @return documento del socio */
        public String getDni() { return dni; }

        /** @return nombre y apellidos */
        public String getNombre() { return nombre; }

        /** @return fecha de alta */
        public LocalDate getFechaAlta() { return fechaAlta; }

        /**
         * Indica si el socio esta al corriente de pago.
         *
         * @return true si no tiene mensualidades impagadas
         */
        public boolean estaAlCorriente() {
            // En el sistema completo consulta sus cuotas. Aqui interesa la
            // firma y la visibilidad, que es lo que evalua el ejercicio.
            return true;
        }

        /**
         * Inscribe al socio en una clase colectiva.
         *
         * @param clase clase en la que inscribirse
         */
        public void inscribirse(ClaseColectiva clase) {
            Inscripcion i = new Inscripcion(LocalDate.now());
            inscripciones.add(i);
            clase.registrar(i);
        }

        /**
         * Calcula la cuota que corresponde a este socio.
         *
         * <p>Protegido a proposito: es un detalle del calculo que las subclases
         * (socio infantil, socio con tarifa reducida) necesitan redefinir, pero
         * que nadie de fuera debe invocar directamente.</p>
         *
         * @return importe de la cuota en euros
         */
        protected double calcularCuota() {
            return 40.0;
        }

        /** @return inscripciones del socio, en solo lectura */
        public List<Inscripcion> getInscripciones() {
            return Collections.unmodifiableList(inscripciones);
        }
    }

    /** Clase colectiva del gimnasio. */
    public static class ClaseColectiva {

        private String codigo;                                           // - codigo: String
        private String diaSemana;                                        // - diaSemana: String
        private LocalTime hora;                                          // - hora: LocalTime
        private int aforoMaximo;                                         // - aforoMaximo: int
        private List<Inscripcion> inscripciones = new ArrayList<>();     // 0..*

        /**
         * @param codigo      identificador de la clase
         * @param diaSemana   dia en que se imparte
         * @param hora        hora de comienzo
         * @param aforoMaximo plazas totales
         */
        public ClaseColectiva(String codigo, String diaSemana, LocalTime hora, int aforoMaximo) {
            this.codigo = codigo;
            this.diaSemana = diaSemana;
            this.hora = hora;
            this.aforoMaximo = aforoMaximo;
        }

        /** @return identificador de la clase */
        public String getCodigo() { return codigo; }

        /** @return dia en que se imparte */
        public String getDiaSemana() { return diaSemana; }

        /** @return hora de comienzo */
        public LocalTime getHora() { return hora; }

        /** @return plazas totales */
        public int getAforoMaximo() { return aforoMaximo; }

        /**
         * @return plazas que quedan libres
         */
        public int plazasLibres() {
            return aforoMaximo - inscripciones.size();
        }

        /**
         * @return true si no quedan plazas
         */
        public boolean estaCompleta() {
            return plazasLibres() <= 0;
        }

        /**
         * Registra una inscripcion en esta clase.
         *
         * @param i inscripcion a registrar
         */
        void registrar(Inscripcion i) {
            inscripciones.add(i);
        }
    }

    /** Inscripcion de un socio en una clase: la clase-asociacion del analisis. */
    public static class Inscripcion {

        private LocalDate fecha;                                         // - fecha: LocalDate
        private String estado;                                           // - estado: String

        /**
         * @param fecha fecha en que se realiza la inscripcion
         */
        public Inscripcion(LocalDate fecha) {
            this.fecha = fecha;
            this.estado = "PENDIENTE_PAGO";
        }

        /** @return fecha de la inscripcion */
        public LocalDate getFecha() { return fecha; }

        /** @return estado actual */
        public String getEstado() { return estado; }

        /** Cancela la inscripcion. */
        public void cancelar() {
            estado = "CANCELADA";
        }
    }

    // =====================================================================
    // COMPROBACION (apartado 6): reconstruir el diagrama desde el codigo
    // =====================================================================

    /**
     * Devuelve el simbolo UML que corresponde a unos modificadores Java.
     *
     * @param modificadores modificadores del miembro
     * @return "+", "-", "#" o "~" para la visibilidad de paquete
     */
    public static String simboloUml(int modificadores) {
        if (Modifier.isPublic(modificadores)) {
            return "+";
        }
        if (Modifier.isPrivate(modificadores)) {
            return "-";
        }
        if (Modifier.isProtected(modificadores)) {
            return "#";
        }
        return "~";
    }

    /**
     * Reconstruye los tres compartimentos del diagrama a partir de la clase.
     *
     * @param clase clase Java a examinar
     */
    private static void reconstruir(Class<?> clase) {
        System.out.println("    +-------------------------------------------------+");
        System.out.printf("    | %-47s |%n", clase.getSimpleName());
        System.out.println("    +-------------------------------------------------+");
        for (Field f : clase.getDeclaredFields()) {
            if (f.isSynthetic()) {
                continue;   // campos que anade el compilador, no son del diseno
            }
            String tipo = f.getGenericType().getTypeName()
                    .replace("java.lang.", "").replace("java.util.", "").replace("java.time.", "")
                    .replace("com.ifcd0112.ejercicios.uf2406.bloque2_uml.Ej04DiagramaClases$", "");
            System.out.printf("    | %s %-45s |%n", simboloUml(f.getModifiers()),
                    f.getName() + ": " + tipo);
        }
        System.out.println("    +-------------------------------------------------+");
        for (Method m : clase.getDeclaredMethods()) {
            if (m.isSynthetic()) {
                continue;
            }
            StringBuilder firma = new StringBuilder(m.getName()).append('(');
            Class<?>[] params = m.getParameterTypes();
            for (int i = 0; i < params.length; i++) {
                if (i > 0) {
                    firma.append(", ");
                }
                firma.append(params[i].getSimpleName());
            }
            firma.append("): ").append(m.getReturnType().getSimpleName());
            System.out.printf("    | %s %-45s |%n", simboloUml(m.getModifiers()), firma);
        }
        System.out.println("    +-------------------------------------------------+");
    }

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2406-E04] Diagrama de clases con notacion completa");
        System.out.println("  Diagrama dibujado (apartados 1 a 4): " + DOCUMENTO);
        System.out.println("  Apartado 6, comprobacion inversa. El diagrama de abajo NO esta");
        System.out.println("  escrito a mano: se reconstruye leyendo por reflexion los");
        System.out.println("  modificadores y los tipos reales de las clases del apartado 5.");
        System.out.println();
        reconstruir(Socio.class);
        System.out.println();
        reconstruir(ClaseColectiva.class);
        System.out.println();
        reconstruir(Inscripcion.class);

        // Las multiplicidades tambien salen del codigo: un campo de tipo List
        // es, por definicion, una multiplicidad "muchos".
        System.out.println("  Multiplicidades deducidas de los tipos:");
        for (Class<?> c : new Class<?>[] { Socio.class, ClaseColectiva.class, Inscripcion.class }) {
            for (Field f : c.getDeclaredFields()) {
                if (List.class.isAssignableFrom(f.getType())) {
                    String elemento = f.getGenericType().getTypeName();
                    elemento = elemento.substring(elemento.lastIndexOf('$') + 1).replace(">", "");
                    System.out.println("    " + c.getSimpleName() + " 1 -- 0..* " + elemento);
                }
            }
        }
        System.out.println("  Lo que el codigo NO puede decir por si solo, y por eso el diagrama");
        System.out.println("  sigue haciendo falta: si el maximo de la coleccion es 20, si el rombo");
        System.out.println("  es relleno o hueco (eso depende de quien hace el new) y si la");
        System.out.println("  navegabilidad es en un sentido o en los dos.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
