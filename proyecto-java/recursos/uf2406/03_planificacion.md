# UF2406 · Bloque 3 — Planificación y gestión del proyecto

Artefactos del ejercicio 6. Caso: sistema de gestión de un gimnasio.

Los números de este documento **no están copiados a mano**: los calcula y verifica
`src/com/ifcd0112/ejercicios/uf2406/bloque3_planificacion/Ej06GanttCaminoCritico.java`,
que implementa el método del camino crítico y recorre las tareas de la tabla.

---

## Apartado 1: tabla de tareas y precedencias

| Cód. | Tarea | Duración | Predecesoras |
|---|---|---|---|
| A | Análisis de requisitos | 4 d | — |
| B | Diseño de la base de datos | 3 d | A |
| C | Diseño de la interfaz | 3 d | A |
| D | Desarrollo del acceso a datos | 6 d | B |
| E | Desarrollo de la interfaz | 5 d | C |
| F | Integración | 3 d | D, E |
| G | Pruebas de sistema | 4 d | F |

## Apartado 2: diagrama de Gantt

    Día:      1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20
    A (4d)   ████████████
    B (3d)                █████████
    C (3d)                █████████
    D (6d)                         ██████████████████
    E (5d)                         ███████████████
    F (3d)                                          █████████
    G (4d)                                                   ████████████

    Duración total del proyecto: 20 días laborables

El paralelismo aprovechado está en dos sitios: B y C arrancan a la vez en cuanto
termina A, y D y E corren en paralelo. F no puede empezar hasta que terminen los
dos desarrollos, y ahí es donde E gana su holgura.

## Apartado 3: diagrama de red (PERT)

              ┌──B(3)──┐         ┌──D(6)──┐
              │         │         │         │
    ●──A(4)──>┤         ├────────>┤         ├──>F(3)──>G(4)──>●
              │         │         │         │
              └──C(3)──┘         └──E(5)──┘

    Precedencias reales:  A→B, A→C, B→D, C→E, D→F, E→F, F→G

## Apartado 4: camino crítico y duración mínima

    Rutas posibles de inicio a fin:
      A → B → D → F → G  =  4 + 3 + 6 + 3 + 4 = 20 días   <-- CAMINO CRÍTICO
      A → C → E → F → G  =  4 + 3 + 5 + 3 + 4 = 19 días

    Duración mínima del proyecto: 20 días laborables.

## Apartado 5: holgura de «Desarrollo de la interfaz»

**Holgura de E = 1 día.**

Significado exacto: E puede empezar hasta 1 día más tarde de lo previsto, o durar
1 día más, sin que la fecha de entrega del proyecto se mueva. La razón es que E
termina el día 15 pero F, que la espera, no puede empezar hasta el 16 porque D no
acaba antes.

Si E se retrasa **2** días, deja de tener holgura y pasa a ser también crítica: a
partir de ahí, el camino A→C→E→F→G mide 21 días y manda sobre el otro. Esa es la
parte que suele sorprender: el camino crítico no es fijo, cambia según se
consumen las holguras.

## Apartado 6: cómo adelantar la entrega una semana

Hay que actuar **sobre el camino crítico**: A, B, D, F o G. Preferentemente sobre
**D (Desarrollo del acceso a datos, 6 días)**, que es la tarea más larga y por
tanto la que más margen de compresión ofrece: dividirla entre dos personas, o
partirla en dos entregas.

Acelerar **E** sería inútil: como mucho consumiría su holgura de 1 día sin
adelantar ni un solo día la fecha final. Es el error de gestión más repetido, y el
motivo por el que se calcula el camino crítico: sin él, la reacción intuitiva es
meter recursos donde se ve que hay presión, no donde de verdad importa.

Advertencia sobre la compresión: acortar 5 días de un proyecto de 20 es un 25 %.
Al comprimir el camino crítico, la holgura de E se agota enseguida y **aparece un
segundo camino crítico**. A partir de ahí hay que acelerar los dos a la vez, y el
coste crece mucho más deprisa que el ahorro.

## Apartado 7: registro de riesgos

| ID | Riesgo | Prob. | Impacto | Mitigación |
|---|---|---|---|---|
| R1 | El gerente, única fuente de requisitos, no está disponible | Media | Alto | Identificar un segundo interlocutor desde el inicio y dejar por escrito cada decisión acordada |
| R2 | El desarrollo del acceso a datos se alarga (está en el camino crítico) | Alta | Alto | Prototipar la parte crítica en la primera semana, en paralelo al análisis |
| R3 | Cambio de requisitos a mitad del proyecto | Alta | Medio | Validar con el cliente al terminar cada diseño; modelo iterativo |
| R4 | Bajas del equipo por enfermedad | Baja | Alto | Documentar el trabajo para que sea transferible; evitar que una tarea dependa de una sola persona |
| R5 | El aforo de las clases resulta más complejo de lo previsto (turnos, festivos) | Media | Medio | Acotar el alcance de la primera iteración a clases de horario fijo |

Sobre R2 conviene detenerse en clase: su probabilidad es alta **y** está en el
camino crítico, de modo que su impacto se traslada directamente a la fecha de
entrega. Un riesgo idéntico sobre E sería mucho menos grave, porque E tiene
holgura. Cruzar el registro de riesgos con el camino crítico es lo que convierte
esa tabla en algo más que un trámite.
