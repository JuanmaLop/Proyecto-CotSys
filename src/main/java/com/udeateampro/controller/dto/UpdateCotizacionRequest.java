package com.udeateampro.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateCotizacionRequest(
        String estado,
        LocalDate fechaCreacion,
        LocalDate fechaValidez,
        BigDecimal margenGeneral,
        String monedaCotizacion,
        Long usuario,
        Long cliente) {

}
