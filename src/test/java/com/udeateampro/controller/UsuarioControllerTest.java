package com.udeateampro.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.udeateampro.controller.dto.UpdateUserRequest;
import com.udeateampro.controller.dto.UsuarioResponse;
import com.udeateampro.service.UsuarioService;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private UsuarioResponse usuarioResponse1;
    private UsuarioResponse usuarioResponse2;
    private UpdateUserRequest updateRequest1;
    private UpdateUserRequest updateRequest2;

    @BeforeEach
    void setUp() {
        usuarioResponse1 = new UsuarioResponse(
                1L,
                "Juan Pérez",
                "juan@example.com",
                "COMERCIAL",
                true
        );

        usuarioResponse2 = new UsuarioResponse(
                2L,
                "María García",
                "maria@example.com",
                "LIDER_TECNICO",
                true
        );

        updateRequest1 = new UpdateUserRequest(
                "juan@example.com",
                "ADMINISTRADOR",
                true
        );

        updateRequest2 = new UpdateUserRequest(
                "maria@example.com",
                "COMERCIAL",
                false
        );
    }

    @Test
    void getAllUsuariosShouldReturnAllUsuarios() {
        List<UsuarioResponse> usuarios = List.of(usuarioResponse1, usuarioResponse2);
        when(usuarioService.getAllUsuarios()).thenReturn(usuarios);

        ResponseEntity<List<UsuarioResponse>> response = usuarioController.getAllUsuarios();
        List<UsuarioResponse> body = response.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(body);
        assertEquals(usuarios, body);
        assertEquals(2, body.size());
        verify(usuarioService).getAllUsuarios();
    }

    @Test
    void getAllUsuariosShouldReturnEmptyListWhenNoUsuarios() {
        when(usuarioService.getAllUsuarios()).thenReturn(List.of());

        ResponseEntity<List<UsuarioResponse>> response = usuarioController.getAllUsuarios();
        List<UsuarioResponse> body = response.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(body);
        assertTrue(body.isEmpty());
        verify(usuarioService).getAllUsuarios();
    }

    @Test
    void updateUserShouldReturnUpdatedUsers() {
        List<UpdateUserRequest> updateRequests = List.of(updateRequest1, updateRequest2);
        List<UsuarioResponse> updatedUsers = List.of(
                new UsuarioResponse(1L, "Juan Pérez", "juan@example.com", "ADMINISTRADOR", true),
                new UsuarioResponse(2L, "María García", "maria@example.com", "COMERCIAL", false)
        );

        when(usuarioService.updateUser(anyList())).thenReturn(updatedUsers);

        ResponseEntity<List<UsuarioResponse>> response = usuarioController.updateUser(updateRequests);
        List<UsuarioResponse> body = response.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(body);
        assertEquals(updatedUsers, body);
        assertEquals(2, body.size());
        verify(usuarioService).updateUser(updateRequests);
    }

    @Test
    void updateUserShouldReturnEmptyListWhenNoUpdates() {
        List<UpdateUserRequest> updateRequests = List.of();

        when(usuarioService.updateUser(anyList())).thenReturn(List.of());

        ResponseEntity<List<UsuarioResponse>> response = usuarioController.updateUser(updateRequests);
        List<UsuarioResponse> body = response.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(body);
        assertTrue(body.isEmpty());
        verify(usuarioService).updateUser(updateRequests);
    }

    @Test
    void updateUserShouldHandleSingleUserUpdate() {
        List<UpdateUserRequest> updateRequests = List.of(updateRequest1);
        List<UsuarioResponse> updatedUsers = List.of(
                new UsuarioResponse(1L, "Juan Pérez", "juan@example.com", "ADMINISTRADOR", true)
        );

        when(usuarioService.updateUser(updateRequests)).thenReturn(updatedUsers);

        ResponseEntity<List<UsuarioResponse>> response = usuarioController.updateUser(updateRequests);
        List<UsuarioResponse> body = response.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(body);
        assertEquals(updatedUsers, body);
        assertEquals(1, body.size());
        assertEquals("ADMINISTRADOR", body.get(0).rol());
        verify(usuarioService).updateUser(updateRequests);
    }

    @Test
    void getAllUsuariosShouldCallService() {
        when(usuarioService.getAllUsuarios()).thenReturn(List.of());

        usuarioController.getAllUsuarios();

        verify(usuarioService).getAllUsuarios();
    }

    @Test
    void updateUserShouldCallServiceWithCorrectRequests() {
        List<UpdateUserRequest> updateRequests = List.of(updateRequest1);
        when(usuarioService.updateUser(updateRequests)).thenReturn(List.of());

        usuarioController.updateUser(updateRequests);

        verify(usuarioService).updateUser(updateRequests);
    }

    @Test
    void updateUserShouldHandleRoleChanges() {
        UpdateUserRequest roleChangeRequest = new UpdateUserRequest(
                "juan@example.com",
                "LIDER_TECNICO",
                true
        );
        List<UpdateUserRequest> updateRequests = List.of(roleChangeRequest);
        List<UsuarioResponse> updatedUsers = List.of(
                new UsuarioResponse(1L, "Juan Pérez", "juan@example.com", "LIDER_TECNICO", true)
        );

        when(usuarioService.updateUser(updateRequests)).thenReturn(updatedUsers);

        ResponseEntity<List<UsuarioResponse>> response = usuarioController.updateUser(updateRequests);
        List<UsuarioResponse> body = response.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(body);
        assertEquals("LIDER_TECNICO", body.get(0).rol());
        verify(usuarioService).updateUser(updateRequests);
    }
}