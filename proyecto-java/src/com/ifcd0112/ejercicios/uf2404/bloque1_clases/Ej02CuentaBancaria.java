package com.ifcd0112.ejercicios.uf2404.bloque1_clases;

/**
 * UF2404 - BLOQUE 1 - EJERCICIO 2: Cuenta bancaria con control de saldo.
 *
 * <p>Criterios de evaluacion: CE1.2, CE2.4, CE2.10</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej02CuentaBancaria {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Implementa una clase CuentaBancaria que modele una cuenta con titular,
     * numero de cuenta y saldo. La clase debe impedir que el saldo quede en
     * negativo como consecuencia de una retirada.
     *
     * Se pide:
     *   1. Todos los atributos deben ser privados. El numero de cuenta no debe
     *      poder modificarse una vez creada la cuenta.
     *   2. Implementa dos constructores: uno que reciba titular y numero de
     *      cuenta (saldo inicial 0) y otro que reciba ademas un saldo inicial.
     *      El primero debe invocar al segundo utilizando this(...).
     *   3. Implementa ingresar(double cantidad), que rechace cantidades
     *      negativas o cero lanzando una excepcion.
     *   4. Implementa retirar(double cantidad), que lance una excepcion propia
     *      SaldoInsuficienteException si la cantidad supera el saldo disponible.
     *   5. La excepcion SaldoInsuficienteException debe almacenar como atributo
     *      el saldo disponible en el momento del intento, y ofrecer un metodo
     *      para consultarlo.
     *   6. Prueba la clase en un main capturando la excepcion y mostrando un
     *      mensaje que incluya el saldo disponible.
     *
     * Pista: recuerda que la llamada this(...) a otro constructor debe ser la
     * primera instruccion del constructor que la invoca.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /**
     * Apartado 5: excepcion propia que ademas del mensaje transporta un dato
     * estructurado (el saldo disponible en el instante del fallo).
     *
     * <p>Extiende de Exception y no de RuntimeException a proposito: quedarse
     * sin saldo NO es un error de programacion, es una situacion legitima del
     * negocio que quien llama tiene que gestionar. Al ser comprobada (checked),
     * el compilador obliga a tratarla y no se puede ignorar por descuido.</p>
     */
    public static class SaldoInsuficienteException extends Exception {

        /** Toda clase serializable debe declararlo; Exception lo es por herencia. */
        private static final long serialVersionUID = 1L;

        private final double saldoDisponible;

        /**
         * @param mensaje         descripcion del problema
         * @param saldoDisponible saldo que habia en la cuenta al intentar la retirada
         */
        public SaldoInsuficienteException(String mensaje, double saldoDisponible) {
            super(mensaje);
            this.saldoDisponible = saldoDisponible;
        }

        /** @return el saldo disponible en el momento del intento fallido */
        public double getSaldoDisponible() {
            return saldoDisponible;
        }
    }

    /** Cuenta bancaria que nunca puede quedar en descubierto. */
    public static class CuentaBancaria {

        // Apartado 1: todos los atributos privados. El numero de cuenta ademas
        // es final, que es la forma de "no poder modificarse una vez creada":
        // no basta con omitir el setter, final lo garantiza en tiempo de compilacion.
        private final String numeroCuenta;
        private String titular;
        private double saldo;

        /**
         * Apartado 2: constructor corto. Delega en el completo con saldo 0.
         *
         * @param titular      nombre del titular
         * @param numeroCuenta identificador de la cuenta
         */
        public CuentaBancaria(String titular, String numeroCuenta) {
            // this(...) debe ser la PRIMERA instruccion: no puede haber ni una
            // linea de codigo antes, ni siquiera una validacion.
            this(titular, numeroCuenta, 0);
        }

        /**
         * Apartado 2: constructor completo. Es el unico que asigna atributos,
         * de modo que cualquier validacion futura solo hay que escribirla aqui.
         *
         * @param titular      nombre del titular
         * @param numeroCuenta identificador de la cuenta
         * @param saldoInicial saldo de apertura
         */
        public CuentaBancaria(String titular, String numeroCuenta, double saldoInicial) {
            this.titular = titular;
            this.numeroCuenta = numeroCuenta;
            this.saldo = saldoInicial;
        }

        public String getNumeroCuenta() { return numeroCuenta; }
        public String getTitular()      { return titular; }
        public double getSaldo()        { return saldo; }

        // Hay setter de titular (una persona puede cambiar de nombre) pero NO
        // de numeroCuenta ni de saldo: el saldo solo cambia via ingresar/retirar.
        public void setTitular(String titular) { this.titular = titular; }

        /**
         * Apartado 3: ingreso.
         *
         * @param cantidad importe a ingresar; debe ser mayor que cero
         * @throws IllegalArgumentException si la cantidad es negativa o cero
         */
        public void ingresar(double cantidad) {
            // Aqui SI usamos una excepcion no comprobada: ingresar una cantidad
            // negativa no es una situacion de negocio, es un error de quien
            // programa la llamada. Son dos categorias distintas de error y
            // conviene que el tipo de excepcion lo refleje.
            if (cantidad <= 0) {
                throw new IllegalArgumentException(
                        "La cantidad a ingresar debe ser mayor que cero (recibido: " + cantidad + ")");
            }
            saldo += cantidad;
        }

        /**
         * Apartado 4: retirada con control de saldo.
         *
         * @param cantidad importe a retirar; debe ser mayor que cero
         * @throws IllegalArgumentException    si la cantidad es negativa o cero
         * @throws SaldoInsuficienteException  si la cantidad supera el saldo disponible
         */
        public void retirar(double cantidad) throws SaldoInsuficienteException {
            if (cantidad <= 0) {
                throw new IllegalArgumentException(
                        "La cantidad a retirar debe ser mayor que cero (recibido: " + cantidad + ")");
            }
            if (cantidad > saldo) {
                // La excepcion se lanza ANTES de tocar el saldo: si se lanza,
                // el objeto queda exactamente como estaba.
                throw new SaldoInsuficienteException(
                        "Saldo insuficiente para retirar " + String.format("%.2f", cantidad) + " EUR",
                        saldo);
            }
            saldo -= cantidad;
        }

        @Override
        public String toString() {
            return titular + " (" + numeroCuenta + "): " + String.format("%.2f", saldo) + " EUR";
        }
    }

    // =====================================================================
    // COMPROBACION (apartado 6)
    // =====================================================================

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2404-E02] Cuenta bancaria con control de saldo");

        // Constructor corto: delega en el completo con saldo 0.
        CuentaBancaria basica = new CuentaBancaria("Ana Lopez", "ES1111");
        System.out.println("  Con el constructor de 2 parametros -> " + basica);

        CuentaBancaria cuenta = new CuentaBancaria("Ana Lopez", "ES1234", 500);
        System.out.println("  Cuenta de partida -> " + cuenta);

        cuenta.ingresar(200);
        System.out.println("  Tras ingresar 200 -> " + cuenta);

        try {
            cuenta.retirar(150);
            System.out.println("  Tras retirar 150 -> " + cuenta);
        } catch (SaldoInsuficienteException e) {
            System.out.println("  " + e.getMessage());
        }

        // Apartado 6: capturamos la excepcion y aprovechamos el dato que
        // transporta. Ese getSaldoDisponible() es lo que permite dar un mensaje
        // util al usuario en lugar de un simple "no se ha podido".
        try {
            cuenta.retirar(800);
        } catch (SaldoInsuficienteException e) {
            System.out.println("  " + e.getMessage());
            System.out.println("  Puedes retirar como maximo: "
                    + String.format("%.2f", e.getSaldoDisponible()) + " EUR");
        }

        // La retirada fallida no ha alterado el saldo.
        System.out.println("  El saldo no se ha tocado -> " + cuenta);

        // Cantidades invalidas: error de programacion, no de negocio.
        try {
            cuenta.ingresar(-50);
        } catch (IllegalArgumentException e) {
            System.out.println("  ingresar(-50) -> IllegalArgumentException: " + e.getMessage());
        }
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
