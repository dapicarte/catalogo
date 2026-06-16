package BookPoint.catalogo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import BookPoint.catalogo.model.Categoria;
import BookPoint.catalogo.service.CategoriaService;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoriaController.class)
@ActiveProfiles("test")
public class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoriaService categoriaService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testCrearCategoria() throws Exception {
        Categoria nueva = new Categoria(null, "Ficcion", "Libro", null);
        Categoria guardada = new Categoria(1L, "Ficcion", "Libro", null);

        Mockito.when(categoriaService.crearCategoria(any(Categoria.class))).thenReturn(guardada);

        mockMvc.perform(post("/api/v1/categoria")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nueva)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCategoria").value(1L))
                .andExpect(jsonPath("$.nombreCategoria").value("Ficcion"))
                .andExpect(jsonPath("$.tipoProducto").value("Libro"));
    }

    @Test
    void testCrearCategoriaError() throws Exception {
        Categoria nueva = new Categoria(null, "Ficcion", "Libro", null);

        Mockito.when(categoriaService.crearCategoria(any(Categoria.class)))
                .thenThrow(new RuntimeException("Error en BD"));

        mockMvc.perform(post("/api/v1/categoria")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nueva)))
                .andExpect(status().isConflict());
    }

    @Test
    void testListarCategorias() throws Exception {
        Categoria c1 = new Categoria(1L, "Ficcion", "Libro", null);
        Categoria c2 = new Categoria(2L, "Tecnologia", "Revista", null);

        Mockito.when(categoriaService.listarCategorias()).thenReturn(Arrays.asList(c1, c2));

        mockMvc.perform(get("/api/v1/categoria"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombreCategoria", is("Ficcion")))
                .andExpect(jsonPath("$[1].nombreCategoria", is("Tecnologia")));
    }

    @Test
    void testListarCategoriasVacio() throws Exception {
        Mockito.when(categoriaService.listarCategorias()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/categoria"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindByIdExistente() throws Exception {
        Categoria buscado = new Categoria(1L, "Ficcion", "Libro", null);

        Mockito.when(categoriaService.findById(1L)).thenReturn(Optional.of(buscado));

        mockMvc.perform(get("/api/v1/categoria/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCategoria").value(1L))
                .andExpect(jsonPath("$.nombreCategoria").value("Ficcion"));
    }

    @Test
    void testFindByIdNoExistente() throws Exception {
        Mockito.when(categoriaService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/categoria/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEliminarCategoria() throws Exception {
        Mockito.when(categoriaService.eliminarCategoria(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/categoria/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testEliminarCategoriaNoExistente() throws Exception {
        Mockito.when(categoriaService.eliminarCategoria(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/categoria/99"))
                .andExpect(status().isNotFound());
    }
}