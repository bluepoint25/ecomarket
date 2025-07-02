package com.ecomarket.producto.dto;

import com.ecomarket.producto.model.Producto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    private BigDecimal precio;

    @NotNull(message = "El stock es obligatorio")
    private Integer stock;

    // Convierte entidad a DTO
    public static ProductoDTO fromEntity(Producto producto) {
        return new ProductoDTO(
            producto.getId(),
            producto.getNombre(),
            producto.getDescripcion(),
            producto.getPrecio(),
            producto.getStock()
        );
    }

    // Convierte DTO a entidad
    public Producto toEntity() {
        Producto producto = new Producto();
        producto.setId(this.id);
        producto.setNombre(this.nombre);
        producto.setDescripcion(this.descripcion);
        producto.setPrecio(this.precio);
        producto.setStock(this.stock);
        return producto;
    }
}