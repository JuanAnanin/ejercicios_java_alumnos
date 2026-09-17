package com.ifcd0112.ejercicios.uf2406.bloque4_pruebas;

import java.util.ArrayList;
import java.util.List;

/**
 * UF2406 - BLOQUE 4 - EJERCICIO 9: Bateria de pruebas automatizadas con JUnit.
 *
 * <p>Criterios de evaluacion: CE2.6, CE2.7</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej09Junit {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Implementa en JUnit la bateria de pruebas disenada en el ejercicio 7 para
     * el calculo de la cuota mensual.
     *
     * Se pide:
     *   1. Crea la clase CalculadoraCuotaTest con un metodo @Test por cada caso
     *      de prueba disenado.
     *   2. Da a cada metodo un nombre descriptivo que indique que situacion
     *      verifica (por ejemplo, cuotaJovenConDescuentoDelVeinticincoPorCiento).
     *   3. Utiliza @BeforeEach para inicializar el objeto bajo prueba, evitando
     *      repetir esa preparacion en cada metodo.
     *   4. Emplea assertEquals con un margen de tolerancia (delta) para las
     *      comparaciones de numeros decimales, y explica en un comentario por
     *      que es necesario ese margen.
     *   5. Utiliza assertThrows para verificar el comportamiento ante las
     *      entradas invalidas.
     *   6. Ejecuta la bateria completa y comprueba que todas las pruebas pasan.
     *      Despues, introduce deliberadamente un error en el metodo (cambia 0.25
     *      por 0.20) y comprueba que exactamente las pruebas correspondientes
     *      fallan.
     *   7. Interpreta el informe de resultados: que informacion aporta cada
     *      prueba fallida para localizar el error?
     *
     * Salida esperada (orientativa):
     *      Tests run: 12, Failures: 0, Errors: 0, Skipped: 0    <- version correcta
     *      Tests run: 12, Failures: 3, Errors: 0, Skipped: 0    <- tras el error
     *
     * Pista: los numeros decimales no pueden compararse con igualdad exacta por
     * la forma en que se representan internamente: assertEquals(30.0, resultado,
     * 0.001) admite una diferencia despreciable.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * SOBRE LA IMPLEMENTACION: POR QUE UN MINI-JUNIT.
     *
     * La solucion que se entrega al alumnado es la clase CalculadoraCuotaTest
     * con JUnit 5, que esta escrita literalmente en:
     *
     *      recursos/uf2406/CalculadoraCuotaTest.java
     *
     * Ese fichero necesita el jar de JUnit, y este proyecto es de dependencias
     * cero a proposito, asi que no puede compilarse aqui. Para que la bateria se
     * pueda EJECUTAR igualmente, este fichero incluye MiniTest: un armazon de
     * pruebas de cuarenta lineas que reproduce lo justo de JUnit que el
     * ejercicio evalua.
     *
     *      JUnit 5                  MiniTest
     *      -----------------------  ------------------------------
     *      @Test                    prueba("nombre", cuerpo)
     *      @BeforeEach              antesDeCada(cuerpo)
     *      assertEquals(e, o, d)    assertIgual(e, o, d)
     *      assertTrue(cond)         assertCierto(cond, mensaje)
     *      assertThrows(T, cuerpo)  assertLanza(T.class, cuerpo)
     *      Tests run / Failures     el mismo informe, imprimido igual
     *
     * Escribir el armazon tiene ademas valor didactico: se ve que un marco de
     * pruebas no tiene nada de magico. Es una lista de bloques con nombre, un
     * bucle que los ejecuta, un try que atrapa el fallo y un contador.
     *
     * APARTADO 4: por que hace falta el delta.
     *
     * Los numeros decimales se guardan en binario, y hay fracciones decimales
     * que en binario son periodicas, igual que un tercio lo es en decimal. Por
     * eso 0.1 + 0.2 no da exactamente 0.3, sino 0.30000000000000004. En este
     * ejercicio, 40 * 0.75 puede no dar exactamente 30.0.
     *
     * Comparar con igualdad estricta produciria entonces fallos aparentemente
     * inexplicables: la prueba dice que esperaba 30.0 y obtuvo 30.0, y sin
     * embargo falla, porque los ultimos bits no coinciden. El delta admite una
     * diferencia despreciable y elimina ese ruido. El resolver() de abajo lo
     * demuestra imprimiendo el valor con veinte decimales.
     *
     * Regla practica: para dinero, lo correcto en produccion no es el delta,
     * sino no usar double en absoluto. BigDecimal, o guardar el importe en
     * centimos como entero. El delta sirve para probar, no para facturar.
     *
     * APARTADO 7: que informacion aporta una prueba fallida.
     *
     * Tres cosas, y por eso importa el apartado 2:
     *   - QUE valor se esperaba y cual salio. La diferencia entre 30 y 32 dice
     *     ya que el descuento aplicado es del 20 y no del 25 por ciento.
     *   - QUE situacion se estaba probando, y eso lo dice el NOMBRE del metodo.
     *     Un metodo llamado prueba7 no ayuda a nadie.
     *   - CUALES fallan y cuales no. Si fallan las tres pruebas de socios
     *     jovenes y ninguna de las demas, el error esta acotado al descuento
     *     joven antes de mirar una sola linea de codigo.
     */

    /** Ruta de la version con JUnit 5 real. */
    public static final String VERSION_JUNIT = "recursos/uf2406/CalculadoraCuotaTest.java";

    // ---------------------------------------------------------------------
    // MiniTest: el armazon de pruebas sin dependencias
    // ---------------------------------------------------------------------

    /** Error que representa una comprobacion que no se cumple. */
    public static class FalloDeAssert extends AssertionError {

        private static final long serialVersionUID = 1L;

        /**
         * @param mensaje descripcion del fallo
         */
        public FalloDeAssert(String mensaje) {
            super(mensaje);
        }
    }

    /** Resultado de ejecutar una bateria. */
    public static class Resultado {

        private final int ejecutadas;
        private final int fallos;
        private final int errores;
        private final List<String> mensajes;

        Resultado(int ejecutadas, int fallos, int errores, List<String> mensajes) {
            this.ejecutadas = ejecutadas;
            this.fallos = fallos;
            this.errores = errores;
            this.mensajes = mensajes;
        }

        /** @return pruebas ejecutadas */
        public int getEjecutadas() { return ejecutadas; }

        /** @return pruebas que fallaron una comprobacion */
        public int getFallos() { return fallos; }

        /** @return pruebas que lanzaron una excepcion inesperada */
        public int getErrores() { return errores; }

        /** @return descripcion de cada fallo */
        public List<String> getMensajes() { return mensajes; }

        /** @return el informe con el mismo formato que imprime JUnit */
        public String informe() {
            return "Tests run: " + ejecutadas + ", Failures: " + fallos
                    + ", Errors: " + errores + ", Skipped: 0";
        }
    }

    /**
     * Armazon de pruebas minimo, sin dependencias externas.
     *
     * <p>Reproduce lo justo de JUnit que este ejercicio evalua: registrar
     * pruebas con nombre, ejecutar una preparacion antes de cada una,
     * comprobar valores y contar los fallos.</p>
     */
    public static class MiniTest {

        private final List<String> nombres = new ArrayList<>();
        private final List<Runnable> cuerpos = new ArrayList<>();
        private Runnable preparacion;

        /**
         * Equivalente de la anotacion BeforeEach.
         *
         * @param r bloque que se ejecuta antes de cada prueba
         * @return este mismo objeto, para encadenar
         */
        public MiniTest antesDeCada(Runnable r) {
            this.preparacion = r;
            return this;
        }

        /**
         * Equivalente de la anotacion Test.
         *
         * @param nombre nombre descriptivo de la situacion que verifica
         * @param cuerpo codigo de la prueba
         * @return este mismo objeto, para encadenar
         */
        public MiniTest prueba(String nombre, Runnable cuerpo) {
            nombres.add(nombre);
            cuerpos.add(cuerpo);
            return this;
        }

        /**
         * Ejecuta toda la bateria.
         *
         * @param mostrarTodo true para listar tambien las pruebas que pasan
         * @param sangria     prefijo de indentacion de cada linea
         * @return el resultado de la ejecucion
         */
        public Resultado ejecutar(boolean mostrarTodo, String sangria) {
            int fallos = 0;
            int errores = 0;
            List<String> mensajes = new ArrayList<>();
            for (int i = 0; i < nombres.size(); i++) {
                if (preparacion != null) {
                    preparacion.run();
                }
                try {
                    cuerpos.get(i).run();
                    if (mostrarTodo) {
                        System.out.println(sangria + "[OK]     " + nombres.get(i));
                    }
                } catch (FalloDeAssert f) {
                    // Comprobacion que no se cumple: es un FALLO.
                    fallos++;
                    String m = nombres.get(i) + "  ->  " + f.getMessage();
                    mensajes.add(m);
                    System.out.println(sangria + "[FALLA]  " + m);
                } catch (RuntimeException e) {
                    // Excepcion inesperada: JUnit lo cuenta como ERROR, no como
                    // fallo. La distincion importa: un fallo dice que el
                    // programa hace algo distinto de lo esperado; un error dice
                    // que ni siquiera llego a terminar.
                    errores++;
                    String m = nombres.get(i) + "  ->  excepcion inesperada: " + e;
                    mensajes.add(m);
                    System.out.println(sangria + "[ERROR]  " + m);
                }
            }
            return new Resultado(nombres.size(), fallos, errores, mensajes);
        }

        /**
         * Equivalente de assertEquals con delta.
         *
         * @param esperado valor esperado
         * @param obtenido valor obtenido
         * @param delta    diferencia admisible
         */
        public static void assertIgual(double esperado, double obtenido, double delta) {
            if (Math.abs(esperado - obtenido) > delta) {
                throw new FalloDeAssert("expected: " + esperado + " but was: " + obtenido);
            }
        }

        /**
         * Equivalente de assertEquals para textos.
         *
         * @param esperado valor esperado
         * @param obtenido valor obtenido
         */
        public static void assertIgual(Object esperado, Object obtenido) {
            if (esperado == null ? obtenido != null : !esperado.equals(obtenido)) {
                throw new FalloDeAssert("expected: " + esperado + " but was: " + obtenido);
            }
        }

        /**
         * Equivalente de assertTrue.
         *
         * @param condicion condicion que debe cumplirse
         * @param mensaje   descripcion si no se cumple
         */
        public static void assertCierto(boolean condicion, String mensaje) {
            if (!condicion) {
                throw new FalloDeAssert(mensaje);
            }
        }

        /**
         * Equivalente de assertThrows.
         *
         * @param tipo   excepcion que se espera
         * @param cuerpo codigo que deberia lanzarla
         */
        public static void assertLanza(Class<? extends Throwable> tipo, Runnable cuerpo) {
            try {
                cuerpo.run();
            } catch (Throwable t) {
                if (tipo.isInstance(t)) {
                    return;   // era justo lo que se esperaba
                }
                throw new FalloDeAssert("expected " + tipo.getSimpleName()
                        + " but was: " + t.getClass().getSimpleName());
            }
            throw new FalloDeAssert("expected " + tipo.getSimpleName() + " but nothing was thrown");
        }
    }

    // ---------------------------------------------------------------------
    // La bateria de pruebas del ejercicio 7, llevada a MiniTest
    // ---------------------------------------------------------------------

    /** Contrato de la tarifa bajo prueba, para poder cambiarla por la erronea. */
    public interface Tarifa {
        /**
         * @param edad            edad del socio
         * @param antiguedadMeses meses de antiguedad
         * @return cuota mensual
         */
        double calcular(int edad, int antiguedadMeses);
    }

    /** Margen admisible al comparar decimales (apartado 4). */
    private static final double DELTA = 0.001;

    /**
     * Construye la bateria de doce pruebas con nombres descriptivos.
     *
     * @param tarifa implementacion bajo prueba
     * @return la bateria lista para ejecutar
     */
    public static MiniTest bateria(Tarifa tarifa) {
        // Apartado 3: la preparacion comun, una sola vez. Aqui la calculadora no
        // tiene estado, pero el patron es el que importa: si lo tuviera, cada
        // prueba debe empezar con un objeto limpio, o el orden de ejecucion
        // pasaria a influir en el resultado.
        final Ej07CajaNegra.CalculadoraCuota[] calc = new Ej07CajaNegra.CalculadoraCuota[1];

        return new MiniTest()
            .antesDeCada(() -> calc[0] = new Ej07CajaNegra.CalculadoraCuota())

            // Apartado 2: nombres que dicen QUE situacion se verifica.
            .prueba("jovenSinAntiguedadPagaTreinta",
                    () -> MiniTest.assertIgual(30.00, tarifa.calcular(20, 12), DELTA))
            .prueba("limiteSuperiorJovenVeinticincoAniosSigueSiendoJoven",
                    () -> MiniTest.assertIgual(30.00, tarifa.calcular(25, 12), DELTA))
            .prueba("limiteInferiorEstandarVeintiseisAniosYaNoEsJoven",
                    () -> MiniTest.assertIgual(40.00, tarifa.calcular(26, 12), DELTA))
            .prueba("edadCentralDeEstandarPagaLaCuotaBase",
                    () -> MiniTest.assertIgual(40.00, tarifa.calcular(40, 12), DELTA))
            .prueba("limiteSuperiorEstandarSesentaYCuatroSinDescuento",
                    () -> MiniTest.assertIgual(40.00, tarifa.calcular(64, 12), DELTA))
            .prueba("seniorConSesentaYCincoTieneTreintaPorCiento",
                    () -> MiniTest.assertIgual(28.00, tarifa.calcular(65, 12), DELTA))
            .prueba("seniorDeSetentaMantieneElTreintaPorCiento",
                    () -> MiniTest.assertIgual(28.00, tarifa.calcular(70, 12), DELTA))
            .prueba("veinticuatroMesesTodaviaNoDanDescuentoPorAntiguedad",
                    () -> MiniTest.assertIgual(40.00, tarifa.calcular(40, 24), DELTA))
            .prueba("veinticincoMesesYaDescuentanCincoEurosAdicionales",
                    () -> MiniTest.assertIgual(35.00, tarifa.calcular(40, 25), DELTA))
            .prueba("jovenConAntiguedadAcumulaLosDosDescuentos",
                    () -> MiniTest.assertIgual(25.00, tarifa.calcular(20, 36), DELTA))
            // Apartado 5: assertThrows para las entradas invalidas.
            .prueba("edadNegativaLanzaExcepcion",
                    () -> MiniTest.assertLanza(IllegalArgumentException.class,
                            () -> tarifa.calcular(-1, 12)))
            .prueba("antiguedadNegativaLanzaExcepcion",
                    () -> MiniTest.assertLanza(IllegalArgumentException.class,
                            () -> tarifa.calcular(30, -5)));
    }

    // =====================================================================
    // COMPROBACION (apartados 6 y 7)
    // =====================================================================

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2406-E09] Bateria de pruebas automatizadas con JUnit");
        System.out.println("  Version con JUnit 5 real (necesita el jar): " + VERSION_JUNIT);
        System.out.println("  Aqui se ejecuta la misma bateria sobre MiniTest, un armazon de");
        System.out.println("  pruebas sin dependencias incluido en este fichero.");

        Ej07CajaNegra.CalculadoraCuota calc = new Ej07CajaNegra.CalculadoraCuota();

        // Apartado 4: por que el delta.
        double producto = 40 * (1 - 0.25);
        System.out.println("  Apartado 4, por que el delta:");
        System.out.println("    40 * (1 - 0.25) = " + new java.math.BigDecimal(producto).toPlainString());
        System.out.println("    0.1 + 0.2       = " + new java.math.BigDecimal(0.1 + 0.2).toPlainString());
        System.out.println("    La segunda linea es el ejemplo clasico: en binario hay fracciones");
        System.out.println("    decimales periodicas, igual que un tercio lo es en decimal. Comparar");
        System.out.println("    con igualdad estricta produce fallos que parecen inexplicables.");
        System.out.println("    (Para dinero de verdad, lo correcto no es el delta: es BigDecimal o");
        System.out.println("    guardar centimos como entero. El delta sirve para probar.)");

        // Apartado 6, primera parte: la version correcta.
        System.out.println("  Apartado 6, bateria sobre la version CORRECTA:");
        Resultado correcta = bateria(calc::calcular).ejecutar(true, "    ");
        System.out.println("    " + correcta.informe());

        // Apartado 6, segunda parte: se introduce el error 0.25 -> 0.20.
        System.out.println("  Apartado 6, ahora con el error deliberado (0.25 pasa a 0.20):");
        Tarifa conError = (edad, ant) -> {
            if (edad < 0 || ant < 0) {
                throw new IllegalArgumentException("Parametro negativo");
            }
            double cuota = 40.00;
            if (edad < 26) {
                cuota = cuota * (1 - 0.20);      // <-- el error
            } else if (edad >= 65) {
                cuota = cuota * (1 - 0.30);
            }
            if (ant > 24) {
                cuota = cuota - 5.00;
            }
            return Math.max(cuota, 20.00);
        };
        Resultado rota = bateria(conError).ejecutar(false, "    ");
        System.out.println("    " + rota.informe());

        // Apartado 7: leer el informe.
        System.out.println("  Apartado 7, que dice el informe:");
        System.out.println("    Fallan " + rota.getFallos() + " de " + rota.getEjecutadas()
                + ", y las tres son de socios jovenes. Ninguna de las de");
        System.out.println("    socios estandar o senior falla, asi que el error esta acotado al");
        System.out.println("    descuento joven antes de mirar una sola linea de codigo.");
        System.out.println("    El mensaje expected 30.0 but was 32.0 dice ademas cuanto se ha");
        System.out.println("    aplicado: 32 es el 80 por ciento de 40, es decir, un descuento del");
        System.out.println("    20 en lugar del 25. El error queda localizado sin depurar.");
        System.out.println("    De ahi la importancia del apartado 2: un metodo llamado prueba7 no");
        System.out.println("    permitiria ninguna de estas dos deducciones.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
