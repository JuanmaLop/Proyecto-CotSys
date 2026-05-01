package com.udeateampro.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.udeateampro.controller.dto.CreateCotizacionRequest;
import com.udeateampro.entity.Cotizacion;
import com.udeateampro.repository.CotizacionRepository;

@ExtendWith(MockitoExtension.class)
class CotizacionServiceTest {

    @Mock
    private CotizacionRepository cotizacionRepository;

    @InjectMocks
    private CotizacionService cotizacionService;

    private Cotizacion cotizacion1;
    private Cotizacion cotizacion2;
    private CreateCotizacionRequest createRequest;

    @BeforeEach
    void setUp() {
        createRequest = new CreateCotizacionRequest(
                1L, // usuario
                1L, // cliente
                "Test Estado",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                java.math.BigDecimal.valueOf(10.0),
                "USD"
        );

        cotizacion1 = new Cotizacion();
        cotizacion1.setId(1L);
        cotizacion1.setEstado("Test Estado");
        cotizacion1.setFechaCreacion(LocalDate.now());
        cotizacion1.setFechaValidez(LocalDate.now().plusDays(30));
        cotizacion1.setMargenGeneral(java.math.BigDecimal.valueOf(10.0));
        cotizacion1.setMonedaCotizacion("USD");
        cotizacion1.setUsuario(1L);
        cotizacion1.setCliente(1L);

        cotizacion2 = new Cotizacion();
        cotizacion2.setId(2L);
        cotizacion2.setEstado("Test Estado 2");
        cotizacion2.setFechaCreacion(LocalDate.now());
        cotizacion2.setFechaValidez(LocalDate.now().plusDays(60));
        cotizacion2.setMargenGeneral(java.math.BigDecimal.valueOf(15.0));
        cotizacion2.setMonedaCotizacion("COP");
        cotizacion2.setUsuario(2L);
        cotizacion2.setCliente(2L);
    }

    @Test
    void getAllCotizacionesShouldReturnAllCotizacionesList() {
        when(cotizacionRepository.findAll()).thenReturn(List.of(cotizacion1, cotizacion2));

        List<Cotizacion> cotizaciones = cotizacionService.getAllCotizaciones();

        assertNotNull(cotizaciones);
        assertEquals(2, cotizaciones.size());
        assertEquals(cotizacion1.getId(), cotizaciones.get(0).getId());
        assertEquals(cotizacion2.getId(), cotizaciones.get(1).getId());
    }

    @Test
    void getAllCotizacionesShouldReturnEmptyList() {
        when(cotizacionRepository.findAll()).thenReturn(List.of());

        List<Cotizacion> cotizaciones = cotizacionService.getAllCotizaciones();

        assertNotNull(cotizaciones);
        assertEquals(0, cotizaciones.size());
    }

    @Test
    void getCotizacionByIdShouldReturnCotizacionWhenFound() {
        when(cotizacionRepository.findById(1L)).thenReturn(Optional.of(cotizacion1));

        Optional<Cotizacion> cotizacion = cotizacionService.getCotizacionById(1L);

        assertTrue(cotizacion.isPresent());
        assertEquals(cotizacion1.getId(), cotizacion.get().getId());
    }

    @Test
    void getCotizacionByIdShouldReturnEmptyWhenNotFound() {
        when(cotizacionRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Cotizacion> cotizacion = cotizacionService.getCotizacionById(1L);

        assertFalse(cotizacion.isPresent());
    }

    @Test
    void createCotizacionShouldReturnCreatedCotizacion() {
        when(cotizacionRepository.save(any(Cotizacion.class))).thenReturn(cotizacion1);

        Cotizacion createdCotizacion = cotizacionService.createCotizacion(createRequest);

        assertNotNull(createdCotizacion);
        assertEquals(cotizacion1.getId(), createdCotizacion.getId());
    }

    @Test
    void createCotizacionShouldThrowExceptionWhenEstadoIsNull() {
        CreateCotizacionRequest request = new CreateCotizacionRequest(
                1L,
                1L,
                null,
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                java.math.BigDecimal.valueOf(10.0),
                "USD"
        );

        assertThrows(IllegalArgumentException.class, () -> cotizacionService.createCotizacion(request));
    }

    @Test
    void createCotizacionShouldThrowExceptionWhenEstadoIsEmpty() {
        CreateCotizacionRequest request = new CreateCotizacionRequest(
                1L,
                1L,
                "", 
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                java.math.BigDecimal.valueOf(10.0),
                "USD"
        );

        assertThrows(IllegalArgumentException.class, () -> cotizacionService.createCotizacion(request));
    }

    @Test
    void createCotizacionShouldThrowExceptionWhenFechaCreacionIsNull() {
        CreateCotizacionRequest request = new CreateCotizacionRequest(
                1L,
                1L,
                "Test Estado",
                null,
                LocalDate.now().plusDays(30),
                java.math.BigDecimal.valueOf(10.0),
                "USD"
        );

        assertThrows(IllegalArgumentException.class, () -> cotizacionService.createCotizacion(request));
    }

    @Test
    void createCotizacionShouldThrowExceptionWhenFechaValidezIsNull() {
        CreateCotizacionRequest request = new CreateCotizacionRequest(
                1L,
                1L,
                "Test Estado",
                LocalDate.now(),
                null,
                java.math.BigDecimal.valueOf(10.0),
                "USD"
        );

        assertThrows(IllegalArgumentException.class, () -> cotizacionService.createCotizacion(request));
    }

    @Test
    void createCotizacionShouldThrowExceptionWhenMargenGeneralIsNull() {
        CreateCotizacionRequest request = new CreateCotizacionRequest(
                1L,
                1L,
                "Test Estado",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                null,
                "USD"
        );

        assertThrows(IllegalArgumentException.class, () -> cotizacionService.createCotizacion(request));
    }

    @Test
    void createCotizacionShouldThrowExceptionWhenMonedaCotizacionIsNull() {
        CreateCotizacionRequest request = new CreateCotizacionRequest(
                1L,
                1L,
                "Test Estado",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                java.math.BigDecimal.valueOf(10.0),
                null
        );

        assertThrows(IllegalArgumentException.class, () -> cotizacionService.createCotizacion(request));
    }

    @Test
    void createCotizacionShouldThrowExceptionWhenMonedaCotizacionIsEmpty() {
        CreateCotizacionRequest request = new CreateCotizacionRequest(
                1L,
                1L,
                "Test Estado",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                java.math.BigDecimal.valueOf(10.0),
                ""
        );

        assertThrows(IllegalArgumentException.class, () -> cotizacionService.createCotizacion(request));
    }

    @Test
    void createCotizacionShouldThrowExceptionWhenUsuarioIsNull() {
        CreateCotizacionRequest request = new CreateCotizacionRequest(
                null,
                1L,
                "Test Estado",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                java.math.BigDecimal.valueOf(10.0),
                "USD"
        );

        assertThrows(IllegalArgumentException.class, () -> cotizacionService.createCotizacion(request));
    }

    @Test
    void createCotizacionShouldThrowExceptionWhenClienteIsNull() {
        CreateCotizacionRequest request = new CreateCotizacionRequest(
                1L,
                null,
                "Test Estado",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                java.math.BigDecimal.valueOf(10.0),
                "USD"
        );

        assertThrows(IllegalArgumentException.class, () -> cotizacionService.createCotizacion(request));
    }

    @Test
    void updateCotizacionShouldReturnUpdatedCotizacion() {
        Cotizacion updatedCotizacion = new Cotizacion();
        updatedCotizacion.setEstado("Updated Estado");
        updatedCotizacion.setFechaCreacion(LocalDate.now());
        updatedCotizacion.setFechaValidez(LocalDate.now().plusDays(30));
        updatedCotizacion.setMargenGeneral(java.math.BigDecimal.valueOf(10.0));
        updatedCotizacion.setMonedaCotizacion("USD");
        updatedCotizacion.setUsuario(1L);
        updatedCotizacion.setCliente(1L);

        when(cotizacionRepository.findById(1L)).thenReturn(Optional.of(cotizacion1));
        when(cotizacionRepository.save(any(Cotizacion.class))).thenReturn(cotizacion1);

        Cotizacion result = cotizacionService.updateCotizacion(1L, updatedCotizacion);

        assertNotNull(result);
        assertEquals(cotizacion1.getId(), result.getId());
    }

    @Test
    void updateCotizacionShouldThrowExceptionWhenCotizacionNotFound() {
        Cotizacion updatedCotizacion = new Cotizacion();
        updatedCotizacion.setEstado("Updated Estado");
        updatedCotizacion.setFechaCreacion(LocalDate.now());
        updatedCotizacion.setFechaValidez(LocalDate.now().plusDays(30));
        updatedCotizacion.setMargenGeneral(java.math.BigDecimal.valueOf(10.0));
        updatedCotizacion.setMonedaCotizacion("USD");
        updatedCotizacion.setUsuario(1L);
        updatedCotizacion.setCliente(1L);

        when(cotizacionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cotizacionService.updateCotizacion(1L, updatedCotizacion));
    }

    @Test
    void deleteCotizacionShouldDeleteWhenFound() {
        when(cotizacionRepository.existsById(1L)).thenReturn(true);

        cotizacionService.deleteCotizacion(1L);

        verify(cotizacionRepository).deleteById(1L);
    }

    @Test
    void deleteCotizacionShouldThrowExceptionWhenCotizacionNotFound() {
        when(cotizacionRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> cotizacionService.deleteCotizacion(1L));
    }

}