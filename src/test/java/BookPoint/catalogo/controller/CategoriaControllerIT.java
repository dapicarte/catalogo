package BookPoint.catalogo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import BookPoint.catalogo.model.Categoria;
import BookPoint.catalogo.repository.CategoriaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CategoriaControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void cleanDb() {
        categoriaRepository.deleteAll();
    }

    @Test
    void testCrearYListarCategoria() throws Exception {
        Categoria categoria = new Categoria(null, "Ficcion", "Libro", null);

        mockMvc.perform(post("/api/v1/categoria")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoria)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCategoria").exists())
                .andExpect(jsonPath("$.nombreCategoria").value("Ficcion"));

        mockMvc.perform(get("/api/v1/categoria"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.categoriaList[0].nombreCategoria").value("Ficcion"));
    }

    @Test
    void testEliminarCategoria() throws Exception {
        Categoria categoria = new Categoria(null, "Ficcion", "Libro", null);
        Categoria guardada = categoriaRepository.save(categoria);

        mockMvc.perform(delete("/api/v1/categoria/" + guardada.getIdCategoria()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/categoria/" + guardada.getIdCategoria()))
                .andExpect(status().isNotFound());
    }
}