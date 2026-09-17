# Informe de pruebas — Sistema de gestión del gimnasio

Ejercicio A8 de la ampliación (CE3.4, CE3.8).

> Las cifras de los apartados 7 y 10 **no están escritas a mano**: las genera
> `A08InformePruebas` ejecutando las baterías en el momento. Para regenerarlas:
> `java -cp out com.ifcd0112.ejercicios.ampliacion.uf2406.A08InformePruebas`

---

## 1. Identificación

| | |
|---|---|
| Sistema | Gestión del gimnasio |
| Versión probada | 1.1.0 (con lista de espera, RF-09) |
| Fecha | 27/08/2026 |
| Responsable | Equipo de desarrollo |

## 2. Alcance

**Se ha probado**: el cálculo de la cuota mensual, la lógica de inscripción con
sus tres reglas de negocio, la máquina de estados de `Inscripcion` y la
funcionalidad de lista de espera con su regresión.

**NO se ha probado**, y hay que decirlo: el acceso real a la base de datos (las
pruebas usan objetos simulados), la interfaz gráfica más allá de la lógica de su
controlador, el rendimiento con volumen real y la recuperación ante caída del
SGBD. Un informe que no dice lo que falta induce a error.

## 3. Entorno

Java 17 o superior, sin dependencias externas. Datos de partida definidos en el
código de cada batería. Sin base de datos: las pruebas unitarias usan objetos
simulados escritos a mano.

## 4. Estrategia

| Tipo | Técnica | Dónde |
|---|---|---|
| Funcionales | clases de equivalencia y valores límite | ejercicio 7 |
| Estructurales | cobertura de caminos, complejidad ciclomática | ejercicio 8 |
| Unitarias | automatizadas con aserciones | ejercicio 9 |
| Con dobles | objetos simulados para aislar la lógica | ejercicio 10 |
| Regresión | batería completa tras un cambio | ejercicio 12 |

## 5. Diseño de las pruebas

Detallado en `recursos/uf2406/04_diseno_pruebas.md`: clases de equivalencia,
valores límite, tabla de decisión y grafo de flujo.

## 6. Casos de prueba

Los 12 casos de caja negra y los 5 de caja blanca, con su entrada, resultado
esperado y qué verifica cada uno, están en el mismo documento.

## 7. Resultados de la ejecución

| Batería | Qué verifica | Total | Fallos | Errores |
|---|---|---|---|---|
| Caja negra (ej. 7) | equivalencia y valores límite de la cuota | 12 | 0 | 0 |
| Unitarias (ej. 9) | la cuota mensual, automatizada | 12 | 0 | 0 |
| Regresión (ej. 12) | lista de espera y lo que ya funcionaba | 10 | 0 | 0 |
| **TOTAL** | | **34** | **0** | **0** |

De las 10 de regresión, **5 son anteriores al cambio** y siguen pasando: eso es
lo que permite afirmar que el RF-09 no rompió nada.

## 8. Cobertura

Cobertura estimada de la lógica de negocio probada: alta en `CalculadoraCuota` y
`GestorInscripciones`, nula en la capa de presentación.

**Interpretación, que es lo que importa**: la cobertura mide qué porcentaje del
código *ejecutan* las pruebas, no qué porcentaje está *bien probado*. Se usa aquí
para localizar huecos, no como objetivo. Gracias a ella se detectó D-01.

## 9. Defectos detectados

| ID | Gravedad | Estado | Resumen | Decisión |
|---|---|---|---|---|
| D-01 | Baja | **Aceptado** | El suelo de 20 EUR de la cuota es inalcanzable: el mínimo real es 23,00 | Aceptado por el responsable funcional el 27/08/2026: no afecta al usuario; se revisará si se añaden más descuentos |
| D-02 | Alta | Corregido | `mascota.ultima_visita` se actualiza pero ninguna sentencia DDL la crea | Corregido en `02_evolucion_esquema.sql` y verificado |
| D-03 | Media | **Abierto** | La pantalla de inscripción no permite deshacer una inscripción recién hecha | Sin decidir: pendiente de estimar |
| D-04 | Media | **Abierto** | El mensaje de progreso no alcanza el contraste mínimo AA (3,95:1) | Detectado en A12; corrección de una línea |

D-01 y D-03 son la misma situación —un defecto que no se corrige— y sin embargo
son cosas distintas: **el primero lleva firma y fecha de quien decidió entregarlo
así, y el segundo no**. Eso es lo único que separa una decisión de un descuido.

## 10. Veredicto

**Criterio de aceptación acordado**: cero fallos y cero errores en las tres
baterías, y ningún defecto abierto de gravedad alta o crítica.

- Pruebas correctas: **sí** (34/34)
- Defectos bloqueantes abiertos: **0**

### ENTREGA ACEPTADA

Con la salvedad de que D-04 debe corregirse antes de cualquier entrega a un
organismo público, donde el nivel AA es exigible.

## 11. Riesgos residuales

Aunque todo esté en verde:

1. **No se ha probado contra la base de datos real.** Las pruebas con objetos
   simulados verifican la lógica, no que el SQL sea correcto.
2. **No se ha probado la concurrencia de inscripciones.** Dos socios pidiendo la
   última plaza a la vez es un escenario plausible y no cubierto.
3. **No hay pruebas de carga.** El RNF-01 (listado en menos de 2 s con 5.000
   socios) no está verificado.

## 12. Historial de revisiones

| Ed./Rev. | Fecha | Cambio |
|---|---|---|
| 1.1 | 27/08/2026 | Se añaden las 5 pruebas del RF-09 y el defecto D-04 |
| 1.0 | 02/02/2026 | Informe inicial |
