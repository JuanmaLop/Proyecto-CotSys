package com.udeateampro.controller;

import java.util.List;
import java.util.stream.Collectors;

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
import com.udeateampro.controller.dto.KitSolucionResponse;
import com.udeateampro.entity.KitSolucion;
import com.udeateampro.entity.ComponenteKit;
import com.udeateampro.service.KitSolucionService;

@RestController
@RequestMapping("/api/kits")
@CrossOrigin(origins = "*")

public class KitSolucionController {

    @Autowired
    private KitSolucionService kitSolucionService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'LIDER_TECNICO')")
    @PostMapping("/create-kit")
    public ResponseEntity<KitSolucionResponse> addKit(@RequestBody CreateKitSolucionRequest request) {
        KitSolucion kit = kitSolucionService.createKit(request);
        return ResponseEntity.ok(toResponse(kit));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'LIDER_TECNICO')")
    @GetMapping("/get-all-kits")
    public ResponseEntity<List<KitSolucionResponse>> getAllKits() {
        List<KitSolucion> kits = kitSolucionService.getAllKits();
        List<KitSolucionResponse> response = kits.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'LIDER_TECNICO')")
    @PutMapping("/{id_kit}/update-kit")
    public ResponseEntity<KitSolucionResponse> updateKit(@PathVariable Long id_kit, @RequestBody CreateKitSolucionRequest request) {
        KitSolucion kit = kitSolucionService.updateKit(id_kit, request);
        return ResponseEntity.ok(toResponse(kit));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'LIDER_TECNICO')")
    @DeleteMapping("/{id_kit}/delete-kit")
    public ResponseEntity<Void> deleteKit(@PathVariable Long id_kit) {
        kitSolucionService.deleteKit(id_kit);
        return ResponseEntity.noContent().build();
    }
    
    private KitSolucionResponse toResponse(KitSolucion kit) {
        List<ComponenteKit> componentes = kitSolucionService.getComponentesByKit(kit.getId_kit());
        List<KitSolucionResponse.ComponenteKitResponse> componentesResponse = componentes.stream()
                .map(c -> new KitSolucionResponse.ComponenteKitResponse(
                        c.getId_componente_kit(),
                        c.getProducto(),
                        c.getCantidad(),
                        c.getInstrucciones(),
                        c.getEstado()))
                .collect(Collectors.toList());
        
        return new KitSolucionResponse(
                kit.getId_kit(),
                kit.getNombre(),
                kit.getDescripcion(),
                kit.getEstado(),
                componentesResponse);
    }
}

