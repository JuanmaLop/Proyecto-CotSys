package com.udeateampro.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.udeateampro.controller.dto.CreateUserRequest;
import com.udeateampro.controller.dto.LoginRequest;
import com.udeateampro.security.TokenResponse;
import com.udeateampro.service.AuthService;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private LoginRequest loginRequest;
    private CreateUserRequest createUserRequest;
    private TokenResponse tokenResponse;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest("user@example.com", "password123");
        createUserRequest = new CreateUserRequest("John Doe", "john@example.com", "VENDEDOR", "password123");
        tokenResponse = new TokenResponse("access-token-123", "refresh-token-456");
    }

    @Test
    void loginShouldReturnTokenResponse() {
        when(authService.login(any(LoginRequest.class))).thenReturn(tokenResponse);

        ResponseEntity<TokenResponse> response = authController.login(loginRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(tokenResponse, response.getBody());
        verify(authService).login(loginRequest);
    }

    @Test
    void createUserShouldReturnTokenResponse() {
        when(authService.createUser(any(CreateUserRequest.class))).thenReturn(tokenResponse);

        ResponseEntity<TokenResponse> response = authController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(tokenResponse, response.getBody());
        verify(authService).createUser(createUserRequest);
    }

    @Test
    void refreshTokenShouldReturnTokenResponse() {
        String authHeader = "Bearer refresh-token-456";
        when(authService.refreshToken(anyString())).thenReturn(tokenResponse);

        TokenResponse response = authController.refreshToken(authHeader);

        assertNotNull(response);
        assertEquals(tokenResponse, response);
        verify(authService).refreshToken(authHeader);
    }

    @Test
    void refreshTokenShouldHandleNullHeader() {
        when(authService.refreshToken(null)).thenReturn(tokenResponse);

        TokenResponse response = authController.refreshToken(null);

        assertNotNull(response);
        assertEquals(tokenResponse, response);
        verify(authService).refreshToken(null);
    }

    @Test
    void loginShouldCallServiceWithCorrectRequest() {
        when(authService.login(loginRequest)).thenReturn(tokenResponse);

        authController.login(loginRequest);

        verify(authService).login(loginRequest);
    }

    @Test
    void createUserShouldCallServiceWithCorrectRequest() {
        when(authService.createUser(createUserRequest)).thenReturn(tokenResponse);

        authController.createUser(createUserRequest);

        verify(authService).createUser(createUserRequest);
    }
}