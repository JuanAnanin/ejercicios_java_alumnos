package com.ifcd0112.ejercicios.uf2404.bloque3_estructuras;

/**
 * UF2404 - BLOQUE 3 - EJERCICIO 6: Pila enlazada con excepciones propias.
 *
 * <p>Criterios de evaluacion: CE2.7, CE2.9</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej06Pila {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Implementa desde cero una pila de enteros (estructura LIFO) utilizando
     * nodos enlazados, sin emplear ninguna clase de colecciones de la
     * biblioteca estandar de Java.
     *
     * Se pide:
     *   1. Crea una clase Nodo con el valor entero y la referencia al siguiente
     *      nodo.
     *   2. Crea la clase Pila con los metodos apilar(int valor), desapilar(),
     *      cima(), estaVacia() y tamanio().
     *   3. Los metodos desapilar() y cima() deben lanzar una excepcion propia
     *      PilaVaciaException cuando se invoquen sobre una pila sin elementos.
     *   4. Implementa el metodo tamanio() sin recorrer la estructura: manten un
     *      contador como atributo.
     *   5. Escribe un programa que use tu pila para comprobar si una expresion
     *      con parentesis, corchetes y llaves esta correctamente balanceada
     *      (por ejemplo: "{[()]}" esta balanceada, "{[(])}" no lo esta).
     *
     * Salida esperada (orientativa):
     *      {[()]}   -> balanceada
     *      {[(])}   -> NO balanceada
     *      ((()))   -> balanceada
     *      (()      -> NO balanceada
     *
     * Pista: para el comprobador de parentesis, apila cada simbolo de apertura
     * y, al encontrar uno de cierre, desapila y comprueba que se corresponden.
     * Al terminar, la pila debe quedar vacia.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /**
     * Apartado 3: excepcion propia de la estructura.
     *
     * <p>Extiende de RuntimeException y no de Exception a proposito: invocar
     * desapilar() sobre una pila vacia es un error de programacion, no una
     * situacion de negocio. Quien usa la pila tiene estaVacia() para
     * comprobarlo antes, asi que obligarle a un try/catch en cada llamada
     * ensuciaria el codigo sin aportar nada. Es la distincion checked/unchecked
     * que se desarrolla en el bloque 7 de la UF2406.</p>
     */
    public static class PilaVaciaException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public PilaVaciaException(String mensaje) {
            super(mensaje);
        }
    }

    /**
     * Pila de enteros LIFO construida con nodos enlazados.
     *
     * <p>No se usa ArrayList, LinkedList ni Deque: el enunciado pide construir
     * la estructura desde cero para entender como funciona por dentro.</p>
     */
    public static class Pila {

        /**
         * Apartado 1: nodo de la lista enlazada.
         *
         * <p>Es una clase interna privada porque es un detalle de
         * implementacion: nadie fuera de Pila debe saber que hay nodos. Si
         * manana se cambiara la implementacion por un array, el codigo cliente
         * no se enteraria.</p>
         */
        private static class Nodo {
            private final int valor;
            private final Nodo siguiente;

            Nodo(int valor, Nodo siguiente) {
                this.valor = valor;
                this.siguiente = siguiente;
            }
        }

        /** Referencia al nodo que esta arriba del todo. null si la pila esta vacia. */
        private Nodo cima;

        /** Apartado 4: contador propio, para que tamanio() sea O(1) y no O(n). */
        private int tamanio;

        /**
         * Apartado 2: introduce un valor en la cima.
         *
         * <p>La operacion es O(1): el nodo nuevo apunta al antiguo tope y pasa
         * a ser la cima. No hay que recorrer nada.</p>
         *
         * @param valor entero a apilar
         */
        public void apilar(int valor) {
            cima = new Nodo(valor, cima);
            tamanio++;
        }

        /**
         * Apartado 2 y 3: extrae y devuelve el valor de la cima.
         *
         * @return el valor que estaba arriba del todo
         * @throws PilaVaciaException si la pila no tiene elementos
         */
        public int desapilar() {
            if (estaVacia()) {
                throw new PilaVaciaException("No se puede desapilar: la pila esta vacia");
            }
            int valor = cima.valor;
            cima = cima.siguiente;   // el segundo nodo pasa a ser la cima
            tamanio--;
            return valor;
        }

        /**
         * Apartado 2 y 3: consulta el valor de la cima SIN extraerlo.
         *
         * @return el valor que esta arriba del todo
         * @throws PilaVaciaException si la pila no tiene elementos
         */
        public int cima() {
            if (estaVacia()) {
                throw new PilaVaciaException("No se puede consultar la cima: la pila esta vacia");
            }
            return cima.valor;
        }

        /** @return true si no queda ningun elemento */
        public boolean estaVacia() {
            return cima == null;
        }

        /**
         * Apartado 4: numero de elementos, en tiempo constante.
         *
         * @return cuantos elementos contiene la pila
         */
        public int tamanio() {
            return tamanio;
        }
    }

    // =====================================================================
    // APARTADO 5: comprobador de expresiones balanceadas
    // =====================================================================

    /**
     * Comprueba si una expresion tiene sus parentesis, corchetes y llaves
     * correctamente balanceados.
     *
     * <p>El algoritmo apila el codigo del caracter de apertura y, al llegar un
     * cierre, desapila y comprueba que se corresponden. Se apilan enteros
     * porque la pila del enunciado es de enteros: se guarda el caracter
     * convertido a int y se recupera con un cast.</p>
     *
     * @param expresion texto a comprobar
     * @return true si esta balanceada
     */
    public static boolean estaBalanceada(String expresion) {
        Pila pila = new Pila();
        for (char c : expresion.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                pila.apilar(c);
            } else if (c == ')' || c == ']' || c == '}') {
                // Un cierre sin ninguna apertura pendiente ya invalida la
                // expresion. Se comprueba ANTES de desapilar para no provocar
                // la PilaVaciaException: aqui la situacion es previsible.
                if (pila.estaVacia()) {
                    return false;
                }
                char apertura = (char) pila.desapilar();
                if (!seCorresponden(apertura, c)) {
                    return false;
                }
            }
            // Cualquier otro caracter se ignora: solo interesan los delimitadores.
        }
        // Si queda algo en la pila hay aperturas sin cerrar.
        return pila.estaVacia();
    }

    /** @return true si el par apertura-cierre es del mismo tipo */
    private static boolean seCorresponden(char apertura, char cierre) {
        return (apertura == '(' && cierre == ')')
            || (apertura == '[' && cierre == ']')
            || (apertura == '{' && cierre == '}');
    }

    // =====================================================================
    // COMPROBACION
    // =====================================================================

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2404-E06] Pila enlazada con excepciones propias");

        // Operaciones basicas de la pila.
        Pila pila = new Pila();
        System.out.println("  Pila recien creada: estaVacia=" + pila.estaVacia()
                + ", tamanio=" + pila.tamanio());
        pila.apilar(10);
        pila.apilar(20);
        pila.apilar(30);
        System.out.println("  Tras apilar 10, 20 y 30: tamanio=" + pila.tamanio()
                + ", cima=" + pila.cima());
        System.out.println("  desapilar() -> " + pila.desapilar()
                + " (LIFO: sale el ultimo que entro)");
        System.out.println("  Ahora la cima es " + pila.cima() + " y quedan " + pila.tamanio());

        // Apartado 3: la excepcion propia sobre una pila vacia.
        Pila vacia = new Pila();
        try {
            vacia.desapilar();
        } catch (PilaVaciaException e) {
            System.out.println("  desapilar() sobre pila vacia -> PilaVaciaException: " + e.getMessage());
        }
        try {
            vacia.cima();
        } catch (PilaVaciaException e) {
            System.out.println("  cima() sobre pila vacia -> PilaVaciaException: " + e.getMessage());
        }

        // Apartado 5: el comprobador de expresiones.
        System.out.println("  Comprobador de expresiones balanceadas:");
        String[] casos = { "{[()]}", "{[(])}", "((()))", "(()", "", "a(b[c]{d})e", ")(" };
        for (String caso : casos) {
            String etiqueta = caso.isEmpty() ? "(cadena vacia)" : caso;
            System.out.printf("    %-14s -> %s%n",
                    etiqueta, estaBalanceada(caso) ? "balanceada" : "NO balanceada");
        }
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
