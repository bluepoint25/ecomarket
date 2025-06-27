package com.ecomarket.producto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nombre es obligatorio")
    private String nombre;

    private String descripcion;

    @NotNull(message = "Precio es obligatorio")
    private BigDecimal precio;

    @NotNull(message = "Stock es obligatorio")
    private Integer stock;
}