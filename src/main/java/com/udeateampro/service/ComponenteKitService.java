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

    public Optional<ComponenteKit> getComponenteKitById(Long id_componente_kit) {
        return componenteKitRepository.findById(id_componente_kit);
    }

    public ComponenteKit createComponenteKit(CreateComponenteKitRequest request) {
        if (request.kitsolucion() == null) {
            throw new IllegalArgumentException("Kit de solucion cannot be null");
        }
        if (request.producto() == null) {
            throw new IllegalArgumentException("Producto cannot be null");
        }
        if (request.cantidad() == null || request.cantidad() <= 0) {
            throw new IllegalArgumentException("Cantidad must be greater than zero");
        }
        if (request.instrucciones() == null) {
            throw new IllegalArgumentException("Instrucciones cannot be null");
        }

        var componenteKit = ComponenteKit.builder()
                .kitSolucion(request.kitsolucion())
                .producto(request.producto())
                .cantidad(request.cantidad())
                .instrucciones(request.instrucciones())
                .build();

        return componenteKitRepository.save(componenteKit);
    }

    public ComponenteKit updateComponenteKit(Long id_componente_kit, ComponenteKit updated) {
        ComponenteKit existing = componenteKitRepository.findById(id_componente_kit)
                .orElseThrow(() -> new RuntimeException("Componente kit no encontrado"));

        existing.setCantidad(updated.getCantidad());
        existing.setInstrucciones(updated.getInstrucciones());
        existing.setEstado(updated.getEstado());

        return componenteKitRepository.save(existing);
    }

    public void deleteComponenteKit(Long id_componente_kit) {
        if (!componenteKitRepository.existsById(id_componente_kit)) {
            throw new RuntimeException("No existe el componente kit con id: " + id_componente_kit);
        }
        componenteKitRepository.deleteById(id_componente_kit);
    }
}
