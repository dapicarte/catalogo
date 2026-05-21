package BookPoint.catalogo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import BookPoint.catalogo.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

}
