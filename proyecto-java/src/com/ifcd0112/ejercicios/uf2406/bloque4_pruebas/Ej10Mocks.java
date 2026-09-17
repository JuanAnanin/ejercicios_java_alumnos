package com.ifcd0112.ejercicios.uf2406.bloque4_pruebas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.ifcd0112.ejercicios.uf2406.bloque2_uml.Ej05SecuenciaEstados.Inscripcion;

/**
 * UF2406 - BLOQUE 4 - EJERCICIO 10: Pruebas con dependencias simuladas (mocks).
 *
 * <p>Criterios de evaluacion: CE2.7</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej10Mocks {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * La clase GestorInscripciones del gimnasio necesita consultar el aforo de
     * una clase a traves de un ClaseDAO que accede a la base de datos. Debes
     * probar la logica de negocio sin depender de que exista una base de datos
     * disponible.
     *
     * Se pide:
     *   1. Refactoriza GestorInscripciones para que reciba el ClaseDAO en su
     *      constructor en lugar de crearlo internamente, y explica que principio
     *      de diseno estas aplicando.
     *   2. Extrae de ClaseDAO una interfaz con los metodos que
     *      GestorInscripciones necesita, y haz que la clase dependa de la
     *      interfaz y no de la implementacion concreta.
     *   3. Escribe una prueba que utilice un objeto simulado configurado para
     *      devolver un aforo con plazas libres, y verifica que la inscripcion se
     *      completa.
     *   4. Escribe otra prueba con el simulado configurado para devolver una
     *      clase completa, y verifica que se lanza la excepcion correspondiente.
     *   5. Comprueba cronometrando ambas pruebas que se ejecutan en
     *      milisegundos, frente a lo que tardarian accediendo a una base de
     *      datos real.
     *   6. Explica por escrito que diferencia hay entre lo que verifica esta
     *      prueba (unitaria) y lo que verificaria una prueba de integracion de
     *      la misma funcionalidad.
     *
     * Pista: si una clase crea internamente sus dependencias con new, resulta
     * imposible sustituirlas al probarla. Recibirlas desde fuera es lo que hace
     * la clase verdaderamente testeable.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * SOBRE LA IMPLEMENTACION.
     *
     * La solucion que se entrega usa Mockito, y esta escrita en:
     *
     *      recursos/uf2406/GestorInscripcionesTest.java
     *
     * Como este proyecto no lleva dependencias, aqui los objetos simulados estan
     * escritos a mano. No es una limitacion, es una ventaja didactica: se ve que
     * un mock no tiene nada de especial. Es una implementacion de la interfaz
     * que devuelve lo que le digan y apunta a quien la ha llamado.
     *
     *      Mockito                            A mano
     *      ---------------------------------  --------------------------------
     *      mock(RepositorioClases.class)      new RepositorioSimulado()
     *      when(...).thenReturn(clase)        simulado.devolver(clase)
     *      verify(repo, times(1)).guardar()   simulado.vecesGuardado() == 1
     *      verify(repo, never()).guardar()    simulado.vecesGuardado() == 0
     *
     * Mockito ahorra escribir la clase simulada, sobre todo si la interfaz tiene
     * quince metodos. Lo que no hace es cambiar la idea: la idea es que la clase
     * bajo prueba no sepa quien le esta contestando.
     *
     * APARTADO 1: que principio se esta aplicando.
     *
     * Dos, uno dentro de otro.
     *
     *   INYECCION DE DEPENDENCIAS. La clase declara lo que necesita y se lo dan
     *   hecho, en lugar de fabricarselo con new. Es la tecnica.
     *
     *   INVERSION DE DEPENDENCIAS, la D de SOLID. Es el principio de fondo, y
     *   dice algo mas fuerte: que los modulos de alto nivel no deben depender de
     *   los de bajo nivel, sino los dos de una abstraccion. Aqui,
     *   GestorInscripciones (negocio) no depende de ClaseDAO (datos), sino que
     *   los dos dependen de la interfaz RepositorioClases. Y la interfaz, ojo,
     *   pertenece conceptualmente a la capa de negocio: es el negocio quien dice
     *   que necesita, y la capa de datos quien se adapta.
     *
     * Esa es la diferencia entre las dos ideas. La inyeccion sola permitiria
     * seguir dependiendo de la clase concreta ClaseDAO, y entonces no se podria
     * sustituir por nada. Hace falta el apartado 2 para completar el apartado 1.
     *
     * APARTADO 6: unitaria frente a integracion.
     *
     *   Esta prueba (UNITARIA) verifica LA LOGICA del gestor: comprueba el aforo
     *   antes de guardar? Guarda cuando hay sitio? Deja de guardar cuando no lo
     *   hay? Se ejecuta en milisegundos, no necesita nada instalado y, si falla,
     *   el fallo esta en el gestor, porque no hay nada mas en juego.
     *
     *   Una prueba de INTEGRACION verificaria que el DAO real escribe de verdad
     *   en la base de datos real: que la sentencia SQL es correcta, que los tipos
     *   encajan, que la clave ajena existe, que la transaccion confirma. Tarda
     *   mucho mas, necesita una base de datos preparada y, cuando falla, hay que
     *   averiguar cual de las piezas ha fallado.
     *
     *   Ninguna sustituye a la otra. La unitaria dice si la logica es correcta;
     *   la de integracion, si las piezas encajan. Un sistema con muchas pruebas
     *   unitarias y ninguna de integracion puede tener todas sus clases
     *   correctas y no funcionar. Y al reves: solo con pruebas de integracion,
     *   cada fallo obliga a una investigacion.
     */

    /** Ruta de la version con Mockito. */
    public static final String VERSION_MOCKITO = "recursos/uf2406/GestorInscripcionesTest.java";

    /** No quedan plazas en la clase solicitada. */
    public static class ClaseCompletaException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        /**
         * @param codigo codigo de la clase completa
         */
        public ClaseCompletaException(String codigo) {
            super("La clase " + codigo + " ha alcanzado su aforo maximo");
        }
    }

    /** Clase colectiva con su aforo. */
    public static class ClaseColectiva {

        private final String codigo;
        private final int aforoMaximo;
        private int inscritos;

        /**
         * @param codigo      identificador de la clase
         * @param aforoMaximo plazas totales
         * @param inscritos   plazas ya ocupadas
         */
        public ClaseColectiva(String codigo, int aforoMaximo, int inscritos) {
            this.codigo = codigo;
            this.aforoMaximo = aforoMaximo;
            this.inscritos = inscritos;
        }

        /** @return identificador de la clase */
        public String getCodigo() { return codigo; }

        /** @return plazas libres */
        public int plazasLibres() { return aforoMaximo - inscritos; }

        /** @return true si no quedan plazas */
        public boolean estaCompleta() { return plazasLibres() <= 0; }

        /** Ocupa una plaza. */
        public void ocuparPlaza() { inscritos++; }
    }

    /**
     * Apartado 2: la ABSTRACCION de la que depende el negocio.
     *
     * <p>Solo declara los dos metodos que GestorInscripciones necesita, no los
     * quince que pueda tener el DAO real. Una interfaz se define por lo que
     * necesita quien la usa, no por lo que ofrece quien la implementa.</p>
     */
    public interface RepositorioClases {

        /**
         * @param codigo codigo de la clase
         * @return la clase, o null si no existe
         */
        ClaseColectiva buscarPorCodigo(String codigo);

        /**
         * @param inscripcion inscripcion a persistir
         */
        void guardar(Inscripcion inscripcion);
    }

    /** Apartado 1: el gestor recibe el repositorio, no lo crea. */
    public static class GestorInscripciones {

        private final RepositorioClases repositorio;

        /**
         * @param repositorio origen de datos, inyectado desde fuera
         */
        public GestorInscripciones(RepositorioClases repositorio) {
            this.repositorio = repositorio;   // no hay ningun new aqui
        }

        /**
         * Inscribe a un socio en una clase colectiva.
         *
         * @param dniSocio    socio que se inscribe
         * @param codigoClase clase solicitada
         * @return la inscripcion creada, en estado PENDIENTE_PAGO
         * @throws ClaseCompletaException si la clase ha alcanzado su aforo
         */
        public Inscripcion inscribir(String dniSocio, String codigoClase) {
            ClaseColectiva clase = repositorio.buscarPorCodigo(codigoClase);
            if (clase == null) {
                throw new IllegalArgumentException("No existe la clase " + codigoClase);
            }
            // La regla de negocio: comprobar ANTES de guardar. Es exactamente lo
            // que verifica la prueba del apartado 4.
            if (clase.estaCompleta()) {
                throw new ClaseCompletaException(codigoClase);
            }
            Inscripcion inscripcion = new Inscripcion(dniSocio, codigoClase, LocalDate.now());
            clase.ocuparPlaza();
            repositorio.guardar(inscripcion);
            return inscripcion;
        }
    }

    /** Objeto simulado escrito a mano: devuelve lo que se le diga y apunta las llamadas. */
    public static class RepositorioSimulado implements RepositorioClases {

        private ClaseColectiva respuesta;
        private final List<Inscripcion> guardadas = new ArrayList<>();
        private int vecesBuscado;

        /**
         * Configura lo que devolvera buscarPorCodigo. Equivale al when de Mockito.
         *
         * @param clase clase que se devolvera
         * @return este mismo objeto, para encadenar
         */
        public RepositorioSimulado devolver(ClaseColectiva clase) {
            this.respuesta = clase;
            return this;
        }

        @Override
        public ClaseColectiva buscarPorCodigo(String codigo) {
            vecesBuscado++;
            return respuesta;
        }

        @Override
        public void guardar(Inscripcion inscripcion) {
            guardadas.add(inscripcion);
        }

        /** @return numero de veces que se llamo a guardar. Equivale al verify. */
        public int vecesGuardado() { return guardadas.size(); }

        /** @return numero de veces que se llamo a buscarPorCodigo */
        public int vecesBuscado() { return vecesBuscado; }
    }

    /**
     * Repositorio que imita el coste de una base de datos real.
     *
     * <p>Existe solo para el apartado 5: permite medir de verdad la diferencia
     * en lugar de afirmarla. Los 40 milisegundos son un orden de magnitud
     * razonable para abrir conexion, ejecutar una consulta y devolverla en una
     * red local; contra un servidor remoto seria bastante mas.</p>
     */
    public static class RepositorioLento implements RepositorioClases {

        /** Retardo que se atribuye a cada acceso a la base de datos. */
        public static final long RETARDO_MS = 40;

        private final ClaseColectiva respuesta;

        /**
         * @param respuesta clase que devolvera tras el retardo
         */
        public RepositorioLento(ClaseColectiva respuesta) {
            this.respuesta = respuesta;
        }

        @Override
        public ClaseColectiva buscarPorCodigo(String codigo) {
            dormir();
            return respuesta;
        }

        @Override
        public void guardar(Inscripcion inscripcion) {
            dormir();
        }

        private void dormir() {
            try {
                Thread.sleep(RETARDO_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // =====================================================================
    // COMPROBACION (apartados 3, 4 y 5)
    // =====================================================================

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2406-E10] Pruebas con dependencias simuladas (mocks)");
        System.out.println("  Version con Mockito: " + VERSION_MOCKITO);
        System.out.println("  Aqui los simulados estan escritos a mano, sin dependencias.");

        // Apartados 3 y 4, con el armazon del ejercicio 9.
        final RepositorioSimulado[] repo = new RepositorioSimulado[1];

        Ej09Junit.MiniTest bateria = new Ej09Junit.MiniTest()
            .antesDeCada(() -> repo[0] = new RepositorioSimulado())

            .prueba("inscribeCorrectamenteSiHayPlazasLibres", () -> {
                repo[0].devolver(new ClaseColectiva("YOGA-1", 20, 0));
                GestorInscripciones gestor = new GestorInscripciones(repo[0]);
                Inscripcion i = gestor.inscribir("11111111A", "YOGA-1");
                Ej09Junit.MiniTest.assertIgual(Inscripcion.Estado.PENDIENTE_PAGO, i.getEstado());
                Ej09Junit.MiniTest.assertCierto(repo[0].vecesGuardado() == 1,
                        "se esperaba 1 llamada a guardar y hubo " + repo[0].vecesGuardado());
            })

            .prueba("lanzaExcepcionSiLaClaseEstaCompleta", () -> {
                repo[0].devolver(new ClaseColectiva("PILATES-2", 15, 15));   // aforo agotado
                GestorInscripciones gestor = new GestorInscripciones(repo[0]);
                Ej09Junit.MiniTest.assertLanza(ClaseCompletaException.class,
                        () -> gestor.inscribir("11111111A", "PILATES-2"));
                // El verify(never()) de Mockito: lo importante no es solo que
                // lance la excepcion, sino que NO haya guardado nada. Sin esta
                // comprobacion, un gestor que guardase y luego fallase pasaria
                // la prueba dejando basura en la base de datos.
                Ej09Junit.MiniTest.assertCierto(repo[0].vecesGuardado() == 0,
                        "no deberia haber guardado nada y guardo " + repo[0].vecesGuardado());
            })

            .prueba("laUltimaPlazaSeAgotaYLaSiguienteInscripcionFalla", () -> {
                repo[0].devolver(new ClaseColectiva("SPINNING-3", 1, 0));    // una sola plaza
                GestorInscripciones gestor = new GestorInscripciones(repo[0]);
                gestor.inscribir("11111111A", "SPINNING-3");
                Ej09Junit.MiniTest.assertLanza(ClaseCompletaException.class,
                        () -> gestor.inscribir("22222222B", "SPINNING-3"));
                Ej09Junit.MiniTest.assertCierto(repo[0].vecesGuardado() == 1,
                        "solo debia guardarse la primera");
            });

        System.out.println("  Apartados 3 y 4:");
        Ej09Junit.Resultado resultado = bateria.ejecutar(true, "    ");
        System.out.println("    " + resultado.informe());

        // Apartado 5: la comparacion, medida. Se cronometra exactamente el mismo
        // trabajo con los dos repositorios, para que la comparacion sea justa:
        // 30 inscripciones identicas cambiando solo quien contesta.
        final int repeticiones = 30;

        RepositorioSimulado rapido = new RepositorioSimulado()
                .devolver(new ClaseColectiva("YOGA-1", 1000, 0));
        GestorInscripciones gestorRapido = new GestorInscripciones(rapido);
        long inicio = System.nanoTime();
        for (int i = 0; i < repeticiones; i++) {
            gestorRapido.inscribir("SOCIO-" + i, "YOGA-1");
        }
        long conSimuladoMicros = (System.nanoTime() - inicio) / 1_000;

        RepositorioLento lento = new RepositorioLento(new ClaseColectiva("YOGA-1", 1000, 0));
        GestorInscripciones gestorLento = new GestorInscripciones(lento);
        inicio = System.nanoTime();
        for (int i = 0; i < repeticiones; i++) {
            gestorLento.inscribir("SOCIO-" + i, "YOGA-1");
        }
        long conBd = (System.nanoTime() - inicio) / 1_000_000;

        System.out.println("  Apartado 5, cronometrado de verdad. El MISMO trabajo ("
                + repeticiones + " inscripciones)");
        System.out.println("    cambiando unicamente quien contesta al gestor:");
        System.out.println("      con el repositorio simulado: " + conSimuladoMicros
                + " microsegundos (" + (conSimuladoMicros / 1000.0) + " ms)");
        System.out.println("      con un repositorio que imita el coste de una base de datos ("
                + RepositorioLento.RETARDO_MS + " ms por acceso): " + conBd + " ms");
        long factor = (conSimuladoMicros > 0) ? (conBd * 1000) / conSimuladoMicros : 0;
        System.out.println("    El simulado es unas " + factor + " veces mas rapido en esta maquina.");
        System.out.println("    Y la diferencia no importa por treinta");
        System.out.println("    operaciones: importa porque una bateria real tiene cientos de");
        System.out.println("    pruebas. A este ritmo pasan de decimas de segundo a varios minutos,");
        System.out.println("    y una bateria que tarda minutos deja de ejecutarse en cada cambio,");
        System.out.println("    que es exactamente para lo que sirve.");

        System.out.println("  Apartado 6: la prueba unitaria verifica la LOGICA del gestor y si");
        System.out.println("    falla, el fallo esta ahi porque no hay nada mas en juego. Una prueba");
        System.out.println("    de integracion verificaria que el DAO real escribe de verdad en la");
        System.out.println("    base de datos real. Ninguna sustituye a la otra: la unitaria dice si");
        System.out.println("    la logica es correcta, la de integracion si las piezas encajan.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
