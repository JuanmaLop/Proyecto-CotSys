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

import com.udeateampro.controller.dto.CreateClienteRequest;
import com.udeateampro.controller.dto.UpdateClienteRequest;
import com.udeateampro.entity.Cliente;
import com.udeateampro.service.ClienteService;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    private Cliente cliente1;
    private Cliente cliente2;
    private CreateClienteRequest createRequest;

    @BeforeEach
    void setUp() {
        cliente1 = Cliente.builder()
                .idCliente(1L)
                .nombre("Cliente Uno")
                .nit("12345678901")
                .direccion("Dirección 1")
                .tipoRegimen("Común")
                .municipio("Bogotá")
                .autorrentenedor(true)
                .build();

        cliente2 = Cliente.builder()
                .idCliente(2L)
                .nombre("Cliente Dos")
                .nit("09876543210")
                .direccion("Dirección 2")
                .tipoRegimen("Simplificado")
                .municipio("Medellín")
                .autorrentenedor(false)
                .build();

        createRequest = new CreateClienteRequest(
                3L,
                "Nuevo Cliente",
                "11223344556",
                "Nueva Dirección",
                "Común",
                "Cali",
                true
        );
    }

    @Test
    void addProductoShouldReturnCreatedCliente() {
        when(clienteService.createCliente(any(CreateClienteRequest.class))).thenReturn(cliente1);

        ResponseEntity<Cliente> response = clienteController.addProducto(createRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(cliente1, response.getBody());
        verify(clienteService).createCliente(createRequest);
    }

    @Test
    void getAllClientesShouldReturnAllClientes() {
        List<Cliente> clientes = List.of(cliente1, cliente2);
        when(clienteService.getAllClientes()).thenReturn(clientes);

        ResponseEntity<List<Cliente>> response = clienteController.getAllClientes();
        List<Cliente> body = response.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(body);
        assertEquals(clientes, body);
        assertEquals(2, body.size());
        verify(clienteService).getAllClientes();
    }

    @Test
    void getAllClientesShouldReturnEmptyListWhenNoClientes() {
        when(clienteService.getAllClientes()).thenReturn(List.of());

        ResponseEntity<List<Cliente>> response = clienteController.getAllClientes();
        List<Cliente> body = response.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(body);
        assertTrue(body.isEmpty());
        verify(clienteService).getAllClientes();
    }

    @Test
    void updateClienteShouldReturnUpdatedCliente() {
        Cliente updatedCliente = Cliente.builder()
                .idCliente(1L)
                .nombre("Cliente Actualizado")
                .nit("12345678901")
                .direccion("Dirección Actualizada")
                .tipoRegimen("Simplificado")
                .municipio("Bogotá")
                .autorrentenedor(false)
                .build();

        UpdateClienteRequest updateRequest = new UpdateClienteRequest(
                "Cliente Actualizado",
                "12345678901",
                "Dirección Actualizada",
                "Simplificado",
                "Bogotá"
        );

        when(clienteService.updateCliente(anyLong(), any(UpdateClienteRequest.class))).thenReturn(updatedCliente);

        ResponseEntity<Cliente> response = clienteController.updateCliente(1L, updateRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(updatedCliente, response.getBody());
        verify(clienteService).updateCliente(1L, updateRequest);
    }

    @Test
    void deleteClienteShouldReturnNoContent() {
        doNothing().when(clienteService).deleteCliente(anyLong());

        ResponseEntity<Cliente> response = clienteController.deleteCliente(1L);

        assertNotNull(response);
        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());
        verify(clienteService).deleteCliente(1L);
    }

    @Test
    void addProductoShouldCallServiceWithCorrectRequest() {
        when(clienteService.createCliente(createRequest)).thenReturn(cliente1);

        clienteController.addProducto(createRequest);

        verify(clienteService).createCliente(createRequest);
    }

    @Test
    void updateClienteShouldCallServiceWithCorrectParameters() {
        UpdateClienteRequest updateRequest = new UpdateClienteRequest(
                cliente1.getNombre(),
                cliente1.getNit(),
                cliente1.getDireccion(),
                cliente1.getTipoRegimen(),
                cliente1.getMunicipio()
        );

        when(clienteService.updateCliente(1L, updateRequest)).thenReturn(cliente1);

        clienteController.updateCliente(1L, updateRequest);

        verify(clienteService).updateCliente(1L, updateRequest);
    }

    @Test
    void deleteClienteShouldCallServiceWithCorrectId() {
        doNothing().when(clienteService).deleteCliente(1L);

        clienteController.deleteCliente(1L);

        verify(clienteService).deleteCliente(1L);
    }
}