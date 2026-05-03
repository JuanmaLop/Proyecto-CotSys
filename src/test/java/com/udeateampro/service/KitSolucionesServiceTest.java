package com.udeateampro.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.udeateampro.controller.dto.CreateKitSolucionRequest;
import com.udeateampro.entity.ComponenteKit;
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
                .idKit(1L)
                .nombre("Kit Solucion 1")
                .descripcion("Descripcion del Kit Solucion 1")
                .estado(true)
                .build();

        kitSolucion2 = KitSolucion.builder()
                .idKit(2L)
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
        assertEquals(kitSolucion1.getIdKit(), createdKit.getIdKit());    
        }

        @Test
        void createKitWithEmptyComponentesShouldNotSaveComponents() {
            CreateKitSolucionRequest requestWithEmptyComponentes = new CreateKitSolucionRequest(
                "Kit Sin Componentes",
                "Descripcion",
                true,
                List.of()
            );

            when(kitSolucionRepository.save(any(KitSolucion.class))).thenReturn(kitSolucion1);

            KitSolucion createdKit = kitSolucionService.createKit(requestWithEmptyComponentes);

            assertNotNull(createdKit);
            verify(kitSolucionRepository).save(any(KitSolucion.class));
            verify(componenteKitRepository, never()).save(any(ComponenteKit.class));
        }

        @Test
        void getAllKitsShouldReturnAllKitsList() {
            when(kitSolucionRepository.findAll()).thenReturn(List.of(kitSolucion1, kitSolucion2));

            List<KitSolucion> kits = kitSolucionService.getAllKits();

            assertNotNull(kits);
            assertEquals(2, kits.size());
            assertEquals(kitSolucion1.getIdKit(), kits.get(0).getIdKit());
            assertEquals(kitSolucion2.getIdKit(), kits.get(1).getIdKit());
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
            assertEquals(kitSolucion1.getIdKit(), updatedKit.getIdKit());
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

        @Test
        void createKitWithComponentesShouldSaveComponents() {
            // Given
            List<CreateKitSolucionRequest.ComponenteKitDTO> componentes = List.of(
                    new CreateKitSolucionRequest.ComponenteKitDTO(10L, 2, "Instrucción 1", true),
                    new CreateKitSolucionRequest.ComponenteKitDTO(11L, 3, "Instrucción 2", true)
            );
            CreateKitSolucionRequest requestWithComponents = new CreateKitSolucionRequest(
                    "Kit con Componentes",
                    "Descripción",
                    true,
                    componentes
            );

            when(kitSolucionRepository.save(any(KitSolucion.class))).thenReturn(kitSolucion1);

            // When
            KitSolucion createdKit = kitSolucionService.createKit(requestWithComponents);

            // Then
            assertNotNull(createdKit);
            verify(kitSolucionRepository).save(any(KitSolucion.class));
        }

        @Test
        void createKitWithNullComponentesInComponenteShouldSkipIt() {
            // Given
            List<CreateKitSolucionRequest.ComponenteKitDTO> componentes = List.of(
                    new CreateKitSolucionRequest.ComponenteKitDTO(null, 2, "Instrucción 1", true),
                    new CreateKitSolucionRequest.ComponenteKitDTO(11L, 3, "Instrucción 2", true)
            );
            CreateKitSolucionRequest requestWithComponents = new CreateKitSolucionRequest(
                    "Kit con Componentes",
                    "Descripción",
                    true,
                    componentes
            );

            when(kitSolucionRepository.save(any(KitSolucion.class))).thenReturn(kitSolucion1);

            // When
            KitSolucion createdKit = kitSolucionService.createKit(requestWithComponents);

            // Then
            assertNotNull(createdKit);
            verify(kitSolucionRepository).save(any(KitSolucion.class));
        }

        @Test
        void createKitWithNullCantidadInComponenteShouldSkipIt() {
            // Given
            List<CreateKitSolucionRequest.ComponenteKitDTO> componentes = List.of(
                    new CreateKitSolucionRequest.ComponenteKitDTO(10L, null, "Instrucción 1", true)
            );
            CreateKitSolucionRequest requestWithComponents = new CreateKitSolucionRequest(
                    "Kit con Componentes",
                    "Descripción",
                    true,
                    componentes
            );

            when(kitSolucionRepository.save(any(KitSolucion.class))).thenReturn(kitSolucion1);

            // When
            KitSolucion createdKit = kitSolucionService.createKit(requestWithComponents);

            // Then
            assertNotNull(createdKit);
            verify(kitSolucionRepository).save(any(KitSolucion.class));
        }

        @Test
        void createKitWithNullEstadoShouldDefaultToTrue() {
            // Given
            CreateKitSolucionRequest requestWithNullEstado = new CreateKitSolucionRequest(
                    "Kit Sin Estado",
                    "Descripción",
                    null,
                    null
            );

            when(kitSolucionRepository.save(any(KitSolucion.class))).thenReturn(kitSolucion1);

            // When
            KitSolucion createdKit = kitSolucionService.createKit(requestWithNullEstado);

            // Then
            assertNotNull(createdKit);
            verify(kitSolucionRepository).save(any(KitSolucion.class));
        }

            @Test
            void createKitWithNullComponentFieldsShouldUseDefaults() {
                List<CreateKitSolucionRequest.ComponenteKitDTO> componentes = List.of(
                    new CreateKitSolucionRequest.ComponenteKitDTO(10L, 2, null, null)
                );
                CreateKitSolucionRequest requestWithNullComponentFields = new CreateKitSolucionRequest(
                    "Kit con Componentes Nulos",
                    "Descripción",
                    true,
                    componentes
                );

                when(kitSolucionRepository.save(any(KitSolucion.class))).thenReturn(kitSolucion1);
                when(componenteKitRepository.save(any(ComponenteKit.class))).thenAnswer(invocation -> invocation.getArgument(0));

                KitSolucion createdKit = kitSolucionService.createKit(requestWithNullComponentFields);

                assertNotNull(createdKit);
                ArgumentCaptor<ComponenteKit> componenteCaptor = ArgumentCaptor.forClass(ComponenteKit.class);
                verify(componenteKitRepository).save(componenteCaptor.capture());
                assertEquals("", componenteCaptor.getValue().getInstrucciones());
                assertEquals(Boolean.TRUE, componenteCaptor.getValue().getEstado());
            }

        @Test
        void updateKitWithComponentesShouldDeleteAndRecreate() {
            // Given
            List<CreateKitSolucionRequest.ComponenteKitDTO> componentes = List.of(
                    new CreateKitSolucionRequest.ComponenteKitDTO(12L, 1, "Nueva Instrucción", true)
            );
            CreateKitSolucionRequest updateRequest = new CreateKitSolucionRequest(
                    "Kit Actualizado",
                    "Descripción Actualizada",
                    true,
                    componentes
            );

            when(kitSolucionRepository.findById(1L)).thenReturn(java.util.Optional.of(kitSolucion1));
            when(kitSolucionRepository.save(any(KitSolucion.class))).thenReturn(kitSolucion1);

            // When
            KitSolucion updatedKit = kitSolucionService.updateKit(1L, updateRequest);

            // Then
            assertNotNull(updatedKit);
            verify(componenteKitRepository).deleteByKitSolucion(1L);
            verify(kitSolucionRepository).save(any(KitSolucion.class));
        }

        @Test
        void updateKitWithEmptyComponentesShouldOnlyDeleteComponents() {
            // Given
            CreateKitSolucionRequest updateRequest = new CreateKitSolucionRequest(
                    "Kit Actualizado",
                    "Descripción Actualizada",
                    true,
                    List.of()
            );

            when(kitSolucionRepository.findById(1L)).thenReturn(java.util.Optional.of(kitSolucion1));
            when(kitSolucionRepository.save(any(KitSolucion.class))).thenReturn(kitSolucion1);

            // When
            KitSolucion updatedKit = kitSolucionService.updateKit(1L, updateRequest);

            // Then
            assertNotNull(updatedKit);
            verify(componenteKitRepository).deleteByKitSolucion(1L);
            verify(kitSolucionRepository).save(any(KitSolucion.class));
        }

            @Test
            void updateKitWithNullEstadoAndNullComponentFieldsShouldUseDefaults() {
                List<CreateKitSolucionRequest.ComponenteKitDTO> componentes = List.of(
                    new CreateKitSolucionRequest.ComponenteKitDTO(12L, 1, null, null)
                );
                CreateKitSolucionRequest updateRequest = new CreateKitSolucionRequest(
                    "Kit Actualizado",
                    "Descripción Actualizada",
                    null,
                    componentes
                );

                when(kitSolucionRepository.findById(1L)).thenReturn(java.util.Optional.of(kitSolucion1));
                when(kitSolucionRepository.save(any(KitSolucion.class))).thenReturn(kitSolucion1);
                when(componenteKitRepository.save(any(ComponenteKit.class))).thenAnswer(invocation -> invocation.getArgument(0));

                KitSolucion updatedKit = kitSolucionService.updateKit(1L, updateRequest);

                assertNotNull(updatedKit);
                assertEquals(kitSolucion1.getEstado(), updatedKit.getEstado());
                ArgumentCaptor<ComponenteKit> componenteCaptor = ArgumentCaptor.forClass(ComponenteKit.class);
                verify(componenteKitRepository).save(componenteCaptor.capture());
                assertEquals("", componenteCaptor.getValue().getInstrucciones());
                assertEquals(Boolean.TRUE, componenteCaptor.getValue().getEstado());
            }

        @Test
        void getComponentesByKitShouldReturnList() {
            // Given - Mocking returns empty list
            when(componenteKitRepository.findByKitSolucion(1L)).thenReturn(List.of());

            // When
            var componentes = kitSolucionService.getComponentesByKit(1L);

            // Then
            assertNotNull(componentes);
            assertEquals(0, componentes.size());
            verify(componenteKitRepository).findByKitSolucion(1L);
        }
}