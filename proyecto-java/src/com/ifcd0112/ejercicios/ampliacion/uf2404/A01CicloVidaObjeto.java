package com.ifcd0112.ejercicios.ampliacion.uf2404;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * AMPLIACION UF2404 - EJERCICIO A1: Ciclo de vida del objeto y gestion de memoria.
 *
 * <p>Criterios de evaluacion: CE2.2, CE1.2</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class A01CicloVidaObjeto {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * En Java nadie destruye los objetos a mano: de eso se encarga el
     * recolector de basura. Este ejercicio pretende que compruebes cuando deja
     * de existir realmente un objeto, y por que un constructor tiene pareja
     * pero un destructor no.
     *
     * Se pide:
     *   1. Escribe una clase Documento con un contador estatico de instancias
     *      creadas, de modo que en cualquier momento se pueda saber cuantos
     *      objetos se han construido.
     *   2. Crea tres documentos dentro de un metodo y comprueba el contador al
     *      entrar y al salir del metodo. Explica por que el contador NO baja.
     *   3. Guarda una referencia debil (WeakReference) a uno de los documentos,
     *      elimina despues todas las referencias normales que apunten a el,
     *      solicita la recoleccion y comprueba si la referencia debil se ha
     *      quedado vacia. Explica que demuestra ese resultado.
     *   4. Repite la prueba anterior pero guardando ademas el documento en una
     *      lista estatica de la clase. Explica por que ahora no se recolecta y
     *      que nombre recibe ese problema.
     *   5. Implementa una clase Conexion que represente un recurso externo, de
     *      forma que se libere de manera DETERMINISTA. Explica en un comentario
     *      por que finalize() no sirve para eso y que se usa en su lugar.
     *   6. Explica en un comentario la diferencia entre la memoria de la pila y
     *      la del monticulo, e indica donde vive el objeto y donde la variable
     *      que lo referencia.
     *
     * Pista: solicitar la recoleccion no es lo mismo que provocarla.
     * System.gc() es una sugerencia que la maquina virtual puede ignorar, asi
     * que la prueba del apartado 3 hay que plantearla admitiendo que a veces no
     * concluya, y decirlo, en lugar de dar por hecho que el objeto ha muerto.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * APARTADO 6: donde vive cada cosa.
     *
     *   LA PILA (stack). Una por hilo. Guarda las variables locales y los
     *   parametros de cada llamada a metodo. Se libera sola al volver del
     *   metodo, en orden inverso, y por eso es rapidisima y no necesita
     *   recolector. Lo que guarda de un objeto NO es el objeto: es la
     *   referencia, es decir, la direccion donde esta.
     *
     *   EL MONTICULO (heap). Uno para toda la maquina virtual, compartido por
     *   todos los hilos. Ahi vive el objeto de verdad, el que creo el new.
     *   No se libera al salir del metodo: se libera cuando ya nadie lo
     *   referencia, y de eso se encarga el recolector.
     *
     *   Documento d = new Documento("acta");
     *   -------- ---   ------------------------
     *      |      |              |
     *      |      |              +-- el OBJETO, en el monticulo
     *      |      +----------------- la VARIABLE d, en la pila
     *      +------------------------ su tipo, que decide que se le puede pedir
     *
     * Esta distincion explica de golpe tres cosas que suelen costar: por que
     * pasar un objeto a un metodo permite modificarlo (se copia la referencia,
     * no el objeto), por que dos variables pueden apuntar al mismo objeto, y
     * por que un objeto sobrevive al metodo que lo creo si alguien se quedo su
     * referencia.
     *
     * APARTADO 5: por que finalize() no es un destructor.
     *
     * En C++ el destructor se ejecuta en un momento conocido: al salir del
     * ambito. En Java, finalize() se ejecutaba justo antes de recolectar el
     * objeto, y eso significa:
     *
     *   - No se sabe CUANDO. Puede tardar minutos.
     *   - No se sabe SI. Si el programa termina antes, no se ejecuta nunca.
     *   - Si lanza una excepcion, se ignora en silencio.
     *   - Puede resucitar el objeto guardando this en algun sitio.
     *
     * Un fichero abierto o una conexion a base de datos no pueden esperar a
     * "cuando toque": son recursos escasos y hay que devolverlos ya. Por eso
     * finalize() esta obsoleto desde Java 9 y en desuso desde Java 18.
     *
     * Lo que se usa en su lugar es AutoCloseable con try-with-resources: el
     * cierre ocurre al salir del bloque, siempre, incluso si salta una
     * excepcion, y en un momento que se puede senalar con el dedo en el codigo.
     * Es la diferencia entre "se liberara" y "se libera aqui".
     */

    // ---------------------------------------------------------------------
    // Apartado 1: contador de instancias
    // ---------------------------------------------------------------------

    /** Documento con contador estatico de instancias creadas. */
    public static class Documento {

        /**
         * Contador COMPARTIDO por todas las instancias, porque es static: no
         * pertenece a ningun documento concreto, sino a la clase.
         */
        private static int creados = 0;

        private final String titulo;
        private final byte[] contenido;

        /**
         * @param titulo   nombre del documento
         * @param tamanoKb tamano simulado, para que el objeto ocupe de verdad
         */
        public Documento(String titulo, int tamanoKb) {
            this.titulo = titulo;
            // Se reserva memoria de verdad para que el ejemplo no sea teorico:
            // un objeto vacio podria caber en cualquier hueco y no se notaria.
            this.contenido = new byte[tamanoKb * 1024];
            creados++;
        }

        /** @return titulo del documento */
        public String getTitulo() { return titulo; }

        /** @return tamano en KB */
        public int getTamanoKb() { return contenido.length / 1024; }

        /** @return numero de documentos construidos desde que arranco el programa */
        public static int getCreados() { return creados; }

        @Override
        public String toString() {
            return "Documento[" + titulo + ", " + getTamanoKb() + " KB]";
        }
    }

    /**
     * Apartado 4: la lista que provoca la fuga.
     *
     * <p>Un campo estatico vive tanto como la clase, es decir, practicamente
     * todo el programa. Cualquier objeto metido aqui deja de ser recolectable
     * aunque nadie mas lo use.</p>
     */
    private static final List<Documento> CACHE_QUE_NO_SE_VACIA = new ArrayList<>();

    // ---------------------------------------------------------------------
    // Apartado 5: liberacion determinista
    // ---------------------------------------------------------------------

    /**
     * Recurso externo que se libera de forma determinista.
     *
     * <p>Implementar AutoCloseable es lo que permite usarlo en un
     * try-with-resources. No hay ningun destructor: hay un metodo close() que
     * el lenguaje se compromete a llamar al salir del bloque.</p>
     */
    public static class Conexion implements AutoCloseable {

        /** Conexiones abiertas en este momento. */
        private static int abiertas = 0;

        private final String nombre;
        private boolean cerrada;

        /**
         * @param nombre identificador de la conexion
         */
        public Conexion(String nombre) {
            this.nombre = nombre;
            abiertas++;
        }

        /** @return conexiones abiertas ahora mismo */
        public static int getAbiertas() { return abiertas; }

        /**
         * Usa la conexion.
         *
         * @throws IllegalStateException si ya se cerro
         */
        public void usar() {
            if (cerrada) {
                throw new IllegalStateException("La conexion " + nombre + " ya esta cerrada");
            }
        }

        @Override
        public void close() {
            if (!cerrada) {
                cerrada = true;
                abiertas--;
            }
        }
    }

    // =====================================================================
    // COMPROBACION
    // =====================================================================

    /**
     * Apartado 2: crea tres documentos dentro de un metodo.
     *
     * @return referencia debil al primero de ellos, para el apartado 3
     */
    private static WeakReference<Documento> crearTresDocumentos() {
        Documento acta = new Documento("acta", 256);
        Documento informe = new Documento("informe", 256);
        Documento memoria = new Documento("memoria", 256);
        System.out.println("    Dentro del metodo: " + acta + ", " + informe + ", " + memoria);
        // Al volver, las tres variables locales desaparecen de la pila. Los tres
        // objetos quedan sin nadie que los referencie, salvo el que devolvemos
        // envuelto en una referencia debil, que a proposito NO cuenta.
        return new WeakReference<>(acta);
    }

    /**
     * Solicita la recoleccion y espera a ver si la referencia debil se vacia.
     *
     * @param debil referencia a vigilar
     * @return true si el objeto fue recolectado
     */
    private static boolean intentarRecolectar(WeakReference<Documento> debil) {
        // Se intenta varias veces porque System.gc() es una SUGERENCIA: la
        // maquina virtual puede ignorarla, y la recoleccion ocurre en otro hilo.
        for (int intento = 0; intento < 5 && debil.get() != null; intento++) {
            System.gc();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return debil.get() == null;
            }
        }
        return debil.get() == null;
    }

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[AMP-UF2404-A1] Ciclo de vida del objeto y gestion de memoria");

        // Apartado 2
        System.out.println("  Apartado 2, el contador de instancias:");
        System.out.println("    Antes de llamar al metodo: " + Documento.getCreados() + " creados");
        WeakReference<Documento> debil = crearTresDocumentos();
        System.out.println("    Despues de volver:         " + Documento.getCreados() + " creados");
        System.out.println("    El contador NO baja, y hace bien: cuenta cuantos se han");
        System.out.println("    CONSTRUIDO, no cuantos siguen vivos. Nadie decrementa nada al");
        System.out.println("    morir un objeto porque no hay ningun sitio donde escribir eso.");

        // Apartado 3
        System.out.println("  Apartado 3, el objeto sin referencias:");
        System.out.println("    Antes de recolectar, la referencia debil apunta a: " + debil.get());
        boolean recolectado = intentarRecolectar(debil);
        if (recolectado) {
            System.out.println("    Despues de System.gc(), la referencia debil vale: null");
            System.out.println("    Demuestra que el objeto ha sido recolectado: nadie lo");
            System.out.println("    referenciaba con fuerza, asi que la maquina virtual pudo");
            System.out.println("    reclamar su memoria. Ese es el momento en que un objeto Java");
            System.out.println("    deja de existir de verdad.");
        } else {
            // Honestidad: si no se recolecta, no se puede concluir nada, y eso
            // tambien hay que decirlo.
            System.out.println("    La referencia debil sigue apuntando a " + debil.get());
            System.out.println("    Esto NO significa que el objeto sea inmortal: significa que la");
            System.out.println("    maquina virtual ha decidido no recolectar todavia. System.gc()");
            System.out.println("    es una sugerencia, no una orden. Vuelve a ejecutarlo.");
        }

        // Apartado 4
        System.out.println("  Apartado 4, el mismo caso con una cache estatica:");
        Documento retenido = new Documento("contrato", 256);
        CACHE_QUE_NO_SE_VACIA.add(retenido);
        WeakReference<Documento> debilRetenido = new WeakReference<>(retenido);
        retenido = null;   // se elimina la unica referencia "normal"
        boolean recolectado2 = intentarRecolectar(debilRetenido);
        System.out.println("    Referencia local eliminada, pero sigue en la lista estatica.");
        System.out.println("    Recolectado: " + (recolectado2 ? "SI" : "NO") + "  -> "
                + (recolectado2 ? "inesperado" : "correcto, la lista lo retiene"));
        System.out.println("    Esto es una FUGA DE MEMORIA. En Java no se fuga memoria por");
        System.out.println("    olvidar liberar, como en C: se fuga por olvidar SOLTAR. Una cache");
        System.out.println("    que nunca se vacia, un listener que nunca se desregistra o una");
        System.out.println("    coleccion estatica que solo crece son las tres causas habituales.");
        System.out.println("    Documentos en la cache: " + CACHE_QUE_NO_SE_VACIA.size()
                + ", memoria retenida: " + (CACHE_QUE_NO_SE_VACIA.size() * 256) + " KB");

        // Apartado 5
        System.out.println("  Apartado 5, liberacion determinista con try-with-resources:");
        System.out.println("    Conexiones abiertas antes: " + Conexion.getAbiertas());
        try (Conexion c1 = new Conexion("bd-principal");
             Conexion c2 = new Conexion("bd-informes")) {
            c1.usar();
            c2.usar();
            System.out.println("    Dentro del bloque: " + Conexion.getAbiertas() + " abiertas");
        }
        System.out.println("    Al salir del bloque:  " + Conexion.getAbiertas() + " abiertas");

        // Y ahora lo mismo cuando salta una excepcion a mitad.
        try (Conexion c3 = new Conexion("bd-temporal")) {
            c3.usar();
            System.out.println("    Dentro del bloque con fallo: " + Conexion.getAbiertas() + " abiertas");
            throw new IllegalStateException("fallo simulado a mitad de la operacion");
        } catch (IllegalStateException e) {
            System.out.println("    Excepcion capturada: " + e.getMessage());
        }
        System.out.println("    Conexiones abiertas tras la excepcion: " + Conexion.getAbiertas());
        System.out.println("    Se cerro igualmente. Esa garantia es lo que finalize() nunca dio:");
        System.out.println("    con el recolector no se sabe cuando ni si; aqui se sabe donde.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
