package com.udeateampro.controller.dto;

import jakarta.persistence.Column;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateCotizacionRequest(
        Long usuario,
        Long cliente,
        String estado,
        LocalDate fechaCreacion,
        LocalDate fechaValidez,
        BigDecimal margenGeneral,
        String monedaCotizacion) {
}
