package com.udeateampro.CotSys.service;

import com.udeateampro.CotSys.model.Producto;
import com.udeateampro.CotSys.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    //Obtener todos los productos
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    //Obtener producto por id
    public Optional<Producto> getProductoById(String id) {
        return productoRepository.findById(id);
    }

    //crear producto
    public Producto createProducto(Producto producto) {
        //validaciones de búsqueda

        return productoRepository.save(producto);
    }

    //Actualizar producto
    public Producto updateProducto(String id, Producto updatedProducto) {
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
        }else {
            throw new RuntimeException("No existe el producto con id:" + id);
        }
    }

    public void deleteProducto(String id) {
        if(!productoRepository.existsById(id)) {
            throw new RuntimeException("No existe el producto con id:" + id);
        }
        productoRepository.deleteById(id);
    }


}