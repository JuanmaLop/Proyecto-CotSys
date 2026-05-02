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

import com.udeateampro.controller.dto.CreateClienteRequest;
import com.udeateampro.entity.Cliente;
import com.udeateampro.repository.ClienteRepository;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente1;

    private Cliente cliente2;

    private CreateClienteRequest createRequest;

    @BeforeEach
    public void setUp() {  
        cliente1 = Cliente.builder()
                .id_cliente(1L)
                .nombre("Test Cliente")
                .nit("123456789")
                .direccion("Test Address")
                .tipoRegimen("Test Regimen")
                .municipio("Test Municipio")
                .autorrentenedor(true)
                .build();

        cliente2 = Cliente.builder()
                .id_cliente(2L)
                .nombre("Test Cliente 2")
                .nit("987654321")
                .direccion("Test Address 2")
                .tipoRegimen("Test Regimen 2")
                .municipio("Test Municipio 2")
                .autorrentenedor(false)
                .build();

        createRequest = new CreateClienteRequest(1L, "Test Cliente", "123456789", "Test Address", "Test Regimen", "Test Municipio", true);
    }

    @Test
    void getAllClientesShouldReturnAllClientesList() {
        when(clienteRepository.findAll()).thenReturn(List.of(cliente1, cliente2));

        List<Cliente> clientes = clienteService.getAllClientes();

        assertNotNull(clientes);
        assertEquals(2, clientes.size());
        assertEquals(cliente1.getId_cliente(), clientes.get(0).getId_cliente());
        assertEquals(cliente2.getId_cliente(), clientes.get(1).getId_cliente());
    }

    @Test
    void getClienteByIdShouldReturnClienteWhenFound() {
        when(clienteRepository.findById(1L)).thenReturn(java.util.Optional.of(cliente1));

        var cliente = clienteService.getClienteById(1L);

        assertTrue(cliente.isPresent());
        assertEquals(cliente1.getId_cliente(), cliente.get().getId_cliente());
    }

    @Test
    void getClienteByIdShouldReturnEmptyWhenNotFound() {
        when(clienteRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        var cliente = clienteService.getClienteById(1L);

        assertFalse(cliente.isPresent());
    }

    @Test
    void createClienteShouldReturnCreatedCliente() {
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente1);

        Cliente createdCliente = clienteService.createCliente(createRequest);

        assertNotNull(createdCliente);
        assertEquals(cliente1.getId_cliente(), createdCliente.getId_cliente());
    }

    @Test
    void createClienteShouldThrowExceptionWhenRequestIsInvalid() {
        var request = new com.udeateampro.controller.dto.CreateClienteRequest(
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertThrows(RuntimeException.class, () -> { clienteService.createCliente(request); });
    }

    @Test
    void updateClienteShouldReturnUpdatedCliente() {
        when(clienteRepository.findById(1L)).thenReturn(java.util.Optional.of(cliente1));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente1);

        var updatedCliente = clienteService.updateCliente(1L, cliente1);

        assertNotNull(updatedCliente);
        assertEquals(cliente1.getId_cliente(), updatedCliente.getId_cliente());
    }

    @Test
    void updateClienteShouldThrowExceptionWhenClienteNotFound() {
        when(clienteRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(RuntimeException.class, () -> clienteService.updateCliente(1L, cliente1));
    }
    
    @Test
    void deleteClienteShouldDeleteClienteWhenFound() {
        when(clienteRepository.existsById(1L)).thenReturn(true);

        clienteService.deleteCliente(1L);

        verify(clienteRepository).deleteById(1L);
    }

    @Test
    void deleteClienteShouldThrowExceptionWhenClienteNotFound() {
        when(clienteRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> clienteService.deleteCliente(1L));   
    }   
}