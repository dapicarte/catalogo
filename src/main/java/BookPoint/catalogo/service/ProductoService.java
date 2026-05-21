package BookPoint.catalogo.service;

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

    public Producto crearProducto(Producto producto){
        return productoRepository.save(producto);
    }
    
}
