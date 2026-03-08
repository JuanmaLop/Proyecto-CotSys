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

import com.udeateampro.controller.dto.CreateComponenteKitRequest;
import com.udeateampro.entity.ComponenteKit;
import com.udeateampro.repository.ComponenteKitRepository;

@ExtendWith(MockitoExtension.class)
class ComponenteKitServiceTest {

    @Mock
    private ComponenteKitRepository componenteKitRepository;

    @InjectMocks
    private ComponenteKitService componenteKitService;

    private ComponenteKit componenteKit1;
    private ComponenteKit componenteKit2;
    private CreateComponenteKitRequest createRequest;
    
    @BeforeEach
    void setUp() {
        componenteKit1 = ComponenteKit.builder()
                .id_componente_kit(1L)
                .kitSolucion(1L)
                .producto(1L)
                .cantidad(10)
                .instrucciones("Test Instrucciones")
                .estado(true)
                .build();

        componenteKit2 = ComponenteKit.builder()
                .id_componente_kit(2L)
                .kitSolucion(2L)
                .producto(2L)
                .cantidad(20)
                .instrucciones("Test Instrucciones 2")
                .estado(false)
                .build();

        createRequest = new CreateComponenteKitRequest(1L, 1L, 10, "Test Instrucciones");
    }

    @Test
    void getAllComponenteKitsShouldReturnListOfComponenteKits() {
        when(componenteKitRepository.findAll()).thenReturn(List.of(componenteKit1, componenteKit2));

        List<ComponenteKit> result = componenteKitService.getAllComponenteKits();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(componenteKit1.getId_componente_kit(), result.get(0).getId_componente_kit());
        assertEquals(componenteKit2.getId_componente_kit(), result.get(1).getId_componente_kit());
    }
    
    @Test
    void getComponenteKitByIdShouldReturnComponenteKitWhenFound() {
        when(componenteKitRepository.findById(1L)).thenReturn(Optional.of(componenteKit1));

        Optional<ComponenteKit> result = componenteKitService.getComponenteKitById(1L);

        assertTrue(result.isPresent());
        assertEquals(componenteKit1.getId_componente_kit(), result.get().getId_componente_kit());
    }

    @Test
    void getComponenteKitByIdShouldReturnEmptyWhenNotFound() {
        when(componenteKitRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<ComponenteKit> result = componenteKitService.getComponenteKitById(1L);

        assertFalse(result.isPresent());
    }
    
    @Test
    void createComponenteKitShouldReturnCreatedComponenteKit() {
        when(componenteKitRepository.save(any(ComponenteKit.class))).thenReturn(componenteKit1);

        ComponenteKit result = componenteKitService.createComponenteKit(createRequest);

        assertNotNull(result);
        assertEquals(componenteKit1.getId_componente_kit(), result.getId_componente_kit());
        verify(componenteKitRepository).save(any(ComponenteKit.class));
    }

    @Test
    void createComponenteKitShouldThrowExceptionWhenKitSolucionIsNull() {
        CreateComponenteKitRequest invalidRequest = new CreateComponenteKitRequest(null, 1L, 10, "Test");

        assertThrows(IllegalArgumentException.class, () -> componenteKitService.createComponenteKit(invalidRequest));
    }

    @Test
    void createComponenteKitShouldThrowExceptionWhenProductoIsNull() {
        CreateComponenteKitRequest invalidRequest = new CreateComponenteKitRequest(1L, null, 10, "Test");

        assertThrows(IllegalArgumentException.class, () -> componenteKitService.createComponenteKit(invalidRequest));
    }

    @Test
    void createComponenteKitShouldThrowExceptionWhenCantidadIsInvalid() {
        CreateComponenteKitRequest invalidRequest = new CreateComponenteKitRequest(1L, 1L, 0, "Test");

        assertThrows(IllegalArgumentException.class, () -> componenteKitService.createComponenteKit(invalidRequest));
    }

    @Test
    void createComponenteKitShouldThrowExceptionWhenInstruccionesIsNull() {
        CreateComponenteKitRequest invalidRequest = new CreateComponenteKitRequest(1L, 1L, 10, null);

        assertThrows(IllegalArgumentException.class, () -> componenteKitService.createComponenteKit(invalidRequest));
    }

    @Test
    void updateComponenteKitShouldReturnUpdatedComponenteKit() {
        ComponenteKit updated = ComponenteKit.builder()
                .cantidad(15)
                .instrucciones("Updated Instructions")
                .estado(false)
                .build();

        when(componenteKitRepository.findById(1L)).thenReturn(Optional.of(componenteKit1));
        when(componenteKitRepository.save(any(ComponenteKit.class))).thenReturn(componenteKit1);

        ComponenteKit result = componenteKitService.updateComponenteKit(1L, updated);

        assertNotNull(result);
        assertEquals(componenteKit1.getId_componente_kit(), result.getId_componente_kit());
        verify(componenteKitRepository).save(componenteKit1);
    }

    @Test
    void updateComponenteKitShouldThrowExceptionWhenNotFound() {
        ComponenteKit updated = ComponenteKit.builder()
                .cantidad(15)
                .instrucciones("Updated Instructions")
                .estado(false)
                .build();

        when(componenteKitRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> componenteKitService.updateComponenteKit(1L, updated));
    }

    @Test
    void deleteComponenteKitShouldDeleteWhenFound() {
        when(componenteKitRepository.existsById(1L)).thenReturn(true);

        componenteKitService.deleteComponenteKit(1L);

        verify(componenteKitRepository).deleteById(1L);
    }

    @Test
    void deleteComponenteKitShouldThrowExceptionWhenNotFound() {
        when(componenteKitRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> componenteKitService.deleteComponenteKit(1L));
    }
}
