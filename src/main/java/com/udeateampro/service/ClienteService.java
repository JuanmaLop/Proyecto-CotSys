package com.udeateampro.service;


import java.util.List;
import java.util.Optional;

import com.udeateampro.controller.dto.CreateClienteRequest;
import com.udeateampro.controller.dto.CreateProductRequest;
import com.udeateampro.entity.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udeateampro.entity.Cliente;
import com.udeateampro.repository.ClienteRepository;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    //Obtener todos los clientes
    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    //Obtener cliente por id
    public Optional<Cliente> getClienteById(Long id) {
        return clienteRepository.findById(id);
    }

    //crear cliente
    public Cliente createCliente(CreateClienteRequest request) {
        var cliente = Cliente.builder()
                .nombre(request.nombre())
                .nit(request.nit())
                .direccion(request.direccion())
                .tipoRegimen(request.tipoRegimen())
                .municipio(request.municipio())
                .build();
        return clienteRepository.save(cliente);
    }

    //Actualizar cliente
    public Cliente updateCliente(Long id, Cliente updatedCliente) {
        Optional<Cliente> existingCliente = clienteRepository.findById(id);

        if(existingCliente.isPresent()) {
            Cliente cliente = existingCliente.get();
            cliente.setNombre(updatedCliente.getNombre());
            cliente.setNit(updatedCliente.getNit());
            cliente.setDireccion(updatedCliente.getDireccion());
            cliente.setTipoRegimen(updatedCliente.getTipoRegimen());
            cliente.setMunicipio(updatedCliente.getMunicipio());
            return clienteRepository.save(cliente);
        }else{
            throw new RuntimeException("No existe el cliente con id:" + id);
        }
    }

    public void deleteCliente(Long id) {
        if(!clienteRepository.existsById(id)) {
            throw new RuntimeException("No existe el cliente con id:" + id);
        }
        clienteRepository.deleteById(id);
    }
}
