package BookPoint.catalogo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import BookPoint.catalogo.model.Catalogo;
import BookPoint.catalogo.model.Producto;
import BookPoint.catalogo.model.Resenia;
import BookPoint.catalogo.repository.CatalogoRepository;
import BookPoint.catalogo.repository.ProductoRepository;
import BookPoint.catalogo.repository.ReseniaRepository;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReseniaControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReseniaRepository reseniaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CatalogoRepository catalogoRepository;

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private Producto productoGuardado;

    @BeforeEach
    void setUp() {
        reseniaRepository.deleteAll();
        productoRepository.deleteAll();
        catalogoRepository.deleteAll();

        Catalogo catalogo = catalogoRepository.save(new Catalogo(null, "Verano", LocalDate.now(), null));

        Producto p = new Producto();
        p.setIdInventario(10L);
        p.setTipoProducto("Libro");
        p.setTitulo("El Quijote");
        p.setDescripcion("Descripcion");
        p.setPrecioUnitario(5000);
        p.setEstado(true);
        p.setCatalogo(catalogo);
        productoGuardado = productoRepository.save(p);
    }

    private Resenia crearResenia() {
        Resenia r = new Resenia();
        r.setComentario("Excelente");
        r.setCalificacion(5);
        r.setFechaReseña(LocalDate.now());
        r.setProducto(productoGuardado);
        return r;
    }

    @Test
    void testCrearYListarResenia() throws Exception {
        Resenia nueva = new Resenia();
        nueva.setComentario("Excelente");
        nueva.setCalificacion(5);

        mockMvc.perform(post("/api/v1/resenias/" + productoGuardado.getIdProducto())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nueva)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comentario").value("Excelente"));

        mockMvc.perform(get("/api/v1/resenias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reseniaList[0].comentario").value("Excelente"));
    }

    @Test
    void testEliminarResenia() throws Exception {
        Resenia guardada = reseniaRepository.save(crearResenia());

        mockMvc.perform(delete("/api/v1/resenias/" + guardada.getIdReseña()))
                .andExpect(status().isNoContent());
    }
}
