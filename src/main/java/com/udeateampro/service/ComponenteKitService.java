package com.udeateampro.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udeateampro.controller.dto.CreateComponenteKitRequest;
import com.udeateampro.controller.dto.UpdateComponenteKitRequest;
import com.udeateampro.entity.ComponenteKit;
import com.udeateampro.repository.ComponenteKitRepository;
import com.udeateampro.service.validator.RequestValidator;

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
        validateCreateComponenteKitRequest(request);

        var componenteKit = ComponenteKit.builder()
                .kitSolucion(request.kitsolucion())
                .producto(request.producto())
                .cantidad(request.cantidad())
                .instrucciones(request.instrucciones())
                .build();

        return componenteKitRepository.save(componenteKit);
    }

    private void validateCreateComponenteKitRequest(CreateComponenteKitRequest request) {
        RequestValidator.validateNotNull(request.kitsolucion(), "Kit de solucion");
        RequestValidator.validateNotNull(request.producto(), "Producto");
        RequestValidator.validatePositiveInteger(request.cantidad(), "Cantidad");
        RequestValidator.validateNotNull(request.instrucciones(), "Instrucciones");
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
        RequestValidator.validateExists(componenteKitRepository.existsById(idComponenteKit), "componente kit", idComponenteKit);
        componenteKitRepository.deleteById(idComponenteKit);
    }
}
