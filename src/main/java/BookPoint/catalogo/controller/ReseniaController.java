package BookPoint.catalogo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    

    @PostMapping("{idProducto}")
    public ResponseEntity<Resenia> postResenia(@PathVariable Long idProducto ,@RequestBody Resenia resenia){
            try{
            Resenia nueva = reseniaService.registrarReseña(idProducto,resenia);
            return new ResponseEntity<>(nueva, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
    @GetMapping()
    public ResponseEntity<?> getResenia(){
        List<Resenia> resenia = reseniaService.listarResenias();
        if(resenia.isEmpty()){
            return ResponseEntity
            .status(404)
            .body("El producto no tiene ninguna reseña");
        }
        return ResponseEntity.ok(resenia);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarResenia(@PathVariable Long id, @RequestBody Resenia resenia) {
        Resenia actualizado = reseniaService.actualizarResenia(id, resenia);
        if (actualizado == null) {
            return new ResponseEntity<>("Reseña con id " + id + " no existe", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(actualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarResenia(@PathVariable Long id) {
        if (reseniaService.eliminarResenia(id)) {
            return new ResponseEntity<>("Reseña con id " + id + " eliminada correctamente", HttpStatus.OK);
        }
        return new ResponseEntity<>("Reseña con id " + id + " no existe", HttpStatus.NOT_FOUND);
    }
}
