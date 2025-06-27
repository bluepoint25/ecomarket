//Tabla USUARIOS
//1. Insertar usuario 

INSERT INTO USUARIOS (NOMBRE, EMAIL, PASSWORD, TELEFONO)
VALUES ('Ana Gómez', 'ana.gomez@example.com', 'password123', '987654321');
//2. Consultar usuario por ID
SELECT * FROM USUARIOS WHERE ID = 1;

//3. Consultar todos los usuarios
SELECT * FROM USUARIOS;

//4. Actualizar usuario
UPDATE USUARIOS
SET NOMBRE = 'Ana Gómez Actualizada',
    EMAIL = 'ana.gomez@example.com',
    PASSWORD = 'newpassword123',
    TELEFONO = '123456789'
WHERE ID = 1;

//5. Eliminar usuario
DELETE FROM USUARIOS WHERE ID = 1;

//6. Buscar usuario por email
SELECT * FROM USUARIOS WHERE EMAIL = 'ana.gomez@example.com';


//Tabla PRODUCTOS
//1. Insertar producto
INSERT INTO PRODUCTOS (NOMBRE, DESCRIPCION, PRECIO, STOCK)
VALUES ('Smartphone', 'Smartphone de última generación', 799.99, 50);

//2. Consultar producto por ID
SELECT * FROM PRODUCTOS WHERE ID = 1;

//3. Consultar todos los productos
SELECT * FROM PRODUCTOS;

//4. Actualizar producto
UPDATE PRODUCTOS
SET NOMBRE = 'Smartphone Actualizado',
    DESCRIPCION = 'Smartphone con mejoras',
    PRECIO = 749.99,
    STOCK = 45
WHERE ID = 1;

//5. Eliminar producto
DELETE FROM PRODUCTOS WHERE ID = 1;

//6. Buscar productos por nombre (contiene texto)
SELECT * FROM PRODUCTOS WHERE LOWER(NOMBRE) LIKE '%smartphone%';

/*Notas
Ajusta los valores de ID según los registros que tengas.
/*