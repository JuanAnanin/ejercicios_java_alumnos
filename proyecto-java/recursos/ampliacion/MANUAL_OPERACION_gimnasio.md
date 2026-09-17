# Manual de operación y mantenimiento — Sistema del gimnasio

Ejercicio A9 de la ampliación (CE3.5, CE3.9).
Destinatario: **quien administra el servidor**. No ha visto el código y no
necesita verlo.

> **Este manual no contiene ninguna contraseña.** Las credenciales las custodia
> el responsable de sistemas; aquí solo se indica en qué fichero van.

---

## 1. Identificación

| | |
|---|---|
| Sistema | Gestión del gimnasio |
| Versión | 1.1.0 |
| Manual | Edición 1, revisión 2 (27/08/2026) |

## 2. Arquitectura desplegada

    [navegador] --HTTP--> [Tomcat 10 + gimnasio.war] --JDBC--> [MySQL 8]

Tres piezas, que pueden estar en la misma máquina o en dos.

## 3. Requisitos previos

- Java 17 o superior (`java -version`)
- Tomcat 10 o superior — **no vale Tomcat 9**: a partir de la 10 el paquete pasa
  de `javax.servlet` a `jakarta.servlet`, y el `.war` no arrancará
- MySQL 8
- Puertos: 8080 (Tomcat) y 3306 (MySQL), abiertos solo desde la red interna

## 4. Instalación desde cero

1. Crear la base de datos:
   ```
   mysql -u root -p < 01_esquema_clinica.sql
   mysql -u root -p < 02_evolucion_esquema.sql
   ```
2. Crear el usuario de la aplicación **con permisos mínimos**: `SELECT`,
   `INSERT`, `UPDATE`, `DELETE`. **Sin `DROP`**: la aplicación nunca borra
   tablas, y ese permiso solo sirve para agravar un incidente.
3. Copiar `mysql-connector-j.jar` en `TOMCAT/lib`.
4. Configurar el pool en `TOMCAT/conf/context.xml` (apartado 5).
5. Copiar `gimnasio.war` en `TOMCAT/webapps`.
6. Arrancar Tomcat y comprobar (apartado 7).

## 5. Configuración

| Parámetro | Dónde | Por defecto | Efecto |
|---|---|---|---|
| `maxTotal` | `context.xml` | 10 | Conexiones simultáneas máximas. Subirlo consume memoria en MySQL |
| `maxIdle` | `context.xml` | 5 | Conexiones que se mantienen abiertas sin uso |
| `url` | `context.xml` | — | **Debe llevar** `?useUnicode=true&characterEncoding=UTF-8` o los acentos se corrompen |
| `username` / `password` | `context.xml` | — | Las custodia el responsable de sistemas |

## 6. Arranque y parada

```
TOMCAT/bin/startup.sh      # arrancar
TOMCAT/bin/shutdown.sh     # parar de forma ordenada
```

Si `shutdown.sh` no termina en 30 segundos, hay peticiones colgadas: ver la
incidencia del pool agotado en el apartado 14.

## 7. Comprobación de salud tras arrancar

1. `tail -f TOMCAT/logs/catalina.out` — debe aparecer `Server startup in ... ms`
   y **ninguna** traza de excepción.
2. Abrir `http://servidor:8080/gimnasio/mascotas` — debe salir el listado.
3. Si el listado sale vacío pero sin error, la aplicación funciona y la base de
   datos está vacía: son dos problemas distintos.

## 8. Copias de seguridad

| | |
|---|---|
| **Qué** | La base de datos completa y `context.xml`. **El código no**: está en el repositorio |
| **Cuándo** | Copia completa diaria a las 03:00 |
| **Dónde** | En otra máquina *y además* fuera del edificio. Una copia en el mismo servidor no es una copia |
| **Cuánto** | 30 días las diarias, 12 meses las mensuales |
| **Cómo se verifica** | **Restaurándola.** Una vez al trimestre, en una máquina aparte, y anotando el resultado |

```
mysqldump -u root -p --single-transaction clinica > clinica_$(date +%F).sql
```

`--single-transaction` obtiene una copia coherente sin bloquear la base de datos.

> Una copia que nunca se ha restaurado no es una copia de seguridad: es un
> fichero grande del que nadie sabe nada.

## 9. Restauración

1. Parar Tomcat.
2. `mysql -u root -p clinica < clinica_AAAA-MM-DD.sql`
3. Arrancar y comprobar (apartado 7).
4. **Anotar** qué copia se ha restaurado y qué datos se han perdido en el
   intervalo.

## 10. Actualización

1. Copia de seguridad (apartado 8). **Sin excepciones.**
2. Guardar el `.war` actual en `versiones/gimnasio-1.1.0.war`.
3. Parar Tomcat.
4. Aplicar los scripts SQL de la versión nueva, si los hay.
5. Sustituir el `.war` y borrar la carpeta descomprimida anterior.
6. Arrancar y comprobar.

## 11. Vuelta atrás

Redactado para las tres de la madrugada:

1. Parar el servicio.
2. Desplegar el `.war` anterior desde `versiones/`.
3. Si la versión nueva cambió la base de datos, aplicar el script de reversión.
   Si no existe, restaurar la copia previa al despliegue (apartado 9).
4. Arrancar y ejecutar la comprobación del apartado 7.
5. Avisar al responsable, con la hora y el motivo.

> La decisión clave se toma **antes**: cada despliegue debe dejar preparada su
> vuelta atrás. Improvisarla el viernes por la noche es como se pierden los datos.

## 12. Registros

| Fichero | Contiene | Conservar |
|---|---|---|
| `TOMCAT/logs/catalina.out` | arranque y errores no capturados | 30 días |
| `TOMCAT/logs/localhost_access_log` | cada petición HTTP | 90 días |

Configurar la rotación: sin ella, el disco se llena (apartado 14).

## 13. Mantenimiento periódico

- **Semanal**: revisar el tamaño de `TOMCAT/logs` y buscar excepciones nuevas.
- **Mensual**: comprobar el espacio libre en disco y en MySQL.
- **Trimestral**: **restaurar una copia de seguridad** en una máquina de pruebas.
- **Semestral**: revisar versiones de Java, Tomcat y MySQL por si hay
  actualizaciones de seguridad.

## 14. Resolución de incidencias

| Síntoma | Causa probable | Qué hacer |
|---|---|---|
| `No suitable driver found for jdbc:mysql://...` | Falta el conector de MySQL | Copiar `mysql-connector-j.jar` en `TOMCAT/lib` y reiniciar. Comprobar con `ls TOMCAT/lib \| grep mysql` |
| `SQLException`, SQLState `28000` | Usuario o contraseña incorrectos | Revisar `context.xml`. Las credenciales las tiene el responsable de sistemas |
| Los acentos salen como símbolos raros | Falta la codificación | Añadir `?useUnicode=true&characterEncoding=UTF-8` a la URL de conexión |
| Tras unas horas se queda colgado | Pool agotado: alguna conexión no se devuelve | Reiniciar **y abrir incidencia**. La causa es un `try` sin `try-with-resources`: hay que localizarlo, no solo reiniciar |
| `Unknown column 'ultima_visita'` | Falta aplicar el script de evolución | Ejecutar `02_evolucion_esquema.sql`. Comprobar antes con `DESCRIBE mascota` |
| El disco se llena cada pocas semanas | Los registros no se rotan | Configurar rotación a 30 días. Comprobar el tamaño de `TOMCAT/logs` antes de borrar |
| Arranca pero da error 404 en todo | El `.war` no se desplegó | Mirar `catalina.out`; suele ser un fallo al descomprimir o un `web.xml` inválido |

## 15. Escalado

Si la incidencia no está en el apartado 14, o el sistema lleva más de 30 minutos
caído, avisar al equipo de desarrollo **con estos cuatro datos**:

1. Hora exacta en que empezó
2. Qué se estaba haciendo
3. Las últimas 50 líneas de `catalina.out`
4. Si se ha desplegado algo en las últimas 24 horas

Sin el punto 4 se pierde media hora en cada incidencia.

## 16. Historial de revisiones

| Ed./Rev. | Fecha | Cambio |
|---|---|---|
| 1.2 | 27/08/2026 | Se añade la incidencia de `ultima_visita` y el aviso de Tomcat 9 |
| 1.1 | 15/04/2026 | Procedimiento de vuelta atrás |
| 1.0 | 02/02/2026 | Versión inicial |
