package com.ecomarket.usuario.controller;

import com.ecomarket.usuario.config.JwtTokenProvider;
import com.ecomarket.usuario.dto.LoginRequest;
import com.ecomarket.usuario.dto.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            String token = jwtTokenProvider.generateToken(loginRequest.getUsername());
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Usuario o contraseña inválidos");
        }
    }
}