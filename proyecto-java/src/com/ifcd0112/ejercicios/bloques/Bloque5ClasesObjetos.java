package com.ifcd0112.ejercicios.bloques;

/**
 * BLOQUE 5: Clases y Objetos (10 ejercicios).
 *
 * Cada ejercicio define una clase de dominio. Para mantener "un archivo por
 * bloque" se declaran como clases anidadas estaticas (static nested classes)
 * dentro de esta clase contenedora. {@link #ejecutar()} las instancia y prueba.
 */
public class Bloque5ClasesObjetos {

    // ==================================================================
    // Ejercicio 1: clase Libro con titulo, autor, numeroPaginas.
    // ==================================================================
    static class Libro {
        // Atributos del objeto.
        String titulo;
        String autor;
        int numeroPaginas;

        // Constructor: inicializa el estado del objeto al crearlo.
        Libro(String titulo, String autor, int numeroPaginas) {
            this.titulo = titulo;        // 'this' distingue atributo de parametro
            this.autor = autor;
            this.numeroPaginas = numeroPaginas;
        }

        // Metodo de comportamiento: muestra los detalles.
        void mostrarDetalles() {
            System.out.println("  Libro: '" + titulo + "' de " + autor + " (" + numeroPaginas + " pags)");
        }
    }

    // ==================================================================
    // Ejercicio 2: clase CuentaBancaria con depositar/retirar.
    // ==================================================================
    static class CuentaBancaria {
        String numeroCuenta;
        String titular;
        double saldo;

        CuentaBancaria(String numeroCuenta, String titular, double saldoInicial) {
            this.numeroCuenta = numeroCuenta;
            this.titular = titular;
            this.saldo = saldoInicial;
        }

        void depositar(double cantidad) {
            if (cantidad <= 0) { System.out.println("  Deposito invalido"); return; }
            saldo += cantidad;
            System.out.println("  Deposito de " + cantidad + " -> saldo: " + saldo);
        }

        void retirar(double cantidad) {
            if (cantidad > saldo) { // no permitimos numeros rojos
                System.out.println("  Fondos insuficientes para retirar " + cantidad);
                return;
            }
            saldo -= cantidad;
            System.out.println("  Retirada de " + cantidad + " -> saldo: " + saldo);
        }
    }

    // ==================================================================
    // Ejercicio 3: clase Rectangulo con constructor por defecto y parametrizado.
    // ==================================================================
    static class Rectangulo {
        double ancho;
        double alto;

        Rectangulo() {            // constructor por defecto
            this.ancho = 1;
            this.alto = 1;
        }
        Rectangulo(double ancho, double alto) { // constructor parametrizado
            this.ancho = ancho;
            this.alto = alto;
        }
        double area() { return ancho * alto; }
        double perimetro() { return 2 * (ancho + alto); }
    }

    // ==================================================================
    // Ejercicio 4: clase Estudiante. En ejecutar() se crean 3 y se halla la mayor nota.
    // ==================================================================
    static class Estudiante {
        String nombre;
        int id;
        double notaPromedio;

        Estudiante(String nombre, int id, double notaPromedio) {
            this.nombre = nombre;
            this.id = id;
            this.notaPromedio = notaPromedio;
        }
    }

    // ==================================================================
    // Ejercicio 5: clase Coche con atributo estatico que cuenta instancias.
    // ==================================================================
    static class Coche {
        String modelo;                 // atributo de instancia (uno por objeto)
        static int totalCoches = 0;    // atributo de clase (compartido por todos)

        Coche(String modelo) {
            this.modelo = modelo;
            totalCoches++;             // cada vez que se crea un coche, suma 1
        }
    }

    // ==================================================================
    // Ejercicio 6: clase Calculadora solo con metodos estaticos.
    // ==================================================================
    static class Calculadora {
        // No tiene atributos de instancia; los metodos son utilidades estaticas.
        static double sumar(double a, double b) { return a + b; }
        static double restar(double a, double b) { return a - b; }
        static double multiplicar(double a, double b) { return a * b; }
        static double dividir(double a, double b) { return b != 0 ? a / b : 0; }
    }

    // ==================================================================
    // Ejercicio 7: clase Punto (x, y) con distancia euclidea a otro Punto.
    // ==================================================================
    static class Punto {
        double x, y;
        Punto(double x, double y) { this.x = x; this.y = y; }

        // Recibe otro objeto Punto y calcula la distancia entre ambos.
        double distancia(Punto otro) {
            double dx = this.x - otro.x;
            double dy = this.y - otro.y;
            return Math.sqrt(dx * dx + dy * dy); // teorema de Pitagoras
        }
    }

    // ==================================================================
    // Ejercicio 8: clase Reloj con tictac() que avanza 1 segundo controlando
    // desbordamientos.
    // ==================================================================
    static class Reloj {
        int horas, minutos, segundos;
        Reloj(int h, int m, int s) { horas = h; minutos = m; segundos = s; }

        void tictac() {
            segundos++;
            if (segundos == 60) { segundos = 0; minutos++; } // desbordan los segundos
            if (minutos == 60)  { minutos = 0; horas++; }    // desbordan los minutos
            if (horas == 24)    { horas = 0; }               // vuelta al dia siguiente
        }

        String hora() { return String.format("%02d:%02d:%02d", horas, minutos, segundos); }
    }

    // ==================================================================
    // Ejercicio 9: clase Empleado con sueldo neto tras retenciones simuladas.
    // ==================================================================
    static class Empleado {
        String nombre;
        double sueldoBasico;
        Empleado(String nombre, double sueldoBasico) {
            this.nombre = nombre;
            this.sueldoBasico = sueldoBasico;
        }
        double sueldoNeto() {
            double retencion = 0.18; // 18% de retencion simulada (IRPF + SS)
            return sueldoBasico * (1 - retencion);
        }
    }

    // ==================================================================
    // Ejercicio 10: clase Lampara con encender/apagar y estado en texto.
    // ==================================================================
    static class Lampara {
        boolean encendida; // por defecto false
        void encender() { encendida = true; }
        void apagar()   { encendida = false; }
        String estado() { return encendida ? "ENCENDIDA" : "APAGADA"; }
    }

    /** Instancia y prueba las clases de cada ejercicio. */
    public static void ejecutar() {
        System.out.println("==================================================");
        System.out.println(" BLOQUE 5: CLASES Y OBJETOS");
        System.out.println("==================================================");

        System.out.println("[E01] Clase Libro");
        new Libro("Whisper - Travesia I", "ifcd0112", 312).mostrarDetalles();

        System.out.println("[E02] Clase CuentaBancaria");
        CuentaBancaria cuenta = new CuentaBancaria("ES01", "ifcd0112", 500);
        cuenta.depositar(200);
        cuenta.retirar(150);
        cuenta.retirar(1000); // intento que supera el saldo

        System.out.println("[E03] Clase Rectangulo (2 constructores)");
        Rectangulo r1 = new Rectangulo();        // por defecto 1x1
        Rectangulo r2 = new Rectangulo(4, 6);    // parametrizado
        System.out.println("  Por defecto -> area=" + r1.area() + ", perimetro=" + r1.perimetro());
        System.out.println("  4x6         -> area=" + r2.area() + ", perimetro=" + r2.perimetro());

        System.out.println("[E04] Clase Estudiante: hallar la nota mas alta");
        Estudiante[] alumnos = {
                new Estudiante("Sira", 1, 8.5),
                new Estudiante("Rex", 2, 9.2),
                new Estudiante("Corso", 3, 7.8)
        };
        Estudiante mejor = alumnos[0];
        for (Estudiante e : alumnos) {
            if (e.notaPromedio > mejor.notaPromedio) mejor = e;
        }
        System.out.println("  Mejor nota: " + mejor.nombre + " (" + mejor.notaPromedio + ")");

        System.out.println("[E05] Clase Coche con contador estatico");
        new Coche("Civic"); new Coche("Leon"); new Coche("Corsa");
        System.out.println("  Total de coches instanciados: " + Coche.totalCoches);

        System.out.println("[E06] Clase Calculadora (metodos estaticos)");
        System.out.println("  5 + 3 = " + Calculadora.sumar(5, 3) + ", 5 / 2 = " + Calculadora.dividir(5, 2));

        System.out.println("[E07] Clase Punto: distancia euclidea");
        Punto p1 = new Punto(0, 0), p2 = new Punto(3, 4);
        System.out.println("  Distancia (0,0)-(3,4) = " + p1.distancia(p2));

        System.out.println("[E08] Clase Reloj: tictac con desbordamiento");
        Reloj reloj = new Reloj(23, 59, 58);
        System.out.println("  Inicio: " + reloj.hora());
        reloj.tictac(); System.out.println("  +1s:    " + reloj.hora());
        reloj.tictac(); System.out.println("  +1s:    " + reloj.hora() + "  (cambio de dia)");

        System.out.println("[E09] Clase Empleado: sueldo neto");
        Empleado emp = new Empleado("Vela", 2000);
        System.out.printf("  %s: bruto=%.2f, neto=%.2f%n", emp.nombre, emp.sueldoBasico, emp.sueldoNeto());

        System.out.println("[E10] Clase Lampara");
        Lampara lampara = new Lampara();
        System.out.println("  Estado inicial: " + lampara.estado());
        lampara.encender();
        System.out.println("  Tras encender:  " + lampara.estado());
        System.out.println();
    }

    public static void main(String[] args) {
        ejecutar();
    }
}
