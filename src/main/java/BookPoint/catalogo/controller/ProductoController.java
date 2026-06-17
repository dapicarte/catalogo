package BookPoint.catalogo.controller;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import BookPoint.catalogo.model.Producto;
import BookPoint.catalogo.service.ProductoService;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @PostMapping
    public EntityModel<Producto> postProducto(@RequestBody Producto producto) {
        Producto productoGuardado = productoService.registrarProducto(producto);
        return EntityModel.of(productoGuardado,
                linkTo(methodOn(ProductoController.class).getProductoPorId(productoGuardado.getIdProducto()))
                        .withSelfRel(),
                linkTo(methodOn(ProductoController.class).getCatalogo()).withRel("productos"));
    }

    @GetMapping
    public CollectionModel<EntityModel<Producto>> getCatalogo() {
        List<Producto> productos = productoService.listarProductos();
        List<EntityModel<Producto>> productosConLinks = productos.stream()
                .map(producto -> EntityModel.of(producto,
                        linkTo(methodOn(ProductoController.class).getProductoPorId(producto.getIdProducto()))
                                .withSelfRel()))
                .toList();
        return CollectionModel.of(productosConLinks,
                linkTo(methodOn(ProductoController.class).getCatalogo()).withSelfRel());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> getProductoPorId(@PathVariable Long id) {
        return productoService.findByIdProducto(id)
                .map(producto -> ResponseEntity.ok(EntityModel.of(producto,
                        linkTo(methodOn(ProductoController.class).getProductoPorId(id)).withSelfRel(),
                        linkTo(methodOn(ProductoController.class).getCatalogo()).withRel("productos"))))
                .orElse(ResponseEntity.notFound().build());
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
            return new ResponseEntity<>("No se encontraron productos de la editorial " + editorial,
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    @GetMapping("/categoria/{nombreCategoria}")
    public ResponseEntity<?> findByCategoria(@PathVariable String nombreCategoria) {
        List<Producto> productos = productoService.findByCategoria(nombreCategoria);
        if (productos.isEmpty()) {
            return new ResponseEntity<>("No se encontraron productos de la categoría " + nombreCategoria,
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> actualizarProducto(@PathVariable Long id,
            @RequestBody Producto producto) {
        try {
            Producto actualizado = productoService.actualizarProducto(id, producto);
            return ResponseEntity.ok(EntityModel.of(actualizado,
                    linkTo(methodOn(ProductoController.class).getProductoPorId(id)).withSelfRel(),
                    linkTo(methodOn(ProductoController.class).getCatalogo()).withRel("productos")));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        if (productoService.eliminarProducto(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}