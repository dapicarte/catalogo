package BookPoint.catalogo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import BookPoint.catalogo.model.Producto;
import BookPoint.catalogo.model.Resenia;
import BookPoint.catalogo.repository.ReseniaRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReseniaService {
    @Autowired
    private ReseniaRepository reseniaRepository;
    @Autowired
    private ProductoService productoService;


    public Resenia registrarReseña(Long idProducto, Resenia resenia){
        resenia.setFechaReseña(LocalDate.now());
        Optional<Producto> producto = productoService.findByIdProducto(idProducto);
        if(producto.isEmpty()){
        throw new IllegalArgumentException("Producto no encontrado");
        }
        
        resenia.setProducto(producto.get());
        return reseniaRepository.save(resenia);
    }

    public List<Resenia> listarResenias(){
        return reseniaRepository.findAll();
    }

    public boolean eliminarResenia(Long id) {
        if (reseniaRepository.existsById(id)) {
            reseniaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Resenia actualizarResenia(Long id, Resenia resenia) {
        Resenia buscado = reseniaRepository.findById(id).orElse(null);
        if (buscado == null) return null;

        buscado.setComentario(resenia.getComentario());
        buscado.setCalificacion(resenia.getCalificacion());

        return reseniaRepository.save(buscado);
    }
}
