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

import com.udeateampro.controller.dto.CreateKitSolucionRequest;
import com.udeateampro.controller.dto.KitSolucionResponse;
import com.udeateampro.entity.ComponenteKit;
import com.udeateampro.entity.KitSolucion;
import com.udeateampro.service.KitSolucionService;

@ExtendWith(MockitoExtension.class)
class KitSolucionControllerTest {

    @Mock
    private KitSolucionService kitSolucionService;

    @InjectMocks
    private KitSolucionController kitSolucionController;

    private KitSolucion kitSolucion1;
    private KitSolucion kitSolucion2;
    private CreateKitSolucionRequest createRequest;
    private ComponenteKit componenteKit1;
    private ComponenteKit componenteKit2;

    @BeforeEach
    void setUp() {
        kitSolucion1 = KitSolucion.builder()
                .id_kit(1L)
                .nombre("Kit Básico")
                .descripcion("Kit de solución básica para proyectos pequeños")
                .estado(true)
                .build();

        kitSolucion2 = KitSolucion.builder()
                .id_kit(2L)
                .nombre("Kit Avanzado")
                .descripcion("Kit de solución avanzada para proyectos complejos")
                .estado(true)
                .build();

        componenteKit1 = ComponenteKit.builder()
                .id_componente_kit(1L)
                .kitSolucion(1L)
                .producto(1L)
                .cantidad(5)
                .instrucciones("Componente principal")
                .estado(true)
                .build();

        componenteKit2 = ComponenteKit.builder()
                .id_componente_kit(2L)
                .kitSolucion(1L)
                .producto(2L)
                .cantidad(10)
                .instrucciones("Componente secundario")
                .estado(true)
                .build();

        CreateKitSolucionRequest.ComponenteKitDTO componenteDTO1 = new CreateKitSolucionRequest.ComponenteKitDTO(
                1L, 5, "Componente principal", true);
        CreateKitSolucionRequest.ComponenteKitDTO componenteDTO2 = new CreateKitSolucionRequest.ComponenteKitDTO(
                2L, 10, "Componente secundario", true);

        createRequest = new CreateKitSolucionRequest(
                "Kit Nuevo",
                "Descripción del kit nuevo",
                true,
                List.of(componenteDTO1, componenteDTO2)
        );
    }

    @Test
    void addKitShouldReturnCreatedKitResponse() {
        when(kitSolucionService.createKit(any(CreateKitSolucionRequest.class))).thenReturn(kitSolucion1);
        when(kitSolucionService.getComponentesByKit(1L)).thenReturn(List.of(componenteKit1, componenteKit2));

        ResponseEntity<KitSolucionResponse> response = kitSolucionController.addKit(createRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().id_kit());
        assertEquals("Kit Básico", response.getBody().nombre());
        assertEquals(2, response.getBody().componentes().size());
        verify(kitSolucionService).createKit(createRequest);
        verify(kitSolucionService).getComponentesByKit(1L);
    }

    @Test
    void getAllKitsShouldReturnAllKitResponses() {
        when(kitSolucionService.getAllKits()).thenReturn(List.of(kitSolucion1, kitSolucion2));
        when(kitSolucionService.getComponentesByKit(1L)).thenReturn(List.of(componenteKit1, componenteKit2));
        when(kitSolucionService.getComponentesByKit(2L)).thenReturn(List.of());

        ResponseEntity<List<KitSolucionResponse>> response = kitSolucionController.getAllKits();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals("Kit Básico", response.getBody().get(0).nombre());
        assertEquals("Kit Avanzado", response.getBody().get(1).nombre());
        verify(kitSolucionService).getAllKits();
        verify(kitSolucionService).getComponentesByKit(1L);
        verify(kitSolucionService).getComponentesByKit(2L);
    }

    @Test
    void getAllKitsShouldReturnEmptyListWhenNoKits() {
        when(kitSolucionService.getAllKits()).thenReturn(List.of());

        ResponseEntity<List<KitSolucionResponse>> response = kitSolucionController.getAllKits();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(kitSolucionService).getAllKits();
    }

    @Test
    void updateKitShouldReturnUpdatedKitResponse() {
        KitSolucion updatedKit = KitSolucion.builder()
                .id_kit(1L)
                .nombre("Kit Básico Actualizado")
                .descripcion("Descripción actualizada")
                .estado(true)
                .build();

        when(kitSolucionService.updateKit(anyLong(), any(CreateKitSolucionRequest.class))).thenReturn(updatedKit);
        when(kitSolucionService.getComponentesByKit(1L)).thenReturn(List.of(componenteKit1));

        ResponseEntity<KitSolucionResponse> response = kitSolucionController.updateKit(1L, createRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Kit Básico Actualizado", response.getBody().nombre());
        verify(kitSolucionService).updateKit(1L, createRequest);
        verify(kitSolucionService).getComponentesByKit(1L);
    }

    @Test
    void deleteKitShouldReturnNoContent() {
        doNothing().when(kitSolucionService).deleteKit(anyLong());

        ResponseEntity<Void> response = kitSolucionController.deleteKit(1L);

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(kitSolucionService).deleteKit(1L);
    }

    @Test
    void addKitShouldCallServiceWithCorrectRequest() {
        when(kitSolucionService.createKit(createRequest)).thenReturn(kitSolucion1);
        when(kitSolucionService.getComponentesByKit(1L)).thenReturn(List.of());

        kitSolucionController.addKit(createRequest);

        verify(kitSolucionService).createKit(createRequest);
    }

    @Test
    void updateKitShouldCallServiceWithCorrectParameters() {
        when(kitSolucionService.updateKit(1L, createRequest)).thenReturn(kitSolucion1);
        when(kitSolucionService.getComponentesByKit(1L)).thenReturn(List.of());

        kitSolucionController.updateKit(1L, createRequest);

        verify(kitSolucionService).updateKit(1L, createRequest);
    }

    @Test
    void deleteKitShouldCallServiceWithCorrectId() {
        doNothing().when(kitSolucionService).deleteKit(1L);

        kitSolucionController.deleteKit(1L);

        verify(kitSolucionService).deleteKit(1L);
    }
}