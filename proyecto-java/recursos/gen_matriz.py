#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Genera la matriz de correspondencia ejercicios <-> criterios de evaluacion
del MF0227_3, en PDF y en Markdown, leyendo directamente el proyecto."""

import io, os, re
from reportlab.lib import colors
from reportlab.lib.enums import TA_JUSTIFY
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import ParagraphStyle, getSampleStyleSheet
from reportlab.lib.units import cm
from reportlab.platypus import (BaseDocTemplate, Frame, PageBreak, PageTemplate,
                                Paragraph, Spacer, Table, TableStyle)

from ce_catalogo import CE
from matriz import leer, cobertura

AZUL = colors.HexColor('#183E6E')
GRIS = colors.HexColor('#5A6472')
GRIS_CLARO = colors.HexColor('#EEF1F5')
VERDE = colors.HexColor('#1D6F42')
ROJO = colors.HexColor('#9A2B2B')
BORDE = colors.HexColor('#C9D0D9')

ss = getSampleStyleSheet()
E = {
 'titulo': ParagraphStyle('t', parent=ss['Title'], fontName='Helvetica-Bold', fontSize=24,
                          leading=29, textColor=AZUL, spaceAfter=4),
 'sub': ParagraphStyle('s', parent=ss['Normal'], fontName='Helvetica', fontSize=13.5,
                       leading=18, textColor=GRIS, alignment=1),
 'uf': ParagraphStyle('uf', parent=ss['Normal'], fontName='Helvetica-Bold', fontSize=14.5,
                      leading=18, textColor=colors.white, leftIndent=6, keepWithNext=1),
 'h2': ParagraphStyle('h2', parent=ss['Normal'], fontName='Helvetica-Bold', fontSize=12,
                      leading=16, textColor=AZUL, spaceBefore=13, spaceAfter=5, keepWithNext=1),
 'cuerpo': ParagraphStyle('c', parent=ss['Normal'], fontName='Helvetica', fontSize=9.8,
                          leading=14, alignment=TA_JUSTIFY, spaceAfter=6),
 'nota': ParagraphStyle('n', parent=ss['Normal'], fontName='Helvetica-Oblique', fontSize=8.4,
                        leading=11.6, textColor=GRIS, alignment=TA_JUSTIFY, spaceBefore=4),
 'th': ParagraphStyle('th', parent=ss['Normal'], fontName='Helvetica-Bold', fontSize=8.6,
                      leading=11, textColor=colors.white),
 'td': ParagraphStyle('td', parent=ss['Normal'], fontName='Helvetica', fontSize=8.4, leading=11),
 'tdb': ParagraphStyle('tdb', parent=ss['Normal'], fontName='Helvetica-Bold', fontSize=8.4, leading=11),
 'tdce': ParagraphStyle('tdce', parent=ss['Normal'], fontName='Helvetica', fontSize=7.9, leading=10.2),
}
ANCHO = 16.4 * cm

NOTAS_CIERRE = [
 ('Los 68 criterios tienen ejercicio',
  'Los ejercicios A13 y A14 cierran los dos últimos criterios de la UF2404, y la reetiquetación '
  'del ejercicio 12 cierra el de la UF2406. No queda ningún criterio sin ejercicio.'),
 ('UF2404 · CE1.1 — el ciclo de desarrollo (ejercicio A13)',
  'Es un criterio de «explicar»: pide situar la programación orientada a objetos como una fase '
  'dentro del ciclo completo. El ejercicio A13 lo convierte en algo comprobable: modela las nueve '
  'fases del ciclo con sus entradas, sus salidas y el artefacto de este mismo proyecto que las '
  'representa, verifica en disco que los nueve artefactos existen, y comprueba automáticamente '
  'la trazabilidad de la cadena —que todo lo que entra en una fase ha salido de alguna anterior—. '
  'La POO queda situada como la fase 6 de 9: cinco fases antes y tres después.'),
 ('UF2404 · CE1.4 — el paso de mensajes (ejercicio A14)',
  'Pide describir la estructura de un mensaje (receptor, selector, argumentos) y su relación con '
  'el comportamiento. El ejercicio A14 lo demuestra en ejecución: la misma jerarquía respondiendo '
  'de tres formas distintas al mismo envío, la sobrecarga resuelta por el tipo declarado frente a '
  'la redefinición resuelta por el tipo real, la reflexión mostrando en qué clase acaba '
  'resolviéndose cada mensaje, el error al enviar un mensaje sin receptor y la demostración de '
  'que <b>super</b> cambia dónde se busca, no a quién se pregunta.'),
 ('UF2406 · CE4.5 — modificación de una clase por un cambio de diseño',
  'Este criterio no necesitaba ejercicio nuevo: lo cumple punto por punto el ejercicio 12 del '
  'cuaderno de la UF2406, cuyos apartados 4, 5 y 6 recorren los cinco puntos del criterio '
  '(modificar el código, dejar el histórico en la cabecera, ajustar los programas de prueba, '
  'ejecutar la regresión y actualizar la documentación afectada). El fichero <i>.java</i> ya lo '
  'declara junto a CE4.1 y CE4.3. <b>Conviene reflejar el cambio también en el cuaderno original '
  'en .docx</b>, que aún lo atribuye solo a esos dos.'),
]


NOMBRE_UF = {
 'UF2404': 'UF2404 · Programación orientada a objetos',
 'UF2405': 'UF2405 · Modelo de programación web y bases de datos relacionales',
 'UF2406': 'UF2406 · Desarrollo de clases: pruebas, documentación e interfaces',
}
GRUPO = {'UF2404': 2, 'UF2405': 2, 'UF2406': 5}


def barra(texto):
    t = Table([[Paragraph(texto, E['uf'])]], colWidths=[ANCHO])
    t.setStyle(TableStyle([('BACKGROUND', (0, 0), (-1, -1), AZUL),
                           ('TOPPADDING', (0, 0), (-1, -1), 7),
                           ('BOTTOMPADDING', (0, 0), (-1, -1), 7)]))
    return t


def tabla(cab, filas, anchos, alturas_zebra=True):
    datos = [[Paragraph(c, E['th']) for c in cab]] + filas
    t = Table(datos, colWidths=anchos, repeatRows=1)
    est = [('BACKGROUND', (0, 0), (-1, 0), AZUL),
           ('GRID', (0, 0), (-1, -1), 0.5, BORDE),
           ('VALIGN', (0, 0), (-1, -1), 'TOP'),
           ('LEFTPADDING', (0, 0), (-1, -1), 4),
           ('RIGHTPADDING', (0, 0), (-1, -1), 4),
           ('TOPPADDING', (0, 0), (-1, -1), 3.5),
           ('BOTTOMPADDING', (0, 0), (-1, -1), 3.5)]
    if alturas_zebra:
        for i in range(1, len(datos)):
            if i % 2 == 0:
                est.append(('BACKGROUND', (0, i), (-1, i), GRIS_CLARO))
    t.setStyle(TableStyle(est))
    return t


def ubicacion(e):
    return e['fichero'].replace('src/com/ifcd0112/ejercicios/', '').replace('.java', '')


def construir(ruta_pdf, ruta_md):
    ejs = leer()
    m = cobertura(ejs)

    doc = BaseDocTemplate(ruta_pdf, pagesize=A4, leftMargin=2.3 * cm, rightMargin=2.3 * cm,
                          topMargin=2.2 * cm, bottomMargin=2.2 * cm,
                          title='Matriz de criterios de evaluación - MF0227_3',
                          author='IFCD0112 - MF0227_3',
                          subject='Correspondencia ejercicios / criterios de evaluación')
    marco = Frame(doc.leftMargin, doc.bottomMargin, doc.width, doc.height, id='n')

    def pie(canv, d):
        canv.saveState()
        if d.page > 1:
            canv.setStrokeColor(BORDE); canv.setLineWidth(0.5)
            canv.line(2.3 * cm, 1.65 * cm, A4[0] - 2.3 * cm, 1.65 * cm)
            canv.setFont('Helvetica', 8.5); canv.setFillColor(GRIS)
            canv.drawString(2.3 * cm, 1.2 * cm,
                            'Matriz de criterios de evaluación · MF0227_3 · IFCD0112')
            canv.drawRightString(A4[0] - 2.3 * cm, 1.2 * cm, '%d' % d.page)
        canv.restoreState()

    doc.addPageTemplates([PageTemplate(id='b', frames=[marco], onPage=pie)])
    S = []
    MD = []

    # ---------------------------------------------------------------- portada
    S += [Spacer(1, 4.2 * cm),
          Paragraph('MATRIZ DE CRITERIOS<br/>DE EVALUACIÓN', E['titulo']),
          Spacer(1, 0.5 * cm),
          Paragraph('MF0227_3 · Programación Orientada a Objetos', E['sub']),
          Paragraph('Certificado de Profesionalidad IFCD0112', E['sub']),
          Spacer(1, 1.4 * cm)]
    n_ces = sum(len([c for c in CE[u] if c.startswith('CE')]) for u in CE)
    huecos = [(uf, c) for uf in ('UF2404', 'UF2405', 'UF2406')
              for c in CE[uf] if c.startswith('CE') and not m[uf][c]]
    huecos.sort()
    cierre = ('Al final, los criterios que se quedan sin ejercicio y qué haría falta para '
              'cerrarlos.' if huecos else
              'Ningún criterio se queda sin ejercicio asociado.')
    intro = ('Correspondencia, en los dos sentidos, entre los %d ejercicios del proyecto y los %d '
             'criterios de evaluación que fija el Real Decreto 628/2013, de 2 de agosto, en el '
             'anexo IV. Para cada ejercicio, qué criterios acredita; para cada criterio, con qué '
             'ejercicio se demuestra. %s' % (len(ejs), n_ces, cierre))
    S.append(Paragraph(intro, ParagraphStyle('i', parent=E['cuerpo'], alignment=1, fontSize=10.5,
                                             leading=15.5, leftIndent=1.1 * cm, rightIndent=1.1 * cm)))
    S.append(Spacer(1, 1.2 * cm))

    # resumen de cobertura
    filas = []
    tc = tt = ta = 0
    for uf in ('UF2404', 'UF2405', 'UF2406'):
        ces = [c for c in CE[uf] if c.startswith('CE')]
        cuad = [c for c in ces if any(not e['amp'] for e in m[uf][c])]
        todo = [c for c in ces if m[uf][c]]
        nej = len([e for e in ejs if e['uf'] == uf and not e['amp']])
        namp = len([e for e in ejs if e['uf'] == uf and e['amp']])
        filas.append([Paragraph(uf, E['tdb']),
                      Paragraph(str(nej), E['td']), Paragraph(str(namp), E['td']),
                      Paragraph(str(len(ces)), E['td']),
                      Paragraph('%d' % len(cuad), E['td']),
                      Paragraph('<b>%d</b>' % len(todo), E['td']),
                      Paragraph('%d' % (len(ces) - len(todo)), E['td'])])
        tc += len(cuad); tt += len(todo); ta += len(ces)
    n_cuad = len([e for e in ejs if not e['amp']])
    n_amp = len([e for e in ejs if e['amp']])
    filas.append([Paragraph('<b>TOTAL</b>', E['tdb']),
                  Paragraph('<b>%d</b>' % n_cuad, E['tdb']),
                  Paragraph('<b>%d</b>' % n_amp, E['tdb']),
                  Paragraph('<b>%d</b>' % ta, E['tdb']), Paragraph('<b>%d</b>' % tc, E['tdb']),
                  Paragraph('<b>%d</b>' % tt, E['tdb']),
                  Paragraph('<b>%d</b>' % (ta - tt), E['tdb'])])
    t = tabla(['Unidad', 'Ej. cuaderno', 'Ej. ampliación', 'CE del RD',
               'CE cubiertos<br/>solo cuaderno', 'CE cubiertos<br/>con ampliación', 'CE sin<br/>ejercicio'],
              filas, [2.3 * cm, 2.1 * cm, 2.3 * cm, 2.0 * cm, 2.6 * cm, 2.7 * cm, 2.4 * cm])
    t.setStyle(TableStyle([('BACKGROUND', (0, len(filas)), (-1, len(filas)), GRIS_CLARO)]))
    S.append(t)
    S.append(PageBreak())

    MD.append('# Matriz de criterios de evaluación · MF0227_3 (IFCD0112)\n')
    MD.append(intro + '\n')
    MD.append('| Unidad | Ej. cuaderno | Ej. ampliación | CE del RD | CE cubiertos solo cuaderno | '
              'CE cubiertos con ampliación | CE sin ejercicio |')
    MD.append('|---|---|---|---|---|---|---|')
    for uf in ('UF2404', 'UF2405', 'UF2406'):
        ces = [c for c in CE[uf] if c.startswith('CE')]
        cuad = [c for c in ces if any(not e['amp'] for e in m[uf][c])]
        todo = [c for c in ces if m[uf][c]]
        MD.append('| %s | %d | %d | %d | %d | %d | %d |' % (
            uf, len([e for e in ejs if e['uf'] == uf and not e['amp']]),
            len([e for e in ejs if e['uf'] == uf and e['amp']]),
            len(ces), len(cuad), len(todo), len(ces) - len(todo)))
    MD.append('| **TOTAL** | **%d** | **%d** | **%d** | **%d** | **%d** | **%d** |\n'
              % (n_cuad, n_amp, ta, tc, tt, ta - tt))

    # ---------------------------------------------------- una seccion por UF
    for uf in ('UF2404', 'UF2405', 'UF2406'):
        S.append(barra(NOMBRE_UF[uf]))
        S.append(Spacer(1, 0.35 * cm))
        MD.append('\n## %s\n' % NOMBRE_UF[uf].replace('·', '—'))

        # --- A. ejercicio -> CE
        S.append(Paragraph('A. Qué criterios acredita cada ejercicio', E['h2']))
        MD.append('### A. Qué criterios acredita cada ejercicio\n')
        MD.append('| Ej. | Título | Criterios | Fichero |')
        MD.append('|---|---|---|---|')
        filas = []
        for e in [x for x in ejs if x['uf'] == uf]:
            extra = ''
            if e['extra']:
                extra = ' <font color="#9A2B2B">(+%s, ver nota)</font>' % ', '.join(e['extra'])
            filas.append([Paragraph('<b>%s</b>' % e['codigo'], E['tdb']),
                          Paragraph(e['titulo'], E['td']),
                          Paragraph(', '.join(e['ces']) + extra, E['td']),
                          Paragraph('<font size=7>%s</font>' % ubicacion(e), E['td'])])
            MD.append('| %s | %s | %s%s | `%s` |' % (
                e['codigo'], e['titulo'], ', '.join(e['ces']),
                (' (+%s, ver nota)' % ', '.join(e['extra'])) if e['extra'] else '',
                ubicacion(e)))
        S.append(tabla(['Ej.', 'Título', 'Criterios', 'Fichero'],
                       filas, [1.1 * cm, 5.8 * cm, 3.0 * cm, 6.5 * cm]))
        S.append(Spacer(1, 0.3 * cm))
        MD.append('')

        # --- B. CE -> ejercicio
        S.append(Paragraph('B. Con qué ejercicio se demuestra cada criterio', E['h2']))
        MD.append('### B. Con qué ejercicio se demuestra cada criterio\n')
        MD.append('| CE | Enunciado del criterio (RD 628/2013) | Ejercicios |')
        MD.append('|---|---|---|')
        filas = []
        for g in range(1, GRUPO[uf] + 1):
            cs = [c for c in CE[uf] if c.startswith('CE%d.' % g)]
            cs.sort(key=lambda c: int(c.split('.')[1]))
            filas.append([Paragraph('<b>C%d</b>' % g, E['tdb']),
                          Paragraph('<b>%s</b>' % CE[uf]['C%d' % g], E['tdb']), Paragraph('', E['td'])])
            MD.append('| **C%d** | **%s** | |' % (g, CE[uf]['C%d' % g]))
            for c in cs:
                cubren = m[uf][c]
                if cubren:
                    txt = ', '.join(('<b>%s</b>' if e['amp'] else '%s') % e['codigo'] for e in cubren)
                    txtmd = ', '.join(('**%s**' if e['amp'] else '%s') % e['codigo'] for e in cubren)
                else:
                    txt = '<font color="#9A2B2B"><b>sin ejercicio</b></font>'
                    txtmd = '**sin ejercicio**'
                filas.append([Paragraph('<b>%s</b>' % c, E['tdb']),
                              Paragraph(CE[uf][c], E['tdce']),
                              Paragraph(txt, E['td'])])
                MD.append('| %s | %s | %s |' % (c, CE[uf][c], txtmd))
        S.append(tabla(['CE', 'Enunciado del criterio (RD 628/2013, anexo IV)', 'Ejercicios'],
                       filas, [1.5 * cm, 11.7 * cm, 3.2 * cm]))
        S.append(Spacer(1, 0.25 * cm))
        S.append(Paragraph('Los códigos en <b>negrita</b> son ejercicios de ampliación; el resto '
                           'son los del cuaderno original.', E['nota']))
        MD.append('\nLos códigos en **negrita** son ejercicios de ampliación.\n')
        if uf != 'UF2406':
            S.append(PageBreak())
        else:
            S.append(Spacer(1, 0.6 * cm))

    # --------------------------------------------------------------- huecos
    S.append(barra('Criterios que se quedan sin ejercicio' if huecos
                   else 'Cobertura completa: notas sobre los últimos criterios'))
    S.append(Spacer(1, 0.4 * cm))
    MD.append('\n## %s\n' % ('Criterios que se quedan sin ejercicio' if huecos
                              else 'Cobertura completa: notas sobre los últimos criterios'))
    if not huecos:
        for tit, txt in NOTAS_CIERRE:
            S.append(Paragraph(tit, E['h2']))
            S.append(Paragraph(txt, E['cuerpo']))
            MD.append('### %s\n' % tit)
            MD.append(txt + '\n')
    QUE_FALTA = {
     ('UF2404', 'CE1.1'): ('Es un criterio de «explicar»: pide situar la programación orientada a '
                           'objetos como una fase dentro del ciclo completo. El contenido existe en '
                           'el proyecto —lo recorren el ejercicio 1 y el 15 de la UF2406— pero bajo '
                           'los criterios de esa otra unidad, así que formalmente no cuenta aquí. '
                           'Se cerraría con un ejercicio corto de la UF2404 que pida recorrer el '
                           'ciclo de un caso ya resuelto (análisis, diseño, programación, pruebas) '
                           'señalando en qué fase entra la POO y qué produce cada una.'),
     ('UF2404', 'CE1.4'): ('Es el paso de mensajes: qué es un mensaje, qué lo compone (receptor, '
                           'selector, argumentos) y por qué la respuesta depende del objeto que lo '
                           'recibe y no del tipo declarado. El proyecto lo usa constantemente '
                           '—el despacho dinámico del ejercicio 3 es exactamente eso— pero ningún '
                           'ejercicio pide describirlo. Se cerraría con un ejercicio que trace, '
                           'sobre una jerarquía ya escrita, qué método se ejecuta en cada envío y '
                           'por qué, incluyendo un caso de sobrecarga (resuelta en compilación) '
                           'frente a uno de redefinición (resuelta en ejecución).'),
     ('UF2406', 'CE4.5'): ('Este no falta de verdad: lo cumple punto por punto el ejercicio 12 del '
                           'cuaderno de la UF2406, cuyos apartados 4, 5 y 6 recorren los cinco '
                           'puntos del criterio (modificar el código, dejar el histórico en la '
                           'cabecera, ajustar los programas de prueba, ejecutar la regresión y '
                           'actualizar la documentación afectada). El cuaderno original lo atribuye '
                           'solo a CE4.1 y CE4.3. Basta con añadir CE4.5 a la cabecera de ese '
                           'ejercicio en el cuaderno para que la cobertura pase de 65 a 66.'),
    }
    for uf, c in huecos:
        S.append(Paragraph('%s · %s' % (uf, c), E['h2']))
        S.append(Paragraph('<i>%s</i>' % CE[uf][c], E['cuerpo']))
        S.append(Paragraph(QUE_FALTA[(uf, c)], E['cuerpo']))
        MD.append('### %s · %s\n' % (uf, c))
        MD.append('> %s\n' % CE[uf][c])
        MD.append(QUE_FALTA[(uf, c)] + '\n')

    S.append(Spacer(1, 0.2 * cm))
    nota = ('Nota sobre la fuente. El texto de los criterios está transcrito del PDF oficial del '
            'Real Decreto 628/2013 (BOE núm. 225, de 19 de septiembre de 2013), anexo IV. Ese PDF '
            'incrusta parte del articulado con una fuente sin tabla de correspondencia Unicode, de '
            'modo que treinta de los sesenta y ocho criterios se extraen como caracteres de '
            'control. Se han recuperado reconstruyendo la codificación glifo a glifo y '
            'contrastando el resultado con los párrafos legibles del mismo texto; los treinta son '
            'CE1.2, CE1.3, CE1.4, CE1.5, CE1.7, CE1.8, CE2.4, CE2.5 y CE2.10 de la UF2404; '
            'CE2.5 y CE2.6 de la UF2405; y CE1.2, CE1.4, CE1.5, CE2.3, CE2.6, CE3.4, CE3.8, '
            'CE4.1 a CE4.6, CE5.2 y CE5.4 a CE5.8 de la UF2406. Los treinta y ocho restantes se '
            'han comparado carácter a carácter con el BOE y coinciden. Los apartados con guion del '
            'original se resumen aquí entre corchetes. Antes de usar este documento como '
            'justificación formal conviene cotejar con el BOE los treinta reconstruidos.')
    S.append(Paragraph(nota, E['nota']))
    MD.append('\n---\n\n' + nota + '\n')

    doc.build(S)
    io.open(ruta_md, 'w', encoding='utf-8').write('\n'.join(MD))
    print('PDF generado:', ruta_pdf)
    print('MD  generado:', ruta_md)


if __name__ == '__main__':
    aqui = os.path.dirname(os.path.abspath(__file__))
    construir(os.path.join(aqui, 'Matriz_Criterios_Evaluacion_MF0227_3.pdf'),
              os.path.join(aqui, 'MATRIZ_CE.md'))
