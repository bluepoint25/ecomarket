package com.ecomarket.usuario.controller;

import com.ecomarket.usuario.dto.UsuarioDTO;
import com.ecomarket.usuario.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private UsuarioDTO usuarioDTO;

    @BeforeEach
    public void setup() {
        usuarioDTO = new UsuarioDTO(1L, "Juan Perez", "juan@example.com", "password123", "123456789");
    }

    @Test
    public void testCrearUsuario() throws Exception {
        Mockito.when(usuarioService.crearUsuario(any(UsuarioDTO.class))).thenReturn(usuarioDTO);

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(usuarioDTO.getId().intValue())))
                .andExpect(jsonPath("$.nombre", is(usuarioDTO.getNombre())))
                .andExpect(jsonPath("$.email", is(usuarioDTO.getEmail())));
    }

    @Test
    public void testObtenerUsuarioPorId_Existente() throws Exception {
        Mockito.when(usuarioService.obtenerUsuarioPorId(1L)).thenReturn(Optional.of(usuarioDTO.toEntity()));

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(usuarioDTO.getId().intValue())))
                .andExpect(jsonPath("$.nombre", is(usuarioDTO.getNombre())))
                .andExpect(jsonPath("$.email", is(usuarioDTO.getEmail())));
    }

    @Test
    public void testObtenerUsuarioPorId_NoExistente() throws Exception {
        Mockito.when(usuarioService.obtenerUsuarioPorId(2L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuarios/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testObtenerTodosUsuarios() throws Exception {
        UsuarioDTO usuario2 = new UsuarioDTO(2L, "Ana Gomez", "ana@example.com", "pass456", "987654321");
        Mockito.when(usuarioService.obtenerTodosUsuarios()).thenReturn(Arrays.asList(usuarioDTO, usuario2));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is(usuarioDTO.getNombre())))
                .andExpect(jsonPath("$[1].nombre", is(usuario2.getNombre())));
    }

    @Test
    public void testActualizarUsuario() throws Exception {
        UsuarioDTO actualizado = new UsuarioDTO(1L, "Juan Actualizado", "juanact@example.com", "newpass", "111222333");
        Mockito.when(usuarioService.actualizarUsuario(eq(1L), any(UsuarioDTO.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is(actualizado.getNombre())))
                .andExpect(jsonPath("$.email", is(actualizado.getEmail())));
    }

    @Test
    public void testEliminarUsuario() throws Exception {
        Mockito.doNothing().when(usuarioService).eliminarUsuario(1L);

        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent());
    }
}