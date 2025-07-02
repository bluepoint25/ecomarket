package com.ecomarket.producto.service;

import com.ecomarket.producto.dto.ProductoDTO;
import com.ecomarket.producto.model.Producto;
import com.ecomarket.producto.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductoServiceTest {

    private ProductoRepository productoRepository;
    private ProductoService productoService;

    private Producto producto;
    private ProductoDTO productoDTO;

    @BeforeEach
    public void setup() {
        productoRepository = Mockito.mock(ProductoRepository.class);
        productoService = new ProductoService(productoRepository);

        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Producto 1");
        producto.setDescripcion("Descripción 1");
        producto.setPrecio(new BigDecimal("100.00"));
        producto.setStock(10);

        productoDTO = new ProductoDTO(1L, "Producto 1", "Descripción 1", new BigDecimal("100.00"), 10);
    }

    @Test
    public void testCrearProducto_Exitoso() {
        when(productoRepository.findByNombreContainingIgnoreCase(productoDTO.getNombre())).thenReturn(List.of());
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        ProductoDTO creado = productoService.crearProducto(productoDTO);

        assertNotNull(creado);
        assertEquals(productoDTO.getNombre(), creado.getNombre());

        ArgumentCaptor<Producto> captor = ArgumentCaptor.forClass(Producto.class);
        verify(productoRepository).save(captor.capture());
        assertEquals(productoDTO.getNombre(), captor.getValue().getNombre());
    }

    @Test
    public void testCrearProducto_NombreDuplicado() {
        when(productoRepository.findByNombreContainingIgnoreCase(productoDTO.getNombre())).thenReturn(List.of(producto));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productoService.crearProducto(productoDTO);
        });

        assertEquals("Ya existe un producto con ese nombre", exception.getMessage());
        verify(productoRepository, never()).save(any());
    }

    @Test
    public void testObtenerProductoPorId_Existente() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Optional<Producto> resultado = productoService.obtenerProductoPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(producto.getNombre(), resultado.get().getNombre());
    }

    @Test
    public void testObtenerProductoPorId_NoExistente() {
        when(productoRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Producto> resultado = productoService.obtenerProductoPorId(2L);

        assertFalse(resultado.isPresent());
    }

    @Test
    public void testObtenerTodosProductos() {
        Producto producto2 = new Producto();
        producto2.setId(2L);
        producto2.setNombre("Producto 2");
        producto2.setDescripcion("Descripción 2");
        producto2.setPrecio(new BigDecimal("200.00"));
        producto2.setStock(5);

        when(productoRepository.findAll()).thenReturn(Arrays.asList(producto, producto2));

        List<ProductoDTO> productos = productoService.obtenerTodosProductos();

        assertEquals(2, productos.size());
        assertEquals("Producto 1", productos.get(0).getNombre());
        assertEquals("Producto 2", productos.get(1).getNombre());
    }

    @Test
    public void testActualizarProducto_Existente() {
        ProductoDTO actualizadoDTO = new ProductoDTO(null, "Producto Actualizado", "Desc Actualizada", new BigDecimal("150.00"), 8);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenAnswer(i -> i.getArgument(0));

        ProductoDTO resultado = productoService.actualizarProducto(1L, actualizadoDTO);

        assertEquals("Producto Actualizado", resultado.getNombre());
        assertEquals(new BigDecimal("150.00"), resultado.getPrecio());
    }

    @Test
    public void testActualizarProducto_NoExistente() {
        when(productoRepository.findById(2L)).thenReturn(Optional.empty());

        ProductoDTO actualizadoDTO = new ProductoDTO(null, "Producto Actualizado", "Desc Actualizada", new BigDecimal("150.00"), 8);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productoService.actualizarProducto(2L, actualizadoDTO);
        });

        assertEquals("Producto no encontrado", exception.getMessage());
    }

    @Test
    public void testEliminarProducto() {
        doNothing().when(productoRepository).deleteById(1L);

        productoService.eliminarProducto(1L);

        verify(productoRepository, times(1)).deleteById(1L);
    }
}