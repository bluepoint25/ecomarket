package com.ecomarket.producto.controller;

import com.ecomarket.producto.dto.ProductoDTO;
import com.ecomarket.producto.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Productos", description = "Operaciones relacionadas con productos")
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @Operation(summary = "Crear un nuevo producto")
    @PostMapping
    public ResponseEntity<ProductoDTO> crearProducto(@Valid @RequestBody ProductoDTO productoDTO) {
        ProductoDTO creado = productoService.crearProducto(productoDTO);
        return ResponseEntity.ok(creado);
    }

    @Operation(summary = "Obtener producto por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerProductoPorId(@PathVariable Long id) {
        return productoService.obtenerProductoPorId(id)
                .map(ProductoDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener todos los productos")
    @GetMapping
    public ResponseEntity<List<ProductoDTO>> obtenerTodosProductos() {
        return ResponseEntity.ok(productoService.obtenerTodosProductos());
    }

    @Operation(summary = "Actualizar un producto existente")
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizarProducto(@PathVariable Long id, @Valid @RequestBody ProductoDTO productoDTO) {
        ProductoDTO actualizado = productoService.actualizarProducto(id, productoDTO);
        return ResponseEntity.ok(actualizado);
    }

    @Operation(summary = "Eliminar un producto por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}