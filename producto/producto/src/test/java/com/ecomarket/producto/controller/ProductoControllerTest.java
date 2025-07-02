package com.ecomarket.producto.controller;

import com.ecomarket.producto.dto.ProductoDTO;
import com.ecomarket.producto.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductoDTO productoDTO;

    @BeforeEach
    public void setup() {
        productoDTO = new ProductoDTO(1L, "Producto 1", "Descripción 1", new BigDecimal("100.00"), 10);
    }

    @Test
    public void testCrearProducto() throws Exception {
        Mockito.when(productoService.crearProducto(any(ProductoDTO.class))).thenReturn(productoDTO);

        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productoDTO.getId().intValue())))
                .andExpect(jsonPath("$.nombre", is(productoDTO.getNombre())))
                .andExpect(jsonPath("$.precio", is(100.00)));
    }

    @Test
    public void testObtenerProductoPorId_Existente() throws Exception {
        Mockito.when(productoService.obtenerProductoPorId(1L)).thenReturn(Optional.of(productoDTO.toEntity()));

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productoDTO.getId().intValue())))
                .andExpect(jsonPath("$.nombre", is(productoDTO.getNombre())))
                .andExpect(jsonPath("$.precio", is(100.00)));
    }

    @Test
    public void testObtenerProductoPorId_NoExistente() throws Exception {
        Mockito.when(productoService.obtenerProductoPorId(2L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/productos/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testObtenerTodosProductos() throws Exception {
        ProductoDTO producto2 = new ProductoDTO(2L, "Producto 2", "Descripción 2", new BigDecimal("200.00"), 5);
        Mockito.when(productoService.obtenerTodosProductos()).thenReturn(Arrays.asList(productoDTO, producto2));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is(productoDTO.getNombre())))
                .andExpect(jsonPath("$[1].nombre", is(producto2.getNombre())));
    }

    @Test
    public void testActualizarProducto() throws Exception {
        ProductoDTO actualizado = new ProductoDTO(1L, "Producto Actualizado", "Desc Actualizada", new BigDecimal("150.00"), 8);
        Mockito.when(productoService.actualizarProducto(eq(1L), any(ProductoDTO.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is(actualizado.getNombre())))
                .andExpect(jsonPath("$.precio", is(150.00)));
    }

    @Test
    public void testEliminarProducto() throws Exception {
        Mockito.doNothing().when(productoService).eliminarProducto(1L);

        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isNoContent());
    }
}