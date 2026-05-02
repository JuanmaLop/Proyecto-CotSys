package com.udeateampro.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udeateampro.controller.dto.CreateComponenteKitRequest;
import com.udeateampro.controller.dto.UpdateComponenteKitRequest;
import com.udeateampro.entity.ComponenteKit;
import com.udeateampro.repository.ComponenteKitRepository;

@Service
public class ComponenteKitService {
    private final ComponenteKitRepository componenteKitRepository;

    @Autowired
    public ComponenteKitService(ComponenteKitRepository componenteKitRepository) {
        this.componenteKitRepository = componenteKitRepository;
    }

    public List<ComponenteKit> getAllComponenteKits() {
        return componenteKitRepository.findAll();
    }

    public Optional<ComponenteKit> getComponenteKitById(Long idComponenteKit) {
        return componenteKitRepository.findById(idComponenteKit);
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

    public ComponenteKit updateComponenteKit(Long idComponenteKit, UpdateComponenteKitRequest request) {
        ComponenteKit existing = componenteKitRepository.findById(idComponenteKit)
                .orElseThrow(() -> new IllegalArgumentException("Componente kit no encontrado"));

        existing.setCantidad(request.cantidad());
        existing.setInstrucciones(request.instrucciones());
        existing.setEstado(request.estado());

        return componenteKitRepository.save(existing);
    }

    public void deleteComponenteKit(Long idComponenteKit) {
        if (!componenteKitRepository.existsById(idComponenteKit)) {
            throw new IllegalArgumentException("No existe el componente kit con id: " + idComponenteKit);
        }
        componenteKitRepository.deleteById(idComponenteKit);
    }
}
