package com.ifcd0112.ejercicios.uf2406.bloque5_documentacion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ifcd0112.ejercicios.uf2406.bloque4_pruebas.Ej09Junit;

/**
 * UF2406 - BLOQUE 5 - EJERCICIO 12: Gestion de un cambio de especificacion.
 *
 * <p>Criterios de evaluacion: CE4.1, CE4.3, CE4.5</p>
 *
 * <p>Nota de cobertura: el cuaderno en .docx etiqueta este ejercicio solo con
 * CE4.1 y CE4.3. Se le ha anadido aqui CE4.5 porque sus apartados 4, 5 y 6
 * recorren, uno por uno, los cinco puntos de ese criterio: modificar el codigo,
 * dejar el historico en la cabecera, ajustar los programas de prueba, ejecutar
 * la regresion y actualizar la documentacion afectada. Conviene reflejar el
 * cambio tambien en el .docx. Ver recursos/MATRIZ_CE.md.</p>
 *
 * <p>HISTORIAL DE MODIFICACIONES</p>
 * <pre>
 * 1.1  15/04/2026  RF-09: se anade la gestion de lista de espera. Las
 *                  inscripciones sobre clases completas pasan a estado
 *                  EN_ESPERA en lugar de rechazarse, y se promocionan
 *                  automaticamente al producirse una baja.
 * 1.0  02/02/2026  Version inicial de la gestion de inscripciones.
 * </pre>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.1
 */
public class Ej12CambioEspecificacion {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Con el sistema del gimnasio ya en funcionamiento, el cliente solicita un
     * cambio: a partir de ahora las clases colectivas podran tener lista de
     * espera cuando se complete el aforo, y los socios en lista de espera
     * pasaran automaticamente a inscritos si se produce una baja.
     *
     * Se pide:
     *   1. Analiza el impacto del cambio: identifica que requisitos, que clases,
     *      que tablas de la base de datos y que pruebas se veran afectados.
     *      Elabora la tabla de impacto.
     *   2. Enumera los pasos que seguirias, en orden, para incorporar el cambio
     *      de forma segura sobre un sistema ya en produccion.
     *   3. Explica que papel juegan las pruebas ya existentes en este proceso y
     *      por que seria temerario modificar el codigo sin ellas.
     *   4. Implementa el cambio en las clases afectadas, registrando en la
     *      cabecera de cada componente modificado la fecha, el motivo y una
     *      breve descripcion de la modificacion.
     *   5. Actualiza la documentacion Javadoc y los diagramas UML afectados por
     *      el cambio.
     *   6. Ejecuta la bateria completa de pruebas de regresion y documenta el
     *      resultado.
     *   7. Describe como registrarias este cambio en un sistema de control de
     *      versiones: que mensaje escribirias y por que es importante que sea
     *      descriptivo.
     *
     * Pista: la secuencia profesional es siempre la misma: analizar impacto,
     * asegurar que existen pruebas del comportamiento actual, modificar,
     * ejecutar regresion, actualizar documentacion y registrar el cambio de
     * forma trazable.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * APARTADO 1: tabla de analisis de impacto.
     *
     *   Elemento afectado           Tipo        Impacto
     *   -------------------------   ---------   ---------------------------------
     *   RF-06 (rechazar si lleno)   Requisito   MODIFICAR: ahora ofrece espera
     *   RF-09 (nuevo)               Requisito   CREAR: gestion de lista de espera
     *   Inscripcion                 Clase       Nuevo estado EN_ESPERA y sus
     *                                           transiciones
     *   ClaseColectiva              Clase       Nuevo metodo promocionarEspera()
     *   GestorInscripciones         Clase       Modificar inscribir() y cancelar()
     *   Tabla inscripcion           BD          Ampliar los valores del campo estado
     *   Diagrama de estados         Doc.        Anadir estado y transiciones
     *   Diagrama de casos de uso    Doc.        Anadir "apuntarse a lista de espera"
     *                                           como extend de "Inscribirse"
     *   Pruebas de inscripcion      Pruebas     Anadir casos de lista de espera
     *
     * El detalle que se olvida siempre: la tabla de la base de datos. Un cambio
     * de estados no se queda en el codigo. Si el campo estado tiene una
     * restriccion CHECK con los cuatro valores antiguos, el primer INSERT con
     * EN_ESPERA falla en produccion y no en desarrollo, porque en desarrollo
     * nadie recreo la tabla.
     *
     * APARTADO 2: procedimiento de cambio seguro, en orden.
     *
     *   1. Analizar el impacto (tabla anterior) y estimar el esfuerzo.
     *   2. VERIFICAR que existen pruebas del comportamiento actual y que pasan.
     *      Si no existen, escribirlas ANTES de tocar nada.
     *   3. Actualizar primero la especificacion de requisitos y los diagramas.
     *   4. Crear una rama en el control de versiones para el cambio.
     *   5. Modificar el codigo, registrando en la cabecera de cada clase tocada.
     *   6. Anadir pruebas nuevas para la funcionalidad de lista de espera.
     *   7. Ejecutar la bateria COMPLETA de regresion: nada de lo anterior debe
     *      romperse.
     *   8. Actualizar el Javadoc afectado.
     *   9. Registrar el cambio con un mensaje descriptivo e integrarlo.
     *
     * El paso 2 es el unico que la gente se salta, y es el que sostiene todos
     * los demas.
     *
     * APARTADO 3: que papel juegan las pruebas existentes.
     *
     * Son la unica forma de saber que lo que ANTES funcionaba sigue funcionando.
     * Sin ellas, la unica manera de comprobar que el cambio no ha roto nada es
     * probar a mano todo el sistema, y eso no se hace: se prueba lo que uno
     * recuerda, se olvida un caso, y el fallo aparece en produccion tres semanas
     * mas tarde, cuando ya nadie relaciona las dos cosas.
     *
     * Este cambio concreto es especialmente peligroso porque TOCA UNA REGLA QUE
     * YA EXISTIA. Antes, inscribirse en una clase llena lanzaba excepcion;
     * ahora, en general, no. Es facil, al implementarlo, hacer que tampoco la
     * lance en los casos en que si debe (por ejemplo, un socio con cuotas
     * pendientes). Las pruebas antiguas son las que sujetan esa parte.
     *
     * Y hay un matiz que conviene explicar: aqui una prueba antigua SI debe
     * cambiar, la que comprobaba que una clase completa rechazaba la
     * inscripcion. Eso no invalida el argumento, lo afina: la regresion no dice
     * "nada puede cambiar", dice "todo lo que cambie tiene que ser una decision
     * consciente". Si una prueba falla y no sabias que iba a fallar, ahi hay un
     * problema.
     *
     * APARTADO 5: documentacion actualizada.
     *
     *   - El Javadoc de esta clase lleva el historial de modificaciones.
     *   - El diagrama de estados actualizado esta en recursos/uf2406/, con el
     *     nuevo estado EN_ESPERA y sus dos transiciones.
     *
     * APARTADO 7: el mensaje del control de versiones.
     *
     *   "RF-09: gestion de lista de espera en clases completas.
     *
     *    Las inscripciones sobre clases con aforo lleno pasan a estado
     *    EN_ESPERA en lugar de rechazarse, y se promocionan automaticamente a
     *    PENDIENTE_PAGO al producirse una baja, por orden de llegada.
     *
     *    Modifica el comportamiento de GestorInscripciones.inscribir(), que
     *    hasta ahora lanzaba ClaseCompletaException. La prueba
     *    lanzaExcepcionSiLaClaseEstaCompleta se sustituye por
     *    inscripcionEnClaseCompletaEntraEnListaDeEspera.
     *
     *    Incluye 4 pruebas nuevas. Afecta a la tabla inscripcion: hay que
     *    ampliar los valores admitidos del campo estado."
     *
     * Por que importa que sea descriptivo: dentro de seis meses, cuando alguien
     * investigue por que una inscripcion quedo en espera, va a mirar el
     * historial. Un mensaje que diga "cambios" o "arreglos varios" convierte esa
     * investigacion en arqueologia. La primera linea dice QUE, el cuerpo dice
     * POR QUE, y la referencia al requisito enlaza el codigo con la decision de
     * negocio que lo motivo.
     */

    /** Ruta del diagrama de estados actualizado. */
    public static final String DOCUMENTO = "recursos/uf2406/05_documentacion_y_cierre.md";

    // ---------------------------------------------------------------------
    // Apartado 4: el cambio implementado
    // ---------------------------------------------------------------------

    /**
     * Inscripcion con el estado EN_ESPERA anadido por el RF-09.
     *
     * <p>Es la version 1.1 de la clase del ejercicio 5. El diagrama de estados
     * crece con un estado y tres transiciones nuevas:</p>
     *
     * <pre>
     *   (inicio, clase completa) --> EN_ESPERA
     *   EN_ESPERA --promocionar()--> PENDIENTE_PAGO
     *   EN_ESPERA --cancelar()-----> CANCELADA
     * </pre>
     */
    public static class Inscripcion {

        /** Estados posibles tras el cambio de especificacion. */
        public enum Estado {
            /** Anadido en 1.1: la clase estaba llena y el socio espera turno. */
            EN_ESPERA,
            /** Tiene plaza, pendiente de pagarla. */
            PENDIENTE_PAGO,
            /** Pagada y en vigor. */
            ACTIVA,
            /** Anulada. */
            CANCELADA,
            /** La clase ya se ha impartido. */
            FINALIZADA
        }

        private final String dniSocio;
        private final String codigoClase;
        private final LocalDate fecha;
        private Estado estado;

        /**
         * @param dniSocio    socio que se inscribe
         * @param codigoClase clase solicitada
         * @param fecha       fecha de la solicitud
         * @param enEspera    true si la clase estaba completa
         */
        public Inscripcion(String dniSocio, String codigoClase, LocalDate fecha, boolean enEspera) {
            this.dniSocio = dniSocio;
            this.codigoClase = codigoClase;
            this.fecha = fecha;
            this.estado = enEspera ? Estado.EN_ESPERA : Estado.PENDIENTE_PAGO;
        }

        /** @return socio inscrito */
        public String getDniSocio() { return dniSocio; }

        /** @return clase solicitada */
        public String getCodigoClase() { return codigoClase; }

        /** @return fecha de la solicitud */
        public LocalDate getFecha() { return fecha; }

        /** @return estado actual */
        public Estado getEstado() { return estado; }

        /**
         * Anadido en 1.1. Transicion EN_ESPERA a PENDIENTE_PAGO.
         *
         * @throws IllegalStateException si la inscripcion no estaba en espera
         */
        public void promocionar() {
            if (estado != Estado.EN_ESPERA) {
                throw new IllegalStateException("Solo se promociona desde EN_ESPERA, y esta esta "
                        + estado);
            }
            estado = Estado.PENDIENTE_PAGO;
        }

        /**
         * Transicion PENDIENTE_PAGO a ACTIVA.
         *
         * <p>Modificado en 1.1 solo en el mensaje: ahora tambien hay que
         * rechazar el pago de una inscripcion en espera, que todavia no tiene
         * plaza.</p>
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
         * Transicion a CANCELADA desde EN_ESPERA, PENDIENTE_PAGO o ACTIVA.
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
         * @throws IllegalStateException si la inscripcion no esta activa
         */
        public void finalizar() {
            if (estado != Estado.ACTIVA) {
                throw new IllegalStateException("Solo finalizan las inscripciones activas, y esta esta "
                        + estado);
            }
            estado = Estado.FINALIZADA;
        }
    }

    /**
     * Clase colectiva con lista de espera.
     *
     * <p>Version 1.1: se anade promocionarEspera(), invocado cuando se libera
     * una plaza.</p>
     */
    public static class ClaseColectiva {

        private final String codigo;
        private final int aforoMaximo;
        private final List<Inscripcion> inscritos = new ArrayList<>();
        private final List<Inscripcion> espera = new ArrayList<>();

        /**
         * @param codigo      identificador de la clase
         * @param aforoMaximo plazas totales
         */
        public ClaseColectiva(String codigo, int aforoMaximo) {
            this.codigo = codigo;
            this.aforoMaximo = aforoMaximo;
        }

        /** @return identificador de la clase */
        public String getCodigo() { return codigo; }

        /** @return plazas libres */
        public int plazasLibres() { return aforoMaximo - inscritos.size(); }

        /** @return true si no quedan plazas */
        public boolean estaCompleta() { return plazasLibres() <= 0; }

        /** @return numero de socios esperando turno */
        public int enEspera() { return espera.size(); }

        /** @return numero de socios con plaza */
        public int numeroInscritos() { return inscritos.size(); }

        /**
         * Registra una solicitud: con plaza si la hay, en espera si no.
         *
         * @param dniSocio socio que solicita
         * @param fecha    fecha de la solicitud
         * @return la inscripcion creada
         */
        public Inscripcion solicitar(String dniSocio, LocalDate fecha) {
            boolean completa = estaCompleta();
            Inscripcion i = new Inscripcion(dniSocio, codigo, fecha, completa);
            if (completa) {
                espera.add(i);
            } else {
                inscritos.add(i);
            }
            return i;
        }

        /**
         * Anadido en 1.1: da de baja una inscripcion y promociona al primero de
         * la lista de espera, si lo hay.
         *
         * @param inscripcion inscripcion que causa baja
         * @return la inscripcion promocionada, o null si no habia nadie esperando
         */
        public Inscripcion darDeBaja(Inscripcion inscripcion) {
            inscripcion.cancelar();
            boolean tenriaPlaza = inscritos.remove(inscripcion);
            espera.remove(inscripcion);
            if (!tenriaPlaza || espera.isEmpty()) {
                // Si quien se da de baja estaba en la lista de espera, no se
                // libera ninguna plaza y no hay a quien promocionar. Este caso
                // se olvida con facilidad y produce inscritos de mas.
                return null;
            }
            // Por orden de llegada: el primero de la lista. Usar cualquier otro
            // criterio seria una decision de negocio, y esa la toma el cliente.
            Inscripcion promocionado = espera.remove(0);
            promocionado.promocionar();
            inscritos.add(promocionado);
            return promocionado;
        }
    }

    // =====================================================================
    // COMPROBACION (apartado 6): la bateria de regresion
    // =====================================================================

    /**
     * Bateria de regresion: pruebas antiguas que deben seguir pasando y pruebas
     * nuevas de la funcionalidad anadida.
     *
     * @return la bateria lista para ejecutar
     */
    public static Ej09Junit.MiniTest bateriaRegresion() {
        final ClaseColectiva[] clase = new ClaseColectiva[1];
        final LocalDate hoy = LocalDate.of(2026, 4, 15);

        return new Ej09Junit.MiniTest()
            .antesDeCada(() -> clase[0] = new ClaseColectiva("YOGA-1", 2))

            // ---- Pruebas ANTIGUAS: el comportamiento anterior sigue intacto ----
            .prueba("[antigua] conPlazasLibresSeInscribeConPlaza", () -> {
                Inscripcion i = clase[0].solicitar("11111111A", hoy);
                Ej09Junit.MiniTest.assertIgual(Inscripcion.Estado.PENDIENTE_PAGO, i.getEstado());
                Ej09Junit.MiniTest.assertCierto(clase[0].plazasLibres() == 1, "debia quedar 1 plaza");
            })
            .prueba("[antigua] alPagarLaInscripcionPasaAActiva", () -> {
                Inscripcion i = clase[0].solicitar("11111111A", hoy);
                i.pagar();
                Ej09Junit.MiniTest.assertIgual(Inscripcion.Estado.ACTIVA, i.getEstado());
            })
            .prueba("[antigua] noSePuedeFinalizarUnaInscripcionSinPagar", () ->
                Ej09Junit.MiniTest.assertLanza(IllegalStateException.class,
                        () -> clase[0].solicitar("11111111A", hoy).finalizar()))
            .prueba("[antigua] noSePuedeCancelarDosVeces", () -> {
                Inscripcion i = clase[0].solicitar("11111111A", hoy);
                i.cancelar();
                Ej09Junit.MiniTest.assertLanza(IllegalStateException.class, i::cancelar);
            })
            .prueba("[antigua] elAforoSigueSiendoElLimiteDePlazas", () -> {
                clase[0].solicitar("11111111A", hoy);
                clase[0].solicitar("22222222B", hoy);
                clase[0].solicitar("33333333C", hoy);
                Ej09Junit.MiniTest.assertCierto(clase[0].numeroInscritos() == 2,
                        "el aforo son 2 y hay " + clase[0].numeroInscritos() + " inscritos");
            })

            // ---- Pruebas NUEVAS: la funcionalidad del RF-09 ----
            .prueba("[nueva] inscripcionEnClaseCompletaEntraEnListaDeEspera", () -> {
                clase[0].solicitar("11111111A", hoy);
                clase[0].solicitar("22222222B", hoy);
                Inscripcion tercero = clase[0].solicitar("33333333C", hoy);
                Ej09Junit.MiniTest.assertIgual(Inscripcion.Estado.EN_ESPERA, tercero.getEstado());
                Ej09Junit.MiniTest.assertCierto(clase[0].enEspera() == 1, "debia haber 1 en espera");
            })
            .prueba("[nueva] unaBajaPromocionaAlPrimeroDeLaEspera", () -> {
                Inscripcion primero = clase[0].solicitar("11111111A", hoy);
                clase[0].solicitar("22222222B", hoy);
                Inscripcion tercero = clase[0].solicitar("33333333C", hoy);
                Inscripcion promocionado = clase[0].darDeBaja(primero);
                Ej09Junit.MiniTest.assertCierto(promocionado == tercero,
                        "debia promocionar al tercero, que era el primero en esperar");
                Ej09Junit.MiniTest.assertIgual(Inscripcion.Estado.PENDIENTE_PAGO, tercero.getEstado());
                Ej09Junit.MiniTest.assertCierto(clase[0].enEspera() == 0, "la espera debia vaciarse");
            })
            .prueba("[nueva] laPromocionRespetaElOrdenDeLlegada", () -> {
                Inscripcion primero = clase[0].solicitar("11111111A", hoy);
                clase[0].solicitar("22222222B", hoy);
                Inscripcion tercero = clase[0].solicitar("33333333C", hoy);
                clase[0].solicitar("44444444D", hoy);
                Inscripcion promocionado = clase[0].darDeBaja(primero);
                Ej09Junit.MiniTest.assertCierto(promocionado == tercero,
                        "debia entrar el tercero, no el cuarto");
                Ej09Junit.MiniTest.assertCierto(clase[0].enEspera() == 1,
                        "debia quedar uno esperando");
            })
            .prueba("[nueva] noSePuedePagarUnaInscripcionEnEspera", () -> {
                clase[0].solicitar("11111111A", hoy);
                clase[0].solicitar("22222222B", hoy);
                Inscripcion enEspera = clase[0].solicitar("33333333C", hoy);
                // Todavia no tiene plaza: cobrarle seria cobrar por nada.
                Ej09Junit.MiniTest.assertLanza(IllegalStateException.class, enEspera::pagar);
            })
            .prueba("[nueva] laBajaDeAlguienEnEsperaNoPromocionaANadie", () -> {
                clase[0].solicitar("11111111A", hoy);
                clase[0].solicitar("22222222B", hoy);
                Inscripcion tercero = clase[0].solicitar("33333333C", hoy);
                Inscripcion cuarto = clase[0].solicitar("44444444D", hoy);
                // Se da de baja el TERCERO, que estaba en espera: no libera plaza.
                Inscripcion promocionado = clase[0].darDeBaja(tercero);
                Ej09Junit.MiniTest.assertCierto(promocionado == null,
                        "no debia promocionar a nadie");
                Ej09Junit.MiniTest.assertIgual(Inscripcion.Estado.EN_ESPERA, cuarto.getEstado());
                Ej09Junit.MiniTest.assertCierto(clase[0].numeroInscritos() == 2,
                        "el aforo no debia alterarse");
            });
    }

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2406-E12] Gestion de un cambio de especificacion");
        System.out.println("  Tabla de impacto y diagrama de estados actualizado:");
        System.out.println("    " + DOCUMENTO);
        System.out.println("  Apartado 1, resumen del impacto: 2 requisitos, 3 clases, 1 tabla de la");
        System.out.println("    base de datos, 2 diagramas y la bateria de pruebas. El elemento que");
        System.out.println("    mas se olvida es la tabla: si el campo estado tiene un CHECK con los");
        System.out.println("    valores antiguos, el primer INSERT con EN_ESPERA falla en produccion.");
        System.out.println("  Apartado 4: implementado en este fichero, con el historial de");
        System.out.println("    modificaciones en la cabecera Javadoc de la clase.");

        System.out.println("  Apartado 6, bateria de regresion:");
        Ej09Junit.Resultado r = bateriaRegresion().ejecutar(true, "    ");
        System.out.println("    " + r.informe());
        System.out.println("    Las cinco primeras son ANTERIORES al cambio y siguen pasando: eso es");
        System.out.println("    lo que dice que no se ha roto nada. Las cinco nuevas verifican el");
        System.out.println("    RF-09, incluido el caso que mas se escapa: dar de baja a alguien que");
        System.out.println("    estaba en la lista de espera no libera plaza y no debe promocionar.");

        System.out.println("  Apartado 3: sin pruebas previas, la unica forma de saber que el cambio");
        System.out.println("    no ha roto nada seria probar a mano el sistema entero, y eso no se");
        System.out.println("    hace: se prueba lo que uno recuerda. Este cambio es ademas peligroso");
        System.out.println("    porque TOCA UNA REGLA QUE YA EXISTIA.");
        System.out.println("    Matiz: una prueba antigua SI debia cambiar, la que comprobaba que una");
        System.out.println("    clase completa rechazaba la inscripcion. La regresion no dice que");
        System.out.println("    nada pueda cambiar, dice que todo lo que cambie sea una decision");
        System.out.println("    consciente. Si una prueba falla y no sabias que iba a fallar, ahi hay");
        System.out.println("    un problema.");
        System.out.println("  Apartado 7: el mensaje de control de versiones esta redactado entero en");
        System.out.println("    los comentarios de este fichero. Primera linea el QUE, cuerpo el POR");
        System.out.println("    QUE, y referencia al requisito para enlazar codigo y decision.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
