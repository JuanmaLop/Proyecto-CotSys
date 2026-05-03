package com.udeateampro.security;

import java.io.IOException;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.udeateampro.entity.JwtToken;
import com.udeateampro.entity.Usuario;
import com.udeateampro.repository.JwtTokenRepository;
import com.udeateampro.repository.UsuarioRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;

@Component
public class JwtAuthFilter extends OncePerRequestFilter{
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final JwtTokenRepository tokenRepository;
    private final UsuarioRepository usuarioRepository;

    public JwtAuthFilter(JwtService jwtService, @Lazy UserDetailsService userDetailsService, 
                         JwtTokenRepository tokenRepository, UsuarioRepository usuarioRepository) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.tokenRepository = tokenRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String path = request.getServletPath();
        if (shouldSkipFilter(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        if (isInvalidAuthorizationHeader(authHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwtToken = authHeader.substring(7);
        final String usuarioEmail = jwtService.extractEmail(jwtToken);
        if (isAuthenticationAlreadySet(usuarioEmail)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!authenticateToken(jwtToken, usuarioEmail)) {
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean shouldSkipFilter(String path) {
        return path.equals("/api/auth/login") || path.equals("/api/auth/refresh-token");
    }

    private boolean isInvalidAuthorizationHeader(String authHeader) {
        return authHeader == null || !authHeader.startsWith("Bearer ");
    }

    private boolean isAuthenticationAlreadySet(String usuarioEmail) {
        return usuarioEmail == null || SecurityContextHolder.getContext().getAuthentication() != null;
    }

    private boolean authenticateToken(String jwtToken, String usuarioEmail) {
        if (isTokenMissingOrInvalid(jwtToken)) {
            return false;
        }

        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(usuarioEmail);
        final Optional<Usuario> usuario = usuarioRepository.findByEmail(userDetails.getUsername());
        if (usuario.isEmpty()) {
            return false;
        }

        if (!jwtService.isTokenValid(jwtToken, usuario.get())) {
            return false;
        }

        final var authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        return true;
    }

    private boolean isTokenMissingOrInvalid(String jwtToken) {
        return tokenRepository.findByToken(jwtToken)
                .filter(token -> !token.isExpirado() && !token.isRevocado())
                .isEmpty();
    }
}

