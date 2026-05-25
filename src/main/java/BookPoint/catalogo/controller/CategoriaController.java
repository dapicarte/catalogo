package BookPoint.catalogo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import BookPoint.catalogo.model.Categoria;
import BookPoint.catalogo.service.CategoriaService;

@RestController
@RequestMapping("/api/v1/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<?> crearCategoria(@RequestBody Categoria categoria) {
        try {
            return new ResponseEntity<>(categoriaService.crearCategoria(categoria), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al crear la categoria", HttpStatus.CONFLICT);
        }
    }

    @GetMapping
    public ResponseEntity<?> listarCategorias() {
        List<Categoria> categorias = categoriaService.listarCategorias();
        if (categorias.isEmpty()) {
            return new ResponseEntity<>("No existen categorias registradas", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(categorias, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Categoria buscado = categoriaService.findById(id).orElse(null);
        if (buscado == null) {
            return new ResponseEntity<>("Categoria con id " + id + " no existe", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(buscado, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCategoria(@PathVariable Long id) {
        if (categoriaService.eliminarCategoria(id)) {
            return new ResponseEntity<>("Categoria con id " + id + " eliminada correctamente", HttpStatus.OK);
        }
        return new ResponseEntity<>("Categoria con id " + id + " no existe", HttpStatus.NOT_FOUND);
    }
}

