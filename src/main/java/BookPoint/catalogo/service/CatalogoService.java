package BookPoint.catalogo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import BookPoint.catalogo.model.Catalogo;
import BookPoint.catalogo.repository.CatalogoRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CatalogoService {
    @Autowired
    private CatalogoRepository catalogoRepository;

    public Catalogo crearCatalogo(Catalogo catalogo){
        catalogo.setFechaActualizacion(LocalDate.now());
        return catalogoRepository.save(catalogo);
    }

    public List<Catalogo> listarCatalogo(){
        return catalogoRepository.findAll();
    }

    public Optional<Catalogo> findById(Long id) {
    return catalogoRepository.findById(id);
}

    public Catalogo actualizarCatalogo(Long id, Catalogo catalogo) {
        Catalogo buscado = catalogoRepository.findById(id).orElse(null);
        if (buscado == null) return null;

        buscado.setNombreCatalogo(catalogo.getNombreCatalogo());
        buscado.setFechaActualizacion(LocalDate.now());

        return catalogoRepository.save(buscado);
    }

    public boolean eliminarCatalogo(Long id) {
        if (catalogoRepository.existsById(id)) {
            catalogoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
