package com.ecomarket.producto.controller;

import com.ecomarket.producto.model.Producto;
import com.ecomarket.producto.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController productoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearProducto() {
        Producto producto = new Producto(null, "Laptop", "Laptop gamer", new BigDecimal("1500.00"), 20);
        Producto creado = new Producto(1L, "Laptop", "Laptop gamer", new BigDecimal("1500.00"), 20);
        when(productoService.crearProducto(producto)).thenReturn(creado);

        ResponseEntity<Producto> response = productoController.crearProducto(producto);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(creado, response.getBody());
    }

    @Test
    void testObtenerProductoPorId() {
        Producto producto = new Producto(1L, "Laptop", "Laptop gamer", new BigDecimal("1500.00"), 20);
        when(productoService.obtenerProductoPorId(1L)).thenReturn(Optional.of(producto));

        ResponseEntity<Producto> response = productoController.obtenerProductoPorId(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(producto, response.getBody());
    }

    @Test
    void testObtenerProductoPorIdNoEncontrado() {
        when(productoService.obtenerProductoPorId(1L)).thenReturn(Optional.empty());

        ResponseEntity<Producto> response = productoController.obtenerProductoPorId(1L);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testObtenerTodosProductos() {
        List<Producto> productos = List.of(
                new Producto(1L, "Laptop", "Laptop gamer", new BigDecimal("1500.00"), 20),
                new Producto(2L, "Smartphone", "Smartphone nuevo", new BigDecimal("800.00"), 50)
        );
        when(productoService.obtenerTodosProductos()).thenReturn(productos);

        ResponseEntity<List<Producto>> response = productoController.obtenerTodosProductos();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }
}