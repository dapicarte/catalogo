package BookPoint.catalogo.service;

import BookPoint.catalogo.model.Producto;
import BookPoint.catalogo.repository.ProductoRepository;

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
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto crearProductoBase(Long id, String titulo) {
        Producto p = new Producto();
        p.setIdProducto(id);
        p.setIdInventario(10L);
        p.setTipoProducto("Libro");
        p.setTitulo(titulo);
        p.setAutor("Autor X");
        p.setEditorial("Editorial Y");
        p.setDescripcion("Descripcion");
        p.setIsbn("123-456");
        p.setPrecioUnitario(5000);
        p.setEstado(true);
        return p;
    }

    @Test
    void testRegistrarProducto() {
        Producto producto = crearProductoBase(null, "El Quijote");
        Producto guardado = crearProductoBase(1L, "El Quijote");

        when(productoRepository.save(producto)).thenReturn(guardado);

        Producto resultado = productoService.registrarProducto(producto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdProducto());
        assertEquals("El Quijote", resultado.getTitulo());

        verify(productoRepository, times(1)).save(producto);
    }

    @Test
    void testListarProductos() {
        Producto p1 = crearProductoBase(1L, "El Quijote");
        Producto p2 = crearProductoBase(2L, "La Odisea");

        when(productoRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Producto> resultado = productoService.listarProductos();

        assertEquals(2, resultado.size());
        assertEquals("El Quijote", resultado.get(0).getTitulo());
        assertEquals("La Odisea", resultado.get(1).getTitulo());

        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdProductoExistente() {
        Producto producto = crearProductoBase(1L, "El Quijote");

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Optional<Producto> resultado = productoService.findByIdProducto(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getIdProducto());
        assertEquals("El Quijote", resultado.get().getTitulo());

        verify(productoRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdProductoNoExistente() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Producto> resultado = productoService.findByIdProducto(99L);

        assertFalse(resultado.isPresent());

        verify(productoRepository, times(1)).findById(99L);
    }

    @Test
    void testFindByAutor() {
        Producto producto = crearProductoBase(1L, "El Quijote");

        when(productoRepository.findByAutor("Autor X")).thenReturn(Arrays.asList(producto));

        List<Producto> resultado = productoService.findByAutor("Autor X");

        assertEquals(1, resultado.size());
        assertEquals("Autor X", resultado.get(0).getAutor());

        verify(productoRepository, times(1)).findByAutor("Autor X");
    }

    @Test
    void testFindByEditorial() {
        Producto producto = crearProductoBase(1L, "El Quijote");

        when(productoRepository.findByEditorial("Editorial Y")).thenReturn(Arrays.asList(producto));

        List<Producto> resultado = productoService.findByEditorial("Editorial Y");

        assertEquals(1, resultado.size());
        assertEquals("Editorial Y", resultado.get(0).getEditorial());

        verify(productoRepository, times(1)).findByEditorial("Editorial Y");
    }

    @Test
    void testFindByCategoria() {
        Producto producto = crearProductoBase(1L, "El Quijote");

        when(productoRepository.findByCategorias_NombreCategoria("Ficcion")).thenReturn(Arrays.asList(producto));

        List<Producto> resultado = productoService.findByCategoria("Ficcion");

        assertEquals(1, resultado.size());
        assertEquals("El Quijote", resultado.get(0).getTitulo());

        verify(productoRepository, times(1)).findByCategorias_NombreCategoria("Ficcion");
    }

    @Test
    void testActualizarProductoExistente() {
        Producto existente = crearProductoBase(1L, "El Quijote");
        Producto datosNuevos = crearProductoBase(null, "Quijote Edicion Nueva");
        Producto actualizado = crearProductoBase(1L, "Quijote Edicion Nueva");

        when(productoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(productoRepository.save(existente)).thenReturn(actualizado);

        Producto resultado = productoService.actualizarProducto(1L, datosNuevos);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdProducto());
        assertEquals("Quijote Edicion Nueva", existente.getTitulo());

        verify(productoRepository, times(1)).findById(1L);
        verify(productoRepository, times(1)).save(existente);
    }

    @Test
    void testActualizarProductoNoExistente() {
        Producto datosNuevos = crearProductoBase(null, "Quijote Edicion Nueva");

        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        Producto resultado = productoService.actualizarProducto(99L, datosNuevos);

        assertNull(resultado);

        verify(productoRepository, times(1)).findById(99L);
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void testFindByRangoPrecio() {
        Producto producto = crearProductoBase(1L, "El Quijote");

        when(productoRepository.findByPrecioUnitarioBetween(1000, 10000)).thenReturn(Arrays.asList(producto));

        List<Producto> resultado = productoService.findByRangoPrecio(1000, 10000);

        assertEquals(1, resultado.size());
        assertEquals(5000, resultado.get(0).getPrecioUnitario());

        verify(productoRepository, times(1)).findByPrecioUnitarioBetween(1000, 10000);
    }

    @Test
    void testFindByPrecioMaximo() {
        Producto producto = crearProductoBase(1L, "El Quijote");

        when(productoRepository.findByPrecioUnitarioLessThanEqual(10000)).thenReturn(Arrays.asList(producto));

        List<Producto> resultado = productoService.findByPrecioMaximo(10000);

        assertEquals(1, resultado.size());
        assertEquals(5000, resultado.get(0).getPrecioUnitario());

        verify(productoRepository, times(1)).findByPrecioUnitarioLessThanEqual(10000);
    }

    @Test
    void testFindByPrecioMinimo() {
        Producto producto = crearProductoBase(1L, "El Quijote");

        when(productoRepository.findByPrecioUnitarioGreaterThanEqual(1000)).thenReturn(Arrays.asList(producto));

        List<Producto> resultado = productoService.findByPrecioMinimo(1000);

        assertEquals(1, resultado.size());
        assertEquals(5000, resultado.get(0).getPrecioUnitario());

        verify(productoRepository, times(1)).findByPrecioUnitarioGreaterThanEqual(1000);
    }

    @Test
    void testEliminarProductoExistente() {
        when(productoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productoRepository).deleteById(1L);

        boolean resultado = productoService.eliminarProducto(1L);

        assertTrue(resultado);

        verify(productoRepository, times(1)).existsById(1L);
        verify(productoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarProductoNoExistente() {
        when(productoRepository.existsById(99L)).thenReturn(false);

        boolean resultado = productoService.eliminarProducto(99L);

        assertFalse(resultado);

        verify(productoRepository, times(1)).existsById(99L);
        verify(productoRepository, never()).deleteById(anyLong());
    }
}