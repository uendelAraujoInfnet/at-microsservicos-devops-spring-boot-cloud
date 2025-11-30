package com.example.pedidoservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemPedidoRequest {

    @NotNull
    private Long produtoId;

    @NotNull
    @Min(1)
    private Integer quantidade;
}
