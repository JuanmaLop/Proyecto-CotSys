package com.udeateampro.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.udeateampro.controller.dto.UpdateUserRequest;
import com.udeateampro.controller.dto.UsuarioResponse;
import com.udeateampro.entity.Usuario;
import com.udeateampro.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario1;
    private Usuario usuario2;
    private UpdateUserRequest updateRequest1;
    private UpdateUserRequest updateRequest2;

    @BeforeEach
    void setUp() {
        usuario1 = Usuario.builder()
                .id(1L)
                .nombre("Usuario Uno")
                .email("usuario1@example.com")
                .rol("VENDEDOR")
                .password("password123")
                .estado(true)
                .build();

        usuario2 = Usuario.builder()
                .id(2L)
                .nombre("Usuario Dos")
                .email("usuario2@example.com")
                .rol("COMPRADOR")
                .password("password456")
                .estado(false)
                .build();

        updateRequest1 = new UpdateUserRequest("usuario1@example.com", "GERENTE", false);
        updateRequest2 = new UpdateUserRequest("usuario2@example.com", "VENDEDOR", true);
    }

    @Test
    void getAllUsuariosShouldReturnAllNonAdminUsersAsResponses() {
        when(usuarioRepository.findAllExceptAdministrador()).thenReturn(List.of(usuario1, usuario2));

        List<UsuarioResponse> responses = usuarioService.getAllUsuarios();

        assertNotNull(responses);
        assertEquals(2, responses.size());

        UsuarioResponse response1 = responses.get(0);
        assertEquals(usuario1.getId(), response1.usuarioId());
        assertEquals(usuario1.getNombre(), response1.nombre());
        assertEquals(usuario1.getEmail(), response1.email());
        assertEquals(usuario1.getRol(), response1.rol());
        assertEquals(usuario1.isEstado(), response1.estado());

        UsuarioResponse response2 = responses.get(1);
        assertEquals(usuario2.getId(), response2.usuarioId());
        assertEquals(usuario2.getNombre(), response2.nombre());
        assertEquals(usuario2.getEmail(), response2.email());
        assertEquals(usuario2.getRol(), response2.rol());
        assertEquals(usuario2.isEstado(), response2.estado());
    }

    @Test
    void getAllUsuariosShouldReturnEmptyListWhenNoNonAdminUsers() {
        when(usuarioRepository.findAllExceptAdministrador()).thenReturn(List.of());

        List<UsuarioResponse> responses = usuarioService.getAllUsuarios();

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    @Test
    void updateUserShouldUpdateMultipleUsersAndReturnResponses() {
        Usuario updatedUsuario1 = Usuario.builder()
                .id(1L)
                .nombre("Usuario Uno")
                .email("usuario1@example.com")
                .rol("GERENTE")
                .password("password123")
                .estado(false)
                .build();

        Usuario updatedUsuario2 = Usuario.builder()
                .id(2L)
                .nombre("Usuario Dos")
                .email("usuario2@example.com")
                .rol("VENDEDOR")
                .password("password456")
                .estado(true)
                .build();

        when(usuarioRepository.findByEmail("usuario1@example.com")).thenReturn(Optional.of(usuario1));
        when(usuarioRepository.findByEmail("usuario2@example.com")).thenReturn(Optional.of(usuario2));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(updatedUsuario1, updatedUsuario2);

        List<UsuarioResponse> responses = usuarioService.updateUser(List.of(updateRequest1, updateRequest2));

        assertNotNull(responses);
        assertEquals(2, responses.size());

        UsuarioResponse response1 = responses.get(0);
        assertEquals(updatedUsuario1.getId(), response1.usuarioId());
        assertEquals(updatedUsuario1.getNombre(), response1.nombre());
        assertEquals(updatedUsuario1.getEmail(), response1.email());
        assertEquals("GERENTE", response1.rol());
        assertFalse(response1.estado());

        UsuarioResponse response2 = responses.get(1);
        assertEquals(updatedUsuario2.getId(), response2.usuarioId());
        assertEquals(updatedUsuario2.getNombre(), response2.nombre());
        assertEquals(updatedUsuario2.getEmail(), response2.email());
        assertEquals("VENDEDOR", response2.rol());
        assertTrue(response2.estado());

        verify(usuarioRepository, times(2)).save(any(Usuario.class));
    }

    @Test
    void updateUserShouldThrowExceptionWhenUserNotFound() {
        when(usuarioRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        UpdateUserRequest invalidRequest = new UpdateUserRequest("nonexistent@example.com", "VENDEDOR", true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.updateUser(List.of(invalidRequest)));

        assertEquals("Usuario no encontrado: nonexistent@example.com", exception.getMessage());
    }

    @Test
    void updateUserShouldHandleEmptyList() {
        List<UsuarioResponse> responses = usuarioService.updateUser(List.of());

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
        verify(usuarioRepository, never()).findByEmail(anyString());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void updateUserShouldUpdateUserFieldsCorrectly() {
        Usuario updatedUsuario = Usuario.builder()
                .id(1L)
                .nombre("Usuario Uno")
                .email("usuario1@example.com")
                .rol("GERENTE")
                .password("password123")
                .estado(false)
                .build();

        when(usuarioRepository.findByEmail("usuario1@example.com")).thenReturn(Optional.of(usuario1));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(updatedUsuario);

        usuarioService.updateUser(List.of(updateRequest1));

        verify(usuarioRepository).findByEmail("usuario1@example.com");
        verify(usuarioRepository).save(argThat(user -> {
            assertEquals("GERENTE", user.getRol());
            assertFalse(user.isEstado());
            return true;
        }));
    }
}
