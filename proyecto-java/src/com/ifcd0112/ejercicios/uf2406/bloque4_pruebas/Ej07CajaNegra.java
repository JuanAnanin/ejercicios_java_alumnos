package com.ifcd0112.ejercicios.uf2406.bloque4_pruebas;

/**
 * UF2406 - BLOQUE 4 - EJERCICIO 7: Diseno de casos de prueba de caja negra.
 *
 * <p>Criterios de evaluacion: CE2.2, CE2.5</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej07CajaNegra {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * El sistema del gimnasio incluye un metodo que calcula el precio de la
     * cuota mensual de un socio segun estas reglas: la cuota base es de 40 EUR;
     * los menores de 26 anos tienen un 25% de descuento; los mayores de 65, un
     * 30%; los socios con mas de 2 anos de antiguedad tienen 5 EUR adicionales
     * de descuento sobre el resultado anterior; y la cuota final nunca puede ser
     * inferior a 20 EUR.
     *
     * Se pide:
     *   1. Identifica las clases de equivalencia de cada parametro de entrada
     *      (edad y antiguedad).
     *   2. Identifica los valores limite de cada frontera entre clases (presta
     *      especial atencion a las edades 25, 26, 65 y 66).
     *   3. Elabora la tabla de decision que recoja todas las combinaciones
     *      relevantes de condiciones y el resultado esperado de cada una.
     *   4. Disena la bateria minima de casos de prueba que cubra todas las
     *      clases de equivalencia y todos los valores limite, indicando para
     *      cada caso: entrada, resultado esperado y que clase o limite verifica.
     *   5. Incluye al menos dos casos de prueba con entradas invalidas (edad
     *      negativa, antiguedad no numerica) y determina cual deberia ser el
     *      comportamiento correcto del metodo ante ellas.
     *   6. Comprueba que tu bateria detectaria el error de haber programado
     *      "menores de 26" como "edad < 25" en lugar de "edad < 26".
     *
     * Pista: los errores de programacion se concentran de forma muy
     * caracteristica en las fronteras entre rangos: por cada limite conviene
     * probar el valor justo anterior, el valor exacto y el valor justo
     * posterior.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * DONDE ESTA CADA COSA.
     *
     * Las clases de equivalencia, los valores limite, la tabla de decision y la
     * bateria de 12 casos estan tabuladas en:
     *
     *      recursos/uf2406/04_diseno_pruebas.md
     *
     * Aqui esta la implementacion del metodo bajo prueba y la ejecucion real de
     * los 12 casos, incluido el apartado 6: se ejecuta tambien la version con el
     * error deliberado para comprobar que la bateria lo detecta. No se dice que
     * lo detectaria; se ejecuta y se ve cual falla.
     *
     * APARTADO 1: clases de equivalencia.
     *
     *   PARAMETRO EDAD
     *     CE-1  edad < 0          invalida
     *     CE-2  0 <= edad <= 25   descuento del 25 por ciento
     *     CE-3  26 <= edad <= 64  sin descuento por edad
     *     CE-4  edad >= 65        descuento del 30 por ciento
     *
     *   PARAMETRO ANTIGUEDAD (meses)
     *     CE-5  antiguedad < 0    invalida
     *     CE-6  0 <= ant <= 24    sin descuento adicional
     *     CE-7  antiguedad > 24   5 EUR adicionales de descuento
     *
     * APARTADO 2: valores limite.
     *
     *   Edad:        -1, 0, 25, 26, 64, 65, 66
     *   Antiguedad:  -1, 0, 24, 25
     *
     * La idea de fondo: dentro de una clase de equivalencia, todos los valores
     * se comportan igual, asi que probar 30, 40 y 50 aporta lo mismo que probar
     * solo 40. Lo que NO da igual es el borde, porque ahi es donde se equivoca
     * quien programa: <= en lugar de <, o restar uno de menos.
     *
     * APARTADO 5: que hacer con las entradas invalidas.
     *
     * El metodo debe lanzar IllegalArgumentException, no devolver un valor
     * cualquiera ni cero. Una edad negativa no es un caso de negocio raro: es un
     * error de quien llama, y devolver un numero lo esconderia. La cuota saldria
     * mal en una factura y nadie sabria por que.
     *
     * Sobre "antiguedad no numerica" que menciona el enunciado: si el parametro
     * es un int, no puede llegar texto, porque el compilador no lo permite. Ese
     * caso pertenece a la capa que convierte lo que escribe el usuario, y es
     * exactamente el problema del ejercicio 11 de la UF2405. Aqui se prueba con
     * antiguedad negativa, que es el equivalente con este tipo de dato. Conviene
     * comentarlo en clase: el enunciado esta mezclando dos capas.
     *
     * UNA OBSERVACION SOBRE EL ENUNCIADO, COMPROBADA EJECUTANDO.
     *
     * La regla "la cuota final nunca puede ser inferior a 20 EUR" NO SE ACTIVA
     * NUNCA con las reglas dadas. El caso mas barato posible es un socio de 65
     * anos o mas con mas de dos anos de antiguedad: 40 * 0.70 - 5 = 23 EUR. El
     * suelo de 20 esta por debajo del minimo alcanzable, asi que es codigo
     * inalcanzable.
     *
     * No es un fallo grave, pero da mucho juego en clase por tres motivos:
     *   - Es un ejemplo real de codigo defensivo que nunca se ejecuta, y por
     *     tanto nunca se prueba. Si manana se anade un descuento de socio
     *     fundador, ese suelo entraria en funcionamiento por primera vez sin que
     *     nadie lo haya verificado jamas.
     *   - Explica por que una prueba de cobertura marcaria esa linea en rojo, y
     *     por que eso es informacion util y no ruido.
     *   - Enlaza con el ejercicio 8: los caminos que el codigo tiene y el
     *     enunciado no describe, y al reves.
     * El metodo resolver() lo demuestra recorriendo todo el espacio de entradas.
     */

    /** Ruta del documento con las tablas de diseno de pruebas. */
    public static final String DOCUMENTO = "recursos/uf2406/04_diseno_pruebas.md";

    /** Calcula la cuota mensual de un socio del gimnasio. */
    public static class CalculadoraCuota {

        /** Cuota base antes de descuentos. */
        public static final double BASE = 40.00;

        /** Descuento por ser menor de 26. */
        public static final double DESCUENTO_JOVEN = 0.25;

        /** Descuento por tener 65 o mas. */
        public static final double DESCUENTO_SENIOR = 0.30;

        /** Descuento fijo por antiguedad superior a dos anos. */
        public static final double DESCUENTO_ANTIGUEDAD = 5.00;

        /** Cuota minima que se puede llegar a cobrar. */
        public static final double CUOTA_MINIMA = 20.00;

        /**
         * Calcula la cuota mensual.
         *
         * @param edad             edad del socio en anos
         * @param antiguedadMeses  meses que lleva de socio
         * @return importe de la cuota en euros
         * @throws IllegalArgumentException si algun parametro es negativo
         */
        public double calcular(int edad, int antiguedadMeses) {
            // Apartado 5: las entradas invalidas se rechazan, no se maquillan.
            if (edad < 0) {
                throw new IllegalArgumentException("La edad no puede ser negativa: " + edad);
            }
            if (antiguedadMeses < 0) {
                throw new IllegalArgumentException(
                        "La antiguedad no puede ser negativa: " + antiguedadMeses);
            }

            double cuota = BASE;
            if (edad < 26) {
                cuota = cuota * (1 - DESCUENTO_JOVEN);
            } else if (edad >= 65) {
                cuota = cuota * (1 - DESCUENTO_SENIOR);
            }
            if (antiguedadMeses > 24) {
                cuota = cuota - DESCUENTO_ANTIGUEDAD;
            }
            return Math.max(cuota, CUOTA_MINIMA);
        }

        /**
         * La MISMA regla con el error del apartado 6: el joven se programa como
         * edad menor que 25 en lugar de menor que 26.
         *
         * @param edad            edad del socio en anos
         * @param antiguedadMeses meses que lleva de socio
         * @return importe de la cuota, con el error introducido
         */
        public double calcularConError(int edad, int antiguedadMeses) {
            if (edad < 0 || antiguedadMeses < 0) {
                throw new IllegalArgumentException("Parametro negativo");
            }
            double cuota = BASE;
            if (edad < 25) {                       // <-- AQUI esta el error
                cuota = cuota * (1 - DESCUENTO_JOVEN);
            } else if (edad >= 65) {
                cuota = cuota * (1 - DESCUENTO_SENIOR);
            }
            if (antiguedadMeses > 24) {
                cuota = cuota - DESCUENTO_ANTIGUEDAD;
            }
            return Math.max(cuota, CUOTA_MINIMA);
        }
    }

    /** Un caso de prueba de la bateria del apartado 4. */
    public static class CasoPrueba {

        private final int numero;
        private final int edad;
        private final int antiguedad;
        private final Double esperado;      // null significa "debe lanzar excepcion"
        private final String verifica;

        /**
         * @param numero     identificador del caso
         * @param edad       edad de entrada
         * @param antiguedad antiguedad de entrada en meses
         * @param esperado   cuota esperada, o null si se espera excepcion
         * @param verifica   que clase de equivalencia o limite comprueba
         */
        public CasoPrueba(int numero, int edad, int antiguedad, Double esperado, String verifica) {
            this.numero = numero;
            this.edad = edad;
            this.antiguedad = antiguedad;
            this.esperado = esperado;
            this.verifica = verifica;
        }

        /** @return identificador del caso */
        public int getNumero() { return numero; }

        /** @return edad de entrada */
        public int getEdad() { return edad; }

        /** @return antiguedad de entrada */
        public int getAntiguedad() { return antiguedad; }

        /** @return cuota esperada, o null si se espera excepcion */
        public Double getEsperado() { return esperado; }

        /** @return descripcion de lo que verifica */
        public String getVerifica() { return verifica; }
    }

    /**
     * Apartado 4: la bateria minima de casos de prueba.
     *
     * @return los doce casos disenados
     */
    public static CasoPrueba[] bateria() {
        return new CasoPrueba[] {
            new CasoPrueba(1,  20, 12, 30.00, "CE-2 + CE-6, joven sin antiguedad"),
            new CasoPrueba(2,  25, 12, 30.00, "LIMITE superior de joven (detecta el error <25)"),
            new CasoPrueba(3,  26, 12, 40.00, "LIMITE inferior de estandar"),
            new CasoPrueba(4,  40, 12, 40.00, "CE-3 + CE-6, caso central"),
            new CasoPrueba(5,  64, 12, 40.00, "LIMITE superior de estandar"),
            new CasoPrueba(6,  65, 12, 28.00, "LIMITE inferior de senior"),
            new CasoPrueba(7,  70, 12, 28.00, "CE-4 + CE-6, senior"),
            new CasoPrueba(8,  40, 24, 40.00, "LIMITE: 24 meses aun NO da antiguedad"),
            new CasoPrueba(9,  40, 25, 35.00, "LIMITE: 25 meses ya da antiguedad"),
            new CasoPrueba(10, 20, 36, 25.00, "CE-2 + CE-7, el caso mas barato de joven"),
            new CasoPrueba(11, -1, 12, null,  "CE-1, edad invalida"),
            new CasoPrueba(12, 30, -5, null,  "CE-5, antiguedad invalida")
        };
    }

    // =====================================================================
    // COMPROBACION
    // =====================================================================

    /**
     * Ejecuta la bateria contra una version del metodo.
     *
     * @param calc      calculadora a probar
     * @param conError  true para usar la version con el error del apartado 6
     * @param mostrar   true para imprimir el detalle de cada caso
     * @return numero de casos que fallan
     */
    public static int ejecutarBateria(CalculadoraCuota calc, boolean conError, boolean mostrar) {
        int fallos = 0;
        for (CasoPrueba cp : bateria()) {
            String resultado;
            boolean ok;
            try {
                double obtenido = conError
                        ? calc.calcularConError(cp.getEdad(), cp.getAntiguedad())
                        : calc.calcular(cp.getEdad(), cp.getAntiguedad());
                if (cp.getEsperado() == null) {
                    ok = false;
                    resultado = String.format("devolvio %.2f y se esperaba excepcion", obtenido);
                } else {
                    // Delta: los decimales no se comparan con igualdad exacta.
                    ok = Math.abs(obtenido - cp.getEsperado()) < 0.001;
                    resultado = String.format("%.2f", obtenido);
                }
            } catch (IllegalArgumentException e) {
                ok = cp.getEsperado() == null;
                resultado = "excepcion";
            }
            if (!ok) {
                fallos++;
            }
            if (mostrar) {
                System.out.printf("    CP-%-2d edad=%-3d ant=%-3d  esperado %-9s obtenido %-9s %s  %s%n",
                        cp.getNumero(), cp.getEdad(), cp.getAntiguedad(),
                        cp.getEsperado() == null ? "excepcion" : String.format("%.2f", cp.getEsperado()),
                        resultado, ok ? "OK   " : "FALLA", cp.getVerifica());
            }
        }
        return fallos;
    }

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2406-E07] Diseno de casos de prueba de caja negra");
        System.out.println("  Clases de equivalencia, limites y tabla de decision:");
        System.out.println("    " + DOCUMENTO);

        CalculadoraCuota calc = new CalculadoraCuota();

        // Apartado 3: la tabla de decision, calculada.
        System.out.println("  Apartado 3, tabla de decision (los importes se calculan, no se copian):");
        System.out.println("    Condicion            C1     C2     C3     C4     C5     C6");
        System.out.println("    Edad < 26            Si     Si     No     No     No     No");
        System.out.println("    Edad >= 65           No     No     No     No     Si     Si");
        System.out.println("    Antiguedad > 24      No     Si     No     Si     No     Si");
        int[][] combinaciones = { { 20, 12 }, { 20, 36 }, { 40, 12 }, { 40, 36 }, { 70, 12 }, { 70, 36 } };
        StringBuilder fila = new StringBuilder("    Cuota resultante  ");
        for (int[] c : combinaciones) {
            fila.append(String.format("%7.2f", calc.calcular(c[0], c[1])));
        }
        System.out.println(fila);

        // Apartado 4: la bateria contra la version correcta.
        System.out.println("  Apartado 4, bateria de 12 casos contra la version CORRECTA:");
        int fallosCorrecta = ejecutarBateria(calc, false, true);
        System.out.println("    Resultado: " + (12 - fallosCorrecta) + " correctos, "
                + fallosCorrecta + " fallos");

        // Apartado 6: la misma bateria contra la version con el error.
        System.out.println("  Apartado 6, la MISMA bateria contra la version con edad < 25:");
        int fallosError = ejecutarBateria(calc, true, false);
        System.out.println("    Fallos detectados: " + fallosError);
        for (CasoPrueba cp : bateria()) {
            if (cp.getEsperado() == null) {
                continue;
            }
            double conError = calc.calcularConError(cp.getEdad(), cp.getAntiguedad());
            if (Math.abs(conError - cp.getEsperado()) >= 0.001) {
                System.out.printf("    Lo detecta CP-%d (edad %d): esperaba %.2f y sale %.2f  -> %s%n",
                        cp.getNumero(), cp.getEdad(), cp.getEsperado(), conError, cp.getVerifica());
            }
        }
        System.out.println("    Solo lo detecta el caso del valor frontera. Los casos centrales");
        System.out.println("    (edad 20, 40, 70) pasan igual de bien con el codigo erroneo: por eso");
        System.out.println("    una bateria sin valores limite da una falsa sensacion de seguridad.");

        // La observacion sobre el suelo de 20 EUR, comprobada por fuerza bruta.
        double minimo = Double.MAX_VALUE;
        int edadMin = 0;
        int antMin = 0;
        for (int edad = 0; edad <= 120; edad++) {
            for (int ant = 0; ant <= 600; ant++) {
                double c = calc.calcular(edad, ant);
                if (c < minimo) {
                    minimo = c;
                    edadMin = edad;
                    antMin = ant;
                }
            }
        }
        System.out.println("  Observacion sobre el enunciado, comprobada recorriendo todas las");
        System.out.println("    entradas de 0 a 120 anos y de 0 a 600 meses:");
        System.out.printf("    cuota minima alcanzable = %.2f EUR (edad %d, antiguedad %d meses)%n",
                minimo, edadMin, antMin);
        System.out.println("    La regla \"nunca inferior a 20 EUR\" NO se activa nunca: el minimo");
        System.out.println("    posible es 23. Es codigo defensivo inalcanzable, y por tanto codigo");
        System.out.println("    que nunca se prueba. Si manana se anade otro descuento, ese suelo");
        System.out.println("    entraria en funcionamiento por primera vez sin haberse verificado.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
