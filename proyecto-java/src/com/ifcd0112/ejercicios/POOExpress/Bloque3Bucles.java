package com.ifcd0112.ejercicios.POOExpress;

/**
 * BLOQUE 3: Estructuras de Control de Bucles (10 ejercicios).
 *
 * Los ejercicios interactivos (adivinar un numero, leer hasta un negativo) se
 * resuelven de forma AUTOMATIZADA con datos simulados para que el bloque se
 * ejecute completo sin pedir entradas. Se indica en comentario la version Scanner.
 */
public class Bloque3Bucles {

    // ------------------------------------------------------------------
    // Ejercicio 1
    // Enunciado: Imprime los numeros del 1 al 100 en orden ascendente con while.
    // ------------------------------------------------------------------
    public static void e01() {
        System.out.println("[E01] Numeros del 1 al 100 con while (10 por linea)");
        int i = 1;
        StringBuilder linea = new StringBuilder("  ");
        while (i <= 100) {           // condicion de continuacion
            linea.append(String.format("%4d", i));
            if (i % 10 == 0) {       // salto de linea cada 10 numeros
                System.out.println(linea);
                linea = new StringBuilder("  ");
            }
            i++;                     // incremento (clave para no hacer bucle infinito)
        }
    }

    // ------------------------------------------------------------------
    // Ejercicio 2
    // Enunciado: Imprime los pares entre 2 y 50 inclusive con un bucle for.
    // ------------------------------------------------------------------
    public static void e02() {
        System.out.println("[E02] Pares del 2 al 50 con for");
        StringBuilder sb = new StringBuilder("  ");
        // Empezamos en 2 y avanzamos de 2 en 2: asi solo recorremos pares.
        for (int i = 2; i <= 50; i += 2) {
            sb.append(i).append(" ");
        }
        System.out.println(sb.toString().trim());
    }

    // ------------------------------------------------------------------
    // Ejercicio 3
    // Enunciado: Calcula la suma de los primeros N enteros positivos (N por teclado).
    // ------------------------------------------------------------------
    public static void e03(int n) {
        System.out.println("[E03] Suma de los primeros N enteros -> N = " + n);
        int suma = 0;
        for (int i = 1; i <= n; i++) {
            suma += i; // acumulador
        }
        System.out.println("  1 + 2 + ... + " + n + " = " + suma);
    }

    // ------------------------------------------------------------------
    // Ejercicio 4
    // Enunciado: Muestra la tabla de multiplicar completa (1 al 10) de un numero.
    // ------------------------------------------------------------------
    public static void e04(int numero) {
        System.out.println("[E04] Tabla de multiplicar del " + numero);
        for (int i = 1; i <= 10; i++) {
            System.out.println("  " + numero + " x " + i + " = " + (numero * i));
        }
    }

    // ------------------------------------------------------------------
    // Ejercicio 5
    // Enunciado: Calcula el factorial de un entero positivo usando un bucle while.
    // ------------------------------------------------------------------
    public static void e05(int n) {
        System.out.println("[E05] Factorial de " + n + " con while");
        long factorial = 1; // usamos long porque el factorial crece muy rapido
        int i = 1;
        while (i <= n) {
            factorial *= i; // factorial = factorial * i
            i++;
        }
        System.out.println("  " + n + "! = " + factorial);
    }

    // ------------------------------------------------------------------
    // Ejercicio 6
    // Enunciado: Juego "Adivina el numero" con do-while: numero aleatorio 1-100,
    // se pide al usuario hasta acertar, indicando si el objetivo es mayor o menor.
    // ------------------------------------------------------------------
    public static void e06() {
        System.out.println("[E06] Adivina el numero (do-while, jugador automatico)");
        int secreto = 1 + (int) (Math.random() * 100); // numero objetivo 1..100
        // Jugador simulado con busqueda binaria. En real: int intento = sc.nextInt();
        int bajo = 1, alto = 100, intento, intentos = 0;
        do {
            intento = (bajo + alto) / 2; // estrategia: probar el punto medio
            intentos++;
            if (intento < secreto) {
                System.out.println("  Pruebo " + intento + " -> el numero es MAYOR");
                bajo = intento + 1;
            } else if (intento > secreto) {
                System.out.println("  Pruebo " + intento + " -> el numero es MENOR");
                alto = intento - 1;
            }
        } while (intento != secreto); // se repite hasta acertar
        System.out.println("  Acertado: " + secreto + " en " + intentos + " intentos");
    }

    // ------------------------------------------------------------------
    // Ejercicio 7
    // Enunciado: Muestra la secuencia de Fibonacci hasta el termino N.
    // ------------------------------------------------------------------
    public static void e07(int n) {
        System.out.println("[E07] Secuencia de Fibonacci hasta el termino " + n);
        long a = 0, b = 1;
        StringBuilder sb = new StringBuilder("  ");
        for (int i = 1; i <= n; i++) {
            sb.append(a).append(" "); // imprimimos el termino actual
            long siguiente = a + b;   // cada termino es la suma de los dos previos
            a = b;
            b = siguiente;
        }
        System.out.println(sb.toString().trim());
    }

    // ------------------------------------------------------------------
    // Ejercicio 8
    // Enunciado: Solicita enteros hasta que se introduzca un negativo. Al final
    // muestra la suma de todos los positivos introducidos.
    // ------------------------------------------------------------------
    public static void e08(int[] entradasSimuladas) {
        System.out.println("[E08] Sumar positivos hasta leer un negativo (datos simulados)");
        int suma = 0, indice = 0;
        // Version Scanner: while ((n = sc.nextInt()) >= 0) { suma += n; }
        while (indice < entradasSimuladas.length) {
            int n = entradasSimuladas[indice++];
            if (n < 0) {                       // condicion de parada
                System.out.println("  Leido " + n + " (negativo) -> fin");
                break;
            }
            System.out.println("  Leido " + n + " -> sumado");
            suma += n;
        }
        System.out.println("  Suma de positivos: " + suma);
    }

    // ------------------------------------------------------------------
    // Ejercicio 9
    // Enunciado: Con bucles anidados, dibuja un triangulo rectangulo de
    // asteriscos cuya altura indique el usuario.
    // ------------------------------------------------------------------
    public static void e09(int altura) {
        System.out.println("[E09] Triangulo rectangulo de asteriscos, altura = " + altura);
        for (int fila = 1; fila <= altura; fila++) {     // bucle externo: filas
            StringBuilder sb = new StringBuilder("  ");
            for (int col = 1; col <= fila; col++) {      // bucle interno: columnas
                sb.append("*");                          // tantos * como numero de fila
            }
            System.out.println(sb);
        }
    }

    // ------------------------------------------------------------------
    // Ejercicio 10
    // Enunciado: Verifica si un entero introducido es primo (solo divisible por
    // 1 y por si mismo).
    // ------------------------------------------------------------------
    public static void e10(int n) {
        System.out.println("[E10] Comprobar si " + n + " es primo");
        boolean esPrimo = n > 1; // 0 y 1 no son primos
        // Basta comprobar divisores hasta la raiz cuadrada de n.
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) { // si tiene algun divisor, no es primo
                esPrimo = false;
                break;        // no hace falta seguir buscando
            }
        }
        System.out.println("  " + n + " es primo? " + esPrimo);
    }

    /** Ejecuta los 10 ejercicios del bloque. */
    public static void ejecutar() {
        System.out.println("==================================================");
        System.out.println(" BLOQUE 3: ESTRUCTURAS DE CONTROL DE BUCLES");
        System.out.println("==================================================");
        e01();
        e02();
        e03(10);
        e04(7);
        e05(6);
        e06();
        e07(12);
        e08(new int[]{5, 12, 8, 3, -1, 99});
        e09(5);
        e10(29); e10(30);
        System.out.println();
    }

    public static void main(String[] args) {
        ejecutar();
    }
}
