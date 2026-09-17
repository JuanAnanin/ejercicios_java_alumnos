# Matriz de criterios de evaluación · MF0227_3 (IFCD0112)

Correspondencia, en los dos sentidos, entre los 57 ejercicios del proyecto y los 68 criterios de evaluación que fija el Real Decreto 628/2013, de 2 de agosto, en el anexo IV. Para cada ejercicio, qué criterios acredita; para cada criterio, con qué ejercicio se demuestra. Ningún criterio se queda sin ejercicio asociado.

| Unidad | Ej. cuaderno | Ej. ampliación | CE del RD | CE cubiertos solo cuaderno | CE cubiertos con ampliación | CE sin ejercicio |
|---|---|---|---|---|---|---|
| UF2404 | 14 | 6 | 18 | 13 | 18 | 0 |
| UF2405 | 14 | 2 | 14 | 11 | 14 | 0 |
| UF2406 | 15 | 6 | 36 | 23 | 36 | 0 |
| **TOTAL** | **43** | **14** | **68** | **47** | **68** | **0** |


## UF2404 — Programación orientada a objetos

### A. Qué criterios acredita cada ejercicio

| Ej. | Título | Criterios | Fichero |
|---|---|---|---|
| 1 | La clase Fraccion | CE1.2, CE1.3, CE2.10 | `uf2404/bloque1_clases/Ej01Fraccion` |
| 2 | Cuenta bancaria con control de saldo | CE1.2, CE2.4, CE2.10 | `uf2404/bloque1_clases/Ej02CuentaBancaria` |
| 3 | Jerarquia de empleados con nomina polimorfica | CE1.5, CE1.7, CE2.4 | `uf2404/bloque2_herencia/Ej03Empleados` |
| 4 | Interfaces: elementos reproducibles y descargables | CE1.2, CE1.6, CE2.10 | `uf2404/bloque2_herencia/Ej04Interfaces` |
| 5 | Composicion y agregacion: gestion de una biblioteca | CE1.8, CE2.3 | `uf2404/bloque2_herencia/Ej05Composicion` |
| 6 | Pila enlazada con excepciones propias | CE2.7, CE2.9 | `uf2404/bloque3_estructuras/Ej06Pila` |
| 7 | Cola de atencion al cliente | CE2.7, CE2.9 | `uf2404/bloque3_estructuras/Ej07Cola` |
| 8 | Arbol binario de busqueda | CE2.7, CE2.9 | `uf2404/bloque3_estructuras/Ej08ArbolBinario` |
| 9 | Analisis de texto con colecciones estandar | CE2.7, CE2.8 | `uf2404/bloque3_estructuras/Ej09AnalisisTexto` |
| 10 | Jerarquia de excepciones propias | CE2.10 | `uf2404/bloque4_excepciones/Ej10Excepciones` |
| 11 | Concurrencia, la condicion de carrera | CE2.10 | `uf2404/bloque4_excepciones/Ej11Concurrencia` |
| 12 | Comunicacion por sockets, servidor de consultas | CE2.10 | `uf2404/bloque4_excepciones/Ej12Sockets` |
| 13 | Sistema de gestion de un videoclub | CE1.8, CE2.3, CE2.9, CE2.10 | `uf2404/bloque5_integrador/Ej13Videoclub` |
| 14 | Refactorizacion guiada por criterios de calidad | CE2.1, CE2.10 | `uf2404/bloque5_integrador/Ej14Refactorizacion` |
| A1 | Ciclo de vida del objeto y gestion de memoria | CE2.2, CE1.2 | `ampliacion/uf2404/A01CicloVidaObjeto` |
| A2 | Estructuras de datos genericas | CE2.7, CE2.9, CE2.8 | `ampliacion/uf2404/A02EstructurasGenericas` |
| A3 | Persistencia en ficheros | CE2.8, CE2.10, CE2.3 | `ampliacion/uf2404/A03PersistenciaFicheros` |
| A4 | Analisis de un programa ajeno | CE2.5, CE2.6, CE1.8 | `ampliacion/uf2404/A04AnalisisDeCodigo` |
| A13 | El ciclo de desarrollo orientado a objetos | CE1.1, CE1.8 | `ampliacion/uf2404/A13CicloDesarrollo` |
| A14 | El paso de mensajes | CE1.4, CE2.4 | `ampliacion/uf2404/A14PasoDeMensajes` |

### B. Con qué ejercicio se demuestra cada criterio

| CE | Enunciado del criterio (RD 628/2013) | Ejercicios |
|---|---|---|
| **C1** | **Dominar los conceptos fundamentales del paradigma orientado a objetos.** | |
| CE1.1 | Explicar las características del ciclo de desarrollo del software bajo el paradigma de orientación a objetos, distinguiendo la programación orientada a objetos como una fase dentro del mismo. | **A13** |
| CE1.2 | Describir y enumerar las características de una clase: atributos, métodos y mecanismo de encapsulación, identificando la interfaz de la clase y lo que ésta representa. | 1, 2, 4, **A1** |
| CE1.3 | Describir y enumerar las características que definen un objeto, distinguiendo las diferencias entre los conceptos de objeto y clase. | 1 |
| CE1.4 | Describir la estructura y el significado de los mensajes y su relación con el comportamiento de los objetos. | **A14** |
| CE1.5 | Explicar las características fundamentales que tienen que estar presentes en una relación entre dos clases para que pueda ser calificada como relación de herencia. | 3 |
| CE1.6 | Describir el mecanismo de herencia múltiple y los problemas que presenta en el proceso de desarrollo de software. | 4 |
| CE1.7 | Explicar el concepto de polimorfismo y enumerar y describir las características que introduce en el proceso de desarrollo del software. | 3 |
| CE1.8 | En un supuesto práctico, a partir de una documentación típica de diseño detallado, identificar las clases establecidas, los atributos y las relaciones. | 5, 13, **A4**, **A13** |
| **C2** | **Desarrollar clases aplicando los fundamentos del paradigma Orientado a Objetos.** | |
| CE2.1 | Enumerar y describir los principales criterios de calidad del software y los principales factores evaluados por las métricas orientadas a objetos. | 14 |
| CE2.2 | Enumerar y describir los mecanismos de gestión de memoria utilizados en la creación y destrucción de los objetos. | **A1** |
| CE2.3 | Describir los mecanismos existentes para realizar la implementación de las relaciones entre clases (clases contenedores, objetos colección, etc). | 5, 13, **A3** |
| CE2.4 | Explicar la utilización de los objetos «super» y «this» («current», «self» u otros), en relación con el acceso a los atributos definidos en una clase, desde una subclase o desde el código de la propia clase. | 2, 3, **A14** |
| CE2.5 | Clasificar los diferentes lenguajes de programación, identificando y reconociendo en los mismos las principales características del paradigma orientado a objetos (clases, objetos, herencia y polimorfismo). | **A4** |
| CE2.6 | Distinguir y utilizar las características proporcionadas por un entorno de desarrollo asociado a un lenguaje Orientado a Objetos. | **A4** |
| CE2.7 | Distinguir las estructuras de datos más habituales (listas, pilas, árboles, grafos, etc) y los posibles mecanismos de construcción en los lenguajes orientados a objetos. | 6, 7, 8, 9, **A2** |
| CE2.8 | Distinguir las librerías de clases estándares del lenguaje de programación conociendo la utilidad de cada una de ellas y la forma básica de uso. | 9, **A2**, **A3** |
| CE2.9 | En un supuesto práctico, construir las clases que representan las estructuras de datos en un lenguaje orientado a objetos. | 6, 7, 8, 13, **A2** |
| CE2.10 | En un supuesto práctico, en el que se pide realizar la programación de una clase con un lenguaje orientado a objetos y desde una documentación a nivel de diseño detallado: [diseñar un algoritmo por operación; elegir la estructura de datos de cada atributo; codificar atributos, métodos de acceso, constructores con sobrecarga y métodos; incluir las relaciones de especialización, agregación, composición o asociación; incluir el tratamiento de errores y excepciones; usar las librerías existentes para accesos a bases de datos, interfaces gráficas y otras]. | 1, 2, 4, 10, 11, 12, 13, 14, **A3** |

Los códigos en **negrita** son ejercicios de ampliación.


## UF2405 — Modelo de programación web y bases de datos relacionales

### A. Qué criterios acredita cada ejercicio

| Ej. | Título | Criterios | Fichero |
|---|---|---|---|
| 1 | Modelo relacional de una clinica veterinaria | CE2.5 | `uf2405/bloque1_ddl/Ej01ModeloRelacional` |
| 2 | Evolucion del esquema, ALTER, vistas y disparadores | CE2.6 | `uf2405/bloque1_ddl/Ej02EvolucionEsquema` |
| 3 | Consultas con combinacion y agregacion | CE2.6 | `uf2405/bloque2_dml/Ej03Consultas` |
| 4 | Actualizaciones y borrados controlados | CE2.6 | `uf2405/bloque2_dml/Ej04ActualizacionesBorrados` |
| 5 | Conexion, consulta y gestion de errores | CE2.1, CE2.4 | `uf2405/bloque3_jdbc/Ej05Conexion` |
| 6 | Inyeccion SQL, demostracion y correccion | CE2.3 | `uf2405/bloque3_jdbc/Ej06InyeccionSql` |
| 7 | Patron DAO completo | CE2.5 | `uf2405/bloque3_jdbc/Ej07Dao` |
| 8 | Transacciones, alta de consulta con tratamientos | CE2.4 | `uf2405/bloque3_jdbc/Ej08Transacciones` |
| 9 | Formulario HTML con validacion en cliente | CE1.5, CE1.6 | `uf2405/bloque4_web/Ej09FormularioHtml` |
| 10 | Servlet de listado con acceso a base de datos | CE1.8 | `uf2405/bloque4_web/Ej10ServletListado` |
| 11 | Procesamiento de formulario con Servlet | CE1.8 | `uf2405/bloque4_web/Ej11ServletFormulario` |
| 12 | Sesiones, carrito de tratamientos | CE1.7 | `uf2405/bloque4_web/Ej12Sesiones` |
| 13 | De Servlet a JSP, separando la presentacion | CE1.4, CE1.8 | `uf2405/bloque4_web/Ej13ServletAJsp` |
| 14 | Aplicacion web completa de gestion de la clinica | CE1.1, CE1.4, CE1.8, CE2.5, CE2.6 | `uf2405/bloque5_integrador/Ej14AplicacionWeb` |
| A5 | Protocolos y formatos de intercambio | CE1.2, CE1.1 | `ampliacion/uf2405/A05ProtocolosFormatos` |
| A6 | Tecnologias de acceso a datos | CE2.2, CE2.1, CE1.3 | `ampliacion/uf2405/A06TecnologiasAccesoDatos` |

### B. Con qué ejercicio se demuestra cada criterio

| CE | Enunciado del criterio (RD 628/2013) | Ejercicios |
|---|---|---|
| **C1** | **Aplicar los conceptos básicos del modelo de programación web.** | |
| CE1.1 | Enumerar y describir los componentes del modelo multicapa de programación web (cliente ligero, servidores web, servidores de aplicaciones, servidores de base de datos). | 14, **A5** |
| CE1.2 | Enumerar y describir la función de los protocolos y tecnologías habituales (TCP/IP, http, HTML, XML, XSL, SOAP). | **A5** |
| CE1.3 | Enumerar las características básicas de los modelos de programación ampliamente utilizados (J2EE y .NET). | **A6** |
| CE1.4 | Describir las capas lógicas de una aplicación web (Presentación, Aplicación y Datos). | 13, 14 |
| CE1.5 | Describir las características básicas del lenguaje de presentación (HTML). | 9 |
| CE1.6 | Describir las características básicas de los lenguajes de scripting en cliente (JavaScript, VBScript). | 9 |
| CE1.7 | Describir el funcionamiento de una sesión de aplicación en el modelo de programación web. | 12 |
| CE1.8 | Aplicar las características básicas de los lenguajes orientados a objetos a la recepción de solicitudes y preparación de la capa de presentación (JSP, ASP, Servlets, PHP). | 10, 11, 13, 14 |
| **C2** | **Realizar conexiones con bases de datos relacionales.** | |
| CE2.1 | Enumerar y describir las diferentes tecnologías de conexión con la BBDD desde las aplicaciones. | 5, **A6** |
| CE2.2 | Analizar las diferentes tecnologías de conexión y acceso a datos, determinando las que se deben utilizar para la manipulación del sistema de base de datos. | **A6** |
| CE2.3 | Enumerar y describir las clases que proporcionan los medios adecuados para efectuar consultas, actualizaciones, acceder y operar con una base de datos relacional. | 6 |
| CE2.4 | Describir los procedimientos para realizar dichas consultas (abrir y cerrar conexiones, ejecutar comandos, recoger sus resultados y utilizarlos). | 5, 8 |
| CE2.5 | En supuestos prácticos debidamente caracterizados de incorporación de un acceso a una base de datos relacional desde una clase, a partir de un diseño: [seleccionar la tecnología de conexión; cargar el controlador; realizar la conexión; desarrollar la clase según la especificación realizando consultas simples; comprobar su funcionamiento mediante pruebas; documentar la clase]. | 1, 7, 14 |
| CE2.6 | En supuestos prácticos debidamente caracterizados de modificación de una aplicación con acceso a una base de datos relacional desde una clase, a partir de un cambio en el diseño de la aplicación o de la base de datos: [modificar el código; incluir el histórico y la explicación en la cabecera; modificar los programas de prueba; comprobar mediante pruebas que el cambio no altera el resto; actualizar la documentación afectada]. | 2, 3, 4, 14 |

Los códigos en **negrita** son ejercicios de ampliación.


## UF2406 — Desarrollo de clases: pruebas, documentación e interfaces

### A. Qué criterios acredita cada ejercicio

| Ej. | Título | Criterios | Fichero |
|---|---|---|---|
| 1 | Especificacion de requisitos de un gimnasio | CE1.1, CE1.2 | `uf2406/bloque1_requisitos/Ej01Requisitos` |
| 2 | Del enunciado a las clases, tarjetas CRC | CE1.3 | `uf2406/bloque1_requisitos/Ej02TarjetasCrc` |
| 3 | Diagrama de casos de uso del gimnasio | CE5.1 | `uf2406/bloque2_uml/Ej03CasosDeUso` |
| 4 | Diagrama de clases con notacion completa | CE1.3 | `uf2406/bloque2_uml/Ej04DiagramaClases` |
| 5 | Diagramas de secuencia y de estados | CE1.3 | `uf2406/bloque2_uml/Ej05SecuenciaEstados` |
| 6 | Estimacion, Gantt y camino critico | CE1.2 | `uf2406/bloque3_planificacion/Ej06GanttCaminoCritico` |
| 7 | Diseno de casos de prueba de caja negra | CE2.2, CE2.5 | `uf2406/bloque4_pruebas/Ej07CajaNegra` |
| 8 | Pruebas estructurales y complejidad ciclomatica | CE2.3 | `uf2406/bloque4_pruebas/Ej08CajaBlanca` |
| 9 | Bateria de pruebas automatizadas con JUnit | CE2.6, CE2.7 | `uf2406/bloque4_pruebas/Ej09Junit` |
| 10 | Pruebas con dependencias simuladas (mocks) | CE2.7 | `uf2406/bloque4_pruebas/Ej10Mocks` |
| 11 | Documentacion tecnica con Javadoc | CE3.1, CE3.2, CE3.6, CE3.10 | `uf2406/bloque5_documentacion/Ej11Javadoc` |
| 12 | Gestion de un cambio de especificacion | CE4.1, CE4.3, CE4.5 | `uf2406/bloque5_documentacion/Ej12CambioEspecificacion` |
| 13 | Sesion de depuracion guiada | CE2.1, CE2.4 | `uf2406/bloque5_documentacion/Ej13Depuracion` |
| 14 | Interfaz grafica con criterios de usabilidad | CE5.2, CE5.3, CE5.5 | `uf2406/bloque5_documentacion/Ej14InterfazUsabilidad` |
| 15 | Proyecto integrador, cierre del ciclo de vida | CE1.4, CE1.5, CE2.6 | `uf2406/bloque5_documentacion/Ej15ProyectoIntegrador` |
| A7 | Plantilla y documento de diseno de una clase | CE3.3, CE3.7 | `ampliacion/uf2406/A07DocumentoDiseno` |
| A8 | Plantilla e informe de pruebas | CE3.4, CE3.8 | `ampliacion/uf2406/A08InformePruebas` |
| A9 | Manual de operacion y mantenimiento | CE3.5, CE3.9 | `ampliacion/uf2406/A09ManualOperacion` |
| A10 | Gestion de la configuracion del software | CE4.2, CE4.4, CE4.6 | `ampliacion/uf2406/A10GestionConfiguracion` |
| A11 | De la documentacion de diseno a la interfaz | CE5.4, CE5.6, CE5.7 | `ampliacion/uf2406/A11InterfazDesdeDiseno` |
| A12 | Validacion y accesibilidad de la interfaz | CE5.5, CE5.8 | `ampliacion/uf2406/A12ValidacionAccesibilidad` |

### B. Con qué ejercicio se demuestra cada criterio

| CE | Enunciado del criterio (RD 628/2013) | Ejercicios |
|---|---|---|
| **C1** | **Manejar las herramientas de ingeniería de software.** | |
| CE1.1 | Enumerar y comparar los modelos de ingeniería software, indicando los conceptos principales en los que se basan, su ámbito de uso y cómo se estructuran. | 1 |
| CE1.2 | Describir las fases, en cada modelo, del proceso de ingeniería de software, indicando para cada una: [datos de partida (entradas); datos finales (salidas); funciones realizadas en la fase; documentación generada; trazabilidad]. | 1, 6 |
| CE1.3 | Describir en detalle los conceptos fundamentales de una metodología de ingeniería software basada en la orientación a objetos. | 2, 4, 5 |
| CE1.4 | Identificar en las herramientas de desarrollo orientadas a objetos disponibles los diferentes componentes y describir cómo se implementan los conceptos de la metodología y las distintas fases del proceso de ingeniería de software, haciendo especial hincapié en: diseño, codificación, pruebas unitarias, documentación, evaluación de la calidad y métricas, gestión de la configuración y cambios. | 15 |
| CE1.5 | En un supuesto práctico utilizar las herramientas de desarrollo, en el caso de que sea posible, para: [extraer la información de diseño de una clase; codificarla; depurarla; incorporar y trazar pruebas unitarias; configurar baterías automáticas; elaborar y personalizar documentación mediante plantillas; generar informes de calidad y métricas; incorporar cambios]. | 15 |
| **C2** | **Verificar la corrección de las clases desarrolladas mediante la realización de pruebas.** | |
| CE2.1 | Enumerar y describir las herramientas y utilidades más comunes para la depuración de programas. | 13 |
| CE2.2 | Enumerar y describir los tipos de pruebas posibles que se pueden dar en el proceso de desarrollo de aplicaciones, distinguiendo especialmente aquellas que son responsabilidad del programador. | 7 |
| CE2.3 | Enumerar y describir los tipos de pruebas que se deben realizar a una clase para verificar su corrección. | 8 |
| CE2.4 | Utilizar las características proporcionadas por un entorno de desarrollo para realizar la depuración de un programa, mediante: [ejecución paso a paso; establecimiento de puntos de parada (condicionales o incondicionales); monitorización de variables]. | 13 |
| CE2.5 | En supuestos prácticos, documentar una estrategia de pruebas completa a una clase a partir de su documentación de diseño, de forma que se asegure el óptimo funcionamiento en aspectos como: [coherencia en el estado de los objetos; todos los escenarios posibles; rendimiento; casos límite; situaciones excepcionales]. | 7 |
| CE2.6 | En supuestos prácticos, a partir de una clase y la estrategia definida de pruebas: [establecer el conjunto de secuencias y estados iniciales de los objetos; establecer el criterio de evaluación de los resultados; automatizar el proceso mediante programas de prueba]. | 9, 15 |
| CE2.7 | Realizar pruebas a una clase mediante herramientas de prueba. | 9, 10 |
| **C3** | **Elaborar la documentación completa relativa a las clases desarrolladas y pruebas realizadas.** | |
| CE3.1 | Describir la información que debe acompañar a una clase desarrollada. | 11 |
| CE3.2 | Describir los criterios fundamentales para la inclusión de cabeceras y comentarios en el código. | 11 |
| CE3.3 | Proponer índices (plantillas) para los documentos de diseño y explicar el contenido de cada uno de los apartados. | **A7** |
| CE3.4 | Proponer índices (plantillas) para los documentos de pruebas (planificación y resultados) y explicar el contenido de cada uno de los apartados. | **A8** |
| CE3.5 | Proponer índices (plantillas) para la documentación de operación y mantenimiento (manuales técnicos) y explicar el contenido de cada uno de los apartados. | **A9** |
| CE3.6 | Aplicar unos criterios de normalización establecidos para incluir cabeceras y comentarios en el código. | 11 |
| CE3.7 | En un supuesto práctico, a partir de una clase desarrollada, elaborar la documentación de diseño de la clase de acuerdo a un índice establecido. | **A7** |
| CE3.8 | En un supuesto práctico, a partir de una clase y conjunto de pruebas, elaborar la documentación de pruebas (planificación y resultados) de acuerdo a un índice establecido. | **A8** |
| CE3.9 | En un supuesto práctico, a partir de la documentación de desarrollo y pruebas, elaborar la documentación de operación y mantenimiento de acuerdo a un índice establecido. | **A9** |
| CE3.10 | Realizar la documentación de las clases mediante herramientas de documentación automática. | 11 |
| **C4** | **Realizar modificaciones de clases existentes por cambios en las especificaciones.** | |
| CE4.1 | Enumerar y describir los conceptos fundamentales de la gestión de la configuración del software desarrollado por una organización. | 12 |
| CE4.2 | Enumerar y describir los conceptos fundamentales de la gestión de la configuración de la documentación. | **A10** |
| CE4.3 | Describir los pasos y precauciones fundamentales en el proceso de modificación de clases existentes. | 12 |
| CE4.4 | En un supuesto práctico de modificación de un documento, y de acuerdo con un procedimiento: [realizar la modificación en el documento; marcar la hoja cambiada con la modificación y el código de revisión; elaborar la propuesta de cambio según el formato especificado; editar la nueva edición o revisión del documento]. | **A10** |
| CE4.5 | En un supuesto práctico de modificación de una clase por un cambio en su diseño y de acuerdo a un procedimiento: [modificar el código incorporando el cambio; incluir en el lugar de la modificación y en la cabecera del componente los datos del histórico y la explicación; modificar los programas de prueba asociados; comprobar mediante pruebas que la modificación no ha alterado el resto de la clase; actualizar toda la documentación afectada]. | 12 |
| CE4.6 | Utilizar herramientas para la gestión de la configuración y las versiones del software. | **A10** |
| **C5** | **Desarrollar interfaces de usuario en lenguajes de programación orientados a objeto, a partir del diseño detallado.** | |
| CE5.1 | Interpretar diagramas de casos de uso y analizar las necesidades y peticiones de usuarios. | 3 |
| CE5.2 | Identificar y describir las clases básicas que se usan para el interfaz hombre / máquina. | 14 |
| CE5.3 | Explicar el concepto de evento y de programación orientada a eventos y su implementación en los lenguajes orientados a objetos en relación con las clases necesarias para el desarrollo de la interfaz. | 14 |
| CE5.4 | Identificar los recursos multimedia que pueden incluirse en una ventana de interfaz de usuario. | **A11** |
| CE5.5 | Realizar el diseño de las ventanas correspondientes a la interfaz de usuario, aplicando criterios de ergonomía, eficacia y posible utilización por discapacitados, en la comunicación de información. | 14, **A12** |
| CE5.6 | En un supuesto práctico de construcción de un interfaz de usuario: [identificar los elementos básicos que constituyen la interfaz en la documentación de diseño; asociar a cada elemento la clase adecuada de la librería disponible; construir la interfaz con una herramienta de diseño de interfaz gráfica; identificar y describir el código fuente generado por la herramienta]. | **A11** |
| CE5.7 | En un supuesto práctico de construcción de una aplicación con interfaz de usuario, a partir del código generado por una herramienta de interfaz gráfica: [incluir el código necesario para incorporar otros recursos multimedia; programar las clases necesarias para conectar la interfaz con la aplicación, siguiendo los criterios de calidad establecidos]. | **A11** |
| CE5.8 | En un supuesto práctico, sobre una interfaz de usuario desarrollada y con la documentación correspondiente al diseño detallado: [evaluar que los servicios de presentación cumplen las necesidades del usuario y usan de forma óptima los recursos; establecer criterios de validación; describir tipos de errores; comprobar que los formatos de entrada y salida son los esperados; verificar que las operaciones indebidas no alteran la fiabilidad; elaborar la documentación de la interfaz; redactar la guía de usuario]. | **A12** |

Los códigos en **negrita** son ejercicios de ampliación.


## Cobertura completa: notas sobre los últimos criterios

### Los 68 criterios tienen ejercicio

Los ejercicios A13 y A14 cierran los dos últimos criterios de la UF2404, y la reetiquetación del ejercicio 12 cierra el de la UF2406. No queda ningún criterio sin ejercicio.

### UF2404 · CE1.1 — el ciclo de desarrollo (ejercicio A13)

Es un criterio de «explicar»: pide situar la programación orientada a objetos como una fase dentro del ciclo completo. El ejercicio A13 lo convierte en algo comprobable: modela las nueve fases del ciclo con sus entradas, sus salidas y el artefacto de este mismo proyecto que las representa, verifica en disco que los nueve artefactos existen, y comprueba automáticamente la trazabilidad de la cadena —que todo lo que entra en una fase ha salido de alguna anterior—. La POO queda situada como la fase 6 de 9: cinco fases antes y tres después.

### UF2404 · CE1.4 — el paso de mensajes (ejercicio A14)

Pide describir la estructura de un mensaje (receptor, selector, argumentos) y su relación con el comportamiento. El ejercicio A14 lo demuestra en ejecución: la misma jerarquía respondiendo de tres formas distintas al mismo envío, la sobrecarga resuelta por el tipo declarado frente a la redefinición resuelta por el tipo real, la reflexión mostrando en qué clase acaba resolviéndose cada mensaje, el error al enviar un mensaje sin receptor y la demostración de que <b>super</b> cambia dónde se busca, no a quién se pregunta.

### UF2406 · CE4.5 — modificación de una clase por un cambio de diseño

Este criterio no necesitaba ejercicio nuevo: lo cumple punto por punto el ejercicio 12 del cuaderno de la UF2406, cuyos apartados 4, 5 y 6 recorren los cinco puntos del criterio (modificar el código, dejar el histórico en la cabecera, ajustar los programas de prueba, ejecutar la regresión y actualizar la documentación afectada). El fichero <i>.java</i> ya lo declara junto a CE4.1 y CE4.3. <b>Conviene reflejar el cambio también en el cuaderno original en .docx</b>, que aún lo atribuye solo a esos dos.


---

Nota sobre la fuente. El texto de los criterios está transcrito del PDF oficial del Real Decreto 628/2013 (BOE núm. 225, de 19 de septiembre de 2013), anexo IV. Ese PDF incrusta parte del articulado con una fuente sin tabla de correspondencia Unicode, de modo que treinta de los sesenta y ocho criterios se extraen como caracteres de control. Se han recuperado reconstruyendo la codificación glifo a glifo y contrastando el resultado con los párrafos legibles del mismo texto; los treinta son CE1.2, CE1.3, CE1.4, CE1.5, CE1.7, CE1.8, CE2.4, CE2.5 y CE2.10 de la UF2404; CE2.5 y CE2.6 de la UF2405; y CE1.2, CE1.4, CE1.5, CE2.3, CE2.6, CE3.4, CE3.8, CE4.1 a CE4.6, CE5.2 y CE5.4 a CE5.8 de la UF2406. Los treinta y ocho restantes se han comparado carácter a carácter con el BOE y coinciden. Los apartados con guion del original se resumen aquí entre corchetes. Antes de usar este documento como justificación formal conviene cotejar con el BOE los treinta reconstruidos.
