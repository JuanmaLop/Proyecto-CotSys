package com.udeateampro.service;

import com.udeateampro.controller.dto.CreateCotizacionRequest;
import com.udeateampro.entity.Cotizacion;
import com.udeateampro.entity.Producto;
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

    //crear producto
    public Cotizacion createCotizacion(CreateCotizacionRequest request) {

        if (request.estado() == null || request.estado().trim().isEmpty()) {
            throw new IllegalArgumentException("Estado cannot be null or empty");
        }
        if (request.fechaCreacion() == null) {
            throw new IllegalArgumentException("Fecha de creación cannot be null");
        }
        if (request.fechaValidez() == null) {
            throw new IllegalArgumentException("Fecha de validez cannot be null");
        }
        if (request.margenGeneral() == null) {
            throw new IllegalArgumentException("Margen general cannot be null");
        }
        if (request.monedaCotizacion() == null || request.monedaCotizacion().trim().isEmpty()) {
            throw new IllegalArgumentException("Moneda de cotización cannot be null or empty");
        }
        if (request.usuario() == null) {
            throw new IllegalArgumentException("Usuario cannot be null");
        }
        if (request.cliente() == null) {
            throw new IllegalArgumentException("Cliente cannot be null");
        }

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
