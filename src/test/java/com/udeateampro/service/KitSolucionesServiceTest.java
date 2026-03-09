package com.udeateampro.service;

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

import com.udeateampro.controller.dto.CreateKitSolucionRequest;
import com.udeateampro.entity.KitSolucion;
import com.udeateampro.repository.ComponenteKitRepository;
import com.udeateampro.repository.KitSolucionRepository;

@ExtendWith(MockitoExtension.class)
class KitSolucionServiceTest {

    @Mock
    private KitSolucionRepository kitSolucionRepository;

    @Mock
    private ComponenteKitRepository componenteKitRepository;

    @InjectMocks
    private KitSolucionService kitSolucionService;

    private KitSolucion kitSolucion1;
    private KitSolucion kitSolucion2;
    private CreateKitSolucionRequest createKitRequest;

    @BeforeEach
    void setUp() {
        kitSolucion1 = KitSolucion.builder()
                .id_kit(1L)
                .nombre("Kit Solucion 1")
                .descripcion("Descripcion del Kit Solucion 1")
                .estado(true)
                .build();

        kitSolucion2 = KitSolucion.builder()
                .id_kit(2L)
                .nombre("Kit Solucion 2")
                .descripcion("Descripcion del Kit Solucion 2")
                .estado(false)
                .build();

        createKitRequest = new CreateKitSolucionRequest(
                "Nuevo Kit Solucion",   
                "Descripcion del nuevo kit solucion",
                true,
                null
        );
    }

    @Test
    void createKitShouldReturnCreatedKit() {
        when(kitSolucionRepository.save(any(KitSolucion.class))).thenReturn(kitSolucion1);

        KitSolucion createdKit = kitSolucionService.createKit(createKitRequest);

        assertNotNull(createdKit);
        assertEquals(kitSolucion1.getId_kit(), createdKit.getId_kit());    
        }

        @Test
        void getAllKitsShouldReturnAllKitsList() {
            when(kitSolucionRepository.findAll()).thenReturn(List.of(kitSolucion1, kitSolucion2));

            List<KitSolucion> kits = kitSolucionService.getAllKits();

            assertNotNull(kits);
            assertEquals(2, kits.size());
            assertEquals(kitSolucion1.getId_kit(), kits.get(0).getId_kit());
            assertEquals(kitSolucion2.getId_kit(), kits.get(1).getId_kit());
        }

        @Test
        void getAllKitsShouldReturnEmptyListWhenNoKits() {
            when(kitSolucionRepository.findAll()).thenReturn(List.of());

            List<KitSolucion> kits = kitSolucionService.getAllKits();

            assertNotNull(kits);
            assertTrue(kits.isEmpty());
        }

        @Test
        void updateKitShouldReturnUpdatedKit() {
            when(kitSolucionRepository.findById(1L)).thenReturn(java.util.Optional.of(kitSolucion1));
            when(kitSolucionRepository.save(any(KitSolucion.class))).thenReturn(kitSolucion1);

            KitSolucion updatedKit = kitSolucionService.updateKit(1L, createKitRequest);

            assertNotNull(updatedKit);
            assertEquals(kitSolucion1.getId_kit(), updatedKit.getId_kit());
            assertEquals(createKitRequest.nombre(), updatedKit.getNombre());
            assertEquals(createKitRequest.descripcion(), updatedKit.getDescripcion());
            assertEquals(createKitRequest.estado(), updatedKit.getEstado());
        }

        @Test
        void updateKitShouldThrowExceptionWhenKitNotFound() {
            when(kitSolucionRepository.findById(1L)).thenReturn(java.util.Optional.empty());

            assertThrows(RuntimeException.class, () -> kitSolucionService.updateKit(1L, createKitRequest));
        }

        @Test
        void deleteKitShouldDeleteKit() {
            kitSolucionService.deleteKit(1L);

            verify(componenteKitRepository).deleteByKitSolucion(1L);
            verify(kitSolucionRepository).deleteById(1L);
        }
}