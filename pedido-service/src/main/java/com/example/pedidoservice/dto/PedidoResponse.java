package com.example.pedidoservice.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoResponse {

    private Long id;
    private Long clienteId;
    private BigDecimal valorTotal;
    private List<ItemPedidoResponse> itens;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ItemPedidoResponse {
        private Long produtoId;
        private Integer quantidade;
        private BigDecimal precoUnitario;
    }
}
