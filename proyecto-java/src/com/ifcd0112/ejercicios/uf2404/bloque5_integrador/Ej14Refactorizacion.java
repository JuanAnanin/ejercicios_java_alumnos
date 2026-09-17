package com.ifcd0112.ejercicios.uf2404.bloque5_integrador;

import java.util.ArrayList;
import java.util.List;

/**
 * UF2404 - BLOQUE 5 - EJERCICIO 14: Refactorizacion guiada por criterios de calidad.
 *
 * <p>Criterios de evaluacion: CE2.1, CE2.10</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej14Refactorizacion {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Se te entrega el siguiente metodo, que funciona correctamente pero
     * presenta varios problemas de calidad. Tu tarea es refactorizarlo sin
     * alterar su comportamiento externo.
     *
     *      public void procesar(Pedido p) {
     *          double t = 0;
     *          for (int i = 0; i < p.getLineas().size(); i++) {
     *              t += p.getLineas().get(i).getCantidad() *
     *                   p.getLineas().get(i).getProducto().getPrecio();
     *          }
     *          if (p.getCliente().getTipo().equals("VIP")) { t = t * 0.9; }
     *          else if (p.getCliente().getTipo().equals("EMPLEADO")) { t = t * 0.8; }
     *          if (t > 100) { t = t + 0; } else { t = t + 5.95; }
     *          p.setTotal(t);
     *          System.out.println("Pedido procesado por " + t + " euros");
     *          java.sql.Connection c = java.sql.DriverManager.getConnection(URL, USER, PASS);
     *          c.createStatement().executeUpdate("UPDATE pedidos SET total=" + t);
     *      }
     *
     * Se pide:
     *   1. Identifica por escrito al menos cuatro problemas de calidad del
     *      metodo: nombres poco descriptivos, exceso de responsabilidades, alto
     *      acoplamiento, violacion de la Ley de Demeter y mezcla de capas.
     *   2. Extrae el calculo del subtotal, la aplicacion del descuento y el
     *      calculo de los gastos de envio a metodos privados independientes con
     *      nombres descriptivos.
     *   3. Sustituye los descuentos basados en cadenas de texto por un diseno
     *      polimorfico (subclases de Cliente o una interfaz), de modo que anadir
     *      un nuevo tipo de cliente no obligue a modificar este metodo.
     *   4. Elimina del metodo toda referencia a JDBC y a System.out: la
     *      persistencia y la presentacion deben quedar fuera de la logica de
     *      negocio.
     *   5. Corrige la violacion de la Ley de Demeter en el acceso encadenado a
     *      los datos del producto.
     *   6. Comprueba que el comportamiento externo (el total calculado) sigue
     *      siendo exactamente el mismo antes y despues de la refactorizacion.
     *
     * Pista: refactorizar significa mejorar la estructura interna SIN cambiar lo
     * que el codigo hace. Antes de tocar nada, anota los resultados que produce
     * el metodo con tres pedidos de ejemplo: seran tu red de seguridad para
     * comprobar que no has alterado su comportamiento.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * APARTADO 1: los problemas de calidad del metodo entregado.
     *
     * 1. NOMBRES POCO DESCRIPTIVOS. "procesar", "p", "t", "c", "i". Ninguno
     *    dice que se esta calculando. Hay que leer el cuerpo entero para
     *    descubrir que "t" es el total y que "procesar" en realidad cobra.
     *
     * 2. EXCESO DE RESPONSABILIDADES. Un solo metodo suma lineas, aplica
     *    descuentos, calcula gastos de envio, modifica el pedido, imprime por
     *    pantalla y escribe en la base de datos. Son seis motivos distintos por
     *    los que este metodo puede tener que cambiar; el principio de
     *    responsabilidad unica pide que sea uno.
     *
     * 3. MEZCLA DE CAPAS. La logica de negocio (el calculo), la presentacion
     *    (System.out) y la persistencia (JDBC) estan en el mismo sitio. La
     *    consecuencia practica es que este calculo no se puede probar sin base
     *    de datos ni reutilizar desde una aplicacion web, donde imprimir por la
     *    consola del servidor no sirve de nada.
     *
     * 4. ALTO ACOPLAMIENTO. El metodo depende directamente de DriverManager y
     *    de tres constantes de conexion. Cambiar de gestor de base de datos, o
     *    simplemente probar el calculo, obliga a tocar este codigo.
     *
     * 5. VIOLACION DE LA LEY DE DEMETER. La expresion
     *    p.getLineas().get(i).getProducto().getPrecio() atraviesa cuatro
     *    objetos. El metodo conoce la estructura interna de Pedido, de Linea y
     *    de Producto, asi que cualquier cambio en cualquiera de las tres lo
     *    rompe. La ley dice: habla solo con tus vecinos directos.
     *
     * 6. DESCUENTOS POR CADENA DE TEXTO. La cadena de if-else sobre "VIP" y
     *    "EMPLEADO" obliga a modificar este metodo cada vez que se anade un tipo
     *    de cliente, lo que incumple el principio de abierto-cerrado. Ademas un
     *    error de escritura ("Vip") no da error de compilacion: simplemente no
     *    aplica el descuento y nadie se entera.
     *
     * 7. CODIGO MUERTO. La rama "if (t > 100) { t = t + 0; }" no hace nada.
     *    Sumar cero es una operacion inutil que solo sirve para confundir a
     *    quien lee.
     *
     * 8. RECURSO NO CERRADO. La Connection se abre y no se cierra nunca. Cada
     *    pedido procesado deja una conexion abierta hasta agotar el pool.
     *
     * DOS DEFECTOS QUE NO SON DE ESTILO, SINO ERRORES REALES:
     *
     *   a) El UPDATE no tiene clausula WHERE: pone el mismo total a TODOS los
     *      pedidos de la tabla.
     *   b) El total se concatena directamente a la sentencia SQL, que es la
     *      puerta de entrada de la inyeccion de SQL.
     *
     * Estos dos no se pueden "refactorizar", porque corregirlos SI cambia el
     * comportamiento. Lo correcto es dejarlos anotados y avisar, no arreglarlos
     * a escondidas dentro de una refactorizacion: quien revise el cambio espera
     * que el comportamiento sea identico.
     */

    // ---------------------------------------------------------------------
    // MODELO DE DATOS
    // ---------------------------------------------------------------------

    /** Producto del catalogo. */
    public static class Producto {

        private final String nombre;
        private final double precio;

        /**
         * @param nombre nombre del producto
         * @param precio precio unitario en euros
         */
        public Producto(String nombre, double precio) {
            this.nombre = nombre;
            this.precio = precio;
        }

        /** @return nombre del producto */
        public String getNombre() { return nombre; }

        /** @return precio unitario en euros */
        public double getPrecio() { return precio; }
    }

    /** Linea de un pedido: un producto y una cantidad. */
    public static class LineaPedido {

        private final Producto producto;
        private final int cantidad;

        /**
         * @param producto producto pedido
         * @param cantidad unidades
         */
        public LineaPedido(Producto producto, int cantidad) {
            this.producto = producto;
            this.cantidad = cantidad;
        }

        /** @return producto de la linea */
        public Producto getProducto() { return producto; }

        /** @return unidades pedidas */
        public int getCantidad() { return cantidad; }

        /**
         * Apartado 5: importe de la linea.
         *
         * <p>Este es el metodo que corrige la violacion de la Ley de Demeter.
         * El precio lo conoce la linea, que es quien tiene el producto delante;
         * quien calcula el total ya no necesita saber que una linea contiene un
         * producto ni que un producto tiene precio. Solo pregunta cuanto
         * importa la linea.</p>
         *
         * @return cantidad por precio unitario
         */
        public double getImporte() {
            return cantidad * producto.getPrecio();
        }
    }

    /**
     * Apartado 3: cliente. Ahora es abstracto y cada tipo sabe su descuento.
     *
     * <p>El descuento deja de ser un dato que alguien interpreta desde fuera y
     * pasa a ser un comportamiento del propio cliente. Anadir un tipo nuevo es
     * escribir una subclase; el calculo del total no se toca.</p>
     */
    public abstract static class Cliente {

        private final String nombre;

        /**
         * @param nombre nombre del cliente
         */
        protected Cliente(String nombre) {
            this.nombre = nombre;
        }

        /** @return nombre del cliente */
        public String getNombre() { return nombre; }

        /**
         * Etiqueta del tipo de cliente.
         *
         * <p>Se conserva porque el metodo original la usa y porque sigue siendo
         * util para listados e informes. Lo que desaparece es DECIDIR a partir
         * de ella.</p>
         *
         * @return etiqueta del tipo
         */
        public abstract String getTipo();

        /**
         * Descuento que corresponde a este cliente.
         *
         * @return fraccion entre 0 y 1
         */
        public abstract double getPorcentajeDescuento();
    }

    /** Cliente sin descuento. */
    public static class ClienteEstandar extends Cliente {

        /**
         * @param nombre nombre del cliente
         */
        public ClienteEstandar(String nombre) { super(nombre); }

        @Override
        public String getTipo() { return "ESTANDAR"; }

        @Override
        public double getPorcentajeDescuento() { return 0.0; }
    }

    /** Cliente VIP: 10 por ciento de descuento. */
    public static class ClienteVip extends Cliente {

        /**
         * @param nombre nombre del cliente
         */
        public ClienteVip(String nombre) { super(nombre); }

        @Override
        public String getTipo() { return "VIP"; }

        @Override
        public double getPorcentajeDescuento() { return 0.10; }
    }

    /** Cliente empleado: 20 por ciento de descuento. */
    public static class ClienteEmpleado extends Cliente {

        /**
         * @param nombre nombre del cliente
         */
        public ClienteEmpleado(String nombre) { super(nombre); }

        @Override
        public String getTipo() { return "EMPLEADO"; }

        @Override
        public double getPorcentajeDescuento() { return 0.20; }
    }

    /**
     * Tipo NUEVO, anadido despues de la refactorizacion.
     *
     * <p>Es la prueba del apartado 3: esta clase se ha escrito sin tocar ni una
     * linea del calculo del total. Con la version original habria habido que
     * abrir el metodo procesar y anadirle otro else if.</p>
     */
    public static class ClienteEstudiante extends Cliente {

        /**
         * @param nombre nombre del cliente
         */
        public ClienteEstudiante(String nombre) { super(nombre); }

        @Override
        public String getTipo() { return "ESTUDIANTE"; }

        @Override
        public double getPorcentajeDescuento() { return 0.15; }
    }

    /** Pedido: un cliente y sus lineas. */
    public static class Pedido {

        private final String codigo;
        private final Cliente cliente;
        private final List<LineaPedido> lineas = new ArrayList<>();
        private double total;

        /**
         * @param codigo  identificador del pedido
         * @param cliente cliente que lo realiza
         */
        public Pedido(String codigo, Cliente cliente) {
            this.codigo = codigo;
            this.cliente = cliente;
        }

        /** @return identificador del pedido */
        public String getCodigo() { return codigo; }

        /** @return cliente que realiza el pedido */
        public Cliente getCliente() { return cliente; }

        /** @return lineas del pedido */
        public List<LineaPedido> getLineas() { return lineas; }

        /**
         * Anade una linea al pedido.
         *
         * @param producto producto pedido
         * @param cantidad unidades
         */
        public void anadirLinea(Producto producto, int cantidad) {
            lineas.add(new LineaPedido(producto, cantidad));
        }

        /** @return total calculado */
        public double getTotal() { return total; }

        /**
         * @param total total calculado
         */
        public void setTotal(double total) { this.total = total; }
    }

    // ---------------------------------------------------------------------
    // VERSION ORIGINAL (la red de seguridad de la pista)
    // ---------------------------------------------------------------------

    /**
     * El metodo tal y como se entrega, para poder comparar resultados.
     *
     * <p>Unica diferencia con el original: donde el original abre una conexion
     * JDBC real, aqui se llama a un repositorio simulado que apunta la sentencia
     * que se habria ejecutado. El calculo, que es lo que hay que preservar, esta
     * copiado linea por linea.</p>
     */
    public static class VersionOriginal {

        /** Sentencias SQL que este metodo habria lanzado. */
        private final List<String> sqlEjecutado = new ArrayList<>();

        /** Lineas que este metodo habria impreso por pantalla. */
        private final List<String> impreso = new ArrayList<>();

        /**
         * El metodo original, con sus nombres y su estructura intactos.
         *
         * @param p pedido a procesar
         */
        public void procesar(Pedido p) {
            double t = 0;
            for (int i = 0; i < p.getLineas().size(); i++) {
                t += p.getLineas().get(i).getCantidad()
                        * p.getLineas().get(i).getProducto().getPrecio();
            }
            if (p.getCliente().getTipo().equals("VIP")) { t = t * 0.9; }
            else if (p.getCliente().getTipo().equals("EMPLEADO")) { t = t * 0.8; }
            if (t > 100) { t = t + 0; } else { t = t + 5.95; }
            p.setTotal(t);
            impreso.add("Pedido procesado por " + t + " euros");
            // Aqui iba el JDBC. Se deja constancia de la sentencia exacta para
            // que se vea el UPDATE sin WHERE y la concatenacion del importe.
            sqlEjecutado.add("UPDATE pedidos SET total=" + t);
        }

        /** @return sentencias SQL simuladas */
        public List<String> getSqlEjecutado() { return sqlEjecutado; }

        /** @return lineas impresas simuladas */
        public List<String> getImpreso() { return impreso; }
    }

    // ---------------------------------------------------------------------
    // VERSION REFACTORIZADA
    // ---------------------------------------------------------------------

    /**
     * Apartado 4: contrato de persistencia.
     *
     * <p>La logica de negocio depende de esta interfaz, no de JDBC. Eso es lo
     * que permite probarla con una implementacion de mentira y cambiar de
     * tecnologia de almacenamiento sin tocar el calculo.</p>
     */
    public interface RepositorioPedidos {

        /**
         * Guarda el total calculado de un pedido.
         *
         * @param pedido pedido ya calculado
         */
        void actualizarTotal(Pedido pedido);
    }

    /** Repositorio de prueba: apunta lo que se le pide en lugar de guardarlo. */
    public static class RepositorioEnMemoria implements RepositorioPedidos {

        private final List<String> operaciones = new ArrayList<>();

        @Override
        public void actualizarTotal(Pedido pedido) {
            // Con parametros y con WHERE, que es como deberia haber sido desde
            // el principio. Se deja constancia para el listado.
            operaciones.add("UPDATE pedidos SET total=? WHERE codigo=?  ["
                    + String.format("%.2f", pedido.getTotal()) + ", " + pedido.getCodigo() + "]");
        }

        /** @return operaciones registradas */
        public List<String> getOperaciones() { return operaciones; }
    }

    /**
     * Apartados 2, 3, 4 y 5: la calculadora de totales, ya limpia.
     *
     * <p>Esta clase solo calcula. No imprime, no guarda y no sabe que existe una
     * base de datos. Por eso se puede probar con un test de dos lineas.</p>
     */
    public static class CalculadoraPedido {

        /** Importe a partir del cual el envio es gratuito. */
        private static final double UMBRAL_ENVIO_GRATIS = 100.0;

        /** Gastos de envio cuando no se alcanza el umbral. */
        private static final double GASTOS_ENVIO = 5.95;

        /**
         * Calcula el total del pedido: subtotal, descuento y gastos de envio.
         *
         * @param pedido pedido a calcular
         * @return importe total en euros
         */
        public double calcularTotal(Pedido pedido) {
            double subtotal = calcularSubtotal(pedido);
            double conDescuento = aplicarDescuento(subtotal, pedido.getCliente());
            // Ojo al orden: el original comprueba el umbral DESPUES del
            // descuento, asi que un pedido de 110 euros que baja a 99 si paga
            // envio. Puede parecer discutible, pero es el comportamiento
            // entregado y una refactorizacion no lo cambia.
            return conDescuento + calcularGastosEnvio(conDescuento);
        }

        /**
         * Apartado 2: suma de los importes de las lineas.
         *
         * @param pedido pedido a sumar
         * @return subtotal sin descuentos ni envio
         */
        private double calcularSubtotal(Pedido pedido) {
            double subtotal = 0;
            // Apartado 5: cada linea se calcula su importe. Ni un solo
            // encadenamiento de getters.
            for (LineaPedido linea : pedido.getLineas()) {
                subtotal += linea.getImporte();
            }
            return subtotal;
        }

        /**
         * Apartados 2 y 3: aplica el descuento que corresponda al cliente.
         *
         * @param subtotal importe antes del descuento
         * @param cliente  cliente del pedido
         * @return importe con el descuento aplicado
         */
        private double aplicarDescuento(double subtotal, Cliente cliente) {
            // Se pregunta al cliente. Ni un if sobre el tipo.
            return subtotal * (1 - cliente.getPorcentajeDescuento());
        }

        /**
         * Apartado 2: gastos de envio segun el importe.
         *
         * @param importe importe sobre el que se decide
         * @return gastos de envio, cero si supera el umbral
         */
        private double calcularGastosEnvio(double importe) {
            // La rama "sumar cero" del original desaparece: devolver 0 dice lo
            // mismo y se lee de un vistazo.
            return (importe > UMBRAL_ENVIO_GRATIS) ? 0.0 : GASTOS_ENVIO;
        }
    }

    /**
     * Apartado 4: el servicio que orquesta. Calcula, guarda y devuelve.
     *
     * <p>Aqui vuelven a juntarse las tres cosas, pero cada una en su sitio: el
     * calculo lo hace la calculadora, la persistencia el repositorio y la
     * presentacion es cosa de quien llame. El servicio se limita a coordinar.</p>
     */
    public static class ServicioPedidos {

        private final CalculadoraPedido calculadora;
        private final RepositorioPedidos repositorio;

        /**
         * @param calculadora calculadora de totales
         * @param repositorio destino donde persistir el total
         */
        public ServicioPedidos(CalculadoraPedido calculadora, RepositorioPedidos repositorio) {
            this.calculadora = calculadora;
            this.repositorio = repositorio;
        }

        /**
         * Calcula el total del pedido y lo persiste.
         *
         * @param pedido pedido a procesar
         * @return el total calculado
         */
        public double procesarPedido(Pedido pedido) {
            double total = calculadora.calcularTotal(pedido);
            pedido.setTotal(total);
            repositorio.actualizarTotal(pedido);
            return total;
        }
    }

    // =====================================================================
    // COMPROBACION (apartado 6)
    // =====================================================================

    /**
     * Construye los pedidos de prueba. Se crean dos veces, una para cada
     * version, porque procesar() modifica el pedido y no se puede reutilizar el
     * mismo objeto para las dos medidas.
     *
     * @return lista de pedidos de ejemplo
     */
    private static List<Pedido> pedidosDePrueba() {
        Producto teclado = new Producto("Teclado mecanico", 30.00);
        Producto monitor = new Producto("Monitor 24 pulgadas", 40.00);
        Producto portatil = new Producto("Portatil", 130.00);
        Producto raton = new Producto("Raton", 5.00);
        Producto silla = new Producto("Silla de oficina", 110.00);

        List<Pedido> pedidos = new ArrayList<>();

        Pedido p1 = new Pedido("PED-1", new ClienteEstandar("Marta Ruiz"));
        p1.anadirLinea(teclado, 2);                       // 60.00, sin descuento, paga envio
        pedidos.add(p1);

        Pedido p2 = new Pedido("PED-2", new ClienteVip("Ana Lopez"));
        p2.anadirLinea(monitor, 3);                       // 120.00 -> 108.00, envio gratis
        pedidos.add(p2);

        Pedido p3 = new Pedido("PED-3", new ClienteEmpleado("Luis Gomez"));
        p3.anadirLinea(portatil, 1);
        p3.anadirLinea(raton, 2);                         // 140.00 -> 112.00, envio gratis
        pedidos.add(p3);

        // El caso interesante: el descuento lo hace bajar del umbral, asi que
        // acaba pagando envio. Es la clase de detalle que una refactorizacion
        // descuidada rompe sin que nadie se de cuenta.
        Pedido p4 = new Pedido("PED-4", new ClienteVip("Ana Lopez"));
        p4.anadirLinea(silla, 1);                         // 110.00 -> 99.00 + 5.95
        pedidos.add(p4);

        return pedidos;
    }

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2404-E14] Refactorizacion guiada por criterios de calidad");

        List<Pedido> antes = pedidosDePrueba();
        List<Pedido> despues = pedidosDePrueba();

        VersionOriginal original = new VersionOriginal();
        for (Pedido p : antes) {
            original.procesar(p);
        }

        RepositorioEnMemoria repositorio = new RepositorioEnMemoria();
        ServicioPedidos servicio = new ServicioPedidos(new CalculadoraPedido(), repositorio);
        for (Pedido p : despues) {
            servicio.procesarPedido(p);
        }

        // Apartado 6: la comprobacion que da sentido al ejercicio. Se comparan
        // los bits exactos del double, no una aproximacion: si la
        // refactorizacion hubiera cambiado el orden de las operaciones, el
        // redondeo en coma flotante lo delataria aqui.
        System.out.println("  Comparacion antes / despues:");
        boolean identico = true;
        for (int i = 0; i < antes.size(); i++) {
            double a = antes.get(i).getTotal();
            double d = despues.get(i).getTotal();
            boolean igual = Double.compare(a, d) == 0;
            identico = identico && igual;
            System.out.printf("    %-6s %-10s original %8.2f   refactorizado %8.2f   %s%n",
                    antes.get(i).getCodigo(),
                    antes.get(i).getCliente().getTipo(),
                    a, d,
                    igual ? "identico" : "DIFERENTE");
        }
        System.out.println("  Comportamiento externo preservado: " + (identico ? "SI" : "NO"));

        // Apartado 3: el tipo nuevo, sin tocar el calculo.
        Pedido estudiante = new Pedido("PED-5", new ClienteEstudiante("Carlos Diaz"));
        estudiante.anadirLinea(new Producto("Monitor 24 pulgadas", 40.00), 3);
        double totalEstudiante = servicio.procesarPedido(estudiante);
        System.out.printf("  Tipo de cliente nuevo (ESTUDIANTE, 15 por ciento): %.2f EUR%n", totalEstudiante);
        System.out.println("    No ha hecho falta modificar CalculadoraPedido: solo anadir una subclase");

        // Apartado 4: lo que antes estaba dentro del metodo, ahora fuera.
        System.out.println("  Persistencia (antes, dentro del metodo de negocio):");
        System.out.println("    " + original.getSqlEjecutado().get(0) + "   <- sin WHERE y con el importe concatenado");
        System.out.println("  Persistencia (despues, en el repositorio):");
        System.out.println("    " + repositorio.getOperaciones().get(0));
        System.out.println("  Presentacion (antes): " + original.getImpreso().get(0));
        System.out.println("    Ahora imprime quien llama al servicio, no la logica de negocio");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
