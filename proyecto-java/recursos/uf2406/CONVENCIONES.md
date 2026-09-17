# Convenciones de documentación del equipo

Ejercicio 11 de la UF2406, apartado 3. Este fichero es el criterio de
normalización acordado, y se aplica a todo el proyecto.

## Idioma y formato

1. **Idioma de la documentación: castellano.** Los identificadores también.
   La decisión importante no es cuál se elija, sino que sea **uno solo**: un
   proyecto con `getNombre()` junto a `findByName()` cuesta más de leer que
   cualquiera de los dos criterios por separado.

2. **Sin tildes ni eñes en el código fuente `.java`.** No es purismo: evita
   problemas de codificación al compilar en máquinas con distinta configuración
   regional, que es un fallo silencioso y difícil de diagnosticar. Se escribe
   `numero`, `codigo`, `anadir`, `diseno`. En los ficheros que no son código
   (Markdown, SQL, HTML) sí se usan tildes normalmente.

3. **Ancho máximo de línea: 100 caracteres.** Permite ver dos ficheros en
   paralelo y hace legibles los diff.

## Javadoc

4. **Toda clase pública** lleva descripción de su propósito, `@author` y
   `@version`.

5. **Todo método público** lleva descripción y `@param` por cada parámetro.

6. Se documenta **`@return`** salvo en métodos `void`.

7. Se documenta **`@throws` por cada excepción comprobada declarada**, y también
   por las no comprobadas que formen parte del contrato (por ejemplo, un
   `IllegalArgumentException` que el método lanza deliberadamente ante una
   entrada inválida: eso es contrato, no un accidente).

8. **Los métodos privados no llevan Javadoc completo.** Un comentario de una
   línea basta. La documentación generada es para quien usa la clase desde
   fuera, y a ese no le interesan.

9. Los *getters* y *setters* triviales pueden documentarse en una sola línea:
   `/** @return nombre del socio */`. Escribirles tres líneas de Javadoc es
   ruido que dificulta encontrar lo que sí importa.

## Comentarios internos

10. **Los comentarios `//` explican el PORQUÉ, nunca el QUÉ.** El qué ya lo dice
    la instrucción. Si el comentario se puede deducir leyendo la línea, sobra.

11. Un comentario que sobra no es neutro: **envejece**. El código se modifica y
    el comentario no, hasta que acaba mintiendo. Un comentario equivocado hace
    más daño que la ausencia de comentario.

12. Merecen comentario: las decisiones que no son evidentes, los límites del
    negocio, los fallos que se están evitando, el orden de unas comprobaciones
    cuando importa, y cualquier cosa que a uno mismo le costó media hora
    entender.

### Ejemplos

```java
// SOBRA: repite la instrucción
i++;                    // incrementa i en uno

// APORTA: explica una decisión que no se ve
contador++;             // DENTRO del if, porque si no se cuentan también los inactivos

// SOBRA
con.setAutoCommit(true);   // pone el autocommit a true

// APORTA
con.setAutoCommit(true);   // la conexión puede volver a un pool: si se devuelve
                           // con el autocommit desactivado, el siguiente que la
                           // use se encuentra una transacción abierta sin saberlo
```

## Historial de modificaciones

13. Cuando una clase se modifica por un cambio de especificación, se registra en
    su cabecera la versión, la fecha, el requisito que lo motiva y una
    descripción breve. Ver el ejercicio 12.

## Cómo generar la documentación

```
javadoc -d docs -encoding UTF-8 -charset UTF-8 \
        -sourcepath src -subpackages com.ifcd0112.ejercicios
```

Sin `-private` solo se documentan los miembros públicos y protegidos, que es lo
recomendable para la documentación que se entrega. Con `-private` se genera la
referencia interna completa.

## La prueba de calidad

Entregar la documentación generada a alguien que no haya visto el código y
pedirle que use una de las clases guiándose solo por ella. **Si tiene que abrir
el fuente, la documentación no está terminada.**
