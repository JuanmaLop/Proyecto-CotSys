package com.udeateampro.controller.dto;

public record CreateComponenteKitRequest(
        Long kitsolucion,
        Long producto,
        Integer cantidad,
        String instrucciones) {
}
