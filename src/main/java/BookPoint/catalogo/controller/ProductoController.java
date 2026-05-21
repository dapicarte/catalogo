package BookPoint.catalogo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import BookPoint.catalogo.model.Producto;
import BookPoint.catalogo.service.ProductoService;

@RestController
@RequestMapping("api/productos")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @PostMapping()
    public ResponseEntity<Producto> postProducto(@RequestBody Producto producto){
        try{
            return new ResponseEntity<>(productoService.crearProducto(producto),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}
