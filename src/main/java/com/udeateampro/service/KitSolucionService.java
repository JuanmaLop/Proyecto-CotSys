package com.udeateampro.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udeateampro.controller.dto.CreateKitSolucionRequest;
import com.udeateampro.controller.dto.CreateComponenteKitRequest;
import com.udeateampro.entity.KitSolucion;
import com.udeateampro.entity.ComponenteKit;
import com.udeateampro.repository.KitSolucionRepository;
import com.udeateampro.repository.ComponenteKitRepository;

@Service
public class KitSolucionService {

    @Autowired
    private KitSolucionRepository kitSolucionRepository;
    
    @Autowired
    private ComponenteKitRepository componenteKitRepository;

    @Transactional
    public KitSolucion createKit(CreateKitSolucionRequest request) {
        var kitSolucion = KitSolucion.builder()
                .nombre(request.nombre())
                .descripcion(request.descripcion())
                .estado(request.estado() != null ? request.estado() : true)
                .build();
        kitSolucion = kitSolucionRepository.save(kitSolucion);
        
        // Crear componentes si existen
        if (request.componentes() != null && !request.componentes().isEmpty()) {
            for (var compDTO : request.componentes()) {
                if (compDTO.id_producto() != null && compDTO.cantidad() != null) {
                    var componente = ComponenteKit.builder()
                            .kitSolucion(kitSolucion.getId_kit())
                            .producto(compDTO.id_producto())
                            .cantidad(compDTO.cantidad())
                            .instrucciones(compDTO.instrucciones() != null ? compDTO.instrucciones() : "")
                            .estado(compDTO.estado() != null ? compDTO.estado() : true)
                            .build();
                    componenteKitRepository.save(componente);
                }
            }
        }
        
        return kitSolucion;
    }

    public List<KitSolucion> getAllKits() {
        List<KitSolucion> kits = kitSolucionRepository.findAll();
        // Los componentes se cargarán en el controlador o se puede usar un DTO
        return kits;
    }
    
    public List<ComponenteKit> getComponentesByKit(Long kitId) {
        return componenteKitRepository.findByKitSolucion(kitId);
    }

    @Transactional
    public KitSolucion updateKit(Long id, CreateKitSolucionRequest request) {
        KitSolucion existing = kitSolucionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("kit no encontrado"));

        existing.setNombre(request.nombre());
        existing.setDescripcion(request.descripcion());
        if (request.estado() != null) {
            existing.setEstado(request.estado());
        }
        existing = kitSolucionRepository.save(existing);
        
        // Eliminar componentes existentes y crear nuevos
        componenteKitRepository.deleteByKitSolucion(id);
        
        // Crear nuevos componentes
        if (request.componentes() != null && !request.componentes().isEmpty()) {
            for (var compDTO : request.componentes()) {
                if (compDTO.id_producto() != null && compDTO.cantidad() != null) {
                    var componente = ComponenteKit.builder()
                            .kitSolucion(id)
                            .producto(compDTO.id_producto())
                            .cantidad(compDTO.cantidad())
                            .instrucciones(compDTO.instrucciones() != null ? compDTO.instrucciones() : "")
                            .estado(compDTO.estado() != null ? compDTO.estado() : true)
                            .build();
                    componenteKitRepository.save(componente);
                }
            }
        }
        
        return existing;
    }

    @Transactional
    public void deleteKit(Long id) {
        // Eliminar componentes primero
        componenteKitRepository.deleteByKitSolucion(id);
        // Luego eliminar el kit
        kitSolucionRepository.deleteById(id);
    }

}
