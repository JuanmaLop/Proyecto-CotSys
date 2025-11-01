package com.udeateampro.controller;

import com.udeateampro.entity.Cotizacion;
import com.udeateampro.service.CotizacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cotizaciones")
@CrossOrigin(origins = "*")

public class CotizacionController {

    @Autowired
    private CotizacionService cotizacionService;

    @PostMapping
    public ResponseEntity<Cotizacion> addCotizacion(@RequestBody Cotizacion cotizacion) {
        Cotizacion newCotizacion = cotizacionService.createCotizacion(cotizacion);
        return ResponseEntity.ok(newCotizacion);
    }

    @GetMapping
    public ResponseEntity<List<Cotizacion>> getAllCotizaciones() {
        return ResponseEntity.ok(cotizacionService.getAllCotizaciones());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cotizacion> updateCotizacion(@PathVariable Long id, @RequestBody Cotizacion cotizacion) {
        Cotizacion updatedCotizacion = cotizacionService.updateCotizacion(id, cotizacion);
        return ResponseEntity.ok(updatedCotizacion);
    }

    @DeleteMapping(("/{id}"))
    public ResponseEntity<Cotizacion> deleteCotizacion(@PathVariable Long id) {
        cotizacionService.deleteCotizacion(id);
        return ResponseEntity.noContent().build();
    }

}
