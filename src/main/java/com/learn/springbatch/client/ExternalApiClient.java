package com.learn.springbatch.client;

import com.learn.springbatch.dto.AlunosSistemaExternoDTO;
import com.learn.springbatch.dto.ApiResponseDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ExternalApiClient {

    private final RestTemplate restTemplate;

    public ExternalApiClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public AlunosSistemaExternoDTO getPage(int pageNumber) {
        String url = "https://spring-batch-learn.free.beeceptor.com/users?page=" + pageNumber + "&size=2";
        System.out.println("Chamando endpoint: " + url);

        ResponseEntity<ApiResponseDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                ApiResponseDTO.class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            // Pegando o primeiro resultado da página fictícia (ajuste conforme seu JSON real)
            ApiResponseDTO apiResponse = response.getBody();
            if (!apiResponse.getData().isEmpty()) {
                return apiResponse.getData().get(0);
            }
        }
        return null;
    }
}
