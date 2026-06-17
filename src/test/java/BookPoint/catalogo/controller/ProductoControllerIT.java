package BookPoint.catalogo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import BookPoint.catalogo.model.Catalogo;
import BookPoint.catalogo.model.Producto;
import BookPoint.catalogo.repository.CatalogoRepository;
import BookPoint.catalogo.repository.ProductoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CatalogoRepository catalogoRepository;

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void cleanDb() {
        productoRepository.deleteAll();
        catalogoRepository.deleteAll();
    }

    private Producto crearProducto(String titulo, Catalogo catalogo) {
        Producto p = new Producto();
        p.setIdInventario(10L);
        p.setTipoProducto("Libro");
        p.setTitulo(titulo);
        p.setAutor("Autor X");
        p.setEditorial("Editorial Y");
        p.setDescripcion("Descripcion");
        p.setIsbn("123-456");
        p.setPrecioUnitario(5000);
        p.setEstado(true);
        p.setCatalogo(catalogo);
        return p;
    }

    @Test
    void testListarYObtenerProducto() throws Exception {
        Catalogo catalogo = catalogoRepository.save(new Catalogo(null, "Verano", LocalDate.now(), null));
        Producto guardado = productoRepository.save(crearProducto("El Quijote", catalogo));

        mockMvc.perform(get("/api/v1/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.productoList[0].titulo").value("El Quijote"));

        mockMvc.perform(get("/api/v1/productos/" + guardado.getIdProducto()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("El Quijote"));
    }

    @Test
    void testEliminarProducto() throws Exception {
        Catalogo catalogo = catalogoRepository.save(new Catalogo(null, "Verano", LocalDate.now(), null));
        Producto guardado = productoRepository.save(crearProducto("El Quijote", catalogo));

        mockMvc.perform(delete("/api/v1/productos/" + guardado.getIdProducto()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/productos/" + guardado.getIdProducto()))
                .andExpect(status().isNotFound());
    }
}