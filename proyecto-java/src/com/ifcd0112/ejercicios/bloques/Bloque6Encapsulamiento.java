package com.ifcd0112.ejercicios.bloques;

/**
 * BLOQUE 6: Encapsulamiento y Modificadores de Acceso (10 ejercicios).
 *
 * NOTA IMPORTANTE sobre los ejercicios 3, 4 y 8: piden crear PAQUETES distintos
 * (com.academia.usuarios, com.academia.main, etc.) para comprobar el acceso
 * 'package-private' y 'protected' entre paquetes. Como aqui mantenemos "un solo
 * archivo por bloque", no es posible declarar dos paquetes reales en el mismo
 * fichero. Por eso esos ejercicios se demuestran con clases anidadas y se explica
 * en comentarios que ocurriria entre paquetes distintos. Si tu profesor pide la
 * version multi-paquete, basta con mover cada clase a su carpeta/paquete.
 */
public class Bloque6Encapsulamiento {

    // ==================================================================
    // Ejercicio 1: Persona con encapsulamiento estricto (private + get/set).
    // ==================================================================
    static class Persona {
        private String nombre;   // private: solo accesible desde la propia clase
        private int edad;

        public String getNombre() { return nombre; }            // getter
        public void setNombre(String nombre) { this.nombre = nombre; } // setter
        public int getEdad() { return edad; }
        public void setEdad(int edad) { this.edad = edad; }
    }

    // ==================================================================
    // Ejercicio 2: Producto con validacion en setPrecio (sin negativos ni cero).
    // ==================================================================
    static class Producto {
        private double precio;
        public double getPrecio() { return precio; }
        public void setPrecio(double precio) {
            if (precio <= 0) { // la validacion vive dentro del setter
                System.out.println("  Precio invalido (" + precio + "): debe ser > 0");
                return;
            }
            this.precio = precio;
        }
    }

    // ==================================================================
    // Ejercicio 3: paquetes com.academia.usuarios y com.academia.main +
    // acceso por defecto (package-private).
    // ==================================================================
    // Un miembro SIN modificador (ni public/private/protected) es 'package-private':
    // solo es visible para clases del MISMO paquete. Si Cuenta estuviera en
    // com.academia.usuarios y la usaramos desde com.academia.main, el atributo
    // 'codigoInterno' NO seria accesible y daria error de compilacion.
    static class CuentaPaquete {
        String codigoInterno = "ACC-001"; // package-private (sin modificador)
        public String getCodigoPublico() { return codigoInterno; } // acceso controlado
    }

    // ==================================================================
    // Ejercicio 4: atributo protected + subclase en otro paquete.
    // ==================================================================
    // 'protected' es visible en el mismo paquete Y en subclases (aunque esten en
    // otro paquete), pero solo a traves de la herencia. Aqui lo demostramos con
    // herencia dentro del mismo archivo.
    static class Base {
        protected String campoProtegido = "valor-heredable";
    }
    static class Derivada extends Base {
        void mostrar() {
            // Accedemos al campo protected heredado de la clase Base.
            System.out.println("  Subclase accede a protected: " + campoProtegido);
        }
    }

    // ==================================================================
    // Ejercicio 5: ConfiguracionSistema con constantes public static final.
    // ==================================================================
    static class ConfiguracionSistema {
        // public static final = constante de clase, accesible desde fuera y fija.
        public static final String RUTA_RED = "//servidor/datos";
        public static final int PUERTO = 8080;
    }

    // ==================================================================
    // Ejercicio 6: Usuario con id de solo lectura (sin setter publico).
    // ==================================================================
    static class Usuario {
        private final int id;     // 'final' -> solo se asigna una vez (en el constructor)
        private String nombre;
        public Usuario(int id, String nombre) {
            this.id = id;         // unico punto donde se fija el id
            this.nombre = nombre;
        }
        public int getId() { return id; }            // solo lectura, no hay setId()
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
    }

    // ==================================================================
    // Ejercicio 7: Mascota con metodo privado de logging al cambiar el nombre.
    // ==================================================================
    static class Mascota {
        private String nombre;
        public void setNombre(String nuevoNombre) {
            registrar("Cambio de nombre: '" + nombre + "' -> '" + nuevoNombre + "'");
            this.nombre = nuevoNombre;
        }
        public String getNombre() { return nombre; }
        // Metodo privado: detalle interno, no visible fuera de la clase.
        private void registrar(String mensaje) {
            System.out.println("  [LOG] " + mensaje);
        }
    }

    // ==================================================================
    // Ejercicio 8: clase con metodos public/protected/private/default.
    // ==================================================================
    // Visibilidad de cada metodo:
    //   public    -> accesible desde cualquier clase y paquete.
    //   protected -> mismo paquete + subclases (incluso de otro paquete).
    //   default   -> solo mismo paquete (sin modificador).
    //   private   -> solo dentro de la propia clase.
    // Invocar metodoPrivado() o metodoDefault() desde otro paquete daria ERROR
    // de compilacion; metodoPublico() siempre funciona.
    static class Gestion {
        public void metodoPublico()    { System.out.println("  metodoPublico() ejecutado (siempre accesible)"); }
        protected void metodoProtegido(){ System.out.println("  metodoProtegido() ejecutado (mismo paquete/subclases)"); }
        void metodoDefault()           { System.out.println("  metodoDefault() ejecutado (solo mismo paquete)"); }
        private void metodoPrivado()   { System.out.println("  metodoPrivado() ejecutado (solo dentro de la clase)"); }
        // Metodo publico que demuestra que el privado SI es accesible internamente.
        public void llamarTodos() {
            metodoPublico(); metodoProtegido(); metodoDefault(); metodoPrivado();
        }
    }

    // ==================================================================
    // Ejercicio 9: Termometro guardado internamente en Kelvin (private),
    // expuesto en Celsius.
    // ==================================================================
    static class Termometro {
        private double kelvin; // representacion interna oculta
        public double getCelsius() { return kelvin - 273.15; }
        public void setCelsius(double celsius) { this.kelvin = celsius + 273.15; }
    }

    // ==================================================================
    // Ejercicio 10: ControlEntrada con contador privado que solo sube con clave.
    // ==================================================================
    static class ControlEntrada {
        private int registros = 0;
        private static final String CLAVE = "OPEN";
        public void registrar(String claveAcceso) {
            if (!CLAVE.equals(claveAcceso)) { // se exige la clave correcta
                System.out.println("  Acceso denegado: clave incorrecta");
                return;
            }
            registros++;
            System.out.println("  Registro aceptado. Total registros: " + registros);
        }
        public int getRegistros() { return registros; }
    }

    /** Instancia y prueba las clases de cada ejercicio. */
    public static void ejecutar() {
        System.out.println("==================================================");
        System.out.println(" BLOQUE 6: ENCAPSULAMIENTO Y MODIFICADORES DE ACCESO");
        System.out.println("==================================================");

        System.out.println("[E01] Persona encapsulada (getters/setters)");
        Persona p = new Persona();
        p.setNombre("Reme"); p.setEdad(29);
        System.out.println("  " + p.getNombre() + ", " + p.getEdad() + " anos");

        System.out.println("[E02] Producto con validacion en setPrecio");
        Producto prod = new Producto();
        prod.setPrecio(19.99); System.out.println("  Precio aceptado: " + prod.getPrecio());
        prod.setPrecio(-5);    // rechazado por la validacion

        System.out.println("[E03] Acceso package-private (concepto de paquetes)");
        CuentaPaquete c = new CuentaPaquete();
        System.out.println("  Acceso controlado al codigo: " + c.getCodigoPublico());

        System.out.println("[E04] Atributo protected accedido por una subclase");
        new Derivada().mostrar();

        System.out.println("[E05] Constantes public static final");
        System.out.println("  Ruta=" + ConfiguracionSistema.RUTA_RED + ", Puerto=" + ConfiguracionSistema.PUERTO);

        System.out.println("[E06] Usuario con id de solo lectura");
        Usuario u = new Usuario(101, "Corso");
        u.setNombre("Corso Vega"); // el nombre si cambia
        System.out.println("  id=" + u.getId() + " (inmutable), nombre=" + u.getNombre());

        System.out.println("[E07] Mascota: logging privado al cambiar el nombre");
        Mascota m = new Mascota();
        m.setNombre("Vortex");
        m.setNombre("V0rtex");

        System.out.println("[E08] Metodos public/protected/default/private");
        new Gestion().llamarTodos();

        System.out.println("[E09] Termometro (Kelvin interno, Celsius externo)");
        Termometro t = new Termometro();
        t.setCelsius(25);
        System.out.printf("  setCelsius(25) -> getCelsius()=%.2f%n", t.getCelsius());

        System.out.println("[E10] ControlEntrada con clave de acceso");
        ControlEntrada ce = new ControlEntrada();
        ce.registrar("MAL");
        ce.registrar("OPEN");
        ce.registrar("OPEN");
        System.out.println();
    }

    public static void main(String[] args) {
        ejecutar();
    }
}
