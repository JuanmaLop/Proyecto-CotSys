package com.udeateampro.controller;

import java.util.List;

import com.udeateampro.controller.dto.CreateCotizacionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udeateampro.entity.Cotizacion;
import com.udeateampro.service.CotizacionService;

@RestController
@RequestMapping("/api/cotizaciones")
@CrossOrigin(origins = "*")

public class CotizacionController {

    @Autowired
    private CotizacionService cotizacionService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COMERCIAL')")
    @PostMapping("/create-cotizacion")
    public ResponseEntity<Cotizacion> addCotizacion(@RequestBody final CreateCotizacionRequest request) {
        Cotizacion newCotizacion = cotizacionService.createCotizacion(request);
        return ResponseEntity.ok(newCotizacion);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COMERCIAL')")
    @GetMapping("/get-all-cotizaciones")
    public ResponseEntity<List<Cotizacion>> getAllCotizaciones() {
        return ResponseEntity.ok(cotizacionService.getAllCotizaciones());
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COMERCIAL')")
    @PutMapping("/{id}/update-cotizacion")
    public ResponseEntity<Cotizacion> updateCotizacion(@PathVariable Long id, @RequestBody Cotizacion cotizacion) {
        Cotizacion updatedCotizacion = cotizacionService.updateCotizacion(id, cotizacion);
        return ResponseEntity.ok(updatedCotizacion);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COMERCIAL')")
    @DeleteMapping(("/{id}/delete-cotizacion"))
    public ResponseEntity<Cotizacion> deleteCotizacion(@PathVariable Long id) {
        cotizacionService.deleteCotizacion(id);
        return ResponseEntity.noContent().build();
    }

}
