package com.udeateampro.service;

import com.udeateampro.entity.KitSolucion;
import com.udeateampro.repository.KitSolucionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class KitSolucionService {

    @Autowired
    private KitSolucionRepository kitSolucionRepository;

    public KitSolucion createKit(KitSolucion kit) {
        return kitSolucionRepository.save(kit);
    }

    public List<KitSolucion> getAllKits() {
        return kitSolucionRepository.findAll();
    }

    public KitSolucion updateKit(Long id, KitSolucion kitDetails) {
        KitSolucion existing = kitSolucionRepository.findById(id).orElseThrow(() -> new RuntimeException("kit no encontrado"));

        existing.setNombre(kitDetails.getNombre());
        existing.setDescripcion(kitDetails.getDescripcion());
        existing.setEstado(kitDetails.getEstado());

        return kitSolucionRepository.save(existing);
    }

    public void deleteKit(Long id) {
        kitSolucionRepository.deleteById(id);
    }

}
