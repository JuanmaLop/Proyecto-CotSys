package com.udeateampro.controller;

import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestParam;
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

    //Crear producto (admin o líder técnico)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'LIDER_TECNICO')")
    @PostMapping("/create-producto")
    public ResponseEntity<Producto> addProducto(@RequestBody final CreateProductRequest request) {
        Producto newProducto = productoService.createProducto(request);
        return ResponseEntity.ok(newProducto);
    }

    @GetMapping("/get-product")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }

    //Obtener productos (admin o líder técnico)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'LIDER_TECNICO', 'COMERCIAL')")
    @GetMapping("/get-all-productos")
    public ResponseEntity<List<Producto>> getAllProductos() {
        return ResponseEntity.ok(productoService.getAllProductos());
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'LIDER_TECNICO', 'COMERCIAL')")
    @GetMapping("/{id}/get-producto-by-id")
    public ResponseEntity<Optional<Producto>> getProductoById(@PathVariable Long id) {
        Optional<Producto> producto = productoService.getProductoById(id);
        return ResponseEntity.ok(producto);
    }

    //Actualizar producto (admin o líder técnico)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'LIDER_TECNICO')")
    @PutMapping("/{id_producto}/update-producto")
    public ResponseEntity<Producto> updateProducto(@PathVariable Long id_producto, @RequestBody Producto producto) {
        Producto updatedProducto = productoService.updateProducto(id_producto, producto);
        return ResponseEntity.ok(updatedProducto);
    }

    //Eliminar producto (admin o líder técnico)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'LIDER_TECNICO')")
    @DeleteMapping(("/{id_producto}/delete-producto"))
    public ResponseEntity<Producto> deleteProducto(@PathVariable Long id_producto) {
        productoService.deleteProducto(id_producto);
        return ResponseEntity.noContent().build();
    }

}
