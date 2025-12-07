package com.udeateampro.service;

import java.util.List;

import com.udeateampro.controller.dto.UpdateUserRequest;
import com.udeateampro.controller.dto.UsuarioResponse;
import com.udeateampro.entity.Usuario;
import com.udeateampro.repository.UsuarioRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public List<UsuarioResponse> getAllUsuarios() {
        return usuarioRepository.findAllExceptAdministrador().stream()
                .map(usuario -> new UsuarioResponse(
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getEmail(),
                        usuario.getRol(),
                        usuario.isEstado()))
                .toList();
    }

    public List<UsuarioResponse> updateUser(List<UpdateUserRequest> listaUsuario) {
        List<UsuarioResponse> usuarioResponses = new java.util.ArrayList<>();
        for (UpdateUserRequest usuario : listaUsuario) {
            Usuario existingUser = usuarioRepository.findByEmail(usuario.email())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + usuario.email()));
            existingUser.setRol(usuario.rol());
            existingUser.setEstado(usuario.estado());
            Usuario saved = usuarioRepository.save(existingUser);
            usuarioResponses.add(new UsuarioResponse(
                    saved.getId(),
                    saved.getNombre(),
                    saved.getEmail(),
                    saved.getRol(),
                    saved.isEstado()));
        }
        return usuarioResponses;
    }

}
