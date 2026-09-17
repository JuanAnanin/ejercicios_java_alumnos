# Documento de diseño — `GestorInscripciones`

Ejercicio A7 de la ampliación (CE3.3, CE3.7). Sigue la plantilla definida en
`A07DocumentoDiseno.plantilla()`.

---

## 1. Identificación

| | |
|---|---|
| Clase | `GestorInscripciones` |
| Paquete | `negocio` |
| Versión del documento | Edición 1, revisión 1 (27/08/2026) |
| Autor | Equipo de desarrollo |
| Estado | Aprobado |

## 2. Propósito y responsabilidad única

**Registrar la inscripción de un socio en una clase colectiva, aplicando las
reglas de negocio del gimnasio.**

Una sola frase, y a propósito. Todo lo que no quepa en ella no pertenece a esta
clase: ni enviar el correo de confirmación, ni pintar nada, ni abrir conexiones.

## 3. Contexto: capa y colaboradores

Capa de **negocio**. Es la única que toma decisiones.

| Colabora con | Para qué | Cómo lo recibe |
|---|---|---|
| `RepositorioClases` | leer la clase y guardar la inscripción | por constructor |
| `Socio` | preguntar si está al corriente de pago | como parámetro |
| `Inscripcion` | crear el objeto que representa el alta | lo construye |

**No conoce** `jakarta.servlet` ni `java.sql`. Si alguna vez aparece uno de esos
dos `import` en esta clase, la arquitectura se ha roto.

## 4. Interfaz pública

```java
Inscripcion inscribir(String dniSocio, String codigoClase)
```

| | |
|---|---|
| **Precondiciones** | `dniSocio` y `codigoClase` no nulos ni vacíos |
| **Postcondición (éxito)** | existe una `Inscripcion` en estado `PENDIENTE_PAGO` y la clase tiene una plaza menos |
| **Postcondición (fallo)** | *nada ha cambiado*. Ni la clase, ni el socio, ni el repositorio |
| **Devuelve** | la inscripción creada |
| **Lanza** | `NoEncontradoException`, `ClaseCompletaException`, `CuotasPendientesException` |

La postcondición de fallo es la más importante del documento y la que más se
olvida: **una operación que falla no deja rastro**. De ahí sale una decisión de
implementación concreta, la del apartado 8.

## 5. Atributos y estructuras de datos

| Atributo | Tipo | Por qué ese tipo |
|---|---|---|
| `repositorio` | `RepositorioClases` | interfaz, no clase concreta: permite sustituirlo al probar |

La clase **no tiene estado propio**. Es deliberado: sin estado, es segura entre
hilos sin ninguna sincronización, cosa que importa porque el contenedor web crea
una sola instancia para todos los usuarios.

## 6. Reglas de negocio implementadas

| # | Regla | Origen | Dónde vive |
|---|---|---|---|
| R1 | Un socio con cuotas pendientes no puede inscribirse | RF-08 | primera comprobación de `inscribir()` |
| R2 | No se puede inscribir en una clase con el aforo lleno | RF-06 | segunda comprobación |
| R3 | Desde la versión 1.1, si está llena se entra en lista de espera | RF-09 | `ClaseColectiva.solicitar()` |

El **origen** de cada regla es lo que hace útil esta tabla. Sin él, dentro de un
año nadie sabrá si el límite de aforo es una exigencia legal, una decisión del
gerente o algo que alguien supuso.

## 7. Excepciones y tratamiento de errores

Todas heredan de `VideoclubException` / `GimnasioException`, para que quien solo
quiera registrar el fallo capture una y quien necesite distinguir capture las
concretas. **Ninguna se captura aquí**: se propagan a la capa de presentación,
que es la única que sabe cómo se le habla al usuario.

## 8. Decisiones de diseño y alternativas descartadas

**D1 — El repositorio se recibe, no se crea.**
Alternativa descartada: `new ClaseDAO()` dentro del constructor. Se descartó
porque impide probar la clase sin base de datos y ata el negocio a una
implementación concreta de datos. Es la D de SOLID.

**D2 — Se comprueba TODO antes de modificar NADA.**
Alternativa descartada: marcar la plaza y comprobar después. Se descartó porque
una excepción a mitad dejaría la plaza ocupada sin que nadie la tuviera. Es lo
que hace cierta la postcondición de fallo del apartado 4.

**D3 — La clase no tiene estado.**
Alternativa descartada: cachear las clases colectivas en un atributo. Se
descartó porque el contenedor comparte la instancia entre todos los usuarios y
esa caché sería una condición de carrera. Si hace falta caché, va en el
repositorio.

**D4 — El orden de las comprobaciones es cuotas, luego aforo.**
Es intencionado: informar de que la clase está llena a alguien que además debe
tres mensualidades le hace ir a recepción dos veces.

## 9. Diagramas

Los de secuencia y estados están en `recursos/uf2406/02_diagramas_uml.md`.

## 10. Trazabilidad

Implementa RF-05, RF-06, RF-08 y, desde la versión 1.1, RF-09.

## 11. Criterios de aceptación

La clase está terminada cuando pasan las cinco pruebas de
`Ej10Mocks` y `Ej12CambioEspecificacion`, y ninguna de ellas necesita base de
datos para ejecutarse. Esa segunda condición forma parte del criterio: si hiciera
falta una base de datos, D1 no se habría cumplido.

## 12. Historial de revisiones

| Ed./Rev. | Fecha | Cambio | Aprueba |
|---|---|---|---|
| 1.1 | 27/08/2026 | Se añade R3 (lista de espera, RF-09) | Responsable funcional |
| 1.0 | 02/02/2026 | Versión inicial | Responsable funcional |
