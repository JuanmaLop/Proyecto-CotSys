package com.udeateampro.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.udeateampro.controller.CreateUserRequest;
import com.udeateampro.entity.Usuario;
import com.udeateampro.repository.TokenRepository;
import com.udeateampro.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final TokenRepository tokenRepository; 
    private final PasswordEncoder passwordEncoder;

    /*public TokenResponse createUser(CreateUserRequest request) {
        var usuario = Usuario.builder()
                .nombre(request.getNombre())
                .email(request.getEmail())
                .rol(request.getRol())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        var savedUser = usuarioRepository.save(usuario);
    }*/
}
