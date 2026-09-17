package com.ifcd0112.ejercicios.uf2406.bloque5_documentacion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * UF2406 - BLOQUE 5 - EJERCICIO 13: Sesion de depuracion guiada.
 *
 * <p>Criterios de evaluacion: CE2.1, CE2.4</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej13Depuracion {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * El siguiente metodo deberia devolver la media de las cuotas pagadas por
     * los socios activos, pero produce resultados incorrectos en determinados
     * casos. Localiza el error utilizando exclusivamente las herramientas de
     * depuracion del entorno, sin modificar el codigo a base de mensajes por
     * consola.
     *
     *      public double mediaCuotasActivos(List<Socio> socios) {
     *          double suma = 0;
     *          int contador = 0;
     *          for (Socio s : socios) {
     *              if (s.estaActivo()) {
     *                  suma += s.getCuota();
     *              }
     *              contador++;
     *          }
     *          return suma / contador;
     *      }
     *
     * Se pide:
     *   1. Prepara un conjunto de datos de prueba con socios activos e inactivos
     *      mezclados, y calcula a mano cual deberia ser el resultado correcto.
     *   2. Establece un punto de parada en el interior del bucle y ejecuta el
     *      metodo paso a paso, monitorizando la evolucion de las variables suma
     *      y contador.
     *   3. Identifica exactamente en que instruccion se produce el error logico
     *      y explica por que el resultado es incorrecto.
     *   4. Establece ahora un punto de parada condicional que se active
     *      unicamente cuando se procese un socio inactivo, y explica que ventaja
     *      aporta frente al punto de parada normal.
     *   5. Corrige el error y comprueba con tus datos de prueba que el resultado
     *      coincide con el calculado a mano.
     *   6. Identifica un segundo problema latente del metodo (que ocurre si la
     *      lista esta vacia o si no hay ningun socio activo?) y corrigelo
     *      tambien.
     *
     * Pista: presta atencion a la posicion exacta de la instruccion contador++
     * respecto a la condicion del if: ese detalle es precisamente el origen del
     * error.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * APARTADO 3: donde esta el error y por que.
     *
     * La instruccion contador++ esta FUERA del bloque if. Se ejecuta por cada
     * socio de la lista, este activo o no. En cambio, suma solo acumula las
     * cuotas de los activos.
     *
     * El resultado es una division con el numerador correcto y el denominador
     * equivocado: se reparte la suma de los activos entre TODOS los socios. La
     * media sale siempre por debajo de la real, y tanto mas cuanto mas socios
     * inactivos haya.
     *
     * Lo peligroso de este fallo es que NO SE NOTA. No lanza excepcion, no da un
     * numero absurdo y con una lista de solo socios activos da el resultado
     * correcto, que es probablemente como se probo. Solo falla cuando hay
     * inactivos mezclados, es decir, en produccion.
     *
     * APARTADO 4: la ventaja del punto de parada condicional.
     *
     * Un punto de parada normal dentro del bucle se activa en CADA iteracion.
     * Con la lista de tres socios del apartado 1 eso es asumible; con los 500
     * socios reales del gimnasio, hay que pulsar continuar 500 veces, y el fallo
     * se busca precisamente entre esas iteraciones.
     *
     * Un punto de parada con la condicion !s.estaActivo() se detiene UNICAMENTE
     * en los socios inactivos, que son los sospechosos. En la primera parada ya
     * se ve lo que hace falta ver: suma no ha cambiado y contador si.
     *
     * Es la diferencia entre buscar y encontrar. Y el mismo razonamiento vale
     * para el punto de parada por excepcion o el que solo se activa a partir de
     * la iteracion N.
     *
     * APARTADO 6: el segundo problema, el latente.
     *
     * Con la LISTA VACIA, contador vale 0 y el metodo hace 0.0 / 0. En Java, con
     * double, eso NO lanza excepcion: devuelve NaN, que significa "no es un
     * numero". Y NaN se propaga en silencio: sumarlo a otra cosa da NaN,
     * compararlo con cualquier valor da false (incluso NaN == NaN es false), y
     * acaba apareciendo en un informe como "NaN EUR" tres pantallas mas alla,
     * sin ninguna pista de donde salio.
     *
     * Conviene fijarse en que el caso "hay socios pero ninguno activo" se
     * comporta DISTINTO en la version con el error, y ademas peor. Ahi el
     * contador equivocado no vale 0, sino el numero total de socios, asi que la
     * division es 0.0 entre 2 y devuelve 0.0. No hay NaN que delate nada:
     * simplemente sale una media de cero euros, que parece un dato y acaba
     * impresa en un informe como si lo fuera. Es un buen recordatorio de que un
     * error no siempre se manifiesta como un valor absurdo.
     *
     * Conviene comparar los dos casos en clase:
     *      suma / contador  con double y contador 0  ->  NaN, en silencio
     *      suma / contador  con int    y contador 0  ->  ArithmeticException
     * La version que lanza la excepcion es MENOS comoda y MUCHO mejor: falla
     * donde esta el problema, no tres pantallas mas alla.
     *
     * La correccion distingue dos situaciones que no son la misma:
     *   - Lista nula o vacia: es un error de quien llama. IllegalArgumentException.
     *   - Lista con socios pero ninguno activo: no es un error, es un estado
     *     legitimo del gimnasio, y la media simplemente no existe. Lanzar
     *     IllegalStateException es una opcion; devolver un OptionalDouble vacio
     *     es probablemente mejor, porque obliga a quien llama a decidir que
     *     hacer. Se implementan las dos para poder comentarlo.
     */

    /** Socio del gimnasio, reducido a lo que este ejercicio necesita. */
    public static class Socio {

        private final String nombre;
        private final boolean activo;
        private final double cuota;

        /**
         * @param nombre nombre del socio
         * @param activo true si su alta esta en vigor
         * @param cuota  cuota mensual que paga
         */
        public Socio(String nombre, boolean activo, double cuota) {
            this.nombre = nombre;
            this.activo = activo;
            this.cuota = cuota;
        }

        /** @return nombre del socio */
        public String getNombre() { return nombre; }

        /** @return true si el socio esta activo */
        public boolean estaActivo() { return activo; }

        /** @return cuota mensual */
        public double getCuota() { return cuota; }
    }

    /**
     * VERSION ORIGINAL, con el error. Se conserva para poder compararla.
     *
     * @param socios lista de socios
     * @return media mal calculada
     */
    public static double mediaCuotasActivosConError(List<Socio> socios) {
        double suma = 0;
        int contador = 0;
        for (Socio s : socios) {
            if (s.estaActivo()) {
                suma += s.getCuota();
            }
            contador++;              // <-- FUERA del if: aqui esta el error
        }
        return suma / contador;
    }

    /**
     * Apartados 5 y 6: version corregida.
     *
     * @param socios lista de socios
     * @return media de las cuotas de los socios activos
     * @throws IllegalArgumentException si la lista es nula o esta vacia
     * @throws IllegalStateException    si no hay ningun socio activo
     */
    public static double mediaCuotasActivos(List<Socio> socios) {
        // Apartado 6, primera parte: la lista vacia es un error de quien llama.
        if (socios == null || socios.isEmpty()) {
            throw new IllegalArgumentException("La lista de socios esta vacia");
        }
        double suma = 0;
        int contador = 0;
        for (Socio s : socios) {
            if (s.estaActivo()) {
                suma += s.getCuota();
                contador++;          // DENTRO del if: solo cuenta lo que suma
            }
        }
        // Apartado 6, segunda parte: sin activos, la media no existe. Devolver
        // NaN en silencio seria lo peor de todo.
        if (contador == 0) {
            throw new IllegalStateException("No hay ningun socio activo");
        }
        return suma / contador;
    }

    /**
     * Alternativa al apartado 6 que probablemente es mejor diseno.
     *
     * <p>Devolver un OptionalDouble vacio no trata la ausencia de activos como
     * un error, porque no lo es, y ademas obliga a quien llama a decidir que
     * hacer con ese caso. Con una excepcion se puede olvidar capturarla; con un
     * Optional, el compilador no deja ignorarlo sin darse cuenta.</p>
     *
     * @param socios lista de socios
     * @return la media, o vacio si no hay socios activos
     */
    public static java.util.OptionalDouble mediaCuotasActivosOpcional(List<Socio> socios) {
        if (socios == null) {
            return java.util.OptionalDouble.empty();
        }
        return socios.stream()
                .filter(Socio::estaActivo)
                .mapToDouble(Socio::getCuota)
                .average();
    }

    // =====================================================================
    // COMPROBACION
    // =====================================================================

    /**
     * Apartado 1: datos de prueba con activos e inactivos mezclados.
     *
     * @return los tres socios del enunciado
     */
    public static List<Socio> datosDePrueba() {
        return new ArrayList<>(Arrays.asList(
                new Socio("Socio A", true,  40.0),
                new Socio("Socio B", false, 30.0),
                new Socio("Socio C", true,  50.0)));
    }

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2406-E13] Sesion de depuracion guiada");

        List<Socio> socios = datosDePrueba();
        System.out.println("  Apartado 1, datos de prueba:");
        for (Socio s : socios) {
            System.out.printf("    %-8s %-9s cuota %.2f%n", s.getNombre(),
                    s.estaActivo() ? "activo" : "INACTIVO", s.getCuota());
        }
        System.out.println("    Resultado correcto calculado a mano: (40 + 50) / 2 = 45,00");

        // Apartado 2: la traza que se veria en el depurador, reproducida.
        System.out.println("  Apartado 2, traza del bucle en la version con el error:");
        double suma = 0;
        int contador = 0;
        for (Socio s : socios) {
            if (s.estaActivo()) {
                suma += s.getCuota();
            }
            contador++;
            System.out.printf("    tras %-8s (%-8s) suma=%5.1f contador=%d   %s%n",
                    s.getNombre(), s.estaActivo() ? "activo" : "INACTIVO", suma, contador,
                    s.estaActivo() ? "" : "<-- AQUI: contador sube y suma no");
        }

        System.out.printf("  Apartado 3: resultado del metodo erroneo = %.2f  (90 / 3, no 90 / 2)%n",
                mediaCuotasActivosConError(socios));
        System.out.println("    contador++ esta FUERA del if: cuenta a todos los socios, mientras");
        System.out.println("    que suma solo acumula los activos. Numerador correcto, denominador");
        System.out.println("    equivocado. La media sale siempre por debajo de la real.");

        // Apartado 5
        double corregido = mediaCuotasActivos(socios);
        System.out.printf("  Apartado 5: resultado del metodo corregido = %.2f%n", corregido);
        System.out.println("    Coincide con el calculado a mano: "
                + (Math.abs(corregido - 45.0) < 0.001 ? "SI" : "NO"));

        // Apartado 6
        System.out.println("  Apartado 6, el segundo problema, el latente:");
        List<Socio> soloInactivos = new ArrayList<>(Arrays.asList(
                new Socio("Socio D", false, 30.0),
                new Socio("Socio E", false, 35.0)));
        System.out.println("    Version con error, lista SIN NINGUN ACTIVO -> "
                + mediaCuotasActivosConError(soloInactivos));
        System.out.println("      Ojo: no sale NaN, sale 0.0, porque el contador erroneo vale 2 y");
        System.out.println("      no 0. Y eso es todavia peor: NaN al menos se ve raro; un 0,00 EUR");
        System.out.println("      parece un dato y acaba impreso en un informe como si lo fuera.");
        System.out.println("    Version con error, lista VACIA -> "
                + mediaCuotasActivosConError(new ArrayList<>()));
        System.out.println("      Aqui si sale NaN, y en silencio: no lanza nada, se propaga a todo");
        System.out.println("      lo que se le sume y aparece tres pantallas mas alla sin rastro.");
        try {
            mediaCuotasActivos(soloInactivos);
            System.out.println("    Version corregida -> ERROR: deberia haber lanzado excepcion");
        } catch (IllegalStateException e) {
            System.out.println("    Version corregida, sin activos -> " + e.getClass().getSimpleName()
                    + ": " + e.getMessage());
        }
        try {
            mediaCuotasActivos(new ArrayList<>());
            System.out.println("    Version corregida, lista vacia -> ERROR: deberia haber lanzado");
        } catch (IllegalArgumentException e) {
            System.out.println("    Version corregida, lista vacia -> " + e.getClass().getSimpleName()
                    + ": " + e.getMessage());
        }
        System.out.println("    Alternativa con OptionalDouble, sin activos -> "
                + mediaCuotasActivosOpcional(soloInactivos));
        System.out.println("      Probablemente mejor diseno: no tratar como error algo que no lo es,");
        System.out.println("      y obligar a quien llama a decidir que hace con ese caso.");

        System.out.println("  Apartado 4: el punto de parada condicional !s.estaActivo() se detiene");
        System.out.println("    SOLO en los sospechosos. Con 3 socios da igual; con los 500 del");
        System.out.println("    gimnasio, es la diferencia entre buscar y encontrar.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
