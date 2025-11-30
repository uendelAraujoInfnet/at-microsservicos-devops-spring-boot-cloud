package com.example.pedidoservice.service;

import com.example.pedidoservice.dto.*;
import com.example.pedidoservice.model.ItemPedido;
import com.example.pedidoservice.model.Pedido;
import com.example.pedidoservice.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository repository;
    private final ClienteApiClient clienteApiClient;
    private final ProdutoApiClient produtoApiClient;

    public PedidoService(PedidoRepository repository,
                         ClienteApiClient clienteApiClient,
                         ProdutoApiClient produtoApiClient) {
        this.repository = repository;
        this.clienteApiClient = clienteApiClient;
        this.produtoApiClient = produtoApiClient;
    }

    @Transactional
    public PedidoResponse criarPedido(PedidoRequest request) {
        // 1. Valida e busca o cliente
        ClienteResumoDTO cliente = clienteApiClient.buscarClientePorId(request.getClienteId());
        if (cliente == null) {
            throw new RuntimeException("Cliente não encontrado: " + request.getClienteId());
        }

        // 2. Cria o pedido base
        Pedido pedido = new Pedido();
        pedido.setClienteId(cliente.getId());

        // 3. Calcula total e monta a lista de itens
        BigDecimal total = BigDecimal.ZERO;

        List<ItemPedido> itens = new java.util.ArrayList<>();

        for (ItemPedidoRequest itemReq : request.getItens()) {
            ProdutoResumoDTO produto = produtoApiClient.buscarProdutoPorId(itemReq.getProdutoId());
            if (produto == null) {
                throw new RuntimeException("Produto não encontrado: " + itemReq.getProdutoId());
            }

            BigDecimal subtotal = produto.getPreco()
                    .multiply(BigDecimal.valueOf(itemReq.getQuantidade()));
            total = total.add(subtotal);

            ItemPedido item = new ItemPedido();
            item.setProdutoId(produto.getId());
            item.setQuantidade(itemReq.getQuantidade());
            item.setPrecoUnitario(produto.getPreco());
            item.setPedido(pedido); // relação bidirecional

            itens.add(item);
        }

        // 4. Seta total e itens no pedido
        pedido.setValorTotal(total);
        pedido.setItens(itens);

        // 5. Persiste e devolve DTO de resposta
        Pedido salvo = repository.save(pedido);

        return toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public PedidoResponse buscarPorId(Long id) {
        Pedido pedido = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + id));
        return toResponse(pedido);
    }

    private PedidoResponse toResponse(Pedido pedido) {
        List<PedidoResponse.ItemPedidoResponse> itens = pedido.getItens().stream()
                .map(i -> PedidoResponse.ItemPedidoResponse.builder()
                        .produtoId(i.getProdutoId())
                        .quantidade(i.getQuantidade())
                        .precoUnitario(i.getPrecoUnitario())
                        .build())
                .toList();

        return PedidoResponse.builder()
                .id(pedido.getId())
                .clienteId(pedido.getClienteId())
                .valorTotal(pedido.getValorTotal())
                .itens(itens)
                .build();
    }
}
