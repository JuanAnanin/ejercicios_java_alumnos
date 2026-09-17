package com.ifcd0112.ejercicios.uf2405.bloque5_integrador;

/**
 * UF2405 - BLOQUE 5 - EJERCICIO 14: Aplicacion web completa de gestion de la clinica.
 *
 * <p>Criterios de evaluacion: CE1.1, CE1.4, CE1.8, CE2.5, CE2.6</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class Ej14AplicacionWeb {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Integra todo el trabajo de los bloques anteriores en una aplicacion web
     * funcional que permita gestionar la clinica veterinaria de principio a fin.
     *
     * Se pide:
     *   1. Estructura el proyecto en paquetes que reflejen las tres capas:
     *      presentacion (Servlets y JSP), negocio (clases con las reglas de la
     *      clinica) y datos (los DAO).
     *   2. Implementa el acceso a la aplicacion mediante usuario y contrasena,
     *      gestionando la sesion HTTP y protegiendo todas las paginas frente a
     *      accesos sin sesion iniciada.
     *   3. Implementa el mantenimiento completo (alta, consulta, modificacion y
     *      baja) de al menos dos entidades: clientes y mascotas.
     *   4. Implementa el registro de consultas con sus tratamientos, empleando
     *      una transaccion tal y como se trabajo en el ejercicio 8.
     *   5. Incorpora una pantalla de informes que muestre, mediante consultas
     *      con combinacion y agregacion, el numero de consultas por veterinario
     *      y la facturacion total por mascota.
     *   6. Emplea un pool de conexiones en lugar de abrir una conexion nueva en
     *      cada peticion, y justifica por escrito esta decision.
     *   7. Protege todas las consultas frente a inyeccion SQL y todas las
     *      salidas HTML frente a XSS.
     *   8. Documenta con Javadoc todas las clases de las capas de negocio y
     *      datos.
     *
     * Pista: aborda el proyecto de forma incremental: consigue primero que
     * funcione una unica entidad de principio a fin (de la base de datos hasta
     * el navegador) y solo entonces replica el patron para las demas. Es
     * preferible una entidad completa y correcta que cinco a medias.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * DONDE ESTA LA SOLUCION.
     *
     * Este ejercicio es un proyecto web completo y no cabe en un fichero. Los
     * artefactos que lo definen estan en recursos/uf2405/web/:
     *
     *      ESTRUCTURA_PROYECTO.md    apartados 1, 2, 5, 7 y 8: la estructura de
     *                                paquetes, el esquema de autenticacion, las
     *                                consultas de informes y las protecciones.
     *      FiltroAutenticacion.java  apartado 2: control de acceso centralizado.
     *      ConexionBD.java           apartado 6: el pool, con su justificacion.
     *      LEEME.md                  como montar y desplegar la aplicacion.
     *
     * Y las piezas ya resueltas en los bloques anteriores, que aqui solo hay que
     * juntar:
     *
     *      Ejercicio 1 y 2   el esquema de la base de datos
     *      Ejercicio 3       las consultas de la pantalla de informes (apartado 5)
     *      Ejercicio 7       los DAO (apartado 3)
     *      Ejercicio 8       la transaccion de registrarConsulta (apartado 4)
     *      Ejercicio 10 a 13 los Servlets y las vistas
     *
     * QUE ES LO QUE DE VERDAD SE EVALUA AQUI.
     *
     * No es escribir codigo nuevo: casi todo esta hecho. Lo que se evalua es si
     * las piezas encajan sin romper las capas. Los tres fallos tipicos:
     *
     *   - Un DAO que importa jakarta.servlet, normalmente para leer un parametro
     *     de la peticion o para escribir en el log del contenedor. En cuanto eso
     *     pasa, la capa de datos deja de poder usarse fuera de la web y ya no se
     *     puede probar sin levantar Tomcat.
     *   - Un JSP que llama al DAO "solo para este detalle que falta". Es el
     *     principio del fin de la separacion del ejercicio 13.
     *   - La logica de negocio metida en el Servlet. El Servlet debe coordinar:
     *     recibir, pedir, elegir vista. Las reglas de la clinica van en negocio,
     *     que ademas es la unica forma de que se puedan probar.
     *
     * APARTADO 2: dos detalles de seguridad que conviene explicar en clase.
     *
     *   Fijacion de sesion. Al iniciar sesion correctamente hay que llamar a
     *   request.changeSessionId() ANTES de guardar el usuario en la sesion. Si
     *   no, un atacante que consiga que la victima use un identificador de
     *   sesion conocido por el, entrara despues con esa misma sesion ya
     *   autenticada.
     *
     *   Contrasenas. Se guardan con un algoritmo de hash LENTO y con sal:
     *   bcrypt, scrypt o Argon2. Nunca en claro, y tampoco con MD5 o SHA-1 a
     *   secas: esos estan disenados para ser rapidos, que es justo lo contrario
     *   de lo que interesa cuando alguien intenta probar millones de
     *   combinaciones.
     *
     * APARTADO 7: las dos protecciones son el mismo error de fondo.
     *
     *   Inyeccion SQL (ejercicio 6) y XSS (ejercicio 10) parecen problemas
     *   distintos y son el mismo: un dato que viene de fuera acaba
     *   interpretandose como codigo. En un caso lo interpreta el motor de la
     *   base de datos y en el otro el navegador. La defensa tambien es la misma
     *   idea: mantener separado lo que es estructura de lo que es dato.
     *   PreparedStatement lo hace en el lado de la base de datos; escapar la
     *   salida, o usar c:out, lo hace en el lado del navegador.
     *
     * SOBRE LA PISTA: por que conviene ir de una entidad en una.
     *
     *   Hacer las cinco tablas, luego los cinco DAO y luego los cinco Servlets
     *   parece mas ordenado, pero deja sin comprobar hasta el ultimo dia si las
     *   piezas encajan. Terminando Mascota entera, de la base de datos al
     *   navegador, aparecen pronto los problemas de verdad: la codificacion de
     *   los acentos, el formato de las fechas, la conversion de tipos, la
     *   sesion. Resueltos una vez, las otras cuatro entidades salen casi en
     *   copia.
     */

    /** Carpeta con los artefactos de la aplicacion web. */
    public static final String CARPETA = "recursos/uf2405/web/";

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[UF2405-E14] Aplicacion web completa de gestion de la clinica");
        System.out.println("  Estructura, autenticacion, informes y protecciones:");
        System.out.println("    " + CARPETA + "ESTRUCTURA_PROYECTO.md");
        System.out.println("  Filtro de control de acceso: " + CARPETA + "FiltroAutenticacion.java");
        System.out.println("  Pool de conexiones:          " + CARPETA + "ConexionBD.java");
        System.out.println("  Despliegue paso a paso:      " + CARPETA + "LEEME.md");
        System.out.println("  Las piezas ya estan hechas en los bloques anteriores: esquema (1 y 2),");
        System.out.println("    consultas de informes (3), DAO (7), transaccion (8), Servlets y");
        System.out.println("    vistas (10 a 13). Lo que se evalua aqui es que encajen sin romper");
        System.out.println("    las capas: ningun DAO que importe jakarta.servlet, ningun JSP que");
        System.out.println("    llame al DAO, ninguna regla de negocio dentro de un Servlet.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
