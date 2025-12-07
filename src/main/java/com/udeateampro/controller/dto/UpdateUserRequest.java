package com.udeateampro.controller.dto;

public record UpdateUserRequest(
        String email,
        String rol,
        boolean estado
) {

}
