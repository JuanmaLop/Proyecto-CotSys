package com.udeateampro.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udeateampro.controller.dto.CreateClienteRequest;
import com.udeateampro.controller.dto.UpdateClienteRequest;
import com.udeateampro.entity.Cliente;
import com.udeateampro.repository.ClienteRepository;
import com.udeateampro.service.validator.RequestValidator;

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
        RequestValidator.validateRequiredString(request.nombre(), "Nombre");
        RequestValidator.validateRequiredString(request.nit(), "NIT");
        RequestValidator.validateRequiredString(request.direccion(), "Direccion");
        RequestValidator.validateRequiredString(request.tipoRegimen(), "Tipo de regimen");
        RequestValidator.validateRequiredString(request.municipio(), "Municipio");
        RequestValidator.validateNotNull(request.autorrentenedor(), "Autorrentenedor");
    }

    //Actualizar cliente
    public Cliente updateCliente(Long idCliente, UpdateClienteRequest request) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new IllegalArgumentException("No existe el cliente con id:" + idCliente));
        
        updateClienteFields(cliente, request);
        return clienteRepository.save(cliente);
    }

    private void updateClienteFields(Cliente cliente, UpdateClienteRequest request) {
        cliente.setNombre(request.nombre());
        cliente.setNit(request.nit());
        cliente.setDireccion(request.direccion());
        cliente.setTipoRegimen(request.tipoRegimen());
        cliente.setMunicipio(request.municipio());
    }

    public void deleteCliente(Long idCliente) {
        RequestValidator.validateExists(clienteRepository.existsById(idCliente), "cliente", idCliente);
        clienteRepository.deleteById(idCliente);
    }
}
