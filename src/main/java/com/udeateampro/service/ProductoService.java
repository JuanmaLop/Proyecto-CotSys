package com.udeateampro.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udeateampro.controller.dto.CreateProductRequest;
import com.udeateampro.entity.Producto;
import com.udeateampro.repository.ProductoRepository;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    //Obtener todos los productos
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    //Obtener producto por id
    public Optional<Producto> getProductoById(Long id) {
        return productoRepository.findById(id);
    }

    //crear producto
    public Producto createProducto(CreateProductRequest request) {
        var producto = Producto.builder()
                .nombre(request.nombre())
                .descripcion(request.descripcion())
                .categoria(request.categoria())
                .unidadMedida(request.unidadMedida())
                .costoBase(request.costoBase())
                .monedaOriginal(request.monedaOriginal())
                .tipo(request.tipo())
                .build();
        return productoRepository.save(producto);    
    }

    //Actualizar producto
    public Producto updateProducto(Long id, Producto updatedProducto) {
        Optional<Producto> existingProducto = productoRepository.findById(id);

        if(existingProducto.isPresent()) {
            Producto producto = existingProducto.get();
            producto.setNombre(updatedProducto.getNombre());
            producto.setDescripcion(updatedProducto.getDescripcion());
            producto.setCategoria(updatedProducto.getCategoria());
            producto.setUnidadMedida(updatedProducto.getUnidadMedida());
            producto.setCostoBase(updatedProducto.getCostoBase());
            producto.setMonedaOriginal(updatedProducto.getMonedaOriginal());
            producto.setTipo(updatedProducto.getTipo());
            producto.setTipo(updatedProducto.getTipo());
            producto.setEstado(updatedProducto.getEstado());
            return productoRepository.save(producto);
        }else{
            throw new RuntimeException("No existe el producto con id:" + id);
        }
    }

    public void deleteProducto(Long id) {
        if(!productoRepository.existsById(id)) {
            throw new RuntimeException("No existe el producto con id:" + id);
        }
        productoRepository.deleteById(id);
    }
}