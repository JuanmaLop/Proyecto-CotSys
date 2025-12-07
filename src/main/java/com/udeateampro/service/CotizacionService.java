package com.udeateampro.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udeateampro.controller.dto.CreateCotizacionRequest;
import com.udeateampro.controller.dto.CreateItemCotizacionRequest;
import com.udeateampro.entity.Cliente;
import com.udeateampro.entity.Cotizacion;
import com.udeateampro.entity.Impuesto;
import com.udeateampro.entity.ItemCotizacion;
import com.udeateampro.repository.ClienteRepository;
import com.udeateampro.repository.CotizacionRepository;
import com.udeateampro.repository.ImpuestoRepository;
import com.udeateampro.repository.ItemCotizacionRepository;

@Service
public class CotizacionService {

    @Autowired
    private CotizacionRepository cotizacionRepository;

    @Autowired
    private ItemCotizacionRepository itemCotizacionRepository;

    @Autowired
    private ImpuestoRepository impuestoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cotizacion> getAllCotizaciones() {
        return cotizacionRepository.findAll();
    }

    public Optional<Cotizacion> getCotizacionById(Long id) {
        return cotizacionRepository.findById(id);
    }

    //crear cotizacion con items
    public Cotizacion createCotizacionWithItems(CreateCotizacionRequest request, List<CreateItemCotizacionRequest> items) {

        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setEstado(request.estado());
        cotizacion.setFechaCreacion(request.fechaCreacion());
        cotizacion.setFechaValidez(request.fechaValidez());
        cotizacion.setMargenGeneral(request.margenGeneral());
        cotizacion.setMonedaCotizacion(request.monedaCotizacion());
        cotizacion.setUsuario(request.usuario());
        cotizacion.setCliente(request.cliente());
        Cotizacion savedCotizacion = cotizacionRepository.save(cotizacion);

        for (CreateItemCotizacionRequest itemRequest : items) {
            var itemCotizacion = new com.udeateampro.entity.ItemCotizacion();
            itemCotizacion.setCotizacion(savedCotizacion.getId());
            itemCotizacion.setProducto(itemRequest.producto());
            itemCotizacion.setCantidad(itemRequest.cantidad());
            itemCotizacion.setPrecioUnitario(itemRequest.precioUnitario());
            itemCotizacionRepository.save(itemCotizacion);
        }

        // Guardar impuestos: IVA siempre, retención solo si es autorrentenedor
        Cliente cliente = clienteRepository.findById(request.cliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // IVA (19%)
        Impuesto iva = Impuesto.builder()
                .tipo("IVA")
                .descripcion("Impuesto al Valor Agregado")
                .porcentaje(new BigDecimal("19.00"))
                .estado(true)
                .cotizacion(savedCotizacion.getId())
                .build();
        impuestoRepository.save(iva);

        // Retención en la fuente (4%) solo si es autorrentenedor
        if (Boolean.TRUE.equals(cliente.getAutorrentenedor())) {
            Impuesto retencion = Impuesto.builder()
                    .tipo("RETEFUENTE")
                    .descripcion("Retención en la fuente")
                    .porcentaje(new BigDecimal("4.00"))
                    .estado(true)
                    .cotizacion(savedCotizacion.getId())
                    .build();
            impuestoRepository.save(retencion);
        }

        return savedCotizacion;
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

    public List<ItemCotizacion> getItemsByCotizacion(Long cotizacionId) {
        return itemCotizacionRepository.findByCotizacion(cotizacionId);
    }

    public List<Impuesto> getImpuestosByCotizacion(Long cotizacionId) {
        return impuestoRepository.findByCotizacion(cotizacionId);
    }
}
