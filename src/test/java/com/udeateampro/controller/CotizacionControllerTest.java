package com.udeateampro.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.udeateampro.controller.dto.CreateCotizacionRequest;
import com.udeateampro.entity.Cotizacion;
import com.udeateampro.service.CotizacionService;

@ExtendWith(MockitoExtension.class)
class CotizacionControllerTest {

    @Mock
    private CotizacionService cotizacionService;

    @InjectMocks
    private CotizacionController cotizacionController;

    private Cotizacion cotizacion1;
    private Cotizacion cotizacion2;
    private CreateCotizacionRequest createRequest;

    @BeforeEach
    void setUp() {
        cotizacion1 = new Cotizacion();
        cotizacion1.setId(1L);
        cotizacion1.setUsuario(1L);
        cotizacion1.setCliente(1L);
        cotizacion1.setEstado("PENDIENTE");
        cotizacion1.setFechaCreacion(LocalDate.now());
        cotizacion1.setFechaValidez(LocalDate.now().plusDays(30));
        cotizacion1.setMargenGeneral(new BigDecimal("15.50"));
        cotizacion1.setMonedaCotizacion("COP");

        cotizacion2 = new Cotizacion();
        cotizacion2.setId(2L);
        cotizacion2.setUsuario(2L);
        cotizacion2.setCliente(2L);
        cotizacion2.setEstado("APROBADA");
        cotizacion2.setFechaCreacion(LocalDate.now().minusDays(5));
        cotizacion2.setFechaValidez(LocalDate.now().plusDays(25));
        cotizacion2.setMargenGeneral(new BigDecimal("12.75"));
        cotizacion2.setMonedaCotizacion("USD");

        createRequest = new CreateCotizacionRequest(
                3L,
                3L,
                "BORRADOR",
                LocalDate.now(),
                LocalDate.now().plusDays(15),
                new BigDecimal("18.25"),
                "EUR"
        );
    }

    @Test
    void addCotizacionShouldReturnCreatedCotizacion() {
        when(cotizacionService.createCotizacion(any(CreateCotizacionRequest.class))).thenReturn(cotizacion1);

        ResponseEntity<Cotizacion> response = cotizacionController.addCotizacion(createRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(cotizacion1, response.getBody());
        verify(cotizacionService).createCotizacion(createRequest);
    }

    @Test
    void getAllCotizacionesShouldReturnAllCotizaciones() {
        List<Cotizacion> cotizaciones = List.of(cotizacion1, cotizacion2);
        when(cotizacionService.getAllCotizaciones()).thenReturn(cotizaciones);

        ResponseEntity<List<Cotizacion>> response = cotizacionController.getAllCotizaciones();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(cotizaciones, response.getBody());
        assertEquals(2, response.getBody().size());
        verify(cotizacionService).getAllCotizaciones();
    }

    @Test
    void getAllCotizacionesShouldReturnEmptyListWhenNoCotizaciones() {
        when(cotizacionService.getAllCotizaciones()).thenReturn(List.of());

        ResponseEntity<List<Cotizacion>> response = cotizacionController.getAllCotizaciones();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(cotizacionService).getAllCotizaciones();
    }

    @Test
    void updateCotizacionShouldReturnUpdatedCotizacion() {
        Cotizacion updatedCotizacion = new Cotizacion();
        updatedCotizacion.setId(1L);
        updatedCotizacion.setUsuario(1L);
        updatedCotizacion.setCliente(1L);
        updatedCotizacion.setEstado("APROBADA");
        updatedCotizacion.setFechaCreacion(cotizacion1.getFechaCreacion());
        updatedCotizacion.setFechaValidez(cotizacion1.getFechaValidez());
        updatedCotizacion.setMargenGeneral(new BigDecimal("20.00"));
        updatedCotizacion.setMonedaCotizacion("COP");

        when(cotizacionService.updateCotizacion(anyLong(), any(Cotizacion.class))).thenReturn(updatedCotizacion);

        ResponseEntity<Cotizacion> response = cotizacionController.updateCotizacion(1L, cotizacion1);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedCotizacion, response.getBody());
        verify(cotizacionService).updateCotizacion(1L, cotizacion1);
    }

    @Test
    void deleteCotizacionShouldReturnNoContent() {
        doNothing().when(cotizacionService).deleteCotizacion(anyLong());

        ResponseEntity<Cotizacion> response = cotizacionController.deleteCotizacion(1L);

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(cotizacionService).deleteCotizacion(1L);
    }

    @Test
    void addCotizacionShouldCallServiceWithCorrectRequest() {
        when(cotizacionService.createCotizacion(createRequest)).thenReturn(cotizacion1);

        cotizacionController.addCotizacion(createRequest);

        verify(cotizacionService).createCotizacion(createRequest);
    }

    @Test
    void updateCotizacionShouldCallServiceWithCorrectParameters() {
        when(cotizacionService.updateCotizacion(1L, cotizacion1)).thenReturn(cotizacion1);

        cotizacionController.updateCotizacion(1L, cotizacion1);

        verify(cotizacionService).updateCotizacion(1L, cotizacion1);
    }

    @Test
    void deleteCotizacionShouldCallServiceWithCorrectId() {
        doNothing().when(cotizacionService).deleteCotizacion(1L);

        cotizacionController.deleteCotizacion(1L);

        verify(cotizacionService).deleteCotizacion(1L);
    }
}