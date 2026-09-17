package com.ifcd0112.ejercicios.POOExpress;

/**
 * BLOQUE 2: Estructuras de Control Condicionales (10 ejercicios).
 *
 * Muchos ejercicios reciben los datos como parametro del metodo en lugar de
 * leerlos por teclado, asi se pueden invocar varias veces con valores distintos
 * y demostrar todas las ramas del condicional sin bloquear el programa.
 */
public class Bloque2Condicionales {

    // ------------------------------------------------------------------
    // Ejercicio 1
    // Enunciado: Lee un entero e indica si es par o impar.
    // ------------------------------------------------------------------
    public static void e01(int n) {
        System.out.println("[E01] Par o impar -> n = " + n);
        // Un numero es par si el resto de dividirlo entre 2 es 0.
        if (n % 2 == 0) {
            System.out.println("  " + n + " es PAR");
        } else {
            System.out.println("  " + n + " es IMPAR");
        }
    }

    // ------------------------------------------------------------------
    // Ejercicio 2
    // Enunciado: Evaluador de notas (0-10): Insuficiente (<5), Suficiente (5-6.9),
    // Notable (7-8.9) o Sobresaliente (9-10).
    // ------------------------------------------------------------------
    public static void e02(double nota) {
        System.out.println("[E02] Evaluador de notas -> nota = " + nota);
        String calificacion;
        // if-else encadenado: cada rama excluye a las anteriores.
        if (nota < 5) {
            calificacion = "Insuficiente";
        } else if (nota < 7) {        // ya sabemos que es >= 5
            calificacion = "Suficiente";
        } else if (nota < 9) {        // ya sabemos que es >= 7
            calificacion = "Notable";
        } else {
            calificacion = "Sobresaliente";
        }
        System.out.println("  Calificacion: " + calificacion);
    }

    // ------------------------------------------------------------------
    // Ejercicio 3
    // Enunciado: Solicita tres enteros e identifica el mayor, controlando el caso
    // de que sean iguales.
    // ------------------------------------------------------------------
    public static void e03(int a, int b, int c) {
        System.out.println("[E03] Mayor de tres enteros -> " + a + ", " + b + ", " + c);
        if (a == b && b == c) {
            System.out.println("  Los tres numeros son iguales");
        } else {
            int mayor = a;                  // suponemos que a es el mayor
            if (b > mayor) mayor = b;       // corregimos si b lo supera
            if (c > mayor) mayor = c;       // corregimos si c lo supera
            System.out.println("  El mayor es: " + mayor);
        }
    }

    // ------------------------------------------------------------------
    // Ejercicio 4
    // Enunciado: Calculadora basica con switch. Pide dos numeros y un operador
    // (+, -, *, /) en formato caracter.
    // ------------------------------------------------------------------
    public static void e04(double a, double b, char op) {
        System.out.println("[E04] Calculadora con switch -> " + a + " " + op + " " + b);
        double resultado;
        boolean valido = true;
        // switch sobre el caracter operador; cada case maneja una operacion.
        switch (op) {
            case '+': resultado = a + b; break;
            case '-': resultado = a - b; break;
            case '*': resultado = a * b; break;
            case '/':
                if (b == 0) { // controlamos la division por cero
                    System.out.println("  Error: division por cero");
                    return;
                }
                resultado = a / b;
                break;
            default:
                System.out.println("  Operador no valido");
                valido = false;
                resultado = 0;
        }
        if (valido) System.out.println("  Resultado: " + resultado);
    }

    // ------------------------------------------------------------------
    // Ejercicio 5
    // Enunciado: Tienda que aplica 15% de descuento si la compra supera 100 EUR,
    // y 5% si esta entre 50 y 100 EUR.
    // ------------------------------------------------------------------
    public static void e05(double importe) {
        System.out.println("[E05] Descuento de tienda -> importe = " + importe + " EUR");
        double descuento;
        if (importe > 100) {
            descuento = 0.15;
        } else if (importe >= 50) {  // entre 50 y 100
            descuento = 0.05;
        } else {
            descuento = 0.0;
        }
        double total = importe * (1 - descuento);
        System.out.printf("  Descuento aplicado: %.0f%% -> Total a pagar: %.2f EUR%n", descuento * 100, total);
    }

    // ------------------------------------------------------------------
    // Ejercicio 6
    // Enunciado: Control de acceso que verifica si una persona puede entrar segun
    // su edad y si tiene invitacion (booleanos).
    // ------------------------------------------------------------------
    public static void e06(int edad, boolean tieneInvitacion) {
        System.out.println("[E06] Control de acceso -> edad=" + edad + ", invitacion=" + tieneInvitacion);
        // Debe ser mayor de edad Y tener invitacion.
        boolean puedeEntrar = (edad >= 18) && tieneInvitacion;
        if (puedeEntrar) {
            System.out.println("  Acceso PERMITIDO");
        } else {
            System.out.println("  Acceso DENEGADO" +
                    (edad < 18 ? " (menor de edad)" : " (sin invitacion)"));
        }
    }

    // ------------------------------------------------------------------
    // Ejercicio 7
    // Enunciado: Menu interactivo con switch para un cajero automatico
    // (Ver saldo, Depositar, Retirar, Salir).
    // ------------------------------------------------------------------
    public static void e07(int opcion) {
        System.out.println("[E07] Menu de cajero (switch) -> opcion = " + opcion);
        // En una app real, 'opcion' se leeria dentro de un bucle. Aqui mostramos
        // que hace cada opcion concreta.
        switch (opcion) {
            case 1: System.out.println("  > Mostrando saldo de la cuenta..."); break;
            case 2: System.out.println("  > Iniciando deposito..."); break;
            case 3: System.out.println("  > Iniciando retirada..."); break;
            case 4: System.out.println("  > Saliendo del cajero..."); break;
            default: System.out.println("  Opcion no valida");
        }
    }

    // ------------------------------------------------------------------
    // Ejercicio 8
    // Enunciado: Recibe el numero de un mes (1-12) e imprime la estacion del ano.
    // ------------------------------------------------------------------
    public static void e08(int mes) {
        System.out.println("[E08] Estacion del ano segun el mes -> mes = " + mes);
        String estacion;
        // switch agrupando varios case que comparten resultado.
        switch (mes) {
            case 12: case 1: case 2:  estacion = "Invierno";  break;
            case 3:  case 4: case 5:  estacion = "Primavera"; break;
            case 6:  case 7: case 8:  estacion = "Verano";    break;
            case 9:  case 10: case 11: estacion = "Otono";    break;
            default: estacion = "Mes no valido";
        }
        System.out.println("  Estacion: " + estacion);
    }

    // ------------------------------------------------------------------
    // Ejercicio 9
    // Enunciado: Simula un login. Valida usuario "admin" y contrasena "1234XYZ".
    // Informa de los errores de forma especifica.
    // ------------------------------------------------------------------
    public static void e09(String usuario, String password) {
        System.out.println("[E09] Sistema de login -> usuario = '" + usuario + "'");
        final String USUARIO_OK = "admin";
        final String PASS_OK = "1234XYZ";
        // .equals compara el CONTENIDO de las cadenas (no usar == con String).
        boolean userOk = usuario.equals(USUARIO_OK);
        boolean passOk = password.equals(PASS_OK);
        if (userOk && passOk) {
            System.out.println("  Login correcto. Bienvenido, admin.");
        } else if (!userOk) {
            System.out.println("  Error: el usuario no existe");
        } else {
            System.out.println("  Error: contrasena incorrecta");
        }
    }

    // ------------------------------------------------------------------
    // Ejercicio 10
    // Enunciado: Solicita los coeficientes a, b, c de una ecuacion de 2o grado y
    // determina el numero de soluciones reales evaluando el discriminante.
    // ------------------------------------------------------------------
    public static void e10(double a, double b, double c) {
        System.out.println("[E10] Soluciones reales de ax^2+bx+c -> a=" + a + ", b=" + b + ", c=" + c);
        if (a == 0) {
            System.out.println("  No es una ecuacion de segundo grado (a=0)");
            return;
        }
        double discriminante = b * b - 4 * a * c; // b^2 - 4ac
        if (discriminante > 0) {
            System.out.println("  Discriminante = " + discriminante + " > 0 -> 2 soluciones reales distintas");
        } else if (discriminante == 0) {
            System.out.println("  Discriminante = 0 -> 1 solucion real (doble)");
        } else {
            System.out.println("  Discriminante = " + discriminante + " < 0 -> sin soluciones reales");
        }
    }

    /** Ejecuta los 10 ejercicios con varios valores de ejemplo. */
    public static void ejecutar() {
        System.out.println("==================================================");
        System.out.println(" BLOQUE 2: ESTRUCTURAS DE CONTROL CONDICIONALES");
        System.out.println("==================================================");
        e01(7); e01(10);                 // impar y par
        e02(4.5); e02(6.0); e02(8.0); e02(9.5); // las 4 calificaciones
        e03(3, 9, 5); e03(8, 8, 8);
        e04(6, 3, '+'); e04(6, 3, '/'); e04(6, 0, '/');
        e05(120); e05(75); e05(30);
        e06(20, true); e06(16, true); e06(25, false);
        e07(1); e07(3); e07(9);
        e08(1); e08(4); e08(7); e08(10);
        e09("admin", "1234XYZ"); e09("root", "1234XYZ"); e09("admin", "0000");
        e10(1, -3, 2); e10(1, -2, 1); e10(1, 0, 5);
        System.out.println();
    }

    public static void main(String[] args) {
        ejecutar();
    }
}
