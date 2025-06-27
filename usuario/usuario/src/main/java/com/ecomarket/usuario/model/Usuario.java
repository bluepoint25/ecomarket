package com.ecomarket.usuario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "Email es obligatorio")
    @Email(message = "Email debe ser válido")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Contraseña es obligatoria")
    private String password;

    private String telefono;
}