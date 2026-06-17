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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import BookPoint.catalogo.model.Catalogo;
import BookPoint.catalogo.service.CatalogoService;

@RestController
@RequestMapping("api/v1/catalogo")
public class CatalogoController {

    @Autowired
    private CatalogoService catalogoService;

    @PostMapping
    public EntityModel<Catalogo> postCatalogo(@RequestBody Catalogo catalogo) {
        Catalogo catalogoGuardado = catalogoService.crearCatalogo(catalogo);
        return EntityModel.of(catalogoGuardado,
                linkTo(methodOn(CatalogoController.class).findById(catalogoGuardado.getIdCatalogo())).withSelfRel(),
                linkTo(methodOn(CatalogoController.class).getCatalogo()).withRel("catalogos"));
    }

    @GetMapping
    public CollectionModel<EntityModel<Catalogo>> getCatalogo() {
        List<Catalogo> catalogos = catalogoService.listarCatalogo();
        List<EntityModel<Catalogo>> catalogosConLinks = catalogos.stream()
                .map(catalogo -> EntityModel.of(catalogo,
                        linkTo(methodOn(CatalogoController.class).findById(catalogo.getIdCatalogo())).withSelfRel()))
                .toList();
        return CollectionModel.of(catalogosConLinks,
                linkTo(methodOn(CatalogoController.class).getCatalogo()).withSelfRel());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Catalogo>> findById(@PathVariable Long id) {
        return catalogoService.findById(id)
                .map(catalogo -> ResponseEntity.ok(EntityModel.of(catalogo,
                        linkTo(methodOn(CatalogoController.class).findById(id)).withSelfRel(),
                        linkTo(methodOn(CatalogoController.class).getCatalogo()).withRel("catalogos"))))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Catalogo>> actualizarCatalogo(@PathVariable Long id, @RequestBody Catalogo catalogo) {
        try {
            Catalogo actualizado = catalogoService.actualizarCatalogo(id, catalogo);
            return ResponseEntity.ok(EntityModel.of(actualizado,
                    linkTo(methodOn(CatalogoController.class).findById(id)).withSelfRel(),
                    linkTo(methodOn(CatalogoController.class).getCatalogo()).withRel("catalogos")));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCatalogo(@PathVariable Long id) {
        if (catalogoService.eliminarCatalogo(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}