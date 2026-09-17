# UF2406 · Bloque 2 — Modelado con UML

Artefactos de los ejercicios 3, 4 y 5. Caso: sistema de gestión de un gimnasio.

---

## Ejercicio 3 — Diagrama de casos de uso

### Apartados 1, 2 y 4: actores, casos de uso y asociaciones

    ACTORES:  Socio · Monitor · Recepcionista

            ┌──────────────── SISTEMA GIMNASIO ─────────────────┐
            │                                                    │
     Socio ─┼──○ Consultar horario de clases                     │
            ├──○ Inscribirse en clase ──«include»──> Comprobar   │
            ├──○ Cancelar inscripción                cuotas al   │
            ├──○ Consultar mis cuotas                 corriente  │
            │                                                    │
    Monitor─┼──○ Consultar asistentes a mi clase                 │
            │                                                    │
    Recep. ─┼──○ Dar de alta socio                               │
            ├──○ Registrar pago de cuota                         │
            ├──○ Consultar socios con cuotas pendientes          │
            └──○ Dar de alta clase colectiva ────────────────────┘

| Caso de uso | Actor | Requisito |
|---|---|---|
| Consultar horario de clases | Socio | RF-04 |
| Inscribirse en clase | Socio | RF-05 |
| Cancelar inscripción | Socio | RF-07 |
| Consultar mis cuotas | Socio | RF-02, RF-03 |
| Consultar asistentes a mi clase | Monitor | RF-04 |
| Dar de alta socio | Recepcionista | RF-01 |
| Registrar pago de cuota | Recepcionista | RF-02 |
| Consultar socios con cuotas pendientes | Recepcionista | RF-03 |
| Dar de alta clase colectiva | Recepcionista | RF-04 |

### Apartado 3: justificación del «include»

«Comprobar cuotas al corriente» (derivado de RF-08) se ejecuta **siempre** como
parte de «Inscribirse en clase», y **nunca** de forma aislada por decisión del
actor. Eso es exactamente lo que significa `include`: comportamiento común y
obligatorio que se extrae para no repetirlo.

La distinción con `extend` conviene dejarla clara: `extend` sería el caso
contrario, comportamiento **opcional** que se añade en ciertas condiciones. Si el
gimnasio ofreciese «apuntarse a la lista de espera» solo cuando la clase está
llena, esa sí sería una relación `extend` sobre «Inscribirse en clase».

**Error frecuente que conviene señalar al corregir:** dibujar como casos de uso
pasos técnicos internos («validar datos», «conectar a la base de datos»). Un caso
de uso debe aportar valor observable a un actor. Si el actor no puede explicar
para qué le sirve, no es un caso de uso.

### Apartado 5: descripción textual de «Inscribirse en clase»

    Actor principal : Socio
    Precondiciones  : el socio ha iniciado sesión en el sistema
    Postcondiciones : existe una inscripción en estado PENDIENTE_PAGO y las
                      plazas libres de la clase han disminuido en una unidad

    FLUJO NORMAL
      1. El socio consulta el horario de clases disponibles.
      2. El socio selecciona una clase.
      3. El sistema comprueba que el socio está al corriente de pago.
      4. El sistema comprueba que la clase tiene plazas libres.
      5. El sistema registra la inscripción y decrementa las plazas.
      6. El sistema confirma la inscripción al socio.

    FLUJO ALTERNATIVO A — clase completa (en el paso 4)
      4a. El sistema informa de que no quedan plazas.
      4b. El sistema ofrece incorporarse a la lista de espera.
      4c. Si el socio acepta, se registra en estado EN_ESPERA (ver ejercicio 12).

    FLUJO ALTERNATIVO B — cuotas pendientes (en el paso 3)
      3a. El sistema rechaza la inscripción indicando los meses impagados.
      3b. El caso de uso termina sin registrar la inscripción.

    FLUJO ALTERNATIVO C — el socio ya está inscrito (en el paso 4)
      4a. El sistema informa de que ya tiene plaza en esa clase.
      4b. El caso de uso termina sin crear una segunda inscripción.

---

## Ejercicio 4 — Diagrama de clases con notación completa

### Apartados 1 a 4: el diagrama

    ┌─────────────────────────────┐        ┌──────────────────────────────┐
    │         Socio                │        │      ClaseColectiva          │
    ├─────────────────────────────┤        ├──────────────────────────────┤
    │ - dni: String                │        │ - codigo: String             │
    │ - nombre: String             │        │ - diaSemana: String          │
    │ - fechaAlta: LocalDate       │        │ - hora: LocalTime            │
    ├─────────────────────────────┤        │ - aforoMaximo: int           │
    │ + estaAlCorriente(): boolean │        ├──────────────────────────────┤
    │ + inscribirse(c): void       │        │ + plazasLibres(): int        │
    │ # calcularCuota(): double    │        │ + estaCompleta(): boolean    │
    └──────────┬──────────────────┘        └───────────┬──────────────────┘
               │ 1                                      │ 1
               │                                        │
               │ 0..*        ┌────────────────┐         │ 0..*
               └────────────>│  Inscripcion   │<────────┘
                             ├────────────────┤
                             │ - fecha: LocalDate │
                             │ - estado: Estado   │
                             ├────────────────┤
                             │ + pagar(): void    │
                             │ + cancelar(): void │
                             │ + finalizar(): void│
                             └────────────────┘

Notación empleada:

| Símbolo UML | Significado | Equivalente en Java |
|---|---|---|
| `+` | público | `public` |
| `-` | privado | `private` |
| `#` | protegido | `protected` |
| (sin símbolo) | paquete | sin modificador |
| rombo relleno | composición | el todo crea las partes |
| rombo hueco | agregación | el todo recibe las partes |
| flecha triangular | herencia | `extends` |
| `0..*` | multiplicidad «muchos» | una colección |

### Apartados 5 y 6: traducción a Java y comprobación inversa

La traducción está implementada y **ejecutable** en:

    src/com/ifcd0112/ejercicios/uf2406/bloque2_uml/Ej04DiagramaClases.java

Ese fichero no se limita a escribir las clases: usa reflexión para **leer sus
propios modificadores de acceso y reconstruir el diagrama**, que es la forma
literal de responder al apartado 6. Si alguien cambiase un `private` por un
`public`, la comprobación lo detectaría.

---

## Ejercicio 5 — Diagramas de secuencia y de estados

### Apartados 1, 2 y 3: diagrama de secuencia

     Socio      InscripcionServlet   GestorInscripciones   ClaseDAO      BD
       │                │                     │               │           │
       │──inscribir()──>│                     │               │           │
       │                │──inscribir(dni,cod)>│               │           │
       │                │                     │──buscarClase()│           │
       │                │                     │               │──SELECT──>│
       │                │                     │               │<──datos───│
       │                │                     │<───clase──────│           │
       │                │                     │               │           │
       │                │        [si hay plazas libres]       │           │
       │                │                     │──guardar()───>│           │
       │                │                     │               │──INSERT──>│
       │                │<────confirmación────│               │           │
       │<──página OK────│                     │               │           │
       │                │                     │               │           │
       │                │   [ALTERNATIVO: clase completa]     │           │
       │                │<─ClaseCompletaException─│           │           │
       │<──mensaje error│                     │               │           │

Las tres capas del apartado 2 quedan a la vista en las líneas de vida:

- **Presentación**: `InscripcionServlet`. Recibe y responde; no decide nada.
- **Negocio**: `GestorInscripciones`. Aquí vive la regla «no inscribir si está
  completa». Es la única línea de vida que toma decisiones.
- **Datos**: `ClaseDAO` y la base de datos. Leen y escriben; no saben por qué.

Obsérvese que las flechas nunca saltan una capa: el Servlet no habla con el DAO.
Si en un diagrama de secuencia aparece esa flecha directa, la arquitectura en
capas está rota en el diseño, antes incluso de escribir código.

### Apartados 4 y 5: diagrama de estados de `Inscripcion`

      ●
      │ crear()
      ▼
    ┌────────────────┐  pagar()   ┌──────────┐  finalizar()   ┌────────────┐
    │ PENDIENTE_PAGO │──────────>│  ACTIVA   │──────────────>│ FINALIZADA │
    └───────┬────────┘            └─────┬────┘                └────────────┘
            │ cancelar()                │ cancelar()
            ▼                           │
       ┌───────────┐<──────────────────┘
       │ CANCELADA │
       └───────────┘

    Transiciones NO permitidas:
        FINALIZADA -> cualquier otro estado
        CANCELADA  -> cualquier otro estado
        PENDIENTE_PAGO -> FINALIZADA  (hay que pagar primero)

| Transición | Método que la provoca |
|---|---|
| (inicio) → PENDIENTE_PAGO | constructor |
| PENDIENTE_PAGO → ACTIVA | `pagar()` |
| PENDIENTE_PAGO → CANCELADA | `cancelar()` |
| ACTIVA → CANCELADA | `cancelar()` |
| ACTIVA → FINALIZADA | `finalizar()` |

### Apartado 6: implementación

La clase `Inscripcion` que impide las transiciones no dibujadas está implementada
y **ejecutada** en:

    src/com/ifcd0112/ejercicios/uf2406/bloque2_uml/Ej05SecuenciaEstados.java

El fichero recorre las cinco transiciones válidas y las nueve inválidas, y
comprueba que las primeras funcionan y las segundas lanzan excepción. Es la
traducción literal de la pista del enunciado: **cada transición no dibujada en el
diagrama debe ser una transición rechazada por el método correspondiente**.
