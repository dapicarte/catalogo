package BookPoint.catalogo.service;

import BookPoint.catalogo.model.Producto;
import BookPoint.catalogo.model.Resenia;
import BookPoint.catalogo.repository.ReseniaRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReseniaServiceTest {

    @Mock
    private ReseniaRepository reseniaRepository;

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ReseniaService reseniaService;

    private Resenia crearResenia(Long id, String comentario, Integer calificacion) {
        Resenia r = new Resenia();
        r.setIdReseña(id);
        r.setComentario(comentario);
        r.setCalificacion(calificacion);
        return r;
    }

    @Test
    void testRegistrarReseniaProductoExistente() {
        Resenia resenia = crearResenia(null, "Excelente", 5);
        Resenia guardada = crearResenia(1L, "Excelente", 5);

        Producto producto = new Producto();
        producto.setIdProducto(10L);

        when(productoService.findByIdProducto(10L)).thenReturn(Optional.of(producto));
        when(reseniaRepository.save(any(Resenia.class))).thenReturn(guardada);

        Resenia resultado = reseniaService.registrarReseña(10L, resenia);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdReseña());
        assertEquals("Excelente", resultado.getComentario());

        verify(productoService, times(1)).findByIdProducto(10L);
        verify(reseniaRepository, times(1)).save(any(Resenia.class));
    }

    @Test
    void testRegistrarReseniaProductoNoExistente() {
        Resenia resenia = crearResenia(null, "Excelente", 5);

        when(productoService.findByIdProducto(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> reseniaService.registrarReseña(99L, resenia));

        assertEquals("Producto no encontrado", exception.getMessage());

        verify(productoService, times(1)).findByIdProducto(99L);
        verify(reseniaRepository, never()).save(any(Resenia.class));
    }

    @Test
    void testListarResenias() {
        Resenia r1 = crearResenia(1L, "Buena", 4);
        Resenia r2 = crearResenia(2L, "Mala", 1);

        when(reseniaRepository.findAll()).thenReturn(Arrays.asList(r1, r2));

        List<Resenia> resultado = reseniaService.listarResenias();

        assertEquals(2, resultado.size());
        assertEquals("Buena", resultado.get(0).getComentario());
        assertEquals("Mala", resultado.get(1).getComentario());

        verify(reseniaRepository, times(1)).findAll();
    }

    @Test
    void testEliminarReseniaExistente() {
        when(reseniaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(reseniaRepository).deleteById(1L);

        boolean resultado = reseniaService.eliminarResenia(1L);

        assertTrue(resultado);

        verify(reseniaRepository, times(1)).existsById(1L);
        verify(reseniaRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarReseniaNoExistente() {
        when(reseniaRepository.existsById(99L)).thenReturn(false);

        boolean resultado = reseniaService.eliminarResenia(99L);

        assertFalse(resultado);

        verify(reseniaRepository, times(1)).existsById(99L);
        verify(reseniaRepository, never()).deleteById(anyLong());
    }

    @Test
    void testActualizarReseniaExistente() {
        Resenia existente = crearResenia(1L, "Buena", 4);
        Resenia datosNuevos = crearResenia(null, "Excelente", 5);
        Resenia actualizada = crearResenia(1L, "Excelente", 5);

        when(reseniaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(reseniaRepository.save(existente)).thenReturn(actualizada);

        Resenia resultado = reseniaService.actualizarResenia(1L, datosNuevos);

        assertNotNull(resultado);
        assertEquals("Excelente", existente.getComentario());
        assertEquals(5, existente.getCalificacion());

        verify(reseniaRepository, times(1)).findById(1L);
        verify(reseniaRepository, times(1)).save(existente);
    }

    @Test
    void testActualizarReseniaNoExistente() {
        Resenia datosNuevos = crearResenia(null, "Excelente", 5);

        when(reseniaRepository.findById(99L)).thenReturn(Optional.empty());

        Resenia resultado = reseniaService.actualizarResenia(99L, datosNuevos);

        assertNull(resultado);

        verify(reseniaRepository, times(1)).findById(99L);
        verify(reseniaRepository, never()).save(any(Resenia.class));
    }
}