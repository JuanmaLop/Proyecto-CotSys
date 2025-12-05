package com.udeateampro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udeateampro.controller.dto.CreateComponenteKitRequest;
import com.udeateampro.entity.ComponenteKit;
import com.udeateampro.service.ComponenteKitService;


@RestController
@RequestMapping("/api/componente-kit")
@CrossOrigin(origins = "*")
public class ComponenteKitController {
    @Autowired
    private ComponenteKitService componenteKitService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'LIDER_TECNICO')")
    @PostMapping("/create-componente-kit")
    public ResponseEntity<ComponenteKit> addComponenteKit(@RequestBody final CreateComponenteKitRequest request) {
        ComponenteKit newComponenteKit = componenteKitService.createComponenteKit(request);
        return ResponseEntity.ok(newComponenteKit);
    }
}
