package com.example.pedidoservice.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoResumoDTO {
    private Long id;
    private String nome;
    private BigDecimal preco;
}
