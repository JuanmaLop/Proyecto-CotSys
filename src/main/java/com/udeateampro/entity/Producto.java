package com.udeateampro.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "producto")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id_producto;

    @Column(name = "nombre", nullable = false, length = 60)
    private String nombre;

    @Column(name = "descripcion", nullable = false, length = 255)
    private String descripcion;

    @Column(name = "categoria", nullable = false, length = 60)
    private String categoria;

    @Column(name = "unidad_medida", nullable = false, length = 60)
    private String unidadMedida;

    @Column(name = "costo_base", nullable = false)
    private Double costoBase;

    @Column(name = "moneda_original", nullable = false, length = 20)
    private String monedaOriginal;

    @Column(name = "tipo", nullable = false, length = 60)
    private String tipo;

    @Builder.Default
    @Column(name = "estado", nullable = false)
    private Boolean estado = true;
}

