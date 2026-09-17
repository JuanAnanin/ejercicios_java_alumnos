# Estructura del proyecto y ejecución

## Cómo está organizado

```
proyecto-java/
├── src/com/ifcd0112/ejercicios/
│   ├── Main.java
│   ├── POOExpress/     8 bloques · Programación Express (introducción a Java)
│   ├── uf2404/         5 bloques · 14 ejercicios
│   ├── uf2405/         5 bloques · 14 ejercicios
│   ├── uf2406/         5 bloques · 15 ejercicios
│   └── ampliacion/     14 ejercicios que completan la cobertura del RD
└── recursos/           artefactos que NO son Java ejecutable
    ├── uf2405/sql/     4 scripts SQL de la clínica veterinaria
    ├── uf2405/web/     formulario HTML, 7 Servlets, JSP, filtro y pool
    ├── uf2406/         requisitos, UML, planificación, diseño de pruebas
    └── ampliacion/     cuaderno en PDF, plantillas y documentos de ejemplo
```

El paquete `bloques` pasó a llamarse **`POOExpress`** para distinguirlo a simple
vista de los ejercicios de las unidades formativas. El cambio afectó a la
declaración `package` de sus 8 ficheros y a los 8 `import` de `Main.java`; el
contenido de los ejercicios no se tocó.

> Nota menor: la convención de Java pide nombres de paquete en minúscula
> (`pooexpress`). `POOExpress` compila y funciona sin ningún problema, y gana en
> legibilidad dentro del árbol de directorios; queda anotado por si en algún
> momento prefieres ajustarlo a la convención.

## `Main.java` ya está actualizado

Lleva los tres imports nuevos:

```java
import com.ifcd0112.ejercicios.uf2404.UF2404Ejercicios;
import com.ifcd0112.ejercicios.uf2405.UF2405Ejercicios;
import com.ifcd0112.ejercicios.uf2406.UF2406Ejercicios;
import com.ifcd0112.ejercicios.ampliacion.AmpliacionEjercicios;
```

y las cuatro llamadas, después de los 8 bloques de Programación Express:

```java
        // ------------------------------------------------------------------
        // PARTE 2: ejercicios del modulo formativo MF0227_3, por unidad
        // formativa. 43 ejercicios: 14 de la UF2404, 14 de la UF2405 y 15 de
        // la UF2406.
        // ------------------------------------------------------------------
        UF2404Ejercicios.ejecutar();
        UF2405Ejercicios.ejecutar();
        UF2406Ejercicios.ejecutar();

        // ------------------------------------------------------------------
        // PARTE 3: ampliacion. 12 ejercicios que completan la cobertura de los
        // criterios de evaluacion y anaden ficheros y genericos propios.
        // ------------------------------------------------------------------
        AmpliacionEjercicios.ejecutar();
```

## Compilar y ejecutar

```bash
# Compilar todo: un solo comando, sin dependencias
javac -d out $(find src -name "*.java")

# El cuaderno completo
java -cp out com.ifcd0112.ejercicios.Main
```

**Aviso sobre `Main`**: los bloques de `POOExpress` son interactivos y piden
datos por teclado, así que `Main` se queda esperando entrada hasta que se
completan. Es el comportamiento de siempre, no ha cambiado. Para ver solo los
ejercicios del módulo formativo, sin interacción, conviene lanzar cada unidad
por separado.

### Una unidad formativa entera

```bash
java -cp out com.ifcd0112.ejercicios.uf2404.UF2404Ejercicios
java -cp out com.ifcd0112.ejercicios.uf2405.UF2405Ejercicios
java -cp out com.ifcd0112.ejercicios.uf2406.UF2406Ejercicios
java -cp out com.ifcd0112.ejercicios.ampliacion.AmpliacionEjercicios
```

### Un bloque suelto

```bash
java -cp out com.ifcd0112.ejercicios.uf2404.bloque3_estructuras.Bloque3Estructuras
java -cp out com.ifcd0112.ejercicios.uf2406.bloque4_pruebas.Bloque4Pruebas
```

### Un ejercicio suelto

```bash
java -cp out com.ifcd0112.ejercicios.uf2404.bloque5_integrador.Ej13Videoclub
java -cp out com.ifcd0112.ejercicios.uf2406.bloque3_planificacion.Ej06GanttCaminoCritico
```

Para clase resulta cómodo: se proyecta un solo ejercicio sin arrastrar la salida
de los cuarenta y dos restantes.

## Tres ejercicios con modo interactivo

Tienen una segunda forma de ejecución, que se activa con un argumento:

```bash
# UF2404 - Ej13: menú de consola del videoclub
java -cp out com.ifcd0112.ejercicios.uf2404.bloque5_integrador.Ej13Videoclub menu

# UF2406 - Ej14: la ventana Swing de inscripción a clases
java -cp out com.ifcd0112.ejercicios.uf2406.bloque5_documentacion.Ej14InterfazUsabilidad ventana
```

Y el de sockets se puede probar como pide su enunciado, con dos ventanas:

```bash
# Ventana 1: el servidor
java -cp out 'com.ifcd0112.ejercicios.uf2404.bloque4_excepciones.Ej12Sockets$ServidorPrincipal'
# Ventana 2: el cliente
java -cp out 'com.ifcd0112.ejercicios.uf2404.bloque4_excepciones.Ej12Sockets$Cliente'
```

(en Windows, las comillas se sustituyen por `Ej12Sockets"$"ServidorPrincipal`)

## Generar la documentación Javadoc

```bash
javadoc -d docs -encoding UTF-8 -charset UTF-8 \
        -sourcepath src -subpackages com.ifcd0112.ejercicios
```

## Nota sobre `recursos/`

La carpeta `recursos/` **no forma parte de la compilación**. El comando de
construcción solo mira dentro de `src`, así que nada de lo que hay ahí puede
romper el proyecto.

Ahí viven los artefactos que no son Java ejecutable: los scripts SQL, el
formulario HTML, los Servlets, el JSP y los documentos de análisis,
planificación y diseño de pruebas. Cada fichero `.java` del proyecto apunta al
recurso que le corresponde.


## Los ejercicios de ampliación

El paquete `ampliacion` añade 14 ejercicios repartidos por unidad formativa
(6 + 2 + 6). Cubren los criterios de evaluación del Real Decreto 628/2013 que no
tenían ejercicio en los tres cuadernos originales, y añaden entrada/salida de
ficheros y tipos genéricos propios, que el RD no exige pero conviene dominar.

**Los enunciados en limpio, para repartir al alumnado**, están en:

    recursos/ampliacion/Ejercicios_Ampliacion_MF0227_3.pdf

Los documentos que producen esos ejercicios —plantilla y documento de diseño,
informe de pruebas, manual de operación y guía de usuario— están en la misma
carpeta y sirven de solución de referencia.

Con esta ampliación la cobertura de criterios de evaluación pasa de **46 a 68**
de los 68 del módulo, es decir, ninguno se queda sin ejercicio.

Dos matices sobre esa cifra:

- Los ejercicios **A13** (ciclo de desarrollo, CE1.1) y **A14** (paso de mensajes,
  CE1.4) son los que cierran los dos últimos huecos de la UF2404. Son criterios
  de «explicar», pero los dos se ejecutan: A13 comprueba en disco que cada fase
  del ciclo tiene su artefacto en el proyecto y valida la trazabilidad de la
  cadena; A14 demuestra con reflexión quién resuelve cada mensaje y cuándo.
- El **CE4.5** de la UF2406 lo cumple punto por punto el ejercicio 12 de ese
  cuaderno. El `.java` ya lo declara junto a CE4.1 y CE4.3, con una nota
  explicándolo; **conviene reflejar el cambio también en el `.docx`**, que aún
  lo atribuye solo a los otros dos.

## La matriz de criterios de evaluación

    recursos/Matriz_Criterios_Evaluacion_MF0227_3.pdf
    recursos/MATRIZ_CE.md

Correspondencia en los dos sentidos entre los 57 ejercicios y los 68 criterios
del Real Decreto 628/2013 (anexo IV, IFCD0112): para cada ejercicio, qué
criterios acredita; para cada criterio, con qué ejercicio se demuestra. Incluye
el texto literal de cada criterio.

Los dos ficheros se generan del propio proyecto, así que se pueden regenerar
después de tocar cualquier ejercicio:

```bash
cd recursos            && python3 gen_matriz.py   # matriz de criterios
cd recursos/ampliacion && python3 gen_pdf.py      # cuaderno de enunciados
```

Los dos leen las cabeceras Javadoc de los 57 ficheros de ejercicio, así que las
cifras de cobertura que aparecen en los PDF no están escritas a mano: se
recalculan en cada generación.

### Ejecutar solo la ampliación

```bash
java -cp out com.ifcd0112.ejercicios.ampliacion.AmpliacionEjercicios

# o una sola unidad formativa de la ampliación
java -cp out com.ifcd0112.ejercicios.ampliacion.uf2404.AmpliacionUf2404
java -cp out com.ifcd0112.ejercicios.ampliacion.uf2405.AmpliacionUf2405
java -cp out com.ifcd0112.ejercicios.ampliacion.uf2406.AmpliacionUf2406

# o un ejercicio suelto
java -cp out com.ifcd0112.ejercicios.ampliacion.uf2404.A02EstructurasGenericas
java -cp out com.ifcd0112.ejercicios.ampliacion.uf2404.A14PasoDeMensajes
java -cp out com.ifcd0112.ejercicios.ampliacion.uf2406.A12ValidacionAccesibilidad
```
