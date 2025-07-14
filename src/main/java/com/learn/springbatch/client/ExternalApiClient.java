package com.learn.springbatch.client;

import com.learn.springbatch.dto.ApiResponseDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalApiClient {

    private final RestTemplate restTemplate;

    public ExternalApiClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public ApiResponseDTO getPage(int pageNumber, int pageSize) {
        String url = String.format("https://spring-batch-learn.free.beeceptor.com/users?page=%d&size=%d",
                pageNumber, pageSize);

        System.out.println("Chamando API: " + url);

        ResponseEntity<ApiResponseDTO> response;
        try {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    ApiResponseDTO.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }

            throw new RuntimeException("API retornou status: " + response.getStatusCode());

        } catch (Exception e) {
            throw new RuntimeException("Falha ao chamar API: " + e.getMessage(), e);
        }
    }
}