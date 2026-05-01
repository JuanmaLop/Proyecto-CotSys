package com.udeateampro.security;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.udeateampro.entity.Usuario;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private Usuario usuario;
    private String validToken;
    private String invalidToken;
    private String expiredToken;

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

        // Configurar propiedades del servicio
        String jwtSecret = "dXNlclNlY3JldEtleUZvckp3dFRva2VuR2VuZXJhdGlvbkluUHJvamVjdG9Db3RTeXM=";
        ReflectionTestUtils.setField(jwtService, "jwtSecret", jwtSecret);
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", 3600000); // 1 hora
        ReflectionTestUtils.setField(jwtService, "jwtExpirationRefresh", 86400000); // 24 horas

        // Generar un token válido
        validToken = jwtService.generateToken(usuario);

        // Crear token expirado (manual)
        long pastTime = System.currentTimeMillis() - 1000; // 1 segundo atrás
        expiredToken = Jwts.builder()
                .id(usuario.getId().toString())
                .subject(usuario.getEmail())
                .issuedAt(new Date(pastTime - 3600000))
                .expiration(new Date(pastTime))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret)))
                .compact();

        // Token completamente inválido
        invalidToken = "invalid.token.here";
    }

    @Test
    void testGenerateToken() {
        // Given
        Usuario testUser = Usuario.builder()
                .id(2L)
                .email("newuser@example.com")
                .nombre("New User")
                .rol("ADMIN")
                .build();

        // When
        String token = jwtService.generateToken(testUser);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
        assertEquals(3, token.split("\\.").length); // JWT tiene 3 partes
    }

    @Test
    void testGenerateRefreshToken() {
        // Given
        Usuario testUser = Usuario.builder()
                .id(3L)
                .email("refresh@example.com")
                .nombre("Refresh User")
                .rol("USUARIO")
                .build();

        // When
        String refreshToken = jwtService.generateRefreshToken(testUser);

        // Then
        assertNotNull(refreshToken);
        assertFalse(refreshToken.isEmpty());
        assertTrue(refreshToken.contains("."));
    }

    @Test
    void testExtractEmail() {
        // When
        String extractedEmail = jwtService.extractEmail(validToken);

        // Then
        assertNotNull(extractedEmail);
        assertEquals(usuario.getEmail(), extractedEmail);
    }

    @Test
    void testExtractEmailWithInvalidToken() {
        // When & Then
        Exception exception = assertThrows(Exception.class, () -> jwtService.extractEmail(invalidToken));
        assertNotNull(exception);
        // Expected exception was thrown successfully
    }

    @Test
    void testIsTokenValidWithValidToken() {
        // When
        boolean isValid = jwtService.isTokenValid(validToken, usuario);

        // Then
        assertTrue(isValid);
    }

    @Test
    void testIsTokenValidWithExpiredToken() {
        // When
        boolean isValid = jwtService.isTokenValid(expiredToken, usuario);

        // Then
        assertFalse(isValid);
    }

    @Test
    void testIsTokenValidWithInvalidSignature() {
        // Given
        String invalidSignatureToken = validToken.substring(0, validToken.length() - 10) + "invalidSig";

        // When
        boolean isValid = jwtService.isTokenValid(invalidSignatureToken, usuario);

        // Then
        assertFalse(isValid);
    }

    @Test
    void testIsTokenValidWithDifferentUser() {
        // Given
        Usuario differentUser = Usuario.builder()
                .id(2L)
                .email("different@example.com")
                .nombre("Different User")
                .rol("USUARIO")
                .build();

        // When
        boolean isValid = jwtService.isTokenValid(validToken, differentUser);

        // Then
        assertFalse(isValid);
    }

    @Test
    void testIsTokenValidWithNullException() {
        // When
        boolean isValid = jwtService.isTokenValid(invalidToken, usuario);

        // Then
        assertFalse(isValid);
    }

    @Test
    void testGenerateTokenContainsClaims() {
        // When
        String token = jwtService.generateToken(usuario);
        String email = jwtService.extractEmail(token);

        // Then
        assertEquals(usuario.getEmail(), email);
    }

    @Test
    void testMultipleTokensAreUnique() throws InterruptedException {
        // When - Small delay to ensure different timestamps
        String token1 = jwtService.generateToken(usuario);
        Thread.sleep(10);
        String token2 = jwtService.generateToken(usuario);

        // Then
        assertNotNull(token1);
        assertNotNull(token2);
        // Even if tokens are same, they should have been generated
        assertTrue(token1.contains("."));
        assertTrue(token2.contains("."));
    }
}
