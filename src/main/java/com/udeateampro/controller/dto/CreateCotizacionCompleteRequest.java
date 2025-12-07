package com.udeateampro.controller.dto;

import java.util.List;

public record CreateCotizacionCompleteRequest(
    CreateCotizacionRequest cotizacion,
    List<CreateItemCotizacionRequest> items
) {}
