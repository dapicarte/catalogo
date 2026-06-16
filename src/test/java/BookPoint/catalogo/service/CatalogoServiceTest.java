package BookPoint.catalogo.service;

import BookPoint.catalogo.model.Catalogo;
import BookPoint.catalogo.repository.CatalogoRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatalogoServiceTest {

    @Mock
    private CatalogoRepository catalogoRepository;

    @InjectMocks
    private CatalogoService catalogoService;

    @Test
    void testCrearCatalogo() {
        Catalogo catalogo = new Catalogo(null, "Verano 2026", null, null);
        Catalogo guardado = new Catalogo(1L, "Verano 2026", LocalDate.now(), null);

        when(catalogoRepository.save(any(Catalogo.class))).thenReturn(guardado);

        Catalogo resultado = catalogoService.crearCatalogo(catalogo);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdCatalogo());
        assertEquals("Verano 2026", resultado.getNombreCatalogo());
        assertEquals(LocalDate.now(), catalogo.getFechaActualizacion());

        verify(catalogoRepository, times(1)).save(catalogo);
    }

    @Test
    void testListarCatalogo() {
        Catalogo c1 = new Catalogo(1L, "Verano 2026", LocalDate.now(), null);
        Catalogo c2 = new Catalogo(2L, "Invierno 2026", LocalDate.now(), null);

        when(catalogoRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<Catalogo> resultado = catalogoService.listarCatalogo();

        assertEquals(2, resultado.size());
        assertEquals("Verano 2026", resultado.get(0).getNombreCatalogo());
        assertEquals("Invierno 2026", resultado.get(1).getNombreCatalogo());

        verify(catalogoRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdExistente() {
        Catalogo catalogo = new Catalogo(1L, "Verano 2026", LocalDate.now(), null);

        when(catalogoRepository.findById(1L)).thenReturn(Optional.of(catalogo));

        Optional<Catalogo> resultado = catalogoService.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getIdCatalogo());
        assertEquals("Verano 2026", resultado.get().getNombreCatalogo());

        verify(catalogoRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNoExistente() {
        when(catalogoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Catalogo> resultado = catalogoService.findById(99L);

        assertFalse(resultado.isPresent());

        verify(catalogoRepository, times(1)).findById(99L);
    }

    @Test
    void testActualizarCatalogoExistente() {
        Catalogo existente = new Catalogo(1L, "Verano 2026", LocalDate.now(), null);
        Catalogo datosNuevos = new Catalogo(null, "Verano Actualizado", null, null);
        Catalogo actualizado = new Catalogo(1L, "Verano Actualizado", LocalDate.now(), null);

        when(catalogoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(catalogoRepository.save(existente)).thenReturn(actualizado);

        Catalogo resultado = catalogoService.actualizarCatalogo(1L, datosNuevos);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdCatalogo());
        assertEquals("Verano Actualizado", existente.getNombreCatalogo());
        assertEquals(LocalDate.now(), existente.getFechaActualizacion());

        verify(catalogoRepository, times(1)).findById(1L);
        verify(catalogoRepository, times(1)).save(existente);
    }

    @Test
    void testActualizarCatalogoNoExistente() {
        Catalogo datosNuevos = new Catalogo(null, "Verano Actualizado", null, null);

        when(catalogoRepository.findById(99L)).thenReturn(Optional.empty());

        Catalogo resultado = catalogoService.actualizarCatalogo(99L, datosNuevos);

        assertNull(resultado);

        verify(catalogoRepository, times(1)).findById(99L);
        verify(catalogoRepository, never()).save(any(Catalogo.class));
    }

    @Test
    void testEliminarCatalogoExistente() {
        when(catalogoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(catalogoRepository).deleteById(1L);

        boolean resultado = catalogoService.eliminarCatalogo(1L);

        assertTrue(resultado);

        verify(catalogoRepository, times(1)).existsById(1L);
        verify(catalogoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarCatalogoNoExistente() {
        when(catalogoRepository.existsById(99L)).thenReturn(false);

        boolean resultado = catalogoService.eliminarCatalogo(99L);

        assertFalse(resultado);

        verify(catalogoRepository, times(1)).existsById(99L);
        verify(catalogoRepository, never()).deleteById(anyLong());
    }
}