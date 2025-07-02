package com.ecomarket.usuario.service;

import com.ecomarket.usuario.dto.UsuarioDTO;
import com.ecomarket.usuario.model.Usuario;
import com.ecomarket.usuario.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.findByEmail(usuarioDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        Usuario usuario = usuarioDTO.toEntity();
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        Usuario guardado = usuarioRepository.save(usuario);
        return UsuarioDTO.fromEntity(guardado);
    }

    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public List<UsuarioDTO> obtenerTodosUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuarioActualizado = usuarioRepository.findById(id).map(usuario -> {
            usuario.setNombre(usuarioDTO.getNombre());
            usuario.setEmail(usuarioDTO.getEmail());
            if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().isEmpty()) {
                usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
            }
            usuario.setTelefono(usuarioDTO.getTelefono());
            return usuarioRepository.save(usuario);
        }).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return UsuarioDTO.fromEntity(usuarioActualizado);
    }

    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + username));
        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword())
                .roles("USER") // Puedes agregar roles si quieres
                .build();
    }
}