package com.ecomarket.producto.service;

import com.ecomarket.producto.model.Producto;
import com.ecomarket.producto.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    public ProductoServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearProducto() {
        Producto producto = new Producto(null, "Laptop", "Laptop gamer", new BigDecimal("1500.00"), 20);
        when(productoRepository.findByNombreContainingIgnoreCase(producto.getNombre())).thenReturn(List.of());
        when(productoRepository.save(producto)).thenReturn(new Producto(1L, "Laptop", "Laptop gamer", new BigDecimal("1500.00"), 20));

        Producto creado = productoService.crearProducto(producto);
        assertNotNull(creado);
        assertEquals(1L, creado.getId());
        verify(productoRepository, times(1)).save(producto);
    }

    @Test
    void testCrearProductoDuplicado() {
        Producto producto = new Producto(null, "Laptop", "Laptop gamer", new BigDecimal("1500.00"), 20);
        when(productoRepository.findByNombreContainingIgnoreCase(producto.getNombre())).thenReturn(List.of(producto));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productoService.crearProducto(producto);
        });
        assertEquals("Ya existe un producto con ese nombre", exception.getMessage());
    }

    @Test
    void testObtenerProductoPorId() {
        Producto producto = new Producto(1L, "Laptop", "Laptop gamer", new BigDecimal("1500.00"), 20);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Optional<Producto> resultado = productoService.obtenerProductoPorId(1L);
        assertTrue(resultado.isPresent());
        assertEquals("Laptop", resultado.get().getNombre());
    }

    @Test
    void testObtenerTodosProductos() {
        List<Producto> productos = List.of(
                new Producto(1L, "Laptop", "Laptop gamer", new BigDecimal("1500.00"), 20),
                new Producto(2L, "Smartphone", "Smartphone nuevo", new BigDecimal("800.00"), 50)
        );
        when(productoRepository.findAll()).thenReturn(productos);

        List<Producto> resultado = productoService.obtenerTodosProductos();
        assertEquals(2, resultado.size());
    }

    @Test
    void testEliminarProducto() {
        doNothing().when(productoRepository).deleteById(1L);
        productoService.eliminarProducto(1L);
        verify(productoRepository, times(1)).deleteById(1L);
    }
}