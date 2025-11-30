package com.example.clienteservice.controller;

import com.example.clienteservice.dto.ClienteDTO;
import com.example.clienteservice.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveRetornarListaDeClientes() throws Exception {
        ClienteDTO dto = new ClienteDTO(1L, "João", "joao@example.com");
        when(service.listarTodos()).thenReturn(List.of(dto));

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("João"))
                .andExpect(jsonPath("$[0].email").value("joao@example.com"));
    }

    @Test
    void deveCriarCliente() throws Exception {
        ClienteDTO entrada = new ClienteDTO(null, "Maria", "maria@example.com");
        ClienteDTO saida = new ClienteDTO(1L, "Maria", "maria@example.com");

        when(service.criar(entrada)).thenReturn(saida);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entrada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Maria"))
                .andExpect(jsonPath("$.email").value("maria@example.com"));
    }
}
