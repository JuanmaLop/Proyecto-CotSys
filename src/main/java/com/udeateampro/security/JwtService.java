package com.udeateampro.security;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.udeateampro.entity.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${app.jwtExpirationRefresh}")
    private int jwtExpirationRefresh;

    public String generateToken(final Usuario usuario) {
        return buildToken(usuario, jwtExpirationMs);
    }

    public String generateRefreshToken(final Usuario usuario) {
        return buildToken(usuario, jwtExpirationRefresh);
    }

    public String extractEmail(final String token) {
        final Claims claims = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    private String buildToken(final Usuario usuario, final long expiration) {
        return Jwts.builder()
                .id(usuario.getId().toString())
                .claims(Map.of("rol", usuario.getRol(), "nombre", usuario.getNombre()))
                .subject(usuario.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();

    }

    public boolean isTokenValid(final String token, final Usuario usuario) {
        final String email = extractEmail(token);
        return (email.equals(usuario.getEmail())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(final String token) {
        return extractExpiration(token).before(new Date());
    }
    
    private Date extractExpiration(final String token) {
        final Claims claims = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getExpiration();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
 
}