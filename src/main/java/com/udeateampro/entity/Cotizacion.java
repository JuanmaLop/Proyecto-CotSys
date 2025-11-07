package com.udeateampro.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "cotizacion")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Cotizacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cotizacion")
    private Long id;

    @Column(nullable = false, length = 40)
    private String estado;

    @Column(nullable = false)
    private LocalDate fechaCreacion;

    @Column(nullable = false)
    private LocalDate fechaValidez;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal margenGeneral;

    @Column(nullable = false, length = 20)
    private String monedaCotizacion;

    @Column(nullable = false)
    private Long usuario;

    @Column(nullable = false)
    private Long cliente;
}
