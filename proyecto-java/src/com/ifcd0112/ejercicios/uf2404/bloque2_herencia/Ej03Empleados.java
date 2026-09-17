package com.ifcd0112.ejercicios.uf2404.bloque2_herencia;

import java.util.ArrayList;
import java.util.List;

/**
 * UF2404 - BLOQUE 2 - EJERCICIO 3: Jerarquia de empleados con nomina polimorfica.
 *
 * <p>Criterios de evaluacion: CE1.5, CE1.7, CE2.4</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej03Empleados {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Una empresa necesita calcular la nomina de sus empleados. Todos los
     * empleados tienen nombre, DNI y un salario base, pero el salario final se
     * calcula de forma distinta segun el tipo:
     *   - Empleado de plantilla: cobra el salario base.
     *   - Comercial: cobra el salario base mas un 5% de sus ventas totales.
     *   - Directivo: cobra el salario base mas un complemento fijo de
     *     responsabilidad.
     *
     * Se pide:
     *   1. Disena una clase abstracta Empleado con los atributos comunes y un
     *      metodo abstracto calcularNomina().
     *   2. Implementa las tres subclases, cada una con sus atributos propios y
     *      su propia version de calcularNomina().
     *   3. Cada subclase debe invocar al constructor de la superclase mediante
     *      super(...) para inicializar los atributos comunes.
     *   4. Sobrescribe toString() en la clase Empleado y reutilizalo desde las
     *      subclases anadiendo su informacion especifica (usa super.toString()).
     *   5. En el main, crea un ArrayList<Empleado> con objetos de los tres
     *      tipos, recorrelo con un unico bucle e imprime la nomina de cada uno.
     *   6. Calcula e imprime el coste total mensual de la plantilla.
     *
     * Salida esperada (orientativa):
     *      Comercial Ana Lopez (12345678A): 1850,00 EUR
     *      Directivo Luis Gomez (87654321B): 3200,00 EUR
     *      Empleado Marta Ruiz (11223344C): 1500,00 EUR
     *      --------------------------------------
     *      Coste total de la plantilla: 6550,00 EUR
     *
     * Pista: el bucle del main no debe contener ningun if ni instanceof: si
     * necesitas preguntar por el tipo del objeto para calcular la nomina, el
     * diseno polimorfico no esta bien planteado.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /**
     * Apartado 1: superclase abstracta.
     *
     * <p>Es abstracta porque un "Empleado" generico no existe en el negocio:
     * todo empleado real es de alguno de los tres tipos. Declararla abstracta
     * impide instanciarla por error y obliga a que cada subclase decida como se
     * calcula su nomina.</p>
     */
    public abstract static class Empleado {

        // protected y no private: las subclases necesitan leer estos atributos
        // desde sus propios calcularNomina(). Con private tendriamos que pasar
        // por un getter, que aqui no aporta nada.
        protected final String nombre;
        protected final String dni;
        protected final double salarioBase;

        /**
         * @param nombre      nombre y apellidos del empleado
         * @param dni         documento de identidad
         * @param salarioBase salario base mensual, comun a todos los tipos
         */
        public Empleado(String nombre, String dni, double salarioBase) {
            this.nombre = nombre;
            this.dni = dni;
            this.salarioBase = salarioBase;
        }

        /**
         * Apartado 1: metodo abstracto. La superclase declara QUE hay que saber
         * hacer, pero no COMO: eso es responsabilidad de cada subclase. Esta es
         * la pieza que hace posible el bucle sin if del apartado 5.
         *
         * @return el importe de la nomina de este mes
         */
        public abstract double calcularNomina();

        /**
         * Apartado 4: representacion comun. Las subclases la reutilizan con
         * super.toString() en lugar de repetir el formato, de modo que si
         * manana cambia la forma de mostrar un empleado se cambia en un solo
         * sitio.
         */
        @Override
        public String toString() {
            // Ojo: aqui se invoca calcularNomina(), que es abstracto. En tiempo
            // de ejecucion se resuelve por ligadura dinamica a la version de la
            // subclase real, asi que este mismo codigo imprime el importe
            // correcto para los tres tipos.
            return nombre + " (" + dni + "): " + String.format("%.2f", calcularNomina()) + " EUR";
        }
    }

    /** Apartado 2: empleado de plantilla. Cobra exactamente el salario base. */
    public static class EmpleadoPlantilla extends Empleado {

        public EmpleadoPlantilla(String nombre, String dni, double salarioBase) {
            // Apartado 3: super(...) inicializa los atributos comunes.
            super(nombre, dni, salarioBase);
        }

        @Override
        public double calcularNomina() {
            return salarioBase;
        }

        @Override
        public String toString() {
            // Apartado 4: se reutiliza el formato comun y se le antepone el tipo.
            return "Empleado " + super.toString();
        }
    }

    /** Apartado 2: comercial. Salario base mas un 5% de sus ventas totales. */
    public static class Comercial extends Empleado {

        private static final double COMISION = 0.05;

        private final double totalVentas;

        public Comercial(String nombre, String dni, double salarioBase, double totalVentas) {
            super(nombre, dni, salarioBase);
            this.totalVentas = totalVentas;
        }

        @Override
        public double calcularNomina() {
            return salarioBase + totalVentas * COMISION;
        }

        @Override
        public String toString() {
            return "Comercial " + super.toString();
        }
    }

    /** Apartado 2: directivo. Salario base mas un complemento fijo. */
    public static class Directivo extends Empleado {

        private final double complementoResponsabilidad;

        public Directivo(String nombre, String dni, double salarioBase, double complemento) {
            super(nombre, dni, salarioBase);
            this.complementoResponsabilidad = complemento;
        }

        @Override
        public double calcularNomina() {
            return salarioBase + complementoResponsabilidad;
        }

        @Override
        public String toString() {
            return "Directivo " + super.toString();
        }
    }

    // =====================================================================
    // COMPROBACION (apartados 5 y 6)
    // =====================================================================

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2404-E03] Jerarquia de empleados con nomina polimorfica");

        // Apartado 5: la lista es de Empleado, el tipo comun. A partir de aqui
        // el programa "olvida" de que tipo concreto es cada objeto.
        List<Empleado> plantilla = new ArrayList<>();
        plantilla.add(new Comercial("Ana Lopez", "12345678A", 1500, 7000));   // 1500 + 5% de 7000 = 1850
        plantilla.add(new Directivo("Luis Gomez", "87654321B", 2500, 700));   // 2500 + 700       = 3200
        plantilla.add(new EmpleadoPlantilla("Marta Ruiz", "11223344C", 1500)); // 1500            = 1500

        double total = 0;
        // Apartado 5 y pista: un UNICO bucle, sin un solo if ni un instanceof.
        // La ligadura dinamica se encarga de invocar la version correcta de
        // toString() y de calcularNomina() para cada objeto.
        for (Empleado e : plantilla) {
            System.out.println("  " + e);
            total += e.calcularNomina();
        }

        // Apartado 6.
        System.out.println("  --------------------------------------");
        System.out.println("  Coste total de la plantilla: " + String.format("%.2f", total) + " EUR");

        // La prueba de que el diseno es extensible: anadir un cuarto tipo de
        // empleado no obligaria a tocar ni una linea de este bucle. Es el
        // principio abierto/cerrado de SOLID visto en la UF2406.
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
