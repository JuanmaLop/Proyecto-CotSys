package com.udeateampro.controller.dto;

public record CreateClienteRequest(
        String nombre,
        String nit,
        String direccion,
        String tipoRegimen,
        String municipio,
        Boolean autorrentenedor) {

}
