package com.udeateampro.service;

import com.udeateampro.entity.Cotizacion;
import com.udeateampro.repository.CotizacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CotizacionService {

    @Autowired
    private CotizacionRepository cotizacionRepository;

    public List<Cotizacion> getAllCotizaciones() {
        return cotizacionRepository.findAll();
    }

    public Optional<Cotizacion> getCotizacionById(Long id) {
        return cotizacionRepository.findById(id);
    }

    public Cotizacion createCotizacion(Cotizacion cotizacion) {

        return cotizacionRepository.save(cotizacion);
    }

    public Cotizacion updateCotizacion(Long id, Cotizacion updatedCotizacion) {
        Optional<Cotizacion> existingCotizacion = cotizacionRepository.findById(id);

        if(existingCotizacion.isPresent()) {
            Cotizacion cotizacion = existingCotizacion.get();
            cotizacion.setEstado(updatedCotizacion.getEstado());
            cotizacion.setFechaCreacion(updatedCotizacion.getFechaCreacion());
            cotizacion.setFechaValidez(updatedCotizacion.getFechaValidez());
            cotizacion.setMargenGeneral(updatedCotizacion.getMargenGeneral());
            cotizacion.setMonedaCotizacion(updatedCotizacion.getMonedaCotizacion());
            cotizacion.setUsuario(updatedCotizacion.getUsuario());
            cotizacion.setCliente(updatedCotizacion.getCliente());
            return cotizacionRepository.save(cotizacion);

        }else{
            throw new RuntimeException("No existe la cotización con id:" + id);
        }
    }

    public void deleteCotizacion(Long id) {
        if(!cotizacionRepository.existsById(id)) {
            throw new RuntimeException("No existe la cotización con id:" + id);
        }
        cotizacionRepository.deleteById(id);
    }
}
