# UF2406 · Bloque 4 — Diseño de las pruebas

Artefactos de los ejercicios 7 y 8. Los importes y las clasificaciones de estas
tablas **los calcula y verifica el código**: `Ej07CajaNegra` recorre la batería
completa y `Ej08CajaBlanca` compara las dos versiones del método sobre todo el
espacio de entradas.

---

## Ejercicio 7 — Caja negra sobre el cálculo de la cuota

Reglas: base 40 EUR · menores de 26 → −25 % · 65 o más → −30 % · más de 24 meses
de antigüedad → −5 EUR adicionales · mínimo 20 EUR.

### Apartado 1: clases de equivalencia

    PARÁMETRO EDAD
      CE-1  edad < 0            inválida
      CE-2  0 <= edad <= 25     descuento del 25 %
      CE-3  26 <= edad <= 64    sin descuento por edad
      CE-4  edad >= 65          descuento del 30 %

    PARÁMETRO ANTIGÜEDAD (meses)
      CE-5  antigüedad < 0      inválida
      CE-6  0 <= ant <= 24      sin descuento adicional
      CE-7  antigüedad > 24     5 EUR adicionales de descuento

### Apartado 2: valores límite

    Edad:        -1, 0, 25, 26, 64, 65, 66
    Antigüedad:  -1, 0, 24, 25

Dentro de una clase de equivalencia todos los valores se comportan igual: probar
30, 40 y 50 aporta lo mismo que probar solo 40. Lo que no da igual es el borde,
porque ahí es donde se equivoca quien programa (`<=` en lugar de `<`, restar uno
de menos).

### Apartado 3: tabla de decisión

| Condición | C1 | C2 | C3 | C4 | C5 | C6 |
|---|---|---|---|---|---|---|
| Edad < 26 | Sí | Sí | No | No | No | No |
| Edad >= 65 | No | No | No | No | Sí | Sí |
| Antigüedad > 24 meses | No | Sí | No | Sí | No | Sí |
| **Cuota resultante** | **30,00** | **25,00** | **40,00** | **35,00** | **28,00** | **23,00** |

### Apartado 4: batería mínima de casos de prueba

| CP | Edad | Antig. | Esperado | Verifica |
|---|---|---|---|---|
| 1 | 20 | 12 | 30,00 | CE-2 + CE-6 |
| 2 | 25 | 12 | 30,00 | LÍMITE superior de joven |
| 3 | 26 | 12 | 40,00 | LÍMITE inferior de estándar |
| 4 | 40 | 12 | 40,00 | CE-3 + CE-6 |
| 5 | 64 | 12 | 40,00 | LÍMITE superior de estándar |
| 6 | 65 | 12 | 28,00 | LÍMITE inferior de senior |
| 7 | 70 | 12 | 28,00 | CE-4 + CE-6 |
| 8 | 40 | 24 | 40,00 | LÍMITE: 24 meses aún no dan antigüedad |
| 9 | 40 | 25 | 35,00 | LÍMITE: 25 meses ya la dan |
| 10 | 20 | 36 | 25,00 | CE-2 + CE-7 |
| 11 | −1 | 12 | excepción | CE-1, entrada inválida |
| 12 | 30 | −5 | excepción | CE-5, entrada inválida |

### Apartado 5: entradas inválidas

El método debe lanzar `IllegalArgumentException`, no devolver cero ni un valor
cualquiera. Una edad negativa no es un caso de negocio raro: es un error de quien
llama. Devolver un número lo escondería, y la cuota saldría mal en una factura
sin que nadie supiera por qué.

Sobre la «antigüedad no numérica» que menciona el enunciado: si el parámetro es
`int`, no puede llegar texto — el compilador no lo permite. Ese caso pertenece a
la capa que convierte lo que escribe el usuario, y es exactamente el problema del
ejercicio 11 de la UF2405. Aquí se prueba con antigüedad negativa, que es el
equivalente con este tipo de dato. **El enunciado está mezclando dos capas**, y
merece la pena señalarlo en clase.

### Apartado 6: el error `edad < 25`

Lo detecta **únicamente CP-2** (edad 25): con el código correcto devuelve 30,00 y
con el erróneo 40,00. Los casos centrales (20, 40, 70) pasan igual de bien con el
código roto. Es la demostración de por qué una batería sin valores límite da una
falsa sensación de seguridad.

### Hallazgo: el suelo de 20 EUR es inalcanzable

Recorriendo todas las entradas de 0 a 120 años y de 0 a 600 meses, la cuota mínima
posible es **23,00 EUR** (65 años, más de 24 meses: 40 × 0,70 − 5). La regla «la
cuota final nunca puede ser inferior a 20 EUR» **no se activa nunca**.

No es un fallo grave, pero da juego en clase: es un ejemplo real de código
defensivo que nunca se ejecuta y que, por tanto, nunca se prueba. Si mañana se
añade un descuento de socio fundador, ese suelo entraría en funcionamiento por
primera vez sin haber sido verificado jamás. Es también la razón por la que una
herramienta de cobertura marcaría esa línea en rojo, y por qué eso es información
útil y no ruido.

---

## Ejercicio 8 — Caja blanca sobre `clasificarSocio`

### Apartados 1 y 2: grafo de flujo y complejidad

             inicio
               │
         ┌─ ¿!cuotaAlDia? ─Sí──> return MOROSO              (camino 1)
         No
         │
         ├─ ¿antigüedad>60? ─Sí─┬─ ¿edad>=65? ─Sí─> VETERANO_SENIOR  (camino 2)
         No                     └─ No ──────────> VETERANO          (camino 3)
         │
         ├─ ¿edad<26? ─Sí──────────────────────> JOVEN              (camino 4)
         No
         └────────────────────────────────────> ESTANDAR            (camino 5)

    Decisiones (if): 4    →    Complejidad ciclomática V(G) = 4 + 1 = 5
    Casos de prueba mínimos para cubrir los caminos independientes: 5

La fórmula de la pista (`decisiones + 1`) es la versión práctica; la formal es
`aristas − nodos + 2` y da lo mismo. Lo que conviene no olvidar: **los operadores
`&&` y `||` también cuentan**, porque generan una bifurcación aunque estén
escritos en la misma línea. Un `if` con tres condiciones encadenadas suma tres.

### Apartado 3: un caso por camino

| CP | edad | antig. | cuotaAlDía | Camino | Esperado |
|---|---|---|---|---|---|
| 1 | 30 | 12 | false | 1 | MOROSO |
| 2 | 70 | 72 | true | 2 | VETERANO_SENIOR |
| 3 | 40 | 72 | true | 3 | VETERANO |
| 4 | 22 | 12 | true | 4 | JOVEN |
| 5 | 40 | 12 | true | 5 | ESTANDAR |

### Apartado 4: comparación con la caja negra

**Lo que ve la caja negra y la blanca no exige:** los valores frontera. Para
cubrir el camino 4 basta cualquier edad menor de 26, así que la caja blanca se
conforma con 22 — y el error `edad < 25` pasaría desapercibido.

**Lo que ve la caja blanca y la negra no puede ver:** que ninguna rama del código
queda sin ejecutar. Si el método tuviera una rama que el enunciado no menciona
(un caso especial que alguien añadió y no documentó), la caja negra no la probaría
nunca, porque no sabe que existe.

Son complementarias, no alternativas. La negra comprueba que el programa hace lo
que se pidió; la blanca, que todo lo que el programa hace se ha probado.

### Apartado 5: refactorización

```java
public String clasificarSocio(int edad, int antiguedadMeses, boolean cuotaAlDia) {
    if (!cuotaAlDia) return "MOROSO";
    if (esVeterano(antiguedadMeses)) return clasificarVeterano(edad);
    return clasificarPorEdad(edad);
}

private boolean esVeterano(int meses)     { return meses > 60; }
private String clasificarVeterano(int e)  { return (e >= 65) ? "VETERANO_SENIOR" : "VETERANO"; }
private String clasificarPorEdad(int e)   { return (e < 26)  ? "JOVEN" : "ESTANDAR"; }
```

La complejidad total del problema no desaparece: se reparte. El método principal
baja de 5 a 3 y cada auxiliar queda en 2. Lo importante no es el número, sino que
cada trozo se puede probar por separado y que el método principal se lee casi como
el enunciado del requisito.

`Ej08CajaBlanca` comprueba la equivalencia de las dos versiones sobre **58.322
combinaciones** de entrada, no solo sobre los cinco casos: con cinco casos se
cubren los caminos, con la comprobación exhaustiva se demuestra la equivalencia.
