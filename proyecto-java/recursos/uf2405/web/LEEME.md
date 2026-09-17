# UF2405 - Bloque 4 y 5: artefactos web

Los ficheros de esta carpeta **no forman parte de la compilacion del proyecto**.
El proyecto se compila con:

    javac -d out $(find src -name "*.java")

y esta carpeta cuelga de `recursos/`, no de `src/`, asi que `find` no la
alcanza. Es deliberado: estos ficheros necesitan la API de Servlets
(`jakarta.servlet`), que no forma parte del JDK, y un contenedor web como
Tomcat o Jetty para ejecutarse. Si se copiaran a `src/` el proyecto dejaria de
compilar.

## Como montar la aplicacion web

1. Crear un proyecto web (Maven `war` o el asistente de "Dynamic Web Project").
2. Anadir las dependencias: `jakarta.servlet-api` (provided) y el driver
   `mysql-connector-j`.
3. Crear la base de datos con `recursos/uf2405/sql/01_esquema_clinica.sql` y
   `02_evolucion_esquema.sql`.
4. Copiar las clases de esta carpeta a `src/main/java/` respetando los paquetes
   que indica la estructura de `ESTRUCTURA_PROYECTO.md`.
5. Copiar `altaMascota.html` a `src/main/webapp/` y `mascotas.jsp` a
   `src/main/webapp/WEB-INF/`.
6. Desplegar en Tomcat 10 o superior.

> Aviso de version: a partir de Tomcat 10 el paquete pasa de `javax.servlet` a
> `jakarta.servlet`. El codigo de esta carpeta usa `jakarta`. Con Tomcat 9 o
> anterior hay que cambiar los import a `javax`; es lo unico que cambia.

## Correspondencia con los ejercicios

| Fichero | Ejercicio |
|---|---|
| `altaMascota.html` | 9. Formulario HTML con validacion en cliente |
| `ListadoMascotasServlet.java` | 10. Servlet de listado |
| `AltaMascotaServlet.java` | 11. Procesamiento de formulario |
| `AnadirTratamientoServlet.java`, `VerConsultaServlet.java`, `ConfirmarConsultaServlet.java` | 12. Sesiones: carrito de tratamientos |
| `ListadoMascotasServletMvc.java`, `mascotas.jsp` | 13. De Servlet a JSP |
| `FiltroAutenticacion.java`, `ConexionBD.java`, `ESTRUCTURA_PROYECTO.md` | 14. Aplicacion web completa |
