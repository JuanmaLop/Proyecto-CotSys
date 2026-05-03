package com.udeateampro.controller;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.udeateampro.controller.dto.CreateProductRequest;
import com.udeateampro.controller.dto.UpdateProductRequest;
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
                .idProducto(1L)
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
                .idProducto(2L)
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
        assertEquals(200, response.getStatusCode().value());
        assertEquals(producto1, response.getBody());
        verify(productoService).createProducto(createRequest);
    }

    @Test
    void getAllProductosShouldReturnAllProductos() {
        List<Producto> productos = List.of(producto1, producto2);
        when(productoService.getAllProductos()).thenReturn(productos);

        ResponseEntity<List<Producto>> response = productoController.getAllProductos();
        List<Producto> body = response.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(body); 
        assertEquals(productos, body);
        assertEquals(2, body.size());
        verify(productoService).getAllProductos();
    }

    @Test
    void getAllProductosShouldReturnEmptyListWhenNoProductos() {
        when(productoService.getAllProductos()).thenReturn(List.of());

        ResponseEntity<List<Producto>> response = productoController.getAllProductos();
        List<Producto> body = response.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(body);
        assertTrue(body.isEmpty());
        verify(productoService).getAllProductos();
    }

    @Test
    void updateProductoShouldReturnUpdatedProducto() {
        Producto updatedProducto = Producto.builder()
                .idProducto(1L)
                .nombre("Producto A Actualizado")
                .descripcion("Descripción actualizada")
                .categoria("Electrónica")
                .unidadMedida("Unidad")
                .costoBase(150.0)
                .monedaOriginal("COP")
                .tipo("Hardware")
                .estado(true)
                .build();

        UpdateProductRequest updateRequest = new UpdateProductRequest(
                "Producto Actualizado",
                "Descripción actualizada",
                "Electrónica",
                "Unidad",
                150.0,
                "COP",
                "Hardware",
                true
        );

        when(productoService.updateProducto(anyLong(), any(UpdateProductRequest.class))).thenReturn(updatedProducto);

        ResponseEntity<Producto> response = productoController.updateProducto(1L, updateRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(updatedProducto, response.getBody());
        verify(productoService).updateProducto(1L, updateRequest);
    }

    @Test
    void deleteProductoShouldReturnNoContent() {
        doNothing().when(productoService).deleteProducto(anyLong());

        ResponseEntity<Producto> response = productoController.deleteProducto(1L);

        assertNotNull(response);
        assertEquals(204, response.getStatusCode().value());
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
        UpdateProductRequest updateRequest = new UpdateProductRequest(
                producto1.getNombre(),
                producto1.getDescripcion(),
                producto1.getCategoria(),
                producto1.getUnidadMedida(),
                producto1.getCostoBase(),
                producto1.getMonedaOriginal(),
                producto1.getTipo(),
                producto1.getEstado()
        );

        when(productoService.updateProducto(1L, updateRequest)).thenReturn(producto1);

        productoController.updateProducto(1L, updateRequest);

        verify(productoService).updateProducto(1L, updateRequest);
    }

    @Test
    void deleteProductoShouldCallServiceWithCorrectId() {
        doNothing().when(productoService).deleteProducto(1L);

        productoController.deleteProducto(1L);

        verify(productoService).deleteProducto(1L);
    }
}