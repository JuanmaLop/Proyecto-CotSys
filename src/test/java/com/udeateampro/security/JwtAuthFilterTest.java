package com.udeateampro.security;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.udeateampro.entity.JwtToken;
import com.udeateampro.entity.Usuario;
import com.udeateampro.repository.JwtTokenRepository;
import com.udeateampro.repository.UsuarioRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtTokenRepository tokenRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    private Usuario usuario;
    private JwtToken jwtToken;

    @BeforeEach
    public void setUp() {
        SecurityContextHolder.clearContext();

        usuario = Usuario.builder()
                .id(1L)
                .email("test@example.com")
                .nombre("Test User")
                .rol("USUARIO")
                .password("encodedPassword")
                .estado(true)
                .build();

        jwtToken = JwtToken.builder()
                .id(1L)
                .token("valid.jwt.token")
                .revocado(false)
                .expirado(false)
                .usuario(1L)
                .build();
    }

    @Test
    void testDoFilterInternalWithPublicPathLogin() throws ServletException, IOException {
        // Given
        when(request.getServletPath()).thenReturn("/api/auth/login");

        // When
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        verify(jwtService, never()).extractEmail(anyString());
    }

    @Test
    void testDoFilterInternalWithPublicPathRefreshToken() throws ServletException, IOException {
        // Given
        when(request.getServletPath()).thenReturn("/api/auth/refresh-token");

        // When
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        verify(jwtService, never()).extractEmail(anyString());
    }

    @Test
    void testDoFilterInternalWithoutAuthorizationHeader() throws ServletException, IOException {
        // Given
        when(request.getServletPath()).thenReturn("/api/clientes");
        when(request.getHeader("Authorization")).thenReturn(null);

        // When
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternalWithInvalidAuthorizationHeader() throws ServletException, IOException {
        // Given
        when(request.getServletPath()).thenReturn("/api/clientes");
        when(request.getHeader("Authorization")).thenReturn("InvalidBearer token");

        // When
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternalWithValidToken() throws ServletException, IOException {
        // Given
        String token = "valid.jwt.token";
        when(request.getServletPath()).thenReturn("/api/clientes");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractEmail(token)).thenReturn("test@example.com");
        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(jwtToken));
        when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@example.com");
        when(usuarioRepository.findByEmail("test@example.com")).thenReturn(Optional.of(usuario));
        when(jwtService.isTokenValid(token, usuario)).thenReturn(true);

        // When
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        verify(jwtService).extractEmail(token);
        verify(tokenRepository).findByToken(token);
    }

    @Test
    void testDoFilterInternalWithRevokedToken() throws ServletException, IOException {
        // Given
        String token = "revoked.jwt.token";
        JwtToken revokedToken = JwtToken.builder()
                .id(1L)
                .token(token)
                .revocado(true)
                .expirado(false)
                .usuario(1L)
                .build();

        when(request.getServletPath()).thenReturn("/api/clientes");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractEmail(token)).thenReturn("test@example.com");
        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(revokedToken));

        // When
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        verify(usuarioRepository, never()).findByEmail(anyString());
    }

    @Test
    void testDoFilterInternalWithExpiredToken() throws ServletException, IOException {
        // Given
        String token = "expired.jwt.token";
        JwtToken expiredToken = JwtToken.builder()
                .id(1L)
                .token(token)
                .revocado(false)
                .expirado(true)
                .usuario(1L)
                .build();

        when(request.getServletPath()).thenReturn("/api/clientes");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractEmail(token)).thenReturn("test@example.com");
        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(expiredToken));

        // When
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
    void testDoFilterInternalWithNonExistentToken() throws ServletException, IOException {
        // Given
        String token = "nonexistent.jwt.token";
        when(request.getServletPath()).thenReturn("/api/clientes");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractEmail(token)).thenReturn("test@example.com");
        when(tokenRepository.findByToken(token)).thenReturn(Optional.empty());

        // When
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
    void testDoFilterInternalWithNullEmailFromToken() throws ServletException, IOException {
        // Given
        String token = "valid.jwt.token";
        when(request.getServletPath()).thenReturn("/api/clientes");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractEmail(token)).thenReturn(null);

        // When
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        verify(tokenRepository, never()).findByToken(anyString());
    }

    @Test
    void testDoFilterInternalWithUserNotFound() throws ServletException, IOException {
        // Given
        String token = "valid.jwt.token";
        when(request.getServletPath()).thenReturn("/api/clientes");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractEmail(token)).thenReturn("nonexistent@example.com");
        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(jwtToken));
        when(userDetailsService.loadUserByUsername("nonexistent@example.com")).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("nonexistent@example.com");
        when(usuarioRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // When
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        verify(jwtService, never()).isTokenValid(anyString(), any());
    }

    @Test
    void testDoFilterInternalWithInvalidTokenValidation() throws ServletException, IOException {
        // Given
        String token = "valid.jwt.token";
        when(request.getServletPath()).thenReturn("/api/clientes");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractEmail(token)).thenReturn("test@example.com");
        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(jwtToken));
        when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@example.com");
        when(usuarioRepository.findByEmail("test@example.com")).thenReturn(Optional.of(usuario));
        when(jwtService.isTokenValid(token, usuario)).thenReturn(false);

        // When
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
    }
}
