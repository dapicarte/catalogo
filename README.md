# BookPoint · Microservicio de Catálogo (`ms-catalogo`)

Microservicio encargado de la gestión del catálogo de productos dentro del sistema **BookPoint**. Expone una API REST con soporte **HATEOAS** y administra libros, útiles escolares, categorías y reseñas de productos.

---

## 🛠️ Tecnologías

- **Java 24**
- **Spring Boot 4.0.6**
- **Spring Web MVC**
- **Spring HATEOAS** — respuestas con `EntityModel` / `CollectionModel`
- **Spring Data JPA**
- **MySQL** (entorno de producción/desarrollo)
- **H2** (base de datos en memoria para tests)
- **Lombok** — reducción de código boilerplate
- **Jackson** — serialización/deserialización JSON
- **Spring Boot Actuator** — monitoreo del microservicio
- **JUnit 5 + Mockito** — pruebas unitarias e integración

---

## 🏗️ Rol en la arquitectura

`ms-catalogo` es un microservicio proveedor — no consume otros microservicios. Es consultado por otros MS del ecosistema que necesitan información de productos.

```
ms-inventario ──► ms-catalogo (8090)
```

---

## ✅ Requisitos previos

- JDK 24 o superior
- Maven 3.8+
- MySQL en ejecución (para el perfil por defecto)

---

## ⚙️ Configuración

### `src/main/resources/application.properties`

```properties
spring.application.name=catalogo
server.port=8090

spring.datasource.url=jdbc:mysql://localhost:3306/catalogodb
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

### `src/test/resources/application-test.properties`

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
```

---

## 🌐 Acceso vía API Gateway

```
GET http://localhost:8080/api/v1/productos
```

Rutas configuradas en el gateway:

```yaml
- id: ms-catalogo
  uri: http://localhost:8090
  predicates:
    - Path=/api/v1/catalogo,/api/v1/catalogo/**,/api/v1/productos,/api/v1/productos/**,/api/v1/categoria,/api/v1/categoria/**,/api/v1/resenias,/api/v1/resenias/**
```

---

## 📡 Endpoints

### Catálogo — Base: `/api/v1/catalogo`

| Método | Endpoint | Descripción | Código éxito |
|--------|----------|-------------|--------------|
| `GET` | `/api/v1/catalogo` | Listar todos los catálogos | `200 OK` |
| `GET` | `/api/v1/catalogo/{id}` | Obtener catálogo por ID | `200 OK` |
| `POST` | `/api/v1/catalogo` | Crear nuevo catálogo | `200 OK` |
| `PUT` | `/api/v1/catalogo/{id}` | Actualizar catálogo | `200 OK` |
| `DELETE` | `/api/v1/catalogo/{id}` | Eliminar catálogo | `204 No Content` |

### Producto — Base: `/api/v1/productos`

| Método | Endpoint | Descripción | Código éxito |
|--------|----------|-------------|--------------|
| `GET` | `/api/v1/productos` | Listar todos los productos | `200 OK` |
| `GET` | `/api/v1/productos/{id}` | Obtener producto por ID | `200 OK` |
| `GET` | `/api/v1/productos/autor/{autor}` | Filtrar por autor | `200 OK` |
| `GET` | `/api/v1/productos/editorial/{editorial}` | Filtrar por editorial | `200 OK` |
| `GET` | `/api/v1/productos/categoria/{nombreCategoria}` | Filtrar por categoría | `200 OK` |
| `GET` | `/api/v1/productos/precio?precioMin={min}&precioMax={max}` | Filtrar por rango de precio | `200 OK` |
| `POST` | `/api/v1/productos` | Crear nuevo producto | `200 OK` |
| `PUT` | `/api/v1/productos/{id}` | Actualizar producto | `200 OK` |
| `DELETE` | `/api/v1/productos/{id}` | Eliminar producto | `204 No Content` |

### Categoría — Base: `/api/v1/categoria`

| Método | Endpoint | Descripción | Código éxito |
|--------|----------|-------------|--------------|
| `GET` | `/api/v1/categoria` | Listar todas las categorías | `200 OK` |
| `GET` | `/api/v1/categoria/{id}` | Obtener categoría por ID | `200 OK` |
| `POST` | `/api/v1/categoria` | Crear nueva categoría | `200 OK` |
| `DELETE` | `/api/v1/categoria/{id}` | Eliminar categoría | `204 No Content` |

### Reseña — Base: `/api/v1/resenias`

| Método | Endpoint | Descripción | Código éxito |
|--------|----------|-------------|--------------|
| `GET` | `/api/v1/resenias` | Listar todas las reseñas | `200 OK` |
| `POST` | `/api/v1/resenias/{idProducto}` | Crear reseña para un producto | `200 OK` |
| `PUT` | `/api/v1/resenias/{id}` | Actualizar reseña | `200 OK` |
| `DELETE` | `/api/v1/resenias/{id}` | Eliminar reseña | `204 No Content` |

### Ejemplo de respuesta HATEOAS

```json
{
  "idProducto": 1,
  "titulo": "El Señor de los Anillos",
  "autor": "J. R. R. Tolkien",
  "editorial": "Minotauro",
  "precioUnitario": 25990,
  "categorias": [
    {
      "idCategoria": 1,
      "nombreCategoria": "Fantasía",
      "tipoProducto": "LIBRO"
    }
  ],
  "_links": {
    "self": { "href": "http://localhost:8090/api/v1/productos/1" },
    "productos": { "href": "http://localhost:8090/api/v1/productos" }
  }
}
```

---

## 🗂️ Modelos de datos

### `Producto`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `idProducto` | `Long` | Identificador único (PK) |
| `idInventario` | `Long` | ID del inventario asociado |
| `tipoProducto` | `String` | `LIBRO` o `UTIL` |
| `titulo` | `String` | Título del producto |
| `autor` | `String` | Autor (solo libros) |
| `editorial` | `String` | Editorial (solo libros) |
| `descripcion` | `String` | Descripción del producto |
| `isbn` | `String` | ISBN (solo libros) |
| `precioUnitario` | `Integer` | Precio en pesos chilenos |
| `estado` | `boolean` | Disponibilidad del producto |

### `Categoria`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `idCategoria` | `Long` | Identificador único (PK) |
| `nombreCategoria` | `String` | Nombre de la categoría |
| `tipoProducto` | `String` | `LIBRO` o `UTIL` |

### `Resenia`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `idReseña` | `Long` | Identificador único (PK) |
| `comentario` | `String` | Comentario del cliente |
| `calificacion` | `Integer` | Calificación del producto |
| `fechaReseña` | `LocalDate` | Fecha de la reseña |

---

## 🧪 Tests

- `@ExtendWith(MockitoExtension.class)` con `@Mock` / `@InjectMocks`
- `@SpringBootTest` + `@AutoConfigureMockMvc` + `@ActiveProfiles("test")`
- Base de datos H2 en memoria para tests

---

## 📁 Estructura del proyecto

```
ms-catalogo/
├── src/
│   ├── main/
│   │   ├── java/BookPoint/catalogo/
│   │   │   ├── controller/
│   │   │   ├── exception/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   └── CatalogoApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       ├── java/BookPoint/catalogo/
│       │   ├── controller/
│       │   └── service/
│       └── resources/
│           └── application-test.properties
└── pom.xml
```

---

## 👤 Autor

Proyecto **BookPoint** — Microservicio de Catálogo.