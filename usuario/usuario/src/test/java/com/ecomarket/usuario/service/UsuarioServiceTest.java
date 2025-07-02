package com.ecomarket.usuario.service;

import com.ecomarket.usuario.model.Usuario;
import com.ecomarket.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    public UsuarioServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearUsuario() {
        Usuario usuario = new Usuario(null, "Ana", "ana@example.com", "pass", "123456");
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.empty());
        when(usuarioRepository.save(usuario)).thenReturn(new Usuario(1L, "Ana", "ana@example.com", "pass", "123456"));

        Usuario creado = usuarioService.crearUsuario(usuario);
        assertNotNull(creado);
        assertEquals(1L, creado.getId());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void testCrearUsuarioDuplicado() {
        Usuario usuario = new Usuario(null, "Ana", "ana@example.com", "pass", "123456");
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.crearUsuario(usuario);
        });
        assertEquals("El email ya está registrado", exception.getMessage());
    }

    @Test
    void testObtenerUsuarioPorId() {
        Usuario usuario = new Usuario(1L, "Ana", "ana@example.com", "pass", "123456");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioService.obtenerUsuarioPorId(1L);
        assertTrue(resultado.isPresent());
        assertEquals("Ana", resultado.get().getNombre());
    }

    @Test
    void testObtenerTodosUsuarios() {
        List<Usuario> usuarios = List.of(
                new Usuario(1L, "Ana", "ana@example.com", "pass", "123456"),
                new Usuario(2L, "Juan", "juan@example.com", "pass2", "654321")
        );
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> resultado = usuarioService.obtenerTodosUsuarios();
        assertEquals(2, resultado.size());
    }

    @Test
    void testEliminarUsuario() {
        doNothing().when(usuarioRepository).deleteById(1L);
        usuarioService.eliminarUsuario(1L);
        verify(usuarioRepository, times(1)).deleteById(1L);
    }
}