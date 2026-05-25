package BookPoint.catalogo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import BookPoint.catalogo.model.Producto;
import BookPoint.catalogo.repository.ProductoRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    public Producto registrarProducto(Producto producto){
        return productoRepository.save(producto);
    }
    
    public List<Producto> listarProductos(){
        return productoRepository.findAll();
    }

    public Optional<Producto> findByIdProducto(Long id){
        return productoRepository.findById(id);
    }

    public List<Producto> findByAutor(String autor) {
        return productoRepository.findByAutor(autor);
    }

    public List<Producto> findByEditorial(String editorial) {
        return productoRepository.findByEditorial(editorial);
    }

    public List<Producto> findByCategoria(String nombreCategoria) {
        return productoRepository.findByCategorias_NombreCategoria(nombreCategoria);
    }

    public Producto actualizarProducto(Long id, Producto producto) {
        Producto buscado = productoRepository.findById(id).orElse(null);
        if (buscado == null) return null;

        buscado.setTipoProducto(producto.getTipoProducto());
        buscado.setTitulo(producto.getTitulo());
        buscado.setAutor(producto.getAutor());
        buscado.setEditorial(producto.getEditorial());
        buscado.setDescripcion(producto.getDescripcion());
        buscado.setIsbn(producto.getIsbn());
        buscado.setPrecioUnitario(producto.getPrecioUnitario());
        buscado.setEstado(producto.isEstado());
        buscado.setCategorias(producto.getCategorias());

        return productoRepository.save(buscado);
    }
    
    public List<Producto> findByRangoPrecio(Integer precioMin, Integer precioMax) {
        return productoRepository.findByPrecioUnitarioBetween(precioMin, precioMax);
    }

    public List<Producto> findByPrecioMaximo(Integer precioMax) {
        return productoRepository.findByPrecioUnitarioLessThanEqual(precioMax);
    }

    public List<Producto> findByPrecioMinimo(Integer precioMin) {
        return productoRepository.findByPrecioUnitarioGreaterThanEqual(precioMin);
    }
}
