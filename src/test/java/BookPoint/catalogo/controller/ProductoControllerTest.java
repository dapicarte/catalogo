package BookPoint.catalogo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import BookPoint.catalogo.model.Producto;
import BookPoint.catalogo.service.ProductoService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductoController.class)
@ActiveProfiles("test")
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductoService productoService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Producto crearProducto(Long id, String titulo) {
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
    void testPostProducto() throws Exception {
        Producto nuevo = crearProducto(null, "El Quijote");
        Producto guardado = crearProducto(1L, "El Quijote");

        Mockito.when(productoService.registrarProducto(any(Producto.class))).thenReturn(guardado);

        mockMvc.perform(post("/api/v1/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProducto").value(1L))
                .andExpect(jsonPath("$.titulo").value("El Quijote"));
    }

    @Test
    void testGetCatalogo() throws Exception {
        Producto p1 = crearProducto(1L, "El Quijote");
        Producto p2 = crearProducto(2L, "La Odisea");

        Mockito.when(productoService.listarProductos()).thenReturn(Arrays.asList(p1, p2));

        mockMvc.perform(get("/api/v1/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.productoList", hasSize(2)));
    }

    @Test
    void testGetProductoPorIdExistente() throws Exception {
        Producto producto = crearProducto(1L, "El Quijote");

        Mockito.when(productoService.findByIdProducto(1L)).thenReturn(Optional.of(producto));

        mockMvc.perform(get("/api/v1/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProducto").value(1L))
                .andExpect(jsonPath("$.titulo").value("El Quijote"));
    }

    @Test
    void testGetProductoPorIdNoExistente() throws Exception {
        Mockito.when(productoService.findByIdProducto(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/productos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindByAutorConResultados() throws Exception {
        Producto producto = crearProducto(1L, "El Quijote");

        Mockito.when(productoService.findByAutor("Autor X")).thenReturn(Arrays.asList(producto));

        mockMvc.perform(get("/api/v1/productos/autor/Autor X"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].autor", is("Autor X")));
    }

    @Test
    void testFindByAutorSinResultados() throws Exception {
        Mockito.when(productoService.findByAutor("Desconocido")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/productos/autor/Desconocido"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindByEditorialConResultados() throws Exception {
        Producto producto = crearProducto(1L, "El Quijote");

        Mockito.when(productoService.findByEditorial("Editorial Y")).thenReturn(Arrays.asList(producto));

        mockMvc.perform(get("/api/v1/productos/editorial/Editorial Y"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testFindByEditorialSinResultados() throws Exception {
        Mockito.when(productoService.findByEditorial("Desconocida")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/productos/editorial/Desconocida"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindByCategoriaConResultados() throws Exception {
        Producto producto = crearProducto(1L, "El Quijote");

        Mockito.when(productoService.findByCategoria("Ficcion")).thenReturn(Arrays.asList(producto));

        mockMvc.perform(get("/api/v1/productos/categoria/Ficcion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testFindByCategoriaSinResultados() throws Exception {
        Mockito.when(productoService.findByCategoria("Desconocida")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/productos/categoria/Desconocida"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActualizarProducto() throws Exception {
        Producto datosNuevos = crearProducto(null, "Quijote Nuevo");
        Producto actualizado = crearProducto(1L, "Quijote Nuevo");

        Mockito.when(productoService.actualizarProducto(eq(1L), any(Producto.class)))
                .thenReturn(actualizado);

        mockMvc.perform(put("/api/v1/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(datosNuevos)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProducto").value(1L))
                .andExpect(jsonPath("$.titulo").value("Quijote Nuevo"));
    }

    @Test
    void testActualizarProductoError() throws Exception {
        Producto datosNuevos = crearProducto(null, "Quijote Nuevo");

        Mockito.when(productoService.actualizarProducto(eq(99L), any(Producto.class)))
                .thenThrow(new RuntimeException("No existe"));

        mockMvc.perform(put("/api/v1/productos/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(datosNuevos)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindByRangoPrecio() throws Exception {
        Producto producto = crearProducto(1L, "El Quijote");

        Mockito.when(productoService.findByRangoPrecio(1000, 10000)).thenReturn(Arrays.asList(producto));

        mockMvc.perform(get("/api/v1/productos/precio")
                .param("precioMin", "1000")
                .param("precioMax", "10000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testFindByPrecioMaximo() throws Exception {
        Producto producto = crearProducto(1L, "El Quijote");

        Mockito.when(productoService.findByPrecioMaximo(10000)).thenReturn(Arrays.asList(producto));

        mockMvc.perform(get("/api/v1/productos/precio")
                .param("precioMax", "10000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testFindByPrecioMinimo() throws Exception {
        Producto producto = crearProducto(1L, "El Quijote");

        Mockito.when(productoService.findByPrecioMinimo(1000)).thenReturn(Arrays.asList(producto));

        mockMvc.perform(get("/api/v1/productos/precio")
                .param("precioMin", "1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testFindByPrecioSinParametros() throws Exception {
        mockMvc.perform(get("/api/v1/productos/precio"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testFindByPrecioSinResultados() throws Exception {
        Mockito.when(productoService.findByRangoPrecio(1000, 10000)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/productos/precio")
                .param("precioMin", "1000")
                .param("precioMax", "10000"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEliminarProducto() throws Exception {
        Mockito.when(productoService.eliminarProducto(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/productos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminarProductoNoExistente() throws Exception {
        Mockito.when(productoService.eliminarProducto(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/productos/99"))
                .andExpect(status().isNotFound());
    }
}