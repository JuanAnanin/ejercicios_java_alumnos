# Guía de usuario — Pantalla de inscripción a clases

Ejercicio A12 de la ampliación (CE5.8). Destinatario: **el personal de recepción
del gimnasio**. No hace falta saber nada de informática para leerla.

---

## 1. Para qué sirve esta pantalla

Para apuntar a un socio a una clase colectiva. En una sola pantalla se elige a la
persona, se elige la clase y se confirma.

## 2. Antes de empezar

Necesitas saber el nombre o el DNI del socio. Si el socio no aparece en la lista,
es que no está dado de alta: hay que darlo de alta primero desde la pantalla de
socios.

## 3. Cómo inscribir a un socio, paso a paso

1. **Elige el socio** en la lista de arriba. Puedes escribir las primeras letras
   del nombre para encontrarlo antes.
2. **Elige la clase** en la lista del medio. Junto a cada clase verás cuántas
   plazas quedan libres.
3. **Pulsa Inscribir**. También puedes pulsar Intro, o Alt+I.
4. Debajo aparecerá el resultado.

> **El botón está apagado y no puedo pulsarlo.** Es normal: se enciende cuando
> has elegido un socio *y* una clase que tenga plazas. Si sigue apagado, mira las
> plazas libres de la clase que has elegido.

## 4. Qué significa cada mensaje

| Mensaje | Qué ha pasado | Qué hacer |
|---|---|---|
| `[OK] Inscripción realizada correctamente` | Ya está apuntado | Nada más |
| `[...] Procesando inscripción...` | Se está guardando | Esperar un momento |
| `[ERROR] Esa clase ya no tiene plazas disponibles` | Alguien ocupó la última plaza justo antes | Ofrecer otro horario, o apuntarlo a la lista de espera |
| `[ERROR] El socio tiene cuotas pendientes de pago` | Debe alguna mensualidad | Registrar el pago en la pantalla de cuotas y volver aquí |
| `[ERROR] No se ha podido completar la inscripción` | Un problema del sistema | Volver a intentarlo en unos minutos. Si sigue, avisar a soporte |

## 5. La lista de espera

Cuando una clase está completa, el socio puede quedarse en lista de espera. Si
alguien se da de baja, **el primero que esté esperando pasa automáticamente** a
tener plaza y hay que avisarle.

## 6. Preguntas frecuentes

**¿Puedo apuntar a alguien a dos clases a la vez?** Sí, no hay límite de clases
por socio.

**¿Cómo cancelo una inscripción?** Desde la pantalla de socios, en su ficha.
*(De momento no se puede deshacer desde aquí: está previsto añadirlo.)*

**¿Por qué no veo a un socio en la lista?** O no está dado de alta, o está de
baja. Compruébalo en la pantalla de socios.

## 7. Si algo va mal

Anota **qué estabas haciendo**, **el nombre del socio** y **la hora**, y avísalo.
Con esos tres datos se localiza el problema; sin ellos, casi nunca.

---

### Nota sobre esta guía

Compárese con el Javadoc del mismo sistema: aquí no aparece la palabra
*excepción*, ni *base de datos*, ni el nombre de ninguna clase. Está organizada
**por tareas del usuario** y no por la estructura del software. Son dos
documentos distintos para dos personas distintas, y ninguno sustituye al otro.
