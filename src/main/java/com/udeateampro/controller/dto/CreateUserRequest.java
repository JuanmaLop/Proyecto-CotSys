package com.udeateampro.controller.dto;

public record CreateUserRequest(
        String nombre,
        String email,
        String rol,
        String password) {
}
