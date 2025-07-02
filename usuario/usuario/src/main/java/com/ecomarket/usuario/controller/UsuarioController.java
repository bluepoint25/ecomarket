package com.ecomarket.usuario.controller;

import com.ecomarket.usuario.dto.ProductoDTO;
import com.ecomarket.usuario.dto.UsuarioDTO;
import com.ecomarket.usuario.service.ProductoClient;
import com.ecomarket.usuario.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuarios", description = "Operaciones relacionadas con usuarios")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final ProductoClient productoClient;

    public UsuarioController(UsuarioService usuarioService, ProductoClient productoClient) {
        this.usuarioService = usuarioService;
        this.productoClient = productoClient;
    }

    @Operation(summary = "Crear un nuevo usuario")
    @PostMapping
    public ResponseEntity<UsuarioDTO> crearUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO creado = usuarioService.crearUsuario(usuarioDTO);
        return ResponseEntity.ok(creado);
    }

    @Operation(summary = "Obtener usuario por ID")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioPorId(@PathVariable Long id) {
        return usuarioService.obtenerUsuarioPorId(id)
                .map(UsuarioDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener todos los usuarios")
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> obtenerTodosUsuarios() {
        return ResponseEntity.ok(usuarioService.obtenerTodosUsuarios());
    }

    @Operation(summary = "Actualizar un usuario existente")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO actualizado = usuarioService.actualizarUsuario(id, usuarioDTO);
        return ResponseEntity.ok(actualizado);
    }

    @Operation(summary = "Eliminar un usuario por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar usuario por email")
    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioDTO> buscarPorEmail(@PathVariable String email) {
        return usuarioService.buscarPorEmail(email)
                .map(UsuarioDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener producto desde microservicio Producto")
    @GetMapping("/producto/{id}")
    public ResponseEntity<ProductoDTO> obtenerProductoDesdeUsuario(@PathVariable Long id) {
        ProductoDTO producto = productoClient.obtenerProductoPorId(id);
        if (producto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(producto);
     }
}