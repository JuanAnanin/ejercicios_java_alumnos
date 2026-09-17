package com.ifcd0112.ejercicios.POOExpress;

import java.util.Scanner;

/**
 * BLOQUE 8: Mini Proyectos Completos (3 proyectos).
 *
 *  1. Sistema de Gestion de Biblioteca.
 *  2. Simulador de Cuenta de Gastos y Presupuesto Personal.
 *  3. Motor de Batalla por Turnos (RPG escolar).
 *
 * Cada proyecto incluye:
 *   - Sus clases de dominio encapsuladas.
 *   - Un menu/combate INTERACTIVO con Scanner (la version "real" que se pide).
 *   - Una demo AUTOMATIZADA que se ejecuta sin pedir entradas, para poder lanzar
 *     todo el proyecto de corrido. {@link #ejecutar()} llama a las demos.
 *
 * Para usar las versiones interactivas reales, descomenta las llamadas marcadas
 * en {@link #ejecutar()} (usan System.in).
 */
public class Bloque8MiniProyectos {

    // ##################################################################
    // MINI PROYECTO 1: SISTEMA DE GESTION DE BIBLIOTECA
    // ##################################################################
    static class LibroBiblioteca {
        private String titulo;
        private String autor;
        private boolean disponible; // estado de prestamo

        LibroBiblioteca(String titulo, String autor) {
            this.titulo = titulo;
            this.autor = autor;
            this.disponible = true;
        }
        String getTitulo() { return titulo; }
        boolean isDisponible() { return disponible; }
        void setDisponible(boolean disponible) { this.disponible = disponible; }
        String ficha() {
            return "'" + titulo + "' de " + autor + " [" + (disponible ? "DISPONIBLE" : "PRESTADO") + "]";
        }
    }

    static class Biblioteca {
        private LibroBiblioteca[] libros = new LibroBiblioteca[100]; // catalogo dimensionado
        private int cantidad = 0;

        // Agrega un libro al catalogo.
        void agregar(LibroBiblioteca libro) {
            if (cantidad < libros.length) {
                libros[cantidad++] = libro;
                System.out.println("  Anadido: " + libro.getTitulo());
            }
        }
        // Busca un libro por titulo exacto (devuelve null si no existe).
        LibroBiblioteca buscar(String titulo) {
            for (int i = 0; i < cantidad; i++) {
                if (libros[i].getTitulo().equals(titulo)) return libros[i];
            }
            return null;
        }
        // Presta un libro si esta disponible.
        void prestar(String titulo) {
            LibroBiblioteca l = buscar(titulo);
            if (l == null) { System.out.println("  No existe: " + titulo); return; }
            if (!l.isDisponible()) { System.out.println("  Ya prestado: " + titulo); return; }
            l.setDisponible(false);
            System.out.println("  Prestado: " + titulo);
        }
        // Devuelve un libro prestado.
        void devolver(String titulo) {
            LibroBiblioteca l = buscar(titulo);
            if (l == null) { System.out.println("  No existe: " + titulo); return; }
            l.setDisponible(true);
            System.out.println("  Devuelto: " + titulo);
        }
        void listar() {
            System.out.println("  --- Catalogo ---");
            for (int i = 0; i < cantidad; i++) System.out.println("   " + libros[i].ficha());
        }

        // Menu INTERACTIVO real (while + switch). Se deja disponible para uso manual.
        void menuInteractivo(Scanner sc) {
            int opcion;
            do {
                System.out.println("\n1) Listar  2) Buscar  3) Prestar  4) Devolver  0) Salir");
                opcion = Integer.parseInt(sc.nextLine());
                switch (opcion) {
                    case 1: listar(); break;
                    case 2: System.out.print("Titulo: "); LibroBiblioteca l = buscar(sc.nextLine());
                            System.out.println(l != null ? l.ficha() : "  No encontrado"); break;
                    case 3: System.out.print("Titulo a prestar: "); prestar(sc.nextLine()); break;
                    case 4: System.out.print("Titulo a devolver: "); devolver(sc.nextLine()); break;
                    case 0: System.out.println("Saliendo de la biblioteca..."); break;
                    default: System.out.println("Opcion no valida");
                }
            } while (opcion != 0);
        }
    }

    /** Demo automatizada del proyecto 1 (sin Scanner). */
    static void demoBiblioteca() {
        System.out.println("--- MINI PROYECTO 1: BIBLIOTECA (demo automatica) ---");
        Biblioteca biblioteca = new Biblioteca();
        biblioteca.agregar(new LibroBiblioteca("Dune", "Frank Herbert"));
        biblioteca.agregar(new LibroBiblioteca("Neuromante", "William Gibson"));
        biblioteca.agregar(new LibroBiblioteca("Fundacion", "Isaac Asimov"));
        biblioteca.listar();
        biblioteca.prestar("Dune");
        biblioteca.prestar("Dune");        // ya prestado
        biblioteca.prestar("Inexistente"); // no existe
        biblioteca.devolver("Dune");
        biblioteca.listar();
    }

    // ##################################################################
    // MINI PROYECTO 2: SIMULADOR DE CUENTA DE GASTOS Y PRESUPUESTO
    // ##################################################################
    static class Transaccion {
        private String concepto;
        private double monto;
        private String tipo; // "Ingreso" o "Gasto"
        Transaccion(String concepto, double monto, String tipo) {
            this.concepto = concepto; this.monto = monto; this.tipo = tipo;
        }
        String getConcepto() { return concepto; }
        double getMonto() { return monto; }
        String getTipo() { return tipo; }
    }

    static class GestorPresupuesto {
        private Transaccion[] movimientos = new Transaccion[100];
        private int cantidad = 0;
        private double balance = 0;
        private double limiteCredito; // cuanto descubierto se tolera

        GestorPresupuesto(double limiteCredito) { this.limiteCredito = limiteCredito; }

        // Registra un movimiento. Impide gastos que dejen el balance por debajo
        // del limite de credito permitido.
        boolean registrar(String concepto, double monto, String tipo) {
            if (tipo.equals("Gasto")) {
                if (balance - monto < -limiteCredito) {
                    System.out.println("  Gasto '" + concepto + "' (" + monto + ") RECHAZADO: superaria el descubierto");
                    return false;
                }
                balance -= monto;
            } else { // Ingreso
                balance += monto;
            }
            movimientos[cantidad++] = new Transaccion(concepto, monto, tipo);
            System.out.println("  Registrado " + tipo + ": " + concepto + " (" + monto + ") -> balance: " + balance);
            return true;
        }

        // Informe con total de ingresos, gastos y alerta si el gasto supera el 80%.
        void informe() {
            double totalIngresos = 0, totalGastos = 0;
            for (int i = 0; i < cantidad; i++) {
                if (movimientos[i].getTipo().equals("Ingreso")) totalIngresos += movimientos[i].getMonto();
                else totalGastos += movimientos[i].getMonto();
            }
            System.out.println("  --- INFORME ---");
            System.out.printf("  Total ingresos: %.2f%n", totalIngresos);
            System.out.printf("  Total gastos:   %.2f%n", totalGastos);
            System.out.printf("  Balance final:  %.2f%n", balance);
            if (totalIngresos > 0 && totalGastos > totalIngresos * 0.8) {
                System.out.println("  !! ALERTA: el gasto supera el 80% de los ingresos !!");
            }
        }
    }

    /** Demo automatizada del proyecto 2. */
    static void demoPresupuesto() {
        System.out.println("--- MINI PROYECTO 2: PRESUPUESTO (demo automatica) ---");
        GestorPresupuesto gestor = new GestorPresupuesto(200); // tolera -200 de descubierto
        gestor.registrar("Nomina", 1500, "Ingreso");
        gestor.registrar("Alquiler", 700, "Gasto");
        gestor.registrar("Compra", 350, "Gasto");
        gestor.registrar("Capricho", 800, "Gasto"); // probablemente rechazado
        gestor.registrar("Luz", 90, "Gasto");
        gestor.informe();
    }

    // ##################################################################
    // MINI PROYECTO 3: MOTOR DE BATALLA POR TURNOS (RPG)
    // ##################################################################
    static class Personaje {
        private String nombre;
        private int puntosVida;
        private int puntosAtaque;
        private int puntosDefensa;
        private int defensaBase; // para restaurar tras "defenderse"

        Personaje(String nombre, int vida, int ataque, int defensa) {
            this.nombre = nombre;
            this.puntosVida = vida;
            this.puntosAtaque = ataque;
            this.puntosDefensa = defensa;
            this.defensaBase = defensa;
        }
        String getNombre() { return nombre; }
        int getVida() { return puntosVida; }
        boolean estaVivo() { return puntosVida > 0; }

        // Ataca a un objetivo: dano = ataque - defensa (minimo 1).
        void atacar(Personaje objetivo) {
            int dano = this.puntosAtaque - objetivo.puntosDefensa;
            if (dano < 1) dano = 1; // el dano minimo siempre es 1
            objetivo.puntosVida -= dano;
            if (objetivo.puntosVida < 0) objetivo.puntosVida = 0;
            System.out.println("  " + nombre + " ataca a " + objetivo.nombre
                    + " causando " + dano + " de dano. Vida de " + objetivo.nombre + ": " + objetivo.puntosVida);
        }
        // Defenderse: duplica la defensa SOLO durante el turno actual.
        void defenderse() {
            puntosDefensa = defensaBase * 2;
            System.out.println("  " + nombre + " se defiende (defensa temporal: " + puntosDefensa + ")");
        }
        // Restaura la defensa al valor base (al terminar el turno).
        void terminarTurno() { puntosDefensa = defensaBase; }
        // Curarse: recupera una cantidad aleatoria de salud.
        void curarse() {
            int cura = 10 + (int) (Math.random() * 11); // entre 10 y 20
            puntosVida += cura;
            System.out.println("  " + nombre + " se cura " + cura + " puntos. Vida: " + puntosVida);
        }
    }

    // Combate INTERACTIVO real (el usuario elige cada turno). Disponible para uso manual.
    static void combateInteractivo(Scanner sc, Personaje heroe, Personaje enemigo) {
        while (heroe.estaVivo() && enemigo.estaVivo()) {
            System.out.println("\nTurno de " + heroe.getNombre() + " -- 1) Atacar  2) Defenderse  3) Curarse");
            int op = Integer.parseInt(sc.nextLine());
            switch (op) {
                case 1: heroe.atacar(enemigo); break;
                case 2: heroe.defenderse(); break;
                case 3: heroe.curarse(); break;
                default: System.out.println("  Turno perdido");
            }
            if (!enemigo.estaVivo()) break;
            enemigo.atacar(heroe); // el enemigo siempre ataca
            heroe.terminarTurno(); // se restaura la defensa temporal
        }
        System.out.println("Ganador: " + (heroe.estaVivo() ? heroe.getNombre() : enemigo.getNombre()));
    }

    /** Demo automatizada del proyecto 3 (acciones del heroe predefinidas). */
    static void demoCombate() {
        System.out.println("--- MINI PROYECTO 3: BATALLA RPG (demo automatica) ---");
        Personaje heroe = new Personaje("Rex", 50, 12, 4);
        Personaje enemigo = new Personaje("Monstruo", 40, 10, 3);
        // Secuencia de acciones del heroe simulada (1=atacar, 2=defender, 3=curar).
        int[] acciones = {1, 2, 1, 3, 1, 1, 1, 1};
        int turno = 0;
        while (heroe.estaVivo() && enemigo.estaVivo()) {
            int accion = acciones[turno % acciones.length];
            System.out.println(" [Turno " + (turno + 1) + "] " + heroe.getNombre() + " elige opcion " + accion);
            switch (accion) {
                case 1: heroe.atacar(enemigo); break;
                case 2: heroe.defenderse(); break;
                case 3: heroe.curarse(); break;
            }
            if (!enemigo.estaVivo()) break;
            enemigo.atacar(heroe);  // turno del enemigo
            heroe.terminarTurno();  // se quita la defensa temporal
            turno++;
        }
        System.out.println("  GANADOR: " + (heroe.estaVivo() ? heroe.getNombre() : enemigo.getNombre()));
    }

    /** Ejecuta las demos automatizadas de los 3 mini proyectos. */
public static void ejecutar() {
        System.out.println("==================================================");
        System.out.println(" BLOQUE 8: MINI PROYECTOS COMPLETOS");
        System.out.println("==================================================");
        // demoBiblioteca();
        // System.out.println();
        // demoPresupuesto();
        // System.out.println();
        // demoCombate();
        // System.out.println();

        // --- VERSIONES INTERACTIVAS REALES (descomentar para usarlas) ---
        System.out.println("\n--- MINI PROYECTO 1: BIBLIOTECA ---");
        Scanner sc2 = new Scanner(System.in);
        Biblioteca b = new Biblioteca();
        b.agregar(new LibroBiblioteca("Dune", "Frank Herbert"));
        b.menuInteractivo(sc2);
        System.out.println("\n--- MINI PROYECTO 2: PRESUPUESTO ---");
        GestorPresupuesto g = new GestorPresupuesto(200);
        g.registrar("Nomina", 1500, "Ingreso");
        g.registrar("Alquiler", 700, "Gasto");
        g.registrar("Compra", 350, "Gasto");
        g.informe();
        System.out.println("\n--- MINI PROYECTO 3: BATALLA RPG ---");
        combateInteractivo(sc2, new Personaje("Rex", 50, 12, 4), new Personaje("Monstruo", 40, 10, 3));
        
        // El Scanner se cierra al final de la ejecucion para liberar recursos.
        sc2.close();
    }


    public static void main(String[] args) {
        ejecutar();
    }
}
