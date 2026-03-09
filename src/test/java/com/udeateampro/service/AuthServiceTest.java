package com.udeateampro.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.udeateampro.controller.dto.CreateUserRequest;
import com.udeateampro.controller.dto.LoginRequest;
import com.udeateampro.entity.JwtToken;
import com.udeateampro.entity.Usuario;
import com.udeateampro.repository.JwtTokenRepository;
import com.udeateampro.repository.UsuarioRepository;
import com.udeateampro.security.JwtService;
import com.udeateampro.security.TokenResponse;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private JwtTokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private Usuario usuario;
    private LoginRequest loginRequest;
    private CreateUserRequest createUserRequest;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .id(1L)
                .nombre("Test User")
                .email("test@example.com")
                .rol("USER")
                .password("encodedPassword")
                .estado(true)
                .build();

        loginRequest = new LoginRequest("test@example.com", "password");

        createUserRequest = new CreateUserRequest("Test User", "test@example.com", "USER", "password");
    }

    @Test
    void loginShouldReturnTokenResponseWhenCredentialsAreValid() {
        when(usuarioRepository.findByEmail(loginRequest.email())).thenReturn(Optional.of(usuario));
        when(jwtService.generateToken(usuario)).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(usuario)).thenReturn("refreshToken");
        when(tokenRepository.findAll()).thenReturn(List.of());

        TokenResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("accessToken", response.accessToken());
        assertEquals("refreshToken", response.refreshToken());
        verify(authenticationManager).authenticate(any());
        verify(tokenRepository).save(any(JwtToken.class));
    }

    @Test
    void loginShouldThrowExceptionWhenUserNotFound() {
        when(usuarioRepository.findByEmail(loginRequest.email())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
    }

    @Test
    void createUserShouldReturnTokenResponseWhenUserIsCreated() {
        when(passwordEncoder.encode(createUserRequest.password())).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(jwtService.generateToken(usuario)).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(usuario)).thenReturn("refreshToken");

        TokenResponse response = authService.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals("accessToken", response.accessToken());
        assertEquals("refreshToken", response.refreshToken());
        verify(usuarioRepository).save(any(Usuario.class));
        verify(tokenRepository).save(any(JwtToken.class));
    }

    @Test
    void saveUserTokenShouldSaveToken() {
        String jwtToken = "testToken";

        authService.saveUserToken(usuario, jwtToken);

        verify(tokenRepository).save(any(JwtToken.class));
    }

    @Test
    void revokeAllUserTokensShouldRevokeTokensWhenTokensExist() {
        JwtToken token = JwtToken.builder()
                .id(1L)
                .token("token")
                .tipoToken(JwtToken.TokenType.BEARER)
                .usuario(1L)
                .expirado(false)
                .revocado(false)
                .build();
                
        when(tokenRepository.findAll()).thenReturn(List.of(token));

        authService.revokeAllUserTokens(usuario);

        verify(tokenRepository).saveAll(anyList());
        assertTrue(token.isExpirado());
        assertTrue(token.isRevocado());
    }

    @Test
    void revokeAllUserTokensShouldDoNothingWhenNoTokens() {
        when(tokenRepository.findAll()).thenReturn(List.of());

        authService.revokeAllUserTokens(usuario);

        verify(tokenRepository, never()).saveAll(anyList());
    }

    @Test
    void refreshTokenShouldReturnNewTokenResponseWhenValidRefreshToken() {
        String authHeader = "Bearer refreshToken";
        when(jwtService.extractEmail("refreshToken")).thenReturn("test@example.com");
        when(usuarioRepository.findByEmail("test@example.com")).thenReturn(Optional.of(usuario));
        when(jwtService.isTokenValid("refreshToken", usuario)).thenReturn(true);
        when(jwtService.generateToken(usuario)).thenReturn("newAccessToken");
        when(tokenRepository.findAll()).thenReturn(List.of());

        TokenResponse response = authService.refreshToken(authHeader);

        assertNotNull(response);
        assertEquals("newAccessToken", response.accessToken());
        assertEquals("refreshToken", response.refreshToken());
        verify(tokenRepository).save(any(JwtToken.class));
    }

    @Test
    void refreshTokenShouldThrowExceptionWhenAuthHeaderIsNull() {
        assertThrows(IllegalArgumentException.class, () -> authService.refreshToken(null));
    }

    @Test
    void refreshTokenShouldThrowExceptionWhenAuthHeaderDoesNotStartWithBearer() {
        assertThrows(IllegalArgumentException.class, () -> authService.refreshToken("Invalid"));
    }

    @Test
    void refreshTokenShouldThrowExceptionWhenEmailIsNull() {
        String authHeader = "Bearer refreshToken";
        when(jwtService.extractEmail("refreshToken")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> authService.refreshToken(authHeader));
    }

    @Test
    void refreshTokenShouldThrowExceptionWhenUserNotFound() {
        String authHeader = "Bearer refreshToken";
        when(jwtService.extractEmail("refreshToken")).thenReturn("test@example.com");
        when(usuarioRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> authService.refreshToken(authHeader));
    }

    @Test
    void refreshTokenShouldThrowExceptionWhenTokenIsInvalid() {
        String authHeader = "Bearer refreshToken";
        when(jwtService.extractEmail("refreshToken")).thenReturn("test@example.com");
        when(usuarioRepository.findByEmail("test@example.com")).thenReturn(Optional.of(usuario));
        when(jwtService.isTokenValid("refreshToken", usuario)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> authService.refreshToken(authHeader));
    }
}