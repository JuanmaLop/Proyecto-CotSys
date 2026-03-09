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
                .id_cliente(1L)
                .nombre("Cliente Uno")
                .nit("12345678901")
                .direccion("Dirección 1")
                .tipoRegimen("Común")
                .municipio("Bogotá")
                .autorrentenedor(true)
                .build();

        cliente2 = Cliente.builder()
                .id_cliente(2L)
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
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(cliente1, response.getBody());
        verify(clienteService).createCliente(createRequest);
    }

    @Test
    void getAllClientesShouldReturnAllClientes() {
        List<Cliente> clientes = List.of(cliente1, cliente2);
        when(clienteService.getAllClientes()).thenReturn(clientes);

        ResponseEntity<List<Cliente>> response = clienteController.getAllClientes();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(clientes, response.getBody());
        assertEquals(2, response.getBody().size());
        verify(clienteService).getAllClientes();
    }

    @Test
    void getAllClientesShouldReturnEmptyListWhenNoClientes() {
        when(clienteService.getAllClientes()).thenReturn(List.of());

        ResponseEntity<List<Cliente>> response = clienteController.getAllClientes();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(clienteService).getAllClientes();
    }

    @Test
    void updateClienteShouldReturnUpdatedCliente() {
        Cliente updatedCliente = Cliente.builder()
                .id_cliente(1L)
                .nombre("Cliente Actualizado")
                .nit("12345678901")
                .direccion("Dirección Actualizada")
                .tipoRegimen("Simplificado")
                .municipio("Bogotá")
                .autorrentenedor(false)
                .build();

        when(clienteService.updateCliente(anyLong(), any(Cliente.class))).thenReturn(updatedCliente);

        ResponseEntity<Cliente> response = clienteController.updateCliente(1L, cliente1);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedCliente, response.getBody());
        verify(clienteService).updateCliente(1L, cliente1);
    }

    @Test
    void deleteClienteShouldReturnNoContent() {
        doNothing().when(clienteService).deleteCliente(anyLong());

        ResponseEntity<Cliente> response = clienteController.deleteCliente(1L);

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
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
        when(clienteService.updateCliente(1L, cliente1)).thenReturn(cliente1);

        clienteController.updateCliente(1L, cliente1);

        verify(clienteService).updateCliente(1L, cliente1);
    }

    @Test
    void deleteClienteShouldCallServiceWithCorrectId() {
        doNothing().when(clienteService).deleteCliente(1L);

        clienteController.deleteCliente(1L);

        verify(clienteService).deleteCliente(1L);
    }
}