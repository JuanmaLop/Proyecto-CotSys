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

@Entity
@Table(name = "componente_kit")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComponenteKit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_componente_kit")
    private Long id_componente_kit;

    @Column(name = "id_kit_solucion", nullable = false)
    private Long kitSolucion;

    @Column(name = "id_producto", nullable = false)
    private Long producto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "instrucciones", nullable = false)
    private String instrucciones;

    @Builder.Default
    @Column(name = "estado", nullable = false)
    private Boolean estado = true;
}
