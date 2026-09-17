package com.ifcd0112.ejercicios.ampliacion.uf2406;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * AMPLIACION UF2406 - EJERCICIO A7: Plantilla y documento de diseno de una clase.
 *
 * <p>Criterios de evaluacion: CE3.3, CE3.7</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class A07DocumentoDiseno {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * El Javadoc del ejercicio 11 documenta el codigo YA ESCRITO. El documento
     * de diseno es lo contrario: se escribe ANTES, y sirve para que alguien
     * pueda programar la clase sin volver a preguntar.
     *
     * Se pide:
     *   1. Propon el indice (plantilla) del documento de diseno de una clase,
     *      explicando que contiene cada apartado y por que hace falta.
     *   2. Justifica cada apartado respondiendo a esta pregunta: que decision
     *      tendria que tomar por su cuenta quien programe la clase si ese
     *      apartado no estuviera?
     *   3. Elabora el documento completo para la clase GestorInscripciones del
     *      sistema del gimnasio, siguiendo tu propia plantilla.
     *   4. Escribe un comprobador que verifique que un documento concreto
     *      contiene todos los apartados obligatorios de la plantilla y senale
     *      los que faltan.
     *   5. Explica en que se diferencia este documento del Javadoc y del manual
     *      de usuario: destinatario, momento en que se escribe y que pasa si no
     *      existe.
     *   6. Indica que apartados de tu plantilla quedan obsoletos en cuanto el
     *      codigo cambia, y propon como evitar que el documento acabe mintiendo.
     *
     * Pista: un documento de diseno que solo repite los nombres de los metodos
     * no sirve para nada, porque eso ya lo dice el codigo. Lo que aporta valor
     * es todo lo que el codigo NO puede decir: por que se eligio ese diseno, que
     * alternativas se descartaron y que pasa en los casos raros.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * DONDE ESTA EL DOCUMENTO COMPLETO DEL APARTADO 3.
     *
     *      recursos/ampliacion/DISENO_GestorInscripciones.md
     *
     * APARTADO 5: tres documentos que se confunden constantemente.
     *
     *                  DOC. DE DISENO      JAVADOC            MANUAL DE USUARIO
     *   Destinatario   quien programa      quien USA la clase el usuario final
     *   Momento        ANTES de programar  a la vez           al terminar
     *   Responde a     como hay que        como se llama a    como hago mi
     *                  construirla         esto               trabajo
     *   Si no existe   se toman decisiones hay que leer el    el usuario llama
     *                  por libre y cada    fuente para usar   a soporte por
     *                  uno la suya         una clase          todo
     *
     * La diferencia decisiva es el MOMENTO. El documento de diseno se escribe
     * antes y por eso puede evitar trabajo tirado a la basura; el Javadoc se
     * escribe a la vez y evita lecturas del fuente; el manual se escribe al
     * final y evita llamadas a soporte. Los tres ahorran tiempo, pero en
     * momentos distintos y a personas distintas.
     *
     * APARTADO 6: los apartados que envejecen, y como evitarlo.
     *
     * Envejecen mal los que DUPLICAN informacion que ya esta en el codigo: la
     * lista de metodos, las firmas, los tipos de los atributos. En cuanto se
     * anade un parametro, el documento miente.
     *
     * Envejecen bien los que el codigo no puede contener: las decisiones de
     * diseno y sus alternativas descartadas, las reglas de negocio con su
     * origen, los supuestos y las restricciones.
     *
     * Tres formas de evitar que mienta, de menos a mas efectiva:
     *   1. No duplicar. Si el dato esta en el codigo, se enlaza, no se copia.
     *   2. Fechar y versionar cada apartado, para que se vea lo viejo que es.
     *   3. Incluir la revision del documento en la definicion de terminado de
     *      cada cambio, como se hace en el ejercicio 12. Es lo unico que
     *      funciona de verdad: mientras actualizar el documento sea opcional,
     *      no se actualiza.
     */

    /** Apartado del documento de diseno. */
    public record Apartado(String numero, String titulo, boolean obligatorio, String justificacion) {
    }

    /**
     * Apartados 1 y 2: la plantilla, con la justificacion de cada apartado.
     *
     * @return los apartados de la plantilla en orden
     */
    public static List<Apartado> plantilla() {
        return Arrays.asList(
            new Apartado("1", "Identificacion", true,
                "sin nombre, paquete, version y autor no se sabe de que clase se habla"),
            new Apartado("2", "Proposito y responsabilidad unica", true,
                "si no esta escrito en una frase, la clase acabara haciendo tres cosas"),
            new Apartado("3", "Contexto: capa y colaboradores", true,
                "quien programe decidiria por su cuenta si puede llamar al DAO o no"),
            new Apartado("4", "Interfaz publica: operaciones y contratos", true,
                "sin precondiciones y postcondiciones, cada metodo se valida donde caiga"),
            new Apartado("5", "Atributos y estructuras de datos elegidas", true,
                "sin esto se elige la coleccion al azar y luego no rinde"),
            new Apartado("6", "Reglas de negocio implementadas", true,
                "es la parte que NO se puede deducir del codigo: de donde sale el limite de 3"),
            new Apartado("7", "Excepciones y tratamiento de errores", true,
                "sin acuerdo previo, unos metodos devuelven null y otros lanzan excepcion"),
            new Apartado("8", "Decisiones de diseno y alternativas descartadas", true,
                "evita que dentro de un ano alguien deshaga una decision sin saber por que se tomo"),
            new Apartado("9", "Diagramas: clases y secuencia", false,
                "acelera la comprension, pero para una clase pequena puede sobrar"),
            new Apartado("10", "Requisitos que satisface (trazabilidad)", true,
                "sin esto no se puede saber que se rompe al cambiar un requisito"),
            new Apartado("11", "Criterios de aceptacion y pruebas previstas", true,
                "define QUE significa que la clase esta terminada"),
            new Apartado("12", "Historial de revisiones", true,
                "sin fecha, nadie sabe si el documento describe la version actual"));
    }

    /**
     * Apartado 4: comprueba que un documento contiene los apartados obligatorios.
     *
     * @param apartadosDelDocumento titulos presentes en el documento
     * @return apartados obligatorios que faltan
     */
    public static List<String> apartadosQueFaltan(List<String> apartadosDelDocumento) {
        List<String> faltan = new ArrayList<>();
        for (Apartado a : plantilla()) {
            if (a.obligatorio() && !apartadosDelDocumento.contains(a.titulo())) {
                faltan.add(a.numero() + ". " + a.titulo());
            }
        }
        return faltan;
    }

    // =====================================================================
    // COMPROBACION
    // =====================================================================

    /** Ruta del documento elaborado en el apartado 3. */
    public static final String DOCUMENTO = "recursos/ampliacion/DISENO_GestorInscripciones.md";

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[AMP-UF2406-A7] Plantilla y documento de diseno de una clase");

        System.out.println("  Apartados 1 y 2, la plantilla y por que hace falta cada apartado:");
        for (Apartado a : plantilla()) {
            System.out.printf("    %-3s %-42s %s%n", a.numero() + ".",
                    a.titulo() + (a.obligatorio() ? "" : " (opcional)"), a.justificacion());
        }

        System.out.println("  Apartado 3, documento completo de GestorInscripciones:");
        System.out.println("    " + DOCUMENTO);

        // Apartado 4: el comprobador, sobre dos documentos de ejemplo.
        System.out.println("  Apartado 4, comprobador de la plantilla:");

        List<String> documentoCompleto = new ArrayList<>();
        for (Apartado a : plantilla()) {
            documentoCompleto.add(a.titulo());
        }
        System.out.println("    Documento de GestorInscripciones -> faltan "
                + apartadosQueFaltan(documentoCompleto).size() + " apartados obligatorios");

        // Un documento tipico a medio hacer: el que solo repite lo que ya dice
        // el codigo, que es justo lo que advierte la pista.
        List<String> documentoIncompleto = Arrays.asList(
                "Identificacion",
                "Proposito y responsabilidad unica",
                "Interfaz publica: operaciones y contratos",
                "Atributos y estructuras de datos elegidas");
        System.out.println("    Documento entregado por un alumno -> faltan:");
        for (String f : apartadosQueFaltan(documentoIncompleto)) {
            System.out.println("      " + f);
        }
        System.out.println("    Fijate en cuales son: los cuatro que SI estan son los que se");
        System.out.println("    pueden copiar del codigo. Los que faltan son precisamente los que");
        System.out.println("    habria que pensar, que es de lo que trata el ejercicio.");

        System.out.println("  Apartado 5: el diseno se escribe ANTES y dice como construirla; el");
        System.out.println("    Javadoc se escribe a la vez y dice como usarla; el manual se");
        System.out.println("    escribe al final y dice como trabajar con la aplicacion. Tres");
        System.out.println("    destinatarios y tres momentos distintos.");
        System.out.println("  Apartado 6: envejecen mal los apartados que DUPLICAN el codigo");
        System.out.println("    (firmas, tipos); envejecen bien los que el codigo no puede");
        System.out.println("    contener (por que se decidio asi, que se descarto). La unica");
        System.out.println("    medida que funciona es incluir la revision del documento en la");
        System.out.println("    definicion de terminado de cada cambio.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
