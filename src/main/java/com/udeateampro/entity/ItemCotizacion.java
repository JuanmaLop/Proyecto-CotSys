package com.udeateampro.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCotizacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item_cotizacion")
    private Long id;

    @Column(name = "id_producto", nullable = false)
    private Long producto;

    @Column(name = "id_cotizacion", nullable = false)
    private Long cotizacion;

    @Column(name = "cantidad", nullable = false)
    private Double cantidad;

    @Column(name = "descripcion_personalizada", nullable = true)
    private String descripcionPersonalizada;

    @Column(name = "margen_especifico", nullable = true)
    private Double margenEspecifico;

    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;
}
