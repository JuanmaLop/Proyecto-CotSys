package com.udeateampro.CotSys.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;

    @Column(nullable = false, length = 60)
    private String nombre;

    @Column(nullable = false, length = 255)
    private String descripcion;

    @Column(nullable = false, length = 60)
    private String categoria;

    // 🔹 OJO: aquí debe ir exactamente como está en la base
    @Column(name = "unidadmedida", nullable = false, length = 60)
    private String unidadMedida;

    @Column(name = "costobase", nullable = false)
    private Double costoBase;

    @Column(name = "monedaoriginal", nullable = false, length = 20)
    private String monedaOriginal;

    @Column(nullable = false, length = 60)
    private String tipo;

    @Column(name = "estado", nullable = false)
    private Boolean estado;

    @Column(name = "cantidadkit", nullable = false)
    private Integer cantidadKit;

    @Column(name = "instruccioneskit", nullable = false, length = 255)
    private String instruccionesKit;

    @Column(name = "kitsolucion", nullable = false)
    private Long kitSolucion;
}

