package com.udeateampro.service;

import com.udeateampro.controller.dto.CreateCotizacionRequest;
import com.udeateampro.controller.dto.UpdateCotizacionRequest;
import com.udeateampro.entity.Cotizacion;
import com.udeateampro.repository.CotizacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CotizacionService {
    private final CotizacionRepository cotizacionRepository;

    @Autowired
    public CotizacionService(CotizacionRepository cotizacionRepository) {
        this.cotizacionRepository = cotizacionRepository;
    }

    public List<Cotizacion> getAllCotizaciones() {
        return cotizacionRepository.findAll();
    }

    public Optional<Cotizacion> getCotizacionById(Long id) {
        return cotizacionRepository.findById(id);
    }

    //crear producto
    public Cotizacion createCotizacion(CreateCotizacionRequest request) {
        validateCreateCotizacionRequest(request);

        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setEstado(request.estado());
        cotizacion.setFechaCreacion(request.fechaCreacion()); // típica lógica
        cotizacion.setFechaValidez(request.fechaValidez());
        cotizacion.setMargenGeneral(request.margenGeneral());
        cotizacion.setMonedaCotizacion(request.monedaCotizacion());
        cotizacion.setUsuario(request.usuario());
        cotizacion.setCliente(request.cliente());

        return cotizacionRepository.save(cotizacion);
    }

    private void validateCreateCotizacionRequest(CreateCotizacionRequest request) {
        validateRequiredString(request.estado(), "Estado");
        validateNotNull(request.fechaCreacion(), "Fecha de creación");
        validateNotNull(request.fechaValidez(), "Fecha de validez");
        validateNotNull(request.margenGeneral(), "Margen general");
        validateRequiredString(request.monedaCotizacion(), "Moneda de cotización");
        validateNotNull(request.usuario(), "Usuario");
        validateNotNull(request.cliente(), "Cliente");
    }

    private void validateRequiredString(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
    }

    private void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
    }

    public Cotizacion updateCotizacion(Long id, UpdateCotizacionRequest updatedCotizacion) {
        Optional<Cotizacion> existingCotizacion = cotizacionRepository.findById(id);

        if(existingCotizacion.isPresent()) {
            Cotizacion cotizacion = existingCotizacion.get();
            cotizacion.setEstado(updatedCotizacion.estado());
            cotizacion.setFechaCreacion(updatedCotizacion.fechaCreacion());
            cotizacion.setFechaValidez(updatedCotizacion.fechaValidez());
            cotizacion.setMargenGeneral(updatedCotizacion.margenGeneral());
            cotizacion.setMonedaCotizacion(updatedCotizacion.monedaCotizacion());
            cotizacion.setUsuario(updatedCotizacion.usuario());
            cotizacion.setCliente(updatedCotizacion.cliente());
            return cotizacionRepository.save(cotizacion);

        }else{
            throw new IllegalArgumentException("No existe la cotización con id:" + id);
        }
    }

    public void deleteCotizacion(Long id) {
        if(!cotizacionRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe la cotización con id:" + id);
        }
        cotizacionRepository.deleteById(id);
    }
}
