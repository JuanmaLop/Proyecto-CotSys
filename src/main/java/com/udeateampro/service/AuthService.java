package com.udeateampro.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.udeateampro.controller.CreateUserRequest;
import com.udeateampro.controller.LoginRequest;
import com.udeateampro.entity.Usuario;
import com.udeateampro.entity.JwtToken;
import com.udeateampro.repository.JwtTokenRepository;
import com.udeateampro.repository.UsuarioRepository;
import com.udeateampro.security.JwtService;
import com.udeateampro.security.TokenResponse;
import java.util.List;
import org.springframework.security.authentication.AuthenticationManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final JwtTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public TokenResponse createUser(CreateUserRequest request) {
        var usuario = Usuario.builder()
                .nombre(request.nombre())
                .email(request.email())
                .rol(request.rol())
                .password(passwordEncoder.encode(request.password()))
                .build();
        var savedUser = usuarioRepository.save(usuario);
        var jwtToken = jwtService.generateToken(savedUser);
        var refreshToken = jwtService.generateRefreshToken(savedUser);
        saveUserToken(savedUser, jwtToken);
        return new TokenResponse(jwtToken, refreshToken);
    }

    public TokenResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()));
        var usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(usuario);
        var refreshToken = jwtService.generateRefreshToken(usuario);
        revokeAllUserTokens(usuario);
        saveUserToken(usuario, jwtToken);
        return new TokenResponse(jwtToken, refreshToken);
    }
    
    public void saveUserToken(Usuario usuario, String jwtToken) {
        var token = JwtToken.builder()
        .token(jwtToken)
        .tipoToken(JwtToken.TokenType.BEARER)
        .usuario(usuario.getId())
        .expirado(false)
        .revocado(false)
        .build();
        tokenRepository.save(token);
    }

    public void revokeAllUserTokens(Usuario usuario) {
        final List<JwtToken> validUserTokens = tokenRepository.findAll().stream()
                .filter(token -> token.getUsuario().equals(usuario.getId()) && !token.isExpirado()
                        && !token.isRevocado())
                .toList();
        if (!validUserTokens.isEmpty()) {
            for (final JwtToken token : validUserTokens) {
                token.setExpirado(true);
                token.setRevocado(true);
            }
            tokenRepository.saveAll(validUserTokens);
        }
    }
}
