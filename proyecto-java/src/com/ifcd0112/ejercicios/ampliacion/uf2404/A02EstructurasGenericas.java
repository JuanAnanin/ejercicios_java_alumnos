package com.ifcd0112.ejercicios.ampliacion.uf2404;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * AMPLIACION UF2404 - EJERCICIO A2: Estructuras de datos genericas.
 *
 * <p>Criterios de evaluacion: CE2.7, CE2.9, CE2.8</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class A02EstructurasGenericas {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * La pila y la cola que implementaste en el bloque 3 de la UF2404 solo
     * sirven para un tipo de dato. Si necesitas una pila de cadenas y otra de
     * enteros, hay que escribir la clase dos veces. Este ejercicio te pide
     * generalizarlas.
     *
     * Se pide:
     *   1. Reescribe la pila como una clase generica Pila<T> que funcione con
     *      cualquier tipo, conservando las operaciones apilar, desapilar, cima,
     *      estaVacia y tamano.
     *   2. Haz lo mismo con la cola: Cola<T>, con encolar, desencolar, primero,
     *      estaVacia y tamano.
     *   3. Comprueba las dos con al menos tres tipos distintos, incluido uno
     *      propio, y explica que ventaja concreta aporta frente a haber usado
     *      Object en lugar de T.
     *   4. Implementa una ColaConPrioridad<T> que solo admita tipos ordenables,
     *      de modo que el compilador rechace un tipo que no lo sea. Investiga
     *      como se declara esa restriccion.
     *   5. Intenta crear dentro de la pila un array del tipo generico
     *      (new T[10]). Explica por que el compilador no te deja y como se
     *      resuelve en la practica.
     *   6. Anade a la pila un metodo apilarTodos que acepte cualquier coleccion
     *      cuyos elementos sean del tipo de la pila o de un subtipo suyo, y
     *      explica que significa la notacion que has tenido que emplear.
     *   7. Explica en un comentario que es el borrado de tipos y por que, en
     *      tiempo de ejecucion, una Pila<String> y una Pila<Integer> son la
     *      misma clase.
     *
     * Pista: los genericos no existen en tiempo de ejecucion. Todo lo que hacen
     * ocurre durante la compilacion, y esa es a la vez su mayor virtud (los
     * errores aparecen antes de ejecutar) y la explicacion de casi todas sus
     * limitaciones.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * APARTADO 3: que se gana frente a usar Object.
     *
     * Con una pila de Object, esto compila y revienta al ejecutar:
     *
     *      Pila pila = new Pila();
     *      pila.apilar("hola");
     *      Integer n = (Integer) pila.desapilar();   // ClassCastException
     *
     * Con Pila<String>, la segunda linea ni siquiera compila. La diferencia no
     * es de comodidad: es CUANDO se entera uno del error. Con Object se entera
     * el usuario en produccion; con genericos, quien programa, antes de comer.
     *
     * Y hay una segunda ventaja menos vistosa: desaparecen las conversiones.
     * Un codigo lleno de (String) y (Integer) no solo es feo, es una lista de
     * sitios donde puede saltar una excepcion.
     *
     * APARTADO 5: por que no se puede hacer new T[10].
     *
     * Por el borrado de tipos: en tiempo de ejecucion no existe T, asi que la
     * maquina virtual no sabria que array crear. Los arrays, al contrario que
     * los genericos, SI conocen su tipo en ejecucion y lo comprueban en cada
     * escritura, de modo que las dos cosas no encajan.
     *
     * En la practica hay dos salidas:
     *   - Usar una coleccion en lugar de un array, que es lo que hace esta
     *     implementacion y lo que hace la propia biblioteca estandar.
     *   - Crear Object[] y convertir, anotando el metodo con
     *     @SuppressWarnings("unchecked"). Es lo que hace ArrayList por dentro,
     *     y solo es seguro si el array no se expone nunca hacia fuera.
     *
     * APARTADO 6: la notacion <? extends T>.
     *
     * apilarTodos(Collection<? extends T>) significa "una coleccion de T o de
     * cualquier subtipo de T". Sin ese comodin, a una Pila<Number> no se le
     * podria pasar una List<Integer>, aunque todo Integer sea un Number.
     *
     * El motivo es que los genericos NO son covariantes: List<Integer> no es un
     * subtipo de List<Number>, y con razon. Si lo fuera, se podria hacer esto:
     *
     *      List<Integer> enteros = new ArrayList<>();
     *      List<Number> numeros = enteros;      // si esto compilara...
     *      numeros.add(3.14);                   // ...meteriamos un double
     *      int n = enteros.get(0);              // ...y aqui reventaria
     *
     * El comodin resuelve el problema poniendo una condicion: de una coleccion
     * <? extends T> se puede LEER (todo lo que salga es un T), pero no se puede
     * ESCRIBIR, porque no se sabe cual de los subtipos es. La regla mnemotecnica
     * es PECS: Producer Extends, Consumer Super. Si la coleccion produce datos
     * para ti, extends; si la consume, super.
     *
     * APARTADO 7: el borrado de tipos.
     *
     * El compilador usa los genericos para comprobarlo todo y despues los
     * BORRA. Pila<String> y Pila<Integer> compilan a la misma clase Pila, con
     * Object dentro y conversiones automaticas insertadas donde hacen falta.
     *
     * Se comprueba en una linea, y resolver() lo hace:
     *      new Pila<String>().getClass() == new Pila<Integer>().getClass()  -> true
     *
     * Consecuencias practicas, todas derivadas de lo mismo:
     *   - No se puede hacer new T() ni new T[].
     *   - No se puede preguntar  if (x instanceof Pila<String>).
     *   - No se pueden sobrecargar dos metodos que solo difieran en el generico.
     *   - Un atributo static no puede ser de tipo T: solo hay una copia para
     *     todas las instanciaciones, y no sabria de que tipo ser.
     *
     * Se hizo asi por compatibilidad: el codigo compilado antes de Java 5 tenia
     * que seguir funcionando. Es un precio historico, pero conviene saber que
     * es la causa de todas esas restricciones y no una lista de caprichos.
     */

    // ---------------------------------------------------------------------
    // Apartado 1: la pila generica
    // ---------------------------------------------------------------------

    /**
     * Pila generica LIFO: el ultimo en entrar es el primero en salir.
     *
     * @param <T> tipo de los elementos que almacena
     */
    public static class Pila<T> {

        // Apartado 5: aqui iria new T[10] y no compila. Se usa una lista, que es
        // lo que hace tambien la biblioteca estandar por dentro.
        private final List<T> elementos = new ArrayList<>();

        /**
         * Apila un elemento.
         *
         * @param elemento valor a apilar
         */
        public void apilar(T elemento) {
            elementos.add(elemento);
        }

        /**
         * Apartado 6: apila todos los elementos de una coleccion compatible.
         *
         * @param origen coleccion de T o de cualquier subtipo de T
         */
        public void apilarTodos(Collection<? extends T> origen) {
            for (T elemento : origen) {
                apilar(elemento);
            }
        }

        /**
         * Extrae el elemento de la cima.
         *
         * @return el ultimo elemento apilado
         * @throws NoSuchElementException si la pila esta vacia
         */
        public T desapilar() {
            if (estaVacia()) {
                throw new NoSuchElementException("La pila esta vacia");
            }
            return elementos.remove(elementos.size() - 1);
        }

        /**
         * Consulta la cima sin extraerla.
         *
         * @return el ultimo elemento apilado
         * @throws NoSuchElementException si la pila esta vacia
         */
        public T cima() {
            if (estaVacia()) {
                throw new NoSuchElementException("La pila esta vacia");
            }
            return elementos.get(elementos.size() - 1);
        }

        /** @return true si no hay elementos */
        public boolean estaVacia() { return elementos.isEmpty(); }

        /** @return numero de elementos */
        public int tamano() { return elementos.size(); }

        @Override
        public String toString() { return elementos.toString(); }
    }

    // ---------------------------------------------------------------------
    // Apartado 2: la cola generica
    // ---------------------------------------------------------------------

    /**
     * Cola generica FIFO: el primero en entrar es el primero en salir.
     *
     * @param <T> tipo de los elementos que almacena
     */
    public static class Cola<T> {

        private final List<T> elementos = new ArrayList<>();

        /**
         * Encola un elemento por el final.
         *
         * @param elemento valor a encolar
         */
        public void encolar(T elemento) {
            elementos.add(elemento);
        }

        /**
         * Extrae el primer elemento.
         *
         * @return el elemento que lleva mas tiempo esperando
         * @throws NoSuchElementException si la cola esta vacia
         */
        public T desencolar() {
            if (estaVacia()) {
                throw new NoSuchElementException("La cola esta vacia");
            }
            // Quitar por el principio obliga a desplazar el resto. Para una cola
            // de uso intensivo se usaria un array circular o un LinkedList; aqui
            // interesa que se lea con claridad.
            return elementos.remove(0);
        }

        /**
         * Consulta el primero sin extraerlo.
         *
         * @return el elemento que lleva mas tiempo esperando
         * @throws NoSuchElementException si la cola esta vacia
         */
        public T primero() {
            if (estaVacia()) {
                throw new NoSuchElementException("La cola esta vacia");
            }
            return elementos.get(0);
        }

        /** @return true si no hay elementos */
        public boolean estaVacia() { return elementos.isEmpty(); }

        /** @return numero de elementos */
        public int tamano() { return elementos.size(); }

        @Override
        public String toString() { return elementos.toString(); }
    }

    // ---------------------------------------------------------------------
    // Apartado 4: cola con prioridad, con el tipo restringido
    // ---------------------------------------------------------------------

    /**
     * Cola en la que sale siempre el elemento menor segun su orden natural.
     *
     * <p>La declaracion {@code <T extends Comparable<T>>} es un tipo generico
     * ACOTADO: obliga a que el tipo sepa compararse consigo mismo. Gracias a
     * ella se puede llamar a compareTo dentro de la clase, cosa que con un T
     * libre seria imposible, y ademas el compilador rechaza cualquier intento
     * de crear una ColaConPrioridad de un tipo que no sea ordenable.</p>
     *
     * @param <T> tipo ordenable de los elementos
     */
    public static class ColaConPrioridad<T extends Comparable<T>> {

        private final List<T> elementos = new ArrayList<>();

        /**
         * Inserta un elemento manteniendo el orden.
         *
         * @param elemento valor a insertar
         */
        public void insertar(T elemento) {
            int posicion = 0;
            // Se busca el sitio comparando: esto solo es posible porque el
            // tipo esta acotado a Comparable.
            while (posicion < elementos.size()
                    && elementos.get(posicion).compareTo(elemento) <= 0) {
                posicion++;
            }
            elementos.add(posicion, elemento);
        }

        /**
         * Extrae el elemento menor.
         *
         * @return el primero segun el orden natural
         * @throws NoSuchElementException si esta vacia
         */
        public T extraerMinimo() {
            if (elementos.isEmpty()) {
                throw new NoSuchElementException("La cola con prioridad esta vacia");
            }
            return elementos.remove(0);
        }

        /** @return numero de elementos */
        public int tamano() { return elementos.size(); }

        @Override
        public String toString() { return elementos.toString(); }
    }

    /** Tipo propio del dominio, para el apartado 3. */
    public static class Aviso implements Comparable<Aviso> {

        private final String texto;
        private final int urgencia;

        /**
         * @param texto    descripcion del aviso
         * @param urgencia menor numero, mas urgente
         */
        public Aviso(String texto, int urgencia) {
            this.texto = texto;
            this.urgencia = urgencia;
        }

        /** @return descripcion del aviso */
        public String getTexto() { return texto; }

        /** @return urgencia; menor numero significa mas urgente */
        public int getUrgencia() { return urgencia; }

        @Override
        public int compareTo(Aviso otro) {
            return Integer.compare(this.urgencia, otro.urgencia);
        }

        @Override
        public String toString() { return urgencia + ":" + texto; }
    }

    // =====================================================================
    // COMPROBACION
    // =====================================================================

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[AMP-UF2404-A2] Estructuras de datos genericas");

        // Apartado 3: la misma clase, tres tipos distintos.
        System.out.println("  Apartados 1 y 3, la MISMA clase Pila con tres tipos:");
        Pila<String> palabras = new Pila<>();
        palabras.apilar("uno");
        palabras.apilar("dos");
        palabras.apilar("tres");
        System.out.println("    Pila<String>  " + palabras + "  cima: " + palabras.cima()
                + "  -> desapila: " + palabras.desapilar());

        Pila<Integer> numeros = new Pila<>();
        numeros.apilar(10);
        numeros.apilar(20);
        // Aqui NO hace falta convertir: el compilador ya sabe que sale un int.
        int suma = numeros.desapilar() + numeros.desapilar();
        System.out.println("    Pila<Integer> suma de los dos desapilados: " + suma);

        Pila<Aviso> avisos = new Pila<>();
        avisos.apilar(new Aviso("revisar caldera", 2));
        avisos.apilar(new Aviso("fuga de agua", 1));
        System.out.println("    Pila<Aviso>   " + avisos + "  cima: " + avisos.cima().getTexto());

        System.out.println("    Con una pila de Object, esto compilaria y reventaria al ejecutar:");
        System.out.println("      Integer n = (Integer) pila.desapilar();   // ClassCastException");
        System.out.println("    Con Pila<String> ni siquiera compila. La diferencia no es de");
        System.out.println("    comodidad, es de CUANDO se entera uno del error.");

        // Apartado 2
        System.out.println("  Apartado 2, la cola generica:");
        Cola<String> turnos = new Cola<>();
        turnos.encolar("Ana");
        turnos.encolar("Luis");
        turnos.encolar("Marta");
        System.out.println("    Cola<String> " + turnos + "  primero: " + turnos.primero());
        System.out.print("    Atendiendo en orden de llegada:");
        while (!turnos.estaVacia()) {
            System.out.print(" " + turnos.desencolar());
        }
        System.out.println();

        // Apartado 6
        System.out.println("  Apartado 6, apilarTodos con <? extends T>:");
        Pila<Number> mezcla = new Pila<>();
        List<Integer> enteros = List.of(1, 2, 3);
        List<Double> reales = List.of(1.5, 2.5);
        mezcla.apilarTodos(enteros);      // List<Integer> en una Pila<Number>
        mezcla.apilarTodos(reales);       // List<Double>  en la misma pila
        System.out.println("    Pila<Number> tras apilar List<Integer> y List<Double>: " + mezcla);
        System.out.println("    Sin el comodin esto no compilaria, porque List<Integer> NO es");
        System.out.println("    subtipo de List<Number>. Y hace bien en no serlo: si lo fuera, se");
        System.out.println("    podria meter un double en una lista de enteros.");

        // Apartado 4
        System.out.println("  Apartado 4, cola con prioridad y tipo acotado:");
        ColaConPrioridad<Aviso> urgencias = new ColaConPrioridad<>();
        urgencias.insertar(new Aviso("revisar caldera", 3));
        urgencias.insertar(new Aviso("fuga de agua", 1));
        urgencias.insertar(new Aviso("cambiar bombilla", 5));
        urgencias.insertar(new Aviso("ascensor parado", 2));
        System.out.println("    Insertados en desorden: " + urgencias);
        System.out.print("    Salen por urgencia:");
        while (urgencias.tamano() > 0) {
            System.out.print("  " + urgencias.extraerMinimo().getTexto());
        }
        System.out.println();
        System.out.println("    <T extends Comparable<T>> es lo que permite llamar a compareTo");
        System.out.println("    dentro de la clase. Con un T libre no se podria: el compilador no");
        System.out.println("    sabria que ese tipo se puede comparar.");

        // Apartado 7: el borrado de tipos, comprobado.
        System.out.println("  Apartado 7, el borrado de tipos:");
        Pila<String> a = new Pila<>();
        Pila<Integer> b = new Pila<>();
        System.out.println("    new Pila<String>().getClass() -> " + a.getClass().getSimpleName());
        System.out.println("    new Pila<Integer>().getClass() -> " + b.getClass().getSimpleName());
        System.out.println("    Son la misma clase: " + (a.getClass() == b.getClass()));
        System.out.println("    En tiempo de ejecucion el generico ya no existe. De ahi salen");
        System.out.println("    todas las restricciones: nada de new T(), nada de new T[], nada de");
        System.out.println("    instanceof Pila<String> y nada de atributos static de tipo T.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
