package com.udeateampro.controller.dto;

import java.util.List;

public record KitSolucionResponse(
        Long id_kit,
        String nombre,
        String descripcion,
        Boolean estado,
        List<ComponenteKitResponse> componentes) {
    
    public record ComponenteKitResponse(
            Long id_componente_kit,
            Long id_producto,
            Integer cantidad,
            String instrucciones,
            Boolean estado) {
    }
}

