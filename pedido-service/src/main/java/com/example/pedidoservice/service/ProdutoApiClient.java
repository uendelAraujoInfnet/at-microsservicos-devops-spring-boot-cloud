package com.example.pedidoservice.service;

import com.example.pedidoservice.dto.ProdutoResumoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProdutoApiClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public ProdutoApiClient(RestTemplate restTemplate,
                            @Value("${produtos.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public ProdutoResumoDTO buscarProdutoPorId(Long id) {
        String url = baseUrl + "/produtos/" + id;
        return restTemplate.getForObject(url, ProdutoResumoDTO.class);
    }
}
