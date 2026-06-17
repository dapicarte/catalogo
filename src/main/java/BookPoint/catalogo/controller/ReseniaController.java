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

import BookPoint.catalogo.model.Resenia;
import BookPoint.catalogo.service.ReseniaService;

@RestController
@RequestMapping("api/v1/resenias")
public class ReseniaController {

    @Autowired
    private ReseniaService reseniaService;

    @PostMapping("/{idProducto}")
    public EntityModel<Resenia> postResenia(@PathVariable Long idProducto, @RequestBody Resenia resenia) {
        Resenia nueva = reseniaService.registrarReseña(idProducto, resenia);
        return EntityModel.of(nueva,
                linkTo(methodOn(ReseniaController.class).getResenia()).withRel("resenias"));
    }

    @GetMapping
    public CollectionModel<EntityModel<Resenia>> getResenia() {
        List<Resenia> resenias = reseniaService.listarResenias();
        List<EntityModel<Resenia>> reseniasConLinks = resenias.stream()
                .map(resenia -> EntityModel.of(resenia,
                        linkTo(methodOn(ReseniaController.class).getResenia()).withRel("resenias")))
                .toList();
        return CollectionModel.of(reseniasConLinks,
                linkTo(methodOn(ReseniaController.class).getResenia()).withSelfRel());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Resenia>> actualizarResenia(@PathVariable Long id, @RequestBody Resenia resenia) {
        try {
            Resenia actualizado = reseniaService.actualizarResenia(id, resenia);
            return ResponseEntity.ok(EntityModel.of(actualizado,
                    linkTo(methodOn(ReseniaController.class).getResenia()).withRel("resenias")));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarResenia(@PathVariable Long id) {
        if (reseniaService.eliminarResenia(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
