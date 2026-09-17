package com.ifcd0112.ejercicios.uf2404.bloque3_estructuras;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UF2404 - BLOQUE 3 - EJERCICIO 9: Analisis de texto con colecciones estandar.
 *
 * <p>Criterios de evaluacion: CE2.7, CE2.8</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej09AnalisisTexto {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Escribe un programa que analice un texto y genere estadisticas de
     * frecuencia de palabras, utilizando esta vez las clases de la biblioteca
     * estandar de Java en lugar de estructuras propias.
     *
     * Se pide:
     *   1. Lee un texto de varias lineas (puedes tenerlo en un array de String
     *      o leerlo de un fichero).
     *   2. Normaliza las palabras: pasalas a minusculas y elimina los signos de
     *      puntuacion.
     *   3. Utiliza un HashMap<String, Integer> para contar cuantas veces
     *      aparece cada palabra.
     *   4. Muestra las 5 palabras mas frecuentes, ordenadas de mayor a menor
     *      numero de apariciones.
     *   5. Muestra tambien cuantas palabras distintas contiene el texto y cual
     *      es la mas larga.
     *   6. Justifica en un comentario por que un HashMap resulta mas adecuado
     *      que un ArrayList para contar frecuencias.
     *
     * Pista: para ordenar el resultado por numero de apariciones puedes volcar
     * las entradas del mapa en una lista y ordenarla con un Comparator sobre el
     * valor.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * APARTADO 6: por que un HashMap y no un ArrayList.
     *
     * Contar frecuencias exige, por cada palabra del texto, responder a la
     * pregunta "ya la habia visto antes?" y, si la respuesta es si, incrementar
     * su contador.
     *
     * Con un ArrayList de pares palabra-contador habria que RECORRERLO entero
     * en cada palabra para localizarla: si el texto tiene n palabras y d
     * distintas, el coste total es del orden de n por d. Con un texto largo eso
     * se dispara.
     *
     * Un HashMap localiza la clave calculando su hash y saltando directamente
     * al hueco donde deberia estar: la busqueda es de coste constante en
     * promedio, independientemente de cuantas palabras distintas haya
     * almacenadas. El coste total pasa a ser del orden de n.
     *
     * Hay ademas una razon de diseno, y no solo de rendimiento: un mapa
     * expresa exactamente lo que el problema pide, una correspondencia entre
     * una palabra y su numero de apariciones. Con una lista habria que
     * inventarse una clase auxiliar para emparejar los dos datos y ademas
     * garantizar a mano que no se repiten claves, algo que el mapa hace solo.
     */

    /** Resultado del analisis de un texto. */
    public static class Estadisticas {

        private final Map<String, Integer> frecuencias;
        private final String palabraMasLarga;
        private final int totalPalabras;

        Estadisticas(Map<String, Integer> frecuencias, String palabraMasLarga, int totalPalabras) {
            this.frecuencias = frecuencias;
            this.palabraMasLarga = palabraMasLarga;
            this.totalPalabras = totalPalabras;
        }

        /** @return numero total de palabras del texto, contando repeticiones */
        public int getTotalPalabras() { return totalPalabras; }

        /** @return numero de palabras DISTINTAS del texto */
        public int getPalabrasDistintas() { return frecuencias.size(); }

        /** @return la palabra mas larga encontrada */
        public String getPalabraMasLarga() { return palabraMasLarga; }

        /**
         * Apartado 4: devuelve las palabras mas frecuentes, de mayor a menor.
         *
         * @param cuantas numero maximo de palabras a devolver
         * @return lista de entradas ya ordenada
         */
        public List<Map.Entry<String, Integer>> masFrecuentes(int cuantas) {
            // Un HashMap NO garantiza ningun orden, asi que para ordenar hay que
            // volcar sus entradas en una lista, que es lo que dice la pista.
            List<Map.Entry<String, Integer>> entradas = new ArrayList<>(frecuencias.entrySet());
            // Comparator sobre el VALOR y en orden descendente. Como criterio de
            // desempate se usa la palabra en orden alfabetico, para que el
            // resultado sea siempre el mismo y no dependa del orden interno del
            // mapa: sin ese desempate, dos palabras con la misma frecuencia
            // podrian salir en distinto orden en cada ejecucion.
            entradas.sort((a, b) -> {
                int porFrecuencia = b.getValue() - a.getValue();
                return (porFrecuencia != 0) ? porFrecuencia : a.getKey().compareTo(b.getKey());
            });
            return entradas.subList(0, Math.min(cuantas, entradas.size()));
        }
    }

    /**
     * Analiza un texto y devuelve sus estadisticas de frecuencia.
     *
     * @param lineas texto a analizar, una entrada por linea
     * @return las estadisticas calculadas
     */
    public static Estadisticas analizar(String[] lineas) {
        Map<String, Integer> frecuencias = new HashMap<>();
        String masLarga = "";
        int total = 0;

        for (String linea : lineas) {
            // Apartado 2: normalizacion. Primero a minusculas, para que "El" y
            // "el" cuenten como la misma palabra. Despues se sustituye por un
            // espacio todo lo que no sea una letra: asi "casa," y "casa" tambien
            // se unifican. Se conservan las vocales acentuadas y la enie porque
            // en castellano forman parte de la palabra.
            String limpia = linea.toLowerCase().replaceAll("[^a-zaeiouñáéíóúü]", " ");
            for (String palabra : limpia.split("\\s+")) {
                if (palabra.isEmpty()) {
                    continue;
                }
                total++;
                // Apartado 3: merge suma 1 si la clave existe e inserta 1 si no.
                // Equivale a un if-else de cuatro lineas, pero en una sola y sin
                // riesgo de olvidar el caso de la primera aparicion.
                frecuencias.merge(palabra, 1, Integer::sum);
                if (palabra.length() > masLarga.length()) {
                    masLarga = palabra;
                }
            }
        }
        return new Estadisticas(frecuencias, masLarga, total);
    }

    // =====================================================================
    // COMPROBACION
    // =====================================================================

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2404-E09] Analisis de texto con colecciones estandar");

        // Apartado 1: el texto, en un array de String. El enunciado admite
        // tambien leerlo de fichero; se usa un array para que el ejercicio se
        // ejecute sin depender de ningun recurso externo.
        String[] texto = {
            "La programacion orientada a objetos organiza el codigo en objetos.",
            "Cada objeto tiene estado y comportamiento, y el estado es privado.",
            "La encapsulacion protege el estado del objeto: el estado no se toca desde fuera.",
            "Un objeto colabora con otros objetos enviandoles mensajes."
        };

        Estadisticas est = analizar(texto);

        System.out.println("  Palabras totales: " + est.getTotalPalabras());
        System.out.println("  Palabras distintas: " + est.getPalabrasDistintas());   // apartado 5
        System.out.println("  Palabra mas larga: " + est.getPalabraMasLarga());       // apartado 5

        System.out.println("  Las 5 palabras mas frecuentes:");                       // apartado 4
        int puesto = 1;
        for (Map.Entry<String, Integer> e : est.masFrecuentes(5)) {
            System.out.printf("    %d. %-16s %d apariciones%n", puesto, e.getKey(), e.getValue());
            puesto++;
        }
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }

}
