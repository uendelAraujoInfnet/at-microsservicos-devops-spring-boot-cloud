package com.example.clienteservice.service;

import com.example.clienteservice.dto.ClienteDTO;
import com.example.clienteservice.model.Cliente;
import com.example.clienteservice.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    private ClienteRepository repository;
    private ClienteService service;

    @BeforeEach
    void setUp() {
        repository = mock(ClienteRepository.class);
        service = new ClienteService(repository);
    }

    @Test
    void deveListarTodos() {
        Cliente c = new Cliente(1L, "João", "joao@example.com");
        when(repository.findAll()).thenReturn(List.of(c));

        List<ClienteDTO> resultado = service.listarTodos();

        assertEquals(1, resultado.size());
        assertEquals("João", resultado.get(0).getNome());
        verify(repository, times(1)).findAll();
    }

    @Test
    void deveBuscarPorIdQuandoExiste() {
        Cliente c = new Cliente(1L, "João", "joao@example.com");
        when(repository.findById(1L)).thenReturn(Optional.of(c));

        ClienteDTO dto = service.buscarPorId(1L);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("João", dto.getNome());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarCliente() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.buscarPorId(1L));

        assertTrue(ex.getMessage().contains("Cliente não encontrado"));
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void deveCriarCliente() {
        Cliente salvo = new Cliente(1L, "Maria", "maria@example.com");
        when(repository.save(ArgumentMatchers.any(Cliente.class))).thenReturn(salvo);

        ClienteDTO dto = new ClienteDTO(null, "Maria", "maria@example.com");

        ClienteDTO resultado = service.criar(dto);

        assertNotNull(resultado.getId());
        assertEquals("Maria", resultado.getNome());
        assertEquals("maria@example.com", resultado.getEmail());
        verify(repository, times(1)).save(any(Cliente.class));
    }
}
