package com.udeateampro.controller.dto;

public record UsuarioResponse(
        Long usuarioId,
        String nombre,
        String email,
        String rol,
        boolean estado) {
}
