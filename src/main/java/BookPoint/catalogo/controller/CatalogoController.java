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

import BookPoint.catalogo.model.Catalogo;
import BookPoint.catalogo.service.CatalogoService;

@RestController
@RequestMapping("api/v1/catalogo")
public class CatalogoController {
    @Autowired
    private CatalogoService catalogoService;

    @PostMapping()
    public ResponseEntity<Catalogo> postCatalogo(@RequestBody Catalogo catalogo){
        try{
            return new ResponseEntity<>(catalogoService.crearCatalogo(catalogo),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping
    public ResponseEntity<List<Catalogo>> getCatalogo(){
        List<Catalogo> catalogo = catalogoService.listarCatalogo();
        if(catalogo.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(catalogo, HttpStatus.OK);
    }    

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Catalogo buscado = catalogoService.findById(id).orElse(null);
        if (buscado == null) {
            return new ResponseEntity<>("Catalogo con id " + id + " no existe", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(buscado, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCatalogo(@PathVariable Long id, @RequestBody Catalogo catalogo) {
        Catalogo actualizado = catalogoService.actualizarCatalogo(id, catalogo);
        if (actualizado == null) {
            return new ResponseEntity<>("Catalogo con id " + id + " no existe", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(actualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCatalogo(@PathVariable Long id) {
        if (catalogoService.eliminarCatalogo(id)) {
            return new ResponseEntity<>("Catalogo con id " + id + " eliminado correctamente", HttpStatus.OK);
        }
        return new ResponseEntity<>("Catalogo con id " + id + " no existe", HttpStatus.NOT_FOUND);
    }
}
