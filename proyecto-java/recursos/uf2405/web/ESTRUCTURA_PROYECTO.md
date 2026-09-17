# UF2405 - Ejercicio 14: estructura de la aplicacion web completa

## Apartado 1: paquetes por capas

    src/main/java/
     ├── presentacion/                 <- capa de PRESENTACION
     │     ├── LoginServlet.java
     │     ├── ListadoMascotasServlet.java
     │     ├── AltaMascotaServlet.java
     │     ├── AnadirTratamientoServlet.java
     │     ├── VerConsultaServlet.java
     │     ├── ConfirmarConsultaServlet.java
     │     ├── InformesServlet.java
     │     └── FiltroAutenticacion.java
     ├── negocio/                      <- capa de APLICACION (reglas)
     │     ├── GestorClinica.java
     │     └── ServicioAutenticacion.java
     ├── datos/                        <- capa de DATOS
     │     ├── ConexionBD.java         (pool de conexiones)
     │     ├── MascotaDAO.java
     │     ├── ClienteDAO.java
     │     ├── ConsultaDAO.java
     │     └── TratamientoDAO.java
     └── modelo/                       <- objetos que viajan entre capas
           ├── Mascota.java
           ├── Cliente.java
           ├── Consulta.java
           └── Tratamiento.java

    src/main/webapp/
     ├── login.jsp
     ├── altaMascota.html
     └── WEB-INF/
           ├── web.xml
           └── vistas/                 <- JSP inaccesibles por URL directa
                 ├── mascotas.jsp
                 ├── altaMascota.jsp
                 └── informes.jsp

**La regla que ordena todo esto**: las dependencias van siempre hacia abajo.
`presentacion` conoce `negocio`, `negocio` conoce `datos`, y `datos` no conoce
a nadie. Ninguna clase de `datos` debe importar nada de `jakarta.servlet`: si
lo hace, la capa de datos ha dejado de ser reutilizable fuera de la web.

Los JSP van bajo `WEB-INF/vistas/` porque el contenedor no sirve directamente
lo que hay ahi. Sin eso, cualquiera podria pedir `mascotas.jsp` por la URL,
saltarse el Servlet que carga los datos y ver una pagina vacia o, peor, una
pagina que no ha pasado por el control de acceso.

## Apartado 2: autenticacion y sesion

- `LoginServlet` valida usuario y contrasena contra `ServicioAutenticacion`.
- Si son correctos: `sesion.setAttribute("usuario", u)`.
- **Antes** de guardar nada en la sesion, `request.changeSessionId()`, para
  evitar la fijacion de sesion: si un atacante consigue que la victima use un
  identificador de sesion que el conoce, al iniciar sesion esa sesion pasaria a
  estar autenticada y el atacante entraria con ella.
- Las contrasenas se guardan con un algoritmo de hash lento y con sal
  (bcrypt, scrypt o Argon2). Nunca en claro, y nunca con MD5 o SHA-1 a secas:
  esos estan hechos para ser rapidos, que es exactamente lo contrario de lo que
  interesa aqui.
- El cierre de sesion hace `sesion.invalidate()`, no solo borrar el atributo.
- `FiltroAutenticacion` protege todo `/privado/*`.

## Apartado 5: consultas de la pantalla de informes

Son las del ejercicio 3, apartados 2 y 4:

- consultas por veterinario: `JOIN` + `COUNT(*)` + `GROUP BY`
- facturacion por mascota: tres `JOIN` + `SUM(precio * cantidad)` + `GROUP BY`

## Apartado 7: las dos protecciones, y por que son la misma idea

| Riesgo | Donde | Defensa |
|---|---|---|
| Inyeccion SQL | capa de datos | `PreparedStatement` siempre; nunca concatenar |
| XSS | capa de presentacion | `c:out` en los JSP, o escapar a mano en los Servlets |

Las dos son el mismo error de fondo: un dato que viene de fuera acaba siendo
interpretado como codigo. En un caso lo interpreta el motor de base de datos y
en el otro el navegador. Y la defensa es la misma en los dos: mantener separado
lo que es estructura de lo que es dato.

## Apartado 8: Javadoc

Todas las clases publicas de `negocio` y `datos` llevan Javadoc de clase y de
metodo, con `@param`, `@return` y `@throws`. Se genera con:

    javadoc -d docs -subpackages negocio:datos:modelo

## Sobre la pista: ir de una en una

El enunciado recomienda terminar una entidad de principio a fin antes de
empezar la siguiente, y merece la pena insistir en clase. Hacer las cinco
tablas, luego los cinco DAO, luego los cinco Servlets parece mas ordenado, pero
deja sin comprobar hasta el ultimo dia si las piezas encajan. Terminando
Mascota entera se descubren pronto los problemas de verdad (la codificacion,
las fechas, la conversion de tipos, la sesion), y las cuatro entidades
restantes se hacen despues casi en copia.
