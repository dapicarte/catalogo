package BookPoint.catalogo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import BookPoint.catalogo.model.Catalogo;

public interface CatalogoRepository extends JpaRepository<Catalogo, Long> {

}
