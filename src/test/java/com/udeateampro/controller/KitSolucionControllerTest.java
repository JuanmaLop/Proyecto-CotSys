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
    public void setUp() {
        kitSolucion1 = KitSolucion.builder()
                .idKit(1L)
                .nombre("Kit Básico")
                .descripcion("Kit de solución básica para proyectos pequeños")
                .estado(true)
                .build();

        kitSolucion2 = KitSolucion.builder()
                .idKit(2L)
                .nombre("Kit Avanzado")
                .descripcion("Kit de solución avanzada para proyectos complejos")
                .estado(true)
                .build();

        componenteKit1 = ComponenteKit.builder()
                .idComponenteKit(1L)
                .kitSolucion(1L)
                .producto(1L)
                .cantidad(5)
                .instrucciones("Componente principal")
                .estado(true)
                .build();

        componenteKit2 = ComponenteKit.builder()
                .idComponenteKit(2L)
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
        assertEquals(200, response.getStatusCode().value());
        KitSolucionResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(1L, body.id_kit());
        assertEquals("Kit Básico", body.nombre());
        assertEquals(2, body.componentes().size());
        verify(kitSolucionService).createKit(createRequest);
        verify(kitSolucionService).getComponentesByKit(1L);
    }

    @Test
    void getAllKitsShouldReturnAllKitResponses() {
        when(kitSolucionService.getAllKits()).thenReturn(List.of(kitSolucion1, kitSolucion2));
        when(kitSolucionService.getComponentesByKit(1L)).thenReturn(List.of(componenteKit1, componenteKit2));
        when(kitSolucionService.getComponentesByKit(2L)).thenReturn(List.of());

        ResponseEntity<List<KitSolucionResponse>> response = kitSolucionController.getAllKits();
        List<KitSolucionResponse> body = response.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(body);
        assertEquals(2, body.size());
        assertEquals("Kit Básico", body.get(0).nombre());
        assertEquals("Kit Avanzado", body.get(1).nombre());
        verify(kitSolucionService).getAllKits();
        verify(kitSolucionService).getComponentesByKit(1L);
        verify(kitSolucionService).getComponentesByKit(2L);
    }

    @Test
    void getAllKitsShouldReturnEmptyListWhenNoKits() {
        when(kitSolucionService.getAllKits()).thenReturn(List.of());

        ResponseEntity<List<KitSolucionResponse>> response = kitSolucionController.getAllKits();
        List<KitSolucionResponse> body = response.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(body);
        assertTrue(body.isEmpty());
        verify(kitSolucionService).getAllKits();
    }

    @Test
    void updateKitShouldReturnUpdatedKitResponse() {
        KitSolucion updatedKit = KitSolucion.builder()
                .idKit(1L)
                .nombre("Kit Básico Actualizado")
                .descripcion("Descripción actualizada")
                .estado(true)
                .build();

        when(kitSolucionService.updateKit(anyLong(), any(CreateKitSolucionRequest.class))).thenReturn(updatedKit);
        when(kitSolucionService.getComponentesByKit(1L)).thenReturn(List.of(componenteKit1));

        ResponseEntity<KitSolucionResponse> response = kitSolucionController.updateKit(1L, createRequest);
        KitSolucionResponse body = response.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(body);
        assertEquals("Kit Básico Actualizado", body.nombre());
        verify(kitSolucionService).updateKit(1L, createRequest);
        verify(kitSolucionService).getComponentesByKit(1L);
    }

    @Test
    void deleteKitShouldReturnNoContent() {
        doNothing().when(kitSolucionService).deleteKit(anyLong());

        ResponseEntity<Void> response = kitSolucionController.deleteKit(1L);

        assertNotNull(response);
        assertEquals(204, response.getStatusCode().value());
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