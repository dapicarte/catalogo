package BookPoint.catalogo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import BookPoint.catalogo.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{
    
}
