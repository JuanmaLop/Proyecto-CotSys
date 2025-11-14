package com.udeateampro.controller;

import com.udeateampro.entity.KitSolucion;
import com.udeateampro.service.KitSolucionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/kits")
@CrossOrigin(origins = "*")

public class KitSolucionController {

    @Autowired
    private KitSolucionService kitSolucionService;

    @PostMapping
    public ResponseEntity<KitSolucion> addKit(@RequestBody KitSolucion kit) {
        return ResponseEntity.ok(kitSolucionService.createKit(kit));
    }

    @GetMapping
    public ResponseEntity<List<KitSolucion>> getAllKits() {
        return ResponseEntity.ok(kitSolucionService.getAllKits());
    }

    @PutMapping("/{id}")
    public ResponseEntity<KitSolucion> updateKit(@PathVariable Long id, @RequestBody KitSolucion kit) {
        return ResponseEntity.ok(kitSolucionService.updateKit(id, kit));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKit(@PathVariable Long id) {
        kitSolucionService.deleteKit(id);
        return ResponseEntity.noContent().build();
    }
}

