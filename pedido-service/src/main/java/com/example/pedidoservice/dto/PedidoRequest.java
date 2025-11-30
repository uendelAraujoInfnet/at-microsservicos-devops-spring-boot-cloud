package com.example.pedidoservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoRequest {

    @NotNull
    private Long clienteId;

    @NotEmpty
    private List<ItemPedidoRequest> itens;
}
