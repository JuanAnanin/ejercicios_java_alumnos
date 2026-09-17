package com.ifcd0112.ejercicios.uf2406.bloque2_uml;

import java.time.LocalDate;

/**
 * UF2406 - BLOQUE 2 - EJERCICIO 5: Diagramas de secuencia y de estados.
 *
 * <p>Criterios de evaluacion: CE1.3</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej05SecuenciaEstados {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Documenta el comportamiento dinamico de dos aspectos del sistema del
     * gimnasio.
     *
     * Se pide:
     *   1. Elabora el diagrama de secuencia del caso de uso "un socio se apunta
     *      a una clase colectiva", mostrando los objetos que intervienen y el
     *      orden exacto de los mensajes entre ellos.
     *   2. El diagrama debe reflejar la arquitectura en capas: la peticion debe
     *      atravesar la capa de presentacion, la de negocio y la de datos.
     *   3. Incluye en el diagrama el flujo alternativo de que la clase este
     *      completa.
     *   4. Elabora el diagrama de estados de la entidad Inscripcion,
     *      contemplando al menos los estados: pendiente de pago, activa,
     *      cancelada y finalizada.
     *   5. Indica en cada transicion del diagrama de estados que metodo la
     *      provoca.
     *   6. Implementa en Java la clase Inscripcion de modo que sus metodos
     *      impidan cualquier transicion no contemplada en el diagrama, lanzando
     *      una excepcion si se intenta.
     *
     * Pista: un diagrama de estados bien hecho se traduce casi literalmente en
     * las validaciones del codigo: cada transicion no dibujada en el diagrama
     * debe ser una transicion rechazada por el metodo correspondiente.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * DONDE ESTAN LOS DIAGRAMAS.
     *
     *      recursos/uf2406/02_diagramas_uml.md
     *
     * El apartado 6 es codigo, y esta aqui, ejecutado: resolver() recorre las
     * cinco transiciones validas y las nueve invalidas del diagrama, y comprueba
     * que las primeras funcionan y las segundas se rechazan.
     *
     * APARTADO 2: como se ve la arquitectura en capas en un diagrama de
     * secuencia.
     *
     * Las lineas de vida del diagrama son, en orden: Socio (actor),
     * InscripcionServlet (presentacion), GestorInscripciones (negocio),
     * ClaseDAO (datos) y la base de datos. Lo importante no es que esten, sino
     * que NINGUNA FLECHA SALTA UNA CAPA: el Servlet nunca habla directamente con
     * el DAO. Si en un diagrama de secuencia aparece esa flecha, la arquitectura
     * en capas ya esta rota en el diseno, antes incluso de escribir codigo. Es
     * una de las pocas cosas que un diagrama detecta mejor que una revision del
     * codigo terminado.
     *
     * Ademas, solo una linea de vida toma decisiones: GestorInscripciones. Ahi
     * esta el if de "si hay plazas libres". Cuando en un diagrama aparecen
     * decisiones repartidas por tres capas, la logica de negocio esta dispersa.
     *
     * APARTADO 6: por que una maquina de estados no es un simple campo.
     *
     * Guardar el estado en un String o en un enum y dejar que cualquiera lo
     * cambie con un setEstado() convierte el diagrama en decoracion: nada impide
     * pasar de FINALIZADA a PENDIENTE_PAGO. La forma correcta es que NO EXISTA
     * un setter, y que cada transicion tenga su propio metodo con su
     * comprobacion. Entonces el diagrama deja de ser documentacion y pasa a ser
     * la especificacion de las validaciones.
     */

    /** Ruta del documento con los diagramas. */
    public static final String DOCUMENTO = "recursos/uf2406/02_diagramas_uml.md";

    /**
     * Apartado 6: inscripcion con maquina de estados.
     *
     * <p>Los estados y las transiciones son exactamente los del diagrama. No hay
     * ningun metodo que permita fijar el estado directamente: la unica forma de
     * cambiarlo es a traves de pagar(), cancelar() y finalizar(), y cada uno
     * comprueba desde que estado se le puede llamar.</p>
     */
    public static class Inscripcion {

        /** Estados posibles de una inscripcion. */
        public enum Estado {
            /** Recien creada, todavia sin pagar. */
            PENDIENTE_PAGO,
            /** Pagada y en vigor. */
            ACTIVA,
            /** Anulada por el socio o por el gimnasio. */
            CANCELADA,
            /** La clase ya se ha impartido. */
            FINALIZADA
        }

        private final String dniSocio;
        private final String codigoClase;
        private final LocalDate fecha;
        private Estado estado;

        /**
         * Crea una inscripcion en el estado inicial del diagrama.
         *
         * @param dniSocio    socio que se inscribe
         * @param codigoClase clase en la que se inscribe
         * @param fecha       fecha de la inscripcion
         */
        public Inscripcion(String dniSocio, String codigoClase, LocalDate fecha) {
            this.dniSocio = dniSocio;
            this.codigoClase = codigoClase;
            this.fecha = fecha;
            this.estado = Estado.PENDIENTE_PAGO;
        }

        /** @return socio inscrito */
        public String getDniSocio() { return dniSocio; }

        /** @return clase de la inscripcion */
        public String getCodigoClase() { return codigoClase; }

        /** @return fecha de la inscripcion */
        public LocalDate getFecha() { return fecha; }

        /** @return estado actual */
        public Estado getEstado() { return estado; }

        /**
         * Transicion PENDIENTE_PAGO a ACTIVA.
         *
         * @throws IllegalStateException si la inscripcion no esta pendiente de pago
         */
        public void pagar() {
            if (estado != Estado.PENDIENTE_PAGO) {
                throw new IllegalStateException(
                        "Solo puede pagarse una inscripcion pendiente de pago, y esta esta " + estado);
            }
            estado = Estado.ACTIVA;
        }

        /**
         * Transicion a CANCELADA, desde PENDIENTE_PAGO o desde ACTIVA.
         *
         * @throws IllegalStateException si ya esta finalizada o cancelada
         */
        public void cancelar() {
            if (estado == Estado.FINALIZADA || estado == Estado.CANCELADA) {
                throw new IllegalStateException("No se puede cancelar una inscripcion " + estado);
            }
            estado = Estado.CANCELADA;
        }

        /**
         * Transicion ACTIVA a FINALIZADA.
         *
         * <p>Notese que NO se puede finalizar directamente una inscripcion
         * pendiente de pago: en el diagrama no existe esa flecha, y quien la
         * dibujo tenia razon, porque significaria dar por impartida una clase
         * que el socio no llego a pagar.</p>
         *
         * @throws IllegalStateException si la inscripcion no esta activa
         */
        public void finalizar() {
            if (estado != Estado.ACTIVA) {
                throw new IllegalStateException("Solo finalizan las inscripciones activas, y esta esta "
                        + estado);
            }
            estado = Estado.FINALIZADA;
        }

        @Override
        public String toString() {
            return "Inscripcion[" + dniSocio + " en " + codigoClase + ", " + estado + "]";
        }
    }

    // =====================================================================
    // COMPROBACION (apartado 6)
    // =====================================================================

    /**
     * Intenta una transicion y describe el resultado.
     *
     * @param descripcion  texto que identifica el intento
     * @param transicion   accion a ejecutar
     * @param deberiaValer true si el diagrama contempla esa transicion
     * @return true si el resultado coincide con lo que dice el diagrama
     */
    private static boolean intentar(String descripcion, Runnable transicion, boolean deberiaValer) {
        try {
            transicion.run();
            boolean ok = deberiaValer;
            System.out.printf("    %-46s %s%n", descripcion,
                    ok ? "permitida (correcto)" : "PERMITIDA Y NO DEBERIA");
            return ok;
        } catch (IllegalStateException e) {
            boolean ok = !deberiaValer;
            System.out.printf("    %-46s %s%n", descripcion,
                    ok ? "rechazada (correcto)" : "RECHAZADA Y DEBERIA VALER");
            return ok;
        }
    }

    /**
     * Crea una inscripcion ya situada en el estado indicado.
     *
     * @param destino estado al que llevarla
     * @return inscripcion en ese estado
     */
    private static Inscripcion enEstado(Inscripcion.Estado destino) {
        Inscripcion i = new Inscripcion("11111111A", "YOGA-1", LocalDate.of(2026, 4, 1));
        switch (destino) {
            case ACTIVA:
                i.pagar();
                break;
            case CANCELADA:
                i.cancelar();
                break;
            case FINALIZADA:
                i.pagar();
                i.finalizar();
                break;
            default:
                break;   // PENDIENTE_PAGO es el estado inicial
        }
        return i;
    }

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2406-E05] Diagramas de secuencia y de estados");
        System.out.println("  Diagramas de secuencia y de estados: " + DOCUMENTO);
        System.out.println("  Apartado 6, la maquina de estados en funcionamiento.");

        int aciertos = 0;
        int total = 0;

        System.out.println("  Transiciones que SI estan en el diagrama:");
        Inscripcion i1 = enEstado(Inscripcion.Estado.PENDIENTE_PAGO);
        total++; aciertos += intentar("PENDIENTE_PAGO --pagar()--> ACTIVA", i1::pagar, true) ? 1 : 0;
        Inscripcion i2 = enEstado(Inscripcion.Estado.PENDIENTE_PAGO);
        total++; aciertos += intentar("PENDIENTE_PAGO --cancelar()--> CANCELADA", i2::cancelar, true) ? 1 : 0;
        Inscripcion i3 = enEstado(Inscripcion.Estado.ACTIVA);
        total++; aciertos += intentar("ACTIVA --cancelar()--> CANCELADA", i3::cancelar, true) ? 1 : 0;
        Inscripcion i4 = enEstado(Inscripcion.Estado.ACTIVA);
        total++; aciertos += intentar("ACTIVA --finalizar()--> FINALIZADA", i4::finalizar, true) ? 1 : 0;

        System.out.println("  Transiciones que NO estan en el diagrama:");
        Inscripcion i5 = enEstado(Inscripcion.Estado.PENDIENTE_PAGO);
        total++; aciertos += intentar("PENDIENTE_PAGO --finalizar()--> X", i5::finalizar, false) ? 1 : 0;
        Inscripcion i6 = enEstado(Inscripcion.Estado.ACTIVA);
        total++; aciertos += intentar("ACTIVA --pagar()--> X (pagar dos veces)", i6::pagar, false) ? 1 : 0;
        Inscripcion i7 = enEstado(Inscripcion.Estado.CANCELADA);
        total++; aciertos += intentar("CANCELADA --pagar()--> X", i7::pagar, false) ? 1 : 0;
        Inscripcion i8 = enEstado(Inscripcion.Estado.CANCELADA);
        total++; aciertos += intentar("CANCELADA --cancelar()--> X", i8::cancelar, false) ? 1 : 0;
        Inscripcion i9 = enEstado(Inscripcion.Estado.CANCELADA);
        total++; aciertos += intentar("CANCELADA --finalizar()--> X", i9::finalizar, false) ? 1 : 0;
        Inscripcion i10 = enEstado(Inscripcion.Estado.FINALIZADA);
        total++; aciertos += intentar("FINALIZADA --pagar()--> X", i10::pagar, false) ? 1 : 0;
        Inscripcion i11 = enEstado(Inscripcion.Estado.FINALIZADA);
        total++; aciertos += intentar("FINALIZADA --cancelar()--> X", i11::cancelar, false) ? 1 : 0;
        Inscripcion i12 = enEstado(Inscripcion.Estado.FINALIZADA);
        total++; aciertos += intentar("FINALIZADA --finalizar()--> X", i12::finalizar, false) ? 1 : 0;

        System.out.println("  Resultado: " + aciertos + " de " + total
                + " coinciden con el diagrama de estados.");
        System.out.println("  No existe ningun setEstado(): la unica forma de cambiar de estado es");
        System.out.println("  pasar por un metodo que comprueba de donde viene. Por eso el diagrama");
        System.out.println("  no es documentacion, es la especificacion de las validaciones.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
