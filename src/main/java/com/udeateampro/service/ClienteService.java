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

        if (request.nombre() == null || request.nombre().trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre cannot be null or empty");
        }

        if (request.nit() == null || request.nit().trim().isEmpty()) {
            throw new IllegalArgumentException("NIT cannot be null or empty");
        }

        if (request.direccion() == null || request.direccion().trim().isEmpty()) {
            throw new IllegalArgumentException("Direccion cannot be null or empty");
        }

        if (request.tipoRegimen() == null || request.tipoRegimen().trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo de regimen cannot be null or empty");
        }

        if (request.municipio() == null || request.municipio().trim().isEmpty()) {
            throw new IllegalArgumentException("Municipio cannot be null or empty");
        }

        if (request.autorrentenedor() == null) {
            throw new IllegalArgumentException("Autorrentenedor cannot be null");
        }

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
