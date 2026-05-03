package com.udeateampro.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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

import com.udeateampro.controller.dto.CreateCotizacionRequest;
import com.udeateampro.controller.dto.UpdateCotizacionRequest;
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
    public void setUp() {
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
        assertEquals(200, response.getStatusCode().value());
        assertEquals(cotizacion1, response.getBody());
        verify(cotizacionService).createCotizacion(createRequest);
    }

    @Test
    void getAllCotizacionesShouldReturnAllCotizaciones() {
        List<Cotizacion> cotizaciones = List.of(cotizacion1, cotizacion2);
        when(cotizacionService.getAllCotizaciones()).thenReturn(cotizaciones);

        ResponseEntity<List<Cotizacion>> response = cotizacionController.getAllCotizaciones();
        List<Cotizacion> body = response.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(body);
        assertEquals(cotizaciones, response.getBody());
        assertEquals(cotizaciones, body);
        assertEquals(2, body.size());
        verify(cotizacionService).getAllCotizaciones();
    }

    @Test
    void getAllCotizacionesShouldReturnEmptyListWhenNoCotizaciones() {
        when(cotizacionService.getAllCotizaciones()).thenReturn(List.of());

        ResponseEntity<List<Cotizacion>> response = cotizacionController.getAllCotizaciones();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(List.of(), response.getBody());
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

        UpdateCotizacionRequest updateRequest = new UpdateCotizacionRequest(
                "APROBADA",
                cotizacion1.getFechaCreacion(),
                cotizacion1.getFechaValidez(),
                new BigDecimal("20.00"),
                "COP",
                1L,
                1L
        );

        when(cotizacionService.updateCotizacion(anyLong(), any(UpdateCotizacionRequest.class))).thenReturn(updatedCotizacion);

        ResponseEntity<Cotizacion> response = cotizacionController.updateCotizacion(1L, updateRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(updatedCotizacion, response.getBody());
        verify(cotizacionService).updateCotizacion(1L, updateRequest);
    }

    @Test
    void deleteCotizacionShouldReturnNoContent() {
        doNothing().when(cotizacionService).deleteCotizacion(anyLong());

        ResponseEntity<Cotizacion> response = cotizacionController.deleteCotizacion(1L);

        assertNotNull(response);
        assertEquals(204, response.getStatusCode().value());
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
        UpdateCotizacionRequest updateRequest = new UpdateCotizacionRequest(
                cotizacion1.getEstado(),
                cotizacion1.getFechaCreacion(),
                cotizacion1.getFechaValidez(),
                cotizacion1.getMargenGeneral(),
                cotizacion1.getMonedaCotizacion(),
                cotizacion1.getUsuario(),
                cotizacion1.getCliente()
        );

        when(cotizacionService.updateCotizacion(1L, updateRequest)).thenReturn(cotizacion1);

        cotizacionController.updateCotizacion(1L, updateRequest);

        verify(cotizacionService).updateCotizacion(1L, updateRequest);
    }

    @Test
    void deleteCotizacionShouldCallServiceWithCorrectId() {
        doNothing().when(cotizacionService).deleteCotizacion(1L);

        cotizacionController.deleteCotizacion(1L);

        verify(cotizacionService).deleteCotizacion(1L);
    }
}