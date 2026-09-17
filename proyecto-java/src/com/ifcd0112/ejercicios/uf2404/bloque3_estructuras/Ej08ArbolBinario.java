package com.ifcd0112.ejercicios.uf2404.bloque3_estructuras;

/**
 * UF2404 - BLOQUE 3 - EJERCICIO 8: Arbol binario de busqueda.
 *
 * <p>Criterios de evaluacion: CE2.7, CE2.9</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej08ArbolBinario {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Implementa un arbol binario de busqueda de enteros que mantenga la
     * propiedad de orden: para cualquier nodo, todos los valores de su subarbol
     * izquierdo son menores que el y todos los del derecho, mayores.
     *
     * Se pide:
     *   1. Implementa el metodo insertar(int valor) de forma recursiva,
     *      ignorando los valores duplicados.
     *   2. Implementa el metodo buscar(int valor), que devuelva true o false,
     *      aprovechando la propiedad de orden para no recorrer todo el arbol.
     *   3. Implementa los tres recorridos clasicos: inOrden(), preOrden() y
     *      postOrden().
     *   4. Implementa un metodo altura() que devuelva la altura del arbol.
     *   5. Inserta la secuencia 50, 30, 70, 20, 40, 60, 80 y comprueba que el
     *      recorrido inOrden devuelve los valores ordenados de menor a mayor.
     *
     * Salida esperada (orientativa):
     *      InOrden:   20 30 40 50 60 70 80
     *      PreOrden:  50 30 20 40 70 60 80
     *      PostOrden: 20 40 30 60 80 70 50
     *      Altura del arbol: 3
     *      Buscar 40: encontrado    Buscar 55: no encontrado
     *
     * Pista: el recorrido inOrden visita primero el subarbol izquierdo, despues
     * el nodo actual y por ultimo el subarbol derecho. Es precisamente esa
     * secuencia la que produce la salida ordenada.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /** Arbol binario de busqueda de enteros, sin duplicados. */
    public static class ArbolBinarioBusqueda {

        /** Nodo del arbol. Detalle interno de la estructura. */
        private static class Nodo {
            private final int valor;
            private Nodo izq;
            private Nodo der;

            Nodo(int valor) {
                this.valor = valor;
            }
        }

        private Nodo raiz;
        private int numeroNodos;

        /**
         * Apartado 1: inserta un valor manteniendo la propiedad de orden.
         *
         * @param valor entero a insertar; si ya existe, se ignora
         */
        public void insertar(int valor) {
            int antes = numeroNodos;
            raiz = insertarRec(raiz, valor);
            if (numeroNodos == antes) {
                // No ha crecido: era un duplicado. Se ignora en silencio, tal
                // y como pide el enunciado.
                return;
            }
        }

        /**
         * Version recursiva de la insercion.
         *
         * <p>El truco del patron es que el metodo DEVUELVE el subarbol ya
         * modificado y quien llama lo reasigna. Asi no hace falta llevar una
         * referencia al padre: la reasignacion la hace la propia recursion al
         * volver.</p>
         *
         * @param nodo  raiz del subarbol donde insertar
         * @param valor entero a insertar
         * @return la raiz del subarbol despues de la insercion
         */
        private Nodo insertarRec(Nodo nodo, int valor) {
            if (nodo == null) {
                numeroNodos++;
                return new Nodo(valor);      // hueco encontrado: aqui va
            }
            if (valor < nodo.valor) {
                nodo.izq = insertarRec(nodo.izq, valor);
            } else if (valor > nodo.valor) {
                nodo.der = insertarRec(nodo.der, valor);
            }
            // Si es igual no se hace nada: duplicado ignorado (apartado 1).
            return nodo;
        }

        /**
         * Apartado 2: busca un valor aprovechando la propiedad de orden.
         *
         * <p>En cada nodo se descarta la mitad del subarbol restante, asi que
         * el coste es proporcional a la ALTURA y no al numero de nodos. En un
         * arbol equilibrado eso es O(log n) frente al O(n) de recorrerlo entero.</p>
         *
         * @param valor entero a localizar
         * @return true si el valor esta en el arbol
         */
        public boolean buscar(int valor) {
            Nodo actual = raiz;
            while (actual != null) {
                if (valor == actual.valor) {
                    return true;
                }
                // Aqui esta la clave: se elige UNA rama y la otra ni se mira.
                actual = (valor < actual.valor) ? actual.izq : actual.der;
            }
            return false;
        }

        /**
         * Apartado 3: recorrido en orden (izquierda, nodo, derecha).
         *
         * @return los valores separados por espacios, de menor a mayor
         */
        public String inOrden() {
            StringBuilder sb = new StringBuilder();
            inOrdenRec(raiz, sb);
            return sb.toString().trim();
        }

        private void inOrdenRec(Nodo nodo, StringBuilder sb) {
            if (nodo == null) {
                return;
            }
            inOrdenRec(nodo.izq, sb);          // 1. todo lo menor
            sb.append(nodo.valor).append(' '); // 2. el nodo actual
            inOrdenRec(nodo.der, sb);          // 3. todo lo mayor
        }

        /**
         * Apartado 3: recorrido en preorden (nodo, izquierda, derecha).
         *
         * <p>Es el recorrido que reproduce el orden de insercion de un arbol
         * construido desde cero: si se insertan los valores en este orden, sale
         * exactamente el mismo arbol.</p>
         *
         * @return los valores separados por espacios
         */
        public String preOrden() {
            StringBuilder sb = new StringBuilder();
            preOrdenRec(raiz, sb);
            return sb.toString().trim();
        }

        private void preOrdenRec(Nodo nodo, StringBuilder sb) {
            if (nodo == null) {
                return;
            }
            sb.append(nodo.valor).append(' ');
            preOrdenRec(nodo.izq, sb);
            preOrdenRec(nodo.der, sb);
        }

        /**
         * Apartado 3: recorrido en postorden (izquierda, derecha, nodo).
         *
         * <p>Es el que se usa para liberar o destruir un arbol: cuando se
         * procesa un nodo, sus dos hijos ya se han procesado.</p>
         *
         * @return los valores separados por espacios
         */
        public String postOrden() {
            StringBuilder sb = new StringBuilder();
            postOrdenRec(raiz, sb);
            return sb.toString().trim();
        }

        private void postOrdenRec(Nodo nodo, StringBuilder sb) {
            if (nodo == null) {
                return;
            }
            postOrdenRec(nodo.izq, sb);
            postOrdenRec(nodo.der, sb);
            sb.append(nodo.valor).append(' ');
        }

        /**
         * Apartado 4: altura del arbol, contada en numero de niveles.
         *
         * <p>Con este criterio, un arbol vacio tiene altura 0 y un arbol de un
         * solo nodo tiene altura 1. Conviene fijarlo porque hay bibliografia
         * que cuenta aristas en lugar de niveles y da un valor menos.</p>
         *
         * @return numero de niveles del arbol
         */
        public int altura() {
            return alturaRec(raiz);
        }

        private int alturaRec(Nodo nodo) {
            if (nodo == null) {
                return 0;
            }
            return 1 + Math.max(alturaRec(nodo.izq), alturaRec(nodo.der));
        }

        /** @return numero de valores distintos almacenados */
        public int numeroNodos() {
            return numeroNodos;
        }
    }

    // =====================================================================
    // COMPROBACION (apartado 5)
    // =====================================================================

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2404-E08] Arbol binario de busqueda");

        ArbolBinarioBusqueda arbol = new ArbolBinarioBusqueda();
        int[] secuencia = { 50, 30, 70, 20, 40, 60, 80 };
        for (int valor : secuencia) {
            arbol.insertar(valor);
        }
        System.out.println("  Secuencia insertada: 50 30 70 20 40 60 80");

        // Apartado 5: la comprobacion que da sentido al ejercicio.
        System.out.println("  InOrden:   " + arbol.inOrden());
        System.out.println("  PreOrden:  " + arbol.preOrden());
        System.out.println("  PostOrden: " + arbol.postOrden());
        System.out.println("  Altura del arbol: " + arbol.altura());
        System.out.println("  Buscar 40: " + (arbol.buscar(40) ? "encontrado" : "no encontrado")
                + "    Buscar 55: " + (arbol.buscar(55) ? "encontrado" : "no encontrado"));

        // Apartado 1: los duplicados se ignoran.
        System.out.println("  Nodos antes de reinsertar el 40: " + arbol.numeroNodos());
        arbol.insertar(40);
        System.out.println("  Nodos despues de reinsertar el 40: " + arbol.numeroNodos()
                + " (el duplicado se ignora)");

        // El arbol degenerado: si los valores llegan ya ordenados, cada nodo
        // cuelga del anterior y el arbol se convierte en una lista. La busqueda
        // deja de ser O(log n) y pasa a ser O(n). Es la razon por la que
        // existen los arboles autoequilibrados.
        ArbolBinarioBusqueda degenerado = new ArbolBinarioBusqueda();
        for (int v : new int[] { 10, 20, 30, 40, 50 }) {
            degenerado.insertar(v);
        }
        System.out.println("  Arbol con entrada ya ordenada (10 20 30 40 50):");
        System.out.println("    InOrden: " + degenerado.inOrden()
                + " | altura: " + degenerado.altura() + " con 5 nodos -> degenerado en lista");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
