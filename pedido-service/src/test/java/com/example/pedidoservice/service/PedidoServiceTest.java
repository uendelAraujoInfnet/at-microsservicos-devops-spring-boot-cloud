package com.example.pedidoservice.service;

import com.example.pedidoservice.dto.*;
import com.example.pedidoservice.model.ItemPedido;
import com.example.pedidoservice.model.Pedido;
import com.example.pedidoservice.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoServiceTest {

    private PedidoRepository repository;
    private ClienteApiClient clienteApiClient;
    private ProdutoApiClient produtoApiClient;
    private PedidoService service;

    @BeforeEach
    void setUp() {
        repository = mock(PedidoRepository.class);
        clienteApiClient = mock(ClienteApiClient.class);
        produtoApiClient = mock(ProdutoApiClient.class);
        service = new PedidoService(repository, clienteApiClient, produtoApiClient);
    }

    @Test
    void deveCriarPedidoComSucesso() {
        // dado
        PedidoRequest request = PedidoRequest.builder()
                .clienteId(1L)
                .itens(List.of(
                        ItemPedidoRequest.builder().produtoId(10L).quantidade(2).build(),
                        ItemPedidoRequest.builder().produtoId(20L).quantidade(1).build()
                ))
                .build();

        ClienteResumoDTO cliente = new ClienteResumoDTO(1L, "João", "joao@example.com");
        when(clienteApiClient.buscarClientePorId(1L)).thenReturn(cliente);

        ProdutoResumoDTO produto1 = new ProdutoResumoDTO(10L, "Ração", new BigDecimal("50.00"));
        ProdutoResumoDTO produto2 = new ProdutoResumoDTO(20L, "Brinquedo", new BigDecimal("30.00"));

        when(produtoApiClient.buscarProdutoPorId(10L)).thenReturn(produto1);
        when(produtoApiClient.buscarProdutoPorId(20L)).thenReturn(produto2);

        when(repository.save(any(Pedido.class))).thenAnswer(invocation -> {
            Pedido p = invocation.getArgument(0);
            p.setId(100L);
            return p;
        });

        // quando
        PedidoResponse response = service.criarPedido(request);

        // então
        assertNotNull(response.getId());
        assertEquals(1L, response.getClienteId());
        // total: 2 * 50 + 1 * 30 = 130
        assertEquals(new BigDecimal("130.00"), response.getValorTotal());
        assertEquals(2, response.getItens().size());

        verify(clienteApiClient, times(1)).buscarClientePorId(1L);
        verify(produtoApiClient, times(1)).buscarProdutoPorId(10L);
        verify(produtoApiClient, times(1)).buscarProdutoPorId(20L);
        verify(repository, times(1)).save(any(Pedido.class));

        // opcional: capturar o Pedido salvo
        ArgumentCaptor<Pedido> captor = ArgumentCaptor.forClass(Pedido.class);
        verify(repository).save(captor.capture());
        Pedido salvo = captor.getValue();
        assertEquals(new BigDecimal("130.00"), salvo.getValorTotal());
        assertEquals(2, salvo.getItens().size());
        assertEquals(1L, salvo.getClienteId());
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoExistir() {
        PedidoRequest request = PedidoRequest.builder()
                .clienteId(1L)
                .itens(List.of(
                        ItemPedidoRequest.builder().produtoId(10L).quantidade(2).build()
                ))
                .build();

        when(clienteApiClient.buscarClientePorId(1L)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.criarPedido(request));

        assertTrue(ex.getMessage().contains("Cliente não encontrado"));
        verify(produtoApiClient, never()).buscarProdutoPorId(any());
        verify(repository, never()).save(any(Pedido.class));
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExistir() {
        PedidoRequest request = PedidoRequest.builder()
                .clienteId(1L)
                .itens(List.of(
                        ItemPedidoRequest.builder().produtoId(10L).quantidade(2).build()
                ))
                .build();

        ClienteResumoDTO cliente = new ClienteResumoDTO(1L, "João", "joao@example.com");
        when(clienteApiClient.buscarClientePorId(1L)).thenReturn(cliente);

        when(produtoApiClient.buscarProdutoPorId(10L)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.criarPedido(request));

        assertTrue(ex.getMessage().contains("Produto não encontrado"));
        verify(repository, never()).save(any(Pedido.class));
    }

    @Test
    void deveBuscarPedidoPorId() {
        Pedido pedido = new Pedido();
        pedido.setId(123L);
        pedido.setClienteId(1L);
        pedido.setValorTotal(new BigDecimal("99.90"));

        ItemPedido item = new ItemPedido();
        item.setId(1L);
        item.setProdutoId(10L);
        item.setQuantidade(1);
        item.setPrecoUnitario(new BigDecimal("99.90"));
        item.setPedido(pedido);

        pedido.setItens(List.of(item));

        when(repository.findById(123L)).thenReturn(Optional.of(pedido));

        PedidoResponse resp = service.buscarPorId(123L);

        assertEquals(123L, resp.getId());
        assertEquals(1L, resp.getClienteId());
        assertEquals(new BigDecimal("99.90"), resp.getValorTotal());
        assertEquals(1, resp.getItens().size());
        assertEquals(10L, resp.getItens().get(0).getProdutoId());
        verify(repository, times(1)).findById(123L);
    }

    @Test
    void deveLancarExcecaoAoBuscarPedidoInexistente() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.buscarPorId(999L));

        assertTrue(ex.getMessage().contains("Pedido não encontrado"));
        verify(repository, times(1)).findById(999L);
    }
}
