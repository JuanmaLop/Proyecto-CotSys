package com.udeateampro.service;


import com.udeateampro.entity.Cliente;
import com.udeateampro.entity.Producto;
import com.udeateampro.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente createCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    public Cliente updateCliente(Long id, Cliente clienteActualizado) {
        Optional<Cliente> existingCliente = clienteRepository.findById(id);

        if(existingCliente.isPresent()) {
            Cliente cliente = existingCliente.get();
            cliente.setNombre(clienteActualizado.getNombre());
            cliente.setNit(clienteActualizado.getNit());
            cliente.setDireccion( clienteActualizado.getDireccion());
            cliente.setTipoRegimen(clienteActualizado.getTipoRegimen());
            cliente.setAutorrentenedor(clienteActualizado.getAutorrentenedor());
            cliente.setMunicipio(clienteActualizado.getMunicipio());
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
