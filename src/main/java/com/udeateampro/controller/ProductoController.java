package com.udeateampro.controller;

import com.udeateampro.entity.Producto;
import com.udeateampro.service.ProductoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")//Permite peticiones desde el Front

public class ProductoController {

    @Autowired
    private ProductoService productoService;

    //Crear producto
    @PostMapping
    public ResponseEntity<Producto> addProducto(@RequestBody Producto producto) {
        Producto newProducto = productoService.createProducto(producto);
        return ResponseEntity.ok(newProducto);
    }

    //Obtener productos
    @GetMapping
    public ResponseEntity<List<Producto>> getAllProductos() {
        return ResponseEntity.ok(productoService.getAllProductos());
    }

    //Actualizar producto
    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProducto(@PathVariable Long id, @RequestBody Producto producto) {
        Producto updatedProducto = productoService.updateProducto(id, producto);
        return ResponseEntity.ok(updatedProducto);
    }

    //Eliminar producto
    @DeleteMapping(("/{id}"))
    public ResponseEntity<Producto> deleteProducto(@PathVariable Long id) {
        productoService.deleteProducto(id);
        return ResponseEntity.noContent().build();
    }

}
