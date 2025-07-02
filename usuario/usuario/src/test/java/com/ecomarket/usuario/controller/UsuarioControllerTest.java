package com.ecomarket.usuario.controller;

import com.ecomarket.usuario.model.Usuario;
import com.ecomarket.usuario.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearUsuario() {
        Usuario usuario = new Usuario(null, "Ana", "ana@example.com", "pass", "123456");
        Usuario creado = new Usuario(1L, "Ana", "ana@example.com", "pass", "123456");
        when(usuarioService.crearUsuario(usuario)).thenReturn(creado);

        ResponseEntity<Usuario> response = usuarioController.crearUsuario(usuario);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(creado, response.getBody());
    }

    @Test
    void testObtenerUsuarioPorId() {
        Usuario usuario = new Usuario(1L, "Ana", "ana@example.com", "pass", "123456");
        when(usuarioService.obtenerUsuarioPorId(1L)).thenReturn(Optional.of(usuario));

        ResponseEntity<Usuario> response = usuarioController.obtenerUsuarioPorId(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(usuario, response.getBody());
    }

    @Test
    void testObtenerUsuarioPorIdNoEncontrado() {
        when(usuarioService.obtenerUsuarioPorId(1L)).thenReturn(Optional.empty());

        ResponseEntity<Usuario> response = usuarioController.obtenerUsuarioPorId(1L);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testObtenerTodosUsuarios() {
        List<Usuario> usuarios = List.of(
                new Usuario(1L, "Ana", "ana@example.com", "pass", "123456"),
                new Usuario(2L, "Juan", "juan@example.com", "pass2", "654321")
        );
        when(usuarioService.obtenerTodosUsuarios()).thenReturn(usuarios);

        ResponseEntity<List<Usuario>> response = usuarioController.obtenerTodosUsuarios();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }
}