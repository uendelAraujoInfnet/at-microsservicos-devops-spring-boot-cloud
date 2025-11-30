package com.example.pedidoservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteResumoDTO {
    private Long id;
    private String nome;
    private String email;
}
