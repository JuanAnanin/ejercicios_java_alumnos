package com.ifcd0112.ejercicios.POOExpress;
import java.util.Scanner;

/**
 * BLOQUE 1: Variables y Tipos de Datos (20 ejercicios).
 *
 * Cada ejercicio es un metodo estatico (e01..e20). El metodo {@link #ejecutar()}
 * los lanza en orden. La clase tiene su propio main para poder probar el bloque
 * de forma aislada con:  java Bloque1Variables.java
 *
 * Nota de diseno: los ejercicios que en el enunciado piden lectura por teclado
 * (Scanner) se resuelven con valores de ejemplo "quemados" para que el programa
 * pueda ejecutarse de principio a fin sin bloquearse pidiendo entradas. En cada
 * caso se deja, en comentario, la linea Scanner equivalente.
 */
public class Bloque1Variables {

    // ------------------------------------------------------------------
    // Ejercicio 1
    // Enunciado: Declara una variable de cada tipo de dato primitivo en Java
    // (byte, short, int, long, float, double, char, boolean), asignales un valor
    // valido y muestralas por consola.
    // ------------------------------------------------------------------
    public static void e01() {
        System.out.println("[E01] Declaracion de los 8 tipos primitivos de Java");
        // Cada primitivo tiene un rango y un proposito distinto.
        byte unByte = 100;                 // entero pequeno (-128..127)
        short unShort = 30000;             // entero corto
        int unInt = 1_000_000;             // entero estandar
        long unLong = 9_000_000_000L;      // entero grande (sufijo L)
        float unFloat = 3.14f;             // decimal de precision simple (sufijo f)
        double unDouble = 2.718281828;     // decimal de precision doble
        char unChar = 'A';                 // un unico caracter Unicode
        boolean unBoolean = true;          // verdadero/falso

        // Imprimimos cada valor con su tipo para verlo claramente.
        System.out.println("  byte    = " + unByte);
        System.out.println("  short   = " + unShort);
        System.out.println("  int     = " + unInt);
        System.out.println("  long    = " + unLong);
        System.out.println("  float   = " + unFloat);
        System.out.println("  double  = " + unDouble);
        System.out.println("  char    = " + unChar);
        System.out.println("  boolean = " + unBoolean);
        System.out.println();
    }

    // ------------------------------------------------------------------
    // Ejercicio 2
    // Enunciado: Declara dos variables enteras, calcula su suma, resta,
    // multiplicacion, division y modulo, e imprime cada resultado con un mensaje.
    // ------------------------------------------------------------------
    public static void e02() {
        System.out.println("[E02] Operaciones aritmeticas basicas con dos enteros");
        int a = 17, b = 5; // valores de ejemplo
        // La division entre enteros descarta los decimales (division entera).
        System.out.println("  a = " + a + ", b = " + b);
        System.out.println("  Suma           a + b = " + (a + b));
        System.out.println("  Resta          a - b = " + (a - b));
        System.out.println("  Multiplicacion a * b = " + (a * b));
        System.out.println("  Division       a / b = " + (a / b) + "  (division entera)");
        System.out.println("  Modulo (resto) a % b = " + (a % b));
        System.out.println();
    }

    // ------------------------------------------------------------------
    // Ejercicio 3
    // Enunciado: Calcula el area y el perimetro de un rectangulo a partir de su
    // base y altura (Area = base * altura). Usa tipos double.
    // ------------------------------------------------------------------
    public static void e03() {
        System.out.println("[E03] Area y perimetro de un rectangulo (double)");
        double base = 8.5, altura = 3.2;
        double area = base * altura;             // formula del area
        double perimetro = 2 * (base + altura);  // formula del perimetro
        System.out.println("  base = " + base + ", altura = " + altura);
        System.out.println("  Area      = " + area);
        System.out.println("  Perimetro = " + perimetro);
        System.out.println();
    }

    // ------------------------------------------------------------------
    // Ejercicio 4
    // Enunciado: Declara un double con el precio de un producto y una constante
    // IVA = 0.21. Calcula el precio final con IVA y muestralo formateado.
    // ------------------------------------------------------------------
    public static void e04() {
        System.out.println("[E04] Precio final aplicando IVA (constante)");
        final double IVA = 0.21;   // 'final' = constante, no puede reasignarse
        double precio = 49.99;
        double precioFinal = precio * (1 + IVA); // precio + 21%
        // printf con %.2f formatea a 2 decimales.
        System.out.printf("  Precio base: %.2f EUR%n", precio);
        System.out.printf("  Precio con IVA (21%%): %.2f EUR%n", precioFinal);
        System.out.println();
    }

    // ------------------------------------------------------------------
    // Ejercicio 5
    // Enunciado: Convierte una temperatura en grados Celsius a Fahrenheit
    // usando F = C * 9/5 + 32.
    // ------------------------------------------------------------------
    public static void e05() {
        System.out.println("[E05] Conversion de Celsius a Fahrenheit");
        double celsius = 37.0;
        // Importante usar 9.0/5.0 para forzar division en coma flotante.
        double fahrenheit = celsius * 9.0 / 5.0 + 32;
        System.out.printf("  %.1f C equivalen a %.1f F%n", celsius, fahrenheit);
        System.out.println();
    }

    // ------------------------------------------------------------------
    // Ejercicio 6
    // Enunciado: Intercambia los valores de dos variables enteras a y b usando
    // una variable auxiliar.
    // ------------------------------------------------------------------
    public static void e06() {
        System.out.println("[E06] Intercambio de dos variables CON variable auxiliar");
        int a = 1, b = 2;
        System.out.println("  Antes:  a = " + a + ", b = " + b);
        int aux = a; // guardamos a temporalmente
        a = b;       // a toma el valor de b
        b = aux;     // b toma el valor original de a
        System.out.println("  Despues: a = " + a + ", b = " + b);
        System.out.println();
    }

    // ------------------------------------------------------------------
    // Ejercicio 7
    // Enunciado: Intercambia los valores de dos enteros SIN variable auxiliar
    // (mediante operaciones matematicas).
    // ------------------------------------------------------------------
    public static void e07() {
        System.out.println("[E07] Intercambio de dos variables SIN variable auxiliar");
        int a = 7, b = 3;
        System.out.println("  Antes:  a = " + a + ", b = " + b);
        a = a + b; // a guarda la suma
        b = a - b; // b = (a+b) - b = a original
        a = a - b; // a = (a+b) - a original = b original
        System.out.println("  Despues: a = " + a + ", b = " + b);
        System.out.println();
    }

    // ------------------------------------------------------------------
    // Ejercicio 8
    // Enunciado: Declara un char con una letra minuscula y, mediante operaciones
    // aritmeticas sobre caracteres, conviertela a mayuscula.
    // ------------------------------------------------------------------
    public static void e08() {
        System.out.println("[E08] Pasar un caracter de minuscula a mayuscula con aritmetica");
        char minuscula = 'g';
        // En la tabla ASCII, una mayuscula esta 32 posiciones antes que su minuscula.
        char mayuscula = (char) (minuscula - 32);
        System.out.println("  '" + minuscula + "' -> '" + mayuscula + "'  (restando 32 al codigo ASCII)");
        System.out.println();
    }

    // ------------------------------------------------------------------
    // Ejercicio 9
    // Enunciado: Recibe una cantidad de dias e imprime su equivalente en anos,
    // meses y dias restantes (anos de 365 dias, meses de 30 dias).
    // ------------------------------------------------------------------
    public static void e09() {
        System.out.println("[E09] Descomponer dias en anos, meses y dias");
        int totalDias = 1000; // valor de ejemplo
        int anos = totalDias / 365;        // cuantos anos completos
        int resto = totalDias % 365;       // dias que sobran
        int meses = resto / 30;            // meses completos del resto
        int dias = resto % 30;             // dias finales
        System.out.println("  " + totalDias + " dias = " + anos + " anos, " + meses + " meses y " + dias + " dias");
        System.out.println();
    }

    // ------------------------------------------------------------------
    // Ejercicio 10
    // Enunciado: Calcula el promedio de cuatro notas en coma flotante, pero el
    // resultado final debe convertirse explicitamente a entero (casting).
    // ------------------------------------------------------------------
    public static void e10() {
        System.out.println("[E10] Promedio de 4 notas con casting a entero");
        double n1 = 7.5, n2 = 8.25, n3 = 6.0, n4 = 9.5;
        double promedio = (n1 + n2 + n3 + n4) / 4;
        int promedioEntero = (int) promedio; // (int) trunca los decimales
        System.out.println("  Promedio real   = " + promedio);
        System.out.println("  Promedio (int)  = " + promedioEntero + "  (se truncan los decimales)");
        System.out.println();
    }

    // ------------------------------------------------------------------
    // Ejercicio 11
    // Enunciado: Declara un numero de tres digitos. Mediante division y modulo,
    // descomponlo y muestra la suma de sus digitos.
    // ------------------------------------------------------------------
    public static void e11() {
        System.out.println("[E11] Suma de los digitos de un numero de 3 cifras");
        int numero = 472;
        int centenas = numero / 100;        // 4
        int decenas = (numero / 10) % 10;   // 7
        int unidades = numero % 10;         // 2
        int suma = centenas + decenas + unidades;
        System.out.println("  " + numero + " -> " + centenas + " + " + decenas + " + " + unidades + " = " + suma);
        System.out.println();
    }

    // ------------------------------------------------------------------
    // Ejercicio 12
    // Enunciado: Evalua si un ano es bisiesto. Guarda el resultado en un boolean
    // empleando expresiones logicas complejas.
    // ------------------------------------------------------------------
    public static void e12() {
        System.out.println("[E12] Determinar si un ano es bisiesto (boolean logico)");
        int ano = 2024;
        // Bisiesto: divisible por 4 y (no por 100, salvo que tambien por 400).
        boolean bisiesto = (ano % 4 == 0 && ano % 100 != 0) || (ano % 400 == 0);
        System.out.println("  El ano " + ano + " es bisiesto? " + bisiesto);
        System.out.println();
    }

    // ------------------------------------------------------------------
    // Ejercicio 13
    // Enunciado: Usa Scanner para leer nombre, edad y salario, y genera un
    // parrafo resumen con concatenacion de cadenas.
    // ------------------------------------------------------------------
    public static void e13(Scanner sc) {
        System.out.println("[E13] Resumen de usuario por concatenacion (datos simulados)");
        // Version Scanner (descomentar para entrada real):
        System.out.println("Introduce el nombre del usuario: ");
        String nombre = sc.nextLine();
        System.out.println("Introduce la edad del usuario: ");
        int edad = sc.nextInt();
        System.out.println("Introduce el salario del usuario: ");
        double salario = sc.nextDouble();
        //sc.close();
        // String nombre = "ifcd0112";
        // int edad = 34;
        // double salario = 1850.50;
        String resumen = nombre + " tiene " + edad + " anos y cobra " + salario + " EUR al mes.";
        System.out.println("  " + resumen);
        System.out.println();
    }

    // ------------------------------------------------------------------
    // Ejercicio 14
    // Enunciado: Declara un long con segundos transcurridos y conviertelo al
    // formato HH:MM:SS.
    // ------------------------------------------------------------------
    public static void e14() {
        System.out.println("[E14] Convertir segundos a formato HH:MM:SS");
        long totalSegundos = 11_525L; // ejemplo
        long horas = totalSegundos / 3600;
        long minutos = (totalSegundos % 3600) / 60;
        long segundos = totalSegundos % 60;
        // %02d rellena con ceros a la izquierda hasta 2 cifras.
        System.out.printf("  %d s = %02d:%02d:%02d%n", totalSegundos, horas, minutos, segundos);
        System.out.println();
    }

    // ------------------------------------------------------------------
    // Ejercicio 15
    // Enunciado: Calcula el interes simple generado por un capital a una tasa y
    // tiempo determinados (I = C * i * t).
    // ------------------------------------------------------------------
    public static void e15() {
        System.out.println("[E15] Interes simple I = C * i * t");
        double capital = 5000;  // C
        double tasa = 0.03;     // i (3%)
        double tiempo = 4;      // t (anos)
        double interes = capital * tasa * tiempo;
        System.out.printf("  Capital=%.2f, tasa=%.0f%%, tiempo=%.0f anos -> Interes=%.2f EUR%n",
                capital, tasa * 100, tiempo, interes);
        System.out.println();
    }

    // ------------------------------------------------------------------
    // Ejercicio 16
    // Enunciado: Calcula el IMC pidiendo peso (kg) y altura (m): IMC = peso / altura^2.
    // ------------------------------------------------------------------
    public static void e16(Scanner sc) {
        System.out.println("[E16] Indice de Masa Corporal (IMC)");
        //double peso = 72.0;    // kg (en real: sc.nextDouble())
        double peso = sc.nextDouble(); 
        double altura = 1.78;  // m
        double imc = peso / (altura * altura);
        System.out.printf("  Peso=%.1f kg, Altura=%.2f m -> IMC=%.2f%n", peso, altura, imc);
        System.out.println();
    }

    // ------------------------------------------------------------------
    // Ejercicio 17
    // Enunciado: Declara un String largo y usa length(), toUpperCase() y
    // substring() para extraer y transformar partes.
    // ------------------------------------------------------------------
    public static void e17() {
        System.out.println("[E17] Metodos de String: length, toUpperCase, substring");
        String texto = "La programacion en Java es potente";
        System.out.println("  Texto original  : " + texto);
        System.out.println("  Longitud        : " + texto.length());
        System.out.println("  En mayusculas   : " + texto.toUpperCase());
        // substring(inicio, fin) extrae [inicio, fin)
        System.out.println("  Substring (0,15): " + texto.substring(0, 15));
        System.out.println();
    }

    // ------------------------------------------------------------------
    // Ejercicio 18
    // Enunciado: Evalua la precedencia de operadores en x = 5 + 3 * 2 - 8 / 2.
    // Imprime el resultado y explica el orden con comentarios.
    // ------------------------------------------------------------------
    public static void e18() {
        System.out.println("[E18] Precedencia de operadores: 5 + 3 * 2 - 8 / 2");
        // Orden: primero * y / (de izq. a der.), luego + y -.
        //   3 * 2 = 6
        //   8 / 2 = 4
        //   5 + 6 - 4 = 7
        int x = 5 + 3 * 2 - 8 / 2;
        System.out.println("  Resultado x = " + x + "  (multiplicacion y division antes que suma y resta)");
        System.out.println();
    }

    // ------------------------------------------------------------------
    // Ejercicio 19
    // Enunciado: Lee un caracter y determina, imprimiendo un booleano, si es una
    // vocal (mayuscula o minuscula).
    // ------------------------------------------------------------------
    public static void e19(Scanner sc) {
        System.out.println("[E19] Comprobar si un caracter es vocal (boolean)");
        //char c = 'E'; // ejemplo (en real: sc.next().charAt(0))
        char c = sc.next().charAt(0); // lee un caracter del Scanner
        char min = Character.toLowerCase(c); // normalizamos a minuscula
        boolean esVocal = (min == 'a' || min == 'e' || min == 'i' || min == 'o' || min == 'u');
        System.out.println("  '" + c + "' es vocal? " + (esVocal ? " SI" : " NO"));
        System.out.println();
    }

    // ------------------------------------------------------------------
    // Ejercicio 20
    // Enunciado: Define el presupuesto total de un proyecto y calcula el coste de
    // backend (45%), frontend (35%) y QA (20%).
    // ------------------------------------------------------------------
    public static void e20() {
        System.out.println("[E20] Reparto del presupuesto de un proyecto software");
        double presupuesto = 24000.0;
        double backend = presupuesto * 0.45;
        double frontend = presupuesto * 0.35;
        double qa = presupuesto * 0.20;
        System.out.printf("  Presupuesto total: %.2f EUR%n", presupuesto);
        System.out.printf("  Backend  (45%%): %.2f EUR%n", backend);
        System.out.printf("  Frontend (35%%): %.2f EUR%n", frontend);
        System.out.printf("  QA       (20%%): %.2f EUR%n", qa);
        System.out.println();
    }

    /** Ejecuta secuencialmente los 20 ejercicios del bloque. */
    public static void ejecutar() {
        System.out.println("==================================================");
        System.out.println(" BLOQUE 1: VARIABLES Y TIPOS DE DATOS");
        System.out.println("==================================================");
        Scanner sc = new Scanner(System.in);
        e01(); e02(); e03(); e04(); e05();
        e06(); e07(); e08(); e09(); e10();
        e11(); e12(); e13(sc); e14(); e15();
        e16(sc); e17(); e18(); e19(sc); e20();
        System.out.println("FIN DEL BLOQUE 1: VARIABLES Y TIPOS DE DATOS");
        System.out.println();
        // sc.close(); // Descomentar sólo si no se va a usar el Scanner en otros bloques (bloque 8), para evitar cerrar System.in prematuramente.
    }

    /** Permite ejecutar el bloque de forma aislada: java Bloque1Variables.java */
    public static void main(String[] args) {
        ejecutar();
    }
}
