package com.udeateampro.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.udeateampro.controller.dto.CreateProductRequest;
import com.udeateampro.entity.Producto;
import com.udeateampro.repository.ProductoRepository;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto1;
    private Producto producto2;
    private CreateProductRequest createRequest;

    @BeforeEach
    public void setUp() {
        producto1 = Producto.builder()
                .id_producto(1L)
                .nombre("Producto 1")
                .descripcion("Descripción del producto 1")
                .categoria("Categoría 1")
                .unidadMedida("Unidad 1")
                .costoBase(100.0)
                .monedaOriginal("USD")
                .tipo("Tipo 1")
                .estado(true)
                .build();

        producto2 = Producto.builder()
                .id_producto(2L)
                .nombre("Producto 2")
                .descripcion("Descripción del producto 2")
                .categoria("Categoría 2")
                .unidadMedida("Unidad 2")
                .costoBase(200.0)
                .monedaOriginal("COP")
                .tipo("Tipo 2")
                .estado(false)
                .build();

        createRequest = new CreateProductRequest(
                "Nuevo Producto",
                "Descripción del nuevo producto",
                "Nueva Categoría",
                "Nueva Unidad",
                150.0,
                "EUR",
                "Nuevo Tipo"
        );
    }

    @Test
    void getAllProductosShouldReturnAllProductosList() {
        when(productoRepository.findAll()).thenReturn(List.of(producto1, producto2));

        List<Producto> productos = productoService.getAllProductos();

        assertNotNull(productos);
        assertEquals(2, productos.size());
        assertEquals(producto1.getId_producto(), productos.get(0).getId_producto());
        assertEquals(producto2.getId_producto(), productos.get(1).getId_producto());
    }

    @Test
    void getAllProductosShouldReturnEmptyListWhenNoProductos() {
        when(productoRepository.findAll()).thenReturn(List.of());

        List<Producto> productos = productoService.getAllProductos();

        assertNotNull(productos);
        assertTrue(productos.isEmpty());
    }

    @Test
    void getProductoByIdShouldReturnProductoWhenFound() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto1));

        Optional<Producto> producto = productoService.getProductoById(1L);

        assertTrue(producto.isPresent());
        assertEquals(producto1.getId_producto(), producto.get().getId_producto());
    }

    @Test
    void getProductoByIdShouldReturnEmptyWhenNotFound() {
        when(productoRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Producto> producto = productoService.getProductoById(1L);

        assertFalse(producto.isPresent());
    }

    @Test
    void createProductoShouldReturnCreatedProducto() {
        when(productoRepository.save(any(Producto.class))).thenReturn(producto1);

        Producto createdProducto = productoService.createProducto(createRequest);

        assertNotNull(createdProducto);
        assertEquals(producto1.getId_producto(), createdProducto.getId_producto());
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void updateProductoShouldReturnUpdatedProducto() {
        Producto updatedProducto = Producto.builder()
                .nombre("Producto Actualizado")
                .descripcion("Descripción actualizada")
                .categoria("Categoría Actualizada")
                .unidadMedida("Unidad Actualizada")
                .costoBase(250.0)
                .monedaOriginal("GBP")
                .tipo("Tipo Actualizado")
                .estado(false)
                .build();

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto1));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto1);

        Producto result = productoService.updateProducto(1L, updatedProducto);

        assertNotNull(result);
        assertEquals(producto1.getId_producto(), result.getId_producto());
        verify(productoRepository).save(producto1);
    }

    @Test
    void updateProductoShouldThrowExceptionWhenProductoNotFound() {
        Producto updatedProducto = Producto.builder()
                .nombre("Producto Actualizado")
                .descripcion("Descripción actualizada")
                .categoria("Categoría Actualizada")
                .unidadMedida("Unidad Actualizada")
                .costoBase(250.0)
                .monedaOriginal("GBP")
                .tipo("Tipo Actualizado")
                .estado(false)
                .build();

        when(productoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productoService.updateProducto(1L, updatedProducto));
    }

    @Test
    void deleteProductoShouldDeleteWhenFound() {
        when(productoRepository.existsById(1L)).thenReturn(true);

        productoService.deleteProducto(1L);

        verify(productoRepository).deleteById(1L);
    }

    @Test
    void deleteProductoShouldThrowExceptionWhenProductoNotFound() {
        when(productoRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> productoService.deleteProducto(1L));
    }
}
