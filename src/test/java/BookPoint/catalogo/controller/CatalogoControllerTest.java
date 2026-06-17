package BookPoint.catalogo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import BookPoint.catalogo.model.Catalogo;
import BookPoint.catalogo.service.CatalogoService;

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
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CatalogoController.class)
@ActiveProfiles("test")
public class CatalogoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CatalogoService catalogoService;

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    void testCrearCatalogo() throws Exception {
        Catalogo nuevo = new Catalogo(null, "Verano 2026", null, null);
        Catalogo guardado = new Catalogo(1L, "Verano 2026", LocalDate.now(), null);

        Mockito.when(catalogoService.crearCatalogo(any(Catalogo.class))).thenReturn(guardado);

        mockMvc.perform(post("/api/v1/catalogo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCatalogo").value(1L))
                .andExpect(jsonPath("$.nombreCatalogo").value("Verano 2026"));
    }

    @Test
    void testListarCatalogo() throws Exception {
        Catalogo c1 = new Catalogo(1L, "Verano 2026", LocalDate.now(), null);
        Catalogo c2 = new Catalogo(2L, "Invierno 2026", LocalDate.now(), null);

        Mockito.when(catalogoService.listarCatalogo()).thenReturn(Arrays.asList(c1, c2));

        mockMvc.perform(get("/api/v1/catalogo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.catalogoList", hasSize(2)));
    }

    @Test
    void testFindByIdExistente() throws Exception {
        Catalogo buscado = new Catalogo(1L, "Verano 2026", LocalDate.now(), null);

        Mockito.when(catalogoService.findById(1L)).thenReturn(Optional.of(buscado));

        mockMvc.perform(get("/api/v1/catalogo/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCatalogo").value(1L))
                .andExpect(jsonPath("$.nombreCatalogo").value("Verano 2026"));
    }

    @Test
    void testFindByIdNoExistente() throws Exception {
        Mockito.when(catalogoService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/catalogo/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActualizarCatalogo() throws Exception {
        Catalogo datosNuevos = new Catalogo(null, "Verano Actualizado", null, null);
        Catalogo actualizado = new Catalogo(1L, "Verano Actualizado", LocalDate.now(), null);

        Mockito.when(catalogoService.actualizarCatalogo(eq(1L), any(Catalogo.class)))
                .thenReturn(actualizado);

        mockMvc.perform(put("/api/v1/catalogo/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(datosNuevos)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCatalogo").value(1L))
                .andExpect(jsonPath("$.nombreCatalogo").value("Verano Actualizado"));
    }

    @Test
    void testActualizarCatalogoError() throws Exception {
        Catalogo datosNuevos = new Catalogo(null, "Verano Actualizado", null, null);

        Mockito.when(catalogoService.actualizarCatalogo(eq(99L), any(Catalogo.class)))
                .thenThrow(new RuntimeException("No existe"));

        mockMvc.perform(put("/api/v1/catalogo/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(datosNuevos)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEliminarCatalogo() throws Exception {
        Mockito.when(catalogoService.eliminarCatalogo(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/catalogo/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminarCatalogoNoExistente() throws Exception {
        Mockito.when(catalogoService.eliminarCatalogo(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/catalogo/99"))
                .andExpect(status().isNotFound());
    }
}