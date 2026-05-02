package com.udeateampro.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udeateampro.controller.dto.CreateProductRequest;
import com.udeateampro.controller.dto.UpdateProductRequest;
import com.udeateampro.entity.Producto;
import com.udeateampro.repository.ProductoRepository;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

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
    public Producto updateProducto(Long idProducto, UpdateProductRequest request) {
        Optional<Producto> existingProducto = productoRepository.findById(idProducto);

        if(existingProducto.isPresent()) {
            Producto producto = existingProducto.get();
            producto.setNombre(request.nombre());
            producto.setDescripcion(request.descripcion());
            producto.setCategoria(request.categoria());
            producto.setUnidadMedida(request.unidadMedida());
            producto.setCostoBase(request.costoBase());
            producto.setMonedaOriginal(request.monedaOriginal());
            producto.setTipo(request.tipo());
            producto.setEstado(request.estado());
            return productoRepository.save(producto);
        }else{
            throw new IllegalArgumentException("No existe el producto con id:" + idProducto);
        }
    }

    public void deleteProducto(Long idProducto) {
        if(!productoRepository.existsById(idProducto)) {
            throw new IllegalArgumentException("No existe el producto con id:" + idProducto);
        }
        productoRepository.deleteById(idProducto);
    }
}