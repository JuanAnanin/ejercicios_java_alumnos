package com.ifcd0112.ejercicios.uf2406.bloque4_pruebas;

/**
 * UF2406 - BLOQUE 4 - EJERCICIO 8: Pruebas estructurales y complejidad ciclomatica.
 *
 * <p>Criterios de evaluacion: CE2.3</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej08CajaBlanca {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Se te entrega el siguiente metodo ya implementado. Debes analizarlo desde
     * el punto de vista de las pruebas de caja blanca.
     *
     *      public String clasificarSocio(int edad, int antiguedadMeses, boolean cuotaAlDia) {
     *          if (!cuotaAlDia) {
     *              return "MOROSO";
     *          }
     *          if (antiguedadMeses > 60) {
     *              if (edad >= 65) {
     *                  return "VETERANO_SENIOR";
     *              }
     *              return "VETERANO";
     *          }
     *          if (edad < 26) {
     *              return "JOVEN";
     *          }
     *          return "ESTANDAR";
     *      }
     *
     * Se pide:
     *   1. Dibuja el grafo de flujo del metodo, con un nodo por cada decision y
     *      arista por cada camino posible.
     *   2. Calcula su complejidad ciclomatica e indica cuantos casos de prueba
     *      se necesitan como minimo para cubrir todos los caminos
     *      independientes.
     *   3. Disena un caso de prueba por cada camino, indicando los valores de
     *      entrada y el resultado esperado.
     *   4. Compara esta bateria con la que disenarias mediante caja negra a
     *      partir unicamente del enunciado funcional: coinciden? Detecta alguna
     *      de las dos algo que la otra pase por alto?
     *   5. Propon una refactorizacion del metodo que reduzca su complejidad
     *      ciclomatica sin alterar su comportamiento externo, y comprueba con
     *      tus casos de prueba que el resultado sigue siendo identico.
     *
     * Pista: la complejidad ciclomatica puede calcularse contando el numero de
     * decisiones (if, while, for, case, operadores logicos) y sumandole uno.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * APARTADOS 1 Y 2: grafo de flujo y complejidad.
     *
     *          inicio
     *            │
     *      ┌─ ¿!cuotaAlDia? ─Si──> return MOROSO              (camino 1)
     *      No
     *      │
     *      ├─ ¿antiguedad>60? ─Si─┬─ ¿edad>=65? ─Si─> VETERANO_SENIOR  (camino 2)
     *      No                     └─ No ──────────> VETERANO          (camino 3)
     *      │
     *      ├─ ¿edad<26? ─Si──────────────────────> JOVEN              (camino 4)
     *      No
     *      └────────────────────────────────────> ESTANDAR            (camino 5)
     *
     *      Decisiones (if): 4    ->    Complejidad ciclomatica = 4 + 1 = 5
     *      Casos de prueba minimos para cubrir todos los caminos: 5
     *
     * La formula de la pista, V(G) = decisiones + 1, es la version practica. La
     * definicion formal es V(G) = aristas - nodos + 2, y da lo mismo. Lo que
     * conviene no olvidar es que los operadores logicos && y || TAMBIEN cuentan
     * como decision, porque generan una bifurcacion aunque esten escritos en la
     * misma linea. Un if con tres condiciones encadenadas por && suma tres, no
     * uno.
     *
     * APARTADO 4: caja blanca frente a caja negra.
     *
     * Las dos baterias coinciden en gran parte, pero cada una ve algo que la
     * otra no:
     *
     *   Lo que ve la CAJA NEGRA y la blanca no exige: los valores frontera. Para
     *   cubrir el camino 4 basta con cualquier edad menor de 26, asi que la caja
     *   blanca se conforma con 22. El error de programar edad < 25 en lugar de
     *   edad < 26 pasaria desapercibido, porque el camino queda cubierto igual.
     *
     *   Lo que ve la CAJA BLANCA y la negra no puede ver: que ninguna rama del
     *   codigo se queda sin ejecutar. Si el metodo tuviera una rama que el
     *   enunciado no menciona (por ejemplo, un caso especial para socios
     *   fundadores que alguien anadio y no documento), la caja negra no la
     *   probaria nunca, porque no sabe que existe.
     *
     * Por eso son complementarias y no alternativas. La caja negra comprueba que
     * el programa hace lo que se pidio; la caja blanca, que todo lo que el
     * programa hace se ha probado. El ejercicio 7 y este juntos son un buen
     * ejemplo: alli se descubrio que el suelo de 20 EUR es inalcanzable, y eso
     * es un hallazgo de caja blanca hecho sobre una especificacion de caja negra.
     *
     * APARTADO 5: la refactorizacion.
     *
     * La complejidad total del problema no desaparece, se reparte. El metodo
     * principal baja de 5 a 3 y cada metodo auxiliar queda en 2. Lo importante
     * no es el numero, sino que cada trozo se puede probar por separado y que el
     * metodo principal se lee casi como el enunciado del requisito.
     */

    /** Ruta del documento con el grafo y la tabla de caminos. */
    public static final String DOCUMENTO = "recursos/uf2406/04_diseno_pruebas.md";

    /**
     * Version ORIGINAL, tal y como se entrega. Complejidad ciclomatica 5.
     *
     * @param edad            edad del socio
     * @param antiguedadMeses meses de antiguedad
     * @param cuotaAlDia      true si esta al corriente de pago
     * @return categoria del socio
     */
    public static String clasificarSocio(int edad, int antiguedadMeses, boolean cuotaAlDia) {
        if (!cuotaAlDia) {
            return "MOROSO";
        }
        if (antiguedadMeses > 60) {
            if (edad >= 65) {
                return "VETERANO_SENIOR";
            }
            return "VETERANO";
        }
        if (edad < 26) {
            return "JOVEN";
        }
        return "ESTANDAR";
    }

    // ---------------------------------------------------------------------
    // Apartado 5: version refactorizada
    // ---------------------------------------------------------------------

    /**
     * Version REFACTORIZADA. Complejidad 3 en este metodo y 2 en cada auxiliar.
     *
     * @param edad            edad del socio
     * @param antiguedadMeses meses de antiguedad
     * @param cuotaAlDia      true si esta al corriente de pago
     * @return categoria del socio, identica a la de la version original
     */
    public static String clasificarSocioRefactorizado(int edad, int antiguedadMeses,
                                                      boolean cuotaAlDia) {
        if (!cuotaAlDia) {
            return "MOROSO";
        }
        if (esVeterano(antiguedadMeses)) {
            return clasificarVeterano(edad);
        }
        return clasificarPorEdad(edad);
    }

    /**
     * @param meses meses de antiguedad
     * @return true si supera los cinco anos
     */
    private static boolean esVeterano(int meses) {
        return meses > 60;
    }

    /**
     * @param edad edad del socio
     * @return categoria dentro de los veteranos
     */
    private static String clasificarVeterano(int edad) {
        return (edad >= 65) ? "VETERANO_SENIOR" : "VETERANO";
    }

    /**
     * @param edad edad del socio
     * @return categoria por edad para los no veteranos
     */
    private static String clasificarPorEdad(int edad) {
        return (edad < 26) ? "JOVEN" : "ESTANDAR";
    }

    // =====================================================================
    // COMPROBACION (apartados 3 y 5)
    // =====================================================================

    /** Caso de prueba de caja blanca: cubre un camino concreto del grafo. */
    private static final Object[][] CAMINOS = {
        { 1, 30, 12, false, "MOROSO"          },
        { 2, 70, 72, true,  "VETERANO_SENIOR" },
        { 3, 40, 72, true,  "VETERANO"        },
        { 4, 22, 12, true,  "JOVEN"           },
        { 5, 40, 12, true,  "ESTANDAR"        }
    };

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2406-E08] Pruebas estructurales y complejidad ciclomatica");
        System.out.println("  Grafo de flujo y tabla de caminos: " + DOCUMENTO);
        System.out.println("  Apartados 1 y 2: 4 decisiones -> complejidad ciclomatica 5.");
        System.out.println("    Hacen falta 5 casos de prueba para cubrir los caminos independientes.");

        // Apartado 3: un caso por camino, ejecutado contra las dos versiones.
        System.out.println("  Apartado 3, un caso por camino (y apartado 5 en la misma tabla):");
        System.out.println("    CP  edad ant  alDia  esperado          original        refactorizado");
        int fallos = 0;
        for (Object[] c : CAMINOS) {
            int edad = (Integer) c[1];
            int ant = (Integer) c[2];
            boolean alDia = (Boolean) c[3];
            String esperado = (String) c[4];
            String original = clasificarSocio(edad, ant, alDia);
            String refactor = clasificarSocioRefactorizado(edad, ant, alDia);
            boolean ok = esperado.equals(original) && esperado.equals(refactor);
            if (!ok) {
                fallos++;
            }
            System.out.printf("     %d  %4d %4d  %-5s  %-16s %-15s %-15s %s%n",
                    c[0], edad, ant, alDia, esperado, original, refactor, ok ? "OK" : "FALLA");
        }
        System.out.println("    Fallos: " + fallos + " de 5");

        // Apartado 5: equivalencia demostrada por fuerza bruta, no por confianza.
        System.out.println("  Apartado 5, equivalencia de las dos versiones comprobada sobre TODO");
        System.out.println("    el espacio de entradas (edad 0-120, antiguedad 0-240, los dos");
        System.out.println("    valores de cuotaAlDia): " + (121 * 241 * 2) + " combinaciones.");
        int diferencias = 0;
        for (int edad = 0; edad <= 120; edad++) {
            for (int ant = 0; ant <= 240; ant++) {
                for (boolean alDia : new boolean[] { true, false }) {
                    if (!clasificarSocio(edad, ant, alDia)
                            .equals(clasificarSocioRefactorizado(edad, ant, alDia))) {
                        diferencias++;
                    }
                }
            }
        }
        System.out.println("    Combinaciones en las que las dos versiones difieren: " + diferencias);
        System.out.println("    Con cinco casos de prueba se cubren los caminos; con la comprobacion");
        System.out.println("    exhaustiva se demuestra la equivalencia. Aqui se puede hacer porque");
        System.out.println("    el espacio de entradas es pequeno y discreto: no siempre se podra.");

        System.out.println("  Apartado 4: las dos tecnicas son complementarias. La caja blanca no");
        System.out.println("    exige valores frontera (para el camino JOVEN le vale cualquier edad");
        System.out.println("    menor de 26, asi que no detectaria el error <25 del ejercicio 7). La");
        System.out.println("    caja negra no puede probar una rama que el enunciado no menciona,");
        System.out.println("    porque no sabe que existe.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
