package com.ifcd0112.ejercicios.ampliacion.uf2405;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * AMPLIACION UF2405 - EJERCICIO A5: Protocolos y formatos de intercambio.
 *
 * <p>Criterios de evaluacion: CE1.2, CE1.1</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class A05ProtocolosFormatos {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Has programado Servlets sin ver nunca lo que viaja de verdad entre el
     * navegador y el servidor. Este ejercicio levanta la tapa: HTTP es texto
     * plano sobre una conexion TCP, y se puede escribir a mano.
     *
     * Se pide:
     *   1. Escribe un servidor minimo que escuche en un puerto, acepte una
     *      conexion TCP y responda con una respuesta HTTP correcta. No uses
     *      ningun contenedor web: solo sockets.
     *   2. Escribe un cliente que envie una peticion GET escrita a mano y
     *      muestre por pantalla, linea a linea, lo que envia y lo que recibe.
     *   3. Identifica en esa conversacion las cuatro partes de una peticion
     *      (metodo, ruta, version y cabeceras) y las de una respuesta (version,
     *      codigo de estado, cabeceras, linea en blanco y cuerpo).
     *   4. Repite el intercambio con una peticion POST que lleve datos en el
     *      cuerpo. Explica que cabecera indica donde termina ese cuerpo y que
     *      pasaria si faltara.
     *   5. Provoca una respuesta 404 y otra 302. Indica que hace el navegador
     *      con cada codigo y relaciona el 302 con el patron POST-Redirect-GET
     *      del ejercicio 11.
     *   6. Analiza un documento XML con las clases del propio JDK, extrayendo
     *      los datos de una lista de mascotas.
     *   7. Analiza un mensaje SOAP, que no es mas que un XML con una estructura
     *      convenida, e identifica su sobre, su cabecera y su cuerpo.
     *   8. Genera la misma informacion en JSON y compara los tres formatos:
     *      cuando usarias cada uno.
     *
     * Pista: HTTP separa las cabeceras del cuerpo con una LINEA EN BLANCO. Ese
     * detalle, que parece trivial, es lo que permite al receptor saber donde
     * dejan de ser metadatos y empiezan a ser datos.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * APARTADO 3: la anatomia del intercambio.
     *
     *   PETICION
     *      GET /mascotas HTTP/1.1        <- linea de peticion: metodo, ruta, version
     *      Host: localhost:8080          <- cabeceras, una por linea
     *      Accept: text/html
     *                                    <- LINEA EN BLANCO: aqui acaban las cabeceras
     *      (cuerpo, solo si lo hay)
     *
     *   RESPUESTA
     *      HTTP/1.1 200 OK               <- version, codigo y texto del estado
     *      Content-Type: text/html; charset=UTF-8
     *      Content-Length: 137
     *                                    <- LINEA EN BLANCO
     *      <html>...</html>              <- cuerpo
     *
     * Todo lo que hace un Servlet se reduce a esto. request.getParameter lee de
     * la peticion; response.setContentType escribe una cabecera; el PrintWriter
     * escribe el cuerpo. Y de aqui sale la razon de la pista del ejercicio 10:
     * setContentType tiene que llamarse ANTES de escribir nada porque las
     * cabeceras van FISICAMENTE antes en el flujo. Una vez enviada la linea en
     * blanco, ya no se puede volver atras.
     *
     * APARTADO 4: Content-Length.
     *
     * TCP entrega un flujo continuo de bytes, sin marcas de fin. El receptor
     * necesita saber cuantos bytes de cuerpo tiene que leer, y eso lo dice
     * Content-Length. Si falta, el receptor no sabe si el mensaje ha terminado o
     * si faltan datos por llegar, y se queda esperando hasta que la conexion se
     * cierra o salta el tiempo de espera.
     *
     * La alternativa moderna es Transfer-Encoding: chunked, que trocea el cuerpo
     * y marca el final con un trozo de tamano cero. Se usa cuando el servidor
     * empieza a responder sin saber todavia cuanto va a ocupar la respuesta.
     *
     * APARTADO 5: los codigos de estado.
     *
     *   2xx  correcto.        200 OK, 201 Created tras un alta.
     *   3xx  redireccion.     302 Found y 303 See Other: el navegador pide otra
     *                         direccion. Es EXACTAMENTE lo que hace sendRedirect
     *                         en el ejercicio 11, y por eso el patron se llama
     *                         POST-Redirect-GET: el 302 convierte la ultima
     *                         peticion del navegador en un GET recargable.
     *   4xx  culpa del cliente. 400 mal formada, 401 sin autenticar, 403 sin
     *                         permiso, 404 no existe.
     *   5xx  culpa del servidor. 500 es la excepcion no capturada del Servlet.
     *
     * La division entre 4xx y 5xx no es burocracia: decide a quien hay que
     * avisar. Un 4xx lo arregla quien llama; un 5xx lo arregla el equipo.
     *
     * APARTADO 8: XML, JSON y SOAP.
     *
     *   XML    Verboso, pero con esquema (XSD) que permite VALIDAR la estructura
     *          y con transformaciones (XSL). Sigue mandando en banca,
     *          administracion publica y facturacion electronica, donde el
     *          formato esta legislado.
     *   JSON   Mucho mas ligero, se convierte solo en objetos de JavaScript.
     *          Es el formato de facto de las API web actuales. No lleva
     *          comentarios ni tipo fecha, y su validacion (JSON Schema) es mas
     *          reciente y menos usada.
     *   SOAP   No es un formato alternativo: es un PROTOCOLO que viaja dentro de
     *          XML, con sobre, cabecera y cuerpo. Aporta seguridad, transacciones
     *          y contrato formal (WSDL). Hoy se ve sobre todo en integraciones
     *          empresariales antiguas; para servicios nuevos se usa REST + JSON.
     *
     * Criterio: si el formato lo impone un tercero, no hay eleccion. Si hay que
     * validar contra un esquema legal, XML. Para una API propia consumida desde
     * un navegador, JSON.
     *
     * Y una advertencia que conviene dar: el JDK trae analizador de XML pero NO
     * de JSON. Aqui el JSON se genera a mano, que es facil; analizarlo a mano no
     * lo es, y en un proyecto real se usa Jackson o Gson.
     */

    /** Puerto por defecto del servidor de pruebas. */
    public static final int PUERTO = 8080;

    // ---------------------------------------------------------------------
    // Apartados 1, 4 y 5: el servidor HTTP minimo
    // ---------------------------------------------------------------------

    /** Servidor HTTP de una sola pieza, hecho solo con sockets. */
    public static class ServidorHttp implements Runnable {

        private ServerSocket servidor;
        private volatile boolean activo;

        /**
         * Abre el socket de escucha, solo en la interfaz de bucle local.
         *
         * @param puerto puerto solicitado; 0 para que lo elija el sistema
         * @return el puerto realmente abierto
         * @throws IOException si no se puede abrir
         */
        public int abrir(int puerto) throws IOException {
            servidor = new ServerSocket(puerto, 10, InetAddress.getLoopbackAddress());
            activo = true;
            return servidor.getLocalPort();
        }

        @Override
        public void run() {
            while (activo) {
                try (Socket cliente = servidor.accept()) {
                    atender(cliente);
                } catch (IOException e) {
                    if (activo) {
                        System.out.println("      [servidor] " + e.getMessage());
                    }
                }
            }
        }

        /**
         * Lee una peticion HTTP y escribe la respuesta que corresponda.
         *
         * @param socket conexion aceptada
         * @throws IOException si falla la comunicacion
         */
        private void atender(Socket socket) throws IOException {
            BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

            String lineaPeticion = entrada.readLine();
            if (lineaPeticion == null) {
                return;
            }
            // La linea de peticion son tres partes separadas por espacios.
            String[] partes = lineaPeticion.split(" ");
            String metodo = partes[0];
            String ruta = (partes.length > 1) ? partes[1] : "/";

            // Cabeceras, hasta la LINEA EN BLANCO.
            int longitudCuerpo = 0;
            String linea;
            while ((linea = entrada.readLine()) != null && !linea.isEmpty()) {
                if (linea.toLowerCase().startsWith("content-length:")) {
                    longitudCuerpo = Integer.parseInt(linea.substring(15).trim());
                }
            }

            // Apartado 4: el cuerpo se lee EXACTAMENTE Content-Length caracteres.
            // Sin ese dato no se sabria cuando parar, y la lectura se quedaria
            // bloqueada esperando datos que no van a llegar.
            String cuerpo = "";
            if (longitudCuerpo > 0) {
                char[] buffer = new char[longitudCuerpo];
                int leidos = entrada.read(buffer, 0, longitudCuerpo);
                cuerpo = new String(buffer, 0, Math.max(leidos, 0));
            }

            OutputStream salida = socket.getOutputStream();
            if ("/mascotas".equals(ruta) && "GET".equals(metodo)) {
                responder(salida, 200, "OK", "text/html; charset=UTF-8",
                        "<html><body><h1>Mascotas</h1><p>Toby, Misu, Rocky</p></body></html>", null);
            } else if ("/altaMascota".equals(ruta) && "POST".equals(metodo)) {
                // Apartado 5: POST-Redirect-GET. Se responde 302 y el navegador
                // pedira /mascotas con un GET, que ya es recargable sin peligro.
                responder(salida, 302, "Found", "text/plain; charset=UTF-8",
                        "Datos recibidos: " + cuerpo, "/mascotas");
            } else {
                responder(salida, 404, "Not Found", "text/html; charset=UTF-8",
                        "<html><body><p>No existe " + ruta + "</p></body></html>", null);
            }
            salida.flush();
        }

        /**
         * Escribe una respuesta HTTP completa.
         *
         * @param salida       flujo hacia el cliente
         * @param codigo       codigo de estado
         * @param texto        texto del estado
         * @param tipo         valor de Content-Type
         * @param cuerpo       cuerpo de la respuesta
         * @param redireccion  destino de la cabecera Location, o null
         * @throws IOException si falla la escritura
         */
        private void responder(OutputStream salida, int codigo, String texto, String tipo,
                               String cuerpo, String redireccion) throws IOException {
            byte[] datos = cuerpo.getBytes(StandardCharsets.UTF_8);
            StringBuilder cabeceras = new StringBuilder();
            cabeceras.append("HTTP/1.1 ").append(codigo).append(' ').append(texto).append("\r\n");
            cabeceras.append("Content-Type: ").append(tipo).append("\r\n");
            // Se cuentan BYTES, no caracteres: con UTF-8 una enie ocupa dos.
            cabeceras.append("Content-Length: ").append(datos.length).append("\r\n");
            if (redireccion != null) {
                cabeceras.append("Location: ").append(redireccion).append("\r\n");
            }
            cabeceras.append("Connection: close\r\n");
            cabeceras.append("\r\n");        // la LINEA EN BLANCO
            salida.write(cabeceras.toString().getBytes(StandardCharsets.UTF_8));
            salida.write(datos);
        }

        /** Detiene el servidor. */
        public void parar() {
            activo = false;
            try {
                if (servidor != null) {
                    servidor.close();
                }
            } catch (IOException e) {
                // Cerrando: no hay nada util que hacer.
            }
        }
    }

    /**
     * Apartado 2: envia una peticion escrita a mano y muestra el intercambio.
     *
     * @param puerto   puerto del servidor
     * @param peticion texto completo de la peticion HTTP
     * @return la respuesta recibida, linea a linea
     */
    public static List<String> conversar(int puerto, String peticion) {
        List<String> respuesta = new ArrayList<>();
        try (Socket socket = new Socket(InetAddress.getLoopbackAddress(), puerto)) {
            socket.getOutputStream().write(peticion.getBytes(StandardCharsets.UTF_8));
            socket.getOutputStream().flush();
            BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            String linea;
            while ((linea = entrada.readLine()) != null) {
                respuesta.add(linea);
            }
        } catch (IOException e) {
            respuesta.add("[error] " + e.getMessage());
        }
        return respuesta;
    }

    // ---------------------------------------------------------------------
    // Apartados 6 y 7: XML y SOAP con las clases del JDK
    // ---------------------------------------------------------------------

    /**
     * Crea un analizador de XML configurado de forma segura.
     *
     * <p>Las dos opciones que se desactivan cierran el ataque XXE: un XML que
     * declara una entidad externa puede hacer que el analizador lea ficheros del
     * servidor o haga peticiones de red. Es el equivalente en XML a la inyeccion
     * SQL del ejercicio 6, y por defecto viene abierto.</p>
     *
     * @return analizador listo para usar
     * @throws ParserConfigurationException si la configuracion no es admitida
     */
    private static DocumentBuilder analizadorSeguro() throws ParserConfigurationException {
        DocumentBuilderFactory fabrica = DocumentBuilderFactory.newInstance();
        fabrica.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        fabrica.setXIncludeAware(false);
        fabrica.setExpandEntityReferences(false);
        return fabrica.newDocumentBuilder();
    }

    /**
     * Apartado 6: extrae los datos de las mascotas de un documento XML.
     *
     * @param xml documento a analizar
     * @return una linea por mascota
     * @throws ParserConfigurationException si el analizador no se puede crear
     * @throws SAXException                 si el XML esta mal formado
     * @throws IOException                  si falla la lectura
     */
    public static List<String> leerMascotasXml(String xml)
            throws ParserConfigurationException, SAXException, IOException {
        List<String> mascotas = new ArrayList<>();
        org.w3c.dom.Document doc = analizadorSeguro()
                .parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        NodeList nodos = doc.getElementsByTagName("mascota");
        for (int i = 0; i < nodos.getLength(); i++) {
            Element m = (Element) nodos.item(i);
            mascotas.add(m.getAttribute("id") + "  "
                    + m.getElementsByTagName("nombre").item(0).getTextContent() + "  "
                    + m.getElementsByTagName("especie").item(0).getTextContent());
        }
        return mascotas;
    }

    /**
     * Apartado 8: genera JSON a mano.
     *
     * @param mascotas lineas devueltas por leerMascotasXml
     * @return el mismo contenido en JSON
     */
    public static String generarJson(List<String> mascotas) {
        StringBuilder json = new StringBuilder("{\n  \"mascotas\": [\n");
        for (int i = 0; i < mascotas.size(); i++) {
            String[] campos = mascotas.get(i).trim().split("\\s{2,}");
            json.append("    { \"id\": ").append(campos[0])
                .append(", \"nombre\": \"").append(campos[1])
                .append("\", \"especie\": \"").append(campos[2]).append("\" }");
            json.append(i < mascotas.size() - 1 ? ",\n" : "\n");
        }
        return json.append("  ]\n}").toString();
    }

    // =====================================================================
    // COMPROBACION
    // =====================================================================

    /** Documento XML de ejemplo, el mismo dominio de la clinica veterinaria. */
    private static final String XML_MASCOTAS =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<clinica>\n"
            + "  <mascota id=\"1\"><nombre>Toby</nombre><especie>Perro</especie></mascota>\n"
            + "  <mascota id=\"2\"><nombre>Misu</nombre><especie>Gato</especie></mascota>\n"
            + "  <mascota id=\"3\"><nombre>Rocky</nombre><especie>Perro</especie></mascota>\n"
            + "</clinica>";

    /** Mensaje SOAP de ejemplo. */
    private static final String SOBRE_SOAP =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n"
            + "  <soap:Header>\n"
            + "    <autenticacion><usuario>recepcion</usuario></autenticacion>\n"
            + "  </soap:Header>\n"
            + "  <soap:Body>\n"
            + "    <consultarMascota><id>2</id></consultarMascota>\n"
            + "  </soap:Body>\n"
            + "</soap:Envelope>";

    /**
     * Muestra una peticion HTTP linea a linea, senalando la linea en blanco.
     *
     * @param peticion texto completo de la peticion
     */
    private static void mostrarPeticion(String peticion) {
        String[] lineas = peticion.split("\r\n", -1);
        // El mensaje termina en \r\n, asi que el split deja un elemento vacio
        // final que no corresponde a ninguna linea real: se descarta.
        int ultima = (lineas.length > 0 && lineas[lineas.length - 1].isEmpty())
                ? lineas.length - 1 : lineas.length;
        for (int i = 0; i < ultima; i++) {
            System.out.println("    -> " + (lineas[i].isEmpty()
                    ? "(LINEA EN BLANCO: aqui acaban las cabeceras)" : lineas[i]));
        }
    }

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[AMP-UF2405-A5] Protocolos y formatos de intercambio");

        ServidorHttp servidor = new ServidorHttp();
        int puerto;
        try {
            puerto = servidor.abrir(PUERTO);
        } catch (IOException ocupado) {
            try {
                puerto = servidor.abrir(0);
            } catch (IOException e) {
                System.out.println("  No se pudo abrir ningun puerto: " + e.getMessage());
                System.out.println();
                return;
            }
        }
        Thread hilo = new Thread(servidor);
        hilo.setDaemon(true);
        hilo.start();
        System.out.println("  Servidor HTTP minimo escuchando en el puerto " + puerto);

        // Apartados 2 y 3: un GET escrito a mano.
        String get = "GET /mascotas HTTP/1.1\r\n"
                + "Host: localhost:" + puerto + "\r\n"
                + "Accept: text/html\r\n"
                + "\r\n";
        System.out.println("  Apartados 2 y 3, peticion GET enviada tal cual:");
        mostrarPeticion(get);
        System.out.println("    Respuesta recibida:");
        for (String l : conversar(puerto, get)) {
            System.out.println("    <- " + (l.isEmpty() ? "(LINEA EN BLANCO: empieza el cuerpo)" : l));
        }

        // Apartados 4 y 5: POST con cuerpo, que responde 302.
        String cuerpo = "nombre=Nube&especie=Gato&peso=3.8";
        String post = "POST /altaMascota HTTP/1.1\r\n"
                + "Host: localhost:" + puerto + "\r\n"
                + "Content-Type: application/x-www-form-urlencoded\r\n"
                + "Content-Length: " + cuerpo.getBytes(StandardCharsets.UTF_8).length + "\r\n"
                + "\r\n"
                + cuerpo;
        System.out.println("  Apartados 4 y 5, POST con cuerpo:");
        mostrarPeticion(post);
        System.out.println("    Respuesta recibida:");
        for (String l : conversar(puerto, post)) {
            System.out.println("    <- " + (l.isEmpty() ? "(LINEA EN BLANCO)" : l));
        }
        System.out.println("    El 302 con Location es lo que hace sendRedirect por dentro. El");
        System.out.println("    navegador pedira /mascotas con un GET, y esa ultima peticion ya se");
        System.out.println("    puede recargar sin duplicar el alta: es el POST-Redirect-GET.");

        // Apartado 5: el 404.
        System.out.println("  Apartado 5, ruta inexistente:");
        List<String> r404 = conversar(puerto,
                "GET /noExiste HTTP/1.1\r\nHost: localhost\r\n\r\n");
        System.out.println("    <- " + (r404.isEmpty() ? "(sin respuesta)" : r404.get(0)));
        System.out.println("    4xx significa que el fallo es de quien pide; 5xx, que es del");
        System.out.println("    servidor. Esa division decide a quien hay que avisar.");

        servidor.parar();

        // Apartados 6, 7 y 8
        try {
            System.out.println("  Apartado 6, XML analizado con las clases del propio JDK:");
            List<String> mascotas = leerMascotasXml(XML_MASCOTAS);
            for (String m : mascotas) {
                System.out.println("    " + m);
            }

            System.out.println("  Apartado 7, mensaje SOAP:");
            org.w3c.dom.Document soap = analizadorSeguro().parse(
                    new ByteArrayInputStream(SOBRE_SOAP.getBytes(StandardCharsets.UTF_8)));
            System.out.println("    Sobre    : " + soap.getDocumentElement().getNodeName());
            System.out.println("    Cabecera : usuario = " + soap
                    .getElementsByTagName("usuario").item(0).getTextContent()
                    + "   (aqui van seguridad y transaccion)");
            System.out.println("    Cuerpo   : operacion consultarMascota con id = " + soap
                    .getElementsByTagName("id").item(0).getTextContent());
            System.out.println("    SOAP no es un formato distinto de XML: es un protocolo QUE VIAJA");
            System.out.println("    dentro de XML, con una estructura de sobre convenida.");

            System.out.println("  Apartado 8, la misma informacion en JSON:");
            for (String l : generarJson(mascotas).split("\n")) {
                System.out.println("    " + l);
            }
            System.out.println("    XML  " + XML_MASCOTAS.length() + " caracteres, con esquema y validacion");
            System.out.println("    JSON " + generarJson(mascotas).length()
                    + " caracteres, mas ligero y directo para un navegador");
            System.out.println("    Ojo: el JDK trae analizador de XML pero NO de JSON. Generarlo a");
            System.out.println("    mano es facil; analizarlo no lo es, y ahi se usa Jackson o Gson.");

        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.out.println("  No se pudo analizar el XML: " + e.getMessage());
        }
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
