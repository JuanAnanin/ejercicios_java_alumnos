package com.ifcd0112.ejercicios.uf2404.bloque4_excepciones;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * UF2404 - BLOQUE 4 - EJERCICIO 10: Jerarquia de excepciones propias.
 *
 * <p>Criterios de evaluacion: CE2.10</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej10Excepciones {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Un sistema de gestion academica debe validar las matriculas de los
     * alumnos. Existen tres situaciones de error distintas que el sistema debe
     * poder distinguir: que el alumno no exista, que la asignatura este
     * completa y que el alumno ya estuviera matriculado en ella.
     *
     * Se pide:
     *   1. Disena una jerarquia de excepciones con una clase raiz comun
     *      MatriculaException y tres subclases, una por cada situacion descrita.
     *   2. Cada excepcion debe transportar informacion util sobre el error
     *      ademas del mensaje: por ejemplo, la asignatura completa debe indicar
     *      cuantas plazas hay y cuantas estan ocupadas.
     *   3. Implementa una clase GestorMatriculas con el metodo
     *      matricular(String dniAlumno, String codAsignatura) que lance la
     *      excepcion adecuada en cada caso.
     *   4. En el main, escribe un bloque try con tres bloques catch
     *      especificos, uno por cada subclase, que reaccionen de forma distinta
     *      ante cada situacion.
     *   5. Escribe despues un segundo ejemplo con un unico catch de la clase
     *      raiz, y explica en un comentario en que situaciones conviene cada uno
     *      de los dos enfoques.
     *
     * Pista: al capturar excepciones, los bloques catch deben ordenarse siempre
     * de la subclase mas especifica a la mas general; en caso contrario el
     * compilador dara error por codigo inalcanzable.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION - Apartado 1: la jerarquia
    // =====================================================================

    /**
     * Raiz comun de todos los errores de matriculacion.
     *
     * <p>Tener una raiz propia es lo que permite el apartado 5: quien no
     * necesite distinguir el motivo exacto puede capturar solo esta y recoger
     * las tres situaciones de una vez. Sin raiz comun habria que escribir
     * siempre los tres catch.</p>
     */
    public static class MatriculaException extends Exception {

        private static final long serialVersionUID = 1L;

        public MatriculaException(String mensaje) {
            super(mensaje);
        }
    }

    /**
     * Apartado 2: el alumno no existe. Transporta el DNI buscado, para que
     * quien la capture pueda por ejemplo abrir el formulario de alta ya
     * relleno con ese dato.
     */
    public static class AlumnoNoExisteException extends MatriculaException {

        private static final long serialVersionUID = 1L;

        private final String dni;

        public AlumnoNoExisteException(String dni) {
            super("No existe ningun alumno con DNI " + dni);
            this.dni = dni;
        }

        public String getDni() { return dni; }
    }

    /**
     * Apartado 2: la asignatura esta completa. Transporta plazas y ocupadas,
     * exactamente como pide el enunciado.
     */
    public static class AsignaturaCompletaException extends MatriculaException {

        private static final long serialVersionUID = 1L;

        private final String codigo;
        private final int plazas;
        private final int ocupadas;

        public AsignaturaCompletaException(String codigo, int plazas, int ocupadas) {
            super("La asignatura " + codigo + " esta completa");
            this.codigo = codigo;
            this.plazas = plazas;
            this.ocupadas = ocupadas;
        }

        public String getCodigo()  { return codigo; }
        public int getPlazas()     { return plazas; }
        public int getOcupadas()   { return ocupadas; }
    }

    /** Apartado 2: el alumno ya estaba matriculado en esa asignatura. */
    public static class YaMatriculadoException extends MatriculaException {

        private static final long serialVersionUID = 1L;

        private final String dni;
        private final String codigo;

        public YaMatriculadoException(String dni, String codigo) {
            super("El alumno " + dni + " ya estaba matriculado en " + codigo);
            this.dni = dni;
            this.codigo = codigo;
        }

        public String getDni()    { return dni; }
        public String getCodigo() { return codigo; }
    }

    // =====================================================================
    // Apartado 3: el gestor
    // =====================================================================

    /** Asignatura con su cupo de plazas. */
    private static class Asignatura {
        private final String codigo;
        private final int plazas;
        private final Set<String> matriculados = new HashSet<>();

        Asignatura(String codigo, int plazas) {
            this.codigo = codigo;
            this.plazas = plazas;
        }
    }

    /** Gestiona las matriculas y decide que excepcion corresponde a cada fallo. */
    public static class GestorMatriculas {

        private final Set<String> alumnos = new HashSet<>();
        private final Map<String, Asignatura> asignaturas = new HashMap<>();

        public void altaAlumno(String dni) {
            alumnos.add(dni);
        }

        public void altaAsignatura(String codigo, int plazas) {
            asignaturas.put(codigo, new Asignatura(codigo, plazas));
        }

        /**
         * Apartado 3: matricula a un alumno en una asignatura.
         *
         * <p>El orden de las comprobaciones no es arbitrario: primero se
         * comprueba que el alumno existe, porque si no existe las otras dos
         * preguntas ni siquiera tienen sentido. Despues si ya estaba
         * matriculado, que es una situacion neutra. Y por ultimo el cupo, que
         * es la unica que depende del estado del resto de alumnos.</p>
         *
         * @param dniAlumno     documento del alumno
         * @param codAsignatura codigo de la asignatura
         * @throws AlumnoNoExisteException      si el DNI no consta
         * @throws YaMatriculadoException       si ya estaba matriculado
         * @throws AsignaturaCompletaException  si no quedan plazas
         */
        public void matricular(String dniAlumno, String codAsignatura)
                throws MatriculaException {

            if (!alumnos.contains(dniAlumno)) {
                throw new AlumnoNoExisteException(dniAlumno);
            }
            Asignatura a = asignaturas.get(codAsignatura);
            if (a == null) {
                throw new MatriculaException("No existe la asignatura " + codAsignatura);
            }
            if (a.matriculados.contains(dniAlumno)) {
                throw new YaMatriculadoException(dniAlumno, codAsignatura);
            }
            if (a.matriculados.size() >= a.plazas) {
                throw new AsignaturaCompletaException(a.codigo, a.plazas, a.matriculados.size());
            }
            a.matriculados.add(dniAlumno);
        }
    }

    // =====================================================================
    // COMPROBACION (apartados 4 y 5)
    // =====================================================================

    /*
     * APARTADO 5: cuando conviene cada enfoque.
     *
     * CAPTURA ESPECIFICA (varios catch, uno por subclase). Se usa cuando la
     * reaccion del programa es DISTINTA segun el motivo: si el alumno no
     * existe se ofrece darlo de alta, si la asignatura esta completa se ofrece
     * lista de espera, y si ya estaba matriculado basta con avisar. Aqui la
     * informacion que transporta cada excepcion es imprescindible, porque es la
     * que permite construir esa reaccion concreta.
     *
     * CAPTURA GENERICA (un solo catch de la raiz). Se usa cuando la reaccion es
     * LA MISMA en los tres casos: tipicamente registrar la incidencia en un log
     * o devolver un error generico al usuario. Escribir tres catch identicos
     * seria repeticion inutil, y ademas obligaria a tocar el codigo cada vez
     * que se anadiera una cuarta subclase.
     *
     * Regla practica: si los catch que estas escribiendo tienen el mismo cuerpo,
     * te sobra con el de la raiz. Si tienen cuerpos distintos, necesitas los
     * especificos.
     */

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2404-E10] Jerarquia de excepciones propias");

        GestorMatriculas gestor = new GestorMatriculas();
        gestor.altaAlumno("11111111A");
        gestor.altaAlumno("22222222B");
        gestor.altaAsignatura("PROG-1", 2);

        // Apartado 4: captura ESPECIFICA. Tres situaciones, tres reacciones.
        // Ojo al orden de los catch: de la mas especifica a la mas general.
        // Si MatriculaException fuera el primero, los otros tres serian codigo
        // inalcanzable y el compilador daria error.
        String[][] intentos = {
            { "99999999Z", "PROG-1" },   // alumno que no existe
            { "11111111A", "PROG-1" },   // correcta
            { "11111111A", "PROG-1" },   // repetida: ya matriculado
            { "22222222B", "PROG-1" },   // correcta: agota las 2 plazas
            { "22222222B", "PROG-1" }    // repetida sobre asignatura llena
        };

        gestor.altaAlumno("33333333C");

        for (String[] intento : intentos) {
            try {
                gestor.matricular(intento[0], intento[1]);
                System.out.println("  Matricula correcta: " + intento[0] + " en " + intento[1]);
            } catch (AlumnoNoExisteException e) {
                System.out.println("  " + e.getMessage()
                        + " -> se ofrece el alta con DNI " + e.getDni());
            } catch (YaMatriculadoException e) {
                System.out.println("  Aviso: " + e.getMessage() + " -> no se hace nada");
            } catch (AsignaturaCompletaException e) {
                System.out.println("  " + e.getMessage() + " (" + e.getOcupadas() + "/"
                        + e.getPlazas() + ") -> se ofrece lista de espera");
            } catch (MatriculaException e) {
                System.out.println("  Otro problema de matricula: " + e.getMessage());
            }
        }

        // Un alumno que si cabria si hubiera plazas, para provocar el caso de
        // asignatura completa de forma limpia.
        try {
            gestor.matricular("33333333C", "PROG-1");
        } catch (AsignaturaCompletaException e) {
            System.out.println("  " + e.getMessage() + " (" + e.getOcupadas() + "/"
                    + e.getPlazas() + ") -> se ofrece lista de espera");
        } catch (MatriculaException e) {
            System.out.println("  " + e.getMessage());
        }

        // Apartado 5: captura GENERICA. Un solo catch para las tres.
        System.out.println("  Con un unico catch de la raiz (solo interesa registrar):");
        for (String[] intento : new String[][] { { "99999999Z", "PROG-1" }, { "33333333C", "PROG-1" } }) {
            try {
                gestor.matricular(intento[0], intento[1]);
            } catch (MatriculaException e) {
                System.out.println("    Incidencia registrada [" + e.getClass().getSimpleName()
                        + "]: " + e.getMessage());
            }
        }
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
