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

import com.udeateampro.controller.dto.CreateProductRequest;
import com.udeateampro.controller.dto.UpdateProductRequest;
import com.udeateampro.entity.Producto;
import com.udeateampro.service.ProductoService;

@RestController
@RequestMapping("/api/productos")

public class ProductoController {
    private final ProductoService productoService;
    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    //Crear producto (admin o líder técnico)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'LIDER_TECNICO')")
    @PostMapping("/create-producto")
    public ResponseEntity<Producto> addProducto(@RequestBody final CreateProductRequest request) {
        Producto newProducto = productoService.createProducto(request);
        return ResponseEntity.ok(newProducto);
    }

    //Obtener productos (admin o líder técnico)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'LIDER_TECNICO')")
    @GetMapping("/get-all-productos")
    public ResponseEntity<List<Producto>> getAllProductos() {
        return ResponseEntity.ok(productoService.getAllProductos());
    }

    //Actualizar producto (admin o líder técnico)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'LIDER_TECNICO')")
    @PutMapping("/{idProducto}/update-producto")
    public ResponseEntity<Producto> updateProducto(@PathVariable Long idProducto, @RequestBody UpdateProductRequest request) {
        Producto updatedProducto = productoService.updateProducto(idProducto, request);
        return ResponseEntity.ok(updatedProducto);
    }

    //Eliminar producto (admin o líder técnico)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'LIDER_TECNICO')")
    @DeleteMapping(("/{idProducto}/delete-producto"))
    public ResponseEntity<Producto> deleteProducto(@PathVariable Long idProducto) {
        productoService.deleteProducto(idProducto);
        return ResponseEntity.noContent().build();
    }

}
