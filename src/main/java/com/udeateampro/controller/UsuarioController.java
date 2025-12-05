package com.udeateampro.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udeateampro.controller.dto.UpdateUserRequest;
import com.udeateampro.controller.dto.UsuarioResponse;
import com.udeateampro.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/get-all-users")
    public ResponseEntity<List<UsuarioResponse>> getAllUsuarios() {
        return ResponseEntity.ok(usuarioService.getAllUsuarios());
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/update-user")
    public ResponseEntity<List<UsuarioResponse>> updateUser(@RequestBody final List<UpdateUserRequest> listUsuario) {
        final List<UsuarioResponse> updatedUser = usuarioService.updateUser(listUsuario);
        return ResponseEntity.ok(updatedUser);
    }
}
