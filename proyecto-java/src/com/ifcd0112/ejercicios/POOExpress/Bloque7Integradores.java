package com.ifcd0112.ejercicios.POOExpress;

/**
 * BLOQUE 7: Ejercicios Integradores (5 ejercicios).
 *
 * Combinan encapsulamiento, arrays y metodos de analisis. Cada sistema se modela
 * con su propia clase anidada y {@link #ejecutar()} simula un caso de uso real.
 */
public class Bloque7Integradores {

    // ==================================================================
    // Ejercicio 1: Gestion de Inventario.
    // ==================================================================
    static class Articulo {
        private int id;
        private String descripcion;
        private int stock;
        private double precio;

        Articulo(int id, String descripcion, int stock, double precio) {
            this.id = id; 
            this.descripcion = descripcion;
            this.stock = stock; 
            this.precio = precio;
        }
        // Anade unidades al stock.
        void anadirStock(int unidades) {
            if (unidades > 0) stock += unidades;
        }
        // Vende unidades verificando disponibilidad.
        boolean vender(int unidades) {
            if (unidades <= stock) { stock -= unidades; return true; }
            return false; // no hay stock suficiente
        }
        // Valor total del inventario de este articulo.
        double valorInventario() { return stock * precio; }
        String getDescripcion() { return descripcion; }
        int getStock() { return stock; }
    }

    // ==================================================================
    // Ejercicio 2: Simulador de Calificaciones (array privado de hasta 5 notas).
    // ==================================================================
    static class EstudianteNotas {
        private double[] notas = new double[5]; // capacidad maxima 5
        private int cantidad = 0;               // cuantas notas hay realmente

        // Anade una nota de forma segura, evitando desbordar el array.
        boolean anadirNota(double nota) {
            if (cantidad >= notas.length) {
                System.out.println("    Array de notas lleno, no se anade " + nota);
                return false;
            }
            notas[cantidad++] = nota;
            return true;
        }
        // Nota final = media de las notas registradas.
        double notaFinal() {
            if (cantidad == 0) return 0;
            double suma = 0;
            for (int i = 0; i < cantidad; i++) suma += notas[i];
            return suma / cantidad;
        }
        boolean aprobado() { return notaFinal() >= 5.0; }
    }

    // ==================================================================
    // Ejercicio 3: Sistema de Reserva de Vuelos.
    // ==================================================================
    static class Asiento {
        int numero;
        String clase;
        boolean ocupado;
        Asiento(int numero, String clase) {
            this.numero = numero; this.clase = clase; this.ocupado = false;
        }
    }
    static class Vuelo {
        private Asiento[] asientos;
        Vuelo(int totalAsientos) {
            asientos = new Asiento[totalAsientos];
            for (int i = 0; i < totalAsientos; i++) {
                // Primer 20% en clase Business, resto en Turista (ejemplo).
                String clase = (i < totalAsientos * 0.2) ? "Business" : "Turista";
                asientos[i] = new Asiento(i + 1, clase);
            }
        }
        // Reserva un asiento por su numero.
        boolean reservar(int numero) {
            for (Asiento a : asientos) {
                if (a.numero == numero) {
                    if (a.ocupado) return false; // ya estaba ocupado
                    a.ocupado = true;
                    return true;
                }
            }
            return false; // numero no existe
        }
        // Porcentaje de ocupacion actual.
        double porcentajeOcupacion() {
            int ocupados = 0;
            for (Asiento a : asientos) if (a.ocupado) ocupados++;
            return (ocupados * 100.0) / asientos.length;
        }
    }

    // ==================================================================
    // Ejercicio 4: Historial de Transacciones de Tarjeta de Credito.
    // ==================================================================
    static class TarjetaCredito {
        private String titular;
        private double limiteCredito;
        private double saldoDispuesto; // cuanto se ha gastado

        TarjetaCredito(String titular, double limiteCredito) {
            this.titular = titular;
            this.limiteCredito = limiteCredito;
            this.saldoDispuesto = 0;
        }
        // Realiza un pago si no se supera el limite.
        boolean pagar(double importe) {
            if (saldoDispuesto + importe > limiteCredito) {
                System.out.println("    Pago de " + importe + " RECHAZADO (supera el limite)");
                return false;
            }
            saldoDispuesto += importe;
            System.out.println("    Pago de " + importe + " aceptado. Dispuesto: " + saldoDispuesto);
            return true;
        }
        // Abona saldo (devuelve credito disponible).
        void abonar(double importe) {
            saldoDispuesto -= importe;
            if (saldoDispuesto < 0) saldoDispuesto = 0;
            System.out.println("    Abono de " + importe + ". Dispuesto: " + saldoDispuesto);
        }
    }

    // ==================================================================
    // Ejercicio 5: Estacion Meteorologica Automatizada.
    // ==================================================================
    static class EstacionMeteorologica {
        private double[] temperaturas; // una por dia de la semana
        EstacionMeteorologica(double[] temperaturas) { this.temperaturas = temperaturas; }

        double maxima() {
            double max = temperaturas[0];
            for (double t : temperaturas) if (t > max) max = t;
            return max;
        }
        double minima() {
            double min = temperaturas[0];
            for (double t : temperaturas) if (t < min) min = t;
            return min;
        }
        double media() {
            double suma = 0;
            for (double t : temperaturas) suma += t;
            return suma / temperaturas.length;
        }
        int diasSobreUmbral(double umbral) {
            int dias = 0;
            for (double t : temperaturas) if (t > umbral) dias++;
            return dias;
        }
    }

    /** Simula un caso de uso de cada sistema integrador. */
    public static void ejecutar() {
        System.out.println("==================================================");
        System.out.println(" BLOQUE 7: EJERCICIOS INTEGRADORES");
        System.out.println("==================================================");

        System.out.println("[E01] Gestion de inventario");
        Articulo[] inventario = {
                new Articulo(1, "Teclado mecanico", 10, 45.0),
                new Articulo(2, "Raton inalambrico", 25, 19.5),
                new Articulo(3, "Monitor 24\"", 5, 129.0)
        };
        inventario[0].anadirStock(5); // ahora 15
        inventario[1].vender(30);     // falla: no hay 30
        inventario[1].vender(10);     // ok
        double valorTotal = 0;
        for (Articulo a : inventario) {
            System.out.println("  " + a.getDescripcion() + " -> stock=" + a.getStock()
                    + ", valor=" + a.valorInventario());
            valorTotal += a.valorInventario();
        }
        System.out.println("  VALOR TOTAL DEL INVENTARIO: " + valorTotal + " EUR");

        System.out.println("[E02] Simulador de calificaciones");
        EstudianteNotas alumno = new EstudianteNotas();
        double[] examenes = {6.0, 7.5, 4.0, 8.0, 9.0, 5.0}; // 6 notas: la 6a se rechaza
        for (double n : examenes) alumno.anadirNota(n);
        System.out.printf("  Nota final: %.2f -> %s%n", alumno.notaFinal(),
                alumno.aprobado() ? "APROBADO" : "SUSPENSO");

        System.out.println("[E03] Reserva de vuelos");
        Vuelo vuelo = new Vuelo(10);
        vuelo.reservar(1); vuelo.reservar(2); vuelo.reservar(5);
        System.out.println("  reservar(2) de nuevo: " + vuelo.reservar(2) + " (ya ocupado)");
        System.out.printf("  Ocupacion del vuelo: %.1f%%%n", vuelo.porcentajeOcupacion());

        System.out.println("[E04] Tarjeta de credito");
        TarjetaCredito tarjeta = new TarjetaCredito("Pepe", 1000);
        tarjeta.pagar(400);
        tarjeta.pagar(700); // rechazado: 400+700 > 1000
        tarjeta.abonar(200);
        tarjeta.pagar(700); // ahora si cabe

        System.out.println("[E05] Estacion meteorologica");
        EstacionMeteorologica estacion = new EstacionMeteorologica(
                new double[]{28.5, 31.0, 26.2, 33.4, 29.9, 24.1, 35.0});
        System.out.printf("  Maxima=%.1f, Minima=%.1f, Media=%.2f%n",
                estacion.maxima(), estacion.minima(), estacion.media());
        System.out.println("  Dias por encima de 30 grados: " + estacion.diasSobreUmbral(30));
        System.out.println();
    }

    public static void main(String[] args) {
        ejecutar();
    }
}
