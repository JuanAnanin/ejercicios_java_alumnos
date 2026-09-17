package com.ifcd0112.ejercicios.ampliacion.uf2404;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.ifcd0112.ejercicios.uf2404.bloque5_integrador.Ej13Videoclub;

/**
 * AMPLIACION UF2404 - EJERCICIO A4: Analisis de un programa ajeno.
 *
 * <p>Criterios de evaluacion: CE2.5, CE2.6, CE1.8</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class A04AnalisisDeCodigo {

    /* =====================================================================
     * ENUNCIADO
     * ---------------------------------------------------------------------
     * Programar es solo la mitad del oficio: la otra mitad es leer codigo que
     * ha escrito otra persona. En un trabajo real se lee mucho mas de lo que se
     * escribe, y casi nunca desde el principio.
     *
     * Se te entrega el codigo del ejercicio 13 de la UF2404, el sistema del
     * videoclub, SIN su enunciado. No tienes que modificarlo: tienes que
     * entenderlo.
     *
     * Se pide:
     *   1. Localiza en el codigo cada una de las siguientes caracteristicas del
     *      paradigma orientado a objetos, indicando clase y linea:
     *      encapsulacion, herencia, polimorfismo, clase abstracta, metodo
     *      redefinido, composicion, agregacion y jerarquia de excepciones.
     *   2. Reconstruye el diagrama de clases a partir del codigo, con sus
     *      relaciones y multiplicidades, sin mirar la documentacion.
     *   3. Identifica los metodos que forman la INTERFAZ de cada clase (lo que
     *      se puede usar desde fuera) y separalos de los detalles internos.
     *      Explica que criterio has seguido.
     *   4. Deduce, solo leyendo el codigo, cuales son las tres reglas de negocio
     *      del videoclub y en que metodo esta implementada cada una.
     *   5. Utiliza las herramientas del entorno de desarrollo para responder
     *      mas deprisa a los apartados anteriores. Documenta que funcion has
     *      usado para cada cosa:
     *      - Ver la jerarquia de tipos de una clase.
     *      - Encontrar todos los usos de un metodo.
     *      - Saltar a la declaracion de un simbolo y volver.
     *      - Ver la estructura de un fichero de un vistazo.
     *      - Generar el diagrama de clases, si tu entorno lo permite.
     *   6. Escribe un informe de media pagina explicando que hace el programa,
     *      dirigido a alguien que va a modificarlo la semana que viene.
     *   7. AMPLIACION: escribe un programa que analice una clase por reflexion
     *      y responda automaticamente a parte del apartado 1.
     *
     * Pista: para entender codigo ajeno, empieza por los tipos y no por los
     * algoritmos. Saber que clases hay y como se relacionan explica el noventa
     * por ciento; el cuerpo de los metodos casi siempre se deduce despues.
     * ===================================================================== */

    // =====================================================================
    // SOLUCION
    // =====================================================================

    /*
     * APARTADO 3: como se separa la interfaz de los detalles internos.
     *
     * El criterio NO es "lo que es public". Eso es la consecuencia, no la
     * causa. El criterio es: forma parte de la interfaz aquello que un usuario
     * de la clase necesita para hacer su trabajo, y seguiria necesitando aunque
     * la implementacion cambiara por completo.
     *
     * Aplicado al videoclub:
     *
     *   INTERFAZ de Videoclub    alquilar, devolver, consultarAlquilados,
     *                            catalogoDisponible, altaPelicula, altaSocio.
     *                            Son las operaciones del negocio.
     *
     *   DETALLE INTERNO          que el catalogo sea un LinkedHashMap y los
     *                            socios un HashMap. Si manana pasaran a una base
     *                            de datos, la interfaz no cambiaria ni una coma.
     *
     * Hay un caso intermedio que conviene mirar con lupa: Articulo.setAlquilado
     * tiene visibilidad de PAQUETE, ni publica ni privada. No es un descuido: es
     * una decision de diseno para que solo el Videoclub pueda cambiar ese
     * estado. Detectar eso leyendo codigo ajeno es exactamente lo que pide el
     * ejercicio, porque la ausencia de modificador se pasa por alto con mucha
     * facilidad.
     *
     * APARTADO 4: las tres reglas de negocio, y donde viven.
     *
     *   1. Un socio no puede tener mas de 3 articulos a la vez.
     *      Videoclub.alquilar(), comprobacion previa con MAX_ALQUILERES.
     *   2. Un ejemplar solo puede estar alquilado por una persona.
     *      Videoclub.alquilar(), comprobacion de articulo.estaAlquilado().
     *   3. El precio y el plazo dependen del tipo de articulo.
     *      Pelicula.calcularPrecio() y Videojuego.calcularPrecio(), via
     *      polimorfismo desde Articulo.
     *
     * Y un detalle que solo se ve leyendo con atencion: en alquilar(), la
     * comprobacion del limite va ANTES de marcar el articulo. Si fuera al reves,
     * una excepcion dejaria el ejemplar bloqueado sin que nadie lo tuviera. El
     * orden de dos lineas es aqui una decision de correccion, no de estilo.
     *
     * APARTADO 5: que herramienta del entorno sirve para cada cosa.
     *
     *   Jerarquia de tipos        IntelliJ Ctrl+H, Eclipse F4, VS Code "Show
     *                             Type Hierarchy". Responde al apartado 1 sobre
     *                             la herencia en dos segundos.
     *   Buscar todos los usos     Alt+F7 en IntelliJ, Ctrl+Shift+G en Eclipse,
     *                             Shift+F12 en VS Code. Imprescindible antes de
     *                             tocar un metodo: dice a quien vas a romper.
     *   Ir a la declaracion       Ctrl+clic, y Alt+flecha izquierda para volver.
     *                             Es el gesto que mas se repite leyendo codigo.
     *   Estructura del fichero    Ctrl+F12 en IntelliJ, Outline en Eclipse y en
     *                             VS Code. Da el indice de una clase larga sin
     *                             recorrerla entera.
     *   Diagrama de clases        IntelliJ Ultimate lo genera con Ctrl+Alt+U;
     *                             en Eclipse hace falta un complemento como
     *                             ObjectAid o PlantUML.
     *   Extraer un metodo         Ctrl+Alt+M. Util para el ejercicio 14: permite
     *                             refactorizar sin cambiar el comportamiento,
     *                             porque lo hace la herramienta y no la mano.
     *   Renombrar con seguridad   Shift+F6. Cambia el nombre en las decenas de
     *                             sitios donde se usa, sin buscar y reemplazar,
     *                             que es como se rompen los proyectos.
     *
     * Ese es el sentido del CE2.6: el entorno de desarrollo no es un editor con
     * colorines, es una herramienta de ANALISIS. Quien no la usa lee codigo
     * ajeno cinco veces mas despacio.
     */

    /**
     * Apartado 7: resultado del analisis automatico de una clase.
     *
     * @param nombre         nombre simple de la clase analizada
     * @param esAbstracta    si es una clase abstracta
     * @param superclase     nombre de su superclase directa
     * @param interfaces     interfaces que implementa
     * @param atributos      numero total de atributos
     * @param atributosPriv  atributos declarados privados
     * @param metodosPub     metodos publicos
     * @param redefinidos    metodos que redefinen uno de la superclase
     * @param colecciones    atributos que son colecciones
     */
    public record Informe(String nombre, boolean esAbstracta, String superclase,
                          List<String> interfaces, int atributos, int atributosPriv,
                          int metodosPub, List<String> redefinidos, List<String> colecciones) {

        /** @return porcentaje de atributos encapsulados */
        public int porcentajeEncapsulacion() {
            return (atributos == 0) ? 100 : (100 * atributosPriv) / atributos;
        }
    }

    /**
     * Apartado 7: analiza una clase por reflexion y reconoce en ella las
     * caracteristicas del paradigma.
     *
     * @param clase clase a examinar
     * @return el informe con lo detectado
     */
    public static Informe analizar(Class<?> clase) {
        int atributos = 0;
        int privados = 0;
        List<String> colecciones = new ArrayList<>();
        for (Field f : clase.getDeclaredFields()) {
            if (f.isSynthetic() || Modifier.isStatic(f.getModifiers())) {
                continue;   // los estaticos no describen el estado del objeto
            }
            atributos++;
            if (Modifier.isPrivate(f.getModifiers())) {
                privados++;
            }
            if (Collection.class.isAssignableFrom(f.getType())
                    || Map.class.isAssignableFrom(f.getType())) {
                colecciones.add(f.getName() + ": " + f.getType().getSimpleName());
            }
        }

        int publicos = 0;
        List<String> redefinidos = new ArrayList<>();
        for (Method m : clase.getDeclaredMethods()) {
            if (m.isSynthetic()) {
                continue;
            }
            if (Modifier.isPublic(m.getModifiers())) {
                publicos++;
            }
            // Un metodo esta redefinido si existe con la misma firma en alguna
            // superclase. Es la forma de detectar polimorfismo sin leer el
            // fuente: la anotacion @Override no esta en el bytecode.
            Class<?> padre = clase.getSuperclass();
            while (padre != null && padre != Object.class) {
                try {
                    padre.getDeclaredMethod(m.getName(), m.getParameterTypes());
                    redefinidos.add(m.getName() + " (de " + padre.getSimpleName() + ")");
                    break;
                } catch (NoSuchMethodException e) {
                    padre = padre.getSuperclass();
                }
            }
        }

        List<String> interfaces = new ArrayList<>();
        for (Class<?> i : clase.getInterfaces()) {
            interfaces.add(i.getSimpleName());
        }

        Class<?> sup = clase.getSuperclass();
        return new Informe(clase.getSimpleName(),
                Modifier.isAbstract(clase.getModifiers()),
                (sup == null) ? "-" : sup.getSimpleName(),
                interfaces, atributos, privados, publicos, redefinidos, colecciones);
    }

    // =====================================================================
    // COMPROBACION
    // =====================================================================

    /**
     * Imprime un informe.
     *
     * @param i informe a mostrar
     */
    private static void mostrar(Informe i) {
        System.out.printf("    %-28s %-9s hereda de %-16s%n",
                i.nombre(), i.esAbstracta() ? "ABSTRACTA" : "concreta", i.superclase());
        System.out.printf("      atributos %d (privados %d, %d%% encapsulado), metodos publicos %d%n",
                i.atributos(), i.atributosPriv(), i.porcentajeEncapsulacion(), i.metodosPub());
        if (!i.interfaces().isEmpty()) {
            System.out.println("      implementa: " + String.join(", ", i.interfaces()));
        }
        if (!i.redefinidos().isEmpty()) {
            System.out.println("      POLIMORFISMO, metodos redefinidos: "
                    + String.join(", ", i.redefinidos()));
        }
        if (!i.colecciones().isEmpty()) {
            System.out.println("      relacion de multiplicidad 'muchos': "
                    + String.join(", ", i.colecciones()));
        }
    }

    /** Ejecuta el ejercicio completo. Lo invoca el runner del bloque. */
    public static void resolver() {
        System.out.println("[AMP-UF2404-A4] Analisis de un programa ajeno");
        System.out.println("  Apartado 7: analizador automatico aplicado al ejercicio 13 de la");
        System.out.println("  UF2404, sin leer su fuente ni su documentacion.");

        mostrar(analizar(Ej13Videoclub.Articulo.class));
        mostrar(analizar(Ej13Videoclub.Pelicula.class));
        mostrar(analizar(Ej13Videoclub.Videojuego.class));
        mostrar(analizar(Ej13Videoclub.Socio.class));
        mostrar(analizar(Ej13Videoclub.Videoclub.class));
        mostrar(analizar(Ej13Videoclub.LimiteAlquileresException.class));

        System.out.println("  Lo que el analisis ya deja ver sin abrir el fuente:");
        System.out.println("    HERENCIA        Pelicula y Videojuego heredan de Articulo.");
        System.out.println("    ABSTRACCION     Articulo es abstracta: no se puede instanciar.");
        System.out.println("    POLIMORFISMO    las dos subclases redefinen calcularPrecio,");
        System.out.println("                    getDiasAlquiler y getTipo.");
        System.out.println("    ENCAPSULACION   el 100% de los atributos son privados.");
        System.out.println("    MULTIPLICIDAD   Socio y Videoclub tienen colecciones, luego sus");
        System.out.println("                    relaciones son de uno a muchos.");
        System.out.println("    EXCEPCIONES     LimiteAlquileresException hereda de");
        System.out.println("                    VideoclubException: hay una jerarquia propia.");
        System.out.println("  Lo que el analisis NO puede decir, y por eso hace falta leer:");
        System.out.println("    - Si la relacion es composicion o agregacion. Eso depende de QUIEN");
        System.out.println("      hace el new, y esta en el cuerpo de los metodos.");
        System.out.println("    - Las reglas de negocio. Que el limite sean 3 articulos se ve en");
        System.out.println("      la constante, pero que se compruebe ANTES de marcar el articulo");
        System.out.println("      solo se ve leyendo el orden de las lineas.");
        System.out.println("    - Por que setAlquilado tiene visibilidad de paquete. La reflexion");
        System.out.println("      ve el modificador, pero la intencion esta en el comentario.");
        System.out.println("  Esa es la conclusion del ejercicio: la herramienta acelera el mapa,");
        System.out.println("  no sustituye la lectura. Sirve para saber POR DONDE empezar a leer.");
        System.out.println();
    }

    /** Permite ejecutar este ejercicio de forma aislada. */
    public static void main(String[] args) {
        resolver();
    }
}
