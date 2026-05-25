package BookPoint.catalogo.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProducto;

    @Column(nullable = false)
    private Long idInventario;

    @Column(nullable = false)
    private String tipoProducto;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = true)
    private String autor;

    @Column(nullable = true)
    private String editorial;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = true)
    private String isbn;

    @Column(nullable = false)
    private Integer precioUnitario;

    @Column(nullable = true)
    private boolean estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_catalogo", nullable = false)
    @JsonBackReference
    @ToString.Exclude
    private Catalogo catalogo;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Resenia> resenias;

    @ManyToMany
    @JoinTable(name = "producto_categoria", joinColumns = @JoinColumn(name = "id_producto"), inverseJoinColumns = @JoinColumn(name = "id_categoria"))
    //@JsonManagedReference
    @JsonIgnoreProperties
    private List<Categoria> categorias;
}
