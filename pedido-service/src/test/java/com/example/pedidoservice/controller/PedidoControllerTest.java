package com.example.pedidoservice.controller;

import com.example.pedidoservice.dto.ItemPedidoRequest;
import com.example.pedidoservice.dto.PedidoRequest;
import com.example.pedidoservice.dto.PedidoResponse;
import com.example.pedidoservice.service.PedidoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoController.class)
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarPedido() throws Exception {
        PedidoRequest request = PedidoRequest.builder()
                .clienteId(1L)
                .itens(List.of(
                        ItemPedidoRequest.builder().produtoId(10L).quantidade(2).build()
                ))
                .build();

        PedidoResponse.ItemPedidoResponse itemResp =
                PedidoResponse.ItemPedidoResponse.builder()
                        .produtoId(10L)
                        .quantidade(2)
                        .precoUnitario(new BigDecimal("50.00"))
                        .build();

        PedidoResponse response = PedidoResponse.builder()
                .id(100L)
                .clienteId(1L)
                .valorTotal(new BigDecimal("100.00"))
                .itens(List.of(itemResp))
                .build();

        when(service.criarPedido(request)).thenReturn(response);

        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.clienteId").value(1L))
                .andExpect(jsonPath("$.valorTotal").value(100.00))
                .andExpect(jsonPath("$.itens[0].produtoId").value(10L))
                .andExpect(jsonPath("$.itens[0].quantidade").value(2));
    }

    @Test
    void deveBuscarPedidoPorId() throws Exception {
        PedidoResponse.ItemPedidoResponse itemResp =
                PedidoResponse.ItemPedidoResponse.builder()
                        .produtoId(10L)
                        .quantidade(1)
                        .precoUnitario(new BigDecimal("99.90"))
                        .build();

        PedidoResponse response = PedidoResponse.builder()
                .id(123L)
                .clienteId(1L)
                .valorTotal(new BigDecimal("99.90"))
                .itens(List.of(itemResp))
                .build();

        when(service.buscarPorId(123L)).thenReturn(response);

        mockMvc.perform(get("/pedidos/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(123L))
                .andExpect(jsonPath("$.clienteId").value(1L))
                .andExpect(jsonPath("$.valorTotal").value(99.90))
                .andExpect(jsonPath("$.itens[0].produtoId").value(10L));
    }
}
