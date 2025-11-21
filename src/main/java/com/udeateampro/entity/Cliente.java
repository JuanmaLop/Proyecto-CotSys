package com.udeateampro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "cliente")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 11)
    private String nit;

    @Column(nullable = false, length = 80)
    private String direccion;

    @Column(name = "\"tipoRegimen\"", nullable = false, length = 50)
    private String tipoRegimen;

    @Column(nullable = false, length = 80)
    private String municipio;

    @Builder.Default
    @Column(nullable = false)
    private Boolean autorrentenedor = false;
}

