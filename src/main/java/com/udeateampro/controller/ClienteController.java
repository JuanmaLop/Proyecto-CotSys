package com.udeateampro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udeateampro.controller.dto.CreateClienteRequest;
import com.udeateampro.controller.dto.UpdateClienteRequest;
import com.udeateampro.entity.Cliente;
import com.udeateampro.service.ClienteService;


@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }


    //Crear cliente (solo administrador)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/create-cliente")
    public ResponseEntity<Cliente> addProducto(@RequestBody final CreateClienteRequest request) {
        Cliente newCliente = clienteService.createCliente(request);
        return ResponseEntity.ok(newCliente);
    }

    //Obtener clientes (admin o comercial para consulta)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COMERCIAL')")
    @GetMapping("/get-all-clientes")
    public ResponseEntity<List<Cliente>> getAllClientes() {
        return ResponseEntity.ok(clienteService.getAllClientes());
    }

    //Actualizar cliente (solo administrador)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{idCliente}/update-cliente")
    public ResponseEntity<Cliente> updateCliente(@PathVariable Long idCliente, @RequestBody UpdateClienteRequest request) {
        Cliente updatedCliente = clienteService.updateCliente(idCliente, request);
        return ResponseEntity.ok(updatedCliente);
    }

    //Eliminar cliente (solo administrador)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping(("/{idCliente}/delete-cliente"))
    public ResponseEntity<Cliente> deleteCliente(@PathVariable Long idCliente) {
        clienteService.deleteCliente(idCliente);
        return ResponseEntity.noContent().build();
    }

}
