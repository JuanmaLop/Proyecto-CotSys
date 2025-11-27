package com.udeateampro.controller;

import java.util.List;

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

import com.udeateampro.controller.dto.CreateKitSolucionRequest;
import com.udeateampro.entity.KitSolucion;
import com.udeateampro.service.KitSolucionService;

@RestController
@RequestMapping("/api/kits")
@CrossOrigin(origins = "*")

public class KitSolucionController {

    @Autowired
    private KitSolucionService kitSolucionService;

    @PreAuthorize("hasRole('TÉCNICO')")
    @PostMapping("/create-kit")
    public ResponseEntity<KitSolucion> addKit(@RequestBody CreateKitSolucionRequest request) {
        return ResponseEntity.ok(kitSolucionService.createKit(request));
    }

    @GetMapping("/get-all-kits")
    public ResponseEntity<List<KitSolucion>> getAllKits() {
        return ResponseEntity.ok(kitSolucionService.getAllKits());
    }

    @PreAuthorize("hasRole('TÉCNICO')")
    @PutMapping("/{id}/update-kit")
    public ResponseEntity<KitSolucion> updateKit(@PathVariable Long id, @RequestBody KitSolucion kit) {
        return ResponseEntity.ok(kitSolucionService.updateKit(id, kit));
    }

    @PreAuthorize("hasRole('TÉCNICO')")
    @DeleteMapping("/{id}/delete-kit")
    public ResponseEntity<Void> deleteKit(@PathVariable Long id) {
        kitSolucionService.deleteKit(id);
        return ResponseEntity.noContent().build();
    }
}

