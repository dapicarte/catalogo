package BookPoint.catalogo.controller;

import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
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
    public EntityModel<Categoria> crearCategoria(@RequestBody Categoria categoria) {
        Categoria categoriaGuardada = categoriaService.crearCategoria(categoria);
        return EntityModel.of(categoriaGuardada,
                linkTo(methodOn(CategoriaController.class).findById(categoriaGuardada.getIdCategoria())).withSelfRel(),
                linkTo(methodOn(CategoriaController.class).listarCategorias()).withRel("categorias"));
    }

    @GetMapping
    public CollectionModel<EntityModel<Categoria>> listarCategorias() {
        List<Categoria> categorias = categoriaService.listarCategorias();
        List<EntityModel<Categoria>> categoriasConLinks = categorias.stream()
                .map(categoria -> EntityModel.of(categoria,
                        linkTo(methodOn(CategoriaController.class).findById(categoria.getIdCategoria())).withSelfRel()))
                .toList();
        return CollectionModel.of(categoriasConLinks,
                linkTo(methodOn(CategoriaController.class).listarCategorias()).withSelfRel());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Categoria>> findById(@PathVariable Long id) {
        return categoriaService.findById(id)
                .map(categoria -> ResponseEntity.ok(EntityModel.of(categoria,
                        linkTo(methodOn(CategoriaController.class).findById(id)).withSelfRel(),
                        linkTo(methodOn(CategoriaController.class).listarCategorias()).withRel("categorias"))))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        if (categoriaService.eliminarCategoria(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}