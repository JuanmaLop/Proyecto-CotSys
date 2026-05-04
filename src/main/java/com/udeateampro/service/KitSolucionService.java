package com.udeateampro.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udeateampro.controller.dto.CreateKitSolucionRequest;
import com.udeateampro.entity.ComponenteKit;
import com.udeateampro.entity.KitSolucion;
import com.udeateampro.repository.ComponenteKitRepository;
import com.udeateampro.repository.KitSolucionRepository;
import com.udeateampro.service.validator.RequestValidator;

@Service
public class KitSolucionService {
    private final KitSolucionRepository kitSolucionRepository;
    private final ComponenteKitRepository componenteKitRepository;

    @Autowired
    public KitSolucionService(KitSolucionRepository kitSolucionRepository, ComponenteKitRepository componenteKitRepository) {
        this.kitSolucionRepository = kitSolucionRepository;
        this.componenteKitRepository = componenteKitRepository;
    }

    @Transactional
    public KitSolucion createKit(CreateKitSolucionRequest request) {
        var kitSolucion = KitSolucion.builder()
                .nombre(request.nombre())
                .descripcion(request.descripcion())
                .estado(Boolean.TRUE.equals(request.estado()))
                .build();
        kitSolucion = kitSolucionRepository.save(kitSolucion);

        // Crear componentes si existen
        createComponentesForKit(kitSolucion.getIdKit(), request.componentes());

        return kitSolucion;
    }

    public List<KitSolucion> getAllKits() {
        // Los componentes se cargarán en el controlador o se puede usar un DTO
        return kitSolucionRepository.findAll();
    }
    
    public List<ComponenteKit> getComponentesByKit(Long kitId) {
        return componenteKitRepository.findByKitSolucion(kitId);
    }

    @Transactional
    public KitSolucion updateKit(Long id, CreateKitSolucionRequest request) {
        KitSolucion existing = kitSolucionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("kit no encontrado"));

        updateKitFields(existing, request);
        existing = kitSolucionRepository.save(existing);

        // Eliminar componentes existentes y crear nuevos
        componenteKitRepository.deleteByKitSolucion(id);
        createComponentesForKit(id, request.componentes());

        return existing;
    }

    private void updateKitFields(KitSolucion kit, CreateKitSolucionRequest request) {
        kit.setNombre(request.nombre());
        kit.setDescripcion(request.descripcion());
        if (request.estado() != null) {
            kit.setEstado(request.estado());
        }
    }

    @Transactional
    public void deleteKit(Long id) {
        // Eliminar componentes primero
        componenteKitRepository.deleteByKitSolucion(id);
        // Luego eliminar el kit
        kitSolucionRepository.deleteById(id);
    }

    private void createComponentesForKit(Long kitId, List<CreateKitSolucionRequest.ComponenteKitDTO> componentes) {
        if (componentes == null || componentes.isEmpty()) {
            return;
        }
        componentes.stream()
                .filter(this::isValidComponente)
                .map(compDTO -> buildComponenteFromDTO(kitId, compDTO))
                .forEach(componenteKitRepository::save);
    }

    private boolean isValidComponente(CreateKitSolucionRequest.ComponenteKitDTO compDTO) {
        return compDTO.id_producto() != null && compDTO.cantidad() != null;
    }

    private ComponenteKit buildComponenteFromDTO(Long kitId, CreateKitSolucionRequest.ComponenteKitDTO compDTO) {
        return ComponenteKit.builder()
                .kitSolucion(kitId)
                .producto(compDTO.id_producto())
                .cantidad(compDTO.cantidad())
                .instrucciones(Objects.requireNonNullElse(compDTO.instrucciones(), ""))
                .estado(Objects.requireNonNullElse(compDTO.estado(), Boolean.TRUE))
                .build();
    }

}
