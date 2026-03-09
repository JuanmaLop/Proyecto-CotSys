package com.udeateampro.controller.dto;

public record CreateClienteRequest(
        Long idCliente,
        String nombre,
        String nit,
        String direccion,
        String tipoRegimen,
        String municipio,
        Boolean autorrentenedor) {

}
