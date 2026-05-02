package com.udeateampro.controller.dto;

public record UpdateComponenteKitRequest(
        Integer cantidad,
        String instrucciones,
        Boolean estado) {
}