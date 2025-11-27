package com.udeateampro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udeateampro.controller.dto.CreateClienteRequest;
import com.udeateampro.entity.Cliente;
import com.udeateampro.service.ClienteService;


@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    //Crear cliente
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('COMERCIAL')")
    @PostMapping("/create-cliente")
    public ResponseEntity<Cliente> addProducto(@RequestBody final CreateClienteRequest request) {
        Cliente newCliente = clienteService.createCliente(request);
        return ResponseEntity.ok(newCliente);
    }

    //Obtener clientes
    @GetMapping("/get-all-clientes")
    public ResponseEntity<List<Cliente>> getAllClientes() {
        return ResponseEntity.ok(clienteService.getAllClientes());
    }

    //Actualizar cliente
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('COMERCIAL')")
    @PutMapping("/{id}/update-cliente")
    public ResponseEntity<Cliente> updateCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        Cliente updatedCliente = clienteService.updateCliente(id, cliente);
        return ResponseEntity.ok(updatedCliente);
    }

    //Eliminar cliente
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('COMERCIAL')")
    @DeleteMapping(("/{id}/delete-cliente"))
    public ResponseEntity<Cliente> deleteCliente(@PathVariable Long id) {
        clienteService.deleteCliente(id);
        return ResponseEntity.noContent().build();
    }

}
