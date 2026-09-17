#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Genera el cuaderno de ejercicios de ampliacion del MF0227_3 en PDF."""

import os
import sys

from reportlab.lib import colors
from reportlab.lib.enums import TA_JUSTIFY
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import ParagraphStyle, getSampleStyleSheet
from reportlab.lib.units import cm
from reportlab.platypus import (BaseDocTemplate, Frame, KeepTogether, PageBreak,
                                PageTemplate, Paragraph, Spacer, Table, TableStyle)

AZUL = colors.HexColor('#183E6E')
GRIS = colors.HexColor('#5A6472')
GRIS_CLARO = colors.HexColor('#EEF1F5')

ss = getSampleStyleSheet()

E = {
    'portada_titulo': ParagraphStyle('pt', parent=ss['Title'], fontName='Helvetica-Bold',
                                     fontSize=26, leading=31, textColor=AZUL, spaceAfter=4),
    'portada_sub': ParagraphStyle('ps', parent=ss['Normal'], fontName='Helvetica',
                                  fontSize=14, leading=19, textColor=GRIS,
                                  alignment=1, spaceAfter=2),
    'portada_pie': ParagraphStyle('pp', parent=ss['Normal'], fontName='Helvetica',
                                  fontSize=10.5, leading=15, textColor=GRIS, alignment=1),
    'uf': ParagraphStyle('uf', parent=ss['Normal'], fontName='Helvetica-Bold',
                         fontSize=15, leading=19, textColor=colors.white,
                         spaceBefore=0, spaceAfter=0, leftIndent=6, keepWithNext=1),
    'ej': ParagraphStyle('ej', parent=ss['Normal'], fontName='Helvetica-Bold',
                         fontSize=12.5, leading=16, textColor=AZUL,
                         spaceBefore=14, spaceAfter=2, keepWithNext=1),
    'ce': ParagraphStyle('ce', parent=ss['Normal'], fontName='Helvetica-Oblique',
                         fontSize=9.5, leading=13, textColor=GRIS, spaceAfter=7,
                         keepWithNext=1),
    'cuerpo': ParagraphStyle('cu', parent=ss['Normal'], fontName='Helvetica',
                             fontSize=10, leading=14.5, alignment=TA_JUSTIFY,
                             spaceAfter=7),
    'sepide': ParagraphStyle('sp', parent=ss['Normal'], fontName='Helvetica-Bold',
                             fontSize=10, leading=14, spaceBefore=3, spaceAfter=4,
                             keepWithNext=1),
    'item': ParagraphStyle('it', parent=ss['Normal'], fontName='Helvetica',
                           fontSize=10, leading=14, alignment=TA_JUSTIFY,
                           leftIndent=20, firstLineIndent=-13, spaceAfter=3),
    'pista': ParagraphStyle('pi', parent=ss['Normal'], fontName='Helvetica',
                            fontSize=9.5, leading=13.5, alignment=TA_JUSTIFY,
                            textColor=colors.HexColor('#2A2F36'),
                            leftIndent=8, rightIndent=8,
                            spaceBefore=6, spaceAfter=2),
    'nota': ParagraphStyle('no', parent=ss['Normal'], fontName='Helvetica-Oblique',
                           fontSize=9, leading=13, textColor=GRIS,
                           alignment=TA_JUSTIFY, spaceBefore=4),
    'h2': ParagraphStyle('h2', parent=ss['Normal'], fontName='Helvetica-Bold',
                         fontSize=12, leading=16, textColor=AZUL,
                         spaceBefore=12, spaceAfter=5),
}



def cobertura_real():
    """Lee el proyecto y devuelve (filas, antes, despues, total, sin_cubrir).

    Los numeros del cuadro de cierre no se escriben a mano: salen de las
    cabeceras Javadoc de los 57 ejercicios, igual que la matriz de criterios.
    """
    aqui = os.path.dirname(os.path.abspath(__file__))
    sys.path.insert(0, os.path.normpath(os.path.join(aqui, '..')))
    from ce_catalogo import CE          # noqa: E402
    from matriz import leer, cobertura  # noqa: E402
    ejs = leer()
    m = cobertura(ejs)
    filas, antes, despues, total, sin_cubrir = [], 0, 0, 0, []
    for uf in ('UF2404', 'UF2405', 'UF2406'):
        ces = [c for c in CE[uf] if c.startswith('CE')]
        a = len([c for c in ces if any(not e['amp'] for e in m[uf][c])])
        d = len([c for c in ces if m[uf][c]])
        sin_cubrir += ['%s %s' % (uf, c) for c in ces if not m[uf][c]]
        filas.append([uf, str(a), str(d), str(len(ces))])
        antes += a
        despues += d
        total += len(ces)
    filas.append(['TOTAL', str(antes), str(despues), str(total)])
    return filas, antes, despues, total, sin_cubrir


def barra_uf(texto):
    """Cabecera de unidad formativa, en banda azul."""
    t = Table([[Paragraph(texto, E['uf'])]], colWidths=[16.4 * cm])
    t.setStyle(TableStyle([
        ('BACKGROUND', (0, 0), (-1, -1), AZUL),
        ('TOPPADDING', (0, 0), (-1, -1), 7),
        ('BOTTOMPADDING', (0, 0), (-1, -1), 7),
    ]))
    return t


def caja_pista(texto):
    """Recuadro gris con la pista del ejercicio."""
    t = Table([[Paragraph('<b>Pista.</b> ' + texto, E['pista'])]], colWidths=[16.4 * cm])
    t.setStyle(TableStyle([
        ('BACKGROUND', (0, 0), (-1, -1), GRIS_CLARO),
        ('LEFTPADDING', (0, 0), (-1, -1), 8),
        ('RIGHTPADDING', (0, 0), (-1, -1), 8),
        ('TOPPADDING', (0, 0), (-1, -1), 6),
        ('BOTTOMPADDING', (0, 0), (-1, -1), 6),
        ('LINEBEFORE', (0, 0), (0, -1), 2.5, AZUL),
    ]))
    return t


def ejercicio(codigo, titulo, ces, intro, pide, pista, nota=None):
    """Construye el bloque completo de un ejercicio."""
    partes = [
        Paragraph('EJERCICIO %s. %s' % (codigo, titulo), E['ej']),
        Paragraph('Criterios de evaluación: %s' % ces, E['ce']),
    ]
    for p in intro:
        partes.append(Paragraph(p, E['cuerpo']))
    partes.append(Paragraph('Se pide:', E['sepide']))
    for i, item in enumerate(pide, 1):
        partes.append(Paragraph('%d.&nbsp;&nbsp;%s' % (i, item), E['item']))
    partes.append(caja_pista(pista))
    if nota:
        partes.append(Paragraph(nota, E['nota']))
    partes.append(Spacer(1, 0.45 * cm))
    # El texto fluye entre paginas, como en un cuaderno impreso. Lo que NO se
    # permite es que un titulo quede huerfano al final de una pagina: de eso se
    # encarga keepWithNext en los estilos.
    return partes


def pie(canv, doc):
    """Numero de pagina y linea de pie."""
    canv.saveState()
    if doc.page > 1:
        canv.setStrokeColor(colors.HexColor('#D6DBE2'))
        canv.setLineWidth(0.5)
        canv.line(2.3 * cm, 1.65 * cm, A4[0] - 2.3 * cm, 1.65 * cm)
        canv.setFont('Helvetica', 8.5)
        canv.setFillColor(GRIS)
        canv.drawString(2.3 * cm, 1.2 * cm,
                        'Ejercicios de ampliación · MF0227_3 · IFCD0112')
        canv.drawRightString(A4[0] - 2.3 * cm, 1.2 * cm, '%d' % doc.page)
    canv.restoreState()


def construir(ruta):
    doc = BaseDocTemplate(ruta, pagesize=A4,
                          leftMargin=2.3 * cm, rightMargin=2.3 * cm,
                          topMargin=2.2 * cm, bottomMargin=2.2 * cm,
                          title='Ejercicios de ampliación - MF0227_3',
                          author='IFCD0112 - MF0227_3',
                          subject='Cuaderno de ejercicios de ampliación')
    marco = Frame(doc.leftMargin, doc.bottomMargin, doc.width, doc.height, id='n')
    doc.addPageTemplates([PageTemplate(id='base', frames=[marco], onPage=pie)])

    S = []

    # ---------------- Portada ----------------
    S.append(Spacer(1, 4.5 * cm))
    S.append(Paragraph('EJERCICIOS DE AMPLIACIÓN', E['portada_titulo']))
    S.append(Spacer(1, 0.5 * cm))
    S.append(Paragraph('MF0227_3 · Programación Orientada a Objetos', E['portada_sub']))
    S.append(Paragraph('Certificado de Profesionalidad IFCD0112', E['portada_sub']))
    S.append(Spacer(1, 1.6 * cm))
    S.append(Paragraph(
        'Catorce ejercicios que completan los tres cuadernos del módulo. Cubren los criterios de '
        'evaluación del Real Decreto 628/2013 que no tenían ejercicio asociado, y añaden dos '
        'contenidos que el Real Decreto no exige pero que conviene dominar al terminar: la '
        'entrada y salida de ficheros y los tipos genéricos propios.',
        ParagraphStyle('intro', parent=E['cuerpo'], alignment=1,
                       leftIndent=1.2 * cm, rightIndent=1.2 * cm, fontSize=10.5, leading=15)))
    S.append(Spacer(1, 3.2 * cm))
    S.append(Paragraph(
        'Cada enunciado indica los criterios de evaluación que trabaja.<br/>'
        'La solución comentada y ejecutable de cada uno está en el paquete '
        '<b>ampliacion</b> del proyecto Java.', E['portada_pie']))
    S.append(PageBreak())

    # ---------------- Indice ----------------
    S.append(Paragraph('ÍNDICE', E['h2']))
    datos = [['Unidad formativa', 'Ejercicios', 'Nº']]
    for fila in [
        ['UF2404 — Principios de la programación orientada a objetos', '6', 'A1 – A4, A13 – A14'],
        ['UF2405 — Modelo de programación web y bases de datos', '2', 'A5 – A6'],
        ['UF2406 — El ciclo de vida del desarrollo de aplicaciones', '6', 'A7 – A12'],
    ]:
        datos.append(fila)
    t = Table(datos, colWidths=[10.6 * cm, 2.2 * cm, 3.6 * cm])
    t.setStyle(TableStyle([
        ('BACKGROUND', (0, 0), (-1, 0), AZUL),
        ('TEXTCOLOR', (0, 0), (-1, 0), colors.white),
        ('FONTNAME', (0, 0), (-1, 0), 'Helvetica-Bold'),
        ('FONTNAME', (0, 1), (-1, -1), 'Helvetica'),
        ('FONTSIZE', (0, 0), (-1, -1), 9.5),
        ('ALIGN', (1, 0), (-1, -1), 'CENTER'),
        ('VALIGN', (0, 0), (-1, -1), 'MIDDLE'),
        ('TOPPADDING', (0, 0), (-1, -1), 6),
        ('BOTTOMPADDING', (0, 0), (-1, -1), 6),
        ('GRID', (0, 0), (-1, -1), 0.5, colors.HexColor('#C9D0D9')),
        ('ROWBACKGROUNDS', (0, 1), (-1, -1), [colors.white, GRIS_CLARO]),
    ]))
    S.append(t)
    S.append(Spacer(1, 0.7 * cm))
    S.append(Paragraph(
        'Criterios de corrección sugeridos: la solución debe compilar y ejecutarse sin errores '
        '(30%), cumplir todos los requisitos funcionales del enunciado (40%), aplicar '
        'correctamente los conceptos trabajados (20%) y estar documentada y con nombres de '
        'identificadores descriptivos (10%).', E['cuerpo']))
    S.append(Paragraph(
        'Los ejercicios A1 a A4 pueden plantearse en cualquier momento después del bloque 3 de '
        'la UF2404. Los A5 y A6 requieren haber completado el bloque 3 de la UF2405. Los A7 a '
        'A12 se apoyan en los ejercicios 9, 12 y 14 de la UF2406, así que van al final. Los A13 '
        'y A14 son de concepto y no necesitan nada previo: el A14 encaja bien nada más terminar '
        'la herencia, y el A13 conviene dejarlo para cuando el alumnado haya visto ya el '
        'cuaderno de la UF2406 completo, porque recorre sus artefactos.',
        E['cuerpo']))
    S.append(PageBreak())

    # ================= UF2404 =================
    S.append(barra_uf('UF2404 — PRINCIPIOS DE LA PROGRAMACIÓN ORIENTADA A OBJETOS'))

    S += ejercicio(
        'A1', 'Ciclo de vida del objeto y gestión de memoria', 'CE2.2, CE1.2',
        ['En Java nadie destruye los objetos a mano: de eso se encarga el recolector de basura. '
         'Este ejercicio pretende que compruebes cuándo deja de existir realmente un objeto, y '
         'por qué un constructor tiene pareja pero un destructor no.'],
        ['Escribe una clase <b>Documento</b> con un contador estático de instancias creadas, de '
         'modo que en cualquier momento se pueda saber cuántos objetos se han construido.',
         'Crea tres documentos dentro de un método y comprueba el contador al entrar y al salir '
         'del método. Explica por qué el contador <b>no</b> baja.',
         'Guarda una referencia débil (<font face="Courier">WeakReference</font>) a uno de los '
         'documentos, elimina después todas las referencias normales que apunten a él, solicita '
         'la recolección y comprueba si la referencia débil se ha quedado vacía. Explica qué '
         'demuestra ese resultado.',
         'Repite la prueba anterior pero guardando además el documento en una lista estática de '
         'la clase. Explica por qué ahora no se recolecta y qué nombre recibe ese problema.',
         'Implementa una clase <b>Conexion</b> que represente un recurso externo, de forma que '
         'se libere de manera <b>determinista</b>. Explica en un comentario por qué '
         '<font face="Courier">finalize()</font> no sirve para eso y qué se usa en su lugar.',
         'Explica en un comentario la diferencia entre la memoria de la pila y la del montículo, '
         'e indica dónde vive el objeto y dónde la variable que lo referencia.'],
        'Solicitar la recolección no es lo mismo que provocarla. '
        '<font face="Courier">System.gc()</font> es una sugerencia que la máquina virtual puede '
        'ignorar, así que la prueba del apartado 3 hay que plantearla admitiendo que a veces no '
        'concluya, y decirlo, en lugar de dar por hecho que el objeto ha muerto.')

    S += ejercicio(
        'A2', 'Estructuras de datos genéricas', 'CE2.7, CE2.9, CE2.8',
        ['La pila y la cola que implementaste en el bloque 3 de la UF2404 solo sirven para un '
         'tipo de dato. Si necesitas una pila de cadenas y otra de enteros, hay que escribir la '
         'clase dos veces. Este ejercicio te pide generalizarlas.'],
        ['Reescribe la pila como una clase genérica <b>Pila&lt;T&gt;</b> que funcione con '
         'cualquier tipo, conservando las operaciones apilar, desapilar, cima, estaVacia y tamano.',
         'Haz lo mismo con la cola: <b>Cola&lt;T&gt;</b>, con encolar, desencolar, primero, '
         'estaVacia y tamano.',
         'Comprueba las dos con al menos tres tipos distintos, incluido uno propio, y explica qué '
         'ventaja concreta aporta frente a haber usado <font face="Courier">Object</font> en '
         'lugar de <font face="Courier">T</font>.',
         'Implementa una <b>ColaConPrioridad&lt;T&gt;</b> que solo admita tipos ordenables, de '
         'modo que el compilador rechace un tipo que no lo sea. Investiga cómo se declara esa '
         'restricción.',
         'Intenta crear dentro de la pila un array del tipo genérico '
         '(<font face="Courier">new T[10]</font>). Explica por qué el compilador no te deja y '
         'cómo se resuelve en la práctica.',
         'Añade a la pila un método <b>apilarTodos</b> que acepte cualquier colección cuyos '
         'elementos sean del tipo de la pila o de un subtipo suyo, y explica qué significa la '
         'notación que has tenido que emplear.',
         'Explica en un comentario qué es el borrado de tipos y por qué, en tiempo de ejecución, '
         'una <font face="Courier">Pila&lt;String&gt;</font> y una '
         '<font face="Courier">Pila&lt;Integer&gt;</font> son la misma clase.'],
        'Los genéricos no existen en tiempo de ejecución. Todo lo que hacen ocurre durante la '
        'compilación, y esa es a la vez su mayor virtud —los errores aparecen antes de ejecutar— '
        'y la explicación de casi todas sus limitaciones.')

    S += ejercicio(
        'A3', 'Persistencia en ficheros', 'CE2.8, CE2.10, CE2.3',
        ['Todo lo que has programado hasta ahora desaparece al cerrar el programa. Antes de '
         'guardar datos en una base de datos (UF2405), conviene saber guardarlos en un fichero, '
         'que es la forma más sencilla de persistencia y la que se usa para configuraciones, '
         'registros e intercambio de datos.',
         'El videoclub del ejercicio 13 de la UF2404 debe conservar su catálogo entre '
         'ejecuciones.'],
        ['Implementa un método que guarde una lista de artículos en un fichero de texto con '
         'formato CSV, una línea por artículo, empleando las clases de la biblioteca estándar y '
         'garantizando que el fichero se cierra pase lo que pase.',
         'Implementa el método inverso, que lea ese fichero y reconstruya la lista de objetos.',
         'Comprueba el ciclo completo: guardar, leer y verificar que lo leído coincide '
         'exactamente con lo guardado.',
         'Fija explícitamente la codificación de caracteres al escribir y al leer, y explica en '
         'un comentario qué ocurre si no se hace.',
         'Gestiona los errores que pueden darse: que el fichero no exista, que una línea esté mal '
         'formada y que no haya permisos. Decide en cada caso si conviene abortar o continuar, y '
         'justifícalo.',
         'Añade un método que registre una línea en un fichero de bitácora sin borrar lo anterior, '
         'e indica qué opción de apertura lo permite.',
         'Guarda ahora la misma lista mediante serialización de objetos. Compara las dos '
         'soluciones e indica cuándo usarías cada una.',
         '<b>Ampliación:</b> comprueba qué ocurre con el identificador de versión de '
         'serialización si se añade un atributo a la clase, y explica por qué conviene declararlo '
         'a mano.'],
        'Al leer un CSV, no basta con partir la línea por comas. Piensa qué pasa con una película '
        'titulada «Alien, el octavo pasajero».')

    S += ejercicio(
        'A4', 'Análisis de un programa ajeno', 'CE2.5, CE2.6, CE1.8',
        ['Programar es solo la mitad del oficio: la otra mitad es leer código que ha escrito otra '
         'persona. En un trabajo real se lee mucho más de lo que se escribe, y casi nunca desde '
         'el principio.',
         'Se te entrega el código del ejercicio 13 de la UF2404, el sistema del videoclub, '
         '<b>sin su enunciado</b>. No tienes que modificarlo: tienes que entenderlo.'],
        ['Localiza en el código cada una de las siguientes características del paradigma '
         'orientado a objetos, indicando clase y línea: encapsulación, herencia, polimorfismo, '
         'clase abstracta, método redefinido, composición, agregación y jerarquía de excepciones.',
         'Reconstruye el diagrama de clases a partir del código, con sus relaciones y '
         'multiplicidades, sin mirar la documentación.',
         'Identifica los métodos que forman la <b>interfaz</b> de cada clase (lo que se puede '
         'usar desde fuera) y sepáralos de los detalles internos. Explica qué criterio has '
         'seguido.',
         'Deduce, solo leyendo el código, cuáles son las tres reglas de negocio del videoclub y '
         'en qué método está implementada cada una.',
         'Utiliza las herramientas del entorno de desarrollo para responder más deprisa a los '
         'apartados anteriores. Documenta qué función has usado para cada cosa:<br/>'
         '&nbsp;&nbsp;— Ver la jerarquía de tipos de una clase.<br/>'
         '&nbsp;&nbsp;— Encontrar todos los usos de un método.<br/>'
         '&nbsp;&nbsp;— Saltar a la declaración de un símbolo y volver.<br/>'
         '&nbsp;&nbsp;— Ver la estructura de un fichero de un vistazo.<br/>'
         '&nbsp;&nbsp;— Generar el diagrama de clases, si tu entorno lo permite.',
         'Escribe un informe de media página explicando qué hace el programa, dirigido a alguien '
         'que va a modificarlo la semana que viene.',
         '<b>Ampliación:</b> escribe un programa que analice una clase por reflexión y responda '
         'automáticamente a parte del apartado 1.'],
        'Para entender código ajeno, empieza por los tipos y no por los algoritmos. Saber qué '
        'clases hay y cómo se relacionan explica el noventa por ciento; el cuerpo de los métodos '
        'casi siempre se deduce después.')


    S += ejercicio(
        'A13', 'El ciclo de desarrollo orientado a objetos', 'CE1.1, CE1.8',
        ['Este proyecto no empieza en la primera clase que escribiste: empieza mucho antes, con '
         'un cliente que cuenta lo que necesita. El sistema de gestión del gimnasio que recorre '
         'la UF2406 está terminado, y sus artefactos siguen ahí: requisitos, tarjetas CRC, '
         'diagramas, código, pruebas y manuales. Este ejercicio pide recorrer ese ciclo hacia '
         'atrás, sobre un caso ya resuelto, para situar dónde encaja exactamente la programación '
         'orientada a objetos.'],
        ['Enumera las fases del ciclo de desarrollo del software bajo el paradigma orientado a '
         'objetos y describe, para cada una, qué entra, qué sale y qué documento o artefacto la '
         'representa.',
         'Localiza dentro de este proyecto el artefacto concreto que corresponde a cada fase del '
         'sistema del gimnasio y anótalo junto a la fase. Si alguna fase no tiene artefacto, dilo.',
         'Comprueba la <b>trazabilidad</b> de la cadena: verifica que todo lo que entra en una '
         'fase ha salido de alguna fase anterior, y señala en qué punto exacto del ciclo entra la '
         'programación orientada a objetos.',
         'Explica por qué la POO es <b>una</b> fase del ciclo y no el ciclo entero: qué '
         'decisiones están ya tomadas antes de escribir la primera clase, y qué queda por hacer '
         'después de que el código compile.',
         'Compara este ciclo con el del paradigma estructurado: qué unidad recorre las fases en '
         'cada uno, y qué consecuencia tiene eso sobre el mantenimiento cuando cambia un '
         'requisito.',
         'Explica por qué el ciclo orientado a objetos se recorre en espiral y no en cascada, y '
         'qué significa que las fases compartan vocabulario.',
         'Escribe un programa que, dado el nombre de una fase, devuelva sus entradas, sus salidas '
         'y su artefacto, y que compruebe de forma automática la trazabilidad de la cadena '
         'completa.'],
        'La prueba de si has entendido el ciclo es sencilla. Coge el nombre de una clase '
        'cualquiera del proyecto, por ejemplo <b>ClaseColectiva</b>, y rastrea hacia atrás de '
        'dónde ha salido: de un diagrama, que salió de unas tarjetas CRC, que salieron de un '
        'texto de requisitos, que salió de una conversación. Si el rastro se corta en algún '
        'punto, ahí falta una fase.')

    S += ejercicio(
        'A14', 'El paso de mensajes', 'CE1.4, CE2.4',
        ['En el paradigma orientado a objetos los objetos no se leen ni se manipulan: se les pide '
         'algo. A esa petición se le llama <b>mensaje</b>, y el comportamiento del programa es el '
         'resultado de decidir, en cada envío, quién responde. Este ejercicio pretende que veas '
         'esa decisión ocurrir.'],
        ['Explica qué es un mensaje y de qué tres partes se compone. Relaciona cada parte con lo '
         'que escribes en Java al invocar un método.',
         'Escribe una jerarquía <b>Notificador</b> con tres subclases (Email, Sms y Push) que '
         'respondan al mismo mensaje de formas distintas. Envía el mismo mensaje a los tres a '
         'través de una variable del tipo padre y anota qué responde cada uno.',
         'Demuestra la diferencia entre <b>sobrecarga</b> y <b>redefinición</b>: construye un '
         'caso en el que el método elegido dependa del tipo declarado de la variable y otro en el '
         'que dependa del tipo real del objeto. Explica en qué momento se decide cada uno.',
         'Comprueba tu respuesta con reflexión: para cada envío imprime la clase real del '
         'receptor, el selector, los tipos de los argumentos y la clase en la que se ha '
         'encontrado finalmente el método.',
         'Envía un mensaje a una referencia nula, captura el error y explica por qué es un error '
         'de ejecución y no de compilación.',
         'Usa <b>super</b> para enviar el mismo mensaje empezando la búsqueda en la superclase, y '
         'demuestra que el receptor sigue siendo el mismo objeto.',
         'Explica por qué se dice que el comportamiento de un objeto es el conjunto de mensajes '
         'que sabe responder, y no el valor de sus atributos.'],
        'Para el apartado 3, prueba a declarar una variable de tipo <b>Object</b> que contenga '
        'una cadena y pasársela a un método sobrecargado que tenga una versión para Object y otra '
        'para String. El resultado sorprende, y sorprende porque la sobrecarga la resuelve el '
        'compilador, que solo conoce lo que has escrito en la declaración.')

    S.append(PageBreak())

    # ================= UF2405 =================
    S.append(barra_uf('UF2405 — MODELO DE PROGRAMACIÓN WEB Y BASES DE DATOS'))

    S += ejercicio(
        'A5', 'Protocolos y formatos de intercambio', 'CE1.2, CE1.1',
        ['Has programado Servlets sin ver nunca lo que viaja de verdad entre el navegador y el '
         'servidor. Este ejercicio levanta la tapa: HTTP es texto plano sobre una conexión TCP, y '
         'se puede escribir a mano.'],
        ['Escribe un servidor mínimo que escuche en un puerto, acepte una conexión TCP y responda '
         'con una respuesta HTTP correcta. No uses ningún contenedor web: solo sockets.',
         'Escribe un cliente que envíe una petición GET escrita a mano y muestre por pantalla, '
         'línea a línea, lo que envía y lo que recibe.',
         'Identifica en esa conversación las cuatro partes de una petición (método, ruta, versión '
         'y cabeceras) y las de una respuesta (versión, código de estado, cabeceras, línea en '
         'blanco y cuerpo).',
         'Repite el intercambio con una petición POST que lleve datos en el cuerpo. Explica qué '
         'cabecera indica dónde termina ese cuerpo y qué pasaría si faltara.',
         'Provoca una respuesta 404 y otra 302. Indica qué hace el navegador con cada código y '
         'relaciona el 302 con el patrón POST-Redirect-GET del ejercicio 11.',
         'Analiza un documento XML con las clases del propio JDK, extrayendo los datos de una '
         'lista de mascotas.',
         'Analiza un mensaje SOAP, que no es más que un XML con una estructura convenida, e '
         'identifica su sobre, su cabecera y su cuerpo.',
         'Genera la misma información en JSON y compara los tres formatos: cuándo usarías cada uno.'],
        'HTTP separa las cabeceras del cuerpo con una <b>línea en blanco</b>. Ese detalle, que '
        'parece trivial, es lo que permite al receptor saber dónde dejan de ser metadatos y '
        'empiezan a ser datos.')

    S += ejercicio(
        'A6', 'Tecnologías de acceso a datos', 'CE2.2, CE2.1, CE1.3',
        ['En el bloque 3 has usado JDBC con <font face="Courier">DriverManager</font> porque era '
         'lo más sencillo. En un proyecto real hay que <b>elegir</b> la tecnología de acceso a '
         'datos, y esa elección tiene consecuencias que se pagan durante años.'],
        ['Enumera las tecnologías habituales de conexión y acceso a datos desde una aplicación '
         'Java, indicando de cada una qué problema resuelve: DriverManager, DataSource con pool, '
         'JPA/Hibernate y los llamados mapeadores ligeros.',
         'Implementa tres estrategias de acceso con el <b>mismo contrato</b>, de forma que se '
         'puedan intercambiar sin tocar el resto del programa: una que abra conexión en cada '
         'operación, otra que reutilice una del pool y otra que además convierta automáticamente '
         'las filas en objetos.',
         'Cronometra las tres haciendo el mismo trabajo y comenta los resultados. Indica qué '
         'parte del coste es de la conexión y cuál del mapeo.',
         'Cuenta las líneas de código que hace falta escribir con cada una para leer una entidad '
         'de cinco columnas, y compara ese dato con el anterior. Explica por qué la tecnología '
         'más rápida no siempre es la que conviene.',
         'Elabora una tabla de decisión que indique, para cuatro escenarios concretos, cuál '
         'elegirías y por qué: una aplicación de escritorio de un solo usuario, una web con 200 '
         'usuarios simultáneos, un proceso nocturno que carga un millón de filas y un '
         'microservicio con dos tablas.',
         'Explica qué aporta un <font face="Courier">DataSource</font> frente a '
         '<font face="Courier">DriverManager</font> más allá del rendimiento.',
         'Compara brevemente el modelo de acceso a datos de la plataforma Java con el de .NET, '
         'señalando los elementos equivalentes.'],
        'La pregunta correcta no es cuál es la tecnología mejor, sino cuál es el problema '
        'dominante en este proyecto. Si el cuello de botella es el número de conexiones, la '
        'respuesta es un pool; si es la cantidad de código repetitivo, un mapeador; y si es el '
        'control fino de la consulta, JDBC.')

    S.append(PageBreak())

    # ================= UF2406 =================
    S.append(barra_uf('UF2406 — EL CICLO DE VIDA DEL DESARROLLO DE APLICACIONES'))

    S += ejercicio(
        'A7', 'Plantilla y documento de diseño de una clase', 'CE3.3, CE3.7',
        ['El Javadoc del ejercicio 11 documenta el código <b>ya escrito</b>. El documento de '
         'diseño es lo contrario: se escribe <b>antes</b>, y sirve para que alguien pueda '
         'programar la clase sin volver a preguntar.'],
        ['Propón el índice (plantilla) del documento de diseño de una clase, explicando qué '
         'contiene cada apartado y por qué hace falta.',
         'Justifica cada apartado respondiendo a esta pregunta: ¿qué decisión tendría que tomar '
         'por su cuenta quien programe la clase si ese apartado no estuviera?',
         'Elabora el documento completo para la clase <b>GestorInscripciones</b> del sistema del '
         'gimnasio, siguiendo tu propia plantilla.',
         'Escribe un comprobador que verifique que un documento concreto contiene todos los '
         'apartados obligatorios de la plantilla y señale los que faltan.',
         'Explica en qué se diferencia este documento del Javadoc y del manual de usuario: '
         'destinatario, momento en que se escribe y qué pasa si no existe.',
         'Indica qué apartados de tu plantilla quedan obsoletos en cuanto el código cambia, y '
         'propón cómo evitar que el documento acabe mintiendo.'],
        'Un documento de diseño que solo repite los nombres de los métodos no sirve para nada, '
        'porque eso ya lo dice el código. Lo que aporta valor es todo lo que el código <b>no</b> '
        'puede decir: por qué se eligió ese diseño, qué alternativas se descartaron y qué pasa en '
        'los casos raros.')

    S += ejercicio(
        'A8', 'Plantilla e informe de pruebas', 'CE3.4, CE3.8',
        ['Has diseñado pruebas (ejercicio 7), las has automatizado (ejercicio 9) y has ejecutado '
         'una regresión (ejercicio 12). Falta lo que se entrega al cliente o al responsable de '
         'calidad: el <b>informe</b>.'],
        ['Propón el índice (plantilla) del documento de pruebas, que debe recoger tanto el '
         '<b>diseño</b> de las pruebas como sus <b>resultados</b>.',
         'Explica por qué esos dos contenidos van en el mismo documento y qué pasaría si se '
         'entregaran por separado.',
         'Elabora el informe completo de las pruebas del sistema del gimnasio, con los datos '
         'reales obtenidos al ejecutar las baterías de los ejercicios 9 y 12.',
         'Genera el informe de forma <b>automática</b> a partir de la ejecución, en lugar de '
         'copiar los números a mano. Explica qué ventaja tiene.',
         'Incluye en el informe el <b>veredicto</b>: se acepta la entrega o no, y con qué criterio '
         'se decide. Un informe que no concluye nada no sirve.',
         'Añade el apartado de defectos detectados, con su gravedad y su estado, y explica la '
         'diferencia entre un defecto <b>abierto</b> y uno <b>aceptado</b>.',
         'Indica qué cifras del informe <b>no</b> deben usarse como objetivo, y por qué.'],
        'Un informe de pruebas que solo diga «todas las pruebas pasan» es inútil, porque no dice '
        'cuántas eran ni qué cubrían. Cien pruebas que pasan sobre el diez por ciento del código '
        'son peores que veinte sobre el ochenta.')

    S += ejercicio(
        'A9', 'Manual de operación y mantenimiento', 'CE3.5, CE3.9',
        ['El sistema del gimnasio se entrega y alguien tiene que <b>mantenerlo funcionando</b>: '
         'instalarlo, actualizarlo, hacer copias de seguridad y resolver la llamada del sábado '
         'por la tarde cuando no arranca. Esa persona no eres tú, no ha visto el código y '
         'probablemente no sepa Java.'],
        ['Propón el índice (plantilla) del manual de operación y mantenimiento, también llamado '
         'manual técnico.',
         'Explica a quién va dirigido y en qué se diferencia del manual de usuario y del Javadoc. '
         'Los tres son documentación y ninguno sustituye a los otros dos.',
         'Elabora el manual completo del sistema del gimnasio siguiendo tu plantilla.',
         'Redacta con detalle el apartado de <b>resolución de incidencias</b>, con al menos cinco '
         'síntomas reales, su causa probable y su solución.',
         'Redacta el apartado de <b>copias de seguridad</b> indicando qué se copia, cada cuánto, '
         'dónde y, sobre todo, cómo se comprueba que la copia sirve.',
         'Escribe el <b>procedimiento de vuelta atrás</b>: qué hacer si una versión nueva sale mal '
         'en producción un viernes.',
         'Explica qué información <b>no</b> debe aparecer nunca en este manual.'],
        'Escribe cada procedimiento como si lo fuera a seguir alguien a las tres de la madrugada, '
        'con prisa y sin poder preguntarte. Eso descarta las frases del tipo «configurar '
        'adecuadamente el servidor».')

    S += ejercicio(
        'A10', 'Gestión de la configuración del software', 'CE4.2, CE4.4, CE4.6',
        ['En el ejercicio 12 modificaste una clase siguiendo un procedimiento. Este ejercicio se '
         'ocupa de lo que rodea a ese cambio: cómo se identifica cada versión, cómo se guarda su '
         'historia y cómo se sabe exactamente qué código hay instalado en el gimnasio.'],
        ['Explica qué es la gestión de la configuración del <b>software</b> y en qué se diferencia '
         'de la gestión de la configuración de la <b>documentación</b>.',
         'Define los elementos de configuración del proyecto del gimnasio: qué se versiona y qué '
         'no, justificando cada exclusión.',
         'Implementa un versionador que, dado el tipo de cambio, calcule la versión siguiente '
         'según el versionado semántico.',
         'Escribe un validador de mensajes de registro de cambios que compruebe una convención '
         'acordada por el equipo, y pásale mensajes reales, buenos y malos.',
         'Explica qué es una <b>línea base</b> y por qué hace falta poder reconstruir exactamente '
         'la versión que hay instalada en el cliente.',
         'Aplica el procedimiento de cambio a un <b>documento</b>, no a una clase: indica cómo se '
         'identifica una edición y una revisión, y quién aprueba cada cambio.',
         'Enumera las órdenes concretas del sistema de control de versiones para: crear la rama '
         'del cambio del ejercicio 12, integrarla, etiquetar la entrega y volver atrás si sale mal.'],
        'La pregunta que resuelve la gestión de la configuración es siempre la misma, y es más '
        'práctica de lo que parece: si el gimnasio llama mañana diciendo que algo falla, '
        '¿puedes reconstruir <b>exactamente</b> el código que tienen instalado?')

    S += ejercicio(
        'A11', 'De la documentación de diseño a la interfaz', 'CE5.4, CE5.6, CE5.7',
        ['En el ejercicio 14 construiste una ventana a partir de un enunciado. En un proyecto real '
         'se construye a partir de la <b>documentación de diseño</b>, que llega ya hecha, y hay '
         'que respetarla.',
         'Se te entrega el diseño de la pantalla de inscripción del gimnasio, con su distribución, '
         'sus componentes y su comportamiento.'],
        ['Enumera las clases de la biblioteca de interfaz que necesitas para cada elemento del '
         'diseño, y explica qué criterio has seguido para elegir entre las que hacen cosas '
         'parecidas: por ejemplo, una lista desplegable frente a un grupo de botones de opción.',
         'Construye la ventana respetando exactamente la distribución indicada, de modo que se '
         'comporte bien al cambiar de tamaño y al cambiar el tamaño de letra del sistema.',
         'Incorpora recursos multimedia: el logotipo del gimnasio en la cabecera y un aviso sonoro '
         'cuando la inscripción falla.',
         'Genera el logotipo mediante código en lugar de cargarlo de un fichero, y explica en qué '
         'casos conviene cada opción.',
         'Programa las clases que conectan la interfaz con la aplicación, de forma que la ventana '
         'no contenga <b>ninguna</b> regla de negocio.',
         'Demuestra esa separación: sustituye la capa de negocio por otra distinta sin tocar ni '
         'una línea de la ventana.',
         'Explica qué problema tienen los recursos multimedia cargados desde una ruta absoluta, y '
         'cómo se resuelve al empaquetar la aplicación.'],
        'Si para probar un cambio en la lógica tienes que abrir la ventana y hacer clic, la '
        'separación no está bien hecha. La prueba definitiva es poder ejecutar la lógica entera '
        'sin que aparezca nada en pantalla.')

    S += ejercicio(
        'A12', 'Validación y accesibilidad de la interfaz', 'CE5.5, CE5.8',
        ['La pantalla de inscripción ya está construida. Ahora hay que <b>comprobar</b> que cumple '
         'lo que se pedía, y ese es un trabajo distinto de programarla.',
         'El Real Decreto que regula este certificado menciona expresamente los criterios de '
         'accesibilidad para personas con discapacidad, así que no es un extra opcional: forma '
         'parte de lo que hay que verificar.'],
        ['Establece los criterios de validación de la pantalla: qué se considera correcto y cómo '
         'se comprueba cada cosa objetivamente.',
         'Comprueba que los formatos de entrada y salida son los esperados, incluyendo los casos '
         'raros: números con coma, fechas, campos vacíos y textos muy largos.',
         'Clasifica los tipos de error que puede cometer el usuario en esta pantalla y decide cómo '
         'responde la interfaz a cada uno.',
         'Mide el <b>contraste</b> entre el texto y su fondo para todos los mensajes de la '
         'pantalla, y comprueba si alcanzan el nivel exigido. Investiga qué relación de contraste '
         'se considera suficiente.',
         'Comprueba que la pantalla se puede usar <b>entera sin ratón</b>, e indica qué hay que '
         'añadir al código para que así sea.',
         'Revisa si la información se transmite únicamente mediante el color, y corrígelo si es el '
         'caso.',
         'Redacta el índice de la guía de usuario de esta pantalla.',
         'Elabora el informe de validación con el veredicto y la lista de defectos encontrados.'],
        'Para el apartado 6, imprime la pantalla en blanco y negro. Todo lo que deje de '
        'distinguirse es información que estabas transmitiendo solo con el color, y que no llega a '
        'una de cada doce personas.')

    # ---------------- Cierre ----------------
    S.append(PageBreak())
    S.append(Paragraph('COBERTURA DE LOS CRITERIOS DE EVALUACIÓN', E['h2']))
    filas_cob, antes, despues, total, sin_cubrir = cobertura_real()
    S.append(Paragraph(
        'Los tres cuadernos del módulo cubren con ejercicio %d de los %d criterios de evaluación '
        'del Real Decreto. Con estos catorce ejercicios la cobertura pasa a %d.'
        % (antes, total, despues),
        E['cuerpo']))
    datos = [['Unidad formativa', 'Antes', 'Después', 'Total']] + filas_cob
    t = Table(datos, colWidths=[8.4 * cm, 2.6 * cm, 2.6 * cm, 2.8 * cm])
    t.setStyle(TableStyle([
        ('BACKGROUND', (0, 0), (-1, 0), AZUL),
        ('TEXTCOLOR', (0, 0), (-1, 0), colors.white),
        ('FONTNAME', (0, 0), (-1, 0), 'Helvetica-Bold'),
        ('FONTNAME', (0, -1), (-1, -1), 'Helvetica-Bold'),
        ('FONTNAME', (0, 1), (-1, -2), 'Helvetica'),
        ('FONTSIZE', (0, 0), (-1, -1), 9.5),
        ('ALIGN', (1, 0), (-1, -1), 'CENTER'),
        ('TOPPADDING', (0, 0), (-1, -1), 6),
        ('BOTTOMPADDING', (0, 0), (-1, -1), 6),
        ('GRID', (0, 0), (-1, -1), 0.5, colors.HexColor('#C9D0D9')),
        ('BACKGROUND', (0, -1), (-1, -1), GRIS_CLARO),
    ]))
    S.append(t)
    S.append(Spacer(1, 0.5 * cm))
    if sin_cubrir:
        S.append(Paragraph(
            'Siguen sin ejercicio: <b>%s</b>.' % ', '.join(sin_cubrir), E['cuerpo']))
    else:
        S.append(Paragraph(
            'Con los ejercicios A13 y A14 no queda ningún criterio del módulo sin ejercicio '
            'asociado. Los dos últimos cierran los dos criterios de la UF2404 que empiezan por '
            '«explicar» —el ciclo de desarrollo y el paso de mensajes—, que hasta ahora solo se '
            'trataban en la teoría; y el CE4.5 de la UF2406 lo cumple punto por punto el '
            'ejercicio 12 de su cuaderno, que ahora lo declara junto a CE4.1 y CE4.3. Conviene '
            'reflejar ese último cambio también en el cuaderno original en .docx.', E['cuerpo']))
    S.append(Spacer(1, 0.3 * cm))
    S.append(Paragraph(
        'Referencia normativa: Real Decreto 628/2013, de 2 de agosto (BOE núm. 225, de 19 de '
        'septiembre de 2013), anexo IV, certificado IFCD0112. El PDF oficial tiene varios '
        'párrafos con la codificación de fuente dañada; el texto de esos criterios se ha '
        'recuperado glifo a glifo y contrastado con el resto del articulado, y la correspondencia '
        'completa está en <i>recursos/MATRIZ_CE.md</i>.', E['nota']))

    doc.build(S)
    print('PDF generado:', ruta)


if __name__ == '__main__':
    aqui = os.path.dirname(os.path.abspath(__file__))
    construir(os.path.join(aqui, 'Ejercicios_Ampliacion_MF0227_3.pdf'))
