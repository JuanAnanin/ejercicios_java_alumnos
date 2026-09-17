# Cuaderno de Ejercicios de Java

Proyecto que resuelve los 8 bloques del cuaderno (75 ejercicios + 3 mini proyectos),
con un archivo por bloque y un `Main` que los importa y ejecuta en orden.

## Estructura

```
src/
  com/ifcd0112/ejercicios/
    Main.java                         <- punto de entrada (importa y ejecuta los 8 bloques)
    bloques/
      Bloque1Variables.java           <- 20 ejercicios: variables y tipos de datos
      Bloque2Condicionales.java       <- 10 ejercicios: estructuras condicionales
      Bloque3Bucles.java              <- 10 ejercicios: bucles
      Bloque4Metodos.java             <- 10 ejercicios: metodos y funciones
      Bloque5ClasesObjetos.java       <- 10 ejercicios: clases y objetos
      Bloque6Encapsulamiento.java     <- 10 ejercicios: encapsulamiento y accesos
      Bloque7Integradores.java        <-  5 ejercicios integradores
      Bloque8MiniProyectos.java       <-  3 mini proyectos completos
```

Cada ejercicio:
- va precedido de un comentario con su enunciado,
- tiene comentarios explicativos en cada parte,
- imprime por consola lo que va haciendo.

Cada clase de bloque tiene su propio metodo `ejecutar()` (lo lanza el `Main`) y
ademas un `main` propio, por si quieres ejecutar un bloque de forma aislada.

## Como compilar y ejecutar

Necesitas el **JDK** (incluye `javac`). Desde la raiz del proyecto:

```bash
# 1) Compilar todo a la carpeta out/
javac -d out $(find src -name "*.java")

# 2) Ejecutar el Main
java -cp out com.ifcd0112.ejercicios.Main
```

Ejecutar un unico bloque (sin compilar el proyecto entero, requiere JDK 11+):

```bash
java src/com/ifcd0112/ejercicios/bloques/Bloque1Variables.java
```

## Notas de diseno

- **Lectura por teclado (Scanner):** muchos enunciados piden leer datos con
  `Scanner`. Para que el programa se ejecute de corrido sin bloquearse pidiendo
  decenas de entradas, esos ejercicios usan valores de ejemplo y dejan, en
  comentario, la linea `Scanner` equivalente. En el Bloque 8 los menus/combate
  interactivos estan implementados de verdad (`menuInteractivo`, `combateInteractivo`)
  y ademas hay una demo automatica; para usar la version interactiva real solo
  hay que descomentar las llamadas indicadas al final de `Bloque8MiniProyectos`.

- **Bloque 6 (paquetes):** los ejercicios 3, 4 y 8 piden crear paquetes distintos
  para comprobar el acceso `package-private` y `protected` entre paquetes. Como se
  mantiene "un archivo por bloque", esos casos se demuestran con clases anidadas y
  se explica en comentarios el comportamiento entre paquetes. Si te lo piden con
  paquetes reales, basta con mover cada clase a su carpeta/paquete.

- **Clases de dominio (bloques 5 a 8):** se declaran como clases anidadas estaticas
  (`static class`) dentro del archivo del bloque para respetar "un archivo por bloque".
  En un proyecto real, cada una iria en su propio `.java`.
```
