package com.udeateampro.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udeateampro.controller.dto.CreateClienteRequest;
import com.udeateampro.controller.dto.UpdateClienteRequest;
import com.udeateampro.entity.Cliente;
import com.udeateampro.repository.ClienteRepository;

@Service
public class ClienteService {
    
    private final ClienteRepository clienteRepository;
    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    //Obtener todos los clientes
    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    //Obtener cliente por id
    public Optional<Cliente> getClienteById(Long idCliente) {
        return clienteRepository.findById(idCliente);
    }

    //crear cliente
    public Cliente createCliente(CreateClienteRequest request) {
        validateCreateClienteRequest(request);

        var cliente = Cliente.builder()
                .nombre(request.nombre())
                .nit(request.nit())
                .direccion(request.direccion())
                .tipoRegimen(request.tipoRegimen())
                .municipio(request.municipio())
                .autorrentenedor(request.autorrentenedor())
                .build();

        return clienteRepository.save(cliente);
    }

    private void validateCreateClienteRequest(CreateClienteRequest request) {
        validateRequiredString(request.nombre(), "Nombre");
        validateRequiredString(request.nit(), "NIT");
        validateRequiredString(request.direccion(), "Direccion");
        validateRequiredString(request.tipoRegimen(), "Tipo de regimen");
        validateRequiredString(request.municipio(), "Municipio");
        validateNotNull(request.autorrentenedor(), "Autorrentenedor");
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

    //Actualizar cliente
    public Cliente updateCliente(Long idCliente, UpdateClienteRequest request) {
        Optional<Cliente> existingCliente = clienteRepository.findById(idCliente);

        if(existingCliente.isPresent()) {
            Cliente cliente = existingCliente.get();
            cliente.setNombre(request.nombre());
            cliente.setNit(request.nit());
            cliente.setDireccion(request.direccion());
            cliente.setTipoRegimen(request.tipoRegimen());
            cliente.setMunicipio(request.municipio());
            return clienteRepository.save(cliente);
        }else{
            throw new IllegalArgumentException("No existe el cliente con id:" + idCliente);
        }
    }

    public void deleteCliente(Long idCliente) {
        if(!clienteRepository.existsById(idCliente)) {
            throw new IllegalArgumentException("No existe el cliente con id:" + idCliente);
        }
        clienteRepository.deleteById(idCliente);
    }
}
