package com.udeateampro.controller.dto;

public record UpdateProductRequest(
        String nombre,
        String descripcion,
        String categoria,
        String unidadMedida,
        Double costoBase,
        String monedaOriginal,
        String tipo,
        Boolean estado) {
}
