# Microservicio de Catálogo

## server.port=8090

## Modelos
- **Catalogo** — agrupa los productos
- **Producto** — libros y útiles
- **Categoria** — géneros o tipos de producto
- **Reseña** — calificaciones de productos

---

## Endpoints Producto

### POST `/api/v1/productos`
Crea un nuevo producto.

**JSON de entrada:**
```json
{
    "titulo": "El Señor de los Anillos",
    "autor": "J. R. R. Tolkien",
    "editorial": "Minotauro",
    "descripcion": "Novela épica de fantasía ambientada en la Tierra Media.",
    "isbn": "9788445073807",
    "precioUnitario": 25990,
    "estado": true,
    "tipoProducto": "LIBRO",
    "categorias": [
        {"idCategoria": 1}
    ]
}
```

---

### GET `/api/v1/productos`
Lista todos los productos del catálogo.

---

### GET `/api/v1/productos/{id}`
Obtiene un producto por su id.

---

### GET `/api/v1/productos/autor/{autor}`
Filtra productos por autor.

---

### GET `/api/v1/productos/editorial/{editorial}`
Filtra productos por editorial.

---

### GET `/api/v1/productos/categoria/{nombreCategoria}`
Filtra productos por categoría o género.

---

### GET `/api/v1/productos/precio?precioMin={min}&precioMax={max}`
Filtra productos por rango de precio.

**Ejemplos:**
```
GET /api/v1/productos/precio?precioMin=10000&precioMax=30000
GET /api/v1/productos/precio?precioMax=20000
GET /api/v1/productos/precio?precioMin=15000
```

---

### PUT `/api/v1/productos/{id}`
Actualiza un producto existente incluyendo sus categorías.

**JSON de entrada:**
```json
{
    "titulo": "El Señor de los Anillos",
    "autor": "J. R. R. Tolkien",
    "editorial": "Minotauro",
    "descripcion": "Novela épica de fantasía ambientada en la Tierra Media.",
    "isbn": "9788445073807",
    "precioUnitario": 25990,
    "estado": true,
    "tipoProducto": "LIBRO",
    "categorias": [
        {"idCategoria": 1},
        {"idCategoria": 2}
    ]
}
```

---

## Endpoints Categoría

### POST `/api/v1/categorias`
Crea una nueva categoría.

**JSON de entrada:**
```json
{
    "nombreCategoria": "Terror",
    "tipoProducto": "LIBRO"
}
```

---

### GET `/api/v1/categorias`
Lista todas las categorías.

---

### GET `/api/v1/categorias/{id}`
Obtiene una categoría por su id.

---

### DELETE `/api/v1/categorias/{id}`
Elimina una categoría por su id.

---

## Endpoints Reseña

### POST `/api/v1/resenias`
Crea una nueva reseña para un producto.

---

### GET `/api/v1/resenias`
Lista todas las reseñas.

---

### GET `/api/v1/resenias/{id}`
Obtiene una reseña por su id.

---

### DELETE `/api/v1/resenias/{id}`
Elimina una reseña por su id.

---

## Endpoints Catálogo

### POST `/api/v1/catalogo`
Crea un nuevo catálogo.

---

### GET `/api/v1/catalogo`
Lista todos los catálogos con sus productos.

---

### GET `/api/v1/catalogo/{id}`
Obtiene un catálogo por su id.

---

### DELETE `/api/v1/catalogo/{id}`
Elimina un catálogo por su id.

---

## Dependencias
Este MS no depende de ningún otro microservicio.