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

import com.udeateampro.controller.dto.CreateComponenteKitRequest;
import com.udeateampro.controller.dto.UpdateComponenteKitRequest;
import com.udeateampro.entity.ComponenteKit;
import com.udeateampro.service.ComponenteKitService;

@ExtendWith(MockitoExtension.class)
class ComponenteKitControllerTest {

    @Mock
    private ComponenteKitService componenteKitService;

    @InjectMocks
    private ComponenteKitController componenteKitController;

    private ComponenteKit componenteKit1;
    private ComponenteKit componenteKit2;
    private CreateComponenteKitRequest createRequest;

    @BeforeEach
    void setUp() {
        componenteKit1 = ComponenteKit.builder()
                .id_componente_kit(1L)
                .kitSolucion(1L)
                .producto(1L)
                .cantidad(5)
                .instrucciones("Instrucciones para componente 1")
                .estado(true)
                .build();

        componenteKit2 = ComponenteKit.builder()
                .id_componente_kit(2L)
                .kitSolucion(1L)
                .producto(2L)
                .cantidad(10)
                .instrucciones("Instrucciones para componente 2")
                .estado(true)
                .build();

        createRequest = new CreateComponenteKitRequest(
                1L,
                3L,
                15,
                "Nuevas instrucciones"
        );
    }

    @Test
    void addComponenteKitShouldReturnCreatedComponenteKit() {
        when(componenteKitService.createComponenteKit(any(CreateComponenteKitRequest.class))).thenReturn(componenteKit1);

        ResponseEntity<ComponenteKit> response = componenteKitController.addComponenteKit(createRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(componenteKit1, response.getBody());
        verify(componenteKitService).createComponenteKit(createRequest);
    }

    @Test
    void getAllComponenteKitsShouldReturnAllComponenteKits() {
        List<ComponenteKit> componenteKits = List.of(componenteKit1, componenteKit2);
        when(componenteKitService.getAllComponenteKits()).thenReturn(componenteKits);

        ResponseEntity<List<ComponenteKit>> response = componenteKitController.getAllComponenteKits();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        List<ComponenteKit> body = response.getBody();
        assertNotNull(body);
        assertEquals(componenteKits, body);
        assertEquals(2, body.size());
        verify(componenteKitService).getAllComponenteKits();
    }

    @Test
    void getAllComponenteKitsShouldReturnEmptyListWhenNoComponenteKits() {
        when(componenteKitService.getAllComponenteKits()).thenReturn(List.of());

        ResponseEntity<List<ComponenteKit>> response = componenteKitController.getAllComponenteKits();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        List<ComponenteKit> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.isEmpty());
        verify(componenteKitService).getAllComponenteKits();
    }

    @Test
    void updateComponenteKitShouldReturnUpdatedComponenteKit() {
        ComponenteKit updatedComponenteKit = ComponenteKit.builder()
                .id_componente_kit(1L)
                .kitSolucion(1L)
                .producto(1L)
                .cantidad(20)
                .instrucciones("Instrucciones actualizadas")
                .estado(true)
                .build();

        UpdateComponenteKitRequest updateRequest = new UpdateComponenteKitRequest(
                20,
                "Instrucciones actualizadas",
                true
        );

        when(componenteKitService.updateComponenteKit(anyLong(), any(UpdateComponenteKitRequest.class))).thenReturn(updatedComponenteKit);

        ResponseEntity<ComponenteKit> response = componenteKitController.updateComponenteKit(1L, updateRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(updatedComponenteKit, response.getBody());
        verify(componenteKitService).updateComponenteKit(1L, updateRequest);
    }

    @Test
    void deleteComponenteKitShouldReturnNoContent() {
        doNothing().when(componenteKitService).deleteComponenteKit(anyLong());

        ResponseEntity<Void> response = componenteKitController.deleteComponenteKit(1L);

        assertNotNull(response);
        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());
        verify(componenteKitService).deleteComponenteKit(1L);
    }

    @Test
    void addComponenteKitShouldCallServiceWithCorrectRequest() {
        when(componenteKitService.createComponenteKit(createRequest)).thenReturn(componenteKit1);

        componenteKitController.addComponenteKit(createRequest);
UpdateComponenteKitRequest updateRequest = new UpdateComponenteKitRequest(
                componenteKit1.getCantidad(),
                componenteKit1.getInstrucciones(),
                componenteKit1.getEstado()
        );

        when(componenteKitService.updateComponenteKit(1L, updateRequest)).thenReturn(componenteKit1);

        componenteKitController.updateComponenteKit(1L, updateRequest);

        verify(componenteKitService).updateComponenteKit(1L, updateRequest);
    }

    @Test
    void updateComponenteKitShouldCallServiceWithCorrectParameters() {
        UpdateComponenteKitRequest updateRequest = new UpdateComponenteKitRequest(
                componenteKit1.getCantidad(),
                componenteKit1.getInstrucciones(),
                componenteKit1.getEstado()
        );

        when(componenteKitService.updateComponenteKit(1L, updateRequest)).thenReturn(componenteKit1);

        componenteKitController.updateComponenteKit(1L, updateRequest);

        verify(componenteKitService).updateComponenteKit(1L, updateRequest);
    }

    @Test
    void deleteComponenteKitShouldCallServiceWithCorrectId() {
        doNothing().when(componenteKitService).deleteComponenteKit(1L);

        componenteKitController.deleteComponenteKit(1L);

        verify(componenteKitService).deleteComponenteKit(1L);
    }
}