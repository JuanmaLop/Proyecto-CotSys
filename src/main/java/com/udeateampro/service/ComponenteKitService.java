package com.udeateampro.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udeateampro.controller.dto.CreateComponenteKitRequest;
import com.udeateampro.entity.ComponenteKit;
import com.udeateampro.repository.ComponenteKitRepository;

@Service
public class ComponenteKitService {

    @Autowired
    private ComponenteKitRepository componenteKitRepository;

    public List<ComponenteKit> getAllComponenteKits() {
        return componenteKitRepository.findAll();
    }

    public Optional<ComponenteKit> getComponenteKitById(Long id) {
        return componenteKitRepository.findById(id);
    }

    public ComponenteKit updateComponenteKit(Long id, ComponenteKit updatedComponenteKit) {
        Optional<ComponenteKit> existingComponenteKit = componenteKitRepository.findById(id);

        if (existingComponenteKit.isPresent()) {
            ComponenteKit componenteKit = existingComponenteKit.get();
            componenteKit.setCantidad(updatedComponenteKit.getCantidad());
            componenteKit.setInstrucciones(updatedComponenteKit.getInstrucciones());
            componenteKit.setEstado(updatedComponenteKit.getEstado());
            return componenteKitRepository.save(componenteKit);
        } else {
            throw new RuntimeException("No existe el componente kit con id:" + id);
        }
    }

    public ComponenteKit createComponenteKit(CreateComponenteKitRequest request) {
        var componenteKit = ComponenteKit.builder()
                .kitSolucion(request.kitsolucion())
                .producto(request.producto())
                .cantidad(request.cantidad())
                .instrucciones(request.instrucciones())
                .build();
        return componenteKitRepository.save(componenteKit);
    }

    public List<ComponenteKit> getComponentesByKitSolucion(Long kitSolucion) {
        return componenteKitRepository.findByKitSolucion(kitSolucion);
    }
}
