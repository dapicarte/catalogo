package BookPoint.catalogo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import BookPoint.catalogo.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByAutor(String autor);
    List<Producto> findByEditorial(String editorial);
    List<Producto> findByCategorias_NombreCategoria(String nombreCategoria);

    List<Producto> findByPrecioUnitarioBetween(Integer precioMin, Integer precioMax);
    List<Producto> findByPrecioUnitarioLessThanEqual(Integer precioMax);
    List<Producto> findByPrecioUnitarioGreaterThanEqual(Integer precioMin);
}
