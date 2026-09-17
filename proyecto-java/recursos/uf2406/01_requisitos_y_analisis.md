# UF2406 · Bloque 1 — Requisitos y análisis orientado a objetos

Artefactos de los ejercicios 1 y 2 del cuaderno `Ejercicios_UF2406_Ciclo_Vida.docx`.
Caso: sistema de gestión de un gimnasio.

---

## Ejercicio 1 — Especificación de requisitos

### Apartados 1 y 3: requisitos funcionales con su criterio de aceptación

| ID | Requisito funcional | Criterio de aceptación |
|---|---|---|
| RF-01 | Dar de alta un socio con nombre, DNI, fecha de alta y tarifa. | Tras el alta, el socio aparece en el listado y puede consultarse por su DNI. |
| RF-02 | Registrar el pago de la cuota mensual de un socio. | El socio pasa a figurar «al corriente» del mes indicado. |
| RF-03 | Consultar los socios con cuotas pendientes de pago. | El listado muestra solo socios con al menos un mes impagado. |
| RF-04 | Dar de alta una clase colectiva con monitor, día, hora y aforo máximo. | La clase aparece en el horario semanal publicado. |
| RF-05 | Permitir que un socio se inscriba en una clase con plazas libres. | Las plazas disponibles disminuyen en una unidad. |
| RF-06 | Impedir la inscripción cuando la clase ha alcanzado su aforo máximo. | El sistema rechaza la inscripción con un mensaje explicativo. |
| RF-07 | Permitir que un socio cancele una inscripción. | La plaza vuelve a quedar libre para otros socios. |
| RF-08 | Impedir que un socio con cuotas pendientes se inscriba en clases. | El sistema rechaza la inscripción indicando el motivo. |

### Apartado 2: requisitos no funcionales

| ID | Requisito no funcional | Criterio de aceptación |
|---|---|---|
| RNF-01 | El listado de socios debe mostrarse en menos de 2 segundos con 5.000 socios registrados. | Prueba de carga con 5.000 registros; tiempo medido de 10 ejecuciones < 2 s. |
| RNF-02 | El sistema debe soportar 50 inscripciones simultáneas sin degradación. | Prueba de concurrencia con 50 hilos; ninguna petición supera los 3 s ni falla. |
| RNF-03 | Las contraseñas deben almacenarse cifradas, nunca en texto plano. | Inspección de la tabla de usuarios: ningún valor legible; algoritmo con sal. |

RNF-01 es la traducción a términos medibles de la petición del gerente «que sea rápido».
Ese es el punto del ejercicio: **un requisito no verificable es un requisito mal
redactado**. «Rápido» no se puede comprobar; «menos de 2 segundos con 5.000 socios»
sí, y además obliga a acordar con el cliente el volumen de datos previsto.

### Apartado 4: ambigüedades y preguntas al gerente

1. **«Saber quién ha pagado y quién no».**
   ¿El sistema se limita a registrar el pago, o debe además emitir recibos y
   controlar la domiciliación bancaria? ¿Qué ocurre con un pago parcial?

2. **«Los socios deben poder apuntarse desde una aplicación».**
   ¿Aplicación web, móvil o un terminal en el propio gimnasio? ¿Con cuánta
   antelación puede apuntarse un socio y hasta cuándo puede cancelar sin penalización?

3. **«Que sea rápido».**
   ¿Rápido respecto a qué volumen de datos y con cuántos usuarios simultáneos?
   ¿Cuántos socios hay hoy y cuántos se prevén a tres años?

### Apartado 5: índice del documento de especificación (SRS)

    1. Introducción
       1.1 Propósito del documento
       1.2 Ámbito del sistema
       1.3 Definiciones, acrónimos y abreviaturas
       1.4 Referencias
       1.5 Visión general del documento
    2. Descripción general
       2.1 Perspectiva del producto
       2.2 Funciones del producto
       2.3 Características de los usuarios (socio, monitor, recepcionista)
       2.4 Restricciones
       2.5 Suposiciones y dependencias
    3. Requisitos específicos
       3.1 Requisitos funcionales (RF-01 a RF-08)
       3.2 Requisitos no funcionales
           3.2.1 Rendimiento (RNF-01, RNF-02)
           3.2.2 Seguridad (RNF-03)
           3.2.3 Fiabilidad y disponibilidad
       3.3 Requisitos de interfaz
       3.4 Criterios de aceptación
    4. Matriz de trazabilidad de requisitos
    5. Apéndices
       A. Glosario del dominio
       B. Actas de las entrevistas con el cliente

### Apartado 6: modelo de proceso recomendado

**Iterativo e incremental.**

El gerente describe las necesidades en términos vagos y ya en la primera
entrevista aparecen tres ambigüedades. Eso es la señal más fiable de que los
requisitos van a evolucionar: no porque el cliente cambie de idea por capricho,
sino porque todavía no sabe exactamente qué quiere, y solo lo sabrá cuando vea
algo funcionando.

- **Cascada**: exigiría congelar los requisitos antes de empezar a construir. Con
  este punto de partida, ese documento congelado estaría equivocado.
- **En V**: útil cuando el énfasis está en la verificación formal por niveles
  (sistemas críticos, certificaciones). Aquí no aporta lo que hace falta.
- **Iterativo**: entregar primero la gestión de socios y cuotas y, en una segunda
  iteración, las clases colectivas, permite validar con el gerente antes de
  construir el sistema entero. Si la primera entrega revela que hacía falta
  controlar domiciliaciones, se ha perdido una iteración y no el proyecto.

---

## Ejercicio 2 — Del enunciado a las clases: tarjetas CRC

### Apartado 1: clases candidatas y sustantivos descartados

**Clases del dominio:** `Socio`, `Cuota`, `ClaseColectiva`, `Monitor`, `Inscripcion`.

**Descartados razonadamente:**

- **«Gimnasio»**: es el sistema completo, no una entidad dentro de él. Convertirlo
  en clase produce el clásico objeto-dios que acaba conteniendo todos los métodos.
- **«Horario»**: es un atributo de `ClaseColectiva` (día y hora). No tiene
  comportamiento propio ni estado que evolucione con el tiempo.
- **«Aplicación»**: es el medio por el que el socio accede, no un concepto del
  negocio del gimnasio.

El criterio útil no es «¿es un sustantivo?» sino **«¿tiene estado propio que
evolucione y comportamiento asociado?»**. La pista del enunciado señala `Cuota`
como caso dudoso: aquí sí se mantiene como clase porque tiene estado que cambia
(pagada / pendiente), fecha de pago e importe propio; si solo fuese una cantidad
fija, sería un atributo de `Socio`.

### Apartado 2: tarjetas CRC

    ┌────────────────────────────────────────────────────┐
    │ CLASE: Socio                                        │
    ├──────────────────────────────┬─────────────────────┤
    │ RESPONSABILIDADES             │ COLABORADORES        │
    ├──────────────────────────────┼─────────────────────┤
    │ Conocer sus datos              │ —                    │
    │ Conocer sus cuotas             │ Cuota                │
    │ Saber si está al corriente     │ Cuota                │
    │ Conocer sus inscripciones      │ Inscripcion          │
    └──────────────────────────────┴─────────────────────┘

    ┌────────────────────────────────────────────────────┐
    │ CLASE: ClaseColectiva                               │
    ├──────────────────────────────┬─────────────────────┤
    │ RESPONSABILIDADES             │ COLABORADORES        │
    ├──────────────────────────────┼─────────────────────┤
    │ Conocer horario y aforo        │ Monitor              │
    │ Saber cuántas plazas libres    │ Inscripcion          │
    │ Aceptar o rechazar inscripción │ Inscripcion, Socio   │
    └──────────────────────────────┴─────────────────────┘

    ┌────────────────────────────────────────────────────┐
    │ CLASE: Inscripcion                                  │
    ├──────────────────────────────┬─────────────────────┤
    │ RESPONSABILIDADES             │ COLABORADORES        │
    ├──────────────────────────────┼─────────────────────┤
    │ Conocer su estado y su fecha   │ —                    │
    │ Controlar sus transiciones     │ —                    │
    │ Relacionar socio y clase       │ Socio, ClaseColectiva│
    └──────────────────────────────┴─────────────────────┘

    ┌────────────────────────────────────────────────────┐
    │ CLASE: Cuota                                        │
    ├──────────────────────────────┬─────────────────────┤
    │ RESPONSABILIDADES             │ COLABORADORES        │
    ├──────────────────────────────┼─────────────────────┤
    │ Conocer mes e importe          │ —                    │
    │ Saber si está pagada           │ —                    │
    │ Registrar el pago              │ —                    │
    └──────────────────────────────┴─────────────────────┘

    ┌────────────────────────────────────────────────────┐
    │ CLASE: Monitor                                      │
    ├──────────────────────────────┬─────────────────────┤
    │ RESPONSABILIDADES             │ COLABORADORES        │
    ├──────────────────────────────┼─────────────────────┤
    │ Conocer sus datos              │ —                    │
    │ Conocer las clases que imparte │ ClaseColectiva       │
    └──────────────────────────────┴─────────────────────┘

### Apartados 3 y 4: relaciones y multiplicidades

    Socio  1 ──────── 0..*  Cuota                   COMPOSICIÓN
       Las cuotas de un socio no existen sin él: si se da de baja el socio,
       sus cuotas dejan de tener sentido como entidad independiente.

    ClaseColectiva  0..* ──── 1  Monitor            ASOCIACIÓN
       El monitor existe con independencia de las clases que imparta; puede
       dejar de impartir una clase y sigue siendo monitor del gimnasio.

    Socio  1 ──── 0..*  Inscripcion  0..* ──── 1  ClaseColectiva
       Inscripcion es la CLASE-ASOCIACIÓN que resuelve la relación N:M entre
       Socio y ClaseColectiva. Merece ser clase propia porque tiene atributos
       y comportamiento que no pertenecen ni a uno ni a otro: fecha, estado y
       las reglas de cancelación.

Es el mismo razonamiento que la tabla puente de la UF2405: una relación de muchos
a muchos no cabe en una columna. La diferencia es que aquí, además, la relación
tiene datos propios, y eso la convierte en un objeto de pleno derecho.

### Apartado 5: comprobación de cobertura de los requisitos

| Requisito | Clases que lo satisfacen |
|---|---|
| RF-01 | `Socio` |
| RF-02 | `Cuota`, `Socio` |
| RF-03 | `Socio.estaAlCorriente()`, `Cuota` |
| RF-04 | `ClaseColectiva`, `Monitor` |
| RF-05 | `Inscripcion`, `ClaseColectiva.plazasLibres()` |
| RF-06 | `ClaseColectiva.estaCompleta()` |
| RF-07 | `Inscripcion.cancelar()` |
| RF-08 | `Socio.estaAlCorriente()` |

Los ocho requisitos funcionales encuentran acomodo. Si alguno no lo hiciera, la
conclusión correcta no sería «este requisito sobra», sino que falta una clase o
una responsabilidad en el análisis.
