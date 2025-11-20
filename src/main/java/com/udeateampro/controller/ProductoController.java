package com.udeateampro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udeateampro.controller.dto.CreateProductRequest;
import com.udeateampro.entity.Producto;
import com.udeateampro.service.ProductoService;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")//Permite peticiones desde el Front

public class ProductoController {

    @Autowired
    private ProductoService productoService;

    //Crear producto
    @PostMapping("/create-producto")
    public ResponseEntity<Producto> addProducto(@RequestBody final CreateProductRequest request) {
        Producto newProducto = productoService.createProducto(request);
        return ResponseEntity.ok(newProducto);
    }

    //Obtener productos
    @GetMapping("/get-all-productos")
    public ResponseEntity<List<Producto>> getAllProductos() {
        return ResponseEntity.ok(productoService.getAllProductos());
    }

    //Actualizar producto
    @PutMapping("/{id}/update-producto")
    public ResponseEntity<Producto> updateProducto(@PathVariable Long id, @RequestBody Producto producto) {
        Producto updatedProducto = productoService.updateProducto(id, producto);
        return ResponseEntity.ok(updatedProducto);
    }

    //Eliminar producto
    @DeleteMapping(("/{id}/delete-producto"))
    public ResponseEntity<Producto> deleteProducto(@PathVariable Long id) {
        productoService.deleteProducto(id);
        return ResponseEntity.noContent().build();
    }

}
