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

import com.udeateampro.controller.dto.CreateComponenteKitRequest;
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
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(componenteKit1, response.getBody());
        verify(componenteKitService).createComponenteKit(createRequest);
    }

    @Test
    void getAllComponenteKitsShouldReturnAllComponenteKits() {
        List<ComponenteKit> componenteKits = List.of(componenteKit1, componenteKit2);
        when(componenteKitService.getAllComponenteKits()).thenReturn(componenteKits);

        ResponseEntity<List<ComponenteKit>> response = componenteKitController.getAllComponenteKits();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(componenteKits, response.getBody());
        assertEquals(2, response.getBody().size());
        verify(componenteKitService).getAllComponenteKits();
    }

    @Test
    void getAllComponenteKitsShouldReturnEmptyListWhenNoComponenteKits() {
        when(componenteKitService.getAllComponenteKits()).thenReturn(List.of());

        ResponseEntity<List<ComponenteKit>> response = componenteKitController.getAllComponenteKits();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
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

        when(componenteKitService.updateComponenteKit(anyLong(), any(ComponenteKit.class))).thenReturn(updatedComponenteKit);

        ResponseEntity<ComponenteKit> response = componenteKitController.updateComponenteKit(1L, componenteKit1);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedComponenteKit, response.getBody());
        verify(componenteKitService).updateComponenteKit(1L, componenteKit1);
    }

    @Test
    void deleteComponenteKitShouldReturnNoContent() {
        doNothing().when(componenteKitService).deleteComponenteKit(anyLong());

        ResponseEntity<Void> response = componenteKitController.deleteComponenteKit(1L);

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(componenteKitService).deleteComponenteKit(1L);
    }

    @Test
    void addComponenteKitShouldCallServiceWithCorrectRequest() {
        when(componenteKitService.createComponenteKit(createRequest)).thenReturn(componenteKit1);

        componenteKitController.addComponenteKit(createRequest);

        verify(componenteKitService).createComponenteKit(createRequest);
    }

    @Test
    void updateComponenteKitShouldCallServiceWithCorrectParameters() {
        when(componenteKitService.updateComponenteKit(1L, componenteKit1)).thenReturn(componenteKit1);

        componenteKitController.updateComponenteKit(1L, componenteKit1);

        verify(componenteKitService).updateComponenteKit(1L, componenteKit1);
    }

    @Test
    void deleteComponenteKitShouldCallServiceWithCorrectId() {
        doNothing().when(componenteKitService).deleteComponenteKit(1L);

        componenteKitController.deleteComponenteKit(1L);

        verify(componenteKitService).deleteComponenteKit(1L);
    }
}