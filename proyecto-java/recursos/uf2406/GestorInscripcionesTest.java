/*
 * UF2406 - BLOQUE 4 - EJERCICIO 10
 * Pruebas con dependencias simuladas usando Mockito.
 *
 * ESTE FICHERO NO FORMA PARTE DE LA COMPILACION DEL PROYECTO: necesita
 * junit-jupiter y mockito-core. La misma bateria, con los simulados escritos a
 * mano y sin dependencias, esta en
 * src/com/ifcd0112/ejercicios/uf2406/bloque4_pruebas/Ej10Mocks.java
 */

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GestorInscripcionesTest {

    /*
     * Apartado 1: el gestor recibe el repositorio por constructor.
     * Apartado 2: depende de la INTERFAZ RepositorioClases, no del DAO concreto.
     *
     * Es la D de SOLID, inversion de dependencias: negocio y datos dependen los
     * dos de la abstraccion, y la abstraccion pertenece conceptualmente al
     * negocio, que es quien dice lo que necesita.
     */

    @Test
    void inscribeCorrectamenteSiHayPlazasLibres() {
        RepositorioClases repo = mock(RepositorioClases.class);
        ClaseColectiva clase = new ClaseColectiva("YOGA-1", 20, 0);
        when(repo.buscarPorCodigo("YOGA-1")).thenReturn(clase);

        GestorInscripciones gestor = new GestorInscripciones(repo);
        Inscripcion i = gestor.inscribir("11111111A", "YOGA-1");

        assertNotNull(i);
        verify(repo, times(1)).guardar(any(Inscripcion.class));   // se guardo una vez
    }

    @Test
    void lanzaExcepcionSiLaClaseEstaCompleta() {
        RepositorioClases repo = mock(RepositorioClases.class);
        ClaseColectiva completa = mock(ClaseColectiva.class);
        when(completa.estaCompleta()).thenReturn(true);
        when(repo.buscarPorCodigo("PILATES-2")).thenReturn(completa);

        GestorInscripciones gestor = new GestorInscripciones(repo);

        assertThrows(ClaseCompletaException.class,
                     () -> gestor.inscribir("11111111A", "PILATES-2"));

        // Tan importante como que lance la excepcion: que NO haya guardado nada.
        // Sin esta linea, un gestor que guardase y despues fallase pasaria la
        // prueba dejando basura en la base de datos.
        verify(repo, never()).guardar(any());
    }
}
