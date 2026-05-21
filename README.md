+--------------------------------------------------+
|                    Catalogo                      |
+--------------------------------------------------+
| - idCatalogo: Long                               |
| - nombre: String                                 |
| - productos: List<Producto>                      |
| - fechaActualizacion: LocalDate                  |
+--------------------------------------------------+

                  |
                  | contiene
                  v

+--------------------------------------------------+
|                    Producto                      |
+--------------------------------------------------+
| - idProducto: Long                               |
| - idInventario: Long                             |
| - titulo: String                                 |
| - autor: String                                  |
| - editorial: String                              |
| - descripcion: String                            |
| - precio: Double                                 |
| - isbn: String                                   |
| - estado: String                                 |
+--------------------------------------------------+

        | pertenece a                 | tiene
        v                             v

+----------------------+     +----------------------+
|      Categoria       |     |       Reseña         |
+----------------------+     +----------------------+
| - idCategoria: Long  |     | - idReseña: Long     |
| - nombre: String     |     | - comentario:String  |
| - descripcion:String |     | - calificacion:int   |
+----------------------+     | - fecha: LocalDate   |
                             +----------------------+

                  |
                  | consulta stock desde
                  v

+--------------------------------------------------+
|                 InventarioDTO                    |
+--------------------------------------------------+
| - idInventario: Long                             |
| - stockDisponible: int                           |
+--------------------------------------------------+