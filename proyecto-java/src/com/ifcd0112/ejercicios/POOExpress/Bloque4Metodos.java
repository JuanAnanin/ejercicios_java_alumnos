package com.ifcd0112.ejercicios.POOExpress;

import java.util.Arrays;

/**
 * BLOQUE 4: Metodos y Funciones (10 ejercicios).
 *
 * Cada ejercicio define el metodo que pide el enunciado y {@link #ejecutar()} lo
 * invoca con datos de ejemplo, mostrando el valor devuelto.
 */
public class Bloque4Metodos {

    // ------------------------------------------------------------------
    // Ejercicio 1
    // Enunciado: Metodo estatico calcularAreaCirculo que reciba el radio (double)
    // y devuelva el area.
    // ------------------------------------------------------------------
    public static double calcularAreaCirculo(double radio) {
        // Area = PI * radio^2. Math.PI es la constante pi.
        return Math.PI * radio * radio;
    }

    // ------------------------------------------------------------------
    // Ejercicio 2
    // Enunciado: Metodo esPrimo que reciba un entero y devuelva boolean.
    // ------------------------------------------------------------------
    public static boolean esPrimo(int n) {
        if (n <= 1) return false;             // 0, 1 y negativos no son primos
        for (int i = 2; i * i <= n; i++) {    // divisores hasta la raiz
            if (n % i == 0) return false;     // encontramos divisor -> no primo
        }
        return true;
    }

    // ------------------------------------------------------------------
    // Ejercicio 3
    // Enunciado: Metodo SOBRECARGADO calcularArea. Una version recibe el lado de
    // un cuadrado y otra la base y la altura de un rectangulo.
    // ------------------------------------------------------------------
    // Sobrecarga 1: un parametro -> cuadrado.
    public static double calcularArea(double lado) {
        return lado * lado;
    }
    // Sobrecarga 2: dos parametros -> rectangulo. Java elige segun los argumentos.
    public static double calcularArea(double base, double altura) {
        return base * altura;
    }

    // ------------------------------------------------------------------
    // Ejercicio 4
    // Enunciado: Metodo invertirCadena que reciba un String y devuelva una nueva
    // cadena con los caracteres en orden inverso.
    // ------------------------------------------------------------------
    public static String invertirCadena(String texto) {
        StringBuilder sb = new StringBuilder();
        // Recorremos desde el ultimo caracter hasta el primero.
        for (int i = texto.length() - 1; i >= 0; i--) {
            sb.append(texto.charAt(i));
        }
        return sb.toString();
    }

    // ------------------------------------------------------------------
    // Ejercicio 5
    // Enunciado: Metodo calcularPromedio que reciba un array de double y devuelva
    // su promedio aritmetico.
    // ------------------------------------------------------------------
    public static double calcularPromedio(double[] numeros) {
        if (numeros.length == 0) return 0;
        double suma = 0;
        for (double n : numeros) { // for-each: recorre cada elemento
            suma += n;
        }
        return suma / numeros.length;
    }

    // ------------------------------------------------------------------
    // Ejercicio 6
    // Enunciado: Funcion recursiva para calcular el factorial de un entero.
    // ------------------------------------------------------------------
    public static long factorialRecursivo(int n) {
        if (n <= 1) return 1;                    // caso base: 0! = 1! = 1
        return n * factorialRecursivo(n - 1);    // caso recursivo: n * (n-1)!
    }

    // ------------------------------------------------------------------
    // Ejercicio 7
    // Enunciado: Metodo contarVocales que reciba un String y devuelva el numero
    // total de vocales.
    // ------------------------------------------------------------------
    public static int contarVocales(String texto) {
        int contador = 0;
        String vocales = "aeiouAEIOU";
        for (char c : texto.toCharArray()) {
            // indexOf devuelve -1 si el caracter no esta en 'vocales'.
            if (vocales.indexOf(c) != -1) contador++;
        }
        return contador;
    }

    // ------------------------------------------------------------------
    // Ejercicio 8
    // Enunciado: Metodo conversor de moneda que reciba la cantidad en euros y la
    // tasa de cambio, y devuelva la cantidad convertida.
    // ------------------------------------------------------------------
    public static double convertirMoneda(double euros, double tasaCambio) {
        return euros * tasaCambio;
    }

    // ------------------------------------------------------------------
    // Ejercicio 9
    // Enunciado: Metodo que reciba dos cadenas y devuelva un boolean indicando si
    // son anagramas (mismas letras en distinto orden).
    // ------------------------------------------------------------------
    public static boolean sonAnagramas(String a, String b) {
        // Normalizamos: quitamos espacios y pasamos a minusculas.
        char[] ca = a.replace(" ", "").toLowerCase().toCharArray();
        char[] cb = b.replace(" ", "").toLowerCase().toCharArray();
        if (ca.length != cb.length) return false; // distinto numero de letras
        // Si al ordenar ambas quedan iguales, son anagramas.
        Arrays.sort(ca);
        Arrays.sort(cb);
        return Arrays.equals(ca, cb);
    }

    // ------------------------------------------------------------------
    // Ejercicio 10
    // Enunciado: Metodo que acepte un numero variable de argumentos enteros
    // (varargs) y devuelva el valor maximo.
    // ------------------------------------------------------------------
    public static int maximo(int... numeros) {
        // 'int...' permite llamar al metodo con cualquier cantidad de enteros.
        int max = numeros[0];
        for (int n : numeros) {
            if (n > max) max = n;
        }
        return max;
    }

    /** Ejecuta los 10 ejercicios mostrando el valor devuelto por cada metodo. */
    public static void ejecutar() {
        System.out.println("==================================================");
        System.out.println(" BLOQUE 4: METODOS Y FUNCIONES");
        System.out.println("==================================================");

        System.out.println("[E01] calcularAreaCirculo(3.0) = " + calcularAreaCirculo(3.0));
        System.out.println("[E02] esPrimo(13) = " + esPrimo(13) + ", esPrimo(14) = " + esPrimo(14));
        System.out.println("[E03] calcularArea(cuadrado 5) = " + calcularArea(5.0)
                + " | calcularArea(rect 4x6) = " + calcularArea(4.0, 6.0));
        System.out.println("[E04] invertirCadena(\"Vanaheim\") = " + invertirCadena("Vanaheim"));
        System.out.println("[E05] calcularPromedio([7,8,9,10]) = "
                + calcularPromedio(new double[]{7, 8, 9, 10}));
        System.out.println("[E06] factorialRecursivo(5) = " + factorialRecursivo(5));
        System.out.println("[E07] contarVocales(\"Programacion\") = " + contarVocales("Programacion"));
        System.out.println("[E08] convertirMoneda(100 EUR, 1.08) = " + convertirMoneda(100, 1.08) + " USD");
        System.out.println("[E09] sonAnagramas(\"roma\",\"amor\") = " + sonAnagramas("roma", "amor")
                + " | sonAnagramas(\"casa\",\"saco\") = " + sonAnagramas("casa", "saco"));
        System.out.println("[E10] maximo(3, 9, 1, 7, 5) = " + maximo(3, 9, 1, 7, 5));
        System.out.println();
    }

    public static void main(String[] args) {
        ejecutar();
    }
}
