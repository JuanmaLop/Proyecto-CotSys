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
            @NonNull FilterChain filterChain) throws ServletException, IOException  {
        
                // Saltar el filtro solo para rutas públicas específicas
                final String path = request.getServletPath();
                if (path.equals("/api/auth/login") || path.equals("/api/auth/refresh-token")) {
                    filterChain.doFilter(request, response);
                    return;
                }

                final String authHeader = request.getHeader("Authorization");
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    filterChain.doFilter(request, response);
                    return;
                }

                final String jwtToken = authHeader.substring(7);
                final String usuarioEmail = jwtService.extractEmail(jwtToken);
                if (usuarioEmail == null || SecurityContextHolder.getContext().getAuthentication() != null) {
                    filterChain.doFilter(request, response);
                    return;
                }

                final JwtToken token = tokenRepository.findByToken(jwtToken)
                        .orElse(null);
                if (token == null || token.isExpirado() || token.isRevocado()) {
                    filterChain.doFilter(request, response);
                    return;
                }

                final UserDetails userDetails = this.userDetailsService.loadUserByUsername(usuarioEmail);
                final Optional<Usuario> usuario = usuarioRepository.findByEmail(userDetails.getUsername());
                if (usuario.isEmpty()) {
                    filterChain.doFilter(request, response);
                    return;
                }

                final boolean isTokenValid = jwtService.isTokenValid(jwtToken, usuario.get());
                if (!isTokenValid){
                    filterChain.doFilter(request, response);
                    return;
                }

                final var authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
                
                filterChain.doFilter(request, response);
    }
}   
