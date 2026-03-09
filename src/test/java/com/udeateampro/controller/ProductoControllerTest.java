package com.udeateampro.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.udeateampro.controller.dto.CreateProductRequest;
import com.udeateampro.entity.Producto;
import com.udeateampro.service.ProductoService;

@ExtendWith(MockitoExtension.class)
class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController productoController;

    private Producto producto1;
    private Producto producto2;
    private CreateProductRequest createRequest;

    @BeforeEach
    void setUp() {
        producto1 = Producto.builder()
                .id_producto(1L)
                .nombre("Producto A")
                .descripcion("Descripción del producto A")
                .categoria("Electrónica")
                .unidadMedida("Unidad")
                .costoBase(100.0)
                .monedaOriginal("COP")
                .tipo("Hardware")
                .estado(true)
                .build();

        producto2 = Producto.builder()
                .id_producto(2L)
                .nombre("Producto B")
                .descripcion("Descripción del producto B")
                .categoria("Software")
                .unidadMedida("Licencia")
                .costoBase(500.0)
                .monedaOriginal("USD")
                .tipo("Software")
                .estado(true)
                .build();

        createRequest = new CreateProductRequest(
                "Producto Nuevo",
                "Descripción del producto nuevo",
                "Herramientas",
                "Pieza",
                250.0,
                "EUR",
                "Material"
        );
    }

    @Test
    void addProductoShouldReturnCreatedProducto() {
        when(productoService.createProducto(any(CreateProductRequest.class))).thenReturn(producto1);

        ResponseEntity<Producto> response = productoController.addProducto(createRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(producto1, response.getBody());
        verify(productoService).createProducto(createRequest);
    }

    @Test
    void getAllProductosShouldReturnAllProductos() {
        List<Producto> productos = List.of(producto1, producto2);
        when(productoService.getAllProductos()).thenReturn(productos);

        ResponseEntity<List<Producto>> response = productoController.getAllProductos();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(productos, response.getBody());
        assertEquals(2, response.getBody().size());
        verify(productoService).getAllProductos();
    }

    @Test
    void getAllProductosShouldReturnEmptyListWhenNoProductos() {
        when(productoService.getAllProductos()).thenReturn(List.of());

        ResponseEntity<List<Producto>> response = productoController.getAllProductos();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(productoService).getAllProductos();
    }

    @Test
    void updateProductoShouldReturnUpdatedProducto() {
        Producto updatedProducto = Producto.builder()
                .id_producto(1L)
                .nombre("Producto A Actualizado")
                .descripcion("Descripción actualizada")
                .categoria("Electrónica")
                .unidadMedida("Unidad")
                .costoBase(150.0)
                .monedaOriginal("COP")
                .tipo("Hardware")
                .estado(true)
                .build();

        when(productoService.updateProducto(anyLong(), any(Producto.class))).thenReturn(updatedProducto);

        ResponseEntity<Producto> response = productoController.updateProducto(1L, producto1);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedProducto, response.getBody());
        verify(productoService).updateProducto(1L, producto1);
    }

    @Test
    void deleteProductoShouldReturnNoContent() {
        doNothing().when(productoService).deleteProducto(anyLong());

        ResponseEntity<Producto> response = productoController.deleteProducto(1L);

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(productoService).deleteProducto(1L);
    }

    @Test
    void addProductoShouldCallServiceWithCorrectRequest() {
        when(productoService.createProducto(createRequest)).thenReturn(producto1);

        productoController.addProducto(createRequest);

        verify(productoService).createProducto(createRequest);
    }

    @Test
    void updateProductoShouldCallServiceWithCorrectParameters() {
        when(productoService.updateProducto(1L, producto1)).thenReturn(producto1);

        productoController.updateProducto(1L, producto1);

        verify(productoService).updateProducto(1L, producto1);
    }

    @Test
    void deleteProductoShouldCallServiceWithCorrectId() {
        doNothing().when(productoService).deleteProducto(1L);

        productoController.deleteProducto(1L);

        verify(productoService).deleteProducto(1L);
    }
}