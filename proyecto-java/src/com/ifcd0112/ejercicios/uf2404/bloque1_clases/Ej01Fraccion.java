package com.ifcd0112.ejercicios.uf2404.bloque1_clases;

/**
 * UF2404 - BLOQUE 1 - EJERCICIO 1: La clase Fraccion.
 *
 * <p>Criterios de evaluacion: CE1.2, CE1.3, CE2.10</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej01Fraccion {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Disena e implementa una clase Fraccion que represente una fraccion
     * matematica con numerador y denominador enteros. La clase debe garantizar
     * en todo momento que ninguna fraccion pueda existir con denominador cero.
     *
     * Se pide:
     *   1. Declara los atributos como privados y proporciona unicamente los
     *      metodos de acceso que consideres imprescindibles.
     *   2. Implementa un constructor con dos parametros que lance una excepcion
     *      IllegalArgumentException si el denominador recibido es cero.
     *   3. Implementa un metodo privado simplificar() que reduzca la fraccion
     *      usando el maximo comun divisor, y llamalo desde el constructor.
     *   4. Implementa los metodos sumar(Fraccion otra), restar(Fraccion otra) y
     *      multiplicar(Fraccion otra), que devuelvan una nueva Fraccion sin
     *      modificar las originales.
     *   5. Sobrescribe el metodo toString() para que devuelva la fraccion con el
     *      formato "numerador/denominador".
     *   6. Escribe una clase con metodo main que cree varias fracciones y
     *      compruebe todas las operaciones.
     *
     * Salida esperada (orientativa):
     *      1/2 + 1/3 = 5/6
     *      3/4 - 1/4 = 1/2
     *      2/3 * 3/5 = 2/5
     *
     * Pista: para calcular el maximo comun divisor puedes implementar el
     * algoritmo de Euclides de forma recursiva: mcd(a, b) = b == 0 ? a : mcd(b, a % b).
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /**
     * Fraccion matematica inmutable.
     *
     * <p>Se declara inmutable (atributos {@code final} y operaciones que
     * devuelven objetos nuevos) porque el enunciado exige en su apartado 4 que
     * sumar, restar y multiplicar NO modifiquen las fracciones originales. La
     * inmutabilidad es la forma mas simple de garantizarlo: si nadie puede
     * cambiar el estado, es imposible modificarlo por accidente.</p>
     */
    public static final class Fraccion {

        // Apartado 1: atributos privados. Ademas final, por lo dicho arriba.
        private final int numerador;
        private final int denominador;

        /**
         * Crea una fraccion ya simplificada y con el signo normalizado.
         *
         * @param numerador   numerador de la fraccion
         * @param denominador denominador de la fraccion; no puede ser cero
         * @throws IllegalArgumentException si el denominador es cero
         */
        public Fraccion(int numerador, int denominador) {
            // Apartado 2: el constructor es la unica puerta de entrada, asi que
            // validar aqui garantiza que NUNCA existira una fraccion invalida.
            if (denominador == 0) {
                throw new IllegalArgumentException("El denominador no puede ser cero");
            }
            // Normalizamos el signo: lo llevamos siempre al numerador, para que
            // 1/-2 y -1/2 se representen igual y toString() sea predecible.
            int signo = (denominador < 0) ? -1 : 1;
            // Apartado 3: se simplifica DESDE el constructor, de modo que el
            // objeto nace ya en forma canonica.
            int divisor = simplificar(Math.abs(numerador), Math.abs(denominador));
            this.numerador = signo * numerador / divisor;
            this.denominador = signo * denominador / divisor;
        }

        /**
         * Apartado 3: maximo comun divisor por el algoritmo de Euclides,
         * en su version recursiva. Es privado porque es un detalle interno:
         * nadie fuera de la clase necesita invocarlo.
         *
         * @param a primer operando, no negativo
         * @param b segundo operando, no negativo
         * @return el maximo comun divisor, o 1 si ambos son cero
         */
        private static int simplificar(int a, int b) {
            if (b == 0) {
                // Caso numerador 0: mcd(0,0) seria 0 y dividir por cero romperia
                // el constructor. Devolvemos 1, que deja la fraccion intacta.
                return (a == 0) ? 1 : a;
            }
            return simplificar(b, a % b);
        }

        // Apartado 1: solo los getters imprescindibles. No hay setters: la clase
        // es inmutable, de modo que un setter contradiria el diseno.
        public int getNumerador()   { return numerador; }
        public int getDenominador() { return denominador; }

        /**
         * Apartado 4: suma. Devuelve una fraccion NUEVA; ni esta ni la recibida
         * se modifican. Formula: a/b + c/d = (a*d + c*b) / (b*d).
         *
         * @param otra sumando
         * @return una fraccion nueva ya simplificada
         */
        public Fraccion sumar(Fraccion otra) {
            return new Fraccion(this.numerador * otra.denominador + otra.numerador * this.denominador,
                                this.denominador * otra.denominador);
        }

        /**
         * Apartado 4: resta. a/b - c/d = (a*d - c*b) / (b*d).
         *
         * @param otra sustraendo
         * @return una fraccion nueva ya simplificada
         */
        public Fraccion restar(Fraccion otra) {
            return new Fraccion(this.numerador * otra.denominador - otra.numerador * this.denominador,
                                this.denominador * otra.denominador);
        }

        /**
         * Apartado 4: multiplicacion. a/b * c/d = (a*c) / (b*d).
         *
         * @param otra factor
         * @return una fraccion nueva ya simplificada
         */
        public Fraccion multiplicar(Fraccion otra) {
            return new Fraccion(this.numerador * otra.numerador,
                                this.denominador * otra.denominador);
        }

        /** Apartado 5: formato "numerador/denominador". */
        @Override
        public String toString() {
            return numerador + "/" + denominador;
        }
    }

    // =====================================================================
    // COMPROBACION (apartado 6)
    // =====================================================================

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2404-E01] La clase Fraccion");

        Fraccion mitad   = new Fraccion(1, 2);
        Fraccion tercio  = new Fraccion(1, 3);
        Fraccion tresCuartos = new Fraccion(3, 4);
        Fraccion unCuarto    = new Fraccion(1, 4);
        Fraccion dosTercios  = new Fraccion(2, 3);
        Fraccion tresQuintos = new Fraccion(3, 5);

        System.out.println("  " + mitad + " + " + tercio + " = " + mitad.sumar(tercio));
        System.out.println("  " + tresCuartos + " - " + unCuarto + " = " + tresCuartos.restar(unCuarto));
        System.out.println("  " + dosTercios + " * " + tresQuintos + " = " + dosTercios.multiplicar(tresQuintos));

        // Comprobamos que las originales NO se han modificado (apartado 4).
        System.out.println("  Las originales siguen intactas: " + mitad + " y " + tercio);

        // Comprobamos la simplificacion automatica y la normalizacion del signo.
        System.out.println("  6/8 se simplifica a " + new Fraccion(6, 8));
        System.out.println("  1/-2 se normaliza a " + new Fraccion(1, -2));
        System.out.println("  0/5 se representa como " + new Fraccion(0, 5));

        // Apartado 2: el denominador cero se rechaza en el constructor.
        try {
            new Fraccion(3, 0);
            System.out.println("  ERROR: se ha creado una fraccion con denominador 0");
        } catch (IllegalArgumentException e) {
            System.out.println("  new Fraccion(3, 0) -> IllegalArgumentException: " + e.getMessage());
        }
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
