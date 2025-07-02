package com.ecomarket.usuario.service;

import com.ecomarket.usuario.dto.UsuarioDTO;
import com.ecomarket.usuario.model.Usuario;
import com.ecomarket.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UsuarioServiceTest {

    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    public void setup() {
        usuarioRepository = Mockito.mock(UsuarioRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        usuarioService = new UsuarioService(usuarioRepository, passwordEncoder);

        // Mockear encode para devolver la misma cadena
        when(passwordEncoder.encode(anyString())).thenAnswer(invocation -> invocation.getArgument(0));

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan Perez");
        usuario.setEmail("juan@example.com");
        usuario.setPassword("password123");
        usuario.setTelefono("123456789");

        usuarioDTO = new UsuarioDTO(1L, "Juan Perez", "juan@example.com", "password123", "123456789");
    }

    @Test
    public void testCrearUsuario_Exitoso() {
        when(usuarioRepository.findByEmail(usuarioDTO.getEmail())).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioDTO creado = usuarioService.crearUsuario(usuarioDTO);

        assertNotNull(creado);
        assertEquals(usuarioDTO.getEmail(), creado.getEmail());

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        assertEquals(usuarioDTO.getNombre(), captor.getValue().getNombre());
    }

    @Test
    public void testCrearUsuario_EmailDuplicado() {
        when(usuarioRepository.findByEmail(usuarioDTO.getEmail())).thenReturn(Optional.of(usuario));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.crearUsuario(usuarioDTO);
        });

        assertEquals("El email ya está registrado", exception.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    public void testObtenerUsuarioPorId_Existente() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioService.obtenerUsuarioPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(usuario.getEmail(), resultado.get().getEmail());
    }

    @Test
    public void testObtenerUsuarioPorId_NoExistente() {
        when(usuarioRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioService.obtenerUsuarioPorId(2L);

        assertFalse(resultado.isPresent());
    }

    @Test
    public void testObtenerTodosUsuarios() {
        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setNombre("Ana Gomez");
        usuario2.setEmail("ana@example.com");
        usuario2.setPassword("pass456");
        usuario2.setTelefono("987654321");

        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario, usuario2));

        List<UsuarioDTO> usuarios = usuarioService.obtenerTodosUsuarios();

        assertEquals(2, usuarios.size());
        assertEquals("Juan Perez", usuarios.get(0).getNombre());
        assertEquals("Ana Gomez", usuarios.get(1).getNombre());
    }

    @Test
    public void testActualizarUsuario_Existente() {
        UsuarioDTO actualizadoDTO = new UsuarioDTO(null, "Juan Actualizado", "juanact@example.com", "newpass", "111222333");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        UsuarioDTO resultado = usuarioService.actualizarUsuario(1L, actualizadoDTO);

        assertEquals("Juan Actualizado", resultado.getNombre());
        assertEquals("juanact@example.com", resultado.getEmail());
    }

    @Test
    public void testActualizarUsuario_NoExistente() {
        when(usuarioRepository.findById(2L)).thenReturn(Optional.empty());

        UsuarioDTO actualizadoDTO = new UsuarioDTO(null, "Juan Actualizado", "juanact@example.com", "newpass", "111222333");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.actualizarUsuario(2L, actualizadoDTO);
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
    }

    @Test
    public void testEliminarUsuario() {
        doNothing().when(usuarioRepository).deleteById(1L);

        usuarioService.eliminarUsuario(1L);

        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testBuscarPorEmail_Existente() {
        when(usuarioRepository.findByEmail("juan@example.com")).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioService.buscarPorEmail("juan@example.com");

        assertTrue(resultado.isPresent());
        assertEquals("juan@example.com", resultado.get().getEmail());
    }

    @Test
    public void testBuscarPorEmail_NoExistente() {
        when(usuarioRepository.findByEmail("noexiste@example.com")).thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioService.buscarPorEmail("noexiste@example.com");

        assertFalse(resultado.isPresent());
    }
}