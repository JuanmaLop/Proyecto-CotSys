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
    @PutMapping("/{id_cliente}/update-cliente")
    public ResponseEntity<Cliente> updateCliente(@PathVariable Long id_cliente, @RequestBody Cliente cliente) {
        Cliente updatedCliente = clienteService.updateCliente(id_cliente, cliente);
        return ResponseEntity.ok(updatedCliente);
    }

    //Eliminar cliente (solo administrador)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping(("/{id_cliente}/delete-cliente"))
    public ResponseEntity<Cliente> deleteCliente(@PathVariable Long id_cliente) {
        clienteService.deleteCliente(id_cliente);
        return ResponseEntity.noContent().build();
    }

}
