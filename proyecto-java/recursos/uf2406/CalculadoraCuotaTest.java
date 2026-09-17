/*
 * UF2406 - BLOQUE 4 - EJERCICIO 9
 * Bateria de pruebas con JUnit 5 para el calculo de la cuota mensual.
 *
 * ESTE FICHERO NO FORMA PARTE DE LA COMPILACION DEL PROYECTO: necesita el jar
 * de JUnit 5, y el proyecto es de dependencias cero. Vive bajo recursos/ por
 * eso. La misma bateria, ejecutable sin dependencias, esta en
 * src/com/ifcd0112/ejercicios/uf2406/bloque4_pruebas/Ej09Junit.java
 *
 * Para ejecutarlo de verdad hace falta junit-jupiter en el classpath:
 *     mvn test          (con la dependencia junit-jupiter en el pom.xml)
 *     o desde el IDE, boton derecho -> Run tests
 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CalculadoraCuotaTest {

    private CalculadoraCuota calc;

    /*
     * Apartado 4: por que el delta.
     *
     * Los decimales se guardan en binario, y hay fracciones decimales que en
     * binario son periodicas, igual que un tercio lo es en decimal. Por eso
     * 0.1 + 0.2 no da exactamente 0.3. Comparar con igualdad estricta produce
     * fallos aparentemente inexplicables: la prueba dice que esperaba 30.0 y
     * obtuvo 30.0, y sin embargo falla, porque los ultimos bits no coinciden.
     *
     * (Para dinero de verdad lo correcto no es el delta, sino no usar double:
     * BigDecimal, o guardar centimos como entero. El delta sirve para probar.)
     */
    private static final double DELTA = 0.001;

    /** Apartado 3: preparacion comun, una sola vez. */
    @BeforeEach
    void inicializar() {
        calc = new CalculadoraCuota();
    }

    // ---- Clases de equivalencia y valores limite de la EDAD ----

    @Test
    void jovenSinAntiguedadPagaTreinta() {
        assertEquals(30.00, calc.calcular(20, 12), DELTA);
    }

    @Test
    void limiteSuperiorJovenVeinticincoAniosSigueSiendoJoven() {
        // Este es el caso que detecta el error de programar edad < 25.
        assertEquals(30.00, calc.calcular(25, 12), DELTA);
    }

    @Test
    void limiteInferiorEstandarVeintiseisAniosYaNoEsJoven() {
        assertEquals(40.00, calc.calcular(26, 12), DELTA);
    }

    @Test
    void edadCentralDeEstandarPagaLaCuotaBase() {
        assertEquals(40.00, calc.calcular(40, 12), DELTA);
    }

    @Test
    void limiteSuperiorEstandarSesentaYCuatroSinDescuento() {
        assertEquals(40.00, calc.calcular(64, 12), DELTA);
    }

    @Test
    void seniorConSesentaYCincoTieneTreintaPorCiento() {
        assertEquals(28.00, calc.calcular(65, 12), DELTA);
    }

    @Test
    void seniorDeSetentaMantieneElTreintaPorCiento() {
        assertEquals(28.00, calc.calcular(70, 12), DELTA);
    }

    // ---- Clases de equivalencia y valores limite de la ANTIGUEDAD ----

    @Test
    void veinticuatroMesesTodaviaNoDanDescuentoPorAntiguedad() {
        assertEquals(40.00, calc.calcular(40, 24), DELTA);
    }

    @Test
    void veinticincoMesesYaDescuentanCincoEurosAdicionales() {
        assertEquals(35.00, calc.calcular(40, 25), DELTA);
    }

    @Test
    void jovenConAntiguedadAcumulaLosDosDescuentos() {
        assertEquals(25.00, calc.calcular(20, 36), DELTA);
    }

    @Test
    void laCuotaNuncaBajaDelMinimoDeVeinteEuros() {
        // Nota honesta: con las reglas actuales esta comprobacion NUNCA puede
        // fallar, porque el minimo alcanzable es 23 EUR (40 * 0.70 - 5). Se
        // mantiene como red de seguridad para cuando se anadan mas descuentos,
        // pero conviene saber que hoy no verifica nada.
        assertTrue(calc.calcular(65, 60) >= 20.00);
    }

    // ---- Apartado 5: entradas invalidas ----

    @Test
    void edadNegativaLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> calc.calcular(-1, 12));
    }

    @Test
    void antiguedadNegativaLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> calc.calcular(30, -5));
    }
}
