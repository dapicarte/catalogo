package BookPoint.catalogo.service;

import BookPoint.catalogo.model.Categoria;
import BookPoint.catalogo.repository.CategoriaRepository;

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
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    void testCrearCategoria() {
        Categoria categoria = new Categoria(null, "Ficcion", "Libro", null);
        Categoria guardada = new Categoria(1L, "Ficcion", "Libro", null);

        when(categoriaRepository.save(categoria)).thenReturn(guardada);

        Categoria resultado = categoriaService.crearCategoria(categoria);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdCategoria());
        assertEquals("Ficcion", resultado.getNombreCategoria());
        assertEquals("Libro", resultado.getTipoProducto());

        verify(categoriaRepository, times(1)).save(categoria);
    }

    @Test
    void testListarCategorias() {
        Categoria c1 = new Categoria(1L, "Ficcion", "Libro", null);
        Categoria c2 = new Categoria(2L, "Tecnologia", "Revista", null);

        when(categoriaRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<Categoria> resultado = categoriaService.listarCategorias();

        assertEquals(2, resultado.size());
        assertEquals("Ficcion", resultado.get(0).getNombreCategoria());
        assertEquals("Tecnologia", resultado.get(1).getNombreCategoria());

        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdExistente() {
        Categoria categoria = new Categoria(1L, "Ficcion", "Libro", null);

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        Optional<Categoria> resultado = categoriaService.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getIdCategoria());
        assertEquals("Ficcion", resultado.get().getNombreCategoria());

        verify(categoriaRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNoExistente() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Categoria> resultado = categoriaService.findById(99L);

        assertFalse(resultado.isPresent());

        verify(categoriaRepository, times(1)).findById(99L);
    }

    @Test
    void testEliminarCategoriaExistente() {
        when(categoriaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(categoriaRepository).deleteById(1L);

        boolean resultado = categoriaService.eliminarCategoria(1L);

        assertTrue(resultado);

        verify(categoriaRepository, times(1)).existsById(1L);
        verify(categoriaRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarCategoriaNoExistente() {
        when(categoriaRepository.existsById(99L)).thenReturn(false);

        boolean resultado = categoriaService.eliminarCategoria(99L);

        assertFalse(resultado);

        verify(categoriaRepository, times(1)).existsById(99L);
        verify(categoriaRepository, never()).deleteById(anyLong());
    }
}