package com.udeateampro.service;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.udeateampro.controller.dto.CreateUserRequest;
import com.udeateampro.controller.dto.LoginRequest;
import com.udeateampro.entity.JwtToken;
import com.udeateampro.entity.Usuario;
import com.udeateampro.repository.JwtTokenRepository;
import com.udeateampro.repository.UsuarioRepository;
import com.udeateampro.security.JwtService;
import com.udeateampro.security.TokenResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

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

    public TokenResponse refreshToken(final String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Bearer Invalido");
        }

        final String refreshToken = authHeader.substring(7);
        final String usuarioEmail = jwtService.extractEmail(refreshToken);

        if (usuarioEmail == null) {
            throw new IllegalArgumentException("Token inválido");
        }

        final Usuario usuario = usuarioRepository.findByEmail(usuarioEmail)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (!jwtService.isTokenValid(refreshToken, usuario)) {
            throw new IllegalArgumentException("Refresh token inválido");
        }

        final String newAccessToken = jwtService.generateToken(usuario);
        revokeAllUserTokens(usuario);
        saveUserToken(usuario, newAccessToken);
        return new TokenResponse(newAccessToken, refreshToken);
    }
}
