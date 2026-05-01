package com.udeateampro.security;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.udeateampro.entity.Usuario;
import com.udeateampro.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private SecurityConfig securityConfig;

    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        usuario = Usuario.builder()
                .id(1L)
                .email("test@example.com")
                .nombre("Test User")
                .rol("USUARIO")
                .password("encodedPassword")
                .estado(true)
                .build();
    }

    @Test
    void testUserDetailsServiceWithValidUser() {
        // Given
        when(usuarioRepository.findByEmail("test@example.com")).thenReturn(Optional.of(usuario));

        UserDetailsService userDetailsService = securityConfig.userDetailsService();

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");

        // Then
        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        verify(usuarioRepository).findByEmail("test@example.com");
    }

    @Test
    void testUserDetailsServiceWithInvalidUser() {
        // Given
        when(usuarioRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        UserDetailsService userDetailsService = securityConfig.userDetailsService();

        // When & Then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistent@example.com");
        });
        assertNotNull(exception);
        verify(usuarioRepository).findByEmail("nonexistent@example.com");
    }

    @Test
    void testUserDetailsServiceRoleNormalization() {
        // Given - Usuario con rol sin prefijo ROLE_
        Usuario userWithoutPrefix = Usuario.builder()
                .id(2L)
                .email("admin@example.com")
                .nombre("Admin User")
                .rol("ADMIN")
                .password("encodedPassword")
                .estado(true)
                .build();

        when(usuarioRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(userWithoutPrefix));

        UserDetailsService userDetailsService = securityConfig.userDetailsService();

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("admin@example.com");

        // Then
        assertNotNull(userDetails);
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertTrue(authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void testAuthenticationProvider() {
        // When
        AuthenticationProvider authenticationProvider = securityConfig.authenticationProvider();

        // Then
        assertNotNull(authenticationProvider);
    }

    @Test
    void testPasswordEncoder() {
        // When
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        // Then
        assertNotNull(passwordEncoder);
        String rawPassword = "testPassword123";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }
}

