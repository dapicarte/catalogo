package BookPoint.catalogo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import BookPoint.catalogo.model.Resenia;
import BookPoint.catalogo.service.ReseniaService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReseniaController.class)
@ActiveProfiles("test")
public class ReseniaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReseniaService reseniaService;

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private Resenia crearResenia(Long id, String comentario, Integer calificacion) {
        Resenia r = new Resenia();
        r.setIdReseña(id);
        r.setComentario(comentario);
        r.setCalificacion(calificacion);
        r.setFechaReseña(LocalDate.now());
        return r;
    }

    @Test
    void testPostResenia() throws Exception {
        Resenia nueva = crearResenia(null, "Excelente", 5);
        Resenia guardada = crearResenia(1L, "Excelente", 5);

        Mockito.when(reseniaService.registrarReseña(eq(10L), any(Resenia.class))).thenReturn(guardada);

        mockMvc.perform(post("/api/v1/resenias/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nueva)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comentario").value("Excelente"))
                .andExpect(jsonPath("$.calificacion").value(5));
    }

    @Test
    void testGetResenia() throws Exception {
        Resenia r1 = crearResenia(1L, "Buena", 4);
        Resenia r2 = crearResenia(2L, "Mala", 1);

        Mockito.when(reseniaService.listarResenias()).thenReturn(Arrays.asList(r1, r2));

        mockMvc.perform(get("/api/v1/resenias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reseniaList", hasSize(2)));
    }

    @Test
    void testActualizarResenia() throws Exception {
        Resenia datosNuevos = crearResenia(null, "Actualizada", 3);
        Resenia actualizada = crearResenia(1L, "Actualizada", 3);

        Mockito.when(reseniaService.actualizarResenia(eq(1L), any(Resenia.class)))
                .thenReturn(actualizada);

        mockMvc.perform(put("/api/v1/resenias/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(datosNuevos)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comentario").value("Actualizada"))
                .andExpect(jsonPath("$.calificacion").value(3));
    }

    @Test
    void testActualizarReseniaError() throws Exception {
        Resenia datosNuevos = crearResenia(null, "Actualizada", 3);

        Mockito.when(reseniaService.actualizarResenia(eq(99L), any(Resenia.class)))
                .thenThrow(new RuntimeException("No existe"));

        mockMvc.perform(put("/api/v1/resenias/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(datosNuevos)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEliminarResenia() throws Exception {
        Mockito.when(reseniaService.eliminarResenia(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/resenias/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminarReseniaNoExistente() throws Exception {
        Mockito.when(reseniaService.eliminarResenia(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/resenias/99"))
                .andExpect(status().isNotFound());
    }
}