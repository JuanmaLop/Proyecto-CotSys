package com.udeateampro.controller;

import com.udeateampro.entity.Cliente;
import com.udeateampro.service.ClienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    //Crear cliente
    @PostMapping
    public ResponseEntity<Cliente> addProducto(@RequestBody Cliente cliente) {
        Cliente newCliente = clienteService.createCliente(cliente);
        return ResponseEntity.ok(newCliente);
    }

    //Obtener clientes
    @GetMapping
    public ResponseEntity<List<Cliente>> getAllClientes() {
        return ResponseEntity.ok(clienteService.getAllClientes());
    }

    //Actualizar cliente
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        Cliente updatedCliente = clienteService.updateCliente(id, cliente);
        return ResponseEntity.ok(updatedCliente);
    }

    //Eliminar cliente
    @DeleteMapping(("/{id}"))
    public ResponseEntity<Cliente> deleteCliente(@PathVariable Long id) {
        clienteService.deleteCliente(id);
        return ResponseEntity.noContent().build();
    }

}
