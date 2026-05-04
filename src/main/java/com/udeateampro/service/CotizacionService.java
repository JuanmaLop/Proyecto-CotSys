package com.udeateampro.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udeateampro.controller.dto.CreateCotizacionRequest;
import com.udeateampro.controller.dto.UpdateCotizacionRequest;
import com.udeateampro.entity.Cotizacion;
import com.udeateampro.repository.CotizacionRepository;
import com.udeateampro.service.validator.RequestValidator;

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
        RequestValidator.validateRequiredString(request.estado(), "Estado");
        RequestValidator.validateNotNull(request.fechaCreacion(), "Fecha de creación");
        RequestValidator.validateNotNull(request.fechaValidez(), "Fecha de validez");
        RequestValidator.validateNotNull(request.margenGeneral(), "Margen general");
        RequestValidator.validateRequiredString(request.monedaCotizacion(), "Moneda de cotización");
        RequestValidator.validateNotNull(request.usuario(), "Usuario");
        RequestValidator.validateNotNull(request.cliente(), "Cliente");
    }

    public Cotizacion updateCotizacion(Long id, UpdateCotizacionRequest updatedCotizacion) {
        Cotizacion cotizacion = cotizacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe la cotización con id:" + id));
        
        updateCotizacionFields(cotizacion, updatedCotizacion);
        return cotizacionRepository.save(cotizacion);
    }

    private void updateCotizacionFields(Cotizacion cotizacion, UpdateCotizacionRequest request) {
        cotizacion.setEstado(request.estado());
        cotizacion.setFechaCreacion(request.fechaCreacion());
        cotizacion.setFechaValidez(request.fechaValidez());
        cotizacion.setMargenGeneral(request.margenGeneral());
        cotizacion.setMonedaCotizacion(request.monedaCotizacion());
        cotizacion.setUsuario(request.usuario());
        cotizacion.setCliente(request.cliente());
    }

    public void deleteCotizacion(Long id) {
        RequestValidator.validateExists(cotizacionRepository.existsById(id), "cotización", id);
        cotizacionRepository.deleteById(id);
    }
}
