package BookPoint.catalogo.service;

import java.time.LocalDate;
import java.util.List;

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
}
