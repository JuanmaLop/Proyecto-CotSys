package com.udeateampro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.udeateampro.controller.dto.CreateComponenteKitRequest;
import com.udeateampro.controller.dto.UpdateComponenteKitRequest;
import com.udeateampro.entity.ComponenteKit;
import com.udeateampro.service.ComponenteKitService;

import java.util.List;


@RestController
@RequestMapping("/api/componente-kit")
public class ComponenteKitController {
    private final ComponenteKitService componenteKitService;
    
    @Autowired
    public ComponenteKitController(ComponenteKitService componenteKitService) {
        this.componenteKitService = componenteKitService;
    }

    // Crear componente de kit
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'LIDER_TECNICO')")
    @PostMapping("/create-componente-kit")
    public ResponseEntity<ComponenteKit> addComponenteKit(@RequestBody final CreateComponenteKitRequest request) {
        ComponenteKit newComponenteKit = componenteKitService.createComponenteKit(request);
        return ResponseEntity.ok(newComponenteKit);
    }

    // Obtener todos los componentes
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'LIDER_TECNICO')")
    @GetMapping("/get-all")
    public ResponseEntity<List<ComponenteKit>> getAllComponenteKits() {
        return ResponseEntity.ok(componenteKitService.getAllComponenteKits());
    }

    // Actualizar un componente
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'LIDER_TECNICO')")
    @PutMapping("/{idComponenteKit}/update")
    public ResponseEntity<ComponenteKit> updateComponenteKit(
            @PathVariable Long idComponenteKit,
            @RequestBody UpdateComponenteKitRequest request) {

        return ResponseEntity.ok(componenteKitService.updateComponenteKit(idComponenteKit, request));
    }

    // Eliminar un componente
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'LIDER_TECNICO')")
    @DeleteMapping("/{idComponenteKit}/delete")
    public ResponseEntity<Void> deleteComponenteKit(@PathVariable Long idComponenteKit) {
        componenteKitService.deleteComponenteKit(idComponenteKit);
        return ResponseEntity.noContent().build();
    }
}

