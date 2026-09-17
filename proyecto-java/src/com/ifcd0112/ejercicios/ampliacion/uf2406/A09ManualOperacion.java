package com.ifcd0112.ejercicios.ampliacion.uf2406;

import java.util.Arrays;
import java.util.List;

/**
 * AMPLIACION UF2406 - EJERCICIO A9: Manual de operacion y mantenimiento.
 *
 * <p>Criterios de evaluacion: CE3.5, CE3.9</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class A09ManualOperacion {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * El sistema del gimnasio se entrega y alguien tiene que MANTENERLO
     * funcionando: instalarlo, actualizarlo, hacer copias de seguridad y
     * resolver la llamada del sabado por la tarde cuando no arranca. Esa persona
     * no eres tu, no ha visto el codigo y probablemente no sepa Java.
     *
     * Se pide:
     *   1. Propon el indice (plantilla) del manual de operacion y mantenimiento,
     *      tambien llamado manual tecnico.
     *   2. Explica a quien va dirigido y en que se diferencia del manual de
     *      usuario y del Javadoc. Los tres son documentacion y ninguno sustituye
     *      a los otros dos.
     *   3. Elabora el manual completo del sistema del gimnasio siguiendo tu
     *      plantilla.
     *   4. Redacta con detalle el apartado de resolucion de incidencias, con al
     *      menos cinco sintomas reales, su causa probable y su solucion.
     *   5. Redacta el apartado de copias de seguridad indicando que se copia,
     *      cada cuanto, donde y, sobre todo, como se comprueba que la copia
     *      sirve.
     *   6. Escribe el procedimiento de vuelta atras: que hacer si una version
     *      nueva sale mal en produccion un viernes.
     *   7. Explica que informacion NO debe aparecer nunca en este manual.
     *
     * Pista: escribe cada procedimiento como si lo fuera a seguir alguien a las
     * tres de la madrugada, con prisa y sin poder preguntarte. Eso descarta las
     * frases del tipo "configurar adecuadamente el servidor".
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * DONDE ESTA EL MANUAL COMPLETO DEL APARTADO 3.
     *
     *      recursos/ampliacion/MANUAL_OPERACION_gimnasio.md
     *
     * APARTADO 2: los tres documentos, y por que ninguno sustituye a otro.
     *
     *                 JAVADOC          MANUAL DE USUARIO   MANUAL TECNICO
     *   Destinatario  quien programa   el recepcionista    quien administra
     *   Pregunta      como llamo a     como doy de alta    por que no arranca
     *                 este metodo      a un socio          y como lo arreglo
     *   Se lee        programando      el primer dia       cuando algo falla
     *   Si falta      se lee el fuente se llama a soporte  se llama al
     *                                                      programador. Un
     *                                                      domingo.
     *
     * La confusion habitual es creer que el manual tecnico es "el manual de
     * usuario con mas detalle". No lo es: cambia el destinatario y cambia la
     * pregunta. Al recepcionista no le importa donde estan los registros; a
     * quien administra no le importa como se da de alta un socio.
     *
     * APARTADO 7: que NO debe aparecer nunca.
     *
     *   - CONTRASENAS, claves de API y certificados. Nunca, bajo ningun
     *     concepto. El manual se comparte, se imprime, se envia por correo y
     *     acaba en el escritorio de alguien. Se indica DONDE estan las
     *     credenciales y quien las custodia, no cuales son.
     *   - Direcciones IP internas y nombres de servidores con detalle
     *     innecesario, si el documento puede salir de la organizacion.
     *   - Datos reales de personas en los ejemplos. Ni siquiera "de prueba":
     *     los ejemplos se copian, y un DNI real en un manual acaba en un
     *     sistema de pruebas. Ademas de ser mala idea, incumple la proteccion
     *     de datos.
     *   - Explicaciones de por que el codigo esta hecho asi. Eso es el
     *     documento de diseno, y aqui solo estorba.
     */

    /** Ruta del manual elaborado en el apartado 3. */
    public static final String MANUAL = "recursos/ampliacion/MANUAL_OPERACION_gimnasio.md";

    /**
     * Apartado 1: el indice del manual tecnico.
     *
     * @return los apartados en orden
     */
    public static List<String> plantilla() {
        return Arrays.asList(
            "1. Identificacion del sistema y de esta version",
            "2. Arquitectura desplegada: que se instala y en que maquina",
            "3. Requisitos previos: sistema operativo, Java, base de datos, puertos",
            "4. Instalacion desde cero, paso a paso",
            "5. Configuracion: cada parametro, su valor por defecto y su efecto",
            "6. Puesta en marcha y parada del servicio",
            "7. Comprobacion de que el sistema esta sano tras arrancar",
            "8. Copias de seguridad: que, cuando, donde y como se verifica",
            "9. Procedimiento de restauracion",
            "10. Actualizacion a una version nueva",
            "11. Procedimiento de vuelta atras",
            "12. Registros: donde estan, que significan y cuanto se conservan",
            "13. Tareas periodicas de mantenimiento",
            "14. Resolucion de incidencias: sintoma, causa probable, solucion",
            "15. Escalado: a quien avisar, con que datos y en que plazo",
            "16. Historial de revisiones del manual");
    }

    /**
     * Incidencia documentada.
     *
     * @param sintoma  lo que ve quien administra
     * @param causa    causa mas probable
     * @param solucion que hacer, en concreto
     */
    public record Incidencia(String sintoma, String causa, String solucion) {
    }

    /**
     * Apartado 4: incidencias reales de este sistema.
     *
     * <p>Todas salen de fallos que aparecen de verdad en los ejercicios de las
     * tres unidades formativas, no de un catalogo generico.</p>
     *
     * @return las incidencias documentadas
     */
    public static List<Incidencia> incidencias() {
        return Arrays.asList(
            new Incidencia(
                "Al arrancar: No suitable driver found for jdbc:mysql://...",
                "Falta el conector de MySQL en el classpath del servidor",
                "Copiar mysql-connector-j.jar en TOMCAT/lib y reiniciar. Comprobar con "
                + "ls TOMCAT/lib | grep mysql"),
            new Incidencia(
                "SQLException con SQLState 28000 al arrancar",
                "Usuario o contrasena de la base de datos incorrectos",
                "Revisar las credenciales en context.xml. NO estan en este manual: las "
                + "custodia el responsable de sistemas"),
            new Incidencia(
                "La aplicacion responde, pero los acentos salen como simbolos raros",
                "Falta la codificacion en la URL de conexion o en la respuesta",
                "Anadir ?useUnicode=true&characterEncoding=UTF-8 a la URL y comprobar que "
                + "los Servlets llaman a setContentType antes de escribir nada"),
            new Incidencia(
                "Tras unas horas, las peticiones se quedan colgadas y no responde nada",
                "Agotamiento del pool: alguna conexion no se devuelve",
                "Reiniciar como medida inmediata y abrir incidencia. La causa suele ser un "
                + "try sin try-with-resources: hay que localizarlo, no solo reiniciar"),
            new Incidencia(
                "Unknown column 'ultima_visita' in 'field list' al registrar una consulta",
                "La base de datos no tiene aplicado el script de evolucion del esquema",
                "Ejecutar 02_evolucion_esquema.sql. Comprobar antes con DESCRIBE mascota"),
            new Incidencia(
                "El disco del servidor se llena cada pocas semanas",
                "Los registros no se rotan",
                "Configurar la rotacion a 30 dias. Mientras tanto, comprobar el tamano de "
                + "TOMCAT/logs antes de borrar nada"));
    }

    // =====================================================================
    // COMPROBACION
    // =====================================================================

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[AMP-UF2406-A9] Manual de operacion y mantenimiento");

        System.out.println("  Apartado 1, indice del manual tecnico:");
        for (String apartado : plantilla()) {
            System.out.println("    " + apartado);
        }
        System.out.println("  Apartado 3, manual completo: " + MANUAL);

        System.out.println("  Apartado 4, resolucion de incidencias (extracto):");
        for (Incidencia i : incidencias()) {
            System.out.println("    SINTOMA : " + i.sintoma());
            System.out.println("      causa : " + i.causa());
            System.out.println("      accion: " + i.solucion());
        }
        System.out.println("    Las seis salen de fallos que aparecen de verdad en los ejercicios");
        System.out.println("    de las tres unidades: el driver que falta (UF2405 ej. 5), los");
        System.out.println("    acentos (ej. 10), el pool agotado (ej. 14) y la columna que no");
        System.out.println("    existe (el desajuste entre los ejercicios 2 y 8).");

        System.out.println("  Apartado 5, copias de seguridad. Lo que casi nadie escribe:");
        System.out.println("    QUE      la base de datos completa, los ficheros subidos y la");
        System.out.println("             configuracion. El codigo NO: ese esta en el repositorio.");
        System.out.println("    CUANDO   copia completa diaria a las 03:00 y registro de");
        System.out.println("             transacciones cada hora.");
        System.out.println("    DONDE    en otra maquina y, ademas, fuera del edificio. Una copia");
        System.out.println("             en el mismo servidor no es una copia de seguridad.");
        System.out.println("    CUANTO   30 dias diarias, 12 meses mensuales.");
        System.out.println("    COMO SE COMPRUEBA que sirve: restaurandola. Una vez al trimestre,");
        System.out.println("             en una maquina aparte, y anotando el resultado. Una copia");
        System.out.println("             que nunca se ha restaurado no es una copia: es un fichero");
        System.out.println("             grande del que nadie sabe nada.");

        System.out.println("  Apartado 6, vuelta atras, redactada para las tres de la madrugada:");
        System.out.println("    1. Parar el servicio.");
        System.out.println("    2. Desplegar el .war de la version anterior, que se conserva en");
        System.out.println("       versiones/ junto a su fecha.");
        System.out.println("    3. Si la version nueva cambio la base de datos, aplicar el script");
        System.out.println("       de reversion correspondiente. Si no existe, restaurar la copia");
        System.out.println("       previa al despliegue.");
        System.out.println("    4. Arrancar y ejecutar la comprobacion de salud del apartado 7.");
        System.out.println("    5. Avisar al responsable, con la hora y el motivo.");
        System.out.println("    La decision clave se toma ANTES: cada despliegue debe dejar");
        System.out.println("    preparada su vuelta atras. Improvisarla el viernes por la noche es");
        System.out.println("    como se pierden los datos.");

        System.out.println("  Apartado 7: en este manual NUNCA van contrasenas ni claves, ni datos");
        System.out.println("    reales de personas en los ejemplos, ni explicaciones de por que el");
        System.out.println("    codigo esta hecho asi. Se indica DONDE estan las credenciales y");
        System.out.println("    quien las custodia, no cuales son.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
