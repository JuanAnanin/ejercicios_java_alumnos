package com.ifcd0112.ejercicios.uf2404.bloque4_excepciones;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * UF2404 - BLOQUE 4 - EJERCICIO 12: Comunicacion por sockets, servidor de consultas.
 *
 * <p>Criterios de evaluacion: CE2.10</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej12Sockets {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Desarrolla una aplicacion cliente-servidor mediante sockets TCP en la que
     * el servidor responda a consultas sencillas enviadas por el cliente.
     *
     * Se pide:
     *   1. El servidor debe escuchar en el puerto 5000 y aceptar la conexion de
     *      un cliente.
     *   2. El cliente envia lineas de texto con comandos. El servidor debe
     *      reconocer al menos: HORA (devuelve la hora actual),
     *      MAYUS <texto> (devuelve el texto en mayusculas) e
     *      INVERTIR <texto> (devuelve el texto invertido).
     *   3. Ante un comando no reconocido, el servidor debe responder con un
     *      mensaje de error, sin cerrar la conexion.
     *   4. La conexion se cierra ordenadamente cuando el cliente envia SALIR.
     *   5. Gestiona correctamente las excepciones de entrada/salida en ambos
     *      programas y asegurate de cerrar todos los recursos.
     *   6. AMPLIACION: modifica el servidor para que pueda atender varios
     *      clientes simultaneamente, lanzando un hilo por cada conexion aceptada.
     *
     * Pista: para probarlo necesitas ejecutar dos programas a la vez: arranca
     * primero el servidor y despues el cliente, cada uno en su propia ventana de
     * ejecucion del entorno de desarrollo.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * NOTA SOBRE COMO EJECUTAR ESTE EJERCICIO.
     *
     * La pista del enunciado plantea el modo clasico: dos ventanas, una con el
     * servidor y otra con el cliente. Ese modo SIGUE FUNCIONANDO aqui:
     *
     *      java com.ifcd0112.ejercicios.uf2404.bloque4_excepciones.Ej12Sockets$Servidor
     *      java com.ifcd0112.ejercicios.uf2404.bloque4_excepciones.Ej12Sockets$Cliente
     *
     * (en Windows hay que escapar el dolar: Ej12Sockets"$"Servidor)
     *
     * Pero el runner del bloque no puede abrir dos ventanas ni quedarse
     * bloqueado esperando a que alguien teclee comandos. Por eso resolver()
     * arranca el servidor en un hilo aparte del mismo proceso y ejecuta despues
     * un cliente con una lista de comandos ya escrita. La comunicacion es real:
     * viaja por sockets TCP sobre la interfaz de loopback, exactamente igual que
     * si fueran dos maquinas distintas. Lo unico que cambia es quien teclea.
     */

    /** Puerto que pide el enunciado (apartado 1). */
    public static final int PUERTO = 5000;

    /** Formato de la hora que devuelve el comando HORA. */
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

    // ---------------------------------------------------------------------
    // SERVIDOR
    // ---------------------------------------------------------------------

    /**
     * Servidor de consultas. Escucha en un puerto TCP y atiende a cada cliente
     * en su propio hilo (apartado 6).
     */
    public static class Servidor implements Runnable {

        private final int puertoSolicitado;
        private ServerSocket servidor;
        private volatile boolean activo;

        /**
         * @param puerto puerto en el que escuchar; 0 significa "el primero que
         *               el sistema tenga libre", util para pruebas automaticas
         */
        public Servidor(int puerto) {
            this.puertoSolicitado = puerto;
        }

        /**
         * Abre el socket de escucha.
         *
         * @return el puerto realmente abierto
         * @throws IOException si el puerto esta ocupado o no se puede abrir
         */
        public int abrir() throws IOException {
            // Se escucha solo en loopback: este servidor es un ejercicio de
            // clase, no tiene ninguna autenticacion y no debe quedar expuesto a
            // la red del aula.
            servidor = new ServerSocket(puertoSolicitado, 50, InetAddress.getLoopbackAddress());
            activo = true;
            return servidor.getLocalPort();
        }

        /**
         * Apartados 1 y 6: bucle de aceptacion. Por cada conexion aceptada se
         * lanza un hilo, de modo que el bucle vuelve inmediatamente a aceptar la
         * siguiente y varios clientes pueden estar conectados a la vez.
         */
        @Override
        public void run() {
            while (activo) {
                try {
                    Socket cliente = servidor.accept();
                    Thread atencion = new Thread(() -> atender(cliente));
                    // Demonio: si el programa principal termina, este hilo no
                    // debe impedir que la JVM se cierre.
                    atencion.setDaemon(true);
                    atencion.start();
                } catch (IOException e) {
                    // Cuando se llama a parar() se cierra el ServerSocket y
                    // accept() lanza excepcion. Si ya no estamos activos, es la
                    // parada ordenada y no un error.
                    if (activo) {
                        System.out.println("  [servidor] error aceptando conexion: " + e.getMessage());
                    }
                }
            }
        }

        /**
         * Apartados 2, 3, 4 y 5: dialogo con UN cliente hasta que envia SALIR.
         *
         * @param socket conexion ya aceptada
         */
        private void atender(Socket socket) {
            // try-with-resources: cierra socket, lector y escritor pase lo que
            // pase, incluso si salta una excepcion a mitad del dialogo. Es lo
            // que pide el apartado 5 y evita fugas de descriptores.
            try (Socket s = socket;
                 BufferedReader entrada = new BufferedReader(
                         new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
                 PrintWriter salida = new PrintWriter(
                         new java.io.OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8), true)) {

                salida.println("OK Servidor de consultas listo. Comandos: HORA, MAYUS, INVERTIR, SALIR");

                String linea;
                while ((linea = entrada.readLine()) != null) {
                    String respuesta = procesar(linea);
                    if (respuesta == null) {
                        // null es la senal interna de SALIR: se despide y sale
                        // del bucle, cerrando la conexion de forma ordenada.
                        salida.println("OK Hasta luego");
                        break;
                    }
                    salida.println(respuesta);
                }
            } catch (IOException e) {
                System.out.println("  [servidor] conexion perdida: " + e.getMessage());
            }
        }

        /** Detiene el servidor cerrando el socket de escucha. */
        public void parar() {
            activo = false;
            try {
                if (servidor != null) {
                    servidor.close();
                }
            } catch (IOException e) {
                // Cerrando: no hay nada util que hacer con el error.
            }
        }
    }

    /**
     * Apartados 2 y 3: interpreta un comando y devuelve la respuesta.
     *
     * <p>Se ha separado de la clase Servidor a proposito: asi la logica de los
     * comandos se puede probar sin abrir un solo socket, que es una de las
     * ventajas de no mezclar la comunicacion con el procesamiento.</p>
     *
     * @param linea comando recibido del cliente
     * @return la respuesta a enviar, o null si el comando es SALIR
     */
    public static String procesar(String linea) {
        String texto = linea.trim();
        if (texto.isEmpty()) {
            return "ERROR Comando vacio";
        }

        // El comando va separado del argumento por el primer espacio. Se limita
        // el split a 2 trozos para que MAYUS "hola que tal" conserve el
        // argumento entero y no se quede solo con "hola".
        String[] partes = texto.split("\\s+", 2);
        String comando = partes[0].toUpperCase();
        String argumento = (partes.length > 1) ? partes[1] : "";

        switch (comando) {
            case "HORA":
                return "OK " + LocalTime.now().format(FORMATO_HORA);

            case "MAYUS":
                if (argumento.isEmpty()) {
                    return "ERROR MAYUS necesita un texto";
                }
                return "OK " + argumento.toUpperCase();

            case "INVERTIR":
                if (argumento.isEmpty()) {
                    return "ERROR INVERTIR necesita un texto";
                }
                return "OK " + new StringBuilder(argumento).reverse();

            case "SALIR":
                return null;

            default:
                // Apartado 3: se responde con error pero NO se cierra la
                // conexion. Devolver una respuesta y seguir escuchando es lo que
                // distingue un servidor robusto de uno que se cae al primer
                // dedazo del usuario.
                return "ERROR Comando no reconocido: " + comando;
        }
    }

    // ---------------------------------------------------------------------
    // CLIENTE
    // ---------------------------------------------------------------------

    /** Cliente de consola: lee comandos del teclado y los envia al servidor. */
    public static class Cliente {

        /**
         * Ejecuta una sesion completa contra el servidor.
         *
         * @param puerto   puerto del servidor
         * @param comandos comandos a enviar; si es null, se leen del teclado
         * @param prefijo  texto con el que se indenta cada linea impresa
         */
        public static void sesion(int puerto, String[] comandos, String prefijo) {
            try (Socket socket = new Socket(InetAddress.getLoopbackAddress(), puerto);
                 BufferedReader delServidor = new BufferedReader(
                         new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                 PrintWriter alServidor = new PrintWriter(
                         new java.io.OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

                System.out.println(prefijo + "<- " + delServidor.readLine());   // saludo inicial

                if (comandos == null) {
                    // Modo interactivo: el del enunciado, con dos ventanas.
                    try (BufferedReader teclado = new BufferedReader(
                            new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
                        String linea;
                        while ((linea = teclado.readLine()) != null) {
                            alServidor.println(linea);
                            System.out.println(prefijo + "<- " + delServidor.readLine());
                            if ("SALIR".equalsIgnoreCase(linea.trim())) {
                                break;
                            }
                        }
                    }
                } else {
                    // Modo guionizado: el que usa resolver() para demostrar.
                    for (String comando : comandos) {
                        System.out.println(prefijo + "-> " + comando);
                        alServidor.println(comando);
                        System.out.println(prefijo + "<- " + delServidor.readLine());
                    }
                }
            } catch (IOException e) {
                // Apartado 5: el cliente tambien tiene que sobrevivir a que el
                // servidor no este levantado, que es el fallo mas frecuente.
                System.out.println(prefijo + "No se pudo hablar con el servidor: " + e.getMessage());
            }
        }

        /** Cliente interactivo contra el puerto 5000. */
        public static void main(String[] args) {
            System.out.println("Cliente conectado al puerto " + PUERTO + ". Escribe comandos (SALIR para terminar).");
            sesion(PUERTO, null, "");
        }
    }

    /** Servidor autonomo en el puerto 5000, para la prueba con dos ventanas. */
    public static class ServidorPrincipal {
        public static void main(String[] args) {
            Servidor servidor = new Servidor(PUERTO);
            try {
                int puerto = servidor.abrir();
                System.out.println("Servidor escuchando en el puerto " + puerto + ". Ctrl+C para parar.");
                servidor.run();
            } catch (IOException e) {
                System.out.println("No se pudo abrir el puerto " + PUERTO + ": " + e.getMessage());
            }
        }
    }

    // =====================================================================
    // COMPROBACION
    // =====================================================================

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2404-E12] Comunicacion por sockets: servidor de consultas");

        // Se intenta el puerto del enunciado. Si esta ocupado (algo muy normal:
        // en macOS lo usa AirPlay), se pide al sistema uno libre y se dice cual,
        // en lugar de fallar y dejar el ejercicio sin demostrar.
        Servidor servidor = new Servidor(PUERTO);
        int puerto;
        try {
            puerto = servidor.abrir();
        } catch (IOException ocupado) {
            System.out.println("  El puerto " + PUERTO + " esta ocupado (" + ocupado.getMessage()
                    + "), se usa uno libre");
            servidor = new Servidor(0);
            try {
                puerto = servidor.abrir();
            } catch (IOException e) {
                System.out.println("  No se pudo abrir ningun puerto: " + e.getMessage());
                System.out.println();
                return;
            }
        }
        System.out.println("  Servidor escuchando en el puerto " + puerto);

        Thread hiloServidor = new Thread(servidor);
        hiloServidor.setDaemon(true);
        hiloServidor.start();

        // Apartados 2, 3 y 4 con un cliente.
        String[] guion = {
            "HORA",
            "MAYUS hola que tal",
            "INVERTIR programacion",
            "BAILAR un vals",          // apartado 3: no reconocido, sigue vivo
            "MAYUS sigo conectado",    // prueba de que efectivamente sigue vivo
            "SALIR"                    // apartado 4: cierre ordenado
        };
        Cliente.sesion(puerto, guion, "    ");

        // Apartado 6: dos clientes a la vez sobre el mismo servidor.
        System.out.println("  Ampliacion: dos clientes simultaneos");
        final int p = puerto;
        Thread c1 = new Thread(() -> Cliente.sesion(p,
                new String[] { "MAYUS cliente uno", "SALIR" }, "    [A] "));
        Thread c2 = new Thread(() -> Cliente.sesion(p,
                new String[] { "INVERTIR cliente dos", "SALIR" }, "    [B] "));
        c1.start();
        c2.start();
        try {
            c1.join();
            c2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        servidor.parar();
        System.out.println("  Servidor detenido");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
