package com.ifcd0112.ejercicios.uf2405;

import com.ifcd0112.ejercicios.uf2405.bloque1_ddl.Bloque1Ddl;
import com.ifcd0112.ejercicios.uf2405.bloque2_dml.Bloque2Dml;
import com.ifcd0112.ejercicios.uf2405.bloque3_jdbc.Bloque3Jdbc;
import com.ifcd0112.ejercicios.uf2405.bloque4_web.Bloque4Web;
import com.ifcd0112.ejercicios.uf2405.bloque5_integrador.Bloque5Integrador;

/**
 * Runner de la UF2405: Modelo de programacion web y bases de datos.
 *
 * <p>Ejecuta los 14 ejercicios del cuaderno Ejercicios_UF2405_Web_BBDD.docx,
 * agrupados en sus cinco bloques.</p>
 *
 * <p><b>Nota importante.</b> Buena parte de esta unidad formativa no produce
 * codigo Java ejecutable, sino scripts SQL, paginas HTML, Servlets y JSP. Esos
 * artefactos estan resueltos y comentados en la carpeta {@code recursos/uf2405/},
 * y cada fichero .java de aqui conserva el enunciado, el razonamiento que se
 * evalua y la ruta exacta del artefacto correspondiente. Lo que SI se ejecuta
 * de verdad al lanzar esta clase: la gestion de errores de JDBC (ejercicio 5),
 * la demostracion completa de la inyeccion SQL (ejercicio 6), el ciclo CRUD del
 * patron DAO (ejercicio 7), el commit y el rollback de una transaccion
 * (ejercicio 8), la validacion del servidor (ejercicio 11), el escapado frente
 * a XSS (ejercicio 10) y el aislamiento entre sesiones (ejercicio 12).</p>
 *
 * @author Solucionario IFCD0112 - MF0227_3
 * @version 1.0
 */
public class UF2405Ejercicios {

    /** Ejecuta los cinco bloques de la unidad formativa. */
    public static void ejecutar() {
        System.out.println();
        System.out.println("##################################################");
        System.out.println(" UF2405 - MODELO DE PROGRAMACION WEB Y BASES DE DATOS");
        System.out.println(" 14 ejercicios en 5 bloques");
        System.out.println("##################################################");
        System.out.println();
        Bloque1Ddl.ejecutar();
        Bloque2Dml.ejecutar();
        Bloque3Jdbc.ejecutar();
        Bloque4Web.ejecutar();
        Bloque5Integrador.ejecutar();
    }

    /** Permite ejecutar la unidad formativa completa de forma aislada. */
    public static void main(String[] args) {
        ejecutar();
    }
}
