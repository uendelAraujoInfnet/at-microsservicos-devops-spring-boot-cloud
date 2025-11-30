package com.example.produtoservice.controller;

import com.example.produtoservice.dto.ProdutoDTO;
import com.example.produtoservice.service.ProdutoService;
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

@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveRetornarListaDeProdutos() throws Exception {
        ProdutoDTO dto = new ProdutoDTO(1L, "Ração", new BigDecimal("50.00"));
        when(service.listarTodos()).thenReturn(List.of(dto));

        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Ração"))
                .andExpect(jsonPath("$[0].preco").value(50.00));
    }

    @Test
    void deveCriarProduto() throws Exception {
        ProdutoDTO entrada = new ProdutoDTO(null, "Brinquedo", new BigDecimal("20.00"));
        ProdutoDTO saida = new ProdutoDTO(1L, "Brinquedo", new BigDecimal("20.00"));

        when(service.criar(entrada)).thenReturn(saida);

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entrada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Brinquedo"))
                .andExpect(jsonPath("$.preco").value(20.00));
    }
}
