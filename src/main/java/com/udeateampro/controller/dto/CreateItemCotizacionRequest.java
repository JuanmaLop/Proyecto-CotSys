package com.udeateampro.controller.dto;

public record CreateItemCotizacionRequest(
    Long producto,
    Double cantidad,
    Double precioUnitario){
}
