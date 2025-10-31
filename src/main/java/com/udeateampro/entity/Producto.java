package com.udeateampro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 60)
    private String nombre;

    @Column(name = "descripcion", nullable = false, length = 255)
    private String descripcion;

    @Column(name = "categoria", nullable = false, length = 60)
    private String categoria;

    @Column(name = "unidadmedida", nullable = false, length = 60)
    private String unidadMedida;

    @Column(name = "costoBase", nullable = false)
    private Double costoBase;

    @Column(name = "monedaOriginal", nullable = false, length = 20)
    private String monedaOriginal;

    @Column(name = "tipo", nullable = false, length = 60)
    private String tipo;

    @Column(name = "estado", nullable = false)
    private Boolean estado;

    @Column(name = "cantidadKit", nullable = false)
    private Integer cantidadKit;

    @Column(name = "instruccionesKit", nullable = false, length = 255)
    private String instruccionesKit;

    @Column(name = "kitSolucion", nullable = false)
    private Long kitSolucion;
}

