package com.udeateampro.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udeateampro.controller.dto.CreateUserRequest;
import com.udeateampro.controller.dto.LoginRequest;
import com.udeateampro.security.TokenResponse;
import com.udeateampro.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody final LoginRequest request) {
        final TokenResponse token = authService.login(request);
        return ResponseEntity.ok(token);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/create-user")
    public ResponseEntity<TokenResponse> createUser(@RequestBody final CreateUserRequest request) {
        final TokenResponse token = authService.createUser(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh-token")
    public TokenResponse refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) final String authHeader) {
        return authService.refreshToken(authHeader);
    }
}
