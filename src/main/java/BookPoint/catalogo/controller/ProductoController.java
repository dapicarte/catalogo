package BookPoint.catalogo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import BookPoint.catalogo.model.Producto;
import BookPoint.catalogo.service.ProductoService;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @PostMapping()
    public ResponseEntity<Producto> postProducto(@RequestBody Producto producto){
        try{
            return new ResponseEntity<>(productoService.registrarProducto(producto),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping
    public ResponseEntity<List<Producto>> getCatalogo(){
        List<Producto> catalogo = productoService.listarProductos();
        if(catalogo.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoPorId(@PathVariable Long id) {
        try {
            Optional<Producto> producto = productoService.findByIdProducto(id);

            if (producto.isPresent()) {
                return new ResponseEntity<>(producto.get(), HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/autor/{autor}")
    public ResponseEntity<?> findByAutor(@PathVariable String autor) {
        List<Producto> productos = productoService.findByAutor(autor);
        if (productos.isEmpty()) {
            return new ResponseEntity<>("No se encontraron productos del autor " + autor, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    @GetMapping("/editorial/{editorial}")
    public ResponseEntity<?> findByEditorial(@PathVariable String editorial) {
        List<Producto> productos = productoService.findByEditorial(editorial);
        if (productos.isEmpty()) {
            return new ResponseEntity<>("No se encontraron productos de la editorial " + editorial, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    @GetMapping("/categoria/{nombreCategoria}")
    public ResponseEntity<?> findByCategoria(@PathVariable String nombreCategoria) {
        List<Producto> productos = productoService.findByCategoria(nombreCategoria);
        if (productos.isEmpty()) {
            return new ResponseEntity<>("No se encontraron productos de la categoría " + nombreCategoria, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        Producto actualizado = productoService.actualizarProducto(id, producto);
        if (actualizado == null) {
            return new ResponseEntity<>("Producto con id " + id + " no existe", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(actualizado, HttpStatus.OK);
    }

    @GetMapping("/precio")
    public ResponseEntity<?> findByRangoPrecio(
        @RequestParam(required = false) Integer precioMin,
        @RequestParam(required = false) Integer precioMax) {

        List<Producto> productos;
        if (precioMin != null && precioMax != null) {
            productos = productoService.findByRangoPrecio(precioMin, precioMax);
        } else if (precioMax != null) {
            productos = productoService.findByPrecioMaximo(precioMax);
        } else if (precioMin != null) {
            productos = productoService.findByPrecioMinimo(precioMin);
        } else {
            return new ResponseEntity<>("Debe indicar al menos un precio", HttpStatus.BAD_REQUEST);
        }
        if (productos.isEmpty()) {
            return new ResponseEntity<>("No se encontraron productos en ese rango de precio", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }
}
