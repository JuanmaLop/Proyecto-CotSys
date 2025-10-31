package com.udeateampro.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.udeateampro.security.TokenResponse;
import com.udeateampro.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody final LoginRequest request) {
        final TokenResponse token = authService.login(request);
        return ResponseEntity.ok(token);
    }

    //Temporalmente publico para la creación de usuarios
    @PostMapping("/create-user")
    public ResponseEntity<TokenResponse> createUser(@RequestBody final CreateUserRequest request) {
        final TokenResponse token = authService.createUser(request);
        return ResponseEntity.ok(token);
    }
    
}
