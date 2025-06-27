# ecomarket

Proyecto de microservicios para gestión de usuarios y productos, desarrollado con Spring Boot 3.5.3 y Java 17, conectado a base de datos Oracle Cloud.

---

## Descripción

Este proyecto consta de dos microservicios independientes:

- **Usuario Service:** Gestión de cuentas de usuarios (crear, leer, actualizar, eliminar).
- **Producto Service:** Gestión de productos del inventario (crear, leer, actualizar, eliminar).

Cada microservicio expone APIs REST para realizar operaciones CRUD y está conectado a una base de datos Oracle Cloud con esquema en 3ra forma normal (3NF).

---

## Tecnologías

- Java 17
- Spring Boot 3.5.3
- Spring Data JPA
- Oracle Database (Oracle Cloud)
- Lombok
- Bean Validation (Jakarta Validation)
- Maven

---

## Estructura del proyecto

Cada microservicio tiene la siguiente estructura de paquetes:

```
com.ecomarket.[usuario|producto]
  ├── controller      # Controladores REST
  ├── service         # Lógica de negocio
  ├── repository      # Repositorios JPA
  ├── model          # Entidades JPA
  └── Application.java # Clase principal Spring Boot
```

---

## Configuración

Configura la conexión a Oracle Cloud en `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:oracle:thin:@tcps://adb.sa-santiago-1.oraclecloud.com:1522/g0124d9d856f5e7_ecomarket_high.adb.oraclecloud.com?retry_count=20&retry_delay=3
spring.datasource.username=ADMIN
spring.datasource.password=Ecomarketsoftware2025
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.Oracle12cDialect

server.port=8081 # Cambiar a 8082 para el microservicio producto
```

---

## APIs disponibles

### Usuario Service (`http://localhost:8081/api/usuarios`)

- `POST /` Crear usuario
- `GET /{id}` Obtener usuario por ID
- `GET /` Obtener todos los usuarios
- `PUT /{id}` Actualizar usuario
- `DELETE /{id}` Eliminar usuario
- `GET /email/{email}` Buscar usuario por email

### Producto Service (`http://localhost:8082/api/productos`)

- `POST /` Crear producto
- `GET /{id}` Obtener producto por ID
- `GET /` Obtener todos los productos
- `PUT /{id}` Actualizar producto
- `DELETE /{id}` Eliminar producto
- `GET /buscar?nombre=valor` Buscar productos por nombre

---

## Pruebas

Se incluye una colección Postman con pruebas para todos los endpoints. Importa el archivo `ecomarket-postman-collection.json` en Postman para ejecutar las pruebas.

---

## Cómo ejecutar

1. Clona el repositorio.
2. Configura `application.properties` con tus credenciales Oracle.
3. Ejecuta cada microservicio desde su clase principal (`EcomarketUsuarioApplication` y `EcomarketProductoApplication`).
4. Usa Postman o cualquier cliente REST para probar las APIs.

---

## Contacto

Para dudas o soporte, contacta a b.monasterio@duocuc.cl

---

*Proyecto desarrollado para ecomarket - 2025*
