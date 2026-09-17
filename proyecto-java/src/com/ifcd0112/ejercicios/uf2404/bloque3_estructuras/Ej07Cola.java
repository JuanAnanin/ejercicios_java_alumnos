package com.ifcd0112.ejercicios.uf2404.bloque3_estructuras;

/**
 * UF2404 - BLOQUE 3 - EJERCICIO 7: Cola de atencion al cliente.
 *
 * <p>Criterios de evaluacion: CE2.7, CE2.9</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej07Cola {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Un centro de atencion telefonica necesita gestionar la cola de llamadas
     * en espera. Implementa una cola (estructura FIFO) mediante nodos
     * enlazados, manteniendo referencias tanto al primer nodo como al ultimo.
     *
     * Se pide:
     *   1. Crea una clase Llamada con el numero de telefono, el motivo y la
     *      hora de entrada.
     *   2. Implementa la clase ColaLlamadas con encolar(Llamada l),
     *      desencolar(), consultarPrimera() y estaVacia().
     *   3. Ambos metodos de extraccion deben gestionar adecuadamente el caso de
     *      cola vacia.
     *   4. Anade un metodo atenderTodas() que vaya desencolando y mostrando
     *      cada llamada hasta vaciar la cola.
     *   5. Justifica en un comentario por que mantener una referencia al ultimo
     *      nodo evita tener que recorrer la cola completa en cada insercion.
     *
     * Pista: presta especial atencion al caso de encolar en una cola vacia y al
     * de desencolar el ultimo elemento: en ambos, las referencias a frente y
     * final deben quedar coherentes.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /** Excepcion propia para las operaciones de extraccion sobre una cola vacia. */
    public static class ColaVaciaException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public ColaVaciaException(String mensaje) {
            super(mensaje);
        }
    }

    /** Apartado 1: una llamada en espera. */
    public static class Llamada {

        private final String telefono;
        private final String motivo;
        private final String horaEntrada;

        /**
         * @param telefono    numero desde el que se llama
         * @param motivo      asunto declarado por quien llama
         * @param horaEntrada hora de entrada en formato HH:mm
         */
        public Llamada(String telefono, String motivo, String horaEntrada) {
            this.telefono = telefono;
            this.motivo = motivo;
            this.horaEntrada = horaEntrada;
        }

        public String getTelefono()    { return telefono; }
        public String getMotivo()      { return motivo; }
        public String getHoraEntrada() { return horaEntrada; }

        @Override
        public String toString() {
            return horaEntrada + "  " + telefono + "  (" + motivo + ")";
        }
    }

    /**
     * Cola FIFO de llamadas construida con nodos enlazados.
     *
     * <p>APARTADO 5, la justificacion de la doble referencia. En una cola se
     * inserta por un extremo y se extrae por el otro. Con una lista enlazada
     * simple solo se guarda normalmente el primer nodo, y para insertar al
     * final habria que recorrerla entera cada vez: con mil llamadas en espera,
     * mil saltos por cada insercion, es decir una operacion O(n).</p>
     *
     * <p>Manteniendo ademas una referencia al ultimo nodo, insertar consiste en
     * enganchar el nuevo nodo detras del que ya senala 'fin' y mover 'fin'. Dos
     * asignaciones, sin recorrer nada: la operacion pasa a ser O(1) y el coste
     * deja de depender de cuantas llamadas haya en espera. El precio es tener
     * que mantener coherente una referencia mas, que es exactamente lo que
     * complica los dos casos limite de la pista.</p>
     */
    public static class ColaLlamadas {

        /** Nodo de la lista enlazada. Detalle interno de la cola. */
        private static class Nodo {
            private final Llamada llamada;
            private Nodo siguiente;

            Nodo(Llamada llamada) {
                this.llamada = llamada;
            }
        }

        /** Por donde se EXTRAE (el primero que entro). */
        private Nodo frente;

        /** Por donde se INSERTA (el ultimo que entro). Ver justificacion arriba. */
        private Nodo fin;

        private int tamanio;

        /**
         * Apartado 2: anade una llamada al final de la cola. O(1).
         *
         * @param l llamada que entra en espera
         */
        public void encolar(Llamada l) {
            Nodo nuevo = new Nodo(l);
            if (estaVacia()) {
                // CASO LIMITE 1 (pista): en una cola vacia, el nodo nuevo es a
                // la vez el primero y el ultimo. Si aqui solo actualizaramos
                // 'fin', 'frente' seguiria a null y la cola pareceria vacia
                // para siempre.
                frente = nuevo;
                fin = nuevo;
            } else {
                fin.siguiente = nuevo;   // se engancha detras del ultimo
                fin = nuevo;             // y pasa a ser el nuevo ultimo
            }
            tamanio++;
        }

        /**
         * Apartado 2 y 3: extrae la primera llamada de la cola.
         *
         * @return la llamada que llevaba mas tiempo esperando
         * @throws ColaVaciaException si no hay llamadas en espera
         */
        public Llamada desencolar() {
            if (estaVacia()) {
                throw new ColaVaciaException("No hay llamadas en espera");
            }
            Llamada l = frente.llamada;
            frente = frente.siguiente;
            if (frente == null) {
                // CASO LIMITE 2 (pista): acabamos de sacar el ultimo elemento.
                // Si no ponemos 'fin' a null, quedaria apuntando a un nodo que
                // ya no forma parte de la cola, y el siguiente encolar()
                // engancharia detras de un nodo fantasma.
                fin = null;
            }
            tamanio--;
            return l;
        }

        /**
         * Apartado 2 y 3: consulta la primera llamada SIN extraerla.
         *
         * @return la primera de la cola
         * @throws ColaVaciaException si no hay llamadas en espera
         */
        public Llamada consultarPrimera() {
            if (estaVacia()) {
                throw new ColaVaciaException("No hay llamadas en espera");
            }
            return frente.llamada;
        }

        /** @return true si no queda ninguna llamada en espera */
        public boolean estaVacia() {
            return frente == null;
        }

        /** @return numero de llamadas en espera */
        public int tamanio() {
            return tamanio;
        }

        /**
         * Apartado 4: atiende todas las llamadas por orden de llegada hasta
         * vaciar la cola.
         */
        public void atenderTodas() {
            int orden = 1;
            while (!estaVacia()) {
                Llamada l = desencolar();
                System.out.println("    " + orden + ". Atendiendo -> " + l);
                orden++;
            }
            System.out.println("    No quedan llamadas en espera.");
        }
    }

    // =====================================================================
    // COMPROBACION
    // =====================================================================

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2404-E07] Cola de atencion al cliente");

        ColaLlamadas cola = new ColaLlamadas();
        System.out.println("  Cola recien creada: estaVacia=" + cola.estaVacia());

        cola.encolar(new Llamada("600111222", "Alta de servicio", "09:02"));
        cola.encolar(new Llamada("600333444", "Incidencia en la factura", "09:05"));
        cola.encolar(new Llamada("600555666", "Baja de linea", "09:11"));
        System.out.println("  Tras encolar 3 llamadas: tamanio=" + cola.tamanio());
        System.out.println("  consultarPrimera() -> " + cola.consultarPrimera());
        System.out.println("  Sigue habiendo " + cola.tamanio() + " llamadas: consultar no extrae");

        System.out.println("  desencolar() -> " + cola.desencolar()
                + "   (FIFO: sale la que llevaba mas tiempo)");
        System.out.println("  Ahora la primera es -> " + cola.consultarPrimera());

        // Caso limite 2: vaciar la cola por completo y volver a llenarla. Si
        // 'fin' no se hubiera puesto a null, esto fallaria.
        cola.desencolar();
        cola.desencolar();
        System.out.println("  Cola vaciada: estaVacia=" + cola.estaVacia());
        cola.encolar(new Llamada("600777888", "Consulta de tarifa", "09:20"));
        System.out.println("  Se vuelve a encolar tras vaciarla -> " + cola.consultarPrimera());
        cola.desencolar();

        // Apartado 3: la excepcion sobre una cola vacia.
        try {
            cola.desencolar();
        } catch (ColaVaciaException e) {
            System.out.println("  desencolar() sobre cola vacia -> ColaVaciaException: " + e.getMessage());
        }

        // Apartado 4.
        System.out.println("  atenderTodas() con cuatro llamadas nuevas:");
        cola.encolar(new Llamada("611000111", "Cambio de titular", "10:00"));
        cola.encolar(new Llamada("611000222", "Averia", "10:03"));
        cola.encolar(new Llamada("611000333", "Informacion", "10:07"));
        cola.encolar(new Llamada("611000444", "Reclamacion", "10:12"));
        cola.atenderTodas();
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
