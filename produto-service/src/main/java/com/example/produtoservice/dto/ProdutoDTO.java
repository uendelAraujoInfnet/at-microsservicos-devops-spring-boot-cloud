package com.example.produtoservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoDTO {
    private Long id;

    @NotBlank
    private String nome;

    @NotNull
    private BigDecimal preco;
}
