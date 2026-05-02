package com.udeateampro.controller.dto;

public record UpdateClienteRequest(
        String nombre,
        String nit,
        String direccion,
        String tipoRegimen,
        String municipio) {

}
