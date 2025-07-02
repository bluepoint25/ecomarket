package com.ecomarket.producto.service;

import com.ecomarket.producto.dto.ProductoDTO;
import com.ecomarket.producto.model.Producto;
import com.ecomarket.producto.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public ProductoDTO crearProducto(ProductoDTO productoDTO) {
        List<Producto> existentes = productoRepository.findByNombreContainingIgnoreCase(productoDTO.getNombre());
        if (!existentes.isEmpty()) {
            throw new IllegalArgumentException("Ya existe un producto con ese nombre");
        }
        Producto producto = productoDTO.toEntity();
        Producto guardado = productoRepository.save(producto);
        return ProductoDTO.fromEntity(guardado);
    }

    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }

    public List<ProductoDTO> obtenerTodosProductos() {
        return productoRepository.findAll()
                .stream()
                .map(ProductoDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public ProductoDTO actualizarProducto(Long id, ProductoDTO productoDTO) {
        Producto productoActualizado = productoRepository.findById(id).map(producto -> {
            producto.setNombre(productoDTO.getNombre());
            producto.setDescripcion(productoDTO.getDescripcion());
            producto.setPrecio(productoDTO.getPrecio());
            producto.setStock(productoDTO.getStock());
            return productoRepository.save(producto);
        }).orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        return ProductoDTO.fromEntity(productoActualizado);
    }

    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }
}