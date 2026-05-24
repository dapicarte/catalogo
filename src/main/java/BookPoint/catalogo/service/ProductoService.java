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

    
}
