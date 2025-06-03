package com.example.leviathan.service;

import com.example.leviathan.dto.GeminiRequest;
import com.example.leviathan.dto.GeminiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GeminiService {
    private final RestTemplate restTemplate;

    private final String baseUrl;

    private final String apiKey;

    public GeminiService(RestTemplateBuilder builder, @Value("${gemini.baseurl}") String baseUrl, @Value("${gemini.api-key}") String apiKey) {
        this.restTemplate = builder.build();
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    public String getGeminiResponse(String prompt) {
        String url = baseUrl + "/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey;

        GeminiRequest.Part part = new GeminiRequest.Part(prompt);
        GeminiRequest.Content content = new GeminiRequest.Content(List.of(part));
        GeminiRequest request = new GeminiRequest(List.of(content));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GeminiRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<GeminiResponse> response = restTemplate.postForEntity(
                url,
                entity,
                GeminiResponse.class
        );


        if(response.getStatusCode().is2xxSuccessful()) {
            return response.getBody()
                    .getCandidates()
                    .get(0)
                    .getContent()
                    .getParts()
                    .get(0)
                    .getText();
        } else {
            throw new RuntimeException("Gemini API 호출 실패: " + response.getStatusCode());
        }
    }
}
