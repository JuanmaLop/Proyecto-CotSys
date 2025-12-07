package com.udeateampro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.udeateampro.controller.dto.CreateComponenteKitRequest;
import com.udeateampro.entity.ComponenteKit;
import com.udeateampro.service.ComponenteKitService;

import java.util.List;


@RestController
@RequestMapping("/api/componente-kit")
@CrossOrigin(origins = "*")
public class ComponenteKitController {

    @Autowired
    private ComponenteKitService componenteKitService;

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
    @PutMapping("/{id_componente_kit}/update")
    public ResponseEntity<ComponenteKit> updateComponenteKit(
            @PathVariable Long id_componente_kit,
            @RequestBody ComponenteKit componenteKit) {

        return ResponseEntity.ok(componenteKitService.updateComponenteKit(id_componente_kit, componenteKit));
    }

    // Eliminar un componente
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'LIDER_TECNICO')")
    @DeleteMapping("/{id_componente_kit}/delete")
    public ResponseEntity<Void> deleteComponenteKit(@PathVariable Long id_componente_kit) {
        componenteKitService.deleteComponenteKit(id_componente_kit);
        return ResponseEntity.noContent().build();
    }
}

