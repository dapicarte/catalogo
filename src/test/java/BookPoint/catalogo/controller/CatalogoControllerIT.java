package BookPoint.catalogo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import BookPoint.catalogo.model.Catalogo;
import BookPoint.catalogo.repository.CatalogoRepository;

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
class CatalogoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CatalogoRepository catalogoRepository;

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void cleanDb() {
        catalogoRepository.deleteAll();
    }

    @Test
    void testCrearYObtenerCatalogo() throws Exception {
        Catalogo catalogo = new Catalogo(null, "Verano 2026", null, null);

        mockMvc.perform(post("/api/v1/catalogo")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(catalogo)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idCatalogo").exists())
            .andExpect(jsonPath("$.nombreCatalogo").value("Verano 2026"));

        mockMvc.perform(get("/api/v1/catalogo"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].nombreCatalogo").value("Verano 2026"));
    }

    @Test
    void testEliminarCatalogo() throws Exception {
        Catalogo catalogo = new Catalogo(null, "Verano 2026", java.time.LocalDate.now(), null);
        Catalogo guardado = catalogoRepository.save(catalogo);

        mockMvc.perform(delete("/api/v1/catalogo/" + guardado.getIdCatalogo()))
            .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/catalogo/" + guardado.getIdCatalogo()))
            .andExpect(status().isNotFound());
    }
}