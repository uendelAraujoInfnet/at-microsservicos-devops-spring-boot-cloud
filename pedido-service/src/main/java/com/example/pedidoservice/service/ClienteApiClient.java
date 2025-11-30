package com.example.pedidoservice.service;

import com.example.pedidoservice.dto.ClienteResumoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ClienteApiClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public ClienteApiClient(RestTemplate restTemplate,
                            @Value("${clientes.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public ClienteResumoDTO buscarClientePorId(Long id) {
        String url = baseUrl + "/clientes/" + id;
        return restTemplate.getForObject(url, ClienteResumoDTO.class);
    }
}
