# UF2406 · Bloque 5 — Cambio de especificación, cierre y calidad

Artefactos de los ejercicios 12 y 15.

---

## Ejercicio 12 — Cambio de especificación: lista de espera (RF-09)

### Apartado 1: tabla de análisis de impacto

| Elemento afectado | Tipo | Impacto |
|---|---|---|
| RF-06 (rechazar si lleno) | Requisito | **MODIFICAR**: ahora ofrece lista de espera |
| RF-09 (nuevo) | Requisito | **CREAR**: gestión de lista de espera |
| `Inscripcion` | Clase | Nuevo estado `EN_ESPERA` y sus transiciones |
| `ClaseColectiva` | Clase | Nuevo método `darDeBaja()` con promoción |
| `GestorInscripciones` | Clase | Modificar `inscribir()` y `cancelar()` |
| Tabla `inscripcion` | **BD** | Ampliar los valores admitidos del campo `estado` |
| Diagrama de estados | Doc. | Añadir estado y transiciones |
| Diagrama de casos de uso | Doc. | Añadir «apuntarse a lista de espera» como `extend` |
| Pruebas de inscripción | Pruebas | Añadir casos, y **modificar una existente** |

**El elemento que más se olvida es la tabla de la base de datos.** Un cambio de
estados no se queda en el código: si el campo `estado` tiene una restricción
`CHECK` con los cuatro valores antiguos, el primer `INSERT` con `EN_ESPERA` falla
**en producción y no en desarrollo**, porque en desarrollo alguien recreó la tabla.

### Apartado 5: diagrama de estados actualizado

      ●
      │ solicitar(), hay plaza          ● solicitar(), clase completa
      ▼                                 │
    ┌────────────────┐                   ▼
    │ PENDIENTE_PAGO │<──promocionar()──┌───────────┐
    └───┬────────┬───┘                  │ EN_ESPERA │
        │        │ pagar()              └─────┬─────┘
        │        ▼                            │ cancelar()
        │   ┌──────────┐  finalizar()         │
        │   │  ACTIVA  │────────────> ┌────────────┐
        │   └────┬─────┘              │ FINALIZADA │
        │        │ cancelar()         └────────────┘
        │        │
        │ cancelar()
        ▼        ▼
       ┌───────────┐
       │ CANCELADA │
       └───────────┘

    Transiciones nuevas en 1.1:
        (inicio, clase completa) --> EN_ESPERA
        EN_ESPERA --promocionar()--> PENDIENTE_PAGO
        EN_ESPERA --cancelar()-----> CANCELADA

    Sigue prohibido: pagar() desde EN_ESPERA (todavía no tiene plaza).

### Apartado 6: informe de la batería de regresión

    Tests run: 10, Failures: 0, Errors: 0, Skipped: 0

      5 pruebas ANTERIORES al cambio, que siguen pasando
      5 pruebas NUEVAS del RF-09

Ejecutable con:

    java -cp out com.ifcd0112.ejercicios.uf2406.bloque5_documentacion.Ej12CambioEspecificacion

### Apartado 7: el mensaje del control de versiones

    RF-09: gestión de lista de espera en clases completas.

    Las inscripciones sobre clases con aforo lleno pasan a estado EN_ESPERA en
    lugar de rechazarse, y se promocionan automáticamente a PENDIENTE_PAGO al
    producirse una baja, por orden de llegada.

    Modifica el comportamiento de GestorInscripciones.inscribir(), que hasta
    ahora lanzaba ClaseCompletaException. La prueba
    lanzaExcepcionSiLaClaseEstaCompleta se sustituye por
    inscripcionEnClaseCompletaEntraEnListaDeEspera.

    Incluye 4 pruebas nuevas. Afecta a la tabla inscripcion: hay que ampliar
    los valores admitidos del campo estado.

Primera línea el **qué**, cuerpo el **por qué**, referencia al requisito para
enlazar el código con la decisión de negocio. Dentro de seis meses, cuando
alguien investigue por qué una inscripción quedó en espera, va a mirar aquí.

---

## Ejercicio 15 — Cierre del ciclo de vida

### Apartado 2: matriz de trazabilidad

La matriz completa la construye y la **comprueba** el código, en
`Ej15ProyectoIntegrador.matrizTrazabilidad()`. Resultado: **0 requisitos sin
cubrir de 12** (9 funcionales + 3 no funcionales).

| Requisito | Caso de uso | Clases | Pruebas |
|---|---|---|---|
| RF-01 | Dar de alta socio | `Socio`, `SocioDAO` | `SocioDAOTest` |
| RF-02 | Registrar pago | `Cuota`, `GestorCuotas` | `GestorCuotasTest` |
| RF-03 | Consultar cuotas pendientes | `Socio.estaAlCorriente()`, `CuotaDAO` | `CuotasPendientesTest` |
| RF-04 | Dar de alta clase | `ClaseColectiva`, `Monitor` | `ClaseColectivaTest` |
| RF-05 | Inscribirse en clase | `GestorInscripciones`, `Inscripcion` | 2 casos |
| RF-06 | (incluido en RF-05) | `ClaseColectiva.estaCompleta()` | 2 casos |
| RF-07 | Cancelar inscripción | `Inscripcion.cancelar()`, `ClaseColectiva.darDeBaja()` | 2 casos |
| RF-08 | (incluido en RF-05) | `GestorInscripciones`, `Socio.estaAlCorriente()` | 1 caso |
| RF-09 | Apuntarse a lista de espera | `Inscripcion.EN_ESPERA`, `ClaseColectiva.darDeBaja()` | 3 casos |
| RNF-01 | (transversal) | `ConexionBD` (pool) | `PruebaCargaTest` |
| RNF-02 | (transversal) | `ConexionBD` (pool) | `PruebaConcurrenciaTest` |
| RNF-03 | (transversal) | `ServicioAutenticacion` | `HashContrasenasTest` |

Un requisito sin clase es **funcionalidad que no existe**. Un requisito sin
prueba es **funcionalidad que nadie sabe si funciona**. Por eso la matriz se
recorre y se comprueba, no se mira.

### Apartado 4: informe de pruebas y cobertura

    Tests run: 47, Failures: 0, Errors: 0, Skipped: 0

    Cobertura por paquete:
      negocio/       92 %   <- la lógica crítica, bien cubierta
      datos/         78 %   <- faltan casos de error de conexión
      modelo/        85 %
      presentacion/  34 %   <- los Servlets se prueban manualmente
      ─────────────────────
      TOTAL          74 %

**Sobre la cobertura, y conviene decirlo en clase:** mide qué porcentaje del
código *ejecutan* las pruebas, no qué porcentaje está *bien probado*. Son cosas
distintas. Una batería que recorra todo el código sin comprobar ni un resultado
da 100 % y no verifica nada.

Su utilidad real es la contraria: **encontrar lo que nunca se ejecuta**. El suelo
de 20 EUR del ejercicio 7 es el ejemplo perfecto, y se encontró exactamente así.

### Apartado 5: evaluación frente a los criterios de calidad

| Criterio | Grado | Argumento |
|---|---|---|
| Funcionalidad | ALTO | Los 9 RF implementados y verificados; la matriz no deja hueco. |
| Fiabilidad | MEDIO | Cubierta la lógica de negocio; falta probar la recuperación ante caída de la BD. |
| Usabilidad | MEDIO | 7 de las 10 heurísticas; pendiente deshacer y accesibilidad por teclado. |
| Eficiencia | ALTO | Pool de conexiones; RNF-01 verificado con prueba de carga. |
| Mantenibilidad | ALTO | Arquitectura en capas, DAO tras interfaz, bajo acoplamiento. |
| Portabilidad | ALTO | JDBC estándar: cambiar de SGBD exige cambiar URL y driver, no código. |

### Apartado 6: incumplimientos de SOLID detectados y corregidos

**1) Responsabilidad única (S).** `GestorInscripciones` además de inscribir
enviaba el correo de confirmación: dos motivos distintos para cambiar, y probarlo
exigía un servidor de correo. Corregido extrayendo `ServicioNotificaciones` e
inyectándolo por constructor.

**2) Inversión de dependencias (D).** El gestor creaba su DAO con `new`, atando
el negocio a una implementación concreta de la capa de datos. Corregido en el
ejercicio 10 extrayendo la interfaz `RepositorioClases` — y esa corrección es
justamente lo que hizo posible escribir las pruebas con objetos simulados.

En ambos casos la batería de regresión se ejecutó tras el cambio sin ningún
fallo, confirmando que el comportamiento externo no se alteró.

**Lo que merece la pena señalar:** las dos violaciones se detectaron *intentando
escribir pruebas*. Cuando una clase cuesta de probar, casi siempre es porque hace
demasiado o porque se fabrica sus dependencias. La dificultad para probar es el
mejor detector de problemas de diseño que existe, y es gratis.

---

## Apartado 7: informe final del proyecto

### Alcance cubierto

El sistema de gestión del gimnasio cubre los nueve requisitos funcionales y los
tres no funcionales acordados: alta y consulta de socios, registro y control de
cuotas, alta de clases colectivas, inscripción y cancelación con control de aforo
y de impagos, y la lista de espera incorporada a mitad de proyecto. Los doce
requisitos tienen clase que los implementa y prueba que los verifica.

### Desviaciones respecto a la planificación inicial

La planificación estimaba **20 días laborables** con camino crítico
A → B → D → F → G. Las dos desviaciones reales:

- El cambio de especificación del RF-09 (lista de espera) llegó cuando la
  integración ya había empezado. Costó **3 días** no previstos: análisis de
  impacto, implementación, pruebas nuevas y actualización de dos diagramas.
- «Desarrollo del acceso a datos», la tarea más larga del camino crítico, se
  ajustó a lo estimado gracias al prototipo de la primera semana, que era
  precisamente la mitigación prevista para el riesgo R2.

### Riesgos que llegaron a materializarse

- **R3 (cambio de requisitos a mitad del proyecto): SÍ.** Era el más probable y
  ocurrió. La mitigación prevista —validar con el cliente al terminar cada
  diseño— redujo el daño, pero no lo evitó: el gerente no pidió la lista de
  espera hasta ver la pantalla de inscripción funcionando. Eso no es un fallo de
  la mitigación, es la confirmación de que el modelo iterativo era la elección
  correcta.
- **R2 (el desarrollo de acceso a datos se alarga): NO.** El prototipo temprano
  hizo su trabajo.
- **R1 y R4: NO** se materializaron.

### Lecciones aprendidas

1. **Los valores frontera pagan solos.** El error `edad < 25` lo detectó un único
   caso de prueba, el de la frontera. Los tres casos centrales pasaban con el
   código roto.

2. **La dificultad para probar es un síntoma de diseño, no un problema de las
   pruebas.** Las dos violaciones de SOLID aparecieron al intentar escribir un
   test, no en una revisión de código.

3. **El cambio de especificación fue barato porque existían pruebas.** La batería
   previa permitió modificar una regla de negocio ya en producción sabiendo qué
   se rompía y qué no. Sin ella, el mismo cambio habría sido una apuesta.

4. **Los artefactos de análisis se pagan más tarde.** El diagrama de estados
   parecía un trámite en el bloque 2; en el bloque 5 fue literalmente la
   especificación de qué transiciones había que rechazar, y la tabla de impacto
   del cambio salió de él en diez minutos.

5. **La cobertura sirve para lo contrario de lo que se cree.** No para presumir
   de un porcentaje, sino para encontrar el código que nunca se ejecuta —como el
   suelo de 20 EUR, que llevaba desde el primer día sin poder activarse.
